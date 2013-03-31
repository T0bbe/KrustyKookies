package dbtLab3;
import javax.swing.*;

public class KrustyTextField extends JTextField {
	
	KrustyTextField(String text){
		setBorder(null);
		setEditable(false);
		setText(text);
	}

}
