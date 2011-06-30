package tr.edu.metu.ceng.ms.thesis.ui.grid;

import static tr.edu.metu.ceng.ms.thesis.ui.MOPPUIConstants.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class MOPPGridWorld extends JPanel {

	private static final long serialVersionUID = 8995589626013900111L;

	private int eachGridSize;

	private int gridCount;

	private JScrollPane gridSP;

	public MOPPGridWorld(int gridCount, int eachGridSize) {
		this.gridCount = gridCount;
		this.eachGridSize = eachGridSize;

		this.setLayout(new BorderLayout(20, 20));
		// this.gridSP = new JScrollPane();
		// this.gridSP
		// .setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		// this.gridSP
		// .setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.add(new MOPPGridComponent(), BorderLayout.CENTER);
		// this.updateUI();
		// setPreferredSize(this.getParent().getPreferredSize());
	}

	public int getEachGridSize() {
		return eachGridSize;
	}

	public int getGridCount() {
		return gridCount;
	}

	public void setEachGridSize(int eachGridSize) {
		this.eachGridSize = eachGridSize;
	}

	public void setGridCount(int gridCount) {
		this.gridCount = gridCount;
	}

	private class MOPPGridComponent extends JPanel {

		private static final long serialVersionUID = -9035680853371144180L;

		public MOPPGridComponent() {
			// this.setBackground(Color.orange);
			// this.setOpaque(true);
		}

		@Override
		public void paint(Graphics g) {
			Graphics2D g2 = (Graphics2D) g;

			int count = getGridCount();
			int gridSize = getEachGridSize();

			// calculate the center locations...
			double centerX = this.getWidth() / 2;
			double centerY = this.getHeight() / 2;
			// calculate the initial draw starting locations...
			int initialDrawX = (int) (centerX - ((count * gridSize) / 2));
			int initialDrawY = (int) (centerY + ((count * gridSize) / 2));

			for (int i = 0; i < count; i++) {
				for (int j = 0; j < count; j++) {
					// create the grid with properties...
					MOPPGrid grid = new MOPPGrid(initialDrawX + (j * gridSize),
							initialDrawY - (i * gridSize), gridSize, gridSize);
					grid.setxLoc(j);
					grid.setyLoc(i);
					// update the UI environment according to created grid...
					updateUIEnvironment(g2, grid, count);
					// draw the grid...
					g2.setColor(Color.black);
					g2.draw(grid);
				}
			}
			// set preferred size of this component.
			// this.setPreferredSize(new Dimension(count * gridSize, count
			// * gridSize));
		}

		private void updateUIEnvironment(Graphics2D g2, MOPPGrid grid, int count) {
			if (hasAgent(grid)) {
				// set the agent color...
				g2.setColor(agentColor);
			} else if (hasTarget(grid, count)) {
				// set the target color...
				g2.setColor(targetColor);
			} else if (isThreadZone(grid) == 1){
//				g2.setColor(threadZoneColor_1);
			}else if(isObstacle(grid)) {
				
			}else {
				g2.setColor(standardColor);
			}

			//fill the corresponding rectangle.
			g2.fillRect(grid.x, grid.y, grid.width, grid.height);
		}

		private boolean hasTarget(MOPPGrid grid, int count) {
			return grid.getxLoc() == count - 1
					&& grid.getyLoc() == count - 1;
		}

		private boolean hasAgent(MOPPGrid grid) {
			return grid.getxLoc() == 0 && grid.getyLoc() == 0;
		}

		private int isThreadZone(MOPPGrid grid) {
			return 0;
		}

		private boolean isObstacle(MOPPGrid grid) {
			// TODO Auto-generated method stub
			return false;
		}
	}

}