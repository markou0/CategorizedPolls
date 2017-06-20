package com.markkryzh.controller;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.markkryzh.entity.Community;
import com.markkryzh.service.CommunityService;
import com.markkryzh.service.MembershipService;
import com.markkryzh.service.UserService;

@Controller
public class CommunityController {
	@Autowired
	private MembershipService membershipService;
	@Autowired
	private CommunityService communityService;
	@Autowired
	private UserService userService;

	
	@RequestMapping("/community/create")
	public RedirectView create(@RequestParam("community-name") String communityName,
			@RequestParam("members-usernames") String membersUsernames, RedirectAttributes redirectAttrs) {
		RedirectView rv = new RedirectView("/user/personal-cabinet");
		rv.setContextRelative(true);
		if (communityService.save(communityName, membersUsernames) != null) {
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
	public RedirectView accept(RedirectAttributes redirectAttrs,
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
	public RedirectView leave(RedirectAttributes redirectAttrs,
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
	}
	
	@RequestMapping("/community/{communityId}/remove")
	public RedirectView remove(RedirectAttributes redirectAttrs,
			@PathVariable("communityId") Integer communityId, Map<String, Object> model) {
		RedirectView rv = new RedirectView("/user/personal-cabinet");
		rv.setContextRelative(true);
		Community community = communityService.findOne(communityId);
		if (communityService.delete(communityId))
			redirectAttrs.addFlashAttribute("operationSuccess",
					"You you have been successfully delete the community : <strong> \"" + community.getName()
							+ "\"</strong");
		else
			redirectAttrs.addFlashAttribute("operationError", "You can`t delete this community : <strong>\""
					+ community.getName() + "\"</strong> due to the fact you must be creator of that community!");
		return rv;
	}
}