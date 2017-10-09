package br.edu.ifpb.pdm.ouvidoriacliente.presentations.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import br.edu.ifpb.pdm.ouvidoriacliente.R;
import br.edu.ifpb.pdm.ouvidoriacliente.entities.Ticket;
import br.edu.ifpb.pdm.ouvidoriacliente.enums.StatusTicket;
import br.edu.ifpb.pdm.ouvidoriacliente.services.MensagemService;
import br.edu.ifpb.pdm.ouvidoriacliente.services.TicketService;


/**
 * Created by natarajan on 05/10/17.
 */

public class OpenTicketArrayAdapter extends BaseAdapter implements ListAdapter {

    private ArrayList<Ticket> list = new ArrayList<>();
    private Context context;

    public OpenTicketArrayAdapter(ArrayList<Ticket> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Ticket getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).getId();
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {

        final int itemPosition = position;
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.openitemlist, null);
        }

        //Handle TextView and display string from your list
        TextView listItemText = (TextView)view.findViewById(R.id.list_item_string);
        listItemText.setText(list.get(position).toString());

        //Handle buttons and add onClickListeners
        Button btnAction = (Button)view.findViewById(R.id.btnResponse);

        final StatusTicket status = StatusTicket.valueOf(getItem(position).getStatus());
        if (status.equals(StatusTicket.OPEN)) {
            btnAction.setText("Cancelar");

            btnAction.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, TicketService.class);
                    intent.putExtra("command", "CANCEL");
                    intent.putExtra("id", getItem(itemPosition).getId());
                    intent.putExtra("position", itemPosition);

                    context.startService(intent);

                }
            });


        } else {
            btnAction.setText("Responder");

            btnAction.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {


                    Intent intent = new Intent(context, MensagemService.class);
                    intent.putExtra("command", "GETLAST");
                    intent.putExtra("ticket", getItem(itemPosition));
                    intent.putExtra("position", itemPosition);

                    context.startService(intent);

                }
            });
        }




        return view;
    }

    public void remove(int position) {
        this.list.remove(position);
    }


}
