package krustyKookies;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.StringTokenizer;

import javax.swing.AbstractButton;
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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import krustyKookies.ProductionPane.ActionHandler;
import krustyKookies.ProductionPane.CookieNameSelectionListener;


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
	
	private DefaultListModel<Integer> batchNrListModel;
	
	
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
	
	private JRadioButtonMenuItem[] blockButtons;
	
	private JTextField startDateField;
	
	private JTextField endDateField;
	
	private DateChecker dateChecker;
	
	private JMenu menu;
	
	private JMenu menu2;
	
	private ButtonGroup cookieButtonGroup;
	
	private ButtonGroup blockButtonGroup;
	
	private ListActionHandler lActHand;
	
	public SearchPalletPane(Database db) {
	        this.db = db;
			startDateField = new JTextField();
			endDateField = new JTextField();
			dateChecker = new DateChecker();
	        messageLabel = new JLabel("                                                 ");
	        Border border = new LineBorder(Color.black);
			TitledBorder t = new TitledBorder(border, "Message Field");
			messageLabel.setBorder(t);
	        
	        setLayout(new BorderLayout());
	        
	        JComponent leftPanel = createLeftPanel();
	        add(leftPanel, BorderLayout.WEST);
	                
	        JPanel rightPanel = new JPanel();
	        JPanel centerPanel = new JPanel();
	        centerPanel.setLayout(new BorderLayout());
	        rightPanel.setLayout(new BorderLayout());
	        
	        JComponent middlePanel = createMiddlePanel();
	        JComponent rightBottomPanel = createRightBottomPanel();
	        JComponent resultPanel = createRightPanel();
	        rightBottomPanel.setBorder
	            (new CompoundBorder(new SoftBevelBorder(BevelBorder.RAISED),
	                                rightBottomPanel.getBorder()));

	        centerPanel.add(middlePanel, BorderLayout.CENTER);
	        add(centerPanel, BorderLayout.CENTER);
	        rightPanel.add(resultPanel, BorderLayout.CENTER);
	        rightPanel.add(rightBottomPanel, BorderLayout.SOUTH);
	        add(rightPanel, BorderLayout.EAST);
	        add(messageLabel, BorderLayout.SOUTH);
	
	}
	
	public JComponent createLeftPanel() {
		batchNrField = new JTextField(13);
		menu = new JMenu("Select Cookie Name");
		menu2 = new JMenu("Choose Block Status");
		Font newTextFieldFont = new Font(batchNrField.getFont().getName(),Font.BOLD,batchNrField.getFont().getSize());
		Font newTextFieldFont2 = new Font(batchNrField.getFont().getName(),Font.PLAIN,batchNrField.getFont().getSize());
		searchButton1 = new JButton("Perform Search");
		searchButton2 = new JButton("Perform Search");
		JMenuBar cookieMenuField = new JMenuBar();
		menu.setFont(newTextFieldFont2);
		JMenuBar blockMenuField = new JMenuBar();
		menu2.setFont(newTextFieldFont2);
		all = new JRadioButtonMenuItem("All");
		all.setSelected(true);
		menu.add(all);
		
		ArrayList<String> cButtons = getCookies();
		cookieButtons = new JRadioButtonMenuItem[8];
		blockButtons = new JRadioButtonMenuItem[3];
		cookieButtons[0] = all;
		for(int i=1; i <= cButtons.size(); i++){
			cookieButtons[i] = new JRadioButtonMenuItem(cButtons.get(i-1));
			menu.add(cookieButtons[i]);
		}
		blockButtons[0] = new JRadioButtonMenuItem("Both");
		blockButtons[0].setSelected(true);
		menu2.add(blockButtons[0]);
		blockButtons[1] = new JRadioButtonMenuItem("Yes");
		menu2.add(blockButtons[1]);
		blockButtons[2] = new JRadioButtonMenuItem("No");
		menu2.add(blockButtons[2]);
		groupBlockButtons(blockButtons);
		groupCookieButtons(cookieButtons);
		cookieMenuField.add(menu);
		blockMenuField.add(menu2);
		
		
		BatchNrSearchActionHandler batchActHand = new BatchNrSearchActionHandler(searchButton1);
		DateSearchButtonActionHandler dateActHand = new DateSearchButtonActionHandler(searchButton2);
		JPanel p = new JPanel();
		p.setLayout(new GridLayout(15, 1));
		Border border = new LineBorder(Color.black);
		TitledBorder t = new TitledBorder(border, "Search");
		t.setTitleColor(Color.black);
		KrustyTextField searchInfo1 = new KrustyTextField("Search by Pallet Number");
		searchInfo1.setFont(newTextFieldFont);
		KrustyTextField searchInfo2 = new KrustyTextField("Search by Date or Name");
		searchInfo2.setFont(newTextFieldFont);
		p.setBorder(t);
		p.add(searchInfo1);
		p.add(batchNrField);
		p.add(searchButton1);
		p.add(new JPanel());
		p.add(searchInfo2);
		p.add(cookieMenuField);
		p.add(new JPanel());
		p.add(blockMenuField);
		p.add(new JPanel());
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
		batches = new JList(batchNrListModel);
		lActHand = new ListActionHandler();
		batches.addListSelectionListener(lActHand);
		batches.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		batches.setMaximumSize(new Dimension(100,100));
		JPanel p = new JPanel();
		JScrollPane scroll = new JScrollPane(batches);
		scroll.setMaximumSize(new Dimension(100,100));
		p.setMaximumSize(new Dimension(100,100));
		scroll.setVerticalScrollBar(new JScrollBar());
		Border border = new LineBorder(Color.black);
		TitledBorder t = new TitledBorder(border, "Pallet Numbers");
		p.setLayout(new GridLayout(1, 3));
		p.setBorder(t);
		p.add(new JPanel());
		p.add(scroll);
		p.add(new JPanel());
		return p;
	}
	
	
	public JComponent createRightBottomPanel() {
		block = new JButton("Block / Unblock");
		BlockButtonActionHandler blockActHand = new BlockButtonActionHandler(block);
		return block;
	}
	
	public JComponent createRightPanel() {
		JPanel p = new JPanel();
		Border border = new LineBorder(Color.black);
		TitledBorder t = new TitledBorder(border, "Information");
		p.setBorder(t);
		fields = new JTextField[12];
		fields[0] = new KrustyTextField("Pallet Number");
		fields[2] = new KrustyTextField("Cookie Name");
		fields[4] = new KrustyTextField("Production Date");
		fields[6] = new KrustyTextField("Production Time");
		fields[8] = new KrustyTextField("Current Location");
		fields[10] = new KrustyTextField("Blocked");
		p.setLayout(new GridLayout(12, 1));
		for(int i = 1; i < 12; i = i+2){
			fields[i] = new JTextField("                                         ");
			fields[i].setEditable(false);
		}
		for(int i = 0; i < 12; i++){
			p.add(fields[i]);
		}
		
		return p;
	}

