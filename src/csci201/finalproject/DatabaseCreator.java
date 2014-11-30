package csci201.finalproject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.ArrayList;

import com.mysql.jdbc.Statement;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.ibatis.jdbc.ScriptRunner;



public class DatabaseCreator {

	public static final String DB_ADDRESS = "jdbc:mysql://localhost/";
	public static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	public static final String DB_NAME = "BuccBattlesInformation";
	public static final String DRIVER = "com.mysql.jdbc.Driver";
	public static final String USER = "root";
	public static final String PASSWORD = "";
	public static final String SQL_SCRIPT = "BuccBattleInformation.sql";
	private Connection conn = null;
	private Statement stmt = null;
	private final String allAtOnce = "INSERT INTO PlayerInformation(Name, FinalRank, TurnsTaken, ChatMessagesSent, NumShipsDeployed, NumShipsLost, NumShipsSunk, ShotsTaken,SuccessfulShots) VALUE (?,?,?,?,?,?,?,?,?)";
	private java.sql.PreparedStatement everythingAllAtOnce = null;
	
	public DatabaseCreator() {
		
		
		try{
		Class.forName(JDBC_DRIVER);
	    conn = (Connection) DriverManager.getConnection(DB_ADDRESS+DB_NAME, USER, PASSWORD);
	    stmt = (Statement) conn.createStatement();
	    
		ScriptRunner runner=new ScriptRunner(conn);
		InputStreamReader reader = new InputStreamReader(new FileInputStream(SQL_SCRIPT));
		
		runner.runScript(reader);
		}catch(SQLException se){
			System.out.println("Failure creating DatabaseCreator");
			se.printStackTrace();
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
		
		public String getNameByRank(int rank){
			String sql = "SELECT Name, FinalRank FROM  PlayerInformation";
			Statement stm = null;
			
			try {
				 stm = (Statement) conn.createStatement();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			ResultSet rs = null;
			try {
				rs = stm.executeQuery(sql);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			String name = null;
			try {
					while(rs.next())
					{
						int ranking = rs.getInt("FinalRank");
						if(ranking == rank){
							name = rs.getString("Name");
						}
				
					}
				}catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			return name;
		}
		
//		public static void main(String[] args){
//			DatabaseCreator dbTest = new DatabaseCreator();
//			dbTest.addFullRow("thenightman", 1, 5, 5, 5, 5, 5, 5, 5);
//			dbTest.addFullRow("mrjones", 2, 5, 5, 5, 5, 5, 5, 5);
//			System.out.println(dbTest.getNameByRank(1));
//			System.out.println(dbTest.getNameByRank(1));
//			System.out.println(dbTest.getNameByRank(2));
//			System.out.println(dbTest.getNameByRank(1));
//		}
}
