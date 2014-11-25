package csci201.finalproject;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;
import java.util.*;
import java.util.Timer;

public class GameGUI extends JPanel{
	private JPanel topPanel, bottomPanel, statPanel, firePanel, statFirePanel, fireButtonPanel, statWrapper;
	private TimerPanel timerPanel;
	private Board myBoardPanel;
	private JButton fireButton;
	private JLabel shotsFiredStatLabel, turnsTakenStatLabel, shipsSunkStatLabel, maxShotsAllowedStatLabel;
	private BoardSpaceListener bsl;
	private ArrayList<ArrayList <Coordinate> > selectedCoordinates;
	private ArrayList<EnemyPanel> enemyPanels;
	private int maxShotsAllowed;
	private boolean isMyTurn;
	
	public GameGUI(){
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
		topPanel = new JPanel();
		bottomPanel = new JPanel();
		myBoardPanel = new Board();
		statPanel = new JPanel();
		firePanel = new JPanel();
		statFirePanel = new JPanel();
		timerPanel = new TimerPanel(); timerPanel.repaint();
		fireButtonPanel = new JPanel();
		statWrapper = new JPanel();
		
		bsl = new BoardSpaceListener();
		
		isMyTurn = false;
		
		maxShotsAllowed = 5;
		
		maxShotsAllowedStatLabel = new JLabel("Shots per turn: " + maxShotsAllowed);
		shotsFiredStatLabel = new JLabel("Shots Hit/Fired: 0/0");
		turnsTakenStatLabel = new JLabel("Turns Taken: 0");
		shipsSunkStatLabel = new JLabel("Ships Sunk: 0");
		fireButton = new JButton("FIRE!");
		
		enemyPanels = new ArrayList<EnemyPanel>(3);
		for (int i=0;i<3;i++){
			enemyPanels.add(new EnemyPanel());
		}
		selectedCoordinates = new ArrayList< ArrayList<Coordinate> >(3);
		for (int i=0;i<3;i++){
			selectedCoordinates.add(new ArrayList<Coordinate>());
		}
	}
	
	private int getNumSelectedCoordinates(){
		int retval = 0;
		for (ArrayList<Coordinate> al : selectedCoordinates){
			retval += al.size();
		}
		return retval;
	}
	
	private void createTopPanel(){
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
		
		for (EnemyPanel ep: enemyPanels){
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
	
	private void startTurn(){
		isMyTurn = true;
		timerPanel.startTimer();
		fireButton.setEnabled(true);
	}
	
	private class TimerPanel extends JPanel{
		private JLabel label;
		private int timeRemaining;
		private Timer timer;
		
		public TimerPanel(){
			super();
			label = new JLabel("Time Remaining");
			//this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			this.add(label);
			timer = new Timer();
			timeRemaining = 65;
		}
		
		class UpdateTimer extends TimerTask{

			public void run() {
				timeRemaining -= 1;
				TimerPanel.this.repaint();
			}
			
		}
		
		public void startTimer(){
			timer.scheduleAtFixedRate(new UpdateTimer(), 0, 1000);
		}
		
		public void stopTimer(){
			timeRemaining = 65;
			timer.cancel();
		}
		
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			int numCircles = timeRemaining / 6;
			if (timeRemaining < 15){
				g.setColor(Color.red);
			}
			else{
				g.setColor(Color.green);
			}
			for (int i=0;i<10;i++){
				if (i >= numCircles){
					g.drawOval(20 + (i*25),30,20,20);
				}
				else{
					g.fillOval(20 + (i*25),30,20,20);
				}
			}
		}
	}
	
	private class EnemyPanel extends JPanel{
		private JLabel userNameLabel, infoLabel;
		private JPanel userNamePanel, infoPanel;
		private GraphicalBoard boardPanel;
		public EnemyPanel(){
			this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
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
			userNameLabel = new JLabel("[Username]");
			userNamePanel.add(userNameLabel);
		}
		
		private void setUpBoardPanel(){
			boardPanel = new GraphicalBoard(bsl);
		}
		
		private void setUpInfoPanel(){
			infoPanel = new JPanel();
			infoLabel = new JLabel("This player has x ships left");
			infoPanel.add(infoLabel);
			infoPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.black));
		}
		
		public GraphicalBoard getBoard(){
			return boardPanel;
		}
		
	}
	
	class BoardSpaceListener implements MouseListener{

		private BoardSpace bs;
		public void mouseClicked(MouseEvent ae) {
			bs = (BoardSpace) ae.getSource();
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
					int panelIdx = 99;
					for (int i=0;i<3;i++){
						EnemyPanel ep = enemyPanels.get(i);
						if (parent.equals(ep.getBoard())){
							panelIdx = i;
						}
					}
					Coordinate c = new Coordinate(bs.getRow(),bs.getCol());
					ArrayList<Coordinate> sourcePanel = selectedCoordinates.get(panelIdx);
					if (bs.getBackground().equals(Color.red)){
						bs.setBackground(Color.LIGHT_GRAY);
						for (int i=0;i<sourcePanel.size();i++){
							Coordinate newCoordinate = sourcePanel.get(i);
							if (c.equals(newCoordinate)){
								selectedCoordinates.get(panelIdx).remove(newCoordinate);
							}
						}
					}
					else{ //activate the cell
						if (getNumSelectedCoordinates() < maxShotsAllowed){
							bs.setBackground(Color.red);
							selectedCoordinates.get(panelIdx).add(c);
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
}
