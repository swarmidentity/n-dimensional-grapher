package edu.clarkson.racettl.ndimensional;
import javax.swing.*;

import java.awt.BorderLayout;
//import java.awt.Graphics.*;
import java.awt.event.*;

/**
 * Submenu for zooming.
 * @author Louis Racette
 *
 */
public class subGUIzoom extends JPanel implements ActionListener {
	protected JFrame zoomFrame = new JFrame("Zoom");
	protected JButton closeButton = new JButton("close");
	
	public static String scaleMethod = "unscaled";
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Draw the submenu.
	 */
	public subGUIzoom()
	{
		JRadioButton uniform = new JRadioButton("Uniform Scaling");
		JRadioButton relative = new JRadioButton("Relative Scaling");
		JRadioButton unscaled = new JRadioButton("Unscaled Data");
		ButtonGroup zoomGroup = new ButtonGroup();
		zoomGroup.add(uniform);
		zoomGroup.add(relative);
		zoomGroup.add(unscaled);
		JPanel buttonPanel = new JPanel();

		buttonPanel.add(uniform, BorderLayout.NORTH);
		buttonPanel.add(relative, BorderLayout.CENTER);
		buttonPanel.add(unscaled, BorderLayout.SOUTH);
		zoomFrame.add(buttonPanel,BorderLayout.NORTH);
		
		zoomFrame.add(closeButton, BorderLayout.SOUTH);
		uniform.addActionListener(this);
		relative.addActionListener(this);
		unscaled.addActionListener(this);
		closeButton.addActionListener(this);
		closeButton.setActionCommand("close");
		uniform.setActionCommand("uniform");
		relative.setActionCommand("relative");
		unscaled.setActionCommand("unscaled");
		zoomFrame.pack();
		zoomFrame.setVisible(true);
	}
	
	/**
	 * Monitor the submenu for changes.
	 */
	public void actionPerformed(ActionEvent e)
	{

		if ("close".equals(e.getActionCommand()))
		{
			
			paintMethods3d.paint3Dobject();
			zoomFrame.dispose();
		}
		if ("uniform".equals(e.getActionCommand()))
		{
			scaleMethod = "uniform";
		}
		if ("relative".equals(e.getActionCommand()))
		{
			scaleMethod = "relative";
		}
		if ("unscaled".equals(e.getActionCommand()))
		{
			scaleMethod = "unscaled";
		}
	}
}

