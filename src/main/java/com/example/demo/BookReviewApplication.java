package com.example.demo;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.demo.entity.Book;
import com.example.demo.repository.BookRepository;



@SpringBootApplication
public class BookReviewApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookReviewApplication.class, args);
		// SpringApplication.run(BookReviewApplication.class, args).getBean(BookReviewApplication.class).execute();;
	}
	
	// 
	@Autowired
	BookRepository repository;
	
	// 実行メソッド
	private void execute() {
		// 全件取得
		showList();
		
		// 1件取得
		showOne();
	}
	
	// 全件取得メソッド
	private void showList() {
		System.out.println("--- 全件取得開始 ---");
		Iterable<Book> books = repository.findAll();
		for(Book book : books) {
			System.out.println(book);
		}
		System.out.println("--- 全件取得完了 ---");
	}
	
	// 1件取得メソッド
	private void showOne() {
		System.out.println("--- 1件取得開始 ---");
		Optional<Book> bookOpt = repository.findById(1);
		if(bookOpt.isPresent()) {
			System.out.println(bookOpt.get());
		} else {
			System.out.println("値が存在しませんでした。");
		}
		System.out.println("--- 1件取得完了 ---");
	}

}
