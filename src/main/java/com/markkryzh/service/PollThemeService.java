package com.markkryzh.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.markkryzh.entity.PollTheme;
import com.markkryzh.repository.PollThemeRepository;

@Service
public class PollThemeService {
	@Autowired
	PollThemeRepository pollThemeRepository;
	@Autowired
	UserService userService;
	public Integer save(PollTheme pollTheme){
		return pollThemeRepository.save(pollTheme).getId();
	}
	
	public List<PollTheme> findAll(){
		return new ArrayList<PollTheme>(pollThemeRepository.findAll());
	}
}
