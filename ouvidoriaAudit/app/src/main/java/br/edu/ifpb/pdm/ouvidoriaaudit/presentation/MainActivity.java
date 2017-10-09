package br.edu.ifpb.pdm.ouvidoriaaudit.presentation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import br.edu.ifpb.pdm.ouvidoriaaudit.R;
import br.edu.ifpb.pdm.ouvidoriaaudit.services.PubNubService;

public class MainActivity extends AppCompatActivity {

    private Button btnToInactives;
    private Button btnToOpens;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnToInactives = (Button) findViewById(R.id.buttonInativos);
        btnToOpens = (Button) findViewById(R.id.buttonAbertos);

        btnToInactives.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), InativosActivity.class));
            }
        });

        btnToOpens.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), OpenTicketActivity.class));
            }
        });

        startService(new Intent(this, PubNubService.class));
    }
}
