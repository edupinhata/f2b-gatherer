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
import java.util.ArrayList;

public class FileLog{

	/*========================
	 * VARIABLES 
	======================= */
	//PATHS
	private String F2B_LOG = "/var/log/fail2ban.log";
        private String VAR_FILE = "../var_file.txt";

	//
	FileReader fr;
	BufferedReader br;
	FileWriter fw;
	BufferedWriter bw;
	private Line lastLine; //lastLine that is read when file is read.
	private Line firstLine; //first line that is read when file is read. 
	private int countLines; //save in memory the number of lines that
       			// in the lgo file.
	private int numLines; //hold the number of lines that was written
			//in the var file
	private int mac; //identifier to create lines
	
	//constructor
	public FileLog(int mac){
		this.mac = mac;
		update();
	}
	
	//constructor that change the path
	public FileLog(int mac, String F2B_LOG, String VAR_FILE){
		this.mac = mac;
		this.F2B_LOG = F2B_LOG;
		this.VAR_FILE = VAR_FILE;
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
			countLines = counter;				    	
			numLines = getNumLines();

			br.close();

		}catch(Exception e){
			e.printStackTrace();
			System.out.println("Update failed: could not read file.");
		}
		/*finally{
			br.close();
		}*/
		
	}

	/*
	 * getCountNumber: return the countNumber
	 */
	public int getCountLines(){
		return countLines;
	}

	/*
	 * getNumLines
	 * return the variable numLines that is read
	 * from the file.
	 */
	public int getNumLines(){
		return numLines;
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
	 * getLine
	 * Function that return a Line object
	 * given the number of the line of the
	 * log.
	 */
	public Line getLine(int lineNumber){
		try{
			fr = new FileReader(F2B_LOG);
			br = new BufferedReader(fr);

			for(int i=1; i<lineNumber; i++)
				br.readLine();

			Line line = new Line(this.mac, br.readLine());
		
			br.close();

			return line;

		}catch(Exception e){
			e.printStackTrace();
		}
		/*finally{
			br.close();
		}*/
		return null;
	}	

	/*
	 * readNumLine: read the number of lines in VAR_FILE
	 */
	public int readNumLine(){
		String line;
		
		try{
			fr = new FileReader(VAR_FILE);
			br = new BufferedReader(fr);

			//try to read the line. If there is 
			//nothing on the file the function 
			//return 0
			line = br.readLine(); //read the first line

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

			bw.close();

		}catch(Exception e){
			e.printStackTrace();
			System.out.println("Error to access file. It was not possible to access the file to write.");
		}
		/*finally{
			bw.close();
		}*/
	}

	/*
	 * getAllLog
	 * Function that return all the log into
	 * an ArrayList<Line> object
	 */
	public ArrayList<Line> getAllLog(){

		ArrayList<Line> buffer = new ArrayList<Line>();
		String line;

		try{
			fr = new FileReader(F2B_LOG);
			br = new BufferedReader(fr);

			while((line = br.readLine()) != null)
				buffer.add(new Line(this.mac, line));

			br.close();

			return buffer;

		}catch(Exception e){
			e.printStackTrace();
		}
		/*finally{
			br.close();
		}*/
		return null;
	}


	/*
	 * getLinesAfter
	 * Function that return the lines that
	 * must be inserted into the database
	 */
	public ArrayList<Line> getLinesAfter(int lineNum){
		
		ArrayList<Line> buffer = new ArrayList<Line>();
		String line;

		try{
			fr = new FileReader(F2B_LOG);
			br = new BufferedReader(fr);

			for(int i=0; i<lineNum; i++)
				br.readLine();

			while((line = br.readLine()) != null)
				buffer.add(new Line(this.mac, line));

			br.close();

			return buffer;
			
		}catch(Exception e){
			e.printStackTrace();
		}
		/*finally{
			br.close();
		}*/
		return null;
	}

}