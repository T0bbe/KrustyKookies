package krustyKookies;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class FreezerPane extends BasicPane implements Pane {
	private static final long serialVersionUID = 1;

	private ArrayList<Integer> LeftOversLeft = null;

	private DefaultListModel<Integer> palletNbrsListModel;

	private JTextField[] fields;

	private JList palletNbrs;

	private JPanel numberOfBags;

	public FreezerPane(Database db) {
		super(db);
		Border border = new LineBorder(Color.black);
		TitledBorder t = new TitledBorder(border, "Message Field");
		messageLabel.setBorder(t);
		add(messageLabel, BorderLayout.SOUTH);
		// TODO Auto-generated constructor stub
	}

	public JComponent createLeftPanel() {
		palletNbrsListModel = new DefaultListModel();
		palletNbrs = new JList(palletNbrsListModel);

		palletNbrs.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		palletNbrs.setPrototypeCellValue("12345678901234567");
		palletNbrs.addListSelectionListener(new PalletNbrListener());

		JScrollPane p1 = new JScrollPane(palletNbrs);

		JPanel p = new JPanel();
		p.setLayout(new GridLayout(1, 2));
		LineBorder border = new LineBorder(Color.black);
		TitledBorder t = new TitledBorder(border, "Pallets in Production");
		p.setBorder(t);
		p.add(p1);
		return p;
	}
	
	public JComponent createTopPanel() {
		JPanel p = new JPanel();
		Border border = new LineBorder(Color.black);
		TitledBorder t = new TitledBorder(border, "Information");
		p.setBorder(t);
		fields = new JTextField[4];
		fields[0] = new KrustyTextField("Pallet Number");
		fields[2] = new KrustyTextField("Cookie Name");
		
		p.setLayout(new GridLayout(4, 1));
		for(int i = 1; i < 4; i = i+2){
			fields[i] = new JTextField("                                         ");
			fields[i].setEditable(false);
		}
		for(int i = 0; i < 4; i++){
			p.add(fields[i]);
		}
		return p;
	}
	
	private void fetchInformation(String input) {
		ArrayList<String> batchNbrs;
		try{
	    batchNbrs = db.showPallet(input);
	    fields[1].setText(input);
	    fields[3].setText(batchNbrs.get(1));
		} catch (Exception e){
	    	return;
		}
	}
	
	private void clearInformation(){
		fields[1].setText("");
	    fields[3].setText("");
	}
	

	public JComponent createBottomPanel() {
		JButton[] buttons = new JButton[1];
		buttons[0] = new JButton(
				"Move to storage and assign timestamp to Pallet");
		return new PutPalletInDeepFreeze(buttons, new ActionFreeze());
	}

	// fetches all cookieNames
	private void fillPalletNrList() {
		palletNbrsListModel.removeAllElements();
		ArrayList<String> allPallets = db.allProductionPallets();
		for (int i = 0; i < allPallets.size(); i++) {
			palletNbrsListModel.addElement(Integer.valueOf(allPallets.get(i)));
		}
	}

	public void entryActions() {
		clearMessage();
		fillPalletNrList();
	}


	class PalletNbrListener implements ListSelectionListener {
		/**
		 * Called when the user selects a cookieName in the list. makes the
		 * numberOfBags panel visible
		 */
		public void valueChanged(ListSelectionEvent e) {
			if (palletNbrs.isSelectionEmpty()) {
				return;
			}
			fetchInformation(palletNbrs.getSelectedValue().toString());
		}
	}


	class ActionFreeze implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			
			if (palletNbrs.isSelectionEmpty()) {
				displayMessage("Please choose a pallet before proceeding");
				return;
			}
			String cookieName = palletNbrs.getSelectedValue().toString();
			db.updateRaws(cookieName);
			db.readPalletCode((Integer) palletNbrs.getSelectedValue());
			displayMessage("The pallets has been moved to the deep freeze storage.");
			fillPalletNrList();
			clearInformation();
		}
	}

}
