/*
 * DbLog
 * Class that will control the comunication with de database.
 *
 * Author: Eduardo Pinhata
 * Init Date: 07/24/2015
 */

package controller;

import connection.MyConnection;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class DbLog{


	/*============================
	 * Variables
	 =============================*/ 
	//PATHS
	private String DB_IP;
	private String DB_PORT;
	private String DB_USER;
	private String DB_PASS;

	//
	Connection cn;
	private int mac;


	//Constructor using configuration file
	public DbLog(int mac, String DB_IP, String DB_PORT,
			String DB_USER,String DB_PASS){
		this.mac = mac;
		this.DB_IP = DB_IP;
		this.DB_PORT = DB_PORT;
		this.DB_USER = DB_USER;
		this.DB_PASS = DB_PASS;
	}

	//Methods

	/*
	 * getLastLine
	 * Function that retrieve the last line for a 
	 * specific user from the database.
	 */ 
	public Line getLastLine(){
		
		Line line = null; //will receice the information and will be returned
		cn = MyConnection.getInstance(DB_IP, DB_PORT, DB_USER, DB_PASS).sqlConnection;	
	
		String query = "select * from LOGS WHERE LOG_MAC=" + mac + 
		       " AND LOG_DATE = (SELECT MAX(LOG_DATE) FROM LOGS WHERE " + 
	       		"LOG_MAC=" + mac + " AND LOG_TIME= (SELECT MAX(TIME) FROM " +
	 	"LOGS WHERE LOG_MAC=" + mac + "))"; 		
		
		Statement stmt = null;

		try{
			stmt = cn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			if(rs.next())
				line = new Line(mac, rs.getDate("LOG_DATE").toString(),
						rs.getTime("LOG_TIME").toString(),
						rs.getString("LOG_RESOURCE"),
						rs.getString("LOG_PID"),
						rs.getString("LOG_STATUS"),
						rs.getString("LOG_ACTION"));
			cn.close();
			return line;			

		}catch(Exception e){
			e.printStackTrace();
		}
		/*finally{
			cn.close(); //close the connection with db
		}*/
		return null;
	}

	/*
	 * insertLine
	 * Function that allow to insert a single line in the database.
	 *
	 */
	public void insertLine(Line line){
		cn = MyConnection.getInstance(DB_IP, DB_PORT, DB_USER, DB_PASS).sqlConnection;	
		Statement stmt;
		String coma = "', '";

		String query = "INSERT INTO LOGS (LOG_MAC, LOG_DATE," + 
		"LOG_TIME, LOG_RESOURCE, LOG_PID, LOG_STATUS, LOG_ACTION) VALUE ('" + 
		line.getMac() + coma +  line.getDate() + coma +  line.getTime()+
	       	line.getResource() + coma + line.getPid() + coma  + line.getStatus() + 
		coma + line.getAction() + "')";	

		try{
			stmt = cn.createStatement();
			stmt.executeUpdate(query);

			cn.close();

		}catch(Exception e){
			e.printStackTrace();
			System.out.println("Error: could not insert line in the database.");
		}
		/*finally{
			cn.close();
		}*/
	}


	/*
	 * flushLines
	 * Receive an ArrayList of Line and record it
	 * on database.
	 * Return the number of lines that was inserted.
	 */
	public int flushLines(ArrayList<Line> buffer){
		int count = 0;
		
		for(Line line : buffer){
			insertLine(line); //add line in database
			buffer.remove(line); //erase it from buffer
			count++;
		}
		return count;
	}

	/*
	 * findLine
	 * Given one object Line, find it on database
	 * If found, return true, else, return false.
	 *
	 */
	public boolean findLine(Line line){
		//get all the information to create a pk 
		//from a line
		int mac = line.getMac();
		String date = line.getDate();
		String time = line.getTime();

		//make a query to search for line
		cn = MyConnection.getInstance(DB_IP, DB_PORT, DB_USER, DB_PASS).sqlConnection;	

		Statement stmt = null;
		String query = "SELECT * FROM LOGS WHERE LINE=" + mac + date + time;

		try{
			stmt = cn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			if(rs.next())
				return true;

		}catch(Exception e){
			e.printStackTrace();
		}

		return false; //if there is no line, return false
	}

	/*
	 * countLinesAfter
	 * Count how many lines there are after a
	 * specific line, considering the own line.
	 *
	 * Can return 0 if didn't find the line.
	 */
	public int countLinesAfter(Line line){
		int linesNumber; //will hold the number of lines 
		cn = MyConnection.getInstance(DB_IP, DB_PORT, DB_USER, DB_PASS).sqlConnection;	
		Statement stmt = null;
		String query = "SELECT COUNT(*) FROM LOGS WHERE LOG_MAC=" + 
			mac + " AND LOG_DATE >= " + line.getDate() + 
			" AND LOG_TIME >= " + line.getTime();

		try{
			stmt = cn.createStatement();
			ResultSet rs = stmt.executeQuery(query);		
			if(rs.next())
				return rs.getInt(1);		

			cn.close();
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("Error: it was not possible to count the number of lines.");
		}
		/*finally{
			cn.close();
		}*/
		return 0;
	}

	/*
	 * isEmpty
	 * Function that verifies if the database is empty
	 * or not.
	 */
	public boolean isEmpty(){
		int linesNumber;
		cn = MyConnection.getInstance(DB_IP, DB_PORT, DB_USER, DB_PASS).sqlConnection;	
		Statement stmt = null;
		String query = "SELECT 1 FROM LOGS LIMIT 1";

		try{
			stmt = cn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			if(rs.next()){
				cn.close();
				return true;
			}
			else{
				cn.close();
				return false;
			}	

		}catch(Exception e){
			e.printStackTrace();
		}
		/*finally{
			cn.close();
		}*/
		return false;	
	}


}



