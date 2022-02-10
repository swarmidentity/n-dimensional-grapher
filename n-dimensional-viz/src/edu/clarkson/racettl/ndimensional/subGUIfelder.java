package edu.clarkson.racettl.ndimensional;

		

import javax.swing.*;

import java.awt.BorderLayout;
//import java.awt.Graphics.*;
import java.awt.event.*;
/**
 * Submenu for drawing felder representation.
 * @author Louis Racette
 *
 */
public class subGUIfelder extends JPanel implements ActionListener {
	protected decimalRounder decimal = new decimalRounder();
	protected JFrame selectFrame = new JFrame("Setup Felder");
	protected JTable selectTable;
	protected JButton closeButton = new JButton("close");
	protected Object[][] data = new Object[NDimensionalObject.NumberOfDimensions][4];
	
	JScrollPane scrollPane;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Draw the submenu.
	 */
	public subGUIfelder()
	{
		try
		{
		String[] columnNames = {"Dimension","Slice interval","Start","End"};
		for(int jjj=0; jjj < NDimensionalObject.NumberOfDimensions; jjj++)
		{
			data[jjj][0]= NDimensionalObject.DimensionLabels[jjj];
			data[jjj][1]= 1.0;
			data[jjj][2]= -3.0;
			data[jjj][3]= 3.0;
		}
		
		JPanel buttonPanel = new JPanel();
		
		selectTable= new JTable(data,columnNames);
		
		scrollPane = new JScrollPane(selectTable);
		selectTable.setFillsViewportHeight(true);
		selectFrame.add(buttonPanel, BorderLayout.NORTH);
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
		
			Double sliceSpacing = NDimensionalObject.findLargestUniform()+2;
			Double groupSpacing = 2.0;
			Double cellSize = 1.0;
				
				Double[] dimInterval = new Double[NDimensionalObject.NumberOfDimensions];
				Double[] dimStart = new Double[NDimensionalObject.NumberOfDimensions];
				Double[] dimEnd = new Double[NDimensionalObject.NumberOfDimensions];
				
				for(int jjj=0; jjj < NDimensionalObject.NumberOfDimensions; jjj++)
				{
					try
					{
						dimInterval[jjj]= (Double) data[jjj][1];
					}
					
					catch(NullPointerException f)
					{
						dimInterval[jjj]=0.0;
					}
					catch(ClassCastException g)
					{
						dimInterval[jjj]=Double.parseDouble((String) data[jjj][1]);
					}
					try
					{
						dimStart[jjj]=(Double) data[jjj][2];
					}
					
					catch(NullPointerException f)
					{
						dimStart[jjj]=0.0;
					}
					catch(ClassCastException g)
					{
						dimStart[jjj]=Double.parseDouble((String) data[jjj][2]);
					}
					try
					{
						dimEnd[jjj]=(Double) data[jjj][3];
					}
					
					catch(NullPointerException f)
					{
						dimEnd[jjj]=0.0;	
					}
					catch(ClassCastException g)
					{
						dimEnd[jjj]=Double.parseDouble((String) data[jjj][3]);
					}
					finally
					{
						dimEnd[jjj] = decimal.roundTwoDecimals(dimEnd[jjj]);
						dimStart[jjj] = decimal.roundTwoDecimals(dimStart[jjj]);
						dimInterval[jjj] = decimal.roundTwoDecimals(dimInterval[jjj]);
					}
					
					}
			
					
			
			paintMethods3d.paint3Dobject();
			selectFrame.dispose();
			felderAxes(sliceSpacing,groupSpacing,cellSize,dimInterval,dimStart,dimEnd);
			
	}
		
		
	}
	/**
	 * Determine the felder spacing required.
	 * @param sliceSpacing
	 * @param groupSpacing
	 * @param cellSize
	 * @param dimInterval
	 * @param dimStart
	 * @param dimEnd
	 */
	public void felderAxes(Double sliceSpacing, Double groupSpacing, Double cellSize, Double[] dimInterval, Double[] dimStart, Double[] dimEnd)
	{
		
		int height = 1;
		int width = 1;
		
		int[] numSlices = new int[NDimensionalObject.NumberOfDimensions-3];
		for(int iii= 0; iii < NDimensionalObject.NumberOfDimensions-3;iii++)
		{
			numSlices[iii]=(int) ((dimEnd[iii+3]-dimStart[iii+3])/dimInterval[iii+3])+1;
			if (numSlices[iii] !=0)
			{
			if (iii%2 != 0)//0 counts, so this is for even dims over 4
			{
				width *= numSlices[iii];
			}
			else//odd dims over 4
			{
				height *= numSlices[iii];
			}
			}
		}
		float[] axisStartsX= new float[width];
		float[] axisStartsY= new float[height];
		
		
		for(int iii= 0; iii < width; iii++)
		{
			if(iii==0)
				axisStartsX[iii]=0;
			else if(iii%numSlices[0] !=0)
			axisStartsX[iii]= (float) (axisStartsX[iii-1] +(cellSize + sliceSpacing));
			else if(iii%numSlices[0] ==0)
			axisStartsX[iii]= (float) (axisStartsX[iii-1] +(cellSize + sliceSpacing)+groupSpacing);
		}
		for (int jjj=0; jjj< height; jjj++)
		{
			if(jjj==0)
				axisStartsY[jjj]=0;
			else if(jjj%numSlices[0] !=0)
			axisStartsY[jjj]= (float) (axisStartsY[jjj-1] +(cellSize + sliceSpacing));
			else if(jjj%numSlices[0] ==0)
			axisStartsY[jjj]= (float) (axisStartsY[jjj-1] +(cellSize + sliceSpacing)+groupSpacing);//crashes on 7d
		}
			paintMethods3d.setupFelder(dimStart,dimEnd,dimInterval,axisStartsX,axisStartsY,numSlices);
		
	}

		
	
}

