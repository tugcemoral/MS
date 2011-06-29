package tr.edu.metu.ceng.wbsa.validator2.ui.tablemodel;

public class ScriptStatusTableModel extends StatusTableModel {

	private static final long serialVersionUID = 6843241150857972292L;

	@Override
	public String[] getColumnNames() {
		return new String[] { "Script URL", "Is Valid?", "Message" };
	}

}
