package com.markkryzh.form;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.util.StringUtils;

public class PollForm {
	private boolean anonymous;
	private boolean private_;
	private boolean showAuthor;
	private List<Integer> communitiesIds;
	private PollQuestion[] pollQuestions;
	private TestQuestion[] testQuestions;
	private String[] testCategories;
	private String pollName;
	private Integer pollThemeId;
	private Timestamp fromDate;
	private Timestamp toDate;
	private String pollHashtags;
	
	public List<Integer> getCommunitiesIds() {
		return communitiesIds;
	}

	public void setCommunitiesIds(List<Integer> communitiesIds) {
		this.communitiesIds = communitiesIds;
	}

	public TestQuestion[] getTestQuestions() {
		return testQuestions;
	}

	public void setTestQuestions(TestQuestion[] testQuestions) {
		this.testQuestions = testQuestions;
	}

	public String[] getTestCategories() {
		return testCategories;
	}

	public void setTestCategories(String[] testCategories) {
		this.testCategories = testCategories;
	}

	public boolean isAnonymous() {
		return anonymous;
	}

	public void setAnonymous(boolean anonymous) {
		this.anonymous = anonymous;
	}

	public boolean isPrivate_() {
		return private_;
	}

	public void setPrivate_(boolean private_) {
		this.private_ = private_;
	}

	public boolean isShowAuthor() {
		return showAuthor;
	}

	public void setShowAuthor(boolean showAuthor) {
		this.showAuthor = showAuthor;
	}

	public String getPollHashtags() {
		return pollHashtags;
	}

	public void setPollHashtags(String pollHashtags) {
		this.pollHashtags = pollHashtags;
	}

	public Timestamp getFromDateTimestamp() {
		return fromDate;
	}

	public Timestamp getToDateTimestamp() {
		return toDate;
	}

	public String getFromDate() {
		return fromDate + "";
	}

	public String getToDate() {
		return toDate + "";
	}

	public void setToDate(Timestamp toDate) {
		this.toDate = toDate;
	}

	public void setToDate(String toDate) {
		if (!StringUtils.isEmpty(toDate))
			this.toDate = Timestamp.valueOf(toDate);
	}

	public void setFromDate(Timestamp fromDate) {
		this.fromDate = fromDate;
	}

	public void setFromDate(String fromDate) {
		if (!StringUtils.isEmpty(fromDate))
			this.fromDate = Timestamp.valueOf(fromDate);
	}

	public PollQuestion[] getPollQuestions() {
		return pollQuestions;
	}

	public void setPollQuestions(PollQuestion[] pollQuestions) {
		this.pollQuestions = pollQuestions;
	}

	public String getPollName() {
		return pollName;
	}

	public void setPollName(String pollName) {
		this.pollName = pollName;
	}

	public Integer getPollThemeId() {
		return pollThemeId;
	}

	public void setPollThemeId(Integer pollThemeId) {
		this.pollThemeId = pollThemeId;
	}

	static public class Question {
		private String question;
		private String imageName;
		private String imageUrl;

		public String getImageName() {
			return imageName;
		}

		public void setImageName(String imageName) {
			this.imageName = imageName;
		}

		public String getImageUrl() {
			return imageUrl;
		}

		public void setImageUrl(String imageUrl) {
			this.imageUrl = imageUrl;
		}

		public String getQuestion() {
			return question;
		}

		public void setQuestion(String question) {
			this.question = question;
		}
	}

	static public class PollQuestion extends Question {
		private Answer[] answers;
		private boolean allowOtherAnswer;

		public Answer[] getAnswers() {
			return answers;
		}

		public void setAnswers(Answer[] answers) {
			this.answers = answers;
		}

		public boolean isAllowOtherAnswer() {
			return allowOtherAnswer;
		}

		public void setAllowOtherAnswer(boolean allowOtherAnswer) {
			this.allowOtherAnswer = allowOtherAnswer;
		}
	}

	public static class TestQuestion extends Question{
		private TestAnswer[] answers;

		public TestAnswer[] getAnswers() {
			return answers;
		}

		public void setAnswers(TestAnswer[] answers) {
			this.answers = answers;
		}
	}
	
	static public class Answer {
		private String answer;
		private String imageName;
		private String imageUrl;

		public String getAnswer() {
			return answer;
		}

		public void setAnswer(String answer) {
			this.answer = answer;
		}

		public String getImageName() {
			return imageName;
		}

		public void setImageName(String imageName) {
			this.imageName = imageName;
		}

		public String getImageUrl() {
			return imageUrl;
		}

		public void setImageUrl(String imageUrl) {
			this.imageUrl = imageUrl;
		}

	}

	public static class TestAnswer extends Answer {
		private int[] categoriesScores;

		public int[] getCategoriesScores() {
			return categoriesScores;
		}

		public void setCategoriesScores(int[] categoriesScores) {
			this.categoriesScores = categoriesScores;
		}
	}

}
