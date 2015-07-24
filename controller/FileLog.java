/*
 *FileLog
 *Class that will control the comunication between the 
 * fail2ban.log and the Log.java class.
 *
 * Author: Eduardo Pinhata
 * Init Date: 07/23/2015
 */


package controller;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.BufferedWriter;
import controller.Line;
import java.lang.Exception;

public class FileLog{

	/*========================
	 * VARIABLES 
	======================= */
	//PATHS
	private final String F2B_LOG = "/var/log/fail2ban.log";
        private	final String VAR_FILE = "../var_file.txt";

	//
	FileReader fr;
	BufferedReader br;
	FileWriter fw;
	BufferedWriter bw;
	private Line lastLine; //lastLine that is read when file is read.
	private Line firstLine; //first line that is read when file is read. 
	private int countNumber; //save in memory the number of lines that
       			// in the lgo file.
	private int mac; //identifier to create lines
	
	//constructor
	public FileLog(int mac){
		this.mac = mac;
		update();
	}

	//Methods
	
	

	/*
	 * update:
	 * reads the fail2ban.log and update the lastLine and the 
	 * lineNumber information.
	 */
	public void update(){
		try{
			fr = new FileReader(F2B_LOG);
			br = new BufferedReader(fr);
			int counter = 0; //will count the number of lines
			String line; //will save the lines that are being read.

			while((line = br.readLine()) != null){
				
				//save first line
				if(counter == 0)
					this.firstLine = new Line(mac, line);

				counter++;
			}
			
			//after go through all the lines
			if(counter !=0)
				this.lastLine = new Line(mac, line); // get the last line
			countNumber = counter;				    	

		}catch(Exception e){
			e.printStackTrace();
			System.out.println("Update failed: could not read file.");
		}
		finally{
			br.close();
		}
		
	}

	/*
	 * getCountNumber: return the countNumber
	 */
	public int getCountNumber(){
		return countNumber;
	}

	/*
	 * getNumLine: read the number of lines in VAR_FILE
	 */
	public int getNumLine(){
		try{
			fr = new FileReader(VAR_FILE);
			br = new BufferedReader(fr);

			//try to read the line. If there is 
			//nothing on the file the function 
			//return 0
			line = br.readLine() //read the first line

			if(line != null)
				return Integer.parseInt(line);
			else
				return 0;	

		}catch(Exception e){
			e.printStackTrace();
		}
		//if function has any problem
		return -1; 
	}

	/*
	 * getLastLine: return the LastLine saved.
	 * A null value can be gotten if there are no line
	 * in the log.
	 */
	public Line getLastLine(){
		return lastLine;
	}

	/*
	 * getFirstLine: return the FirstLine saved.
	 */
	public Line getFirstLine(){
		return firstLine;
	}
	
	/*
	 * writeNumLine
	 * Function that writes in VAR_FILE a certain amount of line
	 *
	 */
	public void writeNumLine(int numLines){
		try{
			fw = new FileWriter(VAR_FILE, false);
			bw = new BufferedWriter(fw);

			bw.write(numLines);	
			System.out.println("Wirte in " + VAR_FILE + " " + numLines);

		}catch(Exception e){
			e.printStackTrace();
			System.out.println("Error to access file. It was not possible to access the file to write.");
		}finally{
			bw.close();
		}

	}
	


}
