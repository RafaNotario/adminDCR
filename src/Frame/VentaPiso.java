/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Frame;

import java.math.BigDecimal;
import javax.swing.JOptionPane;
import internos.tickets.print.Funciones;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import conexiones.db.ConexionDBOriginal;
import controllers.altadeclientes.controladorCFP;
import java.util.ArrayList;
import java.util.List;
import renderTable.TModel;

/**
 *
 * @author A. Rafael Notario Rodriguez
 */
public class VentaPiso extends javax.swing.JFrame {
Funciones fn = new Funciones();
ConexionDBOriginal con2 = new ConexionDBOriginal();
controladorCFP controlInserts = new controladorCFP();
String[] cabDet1 = {"idPago", "Fecha", "Monto", "Nota","Metodo"};

    static String monto;
    static String folio;
    static String tipe; 
    public VentaPiso(String monto, String folio,String typePay) {
        initComponents();
        this.monto = monto;
        this.folio = folio;
        this.tipe=typePay;
        
        jPanContadoPay.setVisible(false);
        jPanCreditPay.setVisible(false);
        jPanReferenciaPay.setVisible(false);
        
        txtCantCobrar.setText(monto);
        jDatePayFech.setDate(fn.cargafecha());
        
        txtAbonosPays.setText(controlInserts.sumAbonos(tipe, folio));
    }

