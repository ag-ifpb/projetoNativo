package br.edu.ifpb.pdm.ouvidoriacliente.presentations;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifpb.pdm.ouvidoriacliente.R;
import br.edu.ifpb.pdm.ouvidoriacliente.entities.Ticket;
import br.edu.ifpb.pdm.ouvidoriacliente.presentations.adapters.OpenTicketArrayAdapter;
import br.edu.ifpb.pdm.ouvidoriacliente.services.TicketService;

public class MyTicketActivity extends AppCompatActivity {

    private ListView listView;
    private LocalBroadcastManager localBroadcastManager;
    private OpenTicketArrayAdapter openTicketArrayAdapter;
    private BroadcastReceiver showMyTicketsBR;
    private BroadcastReceiver canceledTicketBR;
    private BroadcastReceiver errorBR;
    private BroadcastReceiver msgBR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mytickets);

        listView = (ListView) findViewById(R.id.listViewInactives);


        localBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());

        showMyTicketsBR = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("natarajan-debug","atualizando lista de tickets abertos");
                List<Ticket> myTickets = (List<Ticket>) intent.getSerializableExtra("mytickets");
                ArrayList<Ticket> al = new ArrayList<Ticket>(myTickets);
                atualizarLista(al);

            }
        };

        canceledTicketBR = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int position = (int) intent.getSerializableExtra("position");
                openTicketArrayAdapter.remove(position);
                openTicketArrayAdapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), "Cancelado com sucesso!", Toast.LENGTH_SHORT).show();

            }
        };

        errorBR = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                String error = intent.getStringExtra("error");
                //erro
                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();

            }
        };

        msgBR = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                Toast.makeText(getApplicationContext(), "Mensagem enviada com sucesso", Toast.LENGTH_SHORT).show();

            }
        };

    }


    @Override
    protected void onPause() {
        super.onPause();
        localBroadcastManager.unregisterReceiver(canceledTicketBR);
        localBroadcastManager.unregisterReceiver(showMyTicketsBR);
        localBroadcastManager.unregisterReceiver(errorBR);
        localBroadcastManager.unregisterReceiver(msgBR);

    }

    @Override
    protected void onResume() {
        super.onResume();
        pegarListaAtualizadaNoServer();


        localBroadcastManager.registerReceiver(canceledTicketBR, new IntentFilter("br.edu.ifpb.ouvidoriacliente.CANCELED"));
        localBroadcastManager.registerReceiver(showMyTicketsBR, new IntentFilter("br.edu.ifpb.ouvidoriacliente.GETMY"));
        localBroadcastManager.registerReceiver(errorBR, new IntentFilter("br.edu.ifpb.ouvidoriacliente.CANCELED_ERROR"));
        localBroadcastManager.registerReceiver(errorBR, new IntentFilter("br.edu.ifpb.ouvidoriacliente.ERROR"));
        localBroadcastManager.registerReceiver(errorBR, new IntentFilter("br.edu.ifpb.ouvidoriacliente.ERROR_MSG"));
        localBroadcastManager.registerReceiver(msgBR, new IntentFilter("br.edu.ifpb.ouvidoriacliente.MSGCLIENT"));

    }


    private void atualizarLista(ArrayList<Ticket> tickets) {

        this.openTicketArrayAdapter = new OpenTicketArrayAdapter(tickets, this);
        listView.setAdapter(openTicketArrayAdapter);
        openTicketArrayAdapter.notifyDataSetChanged();

    }


    public void pegarListaAtualizadaNoServer(){
        Intent getUsersIntent = new Intent(this, TicketService.class);
        getUsersIntent.putExtra("command", "GETMY");
        startService(getUsersIntent);
        Log.d("natarajan-debug", "consulta tickets do usuário");
    }
}
