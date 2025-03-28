package edu.kh.jdbc.dao;

import static edu.kh.jdbc.common.JDBCTemplate.close;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import edu.kh.jdbc.model.dto.Todo;
import edu.kh.jdbc.model.dto.User;

public class TodoDAO {

	private PreparedStatement pstmt = null;
	private ResultSet rs = null;

	/** select user(conn, id) method
	 * @param conn
	 * @param id
	 * @return User user
	 * @throws Exception
	 */
	public User selectUser(Connection conn, String id) throws Exception {

		String sql = """
				select * from tb_member where id = ?

				""";

		pstmt = conn.prepareStatement(sql);
		
		pstmt.setString(1, id);

		rs = pstmt.executeQuery();

		User user = null;

		if(rs.next()) {

			String userId = rs.getString("id");
			String pw = rs.getString("pw");
			String name = rs.getString("name");
			String ssn = rs.getString("ssn");

			user = new User(userId, pw, name, ssn);
		}

		close(rs);
		close(pstmt);

		return user;
	}
	
	public User selectUser(Connection conn, String id, String pw) throws Exception {
		
		String sql = """
				select * from tb_member
				where id = ? and pw = ?
				""";
		
		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, id);
		pstmt.setString(2, pw);
		
		rs = pstmt.executeQuery();
		
		User user = null;
		
		if(rs.next()) {
			String userId = rs.getString("id");
			String userPw = rs.getString("pw");
			String userName = rs.getString("name");
			String userSsn = rs.getString("ssn");
			
			user = new User(userId, userPw, userName, userSsn);
		}
		
		close(rs);
		close(pstmt);
		
		return user;
	}

	/** sign up(conn, user) method
	 * @param conn
	 * @param user
	 * @return int result
	 * @throws Exception
	 */
	public int signUp(Connection conn, User user) throws Exception {

		String sql = """
				INSERT INTO tb_member
				VALUES(member_seq.nextval, ?, ?, ?, ?)
				""";
		
		pstmt = conn.prepareStatement(sql);
		
		pstmt.setString(1, user.getId());
		pstmt.setString(2, user.getPw());
		pstmt.setString(3, user.getName());
		pstmt.setString(4, user.getSsn());
		
		int result = pstmt.executeUpdate();
		
		return result;
	}

	public List<Todo> selectTodo(Connection conn, String id) {

		String sql = """
				select * from tb_todo
				where member_no = ?
				"""'
		return null;
	}

}
