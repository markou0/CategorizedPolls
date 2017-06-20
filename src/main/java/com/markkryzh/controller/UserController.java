package com.markkryzh.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.markkryzh.entity.Community;
import com.markkryzh.entity.User;
import com.markkryzh.service.CommunityService;
import com.markkryzh.service.PollService;
import com.markkryzh.service.UserService;

@Controller
public class UserController {
	@Autowired
	private CommunityService communityService;
	@Autowired
	private UserService userService;
	@Autowired
	private PollService pollService;
	
	@ResponseBody
	@RequestMapping(value = "/users-search-autocomplete")
	public List<String> autoComplete(@RequestParam("term") String term) {
		List<String> terms = userService.findByNameContains(term);
		;
		return terms;
	}
	
	@RequestMapping("/user/{username}")
	public String userPage(@PathVariable String username, Map<String, Object> model) {
		User user = userService.findByName(username);
		model.put("user", user);
		model.put("userPolls", pollService.findByUserId(user.getId()));
		model.put("userCommunities", communityService.findUserCommunities(user.getId()));
		return "personal-сabinet";
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
	
	@RequestMapping("/user/{userId}/remove")
	public RedirectView remove(RedirectAttributes redirectAttrs,
			@PathVariable("communityId") Integer userId, Map<String, Object> model) {
		RedirectView rv = new RedirectView("/user/personal-cabinet");
		rv.setContextRelative(true);
		userService.delete(userId);
		return rv;
	}
}
