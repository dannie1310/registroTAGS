package grupohi.mx.registrotags;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.json.JSONObject;

/**
 * Creado por JFEsquivel on 28/09/2016.
 */

class TagModel {
    private Context context;
    private ContentValues data;

    private static SQLiteDatabase db;
    private DBScaSqlite db_sca;

    public String UID;

    TagModel(Context context) {
        this.context = context;
        this.data = new ContentValues();
        db_sca = new DBScaSqlite(this.context, "sca", null, 1);
        this.data.clear();
    }

    boolean registrarTags(JSONObject data) throws Exception {
        this.data.clear();
        this.data.put("uid", data.getString("uid"));
        this.data.put("idcamion", data.getString("idcamion"));
        this.data.put("idproyecto", data.getString("idproyecto"));

        db = db_sca.getWritableDatabase();
        try{
            return db.insert("tags", null, this.data) > -1;
        } finally {
            db.close();
        }
    }

    void deleteAll() {
        db = db_sca.getWritableDatabase();
        try {
            db.execSQL("DELETE FROM tags");
        } finally {
            db.close();
        }
    }



    static boolean create(String UID, String proyecto, Context context) {
        ContentValues data = new ContentValues();

        DBScaSqlite db_sca = new DBScaSqlite(context, "sca", null, 1);
        SQLiteDatabase db = db_sca.getWritableDatabase();
        data.putNull("idcamion");

        data.clear();
        data.put("uid", UID);
        data.put("idcamion", "null");
        data.put("idproyecto",proyecto);

        db = db_sca.getWritableDatabase();
        try{
            return db.insert("tags", null, data) > -1;
        } finally {
            db.close();
        }

    }


    TagModel find(String UID) {
        db = db_sca.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM tags WHERE uid = '" + UID + "'", null);
        try{
            if(c != null && c.moveToFirst()) {
                this.UID = c.getString(c.getColumnIndex("uid"));
            }
            return this;
        } finally {
            c.close();
            db.close();
        }
    }
}
