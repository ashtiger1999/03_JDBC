package edu.kh.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class JDBCExample4 {

	public static void main(String[] args) {
		
		// 부서명을 입력받아
		// 해당 부서에 근무하는 사원의
		// 사번 이름 부서명 직급명을
		// 직급 코드 오름차순으로 조회
		
		// [실행화면]
		// 부서명 입력 : 총무부
		
		// 부서명 입력 : 개발팀
		// 일치하는 부서가 없습니다.
		
		// hint : SQL에서 문자열은 양쪽 '' 필요
		
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		Scanner sc = null;
		
		try {
			
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE","kh","kh1234");
			
			stmt = conn.createStatement();
			
			sc = new Scanner(System.in);
			System.out.print("Dept. : ");
			String input = "'"+sc.next()+"'";
			
			String sql = "select emp_id,emp_name,dept_title,job_name from employee join job using(job_code) left join department on(dept_id = dept_code) where dept_title = "+input+" order by job_code";
			
			rs = stmt.executeQuery(sql);
			/*
			// flag 활용하기
			boolean flag = false;
			
			while(rs.next()) {
				String empId = rs.getString("emp_id");
				String empName = rs.getString("emp_name");
				String deptTitle = rs.getString("dept_title");
				String jobName = rs.getString("job_name");
				
				if(deptTitle != null) {
					flag =true;
				}
				
				System.out.printf("%s / %s / %s / %s\n",empId,empName,deptTitle,jobName);
			}
			if(flag ==false) System.out.println("no dept.");
			*/
			
			if(!rs.next()) {
				System.out.println("no dept.");
				return;
			}
			do {
				String empId = rs.getString("emp_id");
				String empName = rs.getString("emp_name");
				String deptTitle = rs.getString("dept_title");
				String jobName = rs.getString("job_name");
				
				System.out.printf("%s / %s / %s / %s\n",empId,empName,deptTitle,jobName);
			} while(rs.next());
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		
		
		
	}

}
