package br.edu.ifpb.pdm.ouvidoriacliente.presentations;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import br.edu.ifpb.pdm.ouvidoriacliente.R;

public class MainActivity extends AppCompatActivity {

    private Button btnMyTickets;
    private Button btnNewTicket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btnMyTickets = (Button) findViewById(R.id.buttonMyTickets);
        btnNewTicket = (Button) findViewById(R.id.buttonNewTicket);

        btnMyTickets.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MyTicketActivity.class));
            }
        });

        btnNewTicket.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AddTicketActivity.class));
            }
        });


    }
}
