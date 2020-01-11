/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package internos.tickets.print.tickets2;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author monit
 */
public class Imprime {

    /**
     * @param args the command line arguments
     */
public void imprimir(){

    try{

        Date date=new Date();

        SimpleDateFormat fecha=new SimpleDateFormat("dd/MM/yyyy");

        SimpleDateFormat hora=new SimpleDateFormat("hh:mm:ss aa");

        Ticket ticket = new Ticket();

        ticket.AddCabecera("             SANDALS RESTAURANT");

        ticket.AddCabecera(ticket.DarEspacio());

        ticket.AddCabecera("     tlf: 222222  r.u.c: 22222222222");

        ticket.AddCabecera(ticket.DarEspacio());

        ticket.AddSubCabecera(ticket.DibujarLinea(40));

        ticket.AddSubCabecera(ticket.DarEspacio());

        ticket.AddSubCabecera("     Ticket Factura No:'003-000011'");

        ticket.AddSubCabecera(ticket.DarEspacio());

        ticket.AddSubCabecera("        "+fecha.format(date) + " " + hora.format(date));

        ticket.AddSubCabecera(ticket.DarEspacio());

        ticket.AddSubCabecera("         Mesa "+"3"+" Mozo "+"Martin"+" Pers "+"5");
//        ticket.AddSubCabecera("         Mesa "+jTextField7.getText()+" Mozo "+jTextField8.getText()+" Pers "+jTextField9.getText());

        ticket.AddSubCabecera(ticket.DarEspacio());

        ticket.AddSubCabecera(ticket.DibujarLinea(40));

        ticket.AddSubCabecera(ticket.DarEspacio());

        ticket.AddSubCabecera("cant      producto         p.u     total");

        ticket.AddSubCabecera(ticket.DarEspacio());

        ticket.AddSubCabecera(ticket.DibujarLinea(40));

        ticket.AddSubCabecera(ticket.DarEspacio());

       for(int y=0;y<4;y++){

           //cantidad de decimales for(int y=0;y<jTable1.getRowCount();y++){

           NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);

           DecimalFormat form = (DecimalFormat)nf;

           form.applyPattern("#,###.00");

           //cantidad

           String cantidad="4";
//String cantidad=jTable1.getValueAt(y,0).toString();
           if(cantidad.length()<4){

               int cant=4-cantidad.length();String can="";

               for(int f=0;f<cant;f++){can+=" ";}cantidad+=can;

           }

            //items

            String item="CANELITAS";

            if(item.length()>17){item=item.substring(0,16)+".";}

            else{

                int c=17-item.length();String comple="";

                for(int y1=0;y1<c;y1++){comple+=" ";}item+=comple;

            }

            //precio

            String precio="500";

            double pre1=Double.parseDouble(precio);

            precio=form.format(pre1);

            if(precio.length()<8){

                int p=8-precio.length();String pre="";

                for(int y1=0;y1<p;y1++){pre+=" ";}precio=pre+precio;

            }

            //total

            String total="500";//jTable1.getValueAt(y,3).toString()

            total=form.format(Double.parseDouble(total));

            if(total.length()<8){

                int t=8-total.length();String tota="";

                for(int y1=0;y1<t;y1++){tota+=" ";}total=tota+total;

            }

            //agrego los items al detalle

            ticket.AddItem(cantidad,item,precio,total);

            //ticket.AddItem("","","",ticket.DarEspacio());

        }

        ticket.AddItem(ticket.DibujarLinea(40),"","","");

        ticket.AddTotal("",ticket.DarEspacio());

        ticket.AddTotal("total                   ","1500");//jTextField1.getText()

        ticket.AddTotal("",ticket.DarEspacio());

        ticket.AddTotal("Igv                     ","4567");//jTextField2.getText()

        ticket.AddTotal("",ticket.DarEspacio());

        ticket.AddTotal("total venta             ","5000");//jTextField3.getText()

        ticket.AddTotal("",ticket.DarEspacio());

        ticket.AddTotal("paga con                ","TARJETA");//jTextField4.getText()

        ticket.AddTotal("",ticket.DarEspacio());

        ticket.AddTotal("vuelto                  ","25");//jTextField5.getText()

        ticket.AddPieLinea(ticket.DarEspacio());

        ticket.AddPieLinea("Gracias por su Preferencia");

        ticket.ImprimirDocumento("crea.txt",false);

    }catch(Exception e){System.out.print("\nerror "+e.toString());}

}
    
    public static void main(String[] args) {
        Imprime prime = new Imprime();
        prime.imprimir();
    }  
}
