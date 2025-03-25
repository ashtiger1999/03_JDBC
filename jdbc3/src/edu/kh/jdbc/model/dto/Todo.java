package edu.kh.jdbc.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Todo {
	
	private int todoId;
	private String title;
	private String todoYN;
	private String createDate;
	private String editDate;
	private String content;

}
