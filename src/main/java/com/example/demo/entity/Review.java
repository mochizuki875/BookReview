package com.example.demo.entity;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Review {
	@Id
	private Integer id; // 主キー
	private Integer evaluation; // 本の評価（1~5）
	private String review; // 本のレビュー
	private Integer bookid; // 本のID
	private Integer userid; // ユーザーID
}
