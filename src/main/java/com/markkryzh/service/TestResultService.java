package com.markkryzh.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.markkryzh.entity.Category;
import com.markkryzh.entity.Test;
import com.markkryzh.entity.TestAnswer;
import com.markkryzh.entity.TestAnswerScore;
import com.markkryzh.entity.TestResult;
import com.markkryzh.form.PollResultResponse.Rating;
import com.markkryzh.repository.CategoryRepository;
import com.markkryzh.repository.TestRepository;
import com.markkryzh.repository.TestResultRepository;

@Service
public class TestResultService {
	@Autowired
	private PollService pollService;
	@Autowired
	private PollResultService pollResultService;
	@Autowired
	private TestService testService;
	@Autowired
	private UserService userService;
	@Autowired
	private TestResultRepository testResultRepository;
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private TestRepository testRepository;

	public Test findByPollId(Integer pollId) {
		return testRepository.findOneByPollId(pollId);
	}

	private Category calculate(Integer pollId, List<Integer> testAnswerChoices) {
		Test test = findByPollId(pollId);
		List<TestAnswer> testAnswers = test.getTestQuestions().stream().flatMap(q -> q.getTestAnswers().stream())
				.filter(a -> testAnswerChoices.contains(a)).collect(Collectors.toList());
		List<Category> categories = categoryRepository.findByTestId(test.getId());
		HashMap<Category, Integer> catScoreRes = new HashMap<>();
		categories.forEach(cat -> catScoreRes.put(cat, 0));
		for (TestAnswer testAnswer : testAnswers) {
			for (TestAnswerScore score : testAnswer.getTestAnswerScores()) {
				catScoreRes.replace(score.getCategory(), catScoreRes.get(score.getCategory()) + score.getScore());
			}
		}
		return Collections.max(catScoreRes.entrySet(), Map.Entry.comparingByValue()).getKey();
	}

	public boolean saveIfUserHasntPassedPollBefore(Integer pollId, List<Integer> choosenTestAnswer) {
		if(!pollService.checkPermissonsToTake(pollId))return false;
		Category category = calculate(pollId, choosenTestAnswer);
		testResultRepository.save(new TestResult(category, userService.findAuthenticated())).getId();
		return true;
	}

	public List<Rating> getCategoriesRatings(Integer pollId) {
		System.err.println("inside getCategoriesRatings" + testService.findOneByPollId(pollId));
		Integer testId = testService.findOneByPollId(pollId).getId();
		System.err.println("test id" + testId);
		List<Rating> categoryRatingList = new ArrayList<>();
		List<Category> categories = categoryRepository.findByTestId(testId);
		for (Category category : categories) {
			Rating rating = new Rating(category.getName(), testResultRepository.countByCategoryId(category.getId()));
			categoryRatingList.add(rating);
		}
		return categoryRatingList;
	}
}
