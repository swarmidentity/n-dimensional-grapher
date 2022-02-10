package edu.clarkson.racettl.ndimensional;
import javax.swing.*;

import java.awt.BorderLayout;
//import java.awt.Graphics.*;
import java.awt.event.*;

/**
 * Submenu to change the point size.
 * @author Louis Racette
 *
 */
public class subGUIpointSize extends JPanel implements ActionListener {
	JFrame dimFrame = new JFrame("Point Size");
	JTextField dimText = new JTextField(Integer.toString(1),5);
	JButton closeButton = new JButton("close");
	static float pointSize;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Draw the submenu.
	 */
	public subGUIpointSize()
	{
		
		dimFrame.add(dimText,BorderLayout.NORTH);
		dimFrame.add(closeButton, BorderLayout.SOUTH);
		dimText.addActionListener(this);
		closeButton.addActionListener(this);
		closeButton.setActionCommand("close");
		dimFrame.pack();
		dimFrame.setVisible(true);
	}
	
	/**
	 * Monitor the submenu for changes.
	 */
	public void actionPerformed(ActionEvent e)
	{
		
		
		if ("close".equals(e.getActionCommand()))
		{
			paintMethods3d.paint3Dobject();
			pointSize =(Float.parseFloat(dimText.getText()));
			
			dimFrame.dispose();
		
		}
	}
}
