package br.edu.ifpb.pdm.ouvidoriacliente.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifpb.pdm.ouvidoriacliente.entities.TicketDto;
import br.edu.ifpb.pdm.ouvidoriacliente.entities.TicketWithIdLocal;

/**
 * Created by natarajan on 12/10/17.
 */

public class TicketDB extends SQLiteOpenHelper {

    private static final String LOG_TAG = "natarajan-deb SQL";

    public static final String NOME_BANCO = "clienteoffile.sqlite";
    private static final int VERSION = 1;

    public TicketDB(Context context) {
        super(context, NOME_BANCO, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(LOG_TAG, "Criando tabela ticket");
        db.execSQL("create table if not exists ticket (_id integer primary key autoincrement, tipo int, from_user long, resume text);");
        Log.d(LOG_TAG, "Tabela ticket criada");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // dispensável por enquanto
    }

    public long save(TicketDto ticketDto) {
        long id = 0;
        SQLiteDatabase db = getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put("tipo", ticketDto.getTipo());
            values.put("from_user", ticketDto.getFrom());
            values.put("resume", ticketDto.getResume());
            id = db.insert("ticket", "", values);
            return id;
        } finally {
            db.close();
        }
    }

    public int delete(long id) {

        SQLiteDatabase db = getWritableDatabase();
        try {
            // delete from ticket where _id = ?
            int count = db.delete("ticket", "_id=?", new String[]{String.valueOf(id)});
            Log.i(LOG_TAG, "Deletou " + count + " ticket(s)");
            return count;
        } finally {
            db.close();
        }
    }

    public List<TicketWithIdLocal> findOffileTickets() {
        SQLiteDatabase db = getWritableDatabase();
        try {

            //select * from ticket
            Cursor c = db.query("ticket", null, null, null, null, null, null, null);
            return toList(c);

        } finally {

        }
    }

    private List<TicketWithIdLocal> toList(Cursor c) {
        List<TicketWithIdLocal> list = new ArrayList<>();

        if (c.moveToFirst()) {

            do {

                TicketWithIdLocal aTicket = new TicketWithIdLocal();
                int tipo = c.getInt(c.getColumnIndex("tipo"));
                long from = c.getLong(c.getColumnIndex("from_user"));
                String resume = c.getString(c.getColumnIndex("resume"));
                long id = c.getLong(c.getColumnIndex("_id"));

                aTicket.setTipo(tipo);
                aTicket.setFrom(from);
                aTicket.setResume(resume);
                aTicket.setId(id);

                list.add(aTicket);

            } while (c.moveToNext());

        }

        return list;
    }

}
