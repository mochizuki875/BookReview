package com.example.demo.service;

import java.util.Optional;

import com.example.demo.entity.Book;

public interface BookService {
	
	// Book全件取得
	Iterable<Book> selectAll();
	// Bookを1件取得
	Optional<Book> selectOneById(Integer id);
	// Bookを1件登録
	void insertBook(Book book);
	// Bookのtotalevaluationを更新
	void updateTotalevaluationById(Integer id, Double totalevaluation);
	// Bookの削除
	void deleteBookById(Integer id);

}
