/*Server file
*Will keep running and reading the f2b log.
*
*Author: Eduardo Pinhata
*Init Date: 07/07/2015
*/


/*
* This server will have some tasks. 
* First, it must to store the last time that the log was update to verify if 
* it was modified.
* It must also to get the last modification and store it to know 
* which line to take. Or it can record the last line.
* Then, it must to write in a file that will be in other computer line to take. Or it can record the last line.
* Then, it must to write in a file that will be in other computer. If
* it is only a file, it'll be necessary to write a program to listen,
* but it's possible to be a MySQL.  
*/
import java.io.*;



public class server{

	/*==============================
	*	VARIABLES
	===============================*/
	//PATHS
	public String final F2B_LOG_PATH = "/var/log/fail2ban.log";
	public String final VAR_FILE_PATH = "./var_file.info";
	

	//PROGRAM VARIABLES
	FileReader fr;
	FileWriter fw;
	BufferedReader br;
	BufferedWriter bw;
	private boolean changed = false; //variable that detects changes in log
	private int lastLine; //last line read
	


	public static void main(String [] args){
		System.out.println("Hello world!");
	}


	/*
	* Function that initialize the variables 
	* when the server starts.
	*/
	public void init(){
		//read the variable file
		fr = new FileReader(VAR_FILE_PATH);
		br = new BufferedReader(fr);
		
		if(br.next()){
			lastLine = Integer.ParseInt(br.readLine());
		}
		else
			lastLine = 0;	
	}


}

