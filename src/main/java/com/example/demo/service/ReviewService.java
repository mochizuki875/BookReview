package com.example.demo.service;

import java.util.Optional;

import com.example.demo.entity.Review;

public interface ReviewService {

	// RVのIDを指定してRVを1件取得
	Optional<Review> selectOneById(Integer id);
	
	// RVのIDを指定してRVを1件削除
	void deleteOneById(Integer id);

	// RVを1件登録
	void insertOne(Review review);
	
	// 本のIDを指定してRVを全件取得
	 Iterable<Review> selectAllByBookId(Integer bookid);
			
	// 本のIDを指定してRVを全件削除（本のIDに紐付くもの全て）
	 void deleteAllByBookId(Integer bookid);
}
