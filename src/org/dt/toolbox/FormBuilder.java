package org.dt.toolbox;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class FormBuilder{

	public static JFrame createJFrame(JPanel panel, String title) {
		JFrame frm = new JFrame();
		frm.setContentPane(panel);
		frm.setPreferredSize(new java.awt.Dimension(panel.getPreferredSize().width + 16, panel.getPreferredSize().height + 38)); //add some constants because Java is a bitch
		frm.setTitle(title);
		frm.pack();
		frm.setVisible(true);
		return frm;
	}

}
