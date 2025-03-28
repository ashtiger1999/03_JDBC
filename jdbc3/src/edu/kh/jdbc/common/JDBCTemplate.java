package edu.kh.jdbc.common;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class JDBCTemplate {
	private static Connection conn = null;
	public static Connection getConnection() {
		try {
			if(conn!=null&&!conn.isClosed()) return conn;
			Properties prop = new Properties();			
			String filePath = "driver.xml";
			FileInputStream fis = new FileInputStream(filePath);			
			prop.loadFromXML(fis);			
			Class.forName(prop.getProperty("driver"));			
			conn = DriverManager.getConnection(
					prop.getProperty("url"),prop.getProperty("userName"),prop.getProperty("password"));
			conn.setAutoCommit(false);			
		} catch (Exception e) {e.printStackTrace();}
		return conn;
	}	
	public static void commit(Connection conn) {		
		try {			
			if(conn!=null&&!conn.isClosed()) conn.commit();			
		} catch (Exception e) {e.printStackTrace();}
	}	
	public static void rollback(Connection conn) {		
		try {			
			if(conn!=null&&!conn.isClosed()) conn.rollback();			
		} catch (Exception e) {e.printStackTrace();}		
	}
	public static void close(Connection conn) {
		try {
			if(conn!=null&&!conn.isClosed()) conn.close();
		} catch (Exception e) {e.printStackTrace();}
	}
	public static void close(Statement stmt) {
		try {
			if(stmt!=null&&!stmt.isClosed()) stmt.close();
		} catch (Exception e) {e.printStackTrace();}
	}
	public static void close(PreparedStatement pstmt) {
		try {
			if(pstmt!=null&&!pstmt.isClosed()) pstmt.close();
		} catch (Exception e) {e.printStackTrace();}
	}
	public static void close(ResultSet rs) {
		try {
			if(rs!=null&&!rs.isClosed()) rs.close();
		} catch (Exception e) {e.printStackTrace();}
	}
}
