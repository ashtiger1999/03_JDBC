package edu.kh.jdbc.view;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import edu.kh.jdbc.model.dto.Todo;
import edu.kh.jdbc.model.dto.User;
import edu.kh.jdbc.model.service.TodoService;

public class TodoView {

	private TodoService service = new TodoService();
	private Scanner sc = new Scanner(System.in);
	private User loginUser = null;

	public void mainMenu() {

		int input = 0;

		do {
			try {
				System.out.println("\n===== TO DO LIST 관리 프로그램 =====\n");
				System.out.println("1. 회원가입");
				System.out.println("2. 로그인");
				System.out.println("3. 내 TODO LIST 전체 조회");
				System.out.println("4. 새로운 TODO 작성");
				System.out.println("5. TODO 수정");
				System.out.println("6. 완료 여부 변경");
				System.out.println("7. TODO 삭제");
				System.out.println("8. 로그아웃");
				System.out.println("0. 프로그램 종료");

				System.out.print("메뉴 선택 : ");
				input = sc.nextInt();
				sc.nextLine(); // 버퍼에 남은 개행문자 제거

				switch (input) {

				case 1:
					signUp();
					break;
				case 2:
					logIn();
					break;
				case 3:
					selectTodo();
					break;
					/*
				case 4:
					createTodo();
					break;
				case 5:
					updateTodo();
					break;
				case 6:
					updateYN();
					break;
				case 7:
					deleteTodo();
					break;
				case 8:
					logOut();
					break;
				*/
				case 0:
					System.out.println("\n[프로그램 종료]\n");
					break;
				default:
					System.out.println("\n[메뉴 번호만 입력하세요]\n");
				}
				System.out.println("\n-------------------------------------\n");
			} catch (InputMismatchException e) {
				System.out.println("\n***잘못 입력 하셨습니다***\n");

				input = -1; 
				sc.nextLine(); 
			} catch (Exception e) {e.printStackTrace();}
		} while (input != 0);
	} // mainMenu() 종료

	/** sign up method
	 * @throws Exception
	 */
	public void signUp() throws Exception {
		System.out.println("\n=====회원가입=====\n");
		if(loginUser!=null) {
			System.out.println("로그아웃 후 이용해주세요.");
			return;
		}
		System.out.print("아이디 입력 : ");
		String id = sc.next();
		
		User user = service.selectUser(id);
		
		if(user!=null) {
			System.out.println("중복된 아이디 입니다.");
			return;
		}
		
		System.out.println("\n사용 가능한 아이디\n");
		
		System.out.print("패스워드 입력 : ");
		String pw = sc.next();
		System.out.print("패스워드 확인 : ");
		String pw2 = sc.next();
		
		if(!pw.equals(pw2)) {
			System.out.println("비밀번호가 일치하지 않습니다.");
			return;
		}
		
		System.out.print("이름 : ");
		String name = sc.next();
		System.out.println("주민등록번호 : ");
		String ssn = sc.next();
		
		User signUpUser = new User(id, pw, name, ssn);
		
		int result = service.signUp(signUpUser);
		
		if(result>0) {
			System.out.println("회원가입에 성공했습니다.\n");
			return;
		}
		System.out.println("회원가입에 실패하였습니다.\n");
	}
	
	/** log in method
	 * @throws Exception
	 */
	public void logIn() throws Exception {
		System.out.println("\n=====로그인=====\n");
		
		if(loginUser!=null) {
			System.out.println("로그인 중인 계정이 있습니다.");
			return;
		}
		
		System.out.print("아이디 : ");
		String id = sc.next();
		System.out.print("비밀번호 : ");
		String pw = sc.next();
		
		loginUser = service.selectUser(id,pw);
		if(loginUser==null) {
			System.out.println("로그인에 실패하셨습니다.");
			return;
		}
		
		System.out.printf("%s님 환영합니다.",loginUser.getName());
	}

	public void selectTodo() throws Exception {
		System.out.println("\n=====내 TODO LIST 전체 조회=====\n");
		
		if(loginUser==null) {
			System.out.println("로그인 후 이용 가능합니다.");
			return;
		}
		
		List<Todo> todoList = service.selectTodo(loginUser.getId());
		
		int count=1;
		
		for(Todo item : todoList) {
			String str = String.format("%d번째 할 일 /  %s / 완료 여부 : %s / %s",count, item.getTitle(),item.getTodoYN(), item.getCreateDate());
			System.out.println(str);
			count++;
		}
	}
}
