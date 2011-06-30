package tr.edu.metu.ceng.ms.thesis.ui.grid;

import java.awt.Rectangle;

public class MOPPGrid extends Rectangle {

	private static final long serialVersionUID = -8643964784263194346L;

	private int xLoc;

	private int yLoc;

	public MOPPGrid(int x, int y, int width, int height) {
		super(x, y, width, height);
	}

	public int getxLoc() {
		return xLoc;
	}

	public void setxLoc(int xLoc) {
		this.xLoc = xLoc;
	}

	public int getyLoc() {
		return yLoc;
	}

	public void setyLoc(int yLoc) {
		this.yLoc = yLoc;
	}

}
