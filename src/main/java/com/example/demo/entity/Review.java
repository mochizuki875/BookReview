package com.example.demo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

// reviewテーブル用のエンティティ
@Table("review")
public class Review {
	@Id
	@Column("id")
	private Integer id; // 主キー
	@Column("evaluation")
	private Integer evaluation; // 本の評価（1~5）
	@Column("content")
	private String content; // 本のレビュー内容
	@Column("bookid")
	private Integer bookid; // 本のID
	@Column("userid")
	private Integer userid; // ユーザーID
	
	public Review() {
	}
	
	public Review(Integer id, Integer evaluation, String content, Integer bookid, Integer userid) {
		this.id = id;
		this.evaluation = evaluation;
		this.content = content;
		this.bookid = bookid;
		this.userid = userid;
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getEvaluation() {
		return evaluation;
	}
	
	public void setEvaluation(Integer evaluation) {
		this.evaluation = evaluation;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public Integer getBookid() {
		return bookid;
	}
	
	public void setBookid(Integer bookid) {
		this.bookid = bookid;
	}
	
	public Integer getUserid() {
		return userid;
	}
	
	public void setUserid(Integer userid) {
		this.userid = userid;
	}
	
}
