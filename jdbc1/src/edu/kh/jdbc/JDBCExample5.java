package edu.kh.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Scanner;

public class JDBCExample5 {

	public static void main(String[] args) {
		
		// 아이디, 비밀번호, 이름을 입력받아
		// TB_USER 테이블에 삽입(INSERT) 하기

		/*
		 * java.sql.PreparedStatment
		 * - SQL 중간에 ? (위치홀더, placeholder)를 작성하여
		 *   ? 자리에 java 값을 대입할 준비가 되어있는 Statement
		 *   
		 * 장점 1 : SQL 작성이 간단해짐
		 * 장점 2 : ? 에 값 대입 시 자료형에 맞는 형태의 리터럴으로 대입됨
		 * 			ex) String 대입 -> '값' (자동으로 ' ' 추가)
		 * 			ex) int 대입	-> 값
		 * 장점 3 : 성능, 속도에서 우위를 가지고 있음
		 * 
		 * ** PreparedStatement는 Statement 자식 **
		 * 
		 * */
		
		 // 1. 객체 선언
		 Connection conn = null;
		 PreparedStatement pstmt = null;
		 
		 // SELECT가 아니기 때문에 ResultSet 필요 없음
		 
		 Scanner sc = null;
		 
		 try {
			 
			 // 2. DriverManager를 이용해 Connection 객채 생생성
			 Class.forName("oracle.jdbc.driver.OracleDriver");
				
			 conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE","kh","kh1234");
			 
			 // 3. SQL 작성
			 sc = new Scanner(System.in);
			 
			 System.out.print("id : ");
			 String id = sc.next();
			 
			 System.out.print("pw : ");
			 String pw = sc.next();
			 
			 System.out.print("name : ");
			 String name = sc.next();
			 
			 String sql = """
			 		INSERT INTO TB_USER 
			 		VALUES(SEQ_USER_NO.NEXTVAL, ?, ?, ?, DEFAULT )
			 		""";
			 
			 // 4. PreparedStatement 객체 생성
			 // -> 객체 생성과 동시에 SQL이 담겨지게 됨
			 // -> 미리 ? (위치홀더)에 값을 받을 준비를 해야되기 때문에...
			 
			 pstmt = conn.prepareStatement(sql);
			 
			 // 5. ? 위치홀더 알맞은 값 대입
			 // pstmt.set 자료형(?순서, 대입할 값)
			 pstmt.setString(1,id);
			 pstmt.setString(2,pw);
			 pstmt.setString(3,name);
			 // -> SQL 작성 완료
			 
			 // + DML 수행 전에 해줘야 할 것
			 // AutoCommit 끄기
			 // -> 개발자가 트랜젝션을 마음대로 제어하기 위해서
			 conn.setAutoCommit(false);
			 
			 // 6. SQL 수행 후, 결과 반환받기
			 // executeQuery()  : SELECT 수행, ResultSet 반환
			 // executeUpdate() : DML 수행, 결과 행 개수(int) 반환
			 // 보통 DML 실패 0, 성공 시 0 초과된 값 반환
			 
			 // pstmt에서 excuteQuery(), excuteUpdate() 매개변수 자리에 아무것도 없어야한다
			 int result = pstmt.executeUpdate();
			 
			 // 7. result 값에 따른 결과 처리 + 트랜잭션 제어 처리
			 if(result > 0) { // INSERT 성공시
				 System.out.println(name + "님이 추가 되었습니다.");
				 conn.commit(); // COMMIT 수행 -> DB에 INSERT 영구 반영
				 
			 }else { // 실패
				 System.out.println("추가 실패");
				 conn.rollback();				 
			 }
			 
			 
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(pstmt!=null) pstmt.close();
				if(conn!=null) conn.close();
				if(sc!=null) sc.close();
				
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		
	}

}
