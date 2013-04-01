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
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.TitledBorder;

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
	
	private JButton block;
	
	private JButton select;
	
	private ButtonActionHandler bActHand;
	
	private ListActionHandler lActHand;
	
	public SearchPalletPane(Database db) {
	        this.db = db;
	        messageLabel = new JLabel("      ");
	                
	        setLayout(new BorderLayout());
	        
	        JComponent leftPanel = createLeftPanel();
	        add(leftPanel, BorderLayout.WEST);
	                
	        JPanel rightPanel = new JPanel();
	        JPanel centerPanel = new JPanel();
	        centerPanel.setLayout(new BorderLayout());
	        rightPanel.setLayout(new BorderLayout());
	        
	        JComponent middlePanel = createMiddlePanel();
	        JComponent rightBottomPanel = createRightBottomPanel();
	        JComponent leftBottomPanel = createLeftBottomPanel();
	        JComponent resultPanel = createRightPanel();
	        rightBottomPanel.setBorder
	            (new CompoundBorder(new SoftBevelBorder(BevelBorder.RAISED),
	                                rightBottomPanel.getBorder()));
	        leftBottomPanel.setBorder
            (new CompoundBorder(new SoftBevelBorder(BevelBorder.RAISED),
                                leftBottomPanel.getBorder()));
	        centerPanel.add(middlePanel, BorderLayout.NORTH);
	        centerPanel.add(leftBottomPanel, BorderLayout.SOUTH);
	        add(centerPanel, BorderLayout.CENTER);
	        rightPanel.add(resultPanel, BorderLayout.CENTER);
	        rightPanel.add(rightBottomPanel, BorderLayout.SOUTH);
	        add(rightPanel, BorderLayout.EAST);
		bActHand = new ButtonActionHandler();
	}
	
	public JComponent createLeftPanel() {
		inputField = new JTextField(13);
		Font newTextFieldFont = new Font(inputField.getFont().getName(),Font.BOLD,inputField.getFont().getSize());
		Font newTextFieldFont2 = new Font(inputField.getFont().getName(),Font.PLAIN,inputField.getFont().getSize());
		search = new JButton("Perform Search");
		JTextField inputField2 = new JTextField();
		JMenuBar inputField3 = new JMenuBar();
		JMenu menu1 = new JMenu("Select Cookie Name");
		menu1.setFont(newTextFieldFont2);
		menu1.add(new JCheckBoxMenuItem("All"));
		menu1.add(new JCheckBoxMenuItem("Kaka1"));
		menu1.add(new JCheckBoxMenuItem("Kaka2"));
		menu1.add(new JCheckBoxMenuItem("Kaka3"));
		menu1.add(new JCheckBoxMenuItem("Kaka4"));
		menu1.add(new JCheckBoxMenuItem("Kaka5"));
		menu1.add(new JCheckBoxMenuItem("Kaka6"));
		menu1.add(new JCheckBoxMenuItem("Kaka7"));
		inputField3.add(menu1);
		JTextField inputField4 = new JTextField();
		JButton search2 = new JButton("Perform Search");
		search.addActionListener(new ButtonActionHandler());
		JPanel p = new JPanel();
		p.setLayout(new GridLayout(12, 1));
		Border border = new LineBorder(Color.black);
		TitledBorder t = new TitledBorder(border, "Search");
		t.setTitleColor(Color.black);
		KrustyTextField temp = new KrustyTextField("Search by BatchNbr");
		temp.setFont(newTextFieldFont);
		p.setBorder(t);
		p.add(temp);
		p.add(inputField);
		p.add(search);
		p.add(new JPanel());
		KrustyTextField temp2 = new KrustyTextField("Search by Date or Name");
		temp2.setFont(newTextFieldFont);
		KrustyTextField temp4 = new KrustyTextField("Start Date (Optional)");
		KrustyTextField temp5 = new KrustyTextField("End Date (Optional)");
		p.add(temp2);
		p.add(inputField3);
		p.add(temp4);
		p.add(inputField2);
		p.add(temp5);
		p.add(inputField4);
		p.add(search2);
		p.add(new JPanel());
		return p;
	}
	
	
	public JComponent createMiddlePanel() {
		batchNrListModel = new DefaultListModel();
		batchNrListModel.addElement("1");
		batchNrListModel.addElement("2");
		batchNrListModel.addElement("3");
		batchNrListModel.addElement("4");
		batches = new JList(batchNrListModel);
		batches.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JPanel p = new JPanel();
		Border border = new LineBorder(Color.black);
		TitledBorder t = new TitledBorder(border, "Batch Numbers");
		p.setBorder(t)
;		p.add(batches);
		return p;
	}
	
	public JComponent createLeftBottomPanel() {
		select = new JButton("Select Batch");
		return select;
	}
	
	public JComponent createRightBottomPanel() {
		block = new JButton("Block Batch");
		return block;
	}
	
	public JComponent createRightPanel() {
		JPanel p = new JPanel();
		Border border = new LineBorder(Color.black);
		TitledBorder t = new TitledBorder(border, "Information");
		p.setBorder(t);
		fields = new JTextField[8];
		fields[0] = new KrustyTextField("Batch Number");
		fields[2] = new KrustyTextField("Cookie Name");
		fields[4] = new KrustyTextField("Production Date");
		fields[6] = new KrustyTextField("Quality Index");
		p.setLayout(new GridLayout(8, 1));
		for(int i = 1; i < 8; i = i+2){
			fields[i] = new JTextField("                                         ");
			fields[i].setEditable(false);
		}
		for(int i = 0; i < 8; i++){
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
	for(int i = 1; i < 8; i = i+2){
		fields[i].setText("");
	}
    ArrayList<String> batchNbrs = db.showPallet(input);
    for(int i = 1; i < 8; i = i+2){
    fields[i].setText((batchNbrs.get((i-1)/2)));
    }
    if(batchNbrs.get(4).equals("1")){
    	/*block.setBlocked();*/
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