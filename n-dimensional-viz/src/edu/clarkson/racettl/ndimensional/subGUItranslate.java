package edu.clarkson.racettl.ndimensional;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTable;

/**
 * Submenu for translating points, lines, planes or objects.
 * @author Louis Racette
 *
 */
public class subGUItranslate extends JPanel implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected JFrame translateFrame = new JFrame("Translate selected");
	protected JButton closeButton = new JButton("close");
	protected Object[][] data = new Object[NDimensionalObject.NumberOfDimensions][2];
	JRadioButton point;
	JRadioButton line;
	JRadioButton plane;
	JRadioButton objects;
	JTable translateTable;
	String editMe = "point";
	
	/**
	 * Draw the submenu.
	 */
	public subGUItranslate()
	{
		String[] columnNames = {"Dimension","Location"};
		for(int iii=0; iii< NDimensionalObject.NumberOfDimensions; iii++)
		{
			data[iii][0]=NDimensionalObject.DimensionLabels[iii];
			data[iii][1]="0.0";
		}
		translateTable = new JTable(data,columnNames);
		point = new JRadioButton("Points");
		line = new JRadioButton("Lines");
		plane = new JRadioButton("Planes");
		objects = new JRadioButton("Object");
		ButtonGroup colorGroup = new ButtonGroup();
		colorGroup.add(point);
		colorGroup.add(line);
		colorGroup.add(plane);
		colorGroup.add(objects);
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(point, BorderLayout.NORTH);
		buttonPanel.add(line, BorderLayout.AFTER_LAST_LINE);
		buttonPanel.add(plane, BorderLayout.AFTER_LAST_LINE);
		buttonPanel.add(objects, BorderLayout.AFTER_LAST_LINE);
		point.setSelected(true);
		point.addActionListener(this);
		line.addActionListener(this);
		plane.addActionListener(this);
		objects.addActionListener(this);
		closeButton.addActionListener(this);
		closeButton.setActionCommand("close");
		point.setActionCommand("point");
		line.setActionCommand("line");
		plane.setActionCommand("plane");
		objects.setActionCommand("objects");
		translateFrame.add(closeButton, BorderLayout.SOUTH);
		translateFrame.add(buttonPanel, BorderLayout.NORTH);
		translateFrame.add(translateTable, BorderLayout.CENTER);
		translateFrame.pack();
		translateFrame.setVisible(true);
	}
	/**
	 * Monitor the submenu for changes.
	 */
	public void actionPerformed(ActionEvent e)
	{

		if ("close".equals(e.getActionCommand()))
		{
			
			for(int jjj=0; jjj < NDimensionalObject.NumberOfDimensions; jjj++)
			{
				double translateFactor=0;
				try
				{
				translateFactor=Double.parseDouble((String) data[jjj][1]);
				
				}
				catch(NullPointerException f)
				{
					translateFactor= 0;	
				}
				catch(ClassCastException g)
				{
					System.out.println("Error occured. Please enter data in the form of a decimal number (such as 1.32).");
				}
				
				finally
				{
					if(editMe == "point")
					{
						for(int tempPoint:select.selectedPoints)
							NDimensionalObject.actualPoints[tempPoint].location[jjj]+= translateFactor;
					}
					if(editMe == "line")
					{
						for(int tempLine: select.selectedLines)
						{
							NDimensionalObject.actualPoints[NDimensionalObject.myLines[tempLine].endpoint1index].location[jjj]+= translateFactor;
							NDimensionalObject.actualPoints[NDimensionalObject.myLines[tempLine].endpoint1index].location[jjj]+= translateFactor;
						}
					}
					if(editMe == "plane")
					{
						for(int tempPlane: select.selectedPlanes)
						{
							NDimensionalObject.actualPoints[NDimensionalObject.myPlanes[tempPlane].pointsIndex[0]].location[jjj]+= translateFactor;
							NDimensionalObject.actualPoints[NDimensionalObject.myPlanes[tempPlane].pointsIndex[1]].location[jjj]+= translateFactor;
							NDimensionalObject.actualPoints[NDimensionalObject.myPlanes[tempPlane].pointsIndex[2]].location[jjj]+= translateFactor;
						}
					}
					if(editMe == "objects")
					{
						for(point tempPoint:NDimensionalObject.actualPoints)
							tempPoint.location[jjj]+= translateFactor;
					}
					NDimensionalObject.DimensionLabels[jjj]= (String) data[jjj][0];
				}
			
			}
			translateFrame.dispose();
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
		if ("objects".equals(e.getActionCommand()))
		{
			editMe = e.getActionCommand();
		}
	}
}
