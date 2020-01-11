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
import java.sql.Date;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
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
                SQL="INSERT INTO proveedor (nombreP,apellidosP,localidadP,fechaAltaP,telefonoP) VALUES (?,?,?,?,?)";                           
            try {
                pps = cn.prepareStatement(SQL);
                pps.setString(1, param.get(0));
                pps.setString(2, param.get(1));
                pps.setString(3, param.get(2));
                pps.setString(4, param.get(3));
                pps.setString(5, param.get(4));               
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
                SQL="UPDATE proveedor SET nombreP =?, apellidosP=?, localidadP=?, statusP=?, fechaAltaP=?, telefonoP=? WHERE id_Proveedor = '"+id+"' ";                           
            try {
                pps = cn.prepareStatement(SQL);
                pps.setString(1, param.get(0));
                pps.setString(2, param.get(1));
                pps.setString(3, param.get(2));
                pps.setInt(4, Integer.parseInt(param.get(3)));
                pps.setString(5, param.get(4));
                pps.setString(6, param.get(5));
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
        public String[][] matrizPedidos(String fech){
        Connection cn = con2.conexion();
          String sql ="",aux;
              sql = "SELECT\n" +
                    "	pedidocliente.id_pedido,pedidocliente.fechaPedidio,pedidocliente.status,\n" +
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
return mat;            
}

/*filtros buscar pedidos*/
        public String[][] matrizPedidosB(int opcBusq,String idCli,String fech1,String fech2){
        Connection cn = con2.conexion();
          String sql ="",aux;
/*SELECT\n" +
"	pedidoCliente.id_pedido,pedidoCliente.fechaPedidio,pedidoCliente.status,pedidoCliente.totalPrecio,\n" +
"	clientePedidos.id_cliente,clientePedidos.nombre,\n" +
"	SUM(detailPedidio.cantidadCajas),pedidoCliente.notaPed\n" +
"FROM\n" +
"	clientePedidos\n" +
"INNER JOIN\n" +
"	pedidoCliente\n" +
"ON\n" +
"	clientePedidos.id_cliente = pedidoCliente.id_clienteP AND pedidoCliente.id_clienteP = '"+idCli+"'  \n" +
"INNER JOIN\n" +
"	detailPedidio \n" +
"ON\n" +
"	pedidoCliente.id_pedido = detailPedidio.id_PedidioD \n" +
"GROUP BY pedidoCliente.id_pedido;*/        
          if(opcBusq==0){//OPCION BUSQUEDA DE NOMBRE
              sql = "SELECT\n" +
                    "	pedidocliente.id_pedido,pedidocliente.fechaPedidio,pedidocliente.status,\n" +
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
                    "	pedidocliente.id_pedido,pedidocliente.fechaPedidio,pedidocliente.status,\n" +
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
                    "	pedidocliente.id_pedido,pedidocliente.fechaPedidio,pedidocliente.status,\n" +
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
   

    public static void main(String[] argv){
       controladorCFP control = new controladorCFP();
        List<String> contentL= control.regresaDatos(1,"4");
       ListIterator<String> itr=contentL.listIterator();
     //  String[] arr=control.ultimoRegistroPedido();

      control.matrizPedidos("2020-01-09");
    }//main
}//class
