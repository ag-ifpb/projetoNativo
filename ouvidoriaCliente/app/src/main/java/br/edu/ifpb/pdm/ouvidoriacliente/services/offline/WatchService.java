package br.edu.ifpb.pdm.ouvidoriacliente.services.offline;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.Serializable;
import java.util.List;

import br.edu.ifpb.pdm.ouvidoriacliente.entities.TicketDto;
import br.edu.ifpb.pdm.ouvidoriacliente.entities.TicketWithIdLocal;
import br.edu.ifpb.pdm.ouvidoriacliente.persistence.TicketDB;
import br.edu.ifpb.pdm.ouvidoriacliente.services.NetworkVerifier;
import br.edu.ifpb.pdm.ouvidoriacliente.services.rest.clients.TicketService;

/**
 * Created by natarajan on 12/10/17.
 */

public class WatchService extends IntentService {

    private TicketDB ticketDB = new TicketDB(this);
    private LocalBroadcastManager localBroadcastManager;


    public WatchService() {
        super("WatchService");
        Log.d("natarajan-debug", "Entrando no serviço intermediário MiddleService");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public WatchService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        localBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());

        String command = intent.getStringExtra("command");

        switch (command) {
            case "CREATE": {
                saveTicket(intent);
            } break;

            case "VERIFY": {
                verifyTickets();
            } break;

        }

    }

    private void verifyTickets() {

        Log.d("natarajan-debug", "Entrando no serviço de tickets offline");
        final List<TicketWithIdLocal> offileTickets = ticketDB.findOffileTickets();
        for (TicketWithIdLocal offileTicket : offileTickets) {
            saveTicketOnline(new TicketDto(offileTicket));
            ticketDB.delete(offileTicket.getId());
        }

    }


    private void saveTicket(Intent intent) {

        TicketDto ticket = (TicketDto) intent.getSerializableExtra("ticket");

        if (NetworkVerifier.isOnline(getApplicationContext())) {
            saveTicketOnline(ticket);
        } else {
            saveTicketOffline(ticket);
        }
    }

    private void saveTicketOffline(TicketDto ticket) {
        long save = ticketDB.save(ticket);
        Intent intentResponse = new Intent("br.edu.ifpb.ouvidoriacliente.CREATED");
        intentResponse.putExtra("local", "offline");
        localBroadcastManager.sendBroadcast(intentResponse );
    }


    private void saveTicketOnline(TicketDto ticket) {
        Intent intentRestClient = new Intent(this, TicketService.class);
        intentRestClient.putExtra("command", "CREATE");
        intentRestClient.putExtra("ticket", (Serializable) ticket);
        startService(intentRestClient);
    }


}
