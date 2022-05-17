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
		return repository.findAll();
	}

	@Override
	public Optional<Book> selectOneById(Integer id) {
		return repository.findById(id);
	}
	
	@Override
	public void insertBook(Book book) {
		book.setTotalevaluation(0.0); // 暫定で本の評価は0とする
		repository.save(book);
	}
	
	@Override
	public void updateTotalevaluationById(Integer id, Double totalevaluation) {
		repository.updateTotalevaluationById(id, totalevaluation);
	}
	
	@Override
	public void deleteBookById(Integer id) {
		repository.deleteById(id);
	}

}
