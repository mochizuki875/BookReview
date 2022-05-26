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
	public String showHome(@RequestParam(value="user", required=false) String user, Model model) {
		model.addAttribute("user", user); // userをModelに格納
		
		Integer showFlag = 0; // Book表示フラグ
		model.addAttribute("showFlag", showFlag); // Modelに格納
		
		// Iterable<Book> bookList = bookService.selectAll(); // Book情報を全件取得
		Iterable<Book> bookList = bookService.selectTopN(10); // Book情報のうち上位10件を件取得	
		model.addAttribute("bookList", bookList); // Modelに格納

		return "home";
	}
	
	// 全ての本を表示（50件単位でページ分割した際の指定されたページ分）
	@GetMapping("/book")
	public String showAllBook(@RequestParam(value="user", required=false) String user, @RequestParam(value="page", defaultValue = "1") Integer page, Model model) {
		model.addAttribute("user", user); // userをModelに格納
		
		model.addAttribute("page", page); // pageをModelに格納
		
		Integer showFlag = 1; // Book表示フラグ
		model.addAttribute("showFlag", showFlag); // Modelに格納
		
		Integer allPages = bookService.countAllPages(50); // 全ページ数を取得
		model.addAttribute("allPages", allPages); // Modelに格納
		
		Iterable<Book> bookList = bookService.selectAllDescByPage(page,50); // Book情報を全件取得		
		model.addAttribute("bookList", bookList); // Modelに格納

		return "home";
	}	
	
	// 本の検索
	// 検索ワードをリクエストパラメータとして受け取って検索結果を返す
	@GetMapping("/book/search")
	public String searchBook(@RequestParam(value="user", required=false) String user, @RequestParam(value="keyword", required=false) String keyword, RedirectAttributes redirectAttributes, Model model) {
		model.addAttribute("user", user); // userをModelに格納
		
		Integer showFlag = 2; // Book表示フラグ
		model.addAttribute("showFlag", showFlag); // Modelに格納
		
		model.addAttribute("keyword", keyword); // keywordをModelに格納
		
		Iterable<Book> bookList = bookService.searchAll(keyword);
		model.addAttribute(bookList);

		if(keyword == null) {
			return "redirect:/" + "?user=" + user;
		}
		return "home";
	}
	
	// 本の詳細画面表示
	// 書籍IDをリクエストパラメータとして受け取って本の詳細ページを返す
	@GetMapping("/book/detail/{id}")
	public String showBook(@RequestParam(value="user", required=false) String user, @PathVariable Integer id, Model model) {
		model.addAttribute("user", user); // userをModelに格納
		
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
	public String newBook(@RequestParam(value="user", required=false) String user, Model model) {
		model.addAttribute("user", user); // userをModelに格納
		return "newbook";
	}
	
	// 本の新規登録
	@PostMapping("/book/insert")
	public String insert(@RequestParam(value="user", required=false) String user, @RequestParam String title, @RequestParam String overview, RedirectAttributes redirectAttributes, Model model) {
		model.addAttribute("user", user); // userをModelに格納
		
		Book book = new Book();
		// @DataアノテーションによりBookの各フィールドに対するgetter/setterが使える
		book.setTitle(title);
		book.setOverview(overview);
		bookService.insertOne(book);
		redirectAttributes.addFlashAttribute("complete", "登録が完了しました！"); // リダイレクト時のパラメータを設定する（登録完了メッセージ）
		
		return "redirect:/" + "?user=" + user;
		// 新しいBookを登録したいが現状bookidが取得できないので保留
//		return "redirect:/book/detail/" + bookid + "?user=" + user;
	}
	
	// 本の削除
	@PostMapping("/book/delete/{bookid}")
	public String deleteReview(@RequestParam(value="user", required=false) String user, @PathVariable Integer bookid, RedirectAttributes redirectAttributes, Model model) {
		model.addAttribute("user", user); // userをModelに格納
		
		reviewService.deleteAllByBookId(bookid); // Bookに紐付くreviewを全件削除
		bookService.deleteOneById(bookid); // Bookを削除
		
		redirectAttributes.addFlashAttribute("complete", "対象の本の削除が完了しました。"); // リダイレクト時のパラメータを設定する（登録完了メッセージ）
		return "redirect:/" + "?user=" + user;
	}

	// RVの新規登録画面
	@GetMapping("/book/{id}/newreview")
	public String newReview(@RequestParam(value="user", required=false) String user, @PathVariable Integer id, Model model) { // リクエストパラメーターでbookid欲しい
		model.addAttribute("user", user); // userをModelに格納
		
		model.addAttribute("bookid", id);
		Optional<Book> bookOpt = bookService.selectOneById(id);
		if(bookOpt.isPresent()) {
			model.addAttribute("book", bookOpt.get());
		}
		return "newreview";
	}
	
	// RVの新規登録
	@PostMapping("/review/insert")
	public String insertReview(@RequestParam(value="user", required=false) String user, @RequestParam Integer evaluation, @RequestParam String content, @RequestParam Integer bookid, @RequestParam(defaultValue = "0") Integer userid, RedirectAttributes redirectAttributes,  Model model) {
		model.addAttribute("user", user); // userをModelに格納
		
		Review review = new Review();
		review.setEvaluation(evaluation);
		review.setContent(content);
		review.setBookid(bookid);
		review.setUserid(userid);
		reviewService.insertOne(review);
		
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
		
		return "redirect:/book/detail/" + bookid + "?user=" + user;
	}
	
	// RVの削除
	@PostMapping("/book/detail/{bookid}/delete/{reviewid}")
	public String deleteReview(@RequestParam(value="user", required=false) String user, @PathVariable Integer bookid, @PathVariable Integer reviewid, RedirectAttributes redirectAttributes, Model model) {
		model.addAttribute("user", user); // userをModelに格納
		
		// Reviewを削除
		reviewService.deleteOneById(reviewid);
		
		redirectAttributes.addFlashAttribute("complete", "対象レビューの削除が完了しました。"); // リダイレクト時のパラメータを設定する（登録完了メッセージ）
		return "redirect:/book/detail/" + bookid + "?user=" + user;
	}
	
}
