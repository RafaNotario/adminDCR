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
import java.util.Date;
import javax.swing.JLabel;
import javax.swing.JPanel;

import internos.tickets.print.Funciones;
import controllers.altadeclientes.controladorCFP;
import conexiones.db.ConexionDBOriginal;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Rectangle; 
import java.io.File;
import java.io.*;
import java.math.BigDecimal;

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

/**
 *
 * @author A. Rafael Notario
 */
public class interno1 extends javax.swing.JFrame {

    Funciones fn = new Funciones();
    controladorCFP controlInserts = new controladorCFP();
    ConexionDBOriginal con2 = new ConexionDBOriginal();

    //** variables globales     ->netflix oss microservices
    String atribAltaCli = "";//variable para asignar el nombre de la tabla llenar de la base de datos
    List<String> conten = new ArrayList<String>();//lista para guardar el id de cada cleinte en crea pedido y en ventas piso
    List<String> idProducts = new ArrayList<String>();//lista para id de produvtod
    List<String> idProductsVent = new ArrayList<String>();//lista para id de productos venta

    List<String> idProdPrest = new ArrayList<String>();//para productos prestamo
    List<String> idProoved = new ArrayList<String>();//para id_proveedores
    List<String> importes = new ArrayList<String>();//para importes de prestamo

    List<String> contenFletes = new ArrayList<String>();//para id_Fletero

    List<String> iDclisVentaPiso = new ArrayList<String>();//cleinte en crea  ventas piso

    ArrayList<Integer> coloreA = new ArrayList<Integer>();
    ArrayList<Integer> coloreB = new ArrayList<Integer>();
    ArrayList<Integer> coloreF = new ArrayList<Integer>();
    ArrayList<Integer> colorePD = new ArrayList<Integer>();

    DefaultTableModel dtm,//obtener modelo en tabla CreaPedido clientes
            dtmPrec,
            tabCompras,//tabla diamica de compras del dia
            dtmAux;//tabla de compras a proveedor real

    String[] cab = {"ID PEDIDO", "FECHA", "STATUS", "ID CLIENTE", "NOMBRE", "CANTIDAD", "COSTO", "NOTA"};
    String[] cabPrest = {"NO. CREDITO", "FECHA", "STATUS", "ID PROVEEDOR", "NOMBRE", "TOTAL", "NOTA"};
    String[] cabCompra = {"NO. COMPRA", "FECHA", "STATUS", "ID PROVEEDOR", "NOMBRE", "TOTAL", "NOTA"};
    String[] cabvENTAp = {"NO. VENTA", "FECHA", "STATUS", "ID CLIENTE", "NOMBRE", "TOTAL", "NOTA"};
    String[] cabFilterFlete = {"FOLIO", "FLETERO", "FECHA", "CHOFER", "UNIDAD", "COSTO", "STATUS", "NOTA"};
    String[] cabEdoPed = {"FOLIO", "Compras Asignadas", "Pedidos Destinados", "Total de carga"};

    //cabeceras de tables del dia
    String[] cabPaysDay = {"ID", "Fecha", "Monto", "Nota", "Metodo"};
    String[] cabMayAsignados = {"IdR", "Mayorista", "idC", "Fech"};
    String[] cabMayView = {"#Compra", "id", "Tipo", "Cantidad", "Costo", "Importe"};
    String[] cabMayViewcHECK = {"#Compra", "id","idtype", "Tipo", "Cantidad", "Costo", "Importe","Select","A Asignar","Asignados","Disponibles"};
    
    String[][] matLlenaPed = null;

    AgregaFlete aF;
    VentaPiso vP;
    detailPedido dP;
    detallePrestamo dPresta;
    FormacionPedido fP;
    detailCompra dComp;
    detalleVP ventDP;

    /**
     * Creates new form interno1
     */
    public interno1() {//https://opengroup.org/togaf  scrumstudy.com JAVAEE JAKARTAEE-the future of javaee, MICROPROFILE-OPTIMIZING ENTERPRISE JAVA
        initComponents();//apache tomcat, glassfish, JBoss, WebSphere IBM
        this.setLocationRelativeTo(null);//spring.io, spring-boot
        jRad1Activo.setSelected(true);//CF Pivotal Cloud
//panel alta de clientes
        fn.setBorder(jPanAdminist, "Formulario Clientes");
        fn.setBorder(jPanVistaAlta, "Vista de Clientes");
        fn.setBorder(jPanCompraProoved, "Compra a proveedor");
        jTextNombre.requestFocus();
        jDateChFechaAlta.setDate(cargafecha());
        //mostrarTabla();
        jTableAltasCli.setFont(new java.awt.Font("Tahoma", 0, 14));
//        jPanAgregarFlete.setVisible(false);
//panel compra proovedor
        jPanClientOption.setVisible(false);
        jPanSubastaOption.setVisible(false);

        //jTable1.setModel(new TModel(null, cabMayView));
//panel crea pedidos
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
        AgregarMayoreo.setVisible(false);

//panel ventas de mostrador
        jButton10.setMnemonic(KeyEvent.VK_F12);
        //jBCancelVentaP.setMnemonic(KeyEvent.VK_F10);
        jPaNVentaPiso.setVisible(false);
        jPanBusqVentasPiso.setVisible(false);
//pane fletes;
        jLabLetreroFletes.setVisible(false);
        jPanCreaFletes.setVisible(false);
        jPanHistorFletes.setVisible(false);
        //paneAltas.setEnabledAt(5, false);
        //paneAltas.setEnabledAt(6, false);
        //paneAltas.setEnabledAt(7, false);

//panel asignacion
        jDCAsignacionDia.setDate(cargafecha());
        jLabPed.setVisible(false);
        jLaComp.setVisible(false);
        jLabFlet.setVisible(false);

//panel pagos vista
        jDFechPays.setDate(cargafecha());
    }

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
        jPanCabezera = new javax.swing.JPanel();
        jLabel52 = new javax.swing.JLabel();
        jTextField17 = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
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
        jScrollPane10 = new javax.swing.JScrollPane();
        jTabPedidosDiaView = new javax.swing.JTable();
        jButGuardaPedidodia = new javax.swing.JButton();
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
        jScrollPane7 = new javax.swing.JScrollPane();
        jTabPrestamoProovedores = new javax.swing.JTable();
        jScrollPane8 = new javax.swing.JScrollPane();
        textANotaPrestProv = new javax.swing.JTextArea();
        jLabel36 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jLabel64 = new javax.swing.JLabel();
        jLabel65 = new javax.swing.JLabel();
        txtImportPres = new javax.swing.JTextField();
        jLabel67 = new javax.swing.JLabel();
        jScrollPane11 = new javax.swing.JScrollPane();
        jTabPreciosPrest = new javax.swing.JTable();
        jScrollPane13 = new javax.swing.JScrollPane();
        jTabVistaPresta = new javax.swing.JTable();
        jLabel68 = new javax.swing.JLabel();
        jSeparator8 = new javax.swing.JSeparator();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
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
        jScrollPane19 = new javax.swing.JScrollPane();
        jTabFletesDia = new javax.swing.JTable();
        jLabel93 = new javax.swing.JLabel();
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
        jPanNominas = new javax.swing.JPanel();
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
        txtFolioFleteAsign = new javax.swing.JTextField();
        jLabel116 = new javax.swing.JLabel();
        jScrollPane24 = new javax.swing.JScrollPane();
        jTabDetailAsign = new javax.swing.JTable();
        jLabel117 = new javax.swing.JLabel();
        jLabel118 = new javax.swing.JLabel();
        jLabel119 = new javax.swing.JLabel();
        jLabel120 = new javax.swing.JLabel();
        jLabel121 = new javax.swing.JLabel();
        jLabel122 = new javax.swing.JLabel();
        jLabel123 = new javax.swing.JLabel();
        jButGuardDetail = new javax.swing.JButton();
        jLabel127 = new javax.swing.JLabel();
        jScrollPane32 = new javax.swing.JScrollPane();
        jTabDetailAsignTotales = new javax.swing.JTable();
        jLabel124 = new javax.swing.JLabel();
        jScrollPane33 = new javax.swing.JScrollPane();
        jTabDetailAsignTotales1 = new javax.swing.JTable();
        jLabel125 = new javax.swing.JLabel();
        jLabFlet = new javax.swing.JLabel();
        jLabPed = new javax.swing.JLabel();
        jLaComp = new javax.swing.JLabel();
        jLabel126 = new javax.swing.JLabel();
        jScrollPane27 = new javax.swing.JScrollPane();
        jTDetailAsign = new javax.swing.JTable();
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
        jPanPagos = new javax.swing.JPanel();
        jLayeredPane2 = new javax.swing.JLayeredPane();
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
        jDFechPays = new com.toedter.calendar.JDateChooser();
        jButFleteGuardar1 = new javax.swing.JButton();
        jLabel100 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabPed1 = new javax.swing.JLabel();
        jScrollPane29 = new javax.swing.JScrollPane();
        jTabPaysFletesDia = new javax.swing.JTable();
        jLabPed6 = new javax.swing.JLabel();
        jScrollPane31 = new javax.swing.JScrollPane();
        jTabPaysCompraProovedor = new javax.swing.JTable();

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
        AgregarMayoreo.setText("Agregar a mayorista");
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

