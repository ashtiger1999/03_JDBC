package edu.kh.jdbc.view;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import edu.kh.jdbc.dto.User;
import edu.kh.jdbc.service.UserService;

// View : 사용자와 직접 상호작용하는 화면 (UI)를 담당
// 입력을 받고 결과를 출력하는 역할
/**
 * 
 */
public class UserView {
	
	// 필드 
	private Scanner sc = new Scanner(System.in);
	private UserService service = new UserService();
	
	// 메서드
	
	/**
	 * JDBCTemplate 사용 테스트
	 */
	public void test() {
		
		// 입력된 ID와 일치하는 USER 정보 조회
		System.out.print("ID input : ");
		String input = sc.next();
		
		// 서비스 호출 후 결과 반환 받기
		User user = service.selectId(input);
		
		// 결과에 까라 사용자에게 보여줄 응답화면 결정
		System.out.println(user);
	}	
	
	/** User 관리 프로그램 메인 메뉴
	 */
	public void mainMenu() {
		
		int input = 0;
		
		do {
			try {
				
				System.out.println("\n===== User 관리 프로그램 =====\n");
				System.out.println("1. User 등록(INSERT)");
				System.out.println("2. User 전체 조회(SELECT)");
				System.out.println("3. User 중 이름에 검색어가 포함된 회원 조회 (SELECT)");
				System.out.println("4. USER_NO를 입력 받아 일치하는 User 조회(SELECT)");
				System.out.println("5. USER_NO를 입력 받아 일치하는 User 삭제(DELETE)");
				System.out.println("6. ID, PW가 일치하는 회원이 있을 경우 이름 수정(UPDATE)");
				System.out.println("7. User 등록(아이디 중복 검사)");
				System.out.println("8. 여러 User 등록하기");
				System.out.println("0. 프로그램 종료");
				
				System.out.print("메뉴 선택 : ");
				input = sc.nextInt();
				sc.nextLine(); // 버퍼에 남은 개행문자 제거
				
				switch(input) {
				case 1: insertUser(); break;
				case 2: selectAll(); break;
				case 3: selectName(); break;
				case 4: selectUser(); break;
				case 5: deleteUser(); break;
				case 6: updateName(); break;
				case 7: insertUser2(); break;
				case 8: multiInsertUser(); break;
				/*
				*/
				
				case 0 : System.out.println("\n[프로그램 종료]\n"); break;
				default: System.out.println("\n[메뉴 번호만 입력하세요]\n");
				}
				
				System.out.println("\n-------------------------------------\n");
				
			} catch (InputMismatchException e) {
				// Scanner를 이용한 입력 시 자료형이 잘못된 경우
				System.out.println("\n***잘못 입력 하셨습니다***\n");
				
				input = -1; // 잘못 입력해서 while문 멈추는걸 방지
				sc.nextLine(); // 입력 버퍼에 남아있는 잘못된 문자 제거
				
			} catch (Exception e) {
				// 발생되는 예외를 모두 해당 catch 구문으로 모아서 처리
				e.printStackTrace();
			}
			
		}while(input != 0);
		
	} // mainMenu() 종료

	/** 1. User 등록 관련 View
	 * @throws Exception
	 */
	private void insertUser() throws Exception {
		System.out.println("\n===INSERT USER PAGE===\n");
		System.out.print("userID : ");
		String userId = sc.next();
		
		System.out.print("userPw : ");
		String userPw = sc.next();
		
		System.out.print("userName : ");
		String userName = sc.next();
		
		// 입력받은 값 3개를 한번에 묶어서 전달할 수 있도록
		// User DTO 객체를 생성한 후 필드에 값을 세팅
		User user = new User();
		
		// setter 이용
		user.setUserId(userId);
		user.setUserPw(userPw);
		user.setUserName(userName);
		
		// 서비스 호출(INSERT) 후 결과 반환(int, 결과 행의 개수)받기
		int result = service.insertUser(user);
		
		// 반환된 결과에 따라 출력할 내용 선택
		if(result > 0) {
			System.out.println("\nUSER INSERT SUCCESS : " + userId + "\n");
		} else {
			System.out.println("INSERT EXECUTE FAILED");
		}
	}
	
	/** 2. User 전체 조회 관련 View (SELECT)
	 * @throws Exception 
	 */
	private void selectAll() throws Exception {
		System.out.println("\n===SELECT ALL USER INFO===\n");
		
		// 서비스 호출(SELECT) 후 결과(List<User> 반환받기
		List<User> userList = service.selectAll();
		
		// 결과에 따라 처리하기
		
		// 조회 결과가 없을 경우
		if(userList.isEmpty()) {
			System.out.println("NOT FOUND SELECT RESULT");
			return;
		}
		
		// 조회 결과가 있을 경우
		// userList에 있는 User 객체 출력
		for(User user : userList) {
			System.out.println(user);
		}
	}
	
	/** 3. User 중 이름에 검색어가 포함된 회원 조회
	 * @throws Exception
	 */
	private void selectName() throws Exception{
		
		System.out.println("\n===SELECT NAME PAGE===\n");
		
		System.out.print("Search : ");
		String keyword = sc.next();
		
		// 서비스 허출부 결과 반환받기
		List<User> searchList = service.selectName(keyword);
		
		if(searchList.isEmpty()) {
			System.out.println("NOT FOUND SELECT RESULT");
			return;
		}

		for(User user : searchList) {
			System.out.println(user);
		}
	}
	
