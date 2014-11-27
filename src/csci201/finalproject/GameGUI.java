package csci201.finalproject;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class GameGUI extends JPanel{
	private JPanel topPanel, bottomPanel, statPanel, firePanel, statFirePanel, fireButtonPanel, statWrapper, whosTurnLabelPanel;
	private TimerPanel timerPanel;
	private Board myBoardPanel;
	private JButton fireButton;
	private JLabel shotsFiredStatLabel, turnsTakenStatLabel, shipsSunkStatLabel, maxShotsAllowedStatLabel;
	private JTextArea whosTurnArea;
	private BoardSpaceListener bsl;
	private ButtonListener buttonListener;
	private HashMap<String, ArrayList <Coordinate> > selectedCoordinates;
	private HashMap<String, EnemyPanel> enemyPanels;
	private HashMap<Coordinate, Ship> myShips;
	private int maxShotsAllowed;
	private boolean isMyTurn;
	private String currentPlayingUser, myUsername;
	private ArrayList<String> allUsernames;
	private ArrayList<String> enemyUsernames;
	
	public GameGUI(ArrayList<String> allUserNames, String myUN, HashMap<Coordinate, Ship> myShips ){
		this.allUsernames = allUserNames;
		this.myUsername = myUN;
		this.myShips = myShips;
		
		createGUIComponents();
		setUpGUI();
		startTurn();
	}
	
	private void setUpGUI(){
		this.setLayout(new GridLayout(2,1));
		
		createTopPanel();
		createBottomPanel();
		
		//add top and bottom panels to the game gui
		this.add(topPanel);
		this.add(bottomPanel);
	}
	
	private void createGUIComponents(){
		
		enemyUsernames = new ArrayList<String>(allUsernames.size()-1);
		for (String s : allUsernames){
			if (!s.equals(myUsername)){
				enemyUsernames.add(s);
			}
		}
		
		
		bsl = new BoardSpaceListener();
		buttonListener = new ButtonListener();
		
		currentPlayingUser = enemyUsernames.get(0);
		
		topPanel = new JPanel();
		bottomPanel = new JPanel();
		myBoardPanel = new Board();
		statPanel = new JPanel();
		firePanel = new JPanel();
		statFirePanel = new JPanel();
		timerPanel = new TimerPanel(); timerPanel.repaint();
		fireButtonPanel = new JPanel();
		statWrapper = new JPanel();
		whosTurnLabelPanel = new JPanel();
		whosTurnArea = new JTextArea();

		
		isMyTurn = false;
		
		maxShotsAllowed = 5;
		
		whosTurnArea.setText(currentPlayingUser + " is now playing.");
		maxShotsAllowedStatLabel = new JLabel("Shots per turn: " + maxShotsAllowed);
		shotsFiredStatLabel = new JLabel("Shots Hit/Fired: 0/0");
		turnsTakenStatLabel = new JLabel("Turns Taken: 0");
		shipsSunkStatLabel = new JLabel("Ships Sunk: 0");
		fireButton = new JButton("FIRE!");
		fireButton.addActionListener(buttonListener);
		
		enemyPanels = new HashMap<String, EnemyPanel>(enemyUsernames.size());
		for (int i=0;i<enemyUsernames.size();i++){
			String un = enemyUsernames.get(i);
			enemyPanels.put(un, new EnemyPanel(un));
		}
		selectedCoordinates = new HashMap<String, ArrayList<Coordinate> >(enemyUsernames.size());
		for (int i=0;i<enemyUsernames.size();i++){
			selectedCoordinates.put(enemyUsernames.get(i), new ArrayList<Coordinate>());
		}
	}
	
	private int getNumSelectedCoordinates(){
		int retval = 0;
		for (ArrayList<Coordinate> al : selectedCoordinates.values()){
			retval += al.size();
		}
		return retval;
	}
	
	private void createTopPanel(){
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
		
		for (EnemyPanel ep: enemyPanels.values()){
			topPanel.add(ep);
		}
		
		topPanel.setBorder(BorderFactory.createTitledBorder("Enemy Waters"));
	}
	
	private void createBottomPanel(){
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
		
		createMyBoardPanel();
		createStatFirePanel();
		
		bottomPanel.add(myBoardPanel);
		bottomPanel.add(statFirePanel);
	}
	
	private void createMyBoardPanel(){
		myBoardPanel.setPreferredSize(new Dimension(200,200));
		myBoardPanel.setBorder(BorderFactory.createTitledBorder("My Fleet"));
	}
	
	private void createStatFirePanel(){
		statFirePanel.setLayout(new BoxLayout(statFirePanel, BoxLayout.Y_AXIS));
		createStatPanel();
		statPanel.setBorder(BorderFactory.createTitledBorder("Stats"));
		statFirePanel.add(statPanel);
		createFirePanel();
		firePanel.setBorder(BorderFactory.createTitledBorder("Launch the Cannons!"));
		statFirePanel.add(firePanel);
	}
	
	private void createFirePanel(){
		firePanel.setLayout(new BoxLayout(firePanel, BoxLayout.Y_AXIS));
		
		fireButton.setEnabled(false); //start disabled until at least one shot is selected
		fireButtonPanel.add(fireButton);
		firePanel.add(timerPanel);
		firePanel.add(fireButtonPanel);
		setUpWhosTurnArea();
		firePanel.add(whosTurnLabelPanel);
	}
	
	private void setUpWhosTurnArea(){
		whosTurnLabelPanel.add(whosTurnArea);
		whosTurnLabelPanel.setLayout(new GridLayout(1,1));
		whosTurnLabelPanel.setPreferredSize(new Dimension(100,20));
		whosTurnArea.setLineWrap(true);
		whosTurnArea.setWrapStyleWord(true);
		whosTurnArea.setBackground(Color.LIGHT_GRAY);
		whosTurnArea.setEditable(false);
	}
	
	private void createStatPanel(){
		statWrapper.setLayout(new BoxLayout(statWrapper, BoxLayout.Y_AXIS));
		statWrapper.add(maxShotsAllowedStatLabel);
		statWrapper.add(Box.createRigidArea(new Dimension(0,50)));
		statWrapper.add(shotsFiredStatLabel);
		statWrapper.add(turnsTakenStatLabel);
		statWrapper.add(shipsSunkStatLabel);
		statPanel.add(statWrapper);
	}
	
	public void startTurn(){
		isMyTurn = true;
		timerPanel.startTimer();
		fireButton.setEnabled(true);
		whosTurnArea.setForeground(Color.red);
		whosTurnArea.setText("IT'S Y'ARR TURN, TIMER RUNNING!");
	}
	
	public void endTurn(){
		isMyTurn = false;
		timerPanel.stopTimer();
		fireButton.setEnabled(false);
		
		//clear array list
		for (ArrayList<Coordinate> al : selectedCoordinates.values()){
			al.clear();
		}
		
		//reset panels
		for (EnemyPanel ep : enemyPanels.values()){
			ep.userTurnEnded();
		}
		
		//reset timer
		timerPanel.reset();
		
		//reset label
		whosTurnArea.setForeground(Color.black);
		whosTurnArea.setText(currentPlayingUser + " is now playing.");
	}
	
	public void userHasLost(String username){
		EnemyPanel ep = enemyPanels.get(username);
		ep.lostGame();
	}
	
	public void addShotsList(ArrayList<Shot> shots){
		for (Shot s: shots){
			if (s.getTargetPlayer().equals(myUsername)){
				addShot(s);
			}
		}
		myBoardPanel.receiveAttacksList(shots);
	}
	
	public void addShot(Shot shot){
		Coordinate shotDestination = shot.getShotDestination();
		int col = shotDestination.getColumn();
		int row = shotDestination.getRow();
		int idx = (10 * row) + col;
		BoardSpace bs = myBoardPanel.getBoardspaces().get(idx);
		if (myShips.get(shotDestination) == null){
			bs.setText(" M");
			bs.setBackground(Color.green);
		}
		else{
			bs.setText(" H");
			bs.setBackground(Color.red);
		}
	}
	
	private class TimerPanel extends JPanel{
		private JLabel label;
		private int timeRemaining, dotSpacing;
		private Timer timer;
		private boolean running = false;
		
		public TimerPanel(){
			super();
			label = new JLabel("Time Remaining");
			//this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			this.add(label);
			timer = new Timer();
			timeRemaining = 30;
			dotSpacing = 27;
		}
		
		class UpdateTimer extends TimerTask{

			public void run() {
				timeRemaining -= 1;
				if (timeRemaining == 0){
					endTurn();
				}
				TimerPanel.this.repaint();
			}
			
		}
		
		public void startTimer(){
			running = true;
			timer.scheduleAtFixedRate(new UpdateTimer(), 0, 1000);
		}
		
		public void stopTimer(){
			timeRemaining = 65;
			timer.cancel();
		}
		
		public void reset(){
			running = false;
			this.repaint();
		}
		
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			if (! running){
				g.setColor(Color.gray);
				for (int i=0;i<10;i++){
					g.fillOval(20 + (i*dotSpacing),20,20,20);
				}
				return;
			}
			int numCircles = timeRemaining / 6;
			if (timeRemaining < 15){
				g.setColor(Color.red);
			}
			else{
				g.setColor(Color.green);
			}
			for (int i=0;i<10;i++){
				if (i >= numCircles){
					g.drawOval(20 + (i*dotSpacing),20,20,20);
				}
				else{
					g.fillOval(20 + (i*dotSpacing),20,20,20);
				}
			}
		}
	}
	
	private class EnemyPanel extends JPanel{
		private JLabel userNameLabel, infoLabel;
		private JPanel userNamePanel, infoPanel;
		private GraphicalBoard boardPanel;
		private String username;
		private int numShipsRemaining = 5;
		
		public EnemyPanel(String username){
			this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
			this.username = username;
			setUpUserNamePanel();
			setUpBoardPanel();
			setUpInfoPanel();
			this.add(userNamePanel);
			this.add(boardPanel);
			this.add(infoPanel);
			this.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 1, Color.black));
		}

		private void setUpUserNamePanel(){
			userNamePanel = new JPanel();
			userNamePanel.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.black));
			userNameLabel = new JLabel(username);
			userNamePanel.add(userNameLabel);
		}
		
		private void setUpBoardPanel(){
			boardPanel = new GraphicalBoard(bsl, username);
		}
		
		private void setUpInfoPanel(){
			infoPanel = new JPanel();
			infoLabel = new JLabel(username + " has " + numShipsRemaining + " ships left");
			infoPanel.add(infoLabel);
			infoPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.black));
		}
		
		private void userTurnEnded(){
			boardPanel.removeRedBorders();
		}
		
		public void lostGame() {
			userNameLabel.setText(userNameLabel.getText() + " has lost.");
			boardPanel.shipWreck();
			boardPanel.repaint();
		}
		
		public void updateNumShipsRemaining(int newNum){
			numShipsRemaining = newNum;
			infoLabel.setText(username + " has " + numShipsRemaining + " ships left");
			if (newNum == 0){
				lostGame();
			}
		}
	}
	
	class BoardSpaceListener implements MouseListener{

		private BoardSpace bs;
		public void mouseClicked(MouseEvent ae) {
			bs = (BoardSpace) ae.getSource();
			if (bs.getBackground().equals(Color.black)){
				return;
			}
			if (SwingUtilities.isRightMouseButton(ae)){
				if (bs.getText().equals("  *")){
					bs.setText("");
				}
				else{
					bs.setText("  *");
				}
			}
			else{
				if (isMyTurn){
					GraphicalBoard parent = (GraphicalBoard) bs.getParent();
					String user = parent.getUsername();
					
					
					Coordinate c = new Coordinate(bs.getRow(),bs.getCol());
					ArrayList<Coordinate> sourcePanel = selectedCoordinates.get(user);
					if (bs.getBackground().equals(Color.red)){
						bs.setBackground(Color.LIGHT_GRAY);
						for (int i=0;i<sourcePanel.size();i++){
							Coordinate newCoordinate = sourcePanel.get(i);
							if (c.equals(newCoordinate)){
								selectedCoordinates.get(user).remove(newCoordinate);
							}
						}
					}
					else{ //activate the cell
						if (getNumSelectedCoordinates() < maxShotsAllowed){
							bs.setBackground(Color.red);
							selectedCoordinates.get(user).add(c);
						}
					}
				}
			}
		}
		
		public void mouseEntered(MouseEvent arg0) {	}
		
		public void mouseExited(MouseEvent arg0) {}
		
		public void mousePressed(MouseEvent arg0) {	}
		
		public void mouseReleased(MouseEvent arg0) {}
	}
	
	class ButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent ae){
			ArrayList<Shot> shots = new ArrayList<Shot>();
			for (HashMap.Entry<String, ArrayList<Coordinate> > entry : selectedCoordinates.entrySet()) {
			    String user = entry.getKey();
			    ArrayList<Coordinate> coordinates = entry.getValue();
			    for (Coordinate c : coordinates){
			    	shots.add(new Shot(user,c));
			    }
			}
			//TODO send shots array to server
			endTurn();
		}
	}
}
