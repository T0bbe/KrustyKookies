package krustyKookies;

import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class LeftOverdata extends JPanel {
	private static final long serialVersionUID = 1;
	
	public LeftOverdata(String[] texts, JTextField[] fields){
		JPanel left = new JPanel();
		left.setLayout(new GridLayout(texts.length, 1));
		for (int i = 0; i < texts.length; i++) {
			JLabel label = new JLabel(texts[i] + "      ", JLabel.RIGHT);
			left.add(label);
		}

		JPanel right = new JPanel();
		right.setLayout(new GridLayout(fields.length, 1));
		for (int i = 0; i < fields.length; i++) {
			right.add(fields[i]);
		}

		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		add(left);
		add(right);
	}

}
