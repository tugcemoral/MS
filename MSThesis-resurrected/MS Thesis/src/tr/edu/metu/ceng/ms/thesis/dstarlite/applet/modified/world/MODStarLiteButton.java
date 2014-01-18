package tr.edu.metu.ceng.ms.thesis.dstarlite.applet.modified.world;

import javax.swing.JButton;

public class MODStarLiteButton extends JButton {

	private static final long serialVersionUID = 8927190180617823146L;

	private MODStarLiteButtonType buttonType;

	public MODStarLiteButton(String text, MODStarLiteButtonType buttonType) {
		super(text);
		this.buttonType = buttonType;
	}

	public MODStarLiteButtonType getButtonType() {
		return buttonType;
	}

	public void setButtonType(MODStarLiteButtonType buttonType) {
		this.buttonType = buttonType;
	}

}
