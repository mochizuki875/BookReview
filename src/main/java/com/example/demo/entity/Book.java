package com.example.demo.entity;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// bookテーブル用エンティティ
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {
	@Id
	private Integer id; // 主キー
	private String title; // 本のタイトル
	private String overview; // 本の概要
	private Double totalevaluation; // 本の総合評価
}
