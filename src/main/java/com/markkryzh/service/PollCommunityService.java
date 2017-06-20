package com.markkryzh.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.markkryzh.entity.Community;
import com.markkryzh.entity.Poll;
import com.markkryzh.entity.PollCommunity;
import com.markkryzh.repository.PollCommunityRepository;

@Service
public class PollCommunityService {
	@Autowired
	PollService pollService;
	@Autowired
	CommunityService communityService;
	@Autowired
	PollCommunityRepository pollCommunityRepository;

	public List<PollCommunity> save(Poll poll, List<Community> communities) {
		List<PollCommunity> pollCommunities = new ArrayList<>();
		for (Community community : communities) {
			pollCommunities.add(new PollCommunity(community, poll));
		}
		return pollCommunityRepository.save(pollCommunities);
	}
	
	public List<PollCommunity> findByCommunitiesIds(List<Integer> communitiesIds){
		return pollCommunityRepository.findByCommunityIdIn(communitiesIds);
	}
}
