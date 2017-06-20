package com.markkryzh.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.markkryzh.controller.RestUploadController;
import com.markkryzh.entity.Category;
import com.markkryzh.entity.Poll;
import com.markkryzh.entity.PollAnswer;
import com.markkryzh.entity.PollQuestion;
import com.markkryzh.entity.PollTheme;
import com.markkryzh.entity.Test;
import com.markkryzh.entity.TestAnswer;
import com.markkryzh.entity.TestAnswerScore;
import com.markkryzh.entity.TestQuestion;
import com.markkryzh.entity.User;
import com.markkryzh.form.PollForm;
import com.markkryzh.repository.CategoryRepository;
import com.markkryzh.repository.PollAnswerRepository;
import com.markkryzh.repository.PollQuestionRepository;
import com.markkryzh.repository.PollRepository;
import com.markkryzh.repository.TestAnswerRepository;
import com.markkryzh.repository.TestAnswerScoreRepository;
import com.markkryzh.repository.TestQuestionRepository;
import com.markkryzh.repository.TestRepository;
import com.markkryzh.repository.UserRepository;

@Service
public class TestService {
	@Autowired
	PollRepository pollRepository;
	@Autowired
	PollQuestionRepository pollQuestionRepository;
	@Autowired
	PollAnswerRepository pollAnswerRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	CategoryRepository categoryRepository;
	@Autowired
	TestRepository testRepository;
	@Autowired
	TestQuestionRepository testQuestionRepository;
	@Autowired
	TestAnswerRepository testAnswerRepository;
	@Autowired
	TestAnswerScoreRepository testAnswerScoreRepository;

	public Integer parsePollForm(PollForm pollForm, Poll poll) {
		List<TestQuestion> testQuestions = new ArrayList<>();
		List<TestAnswer> testAnswers = new ArrayList<>();
		List<Category> categories = new ArrayList<>();
		List<TestAnswerScore> testAnswerScores = new ArrayList<>();
		Test test = new Test(poll, null, null);

		for (String categoryName : pollForm.getTestCategories()) {
			Category category = new Category(test, categoryName);
			categories.add(category);
		}

		for (PollForm.TestQuestion $testQuestion : pollForm.getTestQuestions()) {
			TestQuestion testQuestion = new TestQuestion(test, $testQuestion.getQuestion(),
					StringUtils.defaultIfEmpty($testQuestion.getImageName(), $testQuestion.getImageUrl()), null);
			for (PollForm.TestAnswer $answer : $testQuestion.getAnswers()) {
				TestAnswer answer = new TestAnswer(testQuestion, $answer.getAnswer(),
						StringUtils.defaultIfEmpty($answer.getImageName(), $answer.getImageUrl()), null);
				for (int i = 0, categoryScore = $answer.getCategoriesScores()[i]; i < $answer
						.getCategoriesScores().length; i++) {
					TestAnswerScore answerScore = new TestAnswerScore(categories.get(i), answer, categoryScore);
					testAnswerScores.add(answerScore);
				}
				testAnswers.add(answer);
			}
			testQuestions.add(testQuestion);
		}
		saveTest(test, testQuestions, testAnswers, categories, testAnswerScores);
		return poll.getId();
	}

	public Integer saveTest(Test test, List<TestQuestion> testQuestions, List<TestAnswer> testAnswers,
			List<Category> categories, List<TestAnswerScore> testAnswerScores) {
		testRepository.save(test);
		testQuestionRepository.save(testQuestions);
		testAnswerRepository.save(testAnswers);
		categoryRepository.save(categories);
		testAnswerScoreRepository.save(testAnswerScores);
		return test.getId();
	}

	public Test findOneByPollQuestionId(Integer pollQuestionId){
		PollQuestion pollQuestion = pollQuestionRepository.findOne(pollQuestionId);
		return testRepository.findOneByPollId(pollQuestion.getPoll().getId());
	}
	
	public Test findOneByPollId(Integer pollId){
		System.err.println("poll id" +pollId);
		System.err.println("testRepository.findOneByPollId(pollId)"+testRepository.findOneByPollId(pollId).getId());
		return testRepository.findOneByPollId(pollId);
	}
}