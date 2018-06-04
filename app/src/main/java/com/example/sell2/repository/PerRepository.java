package com.example.sell2.repository;

import com.example.sell2.entity.SysPermission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PerRepository extends JpaRepository<SysPermission,Integer> {
}
