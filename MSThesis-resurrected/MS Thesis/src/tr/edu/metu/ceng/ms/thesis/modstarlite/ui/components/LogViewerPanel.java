package tr.edu.metu.ceng.ms.thesis.modstarlite.ui.components;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.plaf.metal.MetalBorders.PaletteBorder;

import tr.edu.metu.ceng.ms.thesis.robotutils.ui.MapPanel;

public class LogViewerPanel extends JPanel {

	private static final long serialVersionUID = -6145327994398362632L;

	private MOStaticMap sm;

	private MapPanel mp;

	public LogViewerPanel(MapPanel mp, MOStaticMap sm) {
		this.mp = mp;
		this.sm = sm;
		// set specific properties of operational panel...
		this.setPreferredSize(new Dimension(800, 100));
		this.setBorder(new PaletteBorder());
		// this.setLayout(new BorderLayout(0, 40));
		// this.setLayout(new GridLayout(2,1));
		this.setLayout(new BorderLayout());

		this.decorate();

	}

	private void decorate() {

	}

}
