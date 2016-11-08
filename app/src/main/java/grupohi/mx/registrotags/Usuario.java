package grupohi.mx.registrotags;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Creado por JFEsquivel on 05/10/2016.
 */

class Usuario {

    private Integer idUsuario;
    private Integer idProyecto;
    String usr;
    String pass;
    String nombre;
    String baseDatos;
    String descripcionBaseDatos;

    private Context context;

    private SQLiteDatabase db;
    private DBScaSqlite db_sca;

    Usuario(Context context) {
        this.context = context;
        db_sca = new DBScaSqlite(context, "sca", null, 1);
    }

    boolean create(ContentValues data) {
        db = db_sca.getWritableDatabase();
        Boolean result = db.insert("user", null, data) > -1;
        if (result) {
            this.idUsuario = Integer.valueOf(data.getAsString("idusuario"));
            this.idProyecto = Integer.valueOf(data.getAsString("idproyecto"));
            this.nombre = data.getAsString("nombre");
            this.baseDatos = data.getAsString("base_datos");
            this.descripcionBaseDatos = data.getAsString("descripcion_database");
        }
        db.close();
        return result;
    }

    void destroy() {
        db = db_sca.getWritableDatabase();
        db.execSQL("DELETE FROM user");
        db.close();
    }

    boolean isAuth() {
        db = db_sca.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM user LIMIT 1", null);
        try {
            return c != null && c.moveToFirst();
        } finally {
            c.close();
            db.close();
        }
    }

    public Integer getId() {
        db = db_sca.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM user LIMIT 1", null);
        try {
            if(c != null && c.moveToFirst()) {
                this.idUsuario = c.getInt(0);
            }
            return this.idUsuario;
        } finally {
            c.close();
            db.close();
        }
    }

    Usuario getUsuario() {
        db = db_sca.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM user LIMIT 1", null);
        try {
            if(c != null && c.moveToFirst()) {
                this.idUsuario = c.getInt(c.getColumnIndex("idusuario"));
                this.idProyecto = c.getInt(c.getColumnIndex("idproyecto"));
                this.nombre = c.getString(c.getColumnIndex("nombre"));
                this.baseDatos = c.getString(c.getColumnIndex("base_datos"));
                this.descripcionBaseDatos = c.getString(c.getColumnIndex("descripcion_database"));
                this.usr = c.getString(c.getColumnIndex("user"));
                this.pass = c.getString(c.getColumnIndex("pass"));

                return this;
            } else {
                return null;
            }
        }finally {
            c.close();
            db.close();
        }
    }

    String getNombre(){
        db = db_sca.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT nombre FROM user LIMIT 1", null);
        try {
            if(c!=null && c.moveToFirst()){
                this.nombre =  c.getString(0);
            }
            return this.nombre;
        } finally {
            c.close();
            db.close();
        }
    }

    public String getDescripcion(){
        db = db_sca.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT descripcion_database FROM user LIMIT 1", null);
        try {
            if(c!=null && c.moveToFirst()){
                return c.getString(0);
            }
            else{
                return null;
            }
        } finally {
            c.close();
            db.close();
        }
    }
}

