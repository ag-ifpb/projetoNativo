package br.edu.ifpb.pdm.ouvidoriacliente.services.rest.clients;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by natarajan on 12/10/17.
 */

public class CreatedTicketBR extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        final String msg = intent.getStringExtra("msg");
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
