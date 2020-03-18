/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.altadeclientes;
import conexiones.db.ConexionDBOriginal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import sun.applet.Main;
import internos.tickets.print.Funciones;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.sql.Date;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import javax.swing.JFileChooser;

/**
 *
 * @author A. Rafael Notario 
 */
public class controladorCFP {
    Funciones func = new Funciones();
    ConexionDBOriginal con2 = new ConexionDBOriginal();
    
    public void insertaCampos(String tabla, List<String> param){
            Connection cn = con2.conexion();
            PreparedStatement pps=null;
            String SQL="";        
        switch (tabla){
            case "proveedor":
                SQL="INSERT INTO proveedor (nombreP,apellidosP,localidadP,fechaAltaP,telefonoP,tipo) VALUES (?,?,?,?,?,?)";                           
            try {
                pps = cn.prepareStatement(SQL);
                pps.setString(1, param.get(0));
                pps.setString(2, param.get(1));
                pps.setString(3, param.get(2));
                pps.setString(4, param.get(3));
                pps.setString(5, param.get(4));               
                pps.setInt(6, Integer.parseInt(param.get(6)));               
                pps.executeUpdate();
                JOptionPane.showMessageDialog(null, "Datos de Proveedor guardados correctamente.");
            } catch (SQLException ex) {
                Logger.getLogger(controladorCFP.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, "Error durante la transaccion.");
            }finally{
 //               System.out.println( "cierra conexion a la base de datos" );    
                try {
                    if(pps != null) pps.close();                
                    if(cn !=null) cn.close();
                    } catch (SQLException ex) {
                     JOptionPane.showMessageDialog(null,ex.getMessage() );    
                    }
            }//finally catch
                break;//case :proveedor
            case "clientepedidos":  
                SQL="INSERT INTO clientepedidos (nombre,apellidos,localidad,fechaAlta,telefono,rfc) VALUES (?,?,?,?,?,?)";            
            try {
                pps = cn.prepareStatement(SQL);
                pps.setString(1, param.get(0));
                pps.setString(2, param.get(1));
                pps.setString(3, param.get(2));
                pps.setString(4, param.get(3));
                pps.setString(5, param.get(4));
                pps.setString(6, param.get(5));
                pps.executeUpdate();
                JOptionPane.showMessageDialog(null, "Datos de Cliente guardados correctamente.");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error durante la transaccion.");
                Logger.getLogger(controladorCFP.class.getName()).log(Level.SEVERE, null, ex);
                
            }finally{
 //               System.out.println( "cierra conexion a la base de datos" );    
                try {
                    if(pps != null) pps.close();                
                    if(cn !=null) cn.close();
                    } catch (SQLException ex) {
                      JOptionPane.showMessageDialog(null, ex.getMessage() );    
                    }                
            }//finally catch                
                break;//case:clientepedidos
           case "empleado":  
                SQL="INSERT INTO empleado (nombreE,apellidosE,domicilioE,fechaAltaE,telE) VALUES (?,?,?,?,?)";            
            try {
                pps = cn.prepareStatement(SQL);
                pps.setString(1, param.get(0));
                pps.setString(2, param.get(1));
                pps.setString(3, param.get(2));
                pps.setString(4, param.get(3));
                pps.setString(5, param.get(4));
                pps.executeUpdate();
                JOptionPane.showMessageDialog(null, "Datos de Empleado guardados correctamente.");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error durante la transaccion.");
                Logger.getLogger(controladorCFP.class.getName()).log(Level.SEVERE, null, ex);               
            }finally{
 //               System.out.println( "cierra conexion a la base de datos" );    
                try {
                    if(pps != null) pps.close();                
                    if(cn !=null) cn.close();
                    } catch (SQLException ex) {
                      JOptionPane.showMessageDialog(null, ex.getMessage() );    
                    }                
            }//finally catch                
                break;//case:empleado
           case "fletero":  
                SQL="INSERT INTO fletero (nombreF,localidadF,fechaAltaF,telefonoF) VALUES (?,?,?,?)";            
            try {
                pps = cn.prepareStatement(SQL);
                pps.setString(1, param.get(0));
                pps.setString(2, param.get(2));
                pps.setString(3, param.get(3));
                pps.setString(4, param.get(4));
                pps.executeUpdate();
                JOptionPane.showMessageDialog(null, "Datos de Fletero guardados correctamente.");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error durante la transaccion.");
                Logger.getLogger(controladorCFP.class.getName()).log(Level.SEVERE, null, ex);               
            }finally{
 //               System.out.println( "cierra conexion a la base de datos" );    
                try {
                    if(pps != null) pps.close();                
                    if(cn !=null) cn.close();
                    } catch (SQLException ex) {
                      JOptionPane.showMessageDialog(null, ex.getMessage() );    
                    }                
            }//finally catch                
                break;//case:empleado
            default:                
                break;
        };
 }//metodo insertaCampos
    
    
    public List regresaDatos(int opc,String param){
        Connection cn = con2.conexion();
          List<String> contentL=new ArrayList<String>();
           String sql ="";
           if(opc ==0){
              sql = "SELECT * FROM clientepedidos WHERE id_cliente = '"+param+"'";           
           }
           if(opc ==1){
              sql = "SELECT * FROM proveedor WHERE id_Proveedor = '"+param+"'";           
           }
           if(opc ==2){
              sql = "SELECT * FROM empleado WHERE id_Empleado = '"+param+"'";           
           }
           if(opc ==3){
              sql = "SELECT * FROM fletero WHERE id_Fletero = '"+param+"'";           
           }
            Statement st = null;
            ResultSet rs = null;            
            try {
                st = cn.createStatement();
                rs = st.executeQuery(sql);
                while(rs.next())
                {//es necesario el for para llenar dinamicamente la lista, ya que varia el numero de columnas de las tablas
                        for (int x=1;x<= rs.getMetaData().getColumnCount();x++) {
                            contentL.add(rs.getString(x));
                     // System.out.println(x+" -> "+rs.getString(x));                   
                        }//for

                }//while
            } catch (SQLException ex) {
                Logger.getLogger(controladorCFP.class.getName()).log(Level.SEVERE, null, ex);
            }finally{               
//             System.out.println("cierra conexion a la base de datos");    
             try {        
                 if(st != null) st.close();                
                 if(cn !=null) cn.close();
             } catch (SQLException ex) {
                 JOptionPane.showMessageDialog(null,ex.getMessage()); 
             }
         }//finally  
            return contentL;
    }//regresaDatos
             
