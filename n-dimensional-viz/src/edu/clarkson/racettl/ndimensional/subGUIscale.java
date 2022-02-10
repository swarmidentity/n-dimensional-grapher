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
 * Submenu to scale the object.
 * @author Louis Racette
 *
 */
public class subGUIscale extends JPanel implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected JFrame scaleFrame = new JFrame("Scale selected");
	protected JButton closeButton = new JButton("close");
	protected Object[][] data = new Object[NDimensionalObject.NumberOfDimensions][2];
	JRadioButton point;
	JRadioButton line;
	JRadioButton plane;
	JRadioButton objects;
	JTable scaleTable;
	String editMe = "point";
	
	/**
	 * Draw the submenu.
	 */
	public subGUIscale()
	{
		String[] columnNames = {"Dimension","Location"};
		for(int iii=0; iii< NDimensionalObject.NumberOfDimensions; iii++)
		{
			data[iii][0]=NDimensionalObject.DimensionLabels[iii];
			data[iii][1]="1.0";
		}
		scaleTable = new JTable(data,columnNames);
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
		scaleFrame.add(closeButton, BorderLayout.SOUTH);
		scaleFrame.add(buttonPanel, BorderLayout.NORTH);
		scaleFrame.add(scaleTable, BorderLayout.CENTER);
		scaleFrame.pack();
		scaleFrame.setVisible(true);
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
				double scaleFactor=0;
				try
				{
				scaleFactor=Double.parseDouble((String) data[jjj][1]);
				
				}
				catch(NullPointerException f)
				{
					scaleFactor= 0;	
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
							NDimensionalObject.actualPoints[tempPoint].location[jjj]*= scaleFactor;
					}
					if(editMe == "line")
					{
						for(int tempLine: select.selectedLines)
						{
							NDimensionalObject.actualPoints[NDimensionalObject.myLines[tempLine].endpoint1index].location[jjj]*= scaleFactor;
							NDimensionalObject.actualPoints[NDimensionalObject.myLines[tempLine].endpoint1index].location[jjj]*= scaleFactor;
						}
					}
					if(editMe == "plane")
					{
						for(int tempPlane: select.selectedPlanes)
						{
							NDimensionalObject.actualPoints[NDimensionalObject.myPlanes[tempPlane].pointsIndex[0]].location[jjj]*= scaleFactor;
							NDimensionalObject.actualPoints[NDimensionalObject.myPlanes[tempPlane].pointsIndex[1]].location[jjj]*= scaleFactor;
							NDimensionalObject.actualPoints[NDimensionalObject.myPlanes[tempPlane].pointsIndex[2]].location[jjj]*= scaleFactor;
						}
					}
					if(editMe == "objects")
					{
						for(point tempPoint:NDimensionalObject.actualPoints)
							tempPoint.location[jjj]*= scaleFactor;
					}
					NDimensionalObject.DimensionLabels[jjj]= (String) data[jjj][0];
				}
			
			}
			scaleFrame.dispose();
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
