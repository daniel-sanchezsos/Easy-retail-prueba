//Paquete
package ptovta;

//Importaciones
import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.awt.Cursor;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.Locale;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import static ptovta.Princip.bIdle;




/*Clase que muestra todas las cots de los proyectos*/
public class Cots extends javax.swing.JFrame 
{
    /*Declara variables de instancia*/
    public static int       iContFi;

    /*Variable que contiene el borde actual*/
    private Border          bBordOri;

    /*Para controlar si seleccionar todo o deseleccionarlo de la tabla*/
    private boolean         bSel;
    
    /*Contador para modificar tabla*/
    private int             iContCellEd;
    
    /*Delcara las variables originales de la tabla 1*/
    private String          sCodOri;
    private String          sNoSerOri;
    private String          sEmpOri;    
    private String          sObservOri;
    private String          sSubTotOri;
    private String          sImpueOri;
    private String          sTotOri;
    private String          sTotDescOri;
    private String          sDescripOri;
    private String          sFCreaOri;
    private String          sFModOri;
    private String          sFVencOri;
    private String          sFEntOri;
    private String          sFDocOri;
    private String          sSucOri;
    private String          sCajOri;
    private String          sEstacOri;
    private String          sNomEstacOri;
    private String          sEstadOri;    
    private String          sMotivOri;    
    private String          sVtaOri;    
    private String          sSerFacOri;
    private String          sFolOri;
    
    /*Delcara las variables originales de la tabla 2*/
    private String          sCantOri;
    private String          sProdOri;
    private String          sUnidOri;
    private String          sListOri;
    private String          sPreOri; 
    private String          sDescOri;   
    private String          sAlmaOri;    
    private String          sImpoOri;    
    private String          sImpueImpoOri;
    private String          sImpoDescOri;
    private String          sSerProdOri;
    private String          sComenSerOri;    
    private String          sKitOri;
    private String          sTallOri;
    private String          sColoOri;
    private String          sBackOri;
    private String          sCostOri;
    
    //Thread para mostrar las partidas de la cotización con retardo
    private Thread          thCargPart;         
    
    
    /*Constructor sin argumentos*/
    public Cots(java.util.ArrayList<Boolean> permisos) 
    {
        /*Inicaliza los componentes gráficos*/
        initComponents();
        
        //revisa Permisos 
        jBNew.setEnabled(permisos.get(0));
        jBAbr.setEnabled(permisos.get(1));
        jBVer.setEnabled(permisos.get(2));
        jBCan.setEnabled(permisos.get(3));
        jBMail.setEnabled(permisos.get(4));
        jBVta.setEnabled(permisos.get(5));
        
        
        
        
        //se saca acomoda los componentes dependiendo de la resolucion
        vMyLayout();
        
        /*Para que las tablas tengan scroll horisontal*/
        jTab1.setAutoResizeMode(0);
        jTab2.setAutoResizeMode(0);
        
        /*Establece el tamaño de las columnas de la tabla de encabezados*/      
        jTab1.getColumnModel().getColumn(1).setPreferredWidth(120);
        jTab1.getColumnModel().getColumn(3).setPreferredWidth(120);
        jTab1.getColumnModel().getColumn(4).setPreferredWidth(200);
        jTab1.getColumnModel().getColumn(5).setPreferredWidth(200);
        jTab1.getColumnModel().getColumn(8).setPreferredWidth(120);
        jTab1.getColumnModel().getColumn(9).setPreferredWidth(120);
        jTab1.getColumnModel().getColumn(10).setPreferredWidth(200);
        jTab1.getColumnModel().getColumn(11).setPreferredWidth(130);
        jTab1.getColumnModel().getColumn(12).setPreferredWidth(130);
        jTab1.getColumnModel().getColumn(13).setPreferredWidth(130);
        jTab1.getColumnModel().getColumn(14).setPreferredWidth(130);
        jTab1.getColumnModel().getColumn(16).setPreferredWidth(150);
        
        
        
        /*Establece el tamaño de las columnas de la tabla de partidas*/        
        jTab2.getColumnModel().getColumn(1).setPreferredWidth(120);
        jTab2.getColumnModel().getColumn(3).setPreferredWidth(140);
        jTab2.getColumnModel().getColumn(6).setPreferredWidth(400);
        jTab2.getColumnModel().getColumn(7).setPreferredWidth(120);
        jTab2.getColumnModel().getColumn(13).setPreferredWidth(120);
        jTab2.getColumnModel().getColumn(14).setPreferredWidth(120);
        jTab2.getColumnModel().getColumn(15).setPreferredWidth(130);
        jTab2.getColumnModel().getColumn(16).setPreferredWidth(130);
        jTab2.getColumnModel().getColumn(18).setPreferredWidth(130);
        jTab2.getColumnModel().getColumn(19).setPreferredWidth(130);
        jTab2.getColumnModel().getColumn(20).setPreferredWidth(130);
        
        /*Para que no se muevan las columnas*/
        jTab1.getTableHeader().setReorderingAllowed(false);
        jTab2.getTableHeader().setReorderingAllowed(false);
        
        /*Esconde el link de ayuda*/
        jLAyu.setVisible(false);
        
        /*Centra la ventana*/
        this.setLocationRelativeTo(null);
        
        /*Establece el titulo de la ventana con El usuario, la fecha y hora*/                
        this.setTitle("Cotizaciones, Usuario: <" + Login.sUsrG + "> " + Login.sFLog);        
        
        /*Inicialmente esta deseleccionada la tabla*/
        bSel        = false;
        
        //Establece el ícono de la forma
        Star.vSetIconFram(this);
        
        /*Inicializa el contador de filas*/
        iContFi  = 1;
                
        /*Para que la tabla este ordenada al mostrarce y al dar clic en el nombre de la columna*/
        TableRowSorter trs = new TableRowSorter<>((DefaultTableModel)jTab1.getModel());
        jTab1.setRowSorter(trs);
        trs.setSortsOnUpdates(true);
        
        /*Pon el foco del teclado en el botón de nueva cotización*/
        jBNew.grabFocus();
        
        /*Activa en la tabla que se usen normamente las teclas de tabulador*/
        jTab1.setFocusTraversalKeys(java.awt.KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, null);
        jTab1.setFocusTraversalKeys(java.awt.KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, null);
        
        /*Agrega todas las cotizaciones de la base de datos a la tabla de cotizaciones*/
        (new Thread()
        {
            @Override
            public void run()
            {
                vCargCots("");                
            }
            
        }).start();
        
         /*Crea el listener para cuando se cambia de selección en la tabla de la cotización*/
        jTab1.getSelectionModel().addListSelectionListener(new ListSelectionListener() 
        {
            @Override
            public void valueChanged(ListSelectionEvent lse) 
            {             
                //Si no hay selección entonces regresa
                if(jTab1.getSelectedRow()==-1)
                    return;

                //Detiene el thread para que no cargue las partidas
                if(thCargPart!=null)
                    thCargPart.interrupt();
                
                //Crea el thread retrasado para la forma
                thCargPart = new Thread()
                {
                    @Override
                    public void run()
                    {
                        //Duerme el thread 
                        try
                        {
                            Thread.sleep(200);
                        }
                        catch(InterruptedException expnInterru)
                        {
                            return;
                        }                            
                        
                        /*Carga todas las partidas de la cotización*/
                        vLoadParts();               
                    }
                };      
                thCargPart.start();                                                                            
            }
        });
        
        /*Incializa el contador del cell editor*/
        iContCellEd = 1;
        
        /*Crea el listener para cuando se modifica algo en la tabla 1*/
        PropertyChangeListener pro = new PropertyChangeListener() 
        {
            @Override
            public void propertyChange(PropertyChangeEvent event) 
            {
                /*Obtiene la fila seleccionada*/                
                if(jTab1.getSelectedRow()==-1)
                    return;
                
                /*Obtén la propiedad que a sucedio en el control*/
                String pr = event.getPropertyName();                                
                                                
                /*Si el evento fue por entrar en una celda de la tabla*/
                if("tableCellEditor".equals(pr)) 
                {
                    /*Si el contador de cell editor está en 1 entonces que lea el valor original que estaba ya*/
                    if(iContCellEd==1)
                    {
                        /*Obtiene todos los datos originales*/
                        sCodOri         = jTab1.getValueAt(jTab1.getSelectedRow(), 1).toString();
                        sNoSerOri       = jTab1.getValueAt(jTab1.getSelectedRow(), 2).toString();
                        sEmpOri         = jTab1.getValueAt(jTab1.getSelectedRow(), 3).toString();                        
                        sObservOri      = jTab1.getValueAt(jTab1.getSelectedRow(), 4).toString();
                        sSubTotOri      = jTab1.getValueAt(jTab1.getSelectedRow(), 5).toString();
                        sImpueOri       = jTab1.getValueAt(jTab1.getSelectedRow(), 6).toString();
                        sTotOri         = jTab1.getValueAt(jTab1.getSelectedRow(), 7).toString();
                        sTotDescOri     = jTab1.getValueAt(jTab1.getSelectedRow(), 8).toString();
                        sDescripOri     = jTab1.getValueAt(jTab1.getSelectedRow(), 9).toString();
                        sFCreaOri       = jTab1.getValueAt(jTab1.getSelectedRow(), 10).toString();
                        sFModOri        = jTab1.getValueAt(jTab1.getSelectedRow(), 11).toString();
                        sFVencOri       = jTab1.getValueAt(jTab1.getSelectedRow(), 12).toString();
                        sFEntOri        = jTab1.getValueAt(jTab1.getSelectedRow(), 13).toString();
                        sFDocOri        = jTab1.getValueAt(jTab1.getSelectedRow(), 14).toString();
                        sSucOri         = jTab1.getValueAt(jTab1.getSelectedRow(), 15).toString();
                        sCajOri         = jTab1.getValueAt(jTab1.getSelectedRow(), 16).toString();
                        sEstacOri       = jTab1.getValueAt(jTab1.getSelectedRow(), 17).toString();
                        sNomEstacOri    = jTab1.getValueAt(jTab1.getSelectedRow(), 18).toString();
                        sEstadOri       = jTab1.getValueAt(jTab1.getSelectedRow(), 19).toString();
                        sMotivOri       = jTab1.getValueAt(jTab1.getSelectedRow(), 20).toString();
                        sVtaOri         = jTab1.getValueAt(jTab1.getSelectedRow(), 21).toString();
                        sSerFacOri      = jTab1.getValueAt(jTab1.getSelectedRow(), 22).toString();
                        sFolOri         = jTab1.getValueAt(jTab1.getSelectedRow(), 23).toString();                        
                        
                        /*Aumenta el contador para saber que va de salida*/
                        ++iContCellEd;
                    }
                    /*Else, el contador de cell editor es 2, osea que va de salida*/
                    else
                    {
                        /*Coloca los valores originales que tenian*/
                        jTab1.setValueAt(sCodOri,       jTab1.getSelectedRow(), 1);                        
                        jTab1.setValueAt(sNoSerOri,     jTab1.getSelectedRow(), 2);                        
                        jTab1.setValueAt(sEmpOri,       jTab1.getSelectedRow(), 3);                                                
                        jTab1.setValueAt(sObservOri,    jTab1.getSelectedRow(), 4);                        
                        jTab1.setValueAt(sSubTotOri,    jTab1.getSelectedRow(), 5);                        
                        jTab1.setValueAt(sImpueOri,     jTab1.getSelectedRow(), 6);                        
                        jTab1.setValueAt(sTotOri,       jTab1.getSelectedRow(), 7);                        
                        jTab1.setValueAt(sTotDescOri,   jTab1.getSelectedRow(), 8);                        
                        jTab1.setValueAt(sDescripOri,   jTab1.getSelectedRow(), 9);                        
                        jTab1.setValueAt(sFCreaOri,     jTab1.getSelectedRow(), 10);                        
                        jTab1.setValueAt(sFModOri,      jTab1.getSelectedRow(), 11);                        
                        jTab1.setValueAt(sFVencOri,     jTab1.getSelectedRow(), 12);
                        jTab1.setValueAt(sFEntOri,      jTab1.getSelectedRow(), 13);
                        jTab1.setValueAt(sFDocOri,      jTab1.getSelectedRow(), 14);
                        jTab1.setValueAt(sSucOri,       jTab1.getSelectedRow(), 15);                        
                        jTab1.setValueAt(sCajOri,       jTab1.getSelectedRow(), 16);                        
                        jTab1.setValueAt(sEstacOri,     jTab1.getSelectedRow(), 17);                        
                        jTab1.setValueAt(sNomEstacOri,  jTab1.getSelectedRow(), 18);                        
                        jTab1.setValueAt(sEstadOri,     jTab1.getSelectedRow(), 19);
                        jTab1.setValueAt(sMotivOri,     jTab1.getSelectedRow(), 20);
                        jTab1.setValueAt(sVtaOri,       jTab1.getSelectedRow(), 21);
                        jTab1.setValueAt(sSerFacOri,    jTab1.getSelectedRow(), 22);
                        jTab1.setValueAt(sFolOri,       jTab1.getSelectedRow(), 23);                        
                                                
                        /*Resetea el celleditor*/
                        iContCellEd = 1;
                    }                                            
                                            
                }/*Fin de if("tableCellEditor".equals(property)) */
                
            }/*Fin de public void propertyChange(PropertyChangeEvent event) */            
        };        
        
        /*Establece el listener para la tabla 1*/
        jTab1.addPropertyChangeListener(pro);
        
        /*Crea el listener para cuando se modifica algo en la tabla 2*/
        pro = new PropertyChangeListener() 
        {
            @Override
            public void propertyChange(PropertyChangeEvent event) 
            {
                /*Obtén la propiedad que a sucedio en el control*/
                String pr = event.getPropertyName();                                
                
                /*Si no hay selección en la tabla entonces regresa*/
                if(jTab2.getSelectedRow()==-1)
                    return;
                
                /*Si el evento fue por entrar en una celda de la tabla*/
                if("tableCellEditor".equals(pr)) 
                {
                    /*Si el contador de cell editor está en 1 entonces que lea el valor original que estaba ya*/
                    if(iContCellEd==1)
                    {
                        /*Obtiene todos los datos originales*/                        
                        sCodOri         = jTab2.getValueAt(jTab2.getSelectedRow(), 1).toString();
                        sCantOri        = jTab2.getValueAt(jTab2.getSelectedRow(), 2).toString();
                        sProdOri        = jTab2.getValueAt(jTab2.getSelectedRow(), 3).toString();
                        sUnidOri        = jTab2.getValueAt(jTab2.getSelectedRow(), 4).toString();
                        sListOri        = jTab2.getValueAt(jTab2.getSelectedRow(), 5).toString();
                        sDescripOri     = jTab2.getValueAt(jTab2.getSelectedRow(), 6).toString();
                        sPreOri         = jTab2.getValueAt(jTab2.getSelectedRow(), 7).toString();
                        sDescOri        = jTab2.getValueAt(jTab2.getSelectedRow(), 8).toString();
                        sAlmaOri        = jTab2.getValueAt(jTab2.getSelectedRow(), 9).toString();                                                
                        sImpoOri        = jTab2.getValueAt(jTab2.getSelectedRow(), 10).toString();
                        sImpueOri       = jTab2.getValueAt(jTab2.getSelectedRow(), 11).toString();
                        sImpueImpoOri   = jTab2.getValueAt(jTab2.getSelectedRow(), 12).toString();                                                
                        sImpoDescOri    = jTab2.getValueAt(jTab2.getSelectedRow(), 13).toString();                                                
                        sSerProdOri     = jTab2.getValueAt(jTab2.getSelectedRow(), 14).toString();
                        sComenSerOri    = jTab2.getValueAt(jTab2.getSelectedRow(), 15).toString();
                        sKitOri         = jTab2.getValueAt(jTab2.getSelectedRow(), 16).toString();
                        sTallOri        = jTab2.getValueAt(jTab2.getSelectedRow(), 17).toString();
                        sColoOri        = jTab2.getValueAt(jTab2.getSelectedRow(), 18).toString();
                        sBackOri        = jTab2.getValueAt(jTab2.getSelectedRow(), 19).toString();
                        sCostOri        = jTab2.getValueAt(jTab2.getSelectedRow(), 20).toString();
                       
                        /*Aumenta el contador para saber que va de salida*/
                        ++iContCellEd;
                    }
                    /*Else, el contador de cell editor es 2, osea que va de salida*/
                    else
                    {
                        /*Coloca los valores originales que tenian*/
                        jTab2.setValueAt(sCodOri,       jTab2.getSelectedRow(), 1);                        
                        jTab2.setValueAt(sCantOri,      jTab2.getSelectedRow(), 2);                        
                        jTab2.setValueAt(sProdOri,      jTab2.getSelectedRow(), 3);                        
                        jTab2.setValueAt(sUnidOri,      jTab2.getSelectedRow(), 4);
                        jTab2.setValueAt(sListOri,      jTab2.getSelectedRow(), 5);
                        jTab2.setValueAt(sDescripOri,   jTab2.getSelectedRow(), 6);                        
                        jTab2.setValueAt(sPreOri,       jTab2.getSelectedRow(), 7);                        
                        jTab2.setValueAt(sDescOri,      jTab2.getSelectedRow(), 8);                                                
                        jTab2.setValueAt(sAlmaOri,      jTab2.getSelectedRow(), 9);                                                                        
                        jTab2.setValueAt(sImpoOri,      jTab2.getSelectedRow(), 10);                        
                        jTab2.setValueAt(sImpueOri,     jTab2.getSelectedRow(), 11);                        
                        jTab2.setValueAt(sImpueImpoOri, jTab2.getSelectedRow(), 12);                        
                        jTab2.setValueAt(sImpoDescOri,  jTab2.getSelectedRow(), 13);                        
                        jTab2.setValueAt(sSerProdOri,   jTab2.getSelectedRow(), 14);                        
                        jTab2.setValueAt(sComenSerOri,  jTab2.getSelectedRow(), 15);
                        jTab2.setValueAt(sKitOri,       jTab2.getSelectedRow(), 16);
                        jTab2.setValueAt(sTallOri,      jTab2.getSelectedRow(), 17);
                        jTab2.setValueAt(sColoOri,      jTab2.getSelectedRow(), 18);
                        jTab2.setValueAt(sBackOri,      jTab2.getSelectedRow(), 19);
                        jTab2.setValueAt(sCostOri,      jTab2.getSelectedRow(), 20);
                                                
                        /*Resetea el celleditor*/
                        iContCellEd = 1;
                    }                                            
                                            
                }/*Fin de if("tableCellEditor".equals(property)) */
                
            }/*Fin de public void propertyChange(PropertyChangeEvent event) */            
        };        
        
        /*Establece el listener para la tabla 2*/
        jTab2.addPropertyChangeListener(pro);
        
    }/*Fin de public Cots() */
    
