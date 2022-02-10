package edu.clarkson.racettl.ndimensional;

import javax.swing.*;

import java.awt.BorderLayout;
//import java.awt.Graphics.*;
import java.awt.event.*;
/**
 * Submenu for drawing multiple four-dimensional axes sets.
 * @author Louis Racette
 *
 */
public class subGUImult4d extends JPanel implements ActionListener {
	public static int AxisOfTime;
	public static float[] timeSlices;
	
	protected JFrame selectFrame = new JFrame("Select four dimensions");
	protected JTable selectTable;
	protected JButton closeButton = new JButton("close");

	protected Object[][] data = new Object[NDimensionalObject.NumberOfDimensions][2];
	JScrollPane scrollPane;
	protected JTextArea start= new JTextArea("time start");
	protected JTextArea increment= new JTextArea("time increment");
	protected JTextArea end = new JTextArea("time end");
	public static Boolean rotating = false;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Draw submenu.
	 */
	public subGUImult4d()
	{
		try
		{
			String[] allDims = new String[NDimensionalObject.NumberOfDimensions];
			for(int jjj=0; jjj < allDims.length; jjj++)
				allDims[jjj]=NDimensionalObject.DimensionLabels[jjj];
			
			
		String[] columnNames = new String[2];
		columnNames[0]= "Dimensions";
		columnNames[1]= "Slicing variable";
		for(int jjj=0; jjj < NDimensionalObject.NumberOfDimensions; jjj++)
		{
			data[jjj][0]= NDimensionalObject.DimensionLabels[jjj];
			data[jjj][1]= 0.0;
		}
		
		
		JPanel timePanel = new JPanel();
		
		timePanel.add(start);
		timePanel.add(increment);
		timePanel.add(end);
		JPanel buttonPanel = new JPanel();
		
		selectTable= new JTable(data,columnNames);
		JPanel comboPanel = new JPanel();
		comboPanel.add(buttonPanel,BorderLayout.SOUTH);
		comboPanel.add(timePanel,BorderLayout.NORTH);
		scrollPane = new JScrollPane(selectTable);
		selectTable.setFillsViewportHeight(true);
		selectFrame.add(comboPanel, BorderLayout.NORTH);
		selectFrame.add(scrollPane,BorderLayout.CENTER);
		selectFrame.add(closeButton, BorderLayout.SOUTH);
		
		
		closeButton.addActionListener(this);
		closeButton.setActionCommand("close");
		
		selectFrame.pack();
		selectFrame.setVisible(true);
		
		}
		catch(ArrayIndexOutOfBoundsException f)
		{
			System.out.println("No points added");
		}
	}
	
	/**
	 * Monitor the submenu for changes.
	 */
	public void actionPerformed(ActionEvent e)
	{

		if ("close".equals(e.getActionCommand()))
		{
		
			
				double[] temp = new double[NDimensionalObject.NumberOfDimensions];
				for(int jjj=0; jjj < NDimensionalObject.NumberOfDimensions; jjj++)
				{
					try
					{
					temp[jjj]=Double.parseDouble((String) data[jjj][1]);
					}
					
					catch(NullPointerException f)
					{
					temp[jjj]= 0;	
					}
					catch(ClassCastException g)
					{
					temp[jjj]=(Double) data[jjj][1];
					}
					finally
					{
					
					}
					
					}
					
				paintMethods3d.setSliceVars(temp);
			
			
			
			float tempStart;
			float tempInc;
			float tempEnd;
			try
			{
				tempStart = (float) Double.parseDouble(start.getText());
			}
			catch(Exception f)
			{
				tempStart = -1;
			}
			try
			{
				tempInc = (float) Double.parseDouble(increment.getText());
				if(tempInc ==0)
					tempInc =1;
			}
			catch(Exception f)
			{
				tempInc = (float) .1;
			}
			try
			{
				tempEnd = (float) Double.parseDouble(end.getText());
			}
			catch(Exception f)
			{
				tempEnd = 1;
			}
			int index = 0;
			timeSlices = new float[(int) ((tempEnd-tempStart)/tempInc)+1];
			for(float iii= tempStart; iii <= tempEnd; iii+=tempInc)
			{
				//correct for floating errors
				if(tempInc < 1)
				{
					float tempFix = 100/tempInc;
					iii=(float) Math.rint(iii*tempFix)/tempFix;
				}
				timeSlices[index]=iii;
				index++;
			}
			
			
			paintMethods3d.clearScreen();
			paintMethods3d.paint3Dobject();
			selectFrame.dispose();
	
		}
		if ("sliceNo".equals(e.getActionCommand()))
		{
			paintMethods3d.setSlice(false);
		}
		if ("sliceYes".equals(e.getActionCommand()))
		{
			paintMethods3d.setSlice(true);
		}
		if ("rotateNo".equals(e.getActionCommand()))
		{
			rotating = false;
		}
		if ("rotateYes".equals(e.getActionCommand()))
		{
			rotating = true;
		}
		
	}
}

