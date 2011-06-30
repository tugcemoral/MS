package tr.edu.metu.ceng.ms.thesis.ui;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import tr.edu.metu.ceng.ms.thesis.ui.grid.MOPPGridWorld;
import tr.edu.metu.ceng.ms.thesis.util.PropertiesReader;
import tr.edu.metu.ceng.ms.thesis.util.PropertyNotFoundException;

public class MOPPUIPanel extends JPanel {

	private static final long serialVersionUID = 4928458658849912435L;

	public MOPPUIPanel() {
		this.setLayout(new BorderLayout());
		// decorate the panel...
		this.decorate();
	}

	private void decorate() {
		// create a new grid component and add it to panel...
		try {
			int gridCount = PropertiesReader.getInstance().getIntProperty(
					"grid.count");
			int eachGridSize = PropertiesReader.getInstance().getIntProperty(
					"each.grid.size");
			this.add(new MOPPGridWorld(gridCount, eachGridSize), BorderLayout.CENTER);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (PropertyNotFoundException e) {
			e.printStackTrace();
		}
	}
}
