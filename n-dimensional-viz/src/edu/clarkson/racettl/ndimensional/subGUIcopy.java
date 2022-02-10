package edu.clarkson.racettl.ndimensional;
	import java.awt.BorderLayout;
	import java.awt.event.ActionEvent;
	import java.awt.event.ActionListener;

	import javax.swing.ButtonGroup;
	import javax.swing.JButton;
	import javax.swing.JFrame;
	import javax.swing.JPanel;
	import javax.swing.JRadioButton;

/**
 * Submenu for copying parts of object.
 * @author Louis Racette
 *
 */
	public class subGUIcopy extends JPanel implements ActionListener {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		protected JFrame scaleFrame = new JFrame("Copy selected");
		protected JButton closeButton = new JButton("close");
		JRadioButton point;
		JRadioButton line;
		JRadioButton plane;
		JRadioButton objects;
		String editMe = "point";
		
		/**
		 * Draw submenu.
		 */
		public subGUIcopy()
		{
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
					if(editMe == "point")
						{
							for(int tempPoint:select.selectedPoints)
								NDimensionalObject.addpoint(NDimensionalObject.actualPoints[tempPoint]);
						}
						if(editMe == "line")
						{
							for(int tempLine:select.selectedLines)
								NDimensionalObject.addline(NDimensionalObject.myLines[tempLine]);
						}
						if(editMe == "plane")
						{
							for(int tempPlane:select.selectedPlanes)
								NDimensionalObject.addplane(NDimensionalObject.myPlanes[tempPlane]);
						}
						if(editMe == "objects")
						{
							for(point tempPoint:NDimensionalObject.actualPoints)
								NDimensionalObject.addpoint(tempPoint);
							for(line templine: NDimensionalObject.myLines)
							{
								NDimensionalObject.addline(templine);
							}
							for(plane tempPlane: NDimensionalObject.myPlanes)
							{
								NDimensionalObject.addplane(tempPlane);
							}
						}
						scaleFrame.dispose();
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
