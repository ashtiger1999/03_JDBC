package edu.kh.jdbc.dao;

import static edu.kh.jdbc.common.JDBCTemplate.close;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import edu.kh.jdbc.model.dto.Todo;
import edu.kh.jdbc.model.dto.User;

public class TodoDAO {

	private PreparedStatement pstmt = null;
	private ResultSet rs = null;

	/**
	 * 1-1. select user(conn, id) method
	 * 
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

		if (rs.next()) {

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

	/**
	 * 2. select user(Connection conn, String id, String pw)
	 * 
	 * @param conn
	 * @param id
	 * @param pw
	 * @return User user
	 * @throws Exception
	 */
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

		if (rs.next()) {
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

	/**
	 * 1-2. sign up(conn, user) method
	 * 
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

	/**
	 * 3. select todo(Connection conn, String id)
	 * 
	 * @param conn
	 * @param id
	 * @return List todoList
	 * @throws Exception
	 */
	public List<Todo> selectTodo(Connection conn, String id) throws Exception {

		List<Todo> todoList = new ArrayList<>();

		String sql = """
				select title, todo_yn , to_char(create_date,'YYYY"년" MM"월" DD"일"') as create_date
				from tb_todo
				join tb_member using(member_no)
				where id = ?
				""";

		pstmt = conn.prepareStatement(sql);

		pstmt.setString(1, id);

		rs = pstmt.executeQuery();

		while (rs.next()) {
			Todo todo = new Todo(rs.getString("title"), rs.getString("todo_yn"), rs.getString("create_date"));
			todoList.add(todo);
		}

		close(rs);
		close(pstmt);

		return todoList;
	}

	/**
	 * 4. create todo(Connection conn, String title, String content, String id)
	 * 
	 * @param conn
	 * @param title
	 * @param content
	 * @param id
	 * @return int result
	 * @throws Exception
	 */
	public int createTodo(Connection conn, String title, String content, String id) throws Exception {

		String sql = """
				insert into tb_todo
				VALUES(todo_seq.nextval, ?, 'N', sysdate, ?,
					(select member_no from tb_member where id = ?)
				)
				""";

		pstmt = conn.prepareStatement(sql);

		pstmt.setString(1, title);
		pstmt.setString(2, content);
		pstmt.setString(3, id);

		int result = pstmt.executeUpdate();

		return result;
	}

	/**
	 * 5. update todo(Connection conn, int todo, String content, String id) method
	 * 
	 * @param conn
	 * @param todo
	 * @param content
	 * @param id
	 * @return int result
	 * @throws Exception
	 */
	public int updateTodo(Connection conn, String todo, String title, String content, String id) throws Exception {

		String sql = """
				update tb_todo
				set content = ?, title =?
				where member_no = (
					select member_no
					from tb_member
					where id = ?
					)
				and title = ?
				""";

		pstmt = conn.prepareStatement(sql);

		pstmt.setString(1, content);
		pstmt.setString(2, title);
		pstmt.setString(3, id);
		pstmt.setString(4, todo);

		int result = pstmt.executeUpdate();

		return result;
	}

	public Todo selectTodo(Connection conn, Todo todo) throws Exception {

		String sql = """
				select * from tb_todo
				where title = ?
				""";

		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, todo.getTitle());

		rs = pstmt.executeQuery();
		
		Todo cont = null;

		if (rs.next()) {
			String title = rs.getString("title");
			String createDate = rs.getString("create_date");
			String todoYN = rs.getString("todo_yn");
			String content = rs.getString("content");

			cont = new Todo(title, todoYN, createDate, content);
		}

		close(rs);
		close(pstmt);

		return cont;
	}

	/** 7.
	 * @param conn
	 * @param title
	 * @param yn
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public int updateTodo(Connection conn, String title, String yn, String id) throws Exception {

		String sql = """
				update tb_todo
				set todo_yn =?
				where member_no = (
					select member_no
					from tb_member
					where id = ?
					)
				and title = ?
				""";

		pstmt = conn.prepareStatement(sql);

		pstmt.setString(1, yn);
		pstmt.setString(2, id);
		pstmt.setString(3, title);

		int result = pstmt.executeUpdate();
		
		close(pstmt);

		return result;
		
	}

	/** 7.
	 * @param conn
	 * @param title
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public int deleteTodo(Connection conn, String title, String id) throws Exception {
		
		String sql = """
				delete from tb_todo
				where member_no = (
					select member_no
					from tb_member
					where id = ?
					)
				and title = ?
				""";

		pstmt = conn.prepareStatement(sql);

		pstmt.setString(1, id);
		pstmt.setString(2, title);

		int result = pstmt.executeUpdate();
		
		close(pstmt);

		return result;
	}

}
