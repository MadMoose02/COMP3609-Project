package Game;
/**
 * GameWindow.java
 * Extended from Game Programming lab content
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
	
public class GameWindow extends JFrame 
	implements ActionListener, KeyListener, MouseListener {

	private JLabel statusBarL;
	private JLabel keyL;
	private JLabel mouseL;

	// declare text fields

	private JTextField statusBarTF;
	private JTextField keyTF;
	private JTextField mouseTF;

	// declare buttons

	private JButton startB;
	private JButton pauseB;
	private JButton endB;

	private Container c;

	private JPanel mainPanel;
	private GamePanel gamePanel;

	public GameWindow() {
 
		setTitle ("Mystery Mutt Mayhem");
		setSize (700, 675);
        // setLocation(2200, 0);

		// create user interface objects

		// create labels

		statusBarL = new JLabel ("Application Status: ");
		keyL = new JLabel("Key Pressed: ");
		mouseL = new JLabel("Location of Mouse Click: ");

		// create text fields and set their colour, etc.

		statusBarTF = new JTextField (25);
		keyTF = new JTextField (25);
		mouseTF = new JTextField (25);

		statusBarTF.setEditable(false);
		keyTF.setEditable(false);
		mouseTF.setEditable(false);

		statusBarTF.setBackground(Color.CYAN);
		keyTF.setBackground(Color.YELLOW);
		mouseTF.setBackground(Color.GREEN);

		// create buttons

        startB = new JButton ("Start Game");
        pauseB = new JButton ("Pause Game");
        endB = new JButton ("End Game");


		// add listener to each button (same as the current object)

		startB.addActionListener(this);
		pauseB.addActionListener(this);
		endB.addActionListener(this);

		// create mainPanel

		mainPanel = new JPanel();
		FlowLayout flowLayout = new FlowLayout();
		mainPanel.setLayout(flowLayout);

		GridLayout gridLayout;

		// create the gamePanel for game entities

		gamePanel = new GamePanel();
        gamePanel.setPreferredSize(new Dimension(600, 500));

		// create infoPanel

		JPanel infoPanel = new JPanel();
		gridLayout = new GridLayout(3, 2);
		infoPanel.setLayout(gridLayout);
		infoPanel.setBackground(Color.ORANGE);

		// add user interface objects to infoPanel
	
		infoPanel.add (statusBarL);
		infoPanel.add (statusBarTF);

		infoPanel.add (keyL);
		infoPanel.add (keyTF);		

		infoPanel.add (mouseL);
		infoPanel.add (mouseTF);

		
		// create buttonPanel
		JPanel buttonPanel = new JPanel();
		gridLayout = new GridLayout(2, 3);
		buttonPanel.setLayout(gridLayout);

		// add buttons to buttonPanel
		buttonPanel.add (startB);
		buttonPanel.add (pauseB);
		buttonPanel.add (endB);

		// add sub-panels with GUI objects to mainPanel and set its colour
		mainPanel.add(infoPanel);
		mainPanel.add(gamePanel);
		mainPanel.add(buttonPanel);
		mainPanel.setBackground(Color.PINK);

		// set up mainPanel to respond to keyboard and mouse
		gamePanel.addMouseListener(this);
		mainPanel.addKeyListener(this);

		// add mainPanel to window surface
		c = getContentPane();
		c.add(mainPanel);

		// set properties of window
		setResizable(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

		// set status bar message
		statusBarTF.setText("Application started.");
	}


	
    /** ActionListener method */

	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		statusBarTF.setText(command + " button clicked.");

		if (command.equals(startB.getText())) { gamePanel.startNewGame(); }

		if (command.equals(pauseB.getText())) {
			gamePanel.pauseGame();
			if (command.equals("Pause Game"))
				pauseB.setText ("Resume");
			else
				pauseB.setText ("Pause Game");

		}
		
		if (command.equals(endB.getText())) { gamePanel.endGame(); }

		mainPanel.requestFocus();
	}


	/** KeyListener methods */

	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		String keyText = KeyEvent.getKeyText(keyCode);
		keyTF.setText(keyText + " pressed.");
        gamePanel.handleKeyInput(keyCode);
	}

	public void keyReleased(KeyEvent e) {}

	public void keyTyped(KeyEvent e) {}


    /** MouseListener methods */

	public void mouseClicked(MouseEvent e) {
        mouseTF.setText("Location of Mouse Click: " + e.getX() + ", " + e.getY());
    }

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}

	public void mousePressed(MouseEvent e) {}

	public void mouseReleased(MouseEvent e) {}

}