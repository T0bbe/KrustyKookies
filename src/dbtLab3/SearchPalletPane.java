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

import javax.swing.ButtonGroup;
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
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
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
    protected JLabel errorField;
	
	private DefaultListModel batchNrListModel;
	
	
	// 1 för BatchNr, 3 för Kaknamn, 5 för Produktionsdatum och 7 för QualIndex.
	private JTextField[] fields;
	
	private JTextField batchNrField;
	
	private JList batches;
	
	// Sökknapp för batch
	private JButton searchButton1;
	
	// Sökknapp för datum + kaknamn
	private JButton searchButton2;
	
	// Knapp för att blockera/avblockera batch
	private JButton block;
	
	// Knapp för att välja batch att visas
	private JButton select;
	
	private JRadioButtonMenuItem all;
	
	private JRadioButtonMenuItem[] cookieButtons;
	
	private ButtonActionHandler bActHand;
	
	private ListActionHandler lActHand;
	
	public SearchPalletPane(Database db) {
	        this.db = db;
	        errorField = new JLabel("                                                 ");
	        Border border = new LineBorder(Color.black);
			TitledBorder t = new TitledBorder(border, "Error Message");
			errorField.setBorder(t);
	        
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
	        add(errorField, BorderLayout.SOUTH);
		bActHand = new ButtonActionHandler();
	}
	
	public JComponent createLeftPanel() {
		batchNrField = new JTextField(13);
		JTextField startDateField = new JTextField();
		JTextField endDateField = new JTextField();
		
		JMenu menu = new JMenu("Select Cookie Name");
		Font newTextFieldFont = new Font(batchNrField.getFont().getName(),Font.BOLD,batchNrField.getFont().getSize());
		Font newTextFieldFont2 = new Font(batchNrField.getFont().getName(),Font.PLAIN,batchNrField.getFont().getSize());
		searchButton1 = new JButton("Perform Search");
		
		JMenuBar menuField = new JMenuBar();
		
		menu.setFont(newTextFieldFont2);
		all = new JRadioButtonMenuItem("All");
		all.setSelected(true);
		menu.add(all);
		cookieButtons = new JRadioButtonMenuItem[8];
		cookieButtons[0] = all;
		for(int i=1; i < 8; i++){
			cookieButtons[i] = new JRadioButtonMenuItem("Kaka" + i);
			menu.add(cookieButtons[i]);
		}
		groupButtons(cookieButtons);
		menuField.add(menu);
		
		searchButton2 = new JButton("Perform Search");
		searchButton1.addActionListener(new ButtonActionHandler());
		JPanel p = new JPanel();
		p.setLayout(new GridLayout(12, 1));
		Border border = new LineBorder(Color.black);
		TitledBorder t = new TitledBorder(border, "Search");
		t.setTitleColor(Color.black);
		KrustyTextField searchInfo1 = new KrustyTextField("Search by BatchNbr");
		searchInfo1.setFont(newTextFieldFont);
		KrustyTextField searchInfo2 = new KrustyTextField("Search by Date or Name");
		searchInfo2.setFont(newTextFieldFont);
		p.setBorder(t);
		p.add(searchInfo1);
		p.add(batchNrField);
		p.add(searchButton1);
		p.add(new JPanel());
		p.add(searchInfo2);
		p.add(menuField);
		p.add(new KrustyTextField("Start Date (Optional)"));
		p.add(startDateField);
		p.add(new KrustyTextField("End Date (Optional)"));
		p.add(endDateField);
		p.add(searchButton2);
		p.add(new JPanel());
		return p;
	}
	
	
	public JComponent createMiddlePanel() {
		batchNrListModel = new DefaultListModel();
		batchNrListModel.addElement("  1  ");
		batchNrListModel.addElement("  2  ");
		batchNrListModel.addElement("  3  ");
		batchNrListModel.addElement("  4  ");
		batchNrListModel.addElement("  5  ");
		batchNrListModel.addElement("  6  ");
		batchNrListModel.addElement("  7  ");
		batchNrListModel.addElement("  8  ");
		batchNrListModel.addElement("  9  ");
		batchNrListModel.addElement("  10  ");
		batchNrListModel.addElement("  11  ");
		batchNrListModel.addElement("  12  ");
		batches = new JList(batchNrListModel);
		batches.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JPanel p = new JPanel();
		JScrollPane scroll = new JScrollPane(batches);
		scroll.setVerticalScrollBar(new JScrollBar());
		Border border = new LineBorder(Color.black);
		TitledBorder t = new TitledBorder(border, "Batch Numbers");
		p.setBorder(t);
		p.add(scroll);
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
		try{ fetchBatch(batchNrField.getText());
		} catch (NullPointerException e2){
			errorField.setText("No data entered");
		}
		
		
	}
}

class ListActionHandler implements ActionListener {
	
	public void actionPerformed(ActionEvent e) {
		try{ fetchBatch(batchNrField.getText());
		} catch (NullPointerException e2){
			errorField.setText("No data entered");
		}
}
}

@Override
public void entryActions() {
	// TODO Auto-generated method stub
	
}

public void groupButtons(JRadioButtonMenuItem[] buttons) {
ButtonGroup buttonGroup = new ButtonGroup();
for(int i = 0; i < buttons.length; i++){
	buttonGroup.add(buttons[i]);
}
}
}