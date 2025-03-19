package edu.kh.jdbc.dao;

// import static : 지정된 경로에 존재하는 static 구문을 모두 얻어와
// 클래스명.메서드명() 이 아닌 메서드명()만 작성해도 호출 가능
import static edu.kh.jdbc.common.JDBCTemplate.close;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import edu.kh.jdbc.dto.User;

// (Model 중 하나)DAO (Data Access Object)
// 데이터가 저장된 곳에 접근하는 용도의 객체
// -> DB에 접근하여 Java에서 원하는 결과를 얻기 위해
// 	  SQL을 수행하고 결과를 반환받는 역할
/**
 * 
 */
public class UserDAO {
	
	// 필드
	// - DB 접근 관련한 JDBC 객체 참조 변수 미리 선언
	private Statement stmt = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	
	// 메서드
	
	/** 전달받은 Connection을 이용해서 DB에 접근하여
	 * 전달받은 아이디(input)와 일피하는 User 정보를 DB 조회
	 * @param conn : Service에서 생성한 Connection 객체
	 * @param input : View에서 입력받은 아이디
	 * @return 아이디가 일치하는 회원의 User 또는 null
	 */
	public User selectId(Connection conn, String input) {
		
		// 1. 결과 저장용 변수 선언
		User user = null;
		
		try {
			// 2. SQL 작성
			String sql = """
					Select * from tb_user
					where user_id = ?
					""";
			// 3. PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			
			// 4. 위치홀더에 알맞은 값 세팅
			pstmt.setString(1, input);
			
			// 5. SQL 수행 후 결과 반환 받기
			rs = pstmt.executeQuery();
			
			// 6. 조회 결과가 있을 경우
			// + 중복되는 아이디가 없다고 가정
			// -> 1행만 조회되기 때문에 while문 보다는 if를 사용하는게 효과적
			if(rs.next()) {
				// 첫 행에 데이터가 존재한다면
				
				// 각 컬럼의 값 얻어오기
				int userNo = rs.getInt("user_no");
				String userId = rs.getString("user_id");
				String userPw = rs.getString("user_pw");
				String userName = rs.getString("user_name");
				// java.sql.Date
				Date enrollDate = rs.getDate("enroll_date");
				
				// 조회된 컬럼값을 이용해서 User 객체 생성
				user = new User(userNo, userId, userPw, userName, enrollDate.toString());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(rs);
			close(pstmt);
			
			// Connection 객체는 생성된 Service에서 close
		}
		return user; // 결과 반환 (생성된 User 객체 또는 null)
	}

	/** 1. User 등록 DAO
	 * @param conn : DB 연결 정보가 담겨있는 COnnection 객체
	 * @param user : 입력받은 id, pw, name이 세팅된 User 객체
	 * @return INSERT 결과 행의 개수
	 * @throws Exception 
	 */
	public int insertUser(Connection conn, User user) throws Exception {
		
		// SQL 수행 중 발생하는 예외를  
		// catch로 처리하지 않고, throws를 이용해서 호출부로 던져 처리
		// -> catch 문 필요 없다.		
		
		// 1. 결과 저장용 변수 선언
		int result = 0; 
		
		try {
			// 2. SQL 작성
			String sql = """
					INSERT INTO TB_USER 
					VALUES(SEQ_USER_NO.NEXTVAL, ?, ?, ?, DEFAULT )
					""";
			
			// 3. PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			
			// 4. 위치홀더에 알맞은 값 대입
			pstmt.setString(1, user.getUserId());
			pstmt.setString(2, user.getUserPw());
			pstmt.setString(3, user.getUserName());
			
			// 5. SQL(SQL) 수행(executeUpdate()) 후 결과(update rows) 반환 받기(result)
			result = pstmt.executeUpdate();
			
		} finally {
			// 5. 사용한 jdbc 객체 자원 반환(close)
			close(pstmt);
		}
		
		// 결과 저장용 변수에 저장된 값 반환
		return result;
	}

	/** 2. User 전체 조회 DAO
	 * @param conn
	 * @return userList
	 */
	public List<User> selectAll(Connection conn) throws Exception {
		
		// 1. 결과 저장용 변수 선언
		List<User> userList = new ArrayList<User>();
		
		try {
			
			// 2. SQL 작성
			String sql = """
					SELECT user_no, user_id, user_pw, user_name, 
					to_char(enroll_date,'YYYY"년" MM"월" DD"일"') enroll_date
					FROM tb_user
					ORDER BY user_no asc
					""";
			
			// 3. PreparedStatement 생성
			pstmt = conn.prepareStatement(sql);
			
			// 4. SQL(SELECT) 수행 후 결과(RESULTSET) 반환 받기
			rs = pstmt.executeQuery();
			
			// 5. 조회 결과를 한 행씩 접근하여 컬럼값 가져오기
			// 몇 행이 조회될 지 모른다 -> while 문
			// 무조간 1행만 조회된다	-> if 문
			while(rs.next()) {
				int userNo = rs.getInt("user_no");
				String userId = rs.getString("user_id");
				String userPw = rs.getString("user_pw");
				String userName = rs.getString("user_name");
				String enrollDate = rs.getString("enroll_date");
				// java.sql.Date 타입으로 값을 저장하지 않은 이유
				// -> SELECT 문에서 TO_CHAR()를 이용하여 문자열로 변환해 조회했기 때문에
				
				// User 객체 새로 생성하여 컬럼값 세팅하기
				User user = new User(userNo,userId,userPw,userName,enrollDate);
				
				// listUser에 생성한 User 객체 추가하기
				userList.add(user);
			}
			
		} finally {
			close(pstmt);
			close(rs);	
		}
		
		return userList;
	}

	/** 3. 이름에 검색어가 포함되는 회원 모두 조회 DAO
	 * @param conn
	 * @param keyword
	 * @return search
	 */
	public List<User> selectName(Connection conn, String keyword) throws Exception {
		
		// 결과 저장용 변수 선언
		List<User> searchList = new ArrayList<User>();
		
		try {
			// SQL 작성
			String sql ="""
				SELECT user_no, user_id, user_pw, user_name, 
				to_char(enroll_date,'YYYY"년" MM"월" DD"일"') enroll_date
				FROM TB_USER
				WHERE user_name LIKE '%'||?||'%'
				ORDER BY user_no asc
				""";
			
			// PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			
			// 위치홀더에 알맞은 값 넣기
			pstmt.setString(1, keyword);
			
			// DB 수행 후 결과 반환 받기
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				int userNo = rs.getInt("user_no");
				String userId = rs.getString("user_id");
				String userPw = rs.getString("user_pw");
				String userName = rs.getString("user_name");
				String enrollDate = rs.getString("enroll_date");
				
				User user = new User(userNo,userId,userPw,userName,enrollDate);
				
				searchList.add(user);
			}
			
		} finally {
			close(pstmt);
			close(rs);
		}
		return searchList;
	}

