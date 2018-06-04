package com.example.sell2.repository;

import com.example.sell2.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface Myrepository extends JpaRepository<UserInfo,Integer> {
    @Query(" from UserInfo x where x.uid= :uid")
    UserInfo a(@Param("uid") Integer uid);
    UserInfo findByUsername(String name);
}
