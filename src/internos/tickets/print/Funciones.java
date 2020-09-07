/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package internos.tickets.print;

import com.toedter.calendar.JDateChooser;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;

/**
 *
 * @author A. Rafael Notario Rodriguez
 */

public class Funciones {

 SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");//HACE PRUEBAS PARA DATETIME HH:MM:SS
  SimpleDateFormat formatoPrueba = new SimpleDateFormat("dd-MM-yyyy");
      SimpleDateFormat formato2 = new SimpleDateFormat("yyyy/MM/dd");
  //VARIABLES PARA CALCULO DE DINERO  
  private static int DECIMALS = 1;
  private static int ROUNDING_MODE = BigDecimal.ROUND_HALF_EVEN;
  private BigDecimal fAmountOne;
  private BigDecimal fAmountTwo;
  public static final BigDecimal ONE_HUNDRED = new BigDecimal(100);

 
public void setBorder(JPanel name, String title) {
           name.setBorder(javax.swing.BorderFactory.createTitledBorder(null, title, javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Tahoma", 1, 18))); // NOI18N
}
    /**
     * @param args the command line arguments
     */
    
 //****USO DE FECHAS
    public String setDateActual(){
    //        DateFormat df = DateFormat.getDateInstance();
        Date fechaAct = new Date();   
    //        jDateChooser1.setDate(fechaAct);
            return formato2.format(fechaAct);
    }

    public String setDateActualGuion(){
    //        DateFormat df = DateFormat.getDateInstance();
        Date fechaAct = new Date();    
    //        jDateChooser1.setDate(fechaAct);
            return formatoPrueba.format(fechaAct);
    }


public String getFecha(JDateChooser jd){
    if(jd.getDate()!= null){
        return formato.format(jd.getDate());
    }else{
        return null;
    }
}//getFecha

   public Date cargafecha() {
        Date fechaAct = new Date();
        return fechaAct;
    }
   
public Date StringDate(String fecha){//tenia: java.util.Date
//    SimpleDateFormat formato_texto = new SimpleDateFormat("dd/MM/yyyy");
    Date date1 = null;  
     try {
         date1 = new SimpleDateFormat("dd/MM/yyyy").parse(fecha);
     } catch (ParseException ex) {
         Logger.getLogger(Funciones.class.getName()).log(Level.SEVERE, null, ex);
     }
        return date1;
}//StringDate

public Date stringDateTime(String fecha){//tenia: java.util.Date
//    SimpleDateFormat formato_texto = new SimpleDateFormat("dd/MM/yyyy");
    java.util.Date fechaE = null;
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
        }//volteaFecha
        
public String getSumFechDay(String fech, int nDays){
        Date prox = null;
        Calendar cal = Calendar.getInstance(); 
//obtenemos la fecha actual y la convertmos a date
        cal.setTime(StringDate(fech));
        cal.add(Calendar.DAY_OF_YEAR, nDays);
        prox=cal.getTime();
        return formato.format(prox);
    }

             //CALCULO DE DINERO $$ ***
     private BigDecimal rounded(BigDecimal aNumber){
            return aNumber.setScale(DECIMALS, ROUNDING_MODE);
     }
     
     public BigDecimal multiplicaAmount(BigDecimal aAmountOne, BigDecimal aAmountTwo){
            fAmountOne = rounded(aAmountOne);
            fAmountTwo = rounded(aAmountTwo);
        //    System.out.println(fAmountOne+" -> "+fAmountTwo);
        return fAmountOne.multiply(fAmountTwo);
    }
     
      public BigDecimal getDifference(BigDecimal aAmountOne, BigDecimal aAmountTwo){
            fAmountOne = rounded(aAmountOne);
            fAmountTwo = rounded(aAmountTwo);
        return fAmountOne.subtract(fAmountTwo);
    }
      public BigDecimal getSum(BigDecimal aAmountOne, BigDecimal aAmountTwo){
          fAmountOne = rounded(aAmountOne);
            fAmountTwo = rounded(aAmountTwo);
          return fAmountOne.add(fAmountTwo);
      }
      
     public BigDecimal percentage(BigDecimal base, BigDecimal pct){
         fAmountOne = rounded(base);
            fAmountTwo = rounded(pct);
       return fAmountOne.multiply(fAmountTwo).divide(ONE_HUNDRED);
    }
     
    public BigDecimal divideAmount(BigDecimal aAmountOne, BigDecimal aAmountTwo){
            fAmountOne = rounded(aAmountOne);
            fAmountTwo = rounded(aAmountTwo);
        //    System.out.println(fAmountOne+" -> "+fAmountTwo);
        return fAmountOne.divide(fAmountTwo,1,ROUNDING_MODE);
    }
        
public String getHour(){
    Calendar calendario = Calendar.getInstance();    
    int hora, minutos, segundos;
    hora =calendario.get(Calendar.HOUR_OF_DAY);
    minutos = calendario.get(Calendar.MINUTE);
    segundos = calendario.get(Calendar.SECOND);
    String hor = Integer.toString(hora)+":"+Integer.toString(minutos)+":"+Integer.toString(segundos);
    return hor;
}

    public static void main(String[] args) {
                 Funciones dC = new Funciones();
//        System.out.println("Semana del año es: "+dC.semanYear("2020/04/13",0)+"\t dia de la semana: "+dC.semanYear("2020/04/13",1));
     System.out.println(dC.getSumFechDay("01/04/2020",-1));
    }
    
}