	/** 4. USER_NO를 입력받아 일치하느 User 조회 DAO
	 * @param conn
	 * @param input
	 * @return User
	 */
	public User selectUser(Connection conn, int input) throws Exception {
		
		User user = null;
		
		try {
			String sql = """
					SELECT user_no, user_id, user_pw, user_name, 
					to_char(enroll_date,'YYYY"년" MM"월" DD"일"') enroll_date
					FROM TB_USER
					WHERE user_no = ?
					""";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, input);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				int userNo = rs.getInt("user_no");
				String userId = rs.getString("user_id");
				String userPw = rs.getString("user_pw");
				String userName = rs.getString("user_name");
				String enrollDate = rs.getString("enroll_date");
				
				user = new User(userNo,userId,userPw,userName,enrollDate);
			}
			
		} finally {
			close(pstmt);
			close(rs);
		}
		
		return user;
	}

	/** 5. USER_NO를 입력받아 일치하는 User 삭제 DAO
	 * @param conn
	 * @param input
	 * @return result
	 * @throws Exception
	 */
	public int deleteUser(Connection conn, int input) throws Exception{
		
		int result = 0;
		
		try {
			
			String sql = """
					delete from tb_user
					where user_no = ?
					""";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, input);
			
			result = pstmt.executeUpdate();
			
		} finally {
			close(pstmt);
		}
		return result;
	}

	/** 6-1. ID, PW가 일치하는 회원의 USER_NO 조회 DAO
	 * @param conn
	 * @param userId
	 * @param userPw
	 * @return userNO
	 * @throws Exception
	 */
	public int selectUser(Connection conn, String userId, String userPw) throws Exception {
		
		int userNo = 0;
		
		try {
			String sql = """
					select user_no
					from tb_user
					where user_id = ?
					and user_pw = ?
					""";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userId);
			pstmt.setString(2, userPw);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				userNo = rs.getInt("user_no");
			}
			
		} finally {
			close(rs);
			close(pstmt);
		}
		return userNo;
	}

	/** 6-2. userNo가 일치하는 회원의 이름 수정 DAO
	 * @param conn
	 * @param userNo
	 * @param userName
	 * @return result
	 */
	public int updateName(Connection conn, int userNo, String userName) throws Exception{
		
		int result = 0;
		
		try {
			
			String sql = """
					update tb_user
					set user_name = ?
					where user_no = ? 
					""";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userName);
			pstmt.setInt(2, userNo);
			
			result = pstmt.executeUpdate();
			
		} finally {
			close(pstmt);
		}
		
		return result;
	}

	/** 아이디 중복 확인 DAO
	 * @param conn
	 * @param userId
	 * @return count
	 */
	public int idCheck(Connection conn, String userId) throws Exception {
		
		int count = 0;
		
		try {
			
			String sql = """
					select count(*)
					from tb_user
					where user_id = ?
					""";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userId);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				count = rs.getInt(1); 
			}
			
		} finally {
			close(rs);
			close(pstmt);
		}
		
		return count;
	}	
}