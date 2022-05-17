package com.example.demo.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.entity.Book;
import com.example.demo.entity.Review;
import com.example.demo.service.BookService;
import com.example.demo.service.ReviewService;

@Controller
@RequestMapping
public class BookReviewController {
	// Serviceインスタンスの作成
	@Autowired
	BookService bookService;
	@Autowired
	ReviewService reviewService;
	
	// ホーム画面を表示
	@GetMapping("/")
	public String showHome(Model model) {
		Iterable<Book> bookList = bookService.selectAll(); // Book情報を全件取得		
		model.addAttribute("bookList", bookList); // Modelに格納
		return "home";
	}
	
	// 本の詳細画面表示
	// 書籍IDをリクエストパラメータとして受け取って本の詳細ページを返す
	@GetMapping("/book/detail/{id}")
	public String showBook(@PathVariable Integer id, Model model) {
		
		// 本の情報を取得
		Optional<Book> bookOpt = bookService.selectOneById(id);
		if(bookOpt.isPresent()) {
			model.addAttribute("book", bookOpt.get());
		}
		
		// 本のRVを取得
		Iterable<Review> reviewList = reviewService.selectAllByBookId(id);
		model.addAttribute(reviewList);
		
		return "detail";
	}
	
	// 本の新規登録画面
	@GetMapping("/book/newbook")
	public String newBook() {
		return "newbook";
	}
	
	// 本の新規登録
	@PostMapping("/book/insert")
	public String insert(@RequestParam String title, @RequestParam String overview, RedirectAttributes redirectAttributes) {
		Book book = new Book();
		// @DataアノテーションによりBookの各フィールドに対するgetter/setterが使える
		book.setTitle(title);
		book.setOverview(overview);
		bookService.insertBook(book);
		redirectAttributes.addFlashAttribute("complete", "登録が完了しました！"); // リダイレクト時のパラメータを設定する（登録完了メッセージ）
	
		return "redirect:/";
	}
	
	// 本の削除
	@PostMapping("/book/delete/{bookid}")
	public String deleteReview(@PathVariable Integer bookid, RedirectAttributes redirectAttributes) {
		
		reviewService.deleteAllByBookId(bookid); // Bookに紐付くreviewを全件削除
		bookService.deleteBookById(bookid); // Bookを削除
		
		redirectAttributes.addFlashAttribute("complete", "対象の本の削除が完了しました。"); // リダイレクト時のパラメータを設定する（登録完了メッセージ）
		return "redirect:/";
	}

	// RVの新規登録画面
	@GetMapping("/book/{id}/newReview")
	public String newReview(@PathVariable Integer id, Model model) { // リクエストパラメーターでbookid欲しい
		model.addAttribute("bookid", id);
		Optional<Book> bookOpt = bookService.selectOneById(id);
		if(bookOpt.isPresent()) {
			model.addAttribute("book", bookOpt.get());
		}
		return "newreview";
	}
	
	// RVの新規登録
	@PostMapping("/review/insert")
	public String insertReview(@RequestParam Integer evaluation, @RequestParam String content, @RequestParam Integer bookid, @RequestParam(defaultValue = "0") Integer userid, RedirectAttributes redirectAttributes) {
		Review review = new Review();
		review.setEvaluation(evaluation);
		review.setContent(content);
		review.setBookid(bookid);
		review.setUserid(userid);
		reviewService.insertReview(review);
		
		// RV対象のReviewを全て取得
		Iterable<Review> reviewList = reviewService.selectAllByBookId(bookid);
		
		// 取得したReviewのevaluationの平均値を算出
		Double totalEvaluation = 0.0;
		Integer counter = 0;
		for(Review tempReview : reviewList) {
			totalEvaluation += tempReview.getEvaluation();
			counter ++;
		}
		
		// bookテーブルのtotalevaluationを更新
		bookService.updateTotalevaluationById(bookid, (double)Math.round(totalEvaluation*10/counter)/10);
		
		redirectAttributes.addFlashAttribute("complete", "レビューの登録が完了しました！"); // リダイレクト時のパラメータを設定する（登録完了メッセージ）
		
		return "redirect:/book/detail/" + bookid;
	}
	
	// RVの削除
	@PostMapping("/book/detail/{bookid}/delete/{reviewid}")
	public String deleteReview(@PathVariable Integer bookid, @PathVariable Integer reviewid, RedirectAttributes redirectAttributes) {
		
		// Reviewを削除
		reviewService.deleteOneById(reviewid);
		
		redirectAttributes.addFlashAttribute("complete", "対象レビューの削除が完了しました。"); // リダイレクト時のパラメータを設定する（登録完了メッセージ）
		return "redirect:/book/detail/" + bookid;
	}
	
}
