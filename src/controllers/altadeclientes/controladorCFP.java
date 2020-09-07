
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
   
//METODOS PARA LOGIN
           public int validaLoginUsers(String user, String pass){
            Connection cn = con2.conexion();
            int existe =-1;
            int num=0,i=1;
            String sql = "";
            sql = "SELECT id_user FROM usersdcr WHERE nickName = '"+user+"' AND passw = '"+pass+"'";
            Statement st = null;
            ResultSet rs= null;
            try {
                st = cn.createStatement();
                rs = st.executeQuery(sql);
                rs.beforeFirst();
                if(rs.next())
                {
                    if(rs.getRow() > 0){
                        existe = rs.getInt(1);
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
    
     public String[] getnombreUsuario(int id){
            Connection cn = con2.conexion();
            String[] idUser = new String[4];
            String sql = "";
            sql = "SELECT id_user,nickName,estado,turno FROM usersdcr WHERE id_user = '"+id+"'";
            Statement st = null;
            ResultSet rs= null;
            try {
                st = cn.createStatement();
                rs = st.executeQuery(sql);
                rs.beforeFirst();
                if(rs.next())
                {
                    if(rs.getRow() > 0){
                        idUser[0]=rs.getString(1);
                        idUser[1]=rs.getString(2);
                        idUser[2]=rs.getString(3);
                        idUser[3]=rs.getString(4);
                    }else{
                        for (int j = 0; j < idUser.length; j++) {
                            idUser[j] = "NO-DATA";
                        }
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
           return idUser;
    }//@endgetnombreUsuario
    
         public void GuardaTurno(List<String> param){
             java.util.Date date = new java.util.Date();
             String timeDate = new java.sql.Timestamp(date.getTime()).toString();
            Connection cn = con2.conexion();
            PreparedStatement pps=null;
            String SQL="";     
          SQL="INSERT INTO turnos (idusuario,saldo,finicial) VALUES (?,?,?)";                           
          try {
                pps = cn.prepareStatement(SQL);
                pps.setString(1, param.get(0));
                pps.setString(2, param.get(1));
                pps.setString(3,timeDate);
                pps.executeUpdate();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error durante creacion de Turno.");
                Logger.getLogger(controladorCFP.class.getName()).log(Level.SEVERE, null, ex);               
            }finally{
                try {
                    if(pps != null) pps.close();                
                    if(cn !=null) cn.close();
                    } catch (SQLException ex) {
                      JOptionPane.showMessageDialog(null, ex.getMessage() );    
                    }                
            }//finally catch  
          }//@endGuardaTurno
         
                   ///*** Obtener ultimo turno creado
    public int getenTurno(){
            Connection cn = con2.conexion();
            int idTurno = -1;
            String sql = "";
            sql = "SELECT id FROM turnos ORDER BY id DESC LIMIT 1;";
            Statement st = null;
            ResultSet rs= null;
            try {
                st = cn.createStatement();
                rs = st.executeQuery(sql);
                rs.beforeFirst();
                if(rs.next())
                {
                    if(rs.getRow() > 0){
                        idTurno = rs.getInt(1);
                    }else{
                         idTurno = -1;
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
           return idTurno;
    }//@endgetenTurno
         
                // Obtener ultimo turno creado
     public int getenTurno(int id){
            Connection cn = con2.conexion();
            int idTurno = -1;
            String sql = "";
            sql = "SELECT turnos.id FROM turnos\n" +
                    "INNER JOIN usersdcr\n" +
                    "on turnos.idusuario = usersdcr.id_user AND usersdcr.id_user = "+id+" and turnos.ffinal is null\n" +
                    "order by turnos.id DESC limit 1;";
            Statement st = null;
            ResultSet rs= null;
            try {
                st = cn.createStatement();
                rs = st.executeQuery(sql);
                rs.beforeFirst();
                if(rs.next())
                {
                    if(rs.getRow() > 0){
                        idTurno = rs.getInt(1);
                    }else{
                         idTurno = -1;
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
           return idTurno;
    }//@endgetenTurno
    
     //metodo para actualizar las cancelaciones en pagos_areas, pagos_amb,pagos_carg,otros_venta
       public void f5CancelTypesAll(String table,String opcColumn,String id, String val){
             Connection cn = con2.conexion();
            PreparedStatement pps=null;
            String SQL="";      
                SQL="UPDATE "+table+" SET "+opcColumn+" =? WHERE id = '"+id+"' ";        
            if(table.equals("usersdcr"))
                SQL="UPDATE "+table+" SET "+opcColumn+" =? WHERE id_user = '"+id+"' ";                           

            if(table.equals("turnos"))
                SQL="UPDATE "+table+" SET "+opcColumn+" =? WHERE id = '"+id+"' ";                           

            //Opciones para cancelar el pago de algun movivmiento
            if(table.equals("pagopedidocli"))
                SQL="UPDATE "+table+" SET "+opcColumn+" =? WHERE id_payPedCli = '"+id+"' ";        

            if(table.equals("pagocreditprooved"))
                SQL="UPDATE "+table+" SET "+opcColumn+" =? WHERE id_pay = '"+id+"' ";        

            if(table.equals("pagoventapiso"))
                SQL="UPDATE "+table+" SET "+opcColumn+" =? WHERE id_payVentaP = '"+id+"' ";        

            if(table.equals("pagoflete"))
                SQL="UPDATE "+table+" SET "+opcColumn+" =? WHERE id_pagoFlete = '"+id+"' ";   
            
            if(table.equals("pagarcompraprovee"))
                SQL="UPDATE "+table+" SET "+opcColumn+" =? WHERE id_paycompra = '"+id+"' ";   
         //actualizar compraprooved.statAsign.statAsign para sobras del dia
             if(table.equals("sobras_compraprooved"))
                SQL="UPDATE compraprooved  SET "+opcColumn+" =? WHERE id_compraProve = '"+id+"' ";   
            try {
                pps = cn.prepareStatement(SQL);
                pps.setString(1, val);
                pps.executeUpdate();
                if(table.equals("tarifas"))
                    JOptionPane.showMessageDialog(null, "Tarifa actualizada correctamente."+id);
            } catch (SQLException ex) {
                Logger.getLogger(controladorCFP.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, "Error durante la transaccion."+ex);
            }finally{
                try {
                    if(pps != null) pps.close();                
                    if(cn !=null) cn.close();
                    } catch (SQLException ex) {
                     JOptionPane.showMessageDialog(null,"C.I.-f5postGuardCarg"+ex.getMessage() );    
                    }
            }//finally catch
        }//@end f5CancelTypesAll
       
                public String totalturno(int opc, String idT){
          Connection cn = con2.conexion();
          String nAmb = "";
            String sql = "";
            switch (opc){
 //*** ENTYRADAS DE MONEY
                case 0://pagopedidocli
                  sql = "SELECT sum(pagopedidocli.montoPayCliente) AS totl\n" +
                    "FROM \n" +
                    "	pagopedidocli\n" +
                    "INNER JOIN \n" +
                    "	modopago\n" +
                    "ON\n" +
                    "	pagopedidocli.modoPayCliente = modopago.cod_pago AND pagopedidocli.fechapayCliente = '"+idT+"';";      
                     break;
                
                case 1:// pagocreditprooved
                    sql = "SELECT sum(pagocreditprooved.montoPayProoved) AS totl\n" +
                "FROM \n" +
                "	pagocreditprooved\n" +
                "INNER JOIN \n" +
                "	modopago\n" +
                "ON\n" +
                "	pagoCreditProoved.modoPayProoved = modopago.cod_pago AND pagoCreditProoved.fechapayProoved = '"+idT+"';"; 
                break;
                case 2://pagoventapiso
                     sql = "sum(pagoventapiso.montoVP) AS totl\n" +
                    "FROM \n" +
                    "	pagoventapiso\n" +
                    "INNER JOIN \n" +
                    "	modopago\n" +
                    "ON\n" +
                    "	pagoventapiso.method_payVP = modopago.cod_pago AND pagoventapiso.fechaPayVP = '"+idT+"';";
                break;
 //*** SALIDAS DE MONEY
                case 3://pagoflete
                     sql = "SELECT sum(pagoflete.montoPayFl) AS totl\n" +
                    "FROM \n" +
                    "	pagoflete\n" +
                    "INNER JOIN \n" +
                    "	modopago\n" +
                    "ON\n" +
                    "	pagoflete.modPay = modopago.cod_pago AND pagoflete.fechaPayFlete = '"+idT+"';";
                break;
                case 4://pagarcompraprovee
                     sql = " SELECT sum(pagarcompraprovee.montoPayProveed) AS totl\n" +
                    "FROM \n" +
                    "	pagarcompraprovee\n" +
                    "INNER JOIN \n" +
                    "	modopago\n" +
                    "ON\n" +
                    "	pagarcompraprovee.modoPayProveed = modopago.cod_pago AND pagarcompraprovee.fechpayProveed = '"+idT+"';"; 
                break;
                case 5://gastos_caja
                      sql = "SELECT sum(gastos_caja.monto) AS totl\n" +
                    "FROM \n" +
                    "	gastos_caja\n" +
                    " where\n" +
                    "	gastos_caja.fecha = '"+idT+"';";
                break;
         }
            Statement st = null;
            ResultSet rs= null;
            try {
                st = cn.createStatement();
                rs = st.executeQuery(sql);
                rs.beforeFirst();
                if(rs.next())
                {
                    if(rs.getRow() > 0 && !(rs.getString(1) == null) ){
                        nAmb = rs.getString(1);
                    }else{
                        nAmb = "0";
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
           return nAmb;
         }
       
          public String[] getTurnoData(int id){
            Connection cn = con2.conexion();
            String[] idUser = new String[5];
            String sql = "";
            sql = "SELECT * FROM turnos WHERE id = '"+id+"'";
            Statement st = null;
            ResultSet rs= null;
            try {
                st = cn.createStatement();
                rs = st.executeQuery(sql);
                rs.beforeFirst();
                if(rs.next())
                {
                    if(rs.getRow() > 0){
                        idUser[0]=rs.getString(1);
                        idUser[1]=rs.getString(2);
                        idUser[2]=rs.getString(3);
                        idUser[3]=rs.getString(4);
                        idUser[4]=rs.getString(5);
                    }else{
                        for (int j = 0; j < idUser.length; j++) {
                            idUser[j] = "NO-DATA";
                        }
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
           return idUser;
    }//@end getTurnoData
     
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
                SQL="INSERT INTO pedidocliente (id_clienteP,fechaPedidio,notaPed,horaPedido,idTurno) VALUES (?,?,?,?,?)";                           
            try {
                pps = cn.prepareStatement(SQL);
                pps.setString(1, param.get(0));
                pps.setString(2,param.get(1));
                pps.setString(3,param.get(2));
                pps.setString(4,param.get(3));
                pps.setString(5,param.get(4));
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


//CREAR PEDIDOS CON VISTA DE DETALLES EN TABLA
     public String[][] consultPedidoAsign(String idPro,String fech1,String fech2,String status){
        Connection cn = con2.conexion();
        int cantColumnas=0,cantFilas=0;
        String[][] arre=null;
        String sql ="";
        
        if(idPro.isEmpty()){
            sql = "SELECT id_pedido,id_clienteP FROM pedidocliente WHERE fechaPedidio = '"+fech1+"'";           
        }else{
                if(status.isEmpty()){
                     sql = "SELECT pedidocliente.id_pedido,pedidocliente.id_clienteP FROM pedidocliente "+
                        "INNER JOIN clientepedidos"+
                        " ON  pedidocliente.id_clienteP = clientepedidos.id_cliente AND (pedidocliente.fechaPedidio >= '"+fech1+"' AND pedidocliente.fechaPedidio <= '"+fech2+"'  )"
                       + " AND pedidocliente.id_clienteP = '"+idPro+"' "           
                       + "ORDER BY pedidocliente.id_pedido;";
            }else{
                    sql = "SELECT pedidocliente.id_pedido,pedidocliente.id_clienteP FROM pedidocliente "+
                        "INNER JOIN clientepedidos"+
                        " ON  pedidocliente.id_clienteP = clientepedidos.id_cliente AND (pedidocliente.fechaPedidio >= '"+fech1+"' AND pedidocliente.fechaPedidio <= '"+fech2+"'  )"
                       + " AND pedidocliente.id_clienteP = '"+idPro+"' AND pedidocliente.status = '"+status+"' "        
                       + "ORDER BY pedidocliente.id_pedido;";
            }            
        }  
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
                    " pedidocliente.id_pedido,pedidocliente.fechaPedidio,pedidocliente.status,\n" +
                    " clientepedidos.id_cliente,clientepedidos.nombre,\n" +
                    " SUM(detailpedidio.cantidadCajas),pedidocliente.totalPrecio,\n" +
                    " pedidocliente.notaPed\n" +
                    "FROM\n" +
                    " clientepedidos\n" +
                    "INNER JOIN\n" +
                    " pedidocliente\n" +
                    "ON\n" +
                    " clientePedidos.id_cliente = pedidocliente.id_clienteP AND pedidocliente.id_clienteP ='"+idCli+"' AND\n" +
                    " pedidocliente.fechaPedidio BETWEEN '"+fech1+"' AND '"+fech2+"'\n" +
                    "INNER JOIN \n" +
                    " detailpedidio \n" +
                    "ON\n" +
                    " pedidocliente.id_pedido=detailpedidio.id_PedidioD\n" +
                    " GROUP BY pedidocliente.id_pedido\n" +
                    " ORDER BY pedidocliente.fechaPedidio;";
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
        
       public String[] getPedidonoDet(String id){
            Connection cn = con2.conexion();
            String[] idUser = new String[10];
            String sql = "";
            sql = "SELECT * FROM pedidocliente WHERE id_pedido = '"+id+"'";
            Statement st = null;
            ResultSet rs= null;
            try {
                st = cn.createStatement();
                rs = st.executeQuery(sql);
                rs.beforeFirst();
                if(rs.next())
                {
                    if(rs.getRow() > 0){
                        idUser[0]=rs.getString(1);
                        idUser[1]=rs.getString(2);
                        idUser[2]=rs.getString(3);
                        idUser[3]=rs.getString(4);
                        idUser[4]=rs.getString(5);
                        idUser[5]=rs.getString(6);
                        idUser[6]=rs.getString(7);
                        idUser[7]=rs.getString(8);
                        idUser[8]=rs.getString(9);
                        idUser[9]=rs.getString(10);
                    }else{
                        for (int j = 0; j < idUser.length; j++) {
                            idUser[j] = "NO-DATA";
                        }
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
           return idUser;
    }//@end getPedidonoDet
        
///****OPERACIONES DE GUARDA PRESTAMO
        public void guardaPrestamoProv(List<String> param){
        Connection cn = con2.conexion();
            PreparedStatement pps=null;
            String SQL=""; 
        
          //  No.setFecha_inicio(new java.sql.Date(((Date) celda.getDateCellValue()).getTime()));
                SQL="INSERT INTO creditomerca (id_ProveedorF,fechaPrestamo,status,notaPrest,horaCredi,idTurno) VALUES (?,?,?,?,?,?)";                           
            try {
                pps = cn.prepareStatement(SQL);
                pps.setString(1, param.get(0));
                pps.setString(2,param.get(1));
                pps.setInt(3,Integer.parseInt(param.get(2)));
                pps.setString(4,param.get(3));
                pps.setString(5,param.get(4));
                pps.setString(6,param.get(5));
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

public void guardaDetallePrestamoProv(List<String> param){
     Connection cn = con2.conexion();
            PreparedStatement pps=null;
            String SQL="";        
                SQL="INSERT INTO detallecreditproveed (num_creditCP,codigoProdCP,cantidadCP,costoCP) VALUES (?,?,?,?)";                           
            try {
                pps = cn.prepareStatement(SQL);
                pps.setString(1, param.get(0));
                pps.setString(2,param.get(1));
                pps.setString(3,param.get(2));
                pps.setString(4,param.get(3));
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
                    "creditomerca.num_credito,creditomerca.fechaPrestamo,creditomerca.status,\n" +
                    "proveedor.id_Proveedor,proveedor.nombreP,\n" +
                    "SUM(detallecreditproveed.cantidadCP*detallecreditproveed.costoCP) AS TOTAL,\n" +
                    "creditomerca.notaPrest\n" +
                    "FROM\n" +
                    "proveedor\n" +
                    "INNER JOIN\n" +
                    "creditomerca\n" +
                    "ON\n" +
                    "proveedor.id_Proveedor = creditomerca.id_ProveedorF AND creditomerca.fechaPrestamo = '"+fech+"'\n" +
                    "INNER JOIN \n" +
                    "detallecreditproveed\n" +
                    "ON\n" +
                    "creditomerca.num_credito=detallecreditproveed.num_creditCP\n" +
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
                    "creditomerca.num_credito,creditomerca.fechaPrestamo,IF(creditomerca.status = 0,'PENDIENTE','PAGADO'),\n" +
                    "proveedor.id_Proveedor,proveedor.nombreP,\n" +
                    "SUM(detallecreditproveed.cantidadCP*detallecreditproveed.costoCP) AS TOTAL,\n" +
                    "creditomerca.notaPrest\n" +
                    "FROM\n" +
                    "proveedor\n" +
                    "INNER JOIN\n" +
                    "creditomerca\n" +
                    "ON\n" +
                    "proveedor.id_Proveedor = creditomerca.id_ProveedorF AND creditomerca.id_ProveedorF = '"+idCli+"'\n" +
                    "INNER JOIN \n" +
                    "detallecreditproveed\n" +
                    "ON\n" +
                    "creditomerca.num_credito=detallecreditproveed.num_creditCP\n" +
                    "GROUP BY creditomerca.num_credito\n" +
                    "ORDER BY creditomerca.num_credito DESC;";      
          }    
          if(opcBusq==1){
           sql = "SELECT\n" +
                    "	creditomerca.num_credito,creditomerca.fechaPrestamo,IF(creditomerca.status = 0,'PENDIENTE','PAGADO'),\n" +
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
                    "	creditomerca.num_credito,creditomerca.fechaPrestamo,IF(creditomerca.status = 0,'PENDIENTE','PAGADO'),\n" +
                    "	proveedor.id_Proveedor,proveedor.nombreP,\n" +
                    "	SUM(detallecreditproveed.cantidadCP*detallecreditproveed.costoCP) AS TOTAL,\n" +
                    "	creditomerca.notaPrest\n" +
                    "FROM\n" +
                    "	proveedor\n" +
                    "INNER JOIN\n" +
                    "	creditomerca\n" +
                    "ON\n" +
                    "	proveedor.id_Proveedor = creditomerca.id_ProveedorF AND (creditomerca.fechaPrestamo >= '"+fech1+"' AND creditomerca.fechaPrestamo <= '"+fech2+"' ) \n" +
                    "INNER JOIN \n" +
                    "	detallecreditproveed\n" +
                    "ON\n" +
                    "	creditomerca.num_credito=detallecreditproveed.num_creditCP\n" +
                    "GROUP BY creditomerca.num_credito\n" +
                    "ORDER BY creditomerca.num_credito DESC;";
          }
          if(opcBusq==3){
                     sql = "SELECT\n" +
                    "	creditomerca.num_credito,creditomerca.fechaPrestamo,IF(creditomerca.status = 0,'PENDIENTE','PAGADO'),\n" +
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
                    "	creditomerca.num_credito,creditomerca.fechaPrestamo,IF(creditomerca.status = 0,'PENDIENTE','PAGADO'),\n" +
                    "	proveedor.id_Proveedor,proveedor.nombreP,\n" +
                    "	SUM(detallecreditproveed.cantidadCP*detallecreditproveed.costoCP) AS TOTAL,\n" +
                    "	creditomerca.notaPrest\n" +
                    "FROM\n" +
                    "	proveedor\n" +
                    "INNER JOIN\n" +
                    "	creditomerca\n" +
                    "ON\n" +
 "proveedor.id_Proveedor = creditomerca.id_ProveedorF AND creditomerca.id_ProveedorF = '"+idCli+"' AND (creditomerca.fechaPrestamo >= '"+fech1+"' AND creditomerca.fechaPrestamo <= '"+fech2+"' )\n" +
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
          SQL="INSERT INTO compraProoved (id_ProveedorC,fechaCompra,descripcionSubasta,notaCompra,statusCompra,horaCompra,idTurno) VALUES (?,?,?,?,?,?,?)";                           
            try {
                pps = cn.prepareStatement(SQL);
                pps.setString(1, param.get(0));
                pps.setString(2,param.get(1));
                pps.setString(3,param.get(2));
                pps.setString(4,param.get(3));
                pps.setInt(5,Integer.parseInt(param.get(4)));
                pps.setString(6,param.get(5));
                pps.setString(7,param.get(6));
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
              //  JOptionPane.showMessageDialog(null, "Compra Guardada correctamente");
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
                JOptionPane.showMessageDialog(null, ex.getMessage());
            Logger.getLogger(controladorCFP.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
//            System.out.println("cierra conexion a la base de datos");    
            try {
                if(preparedStmt != null) preparedStmt.close();                
                if(cn !=null) cn.close();
            } catch (SQLException ex) {
                System.err.println("RAFA"); 
                JOptionPane.showMessageDialog(null, ex.getMessage());
//                System.out.println("Error al cerrar la conexon");
            }//catch
        }//finally 
            } else {
                JOptionPane.showMessageDialog(null,"No se borro el registro: "+id);
            }
        }else
            JOptionPane.showMessageDialog(null,"Sin data a eliminar");
}

     public String[][] consultCompra(String fech1,String filt){
        Connection cn = con2.conexion();
        int cantColumnas=0,cantFilas=0;
        String[][] arre=null;
        String sql ="";
        if(filt.isEmpty()){
              sql = "SELECT id_compraProve,id_ProveedorC FROM compraprooved WHERE fechaCompra = '"+fech1+"'";           
            }else if(!filt.equals("sobrinas")){
               sql = "SELECT compraprooved.id_compraProve,compraprooved.id_ProveedorC FROM compraprooved "+
                        "INNER JOIN proveedor"+
                        " ON  compraprooved.id_ProveedorC = proveedor.id_Proveedor AND compraprooved.fechaCompra = '"+fech1+"' AND "
                       + "(proveedor.nombreP LIKE '"+filt+"%'  OR compraprooved.descripcionSubasta LIKE '%"+filt+"%') "
                       + "ORDER BY compraprooved.id_compraProve;";           
            } else if(filt.equals("sobrinas")){
                 sql = "SELECT id_compraProve,id_ProveedorC FROM compraprooved WHERE fechaCompra = '"+fech1+"' AND statAsign <> 0";   
            }
        
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
                       arre[i][0]=rs.getString(1);
                       arre[i][1]=rs.getString(2);
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
       
       //mostrar compra a proveedor con filtros de busqueda, por if, fehca, lapso de fechas
     public String[][] consultComprafilters(String idPro,String fech1,String fech2,String status){
        Connection cn = con2.conexion();
        int cantColumnas=0,cantFilas=0;
        String[][] arre=null;
        String sql ="";
        if(status.isEmpty()){
               sql = "SELECT compraprooved.id_compraProve,compraprooved.id_ProveedorC FROM compraprooved "+
                        "INNER JOIN proveedor"+
                        " ON  compraprooved.id_ProveedorC = proveedor.id_Proveedor AND (compraprooved.fechaCompra >= '"+fech1+"' AND compraprooved.fechaCompra <= '"+fech2+"'  )"
                       + " AND compraprooved.id_ProveedorC = '"+idPro+"' "           
                       + "ORDER BY compraprooved.id_compraProve;";           
        }else{
                           sql = "SELECT compraprooved.id_compraProve,compraprooved.id_ProveedorC FROM compraprooved "+
                        "INNER JOIN proveedor"+
                        " ON  compraprooved.id_ProveedorC = proveedor.id_Proveedor AND (compraprooved.fechaCompra >= '"+fech1+"' AND compraprooved.fechaCompra <= '"+fech2+"'  )"
                       + " AND compraprooved.id_ProveedorC = '"+idPro+"' AND compraprooved.statusCompra = '"+status+"' "           
                       + "ORDER BY compraprooved.id_compraProve;";           
        }
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
                       arre[i][0]=rs.getString(1);
                       arre[i][1]=rs.getString(2);
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
    }//@end consultComprafilters
         
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
                SQL="INSERT INTO notaventapiso (id_clientePiso,fechVenta,statusVent,notaVent,horaVentaPiso,idTurno) VALUES (?,?,?,?,?,?)";                           
            try {
                pps = cn.prepareStatement(SQL);
                pps.setString(1, param.get(0));
                pps.setString(2,param.get(1));
                pps.setInt(3,0);
                pps.setString(4,param.get(2));
                pps.setString(5,param.get(3));
                pps.setString(6,param.get(4));
                pps.executeUpdate();
                JOptionPane.showMessageDialog(null, "Venta agregada correctamente.");
            } catch (SQLException ex) {
                Logger.getLogger(controladorCFP.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, "Error durante la transaccion."+ex);
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
                "	notaventapiso.id_venta,notaventapiso.fechVenta,IF(notaventapiso.statusVent = 0,'PENDIENTE','PAGADO'),\n" +
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
                "	notaventapiso.id_venta,notaventapiso.fechVenta,IF(notaventapiso.statusVent = 0,'PENDIENTE','PAGADO'),\n" +
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
                "	notaventapiso.id_venta,notaventapiso.fechVenta,IF(notaventapiso.statusVent = 0,'PENDIENTE','PAGADO'),\n" +
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
                "	notaventapiso.id_venta,notaventapiso.fechVenta,IF(notaventapiso.statusVent = 0,'PENDIENTE','PAGADO'),\n" +
                "	clientepedidos.id_cliente,clientepedidos.nombre,\n" +
                "	SUM(detailventapiso.cantidadV*detailventapiso.precioPV),\n" +
                "	notaventapiso.notaVent\n" +
                "FROM\n" +
                "	clientepedidos\n" +
                "INNER JOIN\n" +
                "	notaventapiso\n" +
                "ON\n" +
                "	clientePedidos.id_cliente = notaventapiso.id_clientePiso AND\n" +
                "	 (notaventapiso.fechVenta >= '"+fech1+"' AND notaventapiso.fechVenta >= '"+fech2+"' ) \n" +
                "INNER JOIN \n" +
                "	detailVentaPiso \n" +
                "ON\n" +
                "	notaventapiso.id_venta=detailventapiso.num_notaV\n" +
                "GROUP BY notaventapiso.id_venta\n" +
                "ORDER BY notaventapiso.fechVenta DESC;";
          }
          if(opcBusq==3){
              sql="SELECT\n" +
                "	notaventapiso.id_venta,notaventapiso.fechVenta,IF(notaventapiso.statusVent = 0,'PENDIENTE','PAGADO'),\n" +
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
                "	notaventapiso.id_venta,notaventapiso.fechVenta,IF(notaventapiso.statusVent = 0,'PENDIENTE','PAGADO'),\n" +
                "	clientepedidos.id_cliente,clientepedidos.nombre,\n" +
                "	SUM(detailventapiso.cantidadV*detailventapiso.precioPV),\n" +
                "	notaventapiso.notaVent\n" +
                "FROM\n" +
                "	clientepedidos\n" +
                "INNER JOIN\n" +
                "	notaventapiso\n" +
                "ON\n" +
                "	clientePedidos.id_cliente = notaventapiso.id_clientePiso AND notaventapiso.id_clientePiso = '"+idCli+"' AND\n" +
                "	(notaventapiso.fechVenta >= '"+fech1+"' AND notaventapiso.fechVenta <= '"+fech2+"' )\n" +
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
  
                SQL="INSERT INTO fleteenviado (id_fleteE,id_FleteroE,fechaFlete,choferFlete,trocaFlete,costoFlete,status,recivioFlete,cargaParcial) VALUES (?,?,?,?,?,?,?,?,?)";                           
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
                pps.setString(9, param.get(7));               
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
                SQL="UPDATE fleteenviado SET id_FleteroE =?, fechaFlete =?, choferFlete=?, trocaFlete=?, costoFlete=?, status=?, recivioFlete=?,cargaParcial=? WHERE id_fleteE = '"+id+"' ";                           
            try {
                pps = cn.prepareStatement(SQL);
                pps.setString(1, param.get(0));
                pps.setString(2, param.get(1));
                pps.setString(3, param.get(2));
                pps.setString(4, param.get(3));
                pps.setString(5, param.get(4));
                pps.setInt(6, Integer.parseInt(param.get(5)));
                pps.setString(7, param.get(6));
                pps.setString(8, param.get(7));
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
     public String[][] matrizFletesFilter(int opcBusq,String idCli,String fech1,String fech2,String stat){
        Connection cn = con2.conexion();
          String sql ="",aux;
          if(opcBusq==0){//OPCION BUSQUEDA DE NOMBRE
              if(stat.isEmpty()){
                sql = "SELECT fleteEnviado.id_fleteE, fletero.nombreF,fleteEnviado.fechaFlete,fleteEnviado.choferFlete,\n" +
                  "fleteEnviado.trocaFlete,fleteEnviado.costoFlete,IF(fleteEnviado.status = 0,'Pendiente','Pagado'),fleteEnviado.recivioFlete\n" +
                  "FROM\n" +
                  "	fleteEnviado\n" +
                  "INNER JOIN\n" +
                  "	fletero\n" +
                  "ON fletero.id_Fletero = fleteEnviado.id_FleteroE AND fleteEnviado.id_FleteroE = '"+idCli+"';";      
            }else{
                sql = "SELECT fleteEnviado.id_fleteE, fletero.nombreF,fleteEnviado.fechaFlete,fleteEnviado.choferFlete,\n" +
                  "fleteEnviado.trocaFlete,fleteEnviado.costoFlete,IF(fleteEnviado.status = 0,'Pendiente','Pagado'),fleteEnviado.recivioFlete\n" +
                  "FROM\n" +
                  "	fleteEnviado\n" +
                  "INNER JOIN\n" +
                  "	fletero\n" +
                  "ON fletero.id_Fletero = fleteEnviado.id_FleteroE AND fleteEnviado.id_FleteroE = '"+idCli+"' AND fleteEnviado.status = '"+stat+"';";                  
              }
          }
          
          if(opcBusq==1){//OPCION BUSQUEDA FOLIO
             if(stat.isEmpty()){ 
                    sql = "SELECT fleteEnviado.id_fleteE, fletero.nombreF,fleteEnviado.fechaFlete,fleteEnviado.choferFlete,\n" +
                      "fleteEnviado.trocaFlete,fleteEnviado.costoFlete,IF(fleteEnviado.status = 0,'Pendiente','Pagado'),fleteEnviado.recivioFlete\n" +
                      "FROM\n" +
                      "fleteEnviado\n" +
                      "INNER JOIN\n" +
                      "fletero\n" +
                      "ON fletero.id_Fletero = fleteEnviado.id_FleteroE AND fleteEnviado.id_fleteE = '"+idCli+"';";//folio      
            }else{
                    sql = "SELECT fleteEnviado.id_fleteE, fletero.nombreF,fleteEnviado.fechaFlete,fleteEnviado.choferFlete,\n" +
                      "fleteEnviado.trocaFlete,fleteEnviado.costoFlete,IF(fleteEnviado.status = 0,'Pendiente','Pagado'),fleteEnviado.recivioFlete\n" +
                      "FROM\n" +
                      "fleteEnviado\n" +
                      "INNER JOIN\n" +
                      "fletero\n" +
                      "ON fletero.id_Fletero = fleteEnviado.id_FleteroE AND fleteEnviado.id_fleteE = '"+idCli+"' AND fleteEnviado.status = '"+stat+"';";//folio      
             }
          }
          if(opcBusq==2){//OPCION BUSQUEDA POR FECHA
            if(stat.isEmpty()){    
              sql="SELECT fleteEnviado.id_fleteE, fletero.nombreF,fleteEnviado.fechaFlete,fleteEnviado.choferFlete,\n" +
                  "fleteEnviado.trocaFlete,fleteEnviado.costoFlete,IF(fleteEnviado.status = 0,'Pendiente','Pagado'),fleteEnviado.recivioFlete\n" +
                  "	FROM\n" +
                  "	fleteEnviado\n" +
                  "	INNER JOIN\n" +
                  "	fletero\n" +
                  "	ON fletero.id_Fletero = fleteEnviado.id_FleteroE AND fleteEnviado.fechaFlete = '"+fech1+"';";
          }else{
              sql="SELECT fleteEnviado.id_fleteE, fletero.nombreF,fleteEnviado.fechaFlete,fleteEnviado.choferFlete,\n" +
                  "fleteEnviado.trocaFlete,fleteEnviado.costoFlete,IF(fleteEnviado.status = 0,'Pendiente','Pagado'),fleteEnviado.recivioFlete\n" +
                  "	FROM\n" +
                  "	fleteEnviado\n" +
                  "	INNER JOIN\n" +
                  "	fletero\n" +
                  "	ON fletero.id_Fletero = fleteEnviado.id_FleteroE AND fleteEnviado.fechaFlete = '"+fech1+"' AND fleteEnviado.status = '"+stat+"';";
            }
          }
          if(opcBusq==3){//LAPSO DE FECHAS
              if(stat.isEmpty()){
                sql="SELECT fleteEnviado.id_fleteE, fletero.nombreF,fleteEnviado.fechaFlete,fleteEnviado.choferFlete,\n" +
                  "	fleteEnviado.trocaFlete,fleteEnviado.costoFlete,IF(fleteEnviado.status = 0,'Pendiente','Pagado'),fleteEnviado.recivioFlete\n" +
                  "	FROM\n" +
                  "	fleteEnviado\n" +
                  "	INNER JOIN\n" +
                  "	fletero\n" +
                  "	ON fletero.id_Fletero = fleteEnviado.id_FleteroE AND (fleteEnviado.fechaFlete >= '"+fech1+"' AND fleteEnviado.fechaFlete <= '"+fech2+"');";
          }else{
                sql="SELECT fleteEnviado.id_fleteE, fletero.nombreF,fleteEnviado.fechaFlete,fleteEnviado.choferFlete,\n" +
                  "	fleteEnviado.trocaFlete,fleteEnviado.costoFlete,IF(fleteEnviado.status = 0,'Pendiente','Pagado'),fleteEnviado.recivioFlete\n" +
                  "	FROM\n" +
                  "	fleteEnviado\n" +
                  "	INNER JOIN\n" +
                  "	fletero\n" +
                  "	ON fletero.id_Fletero = fleteEnviado.id_FleteroE AND fleteEnviado.status = '"+stat+"' AND (fleteEnviado.fechaFlete >= '"+fech1+"' AND fleteEnviado.fechaFlete <=  '"+fech2+"') ;";
              }
          }
          if(opcBusq ==4){//fletero + fecha
            if(stat.isEmpty()){ 
              sql="SELECT fleteEnviado.id_fleteE, fletero.nombreF,fleteEnviado.fechaFlete,fleteEnviado.choferFlete,\n" +
                "	fleteEnviado.trocaFlete,fleteEnviado.costoFlete,IF(fleteEnviado.status = 0,'Pendiente','Pagado'),fleteEnviado.recivioFlete\n" +
                "	FROM\n" +
                "	fleteEnviado\n" +
                "	INNER JOIN\n" +
                "	fletero\n" +
                "	ON fletero.id_Fletero = fleteEnviado.id_FleteroE AND fleteEnviado.id_FleteroE = '"+idCli+"' AND\n" +
                "	fleteEnviado.fechaFlete = '"+fech1+"';";
          }else{
              sql="SELECT fleteEnviado.id_fleteE, fletero.nombreF,fleteEnviado.fechaFlete,fleteEnviado.choferFlete,\n" +
                "	fleteEnviado.trocaFlete,fleteEnviado.costoFlete,IF(fleteEnviado.status = 0,'Pendiente','Pagado'),fleteEnviado.recivioFlete\n" +
                "	FROM\n" +
                "	fleteEnviado\n" +
                "	INNER JOIN\n" +
                "	fletero\n" +
                "	ON fletero.id_Fletero = fleteEnviado.id_FleteroE AND fleteEnviado.id_FleteroE = '"+idCli+"' AND\n" +
                "	fleteEnviado.fechaFlete = '"+fech1+"' AND fleteEnviado.status = '"+stat+"' ;";
            }
          }
          if(opcBusq ==5){//fletero + lapso de fechas
              if(stat.isEmpty()){
              sql="SELECT fleteEnviado.id_fleteE, fletero.nombreF,fleteEnviado.fechaFlete,fleteEnviado.choferFlete,\n" +
                "	fleteEnviado.trocaFlete,fleteEnviado.costoFlete,IF(fleteEnviado.status = 0,'Pendiente','Pagado'),fleteEnviado.recivioFlete\n" +
                "	FROM\n" +
                "	fleteEnviado\n" +
                "	INNER JOIN\n" +
                "	fletero\n" +
                "	ON fletero.id_Fletero = fleteEnviado.id_FleteroE AND fleteEnviado.id_FleteroE = '"+idCli+"' AND\n" +
                "	fleteEnviado.status = '"+stat+"' AND (fleteEnviado.fechaFlete >= '"+fech1+"' AND fleteEnviado.fechaFlete <= '"+fech2+"');";
          }else{
             sql="SELECT fleteEnviado.id_fleteE, fletero.nombreF,fleteEnviado.fechaFlete,fleteEnviado.choferFlete,\n" +
                "	fleteEnviado.trocaFlete,fleteEnviado.costoFlete,IF(fleteEnviado.status = 0,'Pendiente','Pagado'),fleteEnviado.recivioFlete\n" +
                "	FROM\n" +
                "	fleteEnviado\n" +
                "	INNER JOIN\n" +
                "	fletero\n" +
                "	ON fletero.id_Fletero = fleteEnviado.id_FleteroE AND fleteEnviado.id_FleteroE = '"+idCli+"' AND\n" +
                "	(fleteEnviado.fechaFlete >= '"+fech1+"' AND fleteEnviado.fechaFlete <= '"+fech2+"');";
              }
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
                SQL="INSERT INTO relcomprapedido (id_compraProveed,id_pedidoCli,cantidadCajasRel,tipoMercanRel,id_fleteP,precioAjust,id_detailComp) VALUES (?,?,?,?,?,?,?)";                           
            try {
                pps = cn.prepareStatement(SQL);
                pps.setString(1, dots.get(0));
                pps.setString(2,dots.get(1));
                pps.setString(3,dots.get(2));
                pps.setString(4,dots.get(3));
                pps.setString(5, dots.get(4));
                pps.setString(6, dots.get(5));
                pps.setString(7, dots.get(6));
                pps.executeUpdate();
                JOptionPane.showMessageDialog(null, "Asignacion Guardada correctamente");
            } catch (SQLException ex) {
                Logger.getLogger(controladorCFP.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null,ex);
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
        
             //VALIDAR SI FLETE,PEDIDO Y COMPRA YA ESTAN ASIGNADOS EN LA TABLA relcomprapedido
        public int sumaRelCompPed(String idBusq,String camp){
            Connection cn = con2.conexion();
            int totalon =-1;
            int num=0,i=1;
            String sql = "";
            switch(camp){
                case "id_compraProveed":
                    sql = "SELECT SUM(cantidadCajasRel) FROM relcomprapedido WHERE id_compraProveed = '"+idBusq+"';";
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
                case "id_pedidoCliSum":
                    sql = "SELECT SUM(cantidadCajasRel) FROM relcomprapedido WHERE id_pedidoCli = '"+idBusq+"';";
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
                        totalon =rs.getInt(1);
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
           return totalon;
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
 
// devuelve el total de producto asiganado segun el tipo a un pedido co le fin de no excederel total
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
  public String[][] matPedidosEst(String id,int opc){
        Connection cn = con2.conexion();
          String sql ="",aux;
          if(opc == 0){//opcion vista detalle Pedido cliente
          sql = "SELECT relcomprapedido.id_relacionCP,relcomprapedido.id_fleteP,relcomprapedido.id_compraProveed,\n" +
                "	IF(proveedor.nombreP = 'SUBASTA',compraprooved.descripcionSubasta,proveedor.nombreP) AS condic,\n" +
                "	productocal.nombreP,relcomprapedido.cantidadCajasRel,relcomprapedido.precioAjust,\n" +
                "	(relcomprapedido.cantidadCajasRel*relcomprapedido.precioAjust) AS TOT\n" +
                "FROM\n" +
                "	relcomprapedido\n" +
                " INNER JOIN\n" +
                "	productoCal\n" +
                "ON\n" +
                "	productoCal.codigo = relcomprapedido.tipoMercanRel AND relcomprapedido.id_pedidoCli = '"+id+"'\n" +
                "INNER JOIN \n" +
                "	compraprooved\n" +
                "ON \n" +
                "	relcomprapedido.id_compraProveed = compraprooved.id_compraProve\n" +
                " INNER JOIN \n" +
                "	proveedor\n" +
                "ON \n" +
                "	compraprooved.id_ProveedorC = proveedor.id_Proveedor;";      
          }
          if(opc == 1){
              sql = "SELECT relcomprapedido.id_relacionCP,relcomprapedido.id_fleteP,relcomprapedido.id_pedidoCli,\n" +
                "clientepedidos.nombre,\n" +
                "productocal.nombreP,relcomprapedido.cantidadCajasRel,relcomprapedido.precioAjust,\n" +
                "(relcomprapedido.cantidadCajasRel*relcomprapedido.precioAjust) AS TOT\n" +
                "FROM relcomprapedido\n" +
                "INNER JOIN productoCal\n" +
                "ON productoCal.codigo = relcomprapedido.tipoMercanRel AND relcomprapedido.id_compraProveed = '"+id+"'\n" +
                "INNER JOIN pedidocliente -- compraprooved\n" +
                "ON relcomprapedido.id_pedidoCli = pedidocliente.id_pedido\n" +
                "INNER JOIN clientepedidos\n" +
                "ON pedidocliente.id_clienteP = clientepedidos.id_cliente;";
          }
          if(opc == 2){
              sql = "SELECT relcomprapedido.id_relacionCP,relcomprapedido.id_pedidoCli,relcomprapedido.id_compraProveed,\n" +
            "IF(proveedor.nombreP = 'SUBASTA',compraprooved.descripcionSubasta,proveedor.nombreP) AS alia,productocal.nombreP,relcomprapedido.cantidadCajasRel,relcomprapedido.precioAjust,\n" +
            "(relcomprapedido.cantidadCajasRel*relcomprapedido.precioAjust) AS TOT\n" +
            "FROM relcomprapedido\n" +
            "INNER JOIN productoCal\n" +
            "ON productoCal.codigo = relcomprapedido.tipoMercanRel AND relcomprapedido.id_fleteP = '"+id+"'\n" +
            "INNER JOIN compraprooved ON relcomprapedido.id_compraProveed = compraprooved.id_compraProve\n" +
            "INNER JOIN proveedor ON compraprooved.id_ProveedorC = proveedor.id_Proveedor;";
          }
          
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
        public String[][] regresaPaysFech(String tipe,String fech, String idT){
        Connection cn = con2.conexion();
          String sql ="",aux;
          if(tipe.equals("pagopedidocli")){//OPCION pago de pedididoscli
              if(idT.isEmpty()){
                sql = "SELECT pagopedidocli.id_payPedCli,pagopedidocli.idClientePay,pagopedidocli.fechapayCliente,pagopedidocli.montoPayCliente,\n" +
                  "	pagopedidocli.notaPayCliente,modopago.nombrePay\n" +
                  "FROM \n" +
                  "	pagopedidocli\n" +
                  "INNER JOIN \n" +
                  "	modopago\n" +
                  "ON\n" +
                  "	pagopedidocli.modoPayCliente = modopago.cod_pago AND pagopedidocli.idCancelacion = 0 AND pagopedidocli.fechapayCliente = '"+fech+"';";      
            }else{
                sql = "SELECT pagopedidocli.id_payPedCli,pagopedidocli.idClientePay,pagopedidocli.fechapayCliente,pagopedidocli.montoPayCliente,\n" +
                  "	pagopedidocli.notaPayCliente,modopago.nombrePay\n" +
                  "FROM \n" +
                  "	pagopedidocli\n" +
                  "INNER JOIN \n" +
                  "	modopago\n" +
                  "ON\n" +
                  "	pagopedidocli.modoPayCliente = modopago.cod_pago AND pagopedidocli.idCancelacion = 0 AND pagopedidocli.idTurno = '"+idT+"';";
            }
          }
          if(tipe.equals("pagoventapiso")){//OPCION pago de ventaspiso
              if(idT.isEmpty()){
                sql = "SELECT pagoventapiso.id_payVentaP,pagoventapiso.num_notaVP,pagoventapiso.fechaPayVP,pagoventapiso.montoVP,\n" +
                  "	pagoventapiso.notaPayVP,modopago.nombrePay\n" +
                  "FROM \n" +
                  "	pagoventapiso\n" +
                  "INNER JOIN \n" +
                  "	modopago\n" +
                  "ON\n" +
                  "	pagoventapiso.method_payVP = modopago.cod_pago AND pagoventapiso.idCancelacion = 0 AND pagoventapiso.fechaPayVP = '"+fech+"';";      
            }else{
                sql = "SELECT pagoventapiso.id_payVentaP,pagoventapiso.num_notaVP,pagoventapiso.fechaPayVP,pagoventapiso.montoVP,\n" +
                  "	pagoventapiso.notaPayVP,modopago.nombrePay\n" +
                  "FROM \n" +
                  "	pagoventapiso\n" +
                  "INNER JOIN \n" +
                  "	modopago\n" +
                  "ON\n" +
                  "	pagoventapiso.method_payVP = modopago.cod_pago AND pagoventapiso.idCancelacion = 0 AND pagoventapiso.idTurno = '"+idT+"';";      
              }
          }
          
          if(tipe.equals("pagocreditprooved")){//OPCION pago de credito a proveedor
              if(idT.isEmpty()){
                sql = "SELECT pagoCreditProoved.id_pay,pagoCreditProoved.idProovedPay,pagoCreditProoved.fechapayProoved,pagoCreditProoved.montoPayProoved,\n" +
                  "	pagoCreditProoved.notaPayProoved,modopago.nombrePay\n" +
                  "FROM \n" +
                  "	pagoCreditProoved\n" +
                  "INNER JOIN \n" +
                  "	modopago\n" +
                  "ON\n" +
                  "	pagoCreditProoved.modoPayProoved = modopago.cod_pago AND pagoCreditProoved.idCancelacion = 0 AND pagoCreditProoved.fechapayProoved = '"+fech+"';";      
            }else{
                sql = "SELECT pagoCreditProoved.id_pay,pagoCreditProoved.idProovedPay,pagoCreditProoved.fechapayProoved,pagoCreditProoved.montoPayProoved,\n" +
                  "	pagoCreditProoved.notaPayProoved,modopago.nombrePay\n" +
                  "FROM \n" +
                  "	pagoCreditProoved\n" +
                  "INNER JOIN \n" +
                  "	modopago\n" +
                  "ON\n" +
                  "	pagoCreditProoved.modoPayProoved = modopago.cod_pago AND pagoCreditProoved.idCancelacion = 0 AND pagoCreditProoved.idTurno = '"+idT+"';";      
              }
          }
          
          if(tipe.equals("pagoflete")){//OPCION pago de fletes
              if(idT.isEmpty()){
                sql = "SELECT pagoflete.id_pagoFlete,pagoflete.id_fleteEnv,pagoflete.fechaPayFlete,pagoflete.montoPayFl,\n" +
                  "	pagoflete.notaPayFlete,modopago.nombrePay\n" +
                  "FROM \n" +
                  "	pagoflete\n" +
                  "INNER JOIN \n" +
                  "	modopago\n" +
                  "ON\n" +
                  "	pagoflete.modPay = modopago.cod_pago AND pagoflete.idCancelacion = 0 AND pagoflete.fechaPayFlete = '"+fech+"';";      
            }else{
              sql = "SELECT pagoflete.id_pagoFlete,pagoflete.id_fleteEnv,pagoflete.fechaPayFlete,pagoflete.montoPayFl,\n" +
                "	pagoflete.notaPayFlete,modopago.nombrePay\n" +
                "FROM \n" +
                "	pagoflete\n" +
                "INNER JOIN \n" +
                "	modopago\n" +
                "ON\n" +
                "	pagoflete.modPay = modopago.cod_pago AND pagoflete.idCancelacion = 0 AND pagoflete.idTurno = '"+idT+"';";      
              }
          }
          
          if(tipe.equals("pagarcompraprovee")){//OPCION pago de compra a proveedor
              if(idT.isEmpty()){
                sql = "SELECT pagarcompraprovee.id_paycompra,pagarcompraprovee.num_compraProveed,pagarcompraprovee.fechpayProveed,pagarcompraprovee.montoPayProveed,\n" +
                  "	pagarcompraprovee.notaPayProveed,modopago.nombrePay\n" +
                  "FROM \n" +
                  "	pagarcompraprovee\n" +
                  "INNER JOIN \n" +
                  "	modopago\n" +
                  "ON\n" +
                  "	pagarcompraprovee.modoPayProveed = modopago.cod_pago AND pagarcompraprovee.idCancelacion = 0 AND pagarcompraprovee.fechpayProveed = '"+fech+"';";      
            }else{
              sql = "SELECT pagarcompraprovee.id_paycompra,pagarcompraprovee.num_compraProveed,pagarcompraprovee.fechpayProveed,pagarcompraprovee.montoPayProveed,\n" +
                "	pagarcompraprovee.notaPayProveed,modopago.nombrePay\n" +
                "FROM \n" +
                "	pagarcompraprovee\n" +
                "INNER JOIN \n" +
                "	modopago\n" +
                "ON\n" +
                "	pagarcompraprovee.modoPayProveed = modopago.cod_pago AND pagarcompraprovee.idCancelacion = 0 AND pagarcompraprovee.idTurno = '"+idT+"';";      
              }
          }
          
          if(tipe.equals("gastos_caja")){//OPCION gastos del dia
              if(idT.isEmpty()){
                sql = "SELECT gastos_caja.id,CONCAT(gastos_caja.fecha,', ',gastos_caja.hora),gastos_caja.monto,\n" +
                  "	gastos_caja.concepto,gastos_caja.idTurno\n" +
                  "FROM \n" +
                  "	gastos_caja\n" +
                  " where\n" +
                  "	gastos_caja.fecha = '"+fech+"';";      
            }else{
                sql = "SELECT gastos_caja.id,CONCAT(gastos_caja.fecha,', ',gastos_caja.hora),gastos_caja.monto,\n" +
                  "	gastos_caja.concepto,gastos_caja.idTurno\n" +
                  "FROM \n" +
                  "	gastos_caja\n" +
                  " where\n" +
                  "	gastos_caja.idTurno = '"+idT+"';";      
              }          
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
        String prod = "",sql ="";
        sql = "SELECT\n" +
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
                JOptionPane.showMessageDialog(null, "Asignacion a compra mayorista realizada correctamente");
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
                             sql = "SELECT '1' FROM relcomprapedido WHERE id_detailComp = '"+idBusq+"' ;";// id_pedidoCli
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
                "	compramayoreo.id_compraProveMin = '"+idMay+"';";//id_ProveedorMay
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
        
         public int validaIsMasDeUnProducto(int idMay,int idProd){
            Connection cn = con2.conexion();
            String[] existe = new String[2];
            int num=0,i=1;
            String sql = "";
            sql = "SELECT count(*) FROM detailcompraprooved WHERE id_compraP = '"+idMay+"' AND codigoProdC = '"+idProd+"' GROUP BY codigoProdC ";
            Statement st = null;
            ResultSet rs= null;
            try {
                st = cn.createStatement();
                rs = st.executeQuery(sql);
                //rs.beforeFirst();
               while(rs.next())
                {
                        num =rs.getInt(1);
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
           return num;
    }//validasiexiste mas de un producto del mismo tipo
 
         public int sumaIsMasDeUnProducto(int idMay,int idProd){
            Connection cn = con2.conexion();
            String[] existe = new String[2];
            int num=0,i=1;
            String sql = "";
            sql = "SELECT SUM(cantCajasC) FROM detailcompraprooved WHERE id_compraP = '"+idMay+"' AND codigoProdC = '"+idProd+"' ";//GROUP BY codigoProdC
            Statement st = null;
            ResultSet rs= null;
            try {
                st = cn.createStatement();
                rs = st.executeQuery(sql);
                //rs.beforeFirst();
               while(rs.next())
                {
                        num =rs.getInt(1);
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
           return num;
    }//validasiexiste mas de un producto del mismo tipo
             
         //***RETORNA MATRIZ DECOMPRA A MAYORISTA
        public String[][] regresacompMayorisa(String idC,String fech){
        Connection cn = con2.conexion();
          String sql ="",aux;
          if(fech.isEmpty()){
             sql = "SELECT detailcompraprooved.id_compraP,detailcompraprooved.num_DCompraP,productocal.codigo,productocal.nombreP,\n" +
                "	detailcompraprooved.cantCajasC,detailcompraprooved.precCajaC,(detailcompraprooved.cantCajasC*detailcompraprooved.precCajaC) AS prod\n" +
                "FROM \n" +
                "	detailcompraprooved\n" +
                "INNER JOIN \n" +
                "	compraprooved\n" +
                "ON\n" +
                "	detailcompraprooved.id_compraP = compraprooved.id_compraProve AND\n" +//AND compraprooved.fechaCompra = '"+fech+"' 
                "	compraprooved.id_compraProve = '"+idC+"'\n" +
                "INNER JOIN\n" +
                "	productocal\n" +
                "ON\n" +
                "	productocal.codigo = detailcompraprooved.codigoProdC;";
          }else{
              sql = "SELECT detailcompraprooved.id_compraP,detailcompraprooved.num_DCompraP,productocal.nombreP,\n" +
                "	detailcompraprooved.cantCajasC,detailcompraprooved.precCajaC,(detailcompraprooved.cantCajasC*detailcompraprooved.precCajaC) AS prod\n" +
                "FROM \n" +
                "	detailcompraprooved\n" +
                "INNER JOIN \n" +
                "	compraprooved\n" +
                "ON\n" +
                "	detailcompraprooved.id_compraP = compraprooved.id_compraProve AND compraprooved.fechaCompra = '"+fech+"' AND\n" +
                "	compraprooved.id_compraProve = '"+idC+"'\n" +
                "INNER JOIN\n" +
                "	productocal\n" +
                "ON\n" +
                "	productocal.codigo = detailcompraprooved.codigoProdC;";      
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
        
            /// CARGA LA SUMA DE CAJAS, IMPORTE TOTAL DE COMPRAS Y SUMA DE TIPO DE MERCANCIA EN EL DIA
    protected void cargaTotCompDayProveedor(){
        Connection cn = con2.conexion();
        int cantColumnas = 0, cantFilas = 0, temporal = 0, bandera = 0;
        String sql = "", sql2 = "";
        sql = "SELECT productocal.codigo,\n" +
                "	SUM(detailcompraprooved.cantCajasC) AS sumaType\n" +
                "FROM \n" +
                "	detailcompraprooved\n" +
                "INNER JOIN \n" +
                "	compraprooved\n" +
                "ON\n" +
                "	detailcompraprooved.id_compraP = compraprooved.id_compraProve \n" +
                "	AND compraprooved.fechaCompra = '2020-03-31'\n" +
                "INNER JOIN\n" +
                "	productocal\n" +
                "ON\n" +
                "	productocal.codigo = detailcompraprooved.codigoProdC\n" +
                "GROUP BY productocal.codigo;";
        sql2 = "";
        Statement st = null;
        ResultSet rs = null;
        try {
            st = cn.createStatement();
            rs = st.executeQuery(sql);
            // System.out.print("Filas: "+cantFilas+"\tColumnas: "+cantColumnas+"\n");
            while (rs.next()) {//es necesario el for para llenar dinamicamente la lista, ya que varia el numero de columnas de las tablas
            //    for (int x = 1; x <= rs.getMetaData().getColumnCount(); x++) {
                   // if (x == 3) {//valor,fila,columna
                   //jTabSumTotales.setValueAt(rs.getInt(x + 1), 0, rs.getInt(x) -1);//se le suma 1 por las columnas id,nombre de la jTable
                //}
                    System.out.print(rs.getString(1));//"["+x+"]"+" -> "+                   
                    System.out.println();
                    System.out.print(rs.getString(2));//"["+x+"]"+" -> "+                   
              //  }//for
                
            }//while
    } catch (SQLException ex) {
            Logger.getLogger(controladorCFP.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        }//finally  
    }
    
       public void actualizaDataC(String id,String table){
             Connection cn = con2.conexion();
            PreparedStatement pps=null;
            String SQL="";  
            switch(table){
                case "compraprooved":
                     SQL="UPDATE compraprooved SET statusCompra =? WHERE id_compraProve = '"+id+"' ";      
                    break;
                case "notaventapiso":
                     SQL="UPDATE notaventapiso SET statusVent =? WHERE id_venta = '"+id+"' ";      
                    break;
                case "pedidocliente":
                     SQL="UPDATE pedidocliente SET status =? WHERE id_pedido = '"+id+"' ";      
                    break;
                 case "creditomerca":
                     SQL="UPDATE creditomerca SET status =? WHERE num_credito = '"+id+"' ";      
                    break;
            case "fleteenviado":
                     SQL="UPDATE fleteenviado SET status =? WHERE id_fleteE = '"+id+"' ";      
                    break;
           };
            try {
                pps = cn.prepareStatement(SQL);
                pps.setInt(1, 1);
                pps.executeUpdate();
                //JOptionPane.showMessageDialog(null, "Pago realizado correctamente a compra No:"+id);
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
            }
        }//actualizaData 
        
       public void actualizaDataCompra(String[] id){
             Connection cn = con2.conexion();
            PreparedStatement pps=null;
            String SQL="";  
                     SQL="UPDATE detailcompraprooved SET cantCajasC =?,precCajaC=? WHERE num_DCompraP = '"+id[0]+"' ";      
            try {
                pps = cn.prepareStatement(SQL);
                pps.setString(1, id[1]);
                pps.setString(2, id[2]);
                pps.executeUpdate();
                JOptionPane.showMessageDialog(null, "Actualizado correctamente a compra No:"+id[0]);
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
            }
        }//actualizaData 
       
            public void guardaPayCompraContrl(List<String> datas, String table){
     Connection cn = con2.conexion();
            PreparedStatement pps=null;
            String SQL="",SQL2="";     
            switch(table){
                case "pagarcompraprovee":
                    SQL="INSERT INTO pagarcompraprovee (num_compraProveed,fechpayProveed,montoPayProveed,notaPayProveed,modoPayProveed) VALUES (?,?,?,?,?)";                    
                break;
                case "pagoventapiso":
                    SQL ="INSERT INTO pagoventapiso (num_notaVP,fechaPayVP,montoVP,notaPayVP,method_payVP) VALUES(?,?,?,?,?)";
                break;
                case "pagopedidocli":
                    SQL = "INSERT INTO pagopedidocli (idClientePay,fechapayCliente,montoPayCliente,notaPaycliente,modoPaycliente) VALUES (?,?,?,?,?)";                    
                break;
                case "pagocreditprooved":
                    SQL = "INSERT INTO pagocreditprooved (idProovedPay,fechapayProoved,montoPayProoved,notaPayProoved,modoPayProoved) VALUES (?,?,?,?,?)";                    
                break;
                case "pagoflete":
                    SQL = "INSERT INTO pagoflete (id_fleteEnv,fechaPayFlete,montoPayFl,notaPayFlete,modPay) VALUES (?,?,?,?,?)";                    
                break;
            };
                try {
                pps = cn.prepareStatement(SQL);
                pps.setString(1, datas.get(0));
                pps.setString(2,datas.get(1));
                pps.setString(3,datas.get(2));
                pps.setString(4,datas.get(3));
                pps.setString(5,datas.get(4));
                pps.executeUpdate();
                //JOptionPane.showMessageDialog(null, "Pago realizado correctamente");
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
} //guardaPagoPrveedor
       
         //***RETORNA MATRIZ DETALLE PRESTAMO A PROVEEDOR
        public String[][] regresaprestProveedor(int idC,String fech){
        Connection cn = con2.conexion();
          String sql ="",aux;
    /*       "SELECT detallecreditproveed.num_creditCP,detallecreditproveed.num_detalleCP,productocal.codigo,productocal.nombreP,\n" +
                "	detallecreditproveed.cantidadCP,detallecreditproveed.costoCP,(detallecreditproveed.cantidadCP*detallecreditproveed.costoCP) AS prod\n" +
       */      
          if(fech.isEmpty()){//detallecreditproveed U creditomerca
             sql = "SELECT detallecreditproveed.cantidadCP,\n" +
                "productocal.nombreP,detallecreditproveed.costoCP,(detallecreditproveed.cantidadCP*detallecreditproveed.costoCP) AS prod\n" +
                "FROM \n" +
                "	detallecreditproveed\n" +
                "INNER JOIN \n" +
                "	creditomerca\n" +
                "ON\n" +
                "	detallecreditproveed.num_creditCP = creditomerca.num_credito AND\n" +//AND compraprooved.fechaCompra = '"+fech+"' 
                "	creditomerca.num_credito = '"+idC+"'\n" +
                "INNER JOIN\n" +
                "	productocal\n" +
                "ON\n" +
                "	productocal.codigo = detallecreditproveed.codigoProdCP;";
          }else{
              sql = "SELECT detailcompraprooved.id_compraP,detailcompraprooved.num_DCompraP,productocal.nombreP,\n" +
                "	detailcompraprooved.cantCajasC,detailcompraprooved.precCajaC,(detailcompraprooved.cantCajasC*detailcompraprooved.precCajaC) AS prod\n" +
                "FROM \n" +
                "	detailcompraprooved\n" +
                "INNER JOIN \n" +
                "	compraprooved\n" +
                "ON\n" +
                "	detailcompraprooved.id_compraP = compraprooved.id_compraProve AND compraprooved.fechaCompra = '"+fech+"' AND\n" +
                "	compraprooved.id_compraProve = '"+idC+"'\n" +
                "INNER JOIN\n" +
                "	productocal\n" +
                "ON\n" +
                "	productocal.codigo = detailcompraprooved.codigoProdC;";      
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
        
       //mostrar compra a proveedor con filtros de busqueda, por if, fehca, lapso de fechas
     public String[][] consultPrestfilters(String idPro,String fech1,String fech2,String status){
        Connection cn = con2.conexion();
        int cantColumnas=0,cantFilas=0;
        String[][] arre=null;
        String sql ="";
        if(status.isEmpty()){
              sql = "SELECT creditomerca.num_credito,creditomerca.id_ProveedorF FROM creditomerca "+
                        "INNER JOIN proveedor"+
                        " ON  creditomerca.id_ProveedorF = proveedor.id_Proveedor AND  (creditomerca.fechaPrestamo >= '"+fech1+"' AND creditomerca.fechaPrestamo <= '"+fech2+"'  )"
                       + "AND creditomerca.id_ProveedorF = '"+idPro+"' "           
                       + "ORDER BY creditomerca.fechaPrestamo;";           
        }else{
          sql = "SELECT creditomerca.num_credito,creditomerca.id_ProveedorF FROM creditomerca "+
                        "INNER JOIN proveedor"+
                        " ON  creditomerca.id_ProveedorF = proveedor.id_Proveedor AND  (creditomerca.fechaPrestamo >= '"+fech1+"' AND creditomerca.fechaPrestamo <= '"+fech2+"'  )"
                       + "AND creditomerca.id_ProveedorF = '"+idPro+"' AND creditomerca.status = '"+status+"' "           
                       + "ORDER BY creditomerca.fechaPrestamo;";           
        }
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
                       arre[i][0]=rs.getString(1);
                       arre[i][1]=rs.getString(2);
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
    }//@end consultComprafilters
     
              public int validaIsMasDeUnProductoPrest(int idMay,int idProd){
            Connection cn = con2.conexion();
            String[] existe = new String[2];
            int num=0,i=1;
            String sql = "";
            sql = "SELECT count(*) FROM detallecreditproveed WHERE num_creditCP = '"+idMay+"' AND codigoProdCP = '"+idProd+"' GROUP BY codigoProdCP ";
            Statement st = null;
            ResultSet rs= null;
            try {
                st = cn.createStatement();
                rs = st.executeQuery(sql);
                //rs.beforeFirst();
               while(rs.next())
                {
                        num =rs.getInt(1);
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
           return num;
    }//validasiexiste mas de un producto del mismo tipo
            
           public int sumaIsMasDeUnProductoPrest(int idMay,int idProd){
            Connection cn = con2.conexion();
            String[] existe = new String[2];
            int num=0,i=1;
            String sql = "";
            sql = "SELECT SUM(cantidadCP) FROM detallecreditproveed WHERE num_creditCP = '"+idMay+"' AND codigoProdCP = '"+idProd+"' ";//GROUP BY codigoProdC
            Statement st = null;
            ResultSet rs= null;
            try {
                st = cn.createStatement();
                rs = st.executeQuery(sql);
                //rs.beforeFirst();
               while(rs.next())
                {
                        num =rs.getInt(1);
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
           return num;
    }//validasiexiste mas de un producto del mismo tipo     
         
//*filtros buscar pedidos*/
        public String[][] regresaPaysDetPerfil(String idCli,String fech1,String fech2,String tipe){
        Connection cn = con2.conexion();
          String sql ="",aux;
          if(tipe.equals("pagopedidocli")){//OPCION pago de pedididoscli
              sql = "SELECT pagopedidocli.id_payPedCli,pagopedidocli.idClientePay,CONCAT(pagopedidocli.fechapayCliente,' , ', IF(pagopedidocli.horaPayPedido IS NULL,' -- ', pagopedidocli.horaPayPedido) ),pagopedidocli.montoPayCliente,\n" +
                "	pagopedidocli.notaPayCliente\n" +
                "FROM \n" +
                "	pagopedidocli\n" +
                "INNER JOIN \n" +
                "pedidocliente\n" +
                "ON\n" +
                "	pagopedidocli.idClientePay = pedidocliente.id_pedido AND pedidocliente.id_clienteP = '"+idCli+"'"
                      + "AND (pedidocliente.fechaPedidio >= '"+fech1+"' AND pedidocliente.fechaPedidio <= '"+fech2+"' );";      
          }
          if(tipe.equals("pagarcompraprovee")){//OPCION pago de pedididoscli
              sql = "SELECT pagarcompraprovee.id_paycompra,pagarcompraprovee.num_compraProveed,CONCAT(pagarcompraprovee.fechpayProveed,', ',IF(pagarcompraprovee.horaPayCompProov IS NULL,'--',pagarcompraprovee.horaPayCompProov)),pagarcompraprovee.montoPayProveed,\n" +
                "	pagarcompraprovee.notaPayProveed\n" +
                "FROM \n" +
                "	pagarcompraprovee\n" +
                "INNER JOIN \n" +
                "	compraprooved\n" +
                "ON\n" +
                "	pagarcompraprovee.num_compraProveed = compraprooved.id_compraProve AND (compraprooved.fechaCompra >=  '"+fech1+"' AND  compraprooved.fechaCompra <=  '"+fech2+"'   )"
                      + "AND compraprooved.id_ProveedorC =  '"+idCli+"'";      
          }
         if(tipe.equals("pagocreditprooved")){//OPCION pagoS de cREDIIT PROVEEDOR
              sql = "SELECT pagoCreditProoved.id_pay,pagoCreditProoved.idProovedPay,CONCAT(pagoCreditProoved.fechapayProoved,', ',IF(pagoCreditProoved.horaPayCreditProov IS NULL,'--',pagoCreditProoved.horaPayCreditProov)),pagoCreditProoved.montoPayProoved,\n" +
                "	pagoCreditProoved.notaPayProoved\n" +
                "FROM \n" +
                "	pagoCreditProoved\n" +
                "INNER JOIN \n" +
                "	creditomerca\n" +
                "ON\n" +
                "	pagoCreditProoved.idProovedPay = creditomerca.num_credito AND (creditomerca.fechaPrestamo >=  '"+fech1+"' AND  creditomerca.fechaPrestamo <=  '"+fech2+"')"
              + "AND creditomerca.id_ProveedorF =  '"+idCli+"'";      ;      
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
              sql = "SELECT pagoventapiso.id_payVentaP,pagoventapiso.num_notaVP,CONCAT(pagoventapiso.fechaPayVP,', ',IF(pagoventapiso.horaPayVentaPiso IS NULL,'--',pagoventapiso.horaPayVentaPiso)),pagoventapiso.montoVP,\n" +
                "	pagoventapiso.notaPayVP\n" +
                "FROM \n" +
                "	pagoventapiso\n" +
                "INNER JOIN \n" +
                "	notaventapiso\n" +
                "ON\n" +
                "	pagoventapiso.num_notaVP = notaventapiso.id_venta AND (notaventapiso.fechVenta >= '"+fech1+"' AND notaventapiso.fechVenta <= '"+fech2+"')"
                      + " AND notaventapiso.id_clientePiso = '"+idCli+"';";      
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
        public String regresaPaysSumaAll(String idCli,String fech1,String fech2,String tipe){
        Connection cn = con2.conexion();
          String sql ="",aux;
          if(tipe.equals("pagopedidocli")){//OPCION pago de pedididoscli
              sql = "SELECT SUM(pagopedidocli.montoPayCliente)\n" +
                "FROM \n" +
                "	pagopedidocli\n" +
               "INNER JOIN \n" +
                "pedidocliente\n" +
                "ON\n" +
                "	pagopedidocli.idClientePay = pedidocliente.id_pedido AND pedidocliente.id_clienteP = '"+idCli+"'"
                      + "AND (pedidocliente.fechaPedidio >= '"+fech1+"' AND pedidocliente.fechaPedidio <= '"+fech2+"' );";
          }
          if(tipe.equals("pagarcompraprovee")){//OPCION pago de pedididoscli
              sql = "SELECT SUM(pagarcompraprovee.montoPayProveed)\n" +
                "FROM \n" +
                "	pagarcompraprovee\n" +
                "INNER JOIN \n" +
                "	compraprooved\n" +
                "ON\n" +
                "	pagarcompraprovee.num_compraProveed = compraprooved.id_compraProve AND (compraprooved.fechaCompra >=  '"+fech1+"' AND  compraprooved.fechaCompra <=  '"+fech2+"'   )"
                      + "AND compraprooved.id_ProveedorC =  '"+idCli+"'";      
          }
         if(tipe.equals("pagocreditprooved")){//OPCION pagoS de cREDIIT PROVEEDOR
              sql = "SELECT SUM(pagocreditprooved.montoPayProoved)\n" +
                "FROM \n" +
                "	pagoCreditProoved\n" +
                "INNER JOIN \n" +
                "	creditomerca\n" +
                "ON\n" +
                "	pagoCreditProoved.idProovedPay = creditomerca.num_credito AND (creditomerca.fechaPrestamo >=  '"+fech1+"' AND  creditomerca.fechaPrestamo <=  '"+fech2+"')"
              + "AND creditomerca.id_ProveedorF =  '"+idCli+"'";      ;      
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
              sql = "SELECT SUM(pagoventapiso.montoVP)\n" +
                "FROM \n" +
                "	pagoventapiso\n" +
                "INNER JOIN \n" +
                "	notaventapiso\n" +
                "ON\n" +
                "	pagoventapiso.num_notaVP = notaventapiso.id_venta AND (notaventapiso.fechVenta >= '"+fech1+"' AND notaventapiso.fechVenta <= '"+fech2+"')"
                + " AND notaventapiso.id_clientePiso = '"+idCli+"';";      
          }
         if(tipe.equals("totalcreditSum")){//OPCION pagoS de cREDIIT PROVEEDOR
              sql = "SELECT SUM(detallecreditproveed.cantidadCP * detallecreditproveed.costoCP )\n" +
                "FROM \n" +
                "	detallecreditproveed\n" +
                "INNER JOIN \n" +
                "	creditomerca\n" +
                "ON\n" +
                "	detallecreditproveed.num_creditCP = creditomerca.num_credito AND (creditomerca.fechaPrestamo >=  '"+fech1+"' AND  creditomerca.fechaPrestamo <=  '"+fech2+"')"
              + "AND creditomerca.id_ProveedorF =  '"+idCli+"'";      ;      
          }
         if(tipe.equals("totalventPiso")){//OPCION pagoS de cREDIIT PROVEEDOR
              sql = "SELECT SUM(detailventapiso.cantidadV * detailventapiso.precioPV )\n" +
                "FROM \n" +
                "	detailventapiso\n" +
                "INNER JOIN \n" +
                "	notaventapiso\n" +
                "ON\n" +
                "	detailventapiso.num_notaV = notaventapiso.id_venta AND (notaventapiso.fechVenta >= '"+fech1+"' AND notaventapiso.fechVenta <= '"+fech2+"')"
                + " AND notaventapiso.id_clientePiso = '"+idCli+"';";      
         }
             int i =0,cantFilas=0, cont=1;
             String resultado="-1";
              int[] arrIdPedido = null;//int para usar hashMap
            Statement st = null;
            ResultSet rs = null;    
            int cantColumnas=0;
            try {
                st = cn.createStatement();
                rs = st.executeQuery(sql);
                while(rs.next())
                {
                    resultado=rs.getString(1);
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
                return resultado;            
}//@end regresaPaysSumaAll

       //mostrar compra a proveedor con filtros de busqueda, por if, fehca, lapso de fechas
     public String[][] consultVentPisofilters(String idPro,String fech1,String fech2,String status){
        Connection cn = con2.conexion();
        int cantColumnas=0,cantFilas=0;
        String[][] arre=null;
        String sql ="";
        if(status.isEmpty()){
            sql = "SELECT notaventapiso.id_venta,notaventapiso.id_clientePiso FROM notaventapiso "+
                        "INNER JOIN clientepedidos"+
                        " ON  notaventapiso.id_clientePiso = clientepedidos.id_cliente AND  (notaventapiso.fechVenta >= '"+fech1+"' AND notaventapiso.fechVenta <= '"+fech2+"'  )"
                       + "AND notaventapiso.id_clientePiso = '"+idPro+"' "           
                       + "ORDER BY notaventapiso.fechVenta;";
        }else{
          sql = "SELECT notaventapiso.id_venta,notaventapiso.id_clientePiso FROM notaventapiso "+
                        "INNER JOIN clientepedidos"+
                        " ON  notaventapiso.id_clientePiso = clientepedidos.id_cliente AND  (notaventapiso.fechVenta >= '"+fech1+"' AND notaventapiso.fechVenta <= '"+fech2+"'  )"
                       + "AND notaventapiso.id_clientePiso = '"+idPro+"'  AND notaventapiso.statusVent = '"+status+"'"           
                       + "ORDER BY notaventapiso.fechVenta;";
        }
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
                       arre[i][0]=rs.getString(1);
                       arre[i][1]=rs.getString(2);
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
    }//@end consultVentPisofilters
        
     
     //MEtodos gasto de caja
                            //OBTENER ULTIMO GASTO DE CAJA REALIZADO
          public int getUltimGastCaja(){
            Connection cn = con2.conexion();
            int idTurno = -1;
            String sql = "";
            sql = "SELECT id FROM gastos_caja ORDER BY id DESC LIMIT 1; ";
            Statement st = null;
            ResultSet rs= null;
            try {
                st = cn.createStatement();
                rs = st.executeQuery(sql);
                if(rs.next())
                {
                    if(rs.getRow() > 0){
                        idTurno = rs.getInt(1);
                    }else{
                         idTurno = -1;
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
           return idTurno;
    }//@end getUltimGastCaja
          
               //guarda gasto de caja
     public void guardGastoCaja(String[] param, String idT){
     Connection cn = con2.conexion();
            PreparedStatement pps=null;
            String SQL="",band="";      
            try {
          if(idT.isEmpty()){
 SQL="INSERT INTO gastos_caja (id,idTurno,fecha,hora,idRubrocaja,concepto,solicitante,obs,monto) VALUES (?,?,?,?,?,?,?,?,?)";                           
  pps = cn.prepareStatement(SQL);
                pps.setString(1, param[0]);
                pps.setString(2, param[1]);
                pps.setString(3, param[2]);
                pps.setString(4, param[3]);
                pps.setString(5, param[4]);
                pps.setString(6, param[5]);
                pps.setString(7, param[6]);
                pps.setString(8, param[7]);
                pps.setString(9, param[8]);
                band = "creado";
          }else{
SQL="UPDATE gastos_caja SET concepto=?,solicitante=?,obs=?,monto=? WHERE id = '"+idT+"'; ";              
pps = cn.prepareStatement(SQL);
                pps.setString(1, param[0]);
                pps.setString(2, param[1]);
                pps.setString(3, param[2]);
                pps.setString(4, param[3]);
                band="actualizado";
          }      
                pps.executeUpdate();
                JOptionPane.showMessageDialog(null, "Gasto "+band+" correctamente.");
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
} //@end guardGastoCaja
     
// metodo para guardar cancelaciones normales
     public void guardInCancelaciones(String[] param){
     Connection cn = con2.conexion();
            PreparedStatement pps=null;
            String SQL="";
            SQL="INSERT INTO cancelaciones (id,idUsuario,autorizo,idTurno,fecha,hora,motivo) VALUES (?,?,?,?,?,?,?)";                           
          try {
                pps = cn.prepareStatement(SQL);
                pps.setString(1, param[0]);
                pps.setString(2, param[1]);
                pps.setString(3, param[1]);
                pps.setString(4, param[2]);
                pps.setString(5, param[3]);
                pps.setString(6, param[4]);
                pps.setString(7, param[5]);
                pps.executeUpdate();
                JOptionPane.showMessageDialog(null, param[5]+" guardado correctamente.");
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
} //@end guardInCancelaciones

//Obtener el ultimo id de cancelaciones     
        public int getUltimCancelaciones(){
            Connection cn = con2.conexion();
            int idTurno = -1;
            String sql = "";
            sql = "SELECT id FROM cancelaciones ORDER BY id DESC LIMIT 1; ";
            Statement st = null;
            ResultSet rs= null;
            try {
                st = cn.createStatement();
                rs = st.executeQuery(sql);
                rs.beforeFirst();
                if(rs.next())
                {
                    if(rs.getRow() > 0){
                        idTurno = rs.getInt(1);
                    }else{
                         idTurno = -1;
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
           return idTurno;
    }//@endgetUltimPagoarea
          
            public static void main(String[] argv){
       controladorCFP control = new controladorCFP();
       List<String> contentL= control.regresaDatos(1,"4");
       ListIterator<String> itr=contentL.listIterator();
        control.cargaTotCompDayProveedor();
    }//main
}//class
