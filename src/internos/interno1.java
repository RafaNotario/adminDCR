package internos;

import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.JOptionPane;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.event.KeyEvent;
import Frame.AgregaFlete;
import Frame.FormacionPedido;
import Frame.VentaPiso;
import Frame.detalleVP;

import Frame.detailCompra;
import Frame.detailPedido;
import Frame.detallePrestamo;
import Frame.historCliente;
import Frame.historProveedor;
import java.util.Date;
import javax.swing.JLabel;
import javax.swing.JPanel;

import internos.tickets.print.Funciones;
import internos.tickets.print.Reportes;

import controllers.altadeclientes.controladorCFP;
import conexiones.db.ConexionDBOriginal;
import controllers.altadeclientes.datesControl;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.io.File;
import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.SwingConstants;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import renderTable.TModel;
import renderTable.ColorCelda;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import renderTable.colorCeldaComplete;

public class interno1 extends javax.swing.JFrame {

    Funciones fn = new Funciones();
    controladorCFP controlInserts = new controladorCFP();
    ConexionDBOriginal con2 = new ConexionDBOriginal();
    Reportes rP = new Reportes();
    datesControl datCtrl = new datesControl();

    //** variables globales     ->netflix oss microservices
    String atribAltaCli = "";//variable para asignar el nombre de la tabla llenar de la base de datos
    String buscarTurn ="0"; //BUSCAtURNO EN  REPORTES
    
    List<String> conten = new ArrayList<String>();//lista para guardar el id de cada cleinte en crea pedido y en ventas piso
    List<String> idProducts = new ArrayList<String>();//lista para id de produvtod
    List<String> idProductsVent = new ArrayList<String>();//lista para id de productos venta
    List<String> idCajeros = new ArrayList<String>();//lista para id de cajeros reportes
            
    List<String> idProdPrest = new ArrayList<String>();//para productos prestamo
    List<String> idProoved = new ArrayList<String>();//para id_proveedores
    List<String> importes = new ArrayList<String>();//para importes de prestamo

    List<String> contenFletes = new ArrayList<String>();//para id_Fletero

    List<String> iDclisVentaPiso = new ArrayList<String>();//cleinte en crea  ventas piso

    ArrayList<Integer> coloreA = new ArrayList<Integer>();
    ArrayList<Integer> coloreB = new ArrayList<Integer>();
    ArrayList<Integer> coloreF = new ArrayList<Integer>();
    ArrayList<Integer> colorePD = new ArrayList<Integer>();
    ArrayList<Integer> coloreComplete = new ArrayList<Integer>();
         List<String> contenRubGastos = new ArrayList<String>();//para gastos del dia
    DefaultTableModel dtm,//obtener modelo en tabla CreaPedido clientes
            dtmPrec,
            tabCompras,//tabla diamica de compras del dia
            dtmAux,//tabla de compras a proveedor real
            dtmPedidosDetDia;

    String[] cab = {"ID PEDIDO", "FECHA", "STATUS", "ID CLIENTE", "NOMBRE", "CANTIDAD", "COSTO", "NOTA"};
    String[] cabPrest = {"NO. CREDITO", "FECHA", "STATUS", "ID PROVEEDOR", "NOMBRE", "TOTAL", "NOTA"};
    String[] cabCompra = {"NO. COMPRA", "FECHA", "STATUS", "ID PROVEEDOR", "NOMBRE", "TOTAL", "NOTA"};
    String[] cabvENTAp = {"NO. VENTA", "FECHA", "STATUS", "ID CLIENTE", "NOMBRE", "TOTAL", "NOTA"};
    String[] cabFilterFlete = {"FOLIO", "FLETERO", "FECHA", "CHOFER", "UNIDAD", "COSTO", "STATUS", "NOTA"};
    String[] cabEdoPed = {"FOLIO", "Compras Asignadas", "Pedidos Destinados", "Total de carga"};

    //cabeceras de tables del dia
    String[] cabPaysDay = {"idR","#Operacion", "Nombre", "Fecha", "Monto", "Nota"};
    String[] cabSalidDay = {"id", "Fecha", "Monto", "Nota", "Nombre","Concepto"};
    String[] cabMayAsignados = {"IdR", "Mayorista", "idC", "Fech"};
    String[] cabMayView = {"#Compra", "id", "Tipo", "Cantidad", "Costo", "Importe"};
    String[] cabMayViewcHECK = {"#Compra", "id", "idtype", "Tipo", "Cantidad", "Costo", "Importe", "Select", "A Asignar", "Asignados", "Disponibles"};
    String[] cabDetcompra = {"#IdRel", "Flete", "#Pedido", "Cliente", "Tipo", "Cantidad","$Unitario","Importe$"};
    String[] cabDetFlet = {"#IdRel", "Id_Pedido", "Id_Compra", "Proveedor", "Tipo", "Cantidad","$Unitario","Importe$"};
    String[] cabAreasPays = {"# Ticket", "Hora", "Concepto", "Nombre", "Monto"};
    
    String[][] matLlenaPed = null;
    String[] inTurno = null;
    String[] semanaAct = null;
    
    AgregaFlete aF;
    VentaPiso vP;
    detailPedido dP;
    detallePrestamo dPresta;
    FormacionPedido fP;
    detailCompra dComp;
    detalleVP ventDP;
    historProveedor hProv;
    historCliente hCliente;

    public interno1() {//https://opengroup.org/togaf  scrumstudy.com JAVAEE JAKARTAEE-the future of javaee, MICROPROFILE-OPTIMIZING ENTERPRISE JAVA
        initComponents();//apache tomcat, glassfish, JBoss, WebSphere IBM
        this.setLocationRelativeTo(null);//spring.io, spring-boot
        jLabTurno.setText("12");
        jRad1Activo.setSelected(true);//CF Pivotal Cloud
//panel alta de clientes
        fn.setBorder(jPanAdminist, "Formulario Clientes");
        fn.setBorder(jPanVistaAlta, "Vista de Clientes");
        fn.setBorder(jPanCompraProoved, "Compra a proveedor");
        jTextNombre.requestFocus();
        jDateChFechaAlta.setDate(cargafecha());
        //mostrarTabla();
        jTableAltasCli.setFont(new java.awt.Font("Tahoma", 0, 14));
//panel compra proovedor
        jPanClientOption.setVisible(false);
        jPanSubastaOption.setVisible(false);
        AutoCompleteDecorator.decorate(jCElijaProovedor);
        AutoCompleteDecorator.decorate(jComBPrestamosProv);
//panel crea pedidos
        AutoCompleteDecorator.decorate(jCombPedidoClient);
        jRadioCreaPedido.setSelected(true);
        jPanCreaPedido.setVisible(true);
        jPanConsulPed.setVisible(false);
//panel PROVEEDORES
        jPanPrestamoProovedor.setVisible(false);
        jPanCompraProoved.setVisible(false);
        jPanBusquedaPrest.setVisible(false);
        jDatFechaPrest.setDate(cargafecha());
        jCheckBox1.setSelected(false);//checkBox para cargar solo a proveedores mayoristas al jItem
        llenacomboProducts();
        llenacomboProovedores();
        //AgregarMayoreo.setVisible(false);
//panel ventas de mostrador
        jButton10.setMnemonic(KeyEvent.VK_F12);
        //jBCancelVentaP.setMnemonic(KeyEvent.VK_F10);
        jPaNVentaPiso.setVisible(false);
        jPanBusqVentasPiso.setVisible(false);
        
//pane fletes;
        jLabLetreroFletes.setVisible(false);
        jPanCreaFletes.setVisible(false);
        jPanHistorFletes.setVisible(false);
//panel asignacion
        jDCAsignacionDia.setDate(cargafecha());
        jButton34.setEnabled(false);
//panel reportes
        llenacombogetcuentaGasto();
        jComboBox1.setVisible(false);
        AutoCompleteDecorator.decorate(jComboBox1);
//panel pagos vista
        jDFechPays.setDate(cargafecha());
//panel reportes
        llenacombCajeros();
        
        jCElijaProovedor.getEditor().getEditorComponent().addKeyListener(
                new KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                int var = e.getKeyCode();
                if (var == KeyEvent.VK_ENTER) {
                    siSubastaProv();//buscara si es subasta, pedira camioneta, sino verificara si hay prestamo
                }
                if (var == KeyEvent.VK_F5) {
                    // jButton15.doClick();
                }
            }
        });
    }//@end constructor

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnGAltaClientes = new javax.swing.ButtonGroup();
        jPopPedidosDia = new javax.swing.JPopupMenu();
        jMnRealPay = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        buttonGProveedores = new javax.swing.ButtonGroup();
        jPopupPrestaProov = new javax.swing.JPopupMenu();
        jMIPPVer = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        butnGPedidos = new javax.swing.ButtonGroup();
        butnGProved = new javax.swing.ButtonGroup();
        jPopCompraProveedor = new javax.swing.JPopupMenu();
        jMenPago = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JPopupMenu.Separator();
        AgregarMayoreo = new javax.swing.JMenuItem();
        jPopFILTROCOMPRAS = new javax.swing.JPopupMenu();
        jMPAYFILTRO = new javax.swing.JMenuItem();
        btnGVentasPiso = new javax.swing.ButtonGroup();
        jPopVentaPisoBusq = new javax.swing.JPopupMenu();
        jMnDetailVentaP = new javax.swing.JMenuItem();
        jSeparator14 = new javax.swing.JPopupMenu.Separator();
        jMnPayVentaP = new javax.swing.JMenuItem();
        btnGFletesCrea = new javax.swing.ButtonGroup();
        btnGFletesPane = new javax.swing.ButtonGroup();
        jPMDetailFormaPedido = new javax.swing.JPopupMenu();
        jMItDetailVer = new javax.swing.JMenuItem();
        jSeparator16 = new javax.swing.JPopupMenu.Separator();
        jPMPrestaProvPays = new javax.swing.JPopupMenu();
        jMIPaysPresta = new javax.swing.JMenuItem();
        jPpMnPagoFletes = new javax.swing.JPopupMenu();
        jMItPayFlete = new javax.swing.JMenuItem();
        jPopMFiltrosBusqFletes = new javax.swing.JPopupMenu();
        jMitPayfilterFletes = new javax.swing.JMenuItem();
        jPMACompras = new javax.swing.JPopupMenu();
        jMI_ElimASIGNA = new javax.swing.JMenuItem();
        jPMneliminaDetailMayorista = new javax.swing.JPopupMenu();
        delDetailcompra = new javax.swing.JMenuItem();
        jFramElijeAsignCompras = new javax.swing.JFrame();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTabAsigaDinamicoCompra = new javax.swing.JTable();
        jButton16 = new javax.swing.JButton();
        jLabel24 = new javax.swing.JLabel();
        jLabNumcompra = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabNumcompra1 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabNumcompra2 = new javax.swing.JLabel();
        jPopupMenActualizaCompra = new javax.swing.JPopupMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        butGrpFleterFilters = new javax.swing.ButtonGroup();
        jDialDetailCompraProov = new javax.swing.JDialog();
        jScrollPane10 = new javax.swing.JScrollPane();
        jTabDetProvView = new javax.swing.JTable();
        jLabel129 = new javax.swing.JLabel();
        jLabNumAsogna = new javax.swing.JLabel();
        jLabel131 = new javax.swing.JLabel();
        jLabFaltant = new javax.swing.JLabel();
        jLabel130 = new javax.swing.JLabel();
        jLabel108 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel107 = new javax.swing.JLabel();
        jLabId = new javax.swing.JLabel();
        jLabel109 = new javax.swing.JLabel();
        jLabContad = new javax.swing.JLabel();
        jLabel113 = new javax.swing.JLabel();
        jLabNomProov = new javax.swing.JLabel();
        jPopMnDetailCompAsign = new javax.swing.JPopupMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jDialDetailFlete1 = new javax.swing.JDialog();
        jLabel140 = new javax.swing.JLabel();
        jLabel141 = new javax.swing.JLabel();
        jScrollPane11 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jLabel142 = new javax.swing.JLabel();
        jLabel143 = new javax.swing.JLabel();
        jLabel144 = new javax.swing.JLabel();
        jLabel145 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel135 = new javax.swing.JLabel();
        jLabel136 = new javax.swing.JLabel();
        jLabel138 = new javax.swing.JLabel();
        jLabel139 = new javax.swing.JLabel();
        jLabel146 = new javax.swing.JLabel();
        jLabel147 = new javax.swing.JLabel();
        jLabel137 = new javax.swing.JLabel();
        jLabel124 = new javax.swing.JLabel();
        jPpMnDetFletes = new javax.swing.JPopupMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jDialAltaGastos = new javax.swing.JDialog();
        jPanel16 = new javax.swing.JPanel();
        jLabel127 = new javax.swing.JLabel();
        jLabel128 = new javax.swing.JLabel();
        jLabel133 = new javax.swing.JLabel();
        jLabel148 = new javax.swing.JLabel();
        jLabel150 = new javax.swing.JLabel();
        jLabel151 = new javax.swing.JLabel();
        txtConcept = new javax.swing.JTextField();
        txtsolict = new javax.swing.JTextField();
        txtObservs = new javax.swing.JTextField();
        txtMontoGasto = new javax.swing.JTextField();
        jCombBTypeRubros = new javax.swing.JComboBox<>();
        jComboBox1 = new javax.swing.JComboBox<>();
        jButton39 = new javax.swing.JButton();
        jButton43 = new javax.swing.JButton();
        jDialCancelaciones = new javax.swing.JDialog();
        jLabel152 = new javax.swing.JLabel();
        txtCancelTick = new javax.swing.JTextField();
        jButton32 = new javax.swing.JButton();
        jButton33 = new javax.swing.JButton();
        jLabel154 = new javax.swing.JLabel();
        jLabIdCancel = new javax.swing.JLabel();
        jLabParam2 = new javax.swing.JLabel();
        jDiaViewSobrinas = new javax.swing.JDialog();
        jLabel160 = new javax.swing.JLabel();
        jScrollPane24 = new javax.swing.JScrollPane();
        jTabSobrinasDays = new javax.swing.JTable();
        jLabRcontFilsob = new javax.swing.JLabel();
        jButton35 = new javax.swing.JButton();
        jLabel170 = new javax.swing.JLabel();
        jScrollPane36 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jDialCalendarMantenim = new javax.swing.JDialog();
        jCalendar1 = new com.toedter.calendar.JCalendar();
        jButton42 = new javax.swing.JButton();
        jPanCabezera = new javax.swing.JPanel();
        jLabel52 = new javax.swing.JLabel();
        jTextField17 = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jLabel39 = new javax.swing.JLabel();
        jLabTurno = new javax.swing.JLabel();
        jButton25 = new javax.swing.JButton();
        jLabIdView = new javax.swing.JLabel();
        jButton36 = new javax.swing.JButton();
        paneAltas = new javax.swing.JTabbedPane();
        jPanAltas = new javax.swing.JPanel();
        jPanAdminist = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jTextLocalidad = new javax.swing.JTextField();
        jTextApellidos = new javax.swing.JTextField();
        jButAltasElimina = new javax.swing.JButton();
        jTexTelefono = new javax.swing.JTextField();
        jButaltasGuardar = new javax.swing.JButton();
        jButAltasActualiza = new javax.swing.JButton();
        jLabel17 = new javax.swing.JLabel();
        jRad1Activo = new javax.swing.JRadioButton();
        jRadInactivo = new javax.swing.JRadioButton();
        jLabel19 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabelRFCAlta = new javax.swing.JLabel();
        txtQuintoAltas = new javax.swing.JTextField();
        jTextNombre = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        jCBTypeProv = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        jComboAltas = new javax.swing.JComboBox<>();
        jPanVistaAlta = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTableAltasCli = new javax.swing.JTable();
        txtBusqAltas = new javax.swing.JTextField();
        jDateChFechaAlta = new com.toedter.calendar.JDateChooser();
        jLabel16 = new javax.swing.JLabel();
        txtIdParam = new javax.swing.JTextField();
        jPPedidosHist = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jRadioCreaPedido = new javax.swing.JRadioButton();
        jRadioConsulPedido = new javax.swing.JRadioButton();
        jLayeredPanePedidos = new javax.swing.JLayeredPane();
        jPanCreaPedido = new javax.swing.JPanel();
        jCombPedidoClient = new javax.swing.JComboBox<>();
        jLabel46 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jCombProdPedidos = new javax.swing.JComboBox<>();
        jLabel44 = new javax.swing.JLabel();
        txtCantCreaPedido = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        jButton9 = new javax.swing.JButton();
        txtNotePedidoCli = new javax.swing.JTextField();
        jScrollPane9 = new javax.swing.JScrollPane();
        jTableCreaPedidos = new javax.swing.JTable();
        jDateCHPedido = new com.toedter.calendar.JDateChooser();
        jLabel58 = new javax.swing.JLabel();
        jLabel59 = new javax.swing.JLabel();
        jButGuardaPedidodia = new javax.swing.JButton();
        jScrollPane15 = new javax.swing.JScrollPane();
        jTabVistaPedidosDetDia = new javax.swing.JTable();
        jScrollPane35 = new javax.swing.JScrollPane();
        jTabSumTotalPedido = new javax.swing.JTable();
        jLabel31 = new javax.swing.JLabel();
        jButton15 = new javax.swing.JButton();
        jButton22 = new javax.swing.JButton();
        jSeparator6 = new javax.swing.JSeparator();
        jPanConsulPed = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jComPedBusqCli = new javax.swing.JComboBox<>();
        jLabel60 = new javax.swing.JLabel();
        jCombOpcBusqPedido = new javax.swing.JComboBox<>();
        jLabel61 = new javax.swing.JLabel();
        jDateChoB1Cli = new com.toedter.calendar.JDateChooser();
        jLabel62 = new javax.swing.JLabel();
        jDate2BusqCli = new com.toedter.calendar.JDateChooser();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTablefiltrosBusq = new javax.swing.JTable();
        jLabel63 = new javax.swing.JLabel();
        jSeparator7 = new javax.swing.JSeparator();
        jButton3 = new javax.swing.JButton();
        jLCountHistorP = new javax.swing.JLabel();
        jLabel157 = new javax.swing.JLabel();
        jLabtotaldineroHistorPEd = new javax.swing.JLabel();
        proveedorJP = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jRadBCompraProve = new javax.swing.JRadioButton();
        jRadBPrestamoProv = new javax.swing.JRadioButton();
        jRadBConsultaProv = new javax.swing.JRadioButton();
        jLayeredPane1 = new javax.swing.JLayeredPane();
        jPanCompraProoved = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        jCElijaProovedor = new javax.swing.JComboBox<>();
        jCombProductProv = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtPrecCompraProv = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTabVistaComprasDia = new javax.swing.JTable();
        jLabel41 = new javax.swing.JLabel();
        txtCantidadCompra = new javax.swing.JTextField();
        jLayeredPane3 = new javax.swing.JLayeredPane();
        jPanSubastaOption = new javax.swing.JPanel();
        jLabel57 = new javax.swing.JLabel();
        txtCamSubastaCompra = new javax.swing.JTextField();
        jPanClientOption = new javax.swing.JPanel();
        jLabAdeudaProoved = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jDateFechCompraProv = new com.toedter.calendar.JDateChooser();
        jLabel40 = new javax.swing.JLabel();
        txtNotaCompra = new javax.swing.JTextField();
        jButton13 = new javax.swing.JButton();
        txtImportComp = new javax.swing.JTextField();
        jLabel48 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jCheckBox1 = new javax.swing.JCheckBox();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTabDetallecompraAll = new javax.swing.JTable();
        jScrollPane34 = new javax.swing.JScrollPane();
        jTabSumTotales = new javax.swing.JTable();
        jLabel11 = new javax.swing.JLabel();
        jCheckbpAGADO = new javax.swing.JCheckBox();
        jSeparator3 = new javax.swing.JSeparator();
        jButton19 = new javax.swing.JButton();
        jLabel51 = new javax.swing.JLabel();
        jLabel66 = new javax.swing.JLabel();
        jButton21 = new javax.swing.JButton();
        jPanBusquedaPrest = new javax.swing.JPanel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanInternoBusquedaPrest = new javax.swing.JPanel();
        jDaTFechPrest1 = new com.toedter.calendar.JDateChooser();
        jLabel42 = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        jCombBProvBusqPrest = new javax.swing.JComboBox<>();
        jLabel47 = new javax.swing.JLabel();
        jComBusPrestamo = new javax.swing.JComboBox<>();
        jLabel69 = new javax.swing.JLabel();
        jLabel70 = new javax.swing.JLabel();
        jDaTFechPrest2 = new com.toedter.calendar.JDateChooser();
        jButton5 = new javax.swing.JButton();
        jScrollPane14 = new javax.swing.JScrollPane();
        jTabBusqPrestProv = new javax.swing.JTable();
        jLabel99 = new javax.swing.JLabel();
        jLabel155 = new javax.swing.JLabel();
        jLabel158 = new javax.swing.JLabel();
        jLabel169 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jComBusCompra = new javax.swing.JComboBox<>();
        jCombBProvBusqCompra = new javax.swing.JComboBox<>();
        jDaTFechComp1 = new com.toedter.calendar.JDateChooser();
        jLabel72 = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        jLabel73 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        jSeparator9 = new javax.swing.JSeparator();
        jLabel74 = new javax.swing.JLabel();
        jDaTFechCompraProv2 = new com.toedter.calendar.JDateChooser();
        jButton14 = new javax.swing.JButton();
        jScrollPane16 = new javax.swing.JScrollPane();
        jTabBusqCompraProv1 = new javax.swing.JTable();
        jLabel171 = new javax.swing.JLabel();
        jLabel172 = new javax.swing.JLabel();
        jLabel173 = new javax.swing.JLabel();
        jPanPrestamoProovedor = new javax.swing.JPanel();
        jLabel33 = new javax.swing.JLabel();
        jComBPrestamosProv = new javax.swing.JComboBox<>();
        jLabel34 = new javax.swing.JLabel();
        jComBProveedor = new javax.swing.JComboBox<>();
        txtCantPres = new javax.swing.JTextField();
        jLabBNumerador = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        txtPrecProov = new javax.swing.JTextField();
        jButton7 = new javax.swing.JButton();
        jLabel38 = new javax.swing.JLabel();
        jDatFechaPrest = new com.toedter.calendar.JDateChooser();
        jScrollPane8 = new javax.swing.JScrollPane();
        textANotaPrestProv = new javax.swing.JTextArea();
        jLabel36 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jLabel64 = new javax.swing.JLabel();
        jLabel65 = new javax.swing.JLabel();
        txtImportPres = new javax.swing.JTextField();
        jLabel67 = new javax.swing.JLabel();
        jScrollPane13 = new javax.swing.JScrollPane();
        jTabVistaPresta = new javax.swing.JTable();
        jLabel68 = new javax.swing.JLabel();
        jSeparator8 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTabdetPrestamoProv = new javax.swing.JTable();
        jLabel71 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jPFletes = new javax.swing.JPanel();
        jRdCreaFletes = new javax.swing.JRadioButton();
        jRHistorFletes = new javax.swing.JRadioButton();
        jLayerFletes = new javax.swing.JLayeredPane();
        jPanCreaFletes = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jCBAltasFletes = new javax.swing.JComboBox<>();
        jLabel86 = new javax.swing.JLabel();
        jDFechCreaFlete = new com.toedter.calendar.JDateChooser();
        jPanAdminist1 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jLabel87 = new javax.swing.JLabel();
        txtCostoFlete = new javax.swing.JTextField();
        txtUnidFlete = new javax.swing.JTextField();
        jButAltasElimina1 = new javax.swing.JButton();
        txtNotaFlete = new javax.swing.JTextField();
        jButFleteGuardar = new javax.swing.JButton();
        jButAltasActualiza1 = new javax.swing.JButton();
        jLabel88 = new javax.swing.JLabel();
        jLabel90 = new javax.swing.JLabel();
        txtChoferFlete = new javax.swing.JTextField();
        txtFolioFlete = new javax.swing.JTextField();
        jLabel89 = new javax.swing.JLabel();
        jLabLetreroFletes = new javax.swing.JLabel();
        jLabel92 = new javax.swing.JLabel();
        jRPagadopFlete = new javax.swing.JRadioButton();
        jRPendFlete = new javax.swing.JRadioButton();
        jLabel91 = new javax.swing.JLabel();
        jLabel101 = new javax.swing.JLabel();
        txtCargaFlet = new javax.swing.JTextField();
        jLabel102 = new javax.swing.JLabel();
        jScrollPane19 = new javax.swing.JScrollPane();
        jTabFletesDia = new javax.swing.JTable();
        jLabel93 = new javax.swing.JLabel();
        jButton20 = new javax.swing.JButton();
        jLabContaFletes = new javax.swing.JLabel();
        jPanHistorFletes = new javax.swing.JPanel();
        jScrollPane20 = new javax.swing.JScrollPane();
        jTablefiltrosBusqflete = new javax.swing.JTable();
        jLabel94 = new javax.swing.JLabel();
        jSeparator15 = new javax.swing.JSeparator();
        jDCFol1 = new com.toedter.calendar.JDateChooser();
        jLabel95 = new javax.swing.JLabel();
        jLabel96 = new javax.swing.JLabel();
        jDCFol2 = new com.toedter.calendar.JDateChooser();
        jButton12 = new javax.swing.JButton();
        jLabel97 = new javax.swing.JLabel();
        jCfleteroOpc = new javax.swing.JComboBox<>();
        jCombOpcBusqFletes = new javax.swing.JComboBox<>();
        jLabel98 = new javax.swing.JLabel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jRadioButton3 = new javax.swing.JRadioButton();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel103 = new javax.swing.JLabel();
        jPanVentasPiso = new javax.swing.JPanel();
        jRadCreaVentaPiso = new javax.swing.JRadioButton();
        jRadBusqVentaPiso = new javax.swing.JRadioButton();
        jLayVentasPiso = new javax.swing.JLayeredPane();
        jPaNVentaPiso = new javax.swing.JPanel();
        jLabel53 = new javax.swing.JLabel();
        jCombProdVentaP = new javax.swing.JComboBox<>();
        jLabel54 = new javax.swing.JLabel();
        txtCantVentaPiso = new javax.swing.JTextField();
        jLabel55 = new javax.swing.JLabel();
        txtNotaVentP = new javax.swing.JTextField();
        jButton8 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jLabel75 = new javax.swing.JLabel();
        jCombCliVentaP = new javax.swing.JComboBox<>();
        jLabel76 = new javax.swing.JLabel();
        txtPrecProdVentaP = new javax.swing.JTextField();
        jLabel77 = new javax.swing.JLabel();
        txtImportVentaP = new javax.swing.JTextField();
        jLabel78 = new javax.swing.JLabel();
        jLabel79 = new javax.swing.JLabel();
        jSeparator11 = new javax.swing.JSeparator();
        jScrollPane17 = new javax.swing.JScrollPane();
        jTVistaVentaPisoDia = new javax.swing.JTable();
        jScrollPane12 = new javax.swing.JScrollPane();
        jTabDescVentaP = new javax.swing.JTable();
        jLabel56 = new javax.swing.JLabel();
        txtTotalVentaPiso = new javax.swing.JTextField();
        jLabel80 = new javax.swing.JLabel();
        jSeparator12 = new javax.swing.JSeparator();
        jLabel81 = new javax.swing.JLabel();
        jDFVentaPiso = new com.toedter.calendar.JDateChooser();
        jPanBusqVentasPiso = new javax.swing.JPanel();
        jLabel82 = new javax.swing.JLabel();
        jCombOpcBusqVenta = new javax.swing.JComboBox<>();
        jLabel83 = new javax.swing.JLabel();
        jCCliVentaPiso = new javax.swing.JComboBox<>();
        jLabel84 = new javax.swing.JLabel();
        jDateChoBVent1 = new com.toedter.calendar.JDateChooser();
        jLabel85 = new javax.swing.JLabel();
        jDatebusqVenta2 = new com.toedter.calendar.JDateChooser();
        jLabel20 = new javax.swing.JLabel();
        jScrollPane18 = new javax.swing.JScrollPane();
        jTablefiltrosBusqVent = new javax.swing.JTable();
        jSeparator13 = new javax.swing.JSeparator();
        jButton11 = new javax.swing.JButton();
        jLabel174 = new javax.swing.JLabel();
        jLabel175 = new javax.swing.JLabel();
        jLabel176 = new javax.swing.JLabel();
        jPanAsignac = new javax.swing.JPanel();
        jScrollPane23 = new javax.swing.JScrollPane();
        jTabVistaPedidosDia1 = new javax.swing.JTable();
        jLabel110 = new javax.swing.JLabel();
        jLabel111 = new javax.swing.JLabel();
        jLabel112 = new javax.swing.JLabel();
        jScrollPane25 = new javax.swing.JScrollPane();
        jTabVistaComprasDia3 = new javax.swing.JTable();
        jButton17 = new javax.swing.JButton();
        jScrollPane26 = new javax.swing.JScrollPane();
        jTabFletesDia1 = new javax.swing.JTable();
        jDCAsignacionDia = new com.toedter.calendar.JDateChooser();
        jButton18 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel114 = new javax.swing.JLabel();
        jLabel115 = new javax.swing.JLabel();
        txtCompraAsign = new javax.swing.JTextField();
        txtidPedidoAsign = new javax.swing.JTextField();
        jLabel116 = new javax.swing.JLabel();
        jLabel117 = new javax.swing.JLabel();
        jLabel118 = new javax.swing.JLabel();
        jLabel119 = new javax.swing.JLabel();
        jLabel120 = new javax.swing.JLabel();
        jLabel121 = new javax.swing.JLabel();
        jLabel122 = new javax.swing.JLabel();
        jLabel123 = new javax.swing.JLabel();
        jScrollPane32 = new javax.swing.JScrollPane();
        jTabDetailAsignTotales = new javax.swing.JTable();
        jScrollPane33 = new javax.swing.JScrollPane();
        jTabDetailAsignTotales1 = new javax.swing.JTable();
        jLabel125 = new javax.swing.JLabel();
        jLabFlet = new javax.swing.JLabel();
        jLabPed = new javax.swing.JLabel();
        jLaComp = new javax.swing.JLabel();
        jLabel126 = new javax.swing.JLabel();
        jScrollPane27 = new javax.swing.JScrollPane();
        jTDetailAsign = new javax.swing.JTable();
        txtBusqAignCompra = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel104 = new javax.swing.JLabel();
        jLabel105 = new javax.swing.JLabel();
        jLabel106 = new javax.swing.JLabel();
        jButton34 = new javax.swing.JButton();
        jCheckBox3 = new javax.swing.JCheckBox();
        txtBusqFleteAsign = new javax.swing.JTextField();
        jLabel177 = new javax.swing.JLabel();
        jButton37 = new javax.swing.JButton();
        jPanPagos = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        ReportDay = new javax.swing.JPanel();
        jDFechPays = new com.toedter.calendar.JDateChooser();
        jButFleteGuardar1 = new javax.swing.JButton();
        jLabel100 = new javax.swing.JLabel();
        jLabel161 = new javax.swing.JLabel();
        jLabel162 = new javax.swing.JLabel();
        jButton23 = new javax.swing.JButton();
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel2 = new javax.swing.JPanel();
        jLabPed3 = new javax.swing.JLabel();
        jScrollPane28 = new javax.swing.JScrollPane();
        jTabPayPeds = new javax.swing.JTable();
        jLabPed4 = new javax.swing.JLabel();
        jLabPed5 = new javax.swing.JLabel();
        jScrollPane30 = new javax.swing.JScrollPane();
        jTabPayPrestamosProv = new javax.swing.JTable();
        jScrollPane21 = new javax.swing.JScrollPane();
        jTabVentPisoPays = new javax.swing.JTable();
        jLabCountPC = new javax.swing.JLabel();
        jLabCountPP = new javax.swing.JLabel();
        jLabel132 = new javax.swing.JLabel();
        jLabTotPP = new javax.swing.JLabel();
        jLabel134 = new javax.swing.JLabel();
        jLabTotPC = new javax.swing.JLabel();
        jLabel149 = new javax.swing.JLabel();
        jLabTotVP = new javax.swing.JLabel();
        jLabCountVP = new javax.swing.JLabel();
        jButton26 = new javax.swing.JButton();
        jButton27 = new javax.swing.JButton();
        jButton28 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabPed1 = new javax.swing.JLabel();
        jScrollPane29 = new javax.swing.JScrollPane();
        jTabPaysFletesDia = new javax.swing.JTable();
        jLabPed6 = new javax.swing.JLabel();
        jScrollPane22 = new javax.swing.JScrollPane();
        jTabGastosDias = new javax.swing.JTable();
        jScrollPane31 = new javax.swing.JScrollPane();
        jTabPaysCompraProovedor = new javax.swing.JTable();
        jLabPed7 = new javax.swing.JLabel();
        jLabcountPF = new javax.swing.JLabel();
        jLabel153 = new javax.swing.JLabel();
        jLabTotPF = new javax.swing.JLabel();
        jLabCountCP = new javax.swing.JLabel();
        jLabel156 = new javax.swing.JLabel();
        jLabTotCP = new javax.swing.JLabel();
        jLabCountGast = new javax.swing.JLabel();
        jLabel159 = new javax.swing.JLabel();
        jLabTotGast = new javax.swing.JLabel();
        jButton24 = new javax.swing.JButton();
        jButton29 = new javax.swing.JButton();
        jButton31 = new javax.swing.JButton();
        jButton30 = new javax.swing.JButton();
        jLabel163 = new javax.swing.JLabel();
        jLabel164 = new javax.swing.JLabel();
        jLabel165 = new javax.swing.JLabel();
        jLabel166 = new javax.swing.JLabel();
        jLabel167 = new javax.swing.JLabel();
        jLabel168 = new javax.swing.JLabel();
        jCheckBox2 = new javax.swing.JCheckBox();
        jPanel7 = new javax.swing.JPanel();
        jSplitPane2 = new javax.swing.JSplitPane();
        jPanel10 = new javax.swing.JPanel();
        jScrollPane37 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();
        jPanel11 = new javax.swing.JPanel();
        jScrollPane38 = new javax.swing.JScrollPane();
        jTable5 = new javax.swing.JTable();
        jLabel178 = new javax.swing.JLabel();
        jCmBoxIdCancel = new javax.swing.JComboBox<>();
        jButton38 = new javax.swing.JButton();
        jButton40 = new javax.swing.JButton();
        jLabel179 = new javax.swing.JLabel();
        jLabTurnCancel = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();

        jMnRealPay.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jMnRealPay.setText("REALIZAR PAGO");
        jMnRealPay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMnRealPayActionPerformed(evt);
            }
        });
        jPopPedidosDia.add(jMnRealPay);
        jPopPedidosDia.add(jSeparator2);

        jMIPPVer.setText("jMenuItem1");
        jPopupPrestaProov.add(jMIPPVer);
        jPopupPrestaProov.add(jSeparator1);

        jPopCompraProveedor.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jMenPago.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jMenPago.setText("Realizar Pago");
        jMenPago.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenPagoActionPerformed(evt);
            }
        });
        jPopCompraProveedor.add(jMenPago);
        jPopCompraProveedor.add(jSeparator5);

        AgregarMayoreo.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        AgregarMayoreo.setText("Ver detalle/ Eliminar compra");
        AgregarMayoreo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AgregarMayoreoActionPerformed(evt);
            }
        });
        jPopCompraProveedor.add(AgregarMayoreo);

        jPopFILTROCOMPRAS.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N

        jMPAYFILTRO.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jMPAYFILTRO.setText("Realizar pago");
        jMPAYFILTRO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMPAYFILTROActionPerformed(evt);
            }
        });
        jPopFILTROCOMPRAS.add(jMPAYFILTRO);

        jMnDetailVentaP.setFont(new java.awt.Font("Segoe UI", 0, 17)); // NOI18N
        jMnDetailVentaP.setText("Ver Detalle");
        jMnDetailVentaP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMnDetailVentaPActionPerformed(evt);
            }
        });
        jPopVentaPisoBusq.add(jMnDetailVentaP);
        jPopVentaPisoBusq.add(jSeparator14);

        jMnPayVentaP.setFont(new java.awt.Font("Segoe UI", 0, 17)); // NOI18N
        jMnPayVentaP.setText("Realizar Pago");
        jMnPayVentaP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMnPayVentaPActionPerformed(evt);
            }
        });
        jPopVentaPisoBusq.add(jMnPayVentaP);

        jMItDetailVer.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jMItDetailVer.setText("<html>\n<h2 style=\"color:rgb(11, 239, 251)\">Ver detalle</h2>\n</html>");
        jMItDetailVer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMItDetailVerActionPerformed(evt);
            }
        });
        jPMDetailFormaPedido.add(jMItDetailVer);
        jPMDetailFormaPedido.add(jSeparator16);

        jMIPaysPresta.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jMIPaysPresta.setText("Realizar Pago");
        jMIPaysPresta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMIPaysPrestaActionPerformed(evt);
            }
        });
        jPMPrestaProvPays.add(jMIPaysPresta);

        jMItPayFlete.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jMItPayFlete.setText("Realizar Pago");
        jMItPayFlete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMItPayFleteActionPerformed(evt);
            }
        });
        jPpMnPagoFletes.add(jMItPayFlete);

        jMitPayfilterFletes.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jMitPayfilterFletes.setText("Realizar Pago");
        jMitPayfilterFletes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMitPayfilterFletesActionPerformed(evt);
            }
        });
        jPopMFiltrosBusqFletes.add(jMitPayfilterFletes);

        jMI_ElimASIGNA.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jMI_ElimASIGNA.setText("Eliminar asignacion");
        jMI_ElimASIGNA.setToolTipText("Elimina la asignacion de compra a mayorista");
        jMI_ElimASIGNA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMI_ElimASIGNAActionPerformed(evt);
            }
        });
        jPMACompras.add(jMI_ElimASIGNA);

        jPMneliminaDetailMayorista.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N

        delDetailcompra.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        delDetailcompra.setText("Elimina producto asignado");
        delDetailcompra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                delDetailcompraActionPerformed(evt);
            }
        });
        jPMneliminaDetailMayorista.add(delDetailcompra);

        jFramElijeAsignCompras.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jFramElijeAsignCompras.setTitle("Asignacion de mercancia a pedido");
        jFramElijeAsignCompras.setSize(new java.awt.Dimension(685, 506));

        jLabel9.setText("<html>\n\n  <style type=\"text/css\">\n  span {\n    color: purple;\n    background-color: #d8da3d \n}\n  </style>\n\n<h3>Elija el poducto a asignar y <span>enseguida ingrese la cantidad a asignar en la celda correspondiente </span>.\n</h3>\n</html>");

        jTabAsigaDinamicoCompra.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTabAsigaDinamicoCompra.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jTabAsigaDinamicoCompra.setRowHeight(25);
        jTabAsigaDinamicoCompra.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTabAsigaDinamicoCompraMouseClicked(evt);
            }
        });
        jTabAsigaDinamicoCompra.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTabAsigaDinamicoCompraKeyReleased(evt);
            }
        });
        jScrollPane2.setViewportView(jTabAsigaDinamicoCompra);

        jButton16.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/uicheckOk.png"))); // NOI18N
        jButton16.setText("Asignar");
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });
        jButton16.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jButton16KeyReleased(evt);
            }
        });

        jLabel24.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel24.setText("# Compra:");

        jLabNumcompra.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        jLabNumcompra.setText("--");
        jLabNumcompra.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel29.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel29.setText("Para pedido #:");

        jLabNumcompra1.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        jLabNumcompra1.setText("--");
        jLabNumcompra1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel30.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel30.setText("En flete #:");

        jLabNumcompra2.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        jLabNumcompra2.setText("--");
        jLabNumcompra2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        javax.swing.GroupLayout jFramElijeAsignComprasLayout = new javax.swing.GroupLayout(jFramElijeAsignCompras.getContentPane());
        jFramElijeAsignCompras.getContentPane().setLayout(jFramElijeAsignComprasLayout);
        jFramElijeAsignComprasLayout.setHorizontalGroup(
            jFramElijeAsignComprasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jFramElijeAsignComprasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jFramElijeAsignComprasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jFramElijeAsignComprasLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton16, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jFramElijeAsignComprasLayout.createSequentialGroup()
                        .addGroup(jFramElijeAsignComprasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 615, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jFramElijeAsignComprasLayout.createSequentialGroup()
                                .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabNumcompra, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(41, 41, 41)
                                .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabNumcompra1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(35, 35, 35)
                                .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabNumcompra2, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 17, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jFramElijeAsignComprasLayout.setVerticalGroup(
            jFramElijeAsignComprasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jFramElijeAsignComprasLayout.createSequentialGroup()
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addGroup(jFramElijeAsignComprasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jFramElijeAsignComprasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabNumcompra, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabNumcompra1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel30, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabNumcompra2, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton16, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 98, Short.MAX_VALUE))
        );

        jPopupMenActualizaCompra.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        jMenuItem1.setText("Actualiza");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jPopupMenActualizaCompra.add(jMenuItem1);

        jDialDetailCompraProov.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jDialDetailCompraProov.setBackground(new java.awt.Color(222, 255, 216));
        jDialDetailCompraProov.setMinimumSize(new java.awt.Dimension(700, 400));
        jDialDetailCompraProov.setName(""); // NOI18N

        jTabDetProvView.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jTabDetProvView.setModel(new javax.swing.table.DefaultTableModel(
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
        jTabDetProvView.setRowHeight(25);
        jScrollPane10.setViewportView(jTabDetProvView);

        jLabel129.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel129.setText("Asignados:");

        jLabNumAsogna.setBackground(new java.awt.Color(255, 255, 255));
        jLabNumAsogna.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabNumAsogna.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabNumAsogna.setOpaque(true);

        jLabel131.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel131.setText("Restantes:");

        jLabFaltant.setBackground(new java.awt.Color(255, 255, 255));
        jLabFaltant.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabFaltant.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabFaltant.setOpaque(true);

        jLabel130.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel130.setText("Total de cajas:");

        jLabel108.setBackground(new java.awt.Color(255, 255, 255));
        jLabel108.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel108.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel108.setOpaque(true);

        jPanel5.setBackground(new java.awt.Color(222, 255, 216));

        jLabel107.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel107.setText("Id compra:");

        jLabId.setBackground(new java.awt.Color(255, 255, 255));
        jLabId.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        jLabId.setText("jLabel108");
        jLabId.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabId.setOpaque(true);

        jLabel109.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel109.setText("Detalle de asignaciones:");

        jLabContad.setBackground(new java.awt.Color(255, 255, 255));
        jLabContad.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabContad.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabContad.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabContad.setOpaque(true);

        jLabel113.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel113.setText("Proveedor:");

        jLabNomProov.setBackground(new java.awt.Color(255, 255, 255));
        jLabNomProov.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabNomProov.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabNomProov.setOpaque(true);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel109)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabContad, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel107)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabId)
                        .addGap(40, 40, 40)
                        .addComponent(jLabel113)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabNomProov)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabId)
                    .addComponent(jLabel113)
                    .addComponent(jLabNomProov)
                    .addComponent(jLabel107))
                .addGap(5, 5, 5)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel109, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabContad, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jDialDetailCompraProovLayout = new javax.swing.GroupLayout(jDialDetailCompraProov.getContentPane());
        jDialDetailCompraProov.getContentPane().setLayout(jDialDetailCompraProovLayout);
        jDialDetailCompraProovLayout.setHorizontalGroup(
            jDialDetailCompraProovLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialDetailCompraProovLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jDialDetailCompraProovLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jDialDetailCompraProovLayout.createSequentialGroup()
                        .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 679, Short.MAX_VALUE)
                        .addGap(11, 11, 11))
                    .addGroup(jDialDetailCompraProovLayout.createSequentialGroup()
                        .addComponent(jLabel130)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel108, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(81, 81, 81)
                        .addComponent(jLabel129)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabNumAsogna, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel131)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabFaltant, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jDialDetailCompraProovLayout.setVerticalGroup(
            jDialDetailCompraProovLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDialDetailCompraProovLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jDialDetailCompraProovLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabNumAsogna, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDialDetailCompraProovLayout.createSequentialGroup()
                        .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 340, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jDialDetailCompraProovLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabFaltant, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jDialDetailCompraProovLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel129)
                                .addComponent(jLabel131)
                                .addComponent(jLabel130)
                                .addComponent(jLabel108, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(5, 5, 5))
        );

        jMenuItem2.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jMenuItem2.setText("<html>\n<h2 style=\"color:rgb(11, 239, 251)\">Ver detalle</h2>\n</html>\n");
        jMenuItem2.setActionCommand("");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jPopMnDetailCompAsign.add(jMenuItem2);

        jDialDetailFlete1.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jDialDetailFlete1.setMinimumSize(new java.awt.Dimension(700, 400));
        jDialDetailFlete1.setName(""); // NOI18N
        jDialDetailFlete1.setSize(new java.awt.Dimension(700, 400));

        jLabel140.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel140.setText("Carga:");

        jLabel141.setBackground(new java.awt.Color(255, 255, 255));
        jLabel141.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel141.setText("jLabel128");
        jLabel141.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel141.setOpaque(true);

        jTable3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
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
        jTable3.setRowHeight(25);
        jScrollPane11.setViewportView(jTable3);

        jLabel142.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel142.setText("Asignados:");

        jLabel143.setBackground(new java.awt.Color(255, 255, 255));
        jLabel143.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel143.setText("jLabel130");
        jLabel143.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel143.setOpaque(true);

        jLabel144.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel144.setText("Restantes:");

        jLabel145.setBackground(new java.awt.Color(255, 255, 255));
        jLabel145.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel145.setText("jLabel130");
        jLabel145.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel145.setOpaque(true);

        jPanel6.setBackground(new java.awt.Color(222, 255, 216));
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel135.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel135.setText("Folio:");
        jPanel6.add(jLabel135, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 60, -1));

        jLabel136.setBackground(new java.awt.Color(255, 255, 255));
        jLabel136.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        jLabel136.setText("jLabel108");
        jLabel136.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel136.setOpaque(true);
        jPanel6.add(jLabel136, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 20, -1, -1));

        jLabel138.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel138.setText("Fletero:");
        jPanel6.add(jLabel138, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 20, -1, -1));

        jLabel139.setBackground(new java.awt.Color(255, 255, 255));
        jLabel139.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel139.setText("jLabel124");
        jLabel139.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel139.setOpaque(true);
        jPanel6.add(jLabel139, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 20, -1, -1));

        jLabel146.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel146.setText("Unidad:");
        jPanel6.add(jLabel146, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 20, -1, -1));

        jLabel147.setBackground(new java.awt.Color(255, 255, 255));
        jLabel147.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel147.setText("jLabel124");
        jLabel147.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel147.setOpaque(true);
        jPanel6.add(jLabel147, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 20, -1, -1));

        jLabel137.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel137.setText("Detalle Flete");
        jPanel6.add(jLabel137, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, -1, -1));

        jLabel124.setBackground(new java.awt.Color(255, 255, 255));
        jLabel124.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel124.setText("jLabel124");
        jLabel124.setOpaque(true);
        jPanel6.add(jLabel124, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 60, -1, -1));

        javax.swing.GroupLayout jDialDetailFlete1Layout = new javax.swing.GroupLayout(jDialDetailFlete1.getContentPane());
        jDialDetailFlete1.getContentPane().setLayout(jDialDetailFlete1Layout);
        jDialDetailFlete1Layout.setHorizontalGroup(
            jDialDetailFlete1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialDetailFlete1Layout.createSequentialGroup()
                .addGroup(jDialDetailFlete1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jDialDetailFlete1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jDialDetailFlete1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane11)
                            .addGroup(jDialDetailFlete1Layout.createSequentialGroup()
                                .addComponent(jLabel140)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel141)
                                .addGap(86, 86, 86)
                                .addComponent(jLabel142)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel143)
                                .addGap(36, 36, 36)
                                .addComponent(jLabel144)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel145)))))
                .addGap(0, 0, 0))
        );
        jDialDetailFlete1Layout.setVerticalGroup(
            jDialDetailFlete1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialDetailFlete1Layout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane11, javax.swing.GroupLayout.DEFAULT_SIZE, 274, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jDialDetailFlete1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel142)
                    .addComponent(jLabel143)
                    .addComponent(jLabel144)
                    .addComponent(jLabel145)
                    .addComponent(jLabel140)
                    .addComponent(jLabel141))
                .addContainerGap())
        );

        jMenuItem3.setText("<html>\n<h2 style=\"color:rgb(11, 239, 251)\">Ver detalle</h2>\n</html>\n");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jPpMnDetFletes.add(jMenuItem3);

        jDialAltaGastos.setTitle("Central Huixcolotla");
        jDialAltaGastos.setSize(new java.awt.Dimension(550, 330));
        jDialAltaGastos.setType(java.awt.Window.Type.UTILITY);
        jDialAltaGastos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jDialAltaGastosKeyPressed(evt);
            }
        });

        jPanel16.setBackground(new java.awt.Color(255, 255, 255));

        jLabel127.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel127.setText("Registrar gasto");

        jLabel128.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel128.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel128.setText("Cuenta");

        jLabel133.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel133.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel133.setText("Concepto");

        jLabel148.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel148.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel148.setText("Solicitante");

        jLabel150.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel150.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel150.setText("Obs.");

        jLabel151.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel151.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel151.setText("Monto");

        txtConcept.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtConcept.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtConceptKeyPressed(evt);
            }
        });

        txtsolict.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtsolict.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtsolictKeyPressed(evt);
            }
        });

        txtObservs.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtObservs.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtObservsKeyPressed(evt);
            }
        });

        txtMontoGasto.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtMontoGasto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtMontoGastoKeyPressed(evt);
            }
        });

        jCombBTypeRubros.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCombBTypeRubros.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " " }));
        jCombBTypeRubros.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCombBTypeRubrosActionPerformed(evt);
            }
        });
        jCombBTypeRubros.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jCombBTypeRubrosKeyPressed(evt);
            }
        });

        jComboBox1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel127, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel133, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel128, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel148, javax.swing.GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE)
                            .addComponent(jLabel150, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel151, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtsolict)
                            .addGroup(jPanel16Layout.createSequentialGroup()
                                .addComponent(txtMontoGasto, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 261, Short.MAX_VALUE))
                            .addComponent(txtObservs)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                                .addComponent(jCombBTypeRubros, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(104, 104, 104))
                            .addGroup(jPanel16Layout.createSequentialGroup()
                                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtConcept)))))
                .addGap(33, 33, 33))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel127)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel128, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCombBTypeRubros, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel133, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtConcept, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel148, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtsolict, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel150, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtObservs, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel151, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMontoGasto, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        jButton39.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton39.setText("Guardar");
        jButton39.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton39ActionPerformed(evt);
            }
        });

        jButton43.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton43.setText("Cancelar");
        jButton43.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton43ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jDialAltaGastosLayout = new javax.swing.GroupLayout(jDialAltaGastos.getContentPane());
        jDialAltaGastos.getContentPane().setLayout(jDialAltaGastosLayout);
        jDialAltaGastosLayout.setHorizontalGroup(
            jDialAltaGastosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialAltaGastosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jDialAltaGastosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jDialAltaGastosLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton39, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton43, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jDialAltaGastosLayout.setVerticalGroup(
            jDialAltaGastosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialAltaGastosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jDialAltaGastosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton43, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton39, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        jDialCancelaciones.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jDialCancelaciones.setSize(new java.awt.Dimension(486, 170));
        jDialCancelaciones.setType(java.awt.Window.Type.UTILITY);

        jLabel152.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel152.setText("Ingrese el motivo de la cancelación");

        txtCancelTick.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtCancelTick.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCancelTickKeyReleased(evt);
            }
        });

        jButton32.setText("Cancelar");
        jButton32.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton32ActionPerformed(evt);
            }
        });

        jButton33.setText("OK");
        jButton33.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton33ActionPerformed(evt);
            }
        });

        jLabel154.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel154.setText("Id:");

        jLabIdCancel.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabIdCancel.setText("--");

        jLabParam2.setText("--");
        jLabParam2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        javax.swing.GroupLayout jDialCancelacionesLayout = new javax.swing.GroupLayout(jDialCancelaciones.getContentPane());
        jDialCancelaciones.getContentPane().setLayout(jDialCancelacionesLayout);
        jDialCancelacionesLayout.setHorizontalGroup(
            jDialCancelacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialCancelacionesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jDialCancelacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jDialCancelacionesLayout.createSequentialGroup()
                        .addComponent(txtCancelTick, javax.swing.GroupLayout.PREFERRED_SIZE, 453, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jDialCancelacionesLayout.createSequentialGroup()
                        .addGroup(jDialCancelacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel152, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 309, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jDialCancelacionesLayout.createSequentialGroup()
                                .addGap(202, 202, 202)
                                .addComponent(jButton33, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jDialCancelacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jDialCancelacionesLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jButton32, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)
                                .addGap(23, 23, 23))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDialCancelacionesLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabParam2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel154)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabIdCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())))))
        );
        jDialCancelacionesLayout.setVerticalGroup(
            jDialCancelacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialCancelacionesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jDialCancelacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel152, javax.swing.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE)
                    .addComponent(jLabel154, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabIdCancel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabParam2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(15, 15, 15)
                .addComponent(txtCancelTick, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jDialCancelacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton32, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                    .addComponent(jButton33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jDiaViewSobrinas.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jDiaViewSobrinas.setBackground(new java.awt.Color(255, 255, 255));
        jDiaViewSobrinas.setModalityType(null);
        jDiaViewSobrinas.setSize(new java.awt.Dimension(879, 500));
        jDiaViewSobrinas.setType(java.awt.Window.Type.UTILITY);

        jLabel160.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel160.setText("Compras con sobrantes");

        jTabSobrinasDays.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTabSobrinasDays.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "Proveedor", "PRIM", "SEG", "PRIM_R", "SEG_R", "BOLA_P", "BOLA_S", "3_RA", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, true, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTabSobrinasDays.setRowHeight(23);
        jScrollPane24.setViewportView(jTabSobrinasDays);
        if (jTabSobrinasDays.getColumnModel().getColumnCount() > 0) {
            jTabSobrinasDays.getColumnModel().getColumn(0).setMinWidth(50);
            jTabSobrinasDays.getColumnModel().getColumn(0).setPreferredWidth(50);
            jTabSobrinasDays.getColumnModel().getColumn(0).setMaxWidth(120);
            jTabSobrinasDays.getColumnModel().getColumn(2).setMinWidth(70);
            jTabSobrinasDays.getColumnModel().getColumn(2).setPreferredWidth(70);
            jTabSobrinasDays.getColumnModel().getColumn(2).setMaxWidth(120);
            jTabSobrinasDays.getColumnModel().getColumn(3).setMinWidth(70);
            jTabSobrinasDays.getColumnModel().getColumn(3).setPreferredWidth(70);
            jTabSobrinasDays.getColumnModel().getColumn(3).setMaxWidth(120);
            jTabSobrinasDays.getColumnModel().getColumn(4).setMinWidth(70);
            jTabSobrinasDays.getColumnModel().getColumn(4).setPreferredWidth(70);
            jTabSobrinasDays.getColumnModel().getColumn(4).setMaxWidth(120);
            jTabSobrinasDays.getColumnModel().getColumn(5).setMinWidth(70);
            jTabSobrinasDays.getColumnModel().getColumn(5).setPreferredWidth(70);
            jTabSobrinasDays.getColumnModel().getColumn(5).setMaxWidth(120);
            jTabSobrinasDays.getColumnModel().getColumn(6).setMinWidth(70);
            jTabSobrinasDays.getColumnModel().getColumn(6).setPreferredWidth(70);
            jTabSobrinasDays.getColumnModel().getColumn(6).setMaxWidth(120);
            jTabSobrinasDays.getColumnModel().getColumn(7).setMinWidth(70);
            jTabSobrinasDays.getColumnModel().getColumn(7).setPreferredWidth(70);
            jTabSobrinasDays.getColumnModel().getColumn(7).setMaxWidth(120);
            jTabSobrinasDays.getColumnModel().getColumn(8).setMinWidth(70);
            jTabSobrinasDays.getColumnModel().getColumn(8).setPreferredWidth(70);
            jTabSobrinasDays.getColumnModel().getColumn(8).setMaxWidth(120);
            jTabSobrinasDays.getColumnModel().getColumn(9).setMinWidth(70);
            jTabSobrinasDays.getColumnModel().getColumn(9).setPreferredWidth(70);
            jTabSobrinasDays.getColumnModel().getColumn(9).setMaxWidth(120);
        }

        jLabRcontFilsob.setFont(new java.awt.Font("Tahoma", 3, 16)); // NOI18N
        jLabRcontFilsob.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jButton35.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton35.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/chequeOk.png"))); // NOI18N
        jButton35.setText("Guardar");
        jButton35.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton35ActionPerformed(evt);
            }
        });

        jLabel170.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel170.setText("Cajas sobrantes:");

        jTable2.setFont(new java.awt.Font("Tahoma", 3, 16)); // NOI18N
        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "PRIM", "SEG", "PRIM_R", "SEG_R", "BOLA_P", "BOLA_S", "3_RA", "TOTAL"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable2.setColumnSelectionAllowed(true);
        jTable2.setRowHeight(30);
        jScrollPane36.setViewportView(jTable2);
        jTable2.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        if (jTable2.getColumnModel().getColumnCount() > 0) {
            jTable2.getColumnModel().getColumn(0).setMinWidth(70);
            jTable2.getColumnModel().getColumn(0).setPreferredWidth(70);
            jTable2.getColumnModel().getColumn(0).setMaxWidth(120);
            jTable2.getColumnModel().getColumn(1).setMinWidth(70);
            jTable2.getColumnModel().getColumn(1).setPreferredWidth(70);
            jTable2.getColumnModel().getColumn(1).setMaxWidth(120);
            jTable2.getColumnModel().getColumn(2).setMinWidth(70);
            jTable2.getColumnModel().getColumn(2).setPreferredWidth(70);
            jTable2.getColumnModel().getColumn(2).setMaxWidth(120);
            jTable2.getColumnModel().getColumn(3).setMinWidth(70);
            jTable2.getColumnModel().getColumn(3).setPreferredWidth(70);
            jTable2.getColumnModel().getColumn(3).setMaxWidth(120);
            jTable2.getColumnModel().getColumn(4).setMinWidth(70);
            jTable2.getColumnModel().getColumn(4).setPreferredWidth(70);
            jTable2.getColumnModel().getColumn(4).setMaxWidth(120);
            jTable2.getColumnModel().getColumn(5).setMinWidth(70);
            jTable2.getColumnModel().getColumn(5).setPreferredWidth(70);
            jTable2.getColumnModel().getColumn(5).setMaxWidth(120);
            jTable2.getColumnModel().getColumn(6).setMinWidth(70);
            jTable2.getColumnModel().getColumn(6).setPreferredWidth(70);
            jTable2.getColumnModel().getColumn(6).setMaxWidth(120);
            jTable2.getColumnModel().getColumn(7).setMinWidth(70);
            jTable2.getColumnModel().getColumn(7).setPreferredWidth(70);
            jTable2.getColumnModel().getColumn(7).setMaxWidth(120);
        }

        javax.swing.GroupLayout jDiaViewSobrinasLayout = new javax.swing.GroupLayout(jDiaViewSobrinas.getContentPane());
        jDiaViewSobrinas.getContentPane().setLayout(jDiaViewSobrinasLayout);
        jDiaViewSobrinasLayout.setHorizontalGroup(
            jDiaViewSobrinasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDiaViewSobrinasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jDiaViewSobrinasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jDiaViewSobrinasLayout.createSequentialGroup()
                        .addGroup(jDiaViewSobrinasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jDiaViewSobrinasLayout.createSequentialGroup()
                                .addComponent(jLabel160, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabRcontFilsob, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jScrollPane24))
                        .addContainerGap())
                    .addGroup(jDiaViewSobrinasLayout.createSequentialGroup()
                        .addGap(177, 177, 177)
                        .addComponent(jLabel170)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane36, javax.swing.GroupLayout.DEFAULT_SIZE, 576, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDiaViewSobrinasLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton35, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(121, 121, 121))))
        );
        jDiaViewSobrinasLayout.setVerticalGroup(
            jDiaViewSobrinasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDiaViewSobrinasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jDiaViewSobrinasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel160)
                    .addComponent(jLabRcontFilsob))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane24, javax.swing.GroupLayout.DEFAULT_SIZE, 273, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jDiaViewSobrinasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel170)
                    .addGroup(jDiaViewSobrinasLayout.createSequentialGroup()
                        .addComponent(jScrollPane36, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton35, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(25, 25, 25))
        );

        jDialCalendarMantenim.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jDialCalendarMantenim.setTitle("Elija semana");
        jDialCalendarMantenim.setResizable(false);
        jDialCalendarMantenim.setSize(new java.awt.Dimension(450, 300));
        jDialCalendarMantenim.setType(java.awt.Window.Type.UTILITY);

        jCalendar1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jCalendar1.setDecorationBordersVisible(true);
        jCalendar1.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N

        jButton42.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton42.setText("Seleccionar");
        jButton42.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton42ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jDialCalendarMantenimLayout = new javax.swing.GroupLayout(jDialCalendarMantenim.getContentPane());
        jDialCalendarMantenim.getContentPane().setLayout(jDialCalendarMantenimLayout);
        jDialCalendarMantenimLayout.setHorizontalGroup(
            jDialCalendarMantenimLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialCalendarMantenimLayout.createSequentialGroup()
                .addGroup(jDialCalendarMantenimLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jDialCalendarMantenimLayout.createSequentialGroup()
                        .addGap(144, 144, 144)
                        .addComponent(jButton42, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jDialCalendarMantenimLayout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(jCalendar1, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(28, Short.MAX_VALUE))
        );
        jDialCalendarMantenimLayout.setVerticalGroup(
            jDialCalendarMantenimLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialCalendarMantenimLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jCalendar1, javax.swing.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jButton42, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("ADMINISTRACION DCR");
        setIconImage(getIconImage());

        jPanCabezera.setBackground(new java.awt.Color(117, 229, 255));

        jLabel52.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel52.setText("Usuario:");

        jTextField17.setEditable(false);
        jTextField17.setBackground(new java.awt.Color(255, 255, 255));
        jTextField17.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        jTextField17.setForeground(new java.awt.Color(51, 0, 153));
        jTextField17.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jButton2.setBackground(new java.awt.Color(117, 229, 255));
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/Actualizar.png"))); // NOI18N
        jButton2.setToolTipText("Actualzar sistema.");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton6.setBackground(new java.awt.Color(117, 229, 255));
        jButton6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton6.setText("BACKUP");
        jButton6.setToolTipText("Realizar respaldo de base de datos.");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jLabel39.setFont(new java.awt.Font("Tahoma", 2, 14)); // NOI18N
        jLabel39.setText("#Turno:");

        jLabTurno.setBackground(new java.awt.Color(255, 255, 255));
        jLabTurno.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabTurno.setText("--");
        jLabTurno.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabTurno.setOpaque(true);

        jButton25.setBackground(new java.awt.Color(117, 229, 255));
        jButton25.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/payExit.png"))); // NOI18N
        jButton25.setToolTipText("Registrar gasto");
        jButton25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton25ActionPerformed(evt);
            }
        });

        jLabIdView.setBackground(new java.awt.Color(255, 255, 255));
        jLabIdView.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabIdView.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabIdView.setOpaque(true);

        jButton36.setBackground(new java.awt.Color(117, 229, 255));
        jButton36.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jButton36.setText("$");
        jButton36.setToolTipText("Ingresar efectivo");
        jButton36.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton36ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanCabezeraLayout = new javax.swing.GroupLayout(jPanCabezera);
        jPanCabezera.setLayout(jPanCabezeraLayout);
        jPanCabezeraLayout.setHorizontalGroup(
            jPanCabezeraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanCabezeraLayout.createSequentialGroup()
                .addGroup(jPanCabezeraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanCabezeraLayout.createSequentialGroup()
                        .addGap(80, 80, 80)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton6))
                .addGap(0, 0, 0)
                .addComponent(jButton25)
                .addGap(0, 0, 0)
                .addComponent(jButton36, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabTurno, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(jLabel52, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jLabIdView, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jTextField17, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(96, 96, 96))
        );
        jPanCabezeraLayout.setVerticalGroup(
            jPanCabezeraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jButton25, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addComponent(jLabel39, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanCabezeraLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanCabezeraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabTurno, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel52, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                    .addComponent(jTextField17, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                    .addComponent(jLabIdView, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addGroup(jPanCabezeraLayout.createSequentialGroup()
                .addGroup(jPanCabezeraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(jButton36, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        paneAltas.setBackground(new java.awt.Color(255, 255, 255));
        paneAltas.setForeground(new java.awt.Color(107, 109, 232));
        paneAltas.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        paneAltas.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        paneAltas.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        paneAltas.setMinimumSize(new java.awt.Dimension(1281, 745));
        paneAltas.setOpaque(true);

        jPanAltas.setBackground(new java.awt.Color(255, 255, 255));
        jPanAltas.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanAltas.setPreferredSize(new java.awt.Dimension(1600, 840));

        jPanAdminist.setBackground(new java.awt.Color(255, 255, 255));
        jPanAdminist.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Administracion de clientes", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 13))); // NOI18N
        jPanAdminist.setPreferredSize(new java.awt.Dimension(0, 0));

        jLabel7.setFont(new java.awt.Font("Calibri", 0, 15)); // NOI18N
        jLabel7.setText("LOCALIDAD:");

        jLabel13.setFont(new java.awt.Font("Calibri", 0, 15)); // NOI18N
        jLabel13.setText("APELLIDOS:");

        jTextLocalidad.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N

        jTextApellidos.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N

        jButAltasElimina.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButAltasElimina.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/delete32px.png"))); // NOI18N
        jButAltasElimina.setText("Eliminar");
        jButAltasElimina.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButAltasEliminaActionPerformed(evt);
            }
        });

        jTexTelefono.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N

        jButaltasGuardar.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButaltasGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/add-BN.png"))); // NOI18N
        jButaltasGuardar.setText("    Guardar");
        jButaltasGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButaltasGuardarActionPerformed(evt);
            }
        });

        jButAltasActualiza.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButAltasActualiza.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/Actualizar.png"))); // NOI18N
        jButAltasActualiza.setText("    Actualizar");
        jButAltasActualiza.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButAltasActualizaActionPerformed(evt);
            }
        });

        jLabel17.setFont(new java.awt.Font("Calibri", 0, 15)); // NOI18N
        jLabel17.setText("TELEFONO:");

        btnGAltaClientes.add(jRad1Activo);
        jRad1Activo.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jRad1Activo.setText("ACTIVO");
        jRad1Activo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRad1ActivoActionPerformed(evt);
            }
        });

        btnGAltaClientes.add(jRadInactivo);
        jRadInactivo.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jRadInactivo.setText("INACTIVO");

        jLabel19.setFont(new java.awt.Font("Calibri", 0, 15)); // NOI18N
        jLabel19.setText("STATUS:");

        jLabel21.setFont(new java.awt.Font("Calibri", 0, 15)); // NOI18N
        jLabel21.setText("TIPO PROVEEDOR:");

        jLabelRFCAlta.setFont(new java.awt.Font("Calibri", 0, 15)); // NOI18N
        jLabelRFCAlta.setText("RFC:");

        txtQuintoAltas.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N

        jTextNombre.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N

        jLabel25.setFont(new java.awt.Font("Calibri", 0, 15)); // NOI18N
        jLabel25.setText("NOMBRE:");

        jCBTypeProv.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCBTypeProv.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "MINORISTA", "MAYORISTA" }));

        javax.swing.GroupLayout jPanAdministLayout = new javax.swing.GroupLayout(jPanAdminist);
        jPanAdminist.setLayout(jPanAdministLayout);
        jPanAdministLayout.setHorizontalGroup(
            jPanAdministLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanAdministLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanAdministLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanAdministLayout.createSequentialGroup()
                        .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jTextNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanAdministLayout.createSequentialGroup()
                        .addGroup(jPanAdministLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanAdministLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextApellidos, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextLocalidad, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(29, 29, 29)
                .addGroup(jPanAdministLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanAdministLayout.createSequentialGroup()
                        .addGroup(jPanAdministLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanAdministLayout.createSequentialGroup()
                                .addGap(70, 70, 70)
                                .addComponent(jRad1Activo, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10)
                        .addComponent(jRadInactivo, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanAdministLayout.createSequentialGroup()
                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jTexTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanAdministLayout.createSequentialGroup()
                        .addGroup(jPanAdministLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelRFCAlta, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanAdministLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtQuintoAltas, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jCBTypeProv, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(26, 26, 26)
                .addGroup(jPanAdministLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButaltasGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButAltasActualiza, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButAltasElimina, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(628, Short.MAX_VALUE))
        );
        jPanAdministLayout.setVerticalGroup(
            jPanAdministLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanAdministLayout.createSequentialGroup()
                .addGroup(jPanAdministLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanAdministLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(jPanAdministLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jRad1Activo, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jRadInactivo, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10)
                        .addGroup(jPanAdministLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTexTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10)
                        .addGroup(jPanAdministLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanAdministLayout.createSequentialGroup()
                                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanAdministLayout.createSequentialGroup()
                                .addComponent(jTextApellidos, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(jTextLocalidad, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanAdministLayout.createSequentialGroup()
                                .addComponent(jLabelRFCAlta, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanAdministLayout.createSequentialGroup()
                                .addComponent(txtQuintoAltas, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(jCBTypeProv, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanAdministLayout.createSequentialGroup()
                        .addComponent(jButaltasGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jButAltasActualiza, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jButAltasElimina, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(25, 25, 25))
        );

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel1.setText("Elija tipo de usuario a registrar:");

        jComboAltas.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jComboAltas.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Cliente", "Proveedor", "Empleado", "Fletero" }));
        jComboAltas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboAltasActionPerformed(evt);
            }
        });

        jPanVistaAlta.setBackground(new java.awt.Color(255, 255, 255));
        jPanVistaAlta.setPreferredSize(new java.awt.Dimension(0, 0));

        jLabel12.setFont(new java.awt.Font("Calibri", 0, 15)); // NOI18N
        jLabel12.setText("BUSCAR:");

        jTableAltasCli.setFont(new java.awt.Font("Serif", 1, 14)); // NOI18N
        jTableAltasCli.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jTableAltasCli.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jTableAltasCli.setEditingColumn(0);
        jTableAltasCli.setEditingRow(0);
        jTableAltasCli.setRowHeight(26);
        jTableAltasCli.setRowMargin(4);
        jTableAltasCli.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTableAltasCliMousePressed(evt);
            }
        });
        jScrollPane5.setViewportView(jTableAltasCli);

        txtBusqAltas.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        txtBusqAltas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBusqAltasKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanVistaAltaLayout = new javax.swing.GroupLayout(jPanVistaAlta);
        jPanVistaAlta.setLayout(jPanVistaAltaLayout);
        jPanVistaAltaLayout.setHorizontalGroup(
            jPanVistaAltaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanVistaAltaLayout.createSequentialGroup()
                .addGroup(jPanVistaAltaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanVistaAltaLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtBusqAltas, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanVistaAltaLayout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 1511, Short.MAX_VALUE)))
                .addGap(25, 25, 25))
        );
        jPanVistaAltaLayout.setVerticalGroup(
            jPanVistaAltaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanVistaAltaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanVistaAltaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtBusqAltas, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 372, Short.MAX_VALUE)
                .addGap(35, 35, 35))
        );

        jDateChFechaAlta.setDateFormatString("dd/MM/yyyy");
        jDateChFechaAlta.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N

        jLabel16.setFont(new java.awt.Font("Calibri", 0, 15)); // NOI18N
        jLabel16.setText("FECHA ALTA:");

        txtIdParam.setEditable(false);
        txtIdParam.setBackground(new java.awt.Color(255, 255, 255));
        txtIdParam.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N

        javax.swing.GroupLayout jPanAltasLayout = new javax.swing.GroupLayout(jPanAltas);
        jPanAltas.setLayout(jPanAltasLayout);
        jPanAltasLayout.setHorizontalGroup(
            jPanAltasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanAltasLayout.createSequentialGroup()
                .addComponent(jPanVistaAlta, javax.swing.GroupLayout.DEFAULT_SIZE, 1566, Short.MAX_VALUE)
                .addGap(25, 25, 25))
            .addGroup(jPanAltasLayout.createSequentialGroup()
                .addGroup(jPanAltasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanAltasLayout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jComboAltas, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addComponent(txtIdParam, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(61, 61, 61)
                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jDateChFechaAlta, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanAltasLayout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addComponent(jPanAdminist, javax.swing.GroupLayout.DEFAULT_SIZE, 1495, Short.MAX_VALUE)))
                .addGap(55, 55, 55))
        );
        jPanAltasLayout.setVerticalGroup(
            jPanAltasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanAltasLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanAltasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboAltas, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtIdParam, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jDateChFechaAlta, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanAdminist, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanVistaAlta, javax.swing.GroupLayout.DEFAULT_SIZE, 465, Short.MAX_VALUE)
                .addContainerGap())
        );

        paneAltas.addTab("    ALTA DE CLIENTES    ", jPanAltas);

        jPPedidosHist.setPreferredSize(new java.awt.Dimension(1600, 840));

        jLabel15.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel15.setText("ELIJA ACCION:");

        jRadioCreaPedido.setBackground(new java.awt.Color(255, 255, 255));
        butnGPedidos.add(jRadioCreaPedido);
        jRadioCreaPedido.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jRadioCreaPedido.setText("CREAR PEDIDOS");
        jRadioCreaPedido.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jRadioCreaPedido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioCreaPedidoActionPerformed(evt);
            }
        });

        jRadioConsulPedido.setBackground(new java.awt.Color(255, 255, 255));
        butnGPedidos.add(jRadioConsulPedido);
        jRadioConsulPedido.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jRadioConsulPedido.setText("CONSULTAR PEDIDOS");
        jRadioConsulPedido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioConsulPedidoActionPerformed(evt);
            }
        });

        jLayeredPanePedidos.setPreferredSize(new java.awt.Dimension(1600, 790));

        jPanCreaPedido.setBackground(new java.awt.Color(255, 255, 255));
        jPanCreaPedido.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Nuevo pedido", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13), new java.awt.Color(0, 51, 51))); // NOI18N
        jPanCreaPedido.setPreferredSize(new java.awt.Dimension(1600, 790));

        jCombPedidoClient.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCombPedidoClient.setMaximumRowCount(10);
        jCombPedidoClient.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " " }));
        jCombPedidoClient.setNextFocusableComponent(jCombProdPedidos);

        jLabel46.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel46.setText("PEDIDOS DEL DIA:");

        jLabel43.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel43.setText("Tipo Mercancia:");

        jCombProdPedidos.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCombProdPedidos.setNextFocusableComponent(txtCantCreaPedido);

        jLabel44.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel44.setText("CANTIDAD:");

        txtCantCreaPedido.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtCantCreaPedido.setNextFocusableComponent(txtNotePedidoCli);
        txtCantCreaPedido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCantCreaPedidoActionPerformed(evt);
            }
        });
        txtCantCreaPedido.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCantCreaPedidoKeyReleased(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel6.setText("CAJAS");

        jLabel45.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel45.setText("NOTA:");

        jButton9.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton9.setText("AGREGAR");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        txtNotePedidoCli.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtNotePedidoCli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNotePedidoCliActionPerformed(evt);
            }
        });
        txtNotePedidoCli.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNotePedidoCliKeyReleased(evt);
            }
        });

        jScrollPane9.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jTableCreaPedidos.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTableCreaPedidos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "PRIM", "SEG", "PRIM_R", "SEG_R", "BOLA_P", "BOLA_S", "TERCERA"
            }
        ));
        jTableCreaPedidos.setComponentPopupMenu(jPopPedidosDia);
        jTableCreaPedidos.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jTableCreaPedidos.setEditingColumn(0);
        jTableCreaPedidos.setEditingRow(0);
        jTableCreaPedidos.setRowHeight(32);
        jTableCreaPedidos.setRowMargin(2);
        jTableCreaPedidos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTableCreaPedidosKeyReleased(evt);
            }
        });
        jScrollPane9.setViewportView(jTableCreaPedidos);

        jDateCHPedido.setDateFormatString("dd/MM/yyyy");
        jDateCHPedido.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N

        jLabel58.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel58.setText("ELIJA CLIENTE:");

        jLabel59.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel59.setText("PEDIDO DE CLIENTE:");

        jButGuardaPedidodia.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButGuardaPedidodia.setText("GUARDAR");
        jButGuardaPedidodia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButGuardaPedidodiaActionPerformed(evt);
            }
        });

        jTabVistaPedidosDetDia.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTabVistaPedidosDetDia.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "PROVEEDOR", "PRIM", "SEG", "PRIM_R", "SEG_R", "BOLA_P", "BOLA_S", "TERCERA", "TOTAL CAJAS"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTabVistaPedidosDetDia.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jTabVistaPedidosDetDia.setEditingColumn(0);
        jTabVistaPedidosDetDia.setEditingRow(0);
        jTabVistaPedidosDetDia.setRowHeight(25);
        jTabVistaPedidosDetDia.setSelectionBackground(new java.awt.Color(153, 255, 153));
        jTabVistaPedidosDetDia.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTabVistaPedidosDetDiaMousePressed(evt);
            }
        });
        jScrollPane15.setViewportView(jTabVistaPedidosDetDia);
        if (jTabVistaPedidosDetDia.getColumnModel().getColumnCount() > 0) {
            jTabVistaPedidosDetDia.getColumnModel().getColumn(0).setMinWidth(50);
            jTabVistaPedidosDetDia.getColumnModel().getColumn(0).setPreferredWidth(50);
            jTabVistaPedidosDetDia.getColumnModel().getColumn(0).setMaxWidth(50);
            jTabVistaPedidosDetDia.getColumnModel().getColumn(1).setMinWidth(170);
            jTabVistaPedidosDetDia.getColumnModel().getColumn(1).setPreferredWidth(170);
            jTabVistaPedidosDetDia.getColumnModel().getColumn(1).setMaxWidth(250);
            jTabVistaPedidosDetDia.getColumnModel().getColumn(2).setMinWidth(110);
            jTabVistaPedidosDetDia.getColumnModel().getColumn(2).setPreferredWidth(110);
            jTabVistaPedidosDetDia.getColumnModel().getColumn(2).setMaxWidth(150);
            jTabVistaPedidosDetDia.getColumnModel().getColumn(3).setMinWidth(110);
            jTabVistaPedidosDetDia.getColumnModel().getColumn(3).setPreferredWidth(110);
            jTabVistaPedidosDetDia.getColumnModel().getColumn(3).setMaxWidth(150);
            jTabVistaPedidosDetDia.getColumnModel().getColumn(4).setMinWidth(110);
            jTabVistaPedidosDetDia.getColumnModel().getColumn(4).setPreferredWidth(110);
            jTabVistaPedidosDetDia.getColumnModel().getColumn(4).setMaxWidth(150);
            jTabVistaPedidosDetDia.getColumnModel().getColumn(5).setMinWidth(110);
            jTabVistaPedidosDetDia.getColumnModel().getColumn(5).setPreferredWidth(110);
            jTabVistaPedidosDetDia.getColumnModel().getColumn(5).setMaxWidth(150);
            jTabVistaPedidosDetDia.getColumnModel().getColumn(6).setMinWidth(110);
            jTabVistaPedidosDetDia.getColumnModel().getColumn(6).setPreferredWidth(110);
            jTabVistaPedidosDetDia.getColumnModel().getColumn(6).setMaxWidth(150);
            jTabVistaPedidosDetDia.getColumnModel().getColumn(7).setMinWidth(110);
            jTabVistaPedidosDetDia.getColumnModel().getColumn(7).setPreferredWidth(110);
            jTabVistaPedidosDetDia.getColumnModel().getColumn(7).setMaxWidth(150);
            jTabVistaPedidosDetDia.getColumnModel().getColumn(8).setMinWidth(110);
            jTabVistaPedidosDetDia.getColumnModel().getColumn(8).setPreferredWidth(110);
            jTabVistaPedidosDetDia.getColumnModel().getColumn(8).setMaxWidth(150);
        }

        jScrollPane35.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jTabSumTotalPedido.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jTabSumTotalPedido.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "PRIM", "SEG", "PRIM_R", "SEG_R", "BOLA_P", "BOLA_S", "TERCERA", "TOTAL CAJAS"
            }
        ));
        jTabSumTotalPedido.setRowHeight(44);
        jScrollPane35.setViewportView(jTabSumTotalPedido);
        if (jTabSumTotalPedido.getColumnModel().getColumnCount() > 0) {
            jTabSumTotalPedido.getColumnModel().getColumn(0).setMinWidth(110);
            jTabSumTotalPedido.getColumnModel().getColumn(0).setPreferredWidth(110);
            jTabSumTotalPedido.getColumnModel().getColumn(0).setMaxWidth(150);
            jTabSumTotalPedido.getColumnModel().getColumn(1).setMinWidth(110);
            jTabSumTotalPedido.getColumnModel().getColumn(1).setPreferredWidth(110);
            jTabSumTotalPedido.getColumnModel().getColumn(1).setMaxWidth(150);
            jTabSumTotalPedido.getColumnModel().getColumn(2).setMinWidth(110);
            jTabSumTotalPedido.getColumnModel().getColumn(2).setPreferredWidth(110);
            jTabSumTotalPedido.getColumnModel().getColumn(2).setMaxWidth(150);
            jTabSumTotalPedido.getColumnModel().getColumn(3).setMinWidth(110);
            jTabSumTotalPedido.getColumnModel().getColumn(3).setPreferredWidth(110);
            jTabSumTotalPedido.getColumnModel().getColumn(3).setMaxWidth(150);
            jTabSumTotalPedido.getColumnModel().getColumn(4).setMinWidth(110);
            jTabSumTotalPedido.getColumnModel().getColumn(4).setPreferredWidth(110);
            jTabSumTotalPedido.getColumnModel().getColumn(4).setMaxWidth(150);
            jTabSumTotalPedido.getColumnModel().getColumn(5).setMinWidth(110);
            jTabSumTotalPedido.getColumnModel().getColumn(5).setPreferredWidth(110);
            jTabSumTotalPedido.getColumnModel().getColumn(5).setMaxWidth(150);
            jTabSumTotalPedido.getColumnModel().getColumn(6).setMinWidth(110);
            jTabSumTotalPedido.getColumnModel().getColumn(6).setPreferredWidth(110);
            jTabSumTotalPedido.getColumnModel().getColumn(6).setMaxWidth(150);
        }

        jLabel31.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel31.setText("TOTAL DEL DIA");

        jButton15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/search32pxcolor.png"))); // NOI18N
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });

        jButton22.setBackground(new java.awt.Color(191, 252, 195));
        jButton22.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/database.png"))); // NOI18N
        jButton22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton22ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanCreaPedidoLayout = new javax.swing.GroupLayout(jPanCreaPedido);
        jPanCreaPedido.setLayout(jPanCreaPedidoLayout);
        jPanCreaPedidoLayout.setHorizontalGroup(
            jPanCreaPedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanCreaPedidoLayout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(jPanCreaPedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanCreaPedidoLayout.createSequentialGroup()
                        .addComponent(jLabel46, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 455, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanCreaPedidoLayout.createSequentialGroup()
                        .addGroup(jPanCreaPedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanCreaPedidoLayout.createSequentialGroup()
                                .addGap(105, 105, 105)
                                .addComponent(jLabel31)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane35))
                            .addComponent(jScrollPane15))
                        .addGap(50, 50, 50))))
            .addGroup(jPanCreaPedidoLayout.createSequentialGroup()
                .addGroup(jPanCreaPedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanCreaPedidoLayout.createSequentialGroup()
                        .addGroup(jPanCreaPedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanCreaPedidoLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 681, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButGuardaPedidodia, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanCreaPedidoLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanCreaPedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel43)
                                    .addComponent(jLabel58))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanCreaPedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanCreaPedidoLayout.createSequentialGroup()
                                        .addComponent(jCombPedidoClient, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButton22, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jDateCHPedido, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanCreaPedidoLayout.createSequentialGroup()
                                        .addComponent(jCombProdPedidos, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(28, 28, 28)
                                        .addComponent(jLabel44, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtCantCreaPedido, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, 0)
                                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(20, 20, 20)
                                        .addComponent(jLabel45, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, 0)
                                        .addComponent(txtNotePedidoCli, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanCreaPedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton15)
                            .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanCreaPedidoLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel59, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanCreaPedidoLayout.setVerticalGroup(
            jPanCreaPedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanCreaPedidoLayout.createSequentialGroup()
                .addGroup(jPanCreaPedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanCreaPedidoLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(jPanCreaPedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jCombPedidoClient, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel58, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanCreaPedidoLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanCreaPedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanCreaPedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jButton15, javax.swing.GroupLayout.PREFERRED_SIZE, 32, Short.MAX_VALUE)
                                .addComponent(jDateCHPedido, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jButton22, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanCreaPedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanCreaPedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanCreaPedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel44, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCantCreaPedido, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jLabel45, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanCreaPedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtNotePedidoCli, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanCreaPedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel43, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jCombProdPedidos, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel59)
                .addGap(0, 0, 0)
                .addGroup(jPanCreaPedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButGuardaPedidodia, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanCreaPedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel46, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane15, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                .addGroup(jPanCreaPedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane35, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanCreaPedidoLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel31)))
                .addGap(100, 100, 100))
        );

        jPanConsulPed.setBackground(new java.awt.Color(255, 255, 255));
        jPanConsulPed.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Consultar Pedido", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N
        jPanConsulPed.setPreferredSize(new java.awt.Dimension(1600, 790));

        jLabel18.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel18.setText("Tabla de Resultados");

        jComPedBusqCli.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jComPedBusqCli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComPedBusqCliActionPerformed(evt);
            }
        });

        jLabel60.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel60.setText("TIPO DE BUSQUEDA");

        jCombOpcBusqPedido.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCombOpcBusqPedido.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "CLIENTE", "FECHA", "LAPSO FECHAS", "CLIENTE+FECHA", "CLIENTE+LAPSO FECHAS" }));
        jCombOpcBusqPedido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCombOpcBusqPedidoActionPerformed(evt);
            }
        });

        jLabel61.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel61.setText("ELIJA CLIENTE:");

        jDateChoB1Cli.setDateFormatString("dd/MM/yyyy");
        jDateChoB1Cli.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N

        jLabel62.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel62.setText("ELIJA FECHA 2:");

        jDate2BusqCli.setDateFormatString("dd/MM/yyyy");
        jDate2BusqCli.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N

        jScrollPane3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jTablefiltrosBusq.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTablefiltrosBusq.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTablefiltrosBusq.setComponentPopupMenu(jPopPedidosDia);
        jTablefiltrosBusq.setRowHeight(23);
        jTablefiltrosBusq.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTablefiltrosBusqMousePressed(evt);
            }
        });
        jScrollPane3.setViewportView(jTablefiltrosBusq);

        jLabel63.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel63.setText("ELIJA FECHA:");

        jButton3.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/search32pxcolor.png"))); // NOI18N
        jButton3.setText("BUSCAR");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLCountHistorP.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        jLCountHistorP.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel157.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel157.setText("Importe: $");

        jLabtotaldineroHistorPEd.setFont(new java.awt.Font("Tahoma", 3, 16)); // NOI18N
        jLabtotaldineroHistorPEd.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        javax.swing.GroupLayout jPanConsulPedLayout = new javax.swing.GroupLayout(jPanConsulPed);
        jPanConsulPed.setLayout(jPanConsulPedLayout);
        jPanConsulPedLayout.setHorizontalGroup(
            jPanConsulPedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanConsulPedLayout.createSequentialGroup()
                .addGroup(jPanConsulPedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanConsulPedLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(jPanConsulPedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 1503, Short.MAX_VALUE)
                            .addGroup(jPanConsulPedLayout.createSequentialGroup()
                                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLCountHistorP, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSeparator7, javax.swing.GroupLayout.PREFERRED_SIZE, 460, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(287, 287, 287)
                                .addComponent(jLabel157)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabtotaldineroHistorPEd, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanConsulPedLayout.createSequentialGroup()
                        .addGroup(jPanConsulPedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanConsulPedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(jPanConsulPedLayout.createSequentialGroup()
                                    .addGap(26, 26, 26)
                                    .addComponent(jLabel61, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jComPedBusqCli, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanConsulPedLayout.createSequentialGroup()
                                    .addGap(16, 16, 16)
                                    .addComponent(jLabel60, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jCombOpcBusqPedido, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanConsulPedLayout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addComponent(jLabel63, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jDateChoB1Cli, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanConsulPedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel62, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanConsulPedLayout.createSequentialGroup()
                                        .addGap(120, 120, 120)
                                        .addComponent(jDate2BusqCli, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(55, 55, 55))
        );
        jPanConsulPedLayout.setVerticalGroup(
            jPanConsulPedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanConsulPedLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanConsulPedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanConsulPedLayout.createSequentialGroup()
                        .addGroup(jPanConsulPedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCombOpcBusqPedido, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel60))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanConsulPedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComPedBusqCli, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel61))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanConsulPedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jDateChoB1Cli, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel62, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jDate2BusqCli, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanConsulPedLayout.createSequentialGroup()
                                .addGap(9, 9, 9)
                                .addComponent(jLabel63))))
                    .addGroup(jPanConsulPedLayout.createSequentialGroup()
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)))
                .addGroup(jPanConsulPedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanConsulPedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jSeparator7, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel157)
                        .addComponent(jLabtotaldineroHistorPEd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLCountHistorP, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 485, Short.MAX_VALUE)
                .addGap(55, 55, 55))
        );

        jLayeredPanePedidos.setLayer(jPanCreaPedido, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPanePedidos.setLayer(jPanConsulPed, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jLayeredPanePedidosLayout = new javax.swing.GroupLayout(jLayeredPanePedidos);
        jLayeredPanePedidos.setLayout(jLayeredPanePedidosLayout);
        jLayeredPanePedidosLayout.setHorizontalGroup(
            jLayeredPanePedidosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanCreaPedido, javax.swing.GroupLayout.DEFAULT_SIZE, 1595, Short.MAX_VALUE)
            .addGroup(jLayeredPanePedidosLayout.createSequentialGroup()
                .addComponent(jPanConsulPed, javax.swing.GroupLayout.DEFAULT_SIZE, 1590, Short.MAX_VALUE)
                .addGap(5, 5, 5))
        );
        jLayeredPanePedidosLayout.setVerticalGroup(
            jLayeredPanePedidosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanCreaPedido, javax.swing.GroupLayout.DEFAULT_SIZE, 731, Short.MAX_VALUE)
            .addComponent(jPanConsulPed, javax.swing.GroupLayout.DEFAULT_SIZE, 731, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPPedidosHistLayout = new javax.swing.GroupLayout(jPPedidosHist);
        jPPedidosHist.setLayout(jPPedidosHistLayout);
        jPPedidosHistLayout.setHorizontalGroup(
            jPPedidosHistLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPPedidosHistLayout.createSequentialGroup()
                .addGap(90, 90, 90)
                .addGroup(jPPedidosHistLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPPedidosHistLayout.createSequentialGroup()
                        .addGap(120, 120, 120)
                        .addComponent(jRadioCreaPedido, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(20, 20, 20)
                .addComponent(jRadioConsulPedido, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jLayeredPanePedidos, javax.swing.GroupLayout.DEFAULT_SIZE, 1595, Short.MAX_VALUE)
        );
        jPPedidosHistLayout.setVerticalGroup(
            jPPedidosHistLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPPedidosHistLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPPedidosHistLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jRadioCreaPedido, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jRadioConsulPedido, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addComponent(jLayeredPanePedidos, javax.swing.GroupLayout.DEFAULT_SIZE, 731, Short.MAX_VALUE)
                .addGap(25, 25, 25))
        );

        paneAltas.addTab("    PEDIDOS    ", jPPedidosHist);

        proveedorJP.setPreferredSize(new java.awt.Dimension(1600, 820));

        jLabel3.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel3.setText("ELIJA ACCION:");

        buttonGProveedores.add(jRadBCompraProve);
        jRadBCompraProve.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jRadBCompraProve.setSelected(true);
        jRadBCompraProve.setText("COMPRA");
        jRadBCompraProve.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadBCompraProveActionPerformed(evt);
            }
        });

        buttonGProveedores.add(jRadBPrestamoProv);
        jRadBPrestamoProv.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jRadBPrestamoProv.setText("PRESTAMO");
        jRadBPrestamoProv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadBPrestamoProvActionPerformed(evt);
            }
        });

        buttonGProveedores.add(jRadBConsultaProv);
        jRadBConsultaProv.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jRadBConsultaProv.setText("CONSULTAR OPERACIONES");
        jRadBConsultaProv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadBConsultaProvActionPerformed(evt);
            }
        });

        jLayeredPane1.setBackground(new java.awt.Color(0, 102, 102));
        jLayeredPane1.setPreferredSize(new java.awt.Dimension(1600, 790));

        jPanCompraProoved.setBackground(new java.awt.Color(255, 255, 255));
        jPanCompraProoved.setPreferredSize(new java.awt.Dimension(1600, 790));

        jLabel22.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel22.setText("ELIJA PROVEEDOR:");

        jCElijaProovedor.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCElijaProovedor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jCElijaProovedorKeyReleased(evt);
            }
        });

        jCombProductProv.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCombProductProv.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " " }));
        jCombProductProv.setName(""); // NOI18N
        jCombProductProv.setNextFocusableComponent(txtCantidadCompra);
        jCombProductProv.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jCombProductProvKeyReleased(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel2.setText("CAJAS");

        jLabel8.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel8.setText("FECHA:");

        txtPrecCompraProv.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtPrecCompraProv.setNextFocusableComponent(txtImportComp);
        txtPrecCompraProv.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPrecCompraProvFocusLost(evt);
            }
        });
        txtPrecCompraProv.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPrecCompraProvKeyReleased(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton1.setText("AGREGAR >>");
        jButton1.setNextFocusableComponent(txtCantidadCompra);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel10.setText("TOTALES DEL DIA:");

        jTabVistaComprasDia.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTabVistaComprasDia.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "PROVEEDOR", "PRIM", "SEG", "PRIM_R", "SEG_R", "BOLA_P", "BOLA_S", "TERCERA", "TOTAL CAJAS", "IMPORTE"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTabVistaComprasDia.setCellSelectionEnabled(true);
        jTabVistaComprasDia.setComponentPopupMenu(jPopCompraProveedor);
        jTabVistaComprasDia.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jTabVistaComprasDia.setEditingColumn(0);
        jTabVistaComprasDia.setEditingRow(0);
        jTabVistaComprasDia.setRowHeight(25);
        jTabVistaComprasDia.setSelectionBackground(new java.awt.Color(153, 255, 153));
        jTabVistaComprasDia.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTabVistaComprasDiaMousePressed(evt);
            }
        });
        jScrollPane6.setViewportView(jTabVistaComprasDia);
        if (jTabVistaComprasDia.getColumnModel().getColumnCount() > 0) {
            jTabVistaComprasDia.getColumnModel().getColumn(0).setMinWidth(50);
            jTabVistaComprasDia.getColumnModel().getColumn(0).setPreferredWidth(50);
            jTabVistaComprasDia.getColumnModel().getColumn(0).setMaxWidth(50);
            jTabVistaComprasDia.getColumnModel().getColumn(1).setMinWidth(170);
            jTabVistaComprasDia.getColumnModel().getColumn(1).setPreferredWidth(170);
            jTabVistaComprasDia.getColumnModel().getColumn(1).setMaxWidth(250);
            jTabVistaComprasDia.getColumnModel().getColumn(2).setMinWidth(110);
            jTabVistaComprasDia.getColumnModel().getColumn(2).setPreferredWidth(110);
            jTabVistaComprasDia.getColumnModel().getColumn(2).setMaxWidth(150);
            jTabVistaComprasDia.getColumnModel().getColumn(3).setMinWidth(110);
            jTabVistaComprasDia.getColumnModel().getColumn(3).setPreferredWidth(110);
            jTabVistaComprasDia.getColumnModel().getColumn(3).setMaxWidth(150);
            jTabVistaComprasDia.getColumnModel().getColumn(4).setMinWidth(110);
            jTabVistaComprasDia.getColumnModel().getColumn(4).setPreferredWidth(110);
            jTabVistaComprasDia.getColumnModel().getColumn(4).setMaxWidth(150);
            jTabVistaComprasDia.getColumnModel().getColumn(5).setMinWidth(110);
            jTabVistaComprasDia.getColumnModel().getColumn(5).setPreferredWidth(110);
            jTabVistaComprasDia.getColumnModel().getColumn(5).setMaxWidth(150);
            jTabVistaComprasDia.getColumnModel().getColumn(6).setMinWidth(110);
            jTabVistaComprasDia.getColumnModel().getColumn(6).setPreferredWidth(110);
            jTabVistaComprasDia.getColumnModel().getColumn(6).setMaxWidth(150);
            jTabVistaComprasDia.getColumnModel().getColumn(7).setMinWidth(110);
            jTabVistaComprasDia.getColumnModel().getColumn(7).setPreferredWidth(110);
            jTabVistaComprasDia.getColumnModel().getColumn(7).setMaxWidth(150);
            jTabVistaComprasDia.getColumnModel().getColumn(8).setMinWidth(110);
            jTabVistaComprasDia.getColumnModel().getColumn(8).setPreferredWidth(110);
            jTabVistaComprasDia.getColumnModel().getColumn(8).setMaxWidth(150);
            jTabVistaComprasDia.getColumnModel().getColumn(10).setHeaderValue("IMPORTE");
        }

        jLabel41.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel41.setText("CANTIDAD:");

        txtCantidadCompra.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtCantidadCompra.setNextFocusableComponent(txtPrecCompraProv);
        txtCantidadCompra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCantidadCompraActionPerformed(evt);
            }
        });
        txtCantidadCompra.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCantidadCompraKeyReleased(evt);
            }
        });

        jLayeredPane3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanSubastaOption.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel57.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel57.setText("CAMIONETA:");
        jPanSubastaOption.add(jLabel57, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 80, 40));

        txtCamSubastaCompra.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtCamSubastaCompra.setNextFocusableComponent(jCombProductProv);
        jPanSubastaOption.add(txtCamSubastaCompra, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 0, 270, 32));

        jLayeredPane3.add(jPanSubastaOption, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 380, 40));

        jPanClientOption.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabAdeudaProoved.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabAdeudaProoved.setText("PRESTAMO PENDIENTE");
        jPanClientOption.add(jLabAdeudaProoved, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 230, 50));

        jLayeredPane3.add(jPanClientOption, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 250, 40));

        jLabel35.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel35.setText("NOTA:");

        jDateFechCompraProv.setDateFormatString("dd/MM/yyyy");
        jDateFechCompraProv.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N

        jLabel40.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel40.setText("IMPORTE:");

        txtNotaCompra.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtNotaCompra.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNotaCompraKeyReleased(evt);
            }
        });

        jButton13.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton13.setText("GUARDAR");
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        txtImportComp.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtImportComp.setNextFocusableComponent(txtNotaCompra);
        txtImportComp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtImportCompKeyReleased(evt);
            }
        });

        jLabel48.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel48.setText("PRECIO $:");

        jLabel23.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel23.setText("MERCANCIA:");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel4.setText("TOTAL $:");

        jLabel26.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel26.setText("DETALLE DE COMPRA");

        jTextField1.setFont(new java.awt.Font("Tahoma", 3, 15)); // NOI18N
        jTextField1.setText("0.00");

        jCheckBox1.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jCheckBox1.setText("Carga Mayoristas");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        jTabDetallecompraAll.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTabDetallecompraAll.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "#Compra", "id", "Tipo", "Cantidad", "Costo", "Importe"
            }
        ));
        jTabDetallecompraAll.setComponentPopupMenu(jPopupMenActualizaCompra);
        jTabDetallecompraAll.setRowHeight(25);
        jTabDetallecompraAll.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTabDetallecompraAllMousePressed(evt);
            }
        });
        jScrollPane4.setViewportView(jTabDetallecompraAll);
        jTabDetallecompraAll.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        jScrollPane34.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jTabSumTotales.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jTabSumTotales.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "PRIM", "SEG", "PRIM_R", "SEG_R", "BOLA_P", "BOLA_S", "TERCERA", "TOTAL CAJAS", "IMPORTE TOTAL"
            }
        ));
        jTabSumTotales.setRowHeight(25);
        jScrollPane34.setViewportView(jTabSumTotales);
        if (jTabSumTotales.getColumnModel().getColumnCount() > 0) {
            jTabSumTotales.getColumnModel().getColumn(0).setMinWidth(110);
            jTabSumTotales.getColumnModel().getColumn(0).setPreferredWidth(110);
            jTabSumTotales.getColumnModel().getColumn(0).setMaxWidth(150);
            jTabSumTotales.getColumnModel().getColumn(1).setMinWidth(110);
            jTabSumTotales.getColumnModel().getColumn(1).setPreferredWidth(110);
            jTabSumTotales.getColumnModel().getColumn(1).setMaxWidth(150);
            jTabSumTotales.getColumnModel().getColumn(2).setMinWidth(110);
            jTabSumTotales.getColumnModel().getColumn(2).setPreferredWidth(110);
            jTabSumTotales.getColumnModel().getColumn(2).setMaxWidth(150);
            jTabSumTotales.getColumnModel().getColumn(3).setMinWidth(110);
            jTabSumTotales.getColumnModel().getColumn(3).setPreferredWidth(110);
            jTabSumTotales.getColumnModel().getColumn(3).setMaxWidth(150);
            jTabSumTotales.getColumnModel().getColumn(4).setMinWidth(110);
            jTabSumTotales.getColumnModel().getColumn(4).setPreferredWidth(110);
            jTabSumTotales.getColumnModel().getColumn(4).setMaxWidth(150);
            jTabSumTotales.getColumnModel().getColumn(5).setMinWidth(110);
            jTabSumTotales.getColumnModel().getColumn(5).setPreferredWidth(110);
            jTabSumTotales.getColumnModel().getColumn(5).setMaxWidth(150);
            jTabSumTotales.getColumnModel().getColumn(6).setMinWidth(110);
            jTabSumTotales.getColumnModel().getColumn(6).setPreferredWidth(110);
            jTabSumTotales.getColumnModel().getColumn(6).setMaxWidth(150);
        }

        jLabel11.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel11.setText("COMPRAS DEL DIA:");

        jCheckbpAGADO.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        jCheckbpAGADO.setText("PAGADO");
        jCheckbpAGADO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckbpAGADOActionPerformed(evt);
            }
        });
        jCheckbpAGADO.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jCheckbpAGADOKeyReleased(evt);
            }
        });

        jButton19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/search32pxcolor.png"))); // NOI18N
        jButton19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton19ActionPerformed(evt);
            }
        });

        jLabel51.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel51.setText("1");
        jLabel51.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel66.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel66.setText("jLabel66");
        jLabel66.setAutoscrolls(true);
        jLabel66.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jButton21.setBackground(new java.awt.Color(191, 252, 195));
        jButton21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/database.png"))); // NOI18N
        jButton21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton21ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanCompraProovedLayout = new javax.swing.GroupLayout(jPanCompraProoved);
        jPanCompraProoved.setLayout(jPanCompraProovedLayout);
        jPanCompraProovedLayout.setHorizontalGroup(
            jPanCompraProovedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanCompraProovedLayout.createSequentialGroup()
                .addGroup(jPanCompraProovedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanCompraProovedLayout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addComponent(jLabel51, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel66, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanCompraProovedLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanCompraProovedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanCompraProovedLayout.createSequentialGroup()
                                .addGap(114, 114, 114)
                                .addComponent(jCombProductProv, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanCompraProovedLayout.createSequentialGroup()
                                .addGroup(jPanCompraProovedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel48, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel40, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel35, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanCompraProovedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanCompraProovedLayout.createSequentialGroup()
                                        .addComponent(txtCantidadCompra, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanCompraProovedLayout.createSequentialGroup()
                                        .addGroup(jPanCompraProovedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtImportComp, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtPrecCompraProv, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(txtNotaCompra, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addGap(18, 18, 18)
                .addGroup(jPanCompraProovedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanCompraProovedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(jPanCompraProovedLayout.createSequentialGroup()
                            .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jCheckbpAGADO, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 691, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(471, 471, 471))
            .addGroup(jPanCompraProovedLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanCompraProovedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanCompraProovedLayout.createSequentialGroup()
                        .addComponent(jCElijaProovedor, javax.swing.GroupLayout.PREFERRED_SIZE, 303, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton21, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jCheckBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLayeredPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 366, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jDateFechCompraProv, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton19)
                .addGap(384, 384, 384))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanCompraProovedLayout.createSequentialGroup()
                .addGroup(jPanCompraProovedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanCompraProovedLayout.createSequentialGroup()
                        .addGap(115, 115, 115)
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane34))
                    .addGroup(jPanCompraProovedLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanCompraProovedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanCompraProovedLayout.createSequentialGroup()
                                .addGap(45, 45, 45)
                                .addComponent(jScrollPane6))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanCompraProovedLayout.createSequentialGroup()
                                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 1064, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(336, 336, 336))
        );
        jPanCompraProovedLayout.setVerticalGroup(
            jPanCompraProovedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanCompraProovedLayout.createSequentialGroup()
                .addGroup(jPanCompraProovedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanCompraProovedLayout.createSequentialGroup()
                        .addComponent(jCheckBox1)
                        .addGap(3, 3, 3)
                        .addGroup(jPanCompraProovedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanCompraProovedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jButton19, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jDateFechCompraProv, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(jPanCompraProovedLayout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addGroup(jPanCompraProovedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCElijaProovedor, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLayeredPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton21))
                        .addGroup(jPanCompraProovedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel51)
                            .addComponent(jLabel26)
                            .addComponent(jLabel66))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanCompraProovedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanCompraProovedLayout.createSequentialGroup()
                        .addGroup(jPanCompraProovedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jCombProductProv, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, 0)
                        .addGroup(jPanCompraProovedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtCantidadCompra, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanCompraProovedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanCompraProovedLayout.createSequentialGroup()
                                .addGroup(jPanCompraProovedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtPrecCompraProv, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel48, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, 0)
                                .addGroup(jPanCompraProovedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtImportComp, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanCompraProovedLayout.createSequentialGroup()
                                .addGap(24, 24, 24)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanCompraProovedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNotaCompra, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCheckbpAGADO)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(7, 7, 7)
                .addGroup(jPanCompraProovedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addGroup(jPanCompraProovedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane34, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addGap(50, 50, 50))
        );

        jPanBusquedaPrest.setBackground(new java.awt.Color(255, 255, 255));
        jPanBusquedaPrest.setMinimumSize(new java.awt.Dimension(1600, 820));
        jPanBusquedaPrest.setPreferredSize(new java.awt.Dimension(1600, 790));

        jTabbedPane2.setBackground(new java.awt.Color(0, 255, 204));
        jTabbedPane2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jTabbedPane2.setPreferredSize(new java.awt.Dimension(1600, 790));

        jPanInternoBusquedaPrest.setBackground(new java.awt.Color(255, 255, 255));
        jPanInternoBusquedaPrest.setPreferredSize(new java.awt.Dimension(1600, 668));

        jDaTFechPrest1.setDateFormatString("dd/MM/yyyy");
        jDaTFechPrest1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jLabel42.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N
        jLabel42.setText("Tabla de Resultados:");

        jCombBProvBusqPrest.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCombBProvBusqPrest.setMaximumRowCount(25);

        jLabel47.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel47.setText("ELIJA PROVEEDOR:");

        jComBusPrestamo.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jComBusPrestamo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "CLIENTE", "FECHA", "LAPSO FECHAS", "CLIENTE+FECHA", "CLIENTE+LAPSO FECHAS" }));
        jComBusPrestamo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComBusPrestamoActionPerformed(evt);
            }
        });

        jLabel69.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel69.setText("TIPO DE BUSQUEDA:");

        jLabel70.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel70.setText("ELIJA FECHA 2:");

        jDaTFechPrest2.setDateFormatString("dd/MM/yyyy");
        jDaTFechPrest2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jButton5.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/search32pxcolor.png"))); // NOI18N
        jButton5.setText("BUSCAR");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jTabBusqPrestProv.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jTabBusqPrestProv.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTabBusqPrestProv.setComponentPopupMenu(jPMPrestaProvPays);
        jTabBusqPrestProv.setRowHeight(32);
        jTabBusqPrestProv.setRowMargin(2);
        jTabBusqPrestProv.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTabBusqPrestProvMousePressed(evt);
            }
        });
        jScrollPane14.setViewportView(jTabBusqPrestProv);

        jLabel99.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel99.setText("ELIJA FECHA:");

        jLabel155.setFont(new java.awt.Font("Tahoma", 3, 16)); // NOI18N
        jLabel155.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel158.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel158.setText("Total : $");

        jLabel169.setFont(new java.awt.Font("Tahoma", 3, 16)); // NOI18N
        jLabel169.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        javax.swing.GroupLayout jPanInternoBusquedaPrestLayout = new javax.swing.GroupLayout(jPanInternoBusquedaPrest);
        jPanInternoBusquedaPrest.setLayout(jPanInternoBusquedaPrestLayout);
        jPanInternoBusquedaPrestLayout.setHorizontalGroup(
            jPanInternoBusquedaPrestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanInternoBusquedaPrestLayout.createSequentialGroup()
                .addGroup(jPanInternoBusquedaPrestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanInternoBusquedaPrestLayout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(jLabel69, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jComBusPrestamo, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanInternoBusquedaPrestLayout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(jLabel47, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jCombBProvBusqPrest, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanInternoBusquedaPrestLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(jPanInternoBusquedaPrestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanInternoBusquedaPrestLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jLabel99, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(jDaTFechPrest1, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(20, 20, 20)
                                .addComponent(jLabel70, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(jDaTFechPrest2, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanInternoBusquedaPrestLayout.createSequentialGroup()
                                .addGroup(jPanInternoBusquedaPrestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel42, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanInternoBusquedaPrestLayout.createSequentialGroup()
                                        .addGap(126, 126, 126)
                                        .addComponent(jLabel155, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(14, 14, 14)
                                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(414, 414, 414)
                                .addComponent(jLabel158)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel169, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane14))))
                .addGap(416, 416, 416))
        );
        jPanInternoBusquedaPrestLayout.setVerticalGroup(
            jPanInternoBusquedaPrestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanInternoBusquedaPrestLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanInternoBusquedaPrestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel69, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComBusPrestamo, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanInternoBusquedaPrestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel47, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCombBProvBusqPrest, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanInternoBusquedaPrestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanInternoBusquedaPrestLayout.createSequentialGroup()
                        .addGroup(jPanInternoBusquedaPrestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel99, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jDaTFechPrest1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel70, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jDaTFechPrest2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanInternoBusquedaPrestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel42, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanInternoBusquedaPrestLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jLabel155))
                            .addGroup(jPanInternoBusquedaPrestLayout.createSequentialGroup()
                                .addGap(16, 16, 16)
                                .addGroup(jPanInternoBusquedaPrestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanInternoBusquedaPrestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel158)
                                        .addComponent(jLabel169))
                                    .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane14)
                .addGap(50, 50, 50))
        );

        jTabbedPane2.addTab("PRESTAMOS                   ", jPanInternoBusquedaPrest);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        jComBusCompra.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jComBusCompra.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "CLIENTE", "MAYORISTA", "MAYORISTA+FECHA", "FECHA", "LAPSO FECHAS", "CLIENTE+FECHA", "CLIENTE+LAPSO FECHAS" }));
        jComBusCompra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComBusCompraActionPerformed(evt);
            }
        });

        jCombBProvBusqCompra.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCombBProvBusqCompra.setMaximumRowCount(25);

        jDaTFechComp1.setDateFormatString("dd/MM/yyyy");
        jDaTFechComp1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jLabel72.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel72.setText("ELIJA FECHA:");

        jLabel49.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel49.setText("ELIJA PROVEEDOR:");

        jLabel73.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel73.setText("TIPO DE BUSQUEDA:");

        jLabel50.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N
        jLabel50.setText("Tabla de Resultados:");

        jLabel74.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel74.setText("ELIJA FECHA 2:");

        jDaTFechCompraProv2.setDateFormatString("dd/MM/yyyy");
        jDaTFechCompraProv2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jButton14.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jButton14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/search32pxcolor.png"))); // NOI18N
        jButton14.setText("BUSCAR");
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        jTabBusqCompraProv1.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jTabBusqCompraProv1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTabBusqCompraProv1.setComponentPopupMenu(jPopFILTROCOMPRAS);
        jTabBusqCompraProv1.setRowHeight(32);
        jTabBusqCompraProv1.setRowMargin(2);
        jTabBusqCompraProv1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTabBusqCompraProv1MousePressed(evt);
            }
        });
        jScrollPane16.setViewportView(jTabBusqCompraProv1);

        jLabel171.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel171.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel172.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel172.setText("Total: $");

        jLabel173.setFont(new java.awt.Font("Tahoma", 3, 16)); // NOI18N
        jLabel173.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(80, 80, 80)
                .addComponent(jLabel73, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jComBusCompra, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(80, 80, 80)
                .addComponent(jLabel49, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jCombBProvBusqCompra, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(60, 60, 60)
                        .addComponent(jLabel72, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(130, 130, 130)
                        .addComponent(jLabel171, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel50, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jDaTFechComp1, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addComponent(jLabel74, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jSeparator9, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addComponent(jDaTFechCompraProv2, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(70, 70, 70)
                .addComponent(jLabel172)
                .addGap(23, 23, 23)
                .addComponent(jLabel173, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(jScrollPane16)
                .addGap(365, 365, 365))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel73, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComBusCompra, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel49, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCombBProvBusqCompra, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel72, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jLabel171, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel50, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jDaTFechComp1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel74, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(20, 20, 20)
                        .addComponent(jSeparator9, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jDaTFechCompraProv2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel172, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel173, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane16)
                .addGap(55, 55, 55))
        );

        jTabbedPane2.addTab("COMPRAS", jPanel4);

        javax.swing.GroupLayout jPanBusquedaPrestLayout = new javax.swing.GroupLayout(jPanBusquedaPrest);
        jPanBusquedaPrest.setLayout(jPanBusquedaPrestLayout);
        jPanBusquedaPrestLayout.setHorizontalGroup(
            jPanBusquedaPrestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanBusquedaPrestLayout.setVerticalGroup(
            jPanBusquedaPrestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanBusquedaPrestLayout.createSequentialGroup()
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(30, 30, 30))
        );

        jPanPrestamoProovedor.setBackground(new java.awt.Color(255, 255, 255));
        jPanPrestamoProovedor.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Prestamo a Proveedor", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 12))); // NOI18N
        jPanPrestamoProovedor.setPreferredSize(new java.awt.Dimension(1600, 790));

        jLabel33.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel33.setText("DETALLE:");

        jComBPrestamosProv.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jComBPrestamosProv.setNextFocusableComponent(jComBProveedor);
        jComBPrestamosProv.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jComBPrestamosProvKeyReleased(evt);
            }
        });

        jLabel34.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel34.setText("Prestamo de:");

        jComBProveedor.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jComBProveedor.setNextFocusableComponent(txtCantPres);
        jComBProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComBProveedorActionPerformed(evt);
            }
        });

        txtCantPres.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtCantPres.setNextFocusableComponent(txtPrecProov);
        txtCantPres.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCantPresKeyReleased(evt);
            }
        });

        jLabBNumerador.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabBNumerador.setText("CAJAS");

        jLabel37.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel37.setText("FECHA:");

        txtPrecProov.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtPrecProov.setNextFocusableComponent(txtImportPres);
        txtPrecProov.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPrecProovKeyReleased(evt);
            }
        });

        jButton7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton7.setText("AGREGAR >>");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jLabel38.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel38.setText("NOTA:");

        jDatFechaPrest.setDateFormatString("dd/MM/yyyy");
        jDatFechaPrest.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N

        textANotaPrestProv.setColumns(20);
        textANotaPrestProv.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        textANotaPrestProv.setRows(5);
        textANotaPrestProv.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                textANotaPrestProvKeyPressed(evt);
            }
        });
        jScrollPane8.setViewportView(textANotaPrestProv);

        jLabel36.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel36.setText("CANTIDAD:");

        jButton4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton4.setText("GUARDAR");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel64.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel64.setText("COSTO:");

        jLabel65.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel65.setText("IMPORTE:");

        txtImportPres.setEditable(false);
        txtImportPres.setBackground(new java.awt.Color(255, 255, 255));
        txtImportPres.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtImportPres.setNextFocusableComponent(textANotaPrestProv);
        txtImportPres.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtImportPresKeyPressed(evt);
            }
        });

        jLabel67.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel67.setText("CREDITOS REALIZADOS:");

        jTabVistaPresta.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTabVistaPresta.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTabVistaPresta.setRowHeight(23);
        jTabVistaPresta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTabVistaPrestaMousePressed(evt);
            }
        });
        jScrollPane13.setViewportView(jTabVistaPresta);

        jLabel68.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel68.setText("ELIJA PROVEEDOR:");

        jTabdetPrestamoProv.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTabdetPrestamoProv.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "# Prestamo", "Id", "Tipo", "Cantidad", "Costo", "Importe"
            }
        ));
        jTabdetPrestamoProv.setRowHeight(25);
        jScrollPane1.setViewportView(jTabdetPrestamoProv);

        jLabel71.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel71.setText("TOTAL :");

        jTextField2.setFont(new java.awt.Font("Tahoma", 3, 16)); // NOI18N
        jTextField2.setText("0.00");

        javax.swing.GroupLayout jPanPrestamoProovedorLayout = new javax.swing.GroupLayout(jPanPrestamoProovedor);
        jPanPrestamoProovedor.setLayout(jPanPrestamoProovedorLayout);
        jPanPrestamoProovedorLayout.setHorizontalGroup(
            jPanPrestamoProovedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanPrestamoProovedorLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel68, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jComBPrestamosProv, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(160, 160, 160)
                .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jDatFechaPrest, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanPrestamoProovedorLayout.createSequentialGroup()
                .addGap(54, 54, 54)
                .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jComBProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(130, 130, 130)
                .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanPrestamoProovedorLayout.createSequentialGroup()
                .addGap(64, 64, 64)
                .addGroup(jPanPrestamoProovedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel64, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel65, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanPrestamoProovedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanPrestamoProovedorLayout.createSequentialGroup()
                        .addComponent(txtCantPres, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jLabBNumerador, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanPrestamoProovedorLayout.createSequentialGroup()
                        .addComponent(txtPrecProov, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(50, 50, 50)
                        .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtImportPres, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanPrestamoProovedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanPrestamoProovedorLayout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(360, 360, 360)
                        .addComponent(jLabel71)
                        .addGap(13, 13, 13)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(384, 384, 384))
            .addGroup(jPanPrestamoProovedorLayout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(jLabel67, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jSeparator8, javax.swing.GroupLayout.PREFERRED_SIZE, 1020, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanPrestamoProovedorLayout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(jScrollPane13)
                .addGap(274, 274, 274))
        );
        jPanPrestamoProovedorLayout.setVerticalGroup(
            jPanPrestamoProovedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanPrestamoProovedorLayout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addGroup(jPanPrestamoProovedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel68, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComBPrestamosProv, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jDatFechaPrest, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanPrestamoProovedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComBProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanPrestamoProovedorLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanPrestamoProovedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanPrestamoProovedorLayout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addGroup(jPanPrestamoProovedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanPrestamoProovedorLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jLabel71))
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanPrestamoProovedorLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(jPanPrestamoProovedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanPrestamoProovedorLayout.createSequentialGroup()
                                .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(jLabel64, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(jLabel65, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanPrestamoProovedorLayout.createSequentialGroup()
                                .addGroup(jPanPrestamoProovedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtCantPres, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabBNumerador, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanPrestamoProovedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanPrestamoProovedorLayout.createSequentialGroup()
                                        .addGap(10, 10, 10)
                                        .addComponent(txtPrecProov, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(10, 10, 10)
                                .addComponent(txtImportPres, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(10, 10, 10)
                .addGroup(jPanPrestamoProovedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel67, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addGap(55, 55, 55))
        );

        jLayeredPane1.setLayer(jPanCompraProoved, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(jPanBusquedaPrest, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(jPanPrestamoProovedor, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jLayeredPane1Layout = new javax.swing.GroupLayout(jLayeredPane1);
        jLayeredPane1.setLayout(jLayeredPane1Layout);
        jLayeredPane1Layout.setHorizontalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanBusquedaPrest, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanCompraProoved, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanPrestamoProovedor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jLayeredPane1Layout.setVerticalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanBusquedaPrest, javax.swing.GroupLayout.DEFAULT_SIZE, 820, Short.MAX_VALUE)
            .addComponent(jPanCompraProoved, javax.swing.GroupLayout.DEFAULT_SIZE, 820, Short.MAX_VALUE)
            .addGroup(jLayeredPane1Layout.createSequentialGroup()
                .addComponent(jPanPrestamoProovedor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(30, 30, 30))
        );

        javax.swing.GroupLayout proveedorJPLayout = new javax.swing.GroupLayout(proveedorJP);
        proveedorJP.setLayout(proveedorJPLayout);
        proveedorJPLayout.setHorizontalGroup(
            proveedorJPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLayeredPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(proveedorJPLayout.createSequentialGroup()
                .addGap(92, 92, 92)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(proveedorJPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(proveedorJPLayout.createSequentialGroup()
                        .addGap(166, 166, 166)
                        .addComponent(jRadBPrestamoProv, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40)
                        .addComponent(jRadBConsultaProv, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jRadBCompraProve, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        proveedorJPLayout.setVerticalGroup(
            proveedorJPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(proveedorJPLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(proveedorJPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jRadBPrestamoProv, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jRadBConsultaProv, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jRadBCompraProve, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(proveedorJPLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLayeredPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 820, Short.MAX_VALUE))))
        );

        paneAltas.addTab("    PROVEEDORES    ", proveedorJP);

        btnGFletesPane.add(jRdCreaFletes);
        jRdCreaFletes.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jRdCreaFletes.setText("CREAR FLETES");
        jRdCreaFletes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRdCreaFletesActionPerformed(evt);
            }
        });

        btnGFletesPane.add(jRHistorFletes);
        jRHistorFletes.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jRHistorFletes.setText("HISTORIAL FLETES");
        jRHistorFletes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRHistorFletesActionPerformed(evt);
            }
        });

        jLayerFletes.setBackground(new java.awt.Color(240, 240, 240));
        jLayerFletes.setOpaque(true);
        jLayerFletes.setPreferredSize(new java.awt.Dimension(1600, 800));

        jPanCreaFletes.setBackground(new java.awt.Color(255, 255, 255));
        jPanCreaFletes.setPreferredSize(new java.awt.Dimension(1601, 790));

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel5.setText("Fletes del dia:");

        jCBAltasFletes.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jCBAltasFletes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBAltasFletesActionPerformed(evt);
            }
        });

        jLabel86.setFont(new java.awt.Font("Calibri", 0, 15)); // NOI18N
        jLabel86.setText("FECHA:");

        jDFechCreaFlete.setDateFormatString("dd/MM/yyyy");
        jDFechCreaFlete.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N

        jPanAdminist1.setBackground(new java.awt.Color(255, 255, 255));
        jPanAdminist1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Crear Flete", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 13))); // NOI18N

        jLabel14.setFont(new java.awt.Font("Calibri", 0, 15)); // NOI18N
        jLabel14.setText("COSTO $ :");

        jLabel87.setFont(new java.awt.Font("Calibri", 0, 15)); // NOI18N
        jLabel87.setText("UNIDAD:");

        txtCostoFlete.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N

        txtUnidFlete.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N

        jButAltasElimina1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButAltasElimina1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/delete32px.png"))); // NOI18N
        jButAltasElimina1.setText("Eliminar");
        jButAltasElimina1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButAltasElimina1ActionPerformed(evt);
            }
        });

        txtNotaFlete.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        txtNotaFlete.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNotaFleteKeyReleased(evt);
            }
        });

        jButFleteGuardar.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButFleteGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/add-BN.png"))); // NOI18N
        jButFleteGuardar.setText("    Guardar");
        jButFleteGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButFleteGuardarActionPerformed(evt);
            }
        });

        jButAltasActualiza1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButAltasActualiza1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/Actualizar.png"))); // NOI18N
        jButAltasActualiza1.setText("    Actualizar");
        jButAltasActualiza1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButAltasActualiza1ActionPerformed(evt);
            }
        });

        jLabel88.setFont(new java.awt.Font("Calibri", 0, 15)); // NOI18N
        jLabel88.setText("M.N.");

        jLabel90.setFont(new java.awt.Font("Calibri", 0, 15)); // NOI18N
        jLabel90.setText("CHOFER:");

        txtChoferFlete.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N

        txtFolioFlete.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        txtFolioFlete.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtFolioFleteFocusLost(evt);
            }
        });

        jLabel89.setFont(new java.awt.Font("Calibri", 0, 15)); // NOI18N
        jLabel89.setText("FOLIO:");

        jLabLetreroFletes.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabLetreroFletes.setForeground(new java.awt.Color(204, 0, 0));
        jLabLetreroFletes.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabLetreroFletes.setText("YA EXISTE");

        jLabel92.setFont(new java.awt.Font("Calibri", 0, 15)); // NOI18N
        jLabel92.setText("STATUS:");

        btnGFletesCrea.add(jRPagadopFlete);
        jRPagadopFlete.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jRPagadopFlete.setText("PAGADO");
        jRPagadopFlete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRPagadopFleteActionPerformed(evt);
            }
        });

        btnGFletesCrea.add(jRPendFlete);
        jRPendFlete.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jRPendFlete.setText("PENDIENTE");

        jLabel91.setFont(new java.awt.Font("Calibri", 0, 15)); // NOI18N
        jLabel91.setText("NOTA:");

        jLabel101.setFont(new java.awt.Font("Calibri", 0, 15)); // NOI18N
        jLabel101.setText("CARGA:");

        txtCargaFlet.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        txtCargaFlet.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCargaFletKeyReleased(evt);
            }
        });

        jLabel102.setFont(new java.awt.Font("Calibri", 0, 15)); // NOI18N
        jLabel102.setText("CAJAS");

        javax.swing.GroupLayout jPanAdminist1Layout = new javax.swing.GroupLayout(jPanAdminist1);
        jPanAdminist1.setLayout(jPanAdminist1Layout);
        jPanAdminist1Layout.setHorizontalGroup(
            jPanAdminist1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanAdminist1Layout.createSequentialGroup()
                .addGap(84, 84, 84)
                .addGroup(jPanAdminist1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanAdminist1Layout.createSequentialGroup()
                        .addComponent(jLabel89, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(txtFolioFlete, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jLabLetreroFletes, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(90, 90, 90)
                        .addComponent(jLabel92, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jRPagadopFlete, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40)
                        .addComponent(jRPendFlete, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(70, 70, 70)
                        .addComponent(jButFleteGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanAdminist1Layout.createSequentialGroup()
                        .addComponent(jLabel90, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(txtChoferFlete, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(520, 520, 520)
                        .addComponent(jButAltasActualiza1, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanAdminist1Layout.createSequentialGroup()
                        .addGroup(jPanAdminist1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanAdminist1Layout.createSequentialGroup()
                                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(txtCostoFlete, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel88, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanAdminist1Layout.createSequentialGroup()
                                .addComponent(jLabel87, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(txtUnidFlete, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(80, 80, 80)
                                .addGroup(jPanAdminist1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanAdminist1Layout.createSequentialGroup()
                                        .addComponent(jLabel101, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(txtCargaFlet, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel102, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(76, 76, 76))
                                    .addGroup(jPanAdminist1Layout.createSequentialGroup()
                                        .addComponent(jLabel91, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, 0)
                                        .addComponent(txtNotaFlete, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(50, 50, 50)
                        .addComponent(jButAltasElimina1, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        jPanAdminist1Layout.setVerticalGroup(
            jPanAdminist1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanAdminist1Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(jPanAdminist1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButFleteGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanAdminist1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(jPanAdminist1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel89, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabLetreroFletes, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel92, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jRPagadopFlete, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jRPendFlete, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtFolioFlete, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanAdminist1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel90, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanAdminist1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtChoferFlete, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel101, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButAltasActualiza1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanAdminist1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtCargaFlet, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel102, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanAdminist1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel91, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNotaFlete, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanAdminist1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jButAltasElimina1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanAdminist1Layout.createSequentialGroup()
                        .addGroup(jPanAdminist1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel87, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtUnidFlete, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, 0)
                        .addGroup(jPanAdminist1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanAdminist1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtCostoFlete, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel88, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jScrollPane19.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jTabFletesDia.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTabFletesDia.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTabFletesDia.setComponentPopupMenu(jPpMnPagoFletes);
        jTabFletesDia.setRowHeight(32);
        jTabFletesDia.setRowMargin(2);
        jTabFletesDia.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTabFletesDiaMousePressed(evt);
            }
        });
        jTabFletesDia.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTabFletesDiaKeyReleased(evt);
            }
        });
        jScrollPane19.setViewportView(jTabFletesDia);

        jLabel93.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel93.setText("Elija fletero:");

        jButton20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/search32pxcolor.png"))); // NOI18N
        jButton20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton20ActionPerformed(evt);
            }
        });

        jLabContaFletes.setFont(new java.awt.Font("Tahoma", 3, 16)); // NOI18N
        jLabContaFletes.setForeground(new java.awt.Color(0, 0, 204));
        jLabContaFletes.setText("0");
        jLabContaFletes.setToolTipText("Muestra el numero de fletes en la tabla");
        jLabContaFletes.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        javax.swing.GroupLayout jPanCreaFletesLayout = new javax.swing.GroupLayout(jPanCreaFletes);
        jPanCreaFletes.setLayout(jPanCreaFletesLayout);
        jPanCreaFletesLayout.setHorizontalGroup(
            jPanCreaFletesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanCreaFletesLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(jPanAdminist1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(200, 200, 200))
            .addGroup(jPanCreaFletesLayout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addComponent(jScrollPane19)
                .addGap(100, 100, 100))
            .addGroup(jPanCreaFletesLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(jPanCreaFletesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel93, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanCreaFletesLayout.createSequentialGroup()
                        .addGap(130, 130, 130)
                        .addComponent(jCBAltasFletes, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(310, 310, 310)
                .addComponent(jLabel86, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jDFechCreaFlete, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton20)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanCreaFletesLayout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabContaFletes, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanCreaFletesLayout.setVerticalGroup(
            jPanCreaFletesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanCreaFletesLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanCreaFletesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel93, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCBAltasFletes, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel86, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jDFechCreaFlete, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanAdminist1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanCreaFletesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(jLabContaFletes))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane19, javax.swing.GroupLayout.DEFAULT_SIZE, 385, Short.MAX_VALUE)
                .addGap(50, 50, 50))
        );

        jPanHistorFletes.setBackground(new java.awt.Color(255, 255, 255));
        jPanHistorFletes.setPreferredSize(new java.awt.Dimension(1366, 600));

        jTablefiltrosBusqflete.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTablefiltrosBusqflete.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTablefiltrosBusqflete.setComponentPopupMenu(jPopMFiltrosBusqFletes);
        jTablefiltrosBusqflete.setRowHeight(25);
        jTablefiltrosBusqflete.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTablefiltrosBusqfleteMousePressed(evt);
            }
        });
        jTablefiltrosBusqflete.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTablefiltrosBusqfleteKeyReleased(evt);
            }
        });
        jScrollPane20.setViewportView(jTablefiltrosBusqflete);

        jLabel94.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel94.setText("Tabla de Resultados #:");

        jDCFol1.setDateFormatString("dd/MM/yyyy");
        jDCFol1.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N

        jLabel95.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel95.setText("ELIJA FECHA:");

        jLabel96.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel96.setText("ELIJA FECHA 2:");

        jDCFol2.setDateFormatString("dd/MM/yyyy");
        jDCFol2.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N

        jButton12.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jButton12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/search32pxcolor.png"))); // NOI18N
        jButton12.setText("BUSCAR");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        jLabel97.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel97.setText("ELIJA CLIENTE:");

        jCfleteroOpc.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCfleteroOpc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCfleteroOpcActionPerformed(evt);
            }
        });

        jCombOpcBusqFletes.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCombOpcBusqFletes.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "FLETERO", "FOLIO", "FECHA", "LAPSO FECHAS", "FLETERO+FECHA", "FLETERO+ LAPSO FECHAS" }));
        jCombOpcBusqFletes.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jCombOpcBusqFletesFocusLost(evt);
            }
        });
        jCombOpcBusqFletes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCombOpcBusqFletesActionPerformed(evt);
            }
        });

        jLabel98.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel98.setText("TIPO DE BUSQUEDA");

        butGrpFleterFilters.add(jRadioButton1);
        jRadioButton1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jRadioButton1.setText("Todos");

        butGrpFleterFilters.add(jRadioButton2);
        jRadioButton2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jRadioButton2.setText("Pagados");

        butGrpFleterFilters.add(jRadioButton3);
        jRadioButton3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jRadioButton3.setText("Pendientes");

        jLabel27.setFont(new java.awt.Font("Tahoma", 3, 16)); // NOI18N
        jLabel27.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel28.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel28.setText("importe: $");

        jLabel103.setFont(new java.awt.Font("Tahoma", 3, 16)); // NOI18N
        jLabel103.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        javax.swing.GroupLayout jPanHistorFletesLayout = new javax.swing.GroupLayout(jPanHistorFletes);
        jPanHistorFletes.setLayout(jPanHistorFletesLayout);
        jPanHistorFletesLayout.setHorizontalGroup(
            jPanHistorFletesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanHistorFletesLayout.createSequentialGroup()
                .addGroup(jPanHistorFletesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanHistorFletesLayout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(jScrollPane20))
                    .addGroup(jPanHistorFletesLayout.createSequentialGroup()
                        .addGroup(jPanHistorFletesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanHistorFletesLayout.createSequentialGroup()
                                .addGap(50, 50, 50)
                                .addGroup(jPanHistorFletesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanHistorFletesLayout.createSequentialGroup()
                                        .addComponent(jLabel97, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jCfleteroOpc, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanHistorFletesLayout.createSequentialGroup()
                                        .addComponent(jLabel98, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jCombOpcBusqFletes, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanHistorFletesLayout.createSequentialGroup()
                                        .addComponent(jLabel95, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jDCFol1, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(jPanHistorFletesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanHistorFletesLayout.createSequentialGroup()
                                        .addGap(32, 32, 32)
                                        .addComponent(jLabel96, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jDCFol2, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(27, 27, 27)
                                        .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanHistorFletesLayout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addComponent(jRadioButton1)
                                        .addGap(18, 18, 18)
                                        .addComponent(jRadioButton2)
                                        .addGap(18, 18, 18)
                                        .addComponent(jRadioButton3))))
                            .addGroup(jPanHistorFletesLayout.createSequentialGroup()
                                .addGap(51, 51, 51)
                                .addComponent(jLabel94, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(jSeparator15, javax.swing.GroupLayout.PREFERRED_SIZE, 460, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(252, 252, 252)
                                .addComponent(jLabel28)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel103, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 387, Short.MAX_VALUE)))
                .addGap(54, 54, 54))
        );
        jPanHistorFletesLayout.setVerticalGroup(
            jPanHistorFletesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanHistorFletesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanHistorFletesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCombOpcBusqFletes, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel98))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanHistorFletesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCfleteroOpc, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel97)
                    .addComponent(jRadioButton1)
                    .addComponent(jRadioButton2)
                    .addComponent(jRadioButton3))
                .addGroup(jPanHistorFletesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanHistorFletesLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jDCFol1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanHistorFletesLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel95))
                    .addGroup(jPanHistorFletesLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(jPanHistorFletesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jDCFol2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton12)
                            .addGroup(jPanHistorFletesLayout.createSequentialGroup()
                                .addGap(9, 9, 9)
                                .addComponent(jLabel96)))))
                .addGroup(jPanHistorFletesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanHistorFletesLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(jPanHistorFletesLayout.createSequentialGroup()
                        .addGroup(jPanHistorFletesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanHistorFletesLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanHistorFletesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel28)
                                    .addComponent(jLabel103, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanHistorFletesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanHistorFletesLayout.createSequentialGroup()
                                    .addGap(18, 18, 18)
                                    .addComponent(jSeparator15, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanHistorFletesLayout.createSequentialGroup()
                                    .addGap(10, 10, 10)
                                    .addComponent(jLabel94))))
                        .addGap(7, 7, 7)))
                .addComponent(jScrollPane20, javax.swing.GroupLayout.DEFAULT_SIZE, 526, Short.MAX_VALUE)
                .addGap(51, 51, 51))
        );

        jLayerFletes.setLayer(jPanCreaFletes, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayerFletes.setLayer(jPanHistorFletes, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jLayerFletesLayout = new javax.swing.GroupLayout(jLayerFletes);
        jLayerFletes.setLayout(jLayerFletesLayout);
        jLayerFletesLayout.setHorizontalGroup(
            jLayerFletesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanHistorFletes, javax.swing.GroupLayout.DEFAULT_SIZE, 1595, Short.MAX_VALUE)
            .addComponent(jPanCreaFletes, javax.swing.GroupLayout.DEFAULT_SIZE, 1595, Short.MAX_VALUE)
        );
        jLayerFletesLayout.setVerticalGroup(
            jLayerFletesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanHistorFletes, javax.swing.GroupLayout.DEFAULT_SIZE, 756, Short.MAX_VALUE)
            .addComponent(jPanCreaFletes, javax.swing.GroupLayout.DEFAULT_SIZE, 756, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPFletesLayout = new javax.swing.GroupLayout(jPFletes);
        jPFletes.setLayout(jPFletesLayout);
        jPFletesLayout.setHorizontalGroup(
            jPFletesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPFletesLayout.createSequentialGroup()
                .addGap(430, 430, 430)
                .addComponent(jRdCreaFletes, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(jRHistorFletes, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jLayerFletes, javax.swing.GroupLayout.DEFAULT_SIZE, 1595, Short.MAX_VALUE)
        );
        jPFletesLayout.setVerticalGroup(
            jPFletesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPFletesLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPFletesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jRdCreaFletes, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jRHistorFletes, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addComponent(jLayerFletes, javax.swing.GroupLayout.DEFAULT_SIZE, 756, Short.MAX_VALUE))
        );

        paneAltas.addTab("    FLETES    ", jPFletes);

        jPanVentasPiso.setPreferredSize(new java.awt.Dimension(1600, 820));
        jPanVentasPiso.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jPanVentasPisoKeyReleased(evt);
            }
        });

        btnGVentasPiso.add(jRadCreaVentaPiso);
        jRadCreaVentaPiso.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jRadCreaVentaPiso.setText("Realizar Ventas");
        jRadCreaVentaPiso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadCreaVentaPisoActionPerformed(evt);
            }
        });

        btnGVentasPiso.add(jRadBusqVentaPiso);
        jRadBusqVentaPiso.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jRadBusqVentaPiso.setText("Busqueda de Ventas");
        jRadBusqVentaPiso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadBusqVentaPisoActionPerformed(evt);
            }
        });

        jLayVentasPiso.setBackground(new java.awt.Color(153, 153, 255));

        jPaNVentaPiso.setBackground(new java.awt.Color(255, 255, 255));
        jPaNVentaPiso.setPreferredSize(new java.awt.Dimension(1600, 790));

        jLabel53.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel53.setText("Ventas del día");

        jCombProdVentaP.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jCombProdVentaP.setMaximumRowCount(10);

        jLabel54.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel54.setText("CANTIDAD:");

        txtCantVentaPiso.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtCantVentaPiso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCantVentaPisoActionPerformed(evt);
            }
        });

        jLabel55.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel55.setText("CAJAS");

        txtNotaVentP.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtNotaVentP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNotaVentPActionPerformed(evt);
            }
        });
        txtNotaVentP.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNotaVentPKeyPressed(evt);
            }
        });

        jButton8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton8.setText("AGREGAR");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jButton10.setBackground(new java.awt.Color(153, 255, 0));
        jButton10.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButton10.setText("F12 - COBRAR");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jLabel75.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel75.setText("ELIJA PRODUCTO:");

        jCombCliVentaP.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jCombCliVentaP.setNextFocusableComponent(jCombProdVentaP);

        jLabel76.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel76.setText("FECHA:");

        txtPrecProdVentaP.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtPrecProdVentaP.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPrecProdVentaPFocusLost(evt);
            }
        });

        jLabel77.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel77.setText("IMPORTE:");

        txtImportVentaP.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtImportVentaP.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtImportVentaPKeyPressed(evt);
            }
        });

        jLabel78.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel78.setText("NOTA:");

        jLabel79.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel79.setText("CLIENTE:");

        jTVistaVentaPisoDia.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jTVistaVentaPisoDia.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jTVistaVentaPisoDia.setRowHeight(32);
        jTVistaVentaPisoDia.setRowMargin(2);
        jTVistaVentaPisoDia.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTVistaVentaPisoDiaMousePressed(evt);
            }
        });
        jScrollPane17.setViewportView(jTVistaVentaPisoDia);

        jTabDescVentaP.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jTabDescVentaP.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "CODIGO", "DESCRIPCION", "UNIDADES", "PREC. UNITARIO", "IMPORTE"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTabDescVentaP.setGridColor(new java.awt.Color(204, 204, 204));
        jTabDescVentaP.setRowHeight(32);
        jTabDescVentaP.setRowMargin(2);
        jTabDescVentaP.setShowHorizontalLines(false);
        jTabDescVentaP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTabDescVentaPMousePressed(evt);
            }
        });
        jScrollPane12.setViewportView(jTabDescVentaP);
        if (jTabDescVentaP.getColumnModel().getColumnCount() > 0) {
            jTabDescVentaP.getColumnModel().getColumn(0).setMinWidth(60);
            jTabDescVentaP.getColumnModel().getColumn(0).setPreferredWidth(60);
            jTabDescVentaP.getColumnModel().getColumn(0).setMaxWidth(60);
        }

        jLabel56.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel56.setText("Detalle de Venta:");

        txtTotalVentaPiso.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtTotalVentaPiso.setForeground(new java.awt.Color(0, 51, 204));

        jLabel80.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel80.setText("TOTAL $:");

        jLabel81.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel81.setText("PRECIO $:");

        jDFVentaPiso.setDateFormatString("dd/MM/yyyy");
        jDFVentaPiso.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N

        javax.swing.GroupLayout jPaNVentaPisoLayout = new javax.swing.GroupLayout(jPaNVentaPiso);
        jPaNVentaPiso.setLayout(jPaNVentaPisoLayout);
        jPaNVentaPisoLayout.setHorizontalGroup(
            jPaNVentaPisoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPaNVentaPisoLayout.createSequentialGroup()
                .addGroup(jPaNVentaPisoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPaNVentaPisoLayout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addGroup(jPaNVentaPisoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPaNVentaPisoLayout.createSequentialGroup()
                                .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 743, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(jPaNVentaPisoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPaNVentaPisoLayout.createSequentialGroup()
                                        .addGap(110, 110, 110)
                                        .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPaNVentaPisoLayout.createSequentialGroup()
                                        .addGroup(jPaNVentaPisoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel80, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtTotalVentaPiso, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(18, 18, 18)
                                        .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPaNVentaPisoLayout.createSequentialGroup()
                                .addGap(51, 51, 51)
                                .addComponent(jLabel53, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSeparator12, javax.swing.GroupLayout.PREFERRED_SIZE, 754, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane17)))
                    .addGroup(jPaNVentaPisoLayout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(jLabel79, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCombCliVentaP, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(330, 330, 330)
                        .addComponent(jLabel76, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jDFVentaPiso, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPaNVentaPisoLayout.createSequentialGroup()
                        .addGroup(jPaNVentaPisoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPaNVentaPisoLayout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addComponent(jLabel75, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPaNVentaPisoLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel56, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addGroup(jPaNVentaPisoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPaNVentaPisoLayout.createSequentialGroup()
                                .addComponent(jCombProdVentaP, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)
                                .addComponent(jLabel54, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(txtCantVentaPiso, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(jLabel55, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(20, 20, 20)
                                .addComponent(jLabel81, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(txtPrecProdVentaP, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(40, 40, 40)
                                .addComponent(jLabel77, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(txtImportVentaP, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(20, 20, 20)
                                .addComponent(jLabel78, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtNotaVentP, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jSeparator11, javax.swing.GroupLayout.PREFERRED_SIZE, 430, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(655, 655, 655))
        );
        jPaNVentaPisoLayout.setVerticalGroup(
            jPaNVentaPisoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPaNVentaPisoLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPaNVentaPisoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCombCliVentaP, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jDFVentaPiso, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel79)
                    .addComponent(jLabel76))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPaNVentaPisoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPaNVentaPisoLayout.createSequentialGroup()
                        .addGroup(jPaNVentaPisoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCombProdVentaP, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCantVentaPiso, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtPrecProdVentaP, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtImportVentaP, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNotaVentP, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel54)
                            .addGroup(jPaNVentaPisoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel81, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel55, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jLabel77)
                            .addComponent(jLabel78))
                        .addGap(20, 20, 20)
                        .addComponent(jSeparator11, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPaNVentaPisoLayout.createSequentialGroup()
                        .addComponent(jLabel75)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel56)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(jPaNVentaPisoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPaNVentaPisoLayout.createSequentialGroup()
                        .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addComponent(jLabel80, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addGroup(jPaNVentaPisoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtTotalVentaPiso, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPaNVentaPisoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel53, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator12, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addComponent(jScrollPane17, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                .addGap(51, 51, 51))
        );

        jPanBusqVentasPiso.setBackground(new java.awt.Color(255, 255, 255));
        jPanBusqVentasPiso.setPreferredSize(new java.awt.Dimension(1600, 790));

        jLabel82.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel82.setText("TIPO DE BUSQUEDA");

        jCombOpcBusqVenta.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCombOpcBusqVenta.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "CLIENTE", "FECHA", "LAPSO FECHAS", "CLIENTE+FECHA", "CLIENTE+LAPSO FECHAS" }));
        jCombOpcBusqVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCombOpcBusqVentaActionPerformed(evt);
            }
        });

        jLabel83.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel83.setText("ELIJA CLIENTE:");

        jCCliVentaPiso.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCCliVentaPiso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCCliVentaPisoActionPerformed(evt);
            }
        });

        jLabel84.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel84.setText("ELIJA FECHA:");

        jDateChoBVent1.setDateFormatString("dd/MM/yyyy");
        jDateChoBVent1.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N

        jLabel85.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel85.setText("ELIJA FECHA 2:");

        jDatebusqVenta2.setDateFormatString("dd/MM/yyyy");
        jDatebusqVenta2.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N

        jLabel20.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel20.setText("Tabla de Resultados");

        jTablefiltrosBusqVent.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTablefiltrosBusqVent.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTablefiltrosBusqVent.setComponentPopupMenu(jPopVentaPisoBusq);
        jTablefiltrosBusqVent.setRowHeight(32);
        jTablefiltrosBusqVent.setRowMargin(2);
        jTablefiltrosBusqVent.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTablefiltrosBusqVentMousePressed(evt);
            }
        });
        jScrollPane18.setViewportView(jTablefiltrosBusqVent);

        jButton11.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jButton11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/search32pxcolor.png"))); // NOI18N
        jButton11.setText("BUSCAR");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jLabel174.setFont(new java.awt.Font("Tahoma", 3, 16)); // NOI18N
        jLabel174.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel175.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel175.setText("Total: $");

        jLabel176.setFont(new java.awt.Font("Tahoma", 3, 16)); // NOI18N
        jLabel176.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        javax.swing.GroupLayout jPanBusqVentasPisoLayout = new javax.swing.GroupLayout(jPanBusqVentasPiso);
        jPanBusqVentasPiso.setLayout(jPanBusqVentasPisoLayout);
        jPanBusqVentasPisoLayout.setHorizontalGroup(
            jPanBusqVentasPisoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanBusqVentasPisoLayout.createSequentialGroup()
                .addGroup(jPanBusqVentasPisoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanBusqVentasPisoLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jScrollPane18))
                    .addGroup(jPanBusqVentasPisoLayout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addGroup(jPanBusqVentasPisoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanBusqVentasPisoLayout.createSequentialGroup()
                                .addGroup(jPanBusqVentasPisoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanBusqVentasPisoLayout.createSequentialGroup()
                                        .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(60, 60, 60)
                                        .addComponent(jLabel174, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jSeparator13, javax.swing.GroupLayout.PREFERRED_SIZE, 460, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanBusqVentasPisoLayout.createSequentialGroup()
                                        .addComponent(jLabel84, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jDateChoBVent1, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel85, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(1, 1, 1)
                                        .addComponent(jDatebusqVenta2, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(jPanBusqVentasPisoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanBusqVentasPisoLayout.createSequentialGroup()
                                        .addGap(20, 20, 20)
                                        .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanBusqVentasPisoLayout.createSequentialGroup()
                                        .addGap(226, 226, 226)
                                        .addComponent(jLabel175)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel176, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPanBusqVentasPisoLayout.createSequentialGroup()
                                .addGroup(jPanBusqVentasPisoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel82, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanBusqVentasPisoLayout.createSequentialGroup()
                                        .addComponent(jLabel83, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                                .addGroup(jPanBusqVentasPisoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jCCliVentaPiso, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jCombOpcBusqVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addGap(292, 292, 292))
        );
        jPanBusqVentasPisoLayout.setVerticalGroup(
            jPanBusqVentasPisoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanBusqVentasPisoLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanBusqVentasPisoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCombOpcBusqVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel82))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanBusqVentasPisoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCCliVentaPiso, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel83))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanBusqVentasPisoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanBusqVentasPisoLayout.createSequentialGroup()
                        .addGroup(jPanBusqVentasPisoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jDateChoBVent1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jDatebusqVenta2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel84)
                            .addComponent(jLabel85))
                        .addGap(18, 18, 18)
                        .addGroup(jPanBusqVentasPisoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jSeparator13, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel174, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanBusqVentasPisoLayout.createSequentialGroup()
                        .addGroup(jPanBusqVentasPisoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel175)
                            .addComponent(jLabel176))
                        .addGap(7, 7, 7)))
                .addComponent(jScrollPane18)
                .addGap(55, 55, 55))
        );

        jLayVentasPiso.setLayer(jPaNVentaPiso, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayVentasPiso.setLayer(jPanBusqVentasPiso, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jLayVentasPisoLayout = new javax.swing.GroupLayout(jLayVentasPiso);
        jLayVentasPiso.setLayout(jLayVentasPisoLayout);
        jLayVentasPisoLayout.setHorizontalGroup(
            jLayVentasPisoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPaNVentaPiso, javax.swing.GroupLayout.DEFAULT_SIZE, 2007, Short.MAX_VALUE)
            .addGroup(jLayVentasPisoLayout.createSequentialGroup()
                .addComponent(jPanBusqVentasPiso, javax.swing.GroupLayout.DEFAULT_SIZE, 1551, Short.MAX_VALUE)
                .addGap(456, 456, 456))
        );
        jLayVentasPisoLayout.setVerticalGroup(
            jLayVentasPisoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPaNVentaPiso, javax.swing.GroupLayout.DEFAULT_SIZE, 756, Short.MAX_VALUE)
            .addComponent(jPanBusqVentasPiso, javax.swing.GroupLayout.DEFAULT_SIZE, 756, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanVentasPisoLayout = new javax.swing.GroupLayout(jPanVentasPiso);
        jPanVentasPiso.setLayout(jPanVentasPisoLayout);
        jPanVentasPisoLayout.setHorizontalGroup(
            jPanVentasPisoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanVentasPisoLayout.createSequentialGroup()
                .addGap(200, 200, 200)
                .addComponent(jRadCreaVentaPiso, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(jRadBusqVentaPiso, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jLayVentasPiso)
        );
        jPanVentasPisoLayout.setVerticalGroup(
            jPanVentasPisoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanVentasPisoLayout.createSequentialGroup()
                .addGroup(jPanVentasPisoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jRadCreaVentaPiso, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jRadBusqVentaPiso, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addComponent(jLayVentasPiso))
        );

        paneAltas.addTab("    VENTAS DE PISO    ", jPanVentasPiso);

        jPanAsignac.setBackground(new java.awt.Color(255, 255, 255));
        jPanAsignac.setPreferredSize(new java.awt.Dimension(1600, 850));

        jTabVistaPedidosDia1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTabVistaPedidosDia1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "CLIENTE", "PRIM", "SEG", "PRIM_R", "SEG_R", "BOLA_P", "BOLA_S", "3_RA", "TOT_CAJA", "FALTAN"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTabVistaPedidosDia1.setComponentPopupMenu(jPMDetailFormaPedido);
        jTabVistaPedidosDia1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jTabVistaPedidosDia1.setDragEnabled(true);
        jTabVistaPedidosDia1.setEditingColumn(0);
        jTabVistaPedidosDia1.setEditingRow(0);
        jTabVistaPedidosDia1.setRowHeight(23);
        jTabVistaPedidosDia1.setSelectionBackground(new java.awt.Color(153, 255, 153));
        jTabVistaPedidosDia1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTabVistaPedidosDia1FocusGained(evt);
            }
        });
        jScrollPane23.setViewportView(jTabVistaPedidosDia1);
        if (jTabVistaPedidosDia1.getColumnModel().getColumnCount() > 0) {
            jTabVistaPedidosDia1.getColumnModel().getColumn(0).setMinWidth(60);
            jTabVistaPedidosDia1.getColumnModel().getColumn(0).setPreferredWidth(60);
            jTabVistaPedidosDia1.getColumnModel().getColumn(0).setMaxWidth(60);
            jTabVistaPedidosDia1.getColumnModel().getColumn(2).setPreferredWidth(15);
            jTabVistaPedidosDia1.getColumnModel().getColumn(3).setPreferredWidth(15);
            jTabVistaPedidosDia1.getColumnModel().getColumn(4).setPreferredWidth(20);
            jTabVistaPedidosDia1.getColumnModel().getColumn(5).setPreferredWidth(20);
            jTabVistaPedidosDia1.getColumnModel().getColumn(6).setPreferredWidth(20);
            jTabVistaPedidosDia1.getColumnModel().getColumn(7).setPreferredWidth(20);
            jTabVistaPedidosDia1.getColumnModel().getColumn(8).setPreferredWidth(15);
            jTabVistaPedidosDia1.getColumnModel().getColumn(9).setPreferredWidth(30);
            jTabVistaPedidosDia1.getColumnModel().getColumn(10).setPreferredWidth(25);
        }

        jLabel110.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel110.setText("FLETES ASIGNADOS:");

        jLabel111.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel111.setText("Detalle flete-pedido-compra");

        jLabel112.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel112.setText("COMPRAS DEL DIA:");

        jTabVistaComprasDia3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTabVistaComprasDia3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "PROVEEDOR", "PRIM", "SEG", "PRIM_R", "SEG_R", "BOLA_P", "BOLA_S", "3_RA", "TOT_CAJA", "FALTAN"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTabVistaComprasDia3.setComponentPopupMenu(jPopMnDetailCompAsign);
        jTabVistaComprasDia3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jTabVistaComprasDia3.setEditingColumn(0);
        jTabVistaComprasDia3.setEditingRow(0);
        jTabVistaComprasDia3.setRowHeight(23);
        jTabVistaComprasDia3.setSelectionBackground(new java.awt.Color(153, 255, 153));
        jTabVistaComprasDia3.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTabVistaComprasDia3FocusGained(evt);
            }
        });
        jScrollPane25.setViewportView(jTabVistaComprasDia3);
        if (jTabVistaComprasDia3.getColumnModel().getColumnCount() > 0) {
            jTabVistaComprasDia3.getColumnModel().getColumn(0).setMinWidth(60);
            jTabVistaComprasDia3.getColumnModel().getColumn(0).setPreferredWidth(60);
            jTabVistaComprasDia3.getColumnModel().getColumn(0).setMaxWidth(60);
            jTabVistaComprasDia3.getColumnModel().getColumn(2).setPreferredWidth(15);
            jTabVistaComprasDia3.getColumnModel().getColumn(3).setPreferredWidth(15);
            jTabVistaComprasDia3.getColumnModel().getColumn(4).setPreferredWidth(20);
            jTabVistaComprasDia3.getColumnModel().getColumn(5).setPreferredWidth(20);
            jTabVistaComprasDia3.getColumnModel().getColumn(6).setPreferredWidth(20);
            jTabVistaComprasDia3.getColumnModel().getColumn(7).setPreferredWidth(20);
            jTabVistaComprasDia3.getColumnModel().getColumn(8).setPreferredWidth(15);
            jTabVistaComprasDia3.getColumnModel().getColumn(9).setPreferredWidth(30);
            jTabVistaComprasDia3.getColumnModel().getColumn(10).setPreferredWidth(25);
        }

        jButton17.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton17.setText(">>");
        jButton17.setToolTipText("Realizar asiganacion de las filas seleccionadas");
        jButton17.setAlignmentY(0.0F);
        jButton17.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton17ActionPerformed(evt);
            }
        });

        jScrollPane26.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jTabFletesDia1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTabFletesDia1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTabFletesDia1.setComponentPopupMenu(jPpMnDetFletes);
        jTabFletesDia1.setRowHeight(23);
        jTabFletesDia1.setSelectionBackground(new java.awt.Color(153, 255, 153));
        jTabFletesDia1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTabFletesDia1MousePressed(evt);
            }
        });
        jScrollPane26.setViewportView(jTabFletesDia1);

        jDCAsignacionDia.setDateFormatString("dd/MM/yyyy");

        jButton18.setBackground(new java.awt.Color(117, 229, 255));
        jButton18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/Actualizar.png"))); // NOI18N
        jButton18.setToolTipText("Cargar registros del día");
        jButton18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton18ActionPerformed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(204, 204, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Asignacion de Flete-Mercancia-Pedido", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 14))); // NOI18N

        jLabel114.setFont(new java.awt.Font("Calibri", 0, 15)); // NOI18N
        jLabel114.setText("ID PEDIDO:");

        jLabel115.setFont(new java.awt.Font("Calibri", 0, 15)); // NOI18N
        jLabel115.setText("ID COMPRA:");

        txtCompraAsign.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N

        txtidPedidoAsign.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N

        jLabel116.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel116.setText("Status del pedido:");

        jLabel117.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel117.setText("PRIM");
        jLabel117.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel118.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel118.setText("SEG");
        jLabel118.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel119.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel119.setText("PRIM_R");
        jLabel119.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel120.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel120.setText("SEG_R");
        jLabel120.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel121.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel121.setText("BOLA_P:");
        jLabel121.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel122.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel122.setText("BOLA_S:");
        jLabel122.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel123.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel123.setText("TERCERA:");
        jLabel123.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jTabDetailAsignTotales.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jTabDetailAsignTotales.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "ASIGNADAS", "FALTANTES"
            }
        ));
        jTabDetailAsignTotales.setAlignmentX(3.0F);
        jTabDetailAsignTotales.setRowHeight(25);
        jScrollPane32.setViewportView(jTabDetailAsignTotales);

        jTabDetailAsignTotales1.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jTabDetailAsignTotales1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "ASIGNADAS", "FALTANTES"
            }
        ));
        jTabDetailAsignTotales1.setAlignmentX(3.0F);
        jTabDetailAsignTotales1.setRowHeight(25);
        jScrollPane33.setViewportView(jTabDetailAsignTotales1);

        jLabel125.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel125.setText("Status de compra:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel116, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jLabel117, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                    .addComponent(jLabel120, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(jLabel119, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(jLabel121, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(jLabel122, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(jLabel123, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(2, 2, 2)))
                                        .addGap(8, 8, 8))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel118, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(10, 10, 10)))
                                .addComponent(jScrollPane32, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(34, 34, 34)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel125, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(12, 12, 12))
                            .addComponent(jScrollPane33, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addComponent(jLabel114, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtidPedidoAsign, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(42, 42, 42)
                        .addComponent(jLabel115, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCompraAsign, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(292, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtidPedidoAsign, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel114)
                    .addComponent(txtCompraAsign, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel115))
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel125, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel116))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(jLabel117)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel118)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel119)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel120)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel121)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel122)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel123))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(jScrollPane33, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane32, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabFlet.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabFlet.setForeground(new java.awt.Color(0, 51, 102));
        jLabFlet.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabFlet.setText("jLabel126");

        jLabPed.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabPed.setForeground(new java.awt.Color(0, 51, 102));
        jLabPed.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabPed.setText("jLabel126");

        jLaComp.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLaComp.setForeground(new java.awt.Color(0, 51, 102));
        jLaComp.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLaComp.setText("jLabel126");

        jLabel126.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel126.setText("PEDIDOS DEL DIA:");

        jTDetailAsign.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTDetailAsign.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTDetailAsign.setRowHeight(23);
        jScrollPane27.setViewportView(jTDetailAsign);

        txtBusqAignCompra.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtBusqAignCompra.setToolTipText("Busqueda de compra");
        txtBusqAignCompra.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBusqAignCompraKeyReleased(evt);
            }
        });

        jLabel32.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/search32pxcolor.png"))); // NOI18N
        jLabel32.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jTable1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "PRIM", "SEG", "PRIM_R", "SEG_R", "BOLA_P", "BOLA_S", "3_RA", "TOTAL"
            }
        ));
        jTable1.setRowHeight(25);
        jScrollPane7.setViewportView(jTable1);

        jLabel104.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel104.setText("Pedidos =");

        jLabel105.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel105.setText("Compra =");

        jLabel106.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel106.setText("Faltan =");

        jButton34.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/end.png"))); // NOI18N
        jButton34.setToolTipText("Terminar y registrar sobrantes del dia.");
        jButton34.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton34MouseClicked(evt);
            }
        });
        jButton34.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton34ActionPerformed(evt);
            }
        });

        jCheckBox3.setFont(new java.awt.Font("Tahoma", 2, 14)); // NOI18N
        jCheckBox3.setText("Cargar sobrantes");
        jCheckBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox3ActionPerformed(evt);
            }
        });

        txtBusqFleteAsign.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtBusqFleteAsign.setToolTipText("Busqueda de folio");
        txtBusqFleteAsign.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBusqFleteAsignKeyReleased(evt);
            }
        });

        jLabel177.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel177.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/search32pxcolor.png"))); // NOI18N

        jButton37.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/pdficon.png"))); // NOI18N
        jButton37.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton37ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanAsignacLayout = new javax.swing.GroupLayout(jPanAsignac);
        jPanAsignac.setLayout(jPanAsignacLayout);
        jPanAsignacLayout.setHorizontalGroup(
            jPanAsignacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanAsignacLayout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addGroup(jPanAsignacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanAsignacLayout.createSequentialGroup()
                        .addGroup(jPanAsignacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanAsignacLayout.createSequentialGroup()
                                .addComponent(jLabel112, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtBusqAignCompra, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(jLabel32)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLaComp, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanAsignacLayout.createSequentialGroup()
                                .addComponent(jLabel110)
                                .addGap(18, 18, 18)
                                .addComponent(txtBusqFleteAsign, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel177)
                                .addGap(32, 32, 32)
                                .addComponent(jLabFlet, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 112, Short.MAX_VALUE)))
                        .addGap(14, 14, 14)
                        .addGroup(jPanAsignacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton18, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton17, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel104)
                            .addComponent(jLabel105)))
                    .addGroup(jPanAsignacLayout.createSequentialGroup()
                        .addGroup(jPanAsignacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanAsignacLayout.createSequentialGroup()
                                .addComponent(jLabel126, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(268, 268, 268)
                                .addComponent(jLabPed, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jScrollPane26)
                            .addComponent(jScrollPane25)
                            .addComponent(jScrollPane23))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanAsignacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel106, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanAsignacLayout.createSequentialGroup()
                                .addComponent(jButton34, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(7, 7, 7)))))
                .addGroup(jPanAsignacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanAsignacLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(jScrollPane27))
                    .addComponent(jScrollPane7)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanAsignacLayout.createSequentialGroup()
                        .addGroup(jPanAsignacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel111, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanAsignacLayout.createSequentialGroup()
                                .addComponent(jDCAsignacionDia, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(35, 35, 35)
                                .addComponent(jCheckBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(26, 26, 26)
                                .addComponent(jButton37, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(55, 55, 55))
        );
        jPanAsignacLayout.setVerticalGroup(
            jPanAsignacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanAsignacLayout.createSequentialGroup()
                .addGroup(jPanAsignacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanAsignacLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanAsignacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel126)
                            .addComponent(jLabPed, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, 0)
                        .addComponent(jScrollPane23, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanAsignacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLaComp)
                            .addComponent(jLabel112)
                            .addComponent(txtBusqAignCompra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(1, 1, 1)
                        .addComponent(jScrollPane25, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addGap(4, 4, 4)
                        .addGroup(jPanAsignacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanAsignacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel110)
                                .addComponent(txtBusqFleteAsign, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanAsignacLayout.createSequentialGroup()
                                .addGroup(jPanAsignacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabFlet)
                                    .addComponent(jLabel177, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(2, 2, 2)))
                        .addGap(2, 2, 2)
                        .addComponent(jScrollPane26, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addGroup(jPanAsignacLayout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addGroup(jPanAsignacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanAsignacLayout.createSequentialGroup()
                                .addComponent(jButton18, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel104)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel105)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel106)
                                .addGap(54, 54, 54)
                                .addComponent(jButton17, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton34, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanAsignacLayout.createSequentialGroup()
                                .addGroup(jPanAsignacLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jDCAsignacionDia, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jCheckBox3)
                                    .addComponent(jButton37, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel111)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane27, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)))))
                .addGap(82, 82, 82))
        );

        paneAltas.addTab("ASIGNACION", jPanAsignac);

        jPanPagos.setPreferredSize(new java.awt.Dimension(1600, 820));

        jTabbedPane1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

        jDFechPays.setDateFormatString("dd/MM/yyyy");
        jDFechPays.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N

        jButFleteGuardar1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButFleteGuardar1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/search32pxcolor.png"))); // NOI18N
        jButFleteGuardar1.setText("    Buscar");
        jButFleteGuardar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButFleteGuardar1ActionPerformed(evt);
            }
        });

        jLabel100.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N
        jLabel100.setText("FECHA:");

        jLabel161.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel161.setText("Monto de apertura $:");

        jLabel162.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel162.setForeground(new java.awt.Color(0, 51, 102));
        jLabel162.setText("0.00");

        jButton23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/cajero-color.png"))); // NOI18N
        jButton23.setText("<html>\n<h3>Corte de <br> caja </h3>\n</html>");
        jButton23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton23ActionPerformed(evt);
            }
        });

        jSplitPane1.setDividerSize(8);
        jSplitPane1.setResizeWeight(0.5);
        jSplitPane1.setOneTouchExpandable(true);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "ENTRADAS", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 13))); // NOI18N

        jLabPed3.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabPed3.setForeground(new java.awt.Color(0, 51, 102));
        jLabPed3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabPed3.setText("Pago de pedido cliente/Ingreso");

        jTabPayPeds.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTabPayPeds.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jTabPayPeds.setRowHeight(23);
        jScrollPane28.setViewportView(jTabPayPeds);

        jLabPed4.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabPed4.setForeground(new java.awt.Color(0, 51, 102));
        jLabPed4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabPed4.setText("Pago de ventas de piso");

        jLabPed5.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabPed5.setForeground(new java.awt.Color(0, 51, 102));
        jLabPed5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabPed5.setText("Pago de prestamo a proveedor");

        jTabPayPrestamosProv.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTabPayPrestamosProv.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jTabPayPrestamosProv.setRowHeight(23);
        jScrollPane30.setViewportView(jTabPayPrestamosProv);

        jTabVentPisoPays.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTabVentPisoPays.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jTabVentPisoPays.setRowHeight(23);
        jScrollPane21.setViewportView(jTabVentPisoPays);

        jLabCountPC.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabCountPC.setText("jLabel127");

        jLabCountPP.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabCountPP.setText("jLabel128");

        jLabel132.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel132.setText("Monto: $");

        jLabTotPP.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        jLabTotPP.setForeground(new java.awt.Color(3, 90, 166));
        jLabTotPP.setText("jLabel133");

        jLabel134.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel134.setText("Monto: $");

        jLabTotPC.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        jLabTotPC.setForeground(new java.awt.Color(3, 90, 166));
        jLabTotPC.setText("jLabel148");

        jLabel149.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel149.setText("Monto: $");

        jLabTotVP.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        jLabTotVP.setForeground(new java.awt.Color(3, 90, 166));
        jLabTotVP.setText("jLabel150");

        jLabCountVP.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabCountVP.setText("jLabel151");

        jButton26.setFont(new java.awt.Font("Tahoma", 3, 11)); // NOI18N
        jButton26.setText("Cancel");
        jButton26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton26ActionPerformed(evt);
            }
        });

        jButton27.setFont(new java.awt.Font("Tahoma", 3, 11)); // NOI18N
        jButton27.setText("Cancel");
        jButton27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton27ActionPerformed(evt);
            }
        });

        jButton28.setFont(new java.awt.Font("Tahoma", 3, 11)); // NOI18N
        jButton28.setText("Cancel");
        jButton28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton28ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabPed4)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabCountVP)
                                        .addGap(86, 86, 86)
                                        .addComponent(jLabel149)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabTotVP, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButton28))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabPed5)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabCountPP)
                                        .addGap(35, 35, 35)
                                        .addComponent(jLabel132)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabTotPP, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButton27))))
                            .addComponent(jScrollPane28)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabPed3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabCountPC)
                                .addGap(35, 35, 35)
                                .addComponent(jLabel134)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabTotPC, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton26))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jScrollPane30)
                                .addGap(1, 1, 1)))
                        .addGap(3, 3, 3))
                    .addComponent(jScrollPane21))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabCountPC)
                        .addComponent(jLabel134)
                        .addComponent(jLabTotPC)
                        .addComponent(jButton26, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabPed3))
                .addComponent(jScrollPane28, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabPed5)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabCountPP)
                        .addComponent(jLabel132)
                        .addComponent(jLabTotPP)
                        .addComponent(jButton27, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane30, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabPed4)
                        .addComponent(jLabCountVP))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel149)
                        .addComponent(jLabTotVP)
                        .addComponent(jButton28, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addComponent(jScrollPane21, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE)
                .addGap(15, 15, 15))
        );

        jSplitPane1.setLeftComponent(jPanel2);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "SALIDAS", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 13))); // NOI18N

        jLabPed1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabPed1.setForeground(new java.awt.Color(0, 51, 102));
        jLabPed1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabPed1.setText("Pago de fletes");

        jScrollPane29.setPreferredSize(new java.awt.Dimension(300, 402));

        jTabPaysFletesDia.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTabPaysFletesDia.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTabPaysFletesDia.setRowHeight(23);
        jScrollPane29.setViewportView(jTabPaysFletesDia);

        jLabPed6.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabPed6.setForeground(new java.awt.Color(0, 51, 102));
        jLabPed6.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabPed6.setText("Pago de compra a proveedor");

        jScrollPane22.setPreferredSize(new java.awt.Dimension(300, 402));

        jTabGastosDias.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTabGastosDias.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jTabGastosDias.setRowHeight(23);
        jScrollPane22.setViewportView(jTabGastosDias);

        jScrollPane31.setPreferredSize(new java.awt.Dimension(300, 402));

        jTabPaysCompraProovedor.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTabPaysCompraProovedor.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTabPaysCompraProovedor.setRowHeight(23);
        jScrollPane31.setViewportView(jTabPaysCompraProovedor);

        jLabPed7.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabPed7.setForeground(new java.awt.Color(0, 51, 102));
        jLabPed7.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabPed7.setText("Gastos registrados del día");

        jLabcountPF.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabcountPF.setText("jLabel152");

        jLabel153.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel153.setText("Monto: $");

        jLabTotPF.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        jLabTotPF.setForeground(new java.awt.Color(3, 90, 166));
        jLabTotPF.setText("jLabel154");

        jLabCountCP.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabCountCP.setText("jLabel155");

        jLabel156.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel156.setText("Monto: $");

        jLabTotCP.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        jLabTotCP.setForeground(new java.awt.Color(3, 90, 166));
        jLabTotCP.setText("jLabel157");

        jLabCountGast.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabCountGast.setText("jLabel158");

        jLabel159.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel159.setText("Monto: $");

        jLabTotGast.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        jLabTotGast.setForeground(new java.awt.Color(3, 90, 166));
        jLabTotGast.setText("jLabel160");

        jButton24.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jButton24.setText("+");
        jButton24.setToolTipText("Registra un nuevo gasto del día");
        jButton24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton24ActionPerformed(evt);
            }
        });

        jButton29.setFont(new java.awt.Font("Tahoma", 3, 11)); // NOI18N
        jButton29.setText("Cancel");
        jButton29.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton29ActionPerformed(evt);
            }
        });

        jButton31.setFont(new java.awt.Font("Tahoma", 3, 11)); // NOI18N
        jButton31.setText("Cancel");
        jButton31.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton31ActionPerformed(evt);
            }
        });

        jButton30.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jButton30.setText("-");
        jButton30.setToolTipText("Elimina gasto seleccionado");
        jButton30.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton30ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabPed7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabCountGast)
                        .addGap(45, 45, 45)
                        .addComponent(jLabel159)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabTotGast, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton30)
                        .addGap(0, 0, 0)
                        .addComponent(jButton24))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane31, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabPed6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabCountCP)
                                .addGap(29, 29, 29)
                                .addComponent(jLabel156)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabTotCP, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton31))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabPed1)
                                .addGap(18, 18, 18)
                                .addComponent(jLabcountPF)
                                .addGap(113, 113, 113)
                                .addComponent(jLabel153)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabTotPF, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton29))
                            .addComponent(jScrollPane29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(4, 4, 4)))
                .addGap(22, 22, 22))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabPed1)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabcountPF)
                        .addComponent(jLabel153)
                        .addComponent(jLabTotPF)
                        .addComponent(jButton29, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, 0)
                .addComponent(jScrollPane29, javax.swing.GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabPed6, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabCountCP)
                    .addComponent(jLabel156)
                    .addComponent(jLabTotCP)
                    .addComponent(jButton31, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addComponent(jScrollPane31, javax.swing.GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabPed7, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabCountGast)
                    .addComponent(jLabel159)
                    .addComponent(jLabTotGast)
                    .addComponent(jButton24, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton30, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addComponent(jScrollPane22, javax.swing.GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE)
                .addGap(15, 15, 15))
        );

        jSplitPane1.setRightComponent(jPanel3);

        jLabel163.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel163.setText("Total entradas $:");

        jLabel164.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel164.setForeground(new java.awt.Color(0, 51, 102));
        jLabel164.setText("0.00");

        jLabel165.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel165.setText("Total salidas $:");

        jLabel166.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel166.setForeground(new java.awt.Color(0, 51, 102));
        jLabel166.setText("0.00");

        jLabel167.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel167.setText("Total en caja $:");

        jLabel168.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel168.setForeground(new java.awt.Color(0, 51, 102));
        jLabel168.setText("0.00");

        jCheckBox2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jCheckBox2.setText("Ver todo");

        javax.swing.GroupLayout ReportDayLayout = new javax.swing.GroupLayout(ReportDay);
        ReportDay.setLayout(ReportDayLayout);
        ReportDayLayout.setHorizontalGroup(
            ReportDayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ReportDayLayout.createSequentialGroup()
                .addGroup(ReportDayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ReportDayLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1184, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(ReportDayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(ReportDayLayout.createSequentialGroup()
                                .addComponent(jLabel163)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel164))
                            .addGroup(ReportDayLayout.createSequentialGroup()
                                .addComponent(jLabel161)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel162))
                            .addGroup(ReportDayLayout.createSequentialGroup()
                                .addComponent(jLabel165)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel166))
                            .addGroup(ReportDayLayout.createSequentialGroup()
                                .addComponent(jLabel167)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel168))
                            .addComponent(jButton23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(ReportDayLayout.createSequentialGroup()
                        .addGap(360, 360, 360)
                        .addComponent(jLabel100, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jDFechPays, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jCheckBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButFleteGuardar1, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(218, 218, 218))
        );
        ReportDayLayout.setVerticalGroup(
            ReportDayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ReportDayLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(ReportDayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ReportDayLayout.createSequentialGroup()
                        .addComponent(jLabel100)
                        .addGap(22, 22, 22))
                    .addGroup(ReportDayLayout.createSequentialGroup()
                        .addGroup(ReportDayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ReportDayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jButFleteGuardar1)
                                .addComponent(jCheckBox2))
                            .addGroup(ReportDayLayout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addComponent(jDFechPays, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(4, 4, 4)))
                .addGroup(ReportDayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ReportDayLayout.createSequentialGroup()
                        .addGroup(ReportDayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel161)
                            .addComponent(jLabel162))
                        .addGap(31, 31, 31)
                        .addGroup(ReportDayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel163)
                            .addComponent(jLabel164))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(ReportDayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel165)
                            .addComponent(jLabel166))
                        .addGap(34, 34, 34)
                        .addGroup(ReportDayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel167)
                            .addComponent(jLabel168))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(282, 282, 282))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ReportDayLayout.createSequentialGroup()
                        .addComponent(jSplitPane1)
                        .addGap(31, 31, 31))))
        );

        jTabbedPane1.addTab("Corte del día", new javax.swing.ImageIcon(getClass().getResource("/image/caja-registradora.png")), ReportDay); // NOI18N

        jSplitPane2.setDividerLocation(650);
        jSplitPane2.setResizeWeight(0.5);

        jTable4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTable4.setModel(new javax.swing.table.DefaultTableModel(
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
        jTable4.setRowHeight(25);
        jTable4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable4MouseClicked(evt);
            }
        });
        jScrollPane37.setViewportView(jTable4);

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane37, javax.swing.GroupLayout.DEFAULT_SIZE, 635, Short.MAX_VALUE)
                .addGap(4, 4, 4))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane37, javax.swing.GroupLayout.DEFAULT_SIZE, 449, Short.MAX_VALUE)
                .addGap(127, 127, 127))
        );

        jSplitPane2.setLeftComponent(jPanel10);

        jTable5.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTable5.setModel(new javax.swing.table.DefaultTableModel(
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
        jTable5.setRowHeight(25);
        jScrollPane38.setViewportView(jTable5);

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane38, javax.swing.GroupLayout.DEFAULT_SIZE, 622, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane38, javax.swing.GroupLayout.DEFAULT_SIZE, 446, Short.MAX_VALUE)
                .addGap(130, 130, 130))
        );

        jSplitPane2.setRightComponent(jPanel11);

        jLabel178.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel178.setText("Cuenta:");

        jCmBoxIdCancel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCmBoxIdCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCmBoxIdCancelActionPerformed(evt);
            }
        });

        jButton38.setFont(new java.awt.Font("Tahoma", 2, 12)); // NOI18N
        jButton38.setText("Esta Semana");
        jButton38.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton38ActionPerformed(evt);
            }
        });

        jButton40.setFont(new java.awt.Font("Tahoma", 2, 12)); // NOI18N
        jButton40.setText("Otra Semana");
        jButton40.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton40ActionPerformed(evt);
            }
        });

        jLabel179.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel179.setText("Turno:");

        jLabTurnCancel.setFont(new java.awt.Font("Tahoma", 3, 16)); // NOI18N
        jLabTurnCancel.setText("--");
        jLabTurnCancel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSplitPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 1298, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel178, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCmBoxIdCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton38, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton40, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(66, 66, 66)
                        .addComponent(jLabel179, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabTurnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(282, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel178, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCmBoxIdCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton38, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
                    .addComponent(jButton40, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
                    .addComponent(jLabel179, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabTurnCancel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jSplitPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 589, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(158, 158, 158))
        );

        jTabbedPane1.addTab("Cancelaciones", new javax.swing.ImageIcon(getClass().getResource("/image/exit.png")), jPanel7); // NOI18N

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1590, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 809, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Ingresos", new javax.swing.ImageIcon(getClass().getResource("/image/nomina.png")), jPanel8); // NOI18N

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1590, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 809, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Egresos", new javax.swing.ImageIcon(getClass().getResource("/image/pays.png")), jPanel9); // NOI18N

        javax.swing.GroupLayout jPanPagosLayout = new javax.swing.GroupLayout(jPanPagos);
        jPanPagos.setLayout(jPanPagosLayout);
        jPanPagosLayout.setHorizontalGroup(
            jPanPagosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        jPanPagosLayout.setVerticalGroup(
            jPanPagosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );

        paneAltas.addTab("REPORTES", jPanPagos);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanCabezera, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(paneAltas, javax.swing.GroupLayout.DEFAULT_SIZE, 1600, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanCabezera, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(paneAltas, javax.swing.GroupLayout.DEFAULT_SIZE, 842, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jComboAltasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboAltasActionPerformed
        int var = jComboAltas.getSelectedIndex();
        if (var == 0) {
            fn.setBorder(jPanAdminist, "Formulario Clientes");
            fn.setBorder(jPanVistaAlta, "Vista Clientes");
            txtQuintoAltas.setVisible(true);
            jTextApellidos.setVisible(true);
            jLabel21.setVisible(false);
            jCBTypeProv.setVisible(false);
            atribAltaCli = "clientepedidos";
            mostrarTablaAltaCli(var, "", "nombre");
        }
        if (var == 1) {
            fn.setBorder(jPanAdminist, "Formulario Proveedor");
            fn.setBorder(jPanVistaAlta, "Vista Proveedores");
            txtQuintoAltas.setVisible(false);
            jTextApellidos.setVisible(true);
            jLabel21.setVisible(true);
            jCBTypeProv.setVisible(true);
            atribAltaCli = "proveedor";
            mostrarTablaAltaCli(var, "", "nombreP");
        }
        if (var == 2) {
            fn.setBorder(jPanAdminist, "Formulario Empleados");
            fn.setBorder(jPanVistaAlta, "Vista Empleados");
            txtQuintoAltas.setVisible(false);
            jTextApellidos.setVisible(true);
            jLabel21.setVisible(false);
            jCBTypeProv.setVisible(false);
            atribAltaCli = "empleado";
            mostrarTablaAltaCli(var, "", "nombreE");
        }
        if (var == 3) {
            fn.setBorder(jPanAdminist, "Formulario Fletero");
            fn.setBorder(jPanVistaAlta, "Vista Fleteros");
            txtQuintoAltas.setVisible(false);
            jTextApellidos.setVisible(false);
            jLabel21.setVisible(false);
            jCBTypeProv.setVisible(false);
            atribAltaCli = "fletero";
            mostrarTablaAltaCli(var, "", "nombreF");
        }
        jButaltasGuardar.setEnabled(true);
        limpiaCamposAltaCli();
        jButAltasActualiza.setEnabled(false);
    }//GEN-LAST:event_jComboAltasActionPerformed

    private void jButAltasActualizaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButAltasActualizaActionPerformed
        int var = jComboAltas.getSelectedIndex();
        List<String> contentL = new ArrayList<String>();
        String var1 = jTextNombre.getText(),
                var2 = jTextApellidos.getText(),
                var3 = jTextLocalidad.getText(),
                var4 = jTexTelefono.getText(),
                var5 = txtQuintoAltas.getText(),
                var6 = fn.getFecha(jDateChFechaAlta), radio = "", isPar = txtIdParam.getText();
        if (jRad1Activo.isSelected()) {
            radio = "1";
        } else if (jRadInactivo.isSelected()) {
            radio = "0";
        }
        contentL.add((var1.isEmpty()) ? "/" : var1);
        contentL.add((var2.isEmpty()) ? "/" : var2);
        contentL.add((var3.isEmpty()) ? "/" : var3);
        contentL.add(radio);
        contentL.add(var6);
        contentL.add((var4.isEmpty()) ? "/" : var4);
        contentL.add((var5.isEmpty()) ? "/" : var5);
        contentL.add(Integer.toString(jCBTypeProv.getSelectedIndex()));
        switch (var) {
            case 0:
                controlInserts.actualizaData("clientepedidos", contentL, isPar);
                atribAltaCli = "clientepedidos";
                mostrarTablaAltaCli(var, "", "nombre");
                break;
            case 1:
                controlInserts.actualizaData("proveedor", contentL, isPar);
                atribAltaCli = "proveedor";
                mostrarTablaAltaCli(var, "", "nombreP");
                break;
            case 2:
                controlInserts.actualizaData("empleado", contentL, isPar);
                atribAltaCli = "empleado";
                mostrarTablaAltaCli(var, "", "nombreE");
                break;
            case 3:
                controlInserts.actualizaData("fletero", contentL, isPar);
                atribAltaCli = "fletero";
                mostrarTablaAltaCli(var, "", "nombreF");
                break;
            default:
                break;
        }
        jButaltasGuardar.setEnabled(true);
        limpiaCamposAltaCli();
    }//GEN-LAST:event_jButAltasActualizaActionPerformed

    private void jRad1ActivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRad1ActivoActionPerformed
    }//GEN-LAST:event_jRad1ActivoActionPerformed

    private void jTableAltasCliMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableAltasCliMousePressed
        int opc = jComboAltas.getSelectedIndex();
        List<String> values = new ArrayList<String>();
        if (evt.getClickCount() > 1) {
            jButaltasGuardar.setEnabled(false);
            int fila = jTableAltasCli.getSelectedRow();
            String val = jTableAltasCli.getValueAt(fila, 0).toString();
            if (opc == 0) {
                values = controlInserts.regresaDatos(0, val);
                txtIdParam.setText(values.get(0));
                jTextNombre.setText(values.get(1));
                jTextApellidos.setText(values.get(2));
                jTextLocalidad.setText(values.get(3));
                if (values.get(4).equals("1")) {
                    jRad1Activo.setSelected(true);
                } else if (values.get(4).equals("0")) {
                    jRadInactivo.setSelected(true);
                }
                jDateChFechaAlta.setDate(fn.StringDate(values.get(5)));
                jTexTelefono.setText(values.get(6));
                txtQuintoAltas.setText(values.get(7));
            }
            if (opc == 1) {
                values = controlInserts.regresaDatos(1, val);
                txtIdParam.setText(values.get(0));
                jTextNombre.setText(values.get(1));
                jTextApellidos.setText(values.get(2));
                jTextLocalidad.setText(values.get(3));
                if (values.get(4).equals("1")) {
                    jRad1Activo.setSelected(true);
                } else if (values.get(4).equals("0")) {
                    jRadInactivo.setSelected(true);
                }
                jDateChFechaAlta.setDate(fn.StringDate(values.get(5)));
                jTexTelefono.setText(values.get(6));
                jCBTypeProv.setSelectedIndex(Integer.parseInt(values.get(7)));
            }
            if (opc == 2) {
                values = controlInserts.regresaDatos(2, val);
                txtIdParam.setText(values.get(0));
                jTextNombre.setText(values.get(1));
                jTextApellidos.setText(values.get(2));
                jTextLocalidad.setText(values.get(3));
                if (values.get(4).equals("1")) {
                    jRad1Activo.setSelected(true);
                } else if (values.get(4).equals("0")) {
                    jRadInactivo.setSelected(true);
                }
                jDateChFechaAlta.setDate(fn.StringDate(values.get(5)));
                jTexTelefono.setText(values.get(6));
            }
            if (opc == 3) {
                values = controlInserts.regresaDatos(3, val);
                txtIdParam.setText(values.get(0));
                jTextNombre.setText(values.get(1));
                jTextLocalidad.setText(values.get(2));
                if (values.get(3).equals("1")) {
                    jRad1Activo.setSelected(true);
                } else if (values.get(3).equals("0")) {
                    jRadInactivo.setSelected(true);
                }
                jDateChFechaAlta.setDate(fn.StringDate(values.get(4)));
                jTexTelefono.setText(values.get(5));
            }
        }
        jButAltasActualiza.setEnabled(true);
    }//GEN-LAST:event_jTableAltasCliMousePressed

    private void jRadBCompraProveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadBCompraProveActionPerformed
        if (jRadBCompraProve.isSelected()) {
            jPanCompraProoved.setVisible(true);
            jPanPrestamoProovedor.setVisible(false);
            jPanBusquedaPrest.setVisible(false);
            jPanClientOption.setVisible(false);
            jCheckBox1.setSelected(false);//checkBox para cargar solo a proveedores mayoristas al jItem

            llenacomboProducts();
            llenacomboProovedores();
            jDateFechCompraProv.setDate(cargafecha());
            
            DefaultTableModel dtmDeldias = (DefaultTableModel) jTabVistaComprasDia.getModel();
            int fil = dtmDeldias.getRowCount();
            if (fil > 0) {
                for (int i = 0; i < fil; i++) {
                    dtmDeldias.removeRow(0);
                }
            }   
            for (int i = 0; i < jTabSumTotales.getColumnCount(); i++) {
                        jTabSumTotales.setValueAt("", 0,i);
                        jTabSumTotales.setValueAt("", 1,i); 
            }
            cargaComprasDia(fn.getFecha(jDateFechCompraProv));
            cargaTotCompDayProveedor(jTabSumTotales,fn.getFecha(jDateFechCompraProv),0);
            jLabLetreroFletes.setVisible(false);
            jButton13.setEnabled(true);//deshabilitao
            jTextField1.setText("0.00");
            DefaultTableModel dtmDel = (DefaultTableModel) jTabDetallecompraAll.getModel();
            fil = dtmDel.getRowCount();
            if (fil > 0) {
                for (int i = 0; i < fil; i++) {
                    dtmDel.removeRow(0);
                }
            }
            jCElijaProovedor.requestFocus(true);
        }
    }//GEN-LAST:event_jRadBCompraProveActionPerformed

    private void jMnRealPayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMnRealPayActionPerformed
        int fila = jTablefiltrosBusq.getSelectedRow();
        String Turn = jLabTurno.getText();
        String val = "", name = "", tot = "";
        if (fila == -1) {
            JOptionPane.showMessageDialog(null, "Debe elegir una fila.");
        } else {
            val = jTablefiltrosBusq.getValueAt(fila, 0).toString();
            name = jTablefiltrosBusq.getValueAt(fila, 4).toString();
            tot = jTablefiltrosBusq.getValueAt(fila, 6).toString();
       //     System.out.println(val);
            vP = new VentaPiso(tot, val, "pagopedidocli");
            vP.setVisible(true);
            vP.setEnabled(true);
            vP.validate();
            vP.jLabLetreroTransac.setText("Pago del pedido No.");
            vP.txtidComp.setText(val);
            vP.jLabNameProv.setText("Cliente: ");
            vP.txtProveedorName.setText(name);
            vP.jLabTurno.setText(Turn);
        }
    }//GEN-LAST:event_jMnRealPayActionPerformed

    private void jRadBPrestamoProvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadBPrestamoProvActionPerformed
        if (jRadBPrestamoProv.isSelected()) {
            jPanCompraProoved.setVisible(false);
            jPanPrestamoProovedor.setVisible(true);
            jPanBusquedaPrest.setVisible(false);
           for (int i = 0; i < jTabSumTotales.getColumnCount(); i++) {
                        jTabSumTotales.setValueAt("", 0,i);
                        jTabSumTotales.setValueAt("", 1,i); 
            }
            llenacomboProovedores();
            llenacomboProdPrest();
            String datePed = fn.getFecha(jDatFechaPrest);

            String[][] mat = controlInserts.matrizPrestaProv(datePed);
            jTabVistaPresta.setModel(new TModel(mat, cabPrest));
            jTextField2.setText("0.00");
        }
    }//GEN-LAST:event_jRadBPrestamoProvActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        int opc = jComBPrestamosProv.getSelectedIndex(),//index de proveedor
                codP = jComBProveedor.getSelectedIndex();//index de producto
        importes.add(txtImportPres.getText());
        BigDecimal aux, tot, other;
        String[] ultimo = null;
        String id_cli = idProoved.get(opc),//idProveedor devuelto de la base de datos
                codPro = idProdPrest.get(codP),//idProducto devuelto de la base de datos
                costoProd = txtPrecProov.getText(),
                cantidad = txtCantPres.getText(),//indexCliente
                nota = textANotaPrestProv.getText(),
                importe = txtImportPres.getText(),
                datePed = fn.getFecha(jDatFechaPrest),
                TOTaLL = jTextField2.getText(),
                ora = fn.getHour();

        List<String> dataPrestamo = new ArrayList<String>();
        if (cantidad.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar una cantidad");
        } else {
            dtm = (DefaultTableModel) jTabdetPrestamoProv.getModel();//obtenemos modelo de tablaVista Compra a Mayorista

            int filas = dtm.getRowCount(),
                    column = dtm.getColumnCount();
            //JOptionPane.showMessageDialog(null, "filas= "+filas+"\n Col= "+column);
            if (filas == 0) {
                dtm.setRowCount(1);
                jTabdetPrestamoProv.setModel(dtm);
//id_ProveedorF,fechaPrestamo,status,notaPrest
                dataPrestamo.add(id_cli);
                dataPrestamo.add(datePed);
                dataPrestamo.add("0");
                if (nota.isEmpty()) {
                    dataPrestamo.add("/");
                } else {
                    dataPrestamo.add(nota);
                }
                dataPrestamo.add(ora);
                dataPrestamo.add(jLabTurno.getText());
                controlInserts.guardaPrestamoProv(dataPrestamo);

                ultimo = controlInserts.ultimoRegistroPrestamo();// regresa num_credito,id_ProveedorF

                jTabdetPrestamoProv.setValueAt(ultimo[0], 0, 0);
                jTabdetPrestamoProv.setValueAt(codPro, 0, 1);//id de ultima compra guardada
                jTabdetPrestamoProv.setValueAt(jComBProveedor.getSelectedItem().toString(), 0, 2);//nombre de tipo mercancia
                jTabdetPrestamoProv.setValueAt(cantidad, 0, 3);

                if (costoProd.isEmpty()) {
                    costoProd = "0.00";//costo de mercancia unitaria
                    importe = "0.00";//importe de mercancia
                    jTabdetPrestamoProv.setValueAt("0.00", 0, 4);//costo de mercancia unitaria
                    jTabdetPrestamoProv.setValueAt("0.00", 0, 5);//importe de mercancia
                } else {
                    jTabdetPrestamoProv.setValueAt(costoProd, 0, 4);//costo de mercancia unitaria               
                    jTabdetPrestamoProv.setValueAt(importe, 0, 5);//importe de mercancia
                }
                jComBPrestamosProv.setEnabled(false);
                textANotaPrestProv.setEnabled(false);
            } else {
                ultimo = controlInserts.ultimoRegistroPrestamo();
                if (costoProd.isEmpty()) {
                    costoProd = "0.00";//costo de mercancia unitaria
                    importe = "0.00";//importe de mercancia
                }
                dtm.addRow(new Object[]{ultimo[0], codPro, jComBProveedor.getSelectedItem().toString(), cantidad, costoProd, importe});
                jTabdetPrestamoProv.setModel(dtm);

            }
            aux = fn.multiplicaAmount(new BigDecimal(cantidad), new BigDecimal(costoProd));
            tot = fn.getSum(aux, new BigDecimal(TOTaLL));
            jTextField2.setText(tot.toString());
        }//else vacio

        jComBProveedor.requestFocus(true);
        limpiaPrestamoProv();
    }//GEN-LAST:event_jButton7ActionPerformed

    private void txtCantidadCompraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCantidadCompraActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCantidadCompraActionPerformed

    private void txtCantCreaPedidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCantCreaPedidoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCantCreaPedidoActionPerformed

    private void txtNotePedidoCliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNotePedidoCliActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNotePedidoCliActionPerformed

    private void txtCantVentaPisoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCantVentaPisoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCantVentaPisoActionPerformed

    private void txtNotaVentPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNotaVentPActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNotaVentPActionPerformed

    private void jPanVentasPisoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jPanVentasPisoKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {//,VK_F1
            JOptionPane.showMessageDialog(null, "OPRIMIO ESC TECLA");
        }
        int key = evt.getKeyCode();
  //      System.out.println(key);
    }//GEN-LAST:event_jPanVentasPisoKeyReleased

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        String arr[] = controlInserts.ultimoRegistroVentPiso();
    //    System.out.print(arr[0] + " butGuarda " + arr[1]);
        int filas = jTabDescVentaP.getRowCount(),
                column = jTabDescVentaP.getColumnCount();
        List<String> datos = new ArrayList<String>();

        if (filas > 0) {
            for (int i = 0; i < filas; i++) {
                datos.add(arr[0]);
                for (int j = 0; j < column; j++) {
//                     System.out.print("["+jTabDescVentaP.getValueAt(i, j)+"]");
                    if (j == 0) {
                        datos.add(jTabDescVentaP.getValueAt(i, j).toString());
                    }
                    if (j == 2) {
                        datos.add(jTabDescVentaP.getValueAt(i, j).toString());
                    }
                    if (j == 3) {
                        datos.add(jTabDescVentaP.getValueAt(i, j).toString());
                    }
                }
                controlInserts.guardaDetalleVentPiso(datos);
                datos.clear();
            }

            String dateD = fn.getFecha(jDFVentaPiso);

            String[][] mat = controlInserts.matrizVentaPisoDia(dateD);
            jTVistaVentaPisoDia.setModel(new TModel(mat, cabvENTAp));

            jCombCliVentaP.setEnabled(true);
            txtNotaVentP.setEnabled(true);

            String val = arr[0],
                    name = jCombCliVentaP.getSelectedItem().toString();
    //        System.out.println(val);
            //   vP = new VentaPiso(controlInserts.totalCompraProv(val), val);   

            vP = new VentaPiso(txtTotalVentaPiso.getText(), val, "pagoventapiso");
            vP.setVisible(true);
            vP.setEnabled(true);
            vP.validate();
            vP.jLabLetreroTransac.setText("Pago de venta No.");
            vP.txtidComp.setText(val);
            vP.jLabNameProv.setText("Cliente : ");
            vP.txtProveedorName.setText(name);

            txtTotalVentaPiso.setText("0.0");

            dtm = (DefaultTableModel) jTabDescVentaP.getModel(); //TableProducto es el nombre de mi tabla ;)
            for (int i = 0; filas > i; i++) {
                dtm.removeRow(0);
            }

        } else {
            JOptionPane.showMessageDialog(null, "No ha agregado articulos");
        }


    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButaltasGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButaltasGuardarActionPerformed
        int var = jComboAltas.getSelectedIndex();
        List<String> contentL = new ArrayList<String>();
        String var1 = jTextNombre.getText(),
                var2 = jTextApellidos.getText(),
                var3 = jTextLocalidad.getText(),
                var4 = jTexTelefono.getText(),
                var5 = txtQuintoAltas.getText(),
                var6 = fn.getFecha(jDateChFechaAlta);
        if (var1.isEmpty() && var2.isEmpty() && var3.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar por lo menos campo Nombre");
        } else {

            if (var1.isEmpty()) {
                contentL.add("/");
            } else {
                contentL.add(var1);
            }
            contentL.add((var2.isEmpty()) ? "/" : var2);
            contentL.add((var3.isEmpty()) ? "/" : var3);
            contentL.add(var6);
            contentL.add((var4.isEmpty()) ? "/" : var4);
            contentL.add((var5.isEmpty()) ? "/" : var5);
            contentL.add(Integer.toString(jCBTypeProv.getSelectedIndex()));
            switch (var) {
                case 0:

                    controlInserts.insertaCampos("clientepedidos", contentL);
                    atribAltaCli = "clientepedidos";
                    mostrarTablaAltaCli(var, "", "nombre");
                    break;
                case 1:
                    controlInserts.insertaCampos("proveedor", contentL);
                    atribAltaCli = "proveedor";
                    mostrarTablaAltaCli(var, "", "nombreP");
                    break;
                case 2:
                    controlInserts.insertaCampos("empleado", contentL);
                    atribAltaCli = "empleado";
                    mostrarTablaAltaCli(var, "", "nombreE");
                    break;
                case 3:
                    controlInserts.insertaCampos("fletero", contentL);
                    atribAltaCli = "fletero";
                    mostrarTablaAltaCli(var, "", "nombreF");
                    break;
                default:
                    break;
            }

            limpiaCamposAltaCli();
        }//if vacio
    }//GEN-LAST:event_jButaltasGuardarActionPerformed

    private void txtBusqAltasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBusqAltasKeyPressed
        String pal = txtBusqAltas.getText();
        int opc = jComboAltas.getSelectedIndex();

        if (opc == 0) {
            atribAltaCli = "clientepedidos";
            mostrarTablaAltaCli(opc, pal, "nombre");
        }

        if (opc == 1) {
            atribAltaCli = "proveedor";
            mostrarTablaAltaCli(opc, pal, "nombreP");
        }

        if (opc == 2) {
            atribAltaCli = "empleado";
            mostrarTablaAltaCli(opc, pal, "nombreE");
        }

        if (opc == 3) {
            atribAltaCli = "fletero";
            mostrarTablaAltaCli(opc, pal, "nombreF");
        }
    }//GEN-LAST:event_txtBusqAltasKeyPressed

    private void jRadioCreaPedidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioCreaPedidoActionPerformed
        conten = llenacomboAltaClis();
        jDateCHPedido.setDate(cargafecha());
        llenacomboProducts();
        String datePed = fn.getFecha(jDateCHPedido);
        

        limpDetPedidoDia();//eliminar registros de tabla pedidoDetDia
       
        for (int i = 0; i < jTabSumTotalPedido.getColumnCount(); i++) {
            jTabSumTotalPedido.setValueAt("", 0,i);
        }

        //  String[][] mat = controlInserts.matrizPedidos(datePed);
        // jTabPedidosDiaView.setModel(new TModel(mat, cab));
        cargaPedidosDiaDet(datePed);//carga detalle de pedido del dia
        cargaTotPedidoDay(jTabSumTotalPedido,datePed);

        jPanConsulPed.setVisible(false);
        jPanCreaPedido.setVisible(true);
    }//GEN-LAST:event_jRadioCreaPedidoActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        int opc = jCombPedidoClient.getSelectedIndex(),//index de cliente
                codP = jCombProdPedidos.getSelectedIndex();//index de producto

        String id_cli = conten.get(opc),//idCliente devuelto de la base de datos
                codPro = idProducts.get(codP),//idProducto devuelto de la base de datos
                cantidad = txtCantCreaPedido.getText(),//indexCliente
                nota = txtNotePedidoCli.getText(),
                datePed = fn.getFecha(jDateCHPedido),
                ora = fn.getHour();;
        List<String> dataPedidoCli = new ArrayList<String>();
        if (cantidad.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar una cantidad");
        } else {
            dtm = (DefaultTableModel) jTableCreaPedidos.getModel();
            int filas = dtm.getRowCount();
            int column = dtm.getColumnCount();
            //JOptionPane.showMessageDialog(null, "filas= "+filas+"\n Col= "+column);
            if (filas == 0) {
                dtm.setRowCount(1);
                jTableCreaPedidos.setModel(dtm);
                jTableCreaPedidos.setValueAt(cantidad, 0, codP);

                dataPedidoCli.add(id_cli);
                dataPedidoCli.add(datePed);
                if (nota.isEmpty()) {
                    dataPedidoCli.add("/");
                } else {
                    dataPedidoCli.add(nota);
                }
                dataPedidoCli.add(ora);
                dataPedidoCli.add(jLabTurno.getText());
               
                controlInserts.guardaPedidoCli(dataPedidoCli);
                jCombPedidoClient.setEnabled(false);

            } else {
                jTableCreaPedidos.setValueAt(cantidad, 0, codP);
            }
            txtCantCreaPedido.setText("");
            txtNotePedidoCli.setText("");
        }//else vacio
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButGuardaPedidodiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButGuardaPedidodiaActionPerformed
        dtm = (DefaultTableModel) jTableCreaPedidos.getModel();
        int filas = dtm.getRowCount();
        int column = dtm.getColumnCount();
        if (filas > 0) {
            String[] ultimo = controlInserts.ultimoRegistroPedido();
            Object val = null;
            int opc = jCombPedidoClient.getSelectedIndex(),//index de cliente
                    codP = jCombProdPedidos.getSelectedIndex();//index de producto
            String id_cli = conten.get(opc);
            String cantidad = txtCantCreaPedido.getText(), datePed = fn.getFecha(jDateCHPedido);

            //System.out.println("id_cli:"+id_cli+"\n id ped: "+ultimo[1]);
            for (int i = 0; i < column; i++) {
                val = jTableCreaPedidos.getValueAt(0, i);
                if (val != null && !val.toString().isEmpty()) {
                    controlInserts.guardaDetallePedidoCli(ultimo[0], Integer.toString(i + 1), Integer.parseInt(jTableCreaPedidos.getValueAt(0, i).toString()));
                    //System.out.println("pos= "+(i+1)+" "+jTableCreaPedidos.getColumnName(i)+"\t-> val= "+jTableCreaPedidos.getValueAt(0, i).toString());
                }//if null
            }
            cargaPedidosDiaDet(datePed);//carga detalle de pedido del dia
            cargaTotPedidoDay(jTabSumTotalPedido,datePed);

        } else {
            JOptionPane.showMessageDialog(null, "No ha generado pedido para el cliente: " + jCombPedidoClient.getSelectedItem().toString());
        }
        for (int i = 0; i < filas; i++) {
            dtm.removeRow(0);
        }
        jCombPedidoClient.setEnabled(true);
    }//GEN-LAST:event_jButGuardaPedidodiaActionPerformed

    private void jRadioConsulPedidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioConsulPedidoActionPerformed
        conten = llenacomboAltaClis();
        jDateChoB1Cli.setDate(cargafecha());
        jDate2BusqCli.setDate(cargafecha());

        String datePed = fn.getFecha(jDateCHPedido);
        jCombOpcBusqPedido.setVisible(true);
        jComPedBusqCli.setEnabled(true);
        jDateChoB1Cli.setEnabled(false);
        jDate2BusqCli.setEnabled(false);

        jPanConsulPed.setVisible(true);
        jPanCreaPedido.setVisible(false);
    }//GEN-LAST:event_jRadioConsulPedidoActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        int opc = jComPedBusqCli.getSelectedIndex(), elije = jCombOpcBusqPedido.getSelectedIndex(),
                //jCombOpcBusqPedido->Criterio de Busqueda
                codP = jCombProdPedidos.getSelectedIndex();//index de producto
        String fech1 = fn.getFecha(jDateChoB1Cli), fech2 = fn.getFecha(jDate2BusqCli);
        String id_cli = conten.get(opc),var="";//idCliente devuelto de la base de datos
        String[][] mat = null;
        switch (elije) {

            case 0:
                // System.out.println("IdCli: " + id_cli);
                mat = controlInserts.matrizPedidosB(elije, id_cli, "", "");
                jTablefiltrosBusq.setModel(new TModel(mat, cab));
                break;
            case 1:
                //System.out.println("Fechai: " + fech1);
                mat = controlInserts.matrizPedidosB(elije, "", fech1, "");
                jTablefiltrosBusq.setModel(new TModel(mat, cab));
                break;
            case 2:
                //System.out.println("Fecha1: " + fech1 + "Fecha2: " + fech2);
                mat = controlInserts.matrizPedidosB(elije, "", fech1, fech2);
                jTablefiltrosBusq.setModel(new TModel(mat, cab));
                break;
            case 3:
                // System.out.println("idCli: " + id_cli + "Fecha1: " + fech1);
                mat = controlInserts.matrizPedidosB(elije, id_cli, fech1, "");
                jTablefiltrosBusq.setModel(new TModel(mat, cab));
                break;
            case 4:
                //System.out.println("idCli: " + id_cli + "Fecha1: " + fech1 + "Fecha2: " + fech2);
                mat = controlInserts.matrizPedidosB(elije, id_cli, fech1, fech2);
                jTablefiltrosBusq.setModel(new TModel(mat, cab));
                break;
        };
        var = jTablefiltrosBusq.getValueAt(0, 0).toString();
        if(var.equals("NO DATA")){
            jLCountHistorP.setText("0");
            jLabtotaldineroHistorPEd.setText("0.0");
        }else{
            jLCountHistorP.setText(Integer.toString(jTablefiltrosBusq.getRowCount()));
            jLabtotaldineroHistorPEd.setText(totalon(jTablefiltrosBusq,6));
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jCombOpcBusqPedidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCombOpcBusqPedidoActionPerformed
        int elije = jCombOpcBusqPedido.getSelectedIndex();
        switch (elije) {
            case 0:
                jComPedBusqCli.setEnabled(true);
                jDateChoB1Cli.setEnabled(false);
                jDate2BusqCli.setEnabled(false);
                break;
            case 1:
                jComPedBusqCli.setEnabled(false);
                jDateChoB1Cli.setEnabled(true);
                jDate2BusqCli.setEnabled(false);
                break;
            case 2:
                jComPedBusqCli.setEnabled(false);
                jDateChoB1Cli.setEnabled(true);
                jDate2BusqCli.setEnabled(true);
                break;
            case 3:
                jComPedBusqCli.setEnabled(true);
                jDateChoB1Cli.setEnabled(true);
                jDate2BusqCli.setEnabled(false);
                break;
            case 4:
                jComPedBusqCli.setEnabled(true);
                jDateChoB1Cli.setEnabled(true);
                jDate2BusqCli.setEnabled(true);
                break;
        };
    }//GEN-LAST:event_jCombOpcBusqPedidoActionPerformed

    private void jComPedBusqCliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComPedBusqCliActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComPedBusqCliActionPerformed

    private void jTablefiltrosBusqMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTablefiltrosBusqMousePressed
        if (evt.getClickCount() > 1) {
            int fila = jTablefiltrosBusq.getSelectedRow();
            String val = jTablefiltrosBusq.getValueAt(fila, 0).toString();
            if (val.equals("NO DATA")) {
                JOptionPane.showMessageDialog(null, "No hay datos para mostrar.");
            } else {
                List<String> detailPedidoCli = new ArrayList<String>();
                for (int j = 0; j < jTablefiltrosBusq.getColumnCount(); j++) {
                    detailPedidoCli.add(jTablefiltrosBusq.getValueAt(fila, j).toString());
                }
                dP = new detailPedido(Integer.parseInt(val));

                dP.setEnabled(true);
                dP.setVisible(true);
                dP.validate();
                dP.recibeListData(detailPedidoCli);
            }
        }
    }//GEN-LAST:event_jTablefiltrosBusqMousePressed

    private void jComBProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComBProveedorActionPerformed
        int var = jComBProveedor.getSelectedIndex();
        if (var == 0) {
            jLabBNumerador.setText("CAJAS");
        }
        if (var == 1) {
            jLabBNumerador.setText("M.N.");
        }
        if (var >= 2) {
            jLabBNumerador.setText("LIBRA(S)");
        }

    }//GEN-LAST:event_jComBProveedorActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        dtmAux = (DefaultTableModel) jTabdetPrestamoProv.getModel();
        List<String> dataCompra = new ArrayList<String>();//lista para obtener los datos a insertar por cada row de jTable
        int filas = dtmAux.getRowCount();
        int column = dtmAux.getColumnCount();
        String datePed = fn.getFecha(jDatFechaPrest);
        if (filas > 0) {
            String[] ultimo = controlInserts.ultimoRegistroPrestamo();
            int filas2 = dtmAux.getRowCount(),
                    column2 = dtmAux.getColumnCount();
            for (int i = 0; i < filas; i++) {
                for (int j = 0; j < column; j++) {
                    if (j == 0) {
                        dataCompra.add(jTabdetPrestamoProv.getValueAt(i, j).toString());
                    }
                    if (j == 1) {
                        dataCompra.add(jTabdetPrestamoProv.getValueAt(i, j).toString());
                    }
                    if (j == 3) {
                        dataCompra.add(jTabdetPrestamoProv.getValueAt(i, j).toString());
                    }
                    if (j == 4) {
                        dataCompra.add(jTabdetPrestamoProv.getValueAt(i, j).toString());
                    }
                }
                controlInserts.guardaDetallePrestamoProv(dataCompra);
                dataCompra.clear();
            }

        } else {
            JOptionPane.showMessageDialog(null, "No ha generado prestamo para el proveedor: " + jComBPrestamosProv.getSelectedItem().toString());
        }
        DefaultTableModel dtmDel = (DefaultTableModel) jTabdetPrestamoProv.getModel();
        int fil = dtmDel.getRowCount();
        if (fil > 0) {
            for (int i = 0; i < fil; i++) {
                dtmDel.removeRow(0);
            }
        }
        jComBPrestamosProv.setEnabled(true);
        textANotaPrestProv.setEnabled(true);
        jRadBPrestamoProv.doClick();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jRadBConsultaProvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadBConsultaProvActionPerformed
        if (jRadBConsultaProv.isSelected()) {
            jPanCompraProoved.setVisible(false);
            jPanPrestamoProovedor.setVisible(false);
            jPanBusquedaPrest.setVisible(true);
            llenacomboProovedores();
            jDaTFechPrest2.setDate(cargafecha());
            jDaTFechPrest1.setDate(cargafecha());
            jDaTFechComp1.setDate(cargafecha());
            jDaTFechCompraProv2.setDate(cargafecha());
        }
    }//GEN-LAST:event_jRadBConsultaProvActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        int opc = jCombBProvBusqPrest.getSelectedIndex(),//jCombBProvBusqPrest id_Proveedor
                elije = jComBusPrestamo.getSelectedIndex();
        //jCombOpcBusqPedido->Criterio de Busqueda
        String fech1 = fn.getFecha(jDaTFechPrest1), fech2 = fn.getFecha(jDaTFechPrest2);
        String id_cli = idProoved.get(opc),var ="";//idCliente devuelto de la base de datos
        String[][] mat = null;
        /*         dtm = (DefaultTableModel) jTabBusqPrestProv.getModel();
        int filas = dtm.getRowCount();
       
        if (filas > 0) {
            for (int i = 0; i < filas; i++) {
                dtm.removeRow(0);            
            }
        }
         */ switch (elije) {

            case 0:
                mat = controlInserts.matrizPrestaProvOpc(elije, id_cli, "", "");
                jTabBusqPrestProv.setModel(new TModel(mat, cabPrest));
                break;
            case 1:
                //System.out.println("Fechai: " + fech1);
                mat = controlInserts.matrizPrestaProvOpc(elije, "", fech1, "");
                jTabBusqPrestProv.setModel(new TModel(mat, cabPrest));
                break;
            case 2:
                //System.out.println("Fecha1: " + fech1 + "Fecha2: " + fech2);
                mat = controlInserts.matrizPrestaProvOpc(elije, "", fech1, fech2);
                jTabBusqPrestProv.setModel(new TModel(mat, cabPrest));
                break;
            case 3:
                //System.out.println("idCli: " + id_cli + "Fecha1: " + fech1);
                mat = controlInserts.matrizPrestaProvOpc(elije, id_cli, fech1, "");
                jTabBusqPrestProv.setModel(new TModel(mat, cabPrest));
                break;
            case 4:
                //System.out.println("idCli: " + id_cli + "Fecha1: " + fech1 + "Fecha2: " + fech2);
                mat = controlInserts.matrizPrestaProvOpc(elije, id_cli, fech1, fech2);
                jTabBusqPrestProv.setModel(new TModel(mat, cabPrest));
                break;
        };
        var = jTabBusqPrestProv.getValueAt(0, 0).toString();
        if(var.equals("NO DATA")){
            jLabel155.setText("0");
            jLabel169.setText("0.0");
        }else{
            jLabel155.setText(Integer.toString(jTabBusqPrestProv.getRowCount()));
            jLabel169.setText(totalon(jTabBusqPrestProv,5));
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jComBusPrestamoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComBusPrestamoActionPerformed
        int elije = jComBusPrestamo.getSelectedIndex();
        switch (elije) {
            case 0:
                jCombBProvBusqPrest.setEnabled(true);
                jDaTFechPrest1.setEnabled(false);
                jDaTFechPrest2.setEnabled(false);
                break;
            case 1:
                jCombBProvBusqPrest.setEnabled(false);
                jDaTFechPrest1.setEnabled(true);
                jDaTFechPrest2.setEnabled(false);
                break;
            case 2:
                jCombBProvBusqPrest.setEnabled(false);
                jDaTFechPrest1.setEnabled(true);
                jDaTFechPrest2.setEnabled(true);
                break;
            case 3:
                jCombBProvBusqPrest.setEnabled(true);
                jDaTFechPrest1.setEnabled(true);
                jDaTFechPrest2.setEnabled(false);
                break;
            case 4:
                jCombBProvBusqPrest.setEnabled(true);
                jDaTFechPrest1.setEnabled(true);
                jDaTFechPrest2.setEnabled(true);
                break;
        };
    }//GEN-LAST:event_jComBusPrestamoActionPerformed

    private void jTabBusqPrestProvMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTabBusqPrestProvMousePressed
        if (evt.getClickCount() > 1) {
            int fila = jTabBusqPrestProv.getSelectedRow();
            String val = jTabBusqPrestProv.getValueAt(fila, 0).toString();
            //System.out.println("Envia: "+val);

            if (val.equals("NO DATA")) {
                JOptionPane.showMessageDialog(null, "No hay datos para mostrar.");
            } else {
                List<String> detailPedidoCli = new ArrayList<String>();
                for (int j = 0; j < jTabBusqPrestProv.getColumnCount(); j++) {
                    detailPedidoCli.add(jTabBusqPrestProv.getValueAt(fila, j).toString());
                }
                dPresta = new detallePrestamo(Integer.parseInt(val));
                dPresta.setEnabled(true);
                dPresta.setVisible(true);
                dPresta.validate();
                dPresta.recibeListData(detailPedidoCli);
            }
        }
    }//GEN-LAST:event_jTabBusqPrestProvMousePressed

    private void jTabVistaPrestaMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTabVistaPrestaMousePressed
        if (evt.getClickCount() > 1) {
            int fila = jTabVistaPresta.getSelectedRow();
            String val = jTabVistaPresta.getValueAt(fila, 0).toString();
         //   System.out.println("Envia: " + val);

            if (val.equals("No Data")) {
                JOptionPane.showMessageDialog(null, "No hay datos para mostrar.");
            } else {
                List<String> detailPedidoCli = new ArrayList<String>();
                for (int j = 0; j < jTabVistaPresta.getColumnCount(); j++) {
                    detailPedidoCli.add(jTabVistaPresta.getValueAt(fila, j).toString());
                }
                dPresta = new detallePrestamo(Integer.parseInt(val));
                dPresta.setEnabled(true);
                dPresta.setVisible(true);
                dPresta.validate();
                dPresta.recibeListData(detailPedidoCli);
            }
        }
    }//GEN-LAST:event_jTabVistaPrestaMousePressed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        int opc = jCElijaProovedor.getSelectedIndex(),//index de proveedor
                codP = jCombProductProv.getSelectedIndex();//index de producto
        BigDecimal aux, tot, other;

        String id_cli = idProoved.get(opc),//idProveedor devuelto de la base de datos
                codPro = idProducts.get(codP),//idProducto devuelto de la base de datos
                costoProd = txtPrecCompraProv.getText(),
                cantidad = txtCantidadCompra.getText(),//indexCliente
                nota = txtNotaCompra.getText(),
                importe = txtImportComp.getText(),
                descriSubasta = txtCamSubastaCompra.getText(),
                datePed = fn.getFecha(jDateFechCompraProv),
                TOTaLL = jTextField1.getText(),
                ora = fn.getHour();
        //   System.err.println("variables: "+costoProd+" -> "+ cantidad+ " -> "+importe);
        String[] ultimo = controlInserts.ultimoRegistroCompra();//regresamos el id de la ultima compra realizada 

        List<String> dataCompra = new ArrayList<String>();
        if (cantidad.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar una cantidad");
        } else {
            if (!jPanSubastaOption.isVisible()) {
                descriSubasta = "/";
            }
            if (nota.isEmpty()) {
                nota = "/";
            }
            dtmAux = (DefaultTableModel) jTabDetallecompraAll.getModel();//obtenemos modelo de tablaVista Compra a Mayorista

            int filas = dtmAux.getRowCount();
            int column = dtmAux.getColumnCount();
            //JOptionPane.showMessageDialog(null, "filas= "+filas+"\n Col= "+column);
            if (filas == 0) {
                dtmAux.setRowCount(1);//agrega una fila
                jTabDetallecompraAll.setModel(dtmAux);

//id_ProveedorF,fechaPrestamo,status,notaPrest
                dataCompra.add(id_cli);
                dataCompra.add(datePed);
                dataCompra.add(descriSubasta);
                if (nota.isEmpty()) {
                    dataCompra.add("/");
                } else {
                    dataCompra.add(nota);
                }
                dataCompra.add("0");
                //AGREGADOS POR MDIFICACION TABLA
                dataCompra.add(ora);
                dataCompra.add(jLabTurno.getText());

                controlInserts.guardaCompraProv(dataCompra);//**********COMPRA A MAYORISTAS
                ultimo = controlInserts.ultimoRegistroCompra();
                dtmAux.setRowCount(1);//agrega una fila
                jTabDetallecompraAll.setValueAt(ultimo[0], 0, 0);//codigo de producto
                jTabDetallecompraAll.setValueAt(codPro, 0, 1);//id de ultima compra guardada
                jTabDetallecompraAll.setValueAt(jCombProductProv.getSelectedItem().toString(), 0, 2);//nombre de tipo mercancia
                jTabDetallecompraAll.setValueAt(cantidad, 0, 3);//tipo de mercancia
                if (costoProd.isEmpty()) {
                    costoProd = "0.00";//costo de mercancia unitaria
                    importe = "0.00";//importe de mercancia
                    jTabDetallecompraAll.setValueAt("0.00", 0, 4);//costo de mercancia unitaria
                    jTabDetallecompraAll.setValueAt("0.00", 0, 5);//importe de mercancia
                } else {
                    jTabDetallecompraAll.setValueAt(costoProd, 0, 4);//costo de mercancia unitaria               
                    jTabDetallecompraAll.setValueAt(importe, 0, 5);//importe de mercancia
                }
                //   jTabDetallecompraAll.setValueAt(importe, 0, 5);//importe de mercancia
                if (jCheckBox1.isSelected()) {//preparamos campo para compra a mayorista
                    controlInserts.guardaMayorista(id_cli, ultimo[0], datePed);//Asignamos compra a mayoristaRelacion
                }
                jCElijaProovedor.setEnabled(false);
                txtNotaCompra.setEnabled(false);
            } else {//si ya tiene una fila almenos la vista de compra a proveedor normal
                ultimo = null;
                ultimo = controlInserts.ultimoRegistroCompra();

                if (costoProd.isEmpty()) {
                    costoProd = "0.00";//costo de mercancia unitaria
                    importe = "0.00";//importe de mercancia
                }
                dtmAux.addRow(new Object[]{ultimo[0], codPro, jCombProductProv.getSelectedItem().toString(), cantidad, costoProd, importe});
                jTabDetallecompraAll.setModel(dtmAux);
            }
            aux = fn.multiplicaAmount(new BigDecimal(cantidad), new BigDecimal(costoProd));
            tot = fn.getSum(aux, new BigDecimal(TOTaLL));
            jTextField1.setText(tot.toString());
        }//else vacio
        importes.add(txtImportComp.getText());
        limpiacompraProved();
        jCombProductProv.requestFocus(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void txtPrecCompraProvFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPrecCompraProvFocusLost

    }//GEN-LAST:event_txtPrecCompraProvFocusLost

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        dtmAux = (DefaultTableModel) jTabDetallecompraAll.getModel();
        List<String> dataCompra = new ArrayList<String>();//lista para obtener los datos a insertar por cada row de jTable
        List<String> dataLsi = new ArrayList<String>();//lista para obtener los datos a insertar por cada row de jTable
        int filas = dtmAux.getRowCount();
        int column = dtmAux.getColumnCount();
        String cantidad = txtCantPres.getText(),
                datePed = fn.getFecha(jDateFechCompraProv),
                numC = "", notaC = "";
        if (filas > 0) {
            String[] ultimo = controlInserts.ultimoRegistroCompra();//probar a cambiar guardar ID de compra para no hacer tantas consultas a la DB
            for (int i = 0; i < ultimo.length; i++) {
         //       System.out.print("[" + ultimo[i] + "]");
            }

            Object val = null;
            int opc = jCElijaProovedor.getSelectedIndex(),//index de PROVEEDOR
                    codP = jCombProductProv.getSelectedIndex();//index de producto
            String id_cli = idProoved.get(opc);
            int filas2 = dtmAux.getRowCount(),
                    column2 = dtmAux.getColumnCount();

            for (int i = 0; i < filas; i++) {
                //dataCompra.add(ultimo[0]);
                for (int j = 0; j < column; j++) {
                    //                     System.out.print("["+jTabDescVentaP.getValueAt(i, j)+"]");
                    if (j == 0) {
                        dataCompra.add(jTabDetallecompraAll.getValueAt(i, j).toString());
                    }
                    if (j == 1) {
                        dataCompra.add(jTabDetallecompraAll.getValueAt(i, j).toString());
                    }
                    if (j == 3) {
                        dataCompra.add(jTabDetallecompraAll.getValueAt(i, j).toString());
                    }
                    if (j == 4) {
                        dataCompra.add(jTabDetallecompraAll.getValueAt(i, j).toString());
                    }
                }
                controlInserts.guardaDetalleCompraProv(dataCompra.get(0), dataCompra.get(1), Integer.parseInt(dataCompra.get(2)), dataCompra.get(3));
                numC = dataCompra.get(0);
                dataCompra.clear();
            }

            if (jCheckbpAGADO.isSelected()) {
                String code = "";
                code = JOptionPane.showInputDialog(null, "Ingrese nota de pago");
                dataLsi.add(numC);
                dataLsi.add(datePed);
                dataLsi.add(jTextField1.getText());
                dataLsi.add(code);
                dataLsi.add("1");
                dataLsi.add(fn.getHour());
                dataLsi.add(jLabTurno.getText());
                
                controlInserts.guardaPayCompraContrl(dataLsi, "pagarcompraprovee");
                controlInserts.actualizaDataC(numC, "compraprooved",1);
            }

        } else {
            JOptionPane.showMessageDialog(null, "No ha generado compra para el proveedor: " + jCElijaProovedor.getSelectedItem().toString());
        }
        //      cargaComprasDia(datePed);//carga las compras del dia
        //      cargaTotCompDayProveedor(datePed);//suma de tipos de 
        DefaultTableModel dtmDel = (DefaultTableModel) jTabDetallecompraAll.getModel();
        int fil = dtmDel.getRowCount();
        if (fil > 0) {
            for (int i = 0; i < fil; i++) {
                dtmDel.removeRow(0);
            }
        }
        jCElijaProovedor.setEnabled(true);
        txtNotaCompra.setEnabled(true);
        importes.clear();
        jPanClientOption.setVisible(false);
        jPanSubastaOption.setVisible(false);
        jCheckbpAGADO.setSelected(false);
        jTextField1.setText("0.00");
        jRadBCompraProve.doClick();
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jMenPagoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenPagoActionPerformed
        int fila = jTabVistaComprasDia.getSelectedRow();
        String val = "", name = "";
        if (fila == -1) {
            JOptionPane.showMessageDialog(null, "Debe elegir una fila.");
        } else {
            val = jTabVistaComprasDia.getValueAt(fila, 0).toString();
            name = jTabVistaComprasDia.getValueAt(fila, 1).toString();
            //System.out.println(val);
            vP = new VentaPiso(controlInserts.totalCompraProv(val), val, "pagarcompraprovee");
            vP.setVisible(true);
            vP.setEnabled(true);
            vP.validate();
            vP.jLabLetreroTransac.setText("Pago de compra No.");
            vP.txtidComp.setText(val);
            vP.jLabNameProv.setText("Proveedor: ");
            vP.txtProveedorName.setText(name);
        }
    }//GEN-LAST:event_jMenPagoActionPerformed

    private void jComBusCompraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComBusCompraActionPerformed
        int elije = jComBusCompra.getSelectedIndex();
        switch (elije) {
            case 0:
                jCombBProvBusqCompra.setEnabled(true);
                llenacomboProovedores();
                jDaTFechComp1.setEnabled(false);
                jDaTFechCompraProv2.setEnabled(false);
                break;
            case 1:
                jCombBProvBusqCompra.setEnabled(true);
                llenacomboMayoristas(1);
                jDaTFechComp1.setEnabled(false);
                jDaTFechCompraProv2.setEnabled(false);
                break;
            case 2:
                jCombBProvBusqCompra.setEnabled(true);
                llenacomboMayoristas(1);
                jDaTFechComp1.setEnabled(true);
                jDaTFechCompraProv2.setEnabled(false);
                break;
            case 3:
                jCombBProvBusqCompra.setEnabled(false);
                jDaTFechComp1.setEnabled(true);
                jDaTFechCompraProv2.setEnabled(false);
                break;
            case 4:
                jCombBProvBusqCompra.setEnabled(false);
                jDaTFechComp1.setEnabled(true);
                jDaTFechCompraProv2.setEnabled(true);
                break;
            case 5:
                jCombBProvBusqCompra.setEnabled(true);
                jDaTFechComp1.setEnabled(true);
                llenacomboProovedores();
                jDaTFechCompraProv2.setEnabled(false);
                break;
            case 6:
                jCombBProvBusqCompra.setEnabled(true);
                jDaTFechComp1.setEnabled(true);
                jDaTFechCompraProv2.setEnabled(true);
                llenacomboProovedores();
                break;
        };

    }//GEN-LAST:event_jComBusCompraActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        int opc = jCombBProvBusqCompra.getSelectedIndex(),//jCombBProvBusqPrest id_Proveedor
                elije = jComBusCompra.getSelectedIndex();
        //jCombOpcBusqPedido->Criterio de Busqueda
        String fech1 = fn.getFecha(jDaTFechComp1), fech2 = fn.getFecha(jDaTFechCompraProv2);
        String id_cli = idProoved.get(opc),var ="";//idCliente devuelto de la base de datos
        String[][] mat = null;

        switch (elije) {
            case 0://CLIENTE
                mat = controlInserts.matrizCompraProvOpc(elije, id_cli, "", "");
                jTabBusqCompraProv1.setModel(new TModel(mat, cabCompra));
                break;
            case 1://MAYORISTA
                mat = controlInserts.matrizCompraProvOpc(elije, id_cli, "", "");
                jTabBusqCompraProv1.setModel(new TModel(mat, cabCompra));
                break;
            case 2: //MAYORISTA+FECHA
                mat = controlInserts.matrizCompraProvOpc(elije, id_cli, fech1, "");
                jTabBusqCompraProv1.setModel(new TModel(mat, cabCompra));
                break;
            case 3://FECHA
                //System.out.println("Fechai: " + fech1);
                mat = controlInserts.matrizCompraProvOpc(elije, "", fech1, "");
                jTabBusqCompraProv1.setModel(new TModel(mat, cabCompra));
                break;
            case 4://LAPSO DE FECHAS
                //System.out.println("Fecha1: " + fech1 + "Fecha2: " + fech2);
                mat = controlInserts.matrizCompraProvOpc(elije, "", fech1, fech2);
                jTabBusqCompraProv1.setModel(new TModel(mat, cabCompra));
                break;
            case 5://CLIENTE+FECHA
                // System.out.println("idCli: " + id_cli + "Fecha1: " + fech1);
                mat = controlInserts.matrizCompraProvOpc(elije, id_cli, fech1, "");
                jTabBusqCompraProv1.setModel(new TModel(mat, cabCompra));
                break;
            case 6://CLIENTE+LAPSO DE FECHAS
                //System.out.println("idCli: " + id_cli + "Fecha1: " + fech1 + "Fecha2: " + fech2);
                mat = controlInserts.matrizCompraProvOpc(elije, id_cli, fech1, fech2);
                jTabBusqCompraProv1.setModel(new TModel(mat, cabPrest));
                break;
        };
       var = jTabBusqCompraProv1.getValueAt(0, 0).toString();
        if(var.equals("NO DATA")){
            jLabel171.setText("0");
            jLabel173.setText("0.0");
        }else{
            jLabel171.setText(Integer.toString(jTabBusqCompraProv1.getRowCount()));
            jLabel173.setText(totalon(jTabBusqCompraProv1,5));
        }
    }//GEN-LAST:event_jButton14ActionPerformed

    private void jMPAYFILTROActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMPAYFILTROActionPerformed
        int fila = jTabBusqCompraProv1.getSelectedRow();
        String val = "", name = "";
        if (fila == -1) {
            JOptionPane.showMessageDialog(null, "Debe elegir una fila.");
        } else {
            val = jTabBusqCompraProv1.getValueAt(fila, 0).toString();
            name = jTabBusqCompraProv1.getValueAt(fila, 4).toString();
      //      System.out.println(val);
            vP = new VentaPiso(controlInserts.totalCompraProv(val), val, "pagarcompraprovee");
            vP.setVisible(true);
            vP.setEnabled(true);
            vP.validate();
            vP.jLabLetreroTransac.setText("Pago de compra No.");
            vP.txtidComp.setText(val);
            vP.jLabNameProv.setText("Proveedor: ");
            vP.txtProveedorName.setText(name);
            vP.jLabTurno.setText(jLabTurno.getText());
        }

    }//GEN-LAST:event_jMPAYFILTROActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        llenacomboProducts();
        llenacomboProovedores();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jRadCreaVentaPisoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadCreaVentaPisoActionPerformed
        jPaNVentaPiso.setVisible(true);
        jPanBusqVentasPiso.setVisible(false);
        jDFVentaPiso.setDate(cargafecha());
        String datePed = fn.getFecha(jDFVentaPiso);

        String[][] mat = controlInserts.matrizVentaPisoDia(datePed);
        jTVistaVentaPisoDia.setModel(new TModel(mat, cabvENTAp));
        llenacomboAltaClis();
        llenacomboProductsVenta();
        txtTotalVentaPiso.setText("0.0");
    }//GEN-LAST:event_jRadCreaVentaPisoActionPerformed

    private void txtPrecProdVentaPFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPrecProdVentaPFocusLost
        String cant = txtCantVentaPiso.getText(),
                cos = txtPrecProdVentaP.getText();
        // txtImportVentaP.setText("");

        if (cant.isEmpty() || cos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Campo cantidad o monto vacios\n Verifique Por favor");
        } else {
            BigDecimal amountOne = new BigDecimal(cant);//monto a cobrar
            BigDecimal amountTwo = new BigDecimal(cos);//cantidad recivida
            txtImportVentaP.setText(fn.multiplicaAmount(amountOne, amountTwo).toString());
        }
    }//GEN-LAST:event_txtPrecProdVentaPFocusLost

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        int opc = jCombCliVentaP.getSelectedIndex(),//index de CLIENTE
                codP = jCombProdVentaP.getSelectedIndex();//index de producto

        String id_cli = conten.get(opc),//idProveedor devuelto de la base de datos
                codPro = idProductsVent.get(codP),//idProducto devuelto de la base de datos

                costoProd = txtPrecProdVentaP.getText(),
                cantidad = txtCantVentaPiso.getText(),//indexCliente
                nota = txtNotaVentP.getText(),
                importe = txtImportVentaP.getText(),
                dateVentP = fn.getFecha(jDFVentaPiso),
                sumando = txtTotalVentaPiso.getText();

        importes.add(txtImportVentaP.getText());

        List<String> dataVentPiso = new ArrayList<String>();
        if (cantidad.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar una cantidad");
        } else {
            dtm = (DefaultTableModel) jTabDescVentaP.getModel();

            int filas = dtm.getRowCount(), filasPrec = dtm.getRowCount();
            int column = dtm.getColumnCount(), column2 = dtm.getColumnCount();
            if (filas == 0) {
                if (nota.isEmpty()) {
                    nota = "/";
                }
                dataVentPiso.add(id_cli);
                dataVentPiso.add(dateVentP);
                dataVentPiso.add(nota);
                dataVentPiso.add(fn.getHour());
                dataVentPiso.add(jLabTurno.getText());
                controlInserts.guardaVentaPiso(dataVentPiso);
            }
            dtm.addRow(new Object[]{codPro, jCombProdVentaP.getSelectedItem().toString(), cantidad, costoProd, importe});
            jTabDescVentaP.setModel(dtm);
            BigDecimal amountOne = new BigDecimal(sumando);//monto a cobrar
            BigDecimal amountTwo = new BigDecimal(importe);//cantidad recivida

            txtTotalVentaPiso.setText(fn.getSum(amountOne, amountTwo).toString());

            limpiaVentaPiso();
            jCombCliVentaP.setEnabled(false);
            txtNotaVentP.setEnabled(false);
        }//else vacio
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jTabDescVentaPMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTabDescVentaPMousePressed
        String total = txtTotalVentaPiso.getText(),
                restar = jTabDescVentaP.getValueAt(jTabDescVentaP.getSelectedRow(), 4).toString();
        BigDecimal amountOne = new BigDecimal(total);//monto a cobrar
        BigDecimal amountTwo = new BigDecimal(restar);//cantidad recivida
        txtTotalVentaPiso.setText(fn.getDifference(amountOne, amountTwo).toString());
        DefaultTableModel dtmDel = (DefaultTableModel) jTabDescVentaP.getModel(); //TableProducto es el nombre de mi tabla ;)
        dtmDel.removeRow(jTabDescVentaP.getSelectedRow());
    }//GEN-LAST:event_jTabDescVentaPMousePressed

    private void jCombOpcBusqVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCombOpcBusqVentaActionPerformed
        int elije = jCombOpcBusqVenta.getSelectedIndex();
        switch (elije) {
            case 0://cliente
                jCCliVentaPiso.setEnabled(true);
                jDateChoBVent1.setEnabled(false);
                jDatebusqVenta2.setEnabled(false);
                break;
            case 1://fecha
                jCCliVentaPiso.setEnabled(false);
                jDateChoBVent1.setEnabled(true);
                jDatebusqVenta2.setEnabled(false);
                break;
            case 2://lapso fechas
                jCCliVentaPiso.setEnabled(false);
                jDateChoBVent1.setEnabled(true);
                jDatebusqVenta2.setEnabled(true);
                break;
            case 3://cliente+fecha
                jCCliVentaPiso.setEnabled(true);
                jDateChoBVent1.setEnabled(true);
                jDatebusqVenta2.setEnabled(false);
                break;
            case 4://cliente+lapsoFechas
                jCCliVentaPiso.setEnabled(true);
                jDateChoBVent1.setEnabled(true);
                jDatebusqVenta2.setEnabled(true);
                break;
        };
    }//GEN-LAST:event_jCombOpcBusqVentaActionPerformed

    private void jCCliVentaPisoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCCliVentaPisoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCCliVentaPisoActionPerformed

    private void jTablefiltrosBusqVentMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTablefiltrosBusqVentMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTablefiltrosBusqVentMousePressed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        int opc = jCCliVentaPiso.getSelectedIndex(), elije = jCombOpcBusqVenta.getSelectedIndex();
        //jCombOpcBusqPedido->Criterio de Busqueda
        String fech1 = fn.getFecha(jDateChoBVent1), fech2 = fn.getFecha(jDatebusqVenta2);
        String id_cli = conten.get(opc),var = "";//idCliente devuelto de la base de datos
        String[][] mat = null;
        switch (elije) {

            case 0:
                //System.out.println("IdCli: " + id_cli);
                mat = controlInserts.matrizVentasDia(elije, id_cli, "", "");
                jTablefiltrosBusqVent.setModel(new TModel(mat, cabvENTAp));
                break;
            case 1:
                //System.out.println("Fechai: " + fech1);
                mat = controlInserts.matrizVentasDia(elije, "", fech1, "");
                jTablefiltrosBusqVent.setModel(new TModel(mat, cabvENTAp));
                break;
            case 2:
                //System.out.println("Fecha1: " + fech1 + "Fecha2: " + fech2);
                mat = controlInserts.matrizVentasDia(elije, "", fech1, fech2);
                jTablefiltrosBusqVent.setModel(new TModel(mat, cabvENTAp));
                break;
            case 3:
                //System.out.println("idCli: " + id_cli + "Fecha1: " + fech1);
                mat = controlInserts.matrizVentasDia(elije, id_cli, fech1, "");
                jTablefiltrosBusqVent.setModel(new TModel(mat, cabvENTAp));
                break;
            case 4:
                // System.out.println("idCli: " + id_cli + "Fecha1: " + fech1 + "Fecha2: " + fech2);
                mat = controlInserts.matrizVentasDia(elije, id_cli, fech1, fech2);
                jTablefiltrosBusqVent.setModel(new TModel(mat, cabvENTAp));
                break;
        };
       var = jTablefiltrosBusqVent.getValueAt(0, 0).toString();
        if(var.equals("NO DATA")){
            jLabel174.setText("0");
            jLabel176.setText("0.0");
        }else{
            jLabel174.setText(Integer.toString(jTablefiltrosBusqVent.getRowCount()));
            jLabel176.setText(totalon(jTablefiltrosBusqVent,5));
        }
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jRadBusqVentaPisoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadBusqVentaPisoActionPerformed
        jPaNVentaPiso.setVisible(false);
        jPanBusqVentasPiso.setVisible(true);
        jDateChoBVent1.setDate(cargafecha());
        jDatebusqVenta2.setDate(cargafecha());
        llenacomboAltaClis();
    }//GEN-LAST:event_jRadBusqVentaPisoActionPerformed

    private void jMnPayVentaPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMnPayVentaPActionPerformed
        int fila = jTablefiltrosBusqVent.getSelectedRow();
        String val = "", name = "";
        if (fila == -1) {
            JOptionPane.showMessageDialog(null, "Debe elegir una fila.");
        } else {
            val = jTablefiltrosBusqVent.getValueAt(fila, 0).toString();
            name = jTablefiltrosBusqVent.getValueAt(fila, 4).toString();
            //System.out.println(val);
            vP = new VentaPiso(controlInserts.totalVentaPiso(val), val, "pagoventapiso");
            vP.setVisible(true);
            vP.setEnabled(true);
            vP.validate();
            vP.jLabLetreroTransac.setText("Pago de Venta No.");
            vP.txtidComp.setText(val);
            vP.jLabNameProv.setText("Cliente: ");
            vP.txtProveedorName.setText(name);
            vP.jLabTurno.setText(jLabTurno.getText());
        }
    }//GEN-LAST:event_jMnPayVentaPActionPerformed

    private void jCBAltasFletesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBAltasFletesActionPerformed
        int tm = jCBAltasFletes.getItemCount();
        if (tm > 0) {
            txtChoferFlete.setText(jCBAltasFletes.getSelectedItem().toString());
        }
    }//GEN-LAST:event_jCBAltasFletesActionPerformed

    private void jButFleteGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButFleteGuardarActionPerformed
        int var = jCBAltasFletes.getSelectedIndex();
        List<String> contentL = new ArrayList<String>();
        String var1 = txtFolioFlete.getText(),//id_Flete
                var2 = txtChoferFlete.getText(),//id_Fletero
                var3 = txtUnidFlete.getText(),//
                var4 = txtCostoFlete.getText(),
                var5 = txtNotaFlete.getText(),
                var6 = fn.getFecha(jDFechCreaFlete),
                var7 = contenFletes.get(var),
                var8 = txtCargaFlet.getText();
        if (var1.isEmpty() && var2.isEmpty() && var3.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar por lo menos campo Nombre");
        } else {
            contentL.add(var1);
            contentL.add(var7);
            contentL.add(var6);
            contentL.add((var2.isEmpty()) ? "/" : var2);
            contentL.add((var3.isEmpty()) ? "/" : var3);
            contentL.add((var4.isEmpty()) ? "0.0" : var4);
            contentL.add((var5.isEmpty()) ? "/" : var5);
            contentL.add((var8.isEmpty()) ? "1" : var8);
            controlInserts.creaFletes(contentL);
            mostrarTablaFletesDia(var6);
            limpiaFlete();
        }//if vacio   
        jLabLetreroFletes.setVisible(false);

    }//GEN-LAST:event_jButFleteGuardarActionPerformed

    private void jButAltasActualiza1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButAltasActualiza1ActionPerformed
        int var = jCBAltasFletes.getSelectedIndex();
        List<String> contentL = new ArrayList<String>();
        String var1 = txtFolioFlete.getText(),//id_Flete
                var2 = txtChoferFlete.getText(),//id_Fletero
                var3 = txtUnidFlete.getText(),//
                var4 = txtCostoFlete.getText(),
                var5 = txtNotaFlete.getText(),
                var6 = fn.getFecha(jDFechCreaFlete),
                var7 = contenFletes.get(var),
                var8 = txtCargaFlet.getText();
        if (var1.isEmpty() && var2.isEmpty() && var3.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar por lo menos campo Nombre");
        } else {
            contentL.add(var7);
            contentL.add(var6);
            contentL.add((var2.isEmpty()) ? "/" : var2);
            contentL.add((var3.isEmpty()) ? "/" : var3);
            contentL.add((var4.isEmpty()) ? "0.0" : var4);

            if (jRPendFlete.isSelected()) {
                contentL.add("0");
            } else if (jRPagadopFlete.isSelected()) {
                contentL.add("1");
            }

            contentL.add((var5.isEmpty()) ? "/" : var5);
            contentL.add((var8.isEmpty()) ? "1" : var8);
            controlInserts.actualizaFlete(contentL, var1);
            mostrarTablaFletesDia(var6);
            limpiaFlete();
            jButFleteGuardar.setEnabled(true);
        }//if vacio   
        jLabLetreroFletes.setVisible(false);
    }//GEN-LAST:event_jButAltasActualiza1ActionPerformed

    private void jRPagadopFleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRPagadopFleteActionPerformed
    }//GEN-LAST:event_jRPagadopFleteActionPerformed

    private void jRdCreaFletesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRdCreaFletesActionPerformed
        jPanCreaFletes.setVisible(true);
        jPanHistorFletes.setVisible(false);
        llenacomboFletes();
        jDFechCreaFlete.setDate(cargafecha());
        mostrarTablaFletesDia(fn.getFecha(jDFechCreaFlete));
        jRPendFlete.setSelected(true);
       limpiaFlete();
    }//GEN-LAST:event_jRdCreaFletesActionPerformed

    private void jTabFletesDiaMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTabFletesDiaMousePressed
        if (evt.getClickCount() > 1) {
            jButFleteGuardar.setEnabled(false);
            int fila = jTabFletesDia.getSelectedRow();
            txtFolioFlete.setText(jTabFletesDia.getValueAt(fila, 0).toString());
            jCBAltasFletes.setSelectedItem(jTabFletesDia.getValueAt(fila, 1).toString());
            jDFechCreaFlete.setDate(fn.StringDate(fn.volteaFecha( jTabFletesDia.getValueAt(fila, 2).toString(), 1)));
            txtChoferFlete.setText(jTabFletesDia.getValueAt(fila, 3).toString());
            txtUnidFlete.setText(jTabFletesDia.getValueAt(fila, 4).toString());
            txtCostoFlete.setText(jTabFletesDia.getValueAt(fila, 5).toString());
            if (jTabFletesDia.getValueAt(fila, 6).toString().equals("PENDIENTE")) {
                jRPendFlete.setSelected(true);
            } else if (jTabFletesDia.getValueAt(fila, 6).toString().equals("PAGADO")) {
                jRPagadopFlete.setSelected(true);
            }
            txtNotaFlete.setText(jTabFletesDia.getValueAt(fila, 7).toString());
            txtCargaFlet.setText(jTabFletesDia.getValueAt(fila, 8).toString());
        }
    }//GEN-LAST:event_jTabFletesDiaMousePressed

    private void jButAltasElimina1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButAltasElimina1ActionPerformed
        String borrar = txtFolioFlete.getText();
        controlInserts.elimaRow("fleteenviado", "id_fleteE", borrar);
        mostrarTablaFletesDia(fn.getFecha(jDFechCreaFlete));
        limpiaFlete();
        jButFleteGuardar.setEnabled(true);
    }//GEN-LAST:event_jButAltasElimina1ActionPerformed

    private void jRHistorFletesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRHistorFletesActionPerformed
        jPanCreaFletes.setVisible(false);
        jPanHistorFletes.setVisible(true);
        llenacomboFletes();
        jDCFol1.setDate(cargafecha());
        jDCFol2.setDate(cargafecha());
    }//GEN-LAST:event_jRHistorFletesActionPerformed

    private void jTablefiltrosBusqfleteMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTablefiltrosBusqfleteMousePressed
    }//GEN-LAST:event_jTablefiltrosBusqfleteMousePressed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        int elije = jCombOpcBusqFletes.getSelectedIndex(), opc = 0;
        String foli = "", id_cli = "",stats ="",fil1 ="";
        if(jRadioButton1.isSelected())
            stats = "";
        if(jRadioButton2.isSelected())
            stats = "1";
        if(jRadioButton3.isSelected())
            stats = "0";
        
        if (jCfleteroOpc.isEditable()) {
            foli = jCfleteroOpc.getSelectedItem().toString();
        }
        if (!jCfleteroOpc.isEditable()) {
            opc = jCfleteroOpc.getSelectedIndex();
            id_cli = contenFletes.get(opc);
        }
        //jCombOpcBusqPedido->Criterio de Busqueda
        String fech1 = fn.getFecha(jDCFol1), fech2 = fn.getFecha(jDCFol2);
        String[][] mat = null;
        switch (elije) {
            case 0:
                mat = controlInserts.matrizFletesFilter(elije, id_cli, "", "",stats);
                jTablefiltrosBusqflete.setModel(new TModel(mat, cabFilterFlete));
                break;
            case 1:
                //System.out.println("FOLIO: " + foli);
                mat = controlInserts.matrizFletesFilter(elije, foli, "", "",stats);
                jTablefiltrosBusqflete.setModel(new TModel(mat, cabFilterFlete));
                break;
            case 2:
                //System.out.println("Fecha1: " + fech1);
                mat = controlInserts.matrizFletesFilter(elije, "", fech1, "",stats);
                jTablefiltrosBusqflete.setModel(new TModel(mat, cabFilterFlete));
                break;
            case 3:
                //System.out.println("fech1: " + fech1 + "Fecha2: " + fech2);
                mat = controlInserts.matrizFletesFilter(elije, "", fech1, fech2,stats);
                jTablefiltrosBusqflete.setModel(new TModel(mat, cabFilterFlete));
                break;
            case 4:
                //System.out.println("idCli: " + id_cli + "Fecha1: " + fech1);
                mat = controlInserts.matrizFletesFilter(elije, id_cli, fech1, "",stats);
                jTablefiltrosBusqflete.setModel(new TModel(mat, cabFilterFlete));
                break;
            case 5:
                //System.out.println("idCli: " + id_cli + "Fecha1: " + fech1 + "Fecha2: " + fech2);
                mat = controlInserts.matrizFletesFilter(elije, id_cli, fech1, fech2,stats);
                jTablefiltrosBusqflete.setModel(new TModel(mat, cabFilterFlete));
                break;
        };
        jTablefiltrosBusqflete.getColumnModel().getColumn(0).setMaxWidth(100);
        jTablefiltrosBusqflete.getColumnModel().getColumn(0).setMinWidth(0);
        jTablefiltrosBusqflete.getColumnModel().getColumn(0).setPreferredWidth(100);
        
        jLabel27.setText(Integer.toString(jTablefiltrosBusqflete.getRowCount()));//numero de registros devuelto
        fil1 = jTablefiltrosBusqflete.getValueAt(0, 0).toString();
        if(fil1.equals("NO DATA")){
            jLabel27.setText("--");
            jLabel103.setText("--");
        }else{
            jLabel103.setText(totalon(jTablefiltrosBusqflete, 5));            
        }

    }//GEN-LAST:event_jButton12ActionPerformed

    private void jCfleteroOpcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCfleteroOpcActionPerformed
    }//GEN-LAST:event_jCfleteroOpcActionPerformed

    private void jCombOpcBusqFletesFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jCombOpcBusqFletesFocusLost
    }//GEN-LAST:event_jCombOpcBusqFletesFocusLost

    private void jTabFletesDia1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTabFletesDia1MousePressed
    }//GEN-LAST:event_jTabFletesDia1MousePressed

    private void jButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton18ActionPerformed
        String fechAs = fn.getFecha(jDCAsignacionDia);
        int fil1 = jTabVistaPedidosDia1.getRowCount(), fil2 = jTabVistaComprasDia3.getRowCount(), fil3 = jTabFletesDia1.getRowCount();
        dtm = (DefaultTableModel) jTabVistaPedidosDia1.getModel(); //TableProducto es el nombre de mi tabla ;)
        for (int i = 0; fil1 > i; i++) {
            dtm.removeRow(0);
        }
        dtm = (DefaultTableModel) jTabVistaComprasDia3.getModel();
        for (int i = 0; fil2 > i; i++) {
            dtm.removeRow(0);
        }
        dtm = (DefaultTableModel) jTabFletesDia1.getModel();
        for (int i = 0; fil3 > i; i++) {
            dtm.removeRow(0);
        }
        for (int i = 0; i < jTable1.getRowCount(); i++) {
            for (int j = 0; j < jTable1.getColumnCount(); j++) {
                jTable1.setValueAt("", i, j);
            }
        }
        txtBusqAignCompra.setText("");
        txtBusqFleteAsign.setText("");
        
        cargaComprasDiaAsign(fechAs, "");//carga las compras del dia 
        cargaPedidosDiaAsign(fechAs);//carga los pedidos del dia 
        
        mostrarTablaFletesDiaAsign(fechAs,"");
        
        cargaTotPedidoDay(jTable1,fechAs);
        cargaTotCompDayProveedor(jTable1,fechAs,1);
        String[][] mat = controlInserts.matFletEstados(fechAs);
        jTDetailAsign.setModel(new TModel(mat, cabEdoPed));
        txtBusqAignCompra.setText("");
        Object val1,val2,result;
        int vak1,vak2,resuk;
        //Realiza diferencia de pedido del dia con compras del dia
            for (int j = 0; j < jTable1.getColumnCount(); j++) {
                val1 = jTable1.getValueAt(0, j);
                val2 = jTable1.getValueAt(1, j);
                if (val1 == null || val1.toString().isEmpty())
                    vak1 = 0;
                else{
                    vak1 = Integer.parseInt(val1.toString());
                }
                if (val2 == null || val2.toString().isEmpty())
                    vak2 = 0;
                else{
                    vak2 = Integer.parseInt(val2.toString());
                }
                resuk=vak1-vak2;
               jTable1.setValueAt(resuk, 2, j);
            }
 
            jLabPed.setText(Integer.toString(jTabVistaPedidosDia1.getRowCount()));
            jLaComp.setText(Integer.toString(jTabVistaComprasDia3.getRowCount()));
            jLabFlet.setText(Integer.toString(jTabFletesDia1.getRowCount()));
    }//GEN-LAST:event_jButton18ActionPerformed

    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton17ActionPerformed
        String[][] matViewRep = null;
        String fechi = fn.getFecha(jDCAsignacionDia);
        int filPed = jTabVistaPedidosDia1.getSelectedRow(),
                filcomp = jTabVistaComprasDia3.getSelectedRow(),
                filflet = jTabFletesDia1.getSelectedRow();
        if (filPed != -1 && filcomp != -1 && filflet != -1) {//verificamos que exista por lo menos una fila en las tablas del dia
            String idPed = jTabVistaPedidosDia1.getValueAt(filPed, 0).toString(),
                    idComp = jTabVistaComprasDia3.getValueAt(filcomp, 0).toString(),
                    idFlet = jTabFletesDia1.getValueAt(filflet, 0).toString();
            txtidPedidoAsign.setText(idPed);
            txtCompraAsign.setText(idComp);
            jFramElijeAsignCompras.setLocationRelativeTo(this);
            jFramElijeAsignCompras.setVisible(true);
            jFramElijeAsignCompras.setEnabled(true);
            jLabNumcompra.setText(idComp);
            jLabNumcompra1.setText(idPed);
            jLabNumcompra2.setText(idFlet);
            matViewRep = controlInserts.regresacompMayorisa(idComp, "");
            jTabAsigaDinamicoCompra.setModel(new TModel(matViewRep, cabMayViewcHECK));
            addCheckBox(7, jTabAsigaDinamicoCompra);//agrega el checkBox a la tabla en la columna 6
            rellenaAsignaTabla();
         //   cargaDetailcompAsig(idComp);
        } else {
            JOptionPane.showMessageDialog(null, "Debe elegir una fila de cada tabla.");
        }
    }//GEN-LAST:event_jButton17ActionPerformed

    private void jButFleteGuardar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButFleteGuardar1ActionPerformed
        String fech = fn.getFecha(jDFechPays),
                idT = jLabTurno.getText();
        if(jDFechPays.isVisible() && jCheckBox2.isSelected()){
            llenaTabPayDayPedidos(1,fech);
        }else{
            llenaTabPayDayPedidos(0,idT);//se puede enviar fecha o turno
        }
        rellenaInformDay();//realiza cuentas para 
        calcDiaPagos();
    }//GEN-LAST:event_jButFleteGuardar1ActionPerformed

    private void jMItDetailVerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMItDetailVerActionPerformed
        int opc = jTabVistaPedidosDia1.getSelectedRow();
        if (opc > -1) {
            String val = jTabVistaPedidosDia1.getValueAt(opc, 0).toString();
            if(controlInserts.validaRelCompPed(val, "id_pedidoCli")){
                String param = jTabVistaPedidosDia1.getValueAt(opc, 0).toString(), nombre = jTabVistaPedidosDia1.getValueAt(opc, 1).toString();
                fP = new FormacionPedido(Integer.parseInt(param), nombre);
                fP.setEnabled(true);
                fP.setVisible(true);
                fP.validate();
            }else{
                JOptionPane.showMessageDialog(null, "Aun no hay asignaciones para pedido No: "+val);
            }
            
        }else{
            JOptionPane.showMessageDialog(null, "<html> <h2>Debe elegir alguna fila de la tabla</h2> </html>");
        }
    }//GEN-LAST:event_jMItDetailVerActionPerformed

    private void txtFolioFleteFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFolioFleteFocusLost
        String verifFol = txtFolioFlete.getText();
        if (verifFol.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Folio no debe estar vacio");
        } else {
            if (controlInserts.validaRelCompPed(verifFol, "fleteenviado")) {
                jLabLetreroFletes.setVisible(true);
            } else {
                jLabLetreroFletes.setVisible(false);
            }
        }
    }//GEN-LAST:event_txtFolioFleteFocusLost

    private void jTabVistaComprasDiaMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTabVistaComprasDiaMousePressed
        if (evt.getClickCount() > 1) {
            int fila = jTabVistaComprasDia.getSelectedRow();
            String val = jTabVistaComprasDia.getValueAt(fila, 0).toString(),
                    fech = fn.getFecha(jDateFechCompraProv);
            String[][] mat = null;
            if (fila < 0) {
                JOptionPane.showMessageDialog(null, "No hay datos para mostrar.");
            } else {
                mat = controlInserts.regresacompMayorisa(val, fech);
                jTabDetallecompraAll.setModel(new TModel(mat, cabMayView));

                jTextField1.setText(jTabVistaComprasDia.getValueAt(fila, 10).toString());
                jButton13.setEnabled(false);//deshabilitar el boton de guardado cuando se de docle clic
            }
        }
    }//GEN-LAST:event_jTabVistaComprasDiaMousePressed

    private void jMIPaysPrestaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMIPaysPrestaActionPerformed
        int fila = jTabBusqPrestProv.getSelectedRow();
        String val = "", name = "";
        if (fila == -1) {
            JOptionPane.showMessageDialog(null, "Debe elegir una fila.");
        } else {
            val = jTabBusqPrestProv.getValueAt(fila, 0).toString();
            name = jTabBusqPrestProv.getValueAt(fila, 4).toString();
            vP = new VentaPiso(controlInserts.totalCreditProv(val), val, "pagocreditprooved");
            vP.setVisible(true);
            vP.setEnabled(true);
            vP.validate();
            vP.jLabLetreroTransac.setText("Pago de credito No.");
            vP.txtidComp.setText(val);
            vP.jLabNameProv.setText("Proveedor: ");
            vP.txtProveedorName.setText(name);
            vP.jLabTurno.setText(jLabTurno.getText());
        }
    }//GEN-LAST:event_jMIPaysPrestaActionPerformed

    private void jTabBusqCompraProv1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTabBusqCompraProv1MousePressed
        if (evt.getClickCount() > 1) {
            int fila = jTabBusqCompraProv1.getSelectedRow();
            String val = jTabBusqCompraProv1.getValueAt(fila, 0).toString();
            if (val.equals("NO DATA")) {
                JOptionPane.showMessageDialog(null, "No hay datos para mostrar.");
            } else {
                List<String> detailPedidoCli = new ArrayList<String>();
                detailPedidoCli.add(jTabBusqCompraProv1.getValueAt(fila, 0).toString());
                detailPedidoCli.add(jTabBusqCompraProv1.getValueAt(fila, 4).toString());
                detailPedidoCli.add(jTabBusqCompraProv1.getValueAt(fila, 5).toString());
                dComp = new detailCompra(Integer.parseInt(val));
                dComp.setEnabled(true);
                dComp.setVisible(true);
                dComp.validate();
                dComp.recibeListData2(detailPedidoCli);

            }
        }
    }//GEN-LAST:event_jTabBusqCompraProv1MousePressed

    private void jMItPayFleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMItPayFleteActionPerformed
        int fila = jTabFletesDia.getSelectedRow();
        String val = "", name = "", costo = "";
        if (fila == -1) {
            JOptionPane.showMessageDialog(null, "Debe elegir una fila.");
        } else {
            val = jTabFletesDia.getValueAt(fila, 0).toString();
            name = jTabFletesDia.getValueAt(fila, 1).toString();
            costo = jTabFletesDia.getValueAt(fila, 5).toString();
            vP = new VentaPiso(costo, val, "pagoflete");
            vP.setVisible(true);
            vP.setEnabled(true);
            vP.validate();
            vP.jLabLetreroTransac.setText("Pago de Flete No.");
            vP.txtidComp.setText(val);
            vP.jLabNameProv.setText("Fletero: ");
            vP.txtProveedorName.setText(name);
            vP.jLabTurno.setText(jLabTurno.getText());
            vP.jButContado.doClick();
            vP.jRadContado.doClick();
            vP.txtnotaPay.requestFocus(true);
        }
    }//GEN-LAST:event_jMItPayFleteActionPerformed

    private void jMitPayfilterFletesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMitPayfilterFletesActionPerformed
        int fila = jTablefiltrosBusqflete.getSelectedRow();
        String val = "", name = "", costo = "";
        if (fila == -1) {
            JOptionPane.showMessageDialog(null, "Debe elegir una fila.");
        } else {
            val = jTablefiltrosBusqflete.getValueAt(fila, 0).toString();
            name = jTablefiltrosBusqflete.getValueAt(fila, 1).toString();
            costo = jTablefiltrosBusqflete.getValueAt(fila, 5).toString();
            vP = new VentaPiso(costo, val, "pagoflete");
            vP.setVisible(true);
            vP.setEnabled(true);
            vP.validate();
            vP.jLabLetreroTransac.setText("Pago de Flete No.");
            vP.txtidComp.setText(val);
            vP.jLabNameProv.setText("Fletero: ");
            vP.txtProveedorName.setText(name);
            vP.jLabTurno.setText(jLabTurno.getText());
            
            vP.jButContado.doClick();
            vP.jRadContado.doClick();
            vP.txtnotaPay.requestFocus(true);
        }
    }//GEN-LAST:event_jMitPayfilterFletesActionPerformed

    private void jMnDetailVentaPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMnDetailVentaPActionPerformed
        int fila = jTablefiltrosBusqVent.getSelectedRow(),
                cols = jTablefiltrosBusqVent.getColumnCount();
        String val = "", name = "", costo = "";
        List<String> datos = new ArrayList<String>();
        if (fila == -1) {
            JOptionPane.showMessageDialog(null, "Debe elegir una fila.");
        } else {
            for (int i = 0; i < cols; i++) {
                datos.add(jTablefiltrosBusqVent.getValueAt(fila, i).toString());
            }
            val = jTablefiltrosBusqVent.getValueAt(fila, 0).toString();
            //System.out.println(val);
            ventDP = new detalleVP(Integer.parseInt(val));
            ventDP.setVisible(true);
            ventDP.setEnabled(true);
            ventDP.validate();
            ventDP.recibeListData(datos);

        }
    }//GEN-LAST:event_jMnDetailVentaPActionPerformed

    private void jTVistaVentaPisoDiaMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTVistaVentaPisoDiaMousePressed
        if(evt.getClickCount() > 1){
        int fila = jTVistaVentaPisoDia.getSelectedRow(),
                cols = jTVistaVentaPisoDia.getColumnCount();
        String val = "", name = "", costo = "";
        List<String> datos = new ArrayList<String>();
        if (fila == -1) {
            JOptionPane.showMessageDialog(null, "Debe elegir una fila.");
        } else {
            for (int i = 0; i < cols; i++) {
                datos.add(jTVistaVentaPisoDia.getValueAt(fila, i).toString());
            }
            val = jTVistaVentaPisoDia.getValueAt(fila, 0).toString();
            //System.out.println(val);
            ventDP = new detalleVP(Integer.parseInt(val));
            ventDP.setVisible(true);
            ventDP.setEnabled(true);
            ventDP.validate();
            ventDP.recibeListData(datos);
        }
        }
    }//GEN-LAST:event_jTVistaVentaPisoDiaMousePressed

    private void jButAltasEliminaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButAltasEliminaActionPerformed
        String elim = txtIdParam.getText();
        int opc = jComboAltas.getSelectedIndex();
        switch (opc) {
            case 0:
                controlInserts.elimaRow("clientepedidos", "id_cliente", elim);
                break;
            case 1:
                controlInserts.elimaRow("proveedor", "id_Proveedor", elim);
                break;
            case 2:
                controlInserts.elimaRow("empleado", "id_Empleado", elim);
                break;
            case 3:
                controlInserts.elimaRow("fletero", "id_Fletero", elim);
                break;
        };
        jButaltasGuardar.setEnabled(true);
        limpiaCamposAltaCli();
    }//GEN-LAST:event_jButAltasEliminaActionPerformed

    private void AgregarMayoreoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AgregarMayoreoActionPerformed
        int fila = jTabVistaComprasDia.getSelectedRow();
        String val = jTabVistaComprasDia.getValueAt(fila, 0).toString(),
                fech = fn.getFecha(jDateFechCompraProv);
        String[][] mat = null;
        if (fila < 0) {
            JOptionPane.showMessageDialog(null, "No hay datos para mostrar.");
        } else {
            List<String> detailPedidoCli = new ArrayList<String>();
            detailPedidoCli.add(jTabVistaComprasDia.getValueAt(fila, 0).toString());
            detailPedidoCli.add(jTabVistaComprasDia.getValueAt(fila, 1).toString());
            detailPedidoCli.add(jTabVistaComprasDia.getValueAt(fila, 9).toString());
            detailPedidoCli.add(jTabVistaComprasDia.getValueAt(fila, 10).toString());

            dComp = new detailCompra(Integer.parseInt(val));
            dComp.setEnabled(true);
            dComp.setVisible(true);
            dComp.validate();
            dComp.recibeListData(detailPedidoCli);
        }
    }//GEN-LAST:event_AgregarMayoreoActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        if (jCheckBox1.isSelected()) {
            llenacomboMayoristas(0);
            limpiaDetailMayorista();
        }
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void jMI_ElimASIGNAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMI_ElimASIGNAActionPerformed
        /* 
        int elije = jTabMayorAsignados.getSelectedRow();
        if (elije > -1) {
            String var = jTabMayorAsignados.getValueAt(elije, 0).toString();
            controlInserts.elimaRow("compramayoreo", "id_compraMay", var);
        } else {
            JOptionPane.showMessageDialog(null, "Debe elegir una opcion de la tabla.");
        }
         */
    }//GEN-LAST:event_jMI_ElimASIGNAActionPerformed

    private void txtCantCreaPedidoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCantCreaPedidoKeyReleased
        if (evt.getKeyCode() == evt.VK_ENTER) {
            jButton9.doClick();
        }
    }//GEN-LAST:event_txtCantCreaPedidoKeyReleased

    private void txtNotePedidoCliKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNotePedidoCliKeyReleased
        if (evt.getKeyCode() == evt.VK_ENTER) {
            jButton9.doClick();
        }
    }//GEN-LAST:event_txtNotePedidoCliKeyReleased

    private void jTableCreaPedidosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTableCreaPedidosKeyReleased
        if (evt.getKeyCode() == evt.VK_ENTER) {
            jButGuardaPedidodia.doClick();
        }
    }//GEN-LAST:event_jTableCreaPedidosKeyReleased

    private void txtImportCompKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtImportCompKeyReleased
        if (evt.getKeyCode() == evt.VK_ENTER) {
            jButton1.doClick();
        }
        if (evt.getKeyCode() == 38) {//flecha arriba
            txtPrecCompraProv.requestFocus(true);
            txtPrecCompraProv.selectAll();
        }
        if (evt.getKeyCode() == 40) {//flecha abajo
            txtNotaCompra.requestFocus(true);
            txtNotaCompra.selectAll();
        }
    }//GEN-LAST:event_txtImportCompKeyReleased

    private void txtNotaCompraKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNotaCompraKeyReleased
        if (evt.getKeyCode() == evt.VK_ENTER) {
            jButton1.doClick();
        }
        if (evt.getKeyCode() == 38) {//flecha arriba
            txtImportComp.requestFocus(true);
            txtImportComp.selectAll();
        }

    }//GEN-LAST:event_txtNotaCompraKeyReleased

    private void txtImportPresKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtImportPresKeyPressed
        if (evt.getKeyCode() == evt.VK_ENTER) {
            jButton1.doClick();
        }
        if (evt.getKeyCode() == 38) {//flecha arriba
            txtPrecProov.requestFocus(true);
            txtPrecProov.selectAll();
        }
        if (evt.getKeyCode() == 40) {//flecha abajo
            textANotaPrestProv.requestFocus(true);
            textANotaPrestProv.selectAll();
        }
    }//GEN-LAST:event_txtImportPresKeyPressed

    private void textANotaPrestProvKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textANotaPrestProvKeyPressed
        if (evt.getKeyCode() == evt.VK_ENTER) {
            jButton7.doClick();
        }
    }//GEN-LAST:event_textANotaPrestProvKeyPressed

    private void txtImportVentaPKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtImportVentaPKeyPressed
        if (evt.getKeyCode() == evt.VK_ENTER) {
            jButton8.doClick();
        }
    }//GEN-LAST:event_txtImportVentaPKeyPressed

    private void txtNotaVentPKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNotaVentPKeyPressed
        if (evt.getKeyCode() == evt.VK_ENTER) {
            jButton8.doClick();
        }
    }//GEN-LAST:event_txtNotaVentPKeyPressed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        try {
            Runtime runtime = Runtime.getRuntime();//Escritorio
            //File backupFile = new File(String.valueOf(RealizarBackupMySQL.getSelectedFile().toString())+".sql");
            // File backupFile = new File("C:\\Users\\monit\\Documents\\CENTRAL DE ABASTOS\\autoGenerate.sql");
            File backupFile = new File(controlInserts.cargaConfig() + "autoGenerateAdminDCR" + fn.setDateActualGuion() + ".sql");
            InputStreamReader irs;
            BufferedReader br;
            try (FileWriter fw = new FileWriter(backupFile)) {
                Process child = runtime.exec("C:/Program Files/MySQL/MySQL Server 8.0/bin/mysqldump  --password=0ehn4TNU5R --user=root --default-character-set=utf8mb4 --databases admindcr");// | gzip> respadmin_DCR.sql.gz
                irs = new InputStreamReader(child.getInputStream());
                br = new BufferedReader(irs);
                String line;
                while ((line = br.readLine()) != null) {
                    fw.write(line + "\n");
                }
            }
            irs.close();
            br.close();
            JOptionPane.showMessageDialog(null, "Archivo generado correctamente.", "Verificar", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error no se genero el archivo por el siguiente motivo:" + e.getMessage(), "Verificar", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_jButton6ActionPerformed

    private void delDetailcompraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delDetailcompraActionPerformed
        int elije = jTabDetallecompraAll.getSelectedRow();
        if (elije > -1) {
            String idelim = jTabDetallecompraAll.getValueAt(elije, 1).toString();
            if (Integer.parseInt(idelim) > 7) {
                controlInserts.elimaRow("detailcompraprooved", "num_DCompraP", idelim);
                limpiaDetailMayorista();
                jTextField1.setText("");
            } else {
                DefaultTableModel dtmDel = (DefaultTableModel) jTabDetallecompraAll.getModel();
                int fil = dtmDel.getRowCount();
                if (fil > 0) {
                    dtmDel.removeRow(elije);
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Debe elegir una opcion de la tabla.");
        }        // TODO add your handling code here:
    }//GEN-LAST:event_delDetailcompraActionPerformed

    private void jTabDetallecompraAllMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTabDetallecompraAllMousePressed

    }//GEN-LAST:event_jTabDetallecompraAllMousePressed

    private void jTabAsigaDinamicoCompraMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTabAsigaDinamicoCompraMouseClicked
        int elig = jTabAsigaDinamicoCompra.getSelectedRow();
        Object var = jTabAsigaDinamicoCompra.getValueAt(elig, 7);
//        System.err.println("Valor sin check on = "+var);
        if (var != null && var.equals(true)) {// || !var.toString().isEmpty()
            jTabAsigaDinamicoCompra.setValueAt(jTabAsigaDinamicoCompra.getValueAt(elig, 10), elig, 8);
            jTabAsigaDinamicoCompra.editCellAt(elig, 8);
            jTabAsigaDinamicoCompra.setSurrendersFocusOnKeystroke(true);
            jTabAsigaDinamicoCompra.getEditorComponent().requestFocus();
        } else {
            jTabAsigaDinamicoCompra.setValueAt(null, elig, 8);
            jTabAsigaDinamicoCompra.setValueAt(null, elig, 7);
            jTabAsigaDinamicoCompra.revalidate();
        }
    }//GEN-LAST:event_jTabAsigaDinamicoCompraMouseClicked

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        Object var = "";
        int contRows = 0;
        List<String> datos = new ArrayList<String>();
        for (int i = 0; i < jTabAsigaDinamicoCompra.getRowCount(); i++) {
            var = jTabAsigaDinamicoCompra.getValueAt(i, 7);//col=6 porq es el jcheck
            if (var != null && !var.toString().isEmpty()) {
                contRows++;//es necesario para saber si señalo alguno
            }
        }
        if (contRows > 0) {
            for (int i = 0; i < jTabAsigaDinamicoCompra.getRowCount(); i++) {
                var = jTabAsigaDinamicoCompra.getValueAt(i, 7);//col=6 porq es el jcheck
                if (var != null && !var.toString().isEmpty()) {
                    datos.add(jLabNumcompra.getText());//id_compraProveed
                    datos.add(jLabNumcompra1.getText());//id_pedidoCli
                    datos.add(jTabAsigaDinamicoCompra.getValueAt(i, 8).toString());//cantidadCajasRel
                    datos.add(jTabAsigaDinamicoCompra.getValueAt(i, 2).toString());//tipoMercanRel
                    datos.add(jLabNumcompra2.getText());//id_fleteP
                    datos.add(jTabAsigaDinamicoCompra.getValueAt(i, 5).toString());
                    datos.add(jTabAsigaDinamicoCompra.getValueAt(i, 1).toString());
                    controlInserts.guardaRelOperaciones(datos);
                    datos.clear();
                }
            }//for
            jFramElijeAsignCompras.dispose();
        } else {
            JOptionPane.showMessageDialog(null, "No eligio nada a asignar");
        }
    }//GEN-LAST:event_jButton16ActionPerformed

    private void jButton16KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jButton16KeyReleased

    }//GEN-LAST:event_jButton16KeyReleased

    private void jTabAsigaDinamicoCompraKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTabAsigaDinamicoCompraKeyReleased
        int oprime = evt.getKeyCode(),
                elig = jTabAsigaDinamicoCompra.getSelectedRow();
        String aAsignar = "", disponib = "";
   //     System.err.println("oprimio: " + oprime);
        if (oprime == 27) {
            jFramElijeAsignCompras.dispose();
        }
        if (oprime == evt.VK_ENTER)//KeyEvent.VK_ENTER
        {
            aAsignar = jTabAsigaDinamicoCompra.getValueAt(elig, 8).toString();
            disponib = jTabAsigaDinamicoCompra.getValueAt(elig, 10).toString();

            if (Integer.parseInt(disponib) < Integer.parseInt(aAsignar)) {
                JOptionPane.showMessageDialog(null, "La cantidad a asignar no puede ser mayor a la cantidad disponible \n Verifique por favor.");
                jTabAsigaDinamicoCompra.setValueAt(null, elig, 8);
                jTabAsigaDinamicoCompra.setValueAt(null, elig, 7);
                jTabAsigaDinamicoCompra.revalidate();
            }
        }
    }//GEN-LAST:event_jTabAsigaDinamicoCompraKeyReleased

    private void jTabVistaPedidosDetDiaMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTabVistaPedidosDetDiaMousePressed
        if (evt.getClickCount() > 1) {
            int fila = jTabVistaPedidosDetDia.getSelectedRow();
            String val = jTabVistaPedidosDetDia.getValueAt(fila, 0).toString();
            if (val.equals("NO DATA") || val.equals("0")) {
                JOptionPane.showMessageDialog(null, "No hay datos para mostrar.");
            } else {
                List<String> detailPedidoCli = new ArrayList<String>();
                    detailPedidoCli.add(jTabVistaPedidosDetDia.getValueAt(fila, 0).toString());
                    detailPedidoCli.add(jTabVistaPedidosDetDia.getValueAt(fila, 1).toString());
                    detailPedidoCli.add(jTabVistaPedidosDetDia.getValueAt(fila, 9).toString());
                    
                dP = new detailPedido(Integer.parseInt(val));
                dP.setEnabled(true);
                dP.setVisible(true);
                dP.validate();
                dP.recibeListData(detailPedidoCli);
            }
        }
    }//GEN-LAST:event_jTabVistaPedidosDetDiaMousePressed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        String[] var = new String[3];
        int elij = jTabDetallecompraAll.getSelectedRow();
        if (elij > -1) {
            var[0] = jTabDetallecompraAll.getValueAt(elij, 1).toString();
            var[1] = jTabDetallecompraAll.getValueAt(elij, 3).toString();
            var[2] = jTabDetallecompraAll.getValueAt(elij, 4).toString();
            controlInserts.actualizaDataCompra(var);
        } else {
            JOptionPane.showMessageDialog(null, "Debe eljir alguna opcion.");
        }
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void txtBusqAignCompraKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBusqAignCompraKeyReleased
        String var = txtBusqAignCompra.getText(),
                fechAs = fn.getFecha(jDCAsignacionDia);
        cargaComprasDiaAsign(fechAs, var);//carga las compras del dia 
    }//GEN-LAST:event_txtBusqAignCompraKeyReleased

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        String datePed = fn.getFecha(jDateCHPedido);
        limpDetPedidoDia();//eliminar registros de tabla pedidoDetDia
                for (int i = 0; i < jTabSumTotalPedido.getColumnCount(); i++) {
            jTabSumTotalPedido.setValueAt("", 0,i);
        }
        cargaPedidosDiaDet(datePed);//carga detalle de pedido del dia
        cargaTotPedidoDay(jTabSumTotalPedido,datePed);

        jPanConsulPed.setVisible(false);
        jPanCreaPedido.setVisible(true);
    }//GEN-LAST:event_jButton15ActionPerformed

    private void jButton19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton19ActionPerformed
           for (int i = 0; i < jTabSumTotales.getColumnCount(); i++) {
                        jTabSumTotales.setValueAt("", 0,i);
                        jTabSumTotales.setValueAt("", 1,i); 
            }
           DefaultTableModel dtmDeldias = (DefaultTableModel) jTabVistaComprasDia.getModel();
            int fil = dtmDeldias.getRowCount();
            if (fil > 0) {
                for (int i = 0; i < fil; i++) {
                    dtmDeldias.removeRow(0);
                }
            }  
                  
        cargaComprasDia(fn.getFecha(jDateFechCompraProv));
        cargaTotCompDayProveedor(jTabSumTotales,fn.getFecha(jDateFechCompraProv),0);
    }//GEN-LAST:event_jButton19ActionPerformed

    private void jButton20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton20ActionPerformed
        mostrarTablaFletesDia(fn.getFecha(jDFechCreaFlete));
    }//GEN-LAST:event_jButton20ActionPerformed

    private void txtCantidadCompraKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCantidadCompraKeyReleased
        if (evt.getKeyCode() == evt.VK_ENTER) {
            jButton1.doClick();
        }
        if (evt.getKeyCode() == 38) {
            jCombProductProv.requestFocus(true);
        }
        if (evt.getKeyCode() == 40) {
            txtPrecCompraProv.requestFocus(true);
            txtPrecCompraProv.selectAll();
        }
    }//GEN-LAST:event_txtCantidadCompraKeyReleased

    private void txtPrecCompraProvKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrecCompraProvKeyReleased
        String cant = txtCantidadCompra.getText(),
                cos = txtPrecCompraProv.getText();

        if (cant.isEmpty() || cos.isEmpty()) {
            //JOptionPane.showMessageDialog(null, "Campo cantidad o monto vacios\n Verifique Por favor");
        } else {
            BigDecimal amountOne = new BigDecimal(cant);//monto a cobrar
            BigDecimal amountTwo = new BigDecimal(cos);//cantidad recivida
            txtImportComp.setText(fn.multiplicaAmount(amountOne, amountTwo).toString());
        }

        if (evt.getKeyCode() == evt.VK_ENTER) {
            jButton1.doClick();
        }
        if (evt.getKeyCode() == 38) {//flecha arriba
            txtCantidadCompra.requestFocus(true);
            txtCantidadCompra.selectAll();
        }
        if (evt.getKeyCode() == 40) {//flecha abajo
            txtImportComp.requestFocus(true);
            txtImportComp.selectAll();
        }

    }//GEN-LAST:event_txtPrecCompraProvKeyReleased

    private void jCElijaProovedorKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jCElijaProovedorKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_F5) {
            jButton13.doClick();
        }
    }//GEN-LAST:event_jCElijaProovedorKeyReleased

    private void jCheckbpAGADOKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jCheckbpAGADOKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_F5) {
            jButton13.doClick();
        }
    }//GEN-LAST:event_jCheckbpAGADOKeyReleased

    private void jCombProductProvKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jCombProductProvKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_F5) {
            jButton13.doClick();
        }
    }//GEN-LAST:event_jCombProductProvKeyReleased

    private void txtPrecProovKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrecProovKeyReleased
        String cant = txtCantPres.getText(),
                cos = txtPrecProov.getText();
        txtImportPres.setText("");

        if (cant.isEmpty() || cos.isEmpty()) {
            // JOptionPane.showMessageDialog(null, "Campo cantidad o monto vacios\n Verifique Por favor");
        } else {
            BigDecimal amountOne = new BigDecimal(cant);//monto a cobrar
            BigDecimal amountTwo = new BigDecimal(cos);//cantidad recivida
            txtImportPres.setText(fn.multiplicaAmount(amountOne, amountTwo).toString());
        }
        if (evt.getKeyCode() == evt.VK_ENTER) {
            jButton7.doClick();
        }
        if (evt.getKeyCode() == 38) {//flecha arriba
            txtCantPres.requestFocus(true);
            txtCantPres.selectAll();
        }
        if (evt.getKeyCode() == 40) {//flecha abajo
            txtImportPres.requestFocus(true);
            txtImportPres.selectAll();
        }
    }//GEN-LAST:event_txtPrecProovKeyReleased

    private void txtCantPresKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCantPresKeyReleased
        if (evt.getKeyCode() == evt.VK_ENTER) {
            jButton7.doClick();
        }
        if (evt.getKeyCode() == 38) {//flecha arriba
            jComBProveedor.requestFocus(true);
        }
        if (evt.getKeyCode() == 40) {//flecha abajo
            txtPrecProov.requestFocus(true);
            txtPrecProov.selectAll();
        }
    }//GEN-LAST:event_txtCantPresKeyReleased

    private void jComBPrestamosProvKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jComBPrestamosProvKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_F5) {
            jButton4.doClick();
        }
    }//GEN-LAST:event_jComBPrestamosProvKeyReleased

    private void jButton21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton21ActionPerformed

        int opc = jCElijaProovedor.getSelectedIndex();//index de proveedor
        String id_cli = idProoved.get(opc);//idProveedor devuelto de la base de datos
                hProv = new historProveedor(id_cli);
                hProv.setEnabled(true);
                hProv.setVisible(true);
                hProv.validate();
                hProv.jLabnumProv.setText(id_cli);
                hProv.jLabNombProv.setText(jCElijaProovedor.getSelectedItem().toString());
    }//GEN-LAST:event_jButton21ActionPerformed

    private void jButton22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton22ActionPerformed
         int leng = jCombPedidoClient.getItemCount();
         if( leng <= 1 ){
            JOptionPane.showMessageDialog(null, "Debe cargar lista de clientes");
        }else{
            int opc = jCombPedidoClient.getSelectedIndex()
                     ;//index de proveedor
             String id_cli = conten.get(opc);//idProveedor devuelto de la base de datos
                    hCliente = new historCliente(id_cli);
                    hCliente.setEnabled(true);
                    hCliente.setVisible(true);
                    hCliente.validate();
                    hCliente.jLabnumProv.setText(id_cli);
                    hCliente.jLabNombProv.setText(jCombPedidoClient.getSelectedItem().toString());
        }
    }//GEN-LAST:event_jButton22ActionPerformed

    private void jCombOpcBusqFletesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCombOpcBusqFletesActionPerformed
        int elije = jCombOpcBusqFletes.getSelectedIndex();
        switch (elije) {
            case 0://Fletero
                jCfleteroOpc.setEnabled(true);
                jCCliVentaPiso.setEnabled(true);
                jDCFol1.setEnabled(false);
                jDCFol2.setEnabled(false);
                llenacomboFletes();
                jCfleteroOpc.setEditable(false);
                break;
            case 1://folio
                jCfleteroOpc.setEnabled(true);
                jCfleteroOpc.removeAllItems();
                jCfleteroOpc.setEditable(true);
                jDCFol1.setEnabled(false);
                jDCFol2.setEnabled(false);
                break;
            case 2://fecha
                jCfleteroOpc.setEnabled(false);
                jDCFol1.setEnabled(true);
                jDCFol2.setEnabled(false);
//                llenacomboFletes();
                jCfleteroOpc.setEditable(false);
                break;
            case 3://lapsoDefecha
                jCfleteroOpc.setEnabled(false);
                jDCFol1.setEnabled(true);
                jDCFol2.setEnabled(true);
//                llenacomboFletes();
                jCfleteroOpc.setEditable(false);
                break;
            case 4://Fletero+Fecha
                jCfleteroOpc.setEnabled(true);
                jDCFol1.setEnabled(true);
                jDCFol2.setEnabled(false);
                llenacomboFletes();
                jCfleteroOpc.setEditable(false);
                break;
            case 5://Fletero+lapsoFechas
                jCfleteroOpc.setEnabled(true);
                jDCFol1.setEnabled(true);
                jDCFol2.setEnabled(true);
                llenacomboFletes();
                jCfleteroOpc.setEditable(false);
                break;
        };
    }//GEN-LAST:event_jCombOpcBusqFletesActionPerformed

    private void jTabVistaPedidosDia1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTabVistaPedidosDia1FocusGained
         int var = jTabVistaPedidosDia1.getSelectedRow(), col = jTabVistaPedidosDia1.getColumnCount(), difer = 0;
       if(var > -1){
        String id_Busq = jTabVistaPedidosDia1.getValueAt(var, 0).toString();
        txtidPedidoAsign.setText(id_Busq);
        Object val2 = null, totSum = null;
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 2; j++) {
                jTabDetailAsignTotales.setValueAt("", i, j);
            }
        }
        for (int i = 2; i < col - 2; i++) {//menos dos porque aumento una columna de faltantes
            val2 = jTabVistaPedidosDia1.getValueAt(var, i);
            if (val2 != null && !val2.toString().isEmpty()) {
                //controlInserts.guardaDetallePedidoCli(ultimo[0], Integer.toString(i + 1), Integer.parseInt(jTableCreaPedidos.getValueAt(0, i).toString()));
                //System.out.print("prodComp= "+(i)+" "+jTabVistaPedidosDia1.getColumnName(i)+"\t-> val= "+jTabVistaPedidosDia1.getValueAt(var, i).toString());
                // System.out.println("envia: "+id_Busq+" -> "+(i-1));
                totSum = controlInserts.calcAsignAPed(id_Busq, Integer.toString(i - 1), "id_pedidoCli");
                if (totSum != null && !totSum.toString().isEmpty()) {
                    difer = Integer.parseInt(val2.toString()) - Integer.parseInt(totSum.toString());
                    jTabDetailAsignTotales.setValueAt(difer, i - 2, 1);
                    jTabDetailAsignTotales.setValueAt(totSum, i - 2, 0);
                } else {
                    jTabDetailAsignTotales.setValueAt(val2, i - 2, 1);
                    jTabDetailAsignTotales.setValueAt(0, i - 2, 0);
                }
            }//if null
        }
       }else{
           
       }
    }//GEN-LAST:event_jTabVistaPedidosDia1FocusGained

    private void jTabVistaComprasDia3FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTabVistaComprasDia3FocusGained
         int var = jTabVistaComprasDia3.getSelectedRow(),
                col = jTabVistaComprasDia3.getColumnCount(),
                var2 = jTabVistaPedidosDia1.getSelectedRow(),
                col2 = jTabVistaPedidosDia1.getSelectedColumnCount(),
                difer = 0;
        boolean bandera = false;
        String concid = "";
        Object val = null, val2 = null, totSum = null;
        //VERIFICAR SI LA MERCANCIA ES DEL MISMO TIPO DEL PEDIDO       
        for (int i = 0; i < 7; i++) {//limpia la tabla de totales asignados
            for (int j = 0; j < 2; j++) {
                jTabDetailAsignTotales1.setValueAt("", i, j);
            }
        }
        for (int i = 2; i < col - 2; i++) {
            if(var2 > -1 && var > -1){
                val2 = jTabVistaPedidosDia1.getValueAt(var2, i);
                val = jTabVistaComprasDia3.getValueAt(var, i);
            }
            if (val != null && val2 != null && !val.toString().isEmpty() && !val2.toString().isEmpty()) {
                bandera = true;
            }//if null
        }
        if (!bandera) {
            if(var2 > -1){
                JOptionPane.showMessageDialog(null, "Mercancia no es del mismo tipo del pedido seleccionado; \n Verfique por favor.");
                jButton18.doClick();//para des seleccionar las filas de todas las tablas
            }else {
                
            }
            } else {
            String id_Busq = jTabVistaComprasDia3.getValueAt(var, 0).toString();
            txtCompraAsign.setText(id_Busq);
            for (int i = 2; i < col - 2; i++) {
                val2 = jTabVistaComprasDia3.getValueAt(var, i);
                if (val2 != null && !val2.toString().isEmpty()) {
                    totSum = controlInserts.calcAsignAPed(id_Busq, Integer.toString(i - 1), "id_compraProveed");
                    if (totSum != null && !totSum.toString().isEmpty()) {
                        difer = Integer.parseInt(val2.toString()) - Integer.parseInt(totSum.toString());
                        jTabDetailAsignTotales1.setValueAt(difer, i - 2, 1);
                        jTabDetailAsignTotales1.setValueAt(totSum, i - 2, 0);
                    } else {
                        jTabDetailAsignTotales1.setValueAt(val2, i - 2, 1);
                        jTabDetailAsignTotales1.setValueAt(0, i - 2, 0);
                    }
                }//if null
            }
        }
    }//GEN-LAST:event_jTabVistaComprasDia3FocusGained

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        int opc = jTabVistaComprasDia3.getSelectedRow();
        if (opc > -1) {
            String val = jTabVistaComprasDia3.getValueAt(opc, 0).toString();
            if(controlInserts.validaRelCompPed(val, "id_compraProveed")){
                String param = jTabVistaComprasDia3.getValueAt(opc, 0).toString(), nombre = jTabVistaComprasDia3.getValueAt(opc, 1).toString(),
                        tot = jTabVistaComprasDia3.getValueAt(opc, 9).toString(),falt = jTabVistaComprasDia3.getValueAt(opc, 10).toString();
                int difer = Integer.parseInt(tot) - Integer.parseInt(falt);
        jDialDetailCompraProov.setLocationRelativeTo(null);
        jDialDetailCompraProov.setVisible(true);
        jDialDetailCompraProov.setTitle("C-Proveedor");
        jDialDetailCompraProov.setEnabled(true);
        jLabId.setText(param);
        jLabNomProov.setText(nombre);
        jLabNumAsogna.setText(Integer.toString(difer));
        jLabFaltant.setText(falt);
        jLabel108.setText(tot);
                       String[][] mati = controlInserts.matPedidosEst(param,1);
                jTabDetProvView.setModel(new TModel(mati, cabDetcompra));
                jLabContad.setText(Integer.toString(mati.length));
        jTabDetProvView.getColumnModel().getColumn(0).setMaxWidth(0);
        jTabDetProvView.getColumnModel().getColumn(0).setMinWidth(0);
        jTabDetProvView.getColumnModel().getColumn(0).setPreferredWidth(0);
        
        jTabDetProvView.getColumnModel().getColumn(1).setMaxWidth(100);
        jTabDetProvView.getColumnModel().getColumn(1).setMinWidth(0);
        jTabDetProvView.getColumnModel().getColumn(1).setPreferredWidth(75);
            }else{
                JOptionPane.showMessageDialog(null, "Aun no hay asignaciones para la compra No: "+val);
            }
            
        }else{
            JOptionPane.showMessageDialog(null, "<html> <h2>Debe elegir alguna fila de la tabla</h2> </html>");
        }
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        int opc = jTabFletesDia1.getSelectedRow();
        if (opc > -1) {
            String val = jTabFletesDia1.getValueAt(opc, 0).toString();
         if(controlInserts.validaRelCompPed(val, "id_fleteP")){
                String param = jTabFletesDia1.getValueAt(opc, 0).toString(), nombre = jTabFletesDia1.getValueAt(opc, 1).toString(),
                        tot = jTabFletesDia1.getValueAt(opc, 4).toString(),falt = jTabFletesDia1.getValueAt(opc, 6).toString(),
                        difer = jTabFletesDia1.getValueAt(opc, 5).toString(),unid = jTabFletesDia1.getValueAt(opc, 2).toString();
                jDialDetailFlete1.setLocationRelativeTo(null);
                jDialDetailFlete1.setVisible(true);
                jDialDetailFlete1.setTitle("Detalle Flete");
                jDialDetailFlete1.setEnabled(true);
                jLabel147.setText(unid);
                jLabel136.setText(param);
                jLabel139.setText(nombre);
                jLabel143.setText(difer);
                jLabel145.setText(falt);
                jLabel141.setText(tot);
                        String[][] mati = controlInserts.matPedidosEst(param,2);
                        jTable3.setModel(new TModel(mati, cabDetFlet));
                        jLabel124.setText(Integer.toString(mati.length));
                jTable3.getColumnModel().getColumn(0).setMaxWidth(0);
                jTable3.getColumnModel().getColumn(0).setMinWidth(0);
                jTable3.getColumnModel().getColumn(0).setPreferredWidth(0);

                jTable3.getColumnModel().getColumn(1).setMaxWidth(100);
                jTable3.getColumnModel().getColumn(1).setMinWidth(0);
                jTable3.getColumnModel().getColumn(1).setPreferredWidth(75);
        }else{
                JOptionPane.showMessageDialog(null, "Aun no hay asignaciones para el Flete Folio: "+val);
            }
        }else{
            JOptionPane.showMessageDialog(null, "<html> <h2>Debe elegir alguna fila de la tabla</h2> </html>");
        }
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jTabFletesDiaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTabFletesDiaKeyReleased
          int var = evt.getKeyCode();
                if (var == KeyEvent.VK_F5) {
                    jMItPayFlete.doClick();
                }
    }//GEN-LAST:event_jTabFletesDiaKeyReleased

    private void jTablefiltrosBusqfleteKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTablefiltrosBusqfleteKeyReleased
           int var = evt.getKeyCode();
                if (var == KeyEvent.VK_F5) {
                    jMitPayfilterFletes.doClick();
                }
    }//GEN-LAST:event_jTablefiltrosBusqfleteKeyReleased

    private void jButton23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton23ActionPerformed
        String[] turns = controlInserts.getTurnoData(Integer.parseInt(jLabTurno.getText()));
        String[] paramDats = new String[7];
        
        paramDats[0] = jTextField17.getText();//nombre
        paramDats[1] = turns[3];//fecha apertura
        paramDats[2] = turns[4];//fecha final
        paramDats[3] = turns[2];//saldo entrada
        paramDats[4] = jLabel164.getText();// total entradas del turnojLabel164
        paramDats[5] =jLabel166.getText();//total de gastos en caja
        paramDats[6] = jLabel168.getText();
        
        String fech = fn.getFecha(jDFechPays),
                idT = jLabTurno.getText();
        if(jDFechPays.isVisible() && jCheckBox2.isSelected()){
            paramDats[0] = fech;//nombre
            rP.imprim80MM_corteCaja(fech,false,paramDats);
        }else{
            rP.imprim80MM_corteCaja(jLabTurno.getText(),false,paramDats);
            rP.creListenerButton(Integer.parseInt(jLabTurno.getText()), jTextField17.getText(),jLabIdView.getText(),paramDats);            
        }
    }//GEN-LAST:event_jButton23ActionPerformed

    private void jButton24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton24ActionPerformed
           jDialAltaGastos.setLocationRelativeTo(null);
            jDialAltaGastos.setVisible(true);
            jDialAltaGastos.setEnabled(true);
           jDialAltaGastos.setTitle("Ingreso de gasto");
           jLabel127.setText("Registrar gasto");
            jButton39.setText("Guardar");
            limpiaguardGasto();    }//GEN-LAST:event_jButton24ActionPerformed

    private void txtConceptKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtConceptKeyPressed
        int var = evt.getKeyCode();
        if(var == 27 ){
            jDialAltaGastos.dispose();
        }else{
            if(var == KeyEvent.VK_ENTER){
                jButton39.doClick();
            }
        }
    }//GEN-LAST:event_txtConceptKeyPressed

    private void txtsolictKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtsolictKeyPressed
        int var = evt.getKeyCode();
        if(var == 27 ){
            jDialAltaGastos.dispose();
        }else{
            if(var == KeyEvent.VK_ENTER){
                jButton39.doClick();
            }
        }
    }//GEN-LAST:event_txtsolictKeyPressed

    private void txtObservsKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtObservsKeyPressed
        int var = evt.getKeyCode();
        if(var == 27 ){
            jDialAltaGastos.dispose();
        }else{
            if(var == KeyEvent.VK_ENTER){
                jButton39.doClick();
            }
        }
    }//GEN-LAST:event_txtObservsKeyPressed

    private void txtMontoGastoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMontoGastoKeyPressed
        int var = evt.getKeyCode();
        if(var == 27 ){
            jDialAltaGastos.dispose();
        }else{
            if(var == KeyEvent.VK_ENTER){
                jButton39.doClick();
            }
        }
    }//GEN-LAST:event_txtMontoGastoKeyPressed

    private void jCombBTypeRubrosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jCombBTypeRubrosKeyPressed
        int var = evt.getKeyCode();
        if(var == 27 )
        jDialAltaGastos.dispose();
    }//GEN-LAST:event_jCombBTypeRubrosKeyPressed

    private void jButton39ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton39ActionPerformed
        String[] arregCan = new String[9];
        int ultGast = controlInserts.getUltimGastCaja();
        String[] arregModif = new String[4];
        String[] ultPrest = null;
        List<String> dataPrestamo = new ArrayList<String>();
        
        String  caden = jCombBTypeRubros.getSelectedItem().toString(),
        fech = fn.getFecha(jDFechPays),
        ora = fn.getHour(),
        idRub = contenRubGastos.get(jCombBTypeRubros.getSelectedIndex()),
        concept = txtConcept.getText(),
        solicit = txtsolict.getText(),
        obserGast = txtObservs.getText(),
        mont = txtMontoGasto.getText(),
        stButn = jButton39.getText(),inTurn = jLabTurno.getText();
        
        if(!mont.isEmpty()){
            arregCan[0] = Integer.toString((ultGast + 1 ));
            arregCan[1] = inTurn;//Integer.toString(ultTurno); *
            arregCan[2] = fech;
            arregCan[3] = ora;
            arregCan[4] = idRub;
            arregCan[5] = concept; arregModif[0] = concept;
            arregCan[6] = solicit; arregModif[1] = solicit;
            arregCan[7] = obserGast; arregModif[2] = obserGast;
            arregCan[8] = mont; arregModif[3] = mont;

            if(stButn.equals("Guardar")){
                   if(caden.equals("Préstamo de efectivo a proveedor")){
             //llena list prestamo a proveedor
                         int opc = jComboBox1.getSelectedIndex();
                            String id_cli = idProoved.get(opc);
                        dataPrestamo.add(id_cli);
                       dataPrestamo.add(fech);
                       dataPrestamo.add("0");
                       dataPrestamo.add(obserGast);
                       dataPrestamo.add(ora);
                       dataPrestamo.add(inTurn);
                       controlInserts.guardaPrestamoProv(dataPrestamo);
                       ultPrest = controlInserts.ultimoRegistroPrestamo();//obtenemos ultimo pretamo creado
                       dataPrestamo.clear();
                       dataPrestamo.add(ultPrest[0]);
                       dataPrestamo.add("11");
                       dataPrestamo.add("1");
                       dataPrestamo.add(mont);
                       controlInserts.guardaDetallePrestamoProv(dataPrestamo);
                   }else{
                        controlInserts.guardGastoCaja(arregCan,"");
                   }
            }
            if(stButn.equals("Modificar")){
                int fila = jTabGastosDias.getSelectedRow();
                String mostTic = jTabGastosDias.getValueAt(fila, 0).toString();
                controlInserts.guardGastoCaja(arregModif,mostTic);
            }
        limpiaguardGasto();
        jDialAltaGastos.dispose();
        jButFleteGuardar1.doClick();
        }else{
            JOptionPane.showMessageDialog(null, "Campo monto no puede ser vacio");
        }
    }//GEN-LAST:event_jButton39ActionPerformed

    private void jButton43ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton43ActionPerformed
        jDialAltaGastos.dispose();
    }//GEN-LAST:event_jButton43ActionPerformed

    private void jDialAltaGastosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jDialAltaGastosKeyPressed
 
    }//GEN-LAST:event_jDialAltaGastosKeyPressed

    private void jButton25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton25ActionPerformed
        jButton24.doClick();
    }//GEN-LAST:event_jButton25ActionPerformed

    private void jButton31ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton31ActionPerformed
        cancelPago(jTabPaysCompraProovedor,"Cancelar pago de  compra a proveedor");
    }//GEN-LAST:event_jButton31ActionPerformed

    private void txtCancelTickKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCancelTickKeyReleased
        int var = evt.getKeyCode();
        if(var == 27 ){
            jDialCancelaciones.dispose();
        }else{
            if(evt.getKeyCode()==KeyEvent.VK_ENTER){
                jButton33.doClick();
            }
        }
    }//GEN-LAST:event_txtCancelTickKeyReleased

    private void jButton32ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton32ActionPerformed
        jDialCancelaciones.dispose();
    }//GEN-LAST:event_jButton32ActionPerformed

    private void jButton33ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton33ActionPerformed
      String desc = txtCancelTick.getText(),
        caso = jDialCancelaciones.getTitle();
      if(!desc.isEmpty()){
               String[] arregCan = new String[6];
                String fech = fn.setDateActual(),
                ora = fn.getHour(),
                mostTic = jLabIdCancel.getText(),
                 paramPaynum = jLabParam2.getText();//numero de pago
                int ultCanc = controlInserts.getUltimCancelaciones();
                
                arregCan[0] = Integer.toString((ultCanc+1));
                arregCan[1] = jLabIdView.getText();//id_usuario
                arregCan[2] = jLabTurno.getText();//Integer.toString(ultTurno); *
                arregCan[3] = fech;
                arregCan[4] = ora;
                arregCan[5] = desc;
            
         switch(caso){
                case "Cancelar pago de pedido de cliente":
                    controlInserts.guardInCancelaciones(arregCan);
                    controlInserts.f5CancelTypesAll("pagopedidocli","idCancelacion",mostTic,arregCan[0]);
// Si se cancela un pago se da poor hecho que siempre va a actualizar a pendiente = 0

                    controlInserts.actualizaDataC(paramPaynum,"pedidocliente",0);
                break;
                case "Cancelar pago de prestamo a proveedor":
                    controlInserts.guardInCancelaciones(arregCan);
                    controlInserts.f5CancelTypesAll("pagocreditprooved","idCancelacion",mostTic,arregCan[0]);
                    
                    controlInserts.actualizaDataC(paramPaynum,"creditomerca",0);
                break;
                case "Cancelar pago de venta de piso":
                    controlInserts.guardInCancelaciones(arregCan);
                    controlInserts.f5CancelTypesAll("pagoventapiso","idCancelacion",mostTic,arregCan[0]);
                break;
                case "Cancelar pago flete":
                    controlInserts.guardInCancelaciones(arregCan);
                    controlInserts.f5CancelTypesAll("pagoflete","idCancelacion",mostTic,arregCan[0]);
                    
                    controlInserts.actualizaDataC(paramPaynum,"fleteenviado",0);
                    
                break;
                case "Cancelar pago de  compra a proveedor":
                    controlInserts.guardInCancelaciones(arregCan);
                    controlInserts.f5CancelTypesAll("pagarcompraprovee","idCancelacion",mostTic,arregCan[0]);
                    
                    controlInserts.actualizaDataC(paramPaynum,"compraprooved",0);
                break;
               case "Cancelar ingreso de efectivo":
                    controlInserts.guardInCancelaciones(arregCan);
                    controlInserts.f5CancelTypesAll("pagarcompraprovee","idCancelacion",mostTic,arregCan[0]);
                break;
            };
      
          }else{
                JOptionPane.showMessageDialog(null, "Todos los campos deben estar llenos.");
            }//IS ISEMPTY

        jDialCancelaciones.dispose();
        jButFleteGuardar1.doClick();
    }//GEN-LAST:event_jButton33ActionPerformed

    private void jButton29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton29ActionPerformed
         cancelPago(jTabPaysFletesDia,"Cancelar pago flete");
    }//GEN-LAST:event_jButton29ActionPerformed

    private void jButton28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton28ActionPerformed
        cancelPago(jTabVentPisoPays,"Cancelar pago de venta de piso");
    }//GEN-LAST:event_jButton28ActionPerformed

    private void jButton27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton27ActionPerformed
         cancelPago(jTabPayPrestamosProv,"Cancelar pago de prestamo a proveedor");
    }//GEN-LAST:event_jButton27ActionPerformed

    private void jButton26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton26ActionPerformed
        int elegid = jTabPayPeds.getSelectedRow();
        if(elegid >= 0 ){
            String cancelMot = jTabPayPeds.getValueAt(elegid, 5).toString();
            if(cancelMot.equals("Ingreso")){
                    String mostTic = jTabPayPeds.getValueAt(elegid, 0).toString();
                    if(!mostTic.equals("NO DATA") ){
                        controlInserts.elimaRow("gastos_caja","id",mostTic); 
                        jButFleteGuardar1.doClick();
                    }else{
                        JOptionPane.showMessageDialog(null, "No existe gasto de turno");                        
                    }
                
            }else{
                cancelPago(jTabPayPeds,"Cancelar pago de pedido de cliente");
            }
        }else{
            JOptionPane.showMessageDialog(null, "No hay datos para cancelar");
        }
    }//GEN-LAST:event_jButton26ActionPerformed

    private void jButton30ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton30ActionPerformed
         int fila = jTabGastosDias.getSelectedRow();
                if(fila >= 0){
                    String mostTic = jTabGastosDias.getValueAt(fila, 0).toString(), quees = "";
                    if(!mostTic.equals("NO DATA") ){
                        quees = jTabGastosDias.getValueAt(fila, 5).toString();
                        if(quees.equals("Gasto de caja")){
                            controlInserts.elimaRow("gastos_caja","id",mostTic); 
                        }else{
                            controlInserts.elimaRow("creditomerca","num_credito",mostTic); 
                        }
                        jButFleteGuardar1.doClick();
                    }else{
                        JOptionPane.showMessageDialog(null, "No existe gasto de turno");                        
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "Debe elegir que mostrar");
                }
    }//GEN-LAST:event_jButton30ActionPerformed

    private void txtCargaFletKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCargaFletKeyReleased
           int var = evt.getKeyCode();
                if (var == KeyEvent.VK_ENTER) {
                    if(jButFleteGuardar.isEnabled()){
                        jButFleteGuardar.doClick();
                    }
                    if(!jButFleteGuardar.isEnabled()){
                        jButAltasActualiza1.doClick();
                    }
                }
    }//GEN-LAST:event_txtCargaFletKeyReleased

    private void txtNotaFleteKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNotaFleteKeyReleased
            int var = evt.getKeyCode();
                if (var == KeyEvent.VK_ENTER) {
                    if(jButFleteGuardar.isEnabled()){
                        jButFleteGuardar.doClick();
                    }
                    if(!jButFleteGuardar.isEnabled()){
                        jButAltasActualiza1.doClick();
                    }
                }
    }//GEN-LAST:event_txtNotaFleteKeyReleased

    private void jButton34ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton34ActionPerformed
        int rows = jTabVistaComprasDia3.getRowCount(),roes = jTabSobrinasDays.getRowCount(),
                cols =jTabVistaComprasDia3.getColumnCount() ,conunt = 0, band = 0;
        dtm =(DefaultTableModel) jTabSobrinasDays.getModel();//limpiar tabla sobrinas
        if(roes > 0){
            for (int i = 0; i < roes; i++) {
            dtm.removeRow(0);
            }
        }
        
        String val = "",id="";
        Object var =null;
        if(rows > 0){
         for (int j = 0; j < rows; j++) {
               val = jTabVistaComprasDia3.getValueAt(j, 10).toString();
               if(!val.equals("0")){//creamos filas si hay sobrantes en la compra del dia
                    dtm.addRow(new Object[]{jTabVistaComprasDia3.getValueAt(j, 0),
                        jTabVistaComprasDia3.getValueAt(j, 1),jTabVistaComprasDia3.getValueAt(j, 2),
                        jTabVistaComprasDia3.getValueAt(j, 3),jTabVistaComprasDia3.getValueAt(j, 4),
                        jTabVistaComprasDia3.getValueAt(j, 5),jTabVistaComprasDia3.getValueAt(j, 6),
                        jTabVistaComprasDia3.getValueAt(j, 7),jTabVistaComprasDia3.getValueAt(j, 8),
                        jTabVistaComprasDia3.getValueAt(j, 10)
                    });
                    jTabSobrinasDays.setModel(dtm);
                    conunt++;
               }
            }
         if(conunt > 0){
               jDiaViewSobrinas.setLocationRelativeTo(null);
               jDiaViewSobrinas.setVisible(true);
               jDiaViewSobrinas.setTitle("Sobrantes del día");
               jDiaViewSobrinas.setEnabled(true);
               jLabRcontFilsob.setText(Integer.toString(conunt));
            
          for (int j = 0; j < conunt; j++) {
               val = jTabSobrinasDays.getValueAt(j, 0).toString();
              cargaDetailcompSobrina(Integer.parseInt(val),j);//calcula la diferencia entre total de compra y total asigando
          }//for

          jTable2.setValueAt(totalonNull(jTabSobrinasDays,2), 0, 0);
          jTable2.setValueAt(totalonNull(jTabSobrinasDays,3), 0, 1);
          jTable2.setValueAt(totalonNull(jTabSobrinasDays,4), 0, 2);
          jTable2.setValueAt(totalonNull(jTabSobrinasDays,5), 0, 3);
          jTable2.setValueAt(totalonNull(jTabSobrinasDays,6), 0, 4);
          jTable2.setValueAt(totalonNull(jTabSobrinasDays,7), 0, 5);
          jTable2.setValueAt(totalonNull(jTabSobrinasDays,8), 0, 6);
          jTable2.setValueAt(totalonNull(jTabSobrinasDays,9), 0, 7);
          
//            jLabtotalsobrantes.setText(totalon(jTabSobrinasDays,9));
         }else{
             JOptionPane.showMessageDialog(null, "Sin sobrantes del dia");
         }
       }
    }//GEN-LAST:event_jButton34ActionPerformed

    private void jButton34MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton34MouseClicked
       if(evt.getClickCount() > 1)
        jButton34.setEnabled(true);
    }//GEN-LAST:event_jButton34MouseClicked

    private void jButton35ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton35ActionPerformed
        int rows = jTabSobrinasDays.getRowCount(); 
        String val = "",sobrin ="";
        for (int j = 0; j < rows; j++) {
            val = jTabSobrinasDays.getValueAt(j, 0).toString();
            sobrin = jTabSobrinasDays.getValueAt(j, 3).toString();
            controlInserts.f5CancelTypesAll("sobras_compraprooved","statAsign ",val, sobrin);
        }
        JOptionPane.showMessageDialog(null, "guradado correctamente");
        jDiaViewSobrinas.dispose();
    }//GEN-LAST:event_jButton35ActionPerformed

    private void jCheckBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox3ActionPerformed
         if(jCheckBox3.isSelected()){
            String fechAs = fn.getFecha(jDCAsignacionDia),
                     fech2 = fn.getSumFechDay(fn.volteaFecha(fechAs, 1),-1);
             int fil2 = jTabVistaComprasDia3.getRowCount();
             dtm = null;
            dtm = (DefaultTableModel) jTabVistaComprasDia3.getModel(); 
            for (int i = 0; fil2 > i; i++) {
                dtm.removeRow(0);
            }
            cargaComprasDiaAsign(fech2, "sobrinas");//carga las compras del dia 
         }else{
             jButton18.doClick();
         }  
    }//GEN-LAST:event_jCheckBox3ActionPerformed

    private void txtBusqFleteAsignKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBusqFleteAsignKeyReleased
       String var = txtBusqFleteAsign.getText(),
       fechAs = fn.getFecha(jDCAsignacionDia);
       mostrarTablaFletesDiaAsign(fechAs, var);//carga las compras del dia 
    }//GEN-LAST:event_txtBusqFleteAsignKeyReleased

    private void jCombBTypeRubrosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCombBTypeRubrosActionPerformed
        int var =  jCombBTypeRubros.getItemCount();
        String caden = "";
        if(var > 0){
             caden = jCombBTypeRubros.getSelectedItem().toString();
             if(caden.equals("Préstamo de efectivo a proveedor")){
                 jComboBox1.setVisible(true);
                 txtConcept.setVisible(false);
                 jLabel133.setText("Proveedor");
                 txtsolict.setEnabled(false);
             }else{
                 jComboBox1.setVisible(false);
                 txtConcept.setVisible(true);
                 jLabel133.setText("Concepto");
                 txtsolict.setEnabled(true);
             }
        }
    }//GEN-LAST:event_jCombBTypeRubrosActionPerformed

    private void jButton36ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton36ActionPerformed
           jDialAltaGastos.setLocationRelativeTo(null);
            jDialAltaGastos.setVisible(true);
            jDialAltaGastos.setTitle("Ingreso de efectivo");
            jLabel127.setText("Ingreso de efectivo");
            jDialAltaGastos.setEnabled(true);
            jCombBTypeRubros.setSelectedItem("Ingreso de efectivo");
            jButton39.setText("Guardar");
    }//GEN-LAST:event_jButton36ActionPerformed

    private void jButton37ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton37ActionPerformed
        String fech = fn.getFecha(jDCAsignacionDia),
                idT = jLabTurno.getText(),
                numc = jLaComp.getText();
            rP.imprim80MMAsignacioncompraProv(fech,numc,false);
    }//GEN-LAST:event_jButton37ActionPerformed

    private void jCheckbpAGADOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckbpAGADOActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckbpAGADOActionPerformed

    private void jTable4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable4MouseClicked
        if(evt.getClickCount() > 1){
            int numTurn = jTable4.getSelectedRow();
            if(numTurn > -1){
                buscarTurn = jTable4.getValueAt(numTurn, 0).toString();
                jLabTurnCancel.setText(buscarTurn);
                String[][] mat = controlInserts.matrizgetTicketsDiaCancel("",buscarTurn);
                jTable5.setModel(new TModel(mat, cabAreasPays));
            }else{
                JOptionPane.showMessageDialog(null, "Debe elegir un Turno");
            }
        }

    }//GEN-LAST:event_jTable4MouseClicked

    private void jButton38ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton38ActionPerformed
        int getElig = jCmBoxIdCancel.getSelectedIndex();
        String idUser = "";
        semanaAct = controlInserts.getSemanTableAct(fn.setDateActual());

        if(getElig > -1){
            if(getElig == 0){
                getIntervalTurns2(1,"",semanaAct[3],semanaAct[4]);//opc,idUser,fech1,fech2
            }
            if(getElig > 0){
                idUser = idCajeros.get(getElig - 1);
                           System.err.println("Eligio user turno cancel : "+ idUser);
                getIntervalTurns2(1,idUser,semanaAct[3],semanaAct[4]);//opc,idUser,fech1,fech2
            }
        }
    }//GEN-LAST:event_jButton38ActionPerformed

    private void jButton40ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton40ActionPerformed
        jDialCalendarMantenim.setLocationRelativeTo(this);
        jDialCalendarMantenim.setVisible(true);
        jDialCalendarMantenim.setEnabled(true);
        jDialCalendarMantenim.setTitle("Cancelaciones");
    }//GEN-LAST:event_jButton40ActionPerformed

    private void jCmBoxIdCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCmBoxIdCancelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCmBoxIdCancelActionPerformed

    private void jButton42ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton42ActionPerformed
        String var = datCtrl.getFechaCal(jCalendar1),
        tittol = jDialCalendarMantenim.getTitle()
        ;
        int subRbro = -1;
        semanaAct = controlInserts.getSemanTableAct(var);
 /*       if(tittol.equals("Corte de caja")){
            int getElig = jCmbBCajeros.getSelectedIndex();
            String idUser = "";
            if(getElig > -1){
                if(getElig == 0){
                    getIntervalTurns(1,"",semanaAct[3],semanaAct[4]);//opc,idUser,fech1,fech2
                }
                if(getElig > 0){
                    idUser = idCajeros.get(getElig - 1);
                    System.err.println("Eligio user turno : "+ idUser);
                    getIntervalTurns(1,idUser,semanaAct[3],semanaAct[4]);//opc,idUser,fech1,fech2
                }
            }
        }   */

        if(tittol.equals("Cancelaciones")){
            int getElig = jCmBoxIdCancel.getSelectedIndex();
            String idUser ="";
            if(getElig > -1){
                if(getElig == 0){
                    getIntervalTurns2(1,"",semanaAct[3],semanaAct[4]);//opc,idUser,fech1,fech2
                }
                if(getElig > 0){
                    idUser = idCajeros.get(getElig - 1);
                    System.err.println("Eligio user turno Cancel : "+ idUser);
                    getIntervalTurns2(1,idUser,semanaAct[3],semanaAct[4]);//opc,idUser,fech1,fech2
                }
            }
        }

        /*
        if(tittol.equals("Ingresos")){
            int opc = jCBoxEsIngreso.getSelectedIndex();
            jLabLetrero.setText("Semana "+semanaAct[2]+" : del "+semanaAct[3]+" al "+semanaAct[4]);

            switch(opc){
                case 0 ://Ingreso ver todos
                getIntervalIngresos(opc,"",semanaAct[3],semanaAct[4]);//envia opcion,subopcion,lapso de semanas
                jLabel18.setText(sumCorteCajFechonas(semanaAct[3],semanaAct[4]));
                break;
                case 1 ://areas
                subRbro = jCBoxEsRubroDet.getSelectedIndex();
                if(subRbro == 0){
                    getIntervalIngresos(opc,"",semanaAct[3],semanaAct[4]);
                    BigDecimal totAreas = new BigDecimal(func.totalLapsoFechas(0,"",semanaAct[3],semanaAct[4]));
                    jLabel18.setText(totAreas.toString());
                }else{
                    getIntervalIngresos(opc,Integer.toString((subRbro + 1)),semanaAct[3],semanaAct[4]);
                    BigDecimal totAreas = new BigDecimal(func.totalLapsoFechas(0,Integer.toString((subRbro + 1)),semanaAct[3],semanaAct[4]));
                    jLabel18.setText(totAreas.toString());
                }
                break;
                case 2 ://ambulantes
                subRbro = jCBoxEsRubroDet.getSelectedIndex();
                if(subRbro == 0){
                    getIntervalIngresos(opc,"",semanaAct[3],semanaAct[4]);
                    BigDecimal totambs = new BigDecimal(func.totalLapsoFechas(1,"",semanaAct[3],semanaAct[4]));
                    jLabel18.setText(totambs.toString());
                }else{
                    if(subRbro == 1)
                    subRbro=6;//semana ambulantes
                    if(subRbro == 2)
                    subRbro=7;//Resguardo ambulantes
                    if(subRbro == 3)
                    subRbro=8;//Inscripciones ambulantes
                    getIntervalIngresos(opc,Integer.toString(subRbro),semanaAct[3],semanaAct[4]);
                    BigDecimal totambs = new BigDecimal(func.totalLapsoFechas(1,Integer.toString(subRbro),semanaAct[3],semanaAct[4]));
                    jLabel18.setText(totambs.toString());
                }
                break;
                case 3 :// cargadores
                subRbro = jCBoxEsRubroDet.getSelectedIndex();
                if(subRbro == 0){
                    getIntervalIngresos(opc,"",semanaAct[3],semanaAct[4]);
                    BigDecimal totcarg = new BigDecimal(func.totalLapsoFechas(2,"",semanaAct[3],semanaAct[4]));
                    BigDecimal totcarg2 = new BigDecimal(func.totalLapsoFechas(3,"",semanaAct[3],semanaAct[4]));
                    BigDecimal totcargAll = func.getSum(totcarg2, totcarg);
                    jLabel18.setText(totcargAll.toString());
                }else{
                    if(subRbro == 1){
                        subRbro=11;//semana cargadores
                        BigDecimal totcarg = new BigDecimal(func.totalLapsoFechas(2,Integer.toString(subRbro),semanaAct[3],semanaAct[4]));
                        jLabel18.setText(totcarg.toString());
                    }
                    if(subRbro == 2){
                        subRbro=8;//8,9,10 -> inscripcion cargadores
                        BigDecimal totcarg = new BigDecimal(func.totalLapsoFechas(2,Integer.toString(subRbro),semanaAct[3],semanaAct[4]));
                        jLabel18.setText(totcarg.toString());
                    }
                    if(subRbro == 3){
                        subRbro=12;//Renta  cargadores
                        BigDecimal totcarg2 = new BigDecimal(func.totalLapsoFechas(3,"",semanaAct[3],semanaAct[4]));
                        jLabel18.setText(totcarg2.toString());
                    }
                    getIntervalIngresos(opc,Integer.toString(subRbro),semanaAct[3],semanaAct[4]);
                }
                break;
                case 4 ://infracciones
                getIntervalIngresos(opc,"",semanaAct[3],semanaAct[4]);
                BigDecimal totInfrac = new BigDecimal(func.totalLapsoFechas(4,"",semanaAct[3],semanaAct[4]));
                jLabel18.setText(totInfrac.toString());
                break;
                case 5 ://otros_venta
                subRbro = jCBoxEsRubroDet.getSelectedIndex();
                if(subRbro == 0){
                    getIntervalIngresos(opc,"",semanaAct[3],semanaAct[4]);
                    BigDecimal totIOthers = new BigDecimal(func.totalLapsoFechas(5,"",semanaAct[3],semanaAct[4]));
                    jLabel18.setText(totIOthers.toString());
                }else{
                    getIntervalIngresos(opc,Integer.toString(subRbro),semanaAct[3],semanaAct[4]);
                    BigDecimal totIOthers = new BigDecimal(func.totalLapsoFechas(5,Integer.toString(subRbro),semanaAct[3],semanaAct[4]));
                    jLabel18.setText(totIOthers.toString());
                }
                break;
            };
            jLabel17.setText(Integer.toString(jTabViewIngresosAll.getRowCount()));
        } */ //****INGRESOSO

    /*    
        if(tittol.equals("Egresos")){
            int opc = jCmBoxEgresos.getSelectedIndex();
            int idRibro = -1;
            jLabel8.setText("Semana "+semanaAct[2]+" : del "+semanaAct[3]+" al "+semanaAct[4]);

            if(opc == 0){
                getIntervalEgresos(opc,semanaAct[3],semanaAct[4]);
                jLabTOTEGRESOS.setText(getTOTALEGRESOS(opc,semanaAct[3],semanaAct[4]));
            }
            if(opc > 0){
                idRibro =Integer.parseInt(idGastosEg.get(opc-1));

                getIntervalEgresos(idRibro,semanaAct[3],semanaAct[4]);
                jLabTOTEGRESOS.setText(getTOTALEGRESOS(idRibro,semanaAct[3],semanaAct[4]));
            }
            jLabel10.setText(Integer.toString(jTable6.getRowCount()));
        }
        */
        jDialCalendarMantenim.dispose();
    }//GEN-LAST:event_jButton42ActionPerformed

    public Image getIconImage() {
        Image retValue = Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("image/icons8_customer_32px_1.png"));
        return retValue;
    }

    //carga de detalle de sobras del dia
    public void cargaDetailcompSobrina(int opc, int fila) {
        Connection cn = con2.conexion();
        String aux = "";
        int cantColumnas = 0, cantFilas = 0, temporal = 0, bandera = 0;
        String sql = "", sql2 = "";
        sql = "SELECT productocal.codigo,SUM(relcomprapedido.cantidadCajasRel) AS sumaType\n" +
                "FROM relcomprapedido\n" +
                "INNER JOIN compraprooved\n" +
                "ON compraprooved.id_compraProve = relcomprapedido.id_compraProveed AND compraprooved.id_compraProve = '"+opc+"'\n" +
                "INNER JOIN productocal\n" +
                "ON productocal.codigo = relcomprapedido.tipoMercanRel\n" +
                "GROUP BY productocal.codigo;";
        Statement st = null;
        ResultSet rs = null;
        try {
            st = cn.createStatement();
            rs = st.executeQuery(sql);
            // System.out.print("Filas: "+cantFilas+"\tColumnas: "+cantColumnas+"\n");
            while (rs.next()) {//es necesario el for para llenar dinamicamente la lista, ya que varia el numero de columnas de las tablas
                for (int x = 1; x <= rs.getMetaData().getColumnCount(); x++) {
                    if (x == 1) {//valor,fila,columna
                        aux = jTabSobrinasDays.getValueAt(fila, rs.getInt(x) +1).toString();
                        jTabSobrinasDays.setValueAt(Integer.parseInt(aux) - rs.getInt(x+1) , fila,rs.getInt(x) +1 );//se le suma 1 por las columnas id,nombre de la jTable
                    }//System.out.print("["+x+"]"+" -> "+rs.getString(x));                   
                }//for
            }//while

        } catch (SQLException ex) {
            Logger.getLogger(interno1.class.getName()).log(Level.SEVERE, null, ex);
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
    }//LlenaDetailPedido

    public final void mostrarTabla() {
//        String datos[][] =  new String[2][4];
        String[][] datos = {{"1", "Jimena", "Perez", "23-10-2019", "ACTIVO"}, {"2", "JOSE", "SANCHEZ", "23-11-2018", "BAJA"}, {"4", "ESPERANZA", "ALVARADO", "25-01-2017", "ACTIVO"}};
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("ID");
        modelo.addColumn("NOMBRE");
        modelo.addColumn("APELLIDOS");
        modelo.addColumn("FECHA");
        modelo.addColumn("STATUS");
        for (int x = 0; x < datos.length; x++) {
            for (int y = 0; y < datos[x].length; y++) {
                //System.out.println ("[" + x + "," + y + "] = " + datos[x][y]);
                modelo.addRow(datos[x]);
            }
        }
        jTableAltasCli.setModel(modelo);
    }

    void mostrarTablaAltaCli(int opc, String var, String campo) {//opc=index-combo,var=campo-busqueda,campo=parametro-nombre
        Connection cn = con2.conexion();
        DefaultTableModel modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        TableColumn columna;
        String consul = "";

        if (opc == 0) {
            modelo.addColumn("Id");
            modelo.addColumn("Nombre");
            modelo.addColumn("Apellidos");
            modelo.addColumn("Localidad");
            modelo.addColumn("Status");
            modelo.addColumn("Fecha Registro");
        }

        if (opc == 1) {
            modelo.addColumn("Id");
            modelo.addColumn("Nombre");
            modelo.addColumn("Apellidos");
            modelo.addColumn("Localidad");
            modelo.addColumn("Status");
            modelo.addColumn("Tipo");

        }
        if (opc == 2) {
            modelo.addColumn("Id");
            modelo.addColumn("Nombre");
            modelo.addColumn("Apellidos");
            modelo.addColumn("Localidad");
            modelo.addColumn("Status");

        }

        if (opc == 3) {
            modelo.addColumn("Id");
            modelo.addColumn("Nombre");
            modelo.addColumn("Localidad");
            modelo.addColumn("Status");
            modelo.addColumn("Fecha Registro");
        }

        jTableAltasCli.setModel(modelo);

        TableColumnModel columnModel = jTableAltasCli.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(15);
        columnModel.getColumn(1).setPreferredWidth(50);

        jTableAltasCli.getColumnModel().getColumn(0).setMaxWidth(100);
        jTableAltasCli.getColumnModel().getColumn(0).setMinWidth(0);
        jTableAltasCli.getColumnModel().getColumn(0).setPreferredWidth(100);

        if (var.equals("")) {
            consul = "SELECT * FROM " + atribAltaCli + " ORDER BY " + campo + " ASC";
        } else {

            consul = "SELECT * FROM " + atribAltaCli + " WHERE " + campo + " LIKE '%" + var + "%' ORDER BY " + campo + " ASC";
//        System.out.println(consul);
        }
        String datos[] = new String[6];
        List<String> contentL = new ArrayList<String>();
        Statement st = null;
        ResultSet rs = null;
        int i = 1;
        try {
            st = cn.createStatement();
            rs = st.executeQuery(consul);
            while (rs.next()) {
                /*for (int x=1;x<= rs.getMetaData().getColumnCount();x++) {
                   //System.out.print(x+" -> "+rs.getString(x));
                   contentL.add(rs.getString(x));
                 }*/

                if (opc == 1) {//si eligio proveedores
                    datos[0] = rs.getString(1);
                    datos[1] = rs.getString(2);
                    datos[2] = rs.getString(3);
                    datos[3] = rs.getString(4);
                    datos[4] = rs.getString(5);
                    datos[5] = rs.getString(8);
                    if (rs.getString(5).equals("1")) {
                        datos[4] = "ACTIVO";
                    } else if (rs.getString(5).equals("0")) {
                        datos[4] = "INACTIVO";
                    }
                    if (rs.getString(8).equals("1")) {
                        datos[5] = "MAYORISTA";
                    } else if (rs.getString(8).equals("0")) {
                        datos[5] = "MINORISTA";
                    }
                } else {
                    datos[0] = rs.getString(1);
                    datos[1] = rs.getString(2);
                    datos[2] = rs.getString(3);
                    datos[3] = rs.getString(4);
                    datos[4] = rs.getString(5);
                        datos[5] = rs.getString(6);
                    if ((opc == 0 || opc == 2) && rs.getString(5).equals("1")) {
                        datos[4] = "ACTIVO";
                    } else if ((opc == 0 || opc == 1 || opc == 2) && rs.getString(5).equals("0")) {
                        datos[4] = "INACTIVO";
                    }
                    if (opc == 3 && rs.getString(4).equals("1")) {
                        datos[3] = "ACTIVO";
                    } else if (opc == 3 && rs.getString(4).equals("0")) {
                        datos[3] = "INACTIVO";
                    }
                }//else opc ==1
                modelo.addRow(datos);
            }
            jTableAltasCli.setModel(modelo);
        } catch (SQLException ex) {
            Logger.getLogger(interno1.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
//                    System.out.println( "cierra conexion a la base de datos" );    
            try {
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (cn != null) {
                    cn.close();
                }
//                        if(cn !=null) cn.close();
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
            }
        }
    }//termina mostrartablaAltaCli

    private void limpiaCamposAltaCli() {
        txtIdParam.setText("");

        jTextNombre.setText("");
        jTextApellidos.setText("");
        jTextLocalidad.setText("");

        jRad1Activo.setSelected(true);

        jTexTelefono.setText("");
        txtQuintoAltas.setText("");
        jDateChFechaAlta.setDate(cargafecha());
        txtBusqAltas.setText("");
    }
    //****** TERMINA CODIGO DE ALTA CLIENTES

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
            java.util.logging.Logger.getLogger(interno1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(interno1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(interno1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(interno1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new interno1().setVisible(true);
            }
        });
    }
//*** CODIGO PARA PESTAÑA DE PEDIDOS

    private List llenacomboAltaClis() {
        Connection cn = con2.conexion();
        conten.clear();
        jCombPedidoClient.removeAllItems();
        jComPedBusqCli.removeAllItems();
        jCombCliVentaP.removeAllItems();
        jCCliVentaPiso.removeAllItems();//clientes para venta piso
        String consul = "SELECT id_cliente,nombre from clientepedidos ORDER BY nombre";
        Statement st = null;
        ResultSet rs = null;
        try {
            st = cn.createStatement();
            rs = st.executeQuery(consul);
            while (rs.next()) {
                conten.add(rs.getString(1));
                jCombPedidoClient.addItem(rs.getString(2));
                jComPedBusqCli.addItem(rs.getString(2));
                jCombCliVentaP.addItem(rs.getString(2));
                jCCliVentaPiso.addItem(rs.getString(2));
            }
        } catch (SQLException ex) {
            Logger.getLogger(interno1.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
            }

        }
        return conten;
    }//Llena comboAltasClis    

    private void llenacomboProducts() {
        Connection cn = con2.conexion();
        idProducts.clear();
        jCombProdPedidos.removeAllItems();
        jCombProductProv.removeAllItems();
        String consul = "SELECT codigo,nombreP FROM productocal WHERE categoria = 'VENT'";
        int i = 1;
        Statement st = null;
        ResultSet rs = null;
        try {
            st = cn.createStatement();
            rs = st.executeQuery(consul);
            while (rs.next()) {
                idProducts.add(rs.getString(1));
                jCombProdPedidos.addItem(rs.getString(2));
                jCombProductProv.addItem(rs.getString(2));
            }
        } catch (SQLException ex) {
            Logger.getLogger(interno1.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
            }
        }
    }//Llena comboAltasClis    

    private void llenacomboMayoristas(int opc) {//opc para caso 1 JmenuItem 0=compraprov; 1=filter comprasPRv
        Connection cn = con2.conexion();
        idProoved.clear();
        jCElijaProovedor.removeAllItems();
        jCombBProvBusqCompra.removeAllItems();

        String consul = "SELECT id_Proveedor,nombreP FROM proveedor WHERE TIPO = 1 ";
        int i = 1;
        Statement st = null;
        ResultSet rs = null;
        try {
            st = cn.createStatement();
            rs = st.executeQuery(consul);

            switch (opc) {
                case 0:
                    while (rs.next()) {
                        idProoved.add(rs.getString(1));
                        jCElijaProovedor.addItem(rs.getString(2));
                    }
                    break;

                case 1:
                    while (rs.next()) {
                        idProoved.add(rs.getString(1));
                        jCombBProvBusqCompra.addItem(rs.getString(2));
                    }
                    break;
            };

        } catch (SQLException ex) {
            Logger.getLogger(interno1.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
            }
        }
    }//llenacomboMayoristas    

    //llena combo para ventas piso con todos los productos
    private void llenacomboProductsVenta() {
        Connection cn = con2.conexion();
        idProductsVent.clear();
        jCombProdVentaP.removeAllItems();
        String consul = "SELECT codigo,nombreP FROM productocal ";
        Statement st = null;
        ResultSet rs = null;
        try {
            st = cn.createStatement();
            rs = st.executeQuery(consul);
            while (rs.next()) {
                idProductsVent.add(rs.getString(1));
                jCombProdVentaP.addItem(rs.getString(2));
            }
        } catch (SQLException ex) {
            Logger.getLogger(interno1.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
            }
        }
    }//Llena productos ventas

    //LLENA COMBO PROOVEDORES
    private void llenacomboProovedores() {
        Connection cn = con2.conexion();
        idProoved.clear();
        jComBPrestamosProv.removeAllItems();
        jCombBProvBusqPrest.removeAllItems();
        jCElijaProovedor.removeAllItems();
        jCombBProvBusqCompra.removeAllItems();
        jComboBox1.removeAllItems();
        String consul = "SELECT id_Proveedor,nombreP FROM proveedor WHERE statusP=1 ORDER BY nombreP";
        Statement st = null;
        ResultSet rs = null;
        try {
            st = cn.createStatement();
            rs = st.executeQuery(consul);
            while (rs.next()) {
                idProoved.add(rs.getString(1));
                jComBPrestamosProv.addItem(rs.getString(2));
                jCombBProvBusqPrest.addItem(rs.getString(2));
                jCElijaProovedor.addItem(rs.getString(2));
                jCombBProvBusqCompra.addItem(rs.getString(2));//filtro busqueda compras
                jComboBox1.addItem(rs.getString(2));//jcombo pretamos salida
            }
        } catch (SQLException ex) {
            Logger.getLogger(interno1.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
            }
        }
    }//llenaComboProveedores

    private void llenacomboProdPrest() {
        Connection cn = con2.conexion();
        idProdPrest.clear();
        jComBProveedor.removeAllItems();
        String consul = "SELECT codigo,nombreP from productocal WHERE categoria = 'PRES' ORDER BY nombreP";
        Statement st = null;
        ResultSet rs = null;
        try {
            st = cn.createStatement();
            rs = st.executeQuery(consul);
            while (rs.next()) {
                idProdPrest.add(rs.getString(1));
                jComBProveedor.addItem(rs.getString(2));
            }
        } catch (SQLException ex) {
            Logger.getLogger(interno1.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
            }
        }
    }//

    //*CODIGO PARA CREAR MATRIZ DINAMICA DE VISTA COMPRAS A PROVEEDOR*/
    public void consultDetailCompra(int opc, int fila) {//recibe Numdecompra,Numfila a insertar
        Connection cn = con2.conexion();
        String aux = "";
        int cantColumnas = 0, cantFilas = 0, temporal = 0, bandera = 0;
        String sql = "", sql2 = "";
        sql = "SELECT * FROM detailcompraprooved WHERE id_compraP = '" + opc + "'";
        sql2 = "SELECT compraprooved.id_compraProve,IF(proveedor.nombreP='SUBASTA',compraprooved.descripcionSubasta,proveedor.nombreP),SUM(detailcompraprooved.cantCajasC),"
                + "SUM(detailcompraprooved.cantCajasC*detailcompraprooved.precCajaC)\n"
                + "FROM\n"
                + "proveedor\n"
                + "INNER JOIN\n"
                + "compraprooved \n"
                + "ON\n"
                + "compraprooved.id_ProveedorC = proveedor.id_Proveedor AND compraprooved.id_compraProve = '" + opc + "'\n"
                + "INNER JOIN \n"
                + "detailcompraprooved\n"
                + "ON\n"
                + "detailcompraprooved.id_compraP = compraprooved.id_compraProve;";
        Statement st = null;
        ResultSet rs = null;
        try {
            st = cn.createStatement();
            rs = st.executeQuery(sql);
            // System.out.print("Filas: "+cantFilas+"\tColumnas: "+cantColumnas+"\n");
            while (rs.next()) {//es necesario el for para llenar dinamicamente la lista, ya que varia el numero de columnas de las tablas
                for (int x = 1; x <= rs.getMetaData().getColumnCount(); x++) {
                    if (x == 3) {//valor,fila,columna
                        jTabVistaComprasDia.setValueAt(rs.getInt(x + 1), fila, rs.getInt(x) + 1);//se le suma 1 por las columnas id,nombre de la jTable
                        if (controlInserts.validaIsMasDeUnProducto(opc, rs.getInt(x)) > 1) {//si existe mas de una vez ese producto en la compra a mayorista, => hacemos la sumatoria y reescribimos eso
                            //   System.out.println("\tExiste+de 1 vex: "+rs.getInt(x));
                            jTabVistaComprasDia.setValueAt(controlInserts.sumaIsMasDeUnProducto(opc, rs.getInt(x)), fila, rs.getInt(x) + 1);
                        }
                    }//System.out.print("["+x+"]"+" -> "+rs.getString(x));                   
                }//for
            }//while

            st = null;
            rs = null;

            st = cn.createStatement();
            rs = st.executeQuery(sql2);
            while (rs.next()) {
                jTabVistaComprasDia.setValueAt(rs.getInt(1), fila, 0);

                if (controlInserts.validaRelCompPed(Integer.toString(rs.getInt(1)), "compramayoreo")) {
                    colorePD.add(fila);
                }

                jTabVistaComprasDia.setValueAt(rs.getString(2), fila, 1);
                jTabVistaComprasDia.setValueAt(rs.getString(3), fila, 9);
                jTabVistaComprasDia.setValueAt(rs.getString(4), fila, 10);
            }
        } catch (SQLException ex) {
            Logger.getLogger(interno1.class.getName()).log(Level.SEVERE, null, ex);
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

    //FUNCION PARA LLENAS LA TABLA DE COMPRAS DEL DIA
    private void cargaComprasDia(String fech) {
        String[][] arre = controlInserts.consultCompra(fech, "");//regresa idCompra, idProveedorCompra 
        if (arre.length > 0) {//si are>0 signfica que hay alemos una compra del dia
            tabCompras = (DefaultTableModel) jTabVistaComprasDia.getModel();
            int filas = tabCompras.getRowCount(), filasPrec = tabCompras.getRowCount();
            if (filas > 0) {
                for (int i = 0; filas > i; i++) {
                    tabCompras.removeRow(0);
                }
            }
            tabCompras.setRowCount(arre.length);//CREAMOS EL NUMERO DE FILAS SEGUN LA MATRIZ DEVUELTA
            jTabVistaComprasDia.setModel(tabCompras);//agrego el modelo creado con el numero de filas devuelto
            for (int j = 0; j < arre.length; j++) {
                for (int k = 0; k < arre[0].length; k++) {
                    if (k == 0) {
                        consultDetailCompra(Integer.parseInt(arre[j][k]), j);//envia matriz con id_compra,id_proveedor,fila
                    }
                }
            }
            ColorCelda c = new ColorCelda();
            c.arrIntRowsIluminados = controlInserts.fnToArray(colorePD);
            jTabVistaComprasDia.setDefaultRenderer(Object.class, c);
            colorePD.clear();
        } else {
            JOptionPane.showMessageDialog(null, "No hay compras del dia");
        }
    }//Fin cargaComprasDia

    /// CARGA LA SUMA DE CAJAS, IMPORTE TOTAL DE COMPRAS Y SUMA DE TIPO DE MERCANCIA EN EL DIA
    protected void cargaTotCompDayProveedor(JTable tab, String fech, int opc) {
        Connection cn = con2.conexion();
        int cantColumnas = 0, cantFilas = 0, temporal = 0, bandera = 0,
                fil =opc;
      
        String sql = "", sql2 = "";
        sql = "SELECT productocal.codigo,\n"
                + " SUM(detailcompraprooved.cantCajasC) AS sumaType,\n"
                + "SUM(detailcompraprooved.cantCajasC * detailcompraprooved.precCajaC) AS sumaTypePrec\n"
                + "FROM \n"
                + "detailcompraprooved\n"
                + " INNER JOIN \n"
                + "compraprooved \n"
                + "ON \n"
                + "detailcompraprooved.id_compraP = compraprooved.id_compraProve \n"
                + "AND compraprooved.fechaCompra = '" + fech + "'\n"
                + "INNER JOIN \n"
                + "productocal\n"
                + "ON \n"
                + "productocal.codigo = detailcompraprooved.codigoProdC\n"
                + "GROUP BY productocal.codigo;";

        sql2 = "SELECT \n"
                + "SUM(detailcompraprooved.cantCajasC*detailcompraprooved.precCajaC) AS numDinero,\n"
                + "SUM(detailcompraprooved.cantCajasC) AS numCaja\n"
                + "FROM \n"
                + " detailcompraprooved\n"
                + "INNER JOIN \n"
                + " compraprooved\n"
                + "ON\n"
                + " detailcompraprooved.id_compraP = compraprooved.id_compraProve \n"
                + " AND compraprooved.fechaCompra = '" + fech + "'\n"
                + "INNER JOIN\n"
                + " productocal\n"
                + "ON\n"
                + " productocal.codigo = detailcompraprooved.codigoProdC;";
        Statement st = null;
        ResultSet rs = null;
        try {
            st = cn.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {//es necesario el for para llenar dinamicamente la lista, ya que varia el numero de columnas de las tablas
                for (int x = 1; x <= rs.getMetaData().getColumnCount(); x++) {
                    if (x == 1) {
                        tab.setValueAt(rs.getInt(x + 1), fil, rs.getInt(x) - 1);//se le suma 1 por las columnas id,nombre de la jTable
                       if(opc == 0){
                             tab.setValueAt(rs.getString(x + 2), 1, rs.getInt(x) - 1);
                       }
                    }
                }//for
            }//while
            st = null;
            rs = null;
            st = cn.createStatement();
            rs = st.executeQuery(sql2);
            while (rs.next()) {
                if(opc == 0){
                    tab.setValueAt(rs.getString(1), 0, 8);
                }
                tab.setValueAt(rs.getString(2), fil, 7);
            }
        } catch (SQLException ex) {
            Logger.getLogger(interno1.class.getName()).log(Level.SEVERE, null, ex);
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
    private void cargaComprasDiaAsign(String fech, String filter) {
        String[][] arre = controlInserts.consultCompra(fech, filter);
        if (arre.length > 0) {
            if(filter.equals("sobrinas")){
                 jLaComp.setText("Inventario del dia: "+fech);
            }
            
            tabCompras = (DefaultTableModel) jTabVistaComprasDia3.getModel();
            int filas = tabCompras.getRowCount(), filasPrec = tabCompras.getRowCount();
            if (filas > 0) {
                for (int i = 0; filas > i; i++) {
                    tabCompras.removeRow(0);
                }
            }
            tabCompras.setRowCount(arre.length);//CREAMOS EL NUMERO DE FILAS SEGUN LA MATRIZ DEVUELTA
            jTabVistaComprasDia3.setModel(tabCompras);//agrego el modelo creado con el numero de filas devuelto
            for (int j = 0; j < arre.length; j++) {
                for (int k = 0; k < arre[0].length; k++) {
                    if (k == 0) {
                        consultDetailCompraAsign(Integer.parseInt(arre[j][k]), j);//envia matriz con id_compra,id_proveedor,fila
                    }
                }
            }
            ColorCelda c = new ColorCelda();
            c.arrIntRowsIluminados = controlInserts.fnToArray(coloreA);
            jTabVistaComprasDia3.setDefaultRenderer(Object.class, c);
            coloreA.clear();
        } else {

           if(filter.equals("sobrinas"))
                jLaComp.setText("Sin inventario del dia"+fech);
            else
            jLaComp.setText("No hay compras del día");
        }
    }//Fin cargaComprasDia

    public void consultDetailCompraAsign(int opc, int fila) {
        Connection cn = con2.conexion();
        int cantColumnas = 0, cantFilas = 0, temporal = 0, bandera = 0;
        String sql = "", sql2 = "";
        sql = "SELECT * FROM detailcompraprooved WHERE id_compraP = '" + opc + "'";
        sql2 = "SELECT compraprooved.id_compraProve,IF(proveedor.nombreP='SUBASTA',compraprooved.descripcionSubasta,proveedor.nombreP),SUM(detailcompraprooved.cantCajasC),"
                + "SUM(detailcompraprooved.cantCajasC*detailcompraprooved.precCajaC)\n"
                + "FROM\n"
                + "proveedor\n"
                + "INNER JOIN\n"
                + "compraprooved \n"
                + "ON\n"
                + "compraprooved.id_ProveedorC = proveedor.id_Proveedor AND compraprooved.id_compraProve = '" + opc + "'\n"
                + "INNER JOIN \n"
                + "detailcompraprooved\n"
                + "ON\n"
                + "detailcompraprooved.id_compraP = compraprooved.id_compraProve;";

        Statement st = null;
        ResultSet rs = null;
        try {
            st = cn.createStatement();
            rs = st.executeQuery(sql);
            // System.out.print("Filas: "+cantFilas+"\tColumnas: "+cantColumnas+"\n");
            while (rs.next()) {//es necesario el for para llenar dinamicamente la lista, ya que varia el numero de columnas de las tablas
                for (int x = 1; x <= rs.getMetaData().getColumnCount(); x++) {
                    if (x == 3) {//valor,fila,columna
                        jTabVistaComprasDia3.setValueAt(rs.getInt(x + 1), fila, rs.getInt(x) + 1);//se le suma 1 por las columnas id,nombre de la jTable
                        if (controlInserts.validaIsMasDeUnProducto(opc, rs.getInt(x)) > 1) {//si existe mas de una vez ese producto en la compra a mayorista, => hacemos la sumatoria y reescribimos eso
                            jTabVistaComprasDia3.setValueAt(controlInserts.sumaIsMasDeUnProducto(opc, rs.getInt(x)), fila, rs.getInt(x) + 1);
                        }
                    }//System.out.print("["+x+"]"+" -> "+rs.getString(x));                   
                }//for
            }//while

            st = null;
            rs = null;
            st = cn.createStatement();
            rs = st.executeQuery(sql2);
            while (rs.next()) {
                jTabVistaComprasDia3.setValueAt(rs.getInt(1), fila, 0);
                if (controlInserts.validaRelCompPed(Integer.toString(rs.getInt(1)), "id_compraProveed")) {
                   temporal = controlInserts.sumaRelCompPed(Integer.toString(rs.getInt(1)), "id_compraProveed");
                    //if(temporal == rs.getInt(3)){
                        coloreA.add(fila);    
                   // }
                }
                jTabVistaComprasDia3.setValueAt(rs.getString(2), fila, 1);
                jTabVistaComprasDia3.setValueAt(rs.getInt(3), fila, 9);
                jTabVistaComprasDia3.setValueAt(rs.getInt(3) - temporal, fila,10);
            }
        } catch (SQLException ex) {
            Logger.getLogger(interno1.class.getName()).log(Level.SEVERE, null, ex);
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

    //CARGA PEDIDOS DEL DIA VISTA DE DETALLE
    private void cargaPedidosDiaAsign(String fech) {
        String[][] arre = controlInserts.consultPedidoAsign("",fech,"","");//regresa idPedido,idCliente
        if (arre.length > 0) {
            tabCompras = (DefaultTableModel) jTabVistaPedidosDia1.getModel();
            int filas = tabCompras.getRowCount(), filasPrec = tabCompras.getRowCount();
            if (filas > 0) {
                for (int i = 0; filas > i; i++) {
                    tabCompras.removeRow(0);
                }
            }
            tabCompras.setRowCount(arre.length);//CREAMOS EL NUMERO DE FILAS SEGUN LA MATRIZ DEVUELTA
            jTabVistaPedidosDia1.setModel(tabCompras);//agrego el modelo creado con el numero de filas devuelto

            for (int j = 0; j < arre.length; j++) {
                for (int k = 0; k < arre[0].length; k++) {
                    if (k == 0) {
                        consultDetailPedidosAsign(Integer.parseInt(arre[j][k]), j);//envia matriz con id_compra,id_proveedor,fila
                    }
                }
            }
            ColorCelda c1 = new ColorCelda();
            c1.arrIntRowsIluminados = controlInserts.fnToArray(coloreB);
            jTabVistaPedidosDia1.setDefaultRenderer(Object.class, c1);

          //  ListIterator<Integer> itr = coloreB.listIterator();
        //    while (itr.hasNext()) {
       //         System.out.println("ColoreB -> " + itr.next());
         //   }
            coloreB.clear();
        } else {

            jLabPed.setText("No hay pedidos del día");

            //JOptionPane.showMessageDialog(null, "No hay compras del dia");
        }
    }//Fin cargaPedidosDia

    public void consultDetailPedidosAsign(int opc, int fila) {
        Connection cn = con2.conexion();
        int cantColumnas = 0, cantFilas = 0, temporal = 0, bandera = 0,asignadosC = 0;
        String sql = "", sql2 = "";
        sql = "SELECT * FROM detailpedidio WHERE id_pedidioD = '" + opc + "'";
        sql2 = "SELECT\n"
                + "pedidocliente.id_pedido,clientepedidos.nombre,SUM(detailpedidio.cantidadCajas)\n"
                + "FROM\n"
                + "clientepedidos\n"
                + "INNER JOIN\n"
                + "pedidocliente\n"
                + "ON\n"
                + "pedidocliente.id_clienteP = clientepedidos.id_cliente AND pedidocliente.id_pedido = '" + opc + "'\n"
                + "INNER JOIN\n"
                + "detailpedidio\n"
                + "ON\n"
                + "detailpedidio.id_PedidioD = pedidocliente.id_pedido;";
        Statement st = null;
        ResultSet rs = null;
        try {
            st = cn.createStatement();
            rs = st.executeQuery(sql);
            // System.out.print("Filas: "+cantFilas+"\tColumnas: "+cantColumnas+"\n");
            while (rs.next()) {//es necesario el for para llenar dinamicamente la lista, ya que varia el numero de columnas de las tablas
                for (int x = 1; x <= rs.getMetaData().getColumnCount(); x++) {
                    if (x == 3) {//valor,fila,columna
                        jTabVistaPedidosDia1.setValueAt(rs.getInt(x + 1), fila, rs.getInt(x) + 1);//se le suma 1 por las columnas id,nombre de la jTable
                    }//System.out.print("["+x+"]"+" -> "+rs.getString(x));                   
                }//for
            }//while
            st = null;
            rs = null;
            st = cn.createStatement();
            rs = st.executeQuery(sql2);
            while (rs.next()) {
                jTabVistaPedidosDia1.setValueAt(rs.getInt(1), fila, 0);
                if (controlInserts.validaRelCompPed(Integer.toString(rs.getInt(1)), "id_pedidoCli")) {
                    asignadosC = controlInserts.sumaRelCompPed(Integer.toString(rs.getInt(1)),"id_pedidoCliSum");
                    coloreB.add(fila);
                }

                jTabVistaPedidosDia1.setValueAt(rs.getString(2), fila, 1);
                jTabVistaPedidosDia1.setValueAt(rs.getInt(3), fila, 9);
                jTabVistaPedidosDia1.setValueAt(rs.getInt(3) - asignadosC, fila, 10);

            }
        } catch (SQLException ex) {
            Logger.getLogger(interno1.class.getName()).log(Level.SEVERE, null, ex);
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

    /**
     * codigo para ventas de piso
     */
    private List llenacomboClientesVentaPiso() {
        Connection cn = con2.conexion();
        jCombCliVentaP.removeAllItems();
        conten.clear();
        String consul = "SELECT id_cliente,nombre from clientepedidos ORDER BY nombre";
        Statement st = null;
        ResultSet rs = null;
        try {
            st = cn.createStatement();
            rs = st.executeQuery(consul);
            while (rs.next()) {
                conten.add(rs.getString(1));
                jCombCliVentaP.addItem(rs.getString(2));
            }
        } catch (SQLException ex) {
            Logger.getLogger(interno1.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
            }
        }
        return conten;
    }//Llena comboAltasClis    

    /*CODIGO PARA PESTAÑA FLETES */
    /**
     * codigo para ventas de piso
     */
    private void llenacomboFletes() {
        Connection cn = con2.conexion();
        jCBAltasFletes.removeAllItems();
        jCfleteroOpc.removeAllItems();
        contenFletes.clear();
        String consul = "SELECT id_Fletero,nombreF from fletero ORDER BY nombreF";
        Statement st = null;
        ResultSet rs = null;
        try {
            st = cn.createStatement();
            rs = st.executeQuery(consul);
            while (rs.next()) {
                contenFletes.add(rs.getString(1));
                jCBAltasFletes.addItem(rs.getString(2));
                jCfleteroOpc.addItem(rs.getString(2));
            }
        } catch (SQLException ex) {
            Logger.getLogger(interno1.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
            }
        }
        //  return conten;
    }//LlenacomboFletes

    void mostrarTablaFletesDia(String fech) {//opc=index-combo,var=campo-busqueda,campo=parametro-nombre
        Connection cn = con2.conexion();
        DefaultTableModel modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
        TableColumn columna;
        String consul = "";
        modelo.addColumn("Folio");
        modelo.addColumn("Fletero");
        modelo.addColumn("Fecha");
        modelo.addColumn("Chofer");
        modelo.addColumn("Unidad");
        modelo.addColumn("Costo");
        modelo.addColumn("Status");
        modelo.addColumn("Nota");
        modelo.addColumn("Carga");

        jTabFletesDia.setModel(modelo);

        jTabFletesDia.getColumnModel().getColumn(0).setMaxWidth(150);
        jTabFletesDia.getColumnModel().getColumn(0).setMinWidth(80);
        jTabFletesDia.getColumnModel().getColumn(0).setPreferredWidth(80);

        jTabFletesDia.getColumnModel().getColumn(2).setMaxWidth(150);
        jTabFletesDia.getColumnModel().getColumn(2).setMinWidth(150);
        jTabFletesDia.getColumnModel().getColumn(2).setPreferredWidth(150);
        //   if (var.equals("")) {
        consul = "SELECT fleteEnviado.id_fleteE, fletero.nombreF,fleteEnviado.fechaFlete,fleteEnviado.choferFlete,\n"
                + "fleteEnviado.trocaFlete,fleteEnviado.costoFlete,fleteEnviado.status,fleteEnviado.recivioFlete,fleteEnviado.cargaParcial\n"
                + "FROM\n"
                + "fleteEnviado\n"
                + "INNER JOIN\n"
                + "fletero\n"
                + "ON fletero.id_Fletero = fleteEnviado.id_FleteroE AND fleteEnviado.fechaFlete = '" + fech + "';";
        //  } else {

        //  consul = "SELECT * FROM fleteenviado WHERE fechaFlete = '"+fech+"' LIKE '%" + var + "%' ORDER BY id_fleteE ASC";
//        System.out.println(consul);
        //}
        String datos[] = new String[9];
        List<String> contentL = new ArrayList<String>();
        Statement st = null;
        ResultSet rs = null;
        int i = 1;
        try {
            st = cn.createStatement();
            rs = st.executeQuery(consul);

            while (rs.next()) {
                datos[0] = rs.getString(1);
                datos[1] = rs.getString(2);
                datos[2] = rs.getString(3);
                datos[3] = rs.getString(4);
                datos[4] = rs.getString(5);
                datos[5] = rs.getString(6);
                datos[6] = rs.getString(7);
                if (rs.getString(7).equals("0")) {
                    datos[6] = "PENDIENTE";
                } else if (rs.getString(7).equals("1")) {
                    datos[6] = "PAGADO";
                }
                datos[7] = rs.getString(8);
                datos[8] = rs.getString(9);
                modelo.addRow(datos);
            }
            jTabFletesDia.setModel(modelo);
        } catch (SQLException ex) {
            Logger.getLogger(interno1.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
//                    System.out.println( "cierra conexion a la base de datos" );    
            try {
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (cn != null) {
                    cn.close();
                }
//                        if(cn !=null) cn.close();
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
            }
        }
        
        jLabContaFletes.setText(Integer.toString(jTabFletesDia.getRowCount()));
    }//termina mostrartablaFletes
//´MOSTRAR TABLA DE FLETES ASIGN DEL DIA

    void mostrarTablaFletesDiaAsign(String fech, String val) {//opc=index-combo,var=campo-busqueda,campo=parametro-nombre
        Connection cn = con2.conexion();
        DefaultTableModel modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
        TableColumn columna;
        String consul = "";
        modelo.addColumn("Folio");
        modelo.addColumn("Fletero");
        modelo.addColumn("Unidad");
        modelo.addColumn("Costo");
        modelo.addColumn("TotCarga");
        modelo.addColumn("Lleva");
        modelo.addColumn("Restan");
        
        jTabFletesDia1.setModel(modelo);
        jTabFletesDia1.getColumnModel().getColumn(0).setMaxWidth(100);
        jTabFletesDia1.getColumnModel().getColumn(0).setMinWidth(0);
        jTabFletesDia1.getColumnModel().getColumn(0).setPreferredWidth(75);

        if (val.isEmpty()) {
          consul = "SELECT fleteEnviado.id_fleteE, fletero.nombreF,\n"
                + "fleteEnviado.trocaFlete,fleteEnviado.costoFlete,fleteEnviado.cargaParcial,IF( SUM(relCompraPedido.cantidadCajasRel) IS NULL,0, SUM(relCompraPedido.cantidadCajasRel)),\n"
                + "(fleteEnviado.cargaParcial - SUM(relCompraPedido.cantidadCajasRel)) AS restin\n"
                + "FROM\n"
                + "fletero\n"
                + "INNER JOIN\n"
                + "fleteEnviado\n"
                + "ON fletero.id_Fletero = fleteEnviado.id_FleteroE AND fleteEnviado.fechaFlete = '" +fech +"'  \n"
                + "left JOIN relcomprapedido \n"
                + "ON relcomprapedido.id_fleteP = fleteenviado.id_fleteE "
                + "group by fleteEnviado.id_fleteE ;";
        } else {
         consul = "SELECT fleteEnviado.id_fleteE, fletero.nombreF,\n"
                + "fleteEnviado.trocaFlete,fleteEnviado.costoFlete,fleteEnviado.cargaParcial,IF( SUM(relCompraPedido.cantidadCajasRel) IS NULL,0, SUM(relCompraPedido.cantidadCajasRel)),\n"
                + "(fleteEnviado.cargaParcial - SUM(relCompraPedido.cantidadCajasRel)) AS restin\n"
                + "FROM\n"
                + "fletero\n"
                + "INNER JOIN\n"
                + "fleteEnviado\n"
                + "ON fletero.id_Fletero = fleteEnviado.id_FleteroE AND fleteEnviado.fechaFlete = '" +fech +"' AND (fleteenviado.id_fleteE LIKE '"+val+"%' OR fleteenviado.trocaFlete LIKE '%"+val+"%') \n"
                + "left JOIN relcomprapedido \n"
                + "ON relcomprapedido.id_fleteP = fleteenviado.id_fleteE "
                + "group by fleteEnviado.id_fleteE ;";
        }
        String datos[] = new String[7];
        Statement st = null;
        ResultSet rs = null;
        int i = 0;
        try {
            st = cn.createStatement();
            rs = st.executeQuery(consul);

            while (rs.next()) {
                /*                           for (int x=1;x<= rs.getMetaData().getColumnCount();x++) {
                     //System.out.print(x+" -> "+rs.getString(x));
                     contentL.add(rs.getString(x));
                   }*/
                datos[0] = rs.getString(1);
                if (controlInserts.validaRelCompPed(rs.getString(1), "id_fleteP")) {
                    coloreF.add(i);
                }

                datos[1] = rs.getString(2);

                datos[2] = rs.getString(3);
                datos[3] = rs.getString(4);
                datos[4] = rs.getString(5);

                datos[5] = rs.getString(6);
                datos[6] = rs.getString(7);
                modelo.addRow(datos);
                i++;
            }
            jTabFletesDia1.setModel(modelo);
        } catch (SQLException ex) {
            Logger.getLogger(interno1.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
//                    System.out.println( "cierra conexion a la base de datos" );    
            try {
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (cn != null) {
                    cn.close();
                }
//                        if(cn !=null) cn.close();
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
            }
        }

        if (i > 0) {

            ColorCelda cF = new ColorCelda();
            cF.arrIntRowsIluminados = controlInserts.fnToArray(coloreF);
            jTabFletesDia1.setDefaultRenderer(Object.class, cF);
            coloreF.clear();
        } else {

            jLabFlet.setText("Sin fletes del día");
        }
    }//termina mostrartablaFletesAsign

    //*** CODIGO PARA CARGAR LOS PAGOS DEL DIA
    private void llenaTabPayDayPedidos(int band,String param) {
        //Llena matriz de pagos pedido clis
        if(band == 0){
            String[][] matFlet = controlInserts.regresaPaysFech("pagopedidocli", "",param);
            jTabPayPeds.setModel(new TModel(matFlet, cabPaysDay));
            //Llena matriz de pago de prestamos 
            String[][] mat0 = controlInserts.regresaPaysFech("pagocreditprooved", "",param);
            jTabPayPrestamosProv.setModel(new TModel(mat0, cabPaysDay));
            //Llena matriz de pago ventas piso
            String[][] mat = controlInserts.regresaPaysFech("pagoventapiso", "",param);
            jTabVentPisoPays.setModel(new TModel(mat, cabPaysDay));

            //Llena matriz de pagos a fleteros **SALIDAS
            String[][] mat1 = controlInserts.regresaPaysFech("pagoflete", "",param);
            jTabPaysFletesDia.setModel(new TModel(mat1, cabPaysDay));
            //pagos de compras a proveedor
            String[][] mat2 = controlInserts.regresaPaysFech("pagarcompraprovee", "",param);
            jTabPaysCompraProovedor.setModel(new TModel(mat2, cabPaysDay));
            //Matriz de gastos del dia
            String[][] mat3 = controlInserts.regresaPaysFech("gastos_caja", "",param);
            jTabGastosDias.setModel(new TModel(mat3, cabSalidDay));
        }
        if(band == 1){
            String[][] matFlet = controlInserts.regresaPaysFech("pagopedidocli",param, "");
            jTabPayPeds.setModel(new TModel(matFlet, cabPaysDay));
            //Llena matriz de pago de prestamos 
            String[][] mat0 = controlInserts.regresaPaysFech("pagocreditprooved",param, "");
            jTabPayPrestamosProv.setModel(new TModel(mat0, cabPaysDay));
            //Llena matriz de pago ventas piso
            String[][] mat = controlInserts.regresaPaysFech("pagoventapiso",param, "");
            jTabVentPisoPays.setModel(new TModel(mat, cabPaysDay));

            //Llena matriz de pagos a fleteros **SALIDAS
            String[][] mat1 = controlInserts.regresaPaysFech("pagoflete",param, "");
            jTabPaysFletesDia.setModel(new TModel(mat1, cabPaysDay));
            //pagos de compras a proveedor
            String[][] mat2 = controlInserts.regresaPaysFech("pagarcompraprovee",param, "");
            jTabPaysCompraProovedor.setModel(new TModel(mat2, cabPaysDay));
            //Matriz de gastos del dia
            String[][] mat3 = controlInserts.regresaPaysFech("gastos_caja",param,"");
            jTabGastosDias.setModel(new TModel(mat3, cabSalidDay));            
        }
        
        jTabGastosDias.getColumnModel().getColumn(0).setMaxWidth(75);
        jTabGastosDias.getColumnModel().getColumn(0).setMinWidth(50);
        jTabGastosDias.getColumnModel().getColumn(0).setPreferredWidth(150);
    }
    
    private void rellenaInformDay(){
        String pedidoC = "",prestaProv="";
        jLabCountPC.setText(Integer.toString(jTabPayPeds.getRowCount()));
        jLabCountPP.setText(Integer.toString(jTabPayPrestamosProv.getRowCount()));
        jLabCountVP.setText(Integer.toString(jTabVentPisoPays.getRowCount()));
        jLabcountPF.setText(Integer.toString(jTabPaysFletesDia.getRowCount()));
        jLabCountCP.setText(Integer.toString(jTabPaysCompraProovedor.getRowCount()));
        jLabCountGast.setText(Integer.toString(jTabGastosDias.getRowCount()));
        //tablapago pedido cliente
        pedidoC = jTabPayPeds.getValueAt(0, 0).toString();
       if(pedidoC.equals("NO DATA")){
            jLabTotPC.setText("0.0");
        }else{
            jLabTotPC.setText(totalon(jTabPayPeds, 4));            
        }
              //tablapago prestamo proveedor
        pedidoC = jTabPayPrestamosProv.getValueAt(0, 0).toString();
       if(pedidoC.equals("NO DATA")){
            jLabTotPP.setText("0.0");
        }else{
            jLabTotPP.setText(totalon(jTabPayPrestamosProv, 4));            
        }
              //tablapago ventas de piso
        pedidoC = jTabVentPisoPays.getValueAt(0, 0).toString();
       if(pedidoC.equals("NO DATA")){
            jLabTotVP.setText("0.0");
        }else{
            jLabTotVP.setText(totalon(jTabVentPisoPays, 3));            
        }
              //tablapago pago fletes
        pedidoC = jTabPaysFletesDia.getValueAt(0, 0).toString();
       if(pedidoC.equals("NO DATA")){
            jLabTotPF.setText("0.0");
        }else{
            jLabTotPF.setText(totalon(jTabPaysFletesDia, 4));            
        }
                     //tablapago compra a proveedor
        pedidoC = jTabPaysCompraProovedor.getValueAt(0, 0).toString();
       if(pedidoC.equals("NO DATA")){
            jLabTotCP.setText("0.0");
        }else{
            jLabTotCP.setText(totalon(jTabPaysCompraProovedor, 4));            
        }
                     //tablapago gastos del dia
        pedidoC = jTabGastosDias.getValueAt(0, 0).toString();
       if(pedidoC.equals("NO DATA")){
            jLabTotGast.setText("0.0");
        }else{
            jLabTotGast.setText(totalon(jTabGastosDias, 2));            
        }
    }//@end rellenaInformDay

    private void calcDiaPagos(){
        //suma de entradas del dia
        BigDecimal uno = new BigDecimal(jLabTotPC.getText());
        BigDecimal dos = new BigDecimal(jLabTotPP.getText());
        BigDecimal tres = new BigDecimal(jLabTotVP.getText());
        jLabel164.setText(fn.getSum(tres, fn.getSum(uno, dos)).toString());
        
        //suma de salidas del dia
        BigDecimal one = new BigDecimal(jLabTotPF.getText());
        BigDecimal two = new BigDecimal(jLabTotCP.getText());
        BigDecimal three = new BigDecimal(jLabTotGast.getText());
        jLabel166.setText(fn.getSum(three, fn.getSum(one, two)).toString());
        
        String tot1 = fn.getSum(new BigDecimal(jLabel162.getText()), new BigDecimal(jLabel164.getText())).toString();
        jLabel168.setText(fn.getDifference(new BigDecimal(tot1),new BigDecimal(jLabel166.getText())).toString());
    }
    
    //****CODIGO PARA USO EN GENERAL
    private Date cargafecha() {
        Date fechaAct = new Date();
        return fechaAct;
    }

    public boolean vaciosAltaUsers() {
        boolean vacios = false;
        String nomb = jTextNombre.getText(),
                apes = jTextApellidos.getText(),
                local = jTextLocalidad.getText(),
                tel = jTexTelefono.getText(),
                quin = txtQuintoAltas.getText();
        if (nomb.isEmpty() && apes.isEmpty() && local.isEmpty()) {
            vacios = true;
        }
        return vacios;
    }

//****LIMPIAR CAMPOS
    private void limpiaPrestamoProv() {
        txtCantPres.setText("");
        txtPrecProov.setText("");
        txtImportPres.setText("");
        textANotaPrestProv.setText("");
    }

    private void limpiacompraProved() {
        txtCantidadCompra.setText("");
        txtPrecCompraProv.setText("");
        txtImportComp.setText("");
        txtCamSubastaCompra.setText("");
        txtNotaCompra.setText("");
    }

    private void limpiaVentaPiso() {
        txtCantVentaPiso.setText("");
        txtPrecProdVentaP.setText("");
        txtImportVentaP.setText("");
        txtNotaVentP.setText("");
    }

    private void limpiaFlete() {
        txtFolioFlete.setText("");
        txtChoferFlete.setText("");
        txtUnidFlete.setText("");
        txtCostoFlete.setText("");
        txtNotaFlete.setText("");
        jRPendFlete.setSelected(true);
        jLabLetreroFletes.setVisible(true);
        txtCargaFlet.setText("");
    }

    void limpiaAsign() {
        txtCompraAsign.setText("");
        txtidPedidoAsign.setText("");
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 2; j++) {
                jTabDetailAsignTotales.setValueAt("", i, j);
                jTabDetailAsignTotales1.setValueAt("", i, j);
            }
        }
    }

    public void cargaUser(String[] param) {
            jLabIdView.setText(param[0]);//id_usuario
            jTextField17.setText(param[1]);//nick usuario
            jLabTurno.setText(param[3]);//turno
            inTurno = controlInserts.getTurnoData(Integer.parseInt(param[3]));
            jLabel162.setText(inTurno[2]);
            
            if(param[1].equals("admin")){
                jLabel100.setVisible(true);
                jDFechPays.setVisible(true);
                jCheckBox2.setVisible(true);
            }else{
                jLabel100.setVisible(false);
                jDFechPays.setVisible(false);
                jCheckBox2.setVisible(false);                
            }
    }

    public void limpiaDetailMayorista() {
        dtmAux = (DefaultTableModel) jTabDetallecompraAll.getModel();//obtenemos modelo de tablaVista Compra a Mayorista
        int row = dtmAux.getRowCount();
        for (int i = 0; i < row; i++) {
            dtmAux.removeRow(0);
        }
    }
    
    public void limpDetPedidoDia(){
        DefaultTableModel dtme = (DefaultTableModel) jTabVistaPedidosDetDia.getModel();//jTabVistaPedidosDetDia
        int filas = dtme.getRowCount();       
       for (int i = 0; i < filas; i++) {
            dtme.removeRow(0);
        }
    }

    /*Detalle aignacion de compra proveedor mayorista*/
    void addCheckBox(int column, JTable table) {
        TableColumn tc = table.getColumnModel().getColumn(column);
        tc.setCellEditor(table.getDefaultEditor(Boolean.class));
        tc.setCellRenderer(table.getDefaultRenderer(Boolean.class));
    }

    boolean isSelectedTab(int row, int column, JTable tab) {
        return tab.getValueAt(row, column) != null;
    }

    //Metodo para analizar si ya ha sido asignado alguna compra a un pedido y mostrar cantidades asignadas
    private void rellenaAsignaTabla() {
        int rowC = jTabAsigaDinamicoCompra.getRowCount(),
                colC = jTabAsigaDinamicoCompra.getColumnCount(),
                sumaProd = -1;
        String var = "", sumDetcompra = "", idProdBusq = "", cantProdt = "";
        if (rowC > 0) {
            for (int i = 0; i < rowC; i++) {
                for (int j = 0; j < colC; j++) {
                    if (j == 1) {
                        var = jTabAsigaDinamicoCompra.getValueAt(i, j).toString();
                        //System.out.println("Buscar en tab -> "+var);
                        if (controlInserts.validaCompAsignadas(var, "", "idCli+fech")) {
                            idProdBusq = jTabAsigaDinamicoCompra.getValueAt(i, j + 1).toString(); //pos j=2
                            cantProdt = jTabAsigaDinamicoCompra.getValueAt(i, j + 3).toString(); //pos j=4
                            sumDetcompra = controlInserts.calcAsignAPed(var, idProdBusq, "id_detailComp");
                            jTabAsigaDinamicoCompra.setValueAt(sumDetcompra, i, 9);//j=9 ->Asignados
                            sumaProd = Integer.parseInt(cantProdt) - Integer.parseInt(sumDetcompra);
                            jTabAsigaDinamicoCompra.setValueAt(sumaProd, i, 10);//j=10 ->Disponibles
                        } else {
                            jTabAsigaDinamicoCompra.setValueAt(0, i, 9);//j=9 ->Asignados
                            jTabAsigaDinamicoCompra.setValueAt(jTabAsigaDinamicoCompra.getValueAt(i, j + 3).toString(), i, 10);//j=10 -> Disponibles
                        }
                    }//if i =1
                }//for j
            }//for i
        } else {
            JOptionPane.showMessageDialog(null, "No tiene filas que asignar");
        }
    }

//Muestra detalle de pedidos del dia
    //CARGA PEDIDOS DEL DIA VISTA DE DETALLE
    private void cargaPedidosDiaDet(String fech) {
        String[][] arre = controlInserts.consultPedidoAsign("",fech,"","");//regresa idPedido,idCliente
        if (arre.length > 0) {

            dtmPedidosDetDia = (DefaultTableModel) jTabVistaPedidosDetDia.getModel();
            int filas = dtmPedidosDetDia.getRowCount(), filasPrec = dtmPedidosDetDia.getRowCount();
            if (filas > 0) {
                for (int i = 0; filas > i; i++) {
                    dtmPedidosDetDia.removeRow(0);
                }
            }
            dtmPedidosDetDia.setRowCount(arre.length);//CREAMOS EL NUMERO DE FILAS SEGUN LA MATRIZ DEVUELTA
            jTabVistaPedidosDetDia.setModel(dtmPedidosDetDia);//agrego el modelo creado con el numero de filas devuelto
            for (int j = 0; j < arre.length; j++) {
                for (int k = 0; k < arre[0].length; k++) {
                    if (k == 0) {
                        consultDetailPedidosDetdia(Integer.parseInt(arre[j][k]), j);//envia matriz con id_compra,id_proveedor,fila
                    }
                }
            }
            ColorCelda c1 = new ColorCelda();
            c1.arrIntRowsIluminados = controlInserts.fnToArray(coloreB);
            jTabVistaPedidosDetDia.setDefaultRenderer(Object.class, c1);
            ListIterator<Integer> itr = coloreB.listIterator();

            coloreB.clear();//limpia ArrayList de filas que cumplaen condicion
        } else {
            jLabPed.setText("No hay pedidos del día");
        }
    }//Fin cargaPedidosDia

    public void consultDetailPedidosDetdia(int opc, int fila) {
        Connection cn = con2.conexion();
        int cantColumnas = 0, cantFilas = 0, temporal = 0, bandera = 0;
        String sql = "", sql2 = "";
        sql = "SELECT * FROM detailpedidio WHERE id_pedidioD = '" + opc + "'";
        sql2 = "SELECT\n"
                + "pedidocliente.id_pedido,clientepedidos.nombre,SUM(detailpedidio.cantidadCajas)\n"
                + "FROM\n"
                + "clientepedidos\n"
                + "INNER JOIN\n"
                + "pedidocliente\n"
                + "ON\n"
                + "pedidocliente.id_clienteP = clientepedidos.id_cliente AND pedidocliente.id_pedido = '" + opc + "'\n"
                + "INNER JOIN\n"
                + "detailpedidio\n"
                + "ON\n"
                + "detailpedidio.id_PedidioD = pedidocliente.id_pedido;";
        Statement st = null;
        ResultSet rs = null;
        try {
            st = cn.createStatement();
            rs = st.executeQuery(sql);
            // System.out.print("Filas: "+cantFilas+"\tColumnas: "+cantColumnas+"\n");
            while (rs.next()) {//es necesario el for para llenar dinamicamente la lista, ya que varia el numero de columnas de las tablas
                for (int x = 1; x <= rs.getMetaData().getColumnCount(); x++) {
                    if (x == 3) {//valor,fila,columna
                        jTabVistaPedidosDetDia.setValueAt(rs.getInt(x + 1), fila, rs.getInt(x) + 1);//se le suma 1 por las columnas id,nombre de la jTable
                    }//System.out.print("["+x+"]"+" -> "+rs.getString(x));                   
                }//for
            }//while

            st = null;
            rs = null;
            st = cn.createStatement();
            rs = st.executeQuery(sql2);
            while (rs.next()) {
                jTabVistaPedidosDetDia.setValueAt(rs.getInt(1), fila, 0);
                if (controlInserts.validaRelCompPed(Integer.toString(rs.getInt(1)), "id_pedidoCli")) {
                    coloreB.add(fila);
                }

                jTabVistaPedidosDetDia.setValueAt(rs.getString(2), fila, 1);
                jTabVistaPedidosDetDia.setValueAt(rs.getString(3), fila, 9);
            }
        } catch (SQLException ex) {
            Logger.getLogger(interno1.class.getName()).log(Level.SEVERE, null, ex);
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
    protected void cargaTotPedidoDay(JTable tabon, String fech) {
        Connection cn = con2.conexion();
        int cantColumnas = 0, cantFilas = 0, temporal = 0, bandera = 0;
        String sql = "", sql2 = "";
        sql = "SELECT productocal.codigo,\n"
                + "SUM(detailpedidio.cantidadCajas) AS sumaType\n"
                + "FROM \n"
                + "detailpedidio\n"
                + "INNER JOIN \n"
                + "pedidocliente\n"
                + "ON\n"
                + "detailpedidio.id_PedidioD = pedidocliente.id_pedido \n"
                + "AND pedidocliente.fechaPedidio = '" + fech + "'\n"
                + "INNER JOIN\n"
                + "productocal\n"
                + "ON\n"
                + "productocal.codigo = detailpedidio.codigoProdP\n"
                + "GROUP BY productocal.codigo;";
        sql2 = "SELECT\n"
                +"SUM(detailpedidio.cantidadCajas) AS numCaja\n"
                + "FROM \n"
                + "detailpedidio\n"
                + "INNER JOIN \n"
                + "pedidocliente\n"
                + "ON\n"
                + "detailpedidio.id_PedidioD = pedidocliente.id_pedido \n"
                + "AND pedidocliente.fechaPedidio = '" + fech + "'\n"
                + "INNER JOIN\n"
                + "productocal\n"
                + "ON\n"
                + "productocal.codigo = detailpedidio.codigoProdP;";
        Statement st = null;
        ResultSet rs = null;
        try {
            st = cn.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {//es necesario el for para llenar dinamicamente la lista, ya que varia el numero de columnas de las tablas
                for (int x = 1; x <= rs.getMetaData().getColumnCount(); x++) {
                    if (x == 1) {
                        tabon.setValueAt(rs.getInt(x + 1), 0, rs.getInt(x) - 1);//se le suma 1 por las columnas id,nombre de la jTable
                    }
                }//for
            }//while
            st = null;
            rs = null;
            st = cn.createStatement();
            rs = st.executeQuery(sql2);
            while (rs.next()) {
                tabon.setValueAt(rs.getString(1), 0, 7);
            }
        } catch (SQLException ex) {
            Logger.getLogger(interno1.class.getName()).log(Level.SEVERE, null, ex);
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
    }//@end cargaTotPedidoDay

    // GASTOS DEL DIA
    //metodo para llenar combo de areas
        private void llenacombogetcuentaGasto() {
            Connection cn = con2.conexion();
            contenRubGastos.clear();
            jCombBTypeRubros.removeAllItems();
            String consul = "SELECT id, concepto from rubroscaja ORDER BY concepto";
            Statement st = null;
            ResultSet rs = null;
            try {
                st = cn.createStatement();
                rs = st.executeQuery(consul);
                while (rs.next()) {
                    contenRubGastos.add(rs.getString(1));
                    jCombBTypeRubros.addItem(rs.getString(2));
                }
            } catch (SQLException ex) {
                Logger.getLogger(interno1.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    if (st != null) {
                        st.close();
                    }
                    if (cn != null) {
                        cn.close();
                    }
                } catch (SQLException ex) {
                    System.err.println(ex.getMessage());
                }
            }
    }//Llena llenacombogetcuentaGasto 
    
        
        
    private void siSubastaProv() {
        String var = jCElijaProovedor.getSelectedItem().toString();
        String getIdPro = idProoved.get(jCElijaProovedor.getSelectedIndex());
        if (var.equals("SUBASTA")) {
            jPanClientOption.setVisible(false);
            jPanSubastaOption.setVisible(true);
//            txtCamSubastaCompra.setFocusable(true);
            txtCamSubastaCompra.requestFocus();
        } else if (controlInserts.validaPrestamoProv(getIdPro)) {//validamos si existe en prestamos de mercancia sin pagos
            jPanClientOption.setVisible(true);
            jPanSubastaOption.setVisible(false);
            jCombProductProv.requestFocus(true);
        } else {
            jPanClientOption.setVisible(false);
            jPanSubastaOption.setVisible(false);
            jCombProductProv.requestFocus(true);
        }
    }

    private String totalon(JTable tablon,int colsum){
        String sumon ="",dat;
        double t = 0, p = 0;
        for (int i = 0; i < tablon.getRowCount(); i++) {
            p = Double.parseDouble(tablon.getValueAt(i,colsum).toString());
            t += p;
        }
        sumon = Double.toString(t);
        return sumon;
    }
    
        private String totalonNull(JTable tablon,int colsum){
            String sumon ="",dat;
            double t = 0, p = 0;
            int res =0;
            Object var = null;
            for (int i = 0; i < tablon.getRowCount(); i++) {
                var = tablon.getValueAt(i,colsum);
                if (var != null && !var.toString().isEmpty()) {
                    p = Double.parseDouble(var.toString());
                }else{
                    p = 0;
                }
                t += p;
            }
            res = (int) t;
            sumon = Integer.toString(res);
            return sumon;
    }
    
          public void limpiaguardGasto(){
          jCombBTypeRubros.setSelectedIndex(0);
          txtConcept.setText("");
          txtsolict.setText("");	
          txtObservs.setText("");
          txtMontoGasto.setText("");
      }
          
          private void cancelPago(JTable tab, String param){
                int var = tab.getSelectedRow();
                if(var >  -1){
                    jDialCancelaciones.setLocationRelativeTo(null);
                    jDialCancelaciones.setVisible(true);
                    jDialCancelaciones.setEnabled(true);
                    jDialCancelaciones.setTitle(param);
                    txtCancelTick.setText("");
                    jLabIdCancel.setText(tab.getValueAt(var, 0).toString());
//parametro de # de pago
                    jLabParam2.setText(tab.getValueAt(var, 1).toString());
                }else{
                    JOptionPane.showMessageDialog(null,"Debe elegir una fila de la tabla");
                }
          }
 
/****CODIGO PARA Reportes Turnos*/
    private void llenacombCajeros(){
            Connection cn = con2.conexion();
            idCajeros.clear();
            jCmBoxIdCancel.removeAllItems();
            jCmBoxIdCancel.addItem("Todos"); 
            
            String consul = "SELECT id_user, nombreUser from usersdcr  ORDER BY nombreUser";
            Statement st = null;
            ResultSet rs = null;
            try {
                st = cn.createStatement();
                rs = st.executeQuery(consul);
                while (rs.next()) {
                    idCajeros.add(rs.getString(1));
                    jCmBoxIdCancel.addItem(rs.getString(2));
                }
            } catch (SQLException ex) {
                Logger.getLogger(interno1.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    if (st != null) {
                        st.close();
                    }
                    if (cn != null) {
                        cn.close();
                    }
                } catch (SQLException ex) {
                    System.err.println(ex.getMessage());
                }
            }
    }
    
              //funcion para busqueda automatica de otros conceptos
        void getIntervalTurns2(int opc, String idUsers,String fech1,String fech2){
                Connection cn = con2.conexion();
                DefaultTableModel modelo = new DefaultTableModel()
                { 
                    @Override
                    public boolean isCellEditable (int fila, int columna) {
                        return false;
                    }
                };
                String consul="";
                if(opc == 0){
                    consul ="SELECT  turnos.id,usersdcr.nombreUser,turnos.finicial,turnos.ffinal\n" +
                            "FROM turnos\n" +
                            "INNER JOIN usersdcr\n" +
                            "ON turnos.idusuario = usersdcr.id_user AND turnos.finicial BETWEEN (SELECT semanas.finicial FROM semanas WHERE CURDATE() BETWEEN date(finicial) AND date(ffinal) )\n" +
                            "AND (SELECT semanas.ffinal FROM semanas WHERE CURDATE() BETWEEN date(finicial) AND date(ffinal) )\n" +
                            "ORDER BY turnos.id DESC;";
                    }

                if(opc == 1){
                    if(idUsers.isEmpty()){
                              consul = "SELECT  turnos.id,usersdcr.nombreUser,turnos.finicial,turnos.ffinal\n" +
                              "FROM turnos\n" +
                              "INNER JOIN usersdcr\n" +
                              "ON turnos.idusuario = usersdcr.id_user AND (date(turnos.finicial) >= '"+fech1+"' AND date(turnos.finicial) <= '"+fech2+"') \n" +
                              "ORDER BY turnos.id DESC;";
                    }else{
                       consul = "SELECT  turnos.id,usersdcr.nombreUser,turnos.finicial,turnos.ffinal\n" +
                              "FROM turnos\n" +
                              "INNER JOIN usersdcr\n" +
                              "ON turnos.idusuario = usersdcr.id_user AND turnos.idusuario = '"+idUsers+"' AND (date(turnos.finicial) >= '"+fech1+"' AND date(turnos.finicial) <= '"+fech2+"') \n" +
                              "ORDER BY turnos.id DESC;";
                    }   
                }
                 //consul = "SELECT id, nombre from ambulantes WHERE id LIKE '"+var+"%'  OR nombre LIKE '"+var+"%' ORDER BY id";
                        modelo.addColumn("#Turno");       
                        modelo.addColumn("Cajero");
                        modelo.addColumn("Inicio");
                        modelo.addColumn("Finalizado");

                jTable4.setModel(modelo);
                TableColumnModel columnModel = jTable4.getColumnModel();
              jTable4.getColumnModel().getColumn(0).setPreferredWidth(80);
                jTable4.getColumnModel().getColumn(0).setMaxWidth(150);
                jTable4.getColumnModel().getColumn(0).setMinWidth(50);

                 jTable4.getColumnModel().getColumn(1).setPreferredWidth(180);
                jTable4.getColumnModel().getColumn(1).setMaxWidth(250);
                jTable4.getColumnModel().getColumn(1).setMinWidth(180);

                String datos[] =  new String[4];//tenia 4
                Statement st = null;
                ResultSet rs = null;
                try {
                    st = cn.createStatement();
                    rs = st.executeQuery(consul);

                    while(rs.next()){
                        datos[0] =rs.getString(1);
                        datos[1] = rs.getString(2);
                        datos[2] = rs.getString(3);
                        datos[3] = rs.getString(4);
                        modelo.addRow(datos);
                    }
                    jTable4.setModel(modelo);
                } catch (SQLException ex) {
                    Logger.getLogger(interno1.class.getName()).log(Level.SEVERE, null, ex);
                }finally{
                     try {
                            if(rs != null) rs.close();              
                            if(st != null) st.close();                
                            if(cn !=null) cn.close();
                     } catch (SQLException ex) {
                     }
                 }
    }//@endgetProductsOthers
          
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem AgregarMayoreo;
    private javax.swing.JPanel ReportDay;
    private javax.swing.ButtonGroup btnGAltaClientes;
    private javax.swing.ButtonGroup btnGFletesCrea;
    private javax.swing.ButtonGroup btnGFletesPane;
    private javax.swing.ButtonGroup btnGVentasPiso;
    private javax.swing.ButtonGroup butGrpFleterFilters;
    private javax.swing.ButtonGroup butnGPedidos;
    private javax.swing.ButtonGroup butnGProved;
    private javax.swing.ButtonGroup buttonGProveedores;
    private javax.swing.JMenuItem delDetailcompra;
    private javax.swing.JButton jButAltasActualiza;
    private javax.swing.JButton jButAltasActualiza1;
    private javax.swing.JButton jButAltasElimina;
    private javax.swing.JButton jButAltasElimina1;
    private javax.swing.JButton jButFleteGuardar;
    private javax.swing.JButton jButFleteGuardar1;
    private javax.swing.JButton jButGuardaPedidodia;
    private javax.swing.JButton jButaltasGuardar;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton19;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton20;
    private javax.swing.JButton jButton21;
    private javax.swing.JButton jButton22;
    private javax.swing.JButton jButton23;
    private javax.swing.JButton jButton24;
    private javax.swing.JButton jButton25;
    private javax.swing.JButton jButton26;
    private javax.swing.JButton jButton27;
    private javax.swing.JButton jButton28;
    private javax.swing.JButton jButton29;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton30;
    private javax.swing.JButton jButton31;
    private javax.swing.JButton jButton32;
    private javax.swing.JButton jButton33;
    private javax.swing.JButton jButton34;
    private javax.swing.JButton jButton35;
    private javax.swing.JButton jButton36;
    private javax.swing.JButton jButton37;
    private javax.swing.JButton jButton38;
    private javax.swing.JButton jButton39;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton40;
    private javax.swing.JButton jButton42;
    private javax.swing.JButton jButton43;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox<String> jCBAltasFletes;
    private javax.swing.JComboBox<String> jCBTypeProv;
    private javax.swing.JComboBox<String> jCCliVentaPiso;
    private javax.swing.JComboBox<String> jCElijaProovedor;
    private com.toedter.calendar.JCalendar jCalendar1;
    private javax.swing.JComboBox<String> jCfleteroOpc;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JCheckBox jCheckbpAGADO;
    private javax.swing.JComboBox<String> jCmBoxIdCancel;
    private javax.swing.JComboBox<String> jComBPrestamosProv;
    private javax.swing.JComboBox<String> jComBProveedor;
    private javax.swing.JComboBox<String> jComBusCompra;
    private javax.swing.JComboBox<String> jComBusPrestamo;
    private javax.swing.JComboBox<String> jComPedBusqCli;
    private javax.swing.JComboBox<String> jCombBProvBusqCompra;
    private javax.swing.JComboBox<String> jCombBProvBusqPrest;
    private javax.swing.JComboBox<String> jCombBTypeRubros;
    private javax.swing.JComboBox<String> jCombCliVentaP;
    private javax.swing.JComboBox<String> jCombOpcBusqFletes;
    private javax.swing.JComboBox<String> jCombOpcBusqPedido;
    private javax.swing.JComboBox<String> jCombOpcBusqVenta;
    private javax.swing.JComboBox<String> jCombPedidoClient;
    private javax.swing.JComboBox<String> jCombProdPedidos;
    private javax.swing.JComboBox<String> jCombProdVentaP;
    private javax.swing.JComboBox<String> jCombProductProv;
    private javax.swing.JComboBox<String> jComboAltas;
    private javax.swing.JComboBox<String> jComboBox1;
    private com.toedter.calendar.JDateChooser jDCAsignacionDia;
    private com.toedter.calendar.JDateChooser jDCFol1;
    private com.toedter.calendar.JDateChooser jDCFol2;
    private com.toedter.calendar.JDateChooser jDFVentaPiso;
    private com.toedter.calendar.JDateChooser jDFechCreaFlete;
    private com.toedter.calendar.JDateChooser jDFechPays;
    private com.toedter.calendar.JDateChooser jDaTFechComp1;
    private com.toedter.calendar.JDateChooser jDaTFechCompraProv2;
    private com.toedter.calendar.JDateChooser jDaTFechPrest1;
    private com.toedter.calendar.JDateChooser jDaTFechPrest2;
    private com.toedter.calendar.JDateChooser jDatFechaPrest;
    private com.toedter.calendar.JDateChooser jDate2BusqCli;
    private com.toedter.calendar.JDateChooser jDateCHPedido;
    private com.toedter.calendar.JDateChooser jDateChFechaAlta;
    private com.toedter.calendar.JDateChooser jDateChoB1Cli;
    private com.toedter.calendar.JDateChooser jDateChoBVent1;
    private com.toedter.calendar.JDateChooser jDateFechCompraProv;
    private com.toedter.calendar.JDateChooser jDatebusqVenta2;
    private javax.swing.JDialog jDiaViewSobrinas;
    private javax.swing.JDialog jDialAltaGastos;
    private javax.swing.JDialog jDialCalendarMantenim;
    private javax.swing.JDialog jDialCancelaciones;
    private javax.swing.JDialog jDialDetailCompraProov;
    private javax.swing.JDialog jDialDetailFlete1;
    private javax.swing.JFrame jFramElijeAsignCompras;
    private javax.swing.JLabel jLCountHistorP;
    private javax.swing.JLabel jLaComp;
    private javax.swing.JLabel jLabAdeudaProoved;
    private javax.swing.JLabel jLabBNumerador;
    private javax.swing.JLabel jLabContaFletes;
    private javax.swing.JLabel jLabContad;
    private javax.swing.JLabel jLabCountCP;
    private javax.swing.JLabel jLabCountGast;
    private javax.swing.JLabel jLabCountPC;
    private javax.swing.JLabel jLabCountPP;
    private javax.swing.JLabel jLabCountVP;
    private javax.swing.JLabel jLabFaltant;
    private javax.swing.JLabel jLabFlet;
    private javax.swing.JLabel jLabId;
    private javax.swing.JLabel jLabIdCancel;
    private javax.swing.JLabel jLabIdView;
    private javax.swing.JLabel jLabLetreroFletes;
    private javax.swing.JLabel jLabNomProov;
    private javax.swing.JLabel jLabNumAsogna;
    private javax.swing.JLabel jLabNumcompra;
    private javax.swing.JLabel jLabNumcompra1;
    private javax.swing.JLabel jLabNumcompra2;
    private javax.swing.JLabel jLabParam2;
    private javax.swing.JLabel jLabPed;
    private javax.swing.JLabel jLabPed1;
    private javax.swing.JLabel jLabPed3;
    private javax.swing.JLabel jLabPed4;
    private javax.swing.JLabel jLabPed5;
    private javax.swing.JLabel jLabPed6;
    private javax.swing.JLabel jLabPed7;
    private javax.swing.JLabel jLabRcontFilsob;
    private javax.swing.JLabel jLabTotCP;
    private javax.swing.JLabel jLabTotGast;
    private javax.swing.JLabel jLabTotPC;
    private javax.swing.JLabel jLabTotPF;
    private javax.swing.JLabel jLabTotPP;
    private javax.swing.JLabel jLabTotVP;
    private javax.swing.JLabel jLabTurnCancel;
    private javax.swing.JLabel jLabTurno;
    private javax.swing.JLabel jLabcountPF;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel100;
    private javax.swing.JLabel jLabel101;
    private javax.swing.JLabel jLabel102;
    private javax.swing.JLabel jLabel103;
    private javax.swing.JLabel jLabel104;
    private javax.swing.JLabel jLabel105;
    private javax.swing.JLabel jLabel106;
    private javax.swing.JLabel jLabel107;
    private javax.swing.JLabel jLabel108;
    private javax.swing.JLabel jLabel109;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel110;
    private javax.swing.JLabel jLabel111;
    private javax.swing.JLabel jLabel112;
    private javax.swing.JLabel jLabel113;
    private javax.swing.JLabel jLabel114;
    private javax.swing.JLabel jLabel115;
    private javax.swing.JLabel jLabel116;
    private javax.swing.JLabel jLabel117;
    private javax.swing.JLabel jLabel118;
    private javax.swing.JLabel jLabel119;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel120;
    private javax.swing.JLabel jLabel121;
    private javax.swing.JLabel jLabel122;
    private javax.swing.JLabel jLabel123;
    private javax.swing.JLabel jLabel124;
    private javax.swing.JLabel jLabel125;
    private javax.swing.JLabel jLabel126;
    private javax.swing.JLabel jLabel127;
    private javax.swing.JLabel jLabel128;
    private javax.swing.JLabel jLabel129;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel130;
    private javax.swing.JLabel jLabel131;
    private javax.swing.JLabel jLabel132;
    private javax.swing.JLabel jLabel133;
    private javax.swing.JLabel jLabel134;
    private javax.swing.JLabel jLabel135;
    private javax.swing.JLabel jLabel136;
    private javax.swing.JLabel jLabel137;
    private javax.swing.JLabel jLabel138;
    private javax.swing.JLabel jLabel139;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel140;
    private javax.swing.JLabel jLabel141;
    private javax.swing.JLabel jLabel142;
    private javax.swing.JLabel jLabel143;
    private javax.swing.JLabel jLabel144;
    private javax.swing.JLabel jLabel145;
    private javax.swing.JLabel jLabel146;
    private javax.swing.JLabel jLabel147;
    private javax.swing.JLabel jLabel148;
    private javax.swing.JLabel jLabel149;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel150;
    private javax.swing.JLabel jLabel151;
    private javax.swing.JLabel jLabel152;
    private javax.swing.JLabel jLabel153;
    private javax.swing.JLabel jLabel154;
    private javax.swing.JLabel jLabel155;
    private javax.swing.JLabel jLabel156;
    private javax.swing.JLabel jLabel157;
    private javax.swing.JLabel jLabel158;
    private javax.swing.JLabel jLabel159;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel160;
    private javax.swing.JLabel jLabel161;
    private javax.swing.JLabel jLabel162;
    private javax.swing.JLabel jLabel163;
    private javax.swing.JLabel jLabel164;
    private javax.swing.JLabel jLabel165;
    private javax.swing.JLabel jLabel166;
    private javax.swing.JLabel jLabel167;
    private javax.swing.JLabel jLabel168;
    private javax.swing.JLabel jLabel169;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel170;
    private javax.swing.JLabel jLabel171;
    private javax.swing.JLabel jLabel172;
    private javax.swing.JLabel jLabel173;
    private javax.swing.JLabel jLabel174;
    private javax.swing.JLabel jLabel175;
    private javax.swing.JLabel jLabel176;
    private javax.swing.JLabel jLabel177;
    private javax.swing.JLabel jLabel178;
    private javax.swing.JLabel jLabel179;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel73;
    private javax.swing.JLabel jLabel74;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JLabel jLabel76;
    private javax.swing.JLabel jLabel77;
    private javax.swing.JLabel jLabel78;
    private javax.swing.JLabel jLabel79;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel80;
    private javax.swing.JLabel jLabel81;
    private javax.swing.JLabel jLabel82;
    private javax.swing.JLabel jLabel83;
    private javax.swing.JLabel jLabel84;
    private javax.swing.JLabel jLabel85;
    private javax.swing.JLabel jLabel86;
    private javax.swing.JLabel jLabel87;
    private javax.swing.JLabel jLabel88;
    private javax.swing.JLabel jLabel89;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel90;
    private javax.swing.JLabel jLabel91;
    private javax.swing.JLabel jLabel92;
    private javax.swing.JLabel jLabel93;
    private javax.swing.JLabel jLabel94;
    private javax.swing.JLabel jLabel95;
    private javax.swing.JLabel jLabel96;
    private javax.swing.JLabel jLabel97;
    private javax.swing.JLabel jLabel98;
    private javax.swing.JLabel jLabel99;
    private javax.swing.JLabel jLabelRFCAlta;
    private javax.swing.JLabel jLabtotaldineroHistorPEd;
    private javax.swing.JLayeredPane jLayVentasPiso;
    private javax.swing.JLayeredPane jLayerFletes;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JLayeredPane jLayeredPane3;
    private javax.swing.JLayeredPane jLayeredPanePedidos;
    private javax.swing.JMenuItem jMIPPVer;
    private javax.swing.JMenuItem jMIPaysPresta;
    private javax.swing.JMenuItem jMI_ElimASIGNA;
    private javax.swing.JMenuItem jMItDetailVer;
    private javax.swing.JMenuItem jMItPayFlete;
    private javax.swing.JMenuItem jMPAYFILTRO;
    private javax.swing.JMenuItem jMenPago;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMitPayfilterFletes;
    private javax.swing.JMenuItem jMnDetailVentaP;
    private javax.swing.JMenuItem jMnPayVentaP;
    private javax.swing.JMenuItem jMnRealPay;
    private javax.swing.JPanel jPFletes;
    private javax.swing.JPopupMenu jPMACompras;
    private javax.swing.JPopupMenu jPMDetailFormaPedido;
    private javax.swing.JPopupMenu jPMPrestaProvPays;
    private javax.swing.JPopupMenu jPMneliminaDetailMayorista;
    private javax.swing.JPanel jPPedidosHist;
    private javax.swing.JPanel jPaNVentaPiso;
    private javax.swing.JPanel jPanAdminist;
    private javax.swing.JPanel jPanAdminist1;
    private javax.swing.JPanel jPanAltas;
    private javax.swing.JPanel jPanAsignac;
    private javax.swing.JPanel jPanBusqVentasPiso;
    private javax.swing.JPanel jPanBusquedaPrest;
    private javax.swing.JPanel jPanCabezera;
    private javax.swing.JPanel jPanClientOption;
    private javax.swing.JPanel jPanCompraProoved;
    private javax.swing.JPanel jPanConsulPed;
    private javax.swing.JPanel jPanCreaFletes;
    private javax.swing.JPanel jPanCreaPedido;
    private javax.swing.JPanel jPanHistorFletes;
    private javax.swing.JPanel jPanInternoBusquedaPrest;
    private javax.swing.JPanel jPanPagos;
    private javax.swing.JPanel jPanPrestamoProovedor;
    private javax.swing.JPanel jPanSubastaOption;
    private javax.swing.JPanel jPanVentasPiso;
    private javax.swing.JPanel jPanVistaAlta;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPopupMenu jPopCompraProveedor;
    private javax.swing.JPopupMenu jPopFILTROCOMPRAS;
    private javax.swing.JPopupMenu jPopMFiltrosBusqFletes;
    private javax.swing.JPopupMenu jPopMnDetailCompAsign;
    private javax.swing.JPopupMenu jPopPedidosDia;
    private javax.swing.JPopupMenu jPopVentaPisoBusq;
    private javax.swing.JPopupMenu jPopupMenActualizaCompra;
    private javax.swing.JPopupMenu jPopupPrestaProov;
    private javax.swing.JPopupMenu jPpMnDetFletes;
    private javax.swing.JPopupMenu jPpMnPagoFletes;
    private javax.swing.JRadioButton jRHistorFletes;
    private javax.swing.JRadioButton jRPagadopFlete;
    private javax.swing.JRadioButton jRPendFlete;
    private javax.swing.JRadioButton jRad1Activo;
    private javax.swing.JRadioButton jRadBCompraProve;
    private javax.swing.JRadioButton jRadBConsultaProv;
    private javax.swing.JRadioButton jRadBPrestamoProv;
    private javax.swing.JRadioButton jRadBusqVentaPiso;
    private javax.swing.JRadioButton jRadCreaVentaPiso;
    private javax.swing.JRadioButton jRadInactivo;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JRadioButton jRadioConsulPedido;
    private javax.swing.JRadioButton jRadioCreaPedido;
    private javax.swing.JRadioButton jRdCreaFletes;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane15;
    private javax.swing.JScrollPane jScrollPane16;
    private javax.swing.JScrollPane jScrollPane17;
    private javax.swing.JScrollPane jScrollPane18;
    private javax.swing.JScrollPane jScrollPane19;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane20;
    private javax.swing.JScrollPane jScrollPane21;
    private javax.swing.JScrollPane jScrollPane22;
    private javax.swing.JScrollPane jScrollPane23;
    private javax.swing.JScrollPane jScrollPane24;
    private javax.swing.JScrollPane jScrollPane25;
    private javax.swing.JScrollPane jScrollPane26;
    private javax.swing.JScrollPane jScrollPane27;
    private javax.swing.JScrollPane jScrollPane28;
    private javax.swing.JScrollPane jScrollPane29;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane30;
    private javax.swing.JScrollPane jScrollPane31;
    private javax.swing.JScrollPane jScrollPane32;
    private javax.swing.JScrollPane jScrollPane33;
    private javax.swing.JScrollPane jScrollPane34;
    private javax.swing.JScrollPane jScrollPane35;
    private javax.swing.JScrollPane jScrollPane36;
    private javax.swing.JScrollPane jScrollPane37;
    private javax.swing.JScrollPane jScrollPane38;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JSeparator jSeparator11;
    private javax.swing.JSeparator jSeparator12;
    private javax.swing.JSeparator jSeparator13;
    private javax.swing.JPopupMenu.Separator jSeparator14;
    private javax.swing.JSeparator jSeparator15;
    private javax.swing.JPopupMenu.Separator jSeparator16;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JPopupMenu.Separator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JTable jTDetailAsign;
    private javax.swing.JTable jTVistaVentaPisoDia;
    private javax.swing.JTable jTabAsigaDinamicoCompra;
    private javax.swing.JTable jTabBusqCompraProv1;
    private javax.swing.JTable jTabBusqPrestProv;
    private javax.swing.JTable jTabDescVentaP;
    private javax.swing.JTable jTabDetProvView;
    private javax.swing.JTable jTabDetailAsignTotales;
    private javax.swing.JTable jTabDetailAsignTotales1;
    private javax.swing.JTable jTabDetallecompraAll;
    private javax.swing.JTable jTabFletesDia;
    private javax.swing.JTable jTabFletesDia1;
    private javax.swing.JTable jTabGastosDias;
    private javax.swing.JTable jTabPayPeds;
    private javax.swing.JTable jTabPayPrestamosProv;
    private javax.swing.JTable jTabPaysCompraProovedor;
    private javax.swing.JTable jTabPaysFletesDia;
    private javax.swing.JTable jTabSobrinasDays;
    private javax.swing.JTable jTabSumTotalPedido;
    private javax.swing.JTable jTabSumTotales;
    private javax.swing.JTable jTabVentPisoPays;
    private javax.swing.JTable jTabVistaComprasDia;
    private javax.swing.JTable jTabVistaComprasDia3;
    private javax.swing.JTable jTabVistaPedidosDetDia;
    private javax.swing.JTable jTabVistaPedidosDia1;
    private javax.swing.JTable jTabVistaPresta;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTable jTabdetPrestamoProv;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTable jTable4;
    private javax.swing.JTable jTable5;
    private javax.swing.JTable jTableAltasCli;
    private javax.swing.JTable jTableCreaPedidos;
    private javax.swing.JTable jTablefiltrosBusq;
    private javax.swing.JTable jTablefiltrosBusqVent;
    private javax.swing.JTable jTablefiltrosBusqflete;
    private javax.swing.JTextField jTexTelefono;
    private javax.swing.JTextField jTextApellidos;
    private javax.swing.JTextField jTextField1;
    public javax.swing.JTextField jTextField17;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextLocalidad;
    private javax.swing.JTextField jTextNombre;
    private javax.swing.JTabbedPane paneAltas;
    private javax.swing.JPanel proveedorJP;
    private javax.swing.JTextArea textANotaPrestProv;
    private javax.swing.JTextField txtBusqAignCompra;
    private javax.swing.JTextField txtBusqAltas;
    private javax.swing.JTextField txtBusqFleteAsign;
    private javax.swing.JTextField txtCamSubastaCompra;
    private javax.swing.JTextField txtCancelTick;
    private javax.swing.JTextField txtCantCreaPedido;
    private javax.swing.JTextField txtCantPres;
    private javax.swing.JTextField txtCantVentaPiso;
    private javax.swing.JTextField txtCantidadCompra;
    private javax.swing.JTextField txtCargaFlet;
    private javax.swing.JTextField txtChoferFlete;
    private javax.swing.JTextField txtCompraAsign;
    private javax.swing.JTextField txtConcept;
    private javax.swing.JTextField txtCostoFlete;
    private javax.swing.JTextField txtFolioFlete;
    private javax.swing.JTextField txtIdParam;
    private javax.swing.JTextField txtImportComp;
    private javax.swing.JTextField txtImportPres;
    private javax.swing.JTextField txtImportVentaP;
    private javax.swing.JTextField txtMontoGasto;
    private javax.swing.JTextField txtNotaCompra;
    private javax.swing.JTextField txtNotaFlete;
    private javax.swing.JTextField txtNotaVentP;
    private javax.swing.JTextField txtNotePedidoCli;
    private javax.swing.JTextField txtObservs;
    private javax.swing.JTextField txtPrecCompraProv;
    private javax.swing.JTextField txtPrecProdVentaP;
    private javax.swing.JTextField txtPrecProov;
    private javax.swing.JTextField txtQuintoAltas;
    private javax.swing.JTextField txtTotalVentaPiso;
    private javax.swing.JTextField txtUnidFlete;
    private javax.swing.JTextField txtidPedidoAsign;
    private javax.swing.JTextField txtsolict;
    // End of variables declaration//GEN-END:variables
}
// URREA R470078