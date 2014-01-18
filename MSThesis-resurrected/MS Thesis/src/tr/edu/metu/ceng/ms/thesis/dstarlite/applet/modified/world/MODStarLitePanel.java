package tr.edu.metu.ceng.ms.thesis.dstarlite.applet.modified.world;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class MODStarLitePanel extends JPanel implements MouseListener {

	private static final long serialVersionUID = 9079513720205590515L;

	private static MODStarLitePanel modStarLitePanel;

	public static MODStarLitePanel getInstance() {
		if (modStarLitePanel == null) {
			modStarLitePanel = new MODStarLitePanel();
		}
		return modStarLitePanel;
	}

	private MODStarLitePanel() {
		//set the layout of this panel...
		this.setLayout(new GridBagLayout());
		//add the mouse listener event to this component.
//		this.addMouseListener(this);
		// decorate the root panel.
		this.decorate();
	}

	private Maze maze;
	private MazeCanvas realMazeCanvas;
	private MazeCanvas dStarCanvas;

	private JButton restartButton;
	private JButton moveButton;
	private JButton nextStepButton;
	private JButton changeStartButton;
	private JButton changeGoalButton;

	private boolean place_start;
	private boolean place_goal;
	private boolean single_step;
	private boolean executing;
	@SuppressWarnings("unused")
	private boolean first_change;

	private JCheckBox singleStepExecutionCB;
	private JCheckBox diagonalsCB;

	private JTextArea ta;

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON3) {
			maze.printCell(e.getX(), e.getY());
			return;
		}

		if (place_start) {
			maze.setStart(e.getX(), e.getY());
			place_start = false;
			enableComponents(true);

		} else if (place_goal) {
			maze.setGoal(e.getX(), e.getY());
			place_goal = false;
			enableComponents(true);

		} else {
			if (executing)
				return;

			maze.transformCell(e.getX(), e.getY());
			maze.drawRealMaze(realMazeCanvas.getGraphics2D());
			realMazeCanvas.repaint();

			return;
		}

		first_change = true;

		maze.calculatePath(single_step);
		maze.drawRealMaze(realMazeCanvas.getGraphics2D());
		maze.drawMaze(dStarCanvas.getGraphics2D());

		if (single_step) {
			executing = true;
			getNextStepButton().setEnabled(true);
		}

		ta.setText(maze.getStack());

		if (maze.executionEnd()) {
			getNextStepButton().setEnabled(false);
			executing = false;
		}

		realMazeCanvas.repaint();
		dStarCanvas.repaint();
	}

	public void mouseReleased(MouseEvent e) {
	}

	private void decorate() {
		
		// create the root GBC.
		GridBagConstraints rootGBC = new GridBagConstraints();

		maze = new Maze(Math.random() * Long.MAX_VALUE, 7, 9);
		realMazeCanvas = new MazeCanvas(maze.getPrintedMazeSize());
		dStarCanvas = new MazeCanvas(maze.getPrintedMazeSize());

		int width = (int) maze.getPrintedMazeSize().getWidth();

		// initialize title canvases.
		MazeCanvas realMazeTitleCanvas = new MazeCanvas(
				new Dimension(width, 70));
		MazeCanvas dStarMazeTitleCanvas = new MazeCanvas(new Dimension(width,
				70));

		realMazeTitleCanvas.drawTitle("Real Map", width);
		dStarMazeTitleCanvas.drawTitle("D* Lite", width);

		rootGBC.insets = new Insets(0, 3, 0, 0);

		rootGBC.fill = GridBagConstraints.NONE;
		rootGBC.gridx = 0;
		rootGBC.gridy = 0;
		rootGBC.gridheight = 1;
		rootGBC.gridwidth = 1;
		this.add(realMazeTitleCanvas, rootGBC);

		rootGBC.fill = GridBagConstraints.NONE;
		rootGBC.gridx = 1;
		rootGBC.gridy = 0;
		rootGBC.gridheight = 1;
		rootGBC.gridwidth = 1;
		this.add(dStarMazeTitleCanvas, rootGBC);

		rootGBC.fill = GridBagConstraints.NONE;
		rootGBC.gridx = 0;
		rootGBC.gridy = 1;
		rootGBC.gridheight = 1;
		rootGBC.gridwidth = 1;
		this.add(realMazeCanvas, rootGBC);

		rootGBC.fill = GridBagConstraints.NONE;
		rootGBC.gridx = 1;
		rootGBC.gridy = 1;
		rootGBC.gridheight = 1;
		rootGBC.gridwidth = 1;
		this.add(dStarCanvas, rootGBC);

		rootGBC.fill = GridBagConstraints.VERTICAL;
		rootGBC.gridx = 2;
		rootGBC.gridy = 1;
		rootGBC.gridheight = 1;
		rootGBC.gridwidth = 1;
		ta = new JTextArea("", 1, 12);
		ta.setFocusable(false);
		this.add(ta, rootGBC);

		// construct the buttons container and buttons' functionalities.
		Container buttons = constructButtonsContainer();

		rootGBC.fill = GridBagConstraints.BOTH;
		rootGBC.gridx = 0;
		rootGBC.gridy = 2;
		rootGBC.gridheight = 1;
		rootGBC.gridwidth = 3;
		this.add(buttons, rootGBC);

		 realMazeCanvas.addMouseListener(this);
//		 realMazeCanvas.addKeyListener(this);
		 dStarCanvas.addMouseListener(this);
//		 dStarCanvas.addKeyListener(this);

		executing = single_step = place_start = place_goal = false;
		first_change = true;

		maze.initialize();
		maze.calculatePath(single_step);
		maze.drawRealMaze(realMazeCanvas.getGraphics2D());
		maze.drawMaze(dStarCanvas.getGraphics2D());

		realMazeCanvas.repaint();
		dStarCanvas.repaint();
		ta.setText(maze.getStack());

	}

	private Container constructButtonsContainer() {
		// create a container for buttons...
		Container buttons = new Container();
		// set layout of this container...
		buttons.setLayout(new FlowLayout());

		// add initialized buttons on to buttons component...
		buttons.add(getRestartButton());
		buttons.add(getMoveButton());
		buttons.add(getChangeStartButton());
		buttons.add(getChangeGoalButton());
		buttons.add(getNextStepButton());
		// create and add checkboxes into it.
		buttons.add(getSingleStepExecutionCB());
		buttons.add(getDiagonalsCB());

		return buttons;
	}

	private JButton getRestartButton() {
		if (restartButton == null) {
			restartButton = new JButton("Restart");
			restartButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					first_change = true;

					maze.initialize();
					maze.calculatePath(single_step);
					maze.drawMaze(dStarCanvas.getGraphics2D());
					maze.drawRealMaze(realMazeCanvas.getGraphics2D());

					realMazeCanvas.repaint();
					dStarCanvas.repaint();

					getMoveButton().setEnabled(true);
					getNextStepButton().setEnabled(false);
					executing = false;

					if (!maze.executionEnd()) {
						getNextStepButton().setEnabled(true);
						getMoveButton().setEnabled(false);
						executing = true;
					}
					ta.setText(maze.getStack());
				}
			});

		}
		return restartButton;
	}

	private JButton getMoveButton() {
		if (moveButton == null) {
			moveButton = new JButton("Move");
			moveButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					first_change = true;

					maze.move(single_step);
					maze.drawRealMaze(realMazeCanvas.getGraphics2D());
					maze.drawMaze(dStarCanvas.getGraphics2D());

					realMazeCanvas.repaint();
					dStarCanvas.repaint();
					if (!maze.executionEnd() || maze.reachedGoal()) {
						moveButton.setEnabled(false);
						if (single_step) {
							getNextStepButton().setEnabled(true);
							executing = true;
						}
					}
					ta.setText(maze.getStack());
				}
			});
		}
		return moveButton;
	}

	private JButton getNextStepButton() {
		if (nextStepButton == null) {
			nextStepButton = new JButton("Next Step");
			nextStepButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					maze.calculatePath(single_step);
					maze.drawMaze(dStarCanvas.getGraphics2D());
					dStarCanvas.repaint();

					if (maze.executionEnd()) {
						nextStepButton.setEnabled(false);
						getMoveButton().setEnabled(true);
						executing = false;
					}
					ta.setText(maze.getStack());
				}
			});
		}
		return nextStepButton;
	}

	private JButton getChangeStartButton() {
		if (changeStartButton == null) {
			changeStartButton = new JButton("Place Start");
			changeStartButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					enableComponents(false);
					place_start = true;
				}
			});
		}

		return changeStartButton;
	}

	private JButton getChangeGoalButton() {
		if (changeGoalButton == null) {
			changeGoalButton = new JButton("Place Goal");
			changeGoalButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					enableComponents(false);
					place_goal = true;
				}
			});
		}

		return changeGoalButton;
	}

	private JCheckBox getSingleStepExecutionCB() {
		if (singleStepExecutionCB == null) {
			singleStepExecutionCB = new JCheckBox("Step by Step");
			singleStepExecutionCB.addItemListener(new ItemListener() {

				@Override
				public void itemStateChanged(ItemEvent e) {
					single_step = !single_step;
				}
			});
		}
		return singleStepExecutionCB;
	}

	private JCheckBox getDiagonalsCB() {
		if (diagonalsCB == null) {
			diagonalsCB = new JCheckBox("Allow Diagonal Movement");
			diagonalsCB.addItemListener(new ItemListener() {

				@Override
				public void itemStateChanged(ItemEvent e) {
					first_change = true;

					maze.setDiagonal(diagonalsCB.isSelected());
					maze.initialize();
					maze.calculatePath(single_step);
					maze.drawMaze(dStarCanvas.getGraphics2D());
					maze.drawRealMaze(realMazeCanvas.getGraphics2D());

					if (single_step) {
						executing = true;
						getNextStepButton().setEnabled(true);
					}

					ta.setText(maze.getStack());

					if (maze.executionEnd()) {
						getNextStepButton().setEnabled(false);
						executing = false;
					}

					realMazeCanvas.repaint();
					dStarCanvas.repaint();

				}
			});

		}

		return diagonalsCB;
	}

	private void enableComponents(boolean e) {
		getChangeStartButton().setEnabled(e);
		getChangeGoalButton().setEnabled(e);
		getMoveButton().setEnabled(e);
		getNextStepButton().setEnabled(e);
		getSingleStepExecutionCB().setEnabled(e);
		getDiagonalsCB().setEnabled(e);
		getRestartButton().setEnabled(e);
	}

}
