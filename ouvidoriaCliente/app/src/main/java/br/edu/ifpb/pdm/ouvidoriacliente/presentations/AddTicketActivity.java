package br.edu.ifpb.pdm.ouvidoriacliente.presentations;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import br.edu.ifpb.pdm.ouvidoriacliente.R;
import br.edu.ifpb.pdm.ouvidoriacliente.entities.TicketDto;
import br.edu.ifpb.pdm.ouvidoriacliente.enums.TipoTicket;
import br.edu.ifpb.pdm.ouvidoriacliente.services.offline.WatchService;

public class AddTicketActivity extends AppCompatActivity {

    private Spinner spinnerTipoTicket;
    private EditText editTextResumo;
    private Button btnEnviar;
    private LocalBroadcastManager localBroadcastManager;
    private BroadcastReceiver createTicketsBR;
    private BroadcastReceiver errorCreateTicketsBR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ticket);

        localBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());


        spinnerTipoTicket = (Spinner) findViewById(R.id.spinner);
        editTextResumo = (EditText) findViewById(R.id.editTextResume);
        btnEnviar = (Button) findViewById(R.id.buttonEnviar);

        String[] tipos = {"Informação", "Elogio", "Reclamação"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tipos);
        spinnerTipoTicket.setAdapter(adapter);

        btnEnviar.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("OUVIDORIACLIENTE", MODE_PRIVATE);
                String idusuario = sharedPreferences.getString("idusuario", "erro");

                String tipo = spinnerTipoTicket.getSelectedItem().toString();

                TicketDto newTicket = new TicketDto();
                newTicket.setFrom(Long.parseLong(idusuario));
                newTicket.setResume(editTextResumo.getText().toString());
                newTicket.setTipo(TipoTicket.parse(tipo).getId());

                Intent intent = new Intent(getApplicationContext(), WatchService.class);
                intent.putExtra("ticket", newTicket);
                intent.putExtra("command", "CREATE");
                startService(intent);
            }
        });

        createTicketsBR = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("natarajan-debug","ticket criado");
                limparCampos();
                final String localSaving = intent.getStringExtra("local");
                if (localSaving.equals("offline")) {
                    Toast.makeText(context, "Ticket salvo offline", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Ticket criado com sucesso", Toast.LENGTH_SHORT).show();
                }
            }
        };

        errorCreateTicketsBR = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                String error = intent.getStringExtra("error");
                //erro
                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();

            }
        };


    }

    @Override
    protected void onResume() {
        super.onResume();
        localBroadcastManager.registerReceiver(createTicketsBR, new IntentFilter("br.edu.ifpb.ouvidoriacliente.CREATED"));
        localBroadcastManager.registerReceiver(errorCreateTicketsBR, new IntentFilter("br.edu.ifpb.ouvidoriacliente.ERROR_ONCREATE"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        localBroadcastManager.unregisterReceiver(createTicketsBR);
        localBroadcastManager.unregisterReceiver(errorCreateTicketsBR);
    }

    private void limparCampos() {
        spinnerTipoTicket.setSelection(0);
        editTextResumo.setText("");
    }
}
