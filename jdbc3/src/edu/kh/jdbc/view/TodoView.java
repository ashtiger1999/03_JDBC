package edu.kh.jdbc.view;

import java.util.InputMismatchException;
import java.util.Scanner;

import edu.kh.jdbc.model.service.TodoService;

public class TodoView {

	private TodoService service = new TodoService();
	private Scanner sc = new Scanner(System.in);

	public void mainMenu() {

		int input = 0;

		do {
			try {
				System.out.println("\n===== TO DO LIST 관리 프로그램 =====\n");
				System.out.println("1. 회원가입");
				System.out.println("2. 로그인");
				System.out.println("3. 내 TODO LIST 조회");
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

				/*
				case 1:
					signUp();
					break;
				case 2:
					logIn();
					break;
				case 3:
					selectTodo();
					break;
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

}
