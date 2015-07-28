/* Server.java
*
*
*Author: Eduardo Pinhata
*Init Date: 07/28/2015
*/


/*
* Many configurations will be setted on the beggining.
* After, there is a main loop that will update the Log status and 
* get the information about the fail2ban.log.
*
*/

import controller.Log;

public class Server{

	public static void main(String [] args){	

		Log log = new Log();

		while(true){
			log.isChanged();
			try{
				Thread.sleep(4000);
			}catch(Exception e){
				e.printStackTrace();
			}	
		}
	}
}	

