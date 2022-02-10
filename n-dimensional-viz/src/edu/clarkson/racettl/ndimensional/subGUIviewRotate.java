	package edu.clarkson.racettl.ndimensional;
	import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

	import java.awt.BorderLayout;
	//import java.awt.Graphics.*;
import java.awt.event.*;
	
/**
 * Submenu for rotating the field of view.
 * @author Louis Racette
 *
 */
public class subGUIviewRotate extends JPanel implements ActionListener, ChangeListener {
		protected JFrame rotateFrame = new JFrame("rotate");
		protected JButton closeButton = new JButton("close");
		protected JTextArea rotatex = new JTextArea(Double.toString(paintMethods3d.rotationFactor[0]*(180/3.14159)));
		protected JTextArea rotatey = new JTextArea(Double.toString(paintMethods3d.rotationFactor[1]*(180/3.14159)));
		protected JTextArea rotatez = new JTextArea(Double.toString(paintMethods3d.rotationFactor[2]*(180/3.14159)));
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * Draw the submenu.
		 */
		public subGUIviewRotate()
		{
			JRadioButton uniform = new JRadioButton("Camera-oriented");
			JRadioButton relative = new JRadioButton("Origin-oriented");
		
			ButtonGroup rotateGroup = new ButtonGroup();
			rotateGroup.add(uniform);
			rotateGroup.add(relative);
			JPanel buttonPanel = new JPanel();

			JPanel sliderPanel = new JPanel();
			sliderPanel.add(rotatex,BorderLayout.NORTH);
			sliderPanel.add(rotatey,BorderLayout.CENTER);
			sliderPanel.add(rotatez,BorderLayout.SOUTH);
			buttonPanel.add(uniform, BorderLayout.NORTH);
			buttonPanel.add(relative, BorderLayout.SOUTH);
			
			rotateFrame.add(sliderPanel,BorderLayout.CENTER);
			uniform.setSelected(true);
			rotateFrame.add(closeButton, BorderLayout.SOUTH);
			uniform.addActionListener(this);
			relative.addActionListener(this);
			closeButton.addActionListener(this);
			
			closeButton.setActionCommand("close");
			uniform.setActionCommand("uniform");
			relative.setActionCommand("relative");
			rotateFrame.pack();
			rotateFrame.setVisible(true);
		}
		
		/**
		 * Monitor the submenu for changes.
		 */
		public void actionPerformed(ActionEvent e)
		{

			if ("close".equals(e.getActionCommand()))
			{
				try
				{
				paintMethods3d.rotationFactor[0] = Double.parseDouble(rotatex.getText())*(3.14159/180);
				paintMethods3d.rotationFactor[1] = Double.parseDouble(rotatey.getText())*(3.14159/180);
				paintMethods3d.rotationFactor[2] = Double.parseDouble(rotatez.getText())*(3.14159/180);
				}
				catch(NumberFormatException f)
				{
					
				}
				
				paintMethods3d.paint3Dobject();
				rotateFrame.dispose();
			}
			
			
		}

		
		public void stateChanged(ChangeEvent e) {
			JSlider source = (JSlider)e.getSource();
			if(source.equals(rotatex))
			{
				int value = source.getValue();
				paintMethods3d.rotationFactor[0] = value*(3.14159/180);
			}
			if(source.equals(rotatey))
			{
				int value = source.getValue();
				paintMethods3d.rotationFactor[1] = value*(3.14159/180);
			}
			if(source.equals(rotatez))
			{
				int value = source.getValue();
				paintMethods3d.rotationFactor[2] = value*(3.14159/180);
			}
			
		}
	}



