package br.edu.ifpb.pdm.ouvidoriaaudit.services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.enums.PNStatusCategory;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import br.edu.ifpb.pdm.ouvidoriaaudit.R;
import br.edu.ifpb.pdm.ouvidoriaaudit.entities.Ticket;
import br.edu.ifpb.pdm.ouvidoriaaudit.presentation.ResponseActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//import retrofit2.Response;

/**
 * Created by natarajan on 27/06/17.
 */

/*
 * IntentService que realiza comunicação com o servidor de mensagens
 */
public class PubNubService extends IntentService {


    private static final String PUBNUB_PUBTOKEN = "pub-c-a6e1e709-c24f-4608-ab08-0cacf634bcfd";
    private static final String PUBNUB_SUBTOKEN = "sub-c-bd52fa58-a96e-11e7-a097-061ce3592077";
    private static PubNub pubnub = null;



    public PubNubService() {
        super("PubnubService");
        PNConfiguration pnConfiguration = new PNConfiguration();
        pnConfiguration.setSubscribeKey(PUBNUB_SUBTOKEN);
        pnConfiguration.setPublishKey(PUBNUB_PUBTOKEN);
        pnConfiguration.setSecure(false);
        pubnub = new PubNub(pnConfiguration);

    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public PubNubService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        pubnub.subscribe()
                .channels(Arrays.asList("auditor")) // subscribe to channels
                .execute();

        pubnub.addListener(new SubscribeCallback() {

            @Override
            public void status(PubNub pubnub, PNStatus status) {
//                if (status.getCategory() == PNStatusCategory.PNConnectedCategory){
//                    complexData data = new complexData();
//                    data.fieldA = "Awesome";
//                    data.fieldB = 10;
//                    pubnub.publish().channel("awesomeChannel").message(data).async(new PNCallback<PNPublishResult>() {
//                        @Override
//                        public void onResponse(PNPublishResult result, PNStatus status) {
//                            // handle publish response
//                        }
//                    });
//                }

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

        Log.d("natarajan-debug", "PubNubService: Notificou mensagem do ticket com id: " + idTicket);

        NotificationManager mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(this, MensagemService.class);

        intent.putExtra("id_ticket", Long.valueOf(idTicket));
        intent.putExtra("outro", idTicket);
        intent.putExtra("command", "GETLAST");

        PendingIntent contentIntent = PendingIntent.getService(this, 0,
                    intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder mBuilder = null;

        if (tipoMensagem.equals("ticket")) {

             mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.mipmap.ic_launcher_round)
                            .setContentTitle("Ouvidoria (versão do Auditor)")
                            .setStyle(new NotificationCompat.BigTextStyle()
                                    .bigText("Um novo ticket foi criado"))
                            .setContentText("Um novo ticket foi criado")
                            .setAutoCancel(true);
        } else {

            mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.mipmap.ic_launcher_round)
                            .setContentTitle("Ouvidoria (versão do Auditor)")
                            .setStyle(new NotificationCompat.BigTextStyle()
                                    .bigText("Chegou uma nova mensagem em um ticket"))
                            .setContentText("Chegou uma nova mensagem em um ticket")
                            .setAutoCancel(true);

        }


        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(1, mBuilder.build());







    }
}
