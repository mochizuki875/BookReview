package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.entity.Book;
import com.example.demo.service.BookService;

@Controller
@RequestMapping
public class BookReviewController {
	// Serviceインスタンスの作成
	@Autowired
	BookService bookService;
	
	// ホーム画面を表示
	@GetMapping("/")
	public String showHome(Model model) {
		Iterable<Book> bookList = bookService.selectAll(); // Book情報を全件取得
		model.addAttribute("bookList", bookList); // Modelに格納
		return "home";
	}
	

}
