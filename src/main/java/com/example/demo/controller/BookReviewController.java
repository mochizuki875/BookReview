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
	
	static Integer topNumber = 10; // トップページの表示件数
	static Integer pageSize = 30; // 1ページあたりの表示件数
	
	// ホーム画面を表示
	@GetMapping("/")
	public String showHome(@RequestParam(value="user", required=false) String user, Model model) {
		model.addAttribute("user", user); // userをModelに格納
		
		Integer showFlag = 0; // Book表示フラグ
		model.addAttribute("showFlag", showFlag); // Modelに格納
		
		Iterable<Book> bookList = bookService.selectTopN(topNumber); // Book情報のうち上位topNumber件を件取得	
		model.addAttribute("bookList", bookList); // Modelに格納

		return "home";
	}
	
	// 全ての本を表示（page件単位でページ分割した際の指定されたページ分）
	@GetMapping("/book")
	public String showAllBook(@RequestParam(value="user", required=false) String user, @RequestParam(value="page", defaultValue = "1") Integer page, Model model) {
		model.addAttribute("user", user); // userをModelに格納
		
		model.addAttribute("page", page); // pageをModelに格納
		
		Integer allPages = bookService.countAllPages(pageSize); // 全ページ数を取得
		model.addAttribute("allPages", allPages); // Modelに格納
		
		Integer showFlag = 1; // Book表示フラグ
		model.addAttribute("showFlag", showFlag); // Modelに格納
		
		Iterable<Book> bookList = bookService.selectAllDescByPage(page, pageSize); // Book情報を取得		
		model.addAttribute("bookList", bookList); // Modelに格納

		return "home";
	}	
	
	// 本の検索
	// 検索ワードをリクエストパラメータとして受け取って検索結果を返す（page件単位でページ分割した際の指定されたページ分）
	@GetMapping("/book/search")
	public String searchBook(@RequestParam(value="user", required=false) String user, @RequestParam(value="keyword", required=false) String keyword, @RequestParam(value="page", defaultValue = "1") Integer page, RedirectAttributes redirectAttributes, Model model) {
		model.addAttribute("user", user); // userをModelに格納		
		model.addAttribute("page", page); // pageをModelに格納
		model.addAttribute("keyword", keyword); // keywordをModelに格納
				
		Integer showFlag = 2; // Book表示フラグ
		model.addAttribute("showFlag", showFlag); // Modelに格納

		Iterable<Book> bookList = bookService.searchAllDescByPage(keyword, page, pageSize); // Book情報を取得
		model.addAttribute(bookList);
		
		// (バグ)この時点でbookListには1ページ分の結果しか入ってないのでこれをもとに全ページ数取るとおかしい
		// Integer allPages = bookService.countAllPages(pageSize); // 全ページ数を取得（★もうちょい綺麗にやれそう）
		Integer allPages = bookService.countSearchAllPages(keyword, pageSize);
		
		model.addAttribute("allPages", allPages); // Modelに格納
		
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
		
		Optional<Book> bookOpt = bookService.selectOneById(id); // 本の情報を取得
		if(bookOpt.isPresent()) {
			model.addAttribute("book", bookOpt.get());
		}
		
		Iterable<Review> reviewList = reviewService.selectAllByBookId(id); // 本のRVを取得
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
	public String insert(@RequestParam(value="user", required=false) String user, @RequestParam String title, @RequestParam String overview, Model model) {
		model.addAttribute("user", user); // userをModelに格納
		
		Book book = new Book();
		book.setTitle(title);
		book.setOverview(overview);
		book = bookService.insertOne(book); // Bookの新規登録
		
		return "redirect:/book/detail/" + book.getId() + "?user=" + user; // 登録したBookの詳細ページを返す
	}
	
	// 本の削除
	@PostMapping("/book/delete/{bookid}")
	public String deleteReview(@RequestParam(value="user", required=false) String user, @PathVariable Integer bookid, RedirectAttributes redirectAttributes, Model model) {
		model.addAttribute("user", user); // userをModelに格納
		
		reviewService.deleteAllByBookId(bookid); // Bookに紐付くReviewを全件削除
		bookService.deleteOneById(bookid); // Bookを削除
		
		redirectAttributes.addFlashAttribute("complete", "対象の本の削除が完了しました。"); // リダイレクト時のパラメータを設定する（削除完了メッセージ）
		return "redirect:/" + "?user=" + user;
	}

	// RVの新規登録画面
	@GetMapping("/book/{id}/newreview")
	public String newReview(@RequestParam(value="user", required=false) String user, @PathVariable Integer id, Model model) { // リクエストパラメーターでbookid欲しい
		model.addAttribute("user", user); // userをModelに格納
		model.addAttribute("bookid", id); // BookのidをModelに格納
		
		Optional<Book> bookOpt = bookService.selectOneById(id); // idをベースにBookを取得
		if(bookOpt.isPresent()) {
			model.addAttribute("book", bookOpt.get()); // BookをModelに格納
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
		
		reviewService.insertOne(review); // Reviewを登録
		
		Iterable<Review> reviewList = reviewService.selectAllByBookId(bookid); // 対象BookのReviewを全て取得
		
		// 取得したReviewのevaluationの平均値を算出
		Double totalEvaluation = 0.0;
		Integer counter = 0;
		for(Review tempReview : reviewList) {
			totalEvaluation += tempReview.getEvaluation();
			counter ++;
		}
		
		bookService.updateTotalevaluationById(bookid, (double)Math.round(totalEvaluation*10/counter)/10); // 対象Bookのtotalevaluationを更新
		
		redirectAttributes.addFlashAttribute("complete", "レビューの登録が完了しました！"); // リダイレクト時のパラメータを設定する（登録完了メッセージ）
		
		return "redirect:/book/detail/" + bookid + "?user=" + user;
	}
	
	// RVの削除
	@PostMapping("/book/detail/{bookid}/delete/{reviewid}")
	public String deleteReview(@RequestParam(value="user", required=false) String user, @PathVariable Integer bookid, @PathVariable Integer reviewid, RedirectAttributes redirectAttributes, Model model) {
		model.addAttribute("user", user); // userをModelに格納
		
		reviewService.deleteOneById(reviewid); // idを指定してReviewを削除
		
		redirectAttributes.addFlashAttribute("complete", "対象レビューの削除が完了しました。"); // リダイレクト時のパラメータを設定する（登録完了メッセージ）
		return "redirect:/book/detail/" + bookid + "?user=" + user;
	}
	
}
