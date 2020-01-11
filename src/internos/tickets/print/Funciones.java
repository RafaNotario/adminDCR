/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package internos.tickets.print;

import com.toedter.calendar.JDateChooser;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JPanel;

/**
 *
 * @author A. Rafael Notario
 */


public class Funciones {

 SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");//HACE PRUEBAS PARA DATETIME HH:MM:SS
  SimpleDateFormat formatoPrueba = new SimpleDateFormat("dd-MM-yyyy");
  
public void setBorder(JPanel name, String title) {
           name.setBorder(javax.swing.BorderFactory.createTitledBorder(null, title, javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Tahoma", 1, 18))); // NOI18N
}
    /**
     * @param args the command line arguments
     */
    
 //****USO DE FECHAS
public String getFecha(JDateChooser jd){
    if(jd.getDate()!= null){
        return formato.format(jd.getDate());
    }else{
        return null;
    }
}//getFecha

public Date StringDate(String fecha){//tenia: java.util.Date
//    SimpleDateFormat formato_texto = new SimpleDateFormat("dd/MM/yyyy");
    Date fechaE = null;
    try{
        fechaE = formato.parse(fecha);
        return fechaE;
    }catch(ParseException ex){
        return null;
    }
}
        public String volteaFecha(String cad,int opc){
            //cad = "16/06/2017";
            char var[];
            var = cad.toCharArray();
            String p1 ="",p2="",p3="",newFech="";
            
            if(opc == 0)
            {
                for(int i = 0; i< var.length;i++){
                    if(i<2)
                        p1+=var[i];
                
                    if(i>2 && i<5)
                        p2+=var[i];
                
                    if(i>5 && i<var.length)
                        p3+=var[i];
                }
            }

            if(opc == 1)
            {
                for(int i = 0;i<var.length;i++){
                    if(i<4)
                        p1+=var[i];
                    
                    if(i>4 && i<7)
                        p2+=var[i];
                    
                    if(i>7 && i<var.length)
                        p3+=var[i];
                }
            }           
            newFech=p3+"/"+p2+"/"+p1;
            return newFech;
        }

    public static void main(String[] args) {
        ticketMuestra tM = new ticketMuestra();
        tM.imprimirFactura();
        
    }
    
}
