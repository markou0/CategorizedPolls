package com.markkryzh.controller;

import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.markkryzh.entity.Community;
import com.markkryzh.entity.PollTheme;
import com.markkryzh.entity.User;
import com.markkryzh.service.CommunityService;
import com.markkryzh.service.PollService;
import com.markkryzh.service.PollThemeService;
import com.markkryzh.service.UserService;

@Controller
public class MainController {
	@Autowired
	private PollThemeService pollThemeService;
	@Autowired
	private CommunityService communityService;
	@Autowired
	private UserService userService;
	@Autowired
	private PollService pollService;

	@RequestMapping({"/","main"})
	public String index(Map<String, Object> model) {
		model.put("popularPolls",pollService.findPopular());
		model.put("newestPolls", pollService.findNewest());
		return "main";
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
	@RequestMapping(value = "/poll-theme/create")
	public PollTheme createPollTheme(@RequestBody PollTheme pollTheme) {
		if (StringUtils.isEmpty(pollTheme.getName()) || pollThemeService.save(pollTheme) == null)
			return null;
		return pollTheme;
	}

}