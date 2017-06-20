package com.markkryzh.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.markkryzh.entity.Community;
import com.markkryzh.entity.Membership;
import com.markkryzh.entity.User;
import com.markkryzh.repository.CommunityRepository;
import com.markkryzh.repository.MembershipRepository;

@Service
public class MembershipService {
	@Autowired
	UserService userService;
	@Autowired
	CommunityService communityService;
	@Autowired
	MembershipRepository memebershipRepository;

	void save(Community community, List<User> users) {
		List<Membership> memberships = new ArrayList<>();
		for (User user : users) {
			memberships.add(new Membership(community, user));
		}
		memebershipRepository.save(memberships);
	}

	public long countByCommunityIdAndAcceptedTrue(Integer communityId) {
		return memebershipRepository.countByCommunityIdAndAcceptedTrue(communityId);
	}

	public List<Membership> findByCommunityIdAndAcceptedTrue(Integer communityId) {
		return memebershipRepository.findByCommunityIdAndAcceptedTrue(communityId);
	}

	public List<Membership> findByUserIdAndAcceptedTrue(Integer userId) {
		return memebershipRepository.findByUserIdAndAcceptedTrue(userId);
	}

	public List<Membership> findByUserIdAndAcceptedFalse(Integer userId) {
		return memebershipRepository.findByUserIdAndAcceptedFalse(userId);
	}

	public boolean acceptCommunityMembershipIfExistsInvite(Integer communityId) {
		Membership membership = memebershipRepository.findOneByCommunityIdAndUserId(communityId,
				userService.findAuthenticated().getId());
		if (membership == null)
			return false;
		membership.setAccepted(true);
		memebershipRepository.save(membership);
		return true;
	}
	
	public boolean leaveCommunityMembershipIfExistsInvite(Integer communityId) {
		Membership membership = memebershipRepository.findOneByCommunityIdAndUserId(communityId,
				userService.findAuthenticated().getId());
		if (membership == null)
			return false;
		memebershipRepository.delete(membership);
		return true;
	}
}
