/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Frame;

import conexiones.db.ConexionDBOriginal;
import controllers.altadeclientes.controladorCFP;
import controllers.altadeclientes.datesControl;

import internos.tickets.print.Funciones;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.midi.SysexMessage;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import renderTable.ColorCelda;
import renderTable.TModel;

 
public class historCliente extends javax.swing.JFrame {

    Funciones fn = new Funciones();
    datesControl datC = new datesControl();
    controladorCFP controlInserts = new controladorCFP();
    ConexionDBOriginal con2 = new ConexionDBOriginal();

    String[] cabDet1 = {"idPago","#Compra", "Fecha", "Monto", "Nota"};
    
     DefaultTableModel dtmPrest,//obtener modelo en tabla CreaPedido clientes
            dtmPrec,
            tabCompras;//tabla diamica de compras del dia
     
    static String idProvd ="";
    
    public historCliente(String idprv) {
        initComponents();
        this.idProvd = idprv;
        iniFech();
       
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPanCompras = new javax.swing.JScrollPane();
        jTablePedidos = new javax.swing.JTable();
        jLabel8 = new javax.swing.JLabel();
        jScrollPanTotCompras = new javax.swing.JScrollPane();
        jTabsumtotales = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTabPrestamos = new javax.swing.JTable();
        jLabel9 = new javax.swing.JLabel();
        jLabLetreroCompras = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel10 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabnumProv = new javax.swing.JLabel();
        jLabNombProv = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel5 = new javax.swing.JLabel();
        jRadOneFecha = new javax.swing.JRadioButton();
        jRadLapsoFechs = new javax.swing.JRadioButton();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/ambulantes.png"))); // NOI18N
        jLabel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jPanel1.setBackground(new java.awt.Color(204, 255, 204));

        jTablePedidos.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTablePedidos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Status", "Fecha", "PRIM", "SEG", "PRIM_R", "SEG_R", "BOLA_P", "BOLA_S", "3_RA", "#CAJA", "IMPORTE"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, true, false, false, false, false, false, false, false, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTablePedidos.setRowHeight(23);
        jScrollPanCompras.setViewportView(jTablePedidos);
        if (jTablePedidos.getColumnModel().getColumnCount() > 0) {
            jTablePedidos.getColumnModel().getColumn(0).setMinWidth(40);
            jTablePedidos.getColumnModel().getColumn(0).setPreferredWidth(40);
            jTablePedidos.getColumnModel().getColumn(0).setMaxWidth(55);
            jTablePedidos.getColumnModel().getColumn(3).setMinWidth(50);
            jTablePedidos.getColumnModel().getColumn(3).setPreferredWidth(50);
            jTablePedidos.getColumnModel().getColumn(3).setMaxWidth(75);
            jTablePedidos.getColumnModel().getColumn(4).setMinWidth(50);
            jTablePedidos.getColumnModel().getColumn(4).setPreferredWidth(50);
            jTablePedidos.getColumnModel().getColumn(4).setMaxWidth(75);
            jTablePedidos.getColumnModel().getColumn(5).setMinWidth(55);
            jTablePedidos.getColumnModel().getColumn(5).setPreferredWidth(55);
            jTablePedidos.getColumnModel().getColumn(5).setMaxWidth(75);
            jTablePedidos.getColumnModel().getColumn(6).setMinWidth(55);
            jTablePedidos.getColumnModel().getColumn(6).setPreferredWidth(55);
            jTablePedidos.getColumnModel().getColumn(6).setMaxWidth(75);
            jTablePedidos.getColumnModel().getColumn(7).setMinWidth(60);
            jTablePedidos.getColumnModel().getColumn(7).setPreferredWidth(60);
            jTablePedidos.getColumnModel().getColumn(7).setMaxWidth(75);
            jTablePedidos.getColumnModel().getColumn(8).setMinWidth(60);
            jTablePedidos.getColumnModel().getColumn(8).setPreferredWidth(60);
            jTablePedidos.getColumnModel().getColumn(8).setMaxWidth(75);
            jTablePedidos.getColumnModel().getColumn(9).setMinWidth(55);
            jTablePedidos.getColumnModel().getColumn(9).setPreferredWidth(55);
            jTablePedidos.getColumnModel().getColumn(9).setMaxWidth(75);
            jTablePedidos.getColumnModel().getColumn(10).setMinWidth(65);
            jTablePedidos.getColumnModel().getColumn(10).setPreferredWidth(65);
            jTablePedidos.getColumnModel().getColumn(10).setMaxWidth(90);
            jTablePedidos.getColumnModel().getColumn(11).setMinWidth(120);
            jTablePedidos.getColumnModel().getColumn(11).setPreferredWidth(120);
            jTablePedidos.getColumnModel().getColumn(11).setMaxWidth(120);
        }

        jLabel8.setFont(new java.awt.Font("Tahoma", 3, 15)); // NOI18N
        jLabel8.setText("Pedidos Realizados");

        jTabsumtotales.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "PRIM", "SEG", "PRIM_R", "SEG_R", "BOLA_P", "BOLA_S", "3_RA", "TOT CAJAS", "IMPORTE"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, true, true, true, true, true, true, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTabsumtotales.setRowHeight(23);
        jScrollPanTotCompras.setViewportView(jTabsumtotales);
        if (jTabsumtotales.getColumnModel().getColumnCount() > 0) {
            jTabsumtotales.getColumnModel().getColumn(8).setMinWidth(100);
            jTabsumtotales.getColumnModel().getColumn(8).setPreferredWidth(100);
            jTabsumtotales.getColumnModel().getColumn(8).setMaxWidth(120);
        }

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jTable3.setRowHeight(23);
        jScrollPane3.setViewportView(jTable3);

        jTabPrestamos.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTabPrestamos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "FECHA", "PRIM", "SEG", "PRIM_R", "SEG_R", "BOLA_P", "BOLA_S", "3_RA", "CAJA", "S BRICE B", "S ROCIO L", "EFECTIVO", "IMPORTE"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, true, true, true, true, true, true, true, true, true, true, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTabPrestamos.setRowHeight(23);
        jScrollPane4.setViewportView(jTabPrestamos);
        if (jTabPrestamos.getColumnModel().getColumnCount() > 0) {
            jTabPrestamos.getColumnModel().getColumn(0).setMinWidth(40);
            jTabPrestamos.getColumnModel().getColumn(0).setPreferredWidth(40);
            jTabPrestamos.getColumnModel().getColumn(0).setMaxWidth(55);
        }

        jLabel9.setFont(new java.awt.Font("Tahoma", 3, 15)); // NOI18N
        jLabel9.setText("Ventas de Piso");

        jLabLetreroCompras.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabLetreroCompras.setForeground(new java.awt.Color(51, 0, 255));
        jLabLetreroCompras.setText("jLabel10");

        jLabel3.setFont(new java.awt.Font("Tahoma", 3, 15)); // NOI18N
        jLabel3.setText("Pagos realizados");

        jTable1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jTable1.setRowHeight(23);
        jScrollPane1.setViewportView(jTable1);

        jLabel10.setFont(new java.awt.Font("Tahoma", 3, 15)); // NOI18N
        jLabel10.setText("Pagos venta de piso");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel4.setText("Total pagos: $");

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel11.setText("jLabel11");

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel12.setText("Total pagos : $");

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel13.setText("jLabel13");

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel14.setText("jLabel14");

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel15.setText("jLabel15");

        jLabel16.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel16.setText("Total credito: $");
        jLabel16.setToolTipText("");

        jLabel17.setFont(new java.awt.Font("Tahoma", 3, 15)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(0, 0, 255));
        jLabel17.setText("jLabel17");

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel18.setText("Diferencia: $");

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel19.setText("Diferencia: $");

        jLabel20.setFont(new java.awt.Font("Tahoma", 3, 16)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(0, 0, 204));
        jLabel20.setText("jLabel20");

        jLabel21.setFont(new java.awt.Font("Tahoma", 3, 16)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(0, 0, 204));
        jLabel21.setText("jLabel21");

        jLabel22.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel22.setText("Pedidos:");

        jLabel23.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel23.setText("Entregados:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(312, 312, 312)
                        .addComponent(jLabLetreroCompras, javax.swing.GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 248, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPanTotCompras, javax.swing.GroupLayout.DEFAULT_SIZE, 794, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane4)
                    .addComponent(jScrollPanCompras)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel3)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jLabel14))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel4)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel18)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel19)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addContainerGap(86, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addGap(24, 24, 24))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jLabLetreroCompras)
                    .addComponent(jLabel3)
                    .addComponent(jLabel14))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 269, Short.MAX_VALUE)
                    .addComponent(jScrollPanCompras, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPanTotCompras, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel11))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel18)
                                    .addComponent(jLabel21)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel22)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel23)))))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10)
                    .addComponent(jLabel15))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel12)
                            .addComponent(jLabel13))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel19)
                            .addComponent(jLabel20))
                        .addGap(30, 30, 30))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel16)
                            .addComponent(jLabel17))
                        .addGap(55, 55, 55))))
        );

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel2.setText("Cliente");

        jLabnumProv.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabnumProv.setText("#");

        jLabNombProv.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabNombProv.setText("--");

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel5.setText("Elija:");

        buttonGroup1.add(jRadOneFecha);
        jRadOneFecha.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jRadOneFecha.setText("Fecha");
        jRadOneFecha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadOneFechaActionPerformed(evt);
            }
        });

        buttonGroup1.add(jRadLapsoFechs);
        jRadLapsoFechs.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jRadLapsoFechs.setText("Lapso de fechas.");
        jRadLapsoFechs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadLapsoFechsActionPerformed(evt);
            }
        });

        jDateChooser1.setDateFormatString("dd/MM/yyyy");
        jDateChooser1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel6.setText("Fecha 1:");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel7.setText("Fecha 2:");

        jDateChooser2.setDateFormatString("dd/MM/yyyy");
        jDateChooser2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jButton1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/search-what.png"))); // NOI18N
        jButton1.setText("Buscar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabnumProv)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabNombProv, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(13, 13, 13)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jRadOneFecha)
                        .addGap(51, 51, 51)
                        .addComponent(jRadLapsoFechs))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))))
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabnumProv)
                            .addComponent(jLabNombProv)))
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(jRadOneFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jRadLapsoFechs, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jDateChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jDateChooser2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButton1)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(10, 10, 10)
                                        .addComponent(jLabel7))))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(16, 16, 16)
                                .addComponent(jLabel6)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        String fechO = fn.getFecha(jDateChooser1),
                fechT = fn.getFecha(jDateChooser2);
        Object tot1,totpayC,totPrest,totParPrest;
        BigDecimal sum1, sum2;
        if(jRadOneFecha.isSelected()){
            cargaComprasDiaAsign(idProvd,fechO,fechO,"");
            cargaTotCompDayProveedor(idProvd,fechO,fechO,"");
   
         cargaPrestDet(idProvd,fechO,fechO,"");
         jLabel17.setText(controlInserts.regresaPaysSumaAll(idProvd,fechO,fechO,"totalventPiso"));
            
            String[][] matFlet2 = controlInserts.regresaPaysDetPerfil(idProvd,fechO,fechO,"pagopedidocli");
            jTable3.setModel(new TModel(matFlet2,cabDet1));
                jLabel11.setText( controlInserts.regresaPaysSumaAll(idProvd,fechO,fechO,"pagopedidocli"));//TOTAL DE PAGOS compra mercancia
                jLabel14.setText(Integer.toString(jTable3.getRowCount()));  

//VENTAS DE PISO
           String[][] matCredit2 = controlInserts.regresaPaysDetPerfil(idProvd,fechO,fechO,"pagoventapiso");
                jTable1.setModel(new TModel(matCredit2,cabDet1));
                jLabel13.setText( controlInserts.regresaPaysSumaAll(idProvd,fechO,fechO,"pagoventapiso"));//TOTAL DE PAGOS compra mercancia
                jLabel15.setText(Integer.toString(jTable1.getRowCount()));    

 }//@end solo una fecha
        
        if(jRadLapsoFechs.isSelected()){
            cargaComprasDiaAsign(idProvd,fechO,fechT,"");//llena tabla de compras
            cargaTotCompDayProveedor(idProvd,fechO,fechT,"");//llena tabla de suma de totales
            
           cargaPrestDet(idProvd,fechO,fechT,"");//llena tabla de ventas de piso
            jLabel17.setText(controlInserts.regresaPaysSumaAll(idProvd,fechO,fechT,"totalventPiso"));
      
                String[][] matFlet2 = controlInserts.regresaPaysDetPerfil(idProvd,fechO,fechT,"pagopedidocli");
                jTable3.setModel(new TModel(matFlet2,cabDet1));
      
                jLabel11.setText( controlInserts.regresaPaysSumaAll(idProvd,fechO,fechT,"pagopedidocli"));//TOTAL DE PAGOS compra mercancia
                jLabel14.setText(Integer.toString(jTable3.getRowCount()));
                
                     
                String[][] matCredit2 = controlInserts.regresaPaysDetPerfil(idProvd,fechO,fechT,"pagoventapiso");
                jTable1.setModel(new TModel(matCredit2,cabDet1));
                jLabel13.setText( controlInserts.regresaPaysSumaAll(idProvd,fechO,fechT,"pagoventapiso"));//TOTAL DE PAGOS compra mercancia
                jLabel15.setText(Integer.toString(jTable1.getRowCount()));
          }
        
        tot1 = jTabsumtotales.getValueAt(0, 8);
        totpayC = jLabel11.getText();
        
        totPrest =jLabel17.getText();
        totParPrest =jLabel13.getText();
        tot1 = (tot1 != null && !tot1.toString().isEmpty()) ? tot1 :"0" ;
        totpayC = (totpayC != null && !totpayC.toString().isEmpty()) ? totpayC :"0" ;
        
        sum1 = fn.getDifference(new BigDecimal(tot1.toString()), new BigDecimal(totpayC.toString()));
        jLabel21.setText(sum1.toString());

        totPrest = (totPrest != null && !totPrest.toString().isEmpty()) ? totPrest :"0" ;
        totParPrest = (totParPrest != null && !totParPrest.toString().isEmpty()) ? totParPrest :"0" ;
        sum2 = fn.getDifference(new BigDecimal(totPrest.toString()), new BigDecimal(totParPrest.toString()));
        jLabel20.setText(sum2.toString());
       
       jTable3.getColumnModel().getColumn(0).setMaxWidth(0);
       jTable3.getColumnModel().getColumn(0).setMinWidth(0);
       jTable3.getColumnModel().getColumn(0).setPreferredWidth(0);
       
        jTable3.getColumnModel().getColumn(1).setMaxWidth(100);
       jTable3.getColumnModel().getColumn(1).setMinWidth(55);
       jTable3.getColumnModel().getColumn(1).setPreferredWidth(65);
       
        jTable1.getColumnModel().getColumn(0).setMaxWidth(0);
       jTable1.getColumnModel().getColumn(0).setMinWidth(0);
       jTable1.getColumnModel().getColumn(0).setPreferredWidth(0);
       
        jTable1.getColumnModel().getColumn(1).setMaxWidth(100);
       jTable1.getColumnModel().getColumn(1).setMinWidth(55);
       jTable1.getColumnModel().getColumn(1).setPreferredWidth(65);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jRadOneFechaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadOneFechaActionPerformed
        if(jRadOneFecha.isSelected()){
            jDateChooser1.setEnabled(true);
            jDateChooser2.setEnabled(false);
        }
    }//GEN-LAST:event_jRadOneFechaActionPerformed

    private void jRadLapsoFechsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadLapsoFechsActionPerformed
        if(jRadLapsoFechs.isSelected()){
            jDateChooser1.setEnabled(true);
            jDateChooser2.setEnabled(true);
        }
    }//GEN-LAST:event_jRadLapsoFechsActionPerformed

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
            java.util.logging.Logger.getLogger(historProveedor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(historProveedor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(historProveedor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(historProveedor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new historProveedor(idProvd).setVisible(true);
            }
        });
    }

    public void iniFech(){
        String fech = "";
         jDateChooser1.setDate(fn.cargafecha());
        jDateChooser2.setDate(fn.cargafecha());
        fech = datC.getsumaFecha(jDateChooser1, -2);
        jDateChooser1.setDate(fn.StringDate(fn.volteaFecha(fech, 1)));
        jRadLapsoFechs.setSelected(true);
        jButton1.doClick();
    }
    
      /* ++++ CODIGO PARA ASIGNACION DE MERCANCIAS Y FLETES*/
    private void cargaComprasDiaAsign(String idPro,String fech1,String fech2,String status) {
        String[][] arre = controlInserts.consultPedidoAsign(idPro,fech1,fech2,status);
        if (arre.length > 0) {
            jLabLetreroCompras.setVisible(false);
            tabCompras = (DefaultTableModel) jTablePedidos.getModel();
            int filas = tabCompras.getRowCount(), filasPrec = tabCompras.getRowCount();
            if (filas > 0) {
                for (int i = 0; filas > i; i++) {
                    tabCompras.removeRow(0);
                }
            }
            tabCompras.setRowCount(arre.length);//CREAMOS EL NUMERO DE FILAS SEGUN LA MATRIZ DEVUELTA
            jTablePedidos.setModel(tabCompras);//agrego el modelo creado con el numero de filas devuelto

            for (int j = 0; j < arre.length; j++) {
                for (int k = 0; k < arre[0].length; k++) {
                    if (k == 0) {
                       consultDetailCompra(Integer.parseInt(arre[j][k]), j);//envia matriz con id_compra,id_proveedor,fila
                    }
                }
            }
           // ColorCelda c = new ColorCelda();
           // c.arrIntRowsIluminados = controlInserts.fnToArray(coloreA);
            //jTableCompras.setDefaultRenderer(Object.class, c);
           // coloreA.clear();

        } else {
            jLabLetreroCompras.setVisible(true);
            jLabLetreroCompras.setText("No hay compras del día");
        }
    }//Fin cargaComprasDia
    
        //*CODIGO PARA CREAR MATRIZ DINAMICA DE VISTA COMPRAS A PROVEEDOR*/
    public void consultDetailCompra(int opc, int fila) {//recibe Numdecompra,Numfila a insertar
        Connection cn = con2.conexion();
        String aux = "";
        int cantColumnas = 0, cantFilas = 0, temporal = 0, bandera = 0;
        String sql = "", sql2 = "";
sql = "SELECT * FROM detailpedidio WHERE id_PedidioD = '" + opc + "'";
sql2 ="SELECT pedidocliente.id_pedido,IF(pedidocliente.status = 0,'Pendiente','Pagado'),CONCAT(pedidocliente.fechaPedidio,' , ', IF(pedidocliente.horaPedido IS NULL,' -- ', pedidocliente.horaPedido) ),SUM(detailpedidio.cantidadCajas),"
                + "pedidocliente.totalPrecio\n"
                + "FROM\n"
                + "clientepedidos\n"
                + "INNER JOIN\n"
                + "pedidocliente \n"
                + "ON\n"
                + "pedidocliente.id_clienteP = clientepedidos.id_cliente AND pedidocliente.id_pedido = '"+opc+"'\n"
                + "INNER JOIN \n"
                + "detailpedidio\n"
                + "ON\n"
                + "detailpedidio.id_PedidioD = pedidocliente.id_pedido;";
        Statement st = null;
        ResultSet rs = null;
        try {
            st = cn.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {//es necesario el for para llenar dinamicamente la lista, ya que varia el numero de columnas de las tablas
                for (int x = 1; x <= rs.getMetaData().getColumnCount(); x++) {
                    if (x == 3) {//valor,fila,columna
                        jTablePedidos.setValueAt(rs.getInt(x + 1), fila, rs.getInt(x) + 2);//se le suma 1 por las columnas id,nombre de la jTable
                    }//System.out.print("["+x+"]"+" -> "+rs.getString(x));                   
                }//for
            }//while
            st = null;
            rs = null;
            st = cn.createStatement();
            rs = st.executeQuery(sql2);
            while (rs.next()) {
                jTablePedidos.setValueAt(rs.getInt(1), fila, 0);
                jTablePedidos.setValueAt(rs.getString(2), fila, 1);
                jTablePedidos.setValueAt(rs.getString(3), fila, 2); 
                jTablePedidos.setValueAt(rs.getString(4), fila, 10);
                jTablePedidos.setValueAt(rs.getString(5), fila, 11);
            }
        } catch (SQLException ex) {
            Logger.getLogger(historProveedor.class.getName()).log(Level.SEVERE, null, ex);
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
    }//regresaDatos
    
        /// CARGA LA SUMA DE CAJAS, IMPORTE TOTAL DE COMPRAS Y SUMA DE TIPO DE MERCANCIA EN EL DIA
    protected void cargaTotCompDayProveedor(String idPro,String fech1,String fech2,String status) {
        Connection cn = con2.conexion();
        int cantColumnas = 0, cantFilas = 0, temporal = 0, bandera = 0;
        String sql = "", sql2 = "",sql3 ="",sql4="",sql5="";
        sql = "SELECT productocal.codigo,\n"//Obtenemos la suma por tipo de prodcuto y precio
                + "SUM(detailpedidio.cantidadCajas) AS sumaType,\n"
                + "SUM(detailpedidio.cantidadCajas * detailpedidio.precioCaja) AS sumaTypePrec\n"
                + "FROM \n"
                + "detailpedidio\n"
                + "INNER JOIN \n"
                + "pedidocliente\n"
                + "ON\n"
                + "detailpedidio.id_PedidioD = pedidocliente.id_pedido \n"
                + "AND pedidocliente.id_clienteP = '" + idPro + "'\n"
                + "AND (pedidocliente.fechaPedidio >= '"+fech1+"' AND pedidocliente.fechaPedidio <= '"+fech2+"'  ) "
                + "INNER JOIN\n"
                + "productocal\n"
                + "ON\n"
                + "productocal.codigo = detailpedidio.codigoProdP\n"
                + "GROUP BY productocal.codigo;";

        sql2 = "SELECT SUM(pedidocliente.totalPrecio)\n" +
            "FROM pedidocliente\n" +
            "WHERE pedidocliente.id_clienteP = '"+idPro+"'\n" +
            "AND (pedidocliente.fechaPedidio >= '"+fech1+"' AND pedidocliente.fechaPedidio <= '"+fech2+"')\n" +
            ";";

//Obtenemos la sumatoria de lo que fue asignado y destinado a los pedidos        
 sql3 = "SELECT productocal.codigo,SUM(relcomprapedido.cantidadCajasRel) AS sumaType\n" +//,SUM(relcomprapedido.cantidadCajasRel * relcomprapedido.precioAjust) AS sumaTypePrec
            "FROM relcomprapedido\n" +
            "INNER JOIN pedidocliente \n" +
            "ON \n" +
            "relcomprapedido.id_pedidoCli = pedidocliente.id_pedido\n" +
            "AND pedidocliente.id_clienteP = '" + idPro + "'\n" +
            "AND (pedidocliente.fechaPedidio >=  '"+fech1+"'  AND pedidocliente.fechaPedidio <=  '"+fech2+"'   )\n" +
            "INNER JOIN productocal\n" +
            "ON\n" +
            "productocal.codigo = relcomprapedido.tipoMercanRel\n" +
            "GROUP BY productocal.codigo;";
 //Obtener la suma de numero de cajas asignadas, asi como la suma del importe de todas las cajas asignadas en el 
sql4 ="SELECT SUM(relcomprapedido.cantidadCajasRel) AS sumaType,SUM(relcomprapedido.cantidadCajasRel * relcomprapedido.precioAjust) AS sumaTypePrec\n" +
        "FROM relcomprapedido\n" +
        "INNER JOIN pedidocliente \n" +
        "ON relcomprapedido.id_pedidoCli = pedidocliente.id_pedido\n" +
        "AND pedidocliente.id_clienteP = '" + idPro + "' \n" +
        "AND (pedidocliente.fechaPedidio >= '"+fech1+"' AND pedidocliente.fechaPedidio <= '"+fech2+"');";
sql5 ="SELECT SUM(detailpedidio.cantidadCajas) AS numCaja\n" +
        "FROM\n" +
        "pedidocliente\n" +
        "INNER JOIN\n" +
        "detailpedidio\n" +
        "ON\n" +
        "detailpedidio.id_PedidioD = pedidocliente.id_pedido\n" +
        "AND pedidocliente.id_clienteP = '"+idPro+"'\n" +
        "AND (pedidocliente.fechaPedidio >= '"+fech1+"' AND pedidocliente.fechaPedidio <= '"+fech2+"');";
 Statement st = null;
        ResultSet rs = null;
        try {
            st = cn.createStatement();
            rs = st.executeQuery(sql);
            // System.out.print("Filas: "+cantFilas+"\tColumnas: "+cantColumnas+"\n");
            rs.beforeFirst();
            while (rs.next()) {//es necesario el for para llenar dinamicamente la lista, ya que varia el numero de columnas de las tablas
                for (int x = 1; x <= rs.getMetaData().getColumnCount(); x++) {
                    if (x == 1) {
                        jTabsumtotales.setValueAt(rs.getInt(x + 1), 0, rs.getInt(x) - 1);//se le suma 1 por las columnas id,nombre de la jTable
                        jTabsumtotales.setValueAt(rs.getString(x + 2), 1, rs.getInt(x) - 1);
                    }
                }//for
            }//while

            st = null;
            rs = null;
            st = cn.createStatement();
            rs = st.executeQuery(sql2);
            rs.beforeFirst();
            while (rs.next()) {
                jTabsumtotales.setValueAt(rs.getString(1), 0, 8);
              //  jTabsumtotales.setValueAt(rs.getString(2), 0, 7);
            }
            st = null;
            rs = null;
            st = cn.createStatement();
            rs = st.executeQuery(sql5);
            rs.beforeFirst();
            while (rs.next()) {
                jTabsumtotales.setValueAt(rs.getString(1), 0, 7);
              //  jTabsumtotales.setValueAt(rs.getString(2), 0, 7);
            }
            
            
              st = null;
            rs = null;
            st = cn.createStatement();
            rs = st.executeQuery(sql3);
            rs.beforeFirst();
           while (rs.next()) {//es necesario el for para llenar dinamicamente la lista, ya que varia el numero de columnas de las tablas
                for (int x = 1; x <= rs.getMetaData().getColumnCount(); x++) {
                    if (x == 1) {
                        jTabsumtotales.setValueAt(rs.getInt(x + 1), 1, rs.getInt(x) - 1);//se le suma 1 por las columnas id,nombre de la jTable
                    }
                }//for
            }//while
            
            st = null;
            rs = null;
            st = cn.createStatement();
            rs = st.executeQuery(sql4);
            rs.beforeFirst();
            while (rs.next()) {
                jTabsumtotales.setValueAt(rs.getString(1), 1, 7);
                jTabsumtotales.setValueAt(rs.getString(2), 1, 8);
            }

        } catch (SQLException ex) {
            Logger.getLogger(historProveedor.class.getName()).log(Level.SEVERE, null, ex);
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
    
         /*++++}}CODIGO PARA ASIGNACION DE MERCANCIAS Y FLETES*/
    private void cargaPrestDet(String idPro,String fech1,String fech2,String status) {
        String[][] arre = controlInserts.consultVentPisofilters(idPro,fech1,fech2,status);//OBTENEMOS ID DE PRESTAMOS
        if (arre.length > 0) {
          //  jLabLetreroCompras.setVisible(false);
            dtmPrest = (DefaultTableModel) jTabPrestamos.getModel();
            int filas = dtmPrest.getRowCount(), filasPrec = dtmPrest.getRowCount();
            if (filas > 0) {
                for (int i = 0; filas > i; i++) {
                    dtmPrest.removeRow(0);
                }
            }
            dtmPrest.setRowCount(arre.length);//CREAMOS EL NUMERO DE FILAS SEGUN LA MATRIZ DEVUELTA

            jTabPrestamos.setModel(dtmPrest);//agrego el modelo creado con el numero de filas devuelto

            for (int j = 0; j < arre.length; j++) {
                for (int k = 0; k < arre[0].length; k++) {
                    if (k == 0) {
                        consultDetailPrest(Integer.parseInt(arre[j][k]), j);//envia matriz con id_compra,id_proveedor,fila
                    }
                }
            }
           // ColorCelda c = new ColorCelda();
           // c.arrIntRowsIluminados = controlInserts.fnToArray(coloreA);
            //jTableCompras.setDefaultRenderer(Object.class, c);
           // coloreA.clear();

        } else {
            jLabLetreroCompras.setVisible(true);
            jLabLetreroCompras.setText("No hay compras del día");
//           JOptionPane.showMessageDialog(null, "No hay compras del dia");
        }
    }//Fin cargaComprasDia
    
            //*CODIGO PARA CREAR MATRIZ DINAMICA DE VISTA COMPRAS A PROVEEDOR*/
    public void consultDetailPrest(int opc, int fila) {//recibe Numdecompra,Numfila a insertar
        Connection cn = con2.conexion();
        String aux = "";
        int cantColumnas = 0, cantFilas = 0, temporal = 0, bandera = 0;
        String sql = "", sql2 = "";
        sql = "SELECT * FROM detailventapiso WHERE num_notaV = '" + opc + "'";
        sql2 = "SELECT notaventapiso.id_venta,CONCAT(notaventapiso.fechVenta,', ',IF(notaventapiso.horaVentaPiso IS NULL,'--',notaventapiso.horaVentaPiso)),"
                + "SUM(detailventapiso.cantidadV*detailventapiso.precioPV)\n"
                + "FROM\n"
                + "clientepedidos\n"
                + "INNER JOIN\n"
                + "notaventapiso \n"
                + "ON\n"
                + "notaventapiso.id_clientePiso = clientepedidos.id_cliente AND notaventapiso.id_venta = '" + opc + "'\n"
                + "INNER JOIN \n"
                + "detailventapiso\n"
                + "ON\n"
                + "detailventapiso.num_notaV = notaventapiso.id_venta;";
        Statement st = null;
        ResultSet rs = null;
        try {
            st = cn.createStatement();
            rs = st.executeQuery(sql);
           // System.out.print("Filas: credi "+cantFilas+"\tColumnas: "+cantColumnas+"\n");
            while (rs.next()) {//es necesario el for para llenar dinamicamente la lista, ya que varia el numero de columnas de las tablas
                for (int x = 1; x <= rs.getMetaData().getColumnCount(); x++) {
                    if (x == 3) {//valor,fila,columna
                        jTabPrestamos.setValueAt(rs.getInt(x + 1), fila, rs.getInt(x) +1 );//se le suma 1 por las columnas id,nombre de la jTable
                    //    if (controlInserts.validaIsMasDeUnProductoPrest(opc, rs.getInt(x)) > 1) {//si existe mas de una vez ese producto en la compra a mayorista, => hacemos la sumatoria y reescribimos eso
                            //   System.out.println("\tExiste+de 1 vex: "+rs.getInt(x));
                   //         jTabPrestamos.setValueAt(controlInserts.sumaIsMasDeUnProductoPrest(opc, rs.getInt(x)), fila, rs.getInt(x) -6);
                     //   }
                    }//System.out.print("["+x+"]"+" -> "+rs.getString(x));                   
                }//for
            }//while

            st = null;
            rs = null;

            st = cn.createStatement();
            rs = st.executeQuery(sql2);
            while (rs.next()) {
                jTabPrestamos.setValueAt(rs.getInt(1), fila, 0);

                jTabPrestamos.setValueAt(rs.getString(2), fila, 1);
                jTabPrestamos.setValueAt(rs.getString(3), fila, 13);
             
            }
        } catch (SQLException ex) {
            Logger.getLogger(historProveedor.class.getName()).log(Level.SEVERE, null, ex);
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
    }//regresaDatos
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private com.toedter.calendar.JDateChooser jDateChooser2;
    private javax.swing.JLabel jLabLetreroCompras;
    public javax.swing.JLabel jLabNombProv;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    public javax.swing.JLabel jLabnumProv;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JRadioButton jRadLapsoFechs;
    private javax.swing.JRadioButton jRadOneFecha;
    private javax.swing.JScrollPane jScrollPanCompras;
    private javax.swing.JScrollPane jScrollPanTotCompras;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTabPrestamos;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable3;
    private javax.swing.JTable jTablePedidos;
    private javax.swing.JTable jTabsumtotales;
    // End of variables declaration//GEN-END:variables
}
