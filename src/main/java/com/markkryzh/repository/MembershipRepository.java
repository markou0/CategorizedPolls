package com.markkryzh.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.markkryzh.entity.Membership;
import com.markkryzh.entity.User;

public interface MembershipRepository extends JpaRepository<Membership, Integer> {
	long countByCommunityIdAndAcceptedTrue(Integer communityId);
	List<Membership> findByCommunityIdAndAcceptedTrue(Integer communityId);
	List<Membership> findByUserIdAndAcceptedTrue(Integer userId);
	List<Membership> findByUserIdAndAcceptedFalse(Integer userId);
	Membership findOneByCommunityIdAndUserId(Integer communityId, Integer userId);
}
