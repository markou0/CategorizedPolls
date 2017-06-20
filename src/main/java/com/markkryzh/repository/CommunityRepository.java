package com.markkryzh.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.markkryzh.entity.Community;

public interface CommunityRepository extends JpaRepository<Community, Integer> {
	List<Community> findByUserId(Integer userId);
	List<Community> findByIdIn(List<Integer> communitiesIds);
}
