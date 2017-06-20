package com.markkryzh.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.markkryzh.entity.TestQuestion;

public interface TestQuestionRepository extends JpaRepository<TestQuestion, Integer>{

}
