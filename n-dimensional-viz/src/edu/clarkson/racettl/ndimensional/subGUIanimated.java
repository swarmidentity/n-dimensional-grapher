package edu.clarkson.racettl.ndimensional;
import javax.swing.*;


import java.awt.BorderLayout;
import java.awt.event.*;

/**
 * Submenu for rotating axes method.
 */
public class subGUIanimated extends JPanel implements ActionListener {
	protected JFrame animatedFrame = new JFrame("animated");
	protected JButton closeButton = new JButton("close");
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public subGUIanimated()
	{
		JRadioButton ortho = new JRadioButton("Orthographic");
		JRadioButton perspective = new JRadioButton("Perspective");
	
		ButtonGroup animatedGroup = new ButtonGroup();
		animatedGroup.add(ortho);
		animatedGroup.add(perspective);
		JPanel buttonPanel = new JPanel();

		buttonPanel.add(ortho, BorderLayout.NORTH);
		buttonPanel.add(perspective, BorderLayout.SOUTH);
		animatedFrame.add(buttonPanel,BorderLayout.NORTH);
		
		animatedFrame.add(closeButton, BorderLayout.SOUTH);
		ortho.addActionListener(this);
		perspective.addActionListener(this);
		closeButton.addActionListener(this);
		closeButton.setActionCommand("close");
		ortho.setActionCommand("ortho");
		perspective.setActionCommand("perspective");
		animatedFrame.pack();
		animatedFrame.setVisible(true);
	}
	
	/**
	 * Monitor any changes made to the submenu.
	 */
	public void actionPerformed(ActionEvent e)
	{

		if ("close".equals(e.getActionCommand()))
		{
			paintMethods3d.paint3Dobject();
			animatedFrame.dispose();
		}
		if ("ortho".equals(e.getActionCommand()))
		{
			paintMethods3d.animView = "ortho";
		}
		if ("perspective".equals(e.getActionCommand()))
		{
			paintMethods3d.animView = "perspective";
		}
		
	}
}

