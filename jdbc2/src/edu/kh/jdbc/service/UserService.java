package edu.kh.jdbc.service;

import java.sql.Connection;
import java.util.List;

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

	/** 1. User 등록 서비스
	 * @param user
	 * @return 결과 행의 개수
	 * @throws Exception
	 */
	public int insertUser(User user) throws Exception {
		
		// 1. 커넥션 생성
		Connection conn = JDBCTemplate.getConnection();
		
		// 2. 데이터 가공(가공 시행이 없으면 생략)

		// 3. DAO 메서드 호출 후 결과 반환하기
		int result = dao.insertUser(conn, user); 
		
		// 4. DML(commit/rollback)(DML 미사용시 생략)
		if(result>0) { // INSERT 성공
			JDBCTemplate.commit(conn);
		}
		else { // INSERT 실패 
			JDBCTemplate.rollback(conn);
		}
		
		// 5. Connection 반환
		JDBCTemplate.close(conn);
		
		// 6. 메서드 호출부(View)로 결과 반환
		return result;
	}

	/** 2. User 전체 조회 서비스
	 * @return 조회된 User들이 담긴 List
	 * @throws Exception 
	 */
	public List<User> selectAll() throws Exception {
		
		// 1. Connection 생성
		Connection conn = JDBCTemplate.getConnection();
		
		// 2. DAO 메서드 호출(SELECT) 후 결과(List<User>) 반환 받기
		List<User> userList = dao.selectAll(conn);
		
		// 3. Connection 반환
		JDBCTemplate.close(conn);
		
		return userList;
	}

	/** 3. User 중 이름에 검색어가 포함된 회원 조회 서비스
	 * @param keyword : 입력한 키워드
	 * @return searchList : 조회된 회원 리스트
	 */
	public List<User> selectName(String keyword) throws Exception {

		// 1. Connection 생성
		Connection conn = JDBCTemplate.getConnection();
		
		// 2. DAO 메서드 호출(SELECT) 후 결과(List<User>) 반환 받기
		List<User> searchList = dao.selectName(conn, keyword);
		
		// 3. Connection 반환
		JDBCTemplate.close(conn);
		
		return searchList;
	}

	/** 4. USER_NO를 입력받아 일치하는 User를 조회하는 서비스
	 * @param input
	 * @return User : 조회된 회원 정보 || null
	 */
	public User selectUser(int input) throws Exception {
		
		Connection conn = JDBCTemplate.getConnection();
		
		User user = dao.selectUser(conn, input);
		
		JDBCTemplate.close(conn);
		
		return user;
	}

	/** 5. USER_NO를 입력받아 일치하는 User 삭제 서비스
	 * @param input
	 * @return result
	 */
	public int deleteUser(int input) throws Exception {
		
		Connection conn = JDBCTemplate.getConnection();
		
		int result = dao.deleteUser(conn,input);
		
		if(result>0) {
			JDBCTemplate.commit(conn);
		} else {
			JDBCTemplate.rollback(conn);
		}
		
		JDBCTemplate.close(conn);
		
		return result;
	}

	/** 6-1. ID,PW가 일치하는 회원의 USER_NO 조회 서비스
	 * @param userId
	 * @param userPw
	 * @return userNo
	 */
	public int selectUserNo(String userId, String userPw) throws Exception {
		
		Connection conn = JDBCTemplate.getConnection();
		
		int userNo = dao.selectUser(conn, userId, userPw);
		
		return userNo;
	}

	/** 6-2. userNo가 일치하는 회원의 이름 수정 서비스 
	 * @param userNo
	 * @param userName
	 * @return result
	 * @throws Exception
	 */
	public int updateName(int userNo, String userName) throws Exception{
		
		Connection conn = JDBCTemplate.getConnection();
		
		int result = dao.updateName(conn,userNo,userName);
		
		if(result>0) {
			JDBCTemplate.commit(conn);
		} else {
			JDBCTemplate.rollback(conn);
		}
		
		JDBCTemplate.close(conn);
		
		return result;
	}

	/** 7-1. 아이디 중복 확인 서비스
	 * @param userId
	 * @return count
	 */
	public int idCheck(String userId) throws Exception {
		
		Connection conn = JDBCTemplate.getConnection();
		
		int count = dao.idCheck(conn, userId);
		
		JDBCTemplate.close(conn);
		
		return count;
	}

	/** 8. userList에 있는 모든 User 객체를 INSERT하는 서비스
	 * @param userList
	 * @return count
	 * @throws Exception 
	 */
	public int multiInsertUser(List<User> userList) throws Exception {

		// 다중 INSERT 방법
		// 1) SQL을 이용한 다중 INSERT
		// 2) JAVA 반복문을 이용한 다중 INSERT
		
		// 2)
		Connection conn = JDBCTemplate.getConnection();
		
		int count = 0;
		
		// 1행씩 삽입
		for(User user : userList) {
			int result = dao.insertUser(conn, user);
			count += result;
		}
		
		// 트랜잭션 제어 처리
		// 전체 삽입 성공 시 commit / 아니면 rollback(일부 삽입, 전체 실패)
		if(count == userList.size()) {
			JDBCTemplate.commit(conn);
		} else {
			JDBCTemplate.rollback(conn);
		}
		
		JDBCTemplate.close(conn);
		
		return count;
	}
}
