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
	
	private ButtonActionHandler bActHand;
	
	private ListActionHandler lActHand;
	
	public SearchPalletPane(Database db) {
		super(db);
		bActHand = new ButtonActionHandler();
		// TODO Auto-generated constructor stub
	}
	
	public void entryActions() {
		clearMessage();
	}
	
	public JComponent createLeftPanel() {
		inputField = new JTextField();
		search = new JButton("search");
		search.addActionListener(new ButtonActionHandler());
		batchNrListModel = new DefaultListModel();
		
		batchNrListModel.addElement("1");
		batchNrListModel.addElement("2");
		batchNrListModel.addElement("3");
		batchNrListModel.addElement("4");
		
		batches = new JList(batchNrListModel);
		batches.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JPanel p = new JPanel();
		p.setLayout(new GridLayout(2, 2));
		p.add(inputField);
		p.add(search);
		p.add(batches);
		return p;
	}
	
	public JComponent createMiddlePanel() {
		JPanel p = new JPanel();
		fields = new JTextField[4];
		p.setLayout(new GridLayout(4, 1));
		for(int i = 0; i < 4; i++){
			fields[i] = new JTextField("AAAAAAAAAAAAAAAAAAAAAAA");
			p.add(fields[i]);
		}
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

private void fetchInformation(int input) {
	batchNrListModel.removeAllElements();
    ArrayList<String> batchNbrs = db.showPallet(input);
    for(int i = 0; i < batchNbrs.size(); i++){
    batchNrListModel.addElement(batchNbrs.get(i));
    }
}
class ButtonActionHandler implements ActionListener {
	
	public void actionPerformed(ActionEvent e) {
		try{ fetchBatch(inputField.getText());
		} catch (NullPointerException e2){
			messageLabel.setText("No data entered");
		}
		
		
	}
}

class ListActionHandler implements ActionListener {
	
	public void actionPerformed(ActionEvent e) {
		try{ fetchBatch(inputField.getText());
		} catch (NullPointerException e2){
			messageLabel.setText("No data entered");
		}
}
}
}