    private void vMyLayout()
    {
        //Se calcula la resolucion
         int iW= java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
         //se determina el valor maximo de resolucion
         if(iW>1450)
             iW=1450;
         
         //Se calcula el tamaño que tendra la ventana con un rango mas pequeño
         iW=iW-(int)(iW*.1);
         this.setSize(iW, 540);
         
         //tamaño del panel para los margenes
         jP1.setSize(iW-27, 490);
        
        //se coloca el panel de los botones
        jPanel1.setLocation(iW-jPanel1.getSize().width-30, 10);
        //se le da el tamaño adecuado a los otros dos paneles que contiene las tablas
        jPanel2.setSize(iW-jPanel1.getSize().width-50,201);
        jPanel3.setSize(iW-jPanel1.getSize().width-50,200);
        //se localiza el boton de mostrar todo y el de seleccionar todo
        jBTod.setLocation(iW-jPanel1.getSize().width-31-jBTod.getSize().width, 10);
        jBMosT.setLocation(iW-jPanel1.getSize().width-31-jBMosT.getSize().width,jBMosT.getLocation().y);
        
        //se le da el tamaño a la barra de busqueda
        jTBusc.setSize(jPanel2.getSize().width-jBMosT.getSize().width-jBBusc.getSize().width-1,jTBusc.getSize().height);
        
        //Acomodar los mensajes de error
        jLNot.setSize((int)(jPanel2.getSize().width*.3),jLNot.getSize().height);
        jLNotCli.setLocation(jPanel2.getLocation().x+(int)(jPanel2.getSize().width*.3),jLNotCli.getLocation().y);
        jLNotCli.setSize(jPanel2.getSize().width-(int)(jPanel2.getSize().width*.3),jLNot.getSize().height);
        
        
    }//fin de private void vMyLayout()
    
