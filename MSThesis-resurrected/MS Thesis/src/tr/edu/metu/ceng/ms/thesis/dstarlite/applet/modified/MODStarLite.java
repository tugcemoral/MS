package tr.edu.metu.ceng.ms.thesis.dstarlite.applet.modified;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import tr.edu.metu.ceng.ms.thesis.dstarlite.applet.modified.world.MODStarLitePanel;

public class MODStarLite extends JFrame {

	private static final long serialVersionUID = 1L;

	private static final int APPLET_WIDTH = 1000;
	private static final int APPLET_HEIGHT = 800;

	public static void main(String[] args) {
		// create a new instance and set it visible.
		MODStarLite modStarLite = new MODStarLite();
		modStarLite.setVisible(true);
	}
	
	public MODStarLite() {
		super("MOD* Lite");
		// initialize the frame.
		init();
	}

	public void init() {
		// set generic frame properties.
		this.setSize(APPLET_WIDTH, APPLET_HEIGHT);
		this.setLocation(100,100);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setLayout(new BorderLayout());

		// add root panel into frame container.
		this.add(this.getRootPanel(), BorderLayout.CENTER);
	}

	private MODStarLitePanel getRootPanel() {
		return MODStarLitePanel.getInstance();
	}

}
