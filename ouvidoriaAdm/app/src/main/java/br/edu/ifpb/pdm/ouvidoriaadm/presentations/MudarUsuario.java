package br.edu.ifpb.pdm.ouvidoriaadm.presentations;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import br.edu.ifpb.pdm.ouvidoriaadm.R;
import br.edu.ifpb.pdm.ouvidoriaadm.entity.TipoUsuario;
import br.edu.ifpb.pdm.ouvidoriaadm.entity.Usuario;
import br.edu.ifpb.pdm.ouvidoriaadm.services.UsuarioService;

public class MudarUsuario extends AppCompatActivity {

    private TextView textViewUsuario;
    private Button buttonChange;

    private LocalBroadcastManager localBroadcastManager;
    private BroadcastReceiver changeToAuditorBR, errorBR, changeToClient, clientError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mudar_usuario);

        final Usuario usuario = (Usuario) getIntent().getSerializableExtra("usuario");
        textViewUsuario = (TextView) findViewById(R.id.textViewUsuario);
        textViewUsuario.setText(usuario.toString());

        buttonChange = (Button) findViewById(R.id.buttonMudarPara);


        final Intent intentChange = new Intent(this, UsuarioService.class);
        intentChange.putExtra("usuario", usuario);

        // se for cliente
        if (usuario.getTipoUsuarioInt() == TipoUsuario.CLIENTE.getId()) {
            buttonChange.setText("Mudar para Auditor");
            buttonChange.setBackgroundColor(Color.parseColor("#64dd17"));
            intentChange.putExtra("command", "TOAUDITOR");

        } else if (usuario.getTipoUsuarioInt() == TipoUsuario.AUDITOR.getId()) {        // se o usuario for auditor
            buttonChange.setText("Mudar para Cliente");
            buttonChange.setBackgroundColor(Color.parseColor("#d84315"));
            intentChange.putExtra("command", "TOCLIENT");
        }

        buttonChange.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                Log.d("natarajan-debug", "chamando botão de mudar usuário");
                startService(intentChange);
            }
        });


        localBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());

        changeToAuditorBR = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                Toast.makeText(getApplicationContext(), "Usuário agora é um auditor!", Toast.LENGTH_SHORT).show();
                Log.d("natarajan-debug", "msg agora é auditor");
            }
        };


        errorBR = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                String error = intent.getStringExtra("error");
                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                Log.d("natarajan-debug", "msg erro auditor");

            }
        };


        changeToClient = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                Toast.makeText(getApplicationContext(), "Usuário agora é um CLIENTE!", Toast.LENGTH_SHORT).show();
                Log.d("natarajan-debug", "msg agora é cliente");

            }
        };



        clientError = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                String error = intent.getStringExtra("error");
                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                Log.d("natarajan-debug", "msg erro cliente");

            }
        };

    }

    @Override
    protected void onResume() {
        super.onResume();

        localBroadcastManager.registerReceiver(changeToAuditorBR, new IntentFilter("br.edu.ifpb.ouvidoriaadm.TOAUDITOROK"));
        localBroadcastManager.registerReceiver(errorBR, new IntentFilter("br.edu.ifpb.ouvidoriaadm.TOAUDITORERRO"));
        localBroadcastManager.registerReceiver(changeToClient, new IntentFilter("br.edu.ifpb.ouvidoriaadm.TOCLIENTOK"));
        localBroadcastManager.registerReceiver(clientError, new IntentFilter("br.edu.ifpb.ouvidoriaadm.TOCLIENTERRO"));

    }


    @Override
    protected void onPause() {
        super.onPause();
        localBroadcastManager.unregisterReceiver(changeToAuditorBR);
        localBroadcastManager.unregisterReceiver(errorBR);
        localBroadcastManager.unregisterReceiver(changeToClient);
        localBroadcastManager.unregisterReceiver(clientError);
    }
}
