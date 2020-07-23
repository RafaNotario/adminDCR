package renderTable;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class TModel extends DefaultTableModel{
    
/*    Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class
            };
    boolean[] canEdit = new boolean [] {
                false, false, false, false
            };
   */ 
    public TModel(String[][] mat,String[] cab){//String[][] mat
        //unos valores por default :)
         super(mat,cab);         
         isCellEditable(mat.length, mat[0].length);
    }
   

     
            @Override
            public boolean isCellEditable (int fila, int columna) {
             //   if(fila>1)
                return true;
               // else
                 //   return true;
            }
                

    
  /*  @Override
    public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
          return canEdit [columnIndex];
    }*/
}