    public VentaPiso() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tipoPay = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        jLayeredPane1 = new javax.swing.JLayeredPane();
        jPanContadoPay = new javax.swing.JPanel();
        jLabPagaOMonto = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        txtCambioPAgo = new javax.swing.JTextField();
        txtContadoRecibe = new javax.swing.JTextField();
        jRadContado = new javax.swing.JRadioButton();
        jRadParcial = new javax.swing.JRadioButton();
        jLabel26 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        txtnotaPay = new javax.swing.JTextField();
        jLaBRecomen = new javax.swing.JLabel();
        jPanCreditPay = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTabHistor = new javax.swing.JTable();
        jButton3 = new javax.swing.JButton();
        jPanReferenciaPay = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel6 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jButContado = new javax.swing.JButton();
        jButTransfer = new javax.swing.JButton();
        txtCantCobrar = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        txtAbonosPays = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jDatePayFech = new com.toedter.calendar.JDateChooser();
        jLabel25 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        txtidComp = new javax.swing.JTextField();
        txtProveedorName = new javax.swing.JTextField();
        jLabNameProv = new javax.swing.JLabel();
        jLabLetreroTransac = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("PAGOS");
        setBackground(new java.awt.Color(204, 204, 255));
        setName("frameVentasPiso"); // NOI18N
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel23.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel23.setText("ABONADO:");
        jPanel1.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 110, 90, 50));

        jLayeredPane1.setBackground(new java.awt.Color(204, 255, 255));
        jLayeredPane1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanContadoPay.setBackground(new java.awt.Color(255, 255, 255));
        jPanContadoPay.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Pago a Contado", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13))); // NOI18N
        jPanContadoPay.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanContadoPay.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabPagaOMonto.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabPagaOMonto.setText("PAGO CON:");
        jPanContadoPay.add(jLabPagaOMonto, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 80, 130, 50));

        jLabel22.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel22.setText("Tipo de Pago:");
        jPanContadoPay.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 110, 40));

        txtCambioPAgo.setEditable(false);
        txtCambioPAgo.setBackground(new java.awt.Color(255, 255, 255));
        txtCambioPAgo.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtCambioPAgo.setForeground(new java.awt.Color(0, 51, 204));
        txtCambioPAgo.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanContadoPay.add(txtCambioPAgo, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 140, 90, 50));

        txtContadoRecibe.setFont(new java.awt.Font("Tahoma", 1, 17)); // NOI18N
        txtContadoRecibe.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtContadoRecibe.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtContadoRecibeFocusLost(evt);
            }
        });
        jPanContadoPay.add(txtContadoRecibe, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 80, 120, 50));

        tipoPay.add(jRadContado);
        jRadContado.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jRadContado.setText("TOTAL");
        jRadContado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadContadoActionPerformed(evt);
            }
        });
        jPanContadoPay.add(jRadContado, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 30, 150, 40));

        tipoPay.add(jRadParcial);
        jRadParcial.setText("PARCIAL");
        jRadParcial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadParcialActionPerformed(evt);
            }
        });
        jPanContadoPay.add(jRadParcial, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 30, 150, 40));

        jLabel26.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel26.setText("NOTA:");
        jPanContadoPay.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 210, 70, 50));

        jLabel28.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel28.setText("CAMBIO:");
        jPanContadoPay.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 140, 110, 40));

        txtnotaPay.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanContadoPay.add(txtnotaPay, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 210, 340, 50));

        jLaBRecomen.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLaBRecomen.setForeground(new java.awt.Color(0, 204, 204));
        jPanContadoPay.add(jLaBRecomen, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 80, 130, 50));

        jLayeredPane1.add(jPanContadoPay, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 510, 290));

        jPanCreditPay.setBackground(new java.awt.Color(255, 255, 255));
        jPanCreditPay.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Historial de pagos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N
        jPanCreditPay.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTabHistor.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTabHistor.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTabHistor.setRowHeight(30);
        jScrollPane1.setViewportView(jTabHistor);

        jPanCreditPay.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 410, 260));

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/delete32px.png"))); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanCreditPay.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 30, 60, 50));

        jLayeredPane1.add(jPanCreditPay, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 510, 300));

        jPanReferenciaPay.setBackground(new java.awt.Color(255, 255, 255));
        jPanReferenciaPay.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Referencia de Pago", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N
        jPanReferenciaPay.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTextField1.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanReferenciaPay.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 30, 320, 40));

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel5.setText("MONTO:");
        jPanReferenciaPay.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 80, 40));

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane2.setViewportView(jTextArea1);

        jPanReferenciaPay.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 130, 250, 40));

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel6.setText("NOTA:");
        jPanReferenciaPay.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 140, 80, 30));
        jPanReferenciaPay.add(jTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 80, 160, 40));

        jLayeredPane1.add(jPanReferenciaPay, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 450, 180));

        jPanel1.add(jLayeredPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 190, 520, 300));

        jButContado.setBackground(new java.awt.Color(204, 255, 204));
        jButContado.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/cash.png"))); // NOI18N
        jButContado.setToolTipText("PAGO EN EFECTIVO");
        jButContado.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButContado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButContadoActionPerformed(evt);
            }
        });
        jPanel1.add(jButContado, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, 100, 70));

        jButTransfer.setBackground(new java.awt.Color(204, 255, 204));
        jButTransfer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/transfer.png"))); // NOI18N
        jButTransfer.setToolTipText("HISTORIAL DE PAGOS");
        jButTransfer.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButTransfer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButTransferActionPerformed(evt);
            }
        });
        jPanel1.add(jButTransfer, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 100, 100, 70));

        txtCantCobrar.setEditable(false);
        txtCantCobrar.setBackground(new java.awt.Color(255, 255, 255));
        txtCantCobrar.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        txtCantCobrar.setForeground(new java.awt.Color(0, 0, 255));
        txtCantCobrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCantCobrarActionPerformed(evt);
            }
        });
        jPanel1.add(txtCantCobrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 10, 130, 50));

        jLabel27.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel27.setText("METODO DE PAGO:");
        jPanel1.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 140, 20));

        jLabel24.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel24.setText("IMPORTE:");
        jPanel1.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 10, 110, 50));

        txtAbonosPays.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jPanel1.add(txtAbonosPays, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 110, 150, 50));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 50, 530, 500));

        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/icons8_Multiply_32px.png"))); // NOI18N
        jButton1.setText("Cancelar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 250, 120, 60));

        jButton2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/payMethod.png"))); // NOI18N
        jButton2.setText("Cobrar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 160, 120, 60));

        jDatePayFech.setDateFormatString("dd/MM/yyyy");
        jDatePayFech.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel2.add(jDatePayFech, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 150, 50));

        jLabel25.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel25.setText("Fecha:");
        jPanel2.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 140, 20));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 50, 170, 500));

        jPanel3.setBackground(new java.awt.Color(153, 255, 153));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtidComp.setEditable(false);
        txtidComp.setBackground(new java.awt.Color(153, 255, 153));
        txtidComp.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        txtidComp.setBorder(null);
        jPanel3.add(txtidComp, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 0, 80, 50));

        txtProveedorName.setEditable(false);
        txtProveedorName.setBackground(new java.awt.Color(153, 255, 153));
        txtProveedorName.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        txtProveedorName.setBorder(null);
        jPanel3.add(txtProveedorName, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 0, 190, 50));

        jLabNameProv.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        jLabNameProv.setForeground(new java.awt.Color(51, 0, 204));
        jLabNameProv.setText("Proveedor:");
        jPanel3.add(jLabNameProv, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 0, 110, 50));

        jLabLetreroTransac.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        jLabLetreroTransac.setForeground(new java.awt.Color(51, 0, 204));
        jLabLetreroTransac.setText("COBRAR VENTA");
        jPanel3.add(jLabLetreroTransac, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 220, 50));

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 700, 50));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        String var_id = txtidComp.getText(),//txtContadoRecibe,
                var_fech = fn.getFecha(jDatePayFech),
                var_cant = txtCantCobrar.getText(),
                var_paga = txtContadoRecibe.getText(),//monto a abonar
                var_abonos = txtAbonosPays.getText(),
                var_nota = txtnotaPay.getText(),
                var_metod = "1";//para metodo de pago
                List<String> dataList = new ArrayList<String>();
               
            BigDecimal amountOne = new BigDecimal(var_paga);//monto a cobrar
            BigDecimal amountTwo = new BigDecimal(var_abonos);//cantidad recivida
            BigDecimal amountThree = new BigDecimal(var_cant);
