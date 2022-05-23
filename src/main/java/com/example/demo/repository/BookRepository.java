package com.example.demo.repository;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.Book;

//CrudRepositoryの拡張インターフェースとしてRepositoryを作成
public interface BookRepository extends CrudRepository<Book, Integer> {
	// 本のIDを指定してtotalevaluationを更新する
	// DML系クエリを実行する際は@Modifyingが必要
	@Modifying
	@Query("UPDATE book SET totalevaluation = :totalevaluation WHERE id = :id;")
	void updateTotalevaluationById(@Param("id") Integer id, @Param("totalevaluation") Double totalevaluation);
	
	// keywordでBook titleを検索する
	@Query("SELECT * FROM book WHERE title ILIKE '%' || :keyword || '%';")
	Iterable<Book> searchAll(@Param("keyword") String keyword);
	
	// totalevaluationが高い順にBookを全件取得する
	@Query("SELECT * FROM book ORDER BY totalevaluation DESC;")
	Iterable<Book> selectAllDesc();
	
	// totalevaluationが上位n件のBookを取得する
	@Query("SELECT * FROM book ORDER BY totalevaluation DESC LIMIT :n;")
	Iterable<Book> selectTopN(@Param("n") Integer n);
}
