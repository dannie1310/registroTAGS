package grupohi.mx.registrotags;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Creado por JFEsquivel on 28/09/2016.
 */

class TagModel {
    private Context context;
    private ContentValues data;

    private static SQLiteDatabase db;
    private static DBScaSqlite db_sca;
    String uid;
    String idproyecto;


    public String UID;

    TagModel(Context context) {
        this.context = context;
        this.data = new ContentValues();
        db_sca = new DBScaSqlite(this.context, "sca", null, 1);
        this.data.clear();
    }


    static void deleteAll() {
        db = db_sca.getWritableDatabase();
        try {
            db.execSQL("DELETE FROM tags");
        } finally {
            db.close();
        }
    }



    static boolean create(String UID, String proyecto, String nombre, Context context) {
        ContentValues data = new ContentValues();

        DBScaSqlite db_sca = new DBScaSqlite(context, "sca", null, 1);
        SQLiteDatabase db = db_sca.getWritableDatabase();

        data.clear();
        data.put("uid", UID);
        data.put("idproyecto",proyecto);

        db = db_sca.getWritableDatabase();
        System.out.println("datos: " + data);
        try{
            return db.insert("tags", null, data) > -1;
        } finally {
            db.close();
        }

    }


    static boolean find(String UID) {
        db = db_sca.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM tags WHERE uid = '" + UID + "'", null);
        try{
            if(c != null && c.moveToFirst()) {
               return true;
            }
            else {
                return false;
            }

        } finally {
            c.close();
            db.close();
        }

    }

    TagModel selectAll() {
        db = db_sca.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM tags", null);
        try{
            if (c != null){
                while (c.moveToNext()) {
                    this.UID = c.getString(c.getColumnIndex("uid"));
                    uid = c.getString(c.getColumnIndex("uid"));
                    idproyecto = c.getString(c.getColumnIndex("idproyecto"));
                    System.out.println("SELECT: " + uid + " " + idproyecto);
                }
            }
            return this;
        } finally {
            c.close();
            db.close();
        }
    }

    public TagModel find (Integer id) {
        db = db_sca.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM tag WHERE ID = '" + id + "'", null);
        try {
            if (c != null && c.moveToFirst()) {
                this.uid = c.getString(1);
                this.idproyecto = c.getString(2);

                return this;
            } else {
                return null;
            }
        } finally {
            c.close();
            db.close();
        }
    }
    public static List<TagModel> getTAG(Context context){
        DBScaSqlite db_sca = new DBScaSqlite(context, "sca", null, 1);
        SQLiteDatabase db = db_sca.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM tags ORDER BY 'ID' ASC",null);
        ArrayList tags = new ArrayList<TagModel>();
        try {
            if (c != null){
                while (c.moveToNext()){
                    TagModel ms = new TagModel(context);
                    ms = ms.find(c.getInt(0));
                    tags.add(ms);
                }


                return tags;
            }
            else {
                return new ArrayList<>();
            }
        } finally {
            c.close();
            db.close();
        }
    }
    static Boolean isSync(Context context) {
        DBScaSqlite db_sca = new DBScaSqlite(context, "sca", null, 1);
        SQLiteDatabase db = db_sca.getWritableDatabase();

        Boolean result = true;
        Cursor c = db.rawQuery("SELECT * FROM tags", null);
        try {
            if(c != null && c.moveToFirst()) {
                result = false;
            }
            return result;
        } finally {
            c.close();
            db.close();
        }
    }

    static JSONObject getJSON(Context context) {
        JSONObject JSON = new JSONObject();
        DBScaSqlite db_sca = new DBScaSqlite(context, "sca", null, 1);
        SQLiteDatabase db = db_sca.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM tags", null);
        try {
            if(c != null && c.moveToFirst()) {
                Integer i = 0;
                do {
                    JSONObject json = new JSONObject();

                    json.put("uid", c.getString(c.getColumnIndex("uid")));
                    json.put("id_proyecto", c.getInt(c.getColumnIndex("idproyecto")));

                    JSON.put(i + "", json);
                    i++;
                } while (c.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            c.close();
            db.close();
        }
        System.out.println("json: "+JSON);
        return JSON;
    }
}
