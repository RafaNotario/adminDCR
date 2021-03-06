/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package renderTable;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author monit
 */
public class ColorCelda extends DefaultTableCellRenderer{
    
    public Integer[] arrIntRowsIluminados;
    
    @Override
    public Component getTableCellRendererComponent(JTable table,
                                                                                        Object value,
                                                                                        boolean isSelected,
                                                                                        boolean hasFocus,
                                                                                        int row,
                                                                                        int column){
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if(FnBoolrowiluminated(row)){  
            this.setForeground(Color.RED);
        }else{
               this.setForeground(Color.BLACK);
       }
        return this;
    }
    
    public boolean FnBoolrowiluminated(int iRow){
        boolean bResult = false;//variable de resultado
        for(Integer arrIntRowsIluminado : arrIntRowsIluminados){
            //verifica si correspnde
            if(arrIntRowsIluminado == iRow){
                bResult = true;
                break;
            }
        }
        return bResult;
    }
}
