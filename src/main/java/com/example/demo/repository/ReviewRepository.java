package com.example.demo.repository;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.Review;

//CrudRepositoryの拡張インターフェースとしてRepositoryを作成
public interface ReviewRepository extends CrudRepository<Review, Integer> {
	// 独自で使用したいメソッドを定義
	// bookidを指定してReviewを全件取得
	@Query("SELECT * FROM review WHERE bookid= :bookid;")
	Iterable<Review> findAllByBookid(@Param("bookid") int bookid);
	
	// bookidを指定してReviewを全件削除
	@Modifying
	@Query("DELETE FROM review WHERE bookid= :bookid;")
	void deleteAllByBookid(@Param("bookid") int bookid);
	
	// bookidを指定してTotalEvaluationを取得
	@Query("SELECT ROUND(AVG(evaluation),1) AS totalevaluation FROM review WHERE bookid = :bookid GROUP BY bookid;")
	double findTotalEvaluationByBookId(@Param("bookid") int bookid);
}
