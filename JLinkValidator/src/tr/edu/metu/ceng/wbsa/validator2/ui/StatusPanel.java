package tr.edu.metu.ceng.wbsa.validator2.ui;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import tr.edu.metu.ceng.wbsa.validator2.domain.ResourceValidationInfo;

public class StatusPanel extends JPanel {

	private static final long serialVersionUID = -1666400723666984271L;

	private StatusTabbedPane resourcesTabbedPane;

	public StatusPanel() {
		this.decorate();
	}

	private void decorate() {
		this.setLayout(new BorderLayout());
		this.add(this.getResourcesTabbedPane(), BorderLayout.CENTER);
	}

	public void addResourceValidationInfo(ResourceValidationInfo rvi) {
		this.getResourcesTabbedPane().addResourceValidationInfo(rvi);
	}

	private StatusTabbedPane getResourcesTabbedPane() {
		if (resourcesTabbedPane == null) {
			resourcesTabbedPane = new StatusTabbedPane();
		}
		return resourcesTabbedPane;
	}

}
