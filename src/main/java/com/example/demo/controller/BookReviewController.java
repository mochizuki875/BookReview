package com.example.demo.controller;

import java.util.ArrayList;
import java.util.Optional;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
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
	
	final int TOP_NUMBER = 10; // トップページの表示件数
	final int PAGE_SIZE = 10; // 1ページあたりの表示件数
	
	// Book一覧表示
	@GetMapping("/")
	public String showHome(@RequestParam(value="user", required=false) String user, Model model) {
		try {
			logger.log(Level.INFO, "GET /?user=" + user);
			
			model.addAttribute("user", user); // userをModelに格納
			
			int showFlag = 0; // Book表示フラグ
			model.addAttribute("showFlag", showFlag); // Modelに格納
			
			try {
				logger.log(Level.INFO, "Get BookList of top " + TOP_NUMBER + ".");
				logger.log(Level.FINE, "BookService.selectTopN(" + TOP_NUMBER + ")");
				Iterable<Book> bookList = bookService.selectTopN(TOP_NUMBER); // Book情報のうち上位topNumber件を件取得	
				logger.log(Level.INFO, "Success to get BookList.");
				logger.log(Level.INFO, "BookList has returned.");
				
				model.addAttribute("bookList", bookList); // Modelに格納
			}
			catch(Exception e) {
				// エラーでも画面表示できるように空のレスポンスを返す
				logger.log(Level.SEVERE, "Faild to get BookList.");
				Iterable<Book> bookList = new ArrayList<>();
				model.addAttribute("bookList", bookList);
			}
			
			logger.log(Level.INFO, "Return home page and show BookList.");
			return "home";
		}
		catch(Exception e) {
			logger.log(Level.SEVERE, "Catch Exception");
			logger.log(Level.SEVERE, "Internal Server Error");
			throw e;
		}
	}
	
	// Book表示（page件単位でページ分割した際の指定されたページ分）
	@GetMapping("/book")
	public String showAllBook(@RequestParam(value="user", required=false) String user, @RequestParam(value="page", defaultValue = "1") int page, Model model) {
		try {
			logger.log(Level.INFO, "GET /book?user=" + user + "&page=" + page);
			
			model.addAttribute("user", user); // userをModelに格納
			
			int showFlag = 1; // Book表示フラグ
			model.addAttribute("showFlag", showFlag); // showFlagをModelに格納
			
			model.addAttribute("page", page); // pageをModelに格納
			
			int allPages = bookService.countAllPages(PAGE_SIZE); // 全ページ数を取得
			model.addAttribute("allPages", allPages); // allPagesをModelに格納
			
			try {
			logger.log(Level.INFO, "Get BookList of page " + page + "/" + allPages + ".(PAGE_SIZE=" + PAGE_SIZE + ")");
			logger.log(Level.FINE, "bookService.selectAllDescByPage(" + page + ", " + allPages + ")");
			Iterable<Book> bookList = bookService.selectAllDescByPage(page, PAGE_SIZE); // 指定したページのBook情報一覧を取得	
			logger.log(Level.INFO, "Success to get BookList.(page = " + page + ")");
			logger.log(Level.INFO, "BookList has returned.(page = " + page + ")");
			
			model.addAttribute("bookList", bookList); // bookListをModelに格納
			}
			catch(Exception e) {
				// エラーでも画面表示できるように空のレスポンスを返す
				logger.log(Level.SEVERE, "Faild to get BookList.");
				Iterable<Book> bookList = new ArrayList<>();
				model.addAttribute("bookList", bookList);
			}
			
			logger.log(Level.INFO, "Return home page and show bookList. (page = " + page + ")");
			return "home";
		}
		catch(Exception e) {
			logger.log(Level.SEVERE, "Catch Exception");
			logger.log(Level.SEVERE, "Internal Server Error");
			throw e;
		}
	}	
	
	// Book検索
	// 検索ワードをリクエストパラメータとして受け取って検索結果を返す（page件単位でページ分割した際の指定されたページ分）
	@GetMapping("/book/search")
	public String searchBook(@RequestParam(value="user", required=false) String user, @RequestParam(value="keyword", required=false) String keyword, @RequestParam(value="page", defaultValue = "1") int page, RedirectAttributes redirectAttributes, Model model) {
		try {
		    logger.log(Level.INFO, "GET /book/search?user=" + user + "&keyword=" + keyword + "&page=" + page);
		    
		    if(keyword == "" | keyword == null) { // keywordが指定されていない場合はトップページに戻る				
				logger.log(Level.INFO, "Redirect to /?user=" + user + ".(keyword = null)");
				return "redirect:/" + "?user=" + user;
			} else { // keywordが指定されていれば検索処理実行
				model.addAttribute("user", user); // userをModelに格納
				model.addAttribute("keyword", keyword); // keywordをModelに格納
						
				int showFlag = 2; // Book表示フラグ
				model.addAttribute("showFlag", showFlag); // showFlagをModelに格納
				
				try {
					int allPages = bookService.countSearchAllPages(keyword, PAGE_SIZE); // 検索結果の全ページ数を取得
					
					logger.log(Level.INFO, "Search keyword=" + keyword + " and get bookList of page " + page + "/" + allPages  + ".(pageSize=" + PAGE_SIZE + ")");
					logger.log(Level.FINE, "bookService.searchAllDescByPage(" + keyword + ", " +  page + ", " +  PAGE_SIZE + ")");
					Iterable<Book> bookList = bookService.searchAllDescByPage(keyword, page, PAGE_SIZE); // keyword検索結果のうち指定したページのBook情報一覧を取得
					logger.log(Level.INFO, "Success to get BookList.(keyword = " + keyword + ", page = " + page + ")");
					logger.log(Level.INFO, "BookList has returned.(keyword = " + keyword + ", page = " + page + ")");
					
					model.addAttribute(bookList); // bookListをModelに格納
					model.addAttribute("page", page); // pageをModelに格納
					model.addAttribute("allPages", allPages); // allPagesをModelに格納
				}
				catch (Exception e) {
					// エラーでも画面表示できるように空のレスポンスを返す
					logger.log(Level.SEVERE, "Faild to get BookList.");
					Iterable<Book> bookList = new ArrayList<>();
					model.addAttribute("bookList", bookList);
					model.addAttribute("page", 0); //  pageをModelに格納
					model.addAttribute("allPages", 0); //  allPagesをModelに格納
				}
				logger.log(Level.INFO, "Return home page and show BookList. (keyword = " + keyword + ", page = " + page + ")");
				return "home";
			}
		}
		catch (Exception e) {
			logger.log(Level.SEVERE, "Catch Exception");
			logger.log(Level.SEVERE, "Internal Server Error");
			throw e;
		}
	}
	
	// Book画面表示
	// bookidをリクエストパラメータとして受け取ってBook詳細ページを返す
	@GetMapping("/book/{bookid}")
	public String showBook(@RequestParam(value="user", required=false) String user, @PathVariable int bookid, Model model) {
		try {
			logger.log(Level.INFO, "GET /book/" + bookid + "?user=" + user);
			
			model.addAttribute("user", user); // userをModelに格納
			
			try {
				logger.log(Level.INFO, "Get Book.(bookid = " + bookid + ")");
				logger.log(Level.FINE, "bookService.selectOneById(" + bookid + ")");
				
				Optional<Book> bookOpt = bookService.selectOneById(bookid); // bookidから本の詳細情報を取得
				if(bookOpt.isPresent()) {
					logger.log(Level.INFO, "Success to get Book.(bookid = " + bookid + ")");
					logger.log(Level.INFO, "Book has returned.(bookid = " + bookid + ")");
					logger.log(Level.FINE, "bookOpt.isPresent()=true");
					model.addAttribute("book", bookOpt.get()); // bookをModelに格納
				} else {
					// Bookを取得できなくても画面表示できるように空のレスポンスを返す
					logger.log(Level.SEVERE, "Book was not found.(bookid = " + bookid + ")");
					Book book = new Book();
					model.addAttribute("book", book); // bookをModelに格納
				}
			}
			catch (Exception e) {
				// エラーでも画面表示できるように空のレスポンスを返す
				logger.log(Level.SEVERE, "Faild to get Book.(bookid = " + bookid + ")");
				Book book = new Book();
				model.addAttribute("book", book); // bookをModelに格納
			}
			
			try {
				logger.log(Level.INFO, "Get ReviewList.(bookid = " + bookid + ")");
				logger.log(Level.FINE, "reviewService.selectAllByBookId(" + bookid + ")");
				Iterable<Review> reviewList = reviewService.selectAllByBookId(bookid); // 本のRVを取得
				logger.log(Level.INFO, "Success to get ReviewList.(bookid = " + bookid + ")");
				logger.log(Level.INFO, "ReviewList has returned.(bookid = " + bookid + ")");
				model.addAttribute("reviewList", reviewList); // reviewListをModelに格納
			}
			catch (Exception e) {
				// エラーでも画面表示できるように空のレスポンスを返す
				logger.log(Level.SEVERE, "Faild to get ReviewList.(bookid = " + bookid + ")");
				Iterable<Review> reviewList = new ArrayList<>();
				model.addAttribute("reviewList", reviewList); // reviewListをModelに格納
			}
			
			logger.log(Level.INFO, "Return Book page and show ReviewList. (bookid = " + bookid + ")");
			return "detail";
		}
		catch (Exception e) {
			logger.log(Level.SEVERE, "Catch Exception");
			logger.log(Level.SEVERE, "Internal Server Error");
			throw e;
		}
	}
	
	// Book新規登録画面
	@GetMapping("/book/newbook")
	public String newBook(@RequestParam(value="user", required=false) String user, Model model) {
		try {
			logger.log(Level.INFO, "GET /book/newbook?user=" + user);
			
			model.addAttribute("user", user); // userをModelに格納
			
			boolean editFlag = false; // 編集フラグ（false:新規, true:編集）
			model.addAttribute("editFlag", editFlag); // editFlagをModelに格納
			
			logger.log(Level.INFO, "Return editbook.(editFlag =" + editFlag + ")");
			return "editbook";
		}
		catch (Exception e) {
			logger.log(Level.SEVERE, "Catch Exception");
			logger.log(Level.SEVERE, "Internal Server Error");
			throw e;
		} 
	}
	
	// Book編集画面
	@GetMapping("/book/{bookid}/edit")
	public String editBook(@RequestParam(value="user", required=false) String user, @PathVariable(value="bookid", required=true) int bookid, RedirectAttributes redirectAttributes, Model model) {
		try {
			logger.log(Level.INFO, "GET /book/" + bookid + "/edit?user=" + user);
			
			model.addAttribute("user", user); // userをModelに格納
			
			boolean editFlag = true; // 編集フラグ（false:新規, true:編集）
			model.addAttribute("editFlag", editFlag); // editFlagをModelに格納
			
			logger.log(Level.INFO, "Get Book.(bookid = " + bookid + ")");
			logger.log(Level.FINE, "bookService.selectOneById(" + bookid + ")");
			
			Optional<Book> bookOpt = bookService.selectOneById(bookid); // bookidから本の詳細情報を取得
			if(bookOpt.isPresent()) {
				logger.log(Level.INFO, "Success to get Book.(bookid = " + bookid + ")");
				logger.log(Level.INFO, "Book has returned.(bookid = " + bookid + ")");
				logger.log(Level.FINE, "bookOpt.isPresent()=true");
				model.addAttribute("book", bookOpt.get()); // BookをModelに格納
				
			} else {
				// Bookを取得できない場合はhome画面にリダイレクト
				logger.log(Level.SEVERE, "Book was not found.(bookid = " + bookid + ")");
				redirectAttributes.addFlashAttribute("complete", "本の情報が見つかりませんでした。"); // リダイレクト時のパラメータを設定する（登録成功メッセージ）
				return "redirect:/" + "?user=" + user;
			}
			
			logger.log(Level.INFO, "Return editbook.(editFlag =" + editFlag + ")");
			return "editbook";
		}
		catch (Exception e) {
			logger.log(Level.SEVERE, "Catch Exception");
			logger.log(Level.SEVERE, "Internal Server Error");
			throw e;
		} 
	}
	
	// Book新規登録
	@PostMapping("/book/insert")
	public String insert(@RequestParam(value="user", required=false) String user, @RequestParam String title, @RequestParam String overview, RedirectAttributes redirectAttributes, Model model) {
		try { 
			logger.log(Level.INFO, "POST /book/insert");
			logger.log(Level.INFO, " user: " + user);
			logger.log(Level.INFO, " title: " + title);
			logger.log(Level.INFO, " overview: " + overview);
			
			model.addAttribute("user", user); // userをModelに格納
			
			logger.log(Level.INFO, "Create Book Instance.");
			Book book = new Book();
			book.setTitle(title);
			book.setOverview(overview);
			
			logger.log(Level.INFO, "Insert Book.");
			logger.log(Level.FINE, "bookService.insertOne(" + book + ")");
			book = bookService.insertOne(book); // Bookの新規登録
			logger.log(Level.INFO, "Success to insert Book.(bookid = " + book.getId() + ")");
			logger.log(Level.INFO, "Book has returned.(bookid = " + book.getId() + ")");
			
			redirectAttributes.addFlashAttribute("complete", "本の登録が完了しました。"); // リダイレクト時のパラメータを設定する（登録成功メッセージ）
			logger.log(Level.INFO, "Redirect to /book/" + book.getId() + "?user=" + user);
			return "redirect:/book/" + book.getId() + "?user=" + user; // 登録したBookの詳細ページを返す
		}	
		catch (Exception e) { // Book新規登録処理でエラーが発生した場合はhome画面にリダイレクト
			logger.log(Level.SEVERE, "Internal Server Error");
			
			// homeにリダイレクト
			redirectAttributes.addFlashAttribute("complete", "本の登録に失敗しました。"); // リダイレクト時のパラメータを設定する（登録失敗メッセージ）
			logger.log(Level.INFO, "Redirect to /?user=" + user);
			return "redirect:/?user=" + user;
		} 
	}
	
	// Book更新
	@PostMapping("/book/{bookid}/update")
	public String update(@RequestParam(value="user", required=false) String user, @RequestParam String title, @RequestParam String overview, @PathVariable(value="bookid", required=true) int bookid, RedirectAttributes redirectAttributes, Model model) {
		try {
			logger.log(Level.INFO, "POST /book/" + bookid + "/update");
			logger.log(Level.INFO, " user: " + user);
			logger.log(Level.INFO, " title: " + title);
			logger.log(Level.INFO, " overview: " + overview);
			
			model.addAttribute("user", user); // userをModelに格納
			
			logger.log(Level.INFO, "Create Book Instance.");
			Book book = new Book();
			book.setTitle(title);
			book.setOverview(overview);
			
			logger.log(Level.INFO, "Update book.(bookid = " + bookid + ")");
			book = bookService.updateOne(book); // Bookの更新
			logger.log(Level.INFO, "Success to update Book.(bookid = " + book.getId() + ")");
			logger.log(Level.INFO, "Book has returned.(bookid = " + book.getId() + ")");
			
			redirectAttributes.addFlashAttribute("complete", "本の更新が完了しました。"); // リダイレクト時のパラメータを設定する（更新成功メッセージ）
			logger.log(Level.INFO, "Redirect to /book/" + book.getId() + "?user=" + user);
			return "redirect:/book/" + book.getId() + "?user=" + user; // 登録したBookの詳細ページを返す
		}
		catch (Exception e) { // Book更新処理でエラーが発生した場合はdetail画面にリダイレクト
			logger.log(Level.SEVERE, "Internal Server Error");
			
			redirectAttributes.addFlashAttribute("complete", "本の更新に失敗しました。"); // リダイレクト時のパラメータを設定する（更新失敗メッセージ）
			logger.log(Level.INFO, "Redirect to /book/" + bookid + "?user=" + user);
			return "redirect:/book/" + bookid + "?user=" + user;
		}
	}
	
	// Book削除
	@DeleteMapping("/book/{bookid}")
	public String deleteBook(@RequestParam(value="user", required=false) String user, @PathVariable int bookid, RedirectAttributes redirectAttributes, Model model) {
		try {
			logger.log(Level.INFO, "DELETE /book/" + bookid + "?user=" + user);
			
			model.addAttribute("user", user); // userをModelに格納
			
			logger.log(Level.INFO, "Delete all Review related to bookid = " + bookid + ".");
			logger.log(Level.FINE, "reviewService.deleteAllByBookId(" + bookid + ")");
			reviewService.deleteAllByBookId(bookid); // Bookに紐付くReviewを全件削除
			logger.log(Level.INFO, "Review has deleted.(Related to bookid = " + bookid + ")");
			
			
			logger.log(Level.INFO, "Delete Book. (bookid = " + bookid + ")");
			logger.log(Level.FINE, "bookService.deleteOneById(" + bookid + ")");
			bookService.deleteOneById(bookid); // Bookを削除
			logger.log(Level.INFO, "Success to delete Book.(bookid = " + bookid + ")");
			logger.log(Level.INFO, "Book has deleted.(bookid = " + bookid + ")");
			
			redirectAttributes.addFlashAttribute("complete", "対象の本の削除が完了しました。"); // リダイレクト時のパラメータを設定する（削除完了メッセージ）
			
			logger.log(Level.INFO, "redirect:/?user=" + user);
			return "Redirect to /" + "?user=" + user;
		}
		catch (Exception e) {
			logger.log(Level.SEVERE, "Internal Server Error");
			
			// Book削除処理でエラーが失敗した場合はhomeにリダイレクト
			redirectAttributes.addFlashAttribute("complete", "本の削除に失敗しました。"); // リダイレクト時のパラメータを設定する（削除失敗メッセージ）
			logger.log(Level.INFO, "Redirect to /?user=" + user);
			return "redirect:/?user=" + user;
		} 
	}

	// Review新規登録画面
	@GetMapping("/book/{bookid}/newreview")
	public String newReview(@RequestParam(value="user", required=false) String user, @PathVariable int bookid, RedirectAttributes redirectAttributes, Model model) {
		try {
			logger.log(Level.INFO, "GET /book/" + bookid + "/newreview?user=" + user);
			
			model.addAttribute("user", user); // userをModelに格納
			model.addAttribute("bookid", bookid); // BookのidをModelに格納
			
			logger.log(Level.INFO, "Get Book.(bookid = " + bookid + ")");
			logger.log(Level.FINE, "bookService.selectOneById(" + bookid + ")");
			Optional<Book> bookOpt = bookService.selectOneById(bookid); // idをベースにBookを取得
			if(bookOpt.isPresent()) {
				logger.log(Level.INFO, "Success to get Book.(bookid = " + bookid + ")");
				logger.log(Level.INFO, "Book has returned.(bookid = " + bookid + ")");
				model.addAttribute("book", bookOpt.get()); // BookをModelに格納
			} else {
				// Bookを取得できない場合はhome画面にリダイレクト
				logger.log(Level.SEVERE, "Book was not found.(bookid = " + bookid + ")");
				redirectAttributes.addFlashAttribute("complete", "本の情報が見つかりませんでした。"); // リダイレクト時のパラメータを設定する（登録成功メッセージ）
				return "redirect:/" + "?user=" + user;
			}
			
			logger.log(Level.INFO, "Return newreview.");
			return "newreview";
		}
		catch (Exception e) {
			logger.log(Level.SEVERE, "Catch Exception");
			logger.log(Level.SEVERE, "Internal Server Error");
			throw e;
		} 
	}
	
	// Review新規登録
	@PostMapping("/review/insert")
	public String insertReview(@RequestParam(value="user", required=false) String user, @RequestParam int evaluation, @RequestParam String content, @RequestParam int bookid, @RequestParam(defaultValue = "0") int userid, RedirectAttributes redirectAttributes,  Model model) {
		try {
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
			
			logger.log(Level.INFO, "Insert Review.");
			logger.log(Level.FINE, "reviewService.insertOne(" + review + ")");
			review = reviewService.insertOne(review); // Reviewを登録
			logger.log(Level.INFO, "Success to insert Review.(bookid = " + bookid + ")");
			logger.log(Level.INFO, "Review has inserted.(bookid = " + bookid + ")");
			
			// totalevaluation算出
			logger.log(Level.INFO, "Calculate totalevaluation of bookid = " + bookid + ".");
			double totalevaluation = reviewService.selectTotalEvaluationByBookId(bookid);
			logger.log(Level.INFO, "totalevaluation = " + totalevaluation + ".");
			logger.log(Level.INFO, "Success to calcurate totalevaluation.");
			
			// bookidに対応するBookのtotalevaluationを更新
			logger.log(Level.INFO, "Update Book totalevaluation.(bookid = " + bookid + ")");
			bookService.updateTotalevaluationById(bookid, totalevaluation); // 対象Bookのtotalevaluationを更新
			logger.log(Level.INFO, "Success to update Book.(bookid = " + bookid + ")");
			
			redirectAttributes.addFlashAttribute("complete", "レビューの登録が完了しました！"); // リダイレクト時のパラメータを設定する（登録完了メッセージ）
			
			logger.log(Level.INFO, "Redirect to /book/" + bookid + "?user=" + user);
			return "redirect:/book/" + bookid + "?user=" + user;
		}
		catch (Exception e) {
			logger.log(Level.SEVERE, "Internal Server Error");
			
			redirectAttributes.addFlashAttribute("complete", "レビューの登録に失敗しました。"); // リダイレクト時のパラメータを設定する（登録失敗メッセージ）
			logger.log(Level.INFO, "Redirect to /book/" + bookid + "?user=" + user);
			return "redirect:/book/" + bookid + "?user=" + user;
		}
	}
	
	// Review削除
	@DeleteMapping("/book/detail/{bookid}/delete/{reviewid}")
	public String deleteReview(@RequestParam(value="user", required=false) String user, @PathVariable int bookid, @PathVariable int reviewid, RedirectAttributes redirectAttributes, Model model) {
		try {
			logger.log(Level.INFO, "DELETE /book/" + bookid + "/review/" + reviewid + "?user=" + user);
			
			model.addAttribute("user", user); // userをModelに格納
			
			logger.log(Level.INFO, "Delete Review.(reviewid =" + reviewid + ")");
			logger.log(Level.FINE, "reviewService.deleteOneById(" + reviewid + ")");
			reviewService.deleteOneById(reviewid); // idを指定してReviewを削除
			logger.log(Level.INFO, "Success to delete Review.(reviewid = " + reviewid + ")");
			logger.log(Level.INFO, "Review has deleted.(reviewid =" + reviewid + ")");
			
			// totalevaluation算出
			logger.log(Level.INFO, "Calculate totalevaluation of bookid = " + bookid + ".");
			double totalevaluation = reviewService.selectTotalEvaluationByBookId(bookid);
			logger.log(Level.INFO, "totalevaluation = " + totalevaluation + ".");
			logger.log(Level.INFO, "Success to calcurate totalevaluation.");
			
			// bookidに対応するBookのtotalevaluationを更新
			logger.log(Level.INFO, "Update Book totalevaluation.(bookid = " + bookid + ")");
			bookService.updateTotalevaluationById(bookid, totalevaluation); // 対象Bookのtotalevaluationを更新
			logger.log(Level.INFO, "Success to update Book.(bookid = " + bookid + ")");
			
			redirectAttributes.addFlashAttribute("complete", "対象レビューの削除が完了しました。"); // リダイレクト時のパラメータを設定する（登録完了メッセージ）
			
			logger.log(Level.INFO, "Redirect to /book/" + bookid + "?user=" + user);
			return "redirect:/book/" + bookid + "?user=" + user;
		}
		catch (Exception e) {
			logger.log(Level.SEVERE, "Internal Server Error");
			redirectAttributes.addFlashAttribute("complete", "レビューの削除に失敗しました。"); // リダイレクト時のパラメータを設定する（削除失敗メッセージ）
			logger.log(Level.INFO, "Redirect to /book/" + bookid + "/detail?user=" + user);
			return "redirect:/book/" + bookid + "?user=" + user;
		}
	}
	
}