    /*Carga todas las partidas de la cotización*/
    private void vLoadParts()
    {                
        /*Borra la tabla de partidas*/
        DefaultTableModel dm = (DefaultTableModel)jTab2.getModel();
        dm.setRowCount(0);
        
        /*Obtiene el código de la cotización*/
        String sCot     = jTab1.getValueAt(jTab1.getSelectedRow(), 1).toString();
                
        //Abre la base de datos                             
        Connection  con = Star.conAbrBas(true, false);

        //Si hubo error entonces regresa
        if(con==null)
            return;
        
        //Declara variables de la base de datos
        Statement   st;
        ResultSet   rs;        
        String      sQ; 

        /*Obtiene el nombre del cliente*/
        try
        {
            sQ = "SELECT emps.NOM FROM cots LEFT OUTER JOIN emps ON CONCAT_WS('', emps.SER, emps.CODEMP) = cots.CODEMP WHERE codcot = '" + sCot + "'";                        
            st = con.createStatement();
            rs = st.executeQuery(sQ);
            /*Si hay datos entonces coloca el nombre del cliente en el label*/            
            if(rs.next())            
                jLNotCli.setText(rs.getString("nom"));
            else
                jLNotCli.setText("");
        }
        catch(SQLException expnSQL)
        {
            //Procesa el error y regresa
            Star.iErrProc(this.getClass().getName() + " " + expnSQL.getMessage(), Star.sErrSQL, expnSQL.getStackTrace(), con);                                
            return;
        }  
        
        /*Obtiene el estado de la cotización*/
        try
        {
            sQ = "SELECT estad FROM cots WHERE codcot = '" + sCot + "'";                        
            st = con.createStatement();
            rs = st.executeQuery(sQ);
            /*Si hay datos entonces*/            
            if(rs.next())
            {                
                /*Coloca el label rojo el estado de la cotización*/
                if(rs.getString("estad").compareTo("CO")==0)                
                    jLNot.setText("COTIZACION: CONFIRMADA");
                else if(rs.getString("estad").compareTo("CA")==0)                
                    jLNot.setText("COTIZACION: CANCELADA");
                else if(rs.getString("estad").compareTo("PE")==0)                
                    jLNot.setText("COTIZACION: PENDIENTE");
            }                        
        }
        catch(SQLException expnSQL)
        {
            //Procesa el error y regresa
            Star.iErrProc(this.getClass().getName() + " " + expnSQL.getMessage(), Star.sErrSQL, expnSQL.getStackTrace(), con);                                
            return;
        }  
        
        /*Obtiene las partidas de la cotización*/
        try
        {
            sQ = "SELECT * FROM partcot WHERE codcot = '" + sCot + "'";                        
            st = con.createStatement();
            rs = st.executeQuery(sQ);
            /*Si hay datos*/
            int iContFiParts    = 1;
            while(rs.next())
            {                
                /*Obtiene los totales*/
                String sPre             = rs.getString("pre");                
                String sImpoImp         = rs.getString("impueimpo");
                String sImpo            = rs.getString("impo");
                String sImpoDesc        = rs.getString("impodesc");
                String sCost            = rs.getString("cost");
                
                /*Si es kit entonces coloca el texto*/
                String sKit             = "No";
                if(rs.getString("eskit").compareTo("1")==0)
                    sKit                = "Si";
                
                /*Dales formato de moneda a los totales*/                                
                NumberFormat n  = NumberFormat.getCurrencyInstance(new Locale("es","MX"));
                double dCant    = Double.parseDouble(sPre);                                
                sPre            = n.format(dCant);                
                dCant           = Double.parseDouble(sImpoImp);                                
                sImpoImp        = n.format(dCant);
                dCant           = Double.parseDouble(sImpo);                                
                sImpo           = n.format(dCant);
                dCant           = Double.parseDouble(sImpoDesc);                                
                sImpoDesc       = n.format(dCant);
                dCant           = Double.parseDouble(sCost);                                
                sCost           = n.format(dCant);
                
                /*Carga los datos en la tabla*/                
                DefaultTableModel te    = (DefaultTableModel)jTab2.getModel();
                Object nu[]             = {iContFiParts, rs.getString("codcot"), rs.getString("cant"), rs.getString("prod"), rs.getString("unid"), rs.getString("list"), rs.getString("descrip"), sPre, rs.getString("desc1"), rs.getString("alma"), sImpo, rs.getString("impueval"), sImpoImp, sImpoDesc, rs.getString("serprod"), rs.getString("comenser"), sKit, rs.getString("tall"), rs.getString("colo"), rs.getString("fentre"), sCost};
                te.addRow(nu);
                
                /*Aumenta el contador de filas de las partidas*/
                ++iContFiParts;                
            
            }/*Fin de while(rs.next())*/                        
        }
        catch(SQLException expnSQL)
        {
            //Procesa el error y regresa
            Star.iErrProc(this.getClass().getName() + " " + expnSQL.getMessage(), Star.sErrSQL, expnSQL.getStackTrace(), con);                                
            return;
        }  
	        
        //Cierra la base de datos
        Star.iCierrBas(con);
        
    }/*Fin de private void vLoadParts()*/
    
        
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jP1 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jBGenPDF = new javax.swing.JButton();
        jBNew = new javax.swing.JButton();
        jBAbr = new javax.swing.JButton();
        jBVer = new javax.swing.JButton();
        jBCan = new javax.swing.JButton();
        jBMail = new javax.swing.JButton();
        jBActua = new javax.swing.JButton();
        jBPDF = new javax.swing.JButton();
        jBDirCots = new javax.swing.JButton();
        jBVta = new javax.swing.JButton();
        jBSal = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jBMosT = new javax.swing.JButton();
        jTBusc = new javax.swing.JTextField();
        jBBusc = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jBTab1 = new javax.swing.JButton();
        jBTab2 = new javax.swing.JButton();
        jLAyu = new javax.swing.JLabel();
        jBTod = new javax.swing.JButton();
        jLNot = new javax.swing.JLabel();
        jLNotCli = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTab1 = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTab2 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1400, 602));
        setResizable(false);
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                formMouseDragged(evt);
            }
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                formMouseMoved(evt);
            }
        });
        addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                formMouseWheelMoved(evt);
            }
        });
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });
        getContentPane().setLayout(null);

        jP1.setBackground(new java.awt.Color(255, 255, 255));
        jP1.setMinimumSize(new java.awt.Dimension(1292, 300));
        jP1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jP1KeyPressed(evt);
            }
        });
        jP1.setLayout(null);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(130, 420));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jBGenPDF.setBackground(new java.awt.Color(255, 255, 255));
        jBGenPDF.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jBGenPDF.setForeground(new java.awt.Color(0, 102, 0));
        jBGenPDF.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/genpdf.png"))); // NOI18N
        jBGenPDF.setText("Generar PDF");
        jBGenPDF.setToolTipText("Generar PDF de cotización");
        jBGenPDF.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jBGenPDF.setNextFocusableComponent(jBNew);
        jBGenPDF.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jBGenPDFMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jBGenPDFMouseExited(evt);
            }
        });
        jBGenPDF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBGenPDFActionPerformed(evt);
            }
        });
        jBGenPDF.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jBGenPDFKeyPressed(evt);
            }
        });
        jPanel1.add(jBGenPDF, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 130, 30));

        jBNew.setBackground(new java.awt.Color(255, 255, 255));
        jBNew.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jBNew.setForeground(new java.awt.Color(0, 102, 0));
        jBNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/agre8.png"))); // NOI18N
        jBNew.setText("Nueva");
        jBNew.setToolTipText("Nueva cotizaciòn (Ctrl+N). Presionando (Alt y este Botón) Puedes Tomar la Cotización Seleccionada como Machote para una Nueva cotización");
        jBNew.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jBNew.setNextFocusableComponent(jBVer);
        jBNew.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jBNewMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jBNewMouseExited(evt);
            }
        });
        jBNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBNewActionPerformed(evt);
            }
        });
        jBNew.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jBNewKeyPressed(evt);
            }
        });
        jPanel1.add(jBNew, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 30, 130, 30));

        jBAbr.setBackground(new java.awt.Color(255, 255, 255));
        jBAbr.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jBAbr.setForeground(new java.awt.Color(0, 102, 0));
        jBAbr.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/abr.png"))); // NOI18N
        jBAbr.setText("Abrir");
        jBAbr.setToolTipText("Abrir cotizaciòn(es) (Ctrl+A)");
        jBAbr.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jBAbr.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jBAbrMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jBAbrMouseExited(evt);
            }
        });
        jBAbr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBAbrActionPerformed(evt);
            }
        });
        jBAbr.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jBAbrKeyPressed(evt);
            }
        });
        jPanel1.add(jBAbr, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 60, 130, 30));

        jBVer.setBackground(new java.awt.Color(255, 255, 255));
        jBVer.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jBVer.setForeground(new java.awt.Color(0, 102, 0));
        jBVer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/ver.png"))); // NOI18N
        jBVer.setText("Ver");
        jBVer.setToolTipText("Ver Cotización(es)");
        jBVer.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jBVer.setNextFocusableComponent(jBCan);
        jBVer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jBVerMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jBVerMouseExited(evt);
            }
        });
        jBVer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBVerActionPerformed(evt);
            }
        });
        jBVer.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jBVerKeyPressed(evt);
            }
        });
        jPanel1.add(jBVer, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 90, 130, 30));

        jBCan.setBackground(new java.awt.Color(255, 255, 255));
        jBCan.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jBCan.setForeground(new java.awt.Color(0, 102, 0));
        jBCan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/can.png"))); // NOI18N
        jBCan.setText("Cancelar");
        jBCan.setToolTipText("Reenviar Cotización(es) (F6)");
        jBCan.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jBCan.setNextFocusableComponent(jBMail);
        jBCan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jBCanMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jBCanMouseExited(evt);
            }
        });
        jBCan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBCanActionPerformed(evt);
            }
        });
        jBCan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jBCanKeyPressed(evt);
            }
        });
        jPanel1.add(jBCan, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 120, 130, 30));

        jBMail.setBackground(new java.awt.Color(255, 255, 255));
        jBMail.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jBMail.setForeground(new java.awt.Color(0, 102, 0));
        jBMail.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/corrs.png"))); // NOI18N
        jBMail.setText("Reenviar");
        jBMail.setToolTipText("Reenviar Cotización(es) (F6)");
        jBMail.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jBMail.setNextFocusableComponent(jBActua);
        jBMail.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jBMailMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jBMailMouseExited(evt);
            }
        });
        jBMail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBMailActionPerformed(evt);
            }
        });
        jBMail.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jBMailKeyPressed(evt);
            }
        });
        jPanel1.add(jBMail, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 150, 130, 30));

        jBActua.setBackground(new java.awt.Color(255, 255, 255));
        jBActua.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jBActua.setForeground(new java.awt.Color(0, 102, 0));
        jBActua.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/actualizar.png"))); // NOI18N
        jBActua.setText("Actualizar");
        jBActua.setToolTipText("Actualizar Registros (F5)");
        jBActua.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jBActua.setNextFocusableComponent(jBPDF);
        jBActua.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jBActuaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jBActuaMouseExited(evt);
            }
        });
        jBActua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBActuaActionPerformed(evt);
            }
        });
        jBActua.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jBActuaKeyPressed(evt);
            }
        });
        jPanel1.add(jBActua, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 180, 130, 30));

        jBPDF.setBackground(new java.awt.Color(255, 255, 255));
        jBPDF.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jBPDF.setForeground(new java.awt.Color(0, 102, 0));
        jBPDF.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/pdf.png"))); // NOI18N
        jBPDF.setText("Ver PDF");
        jBPDF.setToolTipText("Ver PDF (Alt+F)");
        jBPDF.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jBPDF.setName(""); // NOI18N
        jBPDF.setNextFocusableComponent(jBDirCots);
        jBPDF.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jBPDFMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jBPDFMouseExited(evt);
            }
        });
        jBPDF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBPDFActionPerformed(evt);
            }
        });
        jBPDF.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jBPDFKeyPressed(evt);
            }
        });
        jPanel1.add(jBPDF, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 210, 130, 30));

        jBDirCots.setBackground(new java.awt.Color(255, 255, 255));
        jBDirCots.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jBDirCots.setForeground(new java.awt.Color(0, 102, 0));
        jBDirCots.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/dircot.png"))); // NOI18N
        jBDirCots.setText("Cotizaciones");
        jBDirCots.setToolTipText("Directorio de Cotizaciones (F9)");
        jBDirCots.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jBDirCots.setName(""); // NOI18N
        jBDirCots.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jBDirCotsMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jBDirCotsMouseExited(evt);
            }
        });
        jBDirCots.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBDirCotsActionPerformed(evt);
            }
        });
        jBDirCots.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jBDirCotsKeyPressed(evt);
            }
        });
        jPanel1.add(jBDirCots, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 240, 130, 30));

        jBVta.setBackground(new java.awt.Color(255, 255, 255));
        jBVta.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jBVta.setForeground(new java.awt.Color(0, 102, 0));
        jBVta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/fac.png"))); // NOI18N
        jBVta.setText("Venta");
        jBVta.setToolTipText("Convertir en Venta la(s) Cotización(es)");
        jBVta.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jBVta.setNextFocusableComponent(jBSal);
        jBVta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jBVtaMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jBVtaMouseExited(evt);
            }
        });
        jBVta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBVtaActionPerformed(evt);
            }
        });
        jBVta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jBVtaKeyPressed(evt);
            }
        });
        jPanel1.add(jBVta, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 270, 130, 30));

        jBSal.setBackground(new java.awt.Color(255, 255, 255));
        jBSal.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jBSal.setForeground(new java.awt.Color(0, 102, 0));
        jBSal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/sal.png"))); // NOI18N
        jBSal.setText("Salir");
        jBSal.setToolTipText("Salir (ESC)");
        jBSal.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jBSal.setNextFocusableComponent(jTab1);
        jBSal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jBSalMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jBSalMouseExited(evt);
            }
        });
        jBSal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBSalActionPerformed(evt);
            }
        });
        jPanel1.add(jBSal, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 300, 130, 30));

        jP1.add(jPanel1);
        jPanel1.setBounds(1160, 30, 130, 340);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("Cotizaciones:");
        jP1.add(jLabel1);
        jLabel1.setBounds(20, 10, 170, 17);

        jBMosT.setBackground(new java.awt.Color(255, 255, 255));
        jBMosT.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jBMosT.setForeground(new java.awt.Color(0, 102, 0));
        jBMosT.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/mostt.png"))); // NOI18N
        jBMosT.setText("Mostrar F4");
        jBMosT.setToolTipText("Mostrar Nuevamente todos los Registros");
        jBMosT.setNextFocusableComponent(jTab2);
        jBMosT.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jBMosTMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jBMosTMouseExited(evt);
            }
        });
        jBMosT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBMosTActionPerformed(evt);
            }
        });
        jBMosT.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jBMosTKeyPressed(evt);
            }
        });
        jP1.add(jBMosT);
        jBMosT.setBounds(1020, 230, 140, 20);

        jTBusc.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 255)));
        jTBusc.setNextFocusableComponent(jBMosT);
        jTBusc.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTBuscFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTBuscFocusLost(evt);
            }
        });
        jTBusc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTBuscKeyPressed(evt);
            }
        });
        jP1.add(jTBusc);
        jTBusc.setBounds(160, 230, 860, 20);

        jBBusc.setBackground(new java.awt.Color(255, 255, 255));
        jBBusc.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jBBusc.setForeground(new java.awt.Color(0, 102, 0));
        jBBusc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/busc5.png"))); // NOI18N
        jBBusc.setText("Buscar F3");
        jBBusc.setNextFocusableComponent(jTBusc);
        jBBusc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jBBuscMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jBBuscMouseExited(evt);
            }
        });
        jBBusc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBBuscActionPerformed(evt);
            }
        });
        jBBusc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jBBuscKeyPressed(evt);
            }
        });
        jP1.add(jBBusc);
        jBBusc.setBounds(20, 230, 140, 20);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setText("Partidas:");
        jP1.add(jLabel2);
        jLabel2.setBounds(20, 270, 170, 17);

        jBTab1.setBackground(new java.awt.Color(0, 153, 153));
        jBTab1.setToolTipText("Mostrar Tabla en Grande");
        jBTab1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBTab1ActionPerformed(evt);
            }
        });
        jBTab1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jBTab1KeyPressed(evt);
            }
        });
        jP1.add(jBTab1);
        jBTab1.setBounds(10, 30, 10, 20);

        jBTab2.setBackground(new java.awt.Color(0, 153, 153));
        jBTab2.setToolTipText("Mostrar Tabla en Grande");
        jBTab2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBTab2ActionPerformed(evt);
            }
        });
        jBTab2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jBTab2KeyPressed(evt);
            }
        });
        jP1.add(jBTab2);
        jBTab2.setBounds(10, 290, 10, 20);

        jLAyu.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jLAyu.setForeground(new java.awt.Color(0, 51, 204));
        jLAyu.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLAyu.setText("http://Ayuda en Lìnea");
        jLAyu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLAyuMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLAyuMouseExited(evt);
            }
        });
        jP1.add(jLAyu);
        jLAyu.setBounds(1140, 400, 150, 20);

        jBTod.setBackground(new java.awt.Color(255, 255, 255));
        jBTod.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jBTod.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/marct.png"))); // NOI18N
        jBTod.setText("Marcar todo");
        jBTod.setToolTipText("Marcar Todos los Registros de la Tabla (Alt+T)");
        jBTod.setNextFocusableComponent(jTab1);
        jBTod.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jBTodMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jBTodMouseExited(evt);
            }
        });
        jBTod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBTodActionPerformed(evt);
            }
        });
        jBTod.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jBTodKeyPressed(evt);
            }
        });
        jP1.add(jBTod);
        jBTod.setBounds(1030, 12, 130, 18);

        jLNot.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLNot.setForeground(new java.awt.Color(204, 0, 0));
        jLNot.setFocusable(false);
        jP1.add(jLNot);
        jLNot.setBounds(20, 250, 300, 20);

        jLNotCli.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLNotCli.setForeground(new java.awt.Color(204, 0, 0));
        jLNotCli.setFocusable(false);
        jP1.add(jLNotCli);
        jLNotCli.setBounds(300, 250, 860, 20);

        jPanel2.setLayout(new java.awt.GridLayout(1, 0));

        jTab1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No.", "Cod. Cotización", "Serie", "Cod.Cliente", "Observaciones", "SubTotal", "Impuesto", "Total", "Total Descuento", "Descripción", "Fecha Creación", "Fecha Modificación", "Fecha Vencimiento", "Fecha entrega", "Fecha documento", "Sucursal", "No. Caja", "Usuario", "Nombre Usuario", "Estado", "Motivo", "Venta", "Serie", "Folio"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTab1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jTab1.setGridColor(new java.awt.Color(255, 255, 255));
        jTab1.setInheritsPopupMenu(true);
        jTab1.setNextFocusableComponent(jBBusc);
        jTab1.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        jTab1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTab1MouseClicked(evt);
            }
        });
        jTab1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTab1KeyPressed(evt);
            }
        });
        jScrollPane2.setViewportView(jTab1);

        jPanel2.add(jScrollPane2);

        jP1.add(jPanel2);
        jPanel2.setBounds(20, 30, 1140, 200);

        jPanel3.setLayout(new java.awt.GridLayout(1, 0));

        jTab2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No.", "Cotización", "Qty", "Producto", "Unidad", "Lista", "Descripción", "Precio", "Desc.%", "Almacén", "Importe", "%Impuesto", "Total Impuesto", "Importe Descuento", "Serie Producto", "Comentario Serie", "Es Kit", "Talla", "Color", "Back Order", "Costo"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTab2.setGridColor(new java.awt.Color(255, 255, 255));
        jTab2.setInheritsPopupMenu(true);
        jTab2.setNextFocusableComponent(jBGenPDF);
        jTab2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTab2KeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(jTab2);

        jPanel3.add(jScrollPane1);

        jP1.add(jPanel3);
        jPanel3.setBounds(20, 290, 1140, 190);

        getContentPane().add(jP1);
        jP1.setBounds(10, 11, 1292, 540);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    
    /*Agrega todos los datos de la base de datos a la tabla de cots*/
    private synchronized void vCargCots(String sBusc)
    {
        /*Inicializa el contador de filas*/
        iContFi     = 1;
                      
        //Abre la base de datos                             
        Connection  con = Star.conAbrBas(true, false);

        //Si hubo error entonces regresa
        if(con==null)
            return;
        
        //Declara variables de la base de datos
        Statement   st;
        ResultSet   rs;        
        String      sQ; 
        
        /*Comprueba si se tiene habilitado que solo se puedan mostrar las cotizaiones del usuario o todas*/
        boolean bSi = false;
        try
        {
            sQ = "SELECT val FROM confgral WHERE clasif = 'cots' AND conf = 'cotsxusr'";
            st = con.createStatement();
            rs = st.executeQuery(sQ);
            /*Si hay datos*/
            if(rs.next())
            {
                /*Coloca la bandera en caso de que este activado*/
                if(rs.getString("val").compareTo("1")==0)                                  
                    bSi = true;                
            }
        }
        catch(SQLException expnSQL)
        {
            //Procesa el error y regresa
            Star.iErrProc(this.getClass().getName() + " " + expnSQL.getMessage(), Star.sErrSQL, expnSQL.getStackTrace(), con);                                
            return;
        }
        
        /*Limpia la tabla de partidas*/
        DefaultTableModel dm = (DefaultTableModel)jTab2.getModel();
        dm.setRowCount(0);
        
        /*Limpia la tabla de partidas de la cotización*/
        dm                  = (DefaultTableModel)jTab1.getModel();
        dm.setRowCount(0);
        
        /*Si esta hablitado entonces la consulta va a ser otra*/
        if(bSi)
            sQ = "SELECT *, estacs.NOM FROM cots LEFT OUTER JOIN estacs ON cots.ESTAC = estacs.ESTAC WHERE cots.ESTAC = '" + Login.sUsrG + "' AND cots.CODCOT LIKE('%" + sBusc.replace(" ", "%") + "%') OR estacs.NOM LIKE('%" + sBusc.replace(" ", "%") + "%') OR cots.PROY LIKE('%" + sBusc.replace(" ", "%") + "%') OR cots.OBSERV LIKE('%" + sBusc.replace(" ", "%") + "%') OR cots.SUBTOTGRAL LIKE('%" + sBusc.replace(" ", "%") + "%') OR cots.MANOBR LIKE('%" + sBusc.replace(" ", "%") + "%') OR cots.SUBTOTMAT LIKE('%" + sBusc.replace(" ", "%") + "%') OR cots.DESCRIP LIKE('%" + sBusc.replace(" ", "%") + "%') OR cots.ESTAC LIKE('%" + sBusc.replace(" ", "%") + "%') OR cots.FALT LIKE('%" + sBusc.replace(" ", "%") + "%') OR cots.FMOD LIKE('%" + sBusc.replace(" ", "%") + "%') OR cots.ESTAC LIKE('%" + sBusc.replace(" ", "%") + "%') OR cots.CODEMP LIKE('%" + sBusc.replace(" ", "%") + "%') OR cots.SUCU LIKE('%" + sBusc.replace(" ", "%") + "%') OR cots.NOCAJ LIKE('%" + sBusc.replace(" ", "%") + "%') OR cots.ESTAD LIKE('%" + sBusc.replace(" ", "%") + "%') ORDER BY cots.ID_ID DESC";
        else
            sQ = "SELECT *, estacs.NOM FROM cots LEFT OUTER JOIN estacs ON cots.ESTAC = estacs.ESTAC WHERE cots.CODCOT LIKE('%" + sBusc.replace(" ", "%") + "%') OR estacs.NOM LIKE('%" + sBusc.replace(" ", "%") + "%') OR cots.PROY LIKE('%" + sBusc.replace(" ", "%") + "%') OR cots.OBSERV LIKE('%" + sBusc.replace(" ", "%") + "%') OR cots.SUBTOTGRAL LIKE('%" + sBusc.replace(" ", "%") + "%') OR cots.MANOBR LIKE('%" + sBusc.replace(" ", "%") + "%') OR cots.SUBTOTMAT LIKE('%" + sBusc.replace(" ", "%") + "%') OR cots.DESCRIP LIKE('%" + sBusc.replace(" ", "%") + "%') OR cots.ESTAC LIKE('%" + sBusc.replace(" ", "%") + "%') OR cots.FALT LIKE('%" + sBusc.replace(" ", "%") + "%') OR cots.FMOD LIKE('%" + sBusc.replace(" ", "%") + "%') OR cots.ESTAC LIKE('%" + sBusc.replace(" ", "%") + "%') OR cots.CODEMP LIKE('%" + sBusc.replace(" ", "%") + "%') OR cots.SUCU LIKE('%" + sBusc.replace(" ", "%") + "%') OR cots.NOCAJ LIKE('%" + sBusc.replace(" ", "%") + "%') OR cots.ESTAD LIKE('%" + sBusc.replace(" ", "%") + "%') ORDER BY cots.ID_ID DESC";
        
        /*Obtiene todos los datos de las cotizaciones de la base de datos*/
        try
        {                              
            st = con.createStatement();
            rs = st.executeQuery(sQ);
            /*Si hay datos*/
            while(rs.next())
            {
                /*Obtiene el subtotal*/
                String sSubTot      = rs.getString("cots.SUBTOT");
                                                
                /*Formatea el subtotal a moneda*/
                double dCant        = Double.parseDouble(sSubTot);                        
                NumberFormat n      = NumberFormat.getCurrencyInstance(new Locale("es","MX"));
                sSubTot             = n.format(dCant);
                
                /*Obtiene el importe*/
                String sImp         = rs.getString("cots.IMPUE");   
                
                /*Formatea el importe a moneda*/
                dCant               = Double.parseDouble(sImp);                                        
                sImp                = n.format(dCant);
                
                /*Obtiene el descuento total*/
                String sTotDesc     = rs.getString("cots.TOTDESCU");   
                
                /*Formatea el total del descunto a moneda*/
                dCant               = Double.parseDouble(sTotDesc);                                        
                sTotDesc            = n.format(dCant);
                
                /*Obtiene el total*/
                String sTot         = rs.getString("cots.TOT");   
                
                /*Formatea el total a moneda*/
                dCant           = Double.parseDouble(sTot);                                        
                sTot            = n.format(dCant);                
                
                /*Agregalos a la tabla de cots*/                
                DefaultTableModel tm= (DefaultTableModel)jTab1.getModel();
                Object nu[]         = {iContFi, rs.getString("cots.CODCOT"), rs.getString("cots.NOSER"), rs.getString("cots.CODEMP"), rs.getString("cots.OBSERV"), sSubTot, sImp, sTot, sTotDesc, rs.getString("cots.DESCRIP"), rs.getString("cots.FALT"), rs.getString("cots.FMOD"), rs.getString("cots.FVENC"), rs.getString("fentre"), rs.getString("fdoc"), rs.getString("cots.SUCU"), rs.getString("cots.NOCAJ"), rs.getString("cots.ESTAC"), rs.getString("estacs.NOM"), rs.getString("cots.ESTAD"), rs.getString("cots.MOTIV"), rs.getString("cots.VTA"), rs.getString("cots.NOSERVTA"), rs.getString("cots.NOREFER")};
                tm.addRow(nu);
                
                /*Aumenta en uno el contador de filas*/
                ++iContFi;            
                
            }/*Fin de while(rs.next())*/                        
        }
        catch(SQLException expnSQL)
        {
            //Procesa el error y regresa
            Star.iErrProc(this.getClass().getName() + " " + expnSQL.getMessage(), Star.sErrSQL, expnSQL.getStackTrace(), con);                                
            return;
        }
        catch(NumberFormatException expnNumForm)
        {
            //Procesa el error y regresa
            Star.iErrProc(this.getClass().getName() + " " + expnNumForm.getMessage(), Star.sErrNumForm, expnNumForm.getStackTrace(), con);                                
            return;
        }
        
        //Cierra la base de datos
        Star.iCierrBas(con);           
        
    }/*Fin de private synchronized void vCargCots()*/
        
        
    /*Al presionar el botón de abrir*/
    private void jBVerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBVerActionPerformed
        
        /*Obtiene el índice de la fila seleccionada*/
        int row             = jTab1.getSelectedRow();
        
        /*Si no hay selección en la tabla entonces*/
        if(row==-1)
        {
            /*Mensajea*/
            JOptionPane.showMessageDialog(null, "Selecciona por lo menos una cotización de la tabla.", "Cotizaciones", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource(Star.sRutIconAd))); 
            
            /*Pon el foco del teclado en la tabla y regresa*/
            jTab1.grabFocus();                        
            return;
        }
        
        /*Recorre toda la selección del usuario*/
        int iSel[]              = jTab1.getSelectedRows();      
        for(int x = iSel.length - 1; x >= 0; x--)
        {
            /*Obtiene el código de la cotización de la fila*/
            String sCodCot      = jTab1.getModel().getValueAt(row, 1).toString();

            /*Mostrar el formulario para ver la cotización*/
            CotNorm n           = new CotNorm(jTab1, iContFi, sCodCot, true);
            n.setVisible(true);
        }                    
        
    }//GEN-LAST:event_jBVerActionPerformed

    
    /*Cuando se presiona una tecla en el formulario*/
    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        
        //Llama a la función escalable
        vKeyPreEsc(evt);
        
    }//GEN-LAST:event_formKeyPressed

    
    /*Cuando se presiona una tecla en el botón de ver*/
    private void jBVerKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jBVerKeyPressed
        
        //Llama a la función escalable
        vKeyPreEsc(evt);
        
    }//GEN-LAST:event_jBVerKeyPressed

   
    /*Cuando se presiona una tecla en la tabla*/
    private void jTab1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTab1KeyPressed
        
        //Llama a la función escalable
        vKeyPreEsc(evt);
        
    }//GEN-LAST:event_jTab1KeyPressed

    
    /*Cuando se presiona una tecla en el panel*/
    private void jP1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jP1KeyPressed
        
        //Llama a la función escalable
        vKeyPreEsc(evt);
        
    }//GEN-LAST:event_jP1KeyPressed

    
    /*Cuando se presiona el botón de salir*/
    private void jBSalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBSalActionPerformed
        
        /*Llama al recolector de basura*/
        System.gc();
        
        /*Sal del formulario*/
        this.dispose();        
        Star.gCots = null;
        
    }//GEN-LAST:event_jBSalActionPerformed

    
    /*Cuando se da un clic en la tabla de cots*/
    private void jTab1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTab1MouseClicked
        
        /*Si se hiso doble clic entonces presiona el botón de abrir*/
        if(evt.getClickCount() == 2) 
            jBAbr.doClick();
        
    }//GEN-LAST:event_jTab1MouseClicked

    
    /*Cuando se presiona una tecla en el botón de mostrar*/
    private void jBMosTKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jBMosTKeyPressed

        //Llama a la función escalable
        vKeyPreEsc(evt);

    }//GEN-LAST:event_jBMosTKeyPressed

        
    /*Cuando se gana el foco del teclado en el campo de buscar*/
    private void jTBuscFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTBuscFocusGained

        /*Selecciona todo el texto cuando gana el foco*/
        jTBusc.setSelectionStart(0);jTBusc.setSelectionEnd(jTBusc.getText().length());

    }//GEN-LAST:event_jTBuscFocusGained

    
    /*Cuando se prena una tecla en el campo de buscar*/
    private void jTBuscKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTBuscKeyPressed

        /*Si se presionó enter entonces presiona el botón de búsqueda y regresa*/
        if(evt.getKeyCode() == KeyEvent.VK_ENTER)
        {
            jBBusc.doClick();
            return;
        }
        
        //Llama a la función escalable
        vKeyPreEsc(evt);

    }//GEN-LAST:event_jTBuscKeyPressed

    
    /*Cuando se presiona una tecla en el botón de buscar*/
    private void jBBuscKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jBBuscKeyPressed

        //Llama a la función escalable
        vKeyPreEsc(evt);

    }//GEN-LAST:event_jBBuscKeyPressed

    
    /*Función para cargar nuevamente todos los elementos en la tabla*/
    private void vCargT()
    {
        /*Borra todos los item en la tabla de partidas de cotizaciones*/
        DefaultTableModel dm = (DefaultTableModel)jTab2.getModel();
        dm.setRowCount(0);
        
        /*Borra todos los item en la tabla de cotizaciones*/
        dm                  = (DefaultTableModel)jTab1.getModel();
        dm.setRowCount(0);
        
        /*Resetea el contador de filas*/
        iContFi = 1;
        
        /*Agrega todos los datos de la base de datos a la tabla de cots*/
        vCargCots("");  
        
        /*Vuelve a poner el foco del teclaod en el campo de buscar*/
        jTBusc.grabFocus();
        
    }/*Fin de private void vCargT()*/
    
    
    /*Cuando se presiona el botón de mostrar todo*/
    private void jBMosTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBMosTActionPerformed

        /*Resetea el label rojo*/
        jLNot.setText("");
       
        /*Función para cargar nuevamente todos los elementos en la tabla*/
        vCargT();
        
    }//GEN-LAST:event_jBMosTActionPerformed

    
    /*Cuando se presiona el botón de buscar*/
    private void jBBuscActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBBuscActionPerformed
        
        /*Si el campo de buscar esta vacio no puede seguir*/
        if(jTBusc.getText().compareTo("")==0)
        {
            /*Coloca el borde rojo*/                               
            jTBusc.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.RED));
            
            /*Mensajea*/
            JOptionPane.showMessageDialog(null, "El campo de búsqueda esta vacio.", "Campo vacio", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource(Star.sRutIconAd)));

            /*Coloca el foco del teclado en el campo de búsqueda y regresa*/
            jTBusc.grabFocus();           
            return;
        }       
        
        /*Agrega todas las cotizaciones de la base de datos a la tabla de cotizaciones en base al filtro de búsqueda*/
        (new Thread()
        {
            @Override
            public void run()
            {
                vCargCots(jTBusc.getText().trim());                
            }
            
        }).start();
        
        /*Resetea el label rojo*/
        jLNot.setText("");
        
        /*Vuelve a poner el foco del teclado en el campo de buscar*/
        jTBusc.grabFocus();
        
    }//GEN-LAST:event_jBBuscActionPerformed

    
    /*Cuando se presiona el botón de actualizar*/
    private void jBActuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBActuaActionPerformed

        /*Función para cargar nuevamente todos los elementos en la tabla*/
        vCargT();

    }//GEN-LAST:event_jBActuaActionPerformed

    
    /*Cuando se presiona una tecla en el botón de actualizar*/
    private void jBActuaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jBActuaKeyPressed

        //Llama a la función escalable
        vKeyPreEsc(evt);

    }//GEN-LAST:event_jBActuaKeyPressed

    
    /*Cuando se presiona una tecla en el botón de nuevo*/
    private void jBNewKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jBNewKeyPressed

        //Llama a la función escalable
        vKeyPreEsc(evt);
        
    }//GEN-LAST:event_jBNewKeyPressed

    
    /*Cuando se presiona la forma de nueva cotización*/
    private void jBNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBNewActionPerformed

        /*Muestra la forma para la nueva cotización*/
        vAbrCot(jTab1, iContFi, null, false);        
        
    }//GEN-LAST:event_jBNewActionPerformed

    
    /*Método para abrir una sola vez la cotización*/
    private void vAbrCot(javax.swing.JTable jTab, int iContF, String sCod, boolean bVal)
    {
        /*Abre la forma de las compras una sola vez*/
        if(Star.gCot==null)
        {            
            Star.gCot = new CotNorm(jTab, iContF, sCod, bVal);
            Star.gCot.setVisible(true);
        }
        else
        {            
            /*Si ya esta visible entonces traela al frente*/
            if(Star.gCot.isVisible())            
                Star.gCot.toFront();
            else
                Star.gCot.setVisible(true);            
        }                    
    }
    
    
    /*Cuando se presiona el botón de ver PDF*/
    private void jBPDFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBPDFActionPerformed

        /*Si el usuario no a seleccionado una cotización no puede avanzar*/
        if(jTab1.getSelectedRow()==-1)
        {
            /*Mensajea*/
            JOptionPane.showMessageDialog(null, "Selecciona una cotización primero.", "Cotizaciones", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource(Star.sRutIconAd)));

            /*Pon el foco del teclado en la tabla y regresa*/
            jTab1.grabFocus();
            return;

        }/*Fin de if(jTabVents.getSelectedRow()==-1)*/

        /*Preguntar al usuario si esta seguro de querer abrir los PDF*/
        Object[] op = {"Si","No"};
        int iRes    = JOptionPane.showOptionDialog(this, "¿Seguro que quieres abrir el(los) PDF?", "PDF", JOptionPane.YES_NO_OPTION,  JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource(Star.sRutIconDu)), op, op[0]);
        if(iRes==JOptionPane.NO_OPTION || iRes==JOptionPane.CLOSED_OPTION)
            return;                                    
        
        //Abre la base de datos                             
        Connection  con = Star.conAbrBas(true, false);

        //Si hubo error entonces regresa
        if(con==null)
            return;
        
        //Trae la carpeta compartida de la aplicación y la ruta en el servidor de la base de datos
        String sCarp    = Star.sGetRutCarp(con);                    

        //Si hubo error entonces regresa
        if(sCarp==null)
            return;
        
        //Cierra la base de datos
        if(Star.iCierrBas(con)==-1)
            return;

        /*Si la carpeta de la aplicación compartida en el servidor no esta definida entonces*/
        if(sCarp.compareTo("")==0)
        {
            /*Mensajea y regresa*/
            JOptionPane.showMessageDialog(null, "No se a definido la carpeta compartida de la aplicación en el servidor.", "Servidor",  JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource(Star.sRutIconAd)));           
            return;
        }
        
        /*Si la ruta a las cotizaciones no existe entonces crea la carpeta*/
        sCarp       += "\\Cotizaciones";
        if(!new File(sCarp).exists())
            new File(sCarp).mkdir();
        
        /*Si la ruta a la empresa no existe entonces crea la carpeta*/
        sCarp       += "\\" + Login.sCodEmpBD;
        if(!new File(sCarp).exists())
            new File(sCarp).mkdir();        
        
        /*Recorre toda la selección del usuario*/
        int iSel[]              = jTab1.getSelectedRows();        
        for(int x = iSel.length - 1; x >= 0; x--)
        {
            /*Obtiene algunos datos de la fila*/        
            String sCodCot  = jTab1.getValueAt(iSel[x], 1).toString();        
            
            /*Si no existe el archivo PDF entonces*/
            String sRut     = sCarp + "\\" + sCodCot + ".pdf";
            if(!new File(sRut).exists())
            {
                /*Mensajea y continua*/
                JOptionPane.showMessageDialog(null, "La cotización \"" + sRut + "\" no existe.", "Cotización", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource(Star.sRutIconAd)));            
                continue;
            }
            
            /*Abre el archivo PDF*/
            try
            {
                Desktop.getDesktop().open(new File(sRut));
            }
            catch(IOException expnIO)
            {
                //Procesa el error y regresa
                Star.iErrProc(this.getClass().getName() + " " + expnIO.getMessage(), Star.sErrIO, expnIO.getStackTrace(), con);                                            
                return;
            }
            
        }/*Fin de for(int x = iSel.length - 1; x >= 0; x--)*/                                                                         

    }//GEN-LAST:event_jBPDFActionPerformed

    
    /*Cuando se presiona una tecla en el botón de ver PDF de la cotización*/
    private void jBPDFKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jBPDFKeyPressed

        //Llama a la función escalable
        vKeyPreEsc(evt);

    }//GEN-LAST:event_jBPDFKeyPressed

    
    /*Cuando se presiona el botón de mandar correo*/
    private void jBMailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBMailActionPerformed

        /*Si no hay selección en la tabla no puede seguir*/
        if(jTab1.getSelectedRow()==-1)
        {
            /*Mensajea*/
            JOptionPane.showMessageDialog(null, "Selecciona por lo menos una cotización para reenviar.", "Reenviar  cotización", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource(Star.sRutIconAd)));

            /*Coloca el foco del teclado en la tabla y regresa*/
            jTab1.grabFocus();            
            return;
        }
                
        //Abre la base de datos                             
        Connection  con = Star.conAbrBas(true, false);

        //Si hubo error entonces regresa
        if(con==null)
            return;
        
        //Obtiene si el usuario tiene correo asociado
        int iRes    = Star.iUsrCon(con, Login.sUsrG);

        //Si hubo error entonces regresa
        if(iRes==-1)
            return;

        //Comprueba si la configuración de mostrar el mensaje esta habilitado o no
        int iMosMsj = Star.iMostMsjCorrUsr(con);

        //Si hubo error entonces regresa
        if(iMosMsj==-1)
            return;                        

        //Si no tiene correo asociado entonces solamente mensajea
        if(iRes==0 && iMosMsj==1)
        {
            //Cierra la base de datos
            if(Star.iCierrBas(con)==-1)
                return;

            /*Mensajea y regresa*/
            JOptionPane.showMessageDialog(null, "No se a definido correo electrónico para el usuario: " + Login.sUsrG + ".", "Correo electrónico",  JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource(Star.sRutIconAd)));                
            return;
        }
            
        //Trae la carpeta compartida de la aplicación y la ruta en el servidor de la base de datos
        String sCarp    = Star.sGetRutCarp(con);                    

        //Si hubo error entonces regresa
        if(sCarp==null)
            return;
        
        /*Si la carpeta de la aplicación compartida en el servidor no esta definida entonces*/
        if(sCarp.compareTo("")==0)
        {
            //Cierra la base de datos
            if(Star.iCierrBas(con)==-1)
                return;
            
            /*Mensajea y regresa*/
            JOptionPane.showMessageDialog(null, "No se a definido la carpeta compartida de la aplicación en el servidor.", "Servidor",  JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource(Star.sRutIconAd)));            
            return;
        }
       
        /*Si el directorio de cots no existe entonces crea el directorio*/
        String sRutPDF = sCarp + "\\Cotizaciones";
        if(!new File(sRutPDF).exists())
            new File(sRutPDF).mkdir();
        
        /*Si no existe la carpeta de la empresa entonces crea el directorio*/
        sRutPDF       += "\\" + Login.sCodEmpBD;
        if(!new File(sRutPDF).exists())
            new File(sRutPDF).mkdir();

        /*Recorre toda la selección del usuario*/
        int iSel[]          = jTab1.getSelectedRows();        
        for(int x = iSel.length - 1; x >= 0; x--)
        {
            /*Obtén el código de la cotización seleccionada*/        
            String sCot     = jTab1.getValueAt(iSel[x], 1).toString();        
       
            /*Completa la ruta al PDF*/
            String sRut     = sRutPDF + "\\" + sCot + ".pdf";

            /*Si no existe el archivo PDF entonces*/
            if(!new File(sRutPDF).exists())
            {                
                /*Mensajea y continua*/
                JOptionPane.showMessageDialog(null, "La cotización: " + sCot + ".pdf no existe.", "Cotización", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource(Star.sRutIconAd)));
                continue;
            }

            /*Contiene el cliente y el nombre*/
            String sCli     = "";
            String sNom     = "";

            //Declara variables de la base de datos
            Statement   st;
            ResultSet   rs;        
            String      sQ;               

            /*Obtiene algunos datos de la empresa en base a la cotización*/            
            try
            {
               sQ = "SELECT cots.CODEMP, emps.NOM FROM cots LEFT OUTER JOIN emps ON CONCAT_WS('', emps.SER, emps.CODEMP) = cots.CODEMP WHERE codcot = '" + sCot + "'";
                st = con.createStatement();
                rs = st.executeQuery(sQ);
                /*Si hay datos entonces obtiene los resultados*/
                if(rs.next())
                {                    
                    sCli        = rs.getString("cots.CODEMP");                                                   
                    sNom        = rs.getString("emps.NOM");                                                   
                }
            }
            catch(SQLException expnSQL)
            {
                //Procesa el error y regresa
                Star.iErrProc(this.getClass().getName() + " " + expnSQL.getMessage(), Star.sErrSQL, expnSQL.getStackTrace(), con);                                
                return;
            }
         
            /*Muestra el formulario para saber de que correo se reenviara la cotización*/            
            SelCorr co = new SelCorr(this, sRut, "", sCli, sCot, sCot + ".pdf", "", 2, sNom);
            co.setVisible(true);
            
        }/*Fin de for(int x = iSel.length - 1; x >= 0; x--)*/                    
        
        //Cierra la base de datos
        Star.iCierrBas(con);          
        
    }//GEN-LAST:event_jBMailActionPerformed

    
    /*Cuando se presiona una tecla en el botón de reenviar correo*/
    private void jBMailKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jBMailKeyPressed

        //Llama a la función escalable
        vKeyPreEsc(evt);

    }//GEN-LAST:event_jBMailKeyPressed

    
    /*Cuando se presiona el botón de ver el directorio de las cots*/
    private void jBDirCotsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBDirCotsActionPerformed

        //Abre la base de datos                             
        Connection  con = Star.conAbrBas(true, false);

        //Si hubo error entonces regresa
        if(con==null)
            return;
        
        //Declara variables de la base de datos
        Statement   st;
        ResultSet   rs;        
        String      sQ;

        /*Trae la carpeta compartida de la aplicación en el servidor de la base de datos*/
        String sCarp    = "";
        try
        {
            sQ = "SELECT IFNULL(rutap, '') AS rutap FROM basdats WHERE codemp = '" + Login.sCodEmpBD + "'";
            st = con.createStatement();
            rs = st.executeQuery(sQ);
            /*Si hay datos entonces obtiene la consulta*/
            if(rs.next()) 
                sCarp          = rs.getString("rutap");
        }
        catch(SQLException expnSQL)
        {
            //Procesa el error y regresa
            Star.iErrProc(this.getClass().getName() + " " + expnSQL.getMessage(), Star.sErrSQL, expnSQL.getStackTrace(), con);                                
            return;
        }

        //Cierra la base de datos
        if(Star.iCierrBas(con)==-1)
            return;

        /*Si la ruta a las cots no existe entonces crea la ruta*/
        String sRutCompro = sCarp + "\\Cotizaciones";
        if(!new File(sRutCompro).exists())
            new File(sRutCompro).mkdir();
        
        /*Si la ruta a la empresa no existe entonces crea la ruta*/
        sRutCompro += "\\" + Login.sCodEmpBD;
        if(!new File(sRutCompro).exists())
            new File(sRutCompro).mkdir();

        /*Abre el directorio de comprobantes*/
        try
        {
            Desktop.getDesktop().open(new File(sRutCompro));
        }
        catch(IOException expnIO)
        {
            //Procesa el error
            Star.iErrProc(this.getClass().getName() + " " + expnIO.getMessage(), Star.sErrSQL, expnIO.getStackTrace(), con);            
        }

    }//GEN-LAST:event_jBDirCotsActionPerformed

    
    /*Cuando se presiona una tecla en el botón de directorio de cots*/
    private void jBDirCotsKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jBDirCotsKeyPressed

        //Llama a la función escalable
        vKeyPreEsc(evt);

    }//GEN-LAST:event_jBDirCotsKeyPressed

    
    
    
    
    
    
    /*Cuando se presiona una tecla en la tabla de partidas*/
    private void jTab2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTab2KeyPressed
        
        //Llama a la función escalable
        vKeyPreEsc(evt);
        
    }//GEN-LAST:event_jTab2KeyPressed

    
    /*Cuando se arrastra el mouse en la forma*/
    private void formMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseDragged
        
        /*Pon la bandera para saber que ya hubó un evento y no se desloguie*/
        bIdle   = true;
        
    }//GEN-LAST:event_formMouseDragged

    
    /*Cuando se mueve el mouse en la forma*/
    private void formMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseMoved
        
        /*Pon la bandera para saber que ya hubó un evento y no se desloguie*/
        bIdle   = true;
        
    }//GEN-LAST:event_formMouseMoved

    
    /*Cuando se mueve la rueda del ratón en la forma*/
    private void formMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_formMouseWheelMoved
        
        /*Pon la bandera para saber que ya hubó un evento y no se desloguie*/
        bIdle   = true;
        
    }//GEN-LAST:event_formMouseWheelMoved

    
    /*Cuando se presiona una tecla en el botón de abrir cotización*/
    private void jBAbrKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jBAbrKeyPressed
        
        //Llama a la función escalable
        vKeyPreEsc(evt);
        
    }//GEN-LAST:event_jBAbrKeyPressed

    
    /*Cuando se presiona el botón de abrir cotización*/
    private void jBAbrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBAbrActionPerformed

        /*Obtiene las filas seleccionadas*/
        int iSel[]              = jTab1.getSelectedRows();
        
        /*Si no se a seleccionado por lo menos una cotización entonces*/
        if(iSel.length==0)
        {
            /*Mensajea*/
            JOptionPane.showMessageDialog(null, "Selecciona por lo menos una cotización para abrir.", "Cotizaciones", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource(Star.sRutIconAd))); 
            
            /*Coloca el foco del teclado en la tabla y regresa*/
            jTab1.grabFocus();
            return;
        }

        //Abre la base de datos                             
        Connection  con = Star.conAbrBas(true, false);

        //Si hubo error entonces regresa
        if(con==null)
            return;
        
        //Trae la carpeta compartida de la aplicación y la ruta en el servidor de la base de datos
        String sCarp    = Star.sGetRutCarp(con);                    

        //Si hubo error entonces regresa
        if(sCarp==null)
            return;
        
        /*Si la carpeta de la aplicación compartida en el servidor no esta definida entonces*/
        if(sCarp.compareTo("")==0)
        {
            //Cierra la base de datos
            if(Star.iCierrBas(con)==-1)
                return;
            
            /*Mensajea y mensajea*/
            JOptionPane.showMessageDialog(null, "No se a definido la carpeta compartida de la aplicación en el servidor.", "Servidor",  JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource(Star.sRutIconAd)));                                            
            return;                        
        }

        //Cierra la base de datos
        if(Star.iCierrBas(con)==-1)
            return;

        /*Recorre toda la selección del usuario y muestra la forma de cotización*/                
        for(int x = iSel.length - 1; x >= 0; x--)        
            vAbrCot(jTab1, iContFi, jTab1.getValueAt(iSel[x], 1).toString().trim(), false);                                    
        
    }//GEN-LAST:event_jBAbrActionPerformed

    
    /*Cuando se presiona el botón de mostrar tabla 1*/
    private void jBTab1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBTab1ActionPerformed

        //Muestra la tabla maximizada
        Star.vMaxTab(jTab1);       

    }//GEN-LAST:event_jBTab1ActionPerformed

    
    /*Cuando se presiona una tecla en el botón de mostrar tabla 1*/
    private void jBTab1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jBTab1KeyPressed

        //Llama a la función escalable
        vKeyPreEsc(evt);

    }//GEN-LAST:event_jBTab1KeyPressed

    
    /*Cuando se presiona el botón de mostrar la tabla 2*/
    private void jBTab2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBTab2ActionPerformed

        //Muestra la tabla maximizada
        Star.vMaxTab(jTab2);       

    }//GEN-LAST:event_jBTab2ActionPerformed

    
    /*Cuando se presiona una tecla en el botón de mostrar tabla 2*/
    private void jBTab2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jBTab2KeyPressed

        //Llama a la función escalable
        vKeyPreEsc(evt);

    }//GEN-LAST:event_jBTab2KeyPressed

    
    /*Cuando el mouse sale del botón de buscar*/
    private void jBBuscMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jBBuscMouseExited
        
        /*Cambia el color del fondo del botón al original*/
        jBBusc.setBackground(Star.colOri);
        
        /*Coloca el borde que tenía*/
        jBBusc.setBorder(bBordOri);
        
    }//GEN-LAST:event_jBBuscMouseExited

    
    /*Cuando el mouse entra en el botón de búscar*/
    private void jBBuscMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jBBuscMouseEntered
        
        /*Cambia el color del fondo del botón*/
        jBBusc.setBackground(Star.colBot);
        
        /*Guardar el borde original que tiene el botón*/
        bBordOri    = jBBusc.getBorder();
                
        /*Aumenta el grosor del botón*/
        jBBusc.setBorder(new LineBorder(Color.GREEN, 3));
        
    }//GEN-LAST:event_jBBuscMouseEntered

    
    /*Cuando el mouse entra en el campo del link de ayuda*/
    private void jLAyuMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLAyuMouseEntered
        
        /*Cambia el cursor del ratón*/
        this.setCursor( new Cursor(Cursor.HAND_CURSOR));
        
    }//GEN-LAST:event_jLAyuMouseEntered

    
    /*Cuando el mouse sale del campo del link de ayuda*/
    private void jLAyuMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLAyuMouseExited
        
        /*Cambia el cursor del ratón al que tenía*/
        this.setCursor( new Cursor(Cursor.DEFAULT_CURSOR));	
        
    }//GEN-LAST:event_jLAyuMouseExited

    
    /*Cuando se presiona el botón de seleccionar todo*/
    private void jBTodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBTodActionPerformed

        /*Si la tabla no contiene elementos entonces regresa*/
        if(jTab1.getRowCount()==0)
            return;
        
        /*Si están seleccionados los elementos en la tabla entonces*/
        if(bSel)
        {
            /*Coloca la bandera y deseleccionalos*/
            bSel    = false;
            jTab1.clearSelection();
        }
        /*Else deseleccionalos y coloca la bandera*/
        else
        {
            bSel    = true;
            jTab1.setRowSelectionInterval(0, jTab1.getModel().getRowCount()-1);
        }

    }//GEN-LAST:event_jBTodActionPerformed


    /*Cuando se presiona una tecla en el botón de seleccionar todo*/
    private void jBTodKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jBTodKeyPressed

        //Llama a la función escalable
        vKeyPreEsc(evt);

    }//GEN-LAST:event_jBTodKeyPressed

    
    /*Cuando se esta saliendo de la forma*/
    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        
        /*Presiona el botón de salir*/
        jBSal.doClick();
        
    }//GEN-LAST:event_formWindowClosing

    
    /*Cuando el mouse entra en el botón específico*/
    private void jBMosTMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jBMosTMouseEntered
        
        /*Cambia el color del fondo del botón*/
        jBMosT.setBackground(Star.colBot);
        
    }//GEN-LAST:event_jBMosTMouseEntered

    
    /*Cuando el mouse entra en el botón específico*/
    private void jBTodMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jBTodMouseEntered
        
        /*Cambia el color del fondo del botón*/
        jBTod.setBackground(Star.colBot);
        
    }//GEN-LAST:event_jBTodMouseEntered

    
    /*Cuando el mouse entra en el botón específico*/
    private void jBNewMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jBNewMouseEntered
        
        /*Cambia el color del fondo del botón*/
        jBNew.setBackground(Star.colBot);
        
    }//GEN-LAST:event_jBNewMouseEntered

    
    /*Cuando el mouse entra en el botón específico*/
    private void jBAbrMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jBAbrMouseEntered
        
        /*Cambia el color del fondo del botón*/
        jBAbr.setBackground(Star.colBot);
        
    }//GEN-LAST:event_jBAbrMouseEntered

    
    /*Cuando el mouse entra en el botón específico*/
    private void jBVerMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jBVerMouseEntered
        
        /*Cambia el color del fondo del botón*/
        jBVer.setBackground(Star.colBot);
        
    }//GEN-LAST:event_jBVerMouseEntered

        
    /*Cuando el mouse entra en el botón específico*/
    private void jBMailMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jBMailMouseEntered
        
        /*Cambia el color del fondo del botón*/
        jBMail.setBackground(Star.colBot);
        
    }//GEN-LAST:event_jBMailMouseEntered

    
    /*Cuando el mouse entra en el botón específico*/
    private void jBActuaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jBActuaMouseEntered
        
        /*Cambia el color del fondo del botón*/
        jBActua.setBackground(Star.colBot);
        
    }//GEN-LAST:event_jBActuaMouseEntered

    
    
    /*Cuando el mouse entra en el botón específico*/
    private void jBPDFMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jBPDFMouseEntered
        
        /*Cambia el color del fondo del botón*/
        jBPDF.setBackground(Star.colBot);
        
    }//GEN-LAST:event_jBPDFMouseEntered

    
    /*Cuando el mouse entra en el botón específico*/
    private void jBDirCotsMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jBDirCotsMouseEntered
        
        /*Cambia el color del fondo del botón*/
        jBDirCots.setBackground(Star.colBot);
        
    }//GEN-LAST:event_jBDirCotsMouseEntered

    
    
    /*Cuando el mouse entra en el botón específico*/
    private void jBSalMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jBSalMouseEntered
        
        /*Cambia el color del fondo del botón*/
        jBSal.setBackground(Star.colBot);
        
    }//GEN-LAST:event_jBSalMouseEntered

    
    /*Cuando el mouse sale del botón específico*/
    private void jBTodMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jBTodMouseExited
        
        /*Cambia el color del fondo del botón al original*/
        jBTod.setBackground(Star.colOri);
        
    }//GEN-LAST:event_jBTodMouseExited

    
    /*Cuando el mouse sale del botón específico*/
    private void jBMosTMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jBMosTMouseExited
        
        /*Cambia el color del fondo del botón al original*/
        jBMosT.setBackground(Star.colOri);
        
    }//GEN-LAST:event_jBMosTMouseExited

    
    /*Cuando el mouse sale del botón específico*/
    private void jBNewMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jBNewMouseExited
        
        /*Cambia el color del fondo del botón al original*/
        jBNew.setBackground(Star.colOri);
        
    }//GEN-LAST:event_jBNewMouseExited

    
    /*Cuando el mouse sale del botón específico*/
    private void jBAbrMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jBAbrMouseExited
        
        /*Cambia el color del fondo del botón al original*/
        jBAbr.setBackground(Star.colOri);
        
    }//GEN-LAST:event_jBAbrMouseExited

    
    /*Cuando el mouse sale del botón específico*/
    private void jBVerMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jBVerMouseExited
        
        /*Cambia el color del fondo del botón al original*/
        jBVer.setBackground(Star.colOri);
        
    }//GEN-LAST:event_jBVerMouseExited

    

    /*Cuando el mouse sale del botón específico*/
    private void jBMailMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jBMailMouseExited
        
        /*Cambia el color del fondo del botón al original*/
        jBMail.setBackground(Star.colOri);
        
    }//GEN-LAST:event_jBMailMouseExited

    
    /*Cuando el mouse sale del botón específico*/
    private void jBActuaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jBActuaMouseExited
        
        /*Cambia el color del fondo del botón al original*/
        jBActua.setBackground(Star.colOri);
        
    }//GEN-LAST:event_jBActuaMouseExited

    
    
    /*Cuando el mouse sale del botón específico*/
    private void jBPDFMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jBPDFMouseExited
        
        /*Cambia el color del fondo del botón al original*/
        jBPDF.setBackground(Star.colOri);
        
    }//GEN-LAST:event_jBPDFMouseExited

    
    /*Cuando el mouse sale del botón específico*/
    private void jBDirCotsMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jBDirCotsMouseExited
        
        /*Cambia el color del fondo del botón al original*/
        jBDirCots.setBackground(Star.colOri);
        
    }//GEN-LAST:event_jBDirCotsMouseExited

    
    
    /*Cuando el mouse sale del botón específico*/
    private void jBSalMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jBSalMouseExited
        
        /*Cambia el color del fondo del botón al original*/
        jBSal.setBackground(Star.colOri);
        
    }//GEN-LAST:event_jBSalMouseExited

    
    /*Cuando se pierde el foco del teclado en el control de bùsqueda*/
    private void jTBuscFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTBuscFocusLost

        /*Coloca el cursor al principio del control*/
        jTBusc.setCaretPosition(0);
        
        /*Coloca el borde negro si tiene datos, caso contrario de rojo*/                               
        if(jTBusc.getText().compareTo("")!=0)
            jTBusc.setBorder(javax.swing.BorderFactory.createLineBorder(new Color(204,204,255)));
        
    }//GEN-LAST:event_jTBuscFocusLost

    
    /*Cuando se presiona una tecla en el botón de convertir a venta*/
    private void jBVtaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jBVtaKeyPressed
        
        //Llama a la función escalable
        vKeyPreEsc(evt);
        
    }//GEN-LAST:event_jBVtaKeyPressed

    
    /*Cuando el mouse entra en el botón de convertir a venta*/
    private void jBVtaMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jBVtaMouseEntered
 
        /*Cambia el color del fondo del botón*/
        jBVta.setBackground(Star.colBot);
        
    }//GEN-LAST:event_jBVtaMouseEntered

    
    /*Cuando el mouse sale del botón de convertir en venta*/
    private void jBVtaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jBVtaMouseExited
        
        /*Cambia el color del fondo del botón al original*/
        jBVta.setBackground(Star.colOri);        
        
    }//GEN-LAST:event_jBVtaMouseExited

    
    /*Cuando se presiona el botón de convertir cotización(es) en venta(s)*/
    private void jBVtaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBVtaActionPerformed
        
        /*Si no a seleccionado una cotización por lo menos entonces*/
        if(jTab1.getSelectedRow()==-1)
        {
            /*Mensajea*/
            JOptionPane.showMessageDialog(null, "Selecciona por lo menos una cotización.", "Venta", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource(Star.sRutIconAd)));
            
            /*Coloca el foco del teclado en la tabla y regresa*/
            jTab1.grabFocus();
            return;
        }
                
        //Abre la base de datos                             
        Connection  con = Star.conAbrBas(true, false);

        //Si hubo error entonces regresa
        if(con==null)
            return;
        
        //Declara variables de la base de datos
        Statement   st;
        ResultSet   rs;        
        String      sQ; 

        /*Recorre toda la selección del usuario y abre la forma de nueva venta con los datos de la cotización*/
        int iSel[]              = jTab1.getSelectedRows();        
        for(int x = iSel.length - 1; x >= 0; x--)                
        {
            
            //Variable para ver si todas tienen serie
            boolean bAllSer = true;
            /*Obtiene las partidas de la cotización*/
            try
            {
                sQ = "SELECT * FROM partcot WHERE codcot = '" + jTab1.getValueAt(iSel[x], 1).toString().trim() + "'";                        
                st = con.createStatement();
                rs = st.executeQuery(sQ);
                
                while(rs.next())
                {   
                    //Se verifica si algun elemento que deba llevar serie tenga
                    if(rs.getString("serprod").trim().compareTo("")==0 && Star.iProdSolSer(con, rs.getString("prod").trim())==1 && rs.getString("fentre").trim().compareTo("")==0)
                    {
                        /*Mensajea y continua*/
                        JOptionPane.showMessageDialog(null, "La cotización: " + jTab1.getValueAt(iSel[x], 1).toString().trim() + " le faltan series.", "Cotización", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource(Star.sRutIconAd)));
                        bAllSer = false;
                    }
                    
                }/*Fin de while(rs.next())*/

            }/*Fin de try*/
            catch(SQLException e)
            {
                //Cierra la base de datos
                if(Star.iCierrBas(con)==-1)
                    return;

                /*Agrega en el log*/
                Login.vLog(e.getMessage());

                /*Mensajea y regresa*/
                JOptionPane.showMessageDialog(null, this.getClass().getName() + " Error Inventarios() por " + e.getMessage(), "Error BD", JOptionPane.ERROR_MESSAGE, new ImageIcon(getClass().getResource(Star.sRutIconEr))); 
                return;
            }
            
            //Si no tiene series continue
            if(bAllSer == false)
                continue;
        
            /*Contiene el estado de la cotización*/
            String sEstad   = "";
            
            /*Obtiene algunos datos de la cotización*/
            try
            {
                sQ = "SELECT estad FROM cots WHERE codcot = '" + jTab1.getValueAt(iSel[x], 1).toString().trim() + "'";                        
                st = con.createStatement();
                rs = st.executeQuery(sQ);
                /*Si hay datos entonces obtiene el resultado*/            
                if(rs.next())            
                    sEstad  = rs.getString("estad");
            }
            catch(SQLException expnSQL)
            {
                //Procesa el error y regresa
                Star.iErrProc(this.getClass().getName() + " " + expnSQL.getMessage(), Star.sErrSQL, expnSQL.getStackTrace(), con);                                
                return;
            }  
            
            /*Si el estado es confirmado entonces*/
            if(sEstad.compareTo("CO")==0)
            {
                /*Mensajea y continua*/
                JOptionPane.showMessageDialog(null, "La cotización: " + jTab1.getValueAt(iSel[x], 1).toString().trim() + " ya esta confirmada.", "Cotización", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource(Star.sRutIconAd)));
                continue;
            }
            
            /*Si el estado es cancelado entonces*/
            if(sEstad.compareTo("CA")==0)
            {
                /*Mensajea y continua*/
                JOptionPane.showMessageDialog(null, "La cotización: " + jTab1.getValueAt(iSel[x], 1).toString().trim() + " esta cancelada.", "Cotización", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource(Star.sRutIconAd)));
                continue;
            }
            
            /*Función para abrir la forma de ventas una sola vez*/
            Star.AbrVta(null, null, jTab1.getValueAt(iSel[x], 1).toString().trim());                
                    
        }/*Fin de for(int x = iSel.length - 1; x >= 0; x--)*/
        
        //Cierra la base de datos
        Star.iCierrBas(con);            
        
    }//GEN-LAST:event_jBVtaActionPerformed

      
    /*Cuando el mouse entra en el botón de cancelar*/
    private void jBCanMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jBCanMouseEntered
        
        /*Cambia el color del fondo del botón*/
        jBCan.setBackground(Star.colBot);
        
    }//GEN-LAST:event_jBCanMouseEntered

    
    /*Cuando el mouse sale del botón de cancelar cotización*/
    private void jBCanMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jBCanMouseExited
        
        /*Cambia el color del fondo del botón*/
        jBCan.setBackground(Star.colOri);
        
    }//GEN-LAST:event_jBCanMouseExited

    
    /*Cuando se presiona el botón de cancelar*/
    private void jBCanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBCanActionPerformed
        
        /*Si el usuario no a seleccionado por lo menos una cotización para cancelar no puede avanzar*/
        if(jTab1.getSelectedRow()==-1)
        {
            /*Mensajea*/
            JOptionPane.showMessageDialog(null, "Selecciona por lo menos una cotización para cancelar.", "Cancelación", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource(Star.sRutIconAd)));
            
            /*Pon el foco del teclado en la tabla de facturas y regresa*/
            jTab1.grabFocus();                        
            return;            
        }
        
        /*Obtiene toda la selección del usuario*/
        int iSel[]              = jTab1.getSelectedRows();        
        
        /*Mientras el usuario no escriba un motivo de cancelación o cancele la cancelación entonces*/
        String      sMot;
        do
        {            
            /*Pide al usuario el motivo de la cancelación de la venta*/
            sMot     = JOptionPane.showInputDialog(this,"Motivo de cancelación:", "Motivo", JOptionPane.QUESTION_MESSAGE);
            
            /*Si el usuario cancelo el cuadro entonces regresa por que no puede continuar*/
            if(sMot==null)
                return;

            /*Si el usuario no escribio un motiv de cancelación entonces mensajea*/
            if(sMot.compareTo("")==0)
                JOptionPane.showMessageDialog(null, "Escribe un motivo de cancelación.", "Escribir motivo", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource(Star.sRutIconAd)));                 

        }
        while(sMot.compareTo("")== 0);        
        
        /*Preguntar al usuario si esta seguro querer hacer la cancelación de la factura*/
        Object[] op = {"Si","No"};
        int iRes    = JOptionPane.showOptionDialog(this, "¿Seguro que quieres hacer la(s) cancelación(es)?", "Cancelación", JOptionPane.YES_NO_OPTION,  JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource(Star.sRutIconDu)), op, op[0]);
        if(iRes== JOptionPane.NO_OPTION || iRes== JOptionPane.CANCEL_OPTION)
            return;                       
                
        //Abre la base de datos                             
        Connection  con = Star.conAbrBas(true, false);

        //Si hubo error entonces regresa
        if(con==null)
            return;
        
        /*Bandera para saber si hubo alguna cancelación*/
        boolean bCan    = false;

        //Declara variables de la base de datos
        Statement   st;
        ResultSet   rs;        
        String      sQ; 
        
        /*Recorre toda la selección del usuario*/        
        for(int x = iSel.length - 1; x >= 0; x--)
        {            
            //Declara variables locales
            String sEstad   = "";            
            
            /*Obtiene algunos datos de la cotización*/            
            try
            {                  
                sQ = "SELECT estad FROM cots WHERE codcot = '" + jTab1.getValueAt(iSel[x], 1).toString().trim() + "'";                
                st = con.createStatement();
                rs = st.executeQuery(sQ);
                /*Si hay datos entonces obtiene el resultado*/
                if(rs.next())
                    sEstad  = rs.getString("estad");
            }
            catch(SQLException expnSQL)
            {
                //Procesa el error y regresa
                Star.iErrProc(this.getClass().getName() + " " + expnSQL.getMessage(), Star.sErrSQL, expnSQL.getStackTrace(), con);                                
                return;
            }
            
            /*Si la cotización esta confirmada entonces*/
            if(sEstad.compareTo("CO")== 0)
            {
                /*Mensajea y continua*/
                JOptionPane.showMessageDialog(null, "La cotización " + jTab1.getValueAt(iSel[x], 1).toString() + " esta confirmada.", "Cotización", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource(Star.sRutIconAd)));                
                continue;
            }

            /*Si es una cotización cancelada entonces*/
            if(sEstad.compareTo("CA")== 0)
            {
                /*Mensajea y continua*/
                JOptionPane.showMessageDialog(null, "La cotización " + jTab1.getValueAt(iSel[x], 1).toString() + " esta cancelada.", "Cotización", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource(Star.sRutIconAd))); 
                continue;
            }

            /*Actualiza la cotización para que sea de cancelación*/
            try 
            {            
                sQ = "UPDATE cots SET "
                        + "estad        = 'CA', "
                        + "motiv        = '" + sMot.replace("'", "''") + "', "
                        + "sucu         = '" + Star.sSucu.replace("'", "''") + "', "
                        + "nocaj        = '" + Star.sNoCaj.replace("'", "''") + "' "
                        + "WHERE codcot = '" + jTab1.getValueAt(iSel[x], 1).toString() + "'";                                                
                st = con.createStatement();
                st.executeUpdate(sQ);
             }
             catch(SQLException expnSQL) 
             { 
                //Procesa el error y regresa
                Star.iErrProc(this.getClass().getName() + " " + expnSQL.getMessage(), Star.sErrSQL, expnSQL.getStackTrace(), con);                                
                return;
            }        
            
            /*Coloca la bandera para saber que hubo alguna cancelación*/
            bCan    = true;
            
        }/*Fin de for(int x = iSel.length - 1; x >= 0; x--)*/
        
        //Cierra la base de datos
        if(Star.iCierrBas(con)==-1)
            return;
        
        /*Agrega todas las cotizaciones de la base de datos a la tabla de cotizaciones*/
        (new Thread()
        {
            @Override
            public void run()
            {
                vCargCots("");                
            }
            
        }).start();
        
        /*Mensajea si hubo alguna cancelación*/
        if(bCan)
            JOptionPane.showMessageDialog(null, "Cotización(es) cancelada(s) con éxito.", "Cotización", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource(Star.sRutIconAd)));
        
    }//GEN-LAST:event_jBCanActionPerformed
     
    
    /*Cuando se presiona una tecla en el botón de cancelar*/
    private void jBCanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jBCanKeyPressed
        
        //Llama a la función escalable
        vKeyPreEsc(evt);
        
    }//GEN-LAST:event_jBCanKeyPressed

    
    /*Cuando el mouse entra en el botón de generar PDF*/
    private void jBGenPDFMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jBGenPDFMouseEntered

        /*Cambia el color del fondo del botón*/
        jBGenPDF.setBackground(Star.colBot);

    }//GEN-LAST:event_jBGenPDFMouseEntered

    
    /*Cuando el mouse sale del botón de generar PDF*/
    private void jBGenPDFMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jBGenPDFMouseExited

        /*Cambia el color del fondo del botón al original*/
        jBGenPDF.setBackground(Star.colOri);

    }//GEN-LAST:event_jBGenPDFMouseExited

    
    /*Cuando se presiona el botón de generar PDF*/
    private void jBGenPDFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBGenPDFActionPerformed

        /*Si el usuario no a seleccionado por lo menos una cotización entonces*/
        if(jTab1.getSelectedRow()==-1)
        {
            /*Mensajea*/
            JOptionPane.showMessageDialog(null, "Selecciona por lo menos una cotización.", "Generar PDF", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource(Star.sRutIconAd)));

            /*Pon el foco del teclado en la tabla y regresa*/
            jTab1.grabFocus();
            return;
        }

        /*Configura el file chooser para escoger la ruta del directorio donde esta la calculadora*/
        final JFileChooser fc                       = new JFileChooser  ();
        fc.setDialogTitle                           ("Guardar en...");
        fc.setAcceptAllFileFilterUsed               (false);
        fc.setFileSelectionMode                     (JFileChooser.DIRECTORIES_ONLY);

        /*Si el usuario presiono aceptar entonces obtiene la ruta, caso contrario regresa*/
        String sRut;
        if(fc.showSaveDialog(this)== JFileChooser.APPROVE_OPTION)
            sRut            = fc.getSelectedFile().getAbsolutePath();
        else
            return;

        //Muestra el loading
        Star.vMostLoading("");
        
        //Abre la base de datos                             
        Connection  con = Star.conAbrBas(true, false);

        //Si hubo error entonces regresa
        if(con==null)
            return;        
        
        //Trae la carpeta compartida de la aplicación y la ruta en el servidor de la base de datos
        String sCarp    = Star.sGetRutCarp(con);                    

        //Si hubo error entonces regresa
        if(sCarp==null)
            return;
        
        /*Declara variables*/
        String sNomLoc      = "";
        String sCallLoc     = "";
        String sTelLoc      = "";
        String sColLoc      = "";
        String sCPLoc       = "";
        String sCiuLoc      = "";
        String sEstLoc      = "";
        String sPaiLoc      = "";
        String sRFCLoc      = "";

        //Declara variables de la base de datos
        Statement   st;
        ResultSet   rs;        
        String      sQ;

        /*Obtiene todos los datos de la empresa local*/
        try
        {
            sQ = "SELECT nom, calle, tel, col, cp, ciu, estad, pai, rfc FROM basdats WHERE codemp = '" + Login.sCodEmpBD + "'";
            st = con.createStatement();
            rs = st.executeQuery(sQ);
            /*Si hay datos entonces obtiene los resultados*/
            if(rs.next())
            {
                sNomLoc             = rs.getString("nom");
                sCallLoc            = rs.getString("calle");
                sTelLoc             = rs.getString("tel");
                sColLoc             = rs.getString("col");
                sCPLoc              = rs.getString("cp");
                sCiuLoc             = rs.getString("ciu");
                sEstLoc             = rs.getString("estad");
                sPaiLoc             = rs.getString("pai");
                sRFCLoc             = rs.getString("rfc");
            }
        }
        catch(SQLException expnSQL)
        {
            //Procesa el error y regresa
            Star.iErrProc(this.getClass().getName() + " " + expnSQL.getMessage(), Star.sErrSQL, expnSQL.getStackTrace(), con);                                
            return;
        }
                        
        /*Declara variables*/
        String sCli     = "";
        String sSubTot  = "";
        String sImpue   = "";
        String sTot     = "";
        String sMon     = "";                
        String sFDoc    = "";
        String sObserv  = "";
        String sDescrip = "";
        
        /*Recorre toda la selección del usuario*/
        int iSel[]              = jTab1.getSelectedRows();
        for(int x = iSel.length - 1; x >= 0; x--)
        {            
            /*Completa la ruta de la cotización*/
            sRut        += "\\" + jTab1.getValueAt(iSel[x], 1).toString().trim() + ".pdf";           

            /*Obtiene algunos datos de la cotización*/
            try
            {
                sQ = "SELECT fdoc, codemp, subtot, impue, tot, mon, observ, descrip FROM cots WHERE codcot = '" + jTab1.getValueAt(iSel[x], 1).toString().trim() + "'";
                st = con.createStatement();
                rs = st.executeQuery(sQ);
                /*Si hay datos entonces obtiene los resultados*/
                if(rs.next())
                {
                    sCli        = rs.getString("codemp");
                    sSubTot     = rs.getString("subtot");
                    sImpue      = rs.getString("impue");
                    sTot        = rs.getString("tot");
                    sMon        = rs.getString("mon");
                    sFDoc       = rs.getString("fdoc");                    
                    sObserv     = rs.getString("observ");
                    sDescrip    = rs.getString("descrip");                                        
                }
            }
            catch(SQLException expnSQL)
            {
                //Procesa el error y regresa
                Star.iErrProc(this.getClass().getName() + " " + expnSQL.getMessage(), Star.sErrSQL, expnSQL.getStackTrace(), con);                                
                return;
            }
            
            /*Obtiene el símbolo de la moneda*/
            String sSimb    = "";
            try
            {
                sQ = "SELECT simb FROM mons WHERE mon = '" + sMon + "'";
                st = con.createStatement();
                rs = st.executeQuery(sQ);
                /*Si hay datos entonces obtiene el resultado*/
                if(rs.next())
                    sSimb   = rs.getString("simb");
            }
            catch(SQLException expnSQL)
            {
                //Procesa el error y regresa
                Star.iErrProc(this.getClass().getName() + " " + expnSQL.getMessage(), Star.sErrSQL, expnSQL.getStackTrace(), con);                                
                return;
            }            

            /*Declara las variable final para el thread*/
            final String sCliFi         = sCli;
            final String sCodCotFi      = jTab1.getValueAt(iSel[x], 1).toString().trim();
            final String sSubTotFi      = sSubTot;
            final String sImpueFi       = sImpue;
            final String sTotFi         = sTot;
            final String sMonFi         = sMon;
            final String sNomLocFi      = sNomLoc;
            final String sTelLocFi      = sTelLoc;
            final String sColLocFi      = sColLoc;
            final String sCallLocFi     = sCallLoc;
            final String sCPLocFi       = sCPLoc;
            final String sCiuLocFi      = sCiuLoc;
            final String sEstLocFi      = sEstLoc;
            final String sPaiLocFi      = sPaiLoc;
            final String sRFCLocFi      = sRFCLoc;
            final String sObservFi      = sObserv;
            final String sFCotFi        = sFDoc;
            final String sRutFi         = sRut;
            final String sDescripFi     = sDescrip;            
            final String sImpLetFi      = Star.sObLet(sTot, sMon, sSimb, true);
            
            /*Thread para quitar carga*/
            (new Thread()
            {
                @Override
                public void run()
                {
                    Star.vGenPDFCot(sCliFi, sCodCotFi, sMonFi, sSubTotFi, sImpueFi, sTotFi, sNomLocFi, sTelLocFi, sColLocFi, sCallLocFi, sCPLocFi, sCiuLocFi, sEstLocFi, sPaiLocFi, sRFCLocFi, sObservFi, sDescripFi, sFCotFi, sImpLetFi, false, false, null, null, null, false, sRutFi);
                }
                
            }).start();
                
        }/*Fin de for(int x = iSel.length - 1; x >= 0; x--)*/                    
                
        //Esconde la forma de loading
        Star.vOcultLoadin();

        //Cierra la base de datos
        Star.iCierrBas(con);

    }//GEN-LAST:event_jBGenPDFActionPerformed

    
    /*Cuando se presiona una tecla en el botón de generar PDF*/
    private void jBGenPDFKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jBGenPDFKeyPressed

        //Llama a la función escalable
        vKeyPreEsc(evt);

    }//GEN-LAST:event_jBGenPDFKeyPressed

    
    /*Función escalable para cuando se presiona una tecla en el módulo*/
    void vKeyPreEsc(java.awt.event.KeyEvent evt)
    {
        /*Pon la bandera para saber que ya hubó un evento y no se desloguie*/
        bIdle   = true;
        
        /*Si se presiona la tecla de escape presiona el botón de salir*/
        if(evt.getKeyCode() == KeyEvent.VK_ESCAPE) 
            jBSal.doClick();
        /*Else if se presiona Alt + T entonces presiona el botón de marcar todo*/
        else if(evt.isAltDown() && evt.getKeyCode() == KeyEvent.VK_T)
            jBTod.doClick();
        /*Si se presiona CTRL + N entonces presiona el botón de nuevo*/
        else if(evt.isControlDown() && evt.getKeyCode() == KeyEvent.VK_N)
            jBNew.doClick();
        /*Si se presiona CTRL + A entonces presiona el botón de abrir*/
        else if(evt.isControlDown() && evt.getKeyCode() == KeyEvent.VK_A)
            jBAbr.doClick();
        /*Si se presiona F3 presiona el botón de búscar*/
        else if(evt.getKeyCode() == KeyEvent.VK_F3)
            jBBusc.doClick();
        /*Else if se presiona Alt + F4 entonces presiona el botón de salir*/
        else if(evt.isAltDown() && evt.getKeyCode() == KeyEvent.VK_F4)
            jBSal.doClick();
        /*Si se presiona F4 presiona el botón de mostrar todo*/
        else if(evt.getKeyCode() == KeyEvent.VK_F4)
            jBMosT.doClick();
        /*Si se presiona F6 presiona el botón de mail*/
        else if(evt.getKeyCode() == KeyEvent.VK_F6)
            jBMail.doClick();
        /*Si se presiona F5 presiona el botón de actualizar*/
        else if(evt.getKeyCode() == KeyEvent.VK_F5)
            jBActua.doClick();
        /*Else if se presiona Alt + F presiona el botón de ver PDF*/
        else if(evt.isAltDown() && evt.getKeyCode() == KeyEvent.VK_F)
            jBPDF.doClick();
        /*Si se presiona F9 presiona el botón de directorios*/
        else if(evt.getKeyCode() == KeyEvent.VK_F9)
            jBDirCots.doClick();
        /*Si se presiona F11 presiona el botón de ver*/
        else if(evt.getKeyCode() == KeyEvent.VK_F11)
            jBVer.doClick();
        
    }/*Fin de void vKeyPreEsc(java.awt.event.KeyEvent evt)*/
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBAbr;
    private javax.swing.JButton jBActua;
    private javax.swing.JButton jBBusc;
    private javax.swing.JButton jBCan;
    private javax.swing.JButton jBDirCots;
    private javax.swing.JButton jBGenPDF;
    private javax.swing.JButton jBMail;
    private javax.swing.JButton jBMosT;
    private javax.swing.JButton jBNew;
    private javax.swing.JButton jBPDF;
    private javax.swing.JButton jBSal;
    private javax.swing.JButton jBTab1;
    private javax.swing.JButton jBTab2;
    private javax.swing.JButton jBTod;
    private javax.swing.JButton jBVer;
    private javax.swing.JButton jBVta;
    private javax.swing.JLabel jLAyu;
    private javax.swing.JLabel jLNot;
    private javax.swing.JLabel jLNotCli;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jP1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTBusc;
    private javax.swing.JTable jTab1;
    private javax.swing.JTable jTab2;
    // End of variables declaration//GEN-END:variables

}/*Fin de public class Clientes extends javax.swing.JFrame */
