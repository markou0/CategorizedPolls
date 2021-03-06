package com.markkryzh.entity;
// Generated 5 ����. 2017 18:39:50 by Hibernate Tools 5.2.0.Beta1

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

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
 * Test generated by hbm2java
 */
@Entity
@Table(name = "test", schema = "public")
public class Test implements java.io.Serializable {

	private Integer id;
	private Poll poll;
	private Set<Category> categories = new HashSet<>(0);
	private Set<TestQuestion> testQuestions = new HashSet<>(0);

	public Test() {
	}

	public Test(Poll poll, Set<Category> categories, Set<TestQuestion> testQuestions) {
		super();
		this.poll = poll;
		this.categories = categories;
		this.testQuestions = testQuestions;
	}

	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "poll_id", nullable = false)
	public Poll getPoll() {
		return this.poll;
	}

	public void setPoll(Poll poll) {
		this.poll = poll;
	}

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "test")
	public Set<Category> getCategories() {
		return this.categories;
	}

	public void setCategories(Set<Category> categories) {
		this.categories = categories;
	}

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "test")
	public Set<TestQuestion> getTestQuestions() {
		return this.testQuestions;
	}

	public void setTestQuestions(Set<TestQuestion> testQuestions) {
		this.testQuestions = testQuestions;
	}

}
