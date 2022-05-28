package com.example.demo.service;


import java.util.Optional;

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

	@Override
	public Iterable<Book> selectAll() {
		return bookRepository.selectAllDesc();
	}

	@Override
	public Iterable<Book> selectTopN(Integer n) {
		return bookRepository.selectTopN(n);
	}
	
	@Override
	public Integer countAllPages(Integer offset) {
		return (int)Math.ceil((double)bookRepository.countAll() / offset);
	}
	
//	@Override
	public Iterable<Book> selectAllDescByPage(Integer page, Integer limit) {
		return bookRepository.selectAllDescByLimitOffset(limit, limit*(page-1));
	}

	@Override
	public Optional<Book> selectOneById(Integer id) {
		return bookRepository.findById(id);
	}

	@Override
	public Iterable<Book> searchAllDescByPage(String keyword, Integer page, Integer limit){
		return bookRepository.searchAllDescByLimitOffset(keyword, limit, limit*(page-1));
	}	
	
	@Override
	public Integer countSearchAllPages(String keyword, Integer offset) {
		return (int)Math.ceil((double)bookRepository.countSearchAll(keyword) / offset);
	}
	
	@Override
	public Book insertOne(Book book) {
		book.setTotalevaluation(0.0); // デフォルトの本の評価は0とする
		book = bookRepository.save(book);
		return book;
	}
	
	@Override
	public void updateTotalevaluationById(Integer id, Double totalevaluation) {
		bookRepository.updateTotalevaluationById(id, totalevaluation);
	}
	
	@Override
	public void deleteOneById(Integer id) {
		bookRepository.deleteById(id);
	}
	

	


}
