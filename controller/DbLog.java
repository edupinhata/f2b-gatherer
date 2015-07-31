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
import java.text.SimpleDateFormat;
import java.util.Calendar;

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
	private String mac;


	//Constructor using configuration file
	public DbLog(String mac, String DB_IP, String DB_PORT,
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
		System.out.println("Getting the last line from database");	
		Line line = null; //will receice the information and will be returned
		cn = MyConnection.getInstance(DB_IP, DB_PORT, DB_USER, DB_PASS).sqlConnection;	
	
		String query = "select * from LOGS WHERE LOG_MAC='" + mac + 
		       "' AND LOG_DATETIME = (SELECT MAX(LOG_DATETIME) FROM LOGS WHERE " + 
	       		"LOG_MAC='" + mac + "')"; 		
		
		Statement stmt = null;
		System.out.println("Query for get last line: " + query);
		try{
			stmt = cn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			if(rs.next()){
				System.out.println(rs.getTimestamp("LOG_DATETIME"));

				line = new Line(mac, rs.getTimestamp("LOG_DATETIME").toLocalDateTime(),
						rs.getString("LOG_RESOURCE"),
						rs.getString("LOG_PID"),
						rs.getString("LOG_STATUS"),
						rs.getString("LOG_ACTION"));


}
			cn.close();
			return line;			

		}catch(Exception e){
			e.printStackTrace();
			System.out.println("It was not possible to get Last Line of DB");
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

		String query = "INSERT INTO LOGS (LOG_MAC, LOG_DATETIME," + 
		" LOG_RESOURCE, LOG_PID, LOG_STATUS, LOG_ACTION) VALUES ('" + 
		line.getMac() + coma +  line.getDateTimeString() + coma + 
	       	line.getResource() + coma + line.getPid() + coma  + line.getStatus() + 
		coma + line.getAction() + "')";
		System.out.println("Query:" + query);	

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
			count++;
		}
		buffer.clear(); //erase it from buffer
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
		String mac = line.getMac();
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
			mac + " AND LOG_DATETIME >= " + line.getDateTimeString();

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
		String query = "SELECT 1 FROM LOGS WHERE LOG_MAC='" + mac +"' LIMIT 1";  //is empty for this specific user

		System.out.println("isEmpty query: " + query);
		try{
			stmt = cn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			if(rs.next()){
				cn.close();
				System.out.println("Database not empty");
				return false;
			}
			else{
				cn.close();
				System.out.println("Database empty");
				return true;
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



