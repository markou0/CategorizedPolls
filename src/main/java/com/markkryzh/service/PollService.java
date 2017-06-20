package com.markkryzh.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.markkryzh.controller.RestUploadController;
import com.markkryzh.entity.Community;
import com.markkryzh.entity.Membership;
import com.markkryzh.entity.Poll;
import com.markkryzh.entity.PollAnswer;
import com.markkryzh.entity.PollCommunity;
import com.markkryzh.entity.PollQuestion;
import com.markkryzh.entity.PollTheme;
import com.markkryzh.entity.User;
import com.markkryzh.form.PollForm;
import com.markkryzh.repository.CategoryRepository;
import com.markkryzh.repository.PollAnswerRepository;
import com.markkryzh.repository.PollQuestionRepository;
import com.markkryzh.repository.PollRepository;
import com.markkryzh.repository.PollResultRepository;
import com.markkryzh.repository.PollThemeRepository;
import com.markkryzh.repository.TestAnswerRepository;
import com.markkryzh.repository.TestQuestionRepository;
import com.markkryzh.repository.TestRepository;
import com.markkryzh.repository.UserRepository;

@Service
public class PollService {
	@Autowired
	PollResultService pollResultService;
	@Autowired
	PollCommunityService pollCommunityService;
	@Autowired
	CommunityService communityService;
	@Autowired
	PollAnswerService pollAnswerService;
	@Autowired
	PollResultRepository pollResultRepository;
	@Autowired
	UserService userService;
	@Autowired
	TestService testService;
	@Autowired
	PollRepository pollRepository;
	@Autowired
	PollQuestionRepository pollQuestionRepository;
	@Autowired
	PollAnswerRepository pollAnswerRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	PollThemeRepository pollThemeRepository;

	public Integer parsePollForm(PollForm pollForm) {
		List<Community> communities = null;
		if (pollForm.getCommunitiesIds() != null)
			communities = communityService.findByIdIn(pollForm.getCommunitiesIds());
		User user = userService.findAuthenticated();
		System.err.println("userId"+user.getId());
		PollTheme pollTheme = pollThemeRepository.findOne(pollForm.getPollThemeId());
		List<PollQuestion> pollQuestions = new ArrayList<>();
		List<PollAnswer> pollAnswers = new ArrayList<>();
		Poll poll = new Poll(pollTheme, user, pollForm.getPollName(), pollForm.isPrivate_(), pollForm.isAnonymous(),
				pollForm.isShowAuthor(), null, pollForm.getFromDateTimestamp(), pollForm.getToDateTimestamp(),
				pollForm.getPollHashtags(), null);
		for (PollForm.PollQuestion $pollQuestion : pollForm.getPollQuestions()) {
			PollQuestion pollQuestion = new PollQuestion(poll, $pollQuestion.getQuestion(),
					$pollQuestion.isAllowOtherAnswer(),
					$pollQuestion.getImageName() == null ? $pollQuestion.getImageUrl() : $pollQuestion.getImageName(),
					null);
			for (PollForm.Answer $answer : $pollQuestion.getAnswers()) {
				PollAnswer answer = new PollAnswer(pollQuestion, $answer.getAnswer(), false,
						$answer.getImageName() == null ? $answer.getImageUrl() : $answer.getImageName());
				pollAnswers.add(answer);
			}
			pollQuestions.add(pollQuestion);
		}

		if (save(poll, pollQuestions, pollAnswers, communities) != null && pollForm.getTestQuestions() != null
				&& pollForm.getTestQuestions().length > 0) {
			testService.parsePollForm(pollForm, poll);
		}
		return poll.getId();
	}

	public Integer save(Poll poll, List<PollQuestion> pollQuestions, List<PollAnswer> pollAnswers,
			List<Community> communities) {
		pollRepository.save(poll);
		pollQuestionRepository.save(pollQuestions);
		pollAnswerRepository.save(pollAnswers);
		if (communities != null && communities.size() > 0) {
			poll.setPrivat(true);
			pollRepository.save(poll);
			pollCommunityService.save(poll, communities);
		}
		return poll.getId();
	}

	public Poll findOne(Integer id) {
		return pollRepository.findOne(id);
	}

	public List<Poll> findLastTakedByUser(Integer userId) {
		List<Poll> polls = pollResultRepository.findByUserIdOrderByDateDesc(userId).stream()
				.map(pr -> pr.getPollAnswer()).map(a -> a.getPollQuestion().getPoll()).collect(Collectors.toList());
		return countUsersVoted(polls);
	}

	public List<Poll> findByUserId(Integer userId) {
		return countUsersVoted(pollRepository.findByUserId(userId));
	}

	public List<Poll> countUsersVoted(List<Poll> polls) {
		polls.forEach((Poll poll) -> {
			List<Integer> pollAnswersIds = pollAnswerService.findByPollId(poll.getId()).stream().map(a -> a.getId())
					.collect(Collectors.toList());
			poll.setUsersVoted(pollResultRepository.countByPollAnswerIdIn(pollAnswersIds));
		});
		return polls;
	}

	public boolean checkTime(Integer pollId) {
		Poll poll = findOne(pollId);
		if (poll.getFromTimestamp() == null || poll.getToTimestamp() == null)
			return true;
		Date currentDate = new Date();
		if (currentDate.after(poll.getFromTimestamp()) && currentDate.before(poll.getToTimestamp()))
			return true;
		return false;
	}

	public boolean checkPermissonsToTake(Integer pollId) {
		return checkTime(pollId) && userService.checkPermissionToTakePollIfPrivate(pollId)
				&& !pollResultService.checkIfAuthorizedUserPassedPoll(pollId);
	}

	public List<Poll> findByThemeNameContains(String themeName) {
		return countUsersVoted(pollRepository.findByPollThemeNameContainsIgnoreCase(themeName));
	}

	public List<Poll> findByHashtagsContains(String hashtags) {
		return countUsersVoted(pollRepository.findByHashTagsContainsIgnoreCase(hashtags));
	}

	public List<Poll> findPrivatePollByUserId(Integer userId) {
		List<Integer> communitiesIds = communityService.findUserCommunities(userId).stream().map(c -> c.getId())
				.collect(Collectors.toList());
		List<Poll> polls = pollCommunityService.findByCommunitiesIds(communitiesIds).stream().map(pc -> pc.getPoll())
				.collect(Collectors.toList());
		return countUsersVoted(polls);
	}

	public List<Poll> findPopular() {
		List<Poll> polls = pollRepository.findByPrivatFalse();
		return countUsersVoted(polls).stream().sorted((Poll b, Poll a) -> Long.compare(a.getUsersVoted(), b.getUsersVoted()))
				.limit(10).collect(Collectors.toList());
	}
	
	public List<Poll> findNewest() {
		List<Poll> polls = pollRepository.findByPrivatFalseOrderByDateDesc();
		return countUsersVoted(polls).stream().limit(10).collect(Collectors.toList());
	}
	
	public boolean delete(Integer pollId){
		System.err.println("userService.isAdmin()"+userService.isAdmin());
		if(userService.isAdmin()||isAuthor(pollId)){
			pollRepository.delete(pollId);
			return true;
		}
		return false;
	}
	
	public boolean isAuthor(Integer pollId){
		return findOne(pollId).getUser().equals(userService.findAuthenticated());
	}
}