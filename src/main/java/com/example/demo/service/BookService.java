package com.example.demo.service;


import java.util.Optional;

import com.example.demo.entity.Book;

public interface BookService {
	
	// Book全件取得
	Iterable<Book> selectAll();
	
	// 上位n件のBookを取得
	Iterable<Book> selectTopN(int n);
	
	// Bookをlimit単位で分割表示する際のページ数を取得
	int countAllPages(int limit);
	
	// 登録されている全Bookをlimit単位でページ分割し指定したpageに含まれるBook一覧を取得
	Iterable<Book> selectAllDescByPage(int page, int limit);
	
	// Bookを1件取得
	Optional<Book> selectOneById(int id);
	
	// 登録されている全Bookをkeywordで検索した結果をlimit単位でページ分割し指定したpageに含まれるBook一覧を取得
	Iterable<Book> searchAllDescByPage(String keyword, int page, int limit);
	
	// Bookのkeyword検索結果をlimit単位で分割表示する際のページ数を取得
	int countSearchAllPages(String keyword, int limit);
	
	// Bookを1件登録して登録されたBookを返す
	Book insertOne(Book book);
	
	// Bookを1件更新して更新されたBookを返す
	Book updateOne(Book book);
	
	// Bookのtotalevaluationを更新
	void updateTotalevaluationById(int id, double totalevaluation);
	
	// Bookの削除
	void deleteOneById(int id);



}