    public void actualizaData(String tabla, List<String> param,String id){
             Connection cn = con2.conexion();
            PreparedStatement pps=null;
            String SQL="";        
        switch (tabla){
            case "proveedor":
                SQL="UPDATE proveedor SET nombreP =?, apellidosP=?, localidadP=?, statusP=?, fechaAltaP=?, telefonoP=?,tipo=? WHERE id_Proveedor = '"+id+"' ";                           
            try {
                pps = cn.prepareStatement(SQL);
                pps.setString(1, param.get(0));
                pps.setString(2, param.get(1));
                pps.setString(3, param.get(2));
                pps.setInt(4, Integer.parseInt(param.get(3)));
                pps.setString(5, param.get(4));
                pps.setString(6, param.get(5));
                pps.setInt(7,Integer.parseInt(param.get(7)));
                pps.executeUpdate();
                JOptionPane.showMessageDialog(null, "Datos de Proveedor actualizados correctamente.");
            } catch (SQLException ex) {
                Logger.getLogger(controladorCFP.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, "Error durante la transaccion.");
            }finally{
 //               System.out.println( "cierra conexion a la base de datos" );    
                try {
                    if(pps != null) pps.close();                
                    if(cn !=null) cn.close();
                    } catch (SQLException ex) {
                     JOptionPane.showMessageDialog(null,ex.getMessage() );    
                    }
            }//finally catch
                break;//case :proveedor
            case "clientepedidos":  
                SQL="UPDATE clientepedidos SET nombre=?,apellidos=?,localidad=?,status=?,fechaAlta=?,telefono=?,rfc=? WHERE id_cliente = '"+id+"' ";            
            try {
                pps = cn.prepareStatement(SQL);
                pps.setString(1, param.get(0));
                pps.setString(2, param.get(1));
                pps.setString(3, param.get(2));
                pps.setInt(4, Integer.parseInt(param.get(3)));
                pps.setString(5, param.get(4));
                pps.setString(6, param.get(5));
                pps.setString(7, param.get(6));
                
                pps.executeUpdate();
                JOptionPane.showMessageDialog(null, "Datos de Cliente actualizados correctamente.");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error durante la transaccion.");
                Logger.getLogger(controladorCFP.class.getName()).log(Level.SEVERE, null, ex);
                
            }finally{
 //               System.out.println( "cierra conexion a la base de datos" );    
                try {
                    if(pps != null) pps.close();                
                    if(cn !=null) cn.close();
                    } catch (SQLException ex) {
                      JOptionPane.showMessageDialog(null, ex.getMessage() );    
                    }                
            }//finally catch                
                break;//case:clientepedidos
           case "empleado":  
                SQL="UPDATE empleado SET nombreE=?,apellidosE=?,domicilioE=?,statusE=?,fechaAltaE=?,telE=? WHERE id_Empleado ='"+id+"'";            
            try {
                pps = cn.prepareStatement(SQL);
                pps.setString(1, param.get(0));
                pps.setString(2, param.get(1));
                pps.setString(3, param.get(2));
                pps.setInt(4, Integer.parseInt(param.get(3)));
                pps.setString(5, param.get(4));
                pps.setString(6, param.get(5));
                pps.executeUpdate();
                JOptionPane.showMessageDialog(null, "Datos de Empleado actualizados correctamente.");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error durante la transaccion.");
                Logger.getLogger(controladorCFP.class.getName()).log(Level.SEVERE, null, ex);               
            }finally{
 //               System.out.println( "cierra conexion a la base de datos" );    
                try {
                    if(pps != null) pps.close();                
                    if(cn !=null) cn.close();
                    } catch (SQLException ex) {
                      JOptionPane.showMessageDialog(null, ex.getMessage() );    
                    }                
            }//finally catch                
                break;//case:empleado
           case "fletero":  
                SQL="UPDATE fletero SET nombreF=?,localidadF=?,statusF=?,fechaAltaF=?,telefonoF=? WHERE id_Fletero ='"+id+"' ";            
            try {
                pps = cn.prepareStatement(SQL);
                pps.setString(1, param.get(0));
                pps.setString(2, param.get(2));
                pps.setInt(3, Integer.parseInt(param.get(3)));
                pps.setString(4, param.get(4));
                pps.setString(5, param.get(5));
                pps.executeUpdate();
                JOptionPane.showMessageDialog(null, "Datos de Fletero actualizados correctamente.");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error durante la transaccion.");
                Logger.getLogger(controladorCFP.class.getName()).log(Level.SEVERE, null, ex);               
            }finally{
 //               System.out.println( "cierra conexion a la base de datos" );    
                try {
                    if(pps != null) pps.close();                
                    if(cn !=null) cn.close();
                    } catch (SQLException ex) {
                      JOptionPane.showMessageDialog(null, ex.getMessage() );    
                    }                
            }//finally catch                
                break;//case:empleado
            default:                
                break;
        };
    }//actualizaData
    
///CODIGO PARA GUARDAR PEDIDO
public void guardaPedidoCli(List<String> param){
     Connection cn = con2.conexion();
            PreparedStatement pps=null;
            String SQL="";        

                SQL="INSERT INTO pedidocliente (id_clienteP,fechaPedidio,notaPed) VALUES (?,?,?)";                           
            try {
                pps = cn.prepareStatement(SQL);
                pps.setString(1, param.get(0));
                pps.setString(2,param.get(1));
                pps.setString(3,param.get(2));
                pps.executeUpdate();
                JOptionPane.showMessageDialog(null, "Pedido guardado correctamente.");
            } catch (SQLException ex) {
                Logger.getLogger(controladorCFP.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, "Error durante la transaccion.");
            }finally{
 //               System.out.println( "cierra conexion a la base de datos" );    
                try {
                    if(pps != null) pps.close();                
                    if(cn !=null) cn.close();
                    } catch (SQLException ex) {
                     JOptionPane.showMessageDialog(null,ex.getMessage() );    
                    }
            }//finally catch
    
} //guardaPedidoCli 
    
public String[] ultimoRegistroPedido(){
        Connection cn = con2.conexion();
        String[] arre = new String[2];
        String ultimo="",consul="";
        int num=0,i=1;
        String sql = "SELECT id_pedido,id_clienteP FROM pedidocliente ORDER BY id_pedido DESC LIMIT 1";
        Statement st = null;
        ResultSet rs= null;
        try {
            st = cn.createStatement();
            rs = st.executeQuery(sql);
            rs.beforeFirst();
            while(rs.next())
            {
                arre[0] = rs.getString(1);
                arre[1] = rs.getString(2);
               // System.out.println(consul+" - "+ultimo);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Funciones.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
                    try {
                        if(cn != null) cn.close();
                    } catch (SQLException ex) {
                        System.err.println( ex.getMessage() );    
                    }
                }
        return arre;
    }

public void guardaDetallePedidoCli(String idP,String codP, int cantP){
     Connection cn = con2.conexion();
            PreparedStatement pps=null;
            String SQL="";        

                SQL="INSERT INTO detailpedidio (id_pedidioD,codigoProdP,cantidadCajas) VALUES (?,?,?)";                           
            try {
                pps = cn.prepareStatement(SQL);
                pps.setString(1, idP);
                pps.setString(2,codP);
                pps.setInt(3,cantP);
                pps.executeUpdate();
                JOptionPane.showMessageDialog(null, "Pedido guardado correctamente");
            } catch (SQLException ex) {
                Logger.getLogger(controladorCFP.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, "Error durante la transaccion.");
            }finally{
 //               System.out.println( "cierra conexion a la base de datos" );    
                try {
                    if(pps != null) pps.close();                
                    if(cn !=null) cn.close();
                    } catch (SQLException ex) {
                     JOptionPane.showMessageDialog(null,ex.getMessage() );    
                    }
            }//finally catch
} //guardaPedidoCli 

/****** CODIGO PENDIENTE PATA FORMAR PEDIDOS DEL DIA
    public void matrizPedidos(){
        Connection cn = con2.conexion();
          List<String> idPedido=new ArrayList<String>();
          List<String> idCliente=new ArrayList<String>();
          HashSet<String> hs = new HashSet<String>();
          String sql ="",aux;
              sql = "SELECT\n" +
"	clientePedidos.id_cliente,\n" +
"	pedidoCliente.id_pedido,\n" +
"	detailPedidio.codigoProdP,detailPedidio.cantidadCajas\n" +
"	\n" +
"FROM\n" +
"	clientePedidos\n" +
"INNER JOIN\n" +
"	pedidoCliente\n" +
"ON\n" +
"	clientePedidos.id_cliente = pedidoCliente.id_clienteP AND pedidoCliente.fechaPedidio = '2020-01-09'\n" +
"INNER JOIN\n" +
"	detailPedidio \n" +
"ON\n" +
"	pedidoCliente.id_pedido = detailPedidio.id_PedidioD \n" +
"INNER JOIN\n" +
"	productoCal\n" +
"ON\n" +
"	detailPedidio.codigoProdP = productoCal.codigo ORDER BY pedidoCliente.id_pedido;;" ;           
             int i =0,cantFilas=0, cont=1;
             String[][] mat=null, mat2=null;
              int[] arrIdPedido = null;//int para usar hashMap
            Statement st = null;
            ResultSet rs = null;            
            try {
                st = cn.createStatement();
                rs = st.executeQuery(sql);
                int cantColumnas = rs.getMetaData().getColumnCount();
               if(rs.last()){//Nos posicionamos al final
                    cantFilas = rs.getRow();//sacamos la cantidad de filas/registros
                    rs.beforeFirst();//nos posicionamos antes del inicio (como viene por defecto)
                }
               arrIdPedido = new int[cantFilas];
               mat = new String[cantFilas][cantColumnas];
               //aqui iria crear matriz
                while(rs.next())
                {//es necesario el for para llenar dinamicamente la lista, ya que varia el numero de columnas de las tablas
                    idCliente.add(rs.getString(1));
                    idPedido.add(rs.getString(2));
                    arrIdPedido[i]=rs.getInt(2);//LLenamos de enteros
                   System.out.println(" 2->  "+rs.getInt(2));     
                      for (int x=1;x<= rs.getMetaData().getColumnCount();x++) {
                           // System.out.print("| "+rs.getString(x)+" |");
                             mat[i][x-1]=rs.getString(x);
                     // System.out.println(x+" -> "+rs.getString(x));                   
                      }//for
                       System.out.println();
                       i++;
                }//while
                hs.addAll(idPedido);//obtenemos numero de filas con los elementos repetidos


                System.out.println("Elementos o pedidos: "+hs.size());
            } catch (SQLException ex) {
                Logger.getLogger(controladorCFP.class.getName()).log(Level.SEVERE, null, ex);
            }finally{               
//             System.out.println("cierra conexion a la base de datos");    
             try {        
                 if(st != null) st.close();                
                 if(cn !=null) cn.close();
             } catch (SQLException ex) {
                 JOptionPane.showMessageDialog(null,ex.getMessage()); 
             }
         }//finally  
            //YA TENEMOS LAS MATRICES LLENAS
            
            mat2 = new String[hs.size()][8];
            for (int j = 0; j < mat2.length; j++) {
                for (int k = 0; k < mat2[0].length; k++) {
                    mat2[j][k]="RAFA";
                }
        }
     System.out.println("mat1");
            for (int j = 0; j < mat.length; j++) {
                for (int k = 0; k < mat[0].length; k++) {
                    System.out.print("[ "+mat[j][k]+" ]");
                }
                System.out.println();
        }
     System.out.println("mat2");
            for (int j = 0; j < mat2.length; j++) {
                for (int k = 0; k < mat2[0].length; k++) {
                    System.out.print("[ "+mat2[j][k]+" ]");
                }
                System.out.println();
        }
            
            //Prueba hashMap
            System.out.println("Prueba hashMap");
            repetidos(arrIdPedido);
            
    }//matrizPedidos
             
    public void repetidos(int[] a){
        HashMap<Integer, Integer> hashMapEdad = new HashMap<Integer, Integer>();      
       //   int[] a = { 1, 1, 2, 3, 5, 8, 13, 13 };
        List<Integer> list = Arrays.stream(a).boxed().collect(Collectors.toList());
        for (Integer ch : list) 
            hashMapEdad.put(ch,Collections.frequency(list, ch));
            //System.out.println(ch + " :  " + Collections.frequency(list, ch));           
        //    for(Integer key: hashMapEdad.keySet()){
                //System.out.println(key + " tiene " + hashMapEdad.get(key) + " años de edad.");
                //}
  TreeMap<Integer, Integer> treeMap = new TreeMap<Integer, Integer>(hashMapEdad);
for (Integer key : treeMap.keySet()) {
 System.out.println(key + " tiene " + treeMap.get(key) + " años de edad.");
    }  
}
    *//////////

//CREAR PEDIDOS CON VISTA DE DETALLES EN TABLA
     public String[][] consultPedidoAsign(String opc){
        Connection cn = con2.conexion();
        int cantColumnas=0,cantFilas=0;
        String[][] arre=null;
        String sql ="";
              sql = "SELECT id_pedido,id_clienteP FROM pedidocliente WHERE fechaPedidio = '"+opc+"'";           
            Statement st = null;
            ResultSet rs = null;            
            int i =0;
            try {
                st = cn.createStatement();
                rs = st.executeQuery(sql);
                
            cantColumnas = rs.getMetaData().getColumnCount();
               if(rs.last()){//Nos posicionamos al final
                    cantFilas = rs.getRow();//sacamos la cantidad de filas/registros
                    rs.beforeFirst();//nos posicionamos antes del inicio (como viene por defecto)
                }     
               arre = new String[cantFilas][cantColumnas];
                while(rs.next())
                {//es necesario el for para llenar dinamicamente la lista, ya que varia el numero de columnas de las tablas
                        //for (int x=1;x<= rs.getMetaData().getColumnCount()-1;x++) {
                          //      if(x==3){
//                                    if(rs.getInt(x)==1){
                       arre[i][0]=rs.getString(1);
                       arre[i][1]=rs.getString(2);
                            //    }
                            //System.out.print("["+x+"]"+" -> "+rs.getString(x));                   
                        //}//for
                            //System.out.println(rs.getString(1));
                          //  consultDetailCompra(rs.getInt(1));
                      i++;      
                }//while
            } catch (SQLException ex) {
                Logger.getLogger(controladorCFP.class.getName()).log(Level.SEVERE, null, ex);
            }finally{               
             try {        
                 if(st != null) st.close();                
                 if(cn !=null) cn.close();
             } catch (SQLException ex) {
                 JOptionPane.showMessageDialog(null,ex.getMessage()); 
             }
         }//finally  
            return arre;
    }//regresaDatosPedido
     
        public String[][] matrizPedidos(String fech){
        Connection cn = con2.conexion();
          String sql ="",aux;
              sql = "SELECT\n" +
                                    "	pedidocliente.id_pedido,pedidocliente.fechaPedidio,IF(pedidocliente.status = 0,'PENDIENTE','PAGADO'),\n" +
                                    "	clientepedidos.id_cliente,clientepedidos.nombre,\n" +
                                    "	SUM(detailpedidio.cantidadCajas),pedidocliente.totalPrecio,\n" +
                                    "	pedidocliente.notaPed\n" +
                                    "FROM\n" +
                                    "	clientepedidos\n" +
                                    "INNER JOIN\n" +
                                    "	pedidocliente\n" +
                                    "ON\n" +
                                    "	clientePedidos.id_cliente = pedidocliente.id_clienteP AND pedidocliente.fechaPedidio = '"+fech+"'\n" +
                                    "INNER JOIN \n" +
                                    "	detailpedidio \n" +
                                    "ON\n" +
                                    "	pedidocliente.id_pedido=detailpedidio.id_PedidioD\n" +
                                    "GROUP BY pedidocliente.id_pedido;";      
              
             int i =0,cantFilas=0, cont=1,cantColumnas=0;
             String[][] mat=null, mat2=null;
              int[] arrIdPedido = null;//int para usar hashMap
            Statement st = null;
            ResultSet rs = null;            
            try {
                st = cn.createStatement();
                rs = st.executeQuery(sql);
                cantColumnas = rs.getMetaData().getColumnCount();
               if(rs.last()){//Nos posicionamos al final
                    cantFilas = rs.getRow();//sacamos la cantidad de filas/registros
                    rs.beforeFirst();//nos posicionamos antes del inicio (como viene por defecto)
                }
               mat = new String[cantFilas][cantColumnas];
               //aqui iria crear matriz
                while(rs.next())
                {//es necesario el for para llenar dinamicamente la lista, ya que varia el numero de columnas de las tablas
                 
                      for (int x=1;x<= rs.getMetaData().getColumnCount();x++) {
                           // System.out.print("| "+rs.getString(x)+" |");
                             mat[i][x-1]=rs.getString(x);
                      //System.out.print(x+" -> "+rs.getString(x));                   
                      }//for
                       i++;
                }//whilE
            } catch (SQLException ex) {
                Logger.getLogger(controladorCFP.class.getName()).log(Level.SEVERE, null, ex);
            }finally{               
//             System.out.println("cierra conexion a la base de datos");    
             try {        
                 if(st != null) st.close();                
                 if(cn !=null) cn.close();
             } catch (SQLException ex) {
                 JOptionPane.showMessageDialog(null,ex.getMessage()); 
             }
         }//finally        
           if (cantFilas == 0){
                mat=null;
                mat = new String[1][cantColumnas];
                
                for (int j = 0; j < mat[0].length; j++) {
                     mat[0][j]="NO DATA";
                }
           }
return mat;            
}

/*filtros buscar pedidos*/
        public String[][] matrizPedidosB(int opcBusq,String idCli,String fech1,String fech2){
        Connection cn = con2.conexion();
          String sql ="",aux;
          if(opcBusq==0){//OPCION BUSQUEDA DE NOMBRE
              sql = "SELECT\n" +
                                    "	pedidocliente.id_pedido,pedidocliente.fechaPedidio,"
                      + "IF(pedidocliente.status=0,'PENDIENTE','PAGADO'),\n" +
                                    "	clientepedidos.id_cliente,clientepedidos.nombre,\n" +
                                    "	SUM(detailpedidio.cantidadCajas),pedidocliente.totalPrecio,\n" +
                                    "	pedidocliente.notaPed\n" +
                                    "FROM\n" +
                                    "	clientepedidos\n" +
                                    "INNER JOIN\n" +
                                    "	pedidocliente\n" +
                                    "ON\n" +
                                    "	clientePedidos.id_cliente = pedidocliente.id_clienteP AND pedidocliente.id_clienteP = '"+idCli+"'\n" +
                                    "INNER JOIN \n" +
                                    "	detailpedidio \n" +
                                    "ON\n" +
                                    "	pedidocliente.id_pedido=detailpedidio.id_PedidioD\n" +
                                    "GROUP BY pedidocliente.id_pedido ORDER BY pedidocliente.fechaPedidio;";      
          }
          
          if(opcBusq==1){//OPCION BUSQUEDA NOMBRE Y FECHA
              sql = "SELECT\n" +
                                    "	pedidocliente.id_pedido,pedidocliente.fechaPedidio,"
                      + "IF(pedidocliente.status=0,'PENDIENTE','PAGADO'),\n" +
                                    "	clientepedidos.id_cliente,clientepedidos.nombre,\n" +
                                    "	SUM(detailpedidio.cantidadCajas),pedidocliente.totalPrecio,\n" +
                                    "	pedidocliente.notaPed\n" +
                                    "FROM\n" +
                                    "	clientepedidos\n" +
                                    "INNER JOIN\n" +
                                    "	pedidocliente\n" +
                                    "ON\n" +
                                    "	clientePedidos.id_cliente = pedidocliente.id_clienteP AND pedidocliente.fechaPedidio = '"+fech1+"'\n" +
                                    "INNER JOIN \n" +
                                    "	detailpedidio \n" +
                                    "ON\n" +
                                    "	pedidocliente.id_pedido=detailpedidio.id_PedidioD\n" +
                                    "GROUP BY pedidocliente.id_pedido;";      
          }
          if(opcBusq==2){
              sql="SELECT\n" +
                                    "	pedidocliente.id_pedido,pedidocliente.fechaPedidio,"
                      + "IF(pedidocliente.status=0,'PENDIENTE','PAGADO'),\n" +
                                    "	clientepedidos.id_cliente,clientepedidos.nombre,\n" +
                                    "	SUM(detailpedidio.cantidadCajas),pedidocliente.totalPrecio,\n" +
                                    "	pedidocliente.notaPed\n" +
                                    "FROM\n" +
                                    "	clientepedidos\n" +
                                    "INNER JOIN\n" +
                                    "	pedidocliente\n" +
                                    "ON\n" +
                                    "	clientePedidos.id_cliente = pedidocliente.id_clienteP AND pedidocliente.fechaPedidio BETWEEN '"+fech1+"' AND '"+fech2+"'\n" +
                                    "INNER JOIN \n" +
                                    "	detailpedidio \n" +
                                    "ON\n" +
                                    "	pedidocliente.id_pedido=detailpedidio.id_PedidioD\n" +
                                    "GROUP BY pedidocliente.id_pedido\n" +
                                    "ORDER BY pedidocliente.fechaPedidio;";
          }
          if(opcBusq==3){
              sql="SELECT\n" +
                                    "	pedidocliente.id_pedido,pedidocliente.fechaPedidio,"
                      + "IF(pedidocliente.status=0,'PENDIENTE','PAGADO'),\n" +
                                    "	clientepedidos.id_cliente,clientepedidos.nombre,\n" +
                                    "	SUM(detailpedidio.cantidadCajas),pedidocliente.totalPrecio,\n" +
                                    "	pedidocliente.notaPed\n" +
                                    "FROM\n" +
                                    "	clientepedidos\n" +
                                    "INNER JOIN\n" +
                                    "	pedidocliente\n" +
                                    "ON\n" +
                                    "	clientePedidos.id_cliente = pedidocliente.id_clienteP AND pedidocliente.id_clienteP ='"+idCli+"' AND\n" +
                                    "	 pedidocliente.fechaPedidio = '"+fech1+"'\n" +
                                    "INNER JOIN \n" +
                                    "	detailpedidio \n" +
                                    "ON\n" +
                                    "	pedidocliente.id_pedido=detailpedidio.id_PedidioD\n" +
                                    "GROUP BY pedidocliente.id_pedido;";
          }
          if(opcBusq ==4){
              sql="SELECT\n" +
                    "	pedidocliente.id_pedido,pedidocliente.fechaPedidio,pedidocliente.status,\n" +
                    "	clientepedidos.id_cliente,clientepedidos.nombre,\n" +
                    "	SUM(detailpedidio.cantidadCajas),pedidocliente.totalPrecio,\n" +
                    "	pedidocliente.notaPed\n" +
                    "FROM\n" +
                    "	clientepedidos\n" +
                    "INNER JOIN\n" +
                    "	pedidocliente\n" +
                    "ON\n" +
                    "	clientePedidos.id_cliente = pedidocliente.id_clienteP AND pedidocliente.id_clienteP ='"+idCli+"' AND\n" +
                    "	 pedidocliente.fechaPedidio BETWEEN '"+fech1+"' AND '"+fech2+"'\n" +
                    "INNER JOIN \n" +
                    "	detailpedidio \n" +
                    "ON\n" +
                    "	pedidocliente.id_pedido=detailpedidio.id_PedidioD\n" +
                    "GROUP BY pedidocliente.id_pedido\n" +
                    "ORDER BY pedidocliente.fechaPedidio;";
          }
          
             int i =0,cantFilas=0, cont=1;
             String[][] mat=null, mat2=null;
              int[] arrIdPedido = null;//int para usar hashMap
            Statement st = null;
            ResultSet rs = null;    
            int cantColumnas=0;
            try {
                st = cn.createStatement();
                rs = st.executeQuery(sql);
                cantColumnas = rs.getMetaData().getColumnCount();
               if(rs.last()){//Nos posicionamos al final
                    cantFilas = rs.getRow();//sacamos la cantidad de filas/registros
                    rs.beforeFirst();//nos posicionamos antes del inicio (como viene por defecto)
                }
               mat = new String[cantFilas][cantColumnas];
               //aqui iria crear matriz
                while(rs.next())
                {//es necesario el for para llenar dinamicamente la lista, ya que varia el numero de columnas de las tablas
                 
                      for (int x=1;x<= rs.getMetaData().getColumnCount();x++) {
                           // System.out.print("| "+rs.getString(x)+" |");
                             mat[i][x-1]=rs.getString(x);
                      //System.out.print(x+" -> "+rs.getString(x));                   
                      }//for
                       i++;
                }//whilE
            } catch (SQLException ex) {
                Logger.getLogger(controladorCFP.class.getName()).log(Level.SEVERE, null, ex);
            }finally{               
//             System.out.println("cierra conexion a la base de datos");    
             try {        
                 if(st != null) st.close();                
                 if(cn !=null) cn.close();
             } catch (SQLException ex) {
                 JOptionPane.showMessageDialog(null,ex.getMessage()); 
             }
         }//finally 
            
           // System.out.println("filas: "+cantFilas);
            if (cantFilas == 0){
                mat=null;
                mat = new String[1][cantColumnas];
                
                for (int j = 0; j < mat[0].length; j++) {
                     mat[0][j]="NO DATA";
                }
           }
return mat;            
}
        
///****OPERACIONES DE GUARDA PRESTAMO
        public void guardaPrestamoProv(List<String> param){
        Connection cn = con2.conexion();
            PreparedStatement pps=null;
            String SQL=""; 
        
          //  No.setFecha_inicio(new java.sql.Date(((Date) celda.getDateCellValue()).getTime()));
                SQL="INSERT INTO creditomerca (id_ProveedorF,fechaPrestamo,status,notaPrest) VALUES (?,?,?,?)";                           
            try {
                pps = cn.prepareStatement(SQL);
                pps.setString(1, param.get(0));
                pps.setString(2,param.get(1));
                pps.setInt(3,Integer.parseInt(param.get(2)));
                pps.setString(4,param.get(3));
                pps.executeUpdate();
                JOptionPane.showMessageDialog(null, "Prestamo creado correctamente.");
            } catch (SQLException ex) {
                Logger.getLogger(controladorCFP.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, "Error durante la transaccion.");
            }finally{
 //               System.out.println( "cierra conexion a la base de datos" );    
                try {
                    if(pps != null) pps.close();                
                    if(cn !=null) cn.close();
                    } catch (SQLException ex) {
                     JOptionPane.showMessageDialog(null,ex.getMessage() );    
                    }
            }//finally catch
} //guardaPrestamoProv
  
    public String[] ultimoRegistroPrestamo(){
        Connection cn = con2.conexion();
        String[] arre = new String[2];
        String ultimo="",consul="";
        int num=0,i=1;
        String sql = "SELECT num_credito,id_ProveedorF FROM creditomerca ORDER BY num_credito DESC LIMIT 1";
        Statement st = null;
        ResultSet rs= null;
        try {
            st = cn.createStatement();
            rs = st.executeQuery(sql);
            rs.beforeFirst();
            while(rs.next())
            {
                arre[0] = rs.getString(1);
                arre[1] = rs.getString(2);
               // System.out.println(consul+" - "+ultimo);
            }
        } catch (SQLException ex) {
            Logger.getLogger(controladorCFP.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
                    try {
                        if(cn != null) cn.close();
                    } catch (SQLException ex) {
                        System.err.println( ex.getMessage() );    
                    }
                }
        return arre;
    }

public void guardaDetallePrestamoProv(String numCP,String codP, int cantP,String costP){
     Connection cn = con2.conexion();
            PreparedStatement pps=null;
            String SQL="";        
                SQL="INSERT INTO detallecreditproveed (num_creditCP,codigoProdCP,cantidadCP,costoCP) VALUES (?,?,?,?)";                           
            try {
                pps = cn.prepareStatement(SQL);
                pps.setString(1, numCP);
                pps.setString(2,codP);
                pps.setInt(3,cantP);
                pps.setString(4,costP);
                pps.executeUpdate();
                JOptionPane.showMessageDialog(null, "Prestamo guardado correctamente");
            } catch (SQLException ex) {
                Logger.getLogger(controladorCFP.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, "Error durante la transaccion.");
            }finally{
//               System.out.println( "finally detail->cierra conexion a la base de datos" );    
                try {
                    if(pps != null) pps.close();                
                    if(cn !=null) cn.close();
                    } catch (SQLException ex) {
                     JOptionPane.showMessageDialog(null,ex.getMessage() );    
                    }
            }//finally catch
} //guardaDetallePrestamoProveedor 

        public String[][] matrizPrestaProv(String fech){
        Connection cn = con2.conexion();
          String sql ="",aux;
              sql = "SELECT\n" +
                    "	creditomerca.num_credito,creditomerca.fechaPrestamo,creditomerca.status,\n" +
                    "	proveedor.id_Proveedor,proveedor.nombreP,\n" +
                    "	SUM(detallecreditproveed.cantidadCP*detallecreditproveed.costoCP) AS TOTAL,\n" +
                    "	creditomerca.notaPrest\n" +
                    "FROM\n" +
                    "	proveedor\n" +
                    "INNER JOIN\n" +
                    "	creditomerca\n" +
                    "ON\n" +
                    "	proveedor.id_Proveedor = creditomerca.id_ProveedorF AND creditomerca.fechaPrestamo = '"+fech+"'\n" +
                    "INNER JOIN \n" +
                    "	detallecreditproveed\n" +
                    "ON\n" +
                    "	creditomerca.num_credito=detallecreditproveed.num_creditCP\n" +
                    "GROUP BY creditomerca.num_credito\n" +
                    "ORDER BY creditomerca.num_credito DESC;";      
              
             int i =0,cantFilas=0, cont=1,cantColumnas=0;
             String[][] mat=null, mat2=null;
              int[] arrIdPedido = null;//int para usar hashMap
            Statement st = null;
            ResultSet rs = null;            
            try {
                st = cn.createStatement();
                rs = st.executeQuery(sql);
                cantColumnas = rs.getMetaData().getColumnCount();
               if(rs.last()){//Nos posicionamos al final
                    cantFilas = rs.getRow();//sacamos la cantidad de filas/registros
                    rs.beforeFirst();//nos posicionamos antes del inicio (como viene por defecto)
                }
               mat = new String[cantFilas][cantColumnas];
               //aqui iria crear matriz
                while(rs.next())
                {//es necesario el for para llenar dinamicamente la lista, ya que varia el numero de columnas de las tablas
                      for (int x=1;x<= rs.getMetaData().getColumnCount();x++) {
                           // System.out.print("| "+rs.getString(x)+" |");
                             mat[i][x-1]=rs.getString(x);
                      //System.out.print(x+" -> "+rs.getString(x));                   
                      }//for
                       i++;
                }//whilE
            } catch (SQLException ex) {
                Logger.getLogger(controladorCFP.class.getName()).log(Level.SEVERE, null, ex);
            }finally{               
//             System.out.println("cierra conexion a la base de datos");    
             try {        
                 if(st != null) st.close();                
                 if(cn !=null) cn.close();
             } catch (SQLException ex) {
                 JOptionPane.showMessageDialog(null,ex.getMessage()); 
             }
         }//finally        
           if (cantFilas == 0){
                mat=null;
                mat = new String[1][cantColumnas];
                
                for (int j = 0; j < mat[0].length; j++) {
                     mat[0][j]="NO DATA";
                }
           }
return mat;            
}//prestaProov
        
      public String[][] matrizPrestaProvOpc(int opcBusq,String idCli,String fech1,String fech2){
        Connection cn = con2.conexion();
          String sql ="",aux;
          if(opcBusq == 0){
              sql = "SELECT\n" +
                    "	creditomerca.num_credito,creditomerca.fechaPrestamo,creditomerca.status,\n" +
                    "	proveedor.id_Proveedor,proveedor.nombreP,\n" +
                    "	SUM(detallecreditproveed.cantidadCP*detallecreditproveed.costoCP) AS TOTAL,\n" +
                    "	creditomerca.notaPrest\n" +
                    "FROM\n" +
                    "	proveedor\n" +
                    "INNER JOIN\n" +
                    "	creditomerca\n" +
                    "ON\n" +
                    "	proveedor.id_Proveedor = creditomerca.id_ProveedorF AND creditomerca.id_ProveedorF = '"+idCli+"'\n" +
                    "INNER JOIN \n" +
                    "	detallecreditproveed\n" +
                    "ON\n" +
                    "	creditomerca.num_credito=detallecreditproveed.num_creditCP\n" +
                    "GROUP BY creditomerca.num_credito\n" +
                    "ORDER BY creditomerca.num_credito DESC;";      
          }    
          if(opcBusq==1){
           sql = "SELECT\n" +
                    "	creditomerca.num_credito,creditomerca.fechaPrestamo,creditomerca.status,\n" +
                    "	proveedor.id_Proveedor,proveedor.nombreP,\n" +
                    "	SUM(detallecreditproveed.cantidadCP*detallecreditproveed.costoCP) AS TOTAL,\n" +
                    "	creditomerca.notaPrest\n" +
                    "FROM\n" +
                    "	proveedor\n" +
                    "INNER JOIN\n" +
                    "	creditomerca\n" +
                    "ON\n" +
                    "	proveedor.id_Proveedor = creditomerca.id_ProveedorF AND creditomerca.fechaPrestamo = '"+fech1+"'\n" +
                    "INNER JOIN \n" +
                    "	detallecreditproveed\n" +
                    "ON\n" +
                    "	creditomerca.num_credito=detallecreditproveed.num_creditCP\n" +
                    "GROUP BY creditomerca.num_credito\n" +
                    "ORDER BY creditomerca.num_credito DESC;";
          }
          if(opcBusq==2){
           sql = "SELECT\n" +
                    "	creditomerca.num_credito,creditomerca.fechaPrestamo,creditomerca.status,\n" +
                    "	proveedor.id_Proveedor,proveedor.nombreP,\n" +
                    "	SUM(detallecreditproveed.cantidadCP*detallecreditproveed.costoCP) AS TOTAL,\n" +
                    "	creditomerca.notaPrest\n" +
                    "FROM\n" +
                    "	proveedor\n" +
                    "INNER JOIN\n" +
                    "	creditomerca\n" +
                    "ON\n" +
                    "	proveedor.id_Proveedor = creditomerca.id_ProveedorF AND creditomerca.fechaPrestamo BETWEEN '"+fech1+"' AND '"+fech2+"'\n" +
                    "INNER JOIN \n" +
                    "	detallecreditproveed\n" +
                    "ON\n" +
                    "	creditomerca.num_credito=detallecreditproveed.num_creditCP\n" +
                    "GROUP BY creditomerca.num_credito\n" +
                    "ORDER BY creditomerca.num_credito DESC;";
          }
          if(opcBusq==3){
                     sql = "SELECT\n" +
                    "	creditomerca.num_credito,creditomerca.fechaPrestamo,creditomerca.status,\n" +
                    "	proveedor.id_Proveedor,proveedor.nombreP,\n" +
                    "	SUM(detallecreditproveed.cantidadCP*detallecreditproveed.costoCP) AS TOTAL,\n" +
                    "	creditomerca.notaPrest\n" +
                    "FROM\n" +
                    "	proveedor\n" +
                    "INNER JOIN\n" +
                    "	creditomerca\n" +
                    "ON\n" +
                    "	proveedor.id_Proveedor = creditomerca.id_ProveedorF AND creditomerca.id_ProveedorF = '"+idCli+"' AND creditomerca.fechaPrestamo = '"+fech1+"'\n" +
                    "INNER JOIN \n" +
                    "	detallecreditproveed\n" +
                    "ON\n" +
                    "	creditomerca.num_credito=detallecreditproveed.num_creditCP\n" +
                    "GROUP BY creditomerca.num_credito\n" +
                    "ORDER BY creditomerca.num_credito DESC;";
          }
          if(opcBusq==4){
           sql = "SELECT\n" +
                    "	creditomerca.num_credito,creditomerca.fechaPrestamo,creditomerca.status,\n" +
                    "	proveedor.id_Proveedor,proveedor.nombreP,\n" +
                    "	SUM(detallecreditproveed.cantidadCP*detallecreditproveed.costoCP) AS TOTAL,\n" +
                    "	creditomerca.notaPrest\n" +
                    "FROM\n" +
                    "	proveedor\n" +
                    "INNER JOIN\n" +
                    "	creditomerca\n" +
                    "ON\n" +
                    "	proveedor.id_Proveedor = creditomerca.id_ProveedorF AND creditomerca.id_ProveedorF = '"+idCli+"' AND creditomerca.fechaPrestamo BETWEEN '"+fech1+"' "
                   + "AND '"+fech2+"'\n" +
                    "INNER JOIN \n" +
                    "	detallecreditproveed\n" +
                    "ON\n" +
                    "	creditomerca.num_credito=detallecreditproveed.num_creditCP\n" +
                    "GROUP BY creditomerca.num_credito\n" +
                    "ORDER BY creditomerca.num_credito DESC;";
          }
             int i =0,cantFilas=0, cont=1,cantColumnas=0;
             String[][] mat=null, mat2=null;
              int[] arrIdPedido = null;//int para usar hashMap
            Statement st = null;
            ResultSet rs = null;            
            try {
                st = cn.createStatement();
                rs = st.executeQuery(sql);
                cantColumnas = rs.getMetaData().getColumnCount();
               if(rs.last()){//Nos posicionamos al final
                    cantFilas = rs.getRow();//sacamos la cantidad de filas/registros
                    rs.beforeFirst();//nos posicionamos antes del inicio (como viene por defecto)
                }
               mat = new String[cantFilas][cantColumnas];
               //aqui iria crear matriz
                while(rs.next())
                {//es necesario el for para llenar dinamicamente la lista, ya que varia el numero de columnas de las tablas
                      for (int x=1;x<= rs.getMetaData().getColumnCount();x++) {
                           // System.out.print("| "+rs.getString(x)+" |");
                             mat[i][x-1]=rs.getString(x);
                      //System.out.print(x+" -> "+rs.getString(x));                   
                      }//for
                       i++;
                }//whilE
            } catch (SQLException ex) {
                Logger.getLogger(controladorCFP.class.getName()).log(Level.SEVERE, null, ex);
            }finally{               
//             System.out.println("cierra conexion a la base de datos");    
             try {        
                 if(st != null) st.close();                
                 if(cn !=null) cn.close();
             } catch (SQLException ex) {
                 JOptionPane.showMessageDialog(null,ex.getMessage()); 
             }
         }//finally        
           if (cantFilas == 0){
                mat=null;
                mat = new String[1][cantColumnas];
                
                for (int j = 0; j < mat[0].length; j++) {
                     mat[0][j]="NO DATA";
                }
           }
return mat;            
}//prestaProovOps
      
      /*COMPRA A PROVEEDORES*/
    public boolean validaPrestamoProv(String idProv){
        Connection cn = con2.conexion();
        boolean existe =false;
        int num=0,i=1;
        String sql = "SELECT '1' FROM creditomerca WHERE id_ProveedorF = '"+idProv+"' AND status = '0';";
        Statement st = null;
        ResultSet rs= null;
        try {
            st = cn.createStatement();
            rs = st.executeQuery(sql);
            rs.beforeFirst();
            if(rs.next())
            {
                if(rs.getRow() > 0){
                    existe =true;
                }
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(controladorCFP.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
                    try {
                        if(cn != null) cn.close();
                    } catch (SQLException ex) {
                        System.err.println( ex.getMessage() );    
                    }
                }
       return existe;
    }
      
     public void guardaCompraProv(List<String> param){
        Connection cn = con2.conexion();
            PreparedStatement pps=null;
            String SQL=""; 
        
          //  No.setFecha_inicio(new java.sql.Date(((Date) celda.getDateCellValue()).getTime()));
                SQL="INSERT INTO compraProoved (id_ProveedorC,fechaCompra,descripcionSubasta,notaCompra,statusCompra) VALUES (?,?,?,?,?)";                           
            try {
                pps = cn.prepareStatement(SQL);
                pps.setString(1, param.get(0));
                pps.setString(2,param.get(1));
                pps.setString(3,param.get(2));
                pps.setString(4,param.get(3));
                pps.setInt(5,Integer.parseInt(param.get(4)));
                pps.executeUpdate();
                JOptionPane.showMessageDialog(null, "Compra guardada correctamente.");
            } catch (SQLException ex) {
                Logger.getLogger(controladorCFP.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, "Error durante la transaccion.");
            }finally{
 //               System.out.println( "cierra conexion a la base de datos" );    
                try {
                    if(pps != null) pps.close();                
                    if(cn !=null) cn.close();
                    } catch (SQLException ex) {
                     JOptionPane.showMessageDialog(null,ex.getMessage() );    
                    }
            }//finally catch
    
} //guardaCompraProveedor 
 
     public String[] ultimoRegistroCompra(){
        Connection cn = con2.conexion();
        String[] arre = new String[2];
        String ultimo="",consul="";
        int num=0,i=1;
        String sql = "SELECT id_compraProve,id_ProveedorC FROM compraprooved ORDER BY id_compraProve DESC LIMIT 1";
        Statement st = null;
        ResultSet rs= null;
        try {
            st = cn.createStatement();
            rs = st.executeQuery(sql);
            rs.beforeFirst();
            while(rs.next())
            {
                arre[0] = rs.getString(1);
                arre[1] = rs.getString(2);
               // System.out.println(consul+" - "+ultimo);
            }
        } catch (SQLException ex) {
            Logger.getLogger(controladorCFP.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
                    try {
                        if(cn != null) cn.close();
                    } catch (SQLException ex) {
                        System.err.println( ex.getMessage() );    
                    }
                }
        return arre;
    }
     
     public void guardaDetalleCompraProv(String numCP,String codP, int cantP,String costP){
     Connection cn = con2.conexion();
            PreparedStatement pps=null;
            String SQL="";        
                SQL="INSERT INTO detailcompraprooved (id_compraP,codigoProdC,cantCajasC,precCajaC) VALUES (?,?,?,?)";                           
            try {
                pps = cn.prepareStatement(SQL);
                pps.setString(1, numCP);
                pps.setString(2,codP);
                pps.setInt(3,cantP);
                pps.setString(4,costP);
                pps.executeUpdate();
                JOptionPane.showMessageDialog(null, "Compra Guardada correctamente");
            } catch (SQLException ex) {
                Logger.getLogger(controladorCFP.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, "Error durante la transaccion.");
            }finally{
//               System.out.println( "finally detail->cierra conexion a la base de datos" );    
                try {
                    if(pps != null) pps.close();                
                    if(cn !=null) cn.close();
                    } catch (SQLException ex) {
                     JOptionPane.showMessageDialog(null,ex.getMessage() );    
                    }
            }//finally catch
} //guardaPrestamoPrveedor
              
public void elimaRow(String table,String campo,String id){
        Connection cn = con2.conexion();
        PreparedStatement preparedStmt = null;
        if(!id.isEmpty()){

            int dialogButton = JOptionPane.YES_NO_OPTION;
            int dialogResult = JOptionPane.showConfirmDialog (null, "Seguro que desea eliminar el registro con ID: "+id+" ?","Borrar",dialogButton);
            if(dialogResult == JOptionPane.YES_OPTION){
            try {
            String query = "delete from "+table+" where "+campo+" = '"+id+"' ";
            preparedStmt = cn.prepareStatement(query);
            preparedStmt.execute();
      
            JOptionPane.showMessageDialog(null, "Eliminado");

        } catch (SQLException ex) {
            Logger.getLogger(controladorCFP.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
//            System.out.println("cierra conexion a la base de datos");    
            try {
                if(preparedStmt != null) preparedStmt.close();                
                if(cn !=null) cn.close();
            } catch (SQLException ex) {
                System.err.println(ex.getMessage()); 
//                System.out.println("Error al cerrar la conexon");
            }//catch
        }//finally 
            } else {
                JOptionPane.showMessageDialog(null,"No se borro el registro: "+id);
            }
        }else
            JOptionPane.showMessageDialog(null,"Sin data a eliminar");
}

     public String[][] consultCompra(String opc){
        Connection cn = con2.conexion();
        int cantColumnas=0,cantFilas=0;
        String[][] arre=null;
        String sql ="";
              sql = "SELECT id_compraProve,id_ProveedorC FROM compraprooved WHERE fechaCompra = '"+opc+"'";           
            Statement st = null;
            ResultSet rs = null;            
            int i =0;
            try {
                st = cn.createStatement();
                rs = st.executeQuery(sql);
                
            cantColumnas = rs.getMetaData().getColumnCount();
               if(rs.last()){//Nos posicionamos al final
                    cantFilas = rs.getRow();//sacamos la cantidad de filas/registros
                    rs.beforeFirst();//nos posicionamos antes del inicio (como viene por defecto)
                }     
               arre = new String[cantFilas][cantColumnas];
                while(rs.next())
                {//es necesario el for para llenar dinamicamente la lista, ya que varia el numero de columnas de las tablas
                        //for (int x=1;x<= rs.getMetaData().getColumnCount()-1;x++) {
                          //      if(x==3){
//                                    if(rs.getInt(x)==1){
                       arre[i][0]=rs.getString(1);
                       arre[i][1]=rs.getString(2);
                            //    }
                            //System.out.print("["+x+"]"+" -> "+rs.getString(x));                   
                        //}//for
                            //System.out.println(rs.getString(1));
                          //  consultDetailCompra(rs.getInt(1));
                      i++;      
                }//while
            } catch (SQLException ex) {
                Logger.getLogger(controladorCFP.class.getName()).log(Level.SEVERE, null, ex);
            }finally{               
             try {        
                 if(st != null) st.close();                
                 if(cn !=null) cn.close();
             } catch (SQLException ex) {
                 JOptionPane.showMessageDialog(null,ex.getMessage()); 
             }
         }//finally  
            return arre;
    }//regresaDatos
     
     public  void consultDetailCompra(int opc){
       Connection cn = con2.conexion();
       int cantColumnas=0, cantFilas=0,temporal=0,bandera=0;
        String sql ="";
              sql = "SELECT * FROM detailcompraprooved WHERE id_compraP = '"+opc+"'";           
            Statement st = null;
            ResultSet rs = null;    
            try {
                st = cn.createStatement();
                rs = st.executeQuery(sql);
 cantColumnas = rs.getMetaData().getColumnCount();
               if(rs.last()){//Nos posicionamos al final
                    cantFilas = rs.getRow();//sacamos la cantidad de filas/registros
                    rs.beforeFirst();//nos posicionamos antes del inicio (como viene por defecto)
                }                
             //   System.out.print("Filas: "+cantFilas+"\tColumnas: "+cantColumnas+"\n");
                while(rs.next())
                {//es necesario el for para llenar dinamicamente la lista, ya que varia el numero de columnas de las tablas
                        for (int x=1;x<= rs.getMetaData().getColumnCount();x++) {
                            System.out.print("["+x+"]"+" -> "+rs.getString(x));                   
                        }//for
                            System.out.println();
                }//while
            } catch (SQLException ex) {
                Logger.getLogger(controladorCFP.class.getName()).log(Level.SEVERE, null, ex);
            }finally{               
             try {        
                 if(st != null) st.close();                
                 if(cn !=null) cn.close();
             } catch (SQLException ex) {
                 JOptionPane.showMessageDialog(null,ex.getMessage()); 
             }
         }//finally  
    }//regresaDatos
      
//CALCULA EL TOTAL DE LA COMPRA A PROVEEDOR 
       public String totalCompraProv(String id_comp){
        Connection cn = con2.conexion();
        String prod = "";
        String sql = "SELECT \n" +
                "	SUM(detailcompraprooved.cantCajasC*detailcompraprooved.precCajaC) AS TOTAL\n" +
                "FROM \n" +
                "	detailcompraprooved\n" +
                "WHERE \n" +
                "	detailcompraprooved.id_compraP = '"+id_comp+"'";
        Statement st = null;
        ResultSet rs= null;
        try {
            st = cn.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                prod = rs.getString(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(controladorCFP.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
                    try {
                        if(cn != null) cn.close();
                    } catch (SQLException ex) {
                        System.err.println( ex.getMessage() );    
                    }
                }
        return prod;
    }
         
 //Filtros de busqueda compra a proveedor
    public String[][] matrizCompraProvOpc(int opcBusq,String idCli,String fech1,String fech2){
        Connection cn = con2.conexion();
          String sql ="",aux;
          if(opcBusq == 0){//CLIENTE
              sql = "SELECT\n" +
                 "	compraprooved.id_compraProve,compraprooved.fechaCompra,IF(compraprooved.statusCompra=0,'PENDIENTE','PAGADO'),\n" +
                 "	proveedor.id_Proveedor,IF(proveedor.nombreP='SUBASTA',compraprooved.descripcionSubasta,proveedor.nombreP),\n" +
                 "	SUM(detailcompraprooved.cantCajasC*detailcompraprooved.precCajaC),\n" +
                 "	compraprooved.notaCompra\n" +
                 "FROM\n" +
                 "	proveedor\n" +
                 "INNER JOIN\n" +
                 "	compraprooved\n" +
                 "ON\n" +
                 "	proveedor.id_Proveedor=compraprooved.id_ProveedorC AND proveedor.id_Proveedor = '"+idCli+"'\n" +
                 "INNER JOIN\n" +
                 "	detailCompraProoved\n" +
                 "ON\n" +
                 "	compraprooved.id_compraProve=detailCompraProoved.id_compraP\n" +
                 "GROUP BY compraprooved.id_compraProve\n" +
                 "ORDER BY compraprooved.fechaCompra;";      
          }    
          if(opcBusq==1){//mayorista
           sql = "SELECT compramayoreo.id_compraProveMin,compramayoreo.fechaRel,\n" +
                "	IF(compraprooved.statusCompra = 0,\"PENDIENTE\",\"PAGADO\") AS condic,\n" +
                "	compramayoreo.id_compraMay,proveedor.nombreP,\n" +
                "	SUM(detailcompraprooved.cantCajasC * detailcompraprooved.precCajaC) AS suma,\n" +
                "	compraprooved.notaCompra\n" +
                "FROM\n" +
                "	compraprooved\n" +
                "INNER JOIN\n" +
                "	proveedor\n" +
                "ON \n" +
                "	proveedor.id_Proveedor = compraprooved.id_ProveedorC\n" +
                "INNER JOIN\n" +
                "	compramayoreo\n" +
                "ON \n" +
                "	compraprooved.id_compraProve = compramayoreo.id_compraProveMin\n" +
                "INNER JOIN\n" +
                "	detailcompraprooved\n" +
                "ON\n" +
                "	compraprooved.id_compraProve = detailcompraprooved.id_compraP\n" +
                "AND \n" +
                "	compramayoreo.id_ProveedorMay = '"+idCli+"'\n" +
                "GROUP BY compraprooved.id_compraProve\n" +
                "ORDER BY compraprooved.fechaCompra DESC;";
          }
          
          if(opcBusq==2){//MAYORISTA+FECHA
              sql="SELECT compramayoreo.id_compraProveMin,compramayoreo.fechaRel,\n" +
                "	IF(compraprooved.statusCompra = 0,\"PENDIENTE\",\"PAGADO\") AS condic,\n" +
                "	compramayoreo.id_compraMay,proveedor.nombreP,\n" +
                "	SUM(detailcompraprooved.cantCajasC * detailcompraprooved.precCajaC) AS suma,\n" +
                "	compraprooved.notaCompra\n" +
                "FROM\n" +
                "	compraprooved\n" +
                "INNER JOIN\n" +
                "	proveedor\n" +
                "ON \n" +
                "	proveedor.id_Proveedor = compraprooved.id_ProveedorC\n" +
                "INNER JOIN\n" +
                "	compramayoreo\n" +
                "ON \n" +
                "	compraprooved.id_compraProve = compramayoreo.id_compraProveMin\n" +
                "INNER JOIN\n" +
                "	detailcompraprooved\n" +
                "ON\n" +
                "	compraprooved.id_compraProve = detailcompraprooved.id_compraP\n" +
                "AND \n" +
                "	compramayoreo.fechaRel = '"+fech1+"'\n" +
                "AND \n" +
                "	compramayoreo.id_ProveedorMay = '"+idCli+"'\n" +
                "GROUP BY compraprooved.id_compraProve\n" +
                "ORDER BY compraprooved.fechaCompra;";
          }
          
          if(opcBusq==3){//FECHA
           sql = "SELECT\n" +
                "	compraprooved.id_compraProve,compraprooved.fechaCompra,IF(compraprooved.statusCompra=0,'PENDIENTE','PAGADO'),\n" +
                "	proveedor.id_Proveedor,IF(proveedor.nombreP='SUBASTA',compraprooved.descripcionSubasta,proveedor.nombreP),\n" +
                "	SUM(detailcompraprooved.cantCajasC*detailcompraprooved.precCajaC),\n" +
                "	compraprooved.notaCompra\n" +
                "FROM\n" +
                "	proveedor\n" +
                "INNER JOIN\n" +
                "	compraprooved\n" +
                "ON\n" +
                "	proveedor.id_Proveedor=compraprooved.id_ProveedorC AND compraprooved.fechaCompra = '"+fech1+"'\n" +
                "INNER JOIN\n" +
                "	detailCompraProoved\n" +
                "ON\n" +
                "	compraprooved.id_compraProve=detailCompraProoved.id_compraP\n" +
                "GROUP BY compraprooved.id_compraProve\n" +
                "ORDER BY compraprooved.id_compraProve DESC;";
          }
          if(opcBusq==4){//LAPSO DE FECHAS
                     sql = "SELECT\n" +
                "	compraprooved.id_compraProve,compraprooved.fechaCompra,IF(compraprooved.statusCompra=0,'PENDIENTE','PAGADO'),\n" +
                "	proveedor.id_Proveedor,IF(proveedor.nombreP='SUBASTA',compraprooved.descripcionSubasta,proveedor.nombreP),\n" +
                "	SUM(detailcompraprooved.cantCajasC*detailcompraprooved.precCajaC),\n" +
                "	compraprooved.notaCompra\n" +
                "FROM\n" +
                "	proveedor\n" +
                "INNER JOIN\n" +
                "	compraprooved\n" +
                "ON\n" +
                "	proveedor.id_Proveedor=compraprooved.id_ProveedorC AND compraprooved.fechaCompra BETWEEN '"+fech1+"' AND '"+fech2+"'\n" +
                "INNER JOIN\n" +
                "	detailCompraProoved\n" +
                "ON\n" +
                "	compraprooved.id_compraProve=detailCompraProoved.id_compraP\n" +
                "GROUP BY compraprooved.id_compraProve\n" +
                "ORDER BY compraprooved.id_compraProve DESC;";
          }
          if(opcBusq==5){//CLIENTE+FECHA
           sql = "SELECT\n" +
                "	compraprooved.id_compraProve,compraprooved.fechaCompra,IF(compraprooved.statusCompra=0,'PENDIENTE','PAGADO'),\n" +
                "	proveedor.id_Proveedor,IF(proveedor.nombreP='SUBASTA',compraprooved.descripcionSubasta,proveedor.nombreP),\n" +
                "	SUM(detailcompraprooved.cantCajasC*detailcompraprooved.precCajaC),\n" +
                "	compraprooved.notaCompra\n" +
                "FROM\n" +
                "	proveedor\n" +
                "INNER JOIN\n" +
                "	compraprooved\n" +
                "ON\n" +
                "	proveedor.id_Proveedor=compraprooved.id_ProveedorC AND proveedor.id_Proveedor = '"+idCli+"' AND compraprooved.fechaCompra = '"+fech1+"'\n" +
                "INNER JOIN\n" +
                "	detailCompraProoved\n" +
                "ON\n" +
                "	compraprooved.id_compraProve=detailCompraProoved.id_compraP\n" +
                "GROUP BY compraprooved.id_compraProve;";
          }
          if(opcBusq==6){//CLIENTE+LAPSO DE FECHAS
           sql = "SELECT\n" +
                "	compraprooved.id_compraProve,compraprooved.fechaCompra,IF(compraprooved.statusCompra=0,'PENDIENTE','PAGADO'),\n" +
                "	proveedor.id_Proveedor,IF(proveedor.nombreP='SUBASTA',compraprooved.descripcionSubasta,proveedor.nombreP),\n" +
                "	SUM(detailcompraprooved.cantCajasC*detailcompraprooved.precCajaC),\n" +
                "	compraprooved.notaCompra\n" +
                "FROM\n" +
                "	proveedor\n" +
                "INNER JOIN\n" +
                "	compraprooved\n" +
                "ON\n" +
                "	proveedor.id_Proveedor=compraprooved.id_ProveedorC AND proveedor.id_Proveedor = '1' AND compraprooved.fechaCompra BETWEEN '2020-01-10' AND '2020-01-30'\n" +
                "INNER JOIN\n" +
                "	detailCompraProoved\n" +
                "ON\n" +
                "	compraprooved.id_compraProve=detailCompraProoved.id_compraP\n" +
                "GROUP BY compraprooved.id_compraProve\n" +
                "ORDER BY compraprooved.id_compraProve DESC;";
          }//IF 5          
             int i =0,cantFilas=0, cont=1,cantColumnas=0;
             String[][] mat=null, mat2=null;
              int[] arrIdPedido = null;//int para usar hashMap
            Statement st = null;
            ResultSet rs = null;            
            try {
                st = cn.createStatement();
                rs = st.executeQuery(sql);
                cantColumnas = rs.getMetaData().getColumnCount();
               if(rs.last()){//Nos posicionamos al final
                    cantFilas = rs.getRow();//sacamos la cantidad de filas/registros
                    rs.beforeFirst();//nos posicionamos antes del inicio (como viene por defecto)
                }
               mat = new String[cantFilas][cantColumnas];
               //aqui iria crear matriz
                while(rs.next())
                {//es necesario el for para llenar dinamicamente la lista, ya que varia el numero de columnas de las tablas
                      for (int x=1;x<= rs.getMetaData().getColumnCount();x++) {
                           // System.out.print("| "+rs.getString(x)+" |");
                             mat[i][x-1]=rs.getString(x);
                      //System.out.print(x+" -> "+rs.getString(x));                   
                      }//for
                       i++;
                }//whilE
            } catch (SQLException ex) {
                Logger.getLogger(controladorCFP.class.getName()).log(Level.SEVERE, null, ex);
            }finally{               
//             System.out.println("cierra conexion a la base de datos");    
             try {        
                 if(st != null) st.close();                
                 if(cn !=null) cn.close();
             } catch (SQLException ex) {
                 JOptionPane.showMessageDialog(null,ex.getMessage()); 
             }
         }//finally        
           if (cantFilas == 0){
                mat=null;
                mat = new String[1][cantColumnas];
                
                for (int j = 0; j < mat[0].length; j++) {
                     mat[0][j]="NO DATA";
                }
           }
return mat;            
}//prestaProovOps
             
/////****** OPERACIONES DE VENTA DE PISO
      public void guardaVentaPiso(List<String> param){
        Connection cn = con2.conexion();
            PreparedStatement pps=null;
            String SQL=""; 
                SQL="INSERT INTO notaventapiso (id_clientePiso,fechVenta,statusVent,notaVent) VALUES (?,?,?,?)";                           
            try {
                pps = cn.prepareStatement(SQL);
                pps.setString(1, param.get(0));
                pps.setString(2,param.get(1));
                pps.setInt(3,0);
                pps.setString(4,param.get(2));
                pps.executeUpdate();
                JOptionPane.showMessageDialog(null, "Venta agregada correctamente.");
            } catch (SQLException ex) {
                Logger.getLogger(controladorCFP.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, "Error durante la transaccion.");
            }finally{
                try {
                    if(pps != null) pps.close();                
                    if(cn !=null) cn.close();
                    } catch (SQLException ex) {
                     JOptionPane.showMessageDialog(null,ex.getMessage() );    
                    }
            }//finally catch
} //guardaVentaPiso
      
      public String[] ultimoRegistroVentPiso(){
        Connection cn = con2.conexion();
        String[] arre = new String[2];
        String ultimo="",consul="";
        int num=0,i=1;
        String sql = "SELECT id_venta,id_clientePiso FROM notaventapiso ORDER BY id_venta DESC LIMIT 1";
        Statement st = null;
        ResultSet rs= null;
        try {
            st = cn.createStatement();
            rs = st.executeQuery(sql);
            rs.beforeFirst();
            while(rs.next())
            {
                arre[0] = rs.getString(1);
                arre[1] = rs.getString(2);
               // System.out.println(consul+" - "+ultimo);
            }
        } catch (SQLException ex) {
            Logger.getLogger(controladorCFP.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
                    try {
                        if(cn != null) cn.close();
                    } catch (SQLException ex) {
                        System.err.println( ex.getMessage() );    
                    }
                }
        return arre;
    }
      
     public void guardaDetalleVentPiso(List<String> datas){
        Connection cn = con2.conexion();
               PreparedStatement pps=null;
               String SQL="";        
                   SQL="INSERT INTO detailventapiso (num_notaV,cod_prodV,cantidadV,precioPV) VALUES (?,?,?,?)";                           
               try {
                   pps = cn.prepareStatement(SQL);
                   pps.setString(1, datas.get(0));
                   pps.setString(2, datas.get(1));
                   pps.setString(3, datas.get(2));
                   pps.setString(4, datas.get(3));
                   pps.executeUpdate();
                   JOptionPane.showMessageDialog(null, "Venta Guardada correctamente");
               } catch (SQLException ex) {
                   Logger.getLogger(controladorCFP.class.getName()).log(Level.SEVERE, null, ex);
                   JOptionPane.showMessageDialog(null, "Error durante la transaccion.");
               }finally{
   //               System.out.println( "finally detail->cierra conexion a la base de datos" );    
                   try {
                       if(pps != null) pps.close();                
                       if(cn !=null) cn.close();
                       } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null,ex.getMessage() );    
                       }
               }//finally catch
    } //guardaPrestamoPrveedor
            
             public String[][] matrizVentaPisoDia(String fech){
        Connection cn = con2.conexion();
          String sql ="",aux;
              sql = "SELECT\n" +
                "	notaventapiso.id_venta,notaventapiso.fechVenta,notaventapiso.statusVent,\n" +
                "	clientepedidos.id_cliente,clientepedidos.nombre,\n" +
                "	SUM(detailventapiso.cantidadV*detailventapiso.precioPV),\n" +
                "	notaventapiso.notaVent\n" +
                "FROM\n" +
                "	clientepedidos\n" +
                "INNER JOIN\n" +
                "	notaventapiso\n" +
                "ON\n" +
                "	clientePedidos.id_cliente = notaventapiso.id_clientePiso AND\n" +
                "	 notaventapiso.fechVenta = '"+fech+"' \n" +
                "INNER JOIN \n" +
                "	detailVentaPiso \n" +
                "ON\n" +
                "	notaventapiso.id_venta=detailventapiso.num_notaV\n" +
                "GROUP BY notaventapiso.id_venta\n" +
                "ORDER BY notaventapiso.fechVenta;";      
              
             int i =0,cantFilas=0, cont=1,cantColumnas=0;
             String[][] mat=null, mat2=null;
              int[] arrIdPedido = null;//int para usar hashMap
            Statement st = null;
            ResultSet rs = null;            
            try {
                st = cn.createStatement();
                rs = st.executeQuery(sql);
                cantColumnas = rs.getMetaData().getColumnCount();
               if(rs.last()){//Nos posicionamos al final
                    cantFilas = rs.getRow();//sacamos la cantidad de filas/registros
                    rs.beforeFirst();//nos posicionamos antes del inicio (como viene por defecto)
                }
               mat = new String[cantFilas][cantColumnas];
               //aqui iria crear matriz
                while(rs.next())
                {//es necesario el for para llenar dinamicamente la lista, ya que varia el numero de columnas de las tablas
                      for (int x=1;x<= rs.getMetaData().getColumnCount();x++) {
                           // System.out.print("| "+rs.getString(x)+" |");
                             mat[i][x-1]=rs.getString(x);
                      //System.out.print(x+" -> "+rs.getString(x));                   
                      }//for
                       i++;
                }//whilE
            } catch (SQLException ex) {
                Logger.getLogger(controladorCFP.class.getName()).log(Level.SEVERE, null, ex);
            }finally{               
//             System.out.println("cierra conexion a la base de datos");    
             try {        
                 if(st != null) st.close();                
                 if(cn !=null) cn.close();
             } catch (SQLException ex) {
                 JOptionPane.showMessageDialog(null,ex.getMessage()); 
             }
         }//finally        
           if (cantFilas == 0){
                mat=null;
                mat = new String[1][cantColumnas];
                
                for (int j = 0; j < mat[0].length; j++) {
                     mat[0][j]="NO DATA";
                }
           }
return mat;            
}//prestaProov
   
   public String[][] matrizVentasDia(int opcBusq,String idCli,String fech1,String fech2){
        Connection cn = con2.conexion();
          String sql ="",aux;
          if(opcBusq==0){//OPCION BUSQUEDA DE NOMBRE
              sql = "SELECT\n" +
                "	notaventapiso.id_venta,notaventapiso.fechVenta,notaventapiso.statusVent,\n" +
                "	clientepedidos.id_cliente,clientepedidos.nombre,\n" +
                "	SUM(detailventapiso.cantidadV*detailventapiso.precioPV),\n" +
                "	notaventapiso.notaVent\n" +
                "FROM\n" +
                "	clientepedidos\n" +
                "INNER JOIN\n" +
                "	notaventapiso\n" +
                "ON\n" +
                "	clientePedidos.id_cliente = notaventapiso.id_clientePiso AND notaventapiso.id_clientePiso = '"+idCli+"'\n" +
                "	 \n" +
                "INNER JOIN \n" +
                "	detailVentaPiso \n" +
                "ON\n" +
                "	notaventapiso.id_venta=detailventapiso.num_notaV\n" +
                "GROUP BY notaventapiso.id_venta\n" +
                "ORDER BY notaventapiso.fechVenta;";      
          }
          
          if(opcBusq==1){//OPCION BUSQUEDA FECHA
              sql = "SELECT\n" +
                "	notaventapiso.id_venta,notaventapiso.fechVenta,notaventapiso.statusVent,\n" +
                "	clientepedidos.id_cliente,clientepedidos.nombre,\n" +
                "	SUM(detailventapiso.cantidadV*detailventapiso.precioPV),\n" +
                "	notaventapiso.notaVent\n" +
                "FROM\n" +
                "	clientepedidos\n" +
                "INNER JOIN\n" +
                "	notaventapiso\n" +
                "ON\n" +
                "	clientePedidos.id_cliente = notaventapiso.id_clientePiso AND notaventapiso.fechVenta = '"+fech1+"'\n" +
                "	 \n" +
                "INNER JOIN \n" +
                "	detailVentaPiso \n" +
                "ON\n" +
                "	notaventapiso.id_venta=detailventapiso.num_notaV\n" +
                "GROUP BY notaventapiso.id_venta\n" +
                "ORDER BY notaventapiso.fechVenta;";      
          }
          if(opcBusq==2){//OPCION LAPSO DE FECHAS
              sql="SELECT\n" +
                "	notaventapiso.id_venta,notaventapiso.fechVenta,notaventapiso.statusVent,\n" +
                "	clientepedidos.id_cliente,clientepedidos.nombre,\n" +
                "	SUM(detailventapiso.cantidadV*detailventapiso.precioPV),\n" +
                "	notaventapiso.notaVent\n" +
                "FROM\n" +
                "	clientepedidos\n" +
                "INNER JOIN\n" +
                "	notaventapiso\n" +
                "ON\n" +
                "	clientePedidos.id_cliente = notaventapiso.id_clientePiso AND\n" +
                "	 notaventapiso.fechVenta BETWEEN '"+fech1+"' AND '"+fech2+"'\n" +
                "INNER JOIN \n" +
                "	detailVentaPiso \n" +
                "ON\n" +
                "	notaventapiso.id_venta=detailventapiso.num_notaV\n" +
                "GROUP BY notaventapiso.id_venta\n" +
                "ORDER BY notaventapiso.fechVenta DESC;";
          }
          if(opcBusq==3){
              sql="SELECT\n" +
                "	notaventapiso.id_venta,notaventapiso.fechVenta,notaventapiso.statusVent,\n" +
                "	clientepedidos.id_cliente,clientepedidos.nombre,\n" +
                "	SUM(detailventapiso.cantidadV*detailventapiso.precioPV),\n" +
                "	notaventapiso.notaVent\n" +
                "FROM\n" +
                "	clientepedidos\n" +
                "INNER JOIN\n" +
                "	notaventapiso\n" +
                "ON\n" +
                "	clientePedidos.id_cliente = notaventapiso.id_clientePiso AND notaventapiso.id_clientePiso = '"+idCli+"' AND\n" +
                "	notaventapiso.fechVenta = '"+fech1+"'\n" +
                "INNER JOIN \n" +
                "	detailVentaPiso \n" +
                "ON\n" +
                "	notaventapiso.id_venta=detailventapiso.num_notaV\n" +
                "GROUP BY notaventapiso.id_venta\n" +
                "ORDER BY notaventapiso.fechVenta;";
          }
          if(opcBusq ==4){
              sql="SELECT\n" +
                "	notaventapiso.id_venta,notaventapiso.fechVenta,notaventapiso.statusVent,\n" +
                "	clientepedidos.id_cliente,clientepedidos.nombre,\n" +
                "	SUM(detailventapiso.cantidadV*detailventapiso.precioPV),\n" +
                "	notaventapiso.notaVent\n" +
                "FROM\n" +
                "	clientepedidos\n" +
                "INNER JOIN\n" +
                "	notaventapiso\n" +
                "ON\n" +
                "	clientePedidos.id_cliente = notaventapiso.id_clientePiso AND notaventapiso.id_clientePiso = '"+idCli+"' AND\n" +
                "	notaventapiso.fechVenta BETWEEN '"+fech1+"' AND '"+fech2+"'\n" +
                "INNER JOIN \n" +
                "	detailVentaPiso \n" +
                "ON\n" +
                "	notaventapiso.id_venta=detailventapiso.num_notaV\n" +
                "GROUP BY notaventapiso.id_venta\n" +
                "ORDER BY notaventapiso.fechVenta;";
          }
             int i =0,cantFilas=0, cont=1;
             String[][] mat=null, mat2=null;
              int[] arrIdPedido = null;//int para usar hashMap
            Statement st = null;
            ResultSet rs = null;    
            int cantColumnas=0;
            try {
                st = cn.createStatement();
                rs = st.executeQuery(sql);
                cantColumnas = rs.getMetaData().getColumnCount();
               if(rs.last()){//Nos posicionamos al final
                    cantFilas = rs.getRow();//sacamos la cantidad de filas/registros
                    rs.beforeFirst();//nos posicionamos antes del inicio (como viene por defecto)
                }
               mat = new String[cantFilas][cantColumnas];
               //aqui iria crear matriz
                while(rs.next())
                {//es necesario el for para llenar dinamicamente la lista, ya que varia el numero de columnas de las tablas
                 
                      for (int x=1;x<= rs.getMetaData().getColumnCount();x++) {
                           // System.out.print("| "+rs.getString(x)+" |");
                             mat[i][x-1]=rs.getString(x);
                      //System.out.print(x+" -> "+rs.getString(x));                   
                      }//for
                       i++;
                }//whilE
            } catch (SQLException ex) {
                Logger.getLogger(controladorCFP.class.getName()).log(Level.SEVERE, null, ex);
            }finally{               
//             System.out.println("cierra conexion a la base de datos");    
             try {        
                 if(st != null) st.close();                
                 if(cn !=null) cn.close();
             } catch (SQLException ex) {
                 JOptionPane.showMessageDialog(null,ex.getMessage()); 
             }
         }//finally 
            
           // System.out.println("filas: "+cantFilas);
            if (cantFilas == 0){
                mat=null;
                mat = new String[1][cantColumnas];
                
                for (int j = 0; j < mat[0].length; j++) {
                     mat[0][j]="NO DATA";
                }
           }
return mat;            
}
   
     public String totalVentaPiso(String id_comp){
        Connection cn = con2.conexion();
        String prod = "";
        String sql = "SELECT \n" +
                "	SUM(detailventapiso.cantidadV*detailventapiso.precioPV) AS TOTAL\n" +
                "FROM \n" +
                "	detailventapiso\n" +
                "WHERE \n" +
                "	detailventapiso.num_notaV = '"+id_comp+"' ";
        Statement st = null;
        ResultSet rs= null;
        try {
            st = cn.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                prod = rs.getString(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(controladorCFP.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
                    try {
                        if(cn != null) cn.close();
                    } catch (SQLException ex) {
                        System.err.println( ex.getMessage() );    
                    }
                }
        return prod;
    }
     
 //**CODIGO PARA FLETES*/
         public void creaFletes(List<String> param){
            Connection cn = con2.conexion();
            PreparedStatement pps=null;
            String SQL="";        
  
                SQL="INSERT INTO fleteenviado (id_fleteE,id_FleteroE,fechaFlete,choferFlete,trocaFlete,costoFlete,status,recivioFlete) VALUES (?,?,?,?,?,?,?,?)";                           
            try {
                pps = cn.prepareStatement(SQL);
                pps.setString(1, param.get(0));
                pps.setString(2, param.get(1));
                pps.setString(3, param.get(2));
                pps.setString(4, param.get(3));
                pps.setString(5, param.get(4));               
                pps.setString(6, param.get(5));               
                pps.setInt(7, 0);               
                pps.setString(8, param.get(6));               
                pps.executeUpdate();
                JOptionPane.showMessageDialog(null, "Flete creado correctamente.");
            } catch (SQLException ex) {
                Logger.getLogger(controladorCFP.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, "Error durante la transaccion.");
            }finally{
 //               System.out.println( "cierra conexion a la base de datos" );    
                try {
                    if(pps != null) pps.close();                
                    if(cn !=null) cn.close();
                    } catch (SQLException ex) {
                     JOptionPane.showMessageDialog(null,ex.getMessage() );    
                    }
            }//finally catch
        }
         
       public void actualizaFlete(List<String> param,String id){
             Connection cn = con2.conexion();
            PreparedStatement pps=null;
            String SQL="";        
                SQL="UPDATE fleteenviado SET id_FleteroE =?, fechaFlete =?, choferFlete=?, trocaFlete=?, costoFlete=?, status=?, recivioFlete=? WHERE id_fleteE = '"+id+"' ";                           
            try {
                pps = cn.prepareStatement(SQL);
                pps.setString(1, param.get(0));
                pps.setString(2, param.get(1));
                pps.setString(3, param.get(2));
                pps.setString(4, param.get(3));
                pps.setString(5, param.get(4));
                pps.setInt(6, Integer.parseInt(param.get(5)));
                pps.setString(7, param.get(6));
                
                pps.executeUpdate();
                JOptionPane.showMessageDialog(null, "Flete actualizado correctamente.");
            } catch (SQLException ex) {
                Logger.getLogger(controladorCFP.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, "Error durante la transaccion.");
            }finally{
 //               System.out.println( "cierra conexion a la base de datos" );    
                try {
                    if(pps != null) pps.close();                
                    if(cn !=null) cn.close();
                    } catch (SQLException ex) {
                     JOptionPane.showMessageDialog(null,ex.getMessage() );    
                    }
            }//finally catch
        }
       
//++++FILTROS DE BUSQUEDA DE FLETES
     public String[][] matrizFletesFilter(int opcBusq,String idCli,String fech1,String fech2){
        Connection cn = con2.conexion();
          String sql ="",aux;
          if(opcBusq==0){//OPCION BUSQUEDA DE NOMBRE
              sql = "SELECT fleteEnviado.id_fleteE, fletero.nombreF,fleteEnviado.fechaFlete,fleteEnviado.choferFlete,\n" +
                "fleteEnviado.trocaFlete,fleteEnviado.costoFlete,fleteEnviado.status,fleteEnviado.recivioFlete\n" +
                "FROM\n" +
                "	fleteEnviado\n" +
                "INNER JOIN\n" +
                "	fletero\n" +
                "ON fletero.id_Fletero = fleteEnviado.id_FleteroE AND fleteEnviado.id_FleteroE = '"+idCli+"';";      
          }
          
          if(opcBusq==1){//OPCION BUSQUEDA FOLIO
              sql = "SELECT fleteEnviado.id_fleteE, fletero.nombreF,fleteEnviado.fechaFlete,fleteEnviado.choferFlete,\n" +
                "fleteEnviado.trocaFlete,fleteEnviado.costoFlete,fleteEnviado.status,fleteEnviado.recivioFlete\n" +
                "FROM\n" +
                "	fleteEnviado\n" +
                "INNER JOIN\n" +
                "	fletero\n" +
                "ON fletero.id_Fletero = fleteEnviado.id_FleteroE AND fleteEnviado.id_fleteE = '"+idCli+"';";//folio      
          }
          if(opcBusq==2){//OPCION BUSQUEDA POR FECHA
              sql="SELECT fleteEnviado.id_fleteE, fletero.nombreF,fleteEnviado.fechaFlete,fleteEnviado.choferFlete,\n" +
                "fleteEnviado.trocaFlete,fleteEnviado.costoFlete,fleteEnviado.status,fleteEnviado.recivioFlete\n" +
                "	FROM\n" +
                "		fleteEnviado\n" +
                "	INNER JOIN\n" +
                "		fletero\n" +
                "	ON fletero.id_Fletero = fleteEnviado.id_FleteroE AND fleteEnviado.fechaFlete = '"+fech1+"';";
          }
          if(opcBusq==3){//LAPSO DE FECHAS
              sql="SELECT fleteEnviado.id_fleteE, fletero.nombreF,fleteEnviado.fechaFlete,fleteEnviado.choferFlete,\n" +
                "	fleteEnviado.trocaFlete,fleteEnviado.costoFlete,fleteEnviado.status,fleteEnviado.recivioFlete\n" +
                "	FROM\n" +
                "		fleteEnviado\n" +
                "	INNER JOIN\n" +
                "		fletero\n" +
                "	ON fletero.id_Fletero = fleteEnviado.id_FleteroE AND fleteEnviado.fechaFlete BETWEEN '"+fech1+"' AND '"+fech2+"';";
          }
          if(opcBusq ==4){//fletero + fecha
              sql="SELECT fleteEnviado.id_fleteE, fletero.nombreF,fleteEnviado.fechaFlete,fleteEnviado.choferFlete,\n" +
                "	fleteEnviado.trocaFlete,fleteEnviado.costoFlete,fleteEnviado.status,fleteEnviado.recivioFlete\n" +
                "	FROM\n" +
                "		fleteEnviado\n" +
                "	INNER JOIN\n" +
                "		fletero\n" +
                "	ON fletero.id_Fletero = fleteEnviado.id_FleteroE AND fleteEnviado.id_FleteroE = '"+idCli+"' AND\n" +
                "	 fleteEnviado.fechaFlete = '"+fech1+"';";
          }
          if(opcBusq ==5){//fletero lapso de fechas
              sql="SELECT fleteEnviado.id_fleteE, fletero.nombreF,fleteEnviado.fechaFlete,fleteEnviado.choferFlete,\n" +
                "	fleteEnviado.trocaFlete,fleteEnviado.costoFlete,fleteEnviado.status,fleteEnviado.recivioFlete\n" +
                "	FROM\n" +
                "		fleteEnviado\n" +
                "	INNER JOIN\n" +
                "		fletero\n" +
                "	ON fletero.id_Fletero = fleteEnviado.id_FleteroE AND fleteEnviado.id_FleteroE = '"+idCli+"' AND\n" +
                "	 fleteEnviado.fechaFlete BETWEEN '"+fech1+"' AND '"+fech2+"';";
          }
             int i =0,cantFilas=0, cont=1;
             String[][] mat=null, mat2=null;
              int[] arrIdPedido = null;//int para usar hashMap
            Statement st = null;
            ResultSet rs = null;    
            int cantColumnas=0;
            try {
                st = cn.createStatement();
                rs = st.executeQuery(sql);
                cantColumnas = rs.getMetaData().getColumnCount();
               if(rs.last()){//Nos posicionamos al final
                    cantFilas = rs.getRow();//sacamos la cantidad de filas/registros
                    rs.beforeFirst();//nos posicionamos antes del inicio (como viene por defecto)
                }
               mat = new String[cantFilas][cantColumnas];
               //aqui iria crear matriz
                while(rs.next())
                {//es necesario el for para llenar dinamicamente la lista, ya que varia el numero de columnas de las tablas
                 
                      for (int x=1;x<= rs.getMetaData().getColumnCount();x++) {
                           // System.out.print("| "+rs.getString(x)+" |");
                             mat[i][x-1]=rs.getString(x);
                      //System.out.print(x+" -> "+rs.getString(x));                   
                      }//for
                       i++;
                }//whilE
            } catch (SQLException ex) {
                Logger.getLogger(controladorCFP.class.getName()).log(Level.SEVERE, null, ex);
            }finally{               
//             System.out.println("cierra conexion a la base de datos");    
             try {        
                 if(st != null) st.close();                
                 if(cn !=null) cn.close();
             } catch (SQLException ex) {
                 JOptionPane.showMessageDialog(null,ex.getMessage()); 
             }
         }//finally 
            
           // System.out.println("filas: "+cantFilas);
            if (cantFilas == 0){
                mat=null;
                mat = new String[1][cantColumnas];
                
                for (int j = 0; j < mat[0].length; j++) {
                     mat[0][j]="NO DATA";
                }
           }
return mat;            
}
     
     //CODIGO PARA GUARDAR RELACIONES DE PEDIDO>FLETE>COMPRA PROVEEDOR
         public void guardaRelOperaciones(List<String> dots){
     Connection cn = con2.conexion();
            PreparedStatement pps=null;
            String SQL="";        
                SQL="INSERT INTO relcomprapedido (id_compraProveed,id_pedidoCli,cantidadCajasRel,tipoMercanRel,id_fleteP,precioAjust) VALUES (?,?,?,?,?,?)";                           
            try {
                pps = cn.prepareStatement(SQL);
                pps.setString(1, dots.get(0));
                pps.setString(2,dots.get(1));
                pps.setString(3,dots.get(2));
                pps.setString(4,dots.get(3));
                pps.setString(5, dots.get(4));
                pps.setString(6, dots.get(5));
                pps.executeUpdate();
                JOptionPane.showMessageDialog(null, "Asignacion Guardada correctamente");
            } catch (SQLException ex) {
                Logger.getLogger(controladorCFP.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, "Error durante la transaccion.");
            }finally{
//               System.out.println( "finally detail->cierra conexion a la base de datos" );    
                try {
                    if(pps != null) pps.close();                
                    if(cn !=null) cn.close();
                    } catch (SQLException ex) {
                     JOptionPane.showMessageDialog(null,ex.getMessage() );    
                    }
            }//finally catch
} //guardaRelOperaciones

     //VALIDAR SI FLETE,PEDIDO Y COMPRA YA ESTAN ASIGNADOS EN LA TABLA relcomprapedido
        public boolean validaRelCompPed(String idBusq,String camp){
            Connection cn = con2.conexion();
            boolean existe =false;
            int num=0,i=1;
            String sql = "";
            switch(camp){
                case "id_compraProveed":
                    sql = "SELECT '1' FROM relcomprapedido WHERE id_compraProveed = '"+idBusq+"';";
                break;
                case "id_pedidoCli":
                    sql = "SELECT '1' FROM relcomprapedido WHERE id_pedidoCli = '"+idBusq+"';";
                break;
                case "id_fleteP":
                    sql = "SELECT '1' FROM relcomprapedido WHERE id_fleteP = '"+idBusq+"';";
                break;
                case "fleteenviado":
                    sql = "SELECT '1' FROM fleteenviado WHERE id_fleteE = '"+idBusq+"';";
                break;
                case "compramayoreo":
                    sql = "SELECT '1' FROM compramayoreo WHERE id_compraProveMin = '"+idBusq+"';";
                break;
            };
            Statement st = null;
            ResultSet rs= null;
            try {
                st = cn.createStatement();
                rs = st.executeQuery(sql);
                rs.beforeFirst();
                if(rs.next())
                {
                    if(rs.getRow() > 0){
                        existe =true;
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(controladorCFP.class.getName()).log(Level.SEVERE, null, ex);
            }finally{
                        try {
                            if(cn != null) cn.close();
                        } catch (SQLException ex) {
                            System.err.println( ex.getMessage() );    
                        }
                    }
           return existe;
    }
        
        //CARGA VISTA DE ESTADOS DE CARGA FLETES
        public String[][] matFletEstados(String fech){
        Connection cn = con2.conexion();
          String sql ="",aux;
              sql = "SELECT\n" +
                "	relCompraPedido.id_fleteP,COUNT(DISTINCT(relCompraPedido.id_compraProveed)) AS 'CompCont',\n" +
                "	COUNT(DISTINCT(relCompraPedido.id_pedidoCli)) AS 'llEVAPED',\n" +
                "	SUM(relCompraPedido.cantidadCajasRel)\n" +
                "FROM\n" +
                "	relcomprapedido\n" +
                "INNER JOIN\n" +
                "	fleteenviado\n" +
                "ON\n" +
                " 	relcomprapedido.id_fleteP = fleteenviado.id_fleteE AND fleteenviado.fechaFlete='"+fech+"'\n" +
                " 	GROUP BY relcomprapedido.id_fleteP;";      
             int i =0,cantFilas=0, cont=1,cantColumnas=0;
             String[][] mat=null;
              int[] arrIdPedido = null;//int para usar hashMap
            Statement st = null;
            ResultSet rs = null;            
            try {
                st = cn.createStatement();
                rs = st.executeQuery(sql);
                cantColumnas = rs.getMetaData().getColumnCount();
               if(rs.last()){//Nos posicionamos al final
                    cantFilas = rs.getRow();//sacamos la cantidad de filas/registros
                    rs.beforeFirst();//nos posicionamos antes del inicio (como viene por defecto)
                }
               mat = new String[cantFilas][cantColumnas];
               //aqui iria crear matriz
                while(rs.next())
                {//es necesario el for para llenar dinamicamente la lista, ya que varia el numero de columnas de las tablas
                 
                      for (int x=1;x<= rs.getMetaData().getColumnCount();x++) {
                           // System.out.print("| "+rs.getString(x)+" |");
                             mat[i][x-1]=rs.getString(x);
                      //System.out.print(x+" -> "+rs.getString(x));                   
                      }//for
                       i++;
                }//whilE
            } catch (SQLException ex) {
                Logger.getLogger(controladorCFP.class.getName()).log(Level.SEVERE, null, ex);
            }finally{               
//             System.out.println("cierra conexion a la base de datos");    
             try {        
                 if(st != null) st.close();                
                 if(cn !=null) cn.close();
             } catch (SQLException ex) {
                 JOptionPane.showMessageDialog(null,ex.getMessage()); 
             }
         }//finally        
           if (cantFilas == 0){
                mat=null;
                mat = new String[1][cantColumnas];
                
                for (int j = 0; j < mat[0].length; j++) {
                     mat[0][j]="NO DATA";
                }
           }
return mat;            
}
        
       public String calcAsignAPed(String idPed, String idProd,String camp){
        Connection cn = con2.conexion();
        String prod = "";
        String sql = "SELECT SUM(cantidadCajasRel) FROM relcomprapedido WHERE "+camp+"  = '"+idPed+"' AND tipoMercanRel = '"+idProd+"';";
        Statement st = null;
        ResultSet rs= null;
        try {
            st = cn.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                prod = rs.getString(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(controladorCFP.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
                    try {
                        if(cn != null) cn.close();
                    } catch (SQLException ex) {
                        System.err.println( ex.getMessage() );    
                    }
                }
        return prod;
    }
        
  public Integer[] fnToArray(ArrayList<Integer> alDatos){
      
      Integer[] arrResult = new Integer[alDatos.size()];
      
      for(int iIndex=0; iIndex <alDatos.size();iIndex++ ){
          arrResult[iIndex] = alDatos.get(iIndex);
      }
      return arrResult;
  }
  
  //filtros de detalle pedido en asignacion
  public String[][] matPedidosEst(String id){
        Connection cn = con2.conexion();
          String sql ="",aux;
              sql = "SELECT relcomprapedido.id_relacionCP,relcomprapedido.id_fleteP,relcomprapedido.id_compraProveed,\n" +
                "	IF(proveedor.nombreP = 'SUBASTA',compraprooved.descripcionSubasta,proveedor.nombreP) AS condic,\n" +
                "	productocal.nombreP,relcomprapedido.cantidadCajasRel,relcomprapedido.precioAjust,\n" +
                "	(relcomprapedido.cantidadCajasRel*relcomprapedido.precioAjust) AS TOT\n" +
                "FROM\n" +
                "	relcomprapedido\n" +
                "INNER JOIN\n" +
                "	productoCal\n" +
                "ON\n" +
                "	productoCal.codigo = relcomprapedido.tipoMercanRel AND relcomprapedido.id_pedidoCli = '"+id+"'\n" +
                "INNER JOIN \n" +
                "	compraprooved\n" +
                "ON \n" +
                "	relcomprapedido.id_compraProveed = compraprooved.id_compraProve\n" +
                "INNER JOIN \n" +
                "	proveedor\n" +
                "ON \n" +
                "	compraprooved.id_ProveedorC = proveedor.id_Proveedor;";      
             int i =0,cantFilas=0, cont=1,cantColumnas=0;
             String[][] mat=null;
              int[] arrIdPedido = null;//int para usar hashMap
            Statement st = null;
            ResultSet rs = null;            
            try {
                st = cn.createStatement();
                rs = st.executeQuery(sql);
                cantColumnas = rs.getMetaData().getColumnCount();
               if(rs.last()){//Nos posicionamos al final
                    cantFilas = rs.getRow();//sacamos la cantidad de filas/registros
                    rs.beforeFirst();//nos posicionamos antes del inicio (como viene por defecto)
                }
               mat = new String[cantFilas][cantColumnas];
               //aqui iria crear matriz
                while(rs.next())
                {//es necesario el for para llenar dinamicamente la lista, ya que varia el numero de columnas de las tablas
                 
                      for (int x=1;x<= rs.getMetaData().getColumnCount();x++) {
                           // System.out.print("| "+rs.getString(x)+" |");
                             mat[i][x-1]=rs.getString(x);
                      //System.out.print(x+" -> "+rs.getString(x));                   
                      }//for
                       i++;
                }//whilE
            } catch (SQLException ex) {
                Logger.getLogger(controladorCFP.class.getName()).log(Level.SEVERE, null, ex);
            }finally{               
//             System.out.println("cierra conexion a la base de datos");    
             try {        
                 if(st != null) st.close();                
                 if(cn !=null) cn.close();
             } catch (SQLException ex) {
                 JOptionPane.showMessageDialog(null,ex.getMessage()); 
             }
         }//finally        
           if (cantFilas == 0){
                mat=null;
                mat = new String[1][cantColumnas];
                
                for (int j = 0; j < mat[0].length; j++) {
                     mat[0][j]="NO DATA";
                }
           }
return mat;            
}
    
//*filtros buscar pedidos*/
        public String[][] regresaPays(String idCli,String tipe){
        Connection cn = con2.conexion();
          String sql ="",aux;
          if(tipe.equals("pagopedidocli")){//OPCION pago de pedididoscli
              sql = "SELECT pagopedidocli.id_payPedCli,pagopedidocli.fechapayCliente,pagopedidocli.montoPayCliente,\n" +
                "	pagopedidocli.notaPayCliente,modopago.nombrePay\n" +
                "FROM \n" +
                "	pagopedidocli\n" +
                "INNER JOIN \n" +
                "	modopago\n" +
                "ON\n" +
                "	pagopedidocli.modoPayCliente = modopago.cod_pago AND pagopedidocli.idClientePay = '"+idCli+"';";      
          }
          if(tipe.equals("pagarcompraprovee")){//OPCION pago de pedididoscli
              sql = "SELECT pagarcompraprovee.id_paycompra,pagarcompraprovee.fechpayProveed,pagarcompraprovee.montoPayProveed,\n" +
                "	pagarcompraprovee.notaPayProveed,modopago.nombrePay\n" +
                "FROM \n" +
                "	pagarcompraprovee\n" +
                "INNER JOIN \n" +
                "	modopago\n" +
                "ON\n" +
                "	pagarcompraprovee.modoPayProveed = modopago.cod_pago AND pagarcompraprovee.num_compraProveed = '"+idCli+"';";      
          }
         if(tipe.equals("pagocreditprooved")){//OPCION pagoS de cREDIIT PROVEEDOR
              sql = "SELECT pagoCreditProoved.id_pay,pagoCreditProoved.fechapayProoved,pagoCreditProoved.montoPayProoved,\n" +
                "	pagoCreditProoved.notaPayProoved,modopago.nombrePay\n" +
                "FROM \n" +
                "	pagoCreditProoved\n" +
                "INNER JOIN \n" +
                "	modopago\n" +
                "ON\n" +
                "	pagoCreditProoved.modoPayProoved = modopago.cod_pago AND pagoCreditProoved.idProovedPay = '"+idCli+"';";      
          }
          if(tipe.equals("pagoflete")){//OPCION pagoS de cREDIIT PROVEEDOR
              sql = "SELECT pagoflete.id_pagoFlete,pagoflete.fechaPayFlete,pagoflete.montoPayFl,\n" +
                "	pagoflete.notaPayFlete,modopago.nombrePay\n" +
                "FROM \n" +
                "	pagoflete\n" +
                "INNER JOIN \n" +
                "	modopago\n" +
                "ON\n" +
                "	pagoflete.modPay = modopago.cod_pago AND pagoflete.id_fleteEnv = '"+idCli+"';";      
          }
           if(tipe.equals("pagoventapiso")){//OPCION pagoS de cREDIIT PROVEEDOR
              sql = "SELECT pagoventapiso.id_payVentaP,pagoventapiso.fechaPayVP,pagoventapiso.montoVP,\n" +
                "	pagoventapiso.notaPayVP,modopago.nombrePay\n" +
                "FROM \n" +
                "	pagoventapiso\n" +
                "INNER JOIN \n" +
                "	modopago\n" +
                "ON\n" +
                "	pagoventapiso.method_payVP = modopago.cod_pago AND pagoventapiso.num_notaVP = '"+idCli+"';";      
          }

             int i =0,cantFilas=0, cont=1;
             String[][] mat=null, mat2=null;
              int[] arrIdPedido = null;//int para usar hashMap
            Statement st = null;
            ResultSet rs = null;    
            int cantColumnas=0;
            try {
                st = cn.createStatement();
                rs = st.executeQuery(sql);
                cantColumnas = rs.getMetaData().getColumnCount();
               if(rs.last()){//Nos posicionamos al final
                    cantFilas = rs.getRow();//sacamos la cantidad de filas/registros
                    rs.beforeFirst();//nos posicionamos antes del inicio (como viene por defecto)
                }
               mat = new String[cantFilas][cantColumnas];
               //aqui iria crear matriz
                while(rs.next())
                {//es necesario el for para llenar dinamicamente la lista, ya que varia el numero de columnas de las tablas
                 
                      for (int x=1;x<= rs.getMetaData().getColumnCount();x++) {
                           // System.out.print("| "+rs.getString(x)+" |");
                             mat[i][x-1]=rs.getString(x);
                      //System.out.print(x+" -> "+rs.getString(x));                   
                      }//for
                       i++;
                }//whilE
            } catch (SQLException ex) {
                Logger.getLogger(controladorCFP.class.getName()).log(Level.SEVERE, null, ex);
            }finally{               
//             System.out.println("cierra conexion a la base de datos");    
             try {        
                 if(st != null) st.close();                
                 if(cn !=null) cn.close();
             } catch (SQLException ex) {
                 JOptionPane.showMessageDialog(null,ex.getMessage()); 
             }
         }//finally 
            
           // System.out.println("filas: "+cantFilas);
            if (cantFilas == 0){
                mat=null;
                mat = new String[1][cantColumnas];
                
                for (int j = 0; j < mat[0].length; j++) {
                     mat[0][j]="NO DATA";
                }
           }
return mat;            
}//regresaPays  
  
//*filtros buscar pedidos*/
        public String[][] regresaPaysFech(String tipe,String fech){
        Connection cn = con2.conexion();
          String sql ="",aux;
          if(tipe.equals("pagopedidocli")){//OPCION pago de pedididoscli
              sql = "SELECT pagopedidocli.idClientePay,pagopedidocli.fechapayCliente,pagopedidocli.montoPayCliente,\n" +
                "	pagopedidocli.notaPayCliente,modopago.nombrePay\n" +
                "FROM \n" +
                "	pagopedidocli\n" +
                "INNER JOIN \n" +
                "	modopago\n" +
                "ON\n" +
                "	pagopedidocli.modoPayCliente = modopago.cod_pago AND pagopedidocli.fechapayCliente = '"+fech+"';";      
          }
          if(tipe.equals("pagoventapiso")){//OPCION pago de ventaspiso
              sql = "SELECT pagoventapiso.num_notaVP,pagoventapiso.fechaPayVP,pagoventapiso.montoVP,\n" +
                "	pagoventapiso.notaPayVP,modopago.nombrePay\n" +
                "FROM \n" +
                "	pagoventapiso\n" +
                "INNER JOIN \n" +
                "	modopago\n" +
                "ON\n" +
                "	pagoventapiso.method_payVP = modopago.cod_pago AND pagoventapiso.fechaPayVP = '"+fech+"';";      
          }
          if(tipe.equals("pagocreditprooved")){//OPCION pago de credito a proveedor
              sql = "SELECT pagoCreditProoved.idProovedPay,pagoCreditProoved.fechapayProoved,pagoCreditProoved.montoPayProoved,\n" +
                "	pagoCreditProoved.notaPayProoved,modopago.nombrePay\n" +
                "FROM \n" +
                "	pagoCreditProoved\n" +
                "INNER JOIN \n" +
                "	modopago\n" +
                "ON\n" +
                "	pagoCreditProoved.modoPayProoved = modopago.cod_pago AND pagoCreditProoved.fechapayProoved = '"+fech+"';";      
          }
          if(tipe.equals("pagoflete")){//OPCION pago de fletes
              sql = "SELECT pagoflete.id_fleteEnv,pagoflete.fechaPayFlete,pagoflete.montoPayFl,\n" +
                "	pagoflete.notaPayFlete,modopago.nombrePay\n" +
                "FROM \n" +
                "	pagoflete\n" +
                "INNER JOIN \n" +
                "	modopago\n" +
                "ON\n" +
                "	pagoflete.modPay = modopago.cod_pago AND pagoflete.fechaPayFlete = '"+fech+"';";      
          }
          if(tipe.equals("pagarcompraprovee")){//OPCION pago de fletes
              sql = "SELECT pagarcompraprovee.num_compraProveed,pagarcompraprovee.fechpayProveed,pagarcompraprovee.montoPayProveed,\n" +
                "	pagarcompraprovee.notaPayProveed,modopago.nombrePay\n" +
                "FROM \n" +
                "	pagarcompraprovee\n" +
                "INNER JOIN \n" +
                "	modopago\n" +
                "ON\n" +
                "	pagarcompraprovee.modoPayProveed = modopago.cod_pago AND pagarcompraprovee.fechpayProveed = '"+fech+"';";      
          }
          
             int i =0,cantFilas=0, cont=1;
             String[][] mat=null, mat2=null;
              int[] arrIdPedido = null;//int para usar hashMap
            Statement st = null;
            ResultSet rs = null;    
            int cantColumnas=0;
            try {
                st = cn.createStatement();
                rs = st.executeQuery(sql);
                cantColumnas = rs.getMetaData().getColumnCount();
               if(rs.last()){//Nos posicionamos al final
                    cantFilas = rs.getRow();//sacamos la cantidad de filas/registros
                    rs.beforeFirst();//nos posicionamos antes del inicio (como viene por defecto)
                }
               mat = new String[cantFilas][cantColumnas];
               //aqui iria crear matriz
                while(rs.next())
                {//es necesario el for para llenar dinamicamente la lista, ya que varia el numero de columnas de las tablas
                 
                      for (int x=1;x<= rs.getMetaData().getColumnCount();x++) {
                           // System.out.print("| "+rs.getString(x)+" |");
                             mat[i][x-1]=rs.getString(x);
                      //System.out.print(x+" -> "+rs.getString(x));                   
                      }//for
                       i++;
                }//whilE
            } catch (SQLException ex) {
                Logger.getLogger(controladorCFP.class.getName()).log(Level.SEVERE, null, ex);
            }finally{               
//             System.out.println("cierra conexion a la base de datos");    
             try {        
                 if(st != null) st.close();                
                 if(cn !=null) cn.close();
             } catch (SQLException ex) {
                 JOptionPane.showMessageDialog(null,ex.getMessage()); 
             }
         }//finally 
            
           // System.out.println("filas: "+cantFilas);
            if (cantFilas == 0){
                mat=null;
                mat = new String[1][cantColumnas];
                
                for (int j = 0; j < mat[0].length; j++) {
                     mat[0][j]="NO DATA";
                }
           }
return mat;            
}//regresaPays  
        
     public String totalCreditProv(String credit){//regresa el total de la compra a proveedor
        Connection cn = con2.conexion();
        String prod = "";
        String sql = "SELECT\n" +
                "	SUM(detallecreditproveed.cantidadCP*detallecreditproveed.costoCP) AS coste\n" +
                "FROM\n" +
                "	detallecreditproveed\n" +
                "WHERE\n" +
                " 	detallecreditproveed.num_creditCP = '"+credit+"';";
        Statement st = null;
        ResultSet rs= null;
        try {
            st = cn.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                prod = rs.getString(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(controladorCFP.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
                    try {
                        if(cn != null) cn.close();
                    } catch (SQLException ex) {
                        System.err.println( ex.getMessage() );    
                    }
                }
        return prod;
    }//EndtotalCreditProv
     
     public String sumAbonos(String param,String id){
        Connection cn = con2.conexion();
          Object suma ="";
           String sql ="";
           switch (param){
               case "pagarcompraprovee":
                     sql = "SELECT SUM(montoPayProveed) FROM pagarcompraprovee WHERE num_compraProveed = '"+id+"'";           
                   break;
               case "pagoventapiso":
                     sql = "SELECT SUM(montoVP) FROM pagoventapiso WHERE num_notaVP = '"+id+"'";          
                   break;
               case "pagopedidocli":
                   sql = "SELECT SUM(montoPayCliente) FROM pagopedidocli WHERE idClientePay = '"+id+"'";    
                   break;
               case "pagocreditprooved":
                    sql = "SELECT SUM(montoPayProoved) FROM pagocreditprooved WHERE idProovedPay = '"+id+"'";           
                   break;
               case "pagoflete":
                   sql = "SELECT SUM(montoPayFl) FROM pagoflete WHERE id_fleteenv = '"+id+"'";
                   break;
           };
            Statement st = null;
            ResultSet rs = null;            
            try {
                st = cn.createStatement();
                rs = st.executeQuery(sql);
                while(rs.next())
                {//es necesario el for para llenar dinamicamente la lista, ya que varia el numero de columnas de las tablas
                    suma=rs.getString(1);
                }//while
            } catch (SQLException ex) {
                Logger.getLogger(controladorCFP.class.getName()).log(Level.SEVERE, null, ex);
            }finally{               
//             System.out.println("cierra conexion a la base de datos");    
             try {        
                 if(st != null) st.close();                
                 if(cn !=null) cn.close();
             } catch (SQLException ex) {
                 JOptionPane.showMessageDialog(null,ex.getMessage()); 
             }
         }//finally  
             if(suma != null && !suma.toString().isEmpty() ){
                 return suma.toString();
            }else{
                 suma = "0.0";
             }
            return suma.toString();
    }//regresaDatos

     //codigo para proveedor mayorista
    public void guardaMayorista(String idP,String idC,String fech){
     Connection cn = con2.conexion();
            PreparedStatement pps=null;
            String SQL="";        
                SQL="INSERT INTO compramayoreo (id_ProveedorMay,id_compraProveMin,fechaRel) VALUES (?,?,?)";                           
            try {
                pps = cn.prepareStatement(SQL);
                pps.setString(1, idP);
                pps.setString(2,idC);
                pps.setString(3,fech);
                pps.executeUpdate();
                JOptionPane.showMessageDialog(null, "Asignacion relizada correctamente");
            } catch (SQLException ex) {
                Logger.getLogger(controladorCFP.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, "Error durante la transaccion.");
            }finally{
 //               System.out.println( "cierra conexion a la base de datos" );    
                try {
                    if(pps != null) pps.close();                
                    if(cn !=null) cn.close();
                    } catch (SQLException ex) {
                     JOptionPane.showMessageDialog(null,ex.getMessage() );    
                    }
            }//finally catch
} //guardacompraMayorista
    
  //filtros de detalle pedido en asignacion
  public String[][] matAsignaMayoristas(String id, String fech){
        Connection cn = con2.conexion();
          String sql ="",aux;
          
         // sql = "SELECT * FROM compramayoreo WHERE id_ProveedorMay = '"+id+"' AND fechaRel = '"+fech+"'";                
         sql = "SELECT compramayoreo.id_compraMay,proveedor.nombreP,compramayoreo.id_compraProveMin,\n" +
                "	compramayoreo.fechaRel\n" +
                "FROM\n" +
                "	compramayoreo\n" +
                "INNER JOIN\n" +
                "	proveedor\n" +
                "ON \n" +
                "	proveedor.id_Proveedor = compramayoreo.id_ProveedorMay \n" +
                "AND \n" +
                "	compramayoreo.fechaRel = '"+fech+"'\n" +
                "AND \n" +
                "	compramayoreo.id_ProveedorMay = '"+id+"';";
              int i =0,cantFilas=0, cont=1,cantColumnas=0;
             String[][] mat=null;
              int[] arrIdPedido = null;//int para usar hashMap
            Statement st = null;
            ResultSet rs = null;            
            try {
                st = cn.createStatement();
                rs = st.executeQuery(sql);
                cantColumnas = rs.getMetaData().getColumnCount();
               if(rs.last()){//Nos posicionamos al final
                    cantFilas = rs.getRow();//sacamos la cantidad de filas/registros
                    rs.beforeFirst();//nos posicionamos antes del inicio (como viene por defecto)
                }
               mat = new String[cantFilas][cantColumnas];
               //aqui iria crear matriz
                while(rs.next())
                {//es necesario el for para llenar dinamicamente la lista, ya que varia el numero de columnas de las tablas
                 
                      for (int x=1;x<= rs.getMetaData().getColumnCount();x++) {
                           // System.out.print("| "+rs.getString(x)+" |");
                             mat[i][x-1]=rs.getString(x);
                      //System.out.print(x+" -> "+rs.getString(x));                   
                      }//for
                       i++;
                }//whilE
            } catch (SQLException ex) {
                Logger.getLogger(controladorCFP.class.getName()).log(Level.SEVERE, null, ex);
            }finally{               
//             System.out.println("cierra conexion a la base de datos");    
             try {        
                 if(st != null) st.close();                
                 if(cn !=null) cn.close();
             } catch (SQLException ex) {
                 JOptionPane.showMessageDialog(null,ex.getMessage()); 
             }
         }//finally        
           if (cantFilas == 0){
                mat=null;
                mat = new String[1][cantColumnas];
                
                for (int j = 0; j < mat[0].length; j++) {
                     mat[0][j]="NO DATA";
                }
           }
return mat;            
}//asignaMayoristas
  
  //valida existencia de asigancion a mayoristas
        public boolean validaCompAsignadas(String idBusq,String fech,String camp){
            Connection cn = con2.conexion();
            boolean existe =false;
            int num=0,i=1;
            String sql = "";
            switch(camp){
                case "comp+fech"://validara si el pedido ya ha sido asignado 
                             sql = "SELECT '1' FROM compramayoreo WHERE id_compraProveMin = '"+idBusq+"' AND fechaRel = '"+fech+"'; ";
                break;
                case "idCli+fech":
                             sql = "SELECT '1' FROM relcomprapedido WHERE id_pedidoCli = '"+idBusq+"' ;";
                break;
            };
            Statement st = null;
            ResultSet rs= null;
            try {
                st = cn.createStatement();
                rs = st.executeQuery(sql);
                rs.beforeFirst();
                if(rs.next())
                {
                    if(rs.getRow() > 0){
                        existe =true;
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(controladorCFP.class.getName()).log(Level.SEVERE, null, ex);
            }finally{
                        try {
                            if(cn != null) cn.close();
                        } catch (SQLException ex) {
                            System.err.println( ex.getMessage() );    
                        }
                    }
           return existe;
    }//validaComprasSAsignadas
     
        
//Validara si el proveedor es mayorista para asignarle las compras de minorista
        public boolean validaIsMayorista(String idMay){
            Connection cn = con2.conexion();
            boolean existe =false;
            int num=0,i=1;
            String sql = "";
            sql = "SELECT '1' FROM proveedor WHERE id_Proveedor = '"+idMay+"' AND tipo = 1";

            Statement st = null;
            ResultSet rs= null;
            try {
                st = cn.createStatement();
                rs = st.executeQuery(sql);
                rs.beforeFirst();
                if(rs.next())
                {
                    if(rs.getRow() > 0){
                        existe =true;
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(controladorCFP.class.getName()).log(Level.SEVERE, null, ex);
            }finally{
                        try {
                            if(cn != null) cn.close();
                        } catch (SQLException ex) {
                            System.err.println( ex.getMessage() );    
                        }
                    }
           return existe;
    }//validaProveedorMayorista
        
//regresa duma de pedidos asignados a mayoristas
        public String sumMayorista(String idMay,String fech){
            Connection cn = con2.conexion();
            String suma ="";
            int num=0,i=1;
            String sql = "";
            sql = "SELECT SUM(detailcompraprooved.cantCajasC * detailcompraprooved.precCajaC) AS suma\n" +
                "FROM\n" +
                "	detailcompraprooved\n" +
                "INNER JOIN\n" +
                "	compraprooved\n" +
                "ON \n" +
                "	compraprooved.id_compraProve = detailcompraprooved.id_compraP\n" +
                "INNER JOIN\n" +
                "	compramayoreo\n" +
                "ON\n" +
                "	compramayoreo.id_compraProveMin = compraprooved.id_compraProve\n" +
                "AND \n" +
                "	compramayoreo.fechaRel = '"+fech+"'\n" +
                "AND \n" +
                "	compramayoreo.id_ProveedorMay = '"+idMay+"';";
            Statement st = null;
            ResultSet rs= null;
            try {
                st = cn.createStatement();
                rs = st.executeQuery(sql);
                rs.beforeFirst();
                while(rs.next())
                {//es necesario el for para llenar dinamicamente la lista, ya que varia el numero de columnas de las tablas
                    suma=rs.getString(1);
                }//while
            } catch (SQLException ex) {
                Logger.getLogger(controladorCFP.class.getName()).log(Level.SEVERE, null, ex);
            }finally{
                        try {
                            if(cn != null) cn.close();
                        } catch (SQLException ex) {
                            System.err.println( ex.getMessage() );    
                        }
                    }
           return suma;
    }//validaProveedorMayorista
        
      public void respButton(){
                  int resp;
        JFileChooser RealizarBackupMySQL = new JFileChooser();
        resp=RealizarBackupMySQL.showSaveDialog(null);//JFileChooser de nombre RealizarBackupMySQL
        if (resp==JFileChooser.APPROVE_OPTION) {//Si el usuario presiona aceptar; se genera el Backup
        try{
        Runtime runtime = Runtime.getRuntime();
        File backupFile = new File(String.valueOf(RealizarBackupMySQL.getSelectedFile().toString())+".sql");
        InputStreamReader irs;
        BufferedReader br;
            try (FileWriter fw = new FileWriter(backupFile)) {
                Process child = runtime.exec("C:\\xampp\\mysql\\bin\\mysqldump --routines=TRUE --password=0ehn4TNU5I --user=root --databases admindcr");// | gzip> respadmin_DCR.sql.gz
                irs = new InputStreamReader(child.getInputStream());
                br = new BufferedReader(irs);
                String line;
                while( (line=br.readLine()) != null ) {
                    fw.write(line + "\n");
                }   }
        irs.close();
        br.close();

        JOptionPane.showMessageDialog(null, "Archivo generado correctamente.","Verificar",JOptionPane. INFORMATION_MESSAGE);
        }catch(Exception e){
        JOptionPane.showMessageDialog(null, "Error no se genero el archivo por el siguiente motivo:"+e.getMessage(), "Verificar",JOptionPane.ERROR_MESSAGE);
        }
        //JOptionPane.showMessageDialog(null, "Archivogenerado","Verificar",JOptionPane.INFORMAT ION_MESSAGE);
        } else if (resp==JFileChooser.CANCEL_OPTION) {
        JOptionPane.showMessageDialog(null,"Ha sido cancelada la generación del Backup.");
        }        
      }//respaldoButton
      
        public String cargaConfig() {//List
                File archivo = null;
                FileReader fr = null;
                BufferedReader br = null;
                 String ruta="C:/adminDCR/respaldo.txt";//2700 los 4; solo dos: 1850del, 1450traseros, 
                 //List<String> contentL=new ArrayList<String>();
                 String cadena="",rgresa="";  
                try {
                 // Apertura del fichero y creacion de BufferedReader para poder
                 // hacer una lectura comoda (disponer del metodo readLine()).     
                fr = new FileReader(ruta);
                br = new BufferedReader(fr);
                while((cadena = br.readLine())!=null) {
                    //contentL.add(cadena.trim());
                    rgresa=cadena.trim();
                }
                br.close();
                }catch(Exception e){
                 e.printStackTrace();
              }finally{
                 // En el finally cerramos el fichero, para asegurarnos
                 // que se cierra tanto si todo va bien como si salta 
                 // una excepcion.
                 try{                    
                    if( null != fr){   
                       fr.close();     
                    }                  
                 }catch (Exception e2){ 
                    e2.printStackTrace();
                 }
              }

                return rgresa;
    }
          
              public void insertaCamposUsers( List<String> param){
            Connection cn = con2.conexion();
            PreparedStatement pps=null;
            String SQL="";        
                SQL="INSERT INTO usersdcr (nombreUser,apellidosUser,nickName,correo,telef,passw,fechAlta) VALUES (?,?,?,?,?,?,?)";                           
            try {
                pps = cn.prepareStatement(SQL);
                pps.setString(1, param.get(0));
                pps.setString(2, param.get(1));
                pps.setString(3, param.get(2));
                pps.setString(4, param.get(3));
                pps.setString(5, param.get(4));               
                pps.setString(6, param.get(5));               
                pps.setString(7, param.get(6));               
                
                pps.executeUpdate();
                JOptionPane.showMessageDialog(null, "Datos de Usuario guardados correctamente.");
            } catch (SQLException ex) {
                Logger.getLogger(controladorCFP.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, "Error durante la transaccion.");
            }finally{
 //               System.out.println( "cierra conexion a la base de datos" );    
                try {
                    if(pps != null) pps.close();                
                    if(cn !=null) cn.close();
                    } catch (SQLException ex) {
                     JOptionPane.showMessageDialog(null,ex.getMessage() );    
                    }
            }//finally catch
        }//inserta usuarios login
        
        public boolean validaLoginUsers(String user, String pass){
            Connection cn = con2.conexion();
            boolean existe =false;
            int num=0,i=1;
            String sql = "";
            sql = "SELECT '1' FROM usersdcr WHERE nickName = '"+user+"' AND passw = '"+pass+"'";
            Statement st = null;
            ResultSet rs= null;
            try {
                st = cn.createStatement();
                rs = st.executeQuery(sql);
                rs.beforeFirst();
                if(rs.next())
                {
                    if(rs.getRow() > 0){
                        existe =true;
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(controladorCFP.class.getName()).log(Level.SEVERE, null, ex);
            }finally{
                        try {
                            if(cn != null) cn.close();
                        } catch (SQLException ex) {
                            System.err.println( ex.getMessage() );    
                        }
                    }
           return existe;
    }//validaloginUsers
              
    public static void main(String[] argv){
       controladorCFP control = new controladorCFP();
       List<String> contentL= control.regresaDatos(1,"4");
       ListIterator<String> itr=contentL.listIterator();
       
       String[][] mat = control.matFletEstados("2020-01-29");
        /*for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat[0].length; j++) {
                System.out.print("{"+mat[i][j]+"]");
            }
            System.out.println();
        }
       */
        System.out.println("ASIGNADAS \n"+control.cargaConfig());
    }//main
}//class
