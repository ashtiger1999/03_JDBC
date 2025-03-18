package edu.kh.jdbc.service;

import java.sql.Connection;

import edu.kh.jdbc.common.JDBCTemplate;
import edu.kh.jdbc.dao.UserDAO;
import edu.kh.jdbc.dto.User;

// (Model 중 하나)Service : 비즈니스 로직을 처리하는 계층
// 데이터를 가공하고 트랜잭션 관리 수행
public class UserService {
	
	// 필드
	private UserDAO dao = new UserDAO();

	// 메서드
	
	/** 전달받은 아이디와 일치하는 User 정보 반환 서비스
	 * @param input ( View 단에서 입력된 아이디)
	 * @return 아이디가 일치하는 회원 정보가 닮긴 User 객체,
	 * 		   없다면 null 반환
	 */
	public User selectId(String input) {
		
		// 1. 커넥션 생성
		Connection conn = JDBCTemplate.getConnection();
				
		// 2. 데이터 가공(가공 시행이 없으면 생략)
		
		// 3. DAO 메서드 호출 결과 반환
		User user = dao.selectId(conn, input);
		
		// 4. DML(commit/rollback)(DML 미사용시 생략)
		
		// 5. 사용을 마친 Connection 자원 반환
		JDBCTemplate.close(conn);
		
		// 6. 결과물 반환
		return user;
	}

	public boolean insertUser(String userId, String userPw, String userName) {
		
		Connection conn = JDBCTemplate.getConnection();
		
		boolean flag = dao.insertUser(conn, userId, userPw, userName); 
		
		JDBCTemplate.commit(conn);
		
		if(flag) return true;
		return false;
	}


}
