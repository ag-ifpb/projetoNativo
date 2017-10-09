package br.edu.ifpb.pdm.ouvidoriacliente.presentations;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import br.edu.ifpb.pdm.ouvidoriacliente.R;
import br.edu.ifpb.pdm.ouvidoriacliente.entities.Mensagem;
import br.edu.ifpb.pdm.ouvidoriacliente.entities.Ticket;
import br.edu.ifpb.pdm.ouvidoriacliente.services.MensagemService;

public class ResponseActivity extends AppCompatActivity {

    private TextView textViewLastMessage;
    private TextView textViewTicket;
    private EditText msgText;
    private Button sendButton;
    private Mensagem mensagem;
    private Ticket ticket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_response);

        mensagem = (Mensagem) getIntent().getSerializableExtra("mensagem");
        ticket = (Ticket) getIntent().getSerializableExtra("ticket");

        textViewTicket= (TextView) findViewById(R.id.textViewTicket);
        textViewTicket.setText(ticket.toString());

        textViewLastMessage = (TextView) findViewById(R.id.textViewUltimaMensagem);

        if (mensagem != null)
            textViewLastMessage.setText(mensagem.toString());
        else
            textViewLastMessage.setText("");

        sendButton = (Button) findViewById(R.id.buttonSend);

        sendButton.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {

                sendMessage();

            }
        });

    }

    private void sendMessage() {

        final String msg = ((EditText) findViewById(R.id.editTextMsgText)).getText().toString();

        Intent intent = new Intent(this, MensagemService.class);
        intent.putExtra("command", "MSGCLIENT");
        intent.putExtra("ticket", ticket);
        intent.putExtra("msg", msg);

        startService(intent);
        this.finish();

    }


}
