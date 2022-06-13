package com.example.demo.controller;

import java.util.Optional;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

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
	// Serviceインスタンスを作成
	@Autowired
	BookService bookService;
	@Autowired
	ReviewService reviewService;
	
	Logger logger = Logger.getLogger(BookReviewController.class.getName());
	ConsoleHandler handler = new ConsoleHandler();
	
	static int topNumber = 10; // トップページの表示件数
	static int pageSize = 10; // 1ページあたりの表示件数
	
	// ホーム画面を表示
	@GetMapping("/")
	public String showHome(@RequestParam(value="user", required=false) String user, Model model) {
		logger.log(Level.INFO, "GET /?user=" + user);
		
		model.addAttribute("user", user); // userをModelに格納
		
		int showFlag = 0; // Book表示フラグ
		model.addAttribute("showFlag", showFlag); // Modelに格納
		
		logger.log(Level.INFO, "Get bookList of top " + topNumber + ".");
		logger.log(Level.FINE, "bookService.selectTopN(" + topNumber + ")");
		
		Iterable<Book> bookList = bookService.selectTopN(topNumber); // Book情報のうち上位topNumber件を件取得	
		model.addAttribute("bookList", bookList); // Modelに格納

		logger.log(Level.INFO, "return home.");
		return "home";
	}
	
	// 全ての本を表示（page件単位でページ分割した際の指定されたページ分）
	@GetMapping("/book")
	public String showAllBook(@RequestParam(value="user", required=false) String user, @RequestParam(value="page", defaultValue = "1") int page, Model model) {
		logger.log(Level.INFO, "GET /book?user=" + user + "&page=" + page);
		
		model.addAttribute("user", user); // userをModelに格納
		
		int showFlag = 1; // Book表示フラグ
		model.addAttribute("showFlag", showFlag); // showFlagをModelに格納
		
		model.addAttribute("page", page); // pageをModelに格納
		
		int allPages = bookService.countAllPages(pageSize); // 全ページ数を取得
		model.addAttribute("allPages", allPages); // allPagesをModelに格納
		
		logger.log(Level.INFO, "Get bookList of page " + page + "/" + allPages + ".(pageSize=" + pageSize + ")");
		logger.log(Level.FINE, "bookService.selectAllDescByPage(" + page + ", " + allPages + ")");
		
		Iterable<Book> bookList = bookService.selectAllDescByPage(page, pageSize); // 指定したページのBook情報一覧を取得		
		model.addAttribute("bookList", bookList); // bookListをModelに格納
		
		logger.log(Level.INFO, "return home.");
		return "home";
	}	
	
	// 本の検索
	// 検索ワードをリクエストパラメータとして受け取って検索結果を返す（page件単位でページ分割した際の指定されたページ分）
	@GetMapping("/book/search")
	public String searchBook(@RequestParam(value="user", required=false) String user, @RequestParam(value="keyword", required=false) String keyword, @RequestParam(value="page", defaultValue = "1") int page, RedirectAttributes redirectAttributes, Model model) {
		logger.log(Level.INFO, "GET /book/search?user=" + user + "&keyword=" + keyword + "&page=" + page);
		
		model.addAttribute("user", user); // userをModelに格納		
				
		int showFlag = 2; // Book表示フラグ
		model.addAttribute("showFlag", showFlag); // showFlagをModelに格納
		
		model.addAttribute("page", page); // pageをModelに格納

		model.addAttribute("keyword", keyword); // keywordをModelに格納

		int allPages = bookService.countSearchAllPages(keyword, pageSize); // 検索結果の全ページ数を取得
		model.addAttribute("allPages", allPages); // allPagesをModelに格納
		
		logger.log(Level.INFO, "Search keyword=" + keyword + " and get bookList of page " + page + "/" + allPages  + ".(pageSize=" + pageSize + ")");
		logger.log(Level.FINE, "bookService.searchAllDescByPage(" + keyword + ", " +  page + ", " +  pageSize + ")");
		
		Iterable<Book> bookList = bookService.searchAllDescByPage(keyword, page, pageSize); // keyword検索結果のうち指定したページのBook情報一覧を取得
		model.addAttribute(bookList); // bookListをModelに格納
				
		if(keyword == "") { // keywordが指定されていない場合はトップページに戻る
			logger.log(Level.INFO, "return home.");
			return "redirect:/" + "?user=" + user;
		}
		logger.log(Level.INFO, "return home.");
		return "home";
	}
	
	// 本の詳細画面表示
	// 書籍IDをリクエストパラメータとして受け取って本の詳細ページを返す
	@GetMapping("/book/{bookid}/detail")
	public String showBook(@RequestParam(value="user", required=false) String user, @PathVariable int bookid, Model model) {
		logger.log(Level.INFO, "GET /book/" + bookid + "/detail?user=" + user);
		
		model.addAttribute("user", user); // userをModelに格納
		
		logger.log(Level.INFO, "Get book.");
		logger.log(Level.FINE, "bookService.selectOneById(" + bookid + ")");
		
		Optional<Book> bookOpt = bookService.selectOneById(bookid); // bookidから本の詳細情報を取得
		if(bookOpt.isPresent()) {
			logger.log(Level.FINE, "bookOpt.isPresent()=true");
			model.addAttribute("book", bookOpt.get()); // bookをModelに格納
		}
		
		logger.log(Level.INFO, "Get book reviews.");
		logger.log(Level.FINE, "reviewService.selectAllByBookId(" + bookid + ")");
		
		Iterable<Review> reviewList = reviewService.selectAllByBookId(bookid); // 本のRVを取得
		model.addAttribute("reviewList", reviewList); // reviewListをModelに格納
		
		logger.log(Level.INFO, "return detail.");
		return "detail";
	}
	
	// 本の新規登録画面
	@GetMapping("/book/newbook")
	public String newBook(@RequestParam(value="user", required=false) String user, Model model) {
		logger.log(Level.INFO, "GET /book/newbook?user=" + user);
		
		model.addAttribute("user", user); // userをModelに格納
		
		boolean editFlag = false; // 編集フラグ（false:新規, true:編集）
		model.addAttribute("editFlag", editFlag); // editFlagをModelに格納
		
		logger.log(Level.INFO, "return editbook with editFlag=" + editFlag);
		return "editbook";
	}
	
	// 本の編集画面
	@GetMapping("/book/{bookid}/edit")
	public String editBook(@RequestParam(value="user", required=false) String user, @PathVariable(value="bookid", required=true) int bookid, Model model) {
		logger.log(Level.INFO, "GET /book/" + bookid + "/edit?user=" + user);
		
		model.addAttribute("user", user); // userをModelに格納
		
		boolean editFlag = true; // 編集フラグ（false:新規, true:編集）
		model.addAttribute("editFlag", editFlag); // editFlagをModelに格納
		
		logger.log(Level.INFO, "Get book.");
		logger.log(Level.FINE, "bookService.selectOneById(" + bookid + ")");
		
		Optional<Book> bookOpt = bookService.selectOneById(bookid); // bookidから本の詳細情報を取得
		if(bookOpt.isPresent()) {
			logger.log(Level.FINE, "bookOpt.isPresent()=true");
			model.addAttribute("book", bookOpt.get()); // BookをModelに格納
		}
		
		logger.log(Level.INFO, "return editbook with editFlag=" + editFlag);
		return "editbook";
	}
	
	// 本の情報を新規登録
	@PostMapping("/book/insert")
	public String insert(@RequestParam(value="user", required=false) String user, @RequestParam String title, @RequestParam String overview, Model model) {
		logger.log(Level.INFO, "POST /book/insert");
		logger.log(Level.INFO, "user: " + user);
		logger.log(Level.INFO, "title: " + title);
		logger.log(Level.INFO, "overview: " + overview);
		
		model.addAttribute("user", user); // userをModelに格納
		
		logger.log(Level.INFO, "Create book instance.");
		Book book = new Book();
		book.setTitle(title);
		book.setOverview(overview);
		
		logger.log(Level.INFO, "Insert book.");
		logger.log(Level.FINE, "bookService.insertOne(" + book + ")");
		
		book = bookService.insertOne(book); // Bookの新規登録
		
		logger.log(Level.INFO, "return redirect:/book/" + book.getId() + "/detail/?user=" + user);
		return "redirect:/book/" + book.getId() + "/detail/?user=" + user; // 登録したBookの詳細ページを返す
	}
	
	// 本の情報を更新
	@PostMapping("/book/{bookid}/update")
	public String update(@RequestParam(value="user", required=false) String user, @RequestParam String title, @RequestParam String overview, @PathVariable(value="bookid", required=true) int bookid, Model model) {
		logger.log(Level.INFO, "POST /book/" + bookid + "/update");
		logger.log(Level.INFO, "user: " + user);
		logger.log(Level.INFO, "title: " + title);
		logger.log(Level.INFO, "overview: " + overview);
		
		model.addAttribute("user", user); // userをModelに格納
		
		Book book = new Book();
		
		logger.log(Level.INFO, "Get book.");
		logger.log(Level.FINE, "bookService.selectOneById(" + bookid + ")");
		
		Optional<Book> bookOpt = bookService.selectOneById(bookid); // bookidから本の詳細情報を取得
		if(bookOpt.isPresent()) {
			logger.log(Level.INFO, "Create Book instance.");
			
			book = bookOpt.get();
			book.setTitle(title);
			book.setOverview(overview);
			book = bookService.updateOne(book); // Bookの更新
		}
		
		logger.log(Level.INFO, "return redirect:/book/" + book.getId() + "/detail/?user=" + user);
		return "redirect:/book/" + book.getId() + "/detail?user=" + user; // 登録したBookの詳細ページを返す
	}
	
	// 本の削除
	@PostMapping("/book/{bookid}/delete")
	public String deleteBook(@RequestParam(value="user", required=false) String user, @PathVariable int bookid, RedirectAttributes redirectAttributes, Model model) {
		logger.log(Level.INFO, "DELETE /book/" + bookid + "/delete");
		logger.log(Level.INFO, "user: " + user);
		
		model.addAttribute("user", user); // userをModelに格納
		
		logger.log(Level.INFO, "Delete all reviewsrelated to bookid =" + bookid + ".");
		logger.log(Level.FINE, "reviewService.deleteAllByBookId(" + bookid + ")");
		reviewService.deleteAllByBookId(bookid); // Bookに紐付くReviewを全件削除
		
		logger.log(Level.INFO, "Delete book bookid =" + bookid + ".");
		logger.log(Level.FINE, "bookService.deleteOneById(" + bookid + ")");
		bookService.deleteOneById(bookid); // Bookを削除
		
		redirectAttributes.addFlashAttribute("complete", "対象の本の削除が完了しました。"); // リダイレクト時のパラメータを設定する（削除完了メッセージ）
		
		logger.log(Level.INFO, "redirect:/?user=" + user);
		return "redirect:/" + "?user=" + user;
	}

	// RVの新規登録画面
	@GetMapping("/book/{bookid}/newreview")
	public String newReview(@RequestParam(value="user", required=false) String user, @PathVariable int bookid, Model model) {
		logger.log(Level.INFO, "GET /book/" + bookid + "/newreview?user=" + user);
		
		model.addAttribute("user", user); // userをModelに格納
		model.addAttribute("bookid", bookid); // BookのidをModelに格納
		
		logger.log(Level.INFO, "Get book.");
		logger.log(Level.FINE, "bookService.selectOneById(" + bookid + ")");
		
		Optional<Book> bookOpt = bookService.selectOneById(bookid); // idをベースにBookを取得
		if(bookOpt.isPresent()) {
			model.addAttribute("book", bookOpt.get()); // BookをModelに格納
		}
		
		logger.log(Level.INFO, "return newreview.");
		return "newreview";
	}
	
	// RVの新規登録
	@PostMapping("/review/insert")
	public String insertReview(@RequestParam(value="user", required=false) String user, @RequestParam int evaluation, @RequestParam String content, @RequestParam int bookid, @RequestParam(defaultValue = "0") int userid, RedirectAttributes redirectAttributes,  Model model) {
		logger.log(Level.INFO, "POST /review/insert");
		logger.log(Level.INFO, "user: " + user);
		logger.log(Level.INFO, "evaluation: " + evaluation);
		logger.log(Level.INFO, "content: " + content);
		logger.log(Level.INFO, "bookid: " + bookid);
		logger.log(Level.INFO, "userid: " + userid);
		
		model.addAttribute("user", user); // userをModelに格納
		
		logger.log(Level.INFO, "Create Review instance.");
		Review review = new Review();
		review.setEvaluation(evaluation);
		review.setContent(content);
		review.setBookid(bookid);
		review.setUserid(userid);
		
		logger.log(Level.INFO, "Insert review.");
		logger.log(Level.FINE, "reviewService.insertOne(" + review + ")");
		reviewService.insertOne(review); // Reviewを登録
		
		logger.log(Level.INFO, "Get reviews of bookid=" + bookid + ".");
		logger.log(Level.FINE, "reviewService.selectAllByBookId(" + bookid + ")");
		Iterable<Review> reviewList = reviewService.selectAllByBookId(bookid); // 対象BookのReviewを全て取得
		
		// 取得したReviewのevaluationの平均値を算出
		double totalEvaluation = 0.0;
		int counter = 0;
		for(Review tempReview : reviewList) {
			totalEvaluation += tempReview.getEvaluation();
			counter ++;
		}
		
		logger.log(Level.INFO, "Set totalEvaluation.");
		logger.log(Level.FINE, "bookService.updateTotalevaluationById(" + bookid + ", " + (double)Math.round(totalEvaluation*10/counter)/10 + ")");
		bookService.updateTotalevaluationById(bookid, (double)Math.round(totalEvaluation*10/counter)/10); // 対象Bookのtotalevaluationを更新
		
		redirectAttributes.addFlashAttribute("complete", "レビューの登録が完了しました！"); // リダイレクト時のパラメータを設定する（登録完了メッセージ）
		
		logger.log(Level.INFO, "return redirect:/book/" + bookid + "/detail?user=" + user);
		return "redirect:/book/" + bookid + "/detail?user=" + user;
	}
	
	// RVの削除
	@PostMapping("/book/detail/{bookid}/delete/{reviewid}")
	public String deleteReview(@RequestParam(value="user", required=false) String user, @PathVariable int bookid, @PathVariable int reviewid, RedirectAttributes redirectAttributes, Model model) {
		logger.log(Level.INFO, "DELETE /book/detail/ + " + bookid + "/delete/" + reviewid);
		logger.log(Level.INFO, "user: " + user);
		
		model.addAttribute("user", user); // userをModelに格納
		
		logger.log(Level.INFO, "Delete review of reviewid=" + reviewid + ".");
		logger.log(Level.FINE, "reviewService.deleteOneById(" + reviewid + ")");
		reviewService.deleteOneById(reviewid); // idを指定してReviewを削除
		
		redirectAttributes.addFlashAttribute("complete", "対象レビューの削除が完了しました。"); // リダイレクト時のパラメータを設定する（登録完了メッセージ）
		
		logger.log(Level.INFO, "redirect:/book/" + bookid + "/detail?user=" + user);
		return "redirect:/book/" + bookid + "/detail?user=" + user;
	}
	
}
