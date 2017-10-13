package br.edu.ifpb.pdm.ouvidoriacliente.services.retrofit.clients;

import java.util.List;

import br.edu.ifpb.pdm.ouvidoriacliente.entities.Ticket;
import br.edu.ifpb.pdm.ouvidoriacliente.entities.TicketDto;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by natarajan
 */

/*
    Classe que usa a biblioteca Retrofit para construir um client rest

    Ver exemplo:
    https://android.jlelse.eu/consuming-rest-api-using-retrofit-library-in-android-ed47aef01ecb

 */
public interface TicketRestApiService {

    @GET("ticket/byuser/{idUser}")
    Call<List<Ticket>> getAllByUser(@Path("idUser") String idUser);

    @POST("ticket")
    Call<Ticket> createTicket(@Body TicketDto ticket);

    @DELETE("ticket/cancelarcliente/{id}")
    Call<ResponseBody> cancel(@Path("id") Long id);

}
