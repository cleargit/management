package com.example.sell2.hander;


import com.example.sell2.emums.BlogEnum;
import com.example.sell2.resultvo.ResultVo;
import com.example.sell2.util.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;


@ControllerAdvice
public class Hander {
    private Logger logger= LoggerFactory.getLogger(Hander.class);
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResultVo hander(Exception e)
    {
        if (e instanceof BlogException)
        {
            BlogException blogException=(BlogException)e;
            return ResultUtil.error(blogException.getCode(),blogException.getMessage());
        }
        if (e instanceof BindException)
        {
            BindException bx=(BindException)e;
            List<ObjectError> errors=bx.getAllErrors();
            ObjectError error=errors.get(0);
            String msg=error.getDefaultMessage();
            return ResultUtil.error(BlogEnum.BIND_ERROE.getCode(),msg);
        }
        logger.error(e.getMessage());
        return ResultUtil.error(BlogEnum.UNKONW);
    }

}
