package com.markkryzh.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.markkryzh.entity.User;

public interface UserRepository extends JpaRepository<User, Integer>{
	User findByEmail(String email);
	User findByName(String name);
	List<User> findByNameContainsIgnoreCase(String fragment);
	boolean existsUserByName(String name);
	List<User> findByNameIn(List<String> names);
}
