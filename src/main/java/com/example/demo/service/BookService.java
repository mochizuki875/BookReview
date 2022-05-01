package com.example.demo.service;

import java.util.Optional;

import com.example.demo.entity.Book;

public interface BookService {
	
	// Book全件取得
	Iterable<Book> selectAll();
	// Bookを1件取得
	Optional<Book> selectOneById(Integer id);

}
