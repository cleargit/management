package com.example.sell2.util;

import com.example.sell2.emums.BlogEnum;
import com.example.sell2.resultvo.ResultVo;

public class ResultUtil {

 public static ResultVo success(Object object)
 {
     ResultVo resultVo=new ResultVo();
     resultVo.setCode(0);
     resultVo.setMsg("成功");
     resultVo.setData(object);
     return resultVo;
 }
    public static ResultVo success(Object object,int pagesize)
    {
        ResultVo resultVo=new ResultVo();
        resultVo.setCode(0);
        resultVo.setMsg(String.valueOf(pagesize));
        resultVo.setData(object);
        return resultVo;
    }
    public static ResultVo error(BlogEnum blogEnum)
    {
        ResultVo resultVo=new ResultVo();
        resultVo.setCode(blogEnum.getCode());
        resultVo.setMsg("失败");
        resultVo.setData(blogEnum.getMessage());
        return resultVo;
    }
    public static ResultVo error(int code,String msg )
    {
        ResultVo resultVo=new ResultVo();
        resultVo.setCode(code);
        resultVo.setMsg("失败");
        resultVo.setData(msg);
        return resultVo;
    }

}