	/** 4. USER_NO를 입력받아 일치하는 User 조회
	 * @throws Exception
	 */
	private void selectUser() throws Exception {
		
		System.out.println("\n===SELECT USER_NO PAGE===\n");
		
		System.out.print("USER_NO : ");
		int input = sc.nextInt();
		
		// 사용자 번호 == PK == 중복이 있을 수 없다
		// == 일치하는 사용자가 있다면 딱 1행만 조회된다.
		// -> 1행의 조회 결과를 담기 위해서 User DTO 객체 1개 사용
		User user = service.selectUser(input);
		
		// 조회결과가 없으면 null, 있으면 null 이 아님
		if(user==null) {
			System.out.println("NOT FOUND USER");
			return;
		}
		
		System.out.println(user);		
	}
	
	/** 5. USER_NO를 입력받아 일치하는 User 삭제
	 * @throws Exception
	 */
	private void deleteUser() throws Exception{
		
		System.out.println("\n===DELETE USER PAGE===\n");
		
		System.out.print("USER_NO to DELETE : ");
		int input = sc.nextInt();
		
		int result = service.deleteUser(input);
		
		if(result>0) System.out.println("DELETE SUCCESS");
		else System.out.println("NOT FOUND USER");
	}
	
	/** 6. ID,PW가 일치하는 회원이 있을 경우 이름 수정
	 * @throws Exception
	 */
	private void updateName() throws Exception{
		
		System.out.println("\n===USER UPDATE PAGE===\n");
		
		System.out.print("ID : ");
		String userId = sc.next();
		
		System.out.print("PASSWORD : ");
		String userPw = sc.next();
		
		// 입력받은 ID, PW가 일치하는 회원이 존재하는지 조회(SELECT)
		int userNo = service.selectUserNo(userId, userPw);
		
		if(userNo==0) {
			System.out.println("NOT FOUND USER");
			return;
		}
		
		System.out.print("NAME : ");
		String userName = sc.next();
		
		int result = service.updateName(userNo,userName);
		
		if(result>0) System.out.println("UPDATE COMPLETE");
		else System.out.println("UPDATE FAILED");
	}
	
	
	/**
	 * 7. User 등록(중복 검사) View
	 */
	private void insertUser2() throws Exception {
		
		System.out.println("\n===INSER USER PAGE2===\n");
		
		String userId = null;
		
		while(true) {
			System.out.print("ID : ");
			userId =sc.next();
			
			// 입력받은 userId가 중복인지 검사하는
			// 서비스(SELECT) 호출 후
			// 결과(int, 중복 == 1, 아니면 == 0)반환 받기
			int count = service.idCheck(userId);
			
			if(count == 0) { // 중복이 아닌 경우
				System.out.println("USABLE ID");
				break;
			}
			System.out.println("UNUSABLE ID");
		} // while문 종료
		
		// pw, name 입력받기
		System.out.print("userPw : ");
		String userPw = sc.next();
		
		System.out.print("userName : ");
		String userName = sc.next();
		
		// 입력받은 값 3개를 한번에 묶어서 전달할 수 있도록
		// User DTO 객체를 생성한 후 필드에 값을 세팅
		User user = new User();
		
		// setter 이용
		user.setUserId(userId);
		user.setUserPw(userPw);
		user.setUserName(userName);
		
		// 서비스 호출(INSERT) 후 결과 반환(int, 결과 행의 개수)받기
		int result = service.insertUser(user);
		
		// 반환된 결과에 따라 출력할 내용 선택
		if(result > 0) {
			System.out.println("\nUSER INSERT SUCCESS : " + userId + "\n");
		} else {
			System.out.println("INSERT EXECUTE FAILED");
		}
	}
	
	
	/**
	 * 8. 여러 User 등록하기
	 */
	private void multiInsertUser() throws Exception {
		
		/* 등록할 User 수 : 2
		 * 
		 * 1번째 userId : user100
		 * -> 사용가능한 ID 입니다
		 * 1번째 userPw : pass100
		 * 2번째 userName : 유저백
		 * --------------------------
		 * 2번째 userId : user200
		 * -> 사용가능한 ID 입니다
		 * 2번째 userPw : pass200
		 * 2번째 userName : 유저이백
		 * 
		 * */ 
		
		System.out.println("\n===INSERT MUTI USER===\n");
		
		System.out.print("NUMBER OF USER TO INSERT : ");
		int input = sc.nextInt();
		sc.nextLine();
		
		// 입력받은 회원 정보를 저장할 List 객체 생성
		List<User> userList = new ArrayList<User>();
		
		for(int i = 0; i < input; i ++) {

			String userId = null;
			
			while(true) {
				System.out.print("ID "+(i+1)+" : ");
				userId =sc.next();
				
				// 입력받은 userId가 중복인지 검사하는
				// 서비스(SELECT) 호출 후
				// 결과(int, 중복 == 1, 아니면 == 0)반환 받기
				int count = service.idCheck(userId);
				
				if(count == 0) { // 중복이 아닌 경우
					System.out.println("USABLE ID");
					break;
				}
				System.out.println("UNUSABLE ID");
			} // while문 종료
			
			// pw, name 입력받기
			System.out.print("PASSWORD "+(i+1)+" : ");
			String userPw = sc.next();
			
			System.out.print("NAME "+(i+1)+" : ");
			String userName = sc.next();
			
			// 입력받은 값 3개를 한번에 묶어서 전달할 수 있도록
			// User 객체를 생성한 후 userList에 추가
			
			User user = new User();
			
			user.setUserId(userId);
			user.setUserPw(userPw);
			user.setUserName(userName);
			
			userList.add(user);
		} // for문 종료
		
		// 입력받은 모든 사용자를 insert 하는 서비스 호출
		// -> 결과로 삽입된 행의 개수 반환
		int result = service.multiInsertUser(userList);
		
		if(result == userList.size()) {
			System.out.println("INSERT COMPLETE");
		} else {
			System.out.println("INSERT FAILED");
		}
	}
}
