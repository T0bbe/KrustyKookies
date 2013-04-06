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

public class ProductionPane extends BasicPane implements Pane {
	private static final long serialVersionUID = 1;

	private ArrayList<Integer> LeftOversLeft = null;

	private DefaultListModel cookieNameListModel;

	private JTextField[] leftovers;

	private JTextField fields;

	private JList cookieNames;

	private JPanel numberOfBags;

	public ProductionPane(Database db) {
		super(db);
		Border border = new LineBorder(Color.black);
		TitledBorder t = new TitledBorder(border, "Message Field");
		messageLabel.setBorder(t);
		add(messageLabel, BorderLayout.SOUTH);
		// TODO Auto-generated constructor stub
	}

	// Creates left panel with a cookieName list

	public JComponent createLeftPanel() {
		cookieNameListModel = new DefaultListModel();
		cookieNames = new JList(cookieNameListModel);

		cookieNames.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		cookieNames.setPrototypeCellValue("123456789012");
		cookieNames.addListSelectionListener(new CookieNameSelectionListener());

		JScrollPane p1 = new JScrollPane(cookieNames);

		JPanel p = new JPanel();
		Border border = new LineBorder(Color.black);
		TitledBorder t = new TitledBorder(border, "Cookies");
		p.setBorder(t);
		p.setLayout(new GridLayout(1, 2));
		p.add(p1);
		return p;
	}

	public JComponent createTopPanel() {
		String[] text = new String[1];
		text[0] = "How many cookies?";
		fields = new JTextField(20);
		fields.setEditable(true);

		JPanel cookieData = new CookieInput(text, fields);

		JPanel numberOfBags = new JPanel();
		numberOfBags.setLayout(new BoxLayout(numberOfBags, BoxLayout.Y_AXIS));
		numberOfBags.add(cookieData);

		numberOfBags.setVisible(true);
		Border border = new LineBorder(Color.black);
		TitledBorder t = new TitledBorder(border, "Production Controller");
		numberOfBags.setBorder(t);
		return numberOfBags;
	}

	public JComponent createBottomPanel() {
		JButton[] buttons = new JButton[1];
		buttons[0] = new JButton("Initiate Production");
		return new BuildPallet(buttons, new ActionHandler());
	}

	public JComponent createMiddlePanel() {
		String[] text = new String[4];
		text[0] = "nbr of cookies left";
		text[1] = "nbr of bags left";
		text[2] = "nbr of boxes left";
		text[3] = "nbr of pallets built";

		leftovers = new JTextField[4];
		for (int i = 0; i < leftovers.length; i++) {
			leftovers[i] = new JTextField(20);
			leftovers[i].setEditable(false);
		}

		JPanel leftoverData = new LeftOverdata(text, leftovers);
		Border border = new LineBorder(Color.black);
		TitledBorder t = new TitledBorder(border, "Leftovers");
		leftoverData.setBorder(t);
		return leftoverData;

	}

	// fetches all cookieNames
	private void fillCookieList() {
		cookieNameListModel.removeAllElements();
		ArrayList<String> allCookies = db.allCookies();
		for (int i = 0; i < allCookies.size(); i++) {
			cookieNameListModel.addElement(allCookies.get(i));
		}
	}

	public void entryActions() {
		clearMessage();
		fillCookieList();
	}

	private void fillLeftOvers(ArrayList<Integer> lefts) {
		String pallets = lefts.get(3).toString();
		String boxes = lefts.get(2).toString();
		String bags = lefts.get(1).toString();
		String cookies = lefts.get(0).toString();
		leftovers[3].setText(pallets);
		leftovers[2].setText(boxes);
		leftovers[1].setText(bags);
		leftovers[0].setText(cookies);
		LeftOversLeft = lefts;
	}

	class CookieNameSelectionListener implements ListSelectionListener {
		/**
		 * Called when the user selects a cookieName in the list. makes the
		 * numberOfBags panel visible
		 */
		public void valueChanged(ListSelectionEvent e) {
			if (cookieNames.isSelectionEmpty()) {
				return;
			}
			String cookieName = (String) cookieNames.getSelectedValue();
		}
	}
	
	class ActionHandler implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			if (cookieNames.isSelectionEmpty()) {
				displayMessage("Please choose a cookie before proceeding");
				return;
			}

			String cookieName = (String) cookieNames.getSelectedValue();
			String cookies = (String) fields.getText();
			ArrayList<Integer> nbrs = db.buildPallet(cookieName, cookies);
			if (nbrs != null) {
				if(nbrs.get(3) == 0){
					displayMessage("Only complete pallets are created, 5400 cookies at least");
					return;
				}else{
					fillLeftOvers(nbrs);
					displayMessage("Your pallets have been built. Please move them to storage.");
				}
			} else {
				displayMessage("Please define a proper cookie amount.");
			}
		}
	}

}
