package tr.edu.metu.ceng.wbsa.validator2.ui.tablemodel;

public class ImagesStatusTableModel extends StatusTableModel {

	private static final long serialVersionUID = -1177555292260684635L;

	@Override
	public String[] getColumnNames() {
		return new String[] { "Image URL", "Is Valid?", "Message" };
	}

}
