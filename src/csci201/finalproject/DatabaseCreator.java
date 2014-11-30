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
				 
				e.printStackTrace();
			}
			
			ResultSet rs = null;
			try {
				rs = stm.executeQuery(sql);
			} catch (SQLException e) {
				 
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
					 
					e.printStackTrace();
				}
			
			return name;
		}
		
		public int getTurnsTakenByRank(int rank){
			
			String sql = "SELECT FinalRank, TurnsTaken FROM  PlayerInformation";
			Statement stm = null;
			
			try {
				 stm = (Statement) conn.createStatement();
			} catch (SQLException e) {
				 
				e.printStackTrace();
			}
			
			ResultSet rs = null;
			try {
				rs = stm.executeQuery(sql);
			} catch (SQLException e) {
				 
				e.printStackTrace();
			}
			
			int turnsTaken = 0;
			try {
					while(rs.next())
					{
						int ranking = rs.getInt("FinalRank");
						if(ranking == rank){
							turnsTaken = rs.getInt("TurnsTaken");
						}
				
					}
				}catch (SQLException e) {
					 
					e.printStackTrace();
				}
			
			return turnsTaken;
			
		}
		
		public int getChatsSentByRank(int rank){
			
			String sql = "SELECT FinalRank, ChatMessagesSent FROM  PlayerInformation";
			Statement stm = null;
			
			try {
				 stm = (Statement) conn.createStatement();
			} catch (SQLException e) {
				 
				e.printStackTrace();
			}
			
			ResultSet rs = null;
			try {
				rs = stm.executeQuery(sql);
			} catch (SQLException e) {
				 
				e.printStackTrace();
			}
			
			int chatsSent = 0;
			try {
					while(rs.next())
					{
						int ranking = rs.getInt("FinalRank");
						if(ranking == rank){
							chatsSent = rs.getInt("ChatMessagesSent");
						}
				
					}
				}catch (SQLException e) {
					 
					e.printStackTrace();
				}
			
			return chatsSent;
		}
		
		public int getNumShipsDeployed(int rank){
			String sql = "SELECT FinalRank, NumShipsDeployed FROM  PlayerInformation";
			Statement stm = null;
			
			try {
				 stm = (Statement) conn.createStatement();
			} catch (SQLException e) {
				 
				e.printStackTrace();
			}
			
			ResultSet rs = null;
			try {
				rs = stm.executeQuery(sql);
			} catch (SQLException e) {
				 
				e.printStackTrace();
			}
			
			int shipsDeployed = 0;
			try {
					while(rs.next())
					{
						int ranking = rs.getInt("FinalRank");
						if(ranking == rank){
							shipsDeployed = rs.getInt("NumShipsDeployed");
						}
				
					}
				}catch (SQLException e) {
					e.printStackTrace();
				}
			
			return shipsDeployed;
		}
		
		public int getNumShipsLost(int rank){
			String sql = "SELECT FinalRank, NumShipsLost FROM  PlayerInformation";
			Statement stm = null;
			
			try {
				 stm = (Statement) conn.createStatement();
			} catch (SQLException e) {
				 
				e.printStackTrace();
			}
			
			ResultSet rs = null;
			try {
				rs = stm.executeQuery(sql);
			} catch (SQLException e) {
				 
				e.printStackTrace();
			}
			
			int numShipsLost = 0;
			try {
					while(rs.next())
					{
						int ranking = rs.getInt("FinalRank");
						if(ranking == rank){
							numShipsLost = rs.getInt("NumShipsLost");
						}
				
					}
				}catch (SQLException e) {
					 
					e.printStackTrace();
				}
			
			return numShipsLost;
		}
		
		public int getShotsTaken(int rank){
			String sql = "SELECT FinalRank, ShotsTaken FROM  PlayerInformation";
			Statement stm = null;
			
			try {
				 stm = (Statement) conn.createStatement();
			} catch (SQLException e) {
				 
				e.printStackTrace();
			}
			
			ResultSet rs = null;
			try {
				rs = stm.executeQuery(sql);
			} catch (SQLException e) {
				 
				e.printStackTrace();
			}
			
			int shotsTaken = 0;
			try {
					while(rs.next())
					{
						int ranking = rs.getInt("FinalRank");
						if(ranking == rank){
							shotsTaken = rs.getInt("ShotsTaken");
						}
				
					}
				}catch (SQLException e) {
					 
					e.printStackTrace();
				}
			
			return shotsTaken;
		}
		
		public int getSuccessfulShots(int rank){
			String sql = "SELECT FinalRank, SuccessfulShots FROM  PlayerInformation";
			Statement stm = null;
			
			try {
				 stm = (Statement) conn.createStatement();
			} catch (SQLException e) {
				 
				e.printStackTrace();
			}
			
			ResultSet rs = null;
			try {
				rs = stm.executeQuery(sql);
			} catch (SQLException e) {
				 
				e.printStackTrace();
			}
			
			int successfulShots = 0;
			try {
					while(rs.next())
					{
						int ranking = rs.getInt("FinalRank");
						if(ranking == rank){
							successfulShots = rs.getInt("SuccessfulShots");
						}
				
					}
				}catch (SQLException e) {
					 
					e.printStackTrace();
				}
			
			return successfulShots;
		}
		
		public double getAccuracyPercentage(int rank){
			return (double) ((double)getSuccessfulShots(rank)/(double)getShotsTaken(rank));
		}
		
		public int getNumShipsSunk(int rank){
			String sql = "SELECT FinalRank, NumShipsSunk FROM  PlayerInformation";
			Statement stm = null;
			
			try {
				 stm = (Statement) conn.createStatement();
			} catch (SQLException e) {
				 
				e.printStackTrace();
			}
			
			ResultSet rs = null;
			try {
				rs = stm.executeQuery(sql);
			} catch (SQLException e) {
				 
				e.printStackTrace();
			}
			
			int shipsSunk = 0;
			try {
					while(rs.next())
					{
						int ranking = rs.getInt("FinalRank");
						if(ranking == rank){
							shipsSunk = rs.getInt("NumShipsSunk");
						}
				
					}
				}catch (SQLException e) {
					 
					e.printStackTrace();
				}
			
			return shipsSunk;
		}
	
}
