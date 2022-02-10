package edu.clarkson.racettl.ndimensional;
//runs out of memory after 3845 points added
//import org.apache.poi.xssf.usermodel.*; 
//no support for .xlsx as of yet
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.JFileChooser;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * Import points from an excel file.
 * @author Louis Racette
 *
 */
public class subGUIimport extends JComponent {
Workbook wb;
int sheetIndex = 0;
JFileChooser fc;
int returnVal;
/**
 * Draw a file chooser and import the selected excel file.
 * @throws FileNotFoundException
 */
public subGUIimport() throws FileNotFoundException
{
	//note: this only works if sheets are in exact right form
	int tempNumDims;
	int tempNumPoints;
	double[][] tempPoints;
	fc = new JFileChooser();
	returnVal = fc.showOpenDialog(subGUIimport.this);
	if (returnVal == JFileChooser.APPROVE_OPTION) {
        File file = fc.getSelectedFile();
	
	FileInputStream oFileInputStream = new FileInputStream(file);
	try {
		
		HSSFWorkbook oWorkbook=new HSSFWorkbook(oFileInputStream);
		wb =oWorkbook;
		sheetIndex = wb.getActiveSheetIndex();
		tempNumDims = wb.getSheetAt(sheetIndex).getRow(1).getLastCellNum();
		NDimensionalObject.DimensionLabels = new String[tempNumDims];
		NDimensionalObject.setNumberOfDimensions(tempNumDims);
		for(int jjj=0; jjj< tempNumDims; jjj++)
		{
			
			{
			NDimensionalObject.DimensionLabels[jjj] = wb.getSheetAt(sheetIndex).getRow(0).getCell(jjj).getStringCellValue();
			}
			
			}
		int iii=1;
		while (true)
		{
			try{
		if (wb.getSheetAt(sheetIndex).getRow(iii).getCell(0).getStringCellValue() == null)
		{
			tempNumPoints = iii-1;
			break;
		}
		
		}
		
		catch (IllegalStateException h)
		{	
		}
		catch (NullPointerException c)
		{
			tempNumPoints = iii-1;
			break;
		}
		finally
		{
			iii++;
		}
		}
		
		tempPoints = new double[tempNumPoints][tempNumDims];
		for (int nnn=1; nnn<= tempNumPoints; nnn++)
		{
			for(int jjj=0; jjj< tempNumDims; jjj++)
			{
				tempPoints[nnn-1][jjj]= wb.getSheetAt(sheetIndex).getRow(nnn).getCell(jjj).getNumericCellValue();
				
			}
		NDimensionalObject.addpoint(new point(tempPoints[nnn-1]));	
		}
		
	   
	
	} catch (IOException e) {
			e.printStackTrace();
	}
	}
}
}