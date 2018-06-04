package com.example.sell2.redis;

public class BlogKey extends RedisKey {

    private static final int TOKEN_EXPIRE = 3600*24 * 2;
    private static final int REGISTER_EXPORE = 300;
    public BlogKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }
    public static  BlogKey token=new BlogKey(TOKEN_EXPIRE,"blog");
    public static BlogKey register=new BlogKey(REGISTER_EXPORE,"register");
    public static BlogKey bbs=new BlogKey(TOKEN_EXPIRE,"bbs");
    public static BlogKey bloglist=new BlogKey(TOKEN_EXPIRE,"bloglist");
    public static BlogKey article_bbs=new BlogKey(TOKEN_EXPIRE,"article_bbs");
    public static BlogKey userlog=new BlogKey(REGISTER_EXPORE/2,"log");
    public static BlogKey userlist=new BlogKey(REGISTER_EXPORE,"userlist");
    public static  BlogKey rolelist=new BlogKey(TOKEN_EXPIRE,"role");
    public static  BlogKey perlist=new BlogKey(TOKEN_EXPIRE,"per");
}
