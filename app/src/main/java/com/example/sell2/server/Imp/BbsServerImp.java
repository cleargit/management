package com.example.sell2.server.Imp;

import com.example.sell2.entity.Bbs;
import com.example.sell2.redis.BlogKey;
import com.example.sell2.repository.BbsRepository;
import com.example.sell2.server.BbsServer;
import com.example.sell2.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.util.Arrays;
import java.util.List;
@Service
public class BbsServerImp implements BbsServer {
    @Autowired
    private BbsRepository repository;

    @Autowired
    private RedisUtil redisUtil;
    @Override
    public Bbs sava(Bbs bbs) {
        return repository.save(bbs);
    }

    @Override
    public Bbs findone(Integer id) {
        return repository.findById(id).get();
    }

    @Override
    public Page<Bbs> getlist(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public List<Bbs> findByIsk(String isk) {
        return repository.findAllByIskAndOb(isk,"chick");
    }

    @Override
    public Page<Bbs> fiondByob(String ob, Pageable pageable) {

        Page<Bbs> page = repository.findByOb(ob, pageable);
        return page;

    }

    @Override
    public Bbs findoneByobAisk(String isk, String ob) {
        return repository.findByIskAndOb(isk,ob);
    }

    @Override
    public Page<Bbs> findByObAndAid(String ob, Integer aid, Pageable pageable) {
        return repository.findByObAndAid(ob,aid,pageable);
    }

    @Override
    public void delect(Bbs bbs) {
        repository.delete(bbs);
    }

    @Override
    public Boolean isDzan(int uid,int aid) {

        String key=String.valueOf(aid);
        Bbs one= ((Bbs) redisUtil.get(BlogKey.bbs,key));
        if(one==null)
        {
            one=repository.findById(aid).get();
            redisUtil.set(BlogKey.bbs,key,one);
        }
        String allUser=one.getOnly();
        if (allUser!=null&&allUser!="")
        {
            List<String> list=Arrays.asList(allUser.split("/"));
            if (list.contains(String.valueOf(uid))){
                return false;
            }
        }
        if (allUser==null){
            allUser="";
        }
        one.setOnly(allUser+uid+"/");
        one.setDzan(one.getDzan()+1);
        redisUtil.set(BlogKey.bbs,key,one);
        redisUtil.remove(BlogKey.article_bbs,String.valueOf(one.getAid()));
        repository.save(one);
        return true;
    }
}
