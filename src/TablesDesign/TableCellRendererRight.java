package TablesDesign;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class TableCellRendererRight extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {

        // returns component used for default header rendering
        // makes it independent on current L&F

        Component retr = table.getTableHeader().getDefaultRenderer().
                getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

        if ( JLabel.class.isAssignableFrom(retr.getClass()) ) {

            JLabel jl = (JLabel) retr;
            jl.setHorizontalAlignment(SwingConstants.RIGHT);

        }

        return retr;

    }

    @Override
    public void validate() {}

    @Override
    public void revalidate() {}

    @Override
    public void firePropertyChange(
            String propertyName, boolean oldValue, boolean newValue) {}

    @Override
    public void firePropertyChange(
            String propertyName, Object oldValue, Object newValue) {}

}