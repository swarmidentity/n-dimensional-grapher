package edu.clarkson.racettl.ndimensional;

import java.text.DecimalFormat;

/**
 * This class rounds decimals to two places.
 * @author Louis Racette
 *
 */
public class decimalRounder {

	double roundTwoDecimals(double d){
		DecimalFormat twoDForm = new DecimalFormat("#.##");
		return Double.valueOf(twoDForm.format(d));
	}
}
