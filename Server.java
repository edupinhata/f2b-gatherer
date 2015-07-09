/*Server file
*Will keep running and reading the f2b log.
*
*Author: Eduardo Pinhata
*Init Date: 07/07/2015
*/


/*
* This class will have some tasks. 
* First, it must to store the last time that the log was update to verify if 
* it was modified.
* It must also to get the last modification and store it to know 
* which line to take. Or it can record the last line.
* Then, it must to write in a file that will be in other computer line to take. Or it can record the last line.
* Then, it must to write in a file that will be in other computer. If
* it is only a file, it'll be necessary to write a program to listen,
* but it's possible to be a MySQL.  
*/
//package gatherer;

import java.io.*;
import java.lang.Exception;
import java.util.ArrayList;

public class Server{

	/*==============================
	*	VARIABLES
	===============================*/
	//PATHS
	private final String F2B_LOG_PATH = "/var/log/fail2ban.log";
	private final String VAR_FILE_PATH = "./var_file.info";
	private final String DB_FILE = "./db_file.db"; //file that will store the db 
						//in the place of MySQl for test purpose	

	//PROGRAM VARIABLES
	FileReader fr;
	FileWriter fw;
	BufferedReader br;
	BufferedWriter bw;
	private boolean changed = false; //variable that detects changes in log
	private int lastLine; //last line read
	private ArrayList<String> lineBuffer = new ArrayList<String>(); //variables that will hold
								//lines not written in database

	/*
	public static void main(String [] args){
		init();

		//main loop
		while(true){
			if(changed){
				//read the lines that were not changed 
				//and insert in mysql	
			
			}	
		}
	}*/


	/*
	* Function that initialize the variables 
	* when the server starts.
	*/
	public void init(){
	
		String line;

		try{	
			//read the variable file
			fr = new FileReader(VAR_FILE_PATH);
			br = new BufferedReader(fr);
		
			if((line = br.readLine()) != null)
				lastLine = Integer.parseInt(line);
			else
				lastLine = 0;	
		
			br.close();

		}catch(Exception e){
			e.printStackTrace();
			System.out.println("Error: Problem to read the variables file.");		
		}
	}


	/*
	* Function: isChanged
	* Function that verifies if the log was modified.
	* It reads the file and compares with the lastLine.
	* At this point, the lastLine should be updated.
	*
	* IMPORTANT: This fucntion is made thinking that the log will not be 
	* deleted. So, if it's possible to the log be erased after some while,
	* this function must be adapted. 
	*/
	public boolean isChanged(){
		
		//variables
		int lineCounting = lastLine;  //variable that hold how many lines are in the file


		if(changed) //if it's alread changed, there is nothing to verify.
			return true;
		else{

			try{
				
				fr = new FileReader(F2B_LOG_PATH);
				br = new BufferedReader(fr);

				//get until the lastLinebefore counting		
				for(int i=0; i<lastLine; i++)
					br.readLine();

				while(br.readLine() != null)
					lineCounting++;
		
				br.close();
						

			}catch(Exception e){
				e.printStackTrace();
				System.out.println("Problem to read the fail2ban log files.");
			}

			//if there is no modification on log, the number of lines will be the same.
			if(lineCounting == lastLine)
				return false;
			else{
				changed = true;
				return true;
			}		
		}	
	}
	

	/*
	* Function: getNewLines
	* Function that will read the fail2ban log and get the new
	* new lines.
	* In this function, the changed and lastLine must be changed
	* accordling with the lines read.
 	*
	*/	
	public void getNewLines(){
		if(isChanged()){
			//add the unread lines to the array list	
			
			try{
				fr = new FileReader(F2B_LOG_PATH);
				br = new BufferedReader(fr);
				String line;
				int countLine = lastLine; //used to define new lastLine

				//get until the line to be readen
				for(int i=0; i<lastLine; i++)
					br.readLine();

				while((line = br.readLine()) != null){
					lineBuffer.add(line);
					countLine++;
				}
					
				lastLine = countLine; 	
				
				br.close();	

			}catch(Exception e){
				e.printStackTrace();
			}		
		}
	}



	/*
	* Function: countLines
	* Description: Function that return the number of lines in a 
	* log file.
	*
	*/
	public int countLines(String logfile){
		try{
			fr = new FileReader(logfile);
			br = new BufferedReader(fr);

			int counter = 0;
			
			while(br.readLine() != null)
				counter++;

			br.close();
			return counter;
			
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("Error: problem to open " + logfile);
			return -1;
		}		
	}


	/*
	* Function: flushLines
	* Function that will write the new lines to MySQL
	* If the action of write in the MySQL is done, the lines in the 
	* lineBuffer must be erased. And the line in the file VAR_FILE_PATH 
	*  must be changed.
	*
	* First test will be made in a file
	*/	
	public void flushLines(){
		
		try{
		
			fw = new FileWriter(DB_FILE, true);
			bw = new BufferedWriter(fw);

			for(int i=0; i<lineBuffer.size();i++){
				bw.write(lineBuffer.get(i));
				bw.newLine();
			}	

			//if the code got in this part, it means that it wrote 
			//on the file.
			lineBuffer.clear();

			bw.close();

		}catch(Exception e){
			e.printStackTrace();
			System.out.println("Error: problem to read the database file.");
		}

		//write the last line on the file

		if(changed){
			try{
				fw = new FileWriter(VAR_FILE_PATH);
				bw = new BufferedWriter(fw);

				bw.write(countLines(F2B_LOG_PATH)); //write number of lines

				bw.close();				
				

			}catch(Exception e){
				e.printStackTrace();
				System.out.println("Error to read/write in " + VAR_FILE_PATH);	
			}
		}
	} 



}	

