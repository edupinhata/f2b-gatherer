/*
* Log
* Class that will use the class FileLog and DbLog
* to build the methods used by the Server, that will allow
* to synchronize the database of fail2ban.
*/

package controller;

import controller.FileLog;
import controller.DbLog;
import controller.Line;
import config.Config;
import java.util.ArrayList;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.nio.ByteBuffer;

public class Log{


	//attributes
	ArrayList<Line> buffer = new ArrayList<Line>(); 
	private String mac; //variable that will hold the unique machine identifier.
	FileLog fl;
	DbLog dl;	
	Config conf;

	//constructor
	public Log(){
		/* 
		* The MAC addess will be into integer format
		*/
		InetAddress ip;

		try{
			ip = InetAddress.getLocalHost();

			NetworkInterface network = NetworkInterface.getByInetAddress(ip);

			byte [] mac = network.getHardwareAddress();
			int macInt = 0;    //contain the bytes in int


			/*for(int i=0; i<mac.length; i++)
				macInt += (10^i)*mac[i].intValue();			
			
			this.mac = macInt;*/

			StringBuilder sb = new StringBuilder();
			for(int i=0; i<mac.length; i++)
				sb.append(String.format("%02X%s", mac[i], (i<mac.length-1) ? "-" : ""));

			this.mac = sb.toString(); 


		}catch(Exception e){
			e.printStackTrace();
			System.out.println("Error: Failed into get the MAC address.");
		}

		//initiating the class that will read the 
		//config file and get the configuration.
		conf = new Config();		


		//create the objects that will control the 
		//comunication with log and database
		dl = new DbLog(this.mac, conf.getIp(), conf.getPort(), conf.getUser()
				, conf.getPassword());
		fl = new FileLog(this.mac, conf.getF2bLog(),conf.getGatherer(),  conf.getVarFile());
	}

	//METHODS

	/*
	* isChanged
	* Function that will verify if the fail2ban.log was modified
	* and return TRUE|FALSE based if there is any modification.
	* This function also trigger the FileLog stats update, 
	* reneweeing the LastLine and coutnLines.
	*/
	public boolean isChanged(){
		fl.update();		
		int numLines = fl.getNumLines();
		int countLines = fl.getCountLines();
		Line lastLine = fl.getLastLine();

		/*System.out.println("Number of lines: " + numLines);
		System.out.println("Count lines: " + countLines);
		System.out.println("Db Last line date: " + dl.getLastLine().getDate());
		System.out.println("File last line date: " + fl.getLine(1));
	*/	
		if(numLines == countLines)
			if(!dl.isEmpty() && 
			dl.getLastLine().compareDate(fl.getLine(numLines))==0)
				return false;
			else{
				flushAll();
				return true;
			}
		else if(numLines < countLines)
			if(!dl.isEmpty() &&
			dl.getLastLine().compareDate(fl.getLine(numLines))==0){
				updateLines();
				return true;
			}
			else{
				flushAll();
				return true;
			}
		else if(numLines==0 && countLines==0)
			return false;
		else if(numLines==0){
			updateLines();
			return true;
		}
		else{
			fl.writeNumLine(0);
			isChanged();	
		}
		return false;	
	}


	/*
	 * flushAll
	 * Function that will take all the current log 
	 * into f2b.log and will try to insert into 
	 * database. There are some situations that
	 * it'll try to insert some duplicated lines.
	 * The database must avoid to insert these 
	 * duplicated lines. 
	 *
	 * It will write in VAR_FILE the number the lines
	 * inserted once it reinserted everything.
	 */
	public void flushAll(){
		int numLines;
		
		numLines = dl.flushLines(fl.getAllLog());

		fl.writeNumLine(numLines);
	}


	/*
	* updateLines
	* function that get the lines that are not
	* updated and update them int he database.
	* 
	*/
	public void updateLines(){

		//Fill buffer with lines after numLines
		//flush this buffer in the database
		buffer = fl.getLinesAfter(fl.getNumLines());
		dl.flushLines(buffer);
		fl.writeNumLine(fl.getCountLines());
	}
	

}
