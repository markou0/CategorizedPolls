package com.markkryzh.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.markkryzh.entity.Test;

public interface TestRepository extends JpaRepository<Test, Integer> {
	Test findOneByPollId(Integer pollQuesion);
	boolean existsByPollId(Integer pollId);
}
