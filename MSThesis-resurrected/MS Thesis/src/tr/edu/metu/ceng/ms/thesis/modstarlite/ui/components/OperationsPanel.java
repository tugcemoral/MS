package tr.edu.metu.ceng.ms.thesis.modstarlite.ui.components;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.plaf.metal.MetalBorders.PaletteBorder;

import tr.edu.metu.ceng.ms.thesis.modstarlite.data.Objective;
import tr.edu.metu.ceng.ms.thesis.modstarlite.data.ObjectiveArray;
import tr.edu.metu.ceng.ms.thesis.modstarlite.data.ObjectiveBehaviour;
import tr.edu.metu.ceng.ms.thesis.modstarlite.data.Path;
import tr.edu.metu.ceng.ms.thesis.robotutils.data.Coordinate;
import tr.edu.metu.ceng.ms.thesis.robotutils.data.IntCoord;
import tr.edu.metu.ceng.ms.thesis.robotutils.ui.MapPanel;

public abstract class OperationsPanel extends JPanel {

	private static final String NUMBER_OF_FOUND_PATHS = "# of Found Paths: ";

	private static final String CURRENT_DRAWN_PATH_NUMBER = "Current Drawn Path #: ";

	private static final String CURRENT_DRAWN_PATH_COST = "Path Cost: ";

	private static final long serialVersionUID = -7997543013874231542L;

	protected MapPanel mp;

	protected MOStaticMap sm;

	private List<Path<Coordinate>> foundPaths;

	private static int currentDrawnPath = 0;

	private JLabel numberOfFoundPathsLabel;

	private JLabel currentDrawnPathNumberLbl;

	private JLabel currentPathCostNumberLbl;

	private JButton drawPrevBttn;

	private JButton drawNextBttn;

	private JButton drawSpecPathBttn;

	private JButton saveSettingsBttn;

	private JTextField pathNumTxt;

	private LogViewerPanel logViewerlanel;

	private JLabel displaySelectPathInfoLabel;

	private JButton selectPathOkButton;

	public Object concurrentObj = new Object();

	public OperationsPanel(MapPanel mp, MOStaticMap sm) {
		this.mp = mp;
		this.sm = sm;
		// set specific properties of operational panel...
		this.setPreferredSize(new Dimension(300, 600));
		this.setBorder(new PaletteBorder());
		// this.setLayout(new BorderLayout(0, 40));
		// this.setLayout(new GridLayout(2,1));
		this.setLayout(new FlowLayout(FlowLayout.LEADING));

		this.decorate();
	}

	public List<Path<Coordinate>> getFoundPaths() {
		if (foundPaths == null) {
			foundPaths = new Vector<Path<Coordinate>>();
		}
		return foundPaths;
	}

	public void setFoundPaths(List<Path<Coordinate>> paths) {
		this.foundPaths = paths;
	}

	public void updatePanel(List<Path<Coordinate>> paths) {
		this.setFoundPaths(paths);
		// update labels.
		updateLabels();
	}

	protected abstract void draw(Path<Coordinate> path);

	protected abstract void saveSettings();

