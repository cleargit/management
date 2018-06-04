package com.example.sell2.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class SysPermissionInit {
    @Id
    String id;
    String url;
    String permissionInit;
    int sort;
}