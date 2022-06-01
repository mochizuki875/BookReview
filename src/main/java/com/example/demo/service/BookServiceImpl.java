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

	@Override
	public Iterable<Book> selectAll() {
		logger.log(Level.FINER, "selectAll()");
		logger.log(Level.FINER, "bookRepository.selectAllDesc()");
		return bookRepository.selectAllDesc();
	}

	@Override
	public Iterable<Book> selectTopN(int n) {
		logger.log(Level.FINER, "selectTopN(" + n + ")");
		logger.log(Level.FINER, "bookRepository.selectTopN(" + n + ")");
		return bookRepository.selectTopN(n);
	}
	
	@Override
	public int countAllPages(int offset) {
		logger.log(Level.FINER, "countAllPages(" +  offset + ")");
		logger.log(Level.FINER, "(int)Math.ceil((double)bookRepository.countAll() /" +  offset);
		return (int)Math.ceil((double)bookRepository.countAll() / offset);
	}
	
//	@Override
	public Iterable<Book> selectAllDescByPage(int page, int limit) {
		logger.log(Level.FINER, "selectAllDescByPage(" + page + ", " + limit + ")");
		logger.log(Level.FINER, "bookRepository.selectAllDescByLimitOffset(" + limit + ", " + limit*(page-1) + ")");
		return bookRepository.selectAllDescByLimitOffset(limit, limit*(page-1));
	}

	@Override
	public Optional<Book> selectOneById(int id) {
		logger.log(Level.FINER, "selectOneById(" + id + ")");
		logger.log(Level.FINER, "bookRepository.findById(" + id + ")");
		return bookRepository.findById(id);
	}

	@Override
	public Iterable<Book> searchAllDescByPage(String keyword, int page, int limit){
		logger.log(Level.FINER, "searchAllDescByPage(" + keyword + ", " +  page + ", " + limit + ")");
		logger.log(Level.FINER, "bookRepository.searchAllDescByLimitOffset(" + keyword + ", " + limit + ", " + limit*(page-1) + ")");
		return bookRepository.searchAllDescByLimitOffset(keyword, limit, limit*(page-1));
	}	
	
	@Override
	public int countSearchAllPages(String keyword, int offset) {
		logger.log(Level.FINER, "countSearchAllPages(" + keyword + ", " + offset + ")");
		logger.log(Level.FINER, "(int)Math.ceil((double)bookRepository.countSearchAll(" + keyword + ") /" + offset + ")");
		return (int)Math.ceil((double)bookRepository.countSearchAll(keyword) / offset);
	}
	
	@Override
	public Book insertOne(Book book) {
		logger.log(Level.FINER, "insertOne(" + book + ")");
		logger.log(Level.FINER, "setTotalevaluation(0.0)");
		book.setTotalevaluation(0.0); // デフォルトの本の評価は0とする
		
		logger.log(Level.FINER, "bookRepository.save(" + book + ")");
		book = bookRepository.save(book);
		return book;
	}
	
	@Override
	public Book updateOne(Book book) {
		logger.log(Level.FINER, "updateOne(" + book + ")");
		logger.log(Level.FINER, "bookRepository.save(" + book + ")");
		book = bookRepository.save(book);
		return book;
	}
	
	@Override
	public void updateTotalevaluationById(int id, double totalevaluation) {
		logger.log(Level.FINER, "updateTotalevaluationById(" + id + ", " + totalevaluation + ")");
		logger.log(Level.FINER, "bookRepository.updateTotalevaluationById(" + id + ", " + totalevaluation + ")");
		bookRepository.updateTotalevaluationById(id, totalevaluation);
	}
	
	@Override
	public void deleteOneById(int id) {
		logger.log(Level.FINER, "deleteOneById(" + id + ")");
		logger.log(Level.FINER, "bookRepository.deleteById(" + id + ")");
		bookRepository.deleteById(id);
	}
}
