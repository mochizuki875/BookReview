package com.example.demo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

// bookテーブル用エンティティ
@Table("book")
public class Book {
	@Id
	@Column("id")
	private int id; // 主キー
	@Column("title")
	private String title; // 本のタイトル
	@Column("overview")
	private String overview; // 本の概要
	@Column("totalevaluation")
	private double totalevaluation; // 本の総合評価
	
	public Book() {
	}
	
	public Book(int id, String title, String overview, double totalevaluation) {
		this.id = id;
		this.title = title;
		this.overview = overview;
		this.totalevaluation = totalevaluation;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getOverview() {
		return overview;
	}
	
	public void setOverview(String overview) {
		this.overview = overview;
	}
	
	public double getTotalevaluation() {
		return totalevaluation;
	}
	
	public void setTotalevaluation(double totalevaluation) {
		this.totalevaluation = totalevaluation;
	}
	
}
