package tr.edu.metu.ceng.ms.thesis.dstarlite.applet.modified.world;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class MazeCanvas extends Canvas {

	private static final long serialVersionUID = 1L;

	private Graphics2D g2d;
	
	private BufferedImage buffer;

	public MazeCanvas(Dimension d) {
		super();
		initialize(d);
		this.setSize(d);
	}

	private void initialize(Dimension d) {
		buffer = new BufferedImage(d.width, d.height,
				BufferedImage.TYPE_INT_RGB);

		g2d = (Graphics2D) buffer.getGraphics();
		g2d.setFont(new Font("Times New Roman", Font.BOLD, 11));
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
	}

	public void draw(Image imagem, int x, int y) {
		g2d.drawImage(imagem, x, y, this);
	}

	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(buffer, 0, 0, null);
	}

	public void update(Graphics g) {
		this.paint(g);
	}

	public Graphics2D getGraphics2D() {
		return g2d;
	}

	public void drawTitle(String titleText, int width) {
		//get g2d instance of this canvas.
		Graphics2D g2d = this.getGraphics2D();
		//set properties of g2d and prepare.
		g2d.setFont(new Font("SansSerif", Font.BOLD | Font.ITALIC, 30));
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, width, 70);
		g2d.setColor(Color.BLACK);
		Rectangle2D r = g2d.getFontMetrics().getStringBounds(titleText, g2d);
		int y = (70 + (int) r.getHeight()) / 2;
		int x = (width - (int) r.getWidth()) / 2;

		//draw the title.
		g2d.drawString(titleText, x, y);
	}
}