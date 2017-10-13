package br.edu.ifpb.pdm.ouvidoriacliente;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import br.edu.ifpb.pdm.ouvidoriacliente.services.NetworkVerifier;
import br.edu.ifpb.pdm.ouvidoriacliente.services.offline.WatchService;

/**
 * Created by natarajan on 09/10/17.
 */

public class NetworkChangeReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        if (NetworkVerifier.isOnline(context)) {

            Toast.makeText(context, "Verificando conteúdos offline", Toast.LENGTH_SHORT).show();
            Intent intentVerifyOffline = new Intent(context, WatchService.class);
            intentVerifyOffline.putExtra("command", "VERIFY");
            context.startService(intentVerifyOffline);
        }

    }


}
