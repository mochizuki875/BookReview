package com.example.demo.repository;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.Review;

//CrudRepositoryの拡張インターフェースとしてRepositoryを作成
public interface ReviewRepository extends CrudRepository<Review, Integer> {
	// 独自で使用したいメソッドを定義
	// 本のIDを指定してRVを全件取得
	@Query("SELECT * FROM review WHERE bookid= :bookid;")
	Iterable<Review> findAllByBookid(@Param("bookid") int bookid);
	
	// 本のIDを指定してRVを全件削除
	// DML系クエリを実行する際は@Modifyingが必要
	@Modifying
	@Query("DELETE FROM review WHERE bookid= :bookid;")
	void deleteAllByBookid(@Param("bookid") int bookid);
}
