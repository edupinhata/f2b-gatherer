/*
* Line
* Class that will be the basic structure that will have the 
* information from one line of the log.
*
*
*/

package controller;

import controller.Line;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.net.NetworkInterface;
import java.net.InetAddress;

public class Line{


	//Attributes
	LocalDateTime dateTime;		//new version from Calendar. Last version to user time on java
	private String resource;	//e.g. fail2ban.action || fail2ban.filter
				//folders that have a filter or action when banning
	private String pid;	//Process id that is running the fail2ban
	private String status; //e.g. INFO || NOTICE

	private String action;	//e.g. information of what happened - NOTICE has the ban and unban info

	private String mac;  //indentify uniquely the computer that wrote the log.



	//Constructor
	public Line(String mac, String date, String time, String resource, String pid,  String status, String action){
		
		this.mac = mac;
		String [] splitDate = date.split("-");
		String [] splitTime = time.split(":|,");
		
		//converts the date and time into the object
		this.dateTime = LocalDateTime.of(Integer.parseInt(splitDate[0]),
			Integer.parseInt(splitDate[1]),
			Integer.parseInt(splitDate[2]),
			Integer.parseInt(splitTime[0]),
			Integer.parseInt(splitTime[1]),
			Integer.parseInt(splitTime[2]),
			Integer.parseInt(splitTime[3]));

		this.resource = resource;
		this.pid = pid;
		this.status = status;
		this.action = action;

		

	}

	//Constructor splitting the String log. (Easier to use in server)
	public Line(String mac, String line){
		String [] lineSplit = line.trim().split("\\s+");
		String [] splitDate = lineSplit[0].split("-");
		String [] splitTime = lineSplit[1].split(":|,");
		
		//converts the date and time into the object
		this.dateTime = LocalDateTime.of(Integer.parseInt(splitDate[0]),
			Integer.parseInt(splitDate[1]),
			Integer.parseInt(splitDate[2]),
			Integer.parseInt(splitTime[0]),
			Integer.parseInt(splitTime[1]),
			Integer.parseInt(splitTime[2]),
			Integer.parseInt(splitTime[3])*1000000);

		this.resource = lineSplit[2]; 
		this.pid = lineSplit[3].replaceAll("\\[|\\]|:", "");
		this.status = lineSplit[4];
		
		//put the last parts from log in the action
		//separated by space
		this.action = "";
		for(int i=5; i<lineSplit.length; i++)
			this.action += lineSplit[i] + " ";

		this.action = this.action.replace("'", "").trim();
	}



	//Setters and Getters
	//mac
	public String getMac(){
		return mac;
	}

	//get the LocalDateTime object
	public LocalDateTime getDateTime(){
		return dateTime;
	}
	//set LocalDateTime itself
	public void setDateTime(LocalDateTime newDateTime){
		this.dateTime = newDateTime;
	}
	

	//date: return the date in a way that can
	//be saved on the database
	public String getDate(){
		
		DateTimeFormatter dTF = DateTimeFormatter.ofPattern("YYY-MM-dd");
		return dateTime.format(dTF);

		/*String date = "";

		date += dateTime.YEAR + "-";
		date += dateTime.MONTH + "-";
		date += dateTime.DAY_OF_MONTH; 	
	
		return date;*/ 
	}
	//set date using format yyyy-mm-dd 
	public void setDate(String newDate){
		int [] date = stringToIntSplit(newDate, "-");
		
		this.dateTime = LocalDateTime.of(date[0], date[1], date[2], 
						dateTime.getHour(), dateTime.getMinute(),
						dateTime.getSecond(), dateTime.getNano());
	}
	
	//Time: return the time in a way that can
	//be saved in the database
	public String getTime(){
		DateTimeFormatter dTF = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
		return dateTime.format(dTF);
	}
	public void setTime(String newTime){
		int [] time = stringToIntSplit(newTime, ":|,");
	
		this.dateTime = LocalDateTime.of(dateTime.getYear(), dateTime.getMonth(),
					dateTime.getDayOfMonth(), time[0], time[1],
					time[2], time[3]);
	}
	
	//Resource
	public String getResource(){
		return resource;
	}
	public void setResource(String newResource){
		resource = newResource;
	}

	//pid
	public String getPid(){
		return pid;
	}
	public void setPid(String newPid){
		pid = newPid;
	}

	//status
	public String getStatus(){
		return status;
	}
	public void setStatus(String newStatus){
		status = newStatus;
	}

	//action
	public String getAction(){
		return action;
	}
	public void setAction(String newAction){
		action = newAction;
	}



	
	//Methods


	//Date management

	/*stringToIntSplit: Function that transform a String into an int array
	*	Example: text = "2015-08-12"
	*    		the function stringToIntSplit(text, "-")
	*		will return an array containing:
	*		array[0] = 2015
	*		array[1] = 8
	*		array[2] = 12
	*/
	public int[] stringToIntSplit(String text, String splitter){
		String [] textStringArray = text.split(splitter);
		int [] textIntArray = new int[textStringArray.length];
		
		try{
			for(int i=0; i<textStringArray.length; i++)
				textIntArray[i] = Integer.parseInt(textStringArray[i]);
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("Error: the log line doesn't has the usual date and time start. Verify the log line: " + text);
		}		


		return textIntArray;
	}

	


	/*
	* compareDate: compare the date and time.
	* 
	* Input: String containing date. Format: yyyy-mm-dd
	*	String containing time. Format: hh:mm:ss
	*
	* Output: -1 if date and time is smaller than the object date
	*	   0 if date and time is equal
	*	   1 if date and time is bigger 
	*/	

	public int compareDate(String date, String time){
		int [] dateArray = stringToIntSplit(date, "-");
		int [] timeArray = stringToIntSplit(time, ":|,|.");

		//create a calendar object
		LocalDateTime lDT = LocalDateTime.of(dateArray[0], dateArray[1], dateArray[2],
			timeArray[0], timeArray[1], timeArray[2], timeArray[3]);


		//compare
		if(dateTime.isAfter(lDT))
			return 1;
		else if(dateTime.isBefore(lDT))
			return -1;
		else
			return 0;
	}	

	/*
	* compareDate: compare the date and time using an Line as argument
	*
	*
	*
	*
	*/
	public int compareDate(Line lineToCompare){
		if(dateTime.isAfter(lineToCompare.getDateTime()))
			return 1;
		else if(dateTime.isBefore(lineToCompare.getDateTime()))
			return -1;
		else
			return 0;
	}
	
}
