package com.example.demo.service;

import java.util.Optional;

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
	
	// RVのIDを指定してRVを1件取得
	@Override
	public Optional<Review> selectOneById(Integer id){
		return reviewRepository.findById(id);
	}
	
	// RVのIDを指定してRVを1件削除
	@Override
	public void deleteOneById(Integer id) {
		reviewRepository.deleteById(id);
	}
	
	// RVを1件登録
	@Override
	public void insertReview(Review review) {
		reviewRepository.save(review);
	}
	
	// 本のIDを指定してRVを全件取得
//	@Override
//	public Iterable<Review> selectAllByBookId(Iterable<Integer> ids){
//		// SQLの独自定義が必要？
//		Iterable<Review> review = (Iterable<Review>) new Review(); //ダミー
//	    return review;
//	}
	
	// 本のIDを指定してRVを全件削除（本のIDに紐付くもの全て）
//	@Override
//	public void deleteAllByBookId(Integer id) {
//		// SQLの独自定義が必要？
//	}
	
}
