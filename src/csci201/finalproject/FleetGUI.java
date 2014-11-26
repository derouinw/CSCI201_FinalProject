package csci201.finalproject;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class FleetGUI extends JPanel {
	JPanel SelectUserInfoPanel;
	JLabel SelectColorLabel;
	JComboBox<String> SelectColorCombo;
	JLabel SelectFleetLabel;
	JRadioButton ClassicFleetButton;
	JRadioButton CreateFleetButton;
	JPanel FleetInfo; //BoxLayout
	JComboBox Type0Combo;
	JComboBox Type1Combo;
	JComboBox Type2Combo;
	JComboBox Type3Combo;
	JComboBox Type4Combo;
	String[] numOptions1 = {"0","1","2","3","4","5","6","7"};
	String[] numOptions2 = {"0","1","2","3","4","5"};
	String[] numOptions3 = {"0","1","2","3"};
	JButton ContinueButton; // only active if correct fleet is chosen
	
	public FleetGUI(){
		SelectUserInfoPanel = new JPanel(new BorderLayout());
		JPanel SelectColorPanel = new JPanel();
		JPanel SelectFleetPanel = new JPanel();
		SelectFleetPanel.setLayout(new BoxLayout(SelectFleetPanel,BoxLayout.X_AXIS));
		JPanel FleetSelect = new JPanel();
		FleetSelect.setLayout(new BoxLayout(FleetSelect,BoxLayout.Y_AXIS));
		
		// Color Selection
		SelectColorLabel = new JLabel("Select your color ");
		String[] ColorOptions = {"red","orange","yellow","cyan","green","pink"};
		SelectColorCombo = new JComboBox<String>(ColorOptions);
		SelectColorPanel.add(SelectColorLabel);
		SelectColorPanel.add(SelectColorCombo);
		SelectUserInfoPanel.add(SelectColorPanel, BorderLayout.NORTH);
		
		// Fleet Selection
		SelectFleetLabel = new JLabel("Select your fleet type ");
		FleetSelect.add(SelectFleetLabel, BorderLayout.NORTH);
		
		ButtonGroup FleetButtonGroup = new ButtonGroup();
		ClassicFleetButton = new JRadioButton("Classic");
		ClassicFleetButton.addActionListener(new RadioListener());
		FleetButtonGroup.add(ClassicFleetButton);
		CreateFleetButton = new JRadioButton("Create my own");
		CreateFleetButton.addActionListener(new RadioListener());
		CreateFleetButton.setSelected(true);
		FleetButtonGroup.add(CreateFleetButton);
		
		// adding radio buttons
		JPanel RadioButtonPanel = new JPanel();
		RadioButtonPanel.add(ClassicFleetButton);
		RadioButtonPanel.add(CreateFleetButton);
		FleetSelect.add(RadioButtonPanel);
		
		// Ship Descriptions
		JPanel descriptionPanel = new JPanel();
		descriptionPanel.setLayout(new BoxLayout(descriptionPanel,BoxLayout.Y_AXIS));
		JLabel instructLabel = new JLabel("Yer fleet should be take'n up 17 spaces.");
		JLabel spaceLabel = new JLabel(" ");
		descriptionPanel.add(instructLabel);
		descriptionPanel.add(spaceLabel);
		JLabel dingyLabel = new JLabel("The Dingy is a 2 space ship.");
		JLabel sloopLabel = new JLabel("The Sloop is a 3 space ship.");
		JLabel frigateLabel = new JLabel("The Frigate is a 3 space ship.");
		JLabel brigantineLabel = new JLabel("The Brigantine is a 4 space ship.");
		JLabel galleonLabel = new JLabel("The Galleon is a 5 space ship.");
		descriptionPanel.add(dingyLabel);
		descriptionPanel.add(sloopLabel);
		descriptionPanel.add(frigateLabel);
		descriptionPanel.add(brigantineLabel);
		descriptionPanel.add(galleonLabel);
		
		JLabel spaceLabel1 = new JLabel(" ");
		descriptionPanel.add(spaceLabel1);
		
		FleetSelect.add(descriptionPanel);
		
		ContinueButton = new JButton("Continue"); // only active if correct fleet is chosen
		FleetSelect.add(ContinueButton);
		ContinueButton.setEnabled(false);
		
		SelectFleetPanel.add(FleetSelect);
		
		// Panel on which users select the number of each type of ship 
		FleetInfo = new JPanel();
		FleetInfo.setLayout(new BoxLayout(FleetInfo,BoxLayout.Y_AXIS)); //BoxLayout
		JLabel FleetInfoLabel = new JLabel("Select th' count o'each type o' ship");
		JLabel FleetInfoLabel2 = new JLabel(" ye want in yer fearsome fleet.");
		FleetInfo.add(FleetInfoLabel);
		FleetInfo.add(FleetInfoLabel2);
		
		JPanel Type0Panel = new JPanel();
		JLabel ShipType0Label = new JLabel("Dingy:");
		Type0Combo = new JComboBox<String>(numOptions1);
		Type0Combo.addActionListener(new ComboListener());
		Type0Panel.add(ShipType0Label);
		Type0Panel.add(Type0Combo);
		
		JPanel Type1Panel = new JPanel();
		JLabel ShipType1Label = new JLabel("Sloop:");
		Type1Combo = new JComboBox<String>(numOptions2);
		Type1Combo.addActionListener(new ComboListener());
		Type1Panel.add(ShipType1Label);
		Type1Panel.add(Type1Combo);
		
		JPanel Type2Panel = new JPanel();
		JLabel ShipType2Label = new JLabel("Frigate:");
		Type2Combo = new JComboBox<String>(numOptions2);
		Type2Combo.addActionListener(new ComboListener());
		Type2Panel.add(ShipType2Label);
		Type2Panel.add(Type2Combo);
		
		JPanel Type3Panel = new JPanel();
		JLabel ShipType3Label = new JLabel("Brigantine:");
		Type3Combo = new JComboBox<String>(numOptions3);
		Type3Combo.addActionListener(new ComboListener());
		Type3Panel.add(ShipType3Label);
		Type3Panel.add(Type3Combo);
		
		JPanel Type4Panel = new JPanel();
		JLabel ShipType4Label = new JLabel("Galleon:");
		Type4Combo = new JComboBox<String>(numOptions3);
		Type4Combo.addActionListener(new ComboListener());
		Type4Panel.add(ShipType4Label);
		Type4Panel.add(Type4Combo);
		
		FleetInfo.add(Type0Panel);
		FleetInfo.add(Type1Panel);
		FleetInfo.add(Type2Panel);
		FleetInfo.add(Type3Panel);
		FleetInfo.add(Type4Panel);
		
		SelectFleetPanel.add(FleetInfo);
		
		SelectUserInfoPanel.add(SelectFleetPanel, BorderLayout.CENTER);
		add(SelectUserInfoPanel, BorderLayout.CENTER);
		
	}
	class ComboListener implements ActionListener{
		
		public void actionPerformed(ActionEvent e) {
			//check that the combo boxes add up to 17
			int d = Integer.valueOf(numOptions1[Type0Combo.getSelectedIndex()]);
			d = d*2;
			int s = Integer.valueOf(numOptions2[Type1Combo.getSelectedIndex()]);
			s = s*3;
			int f = Integer.valueOf(numOptions2[Type2Combo.getSelectedIndex()]);
			f = f*3;
			int b = Integer.valueOf(numOptions3[Type3Combo.getSelectedIndex()]);
			b = b*4;
			int g = Integer.valueOf(numOptions3[Type4Combo.getSelectedIndex()]);
			g = g*5;
			
			if(d+s+f+b+g == 17){
				ContinueButton.setEnabled(true);
			}
			else{
				ContinueButton.setEnabled(false);
			}
			revalidate();
		}
		
	}
	class RadioListener implements ActionListener{
		
		public void actionPerformed(ActionEvent e) {
			if (e.getSource()==ClassicFleetButton){
				Type0Combo.setSelectedIndex(1);
				Type0Combo.setEnabled(false);
				Type1Combo.setSelectedIndex(1);
				Type1Combo.setEnabled(false);
				Type2Combo.setSelectedIndex(1);
				Type2Combo.setEnabled(false);
				Type3Combo.setSelectedIndex(1);
				Type3Combo.setEnabled(false);
				Type4Combo.setSelectedIndex(1);
				Type4Combo.setEnabled(false);
			}
			else{
				Type0Combo.setEnabled(true);
				Type1Combo.setEnabled(true);
				Type2Combo.setEnabled(true);
				Type3Combo.setEnabled(true);
				Type4Combo.setEnabled(true);
			}
		}
		
	}
	
}
