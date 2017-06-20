package com.markkryzh.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.markkryzh.entity.TestResult;

public interface TestResultRepository extends JpaRepository<TestResult, Integer> {
	int countByCategoryId(Integer categoryId);
	List<TestResult> findByCategoryIdIn(List<Integer> categoriesIds);
	List<TestResult> findByCategoryId(Integer categoryId);
}
