package br.edu.ifpb.pdm.ouvidoriacliente.services.pubnub;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.gson.JsonObject;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;

import java.io.Serializable;
import java.util.Arrays;

import br.edu.ifpb.pdm.ouvidoriacliente.R;
import br.edu.ifpb.pdm.ouvidoriacliente.services.rest.clients.MensagemService;

//import retrofit2.Response;

/**
 * Created by natarajan on 27/06/17.
 */

/*
 * IntentService que realiza comunicação com o servidor de mensagens
 */
public class PubNubClientService extends IntentService {


    private static final String PUBNUB_PUBTOKEN = "pub-c-a6e1e709-c24f-4608-ab08-0cacf634bcfd";
    private static final String PUBNUB_SUBTOKEN = "sub-c-bd52fa58-a96e-11e7-a097-061ce3592077";
    private static PubNub pubnub = null;



    public PubNubClientService() {
        super("PubnubService");
        PNConfiguration pnConfiguration = new PNConfiguration();
        pnConfiguration.setSubscribeKey(PUBNUB_SUBTOKEN);
        pnConfiguration.setPublishKey(PUBNUB_PUBTOKEN);
        pnConfiguration.setSecure(false);
        pubnub = new PubNub(pnConfiguration);
        Log.d("natarajan-debug", "Iniciando serviço do Pubnub");

    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public PubNubClientService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        SharedPreferences sharedPreferences = getSharedPreferences("OUVIDORIACLIENTE", MODE_PRIVATE);
        String emailUsuario = sharedPreferences.getString("email", "erro");

        Log.d("natarajan-debug", "Subscrevendo ao channel: " + emailUsuario);

        pubnub.subscribe()
                .channels(Arrays.asList(emailUsuario)) // subscribe to channels
                .execute();

        pubnub.addListener(new SubscribeCallback() {

            @Override
            public void status(PubNub pubnub, PNStatus status) {
                Log.d("natarajan-debug", "mostrou o status");
            }

            @Override
            public void message(PubNub pubnub, PNMessageResult message) {
                Log.d("natarajan-debug", "mostrou o mensagem: " + message.getMessage());

                criarNotifiacao(message);
            }

            @Override
            public void presence(PubNub pubnub, PNPresenceEventResult presence) {
                Log.d("natarajan-debug", "mostrou o mensagem");

            }
        });

    }

    private void criarNotifiacao(PNMessageResult message) {

        JsonObject mensagemRecebida = message.getMessage().getAsJsonObject();
        String tipoMensagem = mensagemRecebida.get("type").getAsString();


        String idTicket = mensagemRecebida.get("idticket").getAsString();

        NotificationManager mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(this, MensagemService.class);
        intent.putExtra("command", "GETLAST");
        intent.putExtra("id_ticket", (Serializable) idTicket);

        PendingIntent contentIntent = PendingIntent.getService(this, 0,
                intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher_round)
                        .setContentTitle("Ouvidoria (Cliente)")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText("O auditor respondeu o ticket de id " + idTicket))
                        .setContentText("O auditor respondeu o ticket de id " + idTicket)
                        .setAutoCancel(true);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(1, mBuilder.build());


    }
}
