package edu.kh.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Scanner;

public class JDBCExample6 {

	public static void main(String[] args) {

		// 아이디, 비밀번호, 이름을 입력받아
		// 아이디, 비밀번호가 일치하는 사용자의 이름을 수정(UPDATE)
		
		// 1. PreparedStatement 이용하기
		// 2. commit/rollback 처리하기
		// 3. 성공시 수정성공, 실패시 아이디 또는 비밀번호가 틀렸습니다.
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		Scanner sc = null;
		
		try {
			
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			String type = "jdbc:oracle:thin:@";// 드라이버의 종류
			String host = "localhost"; // DB 서버 컴퓨터의 IP 또는 도메인 주소
			String port = ":1521"; // 프로그램 연결을 위한 port 번호
			String dbName = ":XE"; // BDMS 이름 (XE == eXpress Edition)

			String userName = "kh";     // 사용자 계정명
			String password = "kh1234"; // 계정 비밀번호
			
			conn = DriverManager.getConnection(type+host+port+dbName,userName,password);
			
			conn.setAutoCommit(false);
			
			sc = new Scanner(System.in);
			System.out.print("id : ");
			String id =sc.next();
			
			System.out.print("pw : ");
			String pw =sc.next();
			
			System.out.print("name : ");
			String name =sc.next();
			
			String sql ="""
					update tb_user set
					user_name = ?
					where user_id = ?
					and user_pw = ? 
					""";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, name);
			pstmt.setString(2, id);
			pstmt.setString(3, pw);
			
			int result = pstmt.executeUpdate();
			
			if(result>0) {
				System.out.println("update success");
				conn.commit();
			} else {
				System.out.println("id || pw is not matched");
				conn.rollback();
			}	
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(pstmt!=null)pstmt.close();
				if(conn!=null)conn.close();
				if(sc!=null)sc.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}	
	}
}
