package com.example.sell2.repository;

import com.example.sell2.entity.Bbs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BbsRepository extends JpaRepository<Bbs,Integer> {
   List<Bbs> findAllByIskAndOb(String isk,String ob);
   Page<Bbs> findByOb(String ob, Pageable pageable);
   Bbs findByIskAndOb(String isk,String ob);
   Page<Bbs> findByObAndAid(String ob,Integer aid, Pageable pageable);
}
