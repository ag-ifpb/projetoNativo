package br.edu.ifpb.pdm.ouvidoriaadm.presentations;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

import java.io.Serializable;

import br.edu.ifpb.pdm.ouvidoriaadm.R;
import br.edu.ifpb.pdm.ouvidoriaadm.entity.TipoUsuario;
import br.edu.ifpb.pdm.ouvidoriaadm.entity.Usuario;
import br.edu.ifpb.pdm.ouvidoriaadm.services.UsuarioService;

public class CadastroUsuarioActivity extends AppCompatActivity {
    private static String[] USER_TYPES = {"CLIENTE", "AUDITOR"};
    private Spinner tipoUsuarioSpiner;
    private Button btnAdicionar;
    private EditText editTextNome;
    private EditText editTextEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastro);

        // pegando componentes
        tipoUsuarioSpiner = (Spinner) findViewById(R.id.spinner);
        btnAdicionar = (Button) findViewById(R.id.buttonAdicionar);
        editTextNome = (EditText)findViewById(R.id.editTextNome);
        editTextEmail = (EditText)findViewById(R.id.editTextEmail);

        //setando spiner do tipo de usuario
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, USER_TYPES);
        tipoUsuarioSpiner.setAdapter(adapter);

        // setando ação do botão de adicionar usuario
        btnAdicionar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String nome = editTextNome.getText().toString();
                String email = editTextEmail.getText().toString();
                final String tipo = tipoUsuarioSpiner.getSelectedItem().toString();
                Integer integer = new Integer(TipoUsuario.valueOf(tipo).getId());

                Usuario usuario = new Usuario();
                usuario.setEmail(email);
                usuario.setNome(nome);
                usuario.setTipo(integer.toString());

                salvarUsuario(usuario);
            }
        });


        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());

        localBroadcastManager.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                limparDados();

                //USUARIO SALVO COM SUCESSO
                Toast.makeText(getApplicationContext(), "Usuário salvo com sucesso!", Toast.LENGTH_LONG).show();

            }
        }, new IntentFilter("br.edu.ifpb.ouvidoriaadm.SAVEDUSER"));


        //mensagem de erro
        localBroadcastManager.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                String error = intent.getStringExtra("error");
                //erro
                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();

            }
        }, new IntentFilter("br.edu.ifpb.ouvidoriaadm.ERRO"));

    }

    private void limparDados() {
        editTextEmail.setText("");
        editTextNome.setText("");
    }

    private void salvarUsuario(Usuario usuario) {
        Log.d("natarajan-debug", "mandando salvar usuário");
        Intent getUsersIntent = new Intent(this, UsuarioService.class);
        getUsersIntent.putExtra("usuario", (Serializable) usuario);
        getUsersIntent.putExtra("command", "POST");
        startService(getUsersIntent);
    }


}
