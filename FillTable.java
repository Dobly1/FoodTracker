import java.sql.*;
import java.util.*;
import javax.swing.table.*;

public class FillTable extends AbstractTableModel{

    /**
     *Not Coded by Me, I used online resource
     */


    private static final long serialVersionUID = -912060609250881296L;
    private ResultSet rs;
    private int rowCount;
    private int columnCount;
    private ArrayList data=new ArrayList();

    public FillTable(ResultSet _rs)
    {
        try {
            setRS(_rs);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
    }
    }

    public void setRS(ResultSet _rs)
    {
        try {
            this.rs = _rs;
            ResultSetMetaData metaData = _rs.getMetaData();
            rowCount = 0;
            columnCount = metaData.getColumnCount();
            while (_rs.next()) {
                Object[] row = new Object[columnCount];
                for (int j = 0; j < columnCount; j++) {
                    row[j] = _rs.getObject(j + 1);
                }
                data.add(row);
                rowCount++;
            }
        }
        catch(Exception e){

        }

    }

    public int getColumnCount(){
        return columnCount;
    }

    public int getRowCount(){
        return rowCount;
    }

    public Object getValueAt(int rowIndex, int columnIndex){
        Object[] row=(Object[]) data.get(rowIndex);
        return row[columnIndex];
    }

}