package grupohi.mx.registrotags;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Creado por JFEsquivel on 19/10/2016.
 */

class Sync extends AsyncTask<Void, Void, Boolean> {

    private Context context;
    private ProgressDialog progressDialog;
    private Usuario usuario;
    private int reject = 0;
    private ArrayList<String> tags = new ArrayList<>();
    private int encontrados = 0;
    private int guardado = 0;
    private int numTag = 0;

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
            JSONArray json = TagModel.getJSON(context);
            numTag = json.length();

            if (json != null) {
                for (int i = 0; i < json.length(); i++){
                    try {
                        JSONObject dato = (JSONObject) json.get(i);
                        values.put("tag", dato.toString());
                        URL url = new URL("http://172.50.32.104:8080/api/tags_nuevos");
                        JSON = HttpConnection.POST(url, values);
                        try {
                            if (JSON.has("msj")) {
                                switch (JSON.get("msj").toString()){
                                    case "ok":
                                        tags.add(dato.get("uid").toString());
                                        encontrados++;
                                        break;
                                    case "true":
                                        tags.add(dato.get("uid").toString());
                                        guardado++;
                                        break;
                                    case "false":
                                        reject++;
                                        break;
                                    case "401":
                                        try {
                                            ContentValues login = new ContentValues();
                                            login.put("usuario", usuario.usr);
                                            login.put("clave", usuario.pass);
                                            URL link = new URL("http://172.50.32.104:8080/api/authenticate");
                                            JSON = Util.JsonHttp(link, login);
                                            ContentValues data = new ContentValues();
                                            data.put("token", (String) JSON.get("token"));
                                            usuario.update(data,usuario.getId());
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            return false;
                                        }
                                        return false;
                                }
                            }
                        } catch (Exception e) {
                            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
            }
            System.out.println(values);
            return true;
        }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        progressDialog.dismiss();
        if(aBoolean) {
            if(reject ==  0){
                for(int i = 0; i< tags.size(); i++){
                    boolean resp = TagModel.deleteTitle(tags.get(i));
                }
            }

            Toast.makeText(context, "Tags Enviados.\nNuevos Registros: "+guardado+" de "+numTag+ ".\nRegistrados previamente "+encontrados+".", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Ocurrio un error al enviar los datos, Intente de nuevo. ", Toast.LENGTH_SHORT).show();
        }
    }
}