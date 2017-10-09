package br.edu.ifpb.pdm.ouvidoriacliente.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;

import br.edu.ifpb.pdm.ouvidoriacliente.entities.ComposedLastMessage;
import br.edu.ifpb.pdm.ouvidoriacliente.entities.Mensagem;
import br.edu.ifpb.pdm.ouvidoriacliente.entities.MensagemDto;
import br.edu.ifpb.pdm.ouvidoriacliente.entities.Ticket;
import br.edu.ifpb.pdm.ouvidoriacliente.presentations.ResponseActivity;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by natarajan.
 */

/*
 * IntentService que realiza comunicação com o servidor de mensagens
 */
public class MensagemService extends IntentService {

    public static final String URL_MESSAGE_API = "http://10.0.3.2:8080/ouvidoriaws/api/";    //para uso com a versão local
    private static Retrofit retrofit;

    public MensagemService() {

        super("MensagemService");
        Log.d("Natarajan-debug", "Entrando no serviço de mensagens");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public MensagemService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        Log.d("Natarajan-debug", "Usando o serviço de mensagens");

        // recupera informação vinda na intent que diz qual comando executar
        String command = intent.getStringExtra("command");


        //usando a biblioteca Retrofit para iniciar um client rest simples
        retrofit = new Retrofit.Builder()
                .baseUrl(URL_MESSAGE_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MensagemRestApiService mensagemRestApiService = retrofit.create(MensagemRestApiService.class);// cria apiService Retrofit

        //cria localBroadcast para enviar resultados da execução do serviço
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(getApplicationContext());

        switch (command) {
            //
            case "GETLAST": {

                Log.d("Natarajan-debug", "Pegando última mensagem do ticket");
                Ticket ticket = (Ticket) intent.getSerializableExtra("ticket");
                Long ticketId = null;

                if (ticket != null) {
                    ticketId = ticket.getId();
                } else {
                    ticketId = Long.valueOf(intent.getStringExtra("id_ticket"));
                }



                Call<ComposedLastMessage> call = mensagemRestApiService.getLast(ticketId);   // prepara chamada à api
                try {

                    Response<ComposedLastMessage> response = call.execute();// executa o GET
                    Intent intentResponse = new Intent(getBaseContext(), ResponseActivity.class);

                    // se houver resultados envia pelo local broacast manager
                    if (response.code() == 200) {

                        ComposedLastMessage mensagem = response.body();

                        // intent que identifica o tratamento que a activity deve dar ao receber esse resultado
                        intentResponse.putExtra("mensagem", (Serializable) mensagem.getMensagem());
                        intentResponse.putExtra("ticket", (Serializable) mensagem.getTicket());
                    } else {
                        intentResponse.putExtra("mensagem", (Serializable) null);
                    }

                    intentResponse.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getApplication().startActivity(intentResponse);

                } catch (IOException e) {
                    e.printStackTrace();
                    manager.sendBroadcast(new Intent("br.edu.ifpb.server.ERRO")); //envia mensagem se houve erro
                }
            } break;


            case "MSGCLIENT": {

                Ticket ticket = (Ticket) intent.getSerializableExtra("ticket");
                final String mensagemText = intent.getStringExtra("msg");

                MensagemDto mensagemDto = new MensagemDto();
                mensagemDto.setTicketId(ticket.getId());
                mensagemDto.setFromId(ticket.getFrom().getId());
                mensagemDto.setText(mensagemText);

                Call<Mensagem> call = mensagemRestApiService.salveClienteMessage(mensagemDto);

                try {
                    Response<Mensagem> response = call.execute();// executa o GET


                    // se houver resultados envia pelo local broacast manager
                    if (response.code() == 201) {

                        Mensagem mensagem = response.body();
                        Intent intentResponse = new Intent("br.edu.ifpb.ouvidoriacliente.MSGCLIENT");
                        intentResponse.putExtra("ticket", (Serializable) ticket);
                        intentResponse.putExtra("mensagem", (Serializable) mensagem);
                        manager.sendBroadcast(intentResponse);

                        Intent intentUpdate = new Intent(getApplicationContext(), TicketService.class);
                        intentUpdate.putExtra("command", "GETMY");
                        startService(intentUpdate);

                    } else {

                        try {
                            JSONObject responseError = new JSONObject(response.errorBody().string());
                            String message = responseError.getString("message");

                            Intent intentResponseError = new Intent("br.edu.ifpb.ouvidoriacliente.ERROR_MSG");
                            intentResponseError.putExtra("error", message);
                            manager.sendBroadcast(intentResponseError);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }


                } catch (IOException e) {
                    e.printStackTrace();
                    manager.sendBroadcast(new Intent("br.edu.ifpb.server.ERRO")); //envia mensagem se houve erro
                }
            } break;



        } //end switch

    }
}
