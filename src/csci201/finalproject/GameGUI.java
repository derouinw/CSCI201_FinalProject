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

import csci201.finalproject.BSClient.NetworkThread;

public class GameGUI extends JPanel{
	private JPanel topPanel, bottomPanel, statPanel, firePanel, statFirePanel, fireButtonPanel, statWrapper, whosTurnLabelPanel;
	private TimerPanel timerPanel;
	private Board myBoardPanel;
	private JButton fireButton;
	private JLabel shotsFiredStatLabel, turnsTakenStatLabel, averageTurnTimeStatLabel, maxShotsAllowedStatLabel;
	private JTextArea whosTurnArea;
	private BoardSpaceListener bsl;
	private ButtonListener buttonListener;
	private HashMap<String, ArrayList <Coordinate> > selectedCoordinates;
	private HashMap<String, EnemyPanel> enemyPanels;
	private HashMap<Coordinate, Ship> myShips;
	private int maxShotsAllowed, turnsTaken, shotsFired, shotsHit;
	private double averageTimePerTurn, totalTimeTaken;
	private boolean isMyTurn;
	private String currentPlayingUser, nextPlayer, myUsername;
	private ArrayList<String> allUsernames;
	private ArrayList<String> enemyUsernames;
	
	NetworkThread nt;
	
	public GameGUI(NetworkThread nt) {
		this.nt = nt;
	}
	
	public GameGUI(){}
	
	public void load(ArrayList<String> allUserNames, String myUN, Board b){
		this.allUsernames = allUserNames;
		this.myUsername = myUN;
		this.myBoardPanel = b;
		
		createGUIComponents();
		setUpGUI();
		startTurn();
		
		this.myShips = myBoardPanel.getMap();
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
		
		enemyUsernames = new ArrayList<String>();
		for (String s : allUsernames){
			if (!s.equals(myUsername)){
				enemyUsernames.add(s);
			}
		}
		
		
		bsl = new BoardSpaceListener();
		buttonListener = new ButtonListener();
		
		currentPlayingUser = nextPlayer = "";
		
		
		topPanel = new JPanel();
		bottomPanel = new JPanel();
		statPanel = new JPanel();
		firePanel = new JPanel();
		statFirePanel = new JPanel();
		timerPanel = new TimerPanel(); timerPanel.repaint();
		fireButtonPanel = new JPanel();
		statWrapper = new JPanel();
		whosTurnLabelPanel = new JPanel();
		whosTurnArea = new JTextArea();

		
		isMyTurn = false;
		
		maxShotsAllowed = myBoardPanel.getHealthOfLargestShip();
		turnsTaken = 0;
		shotsFired = 0;
		shotsHit = 0;
		averageTimePerTurn= 0;
		totalTimeTaken = 0;
		
		whosTurnArea.setText(currentPlayingUser + " is now firing.");
		maxShotsAllowedStatLabel = new JLabel("Shots per turn: " + maxShotsAllowed);
		shotsFiredStatLabel = new JLabel("Shots Hit/Fired: " + shotsHit + "/" + shotsFired);
		turnsTakenStatLabel = new JLabel("Turns Taken: " + turnsTaken);
		averageTurnTimeStatLabel = new JLabel("Sec per turn: " + averageTimePerTurn);
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
		
		fireButton.setEnabled(false); //disable until all shots have been selected
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
		statWrapper.add(averageTurnTimeStatLabel);
		statPanel.add(statWrapper);
	}
	
	public void startTurn(){
		isMyTurn = true;
		timerPanel.startTimer();
		maxShotsAllowed = myBoardPanel.getHealthOfLargestShip();
		maxShotsAllowedStatLabel.setText("Shots per turn: " + maxShotsAllowed);
		whosTurnArea.setForeground(Color.red);
		whosTurnArea.setText("IT'S Y'ARR TURN, TIMER RUNNING!");
	}
	
	public void endTurn(String curUser){
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
		currentPlayingUser = curUser;
		nextPlayer = curUser;
		whosTurnArea.setText(currentPlayingUser + " is now firing.");
	}
	
	public void userHasLost(String username){
		if (username.equals(nt.username)) {
			// you lose!
			whosTurnArea.setText("You lose! ARGGGGHHH");
			isMyTurn = false;
		} else {
			EnemyPanel ep = enemyPanels.get(username);
			ep.lostGame();
		}
	}

