package br.edu.ifpb.pdm.ouvidoriaaudit.presentation;

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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.edu.ifpb.pdm.ouvidoriaaudit.R;
import br.edu.ifpb.pdm.ouvidoriaaudit.entities.Mensagem;
import br.edu.ifpb.pdm.ouvidoriaaudit.entities.Ticket;
import br.edu.ifpb.pdm.ouvidoriaaudit.presentation.adapters.OpenTicketArrayAdapter;
import br.edu.ifpb.pdm.ouvidoriaaudit.presentation.adapters.TicketArrayAdapter;
import br.edu.ifpb.pdm.ouvidoriaaudit.services.TicketService;

public class OpenTicketActivity extends AppCompatActivity {


    private ListView listView;
    private TicketArrayAdapter ticketArrayAdapter;
    private LocalBroadcastManager localBroadcastManager;

    private ArrayList<Ticket> inactiveTickets;
    private OpenTicketArrayAdapter openTicketArrayAdapter;

    private BroadcastReceiver showOpenTicketsBR;
    private BroadcastReceiver canceledTicketBR;
    private BroadcastReceiver errorBR;
    private BroadcastReceiver msgBR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inativos);

        listView = (ListView) findViewById(R.id.listViewInactives);
        inactiveTickets = new ArrayList<>();

        localBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());

        showOpenTicketsBR = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("natarajan-debug","atualizando lista de tickets abertos");
                List<Ticket> inativos = (List<Ticket>) intent.getSerializableExtra("opentickets");
                ArrayList<Ticket> al = new ArrayList<Ticket>(inativos);
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
        localBroadcastManager.unregisterReceiver(showOpenTicketsBR);
        localBroadcastManager.unregisterReceiver(errorBR);
        localBroadcastManager.unregisterReceiver(msgBR);

    }

    @Override
    protected void onResume() {
        super.onResume();
        pegarListaAtualizadaNoServer();
        localBroadcastManager.registerReceiver(canceledTicketBR, new IntentFilter("br.edu.ifpb.ouvidoriaaudit.CANCELED"));
        localBroadcastManager.registerReceiver(showOpenTicketsBR, new IntentFilter("br.edu.ifpb.ouvidoriaaudit.GETOPEN"));
        localBroadcastManager.registerReceiver(errorBR, new IntentFilter("br.edu.ifpb.ouvidoriaaudit.CANCELED_ERROR"));
        localBroadcastManager.registerReceiver(errorBR, new IntentFilter("br.edu.ifpb.ouvidoriaaudit.ERROR"));
        localBroadcastManager.registerReceiver(msgBR, new IntentFilter("br.edu.ifpb.ouvidoriaaudit.MSGCLIENT"));

    }


    private void atualizarLista(ArrayList<Ticket> tickets) {
        this.inactiveTickets = tickets;
        this.openTicketArrayAdapter = new OpenTicketArrayAdapter(inactiveTickets, this);
        listView.setAdapter(openTicketArrayAdapter);
        openTicketArrayAdapter.notifyDataSetChanged();

    }


    public void pegarListaAtualizadaNoServer(){
        Intent getUsersIntent = new Intent(this, TicketService.class);
        getUsersIntent.putExtra("command", "GETOPEN");
        startService(getUsersIntent);
        Log.d("natarajan-debug", "consulta tickets abertos");
    }
}
