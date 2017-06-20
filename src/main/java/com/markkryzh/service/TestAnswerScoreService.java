package com.markkryzh.service;

import com.markkryzh.entity.Test;
import com.markkryzh.entity.TestAnswerScore;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.markkryzh.repository.TestAnswerScoreRepository;
import com.markkryzh.repository.TestRepository;

@Service
public class TestAnswerScoreService {
	@Autowired
	TestRepository testRepository;
	@Autowired
	TestAnswerScoreRepository answerScoreRepository;
	
	public Integer save(TestAnswerScore answerScore){
		return answerScoreRepository.save(answerScore).getId();
	}

	public List<TestAnswerScore> findByTestId(Integer testId) {
		Test test = testRepository.findOne(testId);
		return test.getTestQuestions().stream().flatMap(q -> q.getTestAnswers().stream())
				.flatMap(a -> a.getTestAnswerScores().stream()).collect(Collectors.toList());
	}
}
