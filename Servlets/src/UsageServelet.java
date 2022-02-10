//package edu.clarkson.racettl.onlinesurvey;
/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */ 

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

/**
 * This class is intended to run on an apache tomcat server and maintain the number of participants who have taken the survey.
 * @author Swarmidentity
 *
 */
public class UsageServelet extends HttpServlet
{
    private static String message = "Error during Servlet processing";
    private static String useDataPath = "testingUseData";
    private static int totalNumberParticipants;
	private static ArrayList<Integer> timesQuestionAsked;
    private static String pathSeparator = "\\";
    private static int numberToFill = 215;
    
    //constructor: this initializes the servelet to a starting condition of 0 participants.
    UsageServelet()
    {
    	totalNumberParticipants = 0;
    	timesQuestionAsked = new ArrayList<Integer>();
    	
    	for(int temp = 0; temp < numberToFill; temp++)
    	{
    		timesQuestionAsked.add(0);
    	}
    }
    /**
     * Interpret incoming data.
     */
    public void doPost(HttpServletRequest req, HttpServletResponse resp) {
        //attempt to read the incoming string
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
            
            String received = new String(input);
            //interpret the received string
            if(received.equals("getNext"))
            {
            	//new participant- add new session count
            	totalNumberParticipants++;
            	int sessionNumber = whichQuestionToAsk();
            	timesQuestionAsked.set(sessionNumber, timesQuestionAsked.get(sessionNumber)+1);
            	writer.write(Integer.toString(sessionNumber));
            }
            //new survey requested
            else if(received.equals("createNew"))
            {
            	totalNumberParticipants = 0;
            	timesQuestionAsked = new ArrayList<Integer>();
            	
            	for(int temp = 0; temp < numberToFill; temp++)
            	{
            		timesQuestionAsked.add(0);
            	}
            	writer.write("0");
            }
            //write back unrecognized commands- used for debugging
            else
            {
            	writer.write(received);
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
    
    /**
     * Return the number of participants that have taken the survey
     * @return participants
     */
    public int getTotalNumberParticipants() {
		return totalNumberParticipants;
	}
    
    /**
     * Set the number of participants that have taken the survey.
     * @param totalNumberParticipants
     */
	public void setTotalNumberParticipants(int totalNumberParticipants) {
		this.totalNumberParticipants = totalNumberParticipants;
	}
	
	/**
	 * Get the array list of question counts
	 * @return arrayList
	 */
	public ArrayList<Integer> getTimesQuestionAsked() {
		return timesQuestionAsked;
	}
	
	/**
	 * Set the array list of question counts
	 * @param timesQuestionAsked
	 */
	public void setTimesQuestionAsked(ArrayList<Integer> timesQuestionAsked) {
		this.timesQuestionAsked = timesQuestionAsked;
	}
	
	/**
	 * Determine which question should be asked.
	 * @return question
	 */
	public int whichQuestionToAsk()
	{
		int mostAskedSoFar = timesQuestionAsked.get(0);
		for(int tempInt = 0; tempInt < timesQuestionAsked.size(); tempInt++)
		{
			if (timesQuestionAsked.get(tempInt) < mostAskedSoFar)
			return tempInt;
		}
		return 0;
	}
	
	
}

