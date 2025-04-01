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

	/** 1-1. select user(id) method
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
	
	/** 2. select user(String id, String pw)
	 * @param id
	 * @param pw
	 * @return User user
	 * @throws Exception
	 */
	public User selectUser(String id, String pw) throws Exception {
		Connection conn = getConnection();
		
		User user = dao.selectUser(conn, id, pw);
		
		close(conn);
		
		return user;
	}

	/** 1-2. sign up(user) method
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

	
	/** 3. select todo(String id) method
	 * @param id
	 * @return List todoList
	 * @throws Exception
	 */
	public List<Todo> selectTodo(String id) throws Exception {

		Connection conn = getConnection();
		
		List<Todo> todoList = dao.selectTodo(conn, id);
		
		close(conn);
		
		return todoList;
	}

	/** 4. create todo(String title, String content, String id) method
	 * @param title
	 * @param content
	 * @param id
	 * @return int result
	 * @throws Exception
	 */
	public int createTodo(String title, String content, String id) throws Exception {
		
		Connection conn = getConnection();
		
		int result = dao.createTodo(conn, title, content, id);
		
		if(result>0) commit(conn);
		else rollback(conn);
				
		return result;
	}

	/** 5. update todo(int todo, String content, String id) method
	 * @param todo
	 * @param content
	 * @param id
	 * @return int result
	 * @throws Exception
	 */
	public int updateTodo(String todo, String title, String content, String id) throws Exception {
		
		Connection conn = getConnection();
		
		int result = dao.updateTodo(conn, todo, title, content, id);
		
		if(result>0) commit(conn);
		else rollback(conn);
		
		close(conn);
		
		return result;
	}



}
