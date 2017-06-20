package com.markkryzh.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.markkryzh.entity.Community;
import com.markkryzh.entity.Membership;
import com.markkryzh.entity.Poll;
import com.markkryzh.entity.PollCommunity;
import com.markkryzh.entity.TestResult;
import com.markkryzh.entity.User;
import com.markkryzh.repository.TestResultRepository;
import com.markkryzh.repository.UserRepository;

@Service
public class UserService {
	@Autowired
	PollService pollService;
	@Autowired
	MembershipService membershipService;
	@Autowired
	CommunityService communityService;
	@Autowired
	TestResultRepository testResultRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	public User findUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public void save(User user) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		userRepository.save(user);
	}

	public User findByName(String name) {
		return userRepository.findByName(name);
	}

	public User findAuthenticated() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		System.err.println("auth.getName()"+auth.getName());
		return userRepository.findByEmail(auth.getName());
	}
	
	public boolean isCurrentAuthenticated(User user){
		return user.getEmail().equals(SecurityContextHolder.getContext().getAuthentication().getName());
	}
	
	public boolean isAdmin(){
		System.err.println("is admin ?"+SecurityContextHolder.getContext().getAuthentication().getAuthorities().iterator().next().getAuthority());
		return SecurityContextHolder.getContext().getAuthentication().getAuthorities().iterator().next().getAuthority().equals("ADMIN");
	}

	List<User> findByCategoryId(Integer categoryId) {
		List<TestResult> testResults = testResultRepository.findByCategoryId(categoryId);
		return testResults.stream().map(t -> t.getUser()).collect(Collectors.toList());
	}

	public List<String> findByNameContains(String fragment) {
		return userRepository.findByNameContainsIgnoreCase(fragment).stream().map(u -> u.getName()).collect(Collectors.toList());
	}

	boolean existsByName(String name) {
		return userRepository.existsUserByName(name);
	}

	public List<String> checkNonExistentsByNames(String usernamesStr) {
		List<String> nonExistentUsers = new ArrayList<>();
		List<String> usernames = splitUsernamesStr(usernamesStr);
		for (String username : usernames) {
			if (!existsByName(username))
				nonExistentUsers.add(username);
		}
		return nonExistentUsers;
	}

	List<User> findByNameIn(List<String> names) {
		return userRepository.findByNameIn(names);
	}

	List<User> findByAllNamesStr(String usernamesStr) {
		if (checkNonExistentsByNames(usernamesStr).size() > 0)
			return null;
		return findByNameIn(splitUsernamesStr(usernamesStr));
	}

	public List<User> findByCommunityId(Integer communityId) {
		return membershipService.findByCommunityIdAndAcceptedTrue(communityId).stream().map(m -> m.getUser())
				.collect(Collectors.toList());
	}

	List<String> splitUsernamesStr(String usernamesStr) {
		List<String> usernames = new ArrayList<>();
		usernamesStr = usernamesStr.trim();
		if (StringUtils.endsWith(usernamesStr, ","))
			usernamesStr = usernamesStr.substring(0, usernamesStr.length() - 1);
		for (String username : Arrays.asList(usernamesStr.split(","))) {
			usernames.add(username.trim());
		}
		return usernames;
	}

	private List<User> findByPermissionToTakePollIfPrivate(Poll poll) {
		if (!poll.isPrivat())
			return null;
		Set<PollCommunity> pollCommunities = poll.getPollCommunities();
		if (pollCommunities != null && pollCommunities.size() > 0) {
			return pollCommunities.stream().map(pc -> pc.getCommunity()).flatMap(c -> c.getMemberships().stream())
					.map(m -> m.getUser()).distinct().collect(Collectors.toList());
		}
		return null;
	}

	public boolean checkPermissionToTakePollIfPrivate(Integer pollId) {
		Poll poll = pollService.findOne(pollId);
		if (!poll.isPrivat())
			return true;
		User user = findAuthenticated();
		List<User> users = findByPermissionToTakePollIfPrivate(poll);
		if (users != null && users.size() > 0)
			return users.contains(user)||user.equals(poll.getUser());
		return false;
	}
	
	public boolean delete(Integer userId){
		if(isAdmin()){
			userRepository.delete(userId);
			return true;
		}
		return false;
	}
}
