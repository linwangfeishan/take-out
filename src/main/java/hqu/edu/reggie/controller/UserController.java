package hqu.edu.reggie.controller;



import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import hqu.edu.reggie.Utils.ValidateCodeUtils;
import hqu.edu.reggie.common.Result;
import hqu.edu.reggie.eneity.User;
import hqu.edu.reggie.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Value("${spring.mail.username}")
    private String from;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping("/sendMsg")
    public Result<String> sendMsg(@RequestBody User user, HttpSession session){
        String phone = user.getPhone();
        String subject = "迅雷餐购验证码";
        if(StringUtils.isNotEmpty(phone)){
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("code{}",code);
            String context ="欢迎使用迅雷餐购验证码是："+code+":五分钟内有效，请妥善保管";
            userService.sendMassage(phone,subject,context);
            //将验证码缓存到redis，设置过期时间五分钟
            redisTemplate.opsForValue().set(phone,code,5, TimeUnit.MINUTES);


//            session缓存验证码
//            session.setAttribute(phone,code);

            return Result.success("验证码发送成功");
        }
        return Result.error("验证码发送失败");
    }

    @PostMapping("/login")
    public Result<User> login(@RequestBody Map map,HttpSession session){
        String phone = map.get("phone").toString();
        String code = map.get("code").toString();

        Object codeInSession = redisTemplate.opsForValue().get(phone);
        if (code!=null&codeInSession.equals(code)){
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper();
            queryWrapper.eq(User::getPhone,phone);
            User user = userService.getOne(queryWrapper);
            if (user==null){
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            session.setAttribute("user",user.getId());

            redisTemplate.delete(phone);

            return Result.success(user);

        }
        return Result.error("登陆失败，请重新登录");

    }



}
