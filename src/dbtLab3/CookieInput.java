package dbtLab3;

import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class CookieInput extends JPanel {
	private static final long serialVersionUID = 1;

	public CookieInput(String[] texts, JTextField fields){
		JPanel left = new JPanel();
		left.setLayout(new GridLayout(texts.length, 1));
		for (int i = 0; i < texts.length; i++) {
			JLabel label = new JLabel(texts[i] + "      ", JLabel.RIGHT);
			left.add(label);
		}

		JPanel right = new JPanel();
		right.add(fields);

		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		add(left);
		add(right);
	}
	

	
}
