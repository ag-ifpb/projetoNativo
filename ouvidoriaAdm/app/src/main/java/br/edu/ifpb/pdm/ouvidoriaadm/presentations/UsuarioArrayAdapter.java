package br.edu.ifpb.pdm.ouvidoriaadm.presentations;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.edu.ifpb.pdm.ouvidoriaadm.entity.Usuario;

/**
 * Created by natarajan on 05/10/17.
 */

public class UsuarioArrayAdapter extends ArrayAdapter<Usuario> implements Serializable{

    List<Usuario> userList = new ArrayList<>();

    public UsuarioArrayAdapter(Context context, int textViewResourceId,
                               List<Usuario> objects) {
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
