package com.markkryzh.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.markkryzh.entity.Poll;
import com.markkryzh.entity.Test;
import com.markkryzh.form.PollForm;
import com.markkryzh.repository.TestRepository;
import com.markkryzh.service.CommunityService;
import com.markkryzh.service.PollResultService;
import com.markkryzh.service.PollService;
import com.markkryzh.service.PollThemeService;
import com.markkryzh.service.TestResultService;
import com.markkryzh.service.UserService;

@Controller
public class TestController {
	@Autowired
	private UserService userService;
	@Autowired
	private PollResultService pollResultService;
	@Autowired
	private TestResultService testResultService;
	@Autowired
	private PollService pollService;
	@Autowired
	private TestRepository testRepository;

	@RequestMapping("/poll/{pollId}/test-questions/take")
	public String takeQuestions(@PathVariable Integer pollId, Map<String, Object> model) {
		Test test = testRepository.findOneByPollId(pollId);
		model.put("test", test);
		model.put("hasUserPassedPollBefore", pollResultService.checkIfAuthorizedUserPassedPoll(pollId));
		model.put("hasUserPermissionToTakePollIfPrivate", userService.checkPermissionToTakePollIfPrivate(pollId));
		model.put("isPollOutOfTime", !pollService.checkTime(pollId));
		return "questions-take";
	}

	@RequestMapping(value = "/poll/{pollId}/test-questions/submit", method = RequestMethod.POST)
	public String submit(@PathVariable Integer pollId, @RequestParam Map<String, String> requestParams,
			Map<String, Object> model) {
		List<Integer> choosenAnswers = requestParams.entrySet().stream()
				.filter(x -> StringUtils.startsWith(x.getKey(), "choosenAnswers"))
				.map(x -> Integer.parseInt(x.getValue())).collect(Collectors.toList());

		testResultService.saveIfUserHasntPassedPollBefore(pollId, choosenAnswers);
		return "redirect:/poll/{pollId}/poll-questions/take";
	}
}