        jMItDetailVer.setText("Ver detalle");
        jMItDetailVer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMItDetailVerActionPerformed(evt);
            }
        });
        jPMDetailFormaPedido.add(jMItDetailVer);
        jPMDetailFormaPedido.add(jSeparator16);

        jMIPaysPresta.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("ADMINISTRACION DCR");
        setIconImage(getIconImage());
        setPreferredSize(new java.awt.Dimension(1600, 900));

        jPanCabezera.setBackground(new java.awt.Color(117, 229, 255));

        jLabel52.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel52.setText("USUARIO:");

        jTextField17.setEditable(false);
        jTextField17.setBackground(new java.awt.Color(255, 255, 255));
        jTextField17.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        jTextField17.setForeground(new java.awt.Color(51, 0, 153));
        jTextField17.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jButton2.setBackground(new java.awt.Color(117, 229, 255));
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/Actualizar.png"))); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton6.setBackground(new java.awt.Color(117, 229, 255));
        jButton6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton6.setText("BACKUP");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel52, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jTextField17, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(96, 96, 96))
        );
        jPanCabezeraLayout.setVerticalGroup(
            jPanCabezeraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanCabezeraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel52, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jTextField17, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                .addContainerGap(260, Short.MAX_VALUE))
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
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 1081, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(156, Short.MAX_VALUE))
        );
        jPanVistaAltaLayout.setVerticalGroup(
            jPanVistaAltaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanVistaAltaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanVistaAltaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtBusqAltas, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 326, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(166, Short.MAX_VALUE))
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
                .addComponent(jPanVistaAlta, javax.swing.GroupLayout.PREFERRED_SIZE, 1267, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(324, Short.MAX_VALUE))
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
                        .addComponent(jPanAdminist, javax.swing.GroupLayout.PREFERRED_SIZE, 1127, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, Short.MAX_VALUE))
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
                .addComponent(jPanVistaAlta, javax.swing.GroupLayout.PREFERRED_SIZE, 550, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

        jPanConsulPed.setBackground(new java.awt.Color(255, 255, 255));
        jPanConsulPed.setBorder(javax.swing.BorderFactory.createTitledBorder("Cear Pedido"));
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
        jTablefiltrosBusq.setColumnSelectionAllowed(true);
        jTablefiltrosBusq.setComponentPopupMenu(jPopPedidosDia);
        jTablefiltrosBusq.setRowHeight(25);
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

        javax.swing.GroupLayout jPanConsulPedLayout = new javax.swing.GroupLayout(jPanConsulPed);
        jPanConsulPed.setLayout(jPanConsulPedLayout);
        jPanConsulPedLayout.setHorizontalGroup(
            jPanConsulPedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanConsulPedLayout.createSequentialGroup()
                .addGroup(jPanConsulPedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanConsulPedLayout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(jLabel60, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jCombOpcBusqPedido, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanConsulPedLayout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(jLabel61)
                        .addGap(31, 31, 31)
                        .addComponent(jComPedBusqCli, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanConsulPedLayout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(jLabel63, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(31, 31, 31)
                        .addComponent(jDateChoB1Cli, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanConsulPedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel62, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanConsulPedLayout.createSequentialGroup()
                                .addGap(120, 120, 120)
                                .addComponent(jDate2BusqCli, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(20, 20, 20)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanConsulPedLayout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanConsulPedLayout.createSequentialGroup()
                        .addGap(146, 146, 146)
                        .addComponent(jSeparator7, javax.swing.GroupLayout.PREFERRED_SIZE, 460, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanConsulPedLayout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 1238, Short.MAX_VALUE)))
                .addGap(300, 300, 300))
        );
        jPanConsulPedLayout.setVerticalGroup(
            jPanConsulPedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanConsulPedLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanConsulPedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel60, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCombOpcBusqPedido, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(jPanConsulPedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel61, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComPedBusqCli, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanConsulPedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel63, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jDateChoB1Cli, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel62, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jDate2BusqCli, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(jSeparator7, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 412, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(116, Short.MAX_VALUE))
        );

        jPanCreaPedido.setBackground(new java.awt.Color(255, 255, 255));
        jPanCreaPedido.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Nuevo pedido", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13), new java.awt.Color(0, 51, 51))); // NOI18N
        jPanCreaPedido.setPreferredSize(new java.awt.Dimension(1600, 790));

        jCombPedidoClient.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCombPedidoClient.setMaximumRowCount(100);
        jCombPedidoClient.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " " }));
        jCombPedidoClient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCombPedidoClientActionPerformed(evt);
            }
        });

        jLabel46.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel46.setText("PEDIDOS DEL DIA:");

        jLabel43.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel43.setText("Tipo Mercancia:");

        jCombProdPedidos.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jLabel44.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel44.setText("CANTIDAD:");

        txtCantCreaPedido.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
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

        jTabPedidosDiaView.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTabPedidosDiaView.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTabPedidosDiaView.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jTabPedidosDiaView.setEditingColumn(0);
        jTabPedidosDiaView.setEditingRow(0);
        jTabPedidosDiaView.setRowHeight(32);
        jTabPedidosDiaView.setRowMargin(2);
        jTabPedidosDiaView.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTabPedidosDiaViewMousePressed(evt);
            }
        });
        jScrollPane10.setViewportView(jTabPedidosDiaView);

        jButGuardaPedidodia.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButGuardaPedidodia.setText("GUARDAR");
        jButGuardaPedidodia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButGuardaPedidodiaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanCreaPedidoLayout = new javax.swing.GroupLayout(jPanCreaPedido);
        jPanCreaPedido.setLayout(jPanCreaPedidoLayout);
        jPanCreaPedidoLayout.setHorizontalGroup(
            jPanCreaPedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanCreaPedidoLayout.createSequentialGroup()
                .addGroup(jPanCreaPedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanCreaPedidoLayout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addComponent(jLabel46, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(300, 300, 300)
                        .addComponent(jButGuardaPedidodia, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanCreaPedidoLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanCreaPedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanCreaPedidoLayout.createSequentialGroup()
                                .addGroup(jPanCreaPedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel59, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanCreaPedidoLayout.createSequentialGroup()
                                        .addGap(10, 10, 10)
                                        .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 681, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(39, 39, 39)
                                .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanCreaPedidoLayout.createSequentialGroup()
                                .addGroup(jPanCreaPedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel43)
                                    .addComponent(jLabel58))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanCreaPedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanCreaPedidoLayout.createSequentialGroup()
                                        .addComponent(jCombPedidoClient, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
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
                                        .addComponent(txtNotePedidoCli, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanCreaPedidoLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 1316, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(262, Short.MAX_VALUE))
        );
        jPanCreaPedidoLayout.setVerticalGroup(
            jPanCreaPedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanCreaPedidoLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanCreaPedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanCreaPedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jCombPedidoClient, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel58, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jDateCHPedido, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanCreaPedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanCreaPedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanCreaPedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel44, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCantCreaPedido, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jLabel45, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtNotePedidoCli, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanCreaPedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel43, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jCombProdPedidos, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanCreaPedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanCreaPedidoLayout.createSequentialGroup()
                        .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(84, 84, 84))
                    .addGroup(jPanCreaPedidoLayout.createSequentialGroup()
                        .addComponent(jLabel59, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(jPanCreaPedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanCreaPedidoLayout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(jLabel46, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButGuardaPedidodia, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 374, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(91, 91, 91))
        );

        jLayeredPanePedidos.setLayer(jPanConsulPed, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPanePedidos.setLayer(jPanCreaPedido, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jLayeredPanePedidosLayout = new javax.swing.GroupLayout(jLayeredPanePedidos);
        jLayeredPanePedidos.setLayout(jLayeredPanePedidosLayout);
        jLayeredPanePedidosLayout.setHorizontalGroup(
            jLayeredPanePedidosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jLayeredPanePedidosLayout.createSequentialGroup()
                .addComponent(jPanCreaPedido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(jPanConsulPed, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jLayeredPanePedidosLayout.setVerticalGroup(
            jLayeredPanePedidosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jLayeredPanePedidosLayout.createSequentialGroup()
                .addComponent(jPanCreaPedido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(jPanConsulPed, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
            .addComponent(jLayeredPanePedidos, javax.swing.GroupLayout.DEFAULT_SIZE, 2715, Short.MAX_VALUE)
        );
        jPPedidosHistLayout.setVerticalGroup(
            jPPedidosHistLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPPedidosHistLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPPedidosHistLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jRadioCreaPedido, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jRadioConsulPedido, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addComponent(jLayeredPanePedidos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
        jCElijaProovedor.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jCElijaProovedorFocusLost(evt);
            }
        });
        jCElijaProovedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCElijaProovedorActionPerformed(evt);
            }
        });

        jCombProductProv.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCombProductProv.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " " }));

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

        jButton1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton1.setText("AGREGAR");
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
        jTabVistaComprasDia.setComponentPopupMenu(jPopCompraProveedor);
        jTabVistaComprasDia.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jTabVistaComprasDia.setEditingColumn(0);
        jTabVistaComprasDia.setEditingRow(0);
        jTabVistaComprasDia.setRowHeight(32);
        jTabVistaComprasDia.setRowMargin(2);
        jTabVistaComprasDia.setSelectionBackground(new java.awt.Color(153, 255, 153));
        jTabVistaComprasDia.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTabVistaComprasDiaMousePressed(evt);
            }
        });
        jScrollPane6.setViewportView(jTabVistaComprasDia);
        if (jTabVistaComprasDia.getColumnModel().getColumnCount() > 0) {
            jTabVistaComprasDia.getColumnModel().getColumn(0).setMinWidth(40);
            jTabVistaComprasDia.getColumnModel().getColumn(0).setPreferredWidth(40);
            jTabVistaComprasDia.getColumnModel().getColumn(0).setMaxWidth(40);
            jTabVistaComprasDia.getColumnModel().getColumn(1).setMinWidth(110);
            jTabVistaComprasDia.getColumnModel().getColumn(1).setPreferredWidth(110);
            jTabVistaComprasDia.getColumnModel().getColumn(1).setMaxWidth(110);
            jTabVistaComprasDia.getColumnModel().getColumn(2).setMinWidth(60);
            jTabVistaComprasDia.getColumnModel().getColumn(2).setPreferredWidth(60);
            jTabVistaComprasDia.getColumnModel().getColumn(2).setMaxWidth(60);
            jTabVistaComprasDia.getColumnModel().getColumn(3).setMinWidth(60);
            jTabVistaComprasDia.getColumnModel().getColumn(3).setPreferredWidth(60);
            jTabVistaComprasDia.getColumnModel().getColumn(3).setMaxWidth(60);
            jTabVistaComprasDia.getColumnModel().getColumn(4).setMinWidth(60);
            jTabVistaComprasDia.getColumnModel().getColumn(4).setPreferredWidth(60);
            jTabVistaComprasDia.getColumnModel().getColumn(4).setMaxWidth(60);
            jTabVistaComprasDia.getColumnModel().getColumn(5).setMinWidth(60);
            jTabVistaComprasDia.getColumnModel().getColumn(5).setPreferredWidth(60);
            jTabVistaComprasDia.getColumnModel().getColumn(5).setMaxWidth(60);
            jTabVistaComprasDia.getColumnModel().getColumn(6).setMinWidth(60);
            jTabVistaComprasDia.getColumnModel().getColumn(6).setPreferredWidth(60);
            jTabVistaComprasDia.getColumnModel().getColumn(6).setMaxWidth(60);
            jTabVistaComprasDia.getColumnModel().getColumn(7).setMinWidth(60);
            jTabVistaComprasDia.getColumnModel().getColumn(7).setPreferredWidth(60);
            jTabVistaComprasDia.getColumnModel().getColumn(7).setMaxWidth(60);
            jTabVistaComprasDia.getColumnModel().getColumn(8).setMinWidth(60);
            jTabVistaComprasDia.getColumnModel().getColumn(8).setPreferredWidth(60);
            jTabVistaComprasDia.getColumnModel().getColumn(8).setMaxWidth(60);
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

        jLayeredPane3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanSubastaOption.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel57.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel57.setText("CAMIONETA:");
        jPanSubastaOption.add(jLabel57, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 90, 40));

        txtCamSubastaCompra.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtCamSubastaCompra.setNextFocusableComponent(jCombProductProv);
        jPanSubastaOption.add(txtCamSubastaCompra, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 10, 270, 40));

        jLayeredPane3.add(jPanSubastaOption, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 380, 50));

        jPanClientOption.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabAdeudaProoved.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabAdeudaProoved.setText("PRESTAMO PENDIENTE");
        jPanClientOption.add(jLabAdeudaProoved, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 230, 50));

        jLayeredPane3.add(jPanClientOption, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 250, 50));

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
        jLabel26.setText("DETALLE DE COMPRA A MAYORISTA");

        jTextField1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

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
        jTabDetallecompraAll.setColumnSelectionAllowed(true);
        jTabDetallecompraAll.setComponentPopupMenu(jPMneliminaDetailMayorista);
        jTabDetallecompraAll.setRowHeight(32);
        jTabDetallecompraAll.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTabDetallecompraAllMousePressed(evt);
            }
        });
        jScrollPane4.setViewportView(jTabDetallecompraAll);

        jScrollPane34.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jTabSumTotales.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jTabSumTotales.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "PRIM", "SEG", "PRIM_R", "SEG_R", "BOLA_P", "BOLA_S", "TERCERA", "TOTAL CAJAS", "IMPORTE TOTAL"
            }
        ));
        jTabSumTotales.setRowHeight(44);
        jScrollPane34.setViewportView(jTabSumTotales);
        if (jTabSumTotales.getColumnModel().getColumnCount() > 0) {
            jTabSumTotales.getColumnModel().getColumn(0).setMinWidth(60);
            jTabSumTotales.getColumnModel().getColumn(0).setPreferredWidth(60);
            jTabSumTotales.getColumnModel().getColumn(0).setMaxWidth(60);
            jTabSumTotales.getColumnModel().getColumn(1).setMinWidth(60);
            jTabSumTotales.getColumnModel().getColumn(1).setPreferredWidth(60);
            jTabSumTotales.getColumnModel().getColumn(1).setMaxWidth(60);
            jTabSumTotales.getColumnModel().getColumn(2).setMinWidth(60);
            jTabSumTotales.getColumnModel().getColumn(2).setPreferredWidth(60);
            jTabSumTotales.getColumnModel().getColumn(2).setMaxWidth(60);
            jTabSumTotales.getColumnModel().getColumn(3).setMinWidth(60);
            jTabSumTotales.getColumnModel().getColumn(3).setPreferredWidth(60);
            jTabSumTotales.getColumnModel().getColumn(3).setMaxWidth(60);
            jTabSumTotales.getColumnModel().getColumn(4).setMinWidth(60);
            jTabSumTotales.getColumnModel().getColumn(4).setPreferredWidth(60);
            jTabSumTotales.getColumnModel().getColumn(4).setMaxWidth(60);
            jTabSumTotales.getColumnModel().getColumn(5).setMinWidth(60);
            jTabSumTotales.getColumnModel().getColumn(5).setPreferredWidth(60);
            jTabSumTotales.getColumnModel().getColumn(5).setMaxWidth(60);
            jTabSumTotales.getColumnModel().getColumn(6).setMinWidth(60);
            jTabSumTotales.getColumnModel().getColumn(6).setPreferredWidth(60);
            jTabSumTotales.getColumnModel().getColumn(6).setMaxWidth(60);
        }

        jLabel11.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel11.setText("COMPRAS DEL DIA:");

        javax.swing.GroupLayout jPanCompraProovedLayout = new javax.swing.GroupLayout(jPanCompraProoved);
        jPanCompraProoved.setLayout(jPanCompraProovedLayout);
        jPanCompraProovedLayout.setHorizontalGroup(
            jPanCompraProovedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanCompraProovedLayout.createSequentialGroup()
                .addGroup(jPanCompraProovedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanCompraProovedLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jCElijaProovedor, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addComponent(jLayeredPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jDateFechCompraProv, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanCompraProovedLayout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addGroup(jPanCompraProovedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanCompraProovedLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(jPanCompraProovedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel48, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGroup(jPanCompraProovedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCombProductProv, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanCompraProovedLayout.createSequentialGroup()
                                .addComponent(txtCantidadCompra, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtPrecCompraProv, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanCompraProovedLayout.createSequentialGroup()
                                .addComponent(txtImportComp, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(20, 20, 20)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(20, 20, 20)
                        .addGroup(jPanCompraProovedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 580, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanCompraProovedLayout.createSequentialGroup()
                        .addGap(80, 80, 80)
                        .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(txtNotaCompra, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(110, 110, 110)
                        .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(280, 280, 280)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanCompraProovedLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(270, 270, 270)
                        .addComponent(jCheckBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanCompraProovedLayout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane34, javax.swing.GroupLayout.PREFERRED_SIZE, 995, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanCompraProovedLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 1145, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(435, Short.MAX_VALUE))
        );
        jPanCompraProovedLayout.setVerticalGroup(
            jPanCompraProovedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanCompraProovedLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanCompraProovedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLayeredPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCElijaProovedor, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jDateFechCompraProv, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanCompraProovedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanCompraProovedLayout.createSequentialGroup()
                        .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanCompraProovedLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(jPanCompraProovedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanCompraProovedLayout.createSequentialGroup()
                                .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(20, 20, 20)
                                .addComponent(jLabel48, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(20, 20, 20)
                                .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanCompraProovedLayout.createSequentialGroup()
                                .addComponent(jCombProductProv, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addGroup(jPanCompraProovedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtCantidadCompra, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(20, 20, 20)
                                .addComponent(txtPrecCompraProv, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addGroup(jPanCompraProovedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanCompraProovedLayout.createSequentialGroup()
                                        .addGap(10, 10, 10)
                                        .addComponent(txtImportComp, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addGap(10, 10, 10)
                .addGroup(jPanCompraProovedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanCompraProovedLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(jPanCompraProovedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNotaCompra, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(10, 10, 10)
                .addGroup(jPanCompraProovedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanCompraProovedLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jCheckBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanCompraProovedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane34, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(47, Short.MAX_VALUE))
        );

        jPanBusquedaPrest.setBackground(new java.awt.Color(255, 255, 255));
        jPanBusquedaPrest.setMinimumSize(new java.awt.Dimension(1600, 820));
        jPanBusquedaPrest.setPreferredSize(new java.awt.Dimension(1600, 790));
        jPanBusquedaPrest.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTabbedPane2.setBackground(new java.awt.Color(0, 255, 204));
        jTabbedPane2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jTabbedPane2.setPreferredSize(new java.awt.Dimension(1600, 790));

        jPanInternoBusquedaPrest.setBackground(new java.awt.Color(255, 255, 255));
        jPanInternoBusquedaPrest.setPreferredSize(new java.awt.Dimension(1600, 668));
        jPanInternoBusquedaPrest.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jDaTFechPrest1.setDateFormatString("dd/MM/yyyy");
        jDaTFechPrest1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jPanInternoBusquedaPrest.add(jDaTFechPrest1, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 120, 170, 40));

        jLabel42.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N
        jLabel42.setText("Tabla de Resultados:");
        jPanInternoBusquedaPrest.add(jLabel42, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 160, 140, 40));
        jPanInternoBusquedaPrest.add(jSeparator4, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 180, 260, 10));

        jCombBProvBusqPrest.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCombBProvBusqPrest.setMaximumRowCount(25);
        jPanInternoBusquedaPrest.add(jCombBProvBusqPrest, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 70, 220, 40));

        jLabel47.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel47.setText("ELIJA PROVEEDOR:");
        jPanInternoBusquedaPrest.add(jLabel47, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 70, 150, 40));

        jComBusPrestamo.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jComBusPrestamo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "CLIENTE", "FECHA", "LAPSO FECHAS", "CLIENTE+FECHA", "CLIENTE+LAPSO FECHAS" }));
        jComBusPrestamo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComBusPrestamoActionPerformed(evt);
            }
        });
        jPanInternoBusquedaPrest.add(jComBusPrestamo, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 20, 220, 40));

        jLabel69.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel69.setText("TIPO DE BUSQUEDA:");
        jPanInternoBusquedaPrest.add(jLabel69, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, 150, 40));

        jLabel70.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel70.setText("ELIJA FECHA 2:");
        jPanInternoBusquedaPrest.add(jLabel70, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 120, 120, 40));

        jDaTFechPrest2.setDateFormatString("dd/MM/yyyy");
        jDaTFechPrest2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jPanInternoBusquedaPrest.add(jDaTFechPrest2, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 120, 170, 40));

        jButton5.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/search32pxcolor.png"))); // NOI18N
        jButton5.setText("BUSCAR");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanInternoBusquedaPrest.add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 120, 160, 50));

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

        jPanInternoBusquedaPrest.add(jScrollPane14, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 200, 1140, 430));

        jLabel99.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel99.setText("ELIJA FECHA:");
        jPanInternoBusquedaPrest.add(jLabel99, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 120, 140, 40));

        jTabbedPane2.addTab("PRESTAMOS                   ", jPanInternoBusquedaPrest);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jComBusCompra.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jComBusCompra.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "CLIENTE", "MAYORISTA", "MAYORISTA+FECHA", "FECHA", "LAPSO FECHAS", "CLIENTE+FECHA", "CLIENTE+LAPSO FECHAS" }));
        jComBusCompra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComBusCompraActionPerformed(evt);
            }
        });
        jPanel4.add(jComBusCompra, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 20, 230, 40));

        jCombBProvBusqCompra.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCombBProvBusqCompra.setMaximumRowCount(25);
        jPanel4.add(jCombBProvBusqCompra, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 70, 230, 40));

        jDaTFechComp1.setDateFormatString("dd/MM/yyyy");
        jDaTFechComp1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jPanel4.add(jDaTFechComp1, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 120, 170, 40));

        jLabel72.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel72.setText("ELIJA FECHA:");
        jPanel4.add(jLabel72, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 120, 140, 40));

        jLabel49.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel49.setText("ELIJA PROVEEDOR:");
        jPanel4.add(jLabel49, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 70, 150, 40));

        jLabel73.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel73.setText("TIPO DE BUSQUEDA:");
        jPanel4.add(jLabel73, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 20, 150, 40));

        jLabel50.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N
        jLabel50.setText("Tabla de Resultados:");
        jPanel4.add(jLabel50, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 160, 140, 40));
        jPanel4.add(jSeparator9, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 180, 260, 10));

        jLabel74.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel74.setText("ELIJA FECHA 2:");
        jPanel4.add(jLabel74, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 120, 120, 40));

        jDaTFechCompraProv2.setDateFormatString("dd/MM/yyyy");
        jDaTFechCompraProv2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jPanel4.add(jDaTFechCompraProv2, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 120, 170, 40));

        jButton14.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jButton14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/search32pxcolor.png"))); // NOI18N
        jButton14.setText("BUSCAR");
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });
        jPanel4.add(jButton14, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 120, 160, 50));

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

        jPanel4.add(jScrollPane16, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 200, 1190, 490));

        jTabbedPane2.addTab("COMPRAS", jPanel4);

        jPanBusquedaPrest.add(jTabbedPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1600, -1));

        jPanPrestamoProovedor.setBackground(new java.awt.Color(255, 255, 255));
        jPanPrestamoProovedor.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Prestamo a Proveedor", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 12))); // NOI18N
        jPanPrestamoProovedor.setPreferredSize(new java.awt.Dimension(1600, 790));
        jPanPrestamoProovedor.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel33.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel33.setText("DETALLE:");
        jPanPrestamoProovedor.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, 150, 40));

        jComBPrestamosProv.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jComBPrestamosProv.setNextFocusableComponent(jComBProveedor);
        jComBPrestamosProv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComBPrestamosProvActionPerformed(evt);
            }
        });
        jPanPrestamoProovedor.add(jComBPrestamosProv, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 30, 190, 40));

        jLabel34.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel34.setText("Prestamo de:");
        jPanPrestamoProovedor.add(jLabel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 120, 40));

        jComBProveedor.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jComBProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComBProveedorActionPerformed(evt);
            }
        });
        jPanPrestamoProovedor.add(jComBProveedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 80, 140, 40));

        txtCantPres.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jPanPrestamoProovedor.add(txtCantPres, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 80, 80, 40));

        jLabBNumerador.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabBNumerador.setText("CAJAS");
        jPanPrestamoProovedor.add(jLabBNumerador, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 80, 60, 40));

        jLabel37.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel37.setText("FECHA:");
        jPanPrestamoProovedor.add(jLabel37, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 30, 70, 40));

        txtPrecProov.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtPrecProov.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPrecProovFocusLost(evt);
            }
        });
        jPanPrestamoProovedor.add(txtPrecProov, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 80, 80, 40));

        jButton7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/add-BN.png"))); // NOI18N
        jButton7.setText("AGREGAR");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        jPanPrestamoProovedor.add(jButton7, new org.netbeans.lib.awtextra.AbsoluteConstraints(990, 160, 140, 50));

        jLabel38.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel38.setText("NOTA:");
        jPanPrestamoProovedor.add(jLabel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(1000, 40, 70, 40));

        jDatFechaPrest.setDateFormatString("dd/MM/yyyy");
        jDatFechaPrest.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanPrestamoProovedor.add(jDatFechaPrest, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 30, 170, 40));

        jTabPrestamoProovedores.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTabPrestamoProovedores.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "CAJA", "PERIODICO", "SEMILLA", "EFECTIVO"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTabPrestamoProovedores.setComponentPopupMenu(jPopupPrestaProov);
        jTabPrestamoProovedores.setRowHeight(32);
        jTabPrestamoProovedores.setRowMargin(2);
        jScrollPane7.setViewportView(jTabPrestamoProovedores);

        jPanPrestamoProovedor.add(jScrollPane7, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 160, 830, 60));

        textANotaPrestProv.setColumns(20);
        textANotaPrestProv.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        textANotaPrestProv.setRows(5);
        textANotaPrestProv.setNextFocusableComponent(jTabPrestamoProovedores);
        textANotaPrestProv.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                textANotaPrestProvKeyPressed(evt);
            }
        });
        jScrollPane8.setViewportView(textANotaPrestProv);

        jPanPrestamoProovedor.add(jScrollPane8, new org.netbeans.lib.awtextra.AbsoluteConstraints(1000, 80, 250, 60));

        jLabel36.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel36.setText("CANTIDAD:");
        jPanPrestamoProovedor.add(jLabel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 80, 90, 40));

        jButton4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton4.setText("GUARDAR");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanPrestamoProovedor.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 260, 130, 50));

        jLabel64.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel64.setText("COSTO:");
        jPanPrestamoProovedor.add(jLabel64, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 80, 70, 40));

        jLabel65.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel65.setText("IMPORTE:");
        jPanPrestamoProovedor.add(jLabel65, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 80, 70, 40));

        txtImportPres.setEditable(false);
        txtImportPres.setBackground(new java.awt.Color(255, 255, 255));
        txtImportPres.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtImportPres.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtImportPresKeyPressed(evt);
            }
        });
        jPanPrestamoProovedor.add(txtImportPres, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 80, 80, 40));

        jLabel67.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel67.setText("CREDITOS REALIZADOS:");
        jPanPrestamoProovedor.add(jLabel67, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 300, 190, 40));

        jTabPreciosPrest.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTabPreciosPrest.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "CAJA", "PERIODICO", "SEMILLA", "EFECTIVO"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTabPreciosPrest.setRowHeight(32);
        jTabPreciosPrest.setRowMargin(2);
        jScrollPane11.setViewportView(jTabPreciosPrest);

        jPanPrestamoProovedor.add(jScrollPane11, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 190, 830, 70));

        jTabVistaPresta.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTabVistaPresta.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTabVistaPresta.setRowHeight(32);
        jTabVistaPresta.setRowMargin(2);
        jTabVistaPresta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTabVistaPrestaMousePressed(evt);
            }
        });
        jScrollPane13.setViewportView(jTabVistaPresta);

        jPanPrestamoProovedor.add(jScrollPane13, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 340, 1310, 310));

        jLabel68.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel68.setText("ELIJA PROVEEDOR:");
        jPanPrestamoProovedor.add(jLabel68, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, 150, 40));
        jPanPrestamoProovedor.add(jSeparator8, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 320, 1020, 10));

        jLabel27.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel27.setText("CANTIDAD:");
        jPanPrestamoProovedor.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 180, 90, 40));

        jLabel28.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel28.setText("COSTO   $:");
        jPanPrestamoProovedor.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 220, 90, 40));

        jLayeredPane1.setLayer(jPanCompraProoved, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(jPanBusquedaPrest, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(jPanPrestamoProovedor, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jLayeredPane1Layout = new javax.swing.GroupLayout(jLayeredPane1);
        jLayeredPane1.setLayout(jLayeredPane1Layout);
        jLayeredPane1Layout.setHorizontalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanBusquedaPrest, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jPanCompraProoved, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jPanPrestamoProovedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jLayeredPane1Layout.setVerticalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanBusquedaPrest, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jPanCompraProoved, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jPanPrestamoProovedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        javax.swing.GroupLayout proveedorJPLayout = new javax.swing.GroupLayout(proveedorJP);
        proveedorJP.setLayout(proveedorJPLayout);
        proveedorJPLayout.setHorizontalGroup(
            proveedorJPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(proveedorJPLayout.createSequentialGroup()
                .addGap(84, 84, 84)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(150, 150, 150)
                .addComponent(jRadBPrestamoProv, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addComponent(jRadBConsultaProv, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(proveedorJPLayout.createSequentialGroup()
                .addGap(224, 224, 224)
                .addComponent(jRadBCompraProve, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jLayeredPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        proveedorJPLayout.setVerticalGroup(
            proveedorJPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(proveedorJPLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(proveedorJPLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jRadBPrestamoProv, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(proveedorJPLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jRadBConsultaProv, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(proveedorJPLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jRadBCompraProve, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(proveedorJPLayout.createSequentialGroup()
                .addGap(48, 48, 48)
                .addComponent(jLayeredPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 796, Short.MAX_VALUE))
        );

        paneAltas.addTab("    PROVEEDORES    ", proveedorJP);

        jPFletes.setPreferredSize(new java.awt.Dimension(1600, 800));

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

        jLayerFletes.setBackground(new java.awt.Color(153, 255, 153));
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
                        .addGap(80, 80, 80)
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(txtCostoFlete, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jLabel88, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(160, 160, 160)
                        .addComponent(jButAltasActualiza1, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanAdminist1Layout.createSequentialGroup()
                        .addComponent(jLabel87, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(txtUnidFlete, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(80, 80, 80)
                        .addComponent(jLabel91, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(txtNotaFlete, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                            .addComponent(txtFolioFlete, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabLetreroFletes, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel92, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jRPagadopFlete, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jRPendFlete, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(10, 10, 10)
                .addGroup(jPanAdminist1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel90, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtChoferFlete, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCostoFlete, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel88, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButAltasActualiza1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanAdminist1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel87, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtUnidFlete, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel91, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNotaFlete, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanAdminist1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jButAltasElimina1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))))
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
        jScrollPane19.setViewportView(jTabFletesDia);

        jLabel93.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel93.setText("Elija fletero:");

        javax.swing.GroupLayout jPanCreaFletesLayout = new javax.swing.GroupLayout(jPanCreaFletes);
        jPanCreaFletes.setLayout(jPanCreaFletesLayout);
        jPanCreaFletesLayout.setHorizontalGroup(
            jPanCreaFletesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanCreaFletesLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(jPanAdminist1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(104, 104, 104))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanCreaFletesLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane19, javax.swing.GroupLayout.PREFERRED_SIZE, 1301, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(260, 260, 260))
            .addGroup(jPanCreaFletesLayout.createSequentialGroup()
                .addGroup(jPanCreaFletesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
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
                        .addComponent(jDFechCreaFlete, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanCreaFletesLayout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanCreaFletesLayout.setVerticalGroup(
            jPanCreaFletesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanCreaFletesLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanCreaFletesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel93, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCBAltasFletes, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel86, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jDFechCreaFlete, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addComponent(jPanAdminist1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane19, javax.swing.GroupLayout.PREFERRED_SIZE, 329, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanHistorFletes.setBackground(new java.awt.Color(255, 255, 255));
        jPanHistorFletes.setPreferredSize(new java.awt.Dimension(1600, 790));

        jTablefiltrosBusqflete.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTablefiltrosBusqflete.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTablefiltrosBusqflete.setComponentPopupMenu(jPopMFiltrosBusqFletes);
        jTablefiltrosBusqflete.setRowHeight(32);
        jTablefiltrosBusqflete.setRowMargin(2);
        jTablefiltrosBusqflete.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTablefiltrosBusqfleteMousePressed(evt);
            }
        });
        jScrollPane20.setViewportView(jTablefiltrosBusqflete);

        jLabel94.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel94.setText("Tabla de Resultados");

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

        jLabel98.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel98.setText("TIPO DE BUSQUEDA");

        javax.swing.GroupLayout jPanHistorFletesLayout = new javax.swing.GroupLayout(jPanHistorFletes);
        jPanHistorFletes.setLayout(jPanHistorFletesLayout);
        jPanHistorFletesLayout.setHorizontalGroup(
            jPanHistorFletesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanHistorFletesLayout.createSequentialGroup()
                .addGroup(jPanHistorFletesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanHistorFletesLayout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(jLabel98, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jCombOpcBusqFletes, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanHistorFletesLayout.createSequentialGroup()
                        .addGap(80, 80, 80)
                        .addComponent(jLabel97, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jCfleteroOpc, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanHistorFletesLayout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addGroup(jPanHistorFletesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanHistorFletesLayout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addComponent(jLabel95, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(jDCFol1, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(50, 50, 50)
                                .addGroup(jPanHistorFletesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanHistorFletesLayout.createSequentialGroup()
                                        .addGap(120, 120, 120)
                                        .addComponent(jDCFol2, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel96, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jLabel94, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanHistorFletesLayout.createSequentialGroup()
                                .addGap(130, 130, 130)
                                .addComponent(jSeparator15, javax.swing.GroupLayout.PREFERRED_SIZE, 460, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(20, 20, 20)
                        .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanHistorFletesLayout.createSequentialGroup()
                .addGap(0, 30, Short.MAX_VALUE)
                .addComponent(jScrollPane20, javax.swing.GroupLayout.PREFERRED_SIZE, 1305, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(266, 266, 266))
        );
        jPanHistorFletesLayout.setVerticalGroup(
            jPanHistorFletesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanHistorFletesLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanHistorFletesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel98, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCombOpcBusqFletes, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(jPanHistorFletesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel97, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCfleteroOpc, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanHistorFletesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanHistorFletesLayout.createSequentialGroup()
                        .addGroup(jPanHistorFletesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel95, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jDCFol1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jDCFol2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel96, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanHistorFletesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel94, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanHistorFletesLayout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(jSeparator15, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane20, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jLayerFletes.setLayer(jPanCreaFletes, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayerFletes.setLayer(jPanHistorFletes, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jLayerFletesLayout = new javax.swing.GroupLayout(jLayerFletes);
        jLayerFletes.setLayout(jLayerFletesLayout);
        jLayerFletesLayout.setHorizontalGroup(
            jLayerFletesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanHistorFletes, javax.swing.GroupLayout.DEFAULT_SIZE, 1601, Short.MAX_VALUE)
            .addComponent(jPanCreaFletes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jLayerFletesLayout.setVerticalGroup(
            jLayerFletesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanHistorFletes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jPanCreaFletes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
            .addGroup(jPFletesLayout.createSequentialGroup()
                .addComponent(jLayerFletes, javax.swing.GroupLayout.PREFERRED_SIZE, 1601, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPFletesLayout.setVerticalGroup(
            jPFletesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPFletesLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPFletesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jRdCreaFletes, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jRHistorFletes, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addComponent(jLayerFletes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        paneAltas.addTab("    FLETES    ", jPFletes);

        jPanNominas.setBackground(new java.awt.Color(255, 255, 255));
        jPanNominas.setPreferredSize(new java.awt.Dimension(1600, 850));

        jTabVistaPedidosDia1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTabVistaPedidosDia1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "CLIENTE", "PRIM", "SEG", "PRIM_R", "SEG_R", "BOLA_P", "BOLA_S", "TERCERA", "TOTAL CAJAS"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
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
        jTabVistaPedidosDia1.setRowHeight(32);
        jTabVistaPedidosDia1.setRowMargin(2);
        jTabVistaPedidosDia1.setSelectionBackground(new java.awt.Color(153, 255, 153));
        jTabVistaPedidosDia1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTabVistaPedidosDia1FocusLost(evt);
            }
        });
        jScrollPane23.setViewportView(jTabVistaPedidosDia1);
        if (jTabVistaPedidosDia1.getColumnModel().getColumnCount() > 0) {
            jTabVistaPedidosDia1.getColumnModel().getColumn(0).setMinWidth(60);
            jTabVistaPedidosDia1.getColumnModel().getColumn(0).setPreferredWidth(60);
            jTabVistaPedidosDia1.getColumnModel().getColumn(0).setMaxWidth(60);
            jTabVistaPedidosDia1.getColumnModel().getColumn(2).setPreferredWidth(15);
            jTabVistaPedidosDia1.getColumnModel().getColumn(3).setPreferredWidth(15);
            jTabVistaPedidosDia1.getColumnModel().getColumn(4).setPreferredWidth(15);
            jTabVistaPedidosDia1.getColumnModel().getColumn(5).setPreferredWidth(15);
            jTabVistaPedidosDia1.getColumnModel().getColumn(6).setPreferredWidth(15);
            jTabVistaPedidosDia1.getColumnModel().getColumn(7).setPreferredWidth(15);
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
        jTabVistaComprasDia3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jTabVistaComprasDia3.setEditingColumn(0);
        jTabVistaComprasDia3.setEditingRow(0);
        jTabVistaComprasDia3.setRowHeight(32);
        jTabVistaComprasDia3.setRowMargin(2);
        jTabVistaComprasDia3.setSelectionBackground(new java.awt.Color(153, 255, 153));
        jTabVistaComprasDia3.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTabVistaComprasDia3FocusLost(evt);
            }
        });
        jScrollPane25.setViewportView(jTabVistaComprasDia3);
        if (jTabVistaComprasDia3.getColumnModel().getColumnCount() > 0) {
            jTabVistaComprasDia3.getColumnModel().getColumn(0).setMinWidth(60);
            jTabVistaComprasDia3.getColumnModel().getColumn(0).setPreferredWidth(60);
            jTabVistaComprasDia3.getColumnModel().getColumn(0).setMaxWidth(60);
            jTabVistaComprasDia3.getColumnModel().getColumn(2).setPreferredWidth(15);
            jTabVistaComprasDia3.getColumnModel().getColumn(3).setPreferredWidth(15);
            jTabVistaComprasDia3.getColumnModel().getColumn(4).setPreferredWidth(15);
            jTabVistaComprasDia3.getColumnModel().getColumn(5).setPreferredWidth(15);
            jTabVistaComprasDia3.getColumnModel().getColumn(6).setPreferredWidth(15);
            jTabVistaComprasDia3.getColumnModel().getColumn(7).setPreferredWidth(15);
        }

        jButton17.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton17.setText(">>");
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
        jTabFletesDia1.setRowHeight(32);
        jTabFletesDia1.setRowMargin(2);
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
        jButton18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton18ActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Asignacion de Flete-Mercancia-Pedido", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 14))); // NOI18N

        jLabel114.setFont(new java.awt.Font("Calibri", 0, 15)); // NOI18N
        jLabel114.setText("PARA PEDIDO:");

        jLabel115.setFont(new java.awt.Font("Calibri", 0, 15)); // NOI18N
        jLabel115.setText("ID COMPRA:");

        txtCompraAsign.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N

        txtidPedidoAsign.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N

        txtFolioFleteAsign.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N

        jLabel116.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel116.setText("Status del pedido:");

        jTabDetailAsign.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jTabDetailAsign.setModel(new javax.swing.table.DefaultTableModel(
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
                "CANTIDAD", "PRECIO"
            }
        ));
        jTabDetailAsign.setAlignmentX(3.0F);
        jTabDetailAsign.setRowHeight(40);
        jTabDetailAsign.setRowMargin(2);
        jScrollPane24.setViewportView(jTabDetailAsign);

        jLabel117.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel117.setText("PRIM:");
        jLabel117.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel118.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel118.setText("SEG:");
        jLabel118.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel119.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel119.setText("PRIM_REG:");
        jLabel119.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel120.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel120.setText("SEG_REG:");
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

        jButGuardDetail.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/save32px.png"))); // NOI18N
        jButGuardDetail.setToolTipText("Actualiza pedido.");
        jButGuardDetail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButGuardDetailActionPerformed(evt);
            }
        });

        jLabel127.setFont(new java.awt.Font("Calibri", 0, 15)); // NOI18N
        jLabel127.setText("FOLIO DE FLETE:");

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
        jTabDetailAsignTotales.setRowHeight(40);
        jTabDetailAsignTotales.setRowMargin(2);
        jScrollPane32.setViewportView(jTabDetailAsignTotales);

        jLabel124.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel124.setText("Producto a asignar:");

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
        jTabDetailAsignTotales1.setRowHeight(40);
        jTabDetailAsignTotales1.setRowMargin(2);
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
                        .addGap(14, 14, 14)
                        .addComponent(jLabel127, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(txtFolioFleteAsign, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(50, 50, 50)
                        .addComponent(jLabel115, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(txtCompraAsign, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addComponent(jLabel114, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(txtidPedidoAsign, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(150, 150, 150)
                        .addComponent(jButGuardDetail, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addComponent(jLabel124, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addComponent(jLabel116, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jLabel125, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel117, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel121, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel122, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel123, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel120, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel118, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(70, 70, 70)
                                .addComponent(jScrollPane24, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel119))
                        .addComponent(jScrollPane32, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jScrollPane33, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(107, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel127, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtFolioFleteAsign, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel115, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCompraAsign, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel114, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtidPedidoAsign, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButGuardDetail, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel124, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel116, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel125, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(jLabel117, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(45, 45, 45)
                        .addComponent(jLabel119, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(45, 45, 45)
                        .addComponent(jLabel121, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(230, 230, 230)
                        .addComponent(jLabel122, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(270, 270, 270)
                        .addComponent(jLabel123, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(150, 150, 150)
                        .addComponent(jLabel120, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(70, 70, 70)
                        .addComponent(jLabel118, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane24, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane32, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane33, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)))
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

        jTDetailAsign.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTDetailAsign.setRowHeight(32);
        jTDetailAsign.setRowMargin(2);
        jScrollPane27.setViewportView(jTDetailAsign);

        javax.swing.GroupLayout jPanNominasLayout = new javax.swing.GroupLayout(jPanNominas);
        jPanNominas.setLayout(jPanNominasLayout);
        jPanNominasLayout.setHorizontalGroup(
            jPanNominasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanNominasLayout.createSequentialGroup()
                .addGroup(jPanNominasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanNominasLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(jPanNominasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanNominasLayout.createSequentialGroup()
                                .addGap(50, 50, 50)
                                .addComponent(jLabel126, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane23, javax.swing.GroupLayout.PREFERRED_SIZE, 660, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanNominasLayout.createSequentialGroup()
                        .addGap(450, 450, 450)
                        .addComponent(jLabPed, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanNominasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanNominasLayout.createSequentialGroup()
                            .addGap(60, 60, 60)
                            .addComponent(jLabel110, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(210, 210, 210)
                            .addComponent(jLabFlet, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(70, 70, 70))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanNominasLayout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(jPanNominasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanNominasLayout.createSequentialGroup()
                                    .addGap(50, 50, 50)
                                    .addComponent(jLabel112, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(220, 220, 220)
                                    .addComponent(jLaComp, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jScrollPane25, javax.swing.GroupLayout.PREFERRED_SIZE, 660, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanNominasLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane26, javax.swing.GroupLayout.PREFERRED_SIZE, 660, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanNominasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanNominasLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jButton18, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jDCAsignacionDia, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanNominasLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jButton17, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanNominasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanNominasLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanNominasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jScrollPane27, javax.swing.GroupLayout.PREFERRED_SIZE, 643, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanNominasLayout.createSequentialGroup()
                                .addGap(126, 126, 126)
                                .addComponent(jLabel111, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(192, Short.MAX_VALUE))
        );
        jPanNominasLayout.setVerticalGroup(
            jPanNominasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanNominasLayout.createSequentialGroup()
                .addGroup(jPanNominasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanNominasLayout.createSequentialGroup()
                        .addGroup(jPanNominasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel126, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanNominasLayout.createSequentialGroup()
                                .addComponent(jLabPed, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(3, 3, 3)
                                .addComponent(jScrollPane23, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanNominasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel112, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLaComp, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jScrollPane25, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanNominasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel110, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabFlet, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane26, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanNominasLayout.createSequentialGroup()
                        .addGroup(jPanNominasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanNominasLayout.createSequentialGroup()
                                .addComponent(jButton18, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(237, 237, 237)
                                .addComponent(jButton17, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanNominasLayout.createSequentialGroup()
                                .addComponent(jDCAsignacionDia, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel111)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane27, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(39, Short.MAX_VALUE))
        );

        paneAltas.addTab("ASIGNACION", jPanNominas);

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
                        .addGap(80, 80, 80)
                        .addComponent(jLabel79, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jCombCliVentaP, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(330, 330, 330)
                        .addComponent(jLabel76, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jDFVentaPiso, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPaNVentaPisoLayout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(jLabel75, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
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
                        .addGroup(jPaNVentaPisoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPaNVentaPisoLayout.createSequentialGroup()
                                .addGap(50, 50, 50)
                                .addComponent(txtNotaVentP, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel78, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPaNVentaPisoLayout.createSequentialGroup()
                        .addGap(70, 70, 70)
                        .addGroup(jPaNVentaPisoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel56, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPaNVentaPisoLayout.createSequentialGroup()
                                .addGap(110, 110, 110)
                                .addComponent(jSeparator11, javax.swing.GroupLayout.PREFERRED_SIZE, 430, javax.swing.GroupLayout.PREFERRED_SIZE))))
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
                                        .addGap(20, 20, 20)
                                        .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPaNVentaPisoLayout.createSequentialGroup()
                                .addGap(51, 51, 51)
                                .addComponent(jLabel53, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSeparator12, javax.swing.GroupLayout.PREFERRED_SIZE, 754, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane17, javax.swing.GroupLayout.PREFERRED_SIZE, 1362, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(655, 655, 655))
        );
        jPaNVentaPisoLayout.setVerticalGroup(
            jPaNVentaPisoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPaNVentaPisoLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPaNVentaPisoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel79, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCombCliVentaP, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel76, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jDFVentaPiso, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPaNVentaPisoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel75, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCombProdVentaP, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel54, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCantVentaPiso, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel55, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel81, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPrecProdVentaP, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel77, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtImportVentaP, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNotaVentP, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel78, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPaNVentaPisoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel56, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPaNVentaPisoLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jSeparator11, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPaNVentaPisoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPaNVentaPisoLayout.createSequentialGroup()
                        .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addGroup(jPaNVentaPisoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPaNVentaPisoLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jLabel80, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(txtTotalVentaPiso, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPaNVentaPisoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel53, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator12, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addComponent(jScrollPane17, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
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

        javax.swing.GroupLayout jPanBusqVentasPisoLayout = new javax.swing.GroupLayout(jPanBusqVentasPiso);
        jPanBusqVentasPiso.setLayout(jPanBusqVentasPisoLayout);
        jPanBusqVentasPisoLayout.setHorizontalGroup(
            jPanBusqVentasPisoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanBusqVentasPisoLayout.createSequentialGroup()
                .addGroup(jPanBusqVentasPisoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanBusqVentasPisoLayout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(jLabel82, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jCombOpcBusqVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanBusqVentasPisoLayout.createSequentialGroup()
                        .addGap(80, 80, 80)
                        .addComponent(jLabel83, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jCCliVentaPiso, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanBusqVentasPisoLayout.createSequentialGroup()
                        .addGap(80, 80, 80)
                        .addComponent(jLabel84, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jDateChoBVent1, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(50, 50, 50)
                        .addGroup(jPanBusqVentasPisoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel85, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanBusqVentasPisoLayout.createSequentialGroup()
                                .addGap(120, 120, 120)
                                .addComponent(jDatebusqVenta2, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(20, 20, 20)
                        .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanBusqVentasPisoLayout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addGroup(jPanBusqVentasPisoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanBusqVentasPisoLayout.createSequentialGroup()
                                .addGap(130, 130, 130)
                                .addComponent(jSeparator13, javax.swing.GroupLayout.PREFERRED_SIZE, 460, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanBusqVentasPisoLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jScrollPane18, javax.swing.GroupLayout.PREFERRED_SIZE, 1103, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(477, Short.MAX_VALUE))
        );
        jPanBusqVentasPisoLayout.setVerticalGroup(
            jPanBusqVentasPisoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanBusqVentasPisoLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanBusqVentasPisoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel82, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCombOpcBusqVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(jPanBusqVentasPisoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel83, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCCliVentaPiso, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanBusqVentasPisoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel84, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jDateChoBVent1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel85, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jDatebusqVenta2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanBusqVentasPisoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanBusqVentasPisoLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jSeparator13, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane18, javax.swing.GroupLayout.PREFERRED_SIZE, 391, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jLayVentasPiso.setLayer(jPaNVentaPiso, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayVentasPiso.setLayer(jPanBusqVentasPiso, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jLayVentasPisoLayout = new javax.swing.GroupLayout(jLayVentasPiso);
        jLayVentasPiso.setLayout(jLayVentasPisoLayout);
        jLayVentasPisoLayout.setHorizontalGroup(
            jLayVentasPisoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPaNVentaPiso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jPanBusqVentasPiso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jLayVentasPisoLayout.setVerticalGroup(
            jLayVentasPisoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPaNVentaPiso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jPanBusqVentasPiso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
            .addComponent(jLayVentasPiso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jPanVentasPisoLayout.setVerticalGroup(
            jPanVentasPisoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanVentasPisoLayout.createSequentialGroup()
                .addGroup(jPanVentasPisoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jRadCreaVentaPiso, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jRadBusqVentaPiso, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addComponent(jLayVentasPiso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        paneAltas.addTab("    VENTAS DE PISO    ", jPanVentasPiso);

        jPanPagos.setPreferredSize(new java.awt.Dimension(1600, 820));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "ENTRADAS", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 13))); // NOI18N

        jLabPed3.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabPed3.setForeground(new java.awt.Color(0, 51, 102));
        jLabPed3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabPed3.setText("PAGO DE PEDIDOS CLIENTES");

        jTabPayPeds.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
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
        jTabPayPeds.setRowHeight(30);
        jScrollPane28.setViewportView(jTabPayPeds);

        jLabPed4.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabPed4.setForeground(new java.awt.Color(0, 51, 102));
        jLabPed4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabPed4.setText("PAGO DE VENTAS DE PISO");

        jLabPed5.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabPed5.setForeground(new java.awt.Color(0, 51, 102));
        jLabPed5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabPed5.setText("PAGO DE PRESTAMO A CLIENTES");

        jTabPayPrestamosProv.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
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
        jTabPayPrestamosProv.setRowHeight(30);
        jScrollPane30.setViewportView(jTabPayPrestamosProv);

        jTabVentPisoPays.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
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
        jTabVentPisoPays.setRowHeight(30);
        jScrollPane21.setViewportView(jTabVentPisoPays);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabPed4, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(164, 164, 164))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(184, 184, 184)
                        .addComponent(jLabPed3, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(214, 214, 214)
                        .addComponent(jLabPed5, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(164, 277, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane21, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane30, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane28, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabPed3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane28, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jLabPed5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane30, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabPed4, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane21, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(47, Short.MAX_VALUE))
        );

        jLayeredPane2.setLayer(jPanel2, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jLayeredPane2Layout = new javax.swing.GroupLayout(jLayeredPane2);
        jLayeredPane2.setLayout(jLayeredPane2Layout);
        jLayeredPane2Layout.setHorizontalGroup(
            jLayeredPane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jLayeredPane2Layout.setVerticalGroup(
            jLayeredPane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jLayeredPane2Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );

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

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "SALIDAS", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 13))); // NOI18N

        jLabPed1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabPed1.setForeground(new java.awt.Color(0, 51, 102));
        jLabPed1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabPed1.setText("PAGO DE FLETES");

        jTabPaysFletesDia.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jTabPaysFletesDia.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTabPaysFletesDia.setRowHeight(30);
        jScrollPane29.setViewportView(jTabPaysFletesDia);

        jLabPed6.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabPed6.setForeground(new java.awt.Color(0, 51, 102));
        jLabPed6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabPed6.setText("PAGO DE COMPRA A PROVEEDOR");

        jTabPaysCompraProovedor.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jTabPaysCompraProovedor.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTabPaysCompraProovedor.setRowHeight(30);
        jScrollPane31.setViewportView(jTabPaysCompraProovedor);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(jScrollPane29, javax.swing.GroupLayout.DEFAULT_SIZE, 588, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(jScrollPane31))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(189, 189, 189)
                        .addComponent(jLabPed1, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(180, 180, 180)
                        .addComponent(jLabPed6, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(24, 24, 24))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabPed1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane29, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabPed6, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane31, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanPagosLayout = new javax.swing.GroupLayout(jPanPagos);
        jPanPagos.setLayout(jPanPagosLayout);
        jPanPagosLayout.setHorizontalGroup(
            jPanPagosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanPagosLayout.createSequentialGroup()
                .addGap(110, 110, 110)
                .addComponent(jLabel100, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jDFechPays, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(jButFleteGuardar1, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanPagosLayout.createSequentialGroup()
                .addComponent(jLayeredPane2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(178, 178, 178))
        );
        jPanPagosLayout.setVerticalGroup(
            jPanPagosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanPagosLayout.createSequentialGroup()
                .addGroup(jPanPagosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButFleteGuardar1)
                    .addGroup(jPanPagosLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addGroup(jPanPagosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel100, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jDFechPays, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(9, 9, 9)
                .addGroup(jPanPagosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLayeredPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
                .addComponent(paneAltas, javax.swing.GroupLayout.PREFERRED_SIZE, 880, javax.swing.GroupLayout.PREFERRED_SIZE)
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
            cargaComprasDia(fn.getFecha(jDateFechCompraProv));
            cargaTotCompDayProveedor(fn.getFecha(jDateFechCompraProv));
            
            jLabLetreroFletes.setVisible(false);

            DefaultTableModel dtmDel = (DefaultTableModel) jTabDetallecompraAll.getModel();
            int fil = dtmDel.getRowCount();
            if (fil > 0) {
                for (int i = 0; i < fil; i++) {
                    dtmDel.removeRow(0);
                }
            }
        }
    }//GEN-LAST:event_jRadBCompraProveActionPerformed

    private void jMnRealPayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMnRealPayActionPerformed
        int fila = jTablefiltrosBusq.getSelectedRow();
        String val = "", name = "", tot = "";
        if (fila == -1) {
            JOptionPane.showMessageDialog(null, "Debe elegir una fila.");
        } else {
            val = jTablefiltrosBusq.getValueAt(fila, 0).toString();
            name = jTablefiltrosBusq.getValueAt(fila, 4).toString();
            tot = jTablefiltrosBusq.getValueAt(fila, 6).toString();
            System.out.println(val);
            vP = new VentaPiso(tot, val, "pagopedidocli");
            vP.setVisible(true);
            vP.setEnabled(true);
            vP.validate();
            vP.jLabLetreroTransac.setText("Pago del pedido No.");
            vP.txtidComp.setText(val);
            vP.jLabNameProv.setText("Cliente: ");
            vP.txtProveedorName.setText(name);
        }
    }//GEN-LAST:event_jMnRealPayActionPerformed

    private void jRadBPrestamoProvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadBPrestamoProvActionPerformed
        if (jRadBPrestamoProv.isSelected()) {
            jPanCompraProoved.setVisible(false);
            jPanPrestamoProovedor.setVisible(true);
            jPanBusquedaPrest.setVisible(false);

            llenacomboProovedores();
            llenacomboProdPrest();
            String datePed = fn.getFecha(jDatFechaPrest);

            String[][] mat = controlInserts.matrizPrestaProv(datePed);
            jTabVistaPresta.setModel(new TModel(mat, cabPrest));
        }
    }//GEN-LAST:event_jRadBPrestamoProvActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        int opc = jComBPrestamosProv.getSelectedIndex(),//index de proveedor
                codP = jComBProveedor.getSelectedIndex();//index de producto
        importes.add(txtImportPres.getText());

        String id_cli = idProoved.get(opc),//idProveedor devuelto de la base de datos
                codPro = idProdPrest.get(codP),//idProducto devuelto de la base de datos
                costoProd = txtPrecProov.getText(),
                cantidad = txtCantPres.getText(),//indexCliente
                nota = textANotaPrestProv.getText(),
                importe = txtImportPres.getText(),
                datePed = fn.getFecha(jDatFechaPrest);
        //  System.out.println("IdProveedor: " + id_cli + "\t idProd: " + codPro + "Fecha: " + datePed);

        List<String> dataPrestamo = new ArrayList<String>();
        if (cantidad.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar una cantidad");
        } else {
            dtm = (DefaultTableModel) jTabPrestamoProovedores.getModel();
            dtmPrec = (DefaultTableModel) jTabPreciosPrest.getModel();
            int filas = dtm.getRowCount(), filasPrec = dtmPrec.getRowCount();
            int column = dtm.getColumnCount(), column2 = dtmPrec.getColumnCount();
            //JOptionPane.showMessageDialog(null, "filas= "+filas+"\n Col= "+column);
            if (filas == 0) {
                dtm.setRowCount(1);
                dtmPrec.setRowCount(1);

                jTabPrestamoProovedores.setModel(dtm);
                jTabPreciosPrest.setModel(dtmPrec);

                jTabPrestamoProovedores.setValueAt(cantidad, 0, Integer.parseInt(codPro) - 8);
                jTabPreciosPrest.setValueAt(costoProd, 0, Integer.parseInt(codPro) - 8);

//id_ProveedorF,fechaPrestamo,status,notaPrest
                dataPrestamo.add(id_cli);
                dataPrestamo.add(datePed);
                dataPrestamo.add("0");
                if (nota.isEmpty()) {
                    dataPrestamo.add("/");
                } else {
                    dataPrestamo.add(nota);
                }

                controlInserts.guardaPrestamoProv(dataPrestamo);
                limpiaPrestamoProv();
                jComBPrestamosProv.setEnabled(false);
                textANotaPrestProv.setEnabled(false);
            } else {
                jTabPrestamoProovedores.setValueAt(cantidad, 0, Integer.parseInt(codPro) - 8);
                jTabPreciosPrest.setValueAt(costoProd, 0, Integer.parseInt(codPro) - 8);
                limpiaPrestamoProv();
            }
        }//else vacio
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
        System.out.println(key);
    }//GEN-LAST:event_jPanVentasPisoKeyReleased

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        String arr[] = controlInserts.ultimoRegistroVentPiso();
        System.out.print(arr[0] + " butGuarda " + arr[1]);
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
            System.out.println(val);
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

        String[][] mat = controlInserts.matrizPedidos(datePed);
        jTabPedidosDiaView.setModel(new TModel(mat, cab));
        jPanConsulPed.setVisible(false);
        jPanCreaPedido.setVisible(true);
    }//GEN-LAST:event_jRadioCreaPedidoActionPerformed

    private void jCombPedidoClientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCombPedidoClientActionPerformed
        int opc = jCombPedidoClient.getSelectedIndex();
        dtm = (DefaultTableModel) jTableCreaPedidos.getModel();
        int filas = dtm.getRowCount();
        int column = dtm.getColumnCount();
        if (filas > 0) {
            dtm.removeRow(0);
            jTableCreaPedidos.setModel(dtm);
        }

    }//GEN-LAST:event_jCombPedidoClientActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        int opc = jCombPedidoClient.getSelectedIndex(),//index de cliente
                codP = jCombProdPedidos.getSelectedIndex();//index de producto

        String id_cli = conten.get(opc),//idCliente devuelto de la base de datos
                codPro = idProducts.get(codP),//idProducto devuelto de la base de datos
                cantidad = txtCantCreaPedido.getText(),//indexCliente
                nota = txtNotePedidoCli.getText(),
                datePed = fn.getFecha(jDateCHPedido);
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

            String[][] mat = controlInserts.matrizPedidos(datePed);
            jTabPedidosDiaView.setModel(new TModel(mat, cab));

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
        String id_cli = conten.get(opc);//idCliente devuelto de la base de datos
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

    private void txtPrecProovFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPrecProovFocusLost
        String cant = txtCantPres.getText(),
                cos = txtPrecProov.getText();
        txtImportPres.setText("");

        if (cant.isEmpty() || cos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Campo cantidad o monto vacios\n Verifique Por favor");
        } else {
            BigDecimal amountOne = new BigDecimal(cant);//monto a cobrar
            BigDecimal amountTwo = new BigDecimal(cos);//cantidad recivida
            txtImportPres.setText(fn.multiplicaAmount(amountOne, amountTwo).toString());
        }
    }//GEN-LAST:event_txtPrecProovFocusLost

    private void jComBProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComBProveedorActionPerformed
        int var = jComBProveedor.getSelectedIndex();
        if (var == 0) {
            jLabBNumerador.setText("CAJAS");
        }
        if (var == 1) {
            jLabBNumerador.setText("M.N.");
        }
        if (var == 2) {
            jLabBNumerador.setText("PACAS");
        }
        if (var == 3) {
            jLabBNumerador.setText("LIBRA(S)");
        }
    }//GEN-LAST:event_jComBProveedorActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        dtm = (DefaultTableModel) jTabPrestamoProovedores.getModel();
        dtmPrec = (DefaultTableModel) jTabPreciosPrest.getModel();//tabña de precios

        int filas = dtm.getRowCount();
        int column = dtm.getColumnCount();
        if (filas > 0) {
            String[] ultimo = controlInserts.ultimoRegistroPrestamo();
            Object val = null;

            int opc = jComBPrestamosProv.getSelectedIndex(),//index de PROVEEDOR
                    codP = jComBProveedor.getSelectedIndex();//index de producto

            String id_cli = idProoved.get(opc);
            String cantidad = txtCantPres.getText(), datePed = fn.getFecha(jDatFechaPrest);

            //System.out.println("id_cli:"+id_cli+"\n id ped: "+ultimo[1]);
            //guardaDetallePrestamoProv(String numCP,String codP, int cantP,String costP)          
            for (int i = 0; i < column; i++) {
                val = jTabPrestamoProovedores.getValueAt(0, i);
                if (val != null) {
                    controlInserts.guardaDetallePrestamoProv(ultimo[0], Integer.toString(i + 8), Integer.parseInt(jTabPrestamoProovedores.getValueAt(0, i).toString()), jTabPreciosPrest.getValueAt(0, i).toString());
                    //System.out.println("pos= "+(i+1)+" "+jTableCreaPedidos.getColumnName(i)+"\t-> val= "+jTableCreaPedidos.getValueAt(0, i).toString());
                }//if null
            }

            String[][] mat = controlInserts.matrizPrestaProv(datePed);
            jTabVistaPresta.setModel(new TModel(mat, cabPrest));
            importes.clear();
        } else {
            JOptionPane.showMessageDialog(null, "No ha generado prestamo para el proveedor: " + jComBPrestamosProv.getSelectedItem().toString());
        }
        jComBPrestamosProv.setEnabled(true);
        textANotaPrestProv.setEnabled(true);
        //  ListIterator<String> itrI = importes.listIterator();
        //  while (itrI.hasNext()) {
        // System.out.println(itrI.next());
        //  }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jComBPrestamosProvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComBPrestamosProvActionPerformed
        int opc = jComBPrestamosProv.getSelectedIndex();
        dtm = (DefaultTableModel) jTabPrestamoProovedores.getModel();
        dtmPrec = (DefaultTableModel) jTabPreciosPrest.getModel();

        int filas = dtm.getRowCount();
        int column = dtm.getColumnCount();
        if (filas > 0) {
            dtm.removeRow(0);
            dtmPrec.removeRow(0);
            jTableCreaPedidos.setModel(dtm);
            jTabPreciosPrest.setModel(dtmPrec);
        }
    }//GEN-LAST:event_jComBPrestamosProvActionPerformed

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
        String id_cli = idProoved.get(opc);//idCliente devuelto de la base de datos
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

    private void jTabPedidosDiaViewMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTabPedidosDiaViewMousePressed
        if (evt.getClickCount() > 1) {
            int fila = jTabPedidosDiaView.getSelectedRow();
            String val = jTabPedidosDiaView.getValueAt(fila, 0).toString();
            if (val.equals("NO DATA")) {
                JOptionPane.showMessageDialog(null, "No hay datos para mostrar.");
            } else {
                List<String> detailPedidoCli = new ArrayList<String>();
                for (int j = 0; j < jTabPedidosDiaView.getColumnCount(); j++) {
                    detailPedidoCli.add(jTabPedidosDiaView.getValueAt(fila, j).toString());
                }
                dP = new detailPedido(Integer.parseInt(val));

                dP.setEnabled(true);
                dP.setVisible(true);
                dP.validate();
                dP.recibeListData(detailPedidoCli);
            }
        }        // TODO add your handling code here:
    }//GEN-LAST:event_jTabPedidosDiaViewMousePressed

    private void jTabVistaPrestaMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTabVistaPrestaMousePressed
        if (evt.getClickCount() > 1) {
            int fila = jTabVistaPresta.getSelectedRow();
            String val = jTabVistaPresta.getValueAt(fila, 0).toString();
            System.out.println("Envia: " + val);

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
        
        String id_cli = idProoved.get(opc),//idProveedor devuelto de la base de datos
                codPro = idProducts.get(codP),//idProducto devuelto de la base de datos
                costoProd = txtPrecCompraProv.getText(),
                cantidad = txtCantidadCompra.getText(),//indexCliente
                nota = txtNotaCompra.getText(),
                importe = txtImportComp.getText(),
                descriSubasta = txtCamSubastaCompra.getText(),
                datePed = fn.getFecha(jDateFechCompraProv);

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
                controlInserts.guardaCompraProv(dataCompra);//*********************************** COMPRA A MAYORISTAS
              ultimo = controlInserts.ultimoRegistroCompra();
                    dtmAux.setRowCount(1);//agrega una fila 
                    jTabDetallecompraAll.setValueAt(ultimo[0], 0, 0);//codigo de producto
                    jTabDetallecompraAll.setValueAt(codPro, 0, 1);//id de ultima compra guardada
                    jTabDetallecompraAll.setValueAt(jCombProductProv.getSelectedItem().toString(), 0, 2);//nombre de tipo mercancia
                    jTabDetallecompraAll.setValueAt(cantidad, 0, 3);//tipo de mercancia
                    jTabDetallecompraAll.setValueAt(costoProd, 0, 4);//costo de mercancia unitaria
                    jTabDetallecompraAll.setValueAt(importe, 0, 5);//importe de mercancia
                if (jCheckBox1.isSelected()) {//preparamos campo para compra a mayorista
                    controlInserts.guardaMayorista(id_cli, ultimo[0], datePed);//Asignamos compra a mayoristaRelacion
                }
                jCElijaProovedor.setEnabled(false);
                txtNotaCompra.setEnabled(false);
            } else {//si ya tiene una fila almenos la vista de compra a proveedor normal
                ultimo = null;
                    ultimo = controlInserts.ultimoRegistroCompra();
                    dtmAux.addRow(new Object[]{ultimo[0], codPro, jCombProductProv.getSelectedItem().toString(), cantidad, costoProd, importe});
                     jTabDetallecompraAll.setModel(dtmAux);
               
            }
        }//else vacio
        importes.add(txtImportComp.getText());
        limpiacompraProved();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void txtPrecCompraProvFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPrecCompraProvFocusLost
        String cant = txtCantidadCompra.getText(),
                cos = txtPrecCompraProv.getText();
        txtImportComp.setText("");

        if (cant.isEmpty() || cos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Campo cantidad o monto vacios\n Verifique Por favor");
        } else {
            BigDecimal amountOne = new BigDecimal(cant);//monto a cobrar
            BigDecimal amountTwo = new BigDecimal(cos);//cantidad recivida
            txtImportComp.setText(fn.multiplicaAmount(amountOne, amountTwo).toString());
        }
    }//GEN-LAST:event_txtPrecCompraProvFocusLost

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        dtmAux = (DefaultTableModel) jTabDetallecompraAll.getModel();
        List<String> dataCompra = new ArrayList<String>();//lista para obtener los datos a insertar por cada row de jTable

        int filas = dtmAux.getRowCount();
        int column = dtmAux.getColumnCount();
        String cantidad = txtCantPres.getText(), datePed = fn.getFecha(jDateFechCompraProv);

        if (filas > 0) {
            String[] ultimo = controlInserts.ultimoRegistroCompra();//probar a cambiar guardar ID de compra para no hacer tantas consultas a la DB
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
                        dataCompra.clear();
                    }
        } else {
            JOptionPane.showMessageDialog(null, "No ha generado compra para el proveedor: " + jCElijaProovedor.getSelectedItem().toString());
        }
        
        cargaComprasDia(datePed);//carga las compras del dia
        cargaTotCompDayProveedor(datePed);//suma de tipos de 
        
       
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
        String id_cli = idProoved.get(opc);//idCliente devuelto de la base de datos
        String[][] mat = null;

        switch (elije) {
            case 0://CLIENTE
                mat = controlInserts.matrizCompraProvOpc(elije, id_cli, "", "");
                jTabBusqCompraProv1.setModel(new TModel(mat, cabCompra));
                break;
            case 1://MAYORISTA
                System.out.println("prov May: " + id_cli);
                mat = controlInserts.matrizCompraProvOpc(elije, id_cli, "", "");
                jTabBusqCompraProv1.setModel(new TModel(mat, cabCompra));
                break;
            case 2: //MAYORISTA+FECHA
                System.out.println("prov May: " + id_cli);
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
    }//GEN-LAST:event_jButton14ActionPerformed

    private void jMPAYFILTROActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMPAYFILTROActionPerformed
        int fila = jTabBusqCompraProv1.getSelectedRow();
        String val = "", name = "";
        if (fila == -1) {
            JOptionPane.showMessageDialog(null, "Debe elegir una fila.");
        } else {
            val = jTabBusqCompraProv1.getValueAt(fila, 0).toString();
            name = jTabBusqCompraProv1.getValueAt(fila, 4).toString();
            System.out.println(val);
            vP = new VentaPiso(controlInserts.totalCompraProv(val), val, "pagarcompraprovee");
            vP.setVisible(true);
            vP.setEnabled(true);
            vP.validate();
            vP.jLabLetreroTransac.setText("Pago de compra No.");
            vP.txtidComp.setText(val);
            vP.jLabNameProv.setText("Proveedor: ");
            vP.txtProveedorName.setText(name);
        }

    }//GEN-LAST:event_jMPAYFILTROActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        llenacomboProducts();
        llenacomboProovedores();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jCElijaProovedorFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jCElijaProovedorFocusLost
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
        } else {
            jPanClientOption.setVisible(false);
            jPanSubastaOption.setVisible(false);
        }

    }//GEN-LAST:event_jCElijaProovedorFocusLost

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

        // System.out.println("IdCliente: " + id_cli + "\t idProd: " + codPro + "Fecha: " + dateVentP);
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
        String id_cli = conten.get(opc);//idCliente devuelto de la base de datos
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
                mat = controlInserts.matrizPedidosB(elije, id_cli, fech1, fech2);
                jTablefiltrosBusqVent.setModel(new TModel(mat, cabvENTAp));
                break;
        };

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
        }

    }//GEN-LAST:event_jMnPayVentaPActionPerformed

    private void jCBAltasFletesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBAltasFletesActionPerformed
        // TODO add your handling code here:
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
                var7 = contenFletes.get(var);
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
                var7 = contenFletes.get(var);
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

            controlInserts.actualizaFlete(contentL, var1);
            mostrarTablaFletesDia(var6);
            limpiaFlete();
            jButFleteGuardar.setEnabled(true);
        }//if vacio   
        jLabLetreroFletes.setVisible(false);
    }//GEN-LAST:event_jButAltasActualiza1ActionPerformed

    private void jRPagadopFleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRPagadopFleteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jRPagadopFleteActionPerformed

    private void jRdCreaFletesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRdCreaFletesActionPerformed
        jPanCreaFletes.setVisible(true);
        jPanHistorFletes.setVisible(false);
        llenacomboFletes();
        jDFechCreaFlete.setDate(cargafecha());
        mostrarTablaFletesDia(fn.getFecha(jDFechCreaFlete));
        jRPendFlete.setSelected(true);
    }//GEN-LAST:event_jRdCreaFletesActionPerformed

    private void jTabFletesDiaMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTabFletesDiaMousePressed
        if (evt.getClickCount() > 1) {
            jButFleteGuardar.setEnabled(false);
            int fila = jTabFletesDia.getSelectedRow();
            // String val = jTabFletesDia.getValueAt(fila, 0).toString();

            txtFolioFlete.setText(jTabFletesDia.getValueAt(fila, 0).toString());
            jCBAltasFletes.setSelectedItem(jTabFletesDia.getValueAt(fila, 1).toString());
            jDFechCreaFlete.setDate(fn.StringDate(jTabFletesDia.getValueAt(fila, 2).toString()));
            txtChoferFlete.setText(jTabFletesDia.getValueAt(fila, 3).toString());
            txtUnidFlete.setText(jTabFletesDia.getValueAt(fila, 4).toString());
            txtCostoFlete.setText(jTabFletesDia.getValueAt(fila, 5).toString());
            if (jTabFletesDia.getValueAt(fila, 6).toString().equals("PENDIENTE")) {
                jRPendFlete.setSelected(true);
            } else if (jTabFletesDia.getValueAt(fila, 6).toString().equals("PAGADO")) {
                jRPagadopFlete.setSelected(true);
            }
            txtNotaFlete.setText(jTabFletesDia.getValueAt(fila, 7).toString());

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
        String foli = "", id_cli = "";
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
        //  System.out.println("OPCION: "+elije);
        switch (elije) {
            case 0:
                //System.out.println("IdCli: " + id_cli);
                mat = controlInserts.matrizFletesFilter(elije, id_cli, "", "");
                jTablefiltrosBusqflete.setModel(new TModel(mat, cabFilterFlete));
                break;
            case 1:
                //System.out.println("FOLIO: " + foli);
                mat = controlInserts.matrizFletesFilter(elije, foli, "", "");
                jTablefiltrosBusqflete.setModel(new TModel(mat, cabFilterFlete));
                break;
            case 2:
                //System.out.println("Fecha1: " + fech1);
                mat = controlInserts.matrizFletesFilter(elije, "", fech1, "");
                jTablefiltrosBusqflete.setModel(new TModel(mat, cabFilterFlete));
                break;
            case 3:
                //System.out.println("fech1: " + fech1 + "Fecha2: " + fech2);
                mat = controlInserts.matrizFletesFilter(elije, "", fech1, fech2);
                jTablefiltrosBusqflete.setModel(new TModel(mat, cabFilterFlete));
                break;
            case 4:
                //System.out.println("idCli: " + id_cli + "Fecha1: " + fech1);
                mat = controlInserts.matrizFletesFilter(elije, id_cli, fech1, "");
                jTablefiltrosBusqflete.setModel(new TModel(mat, cabFilterFlete));
                break;
            case 5:
                //System.out.println("idCli: " + id_cli + "Fecha1: " + fech1 + "Fecha2: " + fech2);
                mat = controlInserts.matrizFletesFilter(elije, id_cli, fech1, fech2);
                jTablefiltrosBusqflete.setModel(new TModel(mat, cabFilterFlete));
                break;
        };
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jCfleteroOpcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCfleteroOpcActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCfleteroOpcActionPerformed

    private void jCombOpcBusqFletesFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jCombOpcBusqFletesFocusLost
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
    }//GEN-LAST:event_jCombOpcBusqFletesFocusLost

    private void jTabFletesDia1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTabFletesDia1MousePressed
        // TODO add your handling code here:
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
        cargaComprasDiaAsign(fechAs);//carga las compras del dia 
        cargaPedidosDiaAsign(fechAs);
        mostrarTablaFletesDiaAsign(fechAs);
        String[][] mat = controlInserts.matFletEstados(fechAs);
        jTDetailAsign.setModel(new TModel(mat, cabEdoPed));
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
            txtFolioFleteAsign.setText(idFlet);
            
            jFramElijeAsignCompras.setLocationRelativeTo(this);
            jFramElijeAsignCompras.setVisible(true);
            jFramElijeAsignCompras.setEnabled(true);
            jLabNumcompra.setText(idComp);
            jLabNumcompra1.setText(idPed);
            jLabNumcompra2.setText(idFlet);
            //jFramePays.setTitle("Areas");
           // String total = txtResultSum.getText();//es el total
             matViewRep = controlInserts.regresacompMayorisa(idComp,"");
             jTabAsigaDinamicoCompra.setModel(new TModel(matViewRep, cabMayViewcHECK));
             addCheckBox(7,jTabAsigaDinamicoCompra);//agrega el checkBox a la tabla en la columna 6
             
             rellenaAsignaTabla();
//columna 0 = Numero de compra
/*             jTabAsigaDinamicoCompra.getColumnModel().getColumn(0).setMaxWidth(0);
             jTabAsigaDinamicoCompra.getColumnModel().getColumn(0).setMinWidth(0);
             jTabAsigaDinamicoCompra.getColumnModel().getColumn(0).setPreferredWidth(0);
*/             
             
             
            cargaDetailcompAsig(idComp);

        } else {
            JOptionPane.showMessageDialog(null, "Debe elegir una fila de cada tabla.");
        }
    }//GEN-LAST:event_jButton17ActionPerformed

    private void jButGuardDetailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButGuardDetailActionPerformed
        List<String> datos = new ArrayList<String>();
        int numfil = jTabDetailAsign.getRowCount(), numcol = jTabDetailAsign.getColumnCount(), contadorR = 0, contadorB;
        Object var = "";
        for (int i = 0; i < numfil; i++) {
            System.out.print(i);
            for (int j = 0; j < numcol; j++) {
                var = jTabDetailAsign.getValueAt(i, j);
                if (var != null && !var.toString().isEmpty()) {
                    contadorR++;
                    if (j == 0) {
                        datos.add(txtCompraAsign.getText());
                        datos.add(txtidPedidoAsign.getText());
                        datos.add(jTabDetailAsign.getValueAt(i, j).toString());
                        datos.add(Integer.toString(i + 1));
                        datos.add(txtFolioFleteAsign.getText());
                        datos.add(jTabDetailAsign.getValueAt(i, j + 1).toString());
                        controlInserts.guardaRelOperaciones(datos);
                    }
                }
            }
            System.out.println();
        }
        limpiaAsign();
        jButton18.doClick();
    }//GEN-LAST:event_jButGuardDetailActionPerformed

    private void jButFleteGuardar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButFleteGuardar1ActionPerformed
        String fech = fn.getFecha(jDFechPays);
        llenaTabPayDayPedidos(fech);
    }//GEN-LAST:event_jButFleteGuardar1ActionPerformed

    private void jTabVistaPedidosDia1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTabVistaPedidosDia1FocusLost
        int var = jTabVistaPedidosDia1.getSelectedRow(), col = jTabVistaPedidosDia1.getColumnCount(), difer = 0;

        String id_Busq = jTabVistaPedidosDia1.getValueAt(var, 0).toString();

        Object val2 = null, totSum = null;
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 2; j++) {
                jTabDetailAsignTotales.setValueAt("", i, j);
            }
        }
        //  String vari = jTabVistaPedidosDia1.getValueAt(var, 0).toString();
        for (int i = 2; i < col - 1; i++) {
            val2 = jTabVistaPedidosDia1.getValueAt(var, i);
            if (val2 != null && !val2.toString().isEmpty()) {
                //controlInserts.guardaDetallePedidoCli(ultimo[0], Integer.toString(i + 1), Integer.parseInt(jTableCreaPedidos.getValueAt(0, i).toString()));
                //System.out.print("prodComp= "+(i)+" "+jTabVistaPedidosDia1.getColumnName(i)+"\t-> val= "+jTabVistaPedidosDia1.getValueAt(var, i).toString());
                // System.out.println("envia: "+id_Busq+" -> "+(i-1));
                totSum = controlInserts.calcAsignAPed(id_Busq, Integer.toString(i - 1), "id_pedidoCli");
                // System.out.println("regresa : "+totSum);

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

    }//GEN-LAST:event_jTabVistaPedidosDia1FocusLost

    private void jTabVistaComprasDia3FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTabVistaComprasDia3FocusLost
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
        for (int i = 2; i < col - 1; i++) {
            val = jTabVistaComprasDia3.getValueAt(var, i);
            val2 = jTabVistaPedidosDia1.getValueAt(var2, i);
            if (val != null && val2 != null && !val.toString().isEmpty() && !val2.toString().isEmpty()) {
                //controlInserts.guardaDetallePedidoCli(ultimo[0], Integer.toString(i + 1), Integer.parseInt(jTableCreaPedidos.getValueAt(0, i).toString()));
                //    System.out.print("prodComp= "+(i)+" "+jTabVistaComprasDia3.getColumnName(i)+"\t-> val= "+jTabVistaComprasDia3.getValueAt(var, i).toString());
                bandera = true;
                //      System.out.print("/t prodPed= "+(i)+" "+jTabVistaPedidosDia1.getColumnName(i)+"\t-> val= "+jTabVistaPedidosDia1.getValueAt(var2, i).toString());
            }//if null
        }
        if (!bandera) {
            JOptionPane.showMessageDialog(null, "Mercancia no es del mismo tipo del pedido seleccionado; \n Verfique por favor.");
            jButton18.doClick();//para des seleccionar las filas de todas las tablas
        } else {
            String id_Busq = jTabVistaComprasDia3.getValueAt(var, 0).toString();
            for (int i = 2; i < col - 1; i++) {
                val2 = jTabVistaComprasDia3.getValueAt(var, i);
                if(val2 != null && !val2.toString().isEmpty()) {
                    //controlInserts.guardaDetallePedidoCli(ultimo[0], Integer.toString(i + 1), Integer.parseInt(jTableCreaPedidos.getValueAt(0, i).toString()));
                    //System.out.print("prodComp= "+(i)+" "+jTabVistaPedidosDia1.getColumnName(i)+"\t-> val= "+jTabVistaPedidosDia1.getValueAt(var, i).toString());
                    // System.out.println("CP envia: "+id_Busq+" -> "+(i-1));
                    totSum = controlInserts.calcAsignAPed(id_Busq, Integer.toString(i - 1), "id_compraProveed");
                    //System.out.println("CP regresa : "+totSum);
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
    }//GEN-LAST:event_jTabVistaComprasDia3FocusLost

    private void jMItDetailVerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMItDetailVerActionPerformed
        int opc = jTabVistaPedidosDia1.getSelectedRow();
        if (opc > -1) {
            String param = jTabVistaPedidosDia1.getValueAt(opc, 0).toString(), nombre = jTabVistaPedidosDia1.getValueAt(opc, 1).toString();
            fP = new FormacionPedido(Integer.parseInt(param), nombre);
            fP.setEnabled(true);
            fP.setVisible(true);
            fP.validate();
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
            if (val.equals("NO DATA")) {
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

                mat = controlInserts.regresacompMayorisa(val, fech);
                jTabDetallecompraAll.setModel(new TModel(mat, cabMayView));

                jTextField1.setText(controlInserts.sumMayorista(val, fech));
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
            //System.out.println(val);
            vP = new VentaPiso(costo, val, "pagoflete");
            vP.setVisible(true);
            vP.setEnabled(true);
            vP.validate();
            vP.jLabLetreroTransac.setText("Pago de Flete No.");
            vP.txtidComp.setText(val);
            vP.jLabNameProv.setText("Fletero: ");
            vP.txtProveedorName.setText(name);
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
            //System.out.println(val);
            vP = new VentaPiso(costo, val, "pagoflete");
            vP.setVisible(true);
            vP.setEnabled(true);
            vP.validate();
            vP.jLabLetreroTransac.setText("Pago de Flete No.");
            vP.txtidComp.setText(val);
            vP.jLabNameProv.setText("Fletero: ");
            vP.txtProveedorName.setText(name);
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
        String datePed = fn.getFecha(jDateFechCompraProv);
        int opc = jCElijaProovedor.getSelectedIndex(),
                id_comp = jTabVistaComprasDia.getSelectedRow();

        if (id_comp > -1) {
            String id_cli = idProoved.get(opc),
                    guardacom = jTabVistaComprasDia.getValueAt(id_comp, 0).toString(),
                    importe = jTabVistaComprasDia.getValueAt(id_comp, 10).toString();

            if (controlInserts.validaIsMayorista(id_cli)) {
                if (controlInserts.validaCompAsignadas(guardacom, datePed, "comp+fech")) {
                    JOptionPane.showMessageDialog(null, "Compra ya ha sido asignada");
                } 
            } else {
                JOptionPane.showMessageDialog(null, "Solo puede asignar compras a Proveedor Mayorista");
            }//else valida si es mayorista
        } else {
            JOptionPane.showMessageDialog(null, "Debe elegir una compra de proveedor");
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

    private void jCElijaProovedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCElijaProovedorActionPerformed
        int tam = jCElijaProovedor.getItemCount();
        //validamos si se ha seleccionado checkMayoristas y si existe algun elemnto en el MenuItem
        if (jCheckBox1.isSelected() && tam > 0) {
            String datePed = fn.getFecha(jDateFechCompraProv);
            int opc = jCElijaProovedor.getSelectedIndex();
            String id_cli = idProoved.get(opc);
          //  String[][] mat = controlInserts.matAsignaMayoristas(id_cli, datePed);
           // jTabMayorAsignados.setModel(new TModel(mat, cabMayAsignados));
            jTextField1.setText(controlInserts.sumMayorista(id_cli, datePed));

           /*
            jTabMayorAsignados.getColumnModel().getColumn(0).setMaxWidth(0);
            jTabMayorAsignados.getColumnModel().getColumn(0).setMinWidth(0);
            jTabMayorAsignados.getColumnModel().getColumn(0).setPreferredWidth(0);
        */
            }

    }//GEN-LAST:event_jCElijaProovedorActionPerformed

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
    }//GEN-LAST:event_txtImportCompKeyReleased

    private void txtNotaCompraKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNotaCompraKeyReleased
        if (evt.getKeyCode() == evt.VK_ENTER) {
            jButton1.doClick();
        }        // TODO add your handling code here:
    }//GEN-LAST:event_txtNotaCompraKeyReleased

    private void txtImportPresKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtImportPresKeyPressed
        if (evt.getKeyCode() == evt.VK_ENTER) {
            jButton7.doClick();
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
            File backupFile = new File(controlInserts.cargaConfig() + "autoGenerate" + fn.setDateActualGuion() + ".sql");
            InputStreamReader irs;
            BufferedReader br;
            try (FileWriter fw = new FileWriter(backupFile)) {
                Process child = runtime.exec("C:\\xampp\\mysql\\bin\\mysqldump --routines=TRUE --password=0ehn4TNU5I --user=root --databases admindcr");// | gzip> respadmin_DCR.sql.gz
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
            if(Integer.parseInt(idelim) > 7){
                controlInserts.elimaRow("detailcompraprooved", "num_DCompraP", idelim);     
                limpiaDetailMayorista();
                jTextField1.setText("");
            }else{
               
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
       Object var = jTabAsigaDinamicoCompra.getValueAt(elig,7); 
//        System.err.println("Valor sin check on = "+var);
       if(var != null && var.equals(true) ){// || !var.toString().isEmpty()
           jTabAsigaDinamicoCompra.setValueAt( jTabAsigaDinamicoCompra.getValueAt(elig,10),elig,8);
           jTabAsigaDinamicoCompra.editCellAt(elig, 8);
           jTabAsigaDinamicoCompra.setSurrendersFocusOnKeystroke(true);
           jTabAsigaDinamicoCompra.getEditorComponent().requestFocus();
       }else{
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
        if(contRows > 0){
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
                    datos.clear();                }
            }//for
            jFramElijeAsignCompras.dispose();
        }else{
            JOptionPane.showMessageDialog(null, "No eligo nada a asignar");
        }
    }//GEN-LAST:event_jButton16ActionPerformed

    private void jButton16KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jButton16KeyReleased

    }//GEN-LAST:event_jButton16KeyReleased

    private void jTabAsigaDinamicoCompraKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTabAsigaDinamicoCompraKeyReleased
        int oprime = evt.getKeyCode(),
              elig = jTabAsigaDinamicoCompra.getSelectedRow();
        String aAsignar = "", disponib="";
        System.err.println("oprimio: "+oprime);
         if(oprime == 27){
             jFramElijeAsignCompras.dispose();
         }
         if(oprime == evt.VK_ENTER)//KeyEvent.VK_ENTER
         {
             aAsignar =  jTabAsigaDinamicoCompra.getValueAt(elig, 8).toString();
             disponib = jTabAsigaDinamicoCompra.getValueAt(elig, 10).toString();
             
             if(Integer.parseInt(disponib) < Integer.parseInt(aAsignar)){
                JOptionPane.showMessageDialog(null, "La cantidad a asignar no puede ser mayor a la cantidad disponible \n Verifique por favor.");
                jTabAsigaDinamicoCompra.setValueAt(null, elig, 8);
                jTabAsigaDinamicoCompra.setValueAt(null, elig, 7);
                jTabAsigaDinamicoCompra.revalidate();
             }
             
         }
    }//GEN-LAST:event_jTabAsigaDinamicoCompraKeyReleased

    public Image getIconImage() {
        Image retValue = Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("image/icons8_customer_32px_1.png"));
        return retValue;
    }

    //carga de detalle PedidioAsignacion
    public void cargaDetailcompAsig(String opc) {
        Connection cn = con2.conexion();
        String sql = "";
        sql = "SELECT * FROM detailcompraprooved WHERE id_compraP = '" + opc + "'";
        Statement st = null;
        ResultSet rs = null;
        try {
            st = cn.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {//es necesario el for para llenar dinamicamente la lista, ya que varia el numero de columnas de las tablas
                for (int x = 1; x <= rs.getMetaData().getColumnCount() - 1; x++) {
                    if (x == 3) {
                        jTabDetailAsign.setValueAt(rs.getInt(x + 1), rs.getInt(x) - 1, 0);//valor,fila,columna
                        jTabDetailAsign.setValueAt(rs.getString(x + 2), rs.getInt(x) - 1, 1);//valor,fila,columna restamos uno porque el indice del jtable comienza en cero
                    }
                }//for
            }//while
        } catch (SQLException ex) {
            Logger.getLogger(detailPedido.class.getName()).log(Level.SEVERE, null, ex);
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
        String[][] arre = controlInserts.consultCompra(fech);
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
    protected void cargaTotCompDayProveedor(String fech){
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
                "	AND compraprooved.fechaCompra = '"+fech+"'\n" +
                "INNER JOIN\n" +
                "	productocal\n" +
                "ON\n" +
                "	productocal.codigo = detailcompraprooved.codigoProdC\n" +
                "GROUP BY productocal.codigo;";
        
        sql2 = "SELECT\n" +
                "	SUM(detailcompraprooved.cantCajasC*detailcompraprooved.precCajaC) AS numDinero,\n" +
                "	SUM(detailcompraprooved.cantCajasC) AS numCaja\n" +
                "FROM \n" +
                "	detailcompraprooved\n" +
                "INNER JOIN \n" +
                "	compraprooved\n" +
                "ON\n" +
                "	detailcompraprooved.id_compraP = compraprooved.id_compraProve \n" +
                "	AND compraprooved.fechaCompra = '"+fech+"'\n" +
                "INNER JOIN\n" +
                "	productocal\n" +
                "ON\n" +
                "	productocal.codigo = detailcompraprooved.codigoProdC;";
        Statement st = null;
        ResultSet rs = null;
        try {
            st = cn.createStatement();
            rs = st.executeQuery(sql);
            // System.out.print("Filas: "+cantFilas+"\tColumnas: "+cantColumnas+"\n");
            while (rs.next()) {//es necesario el for para llenar dinamicamente la lista, ya que varia el numero de columnas de las tablas
                for (int x = 1; x <= rs.getMetaData().getColumnCount(); x++) {
                    if(x==1)
                        jTabSumTotales.setValueAt( rs.getInt(x + 1), 0, rs.getInt(x) -1);//se le suma 1 por las columnas id,nombre de la jTable
                }//for
            }//while
            
            st = null;
            rs = null;
            st = cn.createStatement();
            rs = st.executeQuery(sql2);
            while (rs.next()) {
                jTabSumTotales.setValueAt(rs.getString(1), 0, 8);
                jTabSumTotales.setValueAt(rs.getString(2),0,7);
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
    private void cargaComprasDiaAsign(String fech) {
        String[][] arre = controlInserts.consultCompra(fech);
        if (arre.length > 0) {
            jLaComp.setVisible(false);
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
            jLaComp.setVisible(true);
            jLaComp.setText("No hay compras del día");
//           JOptionPane.showMessageDialog(null, "No hay compras del dia");
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
          /*
        "SELECT compraprooved.id_compraProve,proveedor.nombreP,SUM(detailcompraprooved.cantCajasC)\n"
                + "FROM\n"
                + "	proveedor\n"
                + "INNER JOIN\n"
                + "	compraprooved \n"
                + "ON\n"
                + "	compraprooved.id_ProveedorC = proveedor.id_Proveedor AND compraprooved.id_compraProve = '" + opc + "'\n"
                + "INNER JOIN \n"
                + "	detailcompraprooved\n"
                + "ON\n"
                + "	detailcompraprooved.id_compraP = compraprooved.id_compraProve;";
          */
        
        
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
                            //   System.out.println("\tExiste+de 1 vex: "+rs.getInt(x));
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
                    coloreA.add(fila);
                }
                // jTabVistaComprasDia3.setValueAt("ASIGNADO", fila,0);
                jTabVistaComprasDia3.setValueAt(rs.getString(2), fila, 1);
                jTabVistaComprasDia3.setValueAt(rs.getString(3), fila, 9);
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
        String[][] arre = controlInserts.consultPedidoAsign(fech);
        if (arre.length > 0) {
            jLabPed.setVisible(false);
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

            ListIterator<Integer> itr = coloreB.listIterator();
            while (itr.hasNext()) {
                System.out.println("ColoreB -> " + itr.next());
            }
            coloreB.clear();
        } else {
            jLabPed.setVisible(true);
            jLabPed.setText("No hay pedidos del día");

            //JOptionPane.showMessageDialog(null, "No hay compras del dia");
        }
    }//Fin cargaPedidosDia

    public void consultDetailPedidosAsign(int opc, int fila) {
        Connection cn = con2.conexion();
        int cantColumnas = 0, cantFilas = 0, temporal = 0, bandera = 0;
        String sql = "", sql2 = "";
        sql = "SELECT * FROM detailpedidio WHERE id_pedidioD = '" + opc + "'";
        sql2 = "SELECT\n"
                + "	pedidocliente.id_pedido,clientepedidos.nombre,SUM(detailpedidio.cantidadCajas)\n"
                + "	FROM\n"
                + "	clientepedidos\n"
                + "	INNER JOIN\n"
                + "	pedidocliente\n"
                + "	ON\n"
                + " 	pedidocliente.id_clienteP = clientepedidos.id_cliente AND pedidocliente.id_pedido = '" + opc + "'\n"
                + "	INNER JOIN\n"
                + "	detailpedidio\n"
                + "	ON\n"
                + "	detailpedidio.id_PedidioD = pedidocliente.id_pedido;";
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
                    coloreB.add(fila);
                }

                jTabVistaPedidosDia1.setValueAt(rs.getString(2), fila, 1);
                jTabVistaPedidosDia1.setValueAt(rs.getString(3), fila, 9);
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

        jTabFletesDia.setModel(modelo);

        TableColumnModel columnModel = jTabFletesDia.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(15);
        columnModel.getColumn(1).setPreferredWidth(50);

        jTabFletesDia.getColumnModel().getColumn(0).setMaxWidth(100);
        jTabFletesDia.getColumnModel().getColumn(0).setMinWidth(0);
        jTabFletesDia.getColumnModel().getColumn(0).setPreferredWidth(100);

        //   if (var.equals("")) {
        consul = "SELECT fleteEnviado.id_fleteE, fletero.nombreF,fleteEnviado.fechaFlete,fleteEnviado.choferFlete,\n"
                + "	fleteEnviado.trocaFlete,fleteEnviado.costoFlete,fleteEnviado.status,fleteEnviado.recivioFlete\n"
                + "FROM\n"
                + "	fleteEnviado\n"
                + "INNER JOIN\n"
                + "	fletero\n"
                + "ON fletero.id_Fletero = fleteEnviado.id_FleteroE AND fleteEnviado.fechaFlete = '" + fech + "';";
        //  } else {

        //  consul = "SELECT * FROM fleteenviado WHERE fechaFlete = '"+fech+"' LIKE '%" + var + "%' ORDER BY id_fleteE ASC";
//        System.out.println(consul);
        //}
        String datos[] = new String[8];
        List<String> contentL = new ArrayList<String>();
        Statement st = null;
        ResultSet rs = null;
        int i = 1;
        try {
            st = cn.createStatement();
            rs = st.executeQuery(consul);

            while (rs.next()) {
                /*                           for (int x=1;x<= rs.getMetaData().getColumnCount();x++) {
                     //System.out.print(x+" -> "+rs.getString(x));
                     contentL.add(rs.getString(x));
                   }*/
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
    }//termina mostrartablaFletes
//´MOSTRAR TABLA DE FLETES ASIGN DEL DIA

    void mostrarTablaFletesDiaAsign(String fech) {//opc=index-combo,var=campo-busqueda,campo=parametro-nombre
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
        modelo.addColumn("Chofer");
        modelo.addColumn("Unidad");
        modelo.addColumn("Costo");
        modelo.addColumn("Status");
        modelo.addColumn("Nota");

        jTabFletesDia1.setModel(modelo);

        TableColumnModel columnModel = jTabFletesDia1.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(15);
        columnModel.getColumn(1).setPreferredWidth(50);

        jTabFletesDia1.getColumnModel().getColumn(0).setMaxWidth(100);
        jTabFletesDia1.getColumnModel().getColumn(0).setMinWidth(0);
        jTabFletesDia1.getColumnModel().getColumn(0).setPreferredWidth(100);

        //   if (var.equals("")) {
        consul = "SELECT fleteEnviado.id_fleteE, fletero.nombreF,fleteEnviado.choferFlete,\n"
                + "	fleteEnviado.trocaFlete,fleteEnviado.costoFlete,fleteEnviado.status,fleteEnviado.recivioFlete\n"
                + "FROM\n"
                + "	fleteEnviado\n"
                + "INNER JOIN\n"
                + "	fletero\n"
                + "ON fletero.id_Fletero = fleteEnviado.id_FleteroE AND fleteEnviado.fechaFlete = '" + fech + "';";
        //  } else {

        //  consul = "SELECT * FROM fleteenviado WHERE fechaFlete = '"+fech+"' LIKE '%" + var + "%' ORDER BY id_fleteE ASC";
//        System.out.println(consul);
        //}
        String datos[] = new String[7];
        List<String> contentL = new ArrayList<String>();
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
                if (rs.getString(6).equals("0")) {
                    datos[5] = "PENDIENTE";
                } else if (rs.getString(6).equals("1")) {
                    datos[5] = "PAGADO";
                }
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
            jLabFlet.setVisible(false);
            ColorCelda cF = new ColorCelda();
            cF.arrIntRowsIluminados = controlInserts.fnToArray(coloreF);
            jTabFletesDia1.setDefaultRenderer(Object.class, cF);
            coloreF.clear();
        } else {
            jLabFlet.setVisible(true);
            jLabFlet.setText("Sin fletes del día");
//            System.out.println("Nada que colorear");
        }
    }//termina mostrartablaFletesAsign

    //*** CODIGO PARA CARGAR LOS PAGOS DEL DIA
    private void llenaTabPayDayPedidos(String fech) {
        //Llena matriz de pagos pedido clis
        String[][] matFlet = controlInserts.regresaPaysFech("pagopedidocli", fech);
        jTabPayPeds.setModel(new TModel(matFlet, cabPaysDay));
        //Llena matriz de pago de prestamos 
        String[][] mat0 = controlInserts.regresaPaysFech("pagocreditprooved", fech);
        jTabPayPrestamosProv.setModel(new TModel(mat0, cabPaysDay));
        //Llena matriz de pago ventas piso
        String[][] mat = controlInserts.regresaPaysFech("pagoventapiso", fech);
        jTabVentPisoPays.setModel(new TModel(mat, cabPaysDay));
        //Llena matriz de pagos a fleteros
        String[][] mat1 = controlInserts.regresaPaysFech("pagoflete", fech);
        jTabPaysFletesDia.setModel(new TModel(mat1, cabPaysDay));
        //pagos de compras a proveedor
        String[][] mat2 = controlInserts.regresaPaysFech("pagarcompraprovee", fech);
        jTabPaysCompraProovedor.setModel(new TModel(mat2, cabPaysDay));
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
        jLabLetreroFletes.setVisible(true);//
    }

    void limpiaAsign() {
        txtFolioFleteAsign.setText("");
        txtCompraAsign.setText("");
        txtidPedidoAsign.setText("");
//        txtTotCajasAsign.setText("");
//      txtCostoAsign.setText("");
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 2; j++) {
                jTabDetailAsign.setValueAt("", i, j);
                jTabDetailAsignTotales.setValueAt("", i, j);
                jTabDetailAsignTotales1.setValueAt("", i, j);
            }
        }
    }

    public void cargaUser(String param) {
        jTextField17.setText(param);
    }

    public void limpiaDetailMayorista() {
        dtmAux = (DefaultTableModel) jTabDetallecompraAll.getModel();//obtenemos modelo de tablaVista Compra a Mayorista
        int row = dtmAux.getRowCount();
        for (int i = 0; i < row; i++) {
            dtmAux.removeRow(0);
        }
    }
    
    /*Detalle aignacion de compra proveedor mayorista*/
    void addCheckBox(int column, JTable table){
            TableColumn tc = table.getColumnModel().getColumn(column);
            tc.setCellEditor(table.getDefaultEditor(Boolean.class));
            tc.setCellRenderer(table.getDefaultRenderer(Boolean.class));
    }
    
    boolean isSelectedTab(int row, int column, JTable tab){
        return tab.getValueAt(row, column) != null;
    }
    
    //Metodo para analizar si ya ha sido asignado alguna compra a un pedido y mostrar cantidades asignadas
    private void rellenaAsignaTabla(){
        int rowC = jTabAsigaDinamicoCompra.getRowCount(),
                colC = jTabAsigaDinamicoCompra.getColumnCount(),
                sumaProd =-1;
        String var = "",sumDetcompra ="",idProdBusq ="",cantProdt=""; 
        if(rowC > 0){
            for (int i = 0; i < rowC; i++) {
                for (int j = 0; j < colC; j++) {
                    if(j ==1){
                        var = jTabAsigaDinamicoCompra.getValueAt(i, j).toString();
                        //System.out.println("Buscar en tab -> "+var);
                        if (controlInserts.validaCompAsignadas(var, "", "idCli+fech")) {
                            idProdBusq = jTabAsigaDinamicoCompra.getValueAt(i, j+1).toString(); //pos j=2
                            
                            cantProdt = jTabAsigaDinamicoCompra.getValueAt(i, j+3).toString(); //pos j=4
//calcAsignAPed(idAbuscarDetail,,campoNameComparar,);
                            sumDetcompra = controlInserts.calcAsignAPed(var,idProdBusq,"id_detailComp");
                            jTabAsigaDinamicoCompra.setValueAt(sumDetcompra,i, 9);//j=9 ->Asignados
sumaProd = Integer.parseInt(cantProdt) - Integer.parseInt(sumDetcompra);
                            jTabAsigaDinamicoCompra.setValueAt(sumaProd,i, 10);//j=10 ->Disponibles
                        }else{
jTabAsigaDinamicoCompra.setValueAt(0,i, 9);//j=9 ->Asignados
jTabAsigaDinamicoCompra.setValueAt(jTabAsigaDinamicoCompra.getValueAt(i, j+3).toString(),i, 10);//j=10 -> Disponibles
                        }
                    }//if i =1

                }//for j
            }//for i
            

        }else{
            JOptionPane.showMessageDialog(null, "No tiene filas que asignar");
        }
    }
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem AgregarMayoreo;
    private javax.swing.ButtonGroup btnGAltaClientes;
    private javax.swing.ButtonGroup btnGFletesCrea;
    private javax.swing.ButtonGroup btnGFletesPane;
    private javax.swing.ButtonGroup btnGVentasPiso;
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
    private javax.swing.JButton jButGuardDetail;
    private javax.swing.JButton jButGuardaPedidodia;
    private javax.swing.JButton jButaltasGuardar;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox<String> jCBAltasFletes;
    private javax.swing.JComboBox<String> jCBTypeProv;
    private javax.swing.JComboBox<String> jCCliVentaPiso;
    private javax.swing.JComboBox<String> jCElijaProovedor;
    private javax.swing.JComboBox<String> jCfleteroOpc;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JComboBox<String> jComBPrestamosProv;
    private javax.swing.JComboBox<String> jComBProveedor;
    private javax.swing.JComboBox<String> jComBusCompra;
    private javax.swing.JComboBox<String> jComBusPrestamo;
    private javax.swing.JComboBox<String> jComPedBusqCli;
    private javax.swing.JComboBox<String> jCombBProvBusqCompra;
    private javax.swing.JComboBox<String> jCombBProvBusqPrest;
    private javax.swing.JComboBox<String> jCombCliVentaP;
    private javax.swing.JComboBox<String> jCombOpcBusqFletes;
    private javax.swing.JComboBox<String> jCombOpcBusqPedido;
    private javax.swing.JComboBox<String> jCombOpcBusqVenta;
    private javax.swing.JComboBox<String> jCombPedidoClient;
    private javax.swing.JComboBox<String> jCombProdPedidos;
    private javax.swing.JComboBox<String> jCombProdVentaP;
    private javax.swing.JComboBox<String> jCombProductProv;
    private javax.swing.JComboBox<String> jComboAltas;
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
    private javax.swing.JFrame jFramElijeAsignCompras;
    private javax.swing.JLabel jLaComp;
    private javax.swing.JLabel jLabAdeudaProoved;
    private javax.swing.JLabel jLabBNumerador;
    private javax.swing.JLabel jLabFlet;
    private javax.swing.JLabel jLabLetreroFletes;
    private javax.swing.JLabel jLabNumcompra;
    private javax.swing.JLabel jLabNumcompra1;
    private javax.swing.JLabel jLabNumcompra2;
    private javax.swing.JLabel jLabPed;
    private javax.swing.JLabel jLabPed1;
    private javax.swing.JLabel jLabPed3;
    private javax.swing.JLabel jLabPed4;
    private javax.swing.JLabel jLabPed5;
    private javax.swing.JLabel jLabPed6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel100;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel110;
    private javax.swing.JLabel jLabel111;
    private javax.swing.JLabel jLabel112;
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
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
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
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel70;
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
    private javax.swing.JLayeredPane jLayVentasPiso;
    private javax.swing.JLayeredPane jLayerFletes;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JLayeredPane jLayeredPane2;
    private javax.swing.JLayeredPane jLayeredPane3;
    private javax.swing.JLayeredPane jLayeredPanePedidos;
    private javax.swing.JMenuItem jMIPPVer;
    private javax.swing.JMenuItem jMIPaysPresta;
    private javax.swing.JMenuItem jMI_ElimASIGNA;
    private javax.swing.JMenuItem jMItDetailVer;
    private javax.swing.JMenuItem jMItPayFlete;
    private javax.swing.JMenuItem jMPAYFILTRO;
    private javax.swing.JMenuItem jMenPago;
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
    private javax.swing.JPanel jPanNominas;
    private javax.swing.JPanel jPanPagos;
    private javax.swing.JPanel jPanPrestamoProovedor;
    private javax.swing.JPanel jPanSubastaOption;
    private javax.swing.JPanel jPanVentasPiso;
    private javax.swing.JPanel jPanVistaAlta;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPopupMenu jPopCompraProveedor;
    private javax.swing.JPopupMenu jPopFILTROCOMPRAS;
    private javax.swing.JPopupMenu jPopMFiltrosBusqFletes;
    private javax.swing.JPopupMenu jPopPedidosDia;
    private javax.swing.JPopupMenu jPopVentaPisoBusq;
    private javax.swing.JPopupMenu jPopupPrestaProov;
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
    private javax.swing.JRadioButton jRadioConsulPedido;
    private javax.swing.JRadioButton jRadioCreaPedido;
    private javax.swing.JRadioButton jRdCreaFletes;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane16;
    private javax.swing.JScrollPane jScrollPane17;
    private javax.swing.JScrollPane jScrollPane18;
    private javax.swing.JScrollPane jScrollPane19;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane20;
    private javax.swing.JScrollPane jScrollPane21;
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
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JPopupMenu.Separator jSeparator5;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JTable jTDetailAsign;
    private javax.swing.JTable jTVistaVentaPisoDia;
    private javax.swing.JTable jTabAsigaDinamicoCompra;
    private javax.swing.JTable jTabBusqCompraProv1;
    private javax.swing.JTable jTabBusqPrestProv;
    private javax.swing.JTable jTabDescVentaP;
    private javax.swing.JTable jTabDetailAsign;
    private javax.swing.JTable jTabDetailAsignTotales;
    private javax.swing.JTable jTabDetailAsignTotales1;
    private javax.swing.JTable jTabDetallecompraAll;
    private javax.swing.JTable jTabFletesDia;
    private javax.swing.JTable jTabFletesDia1;
    private javax.swing.JTable jTabPayPeds;
    private javax.swing.JTable jTabPayPrestamosProv;
    private javax.swing.JTable jTabPaysCompraProovedor;
    private javax.swing.JTable jTabPaysFletesDia;
    private javax.swing.JTable jTabPedidosDiaView;
    private javax.swing.JTable jTabPreciosPrest;
    private javax.swing.JTable jTabPrestamoProovedores;
    private javax.swing.JTable jTabSumTotales;
    private javax.swing.JTable jTabVentPisoPays;
    private javax.swing.JTable jTabVistaComprasDia;
    private javax.swing.JTable jTabVistaComprasDia3;
    private javax.swing.JTable jTabVistaPedidosDia1;
    private javax.swing.JTable jTabVistaPresta;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTable jTableAltasCli;
    private javax.swing.JTable jTableCreaPedidos;
    private javax.swing.JTable jTablefiltrosBusq;
    private javax.swing.JTable jTablefiltrosBusqVent;
    private javax.swing.JTable jTablefiltrosBusqflete;
    private javax.swing.JTextField jTexTelefono;
    private javax.swing.JTextField jTextApellidos;
    private javax.swing.JTextField jTextField1;
    public javax.swing.JTextField jTextField17;
    private javax.swing.JTextField jTextLocalidad;
    private javax.swing.JTextField jTextNombre;
    private javax.swing.JTabbedPane paneAltas;
    private javax.swing.JPanel proveedorJP;
    private javax.swing.JTextArea textANotaPrestProv;
    private javax.swing.JTextField txtBusqAltas;
    private javax.swing.JTextField txtCamSubastaCompra;
    private javax.swing.JTextField txtCantCreaPedido;
    private javax.swing.JTextField txtCantPres;
    private javax.swing.JTextField txtCantVentaPiso;
    private javax.swing.JTextField txtCantidadCompra;
    private javax.swing.JTextField txtChoferFlete;
    private javax.swing.JTextField txtCompraAsign;
    private javax.swing.JTextField txtCostoFlete;
    private javax.swing.JTextField txtFolioFlete;
    private javax.swing.JTextField txtFolioFleteAsign;
    private javax.swing.JTextField txtIdParam;
    private javax.swing.JTextField txtImportComp;
    private javax.swing.JTextField txtImportPres;
    private javax.swing.JTextField txtImportVentaP;
    private javax.swing.JTextField txtNotaCompra;
    private javax.swing.JTextField txtNotaFlete;
    private javax.swing.JTextField txtNotaVentP;
    private javax.swing.JTextField txtNotePedidoCli;
    private javax.swing.JTextField txtPrecCompraProv;
    private javax.swing.JTextField txtPrecProdVentaP;
    private javax.swing.JTextField txtPrecProov;
    private javax.swing.JTextField txtQuintoAltas;
    private javax.swing.JTextField txtTotalVentaPiso;
    private javax.swing.JTextField txtUnidFlete;
    private javax.swing.JTextField txtidPedidoAsign;
    // End of variables declaration//GEN-END:variables

}
