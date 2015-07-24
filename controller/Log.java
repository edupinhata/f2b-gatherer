/*
* Log
* Class that will use the class FileLog and DbLog
* to build the methods used by the Server, that will allow
* to synchronize the database of fail2ban.
*/

package controler;

import controler.FileLog;
import controler.DbLog;
import controler.Line;
import java.util.ArrayList;
import java.net.InetAddress;
import java.net.NetworkInterface;


public class Log{


	//attributes
	boolean changed; //variable that verify if the Log was modified.
	ArrayList<Line> buffer = new ArrayList<Line>(); private int mac; //variable that will hold the unique machine identifier.

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


			for(int i=0; i<mac.length; i++)
				macInt += (10^i)*mac[i];			
			
			this.mac = macInt;

		}catch(Exception e){
			e.printStackTrace();
			System.out.println("Error: Failed into get the MAC address.");
		}
	}

	//methods

	/*
	* isChanged
	* Function that will verify if the fail2ban.log was modified
	* and return TRUE|FALSE based if there is any modification.
	* This function also trigger the FileLog stats update, 
	* reneweeing the LastLine and coutnLines.
	*/
	public boolean isChanged(){
	
		return false;

	}

	/*
	* updateLines
	* function that get the lines that are not
	* updated and update them int he database.
	* 
	*/
	public void updateLines(){
	
	}

	

}
