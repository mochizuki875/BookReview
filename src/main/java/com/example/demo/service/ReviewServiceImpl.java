package com.example.demo.service;

import java.util.Optional;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Review;
import com.example.demo.repository.ReviewRepository;

@Service
@Transactional
public class ReviewServiceImpl implements ReviewService {
	// ReviewRepositoryインスタンス作成
	@Autowired
	ReviewRepository reviewRepository;
	
	Logger logger = Logger.getLogger(ReviewServiceImpl.class.getName());
	ConsoleHandler handler = new ConsoleHandler();
	
	// RVのIDを指定してRVを1件取得
	@Override
	public Optional<Review> selectOneById(int id){
		logger.log(Level.FINER, "selectOneById(" + id + ")");
		logger.log(Level.FINER, "reviewRepository.findById(" + id + ")");
		return reviewRepository.findById(id);
	}
	
	// RVのIDを指定してRVを1件削除
	@Override
	public void deleteOneById(int id) {
		logger.log(Level.FINER, "deleteOneById(" + id + ")");
		logger.log(Level.FINER, "reviewRepository.deleteById(" + id + ")");
		reviewRepository.deleteById(id);
	}
	
	// RVを1件登録
	@Override
	public Review insertOne(Review review) {
		logger.log(Level.FINER, "insertOne(" + review + ")");
		logger.log(Level.FINER, "reviewRepository.save(" + review + ")");
		return reviewRepository.save(review);
	}
	
	// 本のIDを指定してRVを全件取得
	@Override
	public Iterable<Review> selectAllByBookId(int bookid){
		logger.log(Level.FINER, "selectAllByBookId(" + bookid + ")");
		logger.log(Level.FINER, "reviewRepository.findAllByBookid(" + bookid + ")");
	    return reviewRepository.findAllByBookid(bookid);
	}
	
	// 本のIDを指定してRVを全件削除（本のIDに紐付くもの全て）
	@Override
	public void deleteAllByBookId(int bookid) {
		logger.log(Level.FINER, "deleteAllByBookId(" + bookid + ")");
		logger.log(Level.FINER, "reviewRepository.deleteAllByBookid(" + bookid + ")");
		reviewRepository.deleteAllByBookid(bookid);
	}
	
	// 指定したbookidのTotalEvaluationを取得
	public double selectTotalEvaluationByBookId(int bookid) {
		logger.log(Level.FINER, "selectTotalEvaluationByBookId(" + bookid + ")");
		logger.log(Level.FINER, "reviewRepository.findTotalEvaluationByBookId(" + bookid + ")");
		return reviewRepository.findTotalEvaluationByBookId(bookid);
	}
}
