package com.example.demo.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.entity.Review;

//CrudRepositoryの拡張インターフェースとしてRepositoryを作成
public interface ReviewRepository extends CrudRepository<Review, Integer> {
	// 独自で使用したいメソッドを定義
}
