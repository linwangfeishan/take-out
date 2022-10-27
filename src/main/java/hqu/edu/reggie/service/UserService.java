package hqu.edu.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import hqu.edu.reggie.eneity.User;
import org.springframework.stereotype.Service;


public interface UserService extends IService<User> {

    void sendMassage(String to,String subject,String context);
}
