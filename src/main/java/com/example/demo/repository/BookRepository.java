package com.example.demo.repository;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.Book;

//CrudRepositoryの拡張インターフェースとしてRepositoryを作成
public interface BookRepository extends CrudRepository<Book, Integer> {
	// totalevaluationが高い順にBookを全件取得する
	@Query("SELECT * FROM book ORDER BY totalevaluation DESC;")
	Iterable<Book> selectAllDesc();
	
	// totalevaluationが上位n件のBookを取得する 
	@Query("SELECT * FROM book ORDER BY totalevaluation DESC, id ASC LIMIT :n;")
	Iterable<Book> selectTopN(@Param("n") int n);
	
	// レコード数を取得する
	@Query("SELECT COUNT(*) FROM book;")
	int countAll();

	// totalevaluationが高い順にBookをoffset付きでlimit件取得
	@Query("SELECT * FROM (SELECT * FROM book ORDER BY totalevaluation DESC, id ASC) selectAllDesc LIMIT :limit OFFSET :offset;")
	Iterable<Book> selectAllDescByLimitOffset(@Param("limit") int limit, @Param("offset") int offset);

	// keywordでBook titleを検索しtotalevaluationが高い順にBookをoffset付きでlimit件取得
	@Query("SELECT * FROM (SELECT * FROM book WHERE title ILIKE '%' || :keyword || '%' ORDER BY totalevaluation DESC, id ASC) selectAllDesc LIMIT :limit OFFSET :offset;")
	Iterable<Book> searchAllDescByLimitOffset(@Param("keyword") String keyword, @Param("limit") int limit, @Param("offset") int offset);	
	
	// keword検索にヒットしたBook件数を取得
	@Query("SELECT COUNT(*) FROM book WHERE title ILIKE '%' || :keyword || '%';")
	int countSearchAll(@Param("keyword") String keyword);
	
	// Bookのidを指定してtotalevaluationを更新する
	// DML系クエリを実行する際は@Modifyingが必要
	@Modifying
	@Query("UPDATE book SET totalevaluation = :totalevaluation WHERE id = :id;")
	void updateTotalevaluationById(@Param("id") int id, @Param("totalevaluation") double totalevaluation);
}
