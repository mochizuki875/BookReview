package com.example.demo.service;


import java.util.Optional;

import com.example.demo.entity.Book;

public interface BookService {
	
	// Book全件取得
	Iterable<Book> selectAll();
	// Bookをoffset単位で分割表示する際のページ数を取得
	Integer countAllPages(Integer offset);
	// Bookをlimit単位でページ分割し指定したページに含まれるBook一覧を取得
	Iterable<Book> selectAllDescByPage(Integer page, Integer limit);
	// Bookを1件取得
	Optional<Book> selectOneById(Integer id);
	// Bookを1件登録
	void insertOne(Book book);
	// Bookのtotalevaluationを更新
	void updateTotalevaluationById(Integer id, Double totalevaluation);
	// Bookの削除
	void deleteOneById(Integer id);
	// Bookの検索
	// Iterable<Book> searchAll(String keyword);
	Iterable<Book> searchAllDescByPage(String keyword, Integer page, Integer limit);
	// 上位n件のbookを取得
	Iterable<Book> selectTopN(Integer n);

}
