package com.markkryzh.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.markkryzh.form.PollResultResponse;
import com.markkryzh.service.PollResultService;
import com.markkryzh.service.PollService;
@Controller
public class PollResultController {
	@Autowired
	private PollResultService pollResultService;
	@Autowired
	private PollService pollService;
	
	@RequestMapping(value = "/poll/{pollId}/results")
	public String get(@PathVariable Integer pollId, Map<String, Object> model) {
		model.put("poll", pollService.findOne(pollId));
		return "poll-results";
	}

	@ResponseBody
	@RequestMapping(value = "/poll/{pollId}/results/getJson")
	public PollResultResponse getChartsData(@PathVariable Integer pollId, Map<String, Object> model) {
		return pollResultService.getPollResultResponse(pollId);
	}
}
