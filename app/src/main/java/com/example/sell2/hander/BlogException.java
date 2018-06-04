package com.example.sell2.hander;

import com.example.sell2.emums.BlogEnum;

public class BlogException extends RuntimeException {
    private Integer code;
    public BlogException(Integer code,String message){
        super(message);
        this.code=code;
    }

    public Integer getCode() {
        return code;
    }
    public BlogException(BlogEnum blogEnum)
    {
        super(blogEnum.getMessage());
        this.code=blogEnum.getCode();
    }
}
