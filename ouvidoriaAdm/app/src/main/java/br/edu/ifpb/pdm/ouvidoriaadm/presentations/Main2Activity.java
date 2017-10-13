package br.edu.ifpb.pdm.ouvidoriaadm.presentations;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;

import br.edu.ifpb.pdm.ouvidoriaadm.R;
import br.edu.ifpb.pdm.ouvidoriaadm.entity.Usuario;
import br.edu.ifpb.pdm.ouvidoriaadm.services.UsuarioService;


public class Main2Activity extends AppCompatActivity {

    private ListView listView;
    private List<Usuario> usersList;
    private UsuarioArrayAdapter usuarioArrayAdapter;
    private LocalBroadcastManager localBroadcastManager;

    private BroadcastReceiver updateListBR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        listView = (ListView) findViewById(R.id.listUsers);
        usersList = new ArrayList<>();

        this.usuarioArrayAdapter = new UsuarioArrayAdapter(this, android.R.layout.simple_list_item_1, usersList);
        listView.setAdapter(usuarioArrayAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intentOpenChangeUser = new Intent(Main2Activity.this, MudarUsuario.class);
                intentOpenChangeUser.putExtra("usuario", usersList.get(position));
                startActivity(intentOpenChangeUser);
            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                Main2Activity.this.abrirTelaCadastro();
            }
        });

        localBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());

        updateListBR = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("natarajan-debug", "atualizando lista de usuários");
                List<Usuario> usuarios = (List<Usuario>) intent.getSerializableExtra("usuarios");
                usuarioArrayAdapter.clear();
                usuarioArrayAdapter.addAll(usuarios);
                usuarioArrayAdapter.notifyDataSetChanged();

            }
        };


    }

    @Override
    protected void onResume() {

        super.onResume();
        Intent getUsersIntent = new Intent(this, UsuarioService.class);
        getUsersIntent.putExtra("command", "GETALL");
        startService(getUsersIntent);

        localBroadcastManager.registerReceiver(updateListBR, new IntentFilter("br.edu.ifpb.ouvidoriaadm.GETALLUSERS"));

        Log.d("natarajan-debug", "consulta usuários");

    }

    @Override
    protected void onPause() {
        super.onPause();
        localBroadcastManager.unregisterReceiver(updateListBR);
    }

    public void abrirTelaCadastro() {
        Log.d("natarajan-debug","entrando na tela de cadastro");
        Intent intent = new Intent(this, CadastroUsuarioActivity.class);
        startActivity(intent);
    }




}
