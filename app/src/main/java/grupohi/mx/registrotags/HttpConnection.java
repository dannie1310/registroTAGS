package grupohi.mx.registrotags;

import android.content.ContentValues;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

/**
 * Creado por JFEsquivel on 07/10/2016.
 */

class HttpConnection {

    static JSONObject POST(URL url, ContentValues values) throws JSONException {

        String body = "";
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + values.get("token"));

            OutputStream os = conn.getOutputStream();
            os.write(values.get("tag").toString().getBytes("UTF-8"));
            os.close();

            int statusCode = conn.getResponseCode();
            Log.i("Status Code",String.valueOf(statusCode));

            if(statusCode== 200){//Vemos si es 200 OK y leemos el cuerpo del mensaje.
                body = readStream(conn.getInputStream());
            }else{
                body = "{\"msj\":\""+statusCode+"\"}";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(body);
        return  new JSONObject(body);
    }
    static JSONObject GET(URL url, String token) throws IOException, JSONException {
        String body = " ";

        try {

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Authorization", "Bearer " + token);
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            String codigoRespuesta = Integer.toString(urlConnection.getResponseCode());
            if(codigoRespuesta.equals("200")){//Vemos si es 200 OK y leemos el cuerpo del mensaje.
                body = readStream(urlConnection.getInputStream());
            }else{
                body = "{\"status_code\":\""+codigoRespuesta+"\"}";
            }
            urlConnection.disconnect();
        } catch (MalformedURLException e) {
            body = e.toString(); //Error URL incorrecta
        } catch (SocketTimeoutException e){
            body = e.toString(); //Error: Finalizado el timeout esperando la respuesta del servidor.
        } catch (Exception e) {
            body = e.toString();//Error diferente a los anteriores.
        }
        return new JSONObject(body);
    }
    private static String readStream(InputStream in) throws IOException{

        BufferedReader r = null;
        r = new BufferedReader(new InputStreamReader(in));
        StringBuilder total = new StringBuilder();
        String line;
        while ((line = r.readLine()) != null) {
            total.append(line);
        }
        if(r != null){
            r.close();
        }
        in.close();
        return total.toString();
    }
}
