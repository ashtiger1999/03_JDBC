package edu.kh.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Scanner;

public class JDBCExample6 {

	public static void main(String[] args) {

		// 아이디, 비밀번호, 이름을 입력받아
		// 아이디, 비밀번호가 일치하는 사용자의 이름을 수정(UPDATE)
		
		// 1. PreparedStatement 이용하기
		// 2. commit/rollback 처리하기
		// 3. 성공시 수정성공, 실패시 아이디 또는 비밀번호가 틀렸습니다.
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		Scanner sc = null;
		
		
	}

}
