
package controllers.altadeclientes;

import java.io.File;
import java.io.InputStream;
import internos.tickets.print.Funciones;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HiloLector extends Thread{
    private final InputStream is;
    Funciones fn =  new Funciones();
    public HiloLector(InputStream is){
        this.is = is;
    }
    @Override
    public void run(){
       File errores = new File("C:/adminDCR/ERRCargaDB/errorAutoGenerateAdminDCR" + fn.setDateActualGuion()+"H"+fn.getHour().replace(':', '-') + ".txt");
        try (FileWriter fw = new FileWriter(errores)) {
            byte[] buffer = new byte[1000];
            int leido = is.read(buffer);
            while (leido > 0) {
                String texto = new String(buffer,0,leido);
                fw.write(texto + "\n");
                leido = is.read(buffer);
            }
        } catch (Exception e){
            System.err.println(e.toString());
        }
    }
    
}
