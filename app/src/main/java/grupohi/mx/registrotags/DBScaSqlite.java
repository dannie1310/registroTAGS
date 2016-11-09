package grupohi.mx.registrotags;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by JFEsquivel on 26/09/2016.
 */

public class DBScaSqlite extends SQLiteOpenHelper {

    private String[] queries = new String[] {
            "CREATE TABLE user (idusuario INTEGER PRIMARY KEY, nombre TEXT, usr TEXT, pass TEXT, idproyecto INTEGER, base_datos TEXT, descripcion_database TEXT)",
            "CREATE TABLE tags (ID INTEGER PRIMARY KEY AUTOINCREMENT, uid TEXT, idproyecto INTEGER, usuario TEXT)",
    };

    public DBScaSqlite(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for (String query : queries) {
            db.execSQL(query);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS user");
        db.execSQL("DROP TABLE IF EXISTS tags");

        for (String query : queries) {
            db.execSQL(query);
        }

        db.close();
    }
}
