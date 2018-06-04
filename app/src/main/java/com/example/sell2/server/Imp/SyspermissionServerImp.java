package com.example.sell2.server.Imp;

import com.example.sell2.entity.SysPermissionInit;
import com.example.sell2.repository.SysPermissionRepsitory;
import com.example.sell2.server.SyspermissionServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SyspermissionServerImp implements SyspermissionServer {
    @Autowired
    private SysPermissionRepsitory repsitory;
    @Override
    public List<SysPermissionInit> findAllpermission() {

        return repsitory.findAll();
    }
}
