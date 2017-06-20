package com.markkryzh.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.markkryzh.entity.PollAnswer;
import com.markkryzh.repository.PollAnswerRepository;
import com.markkryzh.repository.PollQuestionRepository;
import com.markkryzh.repository.PollRepository;

@Service
public class PollAnswerService {
	@Autowired
	PollRepository pollRepository;
	@Autowired
	PollAnswerRepository pollAnswerRepository;
	@Autowired
	PollQuestionRepository pollQuestionRepository;

	public List<PollAnswer> findByPollId(Integer pollId) {
		return pollRepository.findOne(pollId).getPollQuestions().stream().flatMap(q -> q.getPollAnswers().stream())
				.collect(Collectors.toList());
	}
	
	public Set<PollAnswer> findByPollQuestionId(Integer pollQuestionId) {
		return pollQuestionRepository.findOne(pollQuestionId).getPollAnswers();
	}
	
	public List<PollAnswer> findByIdIn(List<Integer> ids) {
		return pollAnswerRepository.findByIdIn(ids);
	}
}
