package tr.edu.metu.ceng.ms.thesis.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

public class MOPPUIMenu extends JMenuBar{

	private static final long serialVersionUID = -3663055089482380554L;
	
	public MOPPUIMenu() {
		super();
		decorateMenu();
	}

	private void decorateMenu() {
		//Build the first menu.
		JMenu fileMenu = new JMenu("File");
//		menu.setMnemonic(KeyEvent.VK_A);
//		menu.getAccessibleContext().setAccessibleDescription(
//		        "The only menu in this program that has menu items");

		//a group of JMenuItems
		JMenuItem startMenuItem = new JMenuItem("Start",
		                         KeyEvent.VK_S);
		startMenuItem.setAccelerator(KeyStroke.getKeyStroke(
		        KeyEvent.VK_1, ActionEvent.ALT_MASK));
//		menuItem.getAccessibleContext().setAccessibleDescription(
//		        "This doesn't really do anything");
		fileMenu.add(startMenuItem);

		JMenuItem resetMenuItem = new JMenuItem("Reset", KeyEvent.VK_R);
		resetMenuItem.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_2, ActionEvent.ALT_MASK));
//		startMenuItem.setMnemonic(KeyEvent.VK_B);
		fileMenu.add(resetMenuItem);
		
		fileMenu.addSeparator();

		JMenuItem exitMenuItem = new JMenuItem("Exit", KeyEvent.VK_E);
		exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_ESCAPE, ActionEvent.ALT_MASK));
		exitMenuItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		fileMenu.add(exitMenuItem);

		//add file menu to menu bar...
		this.add(fileMenu);
	}

}
