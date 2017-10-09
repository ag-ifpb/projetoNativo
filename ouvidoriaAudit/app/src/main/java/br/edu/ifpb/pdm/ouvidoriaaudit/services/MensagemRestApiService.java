package br.edu.ifpb.pdm.ouvidoriaaudit.services;

import java.util.List;

import br.edu.ifpb.pdm.ouvidoriaaudit.entities.ComposedLastMessage;
import br.edu.ifpb.pdm.ouvidoriaaudit.entities.Mensagem;
import br.edu.ifpb.pdm.ouvidoriaaudit.entities.MensagemDto;
import br.edu.ifpb.pdm.ouvidoriaaudit.entities.Ticket;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
//import okhttp3.ResponseBody;
//import retrofit2.Call;
//import retrofit2.http.DELETE;
//import retrofit2.http.GET;
//import retrofit2.http.Path;

/**
 * Created by natarajan on 27/06/17.
 */

/*
    Classe que usa a biblioteca Retrofit para construir um client rest

    Ver exemplo:
    https://android.jlelse.eu/consuming-rest-api-using-retrofit-library-in-android-ed47aef01ecb

 */
public interface MensagemRestApiService {

    @GET("mensagem/last/{id}")
//    Call<Mensagem> getLast(@Path("id") Long id);
    Call<ComposedLastMessage> getLast(@Path("id") Long id);

    @POST("mensagem/auditor/{id}")
    Call<Mensagem> salveAuditorMessage(@Body MensagemDto message);

//    @POST("mensagem/cliente/{id}")
//    Call<Mensagem> salveClientMessage();


}
