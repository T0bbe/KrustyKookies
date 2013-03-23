package dbtLab3;

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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


public class ProductionPane extends BasicPane {
	private static final long serialVersionUID = 1;
	
	private DefaultListModel cookieNameListModel;
	
	private JTextField[] fields;
	
	private JList cookieNames;
	
	private JPanel numberOfBags;
	
	public ProductionPane(Database db) {
		super(db);
		// TODO Auto-generated constructor stub
	}
	
	//Creates left panel with a cookieName list 
	
	public JComponent createLeftPanel() {
		cookieNameListModel = new DefaultListModel();
		cookieNames = new JList(cookieNameListModel);
		
		cookieNames.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		cookieNames.setPrototypeCellValue("123456789012");
		cookieNames.addListSelectionListener(new CookieNameSelectionListener());

		JScrollPane p1 = new JScrollPane(cookieNames);
		
		JPanel p = new JPanel();
		p.setLayout(new GridLayout(1, 2));
		p.add(p1);
		return p;
	}
	
	public JComponent createTopPanel(){
		String[] text = new String[1];
		text[0] = "How many cookies?";
		
		fields = new JTextField[1];
		for (int i = 0; i < fields.length; i++) {
			fields[i] = new JTextField(20);
			fields[i].setEditable(true);
		}
		
		JPanel cookieData = new CookieInput(text, fields);
		
		JPanel numberOfBags = new JPanel();
		numberOfBags.setLayout(new BoxLayout(numberOfBags, BoxLayout.Y_AXIS));
		numberOfBags.add(cookieData);
		
		numberOfBags.setVisible(true);
		return numberOfBags;
	}
	
	public JComponent createBottomPanel() {
		JButton[] buttons = new JButton[1];
		buttons[0] = new JButton("Build Pallets");
		return new BuildPallet(buttons,
				new ActionHandler());
	}
	
	//fetches all cookieNames
	private void fillCookieList() {
		cookieNameListModel.removeAllElements();
        ArrayList<String> allCookies = db.allCookies();
        for(int i = 0; i < allCookies.size(); i++){
        	cookieNameListModel.addElement(allCookies.get(i));
        }
	}
	
	public void entryActions() {
		clearMessage();
		fillCookieList();
	}
	
	class CookieNameSelectionListener implements ListSelectionListener {
		/**
		 * Called when the user selects a cookieName in the list. makes
		 * the numberOfBags panel visible
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
				return;
			}
			
			String cookieName = (String) cookieNames.getSelectedValue();
			String cookies = (String) fields[0].getText();
			db.buildPallet(cookieName, cookies);
			
		}
	}
	
}
