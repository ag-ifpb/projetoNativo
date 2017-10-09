package br.edu.ifpb.pdm.ouvidoriaadm.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import br.edu.ifpb.pdm.ouvidoriaadm.entity.ErrorMessage;
import br.edu.ifpb.pdm.ouvidoriaadm.entity.Usuario;
import okhttp3.ResponseBody;
import retrofit2.Call;
//import retrofit2.Response;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by natarajan on 27/06/17.
 */

/*
 * IntentService que realiza comunicação com o servidor de mensagens
 */
public class UsuarioService extends IntentService {

    //    public static final String URL_MESSAGE_API = "http://10.0.3.2:8081/message-server/api/";    //para uso com a versão server docker
//    public static final String URL_MESSAGE_API = "https://pdmmessages.herokuapp.com/api/";    //para uso com a versão server online
//    public static final String URL_MESSAGE_API = "http://localhost:8080/ouvidoriaws/api/";    //para uso com a versão local
    public static final String URL_MESSAGE_API = "http://10.0.3.2:8080/ouvidoriaws/api/";    //para uso com a versão local
    private static Retrofit retrofit;

    public UsuarioService() {

        super("UsuarioService");
        Log.d("Natarajan-debug", "Entrando no serviço");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public UsuarioService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        Log.d("Natarajan-debug", "Usando o serviço");

        // recupera informação vinda na intent que diz qual comando executar
        String command = intent.getStringExtra("command");

        //usando a biblioteca Retrofit para iniciar um client rest simples
        retrofit = new Retrofit.Builder()
                .baseUrl(URL_MESSAGE_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UsuarioRestApiService usuarioRestApiService = retrofit.create(UsuarioRestApiService.class);// cria apiService Retrofit

        //cria localBroadcast para enviar resultados da execução do serviço
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(getApplicationContext());

        switch (command) {
            // comando ler mensagens
            case "GETALL": {

                Call<List<Usuario>> call = usuarioRestApiService.getAll();   // prepara chamada à api
                try {
                    List<Usuario> usuarios = call.execute().body();   // executa o GET

                    // se houver resultados envia pelo local broacast manager
                    if (usuarios != null && usuarios.size() > 0) {

                        // intent que identifica o tratamento que a activity deve dar ao receber esse resultado
                        Intent intentResponse = new Intent("br.edu.ifpb.ouvidoriaadm.GETALLUSERS");

                        intentResponse.putExtra("usuarios", (Serializable) usuarios);
                        manager.sendBroadcast(intentResponse);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    manager.sendBroadcast(new Intent("br.edu.ifpb.server.ERRO")); //envia mensagem se houve erro
                }
            } break;

            // comando salvar usuario
            case "POST": {
                Usuario usuario = (Usuario) intent.getSerializableExtra("usuario");
                Call<Usuario> call = usuarioRestApiService.addUser(usuario); // prepara chamada à api

                try {
                    Response<Usuario> responseCall = call.execute();  // chamando o POST

                    // se a mensagem foi devidamente salva/criada
                    if (responseCall.code() == 201) {

                        Usuario savedUser = responseCall.body();
                        Intent intentResponse = new Intent("br.edu.ifpb.ouvidoriaadm.SAVEDUSER");
                        intentResponse.putExtra("saved", (Serializable) savedUser);
                        manager.sendBroadcast(intentResponse);  // informa à activity

                    } else {
                        // manda mensagem de erro se a menasgem não salva

                        try {
                            JSONObject responseError = new JSONObject(responseCall.errorBody().string());
                            String message = responseError.getString("message");

                            Intent intentResponseError = new Intent("br.edu.ifpb.ouvidoriaadm.ERRO");
                            intentResponseError.putExtra("error", message);
                            manager.sendBroadcast(intentResponseError);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    manager.sendBroadcast(new Intent("br.edu.ifpb.ouvidoriaadm.ERRO")); //envia mensagem se houve erro
                }
            } break;

            case "TOAUDITOR": {
                Usuario usuario = (Usuario) intent.getSerializableExtra("usuario");

                try {

                    Response<ResponseBody> execute = usuarioRestApiService.changeToAuditor(usuario.getId()).execute();

                    // se a mensagem foi devidamente salva/criada
                    if (execute.code() == 202) {

                        Intent intentResponse = new Intent("br.edu.ifpb.ouvidoriaadm.TOAUDITOROK");
                        manager.sendBroadcast(intentResponse);  // informa à activity

                    } else {
                        // manda mensagem de erro se a menasgem não salva

                        try {
                            JSONObject responseError = new JSONObject(execute.errorBody().string());
                            String message = responseError.getString("message");

                            Intent intentResponseError = new Intent("br.edu.ifpb.ouvidoriaadm.TOAUDITORERRO");
                            intentResponseError.putExtra("error", message);
                            manager.sendBroadcast(intentResponseError);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    manager.sendBroadcast(new Intent("br.edu.ifpb.ouvidoriaadm.ERRO")); //envia mensagem se houve erro
                }
            } break;

            case "TOCLIENT": {
                Usuario usuario = (Usuario) intent.getSerializableExtra("usuario");

                try {

                    Response<ResponseBody> execute = usuarioRestApiService.changeToClient(usuario.getId()).execute();

                    // se a mensagem foi devidamente salva/criada
                    if (execute.code() == 202) {

                        Intent intentResponse = new Intent("br.edu.ifpb.ouvidoriaadm.TOCLIENTOK");
                        manager.sendBroadcast(intentResponse);  // informa à activity

                    } else {
                        // manda mensagem de erro se a menasgem não salva

                        try {
                            JSONObject responseError = new JSONObject(execute.errorBody().string());
                            String message = responseError.getString("message");

                            Intent intentResponseError = new Intent("br.edu.ifpb.ouvidoriaadm.TOCLIENTERRO");
                            intentResponseError.putExtra("error", message);
                            manager.sendBroadcast(intentResponseError);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    manager.sendBroadcast(new Intent("br.edu.ifpb.ouvidoriaadm.ERRO")); //envia mensagem se houve erro
                }
            } break;

        } //end switch

    }
}
