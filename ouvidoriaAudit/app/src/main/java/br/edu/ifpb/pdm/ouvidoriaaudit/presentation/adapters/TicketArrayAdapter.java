package br.edu.ifpb.pdm.ouvidoriaaudit.presentation.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.edu.ifpb.pdm.ouvidoriaaudit.entities.Ticket;


/**
 * Created by natarajan on 05/10/17.
 */

public class TicketArrayAdapter extends ArrayAdapter<Ticket> implements Serializable{

    List<Ticket> userList = new ArrayList<>();

    public TicketArrayAdapter(Context context, int textViewResourceId,
                              List<Ticket> objects) {
        super(context, textViewResourceId, objects);
        for (int i = 0; i < objects.size(); ++i) {
            userList.add(objects.get(i));
        }
    }

    @Override
    public long getItemId(int position) {
//            userList.get(position);
//            return userList.get();
        return (long) position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

}
