package tr.edu.metu.ceng.wbsa.validator2.ui.tablemodel;

public class LinkStatusTableModel extends StatusTableModel {

	private static final long serialVersionUID = -3474172345111446385L;

	@Override
	public String[] getColumnNames() {
		return new String[] { "URL", "Is Valid?", "Message" };
	}

}
