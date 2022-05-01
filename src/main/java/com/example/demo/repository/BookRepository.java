package com.example.demo.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.entity.Book;

//CrudRepositoryの拡張インターフェースとしてRepositoryを作成
public interface BookRepository extends CrudRepository<Book, Integer> {

}
