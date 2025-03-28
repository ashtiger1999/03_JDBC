package edu.kh.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class JDBCExample3 {

	public static void main(String[] args) {
		
		// 입력받은 최소 급여 이상
		// 입력받은 최대 급여 이하를 받는
		// 사원의 사번, 이름, 급여를 급여 내림 차순으로 조회
		// -> 이클립스 콘솔 출력
		
		// [실행화면]
		// 최소 급여 : 1000000
		// 최대 급여 : 3000000
		
		// 사번 / 이름 / 급여
		
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		Scanner sc = null;
		
		try {
			
			// 드라이버 객체 로드
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			// DB연결 정보와 DiverManager를 활용하여 Connection 객체 생성
			String userName = "kh";     // 사용자 계정명
			String password = "kh1234"; // 계정 비밀번호
			
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE",userName,password);
			
			// SQL 작성
			sc = new Scanner(System.in);
			System.out.print("Max Salary : ");
			int max = sc.nextInt();
			System.out.print("Min Salary : ");
			int min = sc.nextInt();
			
			String sql = "select emp_id, emp_name, salary from employee where salary between "+max+" and "+min+" order by salary desc";
			
			// java 13부터 지원하는 text block (""")문법
			// 자동으로 개행 포함 + 문자열 연결이 처리됨
			// 기존처럼 + 연산자로 문자열을 연결할 필요가 없음
			 String sql2 = """
					select emp_id, emp_name, salary
					from employee
					where salary between
					""" + min + " and " + max + " order by salary desc"; 
			
			// Statement 생성
			stmt = conn.createStatement();
			// rs 생성
			rs = stmt.executeQuery(sql2);
			
			while(rs.next()) {
				String empId = rs.getString("emp_id");
				String empName = rs.getString("emp_name");
				int salary = rs.getInt("salary");
				
				System.out.printf("%s / %s / %d\n" ,empId, empName, salary );
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs!=null) rs.close();
				if(stmt!=null) stmt.close();
				if(conn!=null) conn.close();
				if(sc!=null) sc.close();
				
			} catch (Exception e2) {
				e2.printStackTrace();				
			}
		}
	}
}
