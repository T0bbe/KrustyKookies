package dbtLab3;

import java.awt.GridLayout;

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
	
	public SearchPalletPane(Database db) {
		super(db);
		// TODO Auto-generated constructor stub
	}
	
	public void entryActions() {
		clearMessage();
	}
	
	public JComponent createLeftPanel() {
		inputField = new JTextField();
		search = new JButton("search");
		
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
}