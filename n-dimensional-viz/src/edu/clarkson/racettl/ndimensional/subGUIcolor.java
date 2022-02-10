package edu.clarkson.racettl.ndimensional;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Submenu for changing point, line, plane, axis and background colors.
 * @author Louis Racette
 *
 */
public class subGUIcolor extends JPanel implements ChangeListener,ActionListener{
	
	protected JFrame colorFrame = new JFrame("Choose colors");
	protected JButton closeButton = new JButton("close");
	JColorChooser choice;
	JRadioButton axes;
	JRadioButton point;
	JRadioButton line;
	JRadioButton plane;
	JRadioButton background;
	Color newColor= Color.black;
	String editMe = "axes";
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Draw the submenu.
	 */
	public subGUIcolor()
	{
		axes = new JRadioButton("Axes");
		point = new JRadioButton("Point");
		line = new JRadioButton("Line");
		plane = new JRadioButton("Plane");
		background = new JRadioButton("Background");
		choice = new JColorChooser();
		choice.getSelectionModel().addChangeListener(this);
		ButtonGroup colorGroup = new ButtonGroup();
		colorGroup.add(axes);
		colorGroup.add(point);
		colorGroup.add(line);
		colorGroup.add(plane);
		colorGroup.add(background);
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(axes, BorderLayout.NORTH);
		buttonPanel.add(point, BorderLayout.AFTER_LAST_LINE);
		buttonPanel.add(line, BorderLayout.AFTER_LAST_LINE);
		buttonPanel.add(plane, BorderLayout.AFTER_LAST_LINE);
		buttonPanel.add(background, BorderLayout.AFTER_LAST_LINE);
		colorFrame.add(buttonPanel,BorderLayout.NORTH);
		colorFrame.add(choice,BorderLayout.CENTER);
		axes.setSelected(true);
		colorFrame.add(closeButton, BorderLayout.SOUTH);
		axes.addActionListener(this);
		point.addActionListener(this);
		line.addActionListener(this);
		plane.addActionListener(this);
		background.addActionListener(this);
		closeButton.addActionListener(this);
		closeButton.setActionCommand("close");
		axes.setActionCommand("axes");
		point.setActionCommand("point");
		line.setActionCommand("line");
		plane.setActionCommand("plane");
		background.setActionCommand("background");
		colorFrame.pack();
		colorFrame.setVisible(true);
	}
	
	/**
	 * Monitor the submenu for changes.
	 */
	public void actionPerformed(ActionEvent e)
	{

		if ("close".equals(e.getActionCommand()))
		{
			colorFrame.dispose();
			
			paintMethods3d.paint3Dobject();
		}
		if ("axes".equals(e.getActionCommand()))
		{
			editMe = e.getActionCommand();
			
		}
		if ("point".equals(e.getActionCommand()))
		{
			editMe = e.getActionCommand();

		}
		if ("line".equals(e.getActionCommand()))
		{
			editMe = e.getActionCommand();
		}
		if ("plane".equals(e.getActionCommand()))
		{
			editMe = e.getActionCommand();

		}
		if ("background".equals(e.getActionCommand()))
		{
			editMe = e.getActionCommand();

		}
		
	}


	/**
	 * Monitor the radio buttons for changes.
	 */
	public void stateChanged(ChangeEvent f) {
		newColor = choice.getColor();
		if (editMe == "axes")
			paintMethods3d.axesColor = newColor;
		if (editMe=="point")
			paintMethods3d.pointColor = newColor;
		if (editMe == "line")
			paintMethods3d.lineColor = newColor;
		if (editMe == "plane")
			paintMethods3d.planeColor = newColor;
		if (editMe == "background")
			paintMethods3d.backgroundColor = newColor;
		
		
	}
}



