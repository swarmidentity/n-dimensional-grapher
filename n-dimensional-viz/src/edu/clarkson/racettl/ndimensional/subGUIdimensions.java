package edu.clarkson.racettl.ndimensional;
import javax.swing.*;

import java.awt.BorderLayout;
//import java.awt.Graphics.*;
import java.awt.event.*;
/**
 * Submenu for changing the number of dimensions.
 * @author Louis Racette
 *
 */
public class subGUIdimensions extends JPanel implements ActionListener {
	JFrame dimFrame = new JFrame("Number of Dimensions");
	JTextField dimText = new JTextField(Integer.toString(NDimensionalObject.NumberOfDimensions),5);
	JButton closeButton = new JButton("close");
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Draw the submenu.
	 */
	public subGUIdimensions()
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
			NDimensionalObject.setNumberOfDimensions(Integer.parseInt(dimText.getText()));
			dimFrame.dispose();
		
		}
	}
}
