package grupohi.mx.registrotags;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.nfc.Tag;
import android.nfc.tech.MifareUltralight;

import java.math.BigInteger;

/**
 * Created by Usuario on 01/11/2016.
 */

public class NFCUltralight {
    private Tag NFCTag;
    Context context;

    public NFCUltralight(Tag NFCTag, Context context) {
        this.context=context;
        this.NFCTag = NFCTag;
    }

    public String read(Tag nfc){
        MifareUltralight mf=MifareUltralight.get(nfc);
        byte[] toRead = null;
        String aux="";
        try{
            mf.connect();
            for (int x=0; x<=41;x++) {
                toRead = mf.readPages(x);
                String s = new String(toRead);
                aux += s;
            }

            mf.close();
        }catch(Exception e){
          //  e.printStackTrace();
            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Se perdió la conexión con el TAG");
            builder.setMessage("Intenta de nuevo.");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    builder.getContext();
                }
            });

            builder.create();
            builder.show();
        }

        return aux;
    }

    public String getId(Tag mytag){
        String aux="";
        MifareUltralight mf= MifareUltralight.get(mytag);
        byte[] id=null;
        try{
            mf.connect();
            for(int r=0; r<=4; r++) {
                id = mf.readPages(r);
                aux+=byteArrayToHexString(id);
            }
            //aux+=byteArrayToHexString(id);

            mf.close();
        }catch (Exception e){
            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Se perdió la conexión con el TAG");
            builder.setMessage("Intenta de nuevo.");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    builder.getContext();
                }
            });

            builder.create();
            builder.show();
        }

        return aux;
    }

    public boolean writePagina(Tag mytag, int page, String mensaje ){
        byte[] value =  mensaje.getBytes();
        byte[] aux =  new byte[4];
        MifareUltralight mf= MifareUltralight.get(mytag);
        int z=0;
        int auxPages =0;
        try{
            mf.connect();
            while(z != value.length) {
                for (int x = 0; x < 4; x++) {
                    if(z < value.length ) {
                        aux[x]=value[z];
                        z++;
                    }
                    else{
                        aux[x]=0;
                    }
                }
                if(aux.length==4) {
                    mf.writePage(page+auxPages, aux);
                    auxPages+=1;
                }
            }
            mf.close();
            return true;
        }catch (Exception e){
            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Se perdió la conexión con el TAG");
            builder.setMessage("Intenta de nuevo.");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    builder.getContext();
                }
            });

            builder.create();
            builder.show();
            return false;
        }
    }

    public boolean formateo(Tag nfc){
        MifareUltralight mf=MifareUltralight.get(nfc);

        byte[] value = new byte[4];
        try{
            mf.connect();
            for (int x = 0; x < 4; x++) {
                value[x] = 0;
            }
            for (int x=4; x<=39;x++) {
                mf.writePage(x,value);
            }

            mf.close();
            return true;
        }catch(Exception e){
            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Se perdió la conexión con el TAG");
            builder.setMessage("Intenta de nuevo.");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    builder.getContext();
                }
            });

            builder.create();
            builder.show();
            return false;
        }
    }

    public boolean write(Tag mytag, int page, String mensaje ){
        byte[] value =  mensaje.getBytes();
        byte[] aux = new byte[4];
        MifareUltralight mf= MifareUltralight.get(mytag);
        int z=0;
        int auxPages =0;
        try{
            mf.connect();
                for (int x = 0; x < 4; x++) {
                    if(z < value.length ) {
                        aux[x]=value[z];
                        z++;
                    }
                    else{
                        aux[x]=0;
                    }
                }
               mf.writePage(page, aux);
            mf.close();
            return true;
        }catch (Exception e){
            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Se perdió la conexión con el TAG");
            builder.setMessage("Intenta de nuevo.");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    builder.getContext();
                }
            });

            builder.create();
            builder.show();
            return false;
        }
    }

    public String readPage(Tag nfc, int page){
        MifareUltralight mf=MifareUltralight.get(nfc);
        byte[] toRead = null;
        byte[] auxRead =  new byte[4];
        String aux="";
        try{
            mf.connect();
            toRead = mf.readPages(page);
            for(int i=0; i<4; i++) {
                    auxRead[i] = toRead[i];
            }
            String x = byteArrayToHexString(auxRead);
            if(x.equalsIgnoreCase("00000000")){
                aux=null;
            }
            else {
                String s = new String(auxRead);
                aux += s;
                toRead = null;
            }
            mf.close();
        }catch(Exception e){
           // e.printStackTrace();
            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Se perdió la conexión con el TAG");
            builder.setMessage("Intenta de nuevo.");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    builder.getContext();
                }
            });

            builder.create();
            builder.show();
        }
        return aux;
    }


    public boolean writeViaje(Tag mytag, String contador){

        byte[] aux =  new byte[4];
        MifareUltralight mf= MifareUltralight.get(mytag);
        int z=0;
        int auxPages =0;
        try {
            mf.connect();
            if (contador.length() != 4) {
                int c = 4 - contador.length();
                while (c != 0) {
                    contador = "0" + contador;
                    c--;
                }

            }

            byte[] value = contador.getBytes();

            mf.writePage(7, value);
            mf.close();
            return true;
        }catch (Exception e){
           // e.printStackTrace();
            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Se perdió la conexión con el TAG");
            builder.setMessage("Intenta de nuevo.");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    builder.getContext();
                }
            });

            builder.create();
            builder.show();
            return false;
        }
    }

    public boolean cleanTag(Tag nfc){
        MifareUltralight mf=MifareUltralight.get(nfc);

        byte[] value = new byte[4];
        try{
            mf.connect();
            for (int x = 0; x < 4; x++) {
                value[x] = 0;
            }
            for (int x=8; x<=39;x++) {
                mf.writePage(x,value);
            }

            mf.close();
            return true;
        }catch(Exception e){
           // e.printStackTrace();
            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Se perdió la conexión con el TAG");
            builder.setMessage("Intenta de nuevo.");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    builder.getContext();
                }
            });

            builder.create();
            builder.show();
            return false;
        }
    }

    public static String byteArrayToHexString(byte[] byteArray){
        return String.format("%0" + (byteArray.length * 2) + "X", new BigInteger(1,byteArray));
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
    String concatenar(String idCamion, String idProyecto){
        String resultado="";
        String aux =idCamion;
        String aux1=idProyecto;
        for(int i=idCamion.length(); i<4;i++){
            aux= 0 + aux;
        }
        for(int i=idProyecto.length(); i<4;i++){
            aux1= 0 + aux1;
        }
        resultado= aux+aux1;
        return resultado;
    }
}
