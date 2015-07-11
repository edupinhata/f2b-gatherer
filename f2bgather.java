/*
* Fail2ban gatherer
*
*Author: Eduardo Pinhata
*Init Date: 07/08/2015
*
*/

/*
* This class will use the server.java class and run an infinit loop
* to monitor the fail2ban log.
*
*
*/

//import Server;



public class f2bgather{


	public static void main(String [] args){

		Server sv = new Server();

		//initialize the server
		System.out.println("Initializing the server...");
		sv.init();
		System.out.println("Server initialized!");

		while(true){
			if(sv.isChanged()){
				sv.getNewLines();
				sv.flushLines();
			}
		try{
			Thread.sleep(4000);		
		}catch(Exception e){
			e.printStackTrace();
		}
		}
	}
}
