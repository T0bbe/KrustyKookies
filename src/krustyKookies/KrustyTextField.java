package krustyKookies;
import javax.swing.*;

public class KrustyTextField extends JTextField {
	
	KrustyTextField(String text){
		super(text);
		setBorder(null);
		setEditable(false);
	}

}
