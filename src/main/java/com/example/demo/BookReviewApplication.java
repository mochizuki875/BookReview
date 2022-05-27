package com.example.demo;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.demo.entity.Book;
import com.example.demo.entity.Review;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.ReviewRepository;
import com.example.demo.service.BookService;
import com.example.demo.service.ReviewService;




@SpringBootApplication
public class BookReviewApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookReviewApplication.class, args);
//		SpringApplication.run(BookReviewApplication.class, args).getBean(BookReviewApplication.class).execute(); // テスト用メソッド実行
	}
	
	// ----------------------------------------------------- 
	@Autowired
	BookRepository repository;
	@Autowired
	BookService bookService;
	@Autowired
	ReviewRepository reviewRepository;
	@Autowired
	ReviewService reviewService;
	
	
	// テスト用実行メソッド
	private void execute() {
		// showList(); // 本を全件取得
		// showOne(); // 本を1件取得
		// searchAll(); // Bookを検索
		// countAllPages(); // ページ分割した際のページ数を取得
		// selectAllDescByPage(); // Bookをlimitを指定してPage単位で取得する
		// showListByPage();// BookをPage単位で取得
		
		// showOneReview(); // RVを1件取得
		// deleteOneReview(); // RVを1件削除
		// insertOneReview(); // RVを1件追加
		// showReview(); // 特定の本のRVを取得
		// deleteAllReview(); // 特定の本に関連するRVを全て削除
		
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

	// ページ分割した際のページ数を取得
	private void countAllPages() {
		System.out.println("--- ページ数取得開始 ---");
		System.out.println(bookService.countAllPages(5));
		System.out.println("--- ページ数取得完了 ---");
	}
	
	// Bookをlimitを指定してPage単位で取得するメソッド
	private void selectAllDescByPage() {		
		System.out.println("--- Nページ目のBook取得開始 ---");
		Iterable<Book> books = bookService.selectAllDescByPage(2,3);
		for(Book book : books) {
			System.out.println(book.getTitle() + ":" + book.getTotalevaluation());
		}
		System.out.println("--- Nページ目のBook取得完了 ---");
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
	
	// Bookをキーワードで検索
//	private void searchAll() {
//		System.out.println("--- 本の検索開始 ---");
//		Iterable<Book> books = bookService.searchAll("book");
//		for(Book book : books) {
//			System.out.println(book.getTitle());
//		}
//		System.out.println("--- 本の検索完了 ---");
//	}
	
	// RVを1件取得
	private void showOneReview() {
		System.out.println("--- RV1件取得開始 ---");
		Optional<Review> reviewOpt = reviewService.selectOneById(13);
		if(reviewOpt.isPresent()) {
			System.out.println(reviewOpt.get());
		} else {
			System.out.println("RVが存在しませんでした。");
		}
		System.out.println("--- RV1件取得完了 ---");
	}
	
	// RVを1件削除
	private void deleteOneReview() {
		System.out.println("--- 1件削除開始 ---");
		reviewService.deleteOneById(13);
		System.out.println("--- 1件削除完了 ---");
	}
	
	// RVを1件登録
	private void insertOneReview() {
		System.out.println("--- 1件登録開始 ---");
		Review review = new Review();
		review.setEvaluation(5);
		review.setContent("テスト用RV登録");
		review.setBookid(4);
		review.setUserid(0);
		reviewService.insertOne(review);		
		System.out.println("--- 1件登録完了 ---");
	}
	
	// 特定の本のRVを取得
	private void showReview() {
		System.out.println("--- RV取得開始 ---");
		Iterable<Review> reviews = reviewService.selectAllByBookId(4);
		for(Review review : reviews) {
			System.out.println(review);
		}
		System.out.println("--- RV取得完了 ---");
	}
	
	// 特定の本に関連するRVを全て削除
	private void deleteAllReview() {
		System.out.println("--- RV削除開始 ---");
		reviewService.deleteAllByBookId(4);
		System.out.println("--- RV削除完了 ---");
	}
}
