package edu.clarkson.racettl.ndimensional;

		

import javax.swing.*;

import java.awt.BorderLayout;

import java.awt.event.*;

/**
 * Submenu to draw multiple three-dimensional axes.
 * @author Louis Racette
 *
 */
public class subGUImult3d extends JPanel implements ActionListener {
	protected JFrame selectFrame = new JFrame("Setup multiple 3d Axes");
	protected JTable selectTable;
	protected JButton closeButton = new JButton("close");
	protected Object[][] data = new Object[NDimensionalObject.NumberOfDimensions][2];
	JScrollPane scrollPane;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Draw submenu.
	 */
	public subGUImult3d()
	{
		try
		{
		String[] columnNames = new String[2];
		columnNames[0]= "Dimensions";
		columnNames[1]= "Slicing variable";
		for(int jjj=0; jjj < NDimensionalObject.NumberOfDimensions; jjj++)
		{
			data[jjj][0]= NDimensionalObject.DimensionLabels[jjj];
			data[jjj][1]= 0.0;
		}
		JRadioButton sliceNo = new JRadioButton("Projection view");
		JRadioButton sliceYes = new JRadioButton("Slice view");
		sliceNo.setSelected(true);
		ButtonGroup group = new ButtonGroup();
		group.add(sliceNo);
		group.add(sliceYes);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(sliceNo);
		buttonPanel.add(sliceYes);
		selectTable= new JTable(data,columnNames);
		
		scrollPane = new JScrollPane(selectTable);
		selectTable.setFillsViewportHeight(true);
		selectFrame.add(buttonPanel, BorderLayout.NORTH);
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
		
	}
}

