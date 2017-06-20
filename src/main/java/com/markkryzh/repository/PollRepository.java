package com.markkryzh.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.markkryzh.entity.Poll;

public interface PollRepository extends JpaRepository<Poll, Integer> {
	List<Poll> findByUserId(Integer userId);
	List<Poll> findByHashTagsContainsIgnoreCase(String hashtags);
	List<Poll> findByPollThemeNameContainsIgnoreCase(String pollThemeName);
	List<Poll> findByPrivatTrueAndUserId(Integer userId);
	List<Poll> findByPrivatFalse();
	List<Poll> findByPrivatFalseOrderByDateDesc();
}
