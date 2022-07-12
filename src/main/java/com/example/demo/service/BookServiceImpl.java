package com.example.demo.service;


import java.util.Optional;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Book;
import com.example.demo.repository.BookRepository;

@Service
@Transactional
public class BookServiceImpl implements BookService {
	// BookRepositoryインスタンス作成
	@Autowired
	BookRepository bookRepository;
	
	Logger logger = Logger.getLogger(BookServiceImpl.class.getName());
	ConsoleHandler handler = new ConsoleHandler();

	// Book全件取得
	@Override
	public Iterable<Book> selectAll() {
		logger.log(Level.FINER, "selectAll()");
		logger.log(Level.FINER, "bookRepository.selectAllDesc()");
		return bookRepository.selectAllDesc();
	}

	// 上位n件のBookを取得
	@Override
	public Iterable<Book> selectTopN(int n) {
		logger.log(Level.FINER, "selectTopN(" + n + ")");
		logger.log(Level.FINER, "bookRepository.selectTopN(" + n + ")");
		return bookRepository.selectTopN(n);
	}
	
	// Bookをlimit単位で分割表示する際のページ数を取得
	@Override
	public int countAllPages(int limit) {
		logger.log(Level.FINER, "countAllPages(" +  limit + ")");
		logger.log(Level.FINER, "(int)Math.ceil((double)bookRepository.countAll() /" +  limit);
		return (int)Math.ceil((double)bookRepository.countAll() / limit);
	}
	
	// 登録されている全Bookをlimit単位でページ分割し指定したpageに含まれるBook一覧を取得
	@Override
	public Iterable<Book> selectAllDescByPage(int page, int limit) {
		logger.log(Level.FINER, "selectAllDescByPage(" + page + ", " + limit + ")");
		logger.log(Level.FINER, "bookRepository.selectAllDescByLimitOffset(" + limit + ", " + limit*(page-1) + ")");
		return bookRepository.selectAllDescByLimitOffset(limit, limit*(page-1));
	}

	// Bookを1件取得
	@Override
	public Optional<Book> selectOneById(int id) {
		logger.log(Level.FINER, "selectOneById(" + id + ")");
		logger.log(Level.FINER, "bookRepository.findById(" + id + ")");
		return bookRepository.findById(id);
	}

	// 登録されている全Bookをkeywordで検索した結果をlimit単位でページ分割し指定したpageに含まれるBook一覧を取得
	@Override
	public Iterable<Book> searchAllDescByPage(String keyword, int page, int limit){
		logger.log(Level.FINER, "searchAllDescByPage(" + keyword + ", " +  page + ", " + limit + ")");
		logger.log(Level.FINER, "bookRepository.searchAllDescByLimitOffset(" + keyword + ", " + limit + ", " + limit*(page-1) + ")");
		return bookRepository.searchAllDescByLimitOffset(keyword, limit, limit*(page-1));
	}	
	
	// Bookのkeyword検索結果をlimit単位で分割表示する際のページ数を取得
	@Override
	public int countSearchAllPages(String keyword, int limit) {
		logger.log(Level.FINER, "countSearchAllPages(" + keyword + ", " + limit + ")");
		logger.log(Level.FINER, "(int)Math.ceil((double)bookRepository.countSearchAll(" + keyword + ") /" + limit + ")");
		return (int)Math.ceil((double)bookRepository.countSearchAll(keyword) / limit);
	}
	
	// Bookを1件登録して登録されたBookを返す
	@Override
	public Book insertOne(Book book) {
		logger.log(Level.FINER, "insertOne(" + book + ")");
		logger.log(Level.FINER, "setTotalevaluation(0.0)");
		book.setTotalevaluation(0.0); // デフォルトの本の評価は0とする
		
		logger.log(Level.FINER, "bookRepository.save(" + book + ")");
		book = bookRepository.save(book);
		return book;
	}
	
	// Bookを1件更新して更新されたBookを返す
	@Override
	public Book updateOne(Book book) {
		logger.log(Level.FINER, "updateOne(" + book + ")");
		logger.log(Level.FINER, "bookRepository.save(" + book + ")");
		book = bookRepository.save(book);
		return book;
	}
	
	// Bookのtotalevaluationを更新
	@Override
	public void updateTotalevaluationById(int id, double totalevaluation) {
		logger.log(Level.FINER, "updateTotalevaluationById(" + id + ", " + totalevaluation + ")");
		logger.log(Level.FINER, "bookRepository.updateTotalevaluationById(" + id + ", " + totalevaluation + ")");
		bookRepository.updateTotalevaluationById(id, totalevaluation);
	}
	
	// Bookの削除
	@Override
	public void deleteOneById(int id) {
		logger.log(Level.FINER, "deleteOneById(" + id + ")");
		logger.log(Level.FINER, "bookRepository.deleteById(" + id + ")");
		bookRepository.deleteById(id);
	}
}
