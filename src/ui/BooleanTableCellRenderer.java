package ui;

import java.awt.Component;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class BooleanTableCellRenderer extends DefaultTableCellRenderer {

    private static final JCheckBox CHECKBOX_RENDERER = new JCheckBox();

    public BooleanTableCellRenderer() {
        super();
        CHECKBOX_RENDERER.setHorizontalAlignment(CENTER); // Center align the checkbox
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {
        CHECKBOX_RENDERER.setSelected((Boolean) value); // Set the checkbox selected state
        return CHECKBOX_RENDERER;
    }
}