private void fetchPalletNr() {
	clearErrorField();
	if(batchNrField.getText().equals("")){
		messageLabel.setText("No data entered");
		return;
	}
	batchNrListModel.removeAllElements();
	try{
	Integer.valueOf(batchNrField.getText());
    batchNrListModel.addElement(db.getPallet(batchNrField.getText()));
	} catch (NumberFormatException e){
		messageLabel.setText("Input is not a number");
	}
	
	catch (Exception e){
		messageLabel.setText(e.getMessage());
	}
	}

private void blockPallet(String input) {
	clearErrorField();
	try{
	db.blockPallet(input);
	}
	catch(Exception e){
		messageLabel.setText(e.getMessage());
	}
	}


private void fetchBatch() {
	clearErrorField();
	String[] searchCriterias = new String[4];
	boolean finished = false;
	int i = 0;
	AbstractButton button;
	Enumeration<AbstractButton> st = cookieButtonGroup.getElements();
	while(i < cookieButtonGroup.getButtonCount() && !finished){
		button = st.nextElement();
		if(button.isSelected()){
			searchCriterias[0] = button.getText();
			finished = true;
		} 
		i++;	
	}
	i = 0;
	finished = false;
	st = blockButtonGroup.getElements();
	while(i < blockButtonGroup.getButtonCount() && !finished){
		button = st.nextElement();
		if(button.isSelected()){
			searchCriterias[3] = button.getText();
			finished = true;
		} 
		i++;	
	}
	{
	try{
	if(dateChecker.checkDate(startDateField)){
		searchCriterias[1] = startDateField.getText();
	}
	if(dateChecker.checkDate(endDateField)){
		searchCriterias[2] = endDateField.getText();
	}
	} catch (Exception e){
		messageLabel.setText(e.getMessage());
		return;
	}
	batchNrListModel.removeAllElements();
	ArrayList<String> output = db.getBatch(searchCriterias);
	for(int j = 0; j < output.size(); j++){
    batchNrListModel.addElement(Integer.valueOf((output.get(j))));
	}
	}
	}

private void fetchInformation(String input) {
	clearErrorField();
	ArrayList<String> batchNbrs;
	try{
    batchNbrs = db.showPallet(input);
    fields[1].setText(input);
	} catch (Exception e){
    	messageLabel.setText(e.getMessage());
    	return;
	}
    for(int i = 3; i < 12; i = i+2){
    fields[i].setText((batchNbrs.get((i-1)/2)));
    }
    
    
}

private void clearErrorField(){
	messageLabel.setText(" ");
}

private void clearInformationField(){
	for(int i = 1; i < 10; i = i+2){
	fields[i].setText(" ");
}
}

private ArrayList<String> getCookies(){
	return db.allCookies();
}

class BlockButtonActionHandler implements ActionListener {
	
	BlockButtonActionHandler(JButton b){
		b.addActionListener(this);
	}
	
	
	public void actionPerformed(ActionEvent e) {
		
		try{String input = fields[1].getText();
			blockPallet(input);
		clearInformationField();
		fetchInformation(input);}
		catch (NullPointerException e2){
			messageLabel.setText("No data entered");
		}	
	}
}

class BatchNrSearchActionHandler implements ActionListener {
	
	BatchNrSearchActionHandler(JButton b){
		b.addActionListener(this);
	}
	
	public void actionPerformed(ActionEvent e) {
    fetchPalletNr();
}
}

class DateSearchButtonActionHandler implements ActionListener {
			
	DateSearchButtonActionHandler(JButton b){
		b.addActionListener(this);
	}
	
		public void actionPerformed(ActionEvent e) {
			fetchBatch();
					
				}
				}
			
		
class ListActionHandler implements ListSelectionListener {
	@Override
	public void valueChanged(ListSelectionEvent arg0) {
		try{
			fetchInformation(( (Integer) (batches.getSelectedValue())).toString());
		} catch (Exception e){
			clearInformationField();
		}
		
	}
}


@Override
public void entryActions() {
	// TODO Auto-generated method stub
	
}

public void groupCookieButtons(JRadioButtonMenuItem[] buttons) {
cookieButtonGroup = new ButtonGroup();
for(int i = 0; i < buttons.length; i++){
	cookieButtonGroup.add(buttons[i]);
}
}

public void groupBlockButtons(JRadioButtonMenuItem[] buttons) {
blockButtonGroup = new ButtonGroup();
for(int i = 0; i < buttons.length; i++){
	blockButtonGroup.add(buttons[i]);
}
}
}