	public Shot checkShot(Shot s) {
		Shot ret = new Shot(s);
		if (myBoardPanel.processAttack(s)) ret.shotHitShip();
		myBoardPanel.repaint();
		return ret;
	}
	
	public void addShotsList(ArrayList<Shot> shots){
		for (Shot s: shots){
			if (s.getTargetPlayer().equals(myUsername)){ // I'm the target
				myBoardPanel.processAttack(s);
				myBoardPanel.repaint();
			}
			else if (s.getOriginPlayer().equals(myUsername)){ //I'm the originator
				//add other shots
				shotsFired++;
				addShotToOtherBoard(s);
			}
		}
	}
	
	public void addShotToOtherBoard(Shot shot){
		Coordinate shotDestination = shot.getShotDestination();
		int col = shotDestination.getColumn();
		int row = shotDestination.getRow();
		int idx = (10 * row) + col;
		EnemyPanel targetPanel = enemyPanels.get(shot.getTargetPlayer());
		targetPanel.addShotGraphic(idx, shot.wasAHit());
		if (shot.wasAHit()){
			shotsHit++;
		}
		shotsFiredStatLabel.setText("Shots Hit/Fired: " + shotsHit + "/" + shotsFired);
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
			timeRemaining = 60;
			dotSpacing = 27;
		}
		
		class UpdateTimer extends TimerTask{

			public void run() {
				timeRemaining -= 1;
				if (timeRemaining == 0){
					// send empty shots message to server
					// it'll send disable message back
					// TODO: bring stuff from endTurn
					fireButton.setEnabled(false);
					nt.send(new Message(new ArrayList<Shot>()));
				}
				TimerPanel.this.repaint();
			}
			
		}
		
		public void startTimer(){
			running = true;
			timeRemaining = 65;
			timer = new Timer();
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
		
		public int getTimeRemaining(){
			return timeRemaining;
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
		
		public void addShotGraphic(int idx, boolean hit){
			boardPanel.addShotGraphic(idx, hit);
		}
	}
	
	class BoardSpaceListener implements MouseListener{

		private BoardSpace bs;
		public void mouseClicked(MouseEvent ae) {
			bs = (BoardSpace) ae.getSource();
			Color bgColor = bs.getBackground();
			if (bgColor.equals(Color.black) || bgColor.equals(Color.green) || bgColor.equals(Color.red)){
				return;
			}
			if (SwingUtilities.isRightMouseButton(ae)){
				if (bs.getText().endsWith("*")){
					bs.setText(bs.getText().substring(0,1));
				}
				else if(bs.getText().equals("")){
					bs.setText(" *");
				}
				else{
					bs.setText(bs.getText() + "*");
				}
			}
			else{
				if (isMyTurn){
					GraphicalBoard parent = (GraphicalBoard) bs.getParent();
					String user = parent.getUsername();
					
					
					Coordinate c = new Coordinate(bs.getRow(),bs.getCol());
					ArrayList<Coordinate> sourcePanel = selectedCoordinates.get(user);
					if (bs.getBackground().equals(Color.blue)){
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
							bs.setBackground(Color.blue);
							selectedCoordinates.get(user).add(c);
						}
					}
				}
			}
			if (getNumSelectedCoordinates() == maxShotsAllowed){
				fireButton.setEnabled(true);
			}
			else{
				fireButton.setEnabled(false);
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
			    	shots.add(new Shot(user,c,myUsername));
			    }
			}
			
			// fire shots
			// TODO: bring stuff from endTurn
			fireButton.setEnabled(false);
			
			
			//add statistics
			turnsTaken++;
			turnsTakenStatLabel.setText("Turns Taken: " + turnsTaken);
			
			int timeRemaining = timerPanel.getTimeRemaining();
			int timeTaken = 65 - timeRemaining;
			totalTimeTaken += timeTaken;
			averageTimePerTurn = totalTimeTaken/turnsTaken;
			averageTimePerTurn = Math.floor(averageTimePerTurn * 100) / 100;
			averageTurnTimeStatLabel.setText("Sec per turn: " + averageTimePerTurn);
			
			shotsFired += shots.size();
			shotsFiredStatLabel.setText("Shots Hit/Fired: " + shotsHit + "/" + shotsFired);
			
			nt.send(new Message(shots));
		}
	}

	
	public int getShipsRemaining() {
		return myBoardPanel.numShipsRemaining();
	}
	
	public void updateNumShipsRemaining(String user, int num) {
		enemyPanels.get(user).updateNumShipsRemaining(num);
	}
}
