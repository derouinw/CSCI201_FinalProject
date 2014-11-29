package csci201.finalproject;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.ArrayList;

//import org.apache.ibatis.jdbc.ScriptRunner;


//import org.apache.ibatis.jdbc.ScriptRunner;



public class DatabaseCreator {

	public static final String DB_ADDRESS = "jdbc:mysql://localhost/";
	public static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	public static final String DB_NAME = "BuccaneerBattles";
	public static final String DRIVER = "com.mysql.jdbc.Driver";
	public static final String USER = "root";
	public static final String PASSWORD = "";
	private Connection conn = null;
	private Statement stmt = null;
	private final String allAtOnce = "INSERT INTO PlayerInformation(Name, FinalRank, TurnsTaken, ChatMessagesSent, NumShipsDeployed, NumShipsLost, NumShipsSunk, ShotsTaken,SuccessfulShots) VALUE (?,?,?,?,?,?,?,?,?)";
	private java.sql.PreparedStatement everythingAllAtOnce = null;
	
	public DatabaseCreator() {
		
		
		try{
		Class.forName(JDBC_DRIVER);
		
		conn = DriverManager.getConnection(DB_ADDRESS, USER, PASSWORD);
		stmt = conn.createStatement();
		
		 //conn = DriverManager.getConnection(DB_URL, USER, PASS);

	      //STEP 4: Execute a query
	      System.out.println("Creating database...");
	     // stmt = conn.createStatement();
	      
	      String sql = "CREATE DATABASE "+DB_NAME;
	      stmt.executeUpdate(sql);
	      
	      conn = DriverManager.getConnection(DB_ADDRESS+DB_NAME, USER,PASSWORD);
		
		//ScriptRunner runner=new ScriptRunner(conn);
		InputStreamReader reader = new InputStreamReader(new FileInputStream("BuccBattleInformation.sql"));
		
		//runner.runScript(reader);
		}catch(SQLException se){
			System.out.println("Failure creating DatabaseCreator");
		}catch(FileNotFoundException fe){
			System.out.println(".sql script not found");
		}catch(ClassNotFoundException ce){
			System.out.println("Class not found exception");
			ce.printStackTrace();
		}
		
		//String sql = "CREATE DATABASE "+DB_NAME;
		
		//stm.executeUpdate(sql);
//		}catch(SQLException se){
//		      //Handle errors for JDBC
//		      se.printStackTrace();
//		   }catch(Exception e){
//		      //Handle errors for Class.forName
//		      e.printStackTrace();
//		   }finally{
//		      //finally block used to close resources
//		      try{
//		         if(stmt!=null)
//		            stmt.close();
//		      }catch(SQLException se2){
//		      }// nothing we can do
//		      try{
//		         if(conn!=null)
//		            conn.close();
//		      }catch(SQLException se){
//		         se.printStackTrace();
//		      }//end finally try
//		   }//end try
//		   System.out.println("Goodbye!");

		
	}
		
		public void addFullRow(String username,  int finalPlace, int turnsTaken, int messagesSent, int totalShipsDeployed, int totalShipsLost, int totalShipsSunk, int totalShotsTaken, int totalSuccessfulShots ){
			
			try{
			everythingAllAtOnce = conn.prepareStatement(allAtOnce);
			
			everythingAllAtOnce.setString(1, username);
			everythingAllAtOnce.setInt(2, finalPlace);
			everythingAllAtOnce.setInt(3, turnsTaken);
			everythingAllAtOnce.setInt(4, messagesSent);
			everythingAllAtOnce.setInt(5, totalShipsDeployed);
			everythingAllAtOnce.setInt(6, totalShipsLost);
			everythingAllAtOnce.setInt(7, totalShipsSunk);
			everythingAllAtOnce.setInt(8, totalShotsTaken);
			everythingAllAtOnce.setInt(9, totalSuccessfulShots);
			
			everythingAllAtOnce.execute();
			}
			catch(SQLException se){
				System.out.println("Failuring adding a full row.");
			}
		}
}
