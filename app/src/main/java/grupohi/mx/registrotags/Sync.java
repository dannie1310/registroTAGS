package grupohi.mx.registrotags;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONObject;

import java.net.URL;

/**
 * Creado por JFEsquivel on 19/10/2016.
 */

class Sync extends AsyncTask<Void, Void, Boolean> {

    private Context context;
    private ProgressDialog progressDialog;
    private Usuario usuario;

    private JSONObject JSON;

    //public String URL_API = "http://portal-aplicaciones.grupohi.mx/";
    public String URL_API = "http://192.168.0.183:8080/";

    Sync(Context context, ProgressDialog progressDialog) {
        this.context = context;
        this.progressDialog = progressDialog;
        usuario = new Usuario(context);
        usuario = usuario.getUsuario();
    }

    @Override
    protected Boolean doInBackground(Void... params) {

            ContentValues values = new ContentValues();
            values.clear();

            values.put("usuario", usuario.usr);
            values.put("clave", usuario.pass);

            if (TagModel.getJSON(context).length() != 0) {
                values.put("tags_nuevos", String.valueOf(TagModel.getJSON(context)));
            }
            System.out.println(values);
            try {
                URL url = new URL(URL_API  + "api/acarreos/tag-global/registrar?access_token=" + usuario.token);
                JSON = Util.JsonHttp(url, values);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        progressDialog.dismiss();
        if(aBoolean) {
            try {
                if (JSON.has("error")) {
                    Toast.makeText(context, (String) JSON.get("error"), Toast.LENGTH_SHORT).show();
                } else if(JSON.has("msj")) {
                    TagModel.deleteAll();
                    Toast.makeText(context, (String) JSON.get("msj"), Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }
}