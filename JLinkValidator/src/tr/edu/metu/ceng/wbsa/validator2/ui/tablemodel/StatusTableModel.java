package tr.edu.metu.ceng.wbsa.validator2.ui.tablemodel;

import java.awt.Color;
import java.awt.Component;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import tr.edu.metu.ceng.wbsa.validator2.domain.ResourceType;
import tr.edu.metu.ceng.wbsa.validator2.domain.ResourceValidationInfo;

public abstract class StatusTableModel extends DefaultTableModel {

	private static final String VALID = "valid";
	private static final String MISSING = "missing";

	private static final long serialVersionUID = 9211963941647868545L;

	// Columns Indexes.
	private static final int URL_COL = 0;
	private static final int IS_VALID_COL = 1;
	private static final int MESSAGE_COL = 2;

	// Types of the columns.
	private static Class[] columnTypes = { String.class, String.class,
			String.class };

	private Vector resourceValidationInfos;

	@Override
	public String getColumnName(int col) {
		return getColumnNames()[col];
	}

	@Override
	public Class getColumnClass(int col) {
		return columnTypes[col];
	}

	@Override
	public int getColumnCount() {
		return getColumnNames().length;
	}

	public abstract String[] getColumnNames();

	@Override
	public int getRowCount() {
		return getResourceValidationInfos().size();
	}

	@Override
	public Object getValueAt(int row, int column) {
		ResourceValidationInfo rvi = (ResourceValidationInfo) (getResourceValidationInfos()
				.elementAt(row));

		switch (column) {
		case URL_COL:
			return rvi.getUrl();
		case IS_VALID_COL:
			return rvi.isValid() ? VALID : MISSING;
//		case TYPE_COL:
//			return rvi.getResourceType().toString();
		case MESSAGE_COL:
			return rvi.getMessage();
		}
		return new String();
	}

	@Override
	public void setValueAt(Object value, int row, int column) {
		ResourceValidationInfo rvi = (ResourceValidationInfo) (getResourceValidationInfos()
				.elementAt(row));

		switch (column) {
		case URL_COL:
			rvi.setUrl((String) value);
			break;
		case IS_VALID_COL:
			rvi.setValid(((String) value).equals(VALID) ? Boolean.TRUE
					: Boolean.FALSE);
			break;
//		case TYPE_COL:
//			rvi.setResourceType(ResourceType.valueOf((String) value));
//			break;
		case MESSAGE_COL:
			rvi.setMessage((String) value);
			break;
		}
	}

	@Override
	public boolean isCellEditable(int arg0, int arg1) {
		return false;
	}

	public Vector getResourceValidationInfos() {
		if (resourceValidationInfos == null) {
			resourceValidationInfos = new Vector();
		}

		return resourceValidationInfos;
	}

	public void addResourceValidationInfo(ResourceValidationInfo rvi) {
		this.getResourceValidationInfos().add(rvi);
		fireTableDataChanged();
	}

	public class StatusBrokenResourceWarningCellRenderer extends
			DefaultTableCellRenderer {

		private static final long serialVersionUID = -6951112933514041764L;

		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			Component component = super.getTableCellRendererComponent(table,
					value, isSelected, hasFocus, row, column);

			String s = table.getModel().getValueAt(row, IS_VALID_COL)
					.toString();

			if (s.equalsIgnoreCase(MISSING)) {
				component.setForeground(Color.red);
			} else {
				component.setForeground(null);
			}

			return (component);
		}

	}

}
