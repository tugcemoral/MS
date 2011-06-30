package tr.edu.metu.ceng.ms.thesis.ui;

import java.awt.BorderLayout;

import javax.swing.JFrame;

public class MOPPExecutor extends JFrame {

	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		MOPPExecutor executor = new MOPPExecutor();
		executor.setVisible(true);
	}

	private static final int X_LOC = 300;

	private static final int Y_LOC = 100;

	public static final int WIDTH = 1000;

	public static final int HEIGHT = 800;

	private static MOPPExecutor executorInstance;

	public static MOPPExecutor getInstance() {
		if (executorInstance == null) {
			executorInstance = new MOPPExecutor();
		}
		return executorInstance;
	}

	private MOPPUIPanel uiPanel;

	private MOPPUIMenu uiMenu;

	private MOPPExecutor() {
		super("MOPP UI");
		// set generic frame properties...
//		this.setAlwaysOnTop(true);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setBounds(X_LOC, Y_LOC, WIDTH, HEIGHT);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		this.setResizable(false);
		// decorate the frame...
		this.decorate();
	}

	private void decorate() {
		// set this frame's layout...
		this.setLayout(new BorderLayout());
		// put corresponding components into frame...
		this.setJMenuBar(getUiMenu());
		this.add(getUiPanel());
	}

	private MOPPUIPanel getUiPanel() {
		if (uiPanel == null) {
			uiPanel = new MOPPUIPanel();
		}
		return uiPanel;
	}

	private MOPPUIMenu getUiMenu() {
		if (uiMenu == null) {
			uiMenu = new MOPPUIMenu();
		}
		return uiMenu;
	}

}
