package com.markkryzh.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

import com.markkryzh.entity.Community;
import com.markkryzh.entity.Poll;
import com.markkryzh.entity.PollResult;
import com.markkryzh.entity.PollTheme;
import com.markkryzh.entity.Test;
import com.markkryzh.entity.User;
import com.markkryzh.form.PollForm;
import com.markkryzh.form.PollForm.Answer;
import com.markkryzh.form.PollResultResponse;
import com.markkryzh.form.PollResultResponse.QuestionResult;
import com.markkryzh.form.PollResultResponse.Rating;
import com.markkryzh.repository.CommunityRepository;
import com.markkryzh.repository.PollRepository;
import com.markkryzh.repository.TestQuestionRepository;
import com.markkryzh.repository.TestRepository;
import com.markkryzh.repository.UserRepository;
import com.markkryzh.service.CommunityService;
import com.markkryzh.service.MembershipService;
import com.markkryzh.service.PollResultService;
import com.markkryzh.service.PollService;
import com.markkryzh.service.PollThemeService;
import com.markkryzh.service.TestResultService;
import com.markkryzh.service.UserService;

@Controller
public class MainController0 {
	/*@Autowired
	MembershipService membershipService;
	@Autowired
	PollThemeService pollThemeService;
	@Autowired
	CommunityService communityService;
	@Autowired
	private UserService userService;
	@Autowired
	private PollResultService pollResultService;
	@Autowired
	private TestResultService testResultService;
	@Autowired
	private PollService pollService;
	@Autowired
	PollRepository pollRepository;
	@Autowired
	TestQuestionRepository testQuestionRepository;
	@Autowired
	TestRepository testRepository;

	@RequestMapping({"/","main"})
	public String index(Map<String, Object> model) {
		model.put("popularPolls",pollService.findPopular());
		model.put("newestPolls", pollService.findNewest());
		return "main";
	}

	@RequestMapping("/poll/create")
	public String createPoll(PollForm pollForm, Map<String, Object> model) {
		model.put("pollThemes", pollThemeService.findAll());
		model.put("userCommunities", communityService.findUserCommunities(userService.findAuthenticated().getId()));
		return "poll-create";
	}

	@ResponseBody
	@PostMapping("/poll/create")
	public Integer parsePoll(@RequestBody PollForm pollForm) {
		Integer pollId = pollService.parsePollForm(pollForm);
		return pollId;
	}

	@RequestMapping("/poll/{pollId}/poll-questions/take")
	public String takePoll(@PathVariable Integer pollId, Map<String, Object> model) {
		Poll poll = pollRepository.findOne(pollId);
		model.put("poll", poll);
		model.put("hasUserPassedPollBefore", pollResultService.checkIfAuthorizedUserPassedPoll(pollId));
		model.put("hasUserPermissionToTakePollIfPrivate", userService.checkPermissionToTakePollIfPrivate(pollId));
		model.put("isPollOutOfTime", !pollService.checkTime(pollId));
		return "questions-take";
	}

	@RequestMapping("/poll/{pollId}/test-questions/take")
	public String takeTest(@PathVariable Integer pollId, Map<String, Object> model) {
		Test test = testRepository.findOneByPollId(pollId);
		model.put("test", test);
		model.put("hasUserPassedPollBefore", pollResultService.checkIfAuthorizedUserPassedPoll(pollId));
		model.put("hasUserPermissionToTakePollIfPrivate", userService.checkPermissionToTakePollIfPrivate(pollId));
		model.put("isPollOutOfTime", !pollService.checkTime(pollId));
		return "questions-take";
	}

	@RequestMapping(value = "/poll/{pollId}/poll-questions/submit", method = RequestMethod.POST)
	public String submitPoll(@PathVariable Integer pollId, @RequestParam Map<String, String> requestParams,
			Map<String, Object> model) {
		List<Integer> choosenAnswersIds = requestParams.entrySet().stream()
				.filter(x -> StringUtils.startsWith(x.getKey(), "choosenAnswers"))
				.map(x -> Integer.parseInt(x.getValue())).collect(Collectors.toList());
		pollResultService.saveIfUserHasntPassedPollBefore(pollId, choosenAnswersIds);
		System.err.println(choosenAnswersIds);
		return "redirect:/poll/{pollId}/results";
	}

	@RequestMapping(value = "/poll/{pollId}/test-questions/submit", method = RequestMethod.POST)
	public String submitTest(@PathVariable Integer pollId, @RequestParam Map<String, String> requestParams,
			Map<String, Object> model) {
		List<Integer> choosenAnswers = requestParams.entrySet().stream()
				.filter(x -> StringUtils.startsWith(x.getKey(), "choosenAnswers"))
				.map(x -> Integer.parseInt(x.getValue())).collect(Collectors.toList());

		testResultService.saveIfUserHasntPassedPollBefore(pollId, choosenAnswers);
		return "redirect:/poll/{pollId}/poll-questions/take";
	}

	@RequestMapping(value = "/poll/{pollId}/results")
	public String pollResults(@PathVariable Integer pollId, Map<String, Object> model) {
		model.put("poll", pollRepository.findOne(pollId));
		return "poll-results";
	}

	@ResponseBody
	@RequestMapping(value = "/poll/{pollId}/results/getJson")
	public PollResultResponse ajaxPollResults(@PathVariable Integer pollId, Map<String, Object> model) {
		return pollResultService.getPollResultResponse(pollId);
	}

	@RequestMapping("/user/personal-cabinet")
	public String personalCabinet(Map<String, Object> model) {
		User user = userService.findAuthenticated();
		model.put("user", user);
		model.put("community", new Community());
		model.put("lastTakedPolls", pollService.findLastTakedByUser(user.getId()));
		model.put("userPolls", pollService.findByUserId(user.getId()));
		model.put("privatePolls", pollService.findPrivatePollByUserId(user.getId()));
		model.put("userCommunities", communityService.findUserCommunities(user.getId()));
		model.put("notAcceptedCommunities", communityService.findNotAcceptedByUser(user.getId()));
		return "personal-сabinet";
	}

	@RequestMapping("/poll-list-by/theme/{themeName}")
	public String pollListByTheme(@PathVariable String themeName, Map<String, Object> model) {
		User user = userService.findAuthenticated();
		model.put("user", user);
		model.put("polls", pollService.findByThemeNameContains(themeName));
		return "poll-list-page";
	}
	
	@RequestMapping("/poll-list-by/hashtag/{hashtag}")
	public String pollListByHashtag(@PathVariable String hashtag, Map<String, Object> model) {
		User user = userService.findAuthenticated();
		model.put("user", user);
		model.put("polls", pollService.findByHashtagsContains(hashtag));
		return "poll-list-page";
	}

	@ResponseBody
	@RequestMapping(value = "/users-search-autocomplete")
	public List<String> autoComplete(@RequestParam("term") String term) {
		List<String> terms = userService.findByNameContains(term);
		;
		return terms;
	}

	@ResponseBody
	@RequestMapping(value = "/poll-theme/create")
	public PollTheme createPollTheme(@RequestBody PollTheme pollTheme) {
		if (StringUtils.isEmpty(pollTheme.getName()) || pollThemeService.save(pollTheme) == null)
			return null;
		return pollTheme;
	}

	@RequestMapping("/community/create")
	public RedirectView createCommunity(@RequestParam("community-name") String communityName,
			@RequestParam("members-usernames") String membersUsernames, RedirectAttributes redirectAttrs) {
		RedirectView rv = new RedirectView("/user/personal-cabinet");
		rv.setContextRelative(true);
		if (communityService.save(communityName, membersUsernames) != null) {
			System.out.println(Arrays.toString(membersUsernames.split(",")));
			redirectAttrs.addFlashAttribute("operationSuccess",
					"You you have been successfully create the community : <strong>\"" + communityName + "\"</strong>");
		} else {
			redirectAttrs.addFlashAttribute("operationError",
					"Fail to create community : <strong>\"" + communityName + "\"</strong> due to the fact users :"
							+ userService.checkNonExistentsByNames(membersUsernames) + " doesn`t exist");
		}
		return rv;
	}

	@RequestMapping("/community/{communityId}/accept")
	public RedirectView acceptCommunity(RedirectAttributes redirectAttrs,
			@PathVariable("communityId") Integer communityId, Map<String, Object> model) {
		RedirectView rv = new RedirectView("/user/personal-cabinet");
		rv.setContextRelative(true);
		Community community = communityService.findOne(communityId);
		if (membershipService.acceptCommunityMembershipIfExistsInvite(communityId))
			redirectAttrs.addFlashAttribute("operationSuccess",
					"You you have been successfully accepted to the community : <strong> \"" + community.getName()
							+ "\"</strong");
		else
			redirectAttrs.addFlashAttribute("operationError", "You can`t be accepted to the community : <strong>\""
					+ community.getName() + "\"</strong> due to the fact you haven`t been invited!");
		return rv;
	}

	@RequestMapping("/community/{communityId}/leave")
	public RedirectView leaveCommunity(RedirectAttributes redirectAttrs,
			@PathVariable("communityId") Integer communityId, Map<String, Object> model) {
		RedirectView rv = new RedirectView("/user/personal-cabinet");
		rv.setContextRelative(true);
		Community community = communityService.findOne(communityId);
		if (membershipService.leaveCommunityMembershipIfExistsInvite(communityId))
			redirectAttrs.addFlashAttribute("operationSuccess",
					"You you have been successfully leaved the community : <strong> \"" + community.getName()
							+ "\"</strong");
		else
			redirectAttrs.addFlashAttribute("operationError", "You can`t leave the community : <strong>\""
					+ community.getName() + "\"</strong> due to the fact you aren`t it`s member!");
		return rv;
	}*/
}