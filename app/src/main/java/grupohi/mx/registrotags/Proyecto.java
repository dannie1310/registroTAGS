package grupohi.mx.registrotags;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Usuario on 09/11/2016.
 */

class Proyecto {

    private Context context;
    private ContentValues data;

    private static SQLiteDatabase db;
    private static DBScaSqlite db_sca;

    Proyecto(Context context){
        this.context = context;
        data = new ContentValues();
        db_sca = new DBScaSqlite(this.context, "sca", null, 1);
    }

    boolean create(JSONObject data) throws Exception {

        this.data.put("id_proyecto", data.getInt("id_proyecto"));
        this.data.put("descripcion", data.getString("descripcion"));

        db = db_sca.getWritableDatabase();
        try{
            return db.insert("proyectos", null, this.data) > -1;
        } finally {
            db.close();
        }
    }

    static void deleteAll() {
        db = db_sca.getWritableDatabase();
        try {
            db.execSQL("DELETE FROM proyectos");
        } finally {
            db.close();
        }
    }

    static ArrayList<String> getArrayListProyectos(Context context) {
        ArrayList<String> data = new ArrayList<>();
        String query;
        DBScaSqlite db_sca = new DBScaSqlite(context, "sca", null, 1);
        SQLiteDatabase db = db_sca.getWritableDatabase();

        query =  "SELECT * FROM proyectos ORDER BY id_proyecto ASC";

        Cursor c = db.rawQuery(query, null);
        if (c != null && c.moveToFirst())
            try {
                data.add("-- Seleccione --");
                data.add(c.getString(c.getColumnIndex("descripcion")));
                while (c.moveToNext()) {
                    data.add(c.getString(c.getColumnIndex("descripcion")));
                }
            } finally {
                c.close();
                db.close();
            }
        return data;
    }

    static ArrayList<String> getArrayListId(Context context) {
        ArrayList<String> data = new ArrayList<>();
        String query;
        DBScaSqlite db_sca = new DBScaSqlite(context, "sca", null, 1);
        SQLiteDatabase db = db_sca.getWritableDatabase();

        query ="SELECT * FROM proyectos ORDER BY id_proyecto ASC";


        Cursor c = db.rawQuery(query, null);
        try {
            if (c != null && c.moveToFirst()) {
                data.add("0");
                data.add(c.getString(c.getColumnIndex("id_proyecto")));
                System.out.println(c.getString(c.getColumnIndex("id_proyecto")));
                while (c.moveToNext()) {
                    data.add(c.getString(c.getColumnIndex("id_proyecto")));
                }
            }
            return data;
        } finally {
            c.close();
            db.close();
        }
    }


}
