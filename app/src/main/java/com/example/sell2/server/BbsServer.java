package com.example.sell2.server;

import com.example.sell2.entity.Bbs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BbsServer {
    Bbs sava(Bbs bbs);
    Bbs findone(Integer id);
    Page<Bbs> getlist(Pageable pageable);
    List<Bbs> findByIsk(String isk);
    Page<Bbs> fiondByob(String ob,Pageable pageable);
    Bbs findoneByobAisk(String isk,String ob);
    Page<Bbs> findByObAndAid(String ob,Integer aid, Pageable pageable);
    void delect(Bbs bbs);
    Boolean isDzan(int uid,int aid);
}
