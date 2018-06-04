package com.example.sell2.resultvo;

import lombok.Data;

import java.io.Serializable;

@Data
public class ResultVo<T> implements Serializable {
    private static final long serialVersionUID = 2954243297301400530L;
    int code;
    String msg;
    T data;
}
