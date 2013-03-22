package dbtLab3;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import dbtLab3.ProductionPane.ActionHandler;
import dbtLab3.ProductionPane.CookieNameSelectionListener;

public class SearchPalletPane extends BasicPane{
private static final long serialVersionUID = 1;
	
	private DefaultListModel batchNrListModel;
	
	private JTextField[] fields;
	
	private JTextField inputField;
	
	private JList batches;
	
	private JPanel numberOfBags;
	
	private JButton search;
	
	private ActionHandler actHand;
	
	public SearchPalletPane(Database db) {
		super(db);
		actHand = new ActionHandler();
		// TODO Auto-generated constructor stub
	}
	
	public void entryActions() {
		clearMessage();
	}
	
	public JComponent createTopPanel() {
		inputField = new JTextField();
		search = new JButton("search");
		search.addActionListener(new ActionHandler());
		
		
	/*	cookieNameListModel = new DefaultListModel();
		cookieNames = new JList(cookieNameListModel);
		
		cookieNames.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		cookieNames.setPrototypeCellValue("123456789012");
		cookieNames.addListSelectionListener(new CookieNameSelectionListener());

		JScrollPane p1 = new JScrollPane(cookieNames);*/
		
		JPanel p = new JPanel();
		p.setLayout(new GridLayout(1, 2));
		p.add(inputField);
		p.add(search);
		return p;
	}
	
	public JComponent createMiddlePanel() {
		batchNrListModel = new DefaultListModel();
		batches = new JList(batchNrListModel);
		batches.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	

		
	/*	cookieNameListModel = new DefaultListModel();
		cookieNames = new JList(cookieNameListModel);
		
		cookieNames.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		cookieNames.setPrototypeCellValue("123456789012");
		cookieNames.addListSelectionListener(new CookieNameSelectionListener());

		JScrollPane p1 = new JScrollPane(cookieNames);*/
		
		JPanel p = new JPanel();
		p.setLayout(new GridLayout(1, 2));
		p.add(inputField);
		p.add(search);
		return p;
	}

private void fetchPalletNr(int input) {
	batchNrListModel.removeAllElements();
    batchNrListModel.addElement(db.getPallet(input));
    db.showPallet(input);
    }


private void fetchBatch(String input) {
	batchNrListModel.removeAllElements();
    ArrayList<String> batchNbrs = db.getBatch(input);
    for(int i = 0; i < batchNbrs.size(); i++){
    batchNrListModel.addElement(batchNbrs.get(i));
    }
}
class ActionHandler implements ActionListener {
	
	public void actionPerformed(ActionEvent e) {
		if (batches.isSelectionEmpty()) {
			return;
		}
		
		Integer batchNbr = (Integer) batches.getSelectedValue();
		db.showPallet(batchNbr);
		
	}
}
}