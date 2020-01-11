/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package internos.tickets.print.tickets2;

public class OrderTotal{
    
    char[] temp=new char[] { ' ' };
    public OrderTotal(char delimit){temp=new char[]{delimit};}
    
    public String GetTotalNombre(String totalItem){
        String[] delimitado=totalItem.split(""+temp);
        return delimitado[0];
    }

    public String GetTotalCantidad(String totalItem){
        String[] delimitado=totalItem.split(""+temp);

        return delimitado[1];
    }

    public String GeneraTotal(String Nombre, String precio){
        return Nombre+temp[0]+temp[0]+temp[0]+temp[0]+precio;
    }

}
