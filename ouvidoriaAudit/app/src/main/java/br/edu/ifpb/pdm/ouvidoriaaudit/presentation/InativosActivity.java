package br.edu.ifpb.pdm.ouvidoriaaudit.presentation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifpb.pdm.ouvidoriaaudit.R;
import br.edu.ifpb.pdm.ouvidoriaaudit.entities.Ticket;
import br.edu.ifpb.pdm.ouvidoriaaudit.presentation.adapters.InactiveTicketArrayAdapter;
import br.edu.ifpb.pdm.ouvidoriaaudit.presentation.adapters.TicketArrayAdapter;
import br.edu.ifpb.pdm.ouvidoriaaudit.services.TicketService;

public class InativosActivity extends AppCompatActivity {


    private ListView listView;
    private TicketArrayAdapter ticketArrayAdapter;
    private LocalBroadcastManager localBroadcastManager;

    private ArrayList<Ticket> inactiveTickets;
    private InactiveTicketArrayAdapter inactiveTicketArrayAdapter;

    private BroadcastReceiver showInactivesBR;
    private BroadcastReceiver canceledTicketBR;
    private BroadcastReceiver errorBR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inativos);

        listView = (ListView) findViewById(R.id.listViewInactives);
        inactiveTickets = new ArrayList<>();

        localBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());

        showInactivesBR = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("natarajan-debug","atualizando lista de usuários");
                List<Ticket> inativos = (List<Ticket>) intent.getSerializableExtra("inativetickets");
                ArrayList<Ticket> al = new ArrayList<Ticket>(inativos);
                atualizarLista(al);

            }
        };

        canceledTicketBR = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int position = (int) intent.getSerializableExtra("position");
                inactiveTicketArrayAdapter.remove(position);
                inactiveTicketArrayAdapter.notifyDataSetChanged();
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


    }


    @Override
    protected void onPause() {
        super.onPause();
        localBroadcastManager.unregisterReceiver(canceledTicketBR);
        localBroadcastManager.unregisterReceiver(showInactivesBR);
        localBroadcastManager.unregisterReceiver(errorBR);
    }

    @Override
    protected void onResume() {
        super.onResume();
        pegarListaAtualizadaNoServer();
        localBroadcastManager.registerReceiver(canceledTicketBR, new IntentFilter("br.edu.ifpb.ouvidoriaaudit.CANCELED"));
        localBroadcastManager.registerReceiver(showInactivesBR, new IntentFilter("br.edu.ifpb.ouvidoriaaudit.GETINACTIVE"));
        localBroadcastManager.registerReceiver(errorBR, new IntentFilter("br.edu.ifpb.ouvidoriaaudit.CANCELED_ERROR"));
        localBroadcastManager.registerReceiver(errorBR, new IntentFilter("br.edu.ifpb.ouvidoriaaudit.ERROR"));
    }


    private void atualizarLista(ArrayList<Ticket> tickets) {
        this.inactiveTickets = tickets;
        this.inactiveTicketArrayAdapter = new InactiveTicketArrayAdapter(inactiveTickets, this);
        listView.setAdapter(inactiveTicketArrayAdapter);
        inactiveTicketArrayAdapter.notifyDataSetChanged();

    }


    public void pegarListaAtualizadaNoServer(){
        Intent getUsersIntent = new Intent(this, TicketService.class);
        getUsersIntent.putExtra("command", "GETINACTIVE");
        startService(getUsersIntent);
        Log.d("natarajan-debug", "consulta inativos");
    }
}
