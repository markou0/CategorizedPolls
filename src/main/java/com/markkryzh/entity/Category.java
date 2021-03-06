package com.markkryzh.entity;
// Generated 19 ����. 2017 0:27:24 by Hibernate Tools 5.2.0.Beta1

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Category generated by hbm2java
 */
@Entity
@Table(name = "category", schema = "public")
public class Category implements java.io.Serializable{

	private Integer id;
	private Test test;
	private String name;
	private Set<TestAnswerScore> testAnswerScores = new HashSet<TestAnswerScore>(0);
	private Set<TestResult> testResults = new HashSet<TestResult>(0);

	public Category() {
	}

	public Category(Test test, String name) {
		super();
		this.test = test;
		this.name = name;
	}

	@Id
	@Column(name = "id", unique = true, nullable = false)
	@GeneratedValue
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "test_id", nullable = false)
	public Test getTest() {
		return this.test;
	}

	public void setTest(Test test) {
		this.test = test;
	}

	@Column(name = "name", nullable = false)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "category")
	public Set<TestAnswerScore> getTestAnswerScores() {
		return this.testAnswerScores;
	}

	public void setTestAnswerScores(Set<TestAnswerScore> testAnswerScores) {
		this.testAnswerScores = testAnswerScores;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "category")
	public Set<TestResult> getTestResults() {
		return this.testResults;
	}

	public void setTestResults(Set<TestResult> testResults) {
		this.testResults = testResults;
	}
	
}
