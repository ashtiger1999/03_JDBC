package edu.kh.jdbc.model.service;

import static edu.kh.jdbc.common.JDBCTemplate.close;
import static edu.kh.jdbc.common.JDBCTemplate.getConnection;
import static edu.kh.jdbc.common.JDBCTemplate.*;

import java.sql.Connection;
import java.util.List;

import edu.kh.jdbc.dao.TodoDAO;
import edu.kh.jdbc.model.dto.Todo;
import edu.kh.jdbc.model.dto.User;

public class TodoService {
	
	private TodoDAO dao = new TodoDAO();

	/** select user(id) method
	 * @param id
	 * @return User user
	 * @throws Exception
	 */
	public User selectUser(String id) throws Exception {
		Connection conn = getConnection();
		
		User user = dao.selectUser(conn, id);
		
		close(conn);
		
		return user;
	}
	
	public User selectUser(String id, String pw) throws Exception {
		Connection conn = getConnection();
		
		User user = dao.selectUser(conn, id, pw);
		
		close(conn);
		
		return user;
	}

	/** sign up(user) method
	 * @param user
	 * @return int result
	 * @throws Exception
	 */
	public int signUp(User user) throws Exception {
		
		Connection conn = getConnection();
		
		int result = dao.signUp(conn, user);
		
		if(result>0) {
			commit(conn);
		}
		else {
			rollback(conn);
		}
		
		close(conn);
		
		return result;
	}

	
	public List<Todo> selectTodo(String id) {

		Connection conn = getConnection();
		
		List<Todo> todoList = dao.selectTodo(conn, id);
		
		close(conn);
		
		return todoList;
	}

}
