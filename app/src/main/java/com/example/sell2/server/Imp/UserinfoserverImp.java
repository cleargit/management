package com.example.sell2.server.Imp;

import com.example.sell2.entity.UserInfo;
import com.example.sell2.repository.Myrepository;
import com.example.sell2.resultvo.Uservo;
import com.example.sell2.server.Userinfoserver;
import com.example.sell2.util.Conver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserinfoserverImp implements Userinfoserver {

    @Autowired
    private Myrepository myrepository;

    @Override
    public UserInfo findone(Integer id) {
        return myrepository.findById(id).get();
    }

    @Override
    public UserInfo insert(UserInfo userInfo) {
        return myrepository.save(userInfo);
    }

    @Override
    public List<Uservo> findAll() {

        List<UserInfo> list=myrepository.findAll();
        return Conver.uservos(list);
    }

    @Override
    public List<UserInfo> finall(List<Integer> list) {

        return myrepository.findAllById(list);
    }

    @Override
    public Boolean delete(Integer id) {
        myrepository.deleteById(id);
        if (myrepository.findById(id).get()==null)
        {
            return true;
        }
       return false;
    }

    @Override
    public void incrementAndUpdate(UserInfo userInfo) {
//        userInfo.setNum(userInfo.getNum()+1);
    }

    @Override
    public UserInfo findbyname(String name) {
        return myrepository.findByUsername(name);
    }

}
