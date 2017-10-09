package br.edu.ifpb.pdm.ouvidoriaadm.services;

import java.util.List;

import br.edu.ifpb.pdm.ouvidoriaadm.entity.Usuario;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by natarajan on 27/06/17.
 */

/*
    Classe que usa a biblioteca Retrofit para construir um client rest

    Ver exemplo:
    https://android.jlelse.eu/consuming-rest-api-using-retrofit-library-in-android-ed47aef01ecb

 */
public interface UsuarioRestApiService {

    @GET("usuario/all")
    Call<List<Usuario>> getAll();

    @POST("usuario")
    Call<Usuario> addUser(@Body Usuario usuario);

    @PUT("usuario/auditor/{id}")
    Call<ResponseBody> changeToAuditor(@Path("id") Long id);

    @DELETE("usuario/auditor/{id}")
    Call<ResponseBody> changeToClient(@Path("id") Long id);

}
