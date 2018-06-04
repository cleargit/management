package com.example.sell2.server.Imp;

import com.example.sell2.entity.Blog;
import com.example.sell2.repository.BlogRepository;
import com.example.sell2.server.Blogserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BlogserverImp implements Blogserver {

    @Autowired
    private BlogRepository repository;


    @Override
    public Page<Blog> getList(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Blog findone(Integer id) {
        return repository.findById(id).get();
    }

    @Transactional
    @Override
    public Blog sava(Blog blog) {

        return repository.save(blog);
    }

    @Override
    public void decelt(Integer id) {

        repository.deleteById(id);
    }



}