if(jPanContadoPay.isVisible()){
       //     JOptionPane.showMessageDialog(null,"Pago al contado");
       if(amountThree.compareTo(fn.getSum(amountOne, amountTwo)) >= 0){
            if(jRadContado.isSelected()){
                if(var_paga.isEmpty()){
                    JOptionPane.showMessageDialog(null, "Debe ingresar monto.");
                }else{
                   dataList.add(var_id);
                   dataList.add(var_fech);
                   dataList.add(var_paga);
                   dataList.add(var_nota);
                   dataList.add(var_metod);
                    guardaPayCompra(dataList,tipe);
                    if(tipe.equals("pagarcompraprovee"))//nombre de la tabla a la q se le aplicara el pago
                        actualizaData(var_id,"compraprooved");
                    else if(tipe.equals("pagoventapiso"))
                        actualizaData(var_id,"notaventapiso");
                    else if(tipe.equals("pagopedidocli"))
                        actualizaData(var_id,"pedidocliente");
                    else if(tipe.equals("pagocreditprooved"))
                        actualizaData(var_id,"creditomerca");
                    else if(tipe.equals("pagoflete"))
                        actualizaData(var_id,"fleteenviado");
                }
                
            }else if(jRadParcial.isSelected()){
                dataList.add(var_id);
                dataList.add(var_fech);
                dataList.add(var_paga);
                dataList.add(var_nota);
                dataList.add(var_metod);
                guardaPayCompra(dataList,tipe);
            }//else if jRadParcial
             }else{//if comparacion que no guarde mas de la cantidad    
           JOptionPane.showMessageDialog(null, "No puede guardar mas que el total de la compra");
         } 
       }else{
            JOptionPane.showMessageDialog(null,"Elija metodo de pago");
        }     
        jPanContadoPay.setVisible(false);
        jPanCreditPay.setVisible(false);
        limpiaCampos();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButContadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButContadoActionPerformed
        jPanContadoPay.setVisible(true);
        jPanCreditPay.setVisible(false);
        jPanReferenciaPay.setVisible(false);
       
        
    }//GEN-LAST:event_jButContadoActionPerformed

    private void txtContadoRecibeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtContadoRecibeFocusLost
         String cant = txtCantCobrar.getText(),//costo total
                cos = txtContadoRecibe.getText(),
                 abonos = txtAbonosPays.getText();//monto abonados
                txtCambioPAgo.setText("");

        if (cant.isEmpty() || cos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Campo cantidad o monto vacios\n Verifique Por favor");
        } else {
            
            BigDecimal amountOne = new BigDecimal(cant);//monto a cobrar
            BigDecimal amountTwo = new BigDecimal(cos);//cantidad recivida
            BigDecimal amountTres = new BigDecimal(abonos);//cantidad recivida
            BigDecimal res = new BigDecimal(0);//cantidad recivida
            
             res =fn.getDifference(amountOne,amountTres);
             
            if(jRadContado.isSelected() ){
                jLabel28.setText("Cambio : ");
                
            }else if(jRadParcial.isSelected()){
            //    txtCambioPAgo.setText(fn.getDifference(amountOne,amountTwo).toString());
                jLabel28.setText("Restan: ");
            }
                txtCambioPAgo.setText(fn.getDifference(res,amountTwo).toString());
        }
    }//GEN-LAST:event_txtContadoRecibeFocusLost

    private void jRadParcialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadParcialActionPerformed
        String cant = txtCantCobrar.getText(),//costo total
            cos = txtAbonosPays.getText();//abonados
            BigDecimal amountOne = new BigDecimal(cant);//monto a cobrar
            BigDecimal amountTwo = new BigDecimal(cos);//cantidad recivida
            txtContadoRecibe.setText(fn.getDifference(amountOne,amountTwo).toString());
            jLabPagaOMonto.setText("Monto maximo a abonar: ");
            jLaBRecomen.setText("<-Debe ser menor que el total");
            limpiaCampos();
            
    }//GEN-LAST:event_jRadParcialActionPerformed

    private void jRadContadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadContadoActionPerformed
        String abonado = txtAbonosPays.getText(),
                cant = txtCantCobrar.getText();        
                jLabPagaOMonto.setText("Paga con: ");
       // String var = txtAbonosPays.getText();
        limpiaCampos();
        if(abonado.equals("0.0")){
            jLaBRecomen.setText("<-Debe ser mayor o igual que el total");
            
        }else{
             BigDecimal amountOne = new BigDecimal(cant);//monto a cobrar
            BigDecimal amountTwo = new BigDecimal(abonado);//cantidad recivida
            txtContadoRecibe.setText(fn.getDifference(amountOne,amountTwo).toString());
             jLaBRecomen.setText("<- monto restante");
        }
                
    }//GEN-LAST:event_jRadContadoActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButTransferActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButTransferActionPerformed
        jPanContadoPay.setVisible(false);
        jPanCreditPay.setVisible(true);
        jPanReferenciaPay.setVisible(false);
        
        switch (tipe){
            case "pagopedidocli":
                String[][] matFlet = controlInserts.regresaPays(folio,tipe);
                jTabHistor.setModel(new TModel(matFlet,cabDet1));
                jTabHistor.getColumnModel().getColumn(0).setMaxWidth(0);
       //       break;
           case "pagarcompraprovee":
                String[][] matFlet2 = controlInserts.regresaPays(folio,tipe);
                jTabHistor.setModel(new TModel(matFlet2,cabDet1));
                jTabHistor.getColumnModel().getColumn(0).setMaxWidth(0);
       // jTabHistor.getColumnModel().getColumn(0).setMinWidth(0);
       // jTabHistor.getColumnModel().getColumn(0).setPreferredWidth(0);
                break;
         case "pagocreditprooved":
                String[][] matFlet3 = controlInserts.regresaPays(folio,tipe);
                jTabHistor.setModel(new TModel(matFlet3,cabDet1));
                jTabHistor.getColumnModel().getColumn(0).setMaxWidth(0);
       // jTabHistor.getColumnModel().getColumn(0).setMinWidth(0);
       // jTabHistor.getColumnModel().getColumn(0).setPreferredWidth(0);
                break;
         case "pagoflete":
                String[][] matFlet4 = controlInserts.regresaPays(folio,tipe);
                jTabHistor.setModel(new TModel(matFlet4,cabDet1));
                jTabHistor.getColumnModel().getColumn(0).setMaxWidth(0);
       // jTabHistor.getColumnModel().getColumn(0).setMinWidth(0);
       // jTabHistor.getColumnModel().getColumn(0).setPreferredWidth(0);
                break;
         case "pagoventapiso":
                String[][] matFlet5 = controlInserts.regresaPays(folio,tipe);
                jTabHistor.setModel(new TModel(matFlet5,cabDet1));
                jTabHistor.getColumnModel().getColumn(0).setMaxWidth(0);
       // jTabHistor.getColumnModel().getColumn(0).setMinWidth(0);
       // jTabHistor.getColumnModel().getColumn(0).setPreferredWidth(0);
                break;
        };
