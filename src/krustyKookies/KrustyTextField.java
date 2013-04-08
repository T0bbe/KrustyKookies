package krustyKookies;
import javax.swing.*;

public class KrustyTextField extends JTextField {
	
	private static final long serialVersionUID = 1;

	KrustyTextField(String text){
		super(text);
		setBorder(null);
		setEditable(false);
	}

}
