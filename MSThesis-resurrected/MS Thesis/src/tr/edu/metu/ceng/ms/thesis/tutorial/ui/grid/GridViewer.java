package tr.edu.metu.ceng.ms.thesis.tutorial.ui.grid;

import javax.swing.JFrame;

/**
 * This frame displays a grid with a size the user specified.
 */
public class GridViewer {
	public static void main(String[] args) {
		JFrame frame = new GridFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("GridViewer");
		frame.setVisible(true);
	}
}