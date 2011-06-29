package tr.edu.metu.ceng.wbsa.validator2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import tr.edu.metu.ceng.wbsa.validator2.domain.ResourceValidationInfo;
import tr.edu.metu.ceng.wbsa.validator2.runners.BrokenResourceFinder;
import tr.edu.metu.ceng.wbsa.validator2.ui.StatusPanel;

public class JLinkValidator extends JFrame {

	private static final String HTTP_LINK = "http://ceng.metu.edu.tr";

	private static final long serialVersionUID = 7183392581139035840L;

	public static final int WIDTH = 600;

	public static final int HEIGHT = 500;

	private static JLinkValidator jLinkValidator;

	private BrokenResourceFinder brokenResourceFinder;

	public static JLinkValidator getInstance() {
		if (jLinkValidator == null) {
			jLinkValidator = new JLinkValidator();
		}

		return jLinkValidator;
	}

	public static void main(String[] args) {
		JLinkValidator.getInstance().setVisible(true);
	}

	private JPanel linkInsertionPanel;

	private JLabel linkInfoLabel;

	private JTextField linkTextField;

	private JButton linkValidatorButton;

	private StatusPanel statusPanel;

	private JLabel progressInfoLabel;

	private JLinkValidator() {
		super("JLinkValidator");
		// set properties of frame...
		this.setSize(WIDTH, HEIGHT);
		this.setLocation(400, 200);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// decorate frame...
		decorate();

	}

	private void decorate() {
		// set layout...
		this.setLayout(new BorderLayout());
		// add link insertion panel to north...
		this.getContentPane().add(this.getLinkInsertionPanel(),
				BorderLayout.NORTH);
		this.getContentPane().add(this.getStatusPanel(), BorderLayout.CENTER);
		this.getContentPane().add(this.getProgressInfoLabel(),
				BorderLayout.SOUTH);
	}

	private JPanel getLinkInsertionPanel() {
		if (linkInsertionPanel == null) {
			linkInsertionPanel = new JPanel();
			linkInsertionPanel.setLayout(new BorderLayout());
			// add components to this panel...
			linkInsertionPanel.add(this.getLinkInfoLabel(), BorderLayout.WEST);
			linkInsertionPanel
					.add(this.getLinkTextField(), BorderLayout.CENTER);
			linkInsertionPanel.add(this.getLinkValidatorButton(),
					BorderLayout.EAST);

		}
		return linkInsertionPanel;
	}

	private JTextField getLinkTextField() {
		if (linkTextField == null) {
			linkTextField = new JTextField();
			linkTextField.setText(HTTP_LINK);
		}
		return linkTextField;
	}

	public StatusPanel getStatusPanel() {
		if (statusPanel == null) {
			statusPanel = new StatusPanel();
		}
		return statusPanel;
	}

	private JButton getLinkValidatorButton() {
		if (linkValidatorButton == null) {
			linkValidatorButton = new JButton("Validate Link");
			linkValidatorButton.addActionListener(new DumperActionListener());
		}
		return linkValidatorButton;
	}

	protected String optimizeBaseURL(String baseURL) {
		if (baseURL == null || baseURL.equals("")) {
			throw new IllegalArgumentException("Please enter a valid URL");
		}

		if (!baseURL.startsWith("http://")) {
			baseURL = "http://" + baseURL;
		}

		String lastChar = String
				.valueOf(baseURL.toCharArray()[baseURL.length() - 1]);
		if (!lastChar.equals("/"))
			baseURL += "/";

		return baseURL;
	}

	private JLabel getLinkInfoLabel() {
		if (linkInfoLabel == null) {
			linkInfoLabel = new JLabel();
			linkInfoLabel.setText("Enter the URL: ");
		}
		return linkInfoLabel;
	}

	private BrokenResourceFinder getBrokenResourceFinder(String baseURL) {
		if (brokenResourceFinder == null) {
			brokenResourceFinder = new BrokenResourceFinder(baseURL, null);
		}
		return brokenResourceFinder;
	}

	public synchronized void addResourceValidationInfo(
			ResourceValidationInfo rvi) {
		this.getStatusPanel().addResourceValidationInfo(rvi);
	}

	private class DumperActionListener implements ActionListener, Runnable {

		@Override
		public void actionPerformed(ActionEvent e) {
			new Thread(this).start();
		}

		@Override
		public void run() {
			// get the base URL...
			String baseURL = getLinkTextField().getText();
			try {
				// optimize it...
				String optimizedBaseURL = optimizeBaseURL(baseURL);
				// update progress info label...
				getProgressInfoLabel().setText(
						"Web Crawling started at " + optimizedBaseURL);
				// find all broken resources according to given base
				// URL...
				getBrokenResourceFinder(optimizedBaseURL)
						.findAllBrokenResources();
			} catch (IllegalArgumentException iae) {
				JOptionPane.showMessageDialog(JLinkValidator.getInstance(),
						iae.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	private JLabel getProgressInfoLabel() {
		if (progressInfoLabel == null) {
			progressInfoLabel = new JLabel();
			progressInfoLabel.setForeground(Color.RED);
		}
		return progressInfoLabel;
	}

	public void updateProgressInfo(String message) {
		this.getProgressInfoLabel().setText(message);

	}

}
