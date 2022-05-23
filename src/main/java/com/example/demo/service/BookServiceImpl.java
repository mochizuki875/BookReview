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
	BookRepository repository;

	@Override
	public Iterable<Book> selectAll() {
		return repository.selectAllDesc();
	}

	@Override
	public Optional<Book> selectOneById(Integer id) {
		return repository.findById(id);
	}
	
	@Override
	public void insertOne(Book book) {
		book.setTotalevaluation(0.0); // 暫定で本の評価は0とする
		repository.save(book);
	}
	
	@Override
	public void updateTotalevaluationById(Integer id, Double totalevaluation) {
		repository.updateTotalevaluationById(id, totalevaluation);
	}
	
	@Override
	public void deleteOneById(Integer id) {
		repository.deleteById(id);
	}
	
	@Override
	public Iterable<Book> searchAll(String keyword) {
		return repository.searchAll(keyword);
	}
	
	@Override
	public Iterable<Book> selectTopN(Integer n) {
		return repository.selectTopN(n);
	}

}
