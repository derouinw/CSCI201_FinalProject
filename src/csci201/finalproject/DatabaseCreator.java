package csci201.finalproject;
import java.sql.*;

public class DatabaseCreator {

	public static final String DB_ADDRESS = "jdbc:mysql://localhost/";
	public static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	public static final String DB_NAME = "BuccaneerBattles";
	public static final String DRIVER = "com.mysql.jdbc.Driver";
	public static final String USER = "root";
	public static final String PASSWORD = "";
	
	public DatabaseCreator() {
		
		Connection conn = null;
		Statement stmt = null;
		
		try{
		Class.forName(JDBC_DRIVER);
		conn = DriverManager.getConnection(DB_ADDRESS, USER, PASSWORD);
		
		stmt = conn.createStatement();
		
		String sql = "CREATE DATABASE "+DB_NAME;
		
		stmt.executeUpdate(sql);
		}catch(SQLException se){
		      //Handle errors for JDBC
		      se.printStackTrace();
		   }catch(Exception e){
		      //Handle errors for Class.forName
		      e.printStackTrace();
		   }finally{
		      //finally block used to close resources
		      try{
		         if(stmt!=null)
		            stmt.close();
		      }catch(SQLException se2){
		      }// nothing we can do
		      try{
		         if(conn!=null)
		            conn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }//end finally try
		   }//end try
		   System.out.println("Goodbye!");

		
	}
}
