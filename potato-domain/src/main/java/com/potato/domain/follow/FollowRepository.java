package com.potato.domain.follow;

import com.potato.domain.follow.repository.FollowRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long>, FollowRepositoryCustom {
}
