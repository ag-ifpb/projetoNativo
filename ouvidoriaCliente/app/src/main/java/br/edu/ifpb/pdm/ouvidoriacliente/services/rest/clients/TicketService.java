package br.edu.ifpb.pdm.ouvidoriacliente.services.rest.clients;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import br.edu.ifpb.pdm.ouvidoriacliente.entities.Ticket;
import br.edu.ifpb.pdm.ouvidoriacliente.entities.TicketDto;
import br.edu.ifpb.pdm.ouvidoriacliente.services.retrofit.clients.TicketRestApiService;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by natarajan on 27/06/17.
 */

/*
 * IntentService que realiza comunicação com o servidor de mensagens
 */
public class TicketService extends IntentService {

    public static final String URL_MESSAGE_API = "http://10.0.3.2:8080/ouvidoriaws/api/";    //para uso com a versão local
    private static Retrofit retrofit;

    public TicketService() {

        super("TicketService");
        Log.d("Natarajan-debug", "Entrando no serviço de tickets");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public TicketService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        Log.d("Natarajan-debug", "Usando o serviço de tickets");

        // recupera informação vinda na intent que diz qual comando executar
        String command = intent.getStringExtra("command");

        //usando a biblioteca Retrofit para iniciar um client rest simples
        retrofit = new Retrofit.Builder()
                .baseUrl(URL_MESSAGE_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TicketRestApiService ticketRestApiService = retrofit.create(TicketRestApiService.class);// cria apiService Retrofit

        //cria localBroadcast para enviar resultados da execução do serviço
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(getApplicationContext());

        switch (command) {
            //
            case "GETMY": {

                SharedPreferences sharedPreferences = getSharedPreferences("OUVIDORIACLIENTE", MODE_PRIVATE);
                String idusuario = sharedPreferences.getString("idusuario", "erro");

                Call<List<Ticket>> call = ticketRestApiService.getAllByUser(idusuario);   // prepara chamada à api
                try {
                    List<Ticket> myTickets = call.execute().body();   // executa o GET

                    // se houver resultados envia pelo local broacast manager
                    if (myTickets != null && myTickets.size() > 0) {

                        // intent que identifica o tratamento que a activity deve dar ao receber esse resultado
                        Intent intentResponse = new Intent("br.edu.ifpb.ouvidoriacliente.GETMY");

                        intentResponse.putExtra("mytickets", (Serializable) myTickets);
                        manager.sendBroadcast(intentResponse);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    manager.sendBroadcast(new Intent("br.edu.ifpb.server.ERRO")); //envia mensagem se houve erro
                }
            } break;



            case "CANCEL": {
                Long id = (Long) intent.getSerializableExtra("id");

                try {

                    Response<ResponseBody> execute = ticketRestApiService.cancel(id).execute();

                    //
                    if (execute.code() == 200) {

                        Intent intentResponse = new Intent("br.edu.ifpb.ouvidoriacliente.CANCELED");
                        intentResponse.putExtra("position", intent.getSerializableExtra("position"));
                        manager.sendBroadcast(intentResponse);  // informa à activity

                    } else {
                        // manda mensagem de erro se a menasgem não salva

                        try {
                            JSONObject responseError = new JSONObject(execute.errorBody().string());
                            String message = responseError.getString("message");

                            Intent intentResponseError = new Intent("br.edu.ifpb.ouvidoriacliente.CANCELED_ERROR");
                            intentResponseError.putExtra("error", message);
                            manager.sendBroadcast(intentResponseError);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    manager.sendBroadcast(new Intent("br.edu.ifpb.ouvidoriacliente.ERROR")); //envia mensagem se houve erro
                }
            } break;

            case "CREATE": {
                TicketDto ticket = (TicketDto) intent.getSerializableExtra("ticket");

                try {

                    Response<Ticket> execute = ticketRestApiService.createTicket(ticket).execute();

                    //
                    if (execute.code() == 201) {

                        Intent intentResponse = new Intent("br.edu.ifpb.ouvidoriacliente.CREATED");
                        intentResponse.putExtra("local", "online");
                        manager.sendBroadcast(intentResponse);  // informa à activity

                    } else {
                        // manda mensagem de erro se a menasgem não salva

                        try {
                            JSONObject responseError = new JSONObject(execute.errorBody().string());
                            String message = responseError.getString("message");

                            Intent intentResponseError = new Intent("br.edu.ifpb.ouvidoriacliente.ERROR_ONCREATE");
                            intentResponseError.putExtra("error", message);
                            manager.sendBroadcast(intentResponseError);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    manager.sendBroadcast(new Intent("br.edu.ifpb.ouvidoriacliente.ERROR")); //envia mensagem se houve erro
                }
            } break;


        } //end switch

    }
}
