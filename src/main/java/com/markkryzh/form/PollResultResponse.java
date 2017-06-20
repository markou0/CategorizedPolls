package com.markkryzh.form;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class PollResultResponse {
	private List<QuestionResult> questionResultList; 
	private List<Rating> categoryRatingList;
	
	public PollResultResponse(List<QuestionResult> questionResultList, List<Rating> categoryRatingList) {
		super();
		this.questionResultList = questionResultList;
		this.categoryRatingList = categoryRatingList;
	}
	
	public static class QuestionResult{
		
		private List<Rating> answerRatingList;
		private HashMap<String,List<Rating>> answerRatingByCategoryMap; 
		
		public QuestionResult(List<Rating> answerRatingList, HashMap<String, List<Rating>> answerRatingByCategoryMap) {
			super();
			this.answerRatingList = answerRatingList;
			this.answerRatingByCategoryMap = answerRatingByCategoryMap;
		}
		public HashMap<String, List<Rating>> getAnswerRatingByCategoryMap() {
			return answerRatingByCategoryMap;
		}
		public void setAnswerRatingByCategoryMap(HashMap<String, List<Rating>> answerRatingByCategoryMap) {
			this.answerRatingByCategoryMap = answerRatingByCategoryMap;
		}
		public List<Rating> getAnswerRatingList() {
			return answerRatingList;
		}
		public void setAnswerRatingList(List<Rating> answerRatingList) {
			this.answerRatingList = answerRatingList;
		}

	}
	public static class Rating {
		private String label;
		private long rating;
		
		public Rating(String label, long answerRatingByCategory) {
			super();
			this.label = label;
			this.rating = answerRatingByCategory;
		}
		
		public String getLabel() {
			return label;
		}
		public void setLabel(String label) {
			this.label = label;
		}
		public long getRating() {
			return rating;
		}
		public void setRating(int rating) {
			this.rating = rating;
		}
		
	}
	
	public List<QuestionResult> getQuestionResultList() {
		return questionResultList;
	}
	public void setQuestionResultList(List<QuestionResult> questionResultList) {
		this.questionResultList = questionResultList;
	}
	public List<Rating> getCategoryRatingList() {
		return categoryRatingList;
	}
	public void setCategoryRatingList(List<Rating> categoryRatingList) {
		this.categoryRatingList = categoryRatingList;
	}

}
