package br.edu.ifpb.pdm.ouvidoriacliente.services;

import br.edu.ifpb.pdm.ouvidoriacliente.entities.ComposedLastMessage;
import br.edu.ifpb.pdm.ouvidoriacliente.entities.Mensagem;
import br.edu.ifpb.pdm.ouvidoriacliente.entities.MensagemDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;


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
    Call<ComposedLastMessage> getLast(@Path("id") Long id);

    @POST("mensagem/cliente/{id}")
    Call<Mensagem> salveClienteMessage(@Body MensagemDto message);

//    @POST("mensagem/cliente/{id}")
//    Call<Mensagem> salveClientMessage();


}
