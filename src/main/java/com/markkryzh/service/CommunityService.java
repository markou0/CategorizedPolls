package com.markkryzh.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.markkryzh.entity.Community;
import com.markkryzh.entity.Membership;
import com.markkryzh.entity.User;
import com.markkryzh.repository.CommunityRepository;
import com.markkryzh.repository.MembershipRepository;

@Service
public class CommunityService {
	@Autowired
	UserService userService;
	@Autowired
	CommunityRepository communityRepository;
	@Autowired
	MembershipService memebershipService;

	public Integer save(String communityName, String membersUsernames) {
		List<User> users = userService.findByAllNamesStr(membersUsernames);
		System.err.println("users" + users);
		if (users == null)
			return null;
		Community community = new Community(userService.findAuthenticated(), communityName);
		communityRepository.save(community);
		memebershipService.save(community, users);
		return community.getId();
	}

	List<Community> findMembers(List<Community> communities) {
		for (Community community : communities) {
			community.setMembers(userService.findByCommunityId(community.getId()));
		}
		return communities;
	}

	public Community findOne(Integer communityId) {
		return communityRepository.findOne(communityId);
	}

	public List<Community> findUserCommunities(Integer userId) {
		List<Community> communities = memebershipService.findByUserIdAndAcceptedTrue(userId).stream()
				.map(m -> m.getCommunity()).collect(Collectors.toSet()).stream().collect(Collectors.toList());
		communities.addAll(findCreatedByUser(userId));
		setMembers(communities);
		return communities;
	}

	public List<Community> findCreatedByUser(Integer userId) {
		List<Community> communities = communityRepository.findByUserId(userId);
		setMembers(communities);
		return communities;
	}

	public List<Community> findNotAcceptedByUser(Integer userId) {
		List<Community> communities = memebershipService.findByUserIdAndAcceptedFalse(userId).stream()
				.map(m -> m.getCommunity()).collect(Collectors.toSet()).stream().collect(Collectors.toList());
		setMembers(communities);
		return communities;
	}

	public List<Community> setMembers(List<Community> communities) {
		communities.forEach(c -> c.setMembers(userService.findByCommunityId(c.getId())));
		return communities;
	}
	
	public List<Community> findByIdIn(List<Integer> communitiesIds){
		return communityRepository.findByIdIn(communitiesIds);
	}
	
	public boolean delete(Integer communityId){
		System.err.println("isAuthor(communityId)"+isAuthor(communityId));
		if(userService.isAdmin()||isAuthor(communityId)){
			communityRepository.delete(communityId);
			return true;
		}
		return false;
	}
	
	public boolean isAuthor(Integer communityId){
		return userService.isCurrentAuthenticated(findOne(communityId).getUser());
	}
}
