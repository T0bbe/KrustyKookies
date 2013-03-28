package dbtLab3;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpringLayout;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.SoftBevelBorder;

import dbtLab3.ProductionPane.ActionHandler;
import dbtLab3.ProductionPane.CookieNameSelectionListener;

public class SearchPalletPane extends JPanel implements Pane{
	private static final long serialVersionUID = 1;
    /**
     * The database object.
     */
    protected Database db;
        
    /**
     * A label which is intended to contain a message text.
     */
    protected JLabel messageLabel;
        
    /** 
     * Create a BasicPane object.
     *
     * @param db The database object.
     */
	
	private DefaultListModel batchNrListModel;
	
	private JTextField[] fields;
	
	private JTextField inputField;
	
	private JList batches;
	
	private JPanel numberOfBags;
	
	private JButton search;
	
	private ButtonActionHandler bActHand;
	
	private ListActionHandler lActHand;
	
	public SearchPalletPane(Database db) {
	        this.db = db;
	        messageLabel = new JLabel("      ");
	                
	        setLayout(new BorderLayout());
	        
	        JComponent leftPanel = createLeftPanel();
	        add(leftPanel, BorderLayout.WEST);
	                
	        JPanel rightPanel = new JPanel();
	        rightPanel.setLayout(new BorderLayout());
	                
	        JComponent topPanel = createTopPanel();
	        JComponent middlePanel = createMiddlePanel();
	        JComponent bottomPanel = createBottomPanel();
	        bottomPanel.setBorder
	            (new CompoundBorder(new SoftBevelBorder(BevelBorder.RAISED),
	                                bottomPanel.getBorder()));
	        add(topPanel, BorderLayout.NORTH);
	        rightPanel.add(middlePanel, BorderLayout.CENTER);
	        rightPanel.add(bottomPanel, BorderLayout.SOUTH);
	        add(rightPanel, BorderLayout.CENTER);
		add(createRightPanel(), BorderLayout.EAST);
		bActHand = new ButtonActionHandler();
	}
	
	public JComponent createLeftPanel() {
		inputField = new JTextField(13);
		search = new JButton("Perform Search");
		JTextField inputField2 = new JTextField();
		JButton search2 = new JButton("Perform Search");
		search.addActionListener(new ButtonActionHandler());
		batchNrListModel = new DefaultListModel();
		
		batchNrListModel.addElement("1");
		batchNrListModel.addElement("2");
		batchNrListModel.addElement("3");
		batchNrListModel.addElement("4");
		
		batches = new JList(batchNrListModel);
		batches.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JPanel p = new JPanel();
		p.setLayout(new GridLayout(10, 1));
		p.add(new JPanel());
		JTextField temp = new JTextField("Search by Pallet Number");
		temp.setBorder(null);
		temp.setEditable(false);
		p.add(temp);
		p.add(inputField);
		p.add(search);
		p.add(new JPanel());
		JTextField temp2 = new JTextField("Search by Production Date");
		temp2.setBorder(null);
		temp2.setEditable(false);
		p.add(temp2);
		p.add(inputField2);
		p.add(search2);
		p.setBorder(new LineBorder(Color.black));
		return p;
	}
	
	public JComponent createTopPanel() {
		JPanel p = new JPanel();
		p.setLayout(new GridLayout(1,3));
		JTextField temp = new JTextField("Search");
		temp.setBorder(null);
		temp.setEditable(false);
		Font newTextFieldFont = new Font(temp.getFont().getName(),Font.BOLD,temp.getFont().getSize());
		temp.setFont(newTextFieldFont);
		p.add(temp);
		JTextField temp2 = new JTextField("Batch Numbers");
		temp2.setBorder(null);
		temp2.setEditable(false);
		temp2.setFont(newTextFieldFont);
		p.add(temp2);
		JTextField temp3 = new JTextField("Information");
		temp3.setBorder(null);
		temp3.setEditable(false);
		temp3.setFont(newTextFieldFont);
		p.add(temp3);
		return p;
	}
	
	public JComponent createMiddlePanel() {
		JPanel p = new JPanel();
		p.setBorder(new LineBorder(Color.black));
		p.add(batches);
		return p;
	}
	
	public JComponent createBottomPanel() {
		JButton block = new JButton("Block Batch");
		return block;
	}
	
	public JComponent createRightPanel() {
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

@Override
public void entryActions() {
	// TODO Auto-generated method stub
	
}
}