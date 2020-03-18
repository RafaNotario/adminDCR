/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Frame;

import conexiones.db.ConexionDBOriginal;
import controllers.altadeclientes.controladorCFP;
import internos.tickets.print.Funciones;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import controllers.altadeclientes.controladorCFP;
import java.sql.PreparedStatement;

/**
 *
 * @author  A. Rafael Notario Rodriguez
 */
public class detailPedido extends javax.swing.JFrame {
    
   static int param =0;

Funciones func = new Funciones();
ConexionDBOriginal con2 = new ConexionDBOriginal();
controladorCFP controller = new controladorCFP();
    /**
     * Creates new form detailPedido
     */
    public detailPedido(int param) {
        initComponents();
        this.param=param;
        txtNumPedidoDetail.setText(Integer.toString(param));
        consultDetail(param);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtNameCli = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txtCosto = new javax.swing.JTextField();
        txtFechDetail = new javax.swing.JTextField();
        txtNumPedidoDetail = new javax.swing.JTextField();
        txtTotCajas = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        txtStatusDetail = new javax.swing.JTextField();
        jButGuardDetail = new javax.swing.JButton();
        txtEliminaPedido = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel14 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTabDetailEspec = new javax.swing.JTable();
        jLabel15 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Detalle de pedido.");
        setBackground(new java.awt.Color(255, 255, 255));
        setResizable(false);
        setType(java.awt.Window.Type.POPUP);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel1.setText("Nota");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 150, 99, 26));

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setText("Fecha:");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(292, 11, 99, 35));

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel3.setText("Pedido no:");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(36, 33, 99, 35));

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel4.setText("Total Cajas:");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 290, 99, 35));
        getContentPane().add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 130, 310, 10));

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel5.setText("SEG:");
        jLabel5.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 230, 80, 35));

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel6.setText("Costo           $:");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 330, 99, 35));

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel7.setText("Cliente:");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 80, 99, 35));

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel8.setText("TERCERA:");
        jLabel8.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 430, 80, 35));

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel9.setText("PRIM:");
        jLabel9.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 190, 80, 35));

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel10.setText("PRIM_REG:");
        jLabel10.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        getContentPane().add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 270, 80, 35));

        txtNameCli.setEditable(false);
        txtNameCli.setBackground(new java.awt.Color(255, 255, 255));
        txtNameCli.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        getContentPane().add(txtNameCli, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 80, 110, 40));

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel11.setText("SEG_REG:");
        jLabel11.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        getContentPane().add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 310, 80, 35));

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel12.setText("BOLA_P:");
        jLabel12.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        getContentPane().add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 350, 80, 35));

        txtCosto.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        getContentPane().add(txtCosto, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 330, 80, 40));

        txtFechDetail.setEditable(false);
        txtFechDetail.setBackground(new java.awt.Color(255, 255, 255));
        txtFechDetail.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        getContentPane().add(txtFechDetail, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 10, 130, 40));

        txtNumPedidoDetail.setEditable(false);
        txtNumPedidoDetail.setBackground(new java.awt.Color(255, 255, 255));
        txtNumPedidoDetail.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        getContentPane().add(txtNumPedidoDetail, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 30, 90, 40));

        txtTotCajas.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        getContentPane().add(txtTotCajas, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 290, 80, 40));

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel13.setText("Status");
        getContentPane().add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 80, 70, 35));

        txtStatusDetail.setEditable(false);
        txtStatusDetail.setBackground(new java.awt.Color(255, 255, 255));
        txtStatusDetail.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        getContentPane().add(txtStatusDetail, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 80, 100, 40));

        jButGuardDetail.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/save32px.png"))); // NOI18N
        jButGuardDetail.setToolTipText("Actualiza pedido.");
        jButGuardDetail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButGuardDetailActionPerformed(evt);
            }
        });
        getContentPane().add(jButGuardDetail, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 400, 80, 60));

        txtEliminaPedido.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/delete32px.png"))); // NOI18N
        txtEliminaPedido.setToolTipText("Elimina pedido.");
        txtEliminaPedido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEliminaPedidoActionPerformed(evt);
            }
        });
        getContentPane().add(txtEliminaPedido, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 400, 80, 60));

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 180, 200, -1));

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel14.setText("Productos:");
        getContentPane().add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, 99, 26));

        jTabDetailEspec.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jTabDetailEspec.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "ID", "CANTIDAD", "PRECIO"
            }
        ));
        jTabDetailEspec.setAlignmentX(3.0F);
        jTabDetailEspec.setRowHeight(40);
        jTabDetailEspec.setRowMargin(2);
        jScrollPane2.setViewportView(jTabDetailEspec);
        if (jTabDetailEspec.getColumnModel().getColumnCount() > 0) {
            jTabDetailEspec.getColumnModel().getColumn(0).setMinWidth(0);
            jTabDetailEspec.getColumnModel().getColumn(0).setPreferredWidth(0);
            jTabDetailEspec.getColumnModel().getColumn(0).setMaxWidth(0);
        }

        getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 160, 210, 310));

        jLabel15.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel15.setText("BOLA_S:");
        jLabel15.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        getContentPane().add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 390, 80, 35));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtEliminaPedidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEliminaPedidoActionPerformed
        String borrar=txtNumPedidoDetail.getText();
        controller.elimaRow("pedidocliente","id_pedido",borrar);
        this.dispose();
    }//GEN-LAST:event_txtEliminaPedidoActionPerformed

    private void jButGuardDetailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButGuardDetailActionPerformed
        int elige = jTabDetailEspec.getSelectedRow();
        Object datoId = jTabDetailEspec.getValueAt(elige, 0),
                cant = jTabDetailEspec.getValueAt(elige, 1);
        String id = txtNumPedidoDetail.getText();
        if(datoId != null && !datoId.toString().isEmpty() && cant != null && !cant.toString().isEmpty())
            actualizaData(datoId.toString(), cant.toString(), id);
        else
            JOptionPane.showMessageDialog(null, "Debe elegir producto existente");
        System.out.println(datoId+" "+cant);
        // TODO add your handling code here:
    }//GEN-LAST:event_jButGuardDetailActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(detailPedido.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(detailPedido.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(detailPedido.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(detailPedido.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new detailPedido(param).setVisible(true);
            }
        });
    }
    
     public void consultDetail(int opc){
        Connection cn = con2.conexion();
        String sql ="";
              sql = "SELECT * FROM detailpedidio WHERE id_PedidioD = '"+opc+"'";           
            Statement st = null;
            ResultSet rs = null;            
            try {
                st = cn.createStatement();
                rs = st.executeQuery(sql);
                while(rs.next())
                {//es necesario el for para llenar dinamicamente la lista, ya que varia el numero de columnas de las tablas
                        for (int x=1;x<= rs.getMetaData().getColumnCount()-1;x++) {
                                if(x==3){
//                                    if(rs.getInt(x)==1){
                                        jTabDetailEspec.setValueAt(rs.getInt(x-2), rs.getInt(x)-1, 0);//valor,fila,columna
                                        jTabDetailEspec.setValueAt(rs.getInt(x+1), rs.getInt(x)-1, 1);//valor,fila,columna
                                        jTabDetailEspec.setValueAt(rs.getString(x+2), rs.getInt(x)-1, 2);//valor,fila,columna restamos uno porque el indice del jtable comienza en cero
  //                                  }
                                }
                            //System.out.print("["+x+"]"+" -> "+rs.getString(x));                   
                        }//for
                            System.out.println();
                }//while
            } catch (SQLException ex) {
                Logger.getLogger(detailPedido.class.getName()).log(Level.SEVERE, null, ex);
            }finally{               
             try {        
                 if(st != null) st.close();                
                 if(cn !=null) cn.close();
             } catch (SQLException ex) {
                 JOptionPane.showMessageDialog(null,ex.getMessage()); 
             }
         }//finally  
    }//regresaDatos
     
         public void actualizaData(String id, String cantidad, String param){
             Connection cn = con2.conexion();
            PreparedStatement pps=null;
            String SQL="";        

                SQL="UPDATE detailpedidio SET cantidadCajas=? WHERE num_DPedido ='"+id+"' AND id_PedidioD = '"+param+"' ";            
            try {
                pps = cn.prepareStatement(SQL);
                pps.setString(1, cantidad);
                pps.executeUpdate();
                JOptionPane.showMessageDialog(null, "Datos de pedido actualizados correctamente.");
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
  
    }//actualizaData
     
     public void recibeListData(List<String> list){
         txtFechDetail.setText(list.get(1));
         txtStatusDetail.setText(list.get(2));
         txtNameCli.setText(list.get(4));
         txtTotCajas.setText(list.get(5));
         txtCosto.setText(list.get(6));
         jTextArea1.setText(list.get(7));
     }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButGuardDetail;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTabDetailEspec;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField txtCosto;
    private javax.swing.JButton txtEliminaPedido;
    private javax.swing.JTextField txtFechDetail;
    private javax.swing.JTextField txtNameCli;
    private javax.swing.JTextField txtNumPedidoDetail;
    private javax.swing.JTextField txtStatusDetail;
    private javax.swing.JTextField txtTotCajas;
    // End of variables declaration//GEN-END:variables
}