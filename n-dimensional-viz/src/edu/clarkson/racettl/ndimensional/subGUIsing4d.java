package edu.clarkson.racettl.ndimensional;

import javax.swing.*;

import java.awt.BorderLayout;
//import java.awt.Graphics.*;
import java.awt.event.*;

/**
 * Submenu for drawing single four-dimensional axes set.
 * @author Louis Racette
 *
 */
public class subGUIsing4d extends JPanel implements ActionListener {
	public static int AxisOfTime;
	public static double[] timeSlices;
	
	protected JFrame selectFrame = new JFrame("Select four dimensions");
	protected JTable selectTable;
	protected JButton closeButton = new JButton("close");
	protected JComboBox timeAxis = new JComboBox();
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
	 * Draw the submenu.
	 */
	public subGUIsing4d()
	{
		try
		{
			String[] allDims = new String[NDimensionalObject.NumberOfDimensions];
			for(int jjj=0; jjj < allDims.length; jjj++)
				allDims[jjj]=NDimensionalObject.DimensionLabels[jjj];
			timeAxis = new JComboBox(allDims);
			timeAxis.setSelectedIndex(0);
			timeAxis.addActionListener(this);
			
		String[] columnNames = new String[2];
		columnNames[0]= "Dimensions";
		columnNames[1]= "Slicing variable (if unselected)";
		for(int jjj=0; jjj < NDimensionalObject.NumberOfDimensions; jjj++)
		{
			data[jjj][0]= NDimensionalObject.DimensionLabels[jjj];
			data[jjj][1]= 0.0;
		}
		JRadioButton sliceNo = new JRadioButton("Projection view");
		JRadioButton sliceYes = new JRadioButton("Slice view");
		
		
		ButtonGroup group = new ButtonGroup();
		group.add(sliceNo);
		group.add(sliceYes);
		
		JPanel timePanel = new JPanel();
		timePanel.add(timeAxis);
		timePanel.add(start);
		timePanel.add(increment);
		timePanel.add(end);
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(sliceNo);
		buttonPanel.add(sliceYes);
		selectTable= new JTable(data,columnNames);
		JPanel comboPanel = new JPanel();
		comboPanel.add(timePanel,BorderLayout.NORTH);
		scrollPane = new JScrollPane(selectTable);
		selectTable.setFillsViewportHeight(true);
		selectFrame.add(comboPanel, BorderLayout.NORTH);
		selectFrame.add(scrollPane,BorderLayout.CENTER);
		selectFrame.add(closeButton, BorderLayout.SOUTH);
		sliceNo.addActionListener(this);
		sliceYes.addActionListener(this);
		
		closeButton.addActionListener(this);
		closeButton.setActionCommand("close");
		sliceNo.setActionCommand("sliceNo");
		sliceYes.setActionCommand("sliceYes");
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
			AxisOfTime = timeAxis.getSelectedIndex();
			int[] selectedDims = new int[3];
			if(selectTable.getSelectedRows().length == 3)
			{
			selectedDims = selectTable.getSelectedRows();
			paintMethods3d.setActive(selectedDims[0], selectedDims[1], selectedDims[2]);
			
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
			
			}
			else
			{
				System.out.println("Invalid number selected.");
				paintMethods3d.setSliceVars(new double[NDimensionalObject.NumberOfDimensions]);
			}
			double tempStart;
			double tempInc;
			double tempEnd;
			try
			{
				tempStart = Double.parseDouble(start.getText());
			}
			catch(Exception f)
			{
				tempStart = -1;
			}
			try
			{
				tempInc = Double.parseDouble(increment.getText());
				if(tempInc ==0)
					tempInc =1;
			}
			catch(Exception f)
			{
				tempInc = .1;
			}
			try
			{
				tempEnd = Double.parseDouble(end.getText());
			}
			catch(Exception f)
			{
				tempEnd = 1;
			}
			int index = 0;
			timeSlices = new double[(int) ((tempEnd-tempStart)/tempInc)+1];
			for(double iii= tempStart; iii <= tempEnd; iii+=tempInc)
			{
				//correct for floating errors
				if(tempInc < 1)
				{
					float tempFix = (float) (100/tempInc);
					iii=(double) Math.rint(iii*tempFix)/tempFix;
				}
				timeSlices[index]=iii;
				index++;
			}
			
			
			
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

