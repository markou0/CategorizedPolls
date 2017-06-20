package com.markkryzh.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.markkryzh.entity.PollCommunity;

public interface PollCommunityRepository extends JpaRepository<PollCommunity, Integer>{
public List<PollCommunity> findByCommunityIdIn(List<Integer> communityIds);
}
