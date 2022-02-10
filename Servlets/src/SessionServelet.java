 

import javax.servlet.*;
import javax.servlet.http.*;

//import edu.clarkson.racettl.onlinesurvey.SurveyData;

import java.io.*;
import java.net.*;

/**
 * This class is responsible for saving user data to the server once the survey has been completed.
 * @author Swarmidentity
 *
 */

public class SessionServelet extends HttpServlet
{
    private static String message = "Error during Servlet processing";
    
    /**
     * Receive an http servlet request and save a file with the sent data.
     */
    public void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
        	
        	int len = req.getContentLength();
            byte[] input = new byte[len];
        
            ServletInputStream sin = req.getInputStream();
            int c, count = 0 ;
            while ((c = sin.read(input, count, input.length-count)) != -1) {
                count +=c;
            }
            sin.close();
            
            // set the response code and write the response data
            resp.setStatus(HttpServletResponse.SC_OK);
            OutputStreamWriter writer = new OutputStreamWriter(resp.getOutputStream());
            int name = (int) System.currentTimeMillis();
      	  	writer.write(Integer.toString(name));
            
            
            try{ 
            	  // Create file with timestamp as name
            	FileOutputStream fstream = new FileOutputStream(new File(Integer.toString(name)));	
            	  BufferedOutputStream out = new BufferedOutputStream(fstream);
            	  out.write(input);
            	  out.flush();
            	  //Close the output stream
            	  out.close();
            	  fstream.close();
            	  writer.write("Data Saved");
            	  }catch (Exception e){//Catch exception if any
            	  System.err.println("Error: " + e.getMessage());
            	  writer.write("Data saving error");
            	  } 
            writer.flush();
            writer.close();
        } catch (IOException e) {
            try{
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().print(e.getMessage());
                resp.getWriter().close();
            } catch (IOException ioe) {
            }
        }
        
    }  
        
}

