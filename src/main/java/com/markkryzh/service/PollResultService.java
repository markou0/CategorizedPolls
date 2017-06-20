package com.markkryzh.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.markkryzh.entity.Category;
import com.markkryzh.entity.Poll;
import com.markkryzh.entity.PollAnswer;
import com.markkryzh.entity.PollQuestion;
import com.markkryzh.entity.PollResult;
import com.markkryzh.entity.Test;
import com.markkryzh.entity.User;
import com.markkryzh.form.PollResultResponse;
import com.markkryzh.form.PollResultResponse.QuestionResult;
import com.markkryzh.form.PollResultResponse.Rating;
import com.markkryzh.repository.PollResultRepository;

@Service
public class PollResultService {
	@Autowired
	private PollService pollService;
	@Autowired
	private TestResultService testResultService;
	@Autowired
	private TestService testService;
	@Autowired
	private PollAnswerService pollAnswerService;
	@Autowired
	private UserService userService;
	@Autowired
	private PollResultRepository pollResultRepository;


	public PollResultResponse getPollResultResponse(Integer pollId) {
		List<Rating> categoryRatingList = testResultService.getCategoriesRatings(pollId);
		List<QuestionResult> questionResultList = new ArrayList<>();
		PollResultResponse pollResultResponse = new PollResultResponse(questionResultList, categoryRatingList);
		Poll poll = pollService.findOne(pollId);
		Set<PollQuestion> pollQuestions = poll.getPollQuestions();
		for (PollQuestion pollQuestion : pollQuestions) {
			HashMap<String, List<Rating>> answerRatingByCategoryMap = getAnswerRatingByCategoryMapByQuestionId(
					pollQuestion.getId());
			List<Rating> answerRatingList = new ArrayList<>();
			QuestionResult questionResult = new QuestionResult(answerRatingList, answerRatingByCategoryMap);
			questionResultList.add(questionResult);
			for (PollAnswer pollAnswer : pollQuestion.getPollAnswers()) {
				answerRatingList.add(
						new Rating(pollAnswer.getText(), pollResultRepository.countByPollAnswerId(pollAnswer.getId())));
			}
		}
		return pollResultResponse;
	}

	public boolean checkIfAuthorizedUserPassedPoll(Integer pollId) {
		List<Integer> pollAnswersIds = pollService.findOne(pollId).getPollQuestions().stream()
				.flatMap(q -> q.getPollAnswers().stream()).map(a -> a.getId()).collect(Collectors.toList());
		return pollResultRepository.existsByPollAnswerIdInAndUserId(pollAnswersIds,
				userService.findAuthenticated().getId());
	}

	public boolean saveIfUserHasntPassedPollBefore(Integer pollId, List<Integer> ChoosenAnswersIds) {
		if(!pollService.checkPermissonsToTake(pollId))return false;
		User user = userService.findAuthenticated();
		List<PollAnswer> pollAnswers = pollAnswerService.findByIdIn(ChoosenAnswersIds);
		for (PollAnswer pollAnswer : pollAnswers) {
			pollResultRepository.save(new PollResult(pollAnswer, user));
		}
		return true;
	}

	public HashMap<String, List<Rating>> getAnswerRatingByCategoryMapByQuestionId(Integer pollQuestionId) {
		HashMap<String, List<Rating>> answerRatingByCategoryMap = new HashMap<>();
		Set<PollAnswer> pollAnswers = pollAnswerService.findByPollQuestionId(pollQuestionId);
		Test test = testService.findOneByPollQuestionId(pollQuestionId);
		Set<Category> categories = test.getCategories();
		for (Category category : categories) {
			List<Rating> answersRatingsByCategory = new ArrayList<>();
			List<Integer> usersIds = userService.findByCategoryId(category.getId()).stream().map(u -> u.getId())
					.collect(Collectors.toList());
			for (PollAnswer pollAnswer : pollAnswers) {
				long answerRatingByCategory = pollResultRepository.countByPollAnswerIdAndUserIdIn(pollAnswer.getId(),
						usersIds);
				Rating rating = new Rating(pollAnswer.getText(), answerRatingByCategory);
				answersRatingsByCategory.add(rating);
			}
			answerRatingByCategoryMap.put(category.getName(), answersRatingsByCategory);
		}
		return answerRatingByCategoryMap;
	}

}
