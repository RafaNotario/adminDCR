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
    
    DefaultTableModel dtm;//obtener modelo en tabla CreaPedido clientes
    DefaultTableModel dtmPrec;
    DefaultTableModel tabCompras;//tabla diamica de compras del dia
    String[] cab = {"ID PEDIDO", "FECHA", "STATUS", "ID CLIENTE", "NOMBRE", "CANTIDAD", "COSTO", "NOTA"};
    String[] cabPrest = {"NO. CREDITO", "FECHA", "STATUS", "ID PROVEEDOR", "NOMBRE", "TOTAL", "NOTA"};
    String[] cabCompra = {"NO. COMPRA", "FECHA", "STATUS", "ID PROVEEDOR", "NOMBRE", "TOTAL", "NOTA"};
    String[] cabvENTAp = {"NO. VENTA", "FECHA", "STATUS", "ID CLIENTE", "NOMBRE", "TOTAL", "NOTA"};
    String[] cabFilterFlete = {"FOLIO", "FLETERO", "FECHA", "CHOFER", "UNIDAD", "COSTO", "STATUS","NOTA"};
    String[] cabEdoPed = {"FOLIO", "Compras Asignadas", "Pedidos Destinados", "Total de carga"};

    //cabeceras de tables del dia
    String[] cabPaysDay = {"ID", "Fecha", "Monto", "Nota","Metodo"};
    String[] cabMayAsignados = {"IdR","Mayorista","idC","Fech"};
    
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
        jPanCreaPedido = new javax.swing.JPanel();
        jCombPedidoClient = new javax.swing.JComboBox<>();
        jLabel46 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jCombProdPedidos = new javax.swing.JComboBox<>();
        jLabel44 = new javax.swing.JLabel();
        txtCantCreaPedido = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        txtNotePedidoCli = new javax.swing.JTextField();
        jButton9 = new javax.swing.JButton();
        jScrollPane9 = new javax.swing.JScrollPane();
        jTableCreaPedidos = new javax.swing.JTable();
        jDateCHPedido = new com.toedter.calendar.JDateChooser();
        jLabel58 = new javax.swing.JLabel();
        jLabel59 = new javax.swing.JLabel();
        jScrollPane10 = new javax.swing.JScrollPane();
        jTabPedidosDiaView = new javax.swing.JTable();
        jButGuardaPedidodia = new javax.swing.JButton();
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
        proveedorJP = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jRadBCompraProve = new javax.swing.JRadioButton();
        jRadBPrestamoProv = new javax.swing.JRadioButton();
        jRadBConsultaProv = new javax.swing.JRadioButton();
        jLayeredPane1 = new javax.swing.JLayeredPane();
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
        jPanCompraProoved = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        jCElijaProovedor = new javax.swing.JComboBox<>();
        jCombProductProv = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtPrecCompraProv = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTabCompraProved = new javax.swing.JTable();
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
        jScrollPane15 = new javax.swing.JScrollPane();
        jTabPrecCompProved = new javax.swing.JTable();
        txtImportComp = new javax.swing.JTextField();
        jLabel48 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTabMayorAsignados = new javax.swing.JTable();
        jLabel26 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jCheckBox1 = new javax.swing.JCheckBox();
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
        jLabel71 = new javax.swing.JLabel();
        jScrollPane14 = new javax.swing.JScrollPane();
        jTabBusqPrestProv = new javax.swing.JTable();
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
        jPFletes = new javax.swing.JPanel();
        jRdCreaFletes = new javax.swing.JRadioButton();
        jRHistorFletes = new javax.swing.JRadioButton();
        jLayerFletes = new javax.swing.JLayeredPane();
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
        jPanel3 = new javax.swing.JPanel();
        jLabPed1 = new javax.swing.JLabel();
        jScrollPane29 = new javax.swing.JScrollPane();
        jTabPaysFletesDia = new javax.swing.JTable();
        jLabPed6 = new javax.swing.JLabel();
        jScrollPane31 = new javax.swing.JScrollPane();
        jTabPaysCompraProovedor = new javax.swing.JTable();
        jDFechPays = new com.toedter.calendar.JDateChooser();
        jButFleteGuardar1 = new javax.swing.JButton();
        jLabel100 = new javax.swing.JLabel();

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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("ADMINISTRACION DCR");
        setIconImage(getIconImage());
        setPreferredSize(new java.awt.Dimension(1366, 768));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanCabezera.setBackground(new java.awt.Color(117, 229, 255));
        jPanCabezera.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel52.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel52.setText("USUARIO:");
        jPanCabezera.add(jLabel52, new org.netbeans.lib.awtextra.AbsoluteConstraints(1000, 0, 100, 35));

        jTextField17.setEditable(false);
        jTextField17.setBackground(new java.awt.Color(255, 255, 255));
        jTextField17.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        jTextField17.setForeground(new java.awt.Color(51, 0, 153));
        jTextField17.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanCabezera.add(jTextField17, new org.netbeans.lib.awtextra.AbsoluteConstraints(1100, 0, 110, 35));

        jButton2.setBackground(new java.awt.Color(117, 229, 255));
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/Actualizar.png"))); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanCabezera.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 0, 80, 40));

        jButton6.setBackground(new java.awt.Color(117, 229, 255));
        jButton6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton6.setText("BACKUP");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jPanCabezera.add(jButton6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 40));

        getContentPane().add(jPanCabezera, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1920, 40));

        paneAltas.setForeground(new java.awt.Color(107, 109, 232));
        paneAltas.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        paneAltas.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        paneAltas.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N

        jPanAltas.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanAltas.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanAdminist.setBackground(new java.awt.Color(255, 255, 255));
        jPanAdminist.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Administracion de clientes", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 13))); // NOI18N
        jPanAdminist.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel7.setFont(new java.awt.Font("Calibri", 0, 15)); // NOI18N
        jLabel7.setText("LOCALIDAD:");
        jPanAdminist.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 180, 120, 40));

        jLabel13.setFont(new java.awt.Font("Calibri", 0, 15)); // NOI18N
        jLabel13.setText("APELLIDOS:");
        jPanAdminist.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 130, 120, 40));

        jTextLocalidad.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jPanAdminist.add(jTextLocalidad, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 180, 190, 40));

        jTextApellidos.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jPanAdminist.add(jTextApellidos, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 130, 190, 40));

        jButAltasElimina.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButAltasElimina.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/delete32px.png"))); // NOI18N
        jButAltasElimina.setText("Eliminar");
        jButAltasElimina.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButAltasEliminaActionPerformed(evt);
            }
        });
        jPanAdminist.add(jButAltasElimina, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 140, 160, 50));

        jTexTelefono.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jPanAdminist.add(jTexTelefono, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 80, 180, 40));

        jButaltasGuardar.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButaltasGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/add-BN.png"))); // NOI18N
        jButaltasGuardar.setText("    Guardar");
        jButaltasGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButaltasGuardarActionPerformed(evt);
            }
        });
        jPanAdminist.add(jButaltasGuardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 20, 160, 50));

        jButAltasActualiza.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButAltasActualiza.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/Actualizar.png"))); // NOI18N
        jButAltasActualiza.setText("    Actualizar");
        jButAltasActualiza.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButAltasActualizaActionPerformed(evt);
            }
        });
        jPanAdminist.add(jButAltasActualiza, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 80, 160, 50));

        jLabel17.setFont(new java.awt.Font("Calibri", 0, 15)); // NOI18N
        jLabel17.setText("TELEFONO:");
        jPanAdminist.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 80, 130, 40));

        btnGAltaClientes.add(jRad1Activo);
        jRad1Activo.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jRad1Activo.setText("ACTIVO");
        jRad1Activo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRad1ActivoActionPerformed(evt);
            }
        });
        jPanAdminist.add(jRad1Activo, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 30, 110, 40));

        btnGAltaClientes.add(jRadInactivo);
        jRadInactivo.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jRadInactivo.setText("INACTIVO");
        jPanAdminist.add(jRadInactivo, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 30, 120, 40));

        jLabel19.setFont(new java.awt.Font("Calibri", 0, 15)); // NOI18N
        jLabel19.setText("STATUS:");
        jPanAdminist.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 30, 130, 40));

        jLabel21.setFont(new java.awt.Font("Calibri", 0, 15)); // NOI18N
        jLabel21.setText("TIPO PROVEEDOR:");
        jPanAdminist.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 180, 120, 40));

        jLabelRFCAlta.setFont(new java.awt.Font("Calibri", 0, 15)); // NOI18N
        jLabelRFCAlta.setText("RFC:");
        jPanAdminist.add(jLabelRFCAlta, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 130, 130, 40));

        txtQuintoAltas.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jPanAdminist.add(txtQuintoAltas, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 130, 180, 40));

        jTextNombre.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jPanAdminist.add(jTextNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 80, 190, 40));

        jLabel25.setFont(new java.awt.Font("Calibri", 0, 15)); // NOI18N
        jLabel25.setText("NOMBRE:");
        jPanAdminist.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 80, 120, 40));

        jCBTypeProv.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCBTypeProv.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "MINORISTA", "MAYORISTA" }));
        jPanAdminist.add(jCBTypeProv, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 180, 190, 40));

        jPanAltas.add(jPanAdminist, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, 1910, 270));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel1.setText("Elija tipo de usuario a registrar:");
        jPanAltas.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 20, 230, 30));

        jComboAltas.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jComboAltas.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Cliente", "Proveedor", "Empleado", "Fletero" }));
        jComboAltas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboAltasActionPerformed(evt);
            }
        });
        jPanAltas.add(jComboAltas, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 20, 200, 40));

        jPanVistaAlta.setBackground(new java.awt.Color(255, 255, 255));
        jPanVistaAlta.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel12.setFont(new java.awt.Font("Calibri", 0, 15)); // NOI18N
        jLabel12.setText("BUSCAR:");
        jPanVistaAlta.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 100, 40));

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

        jPanVistaAlta.add(jScrollPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 80, 1480, 460));

        txtBusqAltas.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        txtBusqAltas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBusqAltasKeyPressed(evt);
            }
        });
        jPanVistaAlta.add(txtBusqAltas, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 30, 210, 40));

        jPanAltas.add(jPanVistaAlta, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 340, 1910, 660));

        jDateChFechaAlta.setDateFormatString("dd/MM/yyyy");
        jDateChFechaAlta.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanAltas.add(jDateChFechaAlta, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 20, 180, 40));

        jLabel16.setFont(new java.awt.Font("Calibri", 0, 15)); // NOI18N
        jLabel16.setText("FECHA ALTA:");
        jPanAltas.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 20, 110, 40));

        txtIdParam.setEditable(false);
        txtIdParam.setBackground(new java.awt.Color(255, 255, 255));
        txtIdParam.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanAltas.add(txtIdParam, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 20, 70, 40));

        paneAltas.addTab("    ALTA DE CLIENTES    ", jPanAltas);

        jPPedidosHist.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel15.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel15.setText("ELIJA ACCION:");
        jPPedidosHist.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 10, 150, 40));

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
        jPPedidosHist.add(jRadioCreaPedido, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 10, 210, 40));

        jRadioConsulPedido.setBackground(new java.awt.Color(255, 255, 255));
        butnGPedidos.add(jRadioConsulPedido);
        jRadioConsulPedido.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jRadioConsulPedido.setText("CONSULTAR PEDIDOS");
        jRadioConsulPedido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioConsulPedidoActionPerformed(evt);
            }
        });
        jPPedidosHist.add(jRadioConsulPedido, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 10, 230, 40));

        jLayeredPanePedidos.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanCreaPedido.setBackground(new java.awt.Color(255, 255, 255));
        jPanCreaPedido.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Nuevo pedido", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13), new java.awt.Color(0, 51, 51))); // NOI18N
        jPanCreaPedido.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jCombPedidoClient.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCombPedidoClient.setMaximumRowCount(100);
        jCombPedidoClient.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " " }));
        jCombPedidoClient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCombPedidoClientActionPerformed(evt);
            }
        });
        jPanCreaPedido.add(jCombPedidoClient, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 30, 190, 40));

        jLabel46.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel46.setText("PEDIDOS DEL DIA:");
        jPanCreaPedido.add(jLabel46, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 280, 170, 30));

        jLabel43.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel43.setText("Tipo Mercancia:");
        jPanCreaPedido.add(jLabel43, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 90, 140, 40));

        jCombProdPedidos.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jPanCreaPedido.add(jCombProdPedidos, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 90, 140, 40));

        jLabel44.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel44.setText("CANTIDAD:");
        jPanCreaPedido.add(jLabel44, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 90, 90, 40));

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
        jPanCreaPedido.add(txtCantCreaPedido, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 90, 80, 40));

        jLabel6.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel6.setText("CAJAS");
        jPanCreaPedido.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 90, 60, 40));

        jLabel45.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel45.setText("NOTA:");
        jPanCreaPedido.add(jLabel45, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 90, 70, 40));

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
        jPanCreaPedido.add(txtNotePedidoCli, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 90, 240, 40));

        jButton9.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton9.setText("AGREGAR");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });
        jPanCreaPedido.add(jButton9, new org.netbeans.lib.awtextra.AbsoluteConstraints(990, 90, 110, 40));

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

        jPanCreaPedido.add(jScrollPane9, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 170, 1180, 80));

        jDateCHPedido.setDateFormatString("dd/MM/yyyy");
        jDateCHPedido.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanCreaPedido.add(jDateCHPedido, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 30, 190, 40));

        jLabel58.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel58.setText("ELIJA CLIENTE:");
        jPanCreaPedido.add(jLabel58, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 30, 150, 40));

        jLabel59.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel59.setText("PEDIDO DE CLIENTE:");
        jPanCreaPedido.add(jLabel59, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, 170, 30));

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

        jPanCreaPedido.add(jScrollPane10, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 320, 1560, 540));

        jButGuardaPedidodia.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButGuardaPedidodia.setText("GUARDAR");
        jButGuardaPedidodia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButGuardaPedidodiaActionPerformed(evt);
            }
        });
        jPanCreaPedido.add(jButGuardaPedidodia, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 250, 140, 50));

        jLayeredPanePedidos.add(jPanCreaPedido, new org.netbeans.lib.awtextra.AbsoluteConstraints(-4, 0, 1920, 950));

        jPanConsulPed.setBackground(new java.awt.Color(255, 255, 255));
        jPanConsulPed.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Consulta Pedidos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14))); // NOI18N
        jPanConsulPed.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel18.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel18.setText("Tabla de Resultados");
        jPanConsulPed.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 200, 140, 40));

        jComPedBusqCli.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jComPedBusqCli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComPedBusqCliActionPerformed(evt);
            }
        });
        jPanConsulPed.add(jComPedBusqCli, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 100, 210, 40));

        jLabel60.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel60.setText("TIPO DE BUSQUEDA");
        jPanConsulPed.add(jLabel60, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 30, 180, 40));

        jCombOpcBusqPedido.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCombOpcBusqPedido.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "CLIENTE", "FECHA", "LAPSO FECHAS", "CLIENTE+FECHA", "CLIENTE+LAPSO FECHAS" }));
        jCombOpcBusqPedido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCombOpcBusqPedidoActionPerformed(evt);
            }
        });
        jPanConsulPed.add(jCombOpcBusqPedido, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 30, 210, 40));

        jLabel61.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel61.setText("ELIJA CLIENTE:");
        jPanConsulPed.add(jLabel61, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 100, 140, 40));

        jDateChoB1Cli.setDateFormatString("dd/MM/yyyy");
        jDateChoB1Cli.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanConsulPed.add(jDateChoB1Cli, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 160, 210, 40));

        jLabel62.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel62.setText("ELIJA FECHA 2:");
        jPanConsulPed.add(jLabel62, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 160, 140, 40));

        jDate2BusqCli.setDateFormatString("dd/MM/yyyy");
        jDate2BusqCli.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanConsulPed.add(jDate2BusqCli, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 160, 210, 40));

        jScrollPane3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jTablefiltrosBusq.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTablefiltrosBusq.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTablefiltrosBusq.setComponentPopupMenu(jPopPedidosDia);
        jTablefiltrosBusq.setRowHeight(32);
        jTablefiltrosBusq.setRowMargin(2);
        jTablefiltrosBusq.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTablefiltrosBusqMousePressed(evt);
            }
        });
        jScrollPane3.setViewportView(jTablefiltrosBusq);

        jPanConsulPed.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 250, 1470, 610));

        jLabel63.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel63.setText("ELIJA FECHA:");
        jPanConsulPed.add(jLabel63, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 160, 140, 40));
        jPanConsulPed.add(jSeparator7, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 220, 460, 10));

        jButton3.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/search32pxcolor.png"))); // NOI18N
        jButton3.setText("BUSCAR");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanConsulPed.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 160, 180, 50));

        jLayeredPanePedidos.add(jPanConsulPed, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1910, 950));

        jPPedidosHist.add(jLayeredPanePedidos, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 50, 1920, 950));

        paneAltas.addTab("    PEDIDOS    ", jPPedidosHist);

        proveedorJP.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel3.setText("ELIJA ACCION:");
        proveedorJP.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 10, 150, 40));

        buttonGProveedores.add(jRadBCompraProve);
        jRadBCompraProve.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jRadBCompraProve.setSelected(true);
        jRadBCompraProve.setText("COMPRA");
        jRadBCompraProve.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadBCompraProveActionPerformed(evt);
            }
        });
        proveedorJP.add(jRadBCompraProve, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 10, 130, 40));

        buttonGProveedores.add(jRadBPrestamoProv);
        jRadBPrestamoProv.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jRadBPrestamoProv.setText("PRESTAMO");
        jRadBPrestamoProv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadBPrestamoProvActionPerformed(evt);
            }
        });
        proveedorJP.add(jRadBPrestamoProv, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 10, 140, 40));

        buttonGProveedores.add(jRadBConsultaProv);
        jRadBConsultaProv.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jRadBConsultaProv.setText("CONSULTAR OPERACIONES");
        jRadBConsultaProv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadBConsultaProvActionPerformed(evt);
            }
        });
        proveedorJP.add(jRadBConsultaProv, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 10, 260, 40));

        jLayeredPane1.setBackground(new java.awt.Color(0, 102, 102));
        jLayeredPane1.setPreferredSize(new java.awt.Dimension(1366, 580));
        jLayeredPane1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanPrestamoProovedor.setBackground(new java.awt.Color(255, 255, 255));
        jPanPrestamoProovedor.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Prestamo a Proveedor", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 12))); // NOI18N
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
        jPanPrestamoProovedor.add(jButton7, new org.netbeans.lib.awtextra.AbsoluteConstraints(1230, 150, 140, 50));

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

        jPanPrestamoProovedor.add(jScrollPane7, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 160, 1080, 60));

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

        jPanPrestamoProovedor.add(jScrollPane8, new org.netbeans.lib.awtextra.AbsoluteConstraints(1000, 80, 280, 60));

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

        jTabPreciosPrest.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "CAJA", "PERIODICO", "SEMILLA", "EFECTIVO"
            }
        ));
        jTabPreciosPrest.setRowHeight(32);
        jTabPreciosPrest.setRowMargin(2);
        jScrollPane11.setViewportView(jTabPreciosPrest);

        jPanPrestamoProovedor.add(jScrollPane11, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 190, 1080, 70));

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

        jPanPrestamoProovedor.add(jScrollPane13, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 340, 1520, 570));

        jLabel68.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel68.setText("ELIJA PROVEEDOR:");
        jPanPrestamoProovedor.add(jLabel68, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, 150, 40));
        jPanPrestamoProovedor.add(jSeparator8, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 320, 1070, 10));

        jLabel27.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel27.setText("CANTIDAD:");
        jPanPrestamoProovedor.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 180, 90, 40));

        jLabel28.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel28.setText("COSTO   $:");
        jPanPrestamoProovedor.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 220, 90, 40));

        jLayeredPane1.add(jPanPrestamoProovedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1920, 960));

        jPanCompraProoved.setBackground(new java.awt.Color(255, 255, 255));
        jPanCompraProoved.setPreferredSize(new java.awt.Dimension(1366, 620));
        jPanCompraProoved.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel22.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel22.setText("ELIJA PROVEEDOR:");
        jPanCompraProoved.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 150, 40));

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
        jPanCompraProoved.add(jCElijaProovedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 50, 190, 40));

        jCombProductProv.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCombProductProv.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " " }));
        jPanCompraProoved.add(jCombProductProv, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 110, 140, 40));

        jLabel9.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel9.setText("CANTIDAD:");
        jPanCompraProoved.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 190, 90, 40));

        jLabel2.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel2.setText("CAJAS");
        jPanCompraProoved.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 110, 60, 40));

        jLabel8.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel8.setText("FECHA:");
        jPanCompraProoved.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 40, 70, 40));

        txtPrecCompraProv.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtPrecCompraProv.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPrecCompraProvFocusLost(evt);
            }
        });
        jPanCompraProoved.add(txtPrecCompraProv, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 110, 80, 40));

        jButton1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton1.setText("AGREGAR");
        jButton1.setNextFocusableComponent(jTabCompraProved);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanCompraProoved.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1210, 100, 110, 50));

        jLabel10.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel10.setText("COMPRAS DEL DIA:");
        jPanCompraProoved.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 300, 180, 30));

        jTabCompraProved.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTabCompraProved.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "PRIM", "SEG", "PRIM_R", "SEG_R", "BOLA_P", "BOLA_S", "TERCERA"
            }
        ));
        jTabCompraProved.setRowHeight(35);
        jTabCompraProved.setRowMargin(2);
        jTabCompraProved.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTabCompraProvedKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(jTabCompraProved);

        jPanCompraProoved.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 170, 960, 60));

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
            jTabVistaComprasDia.getColumnModel().getColumn(0).setMinWidth(50);
            jTabVistaComprasDia.getColumnModel().getColumn(0).setPreferredWidth(50);
            jTabVistaComprasDia.getColumnModel().getColumn(0).setMaxWidth(50);
            jTabVistaComprasDia.getColumnModel().getColumn(1).setMinWidth(120);
            jTabVistaComprasDia.getColumnModel().getColumn(1).setPreferredWidth(120);
            jTabVistaComprasDia.getColumnModel().getColumn(1).setMaxWidth(120);
            jTabVistaComprasDia.getColumnModel().getColumn(2).setMinWidth(100);
            jTabVistaComprasDia.getColumnModel().getColumn(2).setPreferredWidth(100);
            jTabVistaComprasDia.getColumnModel().getColumn(2).setMaxWidth(100);
            jTabVistaComprasDia.getColumnModel().getColumn(3).setMinWidth(100);
            jTabVistaComprasDia.getColumnModel().getColumn(3).setPreferredWidth(100);
            jTabVistaComprasDia.getColumnModel().getColumn(3).setMaxWidth(100);
            jTabVistaComprasDia.getColumnModel().getColumn(4).setMinWidth(100);
            jTabVistaComprasDia.getColumnModel().getColumn(4).setPreferredWidth(100);
            jTabVistaComprasDia.getColumnModel().getColumn(4).setMaxWidth(100);
            jTabVistaComprasDia.getColumnModel().getColumn(5).setMinWidth(100);
            jTabVistaComprasDia.getColumnModel().getColumn(5).setPreferredWidth(100);
            jTabVistaComprasDia.getColumnModel().getColumn(5).setMaxWidth(100);
            jTabVistaComprasDia.getColumnModel().getColumn(6).setMinWidth(100);
            jTabVistaComprasDia.getColumnModel().getColumn(6).setPreferredWidth(100);
            jTabVistaComprasDia.getColumnModel().getColumn(6).setMaxWidth(100);
            jTabVistaComprasDia.getColumnModel().getColumn(7).setMinWidth(100);
            jTabVistaComprasDia.getColumnModel().getColumn(7).setPreferredWidth(100);
            jTabVistaComprasDia.getColumnModel().getColumn(7).setMaxWidth(100);
            jTabVistaComprasDia.getColumnModel().getColumn(8).setMinWidth(100);
            jTabVistaComprasDia.getColumnModel().getColumn(8).setPreferredWidth(100);
            jTabVistaComprasDia.getColumnModel().getColumn(8).setMaxWidth(100);
        }

        jPanCompraProoved.add(jScrollPane6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 330, 1340, 580));

        jLabel41.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel41.setText("CANTIDAD:");
        jPanCompraProoved.add(jLabel41, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 110, 80, 40));

        txtCantidadCompra.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtCantidadCompra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCantidadCompraActionPerformed(evt);
            }
        });
        jPanCompraProoved.add(txtCantidadCompra, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 110, 80, 40));

        jLayeredPane3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanSubastaOption.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel57.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel57.setText("CAMIONETA:");
        jPanSubastaOption.add(jLabel57, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 90, 40));

        txtCamSubastaCompra.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtCamSubastaCompra.setNextFocusableComponent(jCombProductProv);
        jPanSubastaOption.add(txtCamSubastaCompra, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 10, 270, 40));

        jLayeredPane3.add(jPanSubastaOption, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 380, 70));

        jPanClientOption.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabAdeudaProoved.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabAdeudaProoved.setText("PRESTAMO PENDIENTE");
        jPanClientOption.add(jLabAdeudaProoved, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 230, 50));

        jLayeredPane3.add(jPanClientOption, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 250, 50));

        jPanCompraProoved.add(jLayeredPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 30, 380, 70));

        jLabel35.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel35.setText("NOTA:");
        jPanCompraProoved.add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(930, 110, 60, 40));

        jDateFechCompraProv.setDateFormatString("dd/MM/yyyy");
        jDateFechCompraProv.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanCompraProoved.add(jDateFechCompraProv, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 40, 190, 40));

        jLabel40.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel40.setText("IMPORTE:");
        jPanCompraProoved.add(jLabel40, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 110, 70, 40));

        txtNotaCompra.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtNotaCompra.setNextFocusableComponent(jTabCompraProved);
        txtNotaCompra.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNotaCompraKeyReleased(evt);
            }
        });
        jPanCompraProoved.add(txtNotaCompra, new org.netbeans.lib.awtextra.AbsoluteConstraints(990, 110, 210, 40));

        jButton13.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton13.setText("GUARDAR");
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });
        jPanCompraProoved.add(jButton13, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 280, 110, 40));

        jTabPrecCompProved.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTabPrecCompProved.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "PRIM", "SEG", "PRIM_R", "SEG_R", "BOLA_P", "BOLA_S", "TERCERA"
            }
        ));
        jTabPrecCompProved.setRowHeight(35);
        jTabPrecCompProved.setRowMargin(2);
        jTabPrecCompProved.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTabPrecCompProvedKeyPressed(evt);
            }
        });
        jScrollPane15.setViewportView(jTabPrecCompProved);

        jPanCompraProoved.add(jScrollPane15, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 200, 960, 70));

        txtImportComp.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtImportComp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtImportCompKeyReleased(evt);
            }
        });
        jPanCompraProoved.add(txtImportComp, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 110, 80, 40));

        jLabel48.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel48.setText("PRECIO $:");
        jPanCompraProoved.add(jLabel48, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 110, 70, 40));

        jLabel23.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel23.setText("MERCANCIA:");
        jPanCompraProoved.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, 110, 40));

        jLabel24.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel24.setText("COSTO   $:");
        jPanCompraProoved.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 230, 90, 40));

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel4.setText("TOTAL $:");
        jPanCompraProoved.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(1490, 610, 70, 40));

        jTabMayorAsignados.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jTabMayorAsignados.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jTabMayorAsignados.setComponentPopupMenu(jPMACompras);
        jTabMayorAsignados.setRowHeight(32);
        jScrollPane2.setViewportView(jTabMayorAsignados);

        jPanCompraProoved.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(1380, 330, 410, 270));

        jLabel26.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel26.setText("COMPRA MAYORISTA");
        jPanCompraProoved.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(1380, 290, 190, 40));

        jTextField1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jPanCompraProoved.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1560, 610, 100, 40));

        jCheckBox1.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jCheckBox1.setText("Carga Mayoristas");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });
        jPanCompraProoved.add(jCheckBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 290, 170, 40));

        jLayeredPane1.add(jPanCompraProoved, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1920, 960));

        jPanBusquedaPrest.setBackground(new java.awt.Color(255, 255, 255));
        jPanBusquedaPrest.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTabbedPane2.setBackground(new java.awt.Color(0, 255, 204));
        jTabbedPane2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N

        jPanInternoBusquedaPrest.setBackground(new java.awt.Color(255, 255, 255));
        jPanInternoBusquedaPrest.setPreferredSize(new java.awt.Dimension(1368, 668));
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

        jLabel71.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel71.setText("ELIJA FECHA:");
        jPanInternoBusquedaPrest.add(jLabel71, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 120, 140, 40));

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

        jPanInternoBusquedaPrest.add(jScrollPane14, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 200, 1370, 680));

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

        jPanel4.add(jScrollPane16, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 200, 1560, 670));

        jTabbedPane2.addTab("COMPRAS", jPanel4);

        jPanBusquedaPrest.add(jTabbedPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1920, 960));

        jLayeredPane1.add(jPanBusquedaPrest, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1920, 960));

        proveedorJP.add(jLayeredPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(-4, 48, 1920, 960));

        paneAltas.addTab("    PROVEEDORES    ", proveedorJP);

        jPFletes.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnGFletesPane.add(jRdCreaFletes);
        jRdCreaFletes.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jRdCreaFletes.setText("CREAR FLETES");
        jRdCreaFletes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRdCreaFletesActionPerformed(evt);
            }
        });
        jPFletes.add(jRdCreaFletes, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 10, 170, 40));

        btnGFletesPane.add(jRHistorFletes);
        jRHistorFletes.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jRHistorFletes.setText("HISTORIAL FLETES");
        jRHistorFletes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRHistorFletesActionPerformed(evt);
            }
        });
        jPFletes.add(jRHistorFletes, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 10, 170, 40));

        jLayerFletes.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanHistorFletes.setBackground(new java.awt.Color(255, 255, 255));
        jPanHistorFletes.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

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

        jPanHistorFletes.add(jScrollPane20, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 240, 1660, 640));

        jLabel94.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel94.setText("Tabla de Resultados");
        jPanHistorFletes.add(jLabel94, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 200, 140, 40));
        jPanHistorFletes.add(jSeparator15, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 220, 460, 10));

        jDCFol1.setDateFormatString("dd/MM/yyyy");
        jDCFol1.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanHistorFletes.add(jDCFol1, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 160, 210, 40));

        jLabel95.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel95.setText("ELIJA FECHA:");
        jPanHistorFletes.add(jLabel95, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 160, 140, 40));

        jLabel96.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel96.setText("ELIJA FECHA 2:");
        jPanHistorFletes.add(jLabel96, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 160, 140, 40));

        jDCFol2.setDateFormatString("dd/MM/yyyy");
        jDCFol2.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanHistorFletes.add(jDCFol2, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 160, 210, 40));

        jButton12.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jButton12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/search32pxcolor.png"))); // NOI18N
        jButton12.setText("BUSCAR");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });
        jPanHistorFletes.add(jButton12, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 160, 180, 50));

        jLabel97.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel97.setText("ELIJA CLIENTE:");
        jPanHistorFletes.add(jLabel97, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 100, 140, 40));

        jCfleteroOpc.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCfleteroOpc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCfleteroOpcActionPerformed(evt);
            }
        });
        jPanHistorFletes.add(jCfleteroOpc, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 100, 210, 40));

        jCombOpcBusqFletes.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCombOpcBusqFletes.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "FLETERO", "FOLIO", "FECHA", "LAPSO FECHAS", "FLETERO+FECHA", "FLETERO+ LAPSO FECHAS" }));
        jCombOpcBusqFletes.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jCombOpcBusqFletesFocusLost(evt);
            }
        });
        jPanHistorFletes.add(jCombOpcBusqFletes, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 30, 210, 40));

        jLabel98.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel98.setText("TIPO DE BUSQUEDA");
        jPanHistorFletes.add(jLabel98, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 30, 180, 40));

        jLayerFletes.add(jPanHistorFletes, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1910, 960));

        jPanCreaFletes.setBackground(new java.awt.Color(255, 255, 255));
        jPanCreaFletes.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel5.setText("Fletes del dia:");
        jPanCreaFletes.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 280, 140, 30));

        jCBAltasFletes.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jCBAltasFletes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBAltasFletesActionPerformed(evt);
            }
        });
        jPanCreaFletes.add(jCBAltasFletes, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 20, 200, 40));

        jLabel86.setFont(new java.awt.Font("Calibri", 0, 15)); // NOI18N
        jLabel86.setText("FECHA:");
        jPanCreaFletes.add(jLabel86, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 20, 110, 40));

        jDFechCreaFlete.setDateFormatString("dd/MM/yyyy");
        jDFechCreaFlete.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanCreaFletes.add(jDFechCreaFlete, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 20, 180, 40));

        jPanAdminist1.setBackground(new java.awt.Color(255, 255, 255));
        jPanAdminist1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Crear Flete", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 13))); // NOI18N
        jPanAdminist1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel14.setFont(new java.awt.Font("Calibri", 0, 15)); // NOI18N
        jLabel14.setText("COSTO $ :");
        jPanAdminist1.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 80, 100, 40));

        jLabel87.setFont(new java.awt.Font("Calibri", 0, 15)); // NOI18N
        jLabel87.setText("UNIDAD:");
        jPanAdminist1.add(jLabel87, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 130, 100, 40));

        txtCostoFlete.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jPanAdminist1.add(txtCostoFlete, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 80, 120, 40));

        txtUnidFlete.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jPanAdminist1.add(txtUnidFlete, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 130, 210, 40));

        jButAltasElimina1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButAltasElimina1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/delete32px.png"))); // NOI18N
        jButAltasElimina1.setText("Eliminar");
        jButAltasElimina1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButAltasElimina1ActionPerformed(evt);
            }
        });
        jPanAdminist1.add(jButAltasElimina1, new org.netbeans.lib.awtextra.AbsoluteConstraints(920, 140, 160, 50));

        txtNotaFlete.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jPanAdminist1.add(txtNotaFlete, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 130, 290, 40));

        jButFleteGuardar.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButFleteGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/add-BN.png"))); // NOI18N
        jButFleteGuardar.setText("    Guardar");
        jButFleteGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButFleteGuardarActionPerformed(evt);
            }
        });
        jPanAdminist1.add(jButFleteGuardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(920, 20, 160, 50));

        jButAltasActualiza1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButAltasActualiza1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/Actualizar.png"))); // NOI18N
        jButAltasActualiza1.setText("    Actualizar");
        jButAltasActualiza1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButAltasActualiza1ActionPerformed(evt);
            }
        });
        jPanAdminist1.add(jButAltasActualiza1, new org.netbeans.lib.awtextra.AbsoluteConstraints(920, 80, 160, 50));

        jLabel88.setFont(new java.awt.Font("Calibri", 0, 15)); // NOI18N
        jLabel88.setText("M.N.");
        jPanAdminist1.add(jLabel88, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 80, 50, 40));

        jLabel90.setFont(new java.awt.Font("Calibri", 0, 15)); // NOI18N
        jLabel90.setText("CHOFER:");
        jPanAdminist1.add(jLabel90, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 80, 100, 40));

        txtChoferFlete.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jPanAdminist1.add(txtChoferFlete, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 80, 210, 40));

        txtFolioFlete.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        txtFolioFlete.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtFolioFleteFocusLost(evt);
            }
        });
        jPanAdminist1.add(txtFolioFlete, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 30, 100, 40));

        jLabel89.setFont(new java.awt.Font("Calibri", 0, 15)); // NOI18N
        jLabel89.setText("FOLIO:");
        jPanAdminist1.add(jLabel89, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 30, 100, 40));

        jLabLetreroFletes.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabLetreroFletes.setForeground(new java.awt.Color(204, 0, 0));
        jLabLetreroFletes.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabLetreroFletes.setText("YA EXISTE");
        jPanAdminist1.add(jLabLetreroFletes, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 30, 110, 40));

        jLabel92.setFont(new java.awt.Font("Calibri", 0, 15)); // NOI18N
        jLabel92.setText("STATUS:");
        jPanAdminist1.add(jLabel92, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 30, 90, 40));

        btnGFletesCrea.add(jRPagadopFlete);
        jRPagadopFlete.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jRPagadopFlete.setText("PAGADO");
        jRPagadopFlete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRPagadopFleteActionPerformed(evt);
            }
        });
        jPanAdminist1.add(jRPagadopFlete, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 30, 110, 40));

        btnGFletesCrea.add(jRPendFlete);
        jRPendFlete.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jRPendFlete.setText("PENDIENTE");
        jPanAdminist1.add(jRPendFlete, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 30, 120, 40));

        jLabel91.setFont(new java.awt.Font("Calibri", 0, 15)); // NOI18N
        jLabel91.setText("NOTA:");
        jPanAdminist1.add(jLabel91, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 130, 100, 40));

        jPanCreaFletes.add(jPanAdminist1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 70, 1290, 200));

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

        jPanCreaFletes.add(jScrollPane19, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 310, 1590, 610));

        jLabel93.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel93.setText("Elija fletero:");
        jPanCreaFletes.add(jLabel93, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 20, 140, 40));

        jLayerFletes.add(jPanCreaFletes, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1920, 950));

        jPFletes.add(jLayerFletes, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 50, 1920, 950));

        paneAltas.addTab("    FLETES    ", jPFletes);

        jPanNominas.setBackground(new java.awt.Color(255, 255, 255));
        jPanNominas.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

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

        jPanNominas.add(jScrollPane23, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 90, 820, 240));

        jLabel110.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel110.setText("FLETES ASIGNADOS:");
        jPanNominas.add(jLabel110, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 640, 150, 30));

        jLabel111.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel111.setText("Detalle flete-pedido-compra");
        jPanNominas.add(jLabel111, new org.netbeans.lib.awtextra.AbsoluteConstraints(980, 580, 210, 30));

        jLabel112.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel112.setText("COMPRAS DEL DIA:");
        jPanNominas.add(jLabel112, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 350, 150, 30));

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

        jPanNominas.add(jScrollPane25, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 380, 820, 250));

        jButton17.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton17.setText(">>");
        jButton17.setAlignmentY(0.0F);
        jButton17.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton17ActionPerformed(evt);
            }
        });
        jPanNominas.add(jButton17, new org.netbeans.lib.awtextra.AbsoluteConstraints(910, 280, 60, 260));

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

        jPanNominas.add(jScrollPane26, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 670, 820, 250));

        jDCAsignacionDia.setDateFormatString("dd/MM/yyyy");
        jPanNominas.add(jDCAsignacionDia, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 10, 180, 40));

        jButton18.setBackground(new java.awt.Color(117, 229, 255));
        jButton18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/Actualizar.png"))); // NOI18N
        jButton18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton18ActionPerformed(evt);
            }
        });
        jPanNominas.add(jButton18, new org.netbeans.lib.awtextra.AbsoluteConstraints(910, 60, 60, 50));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Asignacion de Flete-Mercancia-Pedido", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 14))); // NOI18N
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel114.setFont(new java.awt.Font("Calibri", 0, 15)); // NOI18N
        jLabel114.setText("PARA PEDIDO:");
        jPanel1.add(jLabel114, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 80, 100, 40));

        jLabel115.setFont(new java.awt.Font("Calibri", 0, 15)); // NOI18N
        jLabel115.setText("ID COMPRA:");
        jPanel1.add(jLabel115, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 30, 80, 40));

        txtCompraAsign.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jPanel1.add(txtCompraAsign, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 30, 100, 40));

        txtidPedidoAsign.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jPanel1.add(txtidPedidoAsign, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 80, 100, 40));

        txtFolioFleteAsign.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jPanel1.add(txtFolioFleteAsign, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 30, 100, 40));

        jLabel116.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel116.setText("Status del pedido:");
        jPanel1.add(jLabel116, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 140, 150, 26));

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

        jPanel1.add(jScrollPane24, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 170, 150, 310));

        jLabel117.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel117.setText("PRIM:");
        jLabel117.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel1.add(jLabel117, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 200, 70, 35));

        jLabel118.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel118.setText("SEG:");
        jLabel118.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel1.add(jLabel118, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 240, 70, 35));

        jLabel119.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel119.setText("PRIM_REG:");
        jLabel119.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel1.add(jLabel119, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 280, -1, 35));

        jLabel120.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel120.setText("SEG_REG:");
        jLabel120.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel1.add(jLabel120, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 320, 70, 35));

        jLabel121.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel121.setText("BOLA_P:");
        jLabel121.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel1.add(jLabel121, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 360, 70, 35));

        jLabel122.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel122.setText("BOLA_S:");
        jLabel122.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel1.add(jLabel122, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 400, 70, 35));

        jLabel123.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel123.setText("TERCERA:");
        jLabel123.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel1.add(jLabel123, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 440, 70, 35));

        jButGuardDetail.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/save32px.png"))); // NOI18N
        jButGuardDetail.setToolTipText("Actualiza pedido.");
        jButGuardDetail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButGuardDetailActionPerformed(evt);
            }
        });
        jPanel1.add(jButGuardDetail, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 70, 90, 50));

        jLabel127.setFont(new java.awt.Font("Calibri", 0, 15)); // NOI18N
        jLabel127.setText("FOLIO DE FLETE:");
        jPanel1.add(jLabel127, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 130, 40));

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

        jPanel1.add(jScrollPane32, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 170, 150, 310));

        jLabel124.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel124.setText("Producto a asignar:");
        jPanel1.add(jLabel124, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 140, 160, 26));

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

        jPanel1.add(jScrollPane33, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 170, 150, 310));

        jLabel125.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel125.setText("Status de compra:");
        jPanel1.add(jLabel125, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 140, 140, 26));

        jPanNominas.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(980, 70, 660, 500));

        jLabFlet.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabFlet.setForeground(new java.awt.Color(0, 51, 102));
        jLabFlet.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabFlet.setText("jLabel126");
        jPanNominas.add(jLabFlet, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 640, 180, 30));

        jLabPed.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabPed.setForeground(new java.awt.Color(0, 51, 102));
        jLabPed.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabPed.setText("jLabel126");
        jPanNominas.add(jLabPed, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 60, 180, 40));

        jLaComp.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLaComp.setForeground(new java.awt.Color(0, 51, 102));
        jLaComp.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLaComp.setText("jLabel126");
        jPanNominas.add(jLaComp, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 350, 180, 30));

        jLabel126.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel126.setText("PEDIDOS DEL DIA:");
        jPanNominas.add(jLabel126, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 60, 150, 30));

        jTDetailAsign.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTDetailAsign.setRowHeight(32);
        jTDetailAsign.setRowMargin(2);
        jScrollPane27.setViewportView(jTDetailAsign);

        jPanNominas.add(jScrollPane27, new org.netbeans.lib.awtextra.AbsoluteConstraints(980, 610, 660, 310));

        paneAltas.addTab("ASIGNACION", jPanNominas);

        jPanVentasPiso.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jPanVentasPisoKeyReleased(evt);
            }
        });
        jPanVentasPiso.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnGVentasPiso.add(jRadCreaVentaPiso);
        jRadCreaVentaPiso.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jRadCreaVentaPiso.setText("Realizar Ventas");
        jRadCreaVentaPiso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadCreaVentaPisoActionPerformed(evt);
            }
        });
        jPanVentasPiso.add(jRadCreaVentaPiso, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 0, 200, 50));

        btnGVentasPiso.add(jRadBusqVentaPiso);
        jRadBusqVentaPiso.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jRadBusqVentaPiso.setText("Busqueda de Ventas");
        jRadBusqVentaPiso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadBusqVentaPisoActionPerformed(evt);
            }
        });
        jPanVentasPiso.add(jRadBusqVentaPiso, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 0, 200, 50));

        jLayVentasPiso.setBackground(new java.awt.Color(153, 153, 255));
        jLayVentasPiso.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPaNVentaPiso.setBackground(new java.awt.Color(255, 255, 255));
        jPaNVentaPiso.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel53.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel53.setText("Ventas del día");
        jPaNVentaPiso.add(jLabel53, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 350, 100, 30));

        jCombProdVentaP.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jCombProdVentaP.setMaximumRowCount(10);
        jPaNVentaPiso.add(jCombProdVentaP, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 70, 120, 40));

        jLabel54.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel54.setText("CANTIDAD:");
        jPaNVentaPiso.add(jLabel54, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 70, 90, 40));

        txtCantVentaPiso.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtCantVentaPiso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCantVentaPisoActionPerformed(evt);
            }
        });
        jPaNVentaPiso.add(txtCantVentaPiso, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 70, 80, 40));

        jLabel55.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel55.setText("CAJAS");
        jPaNVentaPiso.add(jLabel55, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 70, 60, 40));

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
        jPaNVentaPiso.add(txtNotaVentP, new org.netbeans.lib.awtextra.AbsoluteConstraints(990, 70, 230, 40));

        jButton8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton8.setText("AGREGAR");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });
        jPaNVentaPiso.add(jButton8, new org.netbeans.lib.awtextra.AbsoluteConstraints(910, 140, 110, 50));

        jButton10.setBackground(new java.awt.Color(153, 255, 0));
        jButton10.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButton10.setText("F12 - COBRAR");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });
        jPaNVentaPiso.add(jButton10, new org.netbeans.lib.awtextra.AbsoluteConstraints(960, 200, 130, 60));

        jLabel75.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel75.setText("ELIJA PRODUCTO:");
        jPaNVentaPiso.add(jLabel75, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 70, 140, 40));

        jCombCliVentaP.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jCombCliVentaP.setNextFocusableComponent(jCombProdVentaP);
        jPaNVentaPiso.add(jCombCliVentaP, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 10, 150, 40));

        jLabel76.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel76.setText("FECHA:");
        jPaNVentaPiso.add(jLabel76, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 10, 80, 40));

        txtPrecProdVentaP.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtPrecProdVentaP.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPrecProdVentaPFocusLost(evt);
            }
        });
        jPaNVentaPiso.add(txtPrecProdVentaP, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 70, 80, 40));

        jLabel77.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel77.setText("IMPORTE:");
        jPaNVentaPiso.add(jLabel77, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 70, 70, 40));

        txtImportVentaP.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtImportVentaP.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtImportVentaPKeyPressed(evt);
            }
        });
        jPaNVentaPiso.add(txtImportVentaP, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 70, 80, 40));

        jLabel78.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel78.setText("NOTA:");
        jPaNVentaPiso.add(jLabel78, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 70, 70, 40));

        jLabel79.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel79.setText("CLIENTE:");
        jPaNVentaPiso.add(jLabel79, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 10, 90, 40));
        jPaNVentaPiso.add(jSeparator11, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 130, 430, 10));

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

        jPaNVentaPiso.add(jScrollPane17, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 380, 1480, 490));

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

        jPaNVentaPiso.add(jScrollPane12, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 140, 690, 190));

        jLabel56.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel56.setText("Detalle de Venta:");
        jPaNVentaPiso.add(jLabel56, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 110, 140, 30));

        txtTotalVentaPiso.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtTotalVentaPiso.setForeground(new java.awt.Color(0, 51, 204));
        jPaNVentaPiso.add(txtTotalVentaPiso, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 250, 140, 60));

        jLabel80.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel80.setText("TOTAL $:");
        jPaNVentaPiso.add(jLabel80, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 210, 70, 40));
        jPaNVentaPiso.add(jSeparator12, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 360, 430, 10));

        jLabel81.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel81.setText("PRECIO $:");
        jPaNVentaPiso.add(jLabel81, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 70, 80, 40));

        jDFVentaPiso.setDateFormatString("dd/MM/yyyy");
        jDFVentaPiso.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPaNVentaPiso.add(jDFVentaPiso, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 10, 170, 40));

        jLayVentasPiso.add(jPaNVentaPiso, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1920, 950));

        jPanBusqVentasPiso.setBackground(new java.awt.Color(255, 255, 255));
        jPanBusqVentasPiso.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel82.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel82.setText("TIPO DE BUSQUEDA");
        jPanBusqVentasPiso.add(jLabel82, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 30, 180, 40));

        jCombOpcBusqVenta.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCombOpcBusqVenta.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "CLIENTE", "FECHA", "LAPSO FECHAS", "CLIENTE+FECHA", "CLIENTE+LAPSO FECHAS" }));
        jCombOpcBusqVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCombOpcBusqVentaActionPerformed(evt);
            }
        });
        jPanBusqVentasPiso.add(jCombOpcBusqVenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 30, 210, 40));

        jLabel83.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel83.setText("ELIJA CLIENTE:");
        jPanBusqVentasPiso.add(jLabel83, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 100, 140, 40));

        jCCliVentaPiso.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jCCliVentaPiso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCCliVentaPisoActionPerformed(evt);
            }
        });
        jPanBusqVentasPiso.add(jCCliVentaPiso, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 100, 210, 40));

        jLabel84.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel84.setText("ELIJA FECHA:");
        jPanBusqVentasPiso.add(jLabel84, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 160, 140, 40));

        jDateChoBVent1.setDateFormatString("dd/MM/yyyy");
        jDateChoBVent1.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanBusqVentasPiso.add(jDateChoBVent1, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 160, 210, 40));

        jLabel85.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel85.setText("ELIJA FECHA 2:");
        jPanBusqVentasPiso.add(jLabel85, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 160, 140, 40));

        jDatebusqVenta2.setDateFormatString("dd/MM/yyyy");
        jDatebusqVenta2.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanBusqVentasPiso.add(jDatebusqVenta2, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 160, 210, 40));

        jLabel20.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel20.setText("Tabla de Resultados");
        jPanBusqVentasPiso.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 230, 140, 40));

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

        jPanBusqVentasPiso.add(jScrollPane18, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 270, 1580, 610));
        jPanBusqVentasPiso.add(jSeparator13, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 250, 460, 10));

        jButton11.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jButton11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/search32pxcolor.png"))); // NOI18N
        jButton11.setText("BUSCAR");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });
        jPanBusqVentasPiso.add(jButton11, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 160, 180, 50));

        jLayVentasPiso.add(jPanBusqVentasPiso, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1920, 950));

        jPanVentasPiso.add(jLayVentasPiso, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 50, 1920, 960));

        paneAltas.addTab("    VENTAS DE PISO    ", jPanVentasPiso);

        jPanPagos.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLayeredPane2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "ENTRADAS", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 13))); // NOI18N
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabPed3.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabPed3.setForeground(new java.awt.Color(0, 51, 102));
        jLabPed3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabPed3.setText("PAGO DE PEDIDOS CLIENTES");
        jPanel2.add(jLabPed3, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 10, 260, 30));

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

        jPanel2.add(jScrollPane28, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 40, 720, 190));

        jLabPed4.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabPed4.setForeground(new java.awt.Color(0, 51, 102));
        jLabPed4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabPed4.setText("PAGO DE VENTAS DE PISO");
        jPanel2.add(jLabPed4, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 510, 260, 30));

        jLabPed5.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabPed5.setForeground(new java.awt.Color(0, 51, 102));
        jLabPed5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabPed5.setText("PAGO DE PRESTAMO A CLIENTES");
        jPanel2.add(jLabPed5, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 240, 260, 30));

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

        jPanel2.add(jScrollPane30, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 270, 720, 230));

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

        jPanel2.add(jScrollPane21, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 540, 720, 230));

        jLayeredPane2.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 20, 810, 830));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "SALIDAS", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 13))); // NOI18N
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabPed1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabPed1.setForeground(new java.awt.Color(0, 51, 102));
        jLabPed1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabPed1.setText("PAGO DE FLETES");
        jPanel3.add(jLabPed1, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 10, 260, 30));

        jTabPaysFletesDia.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jTabPaysFletesDia.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTabPaysFletesDia.setRowHeight(30);
        jScrollPane29.setViewportView(jTabPaysFletesDia);

        jPanel3.add(jScrollPane29, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 40, 710, 190));

        jLabPed6.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabPed6.setForeground(new java.awt.Color(0, 51, 102));
        jLabPed6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabPed6.setText("PAGO DE COMPRA A PROVEEDOR");
        jPanel3.add(jLabPed6, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 240, 260, 30));

        jTabPaysCompraProovedor.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jTabPaysCompraProovedor.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTabPaysCompraProovedor.setRowHeight(30);
        jScrollPane31.setViewportView(jTabPaysCompraProovedor);

        jPanel3.add(jScrollPane31, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 270, 710, 230));

        jLayeredPane2.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 20, 820, 830));

        jPanPagos.add(jLayeredPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 50, 1910, 950));

        jDFechPays.setDateFormatString("dd/MM/yyyy");
        jDFechPays.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanPagos.add(jDFechPays, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 10, 180, 40));

        jButFleteGuardar1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButFleteGuardar1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/search32pxcolor.png"))); // NOI18N
        jButFleteGuardar1.setText("    Buscar");
        jButFleteGuardar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButFleteGuardar1ActionPerformed(evt);
            }
        });
        jPanPagos.add(jButFleteGuardar1, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 9, 160, -1));

        jLabel100.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N
        jLabel100.setText("FECHA:");
        jPanPagos.add(jLabel100, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 10, 120, 40));

        paneAltas.addTab("    PAGOS    ", jPanPagos);

        getContentPane().add(paneAltas, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, 1920, 1040));

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
            jLabLetreroFletes.setVisible(false);
            
            DefaultTableModel dtmDel = (DefaultTableModel) jTabMayorAsignados.getModel(); //TableProducto es el nombre de mi tabla ;)
            int fil = dtmDel.getRowCount();
            if(fil > 0){
                for(int i = 0; i<fil;i++){
                    dtmDel.removeRow(0);                
                }//for
            }//ifFil
        }
    }//GEN-LAST:event_jRadBCompraProveActionPerformed

    private void jMnRealPayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMnRealPayActionPerformed
         int fila = jTablefiltrosBusq.getSelectedRow();
         String val ="",name="",tot="";
         if(fila == -1){
             JOptionPane.showMessageDialog(null,"Debe elegir una fila.");
         }else{
         val = jTablefiltrosBusq.getValueAt(fila, 0).toString();
         name=jTablefiltrosBusq.getValueAt(fila, 4).toString();
         tot=jTablefiltrosBusq.getValueAt(fila, 6).toString();
         System.out.println(val);
               vP = new VentaPiso(tot, val,"pagopedidocli");
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
        System.out.print(arr[0]+" butGuarda "+arr[1]);
        int filas= jTabDescVentaP.getRowCount(),
             column = jTabDescVentaP.getColumnCount();
        List<String> datos =  new ArrayList<String>();
        
        if(filas > 0){
            for(int i=0;i<filas;i++){
                datos.add(arr[0]);
                for(int j = 0;j<column;j++){
//                     System.out.print("["+jTabDescVentaP.getValueAt(i, j)+"]");
                     if(j==0){
                         datos.add(jTabDescVentaP.getValueAt(i, j).toString());
                     }
                     if(j==2){
                         datos.add(jTabDescVentaP.getValueAt(i, j).toString());
                     }
                     if(j==3){
                         datos.add(jTabDescVentaP.getValueAt(i, j).toString());
                     }
                }
                controlInserts.guardaDetalleVentPiso(datos);
                datos.clear();
            }
            
           String dateD=fn.getFecha(jDFVentaPiso);
    
           String[][] mat = controlInserts.matrizVentaPisoDia(dateD);
            jTVistaVentaPisoDia.setModel(new TModel(mat, cabvENTAp));

            jCombCliVentaP.setEnabled(true);
            txtNotaVentP.setEnabled(true);
        
          String val = arr[0],
         name=jCombCliVentaP.getSelectedItem().toString();
         System.out.println(val);
            //   vP = new VentaPiso(controlInserts.totalCompraProv(val), val);   
            
        vP = new VentaPiso(txtTotalVentaPiso.getText(),val,"pagoventapiso");
        vP.setVisible(true);
        vP.setEnabled(true);
        vP.validate();
        vP.jLabLetreroTransac.setText("Pago de venta No.");
               vP.txtidComp.setText(val);
               vP.jLabNameProv.setText("Cliente : ");
               vP.txtProveedorName.setText(name);
               
        txtTotalVentaPiso .setText("0.0");
        
       dtm = (DefaultTableModel) jTabDescVentaP.getModel(); //TableProducto es el nombre de mi tabla ;)
        for (int i = 0;filas>i; i++) {
            dtm.removeRow(0);
        }
        
        }else{
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
        
   */     switch (elije) {

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
            System.out.println("Envia: "+val);
            
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
        
        System.out.println("IdProveedor: " + id_cli + "\t idProd: " + codPro + "Fecha: " + datePed);

        List<String> dataCompra = new ArrayList<String>();
        if (cantidad.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar una cantidad");
        } else {
             if(!jPanSubastaOption.isVisible()){
                 descriSubasta="/";
             }
             if(nota.isEmpty()){
                 nota="/";
             }
            dtm = (DefaultTableModel) jTabCompraProved.getModel();
            dtmPrec = (DefaultTableModel) jTabPrecCompProved.getModel();
            
            int filas = dtm.getRowCount(), filasPrec = dtmPrec.getRowCount();
            int column = dtm.getColumnCount(), column2 = dtmPrec.getColumnCount();
            //JOptionPane.showMessageDialog(null, "filas= "+filas+"\n Col= "+column);
            if (filas == 0) {
                dtm.setRowCount(1);
                dtmPrec.setRowCount(1);

                jTabCompraProved.setModel(dtm);
                jTabPrecCompProved.setModel(dtmPrec);

                jTabCompraProved.setValueAt(cantidad, 0, Integer.parseInt(codPro)-1);
                jTabPrecCompProved.setValueAt(costoProd, 0, Integer.parseInt(codPro)-1);

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
                controlInserts.guardaCompraProv(dataCompra);

                jCElijaProovedor.setEnabled(false);
                txtNotaCompra.setEnabled(false);
            } else {
                jTabCompraProved.setValueAt(cantidad, 0, Integer.parseInt(codPro) - 1);
                jTabPrecCompProved.setValueAt(costoProd, 0, Integer.parseInt(codPro) - 1);
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
        dtm = (DefaultTableModel) jTabCompraProved.getModel();//tabla de precios
        dtmPrec = (DefaultTableModel) jTabPrecCompProved.getModel();

        int filas = dtm.getRowCount();
        int column = dtm.getColumnCount();
        String cantidad = txtCantPres.getText(), datePed = fn.getFecha(jDateFechCompraProv);

        if (filas > 0) {
            String[] ultimo = controlInserts.ultimoRegistroCompra();
            Object val = null;

            int opc = jCElijaProovedor.getSelectedIndex(),//index de PROVEEDOR
                    codP = jCombProductProv.getSelectedIndex();//index de producto

            String id_cli = idProoved.get(opc);

            //System.out.println("id_cli:"+id_cli+"\n id ped: "+ultimo[1]);
            //guardaDetallePrestamoProv(String numCP,String codP, int cantP,String costP)          
            for (int i = 0; i < column; i++) {
                val = jTabCompraProved.getValueAt(0, i);
                if (val != null) {//String numCP,String codP, int cantP,String costP
                    controlInserts.guardaDetalleCompraProv(ultimo[0], Integer.toString(i + 1), Integer.parseInt(jTabCompraProved.getValueAt(0, i).toString()), jTabPrecCompProved.getValueAt(0, i).toString());
                    //System.out.println("pos= "+(i+1)+" "+jTableCreaPedidos.getColumnName(i)+"\t-> val= "+jTableCreaPedidos.getValueAt(0, i).toString());
                }//if null
            }
        } else {
            JOptionPane.showMessageDialog(null, "No ha generado compra para el proveedor: " + jCElijaProovedor.getSelectedItem().toString());
        }
 /*       ListIterator<String> itrI = importes.listIterator();
        while (itrI.hasNext()) {
            System.out.println("Importes: "+itrI.next());
        }
    */    cargaComprasDia(datePed);//carga las compras del dia
        
         for (int i = 0;filas>i; i++) {//limpiar ambas tablas
            dtm.removeRow(0);
            dtmPrec.removeRow(0);
        }
        jCElijaProovedor.setEnabled(true);
        txtNotaCompra.setEnabled(true);
        importes.clear();
        jPanClientOption.setVisible(false);
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jMenPagoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenPagoActionPerformed
         int fila = jTabVistaComprasDia.getSelectedRow();
         String val ="",name="";
         if(fila == -1){
             JOptionPane.showMessageDialog(null,"Debe elegir una fila.");
         }else{
         val = jTabVistaComprasDia.getValueAt(fila, 0).toString();
         name=jTabVistaComprasDia.getValueAt(fila, 1).toString();
         //System.out.println(val);
               vP = new VentaPiso(controlInserts.totalCompraProv(val), val,"pagarcompraprovee");
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
            case 5 :
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
                System.out.println("prov May: "+id_cli);
                mat = controlInserts.matrizCompraProvOpc(elije, id_cli, "", "");
                jTabBusqCompraProv1.setModel(new TModel(mat, cabCompra));
            break;
            case 2: //MAYORISTA+FECHA
                System.out.println("prov May: "+id_cli);
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
         String val ="",name="";
         if(fila == -1){
             JOptionPane.showMessageDialog(null,"Debe elegir una fila.");
         }else{
         val = jTabBusqCompraProv1.getValueAt(fila, 0).toString();
         name=jTabBusqCompraProv1.getValueAt(fila, 4).toString();
         System.out.println(val);
               vP = new VentaPiso(controlInserts.totalCompraProv(val), val,"pagarcompraprovee");
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
        } else if(controlInserts.validaPrestamoProv(getIdPro)){//validamos si existe en prestamos de mercancia sin pagos
             jPanClientOption.setVisible(true);
            jPanSubastaOption.setVisible(false);
        }else{
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
                sumando =txtTotalVentaPiso.getText();
            
       // System.out.println("IdCliente: " + id_cli + "\t idProd: " + codPro + "Fecha: " + dateVentP);
          importes.add(txtImportVentaP.getText());
          
        List<String> dataVentPiso = new ArrayList<String>();
        if (cantidad.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar una cantidad");
        } else {
            dtm = (DefaultTableModel) jTabDescVentaP.getModel();
            
            int filas = dtm.getRowCount(), filasPrec = dtm.getRowCount();
            int column = dtm.getColumnCount(), column2 = dtm.getColumnCount();
            if(filas == 0){
                if(nota.isEmpty()){
                    nota="/";
                }                
                dataVentPiso.add(id_cli);
                dataVentPiso.add(dateVentP);
                dataVentPiso.add(nota);
                controlInserts.guardaVentaPiso(dataVentPiso);
            }
            dtm.addRow(new Object[]{codPro,jCombProdVentaP.getSelectedItem().toString(),cantidad,costoProd,importe});
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
        
        String total=txtTotalVentaPiso.getText(),
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
         String val ="",name="";
         if(fila == -1){
             JOptionPane.showMessageDialog(null,"Debe elegir una fila.");
         }else{
         val = jTablefiltrosBusqVent.getValueAt(fila, 0).toString();
         name=jTablefiltrosBusqVent.getValueAt(fila, 4).toString();
         //System.out.println(val);
               vP = new VentaPiso(controlInserts.totalVentaPiso(val), val,"pagoventapiso");
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
            
            if(jRPendFlete.isSelected())
                 contentL.add("0");
            else if(jRPagadopFlete.isSelected())
                contentL.add("1");
            
            contentL.add((var5.isEmpty()) ? "/" : var5);
            
            controlInserts.actualizaFlete(contentL,var1);
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
                if (jTabFletesDia.getValueAt(fila,6).toString().equals("PENDIENTE")) {
                    jRPendFlete.setSelected(true);
                } else if (jTabFletesDia.getValueAt(fila,6).toString().equals("PAGADO")) {
                    jRPagadopFlete.setSelected(true);
                }
                txtNotaFlete.setText(jTabFletesDia.getValueAt(fila, 7).toString());

        }        
    }//GEN-LAST:event_jTabFletesDiaMousePressed

    private void jButAltasElimina1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButAltasElimina1ActionPerformed
        String borrar=txtFolioFlete.getText();
        controlInserts.elimaRow("fleteenviado","id_fleteE",borrar);
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
        int elije = jCombOpcBusqFletes.getSelectedIndex(),opc=0;
        String foli="",id_cli="";
        if(jCfleteroOpc.isEditable()){
            foli=jCfleteroOpc.getSelectedItem().toString();
        }
        if(!jCfleteroOpc.isEditable()){
            opc=jCfleteroOpc.getSelectedIndex();
            id_cli=contenFletes.get(opc);
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
                mat = controlInserts.matrizFletesFilter(elije, id_cli, fech1,"");
                jTablefiltrosBusqflete.setModel(new TModel(mat, cabFilterFlete));
                break;
            case 5:
                //System.out.println("idCli: " + id_cli + "Fecha1: " + fech1 + "Fecha2: " + fech2);
                mat = controlInserts.matrizFletesFilter(elije, id_cli, fech1,fech2);
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
       String fechAs=fn.getFecha(jDCAsignacionDia);
       int fil1 = jTabVistaPedidosDia1.getRowCount(),fil2=jTabVistaComprasDia3.getRowCount(),fil3=jTabFletesDia1.getRowCount();
       dtm = (DefaultTableModel) jTabVistaPedidosDia1.getModel(); //TableProducto es el nombre de mi tabla ;)
       for (int i = 0;fil1>i; i++) {
            dtm.removeRow(0);
        }
        dtm =  (DefaultTableModel) jTabVistaComprasDia3.getModel();
        for (int i = 0;fil2>i; i++) {
            dtm.removeRow(0);
        }
        dtm =  (DefaultTableModel) jTabFletesDia1.getModel();
        for (int i = 0;fil3>i; i++) {
            dtm.removeRow(0);
        }
       cargaComprasDiaAsign(fechAs);//carga las compras del dia 
       cargaPedidosDiaAsign(fechAs);
       mostrarTablaFletesDiaAsign(fechAs);      
       String[][] mat = controlInserts.matFletEstados(fechAs);
       jTDetailAsign.setModel(new TModel(mat, cabEdoPed));
    }//GEN-LAST:event_jButton18ActionPerformed

    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton17ActionPerformed
//        limpiaAsign();
        int filPed = jTabVistaPedidosDia1.getSelectedRow(),
             filcomp = jTabVistaComprasDia3.getSelectedRow(),
             filflet = jTabFletesDia1.getSelectedRow();
             if(filPed != -1 && filcomp != -1 && filflet != -1){
                 String idPed = jTabVistaPedidosDia1.getValueAt(filPed,0).toString(),
                         idComp = jTabVistaComprasDia3.getValueAt(filcomp, 0).toString(),
                         idFlet = jTabFletesDia1.getValueAt(filflet, 0).toString();
                        txtidPedidoAsign.setText(idPed);
                        txtCompraAsign.setText(idComp);
                        txtFolioFleteAsign.setText(idFlet);
                 cargaDetailcompAsig(idComp);
                 
             }else{
                 JOptionPane.showMessageDialog(null, "Debe elegir una fila de cada tabla.");
             }
    }//GEN-LAST:event_jButton17ActionPerformed

    private void jButGuardDetailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButGuardDetailActionPerformed
        List<String> datos = new ArrayList <String>();
        
        int numfil = jTabDetailAsign.getRowCount(),numcol=jTabDetailAsign.getColumnCount(),contadorR=0,contadorB;
        Object var ="";
       for (int i = 0; i < numfil; i++) {
            System.out.print(i);
           for (int j = 0; j < numcol; j++) {
                var=jTabDetailAsign.getValueAt(i, j);
              if(var != null && !var.toString().isEmpty() ){
                  contadorR++;
                  if(j==0){
                      datos.add(txtCompraAsign.getText());
                      datos.add(txtidPedidoAsign.getText());
                      datos.add(jTabDetailAsign.getValueAt(i, j).toString());
                      datos.add(Integer.toString(i+1));
                      datos.add(txtFolioFleteAsign.getText());
                      datos.add(jTabDetailAsign.getValueAt(i, j+1).toString());
                      controlInserts.guardaRelOperaciones(datos);
                  }
        //            System.out.print(var+" -> ["+var.toString().length()+"]");
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
        int var = jTabVistaPedidosDia1.getSelectedRow(),col = jTabVistaPedidosDia1.getColumnCount(),difer=0;
        
        String id_Busq = jTabVistaPedidosDia1.getValueAt(var, 0).toString();
        
        Object val2 = null,totSum = null;
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 2; j++) {
                 jTabDetailAsignTotales.setValueAt("",i,j);
            }
        }
      //  String vari = jTabVistaPedidosDia1.getValueAt(var, 0).toString();
          for (int i = 2; i < col-1; i++) {
                val2 = jTabVistaPedidosDia1.getValueAt(var, i);
               if (val2 != null && !val2.toString().isEmpty()) {
                    //controlInserts.guardaDetallePedidoCli(ultimo[0], Integer.toString(i + 1), Integer.parseInt(jTableCreaPedidos.getValueAt(0, i).toString()));
                   //System.out.print("prodComp= "+(i)+" "+jTabVistaPedidosDia1.getColumnName(i)+"\t-> val= "+jTabVistaPedidosDia1.getValueAt(var, i).toString());
                  // System.out.println("envia: "+id_Busq+" -> "+(i-1));
                   totSum = controlInserts.calcAsignAPed(id_Busq, Integer.toString(i-1),"id_pedidoCli");
                  // System.out.println("regresa : "+totSum);
                    
                   if(totSum != null && !totSum.toString().isEmpty()){
                     difer= Integer.parseInt(val2.toString()) - Integer.parseInt(totSum.toString());
                     jTabDetailAsignTotales.setValueAt(difer, i-2, 1);
                      jTabDetailAsignTotales.setValueAt(totSum, i-2, 0);
                   }else{
                        jTabDetailAsignTotales.setValueAt(val2, i-2, 1);
                         jTabDetailAsignTotales.setValueAt(0, i-2, 0);
                   }
                    
                }//if null
            }

    }//GEN-LAST:event_jTabVistaPedidosDia1FocusLost

    private void jTabVistaComprasDia3FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTabVistaComprasDia3FocusLost
        int var = jTabVistaComprasDia3.getSelectedRow(),
              col =  jTabVistaComprasDia3.getColumnCount(),
              var2 = jTabVistaPedidosDia1.getSelectedRow(),
              col2 = jTabVistaPedidosDia1.getSelectedColumnCount(),
                difer =0;
        boolean bandera = false;
        String concid = "";
        Object val = null,val2=null,totSum = null;
 //VERIFICAR SI LA MERCANCIA ES DEL MISMO TIPO DEL PEDIDO       
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 2; j++) {
                 jTabDetailAsignTotales1.setValueAt("",i,j);
            }
        }
        
        for (int i = 2; i < col-1; i++) {
                val = jTabVistaComprasDia3.getValueAt(var, i);
                val2 = jTabVistaPedidosDia1.getValueAt(var2, i);
               if (val != null  && val2 != null && !val.toString().isEmpty() && !val2.toString().isEmpty()) {
                    //controlInserts.guardaDetallePedidoCli(ultimo[0], Integer.toString(i + 1), Integer.parseInt(jTableCreaPedidos.getValueAt(0, i).toString()));
                //    System.out.print("prodComp= "+(i)+" "+jTabVistaComprasDia3.getColumnName(i)+"\t-> val= "+jTabVistaComprasDia3.getValueAt(var, i).toString());
                    bandera = true;                  
              //      System.out.print("/t prodPed= "+(i)+" "+jTabVistaPedidosDia1.getColumnName(i)+"\t-> val= "+jTabVistaPedidosDia1.getValueAt(var2, i).toString());
                }//if null
            }
        if(!bandera){
            JOptionPane.showMessageDialog(null, "Mercancia no es del mismo tipo del pedido seleccionado; \n Verfique por favor.");
        }else{
           String id_Busq =jTabVistaComprasDia3.getValueAt(var, 0).toString();
               for (int i = 2; i < col-1; i++) {
                val2 = jTabVistaComprasDia3.getValueAt(var, i);
               if (val2 != null && !val2.toString().isEmpty()) {
                    //controlInserts.guardaDetallePedidoCli(ultimo[0], Integer.toString(i + 1), Integer.parseInt(jTableCreaPedidos.getValueAt(0, i).toString()));
                   //System.out.print("prodComp= "+(i)+" "+jTabVistaPedidosDia1.getColumnName(i)+"\t-> val= "+jTabVistaPedidosDia1.getValueAt(var, i).toString());
                  // System.out.println("CP envia: "+id_Busq+" -> "+(i-1));
                   totSum = controlInserts.calcAsignAPed(id_Busq, Integer.toString(i-1),"id_compraProveed");
                   //System.out.println("CP regresa : "+totSum);
                    
                   if(totSum != null && !totSum.toString().isEmpty()){
                     difer= Integer.parseInt(val2.toString()) - Integer.parseInt(totSum.toString());
                     jTabDetailAsignTotales1.setValueAt(difer, i-2, 1);
                      jTabDetailAsignTotales1.setValueAt(totSum, i-2, 0);
                   }else{
                        jTabDetailAsignTotales1.setValueAt(val2, i-2, 1);
                         jTabDetailAsignTotales1.setValueAt(0, i-2, 0);
                   }
                }//if null
            }
        }
    }//GEN-LAST:event_jTabVistaComprasDia3FocusLost

    private void jMItDetailVerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMItDetailVerActionPerformed
        int opc = jTabVistaPedidosDia1.getSelectedRow();      
        if(opc > -1){
            String param = jTabVistaPedidosDia1.getValueAt(opc, 0).toString(),nombre = jTabVistaPedidosDia1.getValueAt(opc, 1).toString();
             fP = new FormacionPedido (Integer.parseInt(param),nombre);
             fP.setEnabled(true);
             fP.setVisible(true);
             fP.validate();
        }               
    }//GEN-LAST:event_jMItDetailVerActionPerformed

    private void txtFolioFleteFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFolioFleteFocusLost
        String verifFol = txtFolioFlete.getText();
        if(verifFol.isEmpty()){
            JOptionPane.showMessageDialog(null, "Folio no debe estar vacio");
        }else{
            if(controlInserts.validaRelCompPed(verifFol,"fleteenviado")){
                jLabLetreroFletes.setVisible(true);
            }else{
                jLabLetreroFletes.setVisible(false);
            }
        }
    }//GEN-LAST:event_txtFolioFleteFocusLost

    private void jTabVistaComprasDiaMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTabVistaComprasDiaMousePressed
         if (evt.getClickCount() > 1) {
            int fila = jTabVistaComprasDia.getSelectedRow();
            String val = jTabVistaComprasDia.getValueAt(fila, 0).toString();
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
            }
        }  
    }//GEN-LAST:event_jTabVistaComprasDiaMousePressed

    private void jMIPaysPrestaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMIPaysPrestaActionPerformed
         int fila = jTabBusqPrestProv.getSelectedRow();
         String val ="",name="";
         if(fila == -1){
             JOptionPane.showMessageDialog(null,"Debe elegir una fila.");
         }else{
         val = jTabBusqPrestProv.getValueAt(fila, 0).toString();
         name=jTabBusqPrestProv.getValueAt(fila, 4).toString();
               vP = new VentaPiso(controlInserts.totalCreditProv(val), val,"pagocreditprooved");
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
         String val ="",name="",costo="";
         if(fila == -1){
             JOptionPane.showMessageDialog(null,"Debe elegir una fila.");
         }else{
         val = jTabFletesDia.getValueAt(fila, 0).toString();
         name=jTabFletesDia.getValueAt(fila, 1).toString();
         costo=jTabFletesDia.getValueAt(fila, 5).toString();
         //System.out.println(val);
               vP = new VentaPiso(costo, val,"pagoflete");
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
         String val ="",name="",costo="";
         if(fila == -1){
             JOptionPane.showMessageDialog(null,"Debe elegir una fila.");
         }else{
         val = jTablefiltrosBusqflete.getValueAt(fila, 0).toString();
         name=jTablefiltrosBusqflete.getValueAt(fila, 1).toString();
         costo=jTablefiltrosBusqflete.getValueAt(fila, 5).toString();
         //System.out.println(val);
               vP = new VentaPiso(costo, val,"pagoflete");
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
         String val ="",name="",costo="";
         List<String> datos = new ArrayList <String>();
         if(fila == -1){
             JOptionPane.showMessageDialog(null,"Debe elegir una fila.");
         }else{
             for (int i = 0; i < cols; i++) {
                 datos.add( jTablefiltrosBusqVent.getValueAt(fila, i).toString());
             }
             val=jTablefiltrosBusqVent.getValueAt(fila, 0).toString();
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
         String val ="",name="",costo="";
         List<String> datos = new ArrayList <String>();
         if(fila == -1){
             JOptionPane.showMessageDialog(null,"Debe elegir una fila.");
         }else{
             for (int i = 0; i < cols; i++) {
                 datos.add( jTVistaVentaPisoDia.getValueAt(fila, i).toString());
             }
             val=jTVistaVentaPisoDia.getValueAt(fila, 0).toString();
         //System.out.println(val);
               ventDP = new detalleVP(Integer.parseInt(val));
               ventDP.setVisible(true);
               ventDP.setEnabled(true);
               ventDP.validate();
               ventDP.recibeListData(datos);
               
         }        // TODO add your handling code here:
    }//GEN-LAST:event_jTVistaVentaPisoDiaMousePressed

    private void jButAltasEliminaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButAltasEliminaActionPerformed
        String elim = txtIdParam.getText();
        int opc = jComboAltas.getSelectedIndex() ;
             switch (opc){
            case 0:               
                controlInserts.elimaRow("clientepedidos","id_cliente",elim);
             break;
            case 1:
                 controlInserts.elimaRow("proveedor","id_Proveedor",elim);
             break;
            case 2:
                controlInserts.elimaRow("empleado","id_Empleado",elim);
             break;
             case 3:
                controlInserts.elimaRow("fletero","id_Fletero",elim);
             break;
            };
        
         jButaltasGuardar.setEnabled(true);
        limpiaCamposAltaCli();
    }//GEN-LAST:event_jButAltasEliminaActionPerformed

    private void AgregarMayoreoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AgregarMayoreoActionPerformed
    String datePed = fn.getFecha(jDateFechCompraProv);
    int opc = jCElijaProovedor.getSelectedIndex(),
            id_comp = jTabVistaComprasDia.getSelectedRow();

            if(id_comp > -1){
                String id_cli = idProoved.get(opc),
                guardacom = jTabVistaComprasDia.getValueAt(id_comp, 0).toString(),
                importe = jTabVistaComprasDia.getValueAt(id_comp, 10).toString();
               
                if(controlInserts.validaIsMayorista(id_cli)){
                    if(controlInserts.validaCompAsignadas(guardacom,datePed,"comp+fech")){
                    JOptionPane.showMessageDialog(null, "Compra ya ha sido asignada");
                }else{
                    controlInserts.guardaMayorista(id_cli,guardacom,datePed);
                    String[][] mat = controlInserts.matAsignaMayoristas(id_cli,datePed);
                    jTabMayorAsignados.setModel(new TModel(mat, cabMayAsignados));
                     jTextField1.setText(controlInserts.sumMayorista(id_cli,datePed));
                     
                    jTabMayorAsignados.getColumnModel().getColumn(0).setMaxWidth(0);
                    jTabMayorAsignados.getColumnModel().getColumn(0).setMinWidth(0);
                    jTabMayorAsignados.getColumnModel().getColumn(0).setPreferredWidth(0);
                    
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "Solo puede asigranr compras a Proveedor Mayorista");
                }//else valida si es mayorista
            }else{
                JOptionPane.showMessageDialog(null, "Debe elegir una compra de proveedor");
            }
    }//GEN-LAST:event_AgregarMayoreoActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
          if(jCheckBox1.isSelected())
            llenacomboMayoristas(0);
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void jMI_ElimASIGNAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMI_ElimASIGNAActionPerformed
        int elije = jTabMayorAsignados.getSelectedRow();
        if(elije > -1){
            String var =  jTabMayorAsignados.getValueAt(elije, 0).toString();
            controlInserts.elimaRow("compramayoreo","id_compraMay",var);
        }else{
            JOptionPane.showMessageDialog(null, "Debe elegir una opcion de la tabla.");
        }
    }//GEN-LAST:event_jMI_ElimASIGNAActionPerformed

    private void jCElijaProovedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCElijaProovedorActionPerformed
      int tam = jCElijaProovedor.getItemCount();
        //validamos si se ha seleccionado checkMayoristas y si existe algun elemnto en el MenuItem
        if(jCheckBox1.isSelected() && tam > 0){
            String datePed = fn.getFecha(jDateFechCompraProv);
            int opc = jCElijaProovedor.getSelectedIndex();
            String id_cli = idProoved.get(opc);
            String[][] mat = controlInserts.matAsignaMayoristas(id_cli,datePed);
            jTabMayorAsignados.setModel(new TModel(mat, cabMayAsignados));
            jTextField1.setText(controlInserts.sumMayorista(id_cli,datePed));
            
            jTabMayorAsignados.getColumnModel().getColumn(0).setMaxWidth(0);
            jTabMayorAsignados.getColumnModel().getColumn(0).setMinWidth(0);
            jTabMayorAsignados.getColumnModel().getColumn(0).setPreferredWidth(0);            
        }
        
    }//GEN-LAST:event_jCElijaProovedorActionPerformed

    private void txtCantCreaPedidoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCantCreaPedidoKeyReleased
        if(evt.getKeyCode() == evt.VK_ENTER) {
            jButton9.doClick();
        } 
    }//GEN-LAST:event_txtCantCreaPedidoKeyReleased

    private void txtNotePedidoCliKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNotePedidoCliKeyReleased
        if(evt.getKeyCode() == evt.VK_ENTER) {
            jButton9.doClick();
        }
    }//GEN-LAST:event_txtNotePedidoCliKeyReleased

    private void jTableCreaPedidosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTableCreaPedidosKeyReleased
        if(evt.getKeyCode() == evt.VK_ENTER) {
            jButGuardaPedidodia.doClick();
        }
    }//GEN-LAST:event_jTableCreaPedidosKeyReleased

    private void txtImportCompKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtImportCompKeyReleased
        if(evt.getKeyCode() == evt.VK_ENTER) {
            jButton1.doClick();
        }
    }//GEN-LAST:event_txtImportCompKeyReleased

    private void txtNotaCompraKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNotaCompraKeyReleased
        if(evt.getKeyCode() == evt.VK_ENTER) {
            jButton1.doClick();
        }        // TODO add your handling code here:
    }//GEN-LAST:event_txtNotaCompraKeyReleased

    private void jTabCompraProvedKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTabCompraProvedKeyPressed
        if(evt.getKeyCode() == evt.VK_ENTER) {
            jButton13.doClick();
        }
    }//GEN-LAST:event_jTabCompraProvedKeyPressed

    private void jTabPrecCompProvedKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTabPrecCompProvedKeyPressed
       if(evt.getKeyCode() == evt.VK_ENTER) {
            jButton13.doClick();
        }
    }//GEN-LAST:event_jTabPrecCompProvedKeyPressed

    private void txtImportPresKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtImportPresKeyPressed
        if(evt.getKeyCode() == evt.VK_ENTER) {
            jButton7.doClick();
        }
    }//GEN-LAST:event_txtImportPresKeyPressed

    private void textANotaPrestProvKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textANotaPrestProvKeyPressed
        if(evt.getKeyCode() == evt.VK_ENTER) {
            jButton7.doClick();
        }
    }//GEN-LAST:event_textANotaPrestProvKeyPressed

    private void txtImportVentaPKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtImportVentaPKeyPressed
       if(evt.getKeyCode() == evt.VK_ENTER) {
            jButton8.doClick();
        }
    }//GEN-LAST:event_txtImportVentaPKeyPressed

    private void txtNotaVentPKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNotaVentPKeyPressed
       if(evt.getKeyCode() == evt.VK_ENTER) {
            jButton8.doClick();
        } 
    }//GEN-LAST:event_txtNotaVentPKeyPressed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        try{
        Runtime runtime = Runtime.getRuntime();//Escritorio
        //File backupFile = new File(String.valueOf(RealizarBackupMySQL.getSelectedFile().toString())+".sql");
       // File backupFile = new File("C:\\Users\\monit\\Documents\\CENTRAL DE ABASTOS\\autoGenerate.sql");
        File backupFile = new File(controlInserts.cargaConfig()+"autoGenerate"+fn.setDateActualGuion()+".sql");
        InputStreamReader irs;
        BufferedReader br;
            try (FileWriter fw = new FileWriter(backupFile)) {
                Process child = runtime.exec("C:\\xampp\\mysql\\bin\\mysqldump --routines=TRUE --password=0ehn4TNU5I --user=root --databases admindcr");// | gzip> respadmin_DCR.sql.gz
                irs = new InputStreamReader(child.getInputStream());
                br = new BufferedReader(irs);
                String line;
                while( (line=br.readLine()) != null ) {
                    fw.write(line + "\n");
                }   
            }
        irs.close();
        br.close();

        JOptionPane.showMessageDialog(null, "Archivo generado correctamente.","Verificar",JOptionPane. INFORMATION_MESSAGE);
        }catch(Exception e){
        JOptionPane.showMessageDialog(null, "Error no se genero el archivo por el siguiente motivo:"+e.getMessage(), "Verificar",JOptionPane.ERROR_MESSAGE);
        }
        
    }//GEN-LAST:event_jButton6ActionPerformed

    public Image getIconImage() {
        Image retValue = Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("image/icons8_customer_32px_1.png"));
        return retValue;
    }

    //carga de detalle PedidioAsignacion
         public void cargaDetailcompAsig(String opc){
        Connection cn = con2.conexion();
        String sql ="";
              sql = "SELECT * FROM detailcompraprooved WHERE id_compraP = '"+opc+"'";           
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
                                        jTabDetailAsign.setValueAt(rs.getInt(x+1), rs.getInt(x)-1, 0);//valor,fila,columna
                                        jTabDetailAsign.setValueAt(rs.getString(x+2), rs.getInt(x)-1, 1);//valor,fila,columna restamos uno porque el indice del jtable comienza en cero
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
                
                if(opc ==1){//si eligio proveedores
                    datos[0] = rs.getString(1);
                    datos[1] = rs.getString(2);
                    datos[2] = rs.getString(3);
                    datos[3] = rs.getString(4);
                    datos[4] = rs.getString(5);
                    datos[5] = rs.getString(8);
                    if (rs.getString(5).equals("1")) {
                        datos[4] = "ACTIVO";
                    } else if ( rs.getString(5).equals("0")) {
                        datos[4] = "INACTIVO";
                    }
                    if ( rs.getString(8).equals("1")) {
                        datos[5] = "MAYORISTA";
                    } else if ( rs.getString(8).equals("0")) {
                        datos[5] = "MINORISTA";
                    }
                }else{
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
            
             switch(opc){
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
       public  void consultDetailCompra(int opc,int fila){
       Connection cn = con2.conexion();
       int cantColumnas=0, cantFilas=0,temporal=0,bandera=0;
        String sql ="",sql2="";
              sql = "SELECT * FROM detailcompraprooved WHERE id_compraP = '"+opc+"'";    
              sql2 ="SELECT compraprooved.id_compraProve,IF(proveedor.nombreP='SUBASTA',compraprooved.descripcionSubasta,proveedor.nombreP),SUM(detailcompraprooved.cantCajasC),"
                      + "SUM(detailcompraprooved.cantCajasC*detailcompraprooved.precCajaC)\n" +
                "FROM\n" +
                "	proveedor\n" +
                "INNER JOIN\n" +
                "	compraprooved \n" +
                "ON\n" +
                "	compraprooved.id_ProveedorC = proveedor.id_Proveedor AND compraprooved.id_compraProve = '"+opc+"'\n" +
                "INNER JOIN \n" +
                "	detailcompraprooved\n" +
                "ON\n" +
                "	detailcompraprooved.id_compraP = compraprooved.id_compraProve;";
            Statement st = null;
            ResultSet rs = null;    
            try {
                st = cn.createStatement();
                rs = st.executeQuery(sql);
               // System.out.print("Filas: "+cantFilas+"\tColumnas: "+cantColumnas+"\n");
                while(rs.next())
                {//es necesario el for para llenar dinamicamente la lista, ya que varia el numero de columnas de las tablas
                        for (int x=1;x<= rs.getMetaData().getColumnCount();x++) {
                            if(x==3){//valor,fila,columna
                                jTabVistaComprasDia.setValueAt(rs.getInt(x+1), fila, rs.getInt(x)+1);//se le suma 1 por las columnas id,nombre de la jTable
                            }//System.out.print("["+x+"]"+" -> "+rs.getString(x));                   
                        }//for
                }//while
                
                st=null;rs=null;
                st = cn.createStatement();
                rs = st.executeQuery(sql2);
                while(rs.next()){
                    
                    
                     jTabVistaComprasDia.setValueAt(rs.getInt(1), fila,0);
                     
                      if(controlInserts.validaRelCompPed(Integer.toString(rs.getInt(1)),"compramayoreo"))
                        colorePD.add(fila);
                      
                     jTabVistaComprasDia.setValueAt(rs.getString(2), fila, 1);
                     jTabVistaComprasDia.setValueAt(rs.getString(3), fila, 9);
                     jTabVistaComprasDia.setValueAt(rs.getString(4), fila, 10);
                }
            } catch (SQLException ex) {
                Logger.getLogger(interno1.class.getName()).log(Level.SEVERE, null, ex);
            }finally{               
             try {        
                 if(st != null) st.close();                
                 if(cn !=null) cn.close();
             } catch (SQLException ex) {
                 JOptionPane.showMessageDialog(null,ex.getMessage()); 
             }
         }//finally  
    }//regresaDatos
    
       //FUNCION PARA LLENAS LA TABLA DE COMPRAS DEL DIA
       private void cargaComprasDia(String fech){
       String[][] arre = controlInserts.consultCompra(fech);
       if(arre.length>0){
       tabCompras = (DefaultTableModel) jTabVistaComprasDia.getModel();
       int filas = tabCompras.getRowCount(), filasPrec = tabCompras.getRowCount();
            if (filas > 0) {
                   for (int i = 0;filas>i; i++) {
                        tabCompras.removeRow(0);
                    }
            }
            tabCompras.setRowCount(arre.length);//CREAMOS EL NUMERO DE FILAS SEGUN LA MATRIZ DEVUELTA
            
            jTabVistaComprasDia.setModel(tabCompras);//agrego el modelo creado con el numero de filas devuelto
                
            for (int j = 0; j < arre.length; j++) {
                for (int k = 0; k < arre[0].length; k++) {
                   if(k==0){
                   consultDetailCompra(Integer.parseInt(arre[j][k]),j);//envia matriz con id_compra,id_proveedor,fila
                   }
                }
         }
            
           ColorCelda c = new ColorCelda();
            c.arrIntRowsIluminados = controlInserts.fnToArray(colorePD);
            jTabVistaComprasDia.setDefaultRenderer(Object.class, c);
            colorePD.clear();
       }else{
           JOptionPane.showMessageDialog(null, "No hay compras del dia");
       }         
       }//Fin cargaComprasDia
    /*++++}}CODIGO PARA ASIGNACION DE MERCANCIAS Y FLETES*/
       private void cargaComprasDiaAsign(String fech){
           
       String[][] arre = controlInserts.consultCompra(fech);
       if(arre.length>0){
            jLaComp.setVisible(false);
        tabCompras = (DefaultTableModel) jTabVistaComprasDia3.getModel();
        int filas = tabCompras.getRowCount(), filasPrec = tabCompras.getRowCount();
             if (filas > 0) {
                   for (int i = 0;filas>i; i++) {
                        tabCompras.removeRow(0);
                    }
            }
            tabCompras.setRowCount(arre.length);//CREAMOS EL NUMERO DE FILAS SEGUN LA MATRIZ DEVUELTA
            
            jTabVistaComprasDia3.setModel(tabCompras);//agrego el modelo creado con el numero de filas devuelto
                
            for (int j = 0; j < arre.length; j++) {
                for (int k = 0; k < arre[0].length; k++) {
                   if(k==0){
                   consultDetailCompraAsign(Integer.parseInt(arre[j][k]),j);//envia matriz con id_compra,id_proveedor,fila
                   }
                }
         }
            ColorCelda c = new ColorCelda();
            c.arrIntRowsIluminados = controlInserts.fnToArray(coloreA);
            jTabVistaComprasDia3.setDefaultRenderer(Object.class, c);
            coloreA.clear();
                          
       }else{
           jLaComp.setVisible(true);
           jLaComp.setText("No hay compras del día");
//           JOptionPane.showMessageDialog(null, "No hay compras del dia");
       }         
       }//Fin cargaComprasDia
       
     public  void consultDetailCompraAsign(int opc,int fila){
       Connection cn = con2.conexion();
       int cantColumnas=0, cantFilas=0,temporal=0,bandera=0;
        String sql ="",sql2="";
              sql = "SELECT * FROM detailcompraprooved WHERE id_compraP = '"+opc+"'";    
              sql2 ="SELECT compraprooved.id_compraProve,proveedor.nombreP,SUM(detailcompraprooved.cantCajasC)\n" +
                "FROM\n" +
                "	proveedor\n" +
                "INNER JOIN\n" +
                "	compraprooved \n" +
                "ON\n" +
                "	compraprooved.id_ProveedorC = proveedor.id_Proveedor AND compraprooved.id_compraProve = '"+opc+"'\n" +
                "INNER JOIN \n" +
                "	detailcompraprooved\n" +
                "ON\n" +
                "	detailcompraprooved.id_compraP = compraprooved.id_compraProve;";
            Statement st = null;
            ResultSet rs = null;    
            try {
                st = cn.createStatement();
                rs = st.executeQuery(sql);
               // System.out.print("Filas: "+cantFilas+"\tColumnas: "+cantColumnas+"\n");
                while(rs.next())
                {//es necesario el for para llenar dinamicamente la lista, ya que varia el numero de columnas de las tablas
                        for (int x=1;x<= rs.getMetaData().getColumnCount();x++) {
                            if(x==3){//valor,fila,columna
                                jTabVistaComprasDia3.setValueAt(rs.getInt(x+1), fila, rs.getInt(x)+1);//se le suma 1 por las columnas id,nombre de la jTable
                            }//System.out.print("["+x+"]"+" -> "+rs.getString(x));                   
                        }//for
                }//while
                
                st=null;rs=null;
                st = cn.createStatement();
                rs = st.executeQuery(sql2);
                while(rs.next()){
                    
                     jTabVistaComprasDia3.setValueAt(rs.getInt(1), fila,0);
                    if(controlInserts.validaRelCompPed(Integer.toString(rs.getInt(1)),"id_compraProveed"))
                        coloreA.add(fila);
                         // jTabVistaComprasDia3.setValueAt("ASIGNADO", fila,0);
                     jTabVistaComprasDia3.setValueAt(rs.getString(2), fila, 1);
                     jTabVistaComprasDia3.setValueAt(rs.getString(3), fila, 9);
                }
            } catch (SQLException ex) {
                Logger.getLogger(interno1.class.getName()).log(Level.SEVERE, null, ex);
            }finally{               
             try {        
                 if(st != null) st.close();                
                 if(cn !=null) cn.close();
             } catch (SQLException ex) {
                 JOptionPane.showMessageDialog(null,ex.getMessage()); 
             }
         }//finally  
    }//regresaDatos

     //CARGA PEDIDOS DEL DIA VISTA DE DETALLE
      private void cargaPedidosDiaAsign(String fech){
       String[][] arre = controlInserts.consultPedidoAsign(fech);
       if(arre.length>0){
           jLabPed.setVisible(false);
       tabCompras = (DefaultTableModel) jTabVistaPedidosDia1.getModel();
       int filas = tabCompras.getRowCount(), filasPrec = tabCompras.getRowCount();
            if (filas > 0) {
                   for (int i = 0;filas>i; i++) {
                        tabCompras.removeRow(0);
                    }
            }
            tabCompras.setRowCount(arre.length);//CREAMOS EL NUMERO DE FILAS SEGUN LA MATRIZ DEVUELTA
            
            jTabVistaPedidosDia1.setModel(tabCompras);//agrego el modelo creado con el numero de filas devuelto
                
            for (int j = 0; j < arre.length; j++) {
                for (int k = 0; k < arre[0].length; k++) {
                   if(k==0){
                   consultDetailPedidosAsign(Integer.parseInt(arre[j][k]),j);//envia matriz con id_compra,id_proveedor,fila
                   }
                }
         }
            ColorCelda c1 = new ColorCelda();
             c1.arrIntRowsIluminados = controlInserts.fnToArray(coloreB);
            jTabVistaPedidosDia1.setDefaultRenderer(Object.class, c1);
            
             ListIterator<Integer> itr=coloreB.listIterator();
            while (itr.hasNext()) {
            System.out.println("ColoreB -> "+itr.next());
            }
             coloreB.clear();
       }else{
           jLabPed.setVisible(true);
           jLabPed.setText("No hay pedidos del día");
           
           //JOptionPane.showMessageDialog(null, "No hay compras del dia");
       }         
       }//Fin cargaPedidosDia
         
     public  void consultDetailPedidosAsign(int opc,int fila){
       Connection cn = con2.conexion();
       int cantColumnas=0, cantFilas=0,temporal=0,bandera=0;
        String sql ="",sql2="";
              sql = "SELECT * FROM detailpedidio WHERE id_pedidioD = '"+opc+"'";    
              sql2 ="SELECT\n" +
                "	pedidocliente.id_pedido,clientepedidos.nombre,SUM(detailpedidio.cantidadCajas)\n" +
                "	FROM\n" +
                "	clientepedidos\n" +
                "	INNER JOIN\n" +
                "	pedidocliente\n" +
                "	ON\n" +
                " 	pedidocliente.id_clienteP = clientepedidos.id_cliente AND pedidocliente.id_pedido = '"+opc+"'\n" +
                "	INNER JOIN\n" +
                "	detailpedidio\n" +
                "	ON\n" +
                "	detailpedidio.id_PedidioD = pedidocliente.id_pedido;";
            Statement st = null;
            ResultSet rs = null;    
            try {
                st = cn.createStatement();
                rs = st.executeQuery(sql);
               // System.out.print("Filas: "+cantFilas+"\tColumnas: "+cantColumnas+"\n");
                while(rs.next())
                {//es necesario el for para llenar dinamicamente la lista, ya que varia el numero de columnas de las tablas
                        for (int x=1;x<= rs.getMetaData().getColumnCount();x++) {
                            if(x==3){//valor,fila,columna
                                jTabVistaPedidosDia1.setValueAt(rs.getInt(x+1), fila, rs.getInt(x)+1);//se le suma 1 por las columnas id,nombre de la jTable
                            }//System.out.print("["+x+"]"+" -> "+rs.getString(x));                   
                        }//for
                }//while
                
                st=null;rs=null;
                st = cn.createStatement();
                rs = st.executeQuery(sql2);
                while(rs.next()){
                     jTabVistaPedidosDia1.setValueAt(rs.getInt(1), fila,0);
                        if(controlInserts.validaRelCompPed(Integer.toString(rs.getInt(1)), "id_pedidoCli"))
                            coloreB.add(fila);
                            
                     jTabVistaPedidosDia1.setValueAt(rs.getString(2), fila, 1);
                     jTabVistaPedidosDia1.setValueAt(rs.getString(3), fila, 9);
                }
            } catch (SQLException ex) {
                Logger.getLogger(interno1.class.getName()).log(Level.SEVERE, null, ex);
            }finally{               
             try {        
                 if(st != null) st.close();                
                 if(cn !=null) cn.close();
             } catch (SQLException ex) {
                 JOptionPane.showMessageDialog(null,ex.getMessage()); 
             }
         }//finally  
    }//regresaDatos

/**codigo para ventas de piso*/
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
/**codigo para ventas de piso*/
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
            consul = "SELECT fleteEnviado.id_fleteE, fletero.nombreF,fleteEnviado.fechaFlete,fleteEnviado.choferFlete,\n" +
                "	fleteEnviado.trocaFlete,fleteEnviado.costoFlete,fleteEnviado.status,fleteEnviado.recivioFlete\n" +
                "FROM\n" +
                "	fleteEnviado\n" +
                "INNER JOIN\n" +
                "	fletero\n" +
                "ON fletero.id_Fletero = fleteEnviado.id_FleteroE AND fleteEnviado.fechaFlete = '"+fech+"';";
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
                if ( rs.getString(7).equals("0")) {
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
            consul = "SELECT fleteEnviado.id_fleteE, fletero.nombreF,fleteEnviado.choferFlete,\n" +
                "	fleteEnviado.trocaFlete,fleteEnviado.costoFlete,fleteEnviado.status,fleteEnviado.recivioFlete\n" +
                "FROM\n" +
                "	fleteEnviado\n" +
                "INNER JOIN\n" +
                "	fletero\n" +
                "ON fletero.id_Fletero = fleteEnviado.id_FleteroE AND fleteEnviado.fechaFlete = '"+fech+"';";
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
                if(controlInserts.validaRelCompPed(rs.getString(1),"id_fleteP"))
                        coloreF.add(i);
                
                datos[1] = rs.getString(2);
              
                datos[2] = rs.getString(3);
                datos[3] = rs.getString(4);
                datos[4] = rs.getString(5);

                datos[5] = rs.getString(6);
                if ( rs.getString(6).equals("0")) {
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
        
        if(i>0){
            jLabFlet.setVisible(false);
            ColorCelda cF = new ColorCelda();
            cF.arrIntRowsIluminados = controlInserts.fnToArray(coloreF);
            jTabFletesDia1.setDefaultRenderer(Object.class, cF);
            coloreF.clear();
        }else{
            jLabFlet.setVisible(true);
            jLabFlet.setText("Sin fletes del día");
//            System.out.println("Nada que colorear");
        }
    }//termina mostrartablaFletesAsign
     
        
      //*** CODIGO PARA CARGAR LOS PAGOS DEL DIA
        private void llenaTabPayDayPedidos(String fech){
            //Llena matriz de pagos pedido clis
            String[][] matFlet = controlInserts.regresaPaysFech("pagopedidocli",fech);
            jTabPayPeds.setModel(new TModel(matFlet,cabPaysDay));
            //Llena matriz de pago de prestamos 
            String[][] mat0 = controlInserts.regresaPaysFech("pagocreditprooved",fech);
            jTabPayPrestamosProv.setModel(new TModel(mat0,cabPaysDay));
            //Llena matriz de pago ventas piso
            String[][] mat = controlInserts.regresaPaysFech("pagoventapiso",fech);
            jTabVentPisoPays.setModel(new TModel(mat,cabPaysDay));
           //Llena matriz de pagos a fleteros
            String[][] mat1 = controlInserts.regresaPaysFech("pagoflete",fech);
            jTabPaysFletesDia.setModel(new TModel(mat1,cabPaysDay));
                    //pagos de compras a proveedor
           String[][] mat2 = controlInserts.regresaPaysFech("pagarcompraprovee",fech);
            jTabPaysCompraProovedor.setModel(new TModel(mat2,cabPaysDay));
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
    private void limpiacompraProved(){
        txtCantidadCompra.setText("");
        txtPrecCompraProv.setText("");
        txtImportComp.setText("");
        txtCamSubastaCompra.setText("");
        txtNotaCompra.setText("");
    }
    private void limpiaVentaPiso(){
        txtCantVentaPiso.setText("");
        txtPrecProdVentaP.setText("");
        txtImportVentaP.setText("");
        txtNotaVentP.setText("");
    }
    private void limpiaFlete(){
        txtFolioFlete.setText("");
        txtChoferFlete.setText("");
        txtUnidFlete.setText("");
        txtCostoFlete.setText("");
        txtNotaFlete.setText("");
        jRPendFlete.setSelected(true);
        jLabLetreroFletes.setVisible(true);
    }
    
    void limpiaAsign(){
        
        txtFolioFleteAsign.setText("");
        txtCompraAsign.setText("");
        txtidPedidoAsign.setText("");
//        txtTotCajasAsign.setText("");
  //      txtCostoAsign.setText("");
        for(int i =0; i<6;i++){
        for(int j=0;j<2;j++){
                    jTabDetailAsign.setValueAt("", i, j);
                    jTabDetailAsignTotales.setValueAt("", i, j);
                    jTabDetailAsignTotales1.setValueAt("", i, j);
                    
        }
    }
    }

    public void cargaUser(String param){
        jTextField17.setText(param);
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
    private javax.swing.JLabel jLaComp;
    private javax.swing.JLabel jLabAdeudaProoved;
    private javax.swing.JLabel jLabBNumerador;
    private javax.swing.JLabel jLabFlet;
    private javax.swing.JLabel jLabLetreroFletes;
    private javax.swing.JLabel jLabPed;
    private javax.swing.JLabel jLabPed1;
    private javax.swing.JLabel jLabPed3;
    private javax.swing.JLabel jLabPed4;
    private javax.swing.JLabel jLabPed5;
    private javax.swing.JLabel jLabPed6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel100;
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
    private javax.swing.JLabel jLabel3;
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
    private javax.swing.JTable jTabBusqCompraProv1;
    private javax.swing.JTable jTabBusqPrestProv;
    private javax.swing.JTable jTabCompraProved;
    private javax.swing.JTable jTabDescVentaP;
    private javax.swing.JTable jTabDetailAsign;
    private javax.swing.JTable jTabDetailAsignTotales;
    private javax.swing.JTable jTabDetailAsignTotales1;
    private javax.swing.JTable jTabFletesDia;
    private javax.swing.JTable jTabFletesDia1;
    private javax.swing.JTable jTabMayorAsignados;
    private javax.swing.JTable jTabPayPeds;
    private javax.swing.JTable jTabPayPrestamosProv;
    private javax.swing.JTable jTabPaysCompraProovedor;
    private javax.swing.JTable jTabPaysFletesDia;
    private javax.swing.JTable jTabPedidosDiaView;
    private javax.swing.JTable jTabPrecCompProved;
    private javax.swing.JTable jTabPreciosPrest;
    private javax.swing.JTable jTabPrestamoProovedores;
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
