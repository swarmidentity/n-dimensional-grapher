package edu.clarkson.racettl.ndimensional;
import javax.swing.*;

import java.awt.BorderLayout;
//import java.awt.Graphics.*;
import java.awt.event.*;

/**
 * Submenu for automatically drawing lines.
 * @author Louis Racette
 *
 */
public class subGUIautoLine extends JPanel implements ActionListener {
	JFrame dimFrame = new JFrame("Number of Connections");
	JTextField connectText = new JTextField("Number of Lines per Point");
	JTextField distance = new JTextField("Maximum Line Distance");
	JButton closeButton = new JButton("close");
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Draw submenu.
	 */
	public subGUIautoLine()
	{
		
		dimFrame.add(connectText,BorderLayout.NORTH);
		dimFrame.add(distance,BorderLayout.CENTER);
		dimFrame.add(closeButton, BorderLayout.SOUTH);
		connectText.addActionListener(this);
		closeButton.addActionListener(this);
		closeButton.setActionCommand("close");
		dimFrame.pack();
		dimFrame.setVisible(true);
	}
	
	/**
	 * Monitor changes to submenu.
	 */
	public void actionPerformed(ActionEvent e)
	{
		
		//Actions from submenu
		if ("close".equals(e.getActionCommand()))
		{
			try
			{
			NDimensionalObject.autoLine(Integer.parseInt(connectText.getText()),Double.parseDouble(distance.getText()));
			}
			catch(Exception h)
			{
				NDimensionalObject.autoLine(4, 0.5);
			}
			paintMethods3d.paint3Dobject();
			dimFrame.dispose();
		
		}
	}
}
