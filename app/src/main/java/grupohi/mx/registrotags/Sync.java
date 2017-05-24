package grupohi.mx.registrotags;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.telephony.TelephonyManager;
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

            /*values.put("metodo", "capturaAltas");
            values.put("usr", usuario.usr);*/
            values.put("token", usuario.token);

            if (TagModel.getJSON(context).length() != 0) {
                values.put("tags_nuevos", String.valueOf(TagModel.getJSON(context)));
            }
            System.out.println(values);
            try {
                URL url = new URL("http://control-acarreos.ccgpp.mx/api/tags_nuevos/"+usuario.usr);
                JSON = HttpConnection.POST(url, values);

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