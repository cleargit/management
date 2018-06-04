package com.example.sell2.server;

import com.example.sell2.entity.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface Blogserver {
    Page<Blog> getList(Pageable pageable);
    Blog findone(Integer id);
    Blog sava(Blog blog);
    void decelt(Integer id);
}
