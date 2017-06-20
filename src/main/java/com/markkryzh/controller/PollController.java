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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.markkryzh.entity.Poll;
import com.markkryzh.form.PollForm;
import com.markkryzh.service.CommunityService;
import com.markkryzh.service.PollResultService;
import com.markkryzh.service.PollService;
import com.markkryzh.service.PollThemeService;
import com.markkryzh.service.UserService;

@Controller
public class PollController {
	@Autowired
	private PollThemeService pollThemeService;
	@Autowired
	private CommunityService communityService;
	@Autowired
	private UserService userService;
	@Autowired
	private PollResultService pollResultService;
	@Autowired
	private PollService pollService;

	@RequestMapping("/poll/create")
	public String create(PollForm pollForm, Map<String, Object> model) {
		model.put("pollThemes", pollThemeService.findAll());
		model.put("userCommunities", communityService.findUserCommunities(userService.findAuthenticated().getId()));
		return "poll-create";
	}

	@ResponseBody
	@PostMapping("/poll/create")
	public Integer parse(@RequestBody PollForm pollForm) {
		Integer pollId = pollService.parsePollForm(pollForm);
		return pollId;
	}

	@RequestMapping("/poll/{pollId}/poll-questions/take")
	public String takeQuestions(@PathVariable Integer pollId, Map<String, Object> model) {
		Poll poll = pollService.findOne(pollId);
		model.put("poll", poll);
		model.put("hasUserPassedPollBefore", pollResultService.checkIfAuthorizedUserPassedPoll(pollId));
		model.put("hasUserPermissionToTakePollIfPrivate", userService.checkPermissionToTakePollIfPrivate(pollId));
		model.put("isPollOutOfTime", !pollService.checkTime(pollId));
		return "questions-take";
	}

	@RequestMapping(value = "/poll/{pollId}/poll-questions/submit", method = RequestMethod.POST)
	public String submit(@PathVariable Integer pollId, @RequestParam Map<String, String> requestParams,
			Map<String, Object> model) {
		List<Integer> choosenAnswersIds = requestParams.entrySet().stream()
				.filter(x -> StringUtils.startsWith(x.getKey(), "choosenAnswers"))
				.map(x -> Integer.parseInt(x.getValue())).collect(Collectors.toList());
		pollResultService.saveIfUserHasntPassedPollBefore(pollId, choosenAnswersIds);
		return "redirect:/poll/{pollId}/results";
	}
	
	@RequestMapping("/poll/{pollId}/remove")
	public RedirectView remove(RedirectAttributes redirectAttrs,
			@PathVariable("pollId") Integer pollId, Map<String, Object> model) {
		RedirectView rv = new RedirectView("/user/personal-cabinet");
		rv.setContextRelative(true);
		Poll poll = pollService.findOne(pollId);
		if (pollService.delete(pollId))
			redirectAttrs.addFlashAttribute("operationSuccess",
					"You you have been successfully delete the poll : <strong> \"" + poll.getName()
							+ "\"</strong");
		else
			redirectAttrs.addFlashAttribute("operationError", "You can`t delete this poll : <strong>\""
					+ poll.getName() + "\"</strong> due to the fact you must be creator of that poll!");
		return rv;
	}

}