//        jTabHistor.getColumnModel().getColumn(0).setMinWidth(0);
  //      jTabHistor.getColumnModel().getColumn(0).setPreferredWidth(0);
          
    }//GEN-LAST:event_jButTransferActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        int elim = jTabHistor.getSelectedRow();
        if(elim > -1){//poner switch
            String id = jTabHistor.getValueAt(elim, 0).toString();
             switch (tipe){
            case "pagopedidocli":               
                controlInserts.elimaRow("pagopedidocli","id_payPedcli",id);
             break;
            case "pagarcompraprovee":
                controlInserts.elimaRow("pagarcompraprovee","id_paycompra",id);
             break;
            case "pagocreditprooved":
                controlInserts.elimaRow("pagocreditprooved","id_pay",id);
             break;
             case "pagoflete":
                controlInserts.elimaRow("pagoflete","id_pagoFlete",id);
             break;
             case "pagoventapiso":
                controlInserts.elimaRow("pagoventapiso","id_payVentaP",id);
             break;
             };
             }else{
            JOptionPane.showMessageDialog(null, "No ha realizado pagos");
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void txtCantCobrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCantCobrarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCantCobrarActionPerformed

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
            java.util.logging.Logger.getLogger(VentaPiso.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VentaPiso.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VentaPiso.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VentaPiso.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VentaPiso(monto,folio,tipe).setVisible(true);
            }
        });
    }
    
      public void guardaPayCompra(List<String> datas, String table){
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
                JOptionPane.showMessageDialog(null, "Pago realizado correctamente");
            } catch (SQLException ex) {
                Logger.getLogger(VentaPiso.class.getName()).log(Level.SEVERE, null, ex);
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
      
    public void actualizaData(String id,String table){
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
                JOptionPane.showMessageDialog(null, "Pago realizado correctamente a compra No:"+id);
            } catch (SQLException ex) {
                Logger.getLogger(VentaPiso.class.getName()).log(Level.SEVERE, null, ex);
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
    
        public String regresaTotPagos(String param,String id){
        Connection cn = con2.conexion();
          String suma ="";
           String sql ="";
           switch (param){
               case "pagarcompraprovee":
          sql = "SELECT SUM(montoPayProveed) FROM pagarcompraprovee WHERE num_compraProveed='"+id+"' ";           
                   break;
               case "pagoventapiso":
                     sql = "SELECT * FROM proveedor WHERE id_Proveedor = '"+param+"'";          
                   break;
               case "pagopedidocli":
                   sql = "SELECT * FROM empleado WHERE id_Empleado = '"+param+"'";    
                   break;
               case "pagocreditprooved":
                    sql = "SELECT * FROM fletero WHERE id_Fletero = '"+param+"'";           
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
            return suma;
    }//regresaDatos
    
    void limpiaCampos(){
        txtContadoRecibe.setText("");
        txtCambioPAgo.setText("");
        txtnotaPay.setText("");
    }
    
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButContado;
    public javax.swing.JButton jButTransfer;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private com.toedter.calendar.JDateChooser jDatePayFech;
    private javax.swing.JLabel jLaBRecomen;
    public javax.swing.JLabel jLabLetreroTransac;
    public javax.swing.JLabel jLabNameProv;
    private javax.swing.JLabel jLabPagaOMonto;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JPanel jPanContadoPay;
    private javax.swing.JPanel jPanCreditPay;
    private javax.swing.JPanel jPanReferenciaPay;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JRadioButton jRadContado;
    private javax.swing.JRadioButton jRadParcial;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTabHistor;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.ButtonGroup tipoPay;
    private javax.swing.JTextField txtAbonosPays;
    private javax.swing.JTextField txtCambioPAgo;
    private javax.swing.JTextField txtCantCobrar;
    private javax.swing.JTextField txtContadoRecibe;
    public javax.swing.JTextField txtProveedorName;
    public javax.swing.JTextField txtidComp;
    private javax.swing.JTextField txtnotaPay;
    // End of variables declaration//GEN-END:variables
}
