package edu.kh.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class JDBCExample7 {

	public static void main(String[] args) {

		// EMPLOYEE	테이블에서
		// 사번, 이름, 성별, 급여, 직급명, 부서명을 조회
		// 단, 입력 받은 조건에 맞는 결과만 조회하고 정렬할 것
				
		// - 조건 1 : 성별 (M, F)
		// - 조건 2 : 급여 범위
		// - 조건 3 : 급여 오름차순/내림차순
				
		// [실행화면]
		// 조회할 성별(M/F) : F
		// 급여 범위(최소, 최대 순서로 작성) :
		// 3000000
		// 4000000
		// 급여 정렬(1.ASC, 2.DESC) : 2
				
		// 사번 | 이름   | 성별 | 급여    | 직급명 | 부서명
		//--------------------------------------------------------
		// 218  | 이오리 | F    | 3890000 | 사원   | 없음
		// 203  | 송은희 | F    | 3800000 | 차장   | 해외영업2부
		// 212  | 장쯔위 | F    | 3550000 | 대리   | 기술지원부
		// 222  | 이태림 | F    | 3436240 | 대리   | 기술지원부
		// 207  | 하이유 | F    | 3200000 | 과장   | 해외영업1부
		// 210  | 윤은해 | F    | 3000000 | 사원   | 해외영업1부

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Scanner sc = null;
		
		try {
			
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			String url = "jdbc:oracle:thin:@localhost:1521:XE";
			String userName = "kh";
			String password = "kh1234";
			
			conn = DriverManager.getConnection(url,userName,password);
			
			sc = new Scanner(System.in);
			
			System.out.print("gender(M,F) : ");
			String gender = sc.next().toUpperCase();
			
			System.out.println("salary(min/max) : ");
			int minSal = sc.nextInt();
			int maxSal = sc.nextInt();
			
			System.out.print("order by salary ASC(1) | DESC(2) : ");
			int orderBySort = sc.nextInt();
			String orderBy = "ASC";
			if(orderBySort == 2) orderBy = "DESC";
			
			String sql = """
					select emp_id,emp_name,decode(substr(emp_no,8,1),1,'남',2,'녀') gender,salary,job_name,nvl(dept_title, '-') dept_title
					from employee
					join job using(job_code)
					left join department on(dept_id = dept_code)
					where substr(emp_no,8,1) = ?
					and salary between ? and ?
					order by salary
					""" + orderBy;
						
			pstmt = conn.prepareStatement(sql);
			
			if(gender.equals("M")) pstmt.setInt(1, 1);
			else if(gender.equals("F"))	pstmt.setInt(1, 2);
			else {
				System.out.println("MissMatchedException : gender");
				return;
			}

			pstmt.setInt(2, minSal);
			pstmt.setInt(3, maxSal);
			
			rs = pstmt.executeQuery();
			
			System.out.printf("%-2s | %-6s | %-4s | %-8s | %-5s | %-10s\n","사번","이름","성별","급여","직급","부서");
			System.out.println("------------------------------------------------------------------");
			
			boolean flag = false;
			
			while(rs.next()) { 
				flag = true;
				
				String empId = rs.getString("emp_id");
				String empName = rs.getString("emp_name");
				String empGender = rs.getString("gender");
				int salary = rs.getInt("salary");
				String jobName = rs.getString("job_name");
				String deptTitle = rs.getString("dept_title");
				
				System.out.printf("%-4s | %-6s | %-4s | %-10d | %-5s | %-10s\n",empId,empName,empGender,salary,jobName,deptTitle);
			}
			if(!flag) System.out.println("조회결과 없음");			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs!=null) rs.close();
				if(pstmt!=null) pstmt.close();
				if(conn!=null) conn.close();
				if(sc!=null) sc.close();
				
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
}
