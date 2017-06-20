package com.markkryzh.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.markkryzh.entity.PollAnswer;
import com.markkryzh.entity.PollQuestion;

public interface PollQuestionRepository extends JpaRepository<PollQuestion, Integer> {
	List<PollQuestion> findByPollId(Integer pollId);
}
