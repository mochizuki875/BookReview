package com.example.demo;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.demo.entity.Book;
import com.example.demo.entity.Review;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.ReviewRepository;
import com.example.demo.service.ReviewService;



@SpringBootApplication
public class BookReviewApplication {

	public static void main(String[] args) {
//		SpringApplication.run(BookReviewApplication.class, args);
		SpringApplication.run(BookReviewApplication.class, args).getBean(BookReviewApplication.class).execute(); // テスト用メソッド実行
	}
	
	// ----------------------------------------------------- 
	@Autowired
	BookRepository repository;
	@Autowired
	ReviewRepository reviewRepository;
	@Autowired
	ReviewService reviewService;
	
	// テスト用実行メソッド
	private void execute() {
		// 本を全件取得
		// showList();

		// 本を1件取得
		// showOne();
		
		// RVを1件取得
		showOneReview();
		
		// RVを1件削除
		deleteOneReview();
		
		insertOneReview();
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
	
	// RVを1件取得
	private void showOneReview() {
		System.out.println("--- RV1件取得開始 ---");
		Optional<Review> reviewOpt = reviewService.selectOneById(12);
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
		reviewService.deleteOneById(12);
		System.out.println("--- 1件削除完了 ---");
	}
	
	// RVを1件登録するメソッド
	private void insertOneReview() {
		System.out.println("--- 1件登録開始 ---");
		
		Review review = new Review();
		
		review.setEvaluation(5);
		review.setReview("テスト用RV登録");
		review.setBookid(4);
		review.setUserid(0);


		reviewService.insertReview(review);
		
		System.out.println("--- 1件登録完了 ---");
	}
}
