package com.markkryzh.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.markkryzh.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
	List<Category> findByTestId(Integer testId);
}
