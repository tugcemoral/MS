package tr.edu.metu.ceng.wbsa.validator2.ui;

import java.awt.BorderLayout;

import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;

import tr.edu.metu.ceng.wbsa.validator2.domain.ResourceType;
import tr.edu.metu.ceng.wbsa.validator2.domain.ResourceValidationInfo;
import tr.edu.metu.ceng.wbsa.validator2.ui.tablemodel.ImagesStatusTableModel;
import tr.edu.metu.ceng.wbsa.validator2.ui.tablemodel.LinkStatusTableModel;
import tr.edu.metu.ceng.wbsa.validator2.ui.tablemodel.ScriptStatusTableModel;
import tr.edu.metu.ceng.wbsa.validator2.ui.tablemodel.StatusTableModel;

public class StatusTabbedPane extends JTabbedPane {

	private static final long serialVersionUID = -7789957177248059913L;

	private JTable linksTable;
	private StatusTableModel linksTableModel;

	private JTable imagesTable;
	private StatusTableModel imagesTableModel;

	private JTable scriptsTable;
	private StatusTableModel scriptsTableModel;

	public StatusTabbedPane() {
		// add tabs...
		this.addTab("Links", null, new JScrollPane(this.getLinksTable()),
				"Shows link validation status");
		this.addTab("Images", null, new JScrollPane(this.getImagesTable()),
				"Shows image validation status");
		this.addTab("Scripts", null, new JScrollPane(this.getScriptsTable()),
				"Shows script validation status");
	}

	private JTable getLinksTable() {
		if (linksTable == null) {
			linksTable = new JTable(this.getLinksTableModel());
			linksTable
					.setDefaultRenderer(
							String.class,
							getLinksTableModel().new StatusBrokenResourceWarningCellRenderer());
		}

		return linksTable;
	}

	private StatusTableModel getLinksTableModel() {
		if (linksTableModel == null) {
			linksTableModel = new LinkStatusTableModel();
		}
		return linksTableModel;
	}

	private JTable getScriptsTable() {
		if (scriptsTable == null) {
			scriptsTable = new JTable(this.getScriptsTableModel());
			scriptsTable
					.setDefaultRenderer(
							String.class,
							getScriptsTableModel().new StatusBrokenResourceWarningCellRenderer());
		}
		return scriptsTable;
	}

	private StatusTableModel getScriptsTableModel() {
		if (scriptsTableModel == null) {
			scriptsTableModel = new ScriptStatusTableModel();
		}
		return scriptsTableModel;
	}

	private JTable getImagesTable() {
		if (imagesTable == null) {
			imagesTable = new JTable(this.getImagesTableModel());
			imagesTable
					.setDefaultRenderer(
							String.class,
							getImagesTableModel().new StatusBrokenResourceWarningCellRenderer());
		}
		return imagesTable;
	}

	private StatusTableModel getImagesTableModel() {
		if (imagesTableModel == null) {
			imagesTableModel = new ImagesStatusTableModel();
		}
		return imagesTableModel;
	}

	public void addResourceValidationInfo(ResourceValidationInfo rvi) {
		if (rvi.getResourceType().equals(ResourceType.link)) {
			this.getLinksTableModel().addResourceValidationInfo(rvi);
		} else if (rvi.getResourceType().equals(ResourceType.image)) {
			this.getImagesTableModel().addResourceValidationInfo(rvi);
		} else {
			this.getScriptsTableModel().addResourceValidationInfo(rvi);
		}
	}

}
