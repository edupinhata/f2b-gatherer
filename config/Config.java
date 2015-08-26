/*
 * Config.java
 *
 * Class that will read the config file and will hold the information
 *
 *
 */

package config;

import java.io.FileReader;
import java.io.BufferedReader;

public class Config{
	
	//Create variables attributes with defalt values
	private String DB_IP = "localhost";
	private String DB_PORT = "8889";
	private String DB_USER = "root";
	private String DB_PASS = "root";
	private String F2B_LOG = "/var/log/fail2ban.log";
	private String GATHERER_LOG = "./gatherer.txt";
	private String VAR_FILE = "./var_file.info";
	private String CONFIG_FILE = "./paths.conf";

	FileReader fr;
	BufferedReader br;

	//Constructor
	public Config(){
		readFile();				
	}


	//Getters
	public String getIp(){
		return DB_IP;}

	public String getPort(){
		return DB_PORT;}

	public String getUser(){
		return DB_USER;}

	public String getPassword(){
		return DB_PASS;}

	public String getF2bLog(){
		return F2B_LOG;}

	public String getGatherer(){
		return GATHERER_LOG;}

	public String getVarFile(){
		return VAR_FILE;}


	//Methods
	
	/*
	 * defineString
	 * Function that classify all possible values read in the config file.
	 */
	public void defineString(String option, String value){
		if(option.equals("db_ip")){
			System.out.println("Loading following ip: " + value);
			DB_IP = value;
		}	
		else if(option.equals("db_port"))
			DB_PORT = value;
		else if(option.equals("db_user"))
			DB_USER = value;
		else if(option.equals("db_pass"))
			DB_PASS = value;
		else if(option.equals("f2b_log"))
			F2B_LOG = value;
		else if(option.equals("var_file"))
			VAR_FILE = value;
		else
			System.out.println("Error: option " + option + " is not a valid config.");	
	}

	/*
	 * readFile
	 * Function that will read the file and use 
	 * defineString on it
	 */	
	public void readFile(){
		try{
			fr = new FileReader(CONFIG_FILE);
			br = new BufferedReader(fr);
			String line = "";
			String [] lineArray;

			while((line = br.readLine()) != null){
				lineArray = line.split("=");
				defineString(lineArray[0], lineArray[1]); 
			}
		
			br.close();

		}catch(Exception e){
			e.printStackTrace();
		}
		/*finally{
			br.close();
		}*/
	}
}
