package tr.edu.metu.ceng.ms.thesis.tutorial.ui.grid;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.JComponent;

public class GridComponent extends JComponent {

	public GridComponent(int n) {
		side = n;
		setPreferredSize(new Dimension(1000, 1000));

	}

	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		int count = side;
		int size = 10;

		for (int i = 0; i < count; i++) {
			for (int j = 0; j < count; j++) {
				Rectangle grid = new Rectangle(300 + i * size, 20 + j * size,
						size, size);
				g2.draw(grid);

			}
		}

	}

	private int side;

}