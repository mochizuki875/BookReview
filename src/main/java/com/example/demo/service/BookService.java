package com.example.demo.service;


import java.util.Optional;

import com.example.demo.entity.Book;

public interface BookService {
	
	// Book全件取得
	Iterable<Book> selectAll();
	
	// 上位n件のbookを取得
	Iterable<Book> selectTopN(Integer n);
	
	// Bookをoffset単位で分割表示する際のページ数を取得
	Integer countAllPages(Integer offset);
	
	// 登録されている全Bookをlimit単位でページ分割し指定したpageに含まれるBook一覧を取得
	Iterable<Book> selectAllDescByPage(Integer page, Integer limit);
	
	// Bookを1件取得
	Optional<Book> selectOneById(Integer id);
	
	// 登録されている全Bookをkeywordで検索した結果をlimit単位でページ分割し指定したpageに含まれるBook一覧を取得
	// Iterable<Book> searchAll(String keyword);
	Iterable<Book> searchAllDescByPage(String keyword, Integer page, Integer limit);
	
	// // Bookのkeyword検索結果をoffset単位で分割表示する際のページ数を取得
	Integer countSearchAllPages(String keyword, Integer offset);
	
	// Bookを1件登録して登録されたBookを返す
	Book insertOne(Book book);
	
	// Bookのtotalevaluationを更新
	void updateTotalevaluationById(Integer id, Double totalevaluation);
	
	// Bookの削除
	void deleteOneById(Integer id);



}
