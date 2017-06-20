package com.markkryzh.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.markkryzh.entity.PollResult;

public interface PollResultRepository extends JpaRepository<PollResult, Integer>{
	int countByPollAnswerId(Integer pollAnswerId);
	List<PollResult> findByPollAnswerId(Integer pollAnswerId);
	boolean existsByPollAnswerId(Integer pollAnswerId);
	long countByPollAnswerIdAndUserIdIn(Integer pollAnswerId,List<Integer> usersIds);
	boolean existsByPollAnswerIdInAndUserId(List<Integer> pollAnswersIds, Integer userId);
	List<PollResult> findByUserIdOrderByDateDesc(Integer userId);
	long countByPollAnswerIdIn(List<Integer> answersIds);
}