	protected void drawSpecificPath(int pathIndToDraw) {
		try {
			draw(this.getFoundPaths().get(pathIndToDraw - 1));
			currentDrawnPath = pathIndToDraw - 1;
			if (currentDrawnPath >= this.getFoundPaths().size() - 1) {
				this.getDrawNextButton().setEnabled(false);
			} else {
				this.getDrawNextButton().setEnabled(true);
			}
			if (currentDrawnPath > 0) {
				this.getDrawPrevButton().setEnabled(true);
			} else {
				this.getDrawPrevButton().setEnabled(false);
			}
			updateLabels();
		} catch (Exception aioobe) {
			JOptionPane.showMessageDialog(this,
					"Please enter a number between 1 - "
							+ this.getFoundPaths().size(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}

	}

	protected void drawNextPath(int pathIndToDraw) {
		if (currentDrawnPath >= this.getFoundPaths().size() - 1) {
			this.getDrawNextButton().setEnabled(false);
		}
		this.getDrawPrevButton().setEnabled(true);
		draw(this.getFoundPaths().get(pathIndToDraw));
		updateLabels();
	}

	protected void drawPrevPath(int pathIndToDraw) {
		if (currentDrawnPath == 0) {
			this.getDrawPrevButton().setEnabled(false);
		}
		this.getDrawNextButton().setEnabled(true);
		draw(this.getFoundPaths().get(pathIndToDraw));
		updateLabels();
	}

	private void decorate() {
		// this.add(getInfoLabel(), BorderLayout.NORTH);
		// this.add(getPathOperationsPanel(), BorderLayout.CENTER);
		// this.add(getInfoLabel());
		this.add(getPathOperationsPanel());
		// this.add(getLogViewerPanel());

	}

	private JPanel getLogViewerPanel() {
		if (logViewerlanel == null) {
			logViewerlanel = new LogViewerPanel(mp, sm);
		}
		return logViewerlanel;
	}

	private JLabel getCurrentDrawnPathNumberLbl() {
		if (currentDrawnPathNumberLbl == null) {
			currentDrawnPathNumberLbl = new JLabel();
		}
		return currentDrawnPathNumberLbl;
	}

	private JButton getDrawNextButton() {
		if (drawNextBttn == null) {
			drawNextBttn = new JButton("Next >");
			drawNextBttn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					drawNextPath(++currentDrawnPath);
				}
			});
		}
		return drawNextBttn;
	}

	private JButton getDrawPrevButton() {

		if (drawPrevBttn == null) {
			drawPrevBttn = new JButton("< Prev");
			drawPrevBttn.setEnabled(false);
			drawPrevBttn.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					drawPrevPath(--currentDrawnPath);
				}
			});

		}
		return drawPrevBttn;
	}

	private JLabel getInfoLabel() {
		// create a new information label.
		JLabel infoLabel = new JLabel(
				"<html>"
						+ "<font color=red>(LEFT CLICK)</font> Toggle Obstacle <p> "
						+ "<font color=red>(RIGHT-CLICK)</font> Change start location <p>"
						+ "<font color=blue>(MOUSE DRAG)</font> Pan around map <p> "
						+ "<font color=blue>(MOUSE WHEEL)</font> Zoom in/out of map"
						+ "</html>");
		return infoLabel;
	}

	private JPanel getLabelsPanel() {
		JPanel labelsPanel = new JPanel(new BorderLayout());

		labelsPanel.add(getInfoLabel(), BorderLayout.NORTH);
		labelsPanel.add(getNumberOfFoundPathsLabel(), BorderLayout.CENTER);
		labelsPanel.add(getCurrentDrawnPathNumberLbl(), BorderLayout.SOUTH);
		labelsPanel.add(getCurrentPathCostNumberLbl(), BorderLayout.SOUTH);

		return labelsPanel;
	}

	private JLabel getNumberOfFoundPathsLabel() {
		if (numberOfFoundPathsLabel == null) {
			numberOfFoundPathsLabel = new JLabel();
		}
		return numberOfFoundPathsLabel;
	}

	private JPanel getPathOperationsPanel() {
		// create a new panel for path operations.
		JPanel pathOpsPanel = new JPanel(new GridLayout(6, 1));

		pathOpsPanel.add(getLabelsPanel());
		pathOpsPanel.add(getDisplaySelectPathInfoPanel());
		pathOpsPanel.add(getPrevNextButtonsPanel());
		pathOpsPanel.add(getDrawSpecificPathPanel());
		pathOpsPanel.add(getSaveSettingsPanel());

		return pathOpsPanel;
	}

	private JPanel getDisplaySelectPathInfoPanel() {
		JPanel displaySelectPathInfoPanel = new JPanel(new BorderLayout(1, 0));
		displaySelectPathInfoPanel.add(getDisplaySelectPathInfoLabel(),
				BorderLayout.NORTH);
		displaySelectPathInfoPanel.add(getSelectPathOkButton(),
				BorderLayout.SOUTH);
		return displaySelectPathInfoPanel;
	}

	private JButton getSelectPathOkButton() {
		if (selectPathOkButton == null) {
			selectPathOkButton = new JButton("Ok");
			selectPathOkButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					synchronized (concurrentObj) {
						concurrentObj.notify();
					}
				}
			});
		}
		return selectPathOkButton;
	}

	private JLabel getDisplaySelectPathInfoLabel() {
		if (displaySelectPathInfoLabel == null) {
			displaySelectPathInfoLabel = new JLabel("Select path to use next");
			displaySelectPathInfoLabel.setVisible(false);
		}
		return displaySelectPathInfoLabel;
	}

	private JPanel getSaveSettingsPanel() {
		// create the save settings panel.
		JPanel saveSettingsPanel = new JPanel(new BorderLayout(5, 0));
		// add save settings button.
		saveSettingsPanel.add(getSaveSettingsButton(), BorderLayout.NORTH);
		return saveSettingsPanel;
	}

	private JButton getSaveSettingsButton() {
		if (saveSettingsBttn == null) {
			saveSettingsBttn = new JButton("Save Settings");
			saveSettingsBttn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					saveSettings();
				}
			});
		}

		return saveSettingsBttn;
	}

	private JPanel getDrawSpecificPathPanel() {
		JPanel drawSpecificPathPanel = new JPanel(new BorderLayout(5, 0));

		// create new label and text field.
		JLabel pathNumLabel = new JLabel("Enter the path # :");
		// create a temp panel and add them to it.
		JPanel tempPanel = new JPanel(new BorderLayout(5, 0));
		tempPanel.add(pathNumLabel, BorderLayout.WEST);
		tempPanel.add(getPathNumTxt(), BorderLayout.CENTER);

		drawSpecificPathPanel.add(tempPanel, BorderLayout.NORTH);
		drawSpecificPathPanel
				.add(getDrawSpecificPathBttn(), BorderLayout.SOUTH);

		return drawSpecificPathPanel;
	}

	private Component getDrawSpecificPathBttn() {
		if (drawSpecPathBttn == null) {
			drawSpecPathBttn = new JButton("Draw Path");
			drawSpecPathBttn.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					drawSpecificPath(Integer
							.parseInt(getPathNumTxt().getText()));
				}
			});
		}

		return drawSpecPathBttn;

	}

	private JPanel getPrevNextButtonsPanel() {
		JPanel prevNextPanel = new JPanel(new BorderLayout(5, 0));

		prevNextPanel.add(getDrawPrevButton(), BorderLayout.WEST);
		prevNextPanel.add(getDrawNextButton(), BorderLayout.EAST);

		return prevNextPanel;
	}

	private void updateLabels() {
		// update corresponding components.
		this.getNumberOfFoundPathsLabel().setText(
				NUMBER_OF_FOUND_PATHS + this.getFoundPaths().size());
		this.getCurrentDrawnPathNumberLbl().setText(
				CURRENT_DRAWN_PATH_NUMBER + (currentDrawnPath + 1));

		Path<Coordinate> drawnPath;
		try {
			drawnPath = this.getFoundPaths().get(currentDrawnPath);
			calculatePathCost(drawnPath);

			this.getCurrentPathCostNumberLbl().setText(
					CURRENT_DRAWN_PATH_COST
							+ ((currentDrawnPath < 0
									|| currentDrawnPath >= this.getFoundPaths()
											.size() || this.getFoundPaths()
									.size() == 0) ? "-" : drawnPath.getCost()));
		} catch (ArrayIndexOutOfBoundsException aioobe) {
			this.getCurrentPathCostNumberLbl().setText(
					CURRENT_DRAWN_PATH_COST + "-");
		}catch(IndexOutOfBoundsException ioobe) {
			// FIXME : is it right??
			this.getCurrentPathCostNumberLbl().setText(
					CURRENT_DRAWN_PATH_COST + "-");
		}
	}

	private void calculatePathCost(Path<Coordinate> drawnPath) {
		double totalPathRisk = 0d;
		for (Coordinate currentCoord : drawnPath.getRoute()) {
			IntCoord intCoord = (IntCoord) currentCoord;
			totalPathRisk += sm.get(intCoord.getInts()).getTotalRisk();
		}
		drawnPath.setCost(new ObjectiveArray(new Objective(drawnPath.size(),
				ObjectiveBehaviour.MINIMIZED), new Objective(totalPathRisk,
				ObjectiveBehaviour.MINIMIZED)));
	}

	private JTextField getPathNumTxt() {
		if (pathNumTxt == null) {
			pathNumTxt = new JTextField();
		}
		return pathNumTxt;
	}

	public void displaySelectPathInfo() {
		this.getDisplaySelectPathInfoLabel().setVisible(true);
	}

	public static int getCurrentDrawnPath() {
		return currentDrawnPath;
	}

	private JLabel getCurrentPathCostNumberLbl() {
		if (currentPathCostNumberLbl == null) {
			currentPathCostNumberLbl = new JLabel();
		}
		return currentPathCostNumberLbl;
	}

}
