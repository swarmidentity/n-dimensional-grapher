package edu.clarkson.racettl.ndimensional;
import javax.media.j3d.Transform3D;
import javax.swing.*;
//not intuitive/easy- clean up
import java.awt.BorderLayout;
	//import java.awt.Graphics.*;
import java.awt.event.*;

/**
 * Submenu to change the camera position.
 * @author Louis Racette
 *
 */
public class subGUIcameraPosition extends JPanel implements ActionListener {
		protected JFrame panFrame = new JFrame("pan");
		protected JButton closeButton = new JButton("close");
		JTextArea panY = new JTextArea(Double.toString(paintMethods3d.cameraPosition.y));
		JTextArea panX = new JTextArea(Double.toString(paintMethods3d.cameraPosition.x));
		JTextArea panZ = new JTextArea(Double.toString(paintMethods3d.cameraPosition.z));
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * Draw the submenu.
		 */
		public subGUIcameraPosition()
		{
			JPanel panPanel = new JPanel();
			panPanel.add(panX,BorderLayout.NORTH);
			panPanel.add(panY,BorderLayout.CENTER);
			panPanel.add(panZ,BorderLayout.SOUTH);
			panFrame.add(panPanel,BorderLayout.NORTH);
			panFrame.add(closeButton, BorderLayout.SOUTH);
			closeButton.addActionListener(this);
			closeButton.setActionCommand("close");
			panFrame.pack();
			panFrame.setVisible(true);
			
		}
		
		/**
		 * Monitor the submenu for changes.
		 */
		public void actionPerformed(ActionEvent e)
		{

			if ("close".equals(e.getActionCommand()))
			{
				try{
					
				paintMethods3d.setCamera(Double.parseDouble(panX.getText()), Double.parseDouble(panY.getText()),Double.parseDouble(panZ.getText()));
				}
				catch(NumberFormatException f)
				{
					paintMethods3d.setCamera(0,0,5);
				}
				paintMethods3d.clearScreen();
				paintMethods3d.rotatecam = new Transform3D();
				Transform3D rotatecam1 = new Transform3D();
				   Transform3D rotatecam2 = new Transform3D();
				   Transform3D rotatecam3 = new Transform3D();
				   rotatecam1.rotX(paintMethods3d.rotationFactor[0]); 
				   rotatecam2.rotY(paintMethods3d.rotationFactor[1]);
				   rotatecam3.rotZ(paintMethods3d.rotationFactor[2]);
				   rotatecam1.mul(rotatecam2);
				   rotatecam1.mul(rotatecam3);
				   paintMethods3d.rotatecam.set(paintMethods3d.cameraPosition);
				   paintMethods3d.rotatecam.mul(rotatecam1);
				   paintMethods3d.universe.getViewingPlatform().getViewPlatformTransform().setTransform(paintMethods3d.rotatecam);
				paintMethods3d.paint3Dobject();
				panFrame.dispose();
			}

			
		}
	}


