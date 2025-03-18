package edu.kh.prc;

import java.util.Scanner;

public class ggd {
	
	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in); 
		
		System.out.print("dan : ");
		int dan = sc.nextInt(); 
		
		System.out.print("col : ");
		int col = sc.nextInt();
		
		
		int num = dan;
		int row = 0;
		
		while(dan>0) {
			for(int times=1; times<=9; times++) {
				for(int i = row*col+1; i<=col*(row+1)&&i<=num; i++) {
					System.out.printf("%d X %d = %2d    ",i,times,i*times);
				}
				System.out.println();
			}
			System.out.println();
			row+=1;
			dan-=col;
		}
		
		/*
		for(int row = 0; row < dan/col; row++) {			
			for(int times = 1; times<=9; times++) {			
				for(int i = 1; i<=dan&&i+row<=col; i++) {
					System.out.printf("%d X %d = %2d    ",i+row*col,times,(i+row*col)*times);			
				}
				System.out.println();
			}
			System.out.println();
		}
		*/
		
	}
}