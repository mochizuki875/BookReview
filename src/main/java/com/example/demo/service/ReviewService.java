package com.example.demo.service;

import java.util.Optional;

import com.example.demo.entity.Review;

public interface ReviewService {

	// RVのIDを指定してRVを1件取得
	Optional<Review> selectOneById(int id);
	
	// RVのIDを指定してRVを1件削除
	void deleteOneById(int id);

	// RVを1件登録
	Review insertOne(Review review);
	
	// 本のIDを指定してRVを全件取得
	Iterable<Review> selectAllByBookId(int bookid);
			
	// 本のIDを指定してRVを全件削除（本のIDに紐付くもの全て）
	void deleteAllByBookId(int bookid);
	 
	// 指定したbookidのTotalEvaluationを取得
	double selectTotalEvaluationByBookId(int bookid);
}
