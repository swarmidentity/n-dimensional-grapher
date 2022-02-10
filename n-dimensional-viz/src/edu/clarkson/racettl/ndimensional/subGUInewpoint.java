package edu.clarkson.racettl.ndimensional;
import javax.swing.*;
//note: crashes if dime
import java.awt.BorderLayout;
//import java.awt.Graphics.*;
import java.awt.event.*;

/**
 * Submenu for drawing a new point.
 * @author Louis Racette
 *
 */
public class subGUInewpoint extends JPanel implements ActionListener {
	protected JFrame pointFrame = new JFrame("Add Point");
	protected JTable pointTable;
	protected JButton closeButton = new JButton("Draw Point");
	protected Object[][] data = new Object[NDimensionalObject.NumberOfDimensions+1][2];
	protected double[] newPoint = new double[NDimensionalObject.NumberOfDimensions];
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Draw the submenu.
	 */
	public subGUInewpoint()
	{
		String[] columnNames = {"Dimension labels","Location"};
		
		for(int jjj=0; jjj < NDimensionalObject.NumberOfDimensions; jjj++)
		{
			data[jjj][0]= NDimensionalObject.DimensionLabels[jjj];
		}
		data[NDimensionalObject.NumberOfDimensions][1]= "Click Here When Finished";
		pointTable= new JTable(data, columnNames);
		
		JScrollPane scrollPane = new JScrollPane(pointTable);
		pointTable.setFillsViewportHeight(true);
		
		pointFrame.add(scrollPane,BorderLayout.NORTH);
		pointFrame.add(closeButton, BorderLayout.SOUTH);
		closeButton.addActionListener(this);
		closeButton.setActionCommand("close");
		pointFrame.pack();
		pointFrame.setVisible(true);
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
				try
				{
				newPoint[jjj]= Float.parseFloat((String) data[jjj][1]);
				
				}
				catch(NullPointerException f)
				{
					
					newPoint[jjj]=0;
				}
				catch(NumberFormatException g)
				{
				newPoint[jjj]=0;
				}
				finally
				{
					NDimensionalObject.DimensionLabels[jjj]= (String) data[jjj][0];
				}
			}

			point tempPoint = new point(newPoint);
			NDimensionalObject.addpoint(tempPoint);
			paintMethods3d.paint3Dobject();
			pointFrame.dispose();
			
		}
	}
}

