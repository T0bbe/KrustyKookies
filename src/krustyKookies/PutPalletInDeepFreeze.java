package krustyKookies;

import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class PutPalletInDeepFreeze extends JComponent {
	private static final long serialVersionUID = 1;
	
	public PutPalletInDeepFreeze(JButton[] buttons,
			ActionListener actHand){
		setLayout(new GridLayout(2, 1));

		JPanel buttonPanel = new JPanel();
		for (int i = 0; i < buttons.length; i++) {
			buttonPanel.add(buttons[i]);
		}
		add(buttonPanel);

		for (int i = 0; i < buttons.length; i++) {
			buttons[i].addActionListener(actHand);
		}	
	}
}
