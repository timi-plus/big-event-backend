package com.jie.controller;


import com.jie.pojo.Result;
import com.jie.pojo.User;
import com.jie.service.UserService;

import com.jie.utils.JwtUtil;
import com.jie.utils.Md5Util;
import com.jie.utils.ThreadLocalUtil;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
@Validated
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    //注册
    @PostMapping("/register")
    public Result register(@Pattern(regexp = "^\\S{5,16}$") String username, @Pattern(regexp = "^\\S{5,16}$") String password) {
        //查询用户

        User user = userService.findByUserName(username);
        if(user ==null){
            //没有被占用
            //注册
            userService.register(username,password);
            return Result.success();
        }else {
            //被占用了
            return Result.error("用户名被占用");
        }

    }
    //登录
    @PostMapping("/login")
    public Result<String> login(@Pattern(regexp = "^\\S{5,16}$") String username, @Pattern(regexp = "^\\S{5,16}$") String password) {
        //查询用户
        User user = userService.findByUserName(username);

        if(user ==null){
            //没有被占用
            return Result.error("用户名不存在");
        }
        //密码校验
        if(Md5Util.getMD5String(password).equals(user.getPassword())){
            //登录成功
            //生成token
            Map<String, Object> claims = new HashMap<>();
            claims.put("id",user.getId());
            claims.put("username",user.getUsername());
            String token = JwtUtil.genToken( claims);
            //redis 保存token
            ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
            operations.set(token,token,1, TimeUnit.HOURS);
            return Result.success(token);
        }
        return Result.error("密码错误");
     }
     //获取用户信息
    @GetMapping("/userInfo")
     public Result<User> userinfo(/*@RequestHeader(name = "Authorization") String tocken*/){

        /*Map<String, Object> claims = JwtUtil.parseToken(tocken);
        String username = (String) claims.get("username");*/
        //获取当前用户
        Map<String, Object> claims = ThreadLocalUtil.get();
        String username = (String) claims.get("username");
        User user = userService.findByUserName(username);
        return Result.success(user);
    }

    //修改用户信息
    @PutMapping("/update")
    public Result update(@RequestBody @Validated User user){

        userService.update(user);
        return Result.success();
    }

    //更新用户头像
    @PatchMapping("/updateAvatar")
    public Result updateAvatar(@RequestParam @URL String avatarUrl){
        //获取当前用户
        userService.updateAvatar(avatarUrl);
        return Result.success();
    }

    //更新密码
    @PatchMapping("/updatePwd")
    public Result updatePwd(@RequestBody Map<String,String> map,@RequestHeader("Authorization") String token){

        //1.校验参数
        String oldPwd = map.get("old_pwd");
        String newPwd = map.get("new_pwd");
        String rePwd = map.get("re_pwd");

        if(!StringUtils.hasLength(oldPwd) || !StringUtils.hasLength(newPwd) || !StringUtils.hasLength(rePwd)){
            return Result.error("缺少必要的参数");
        }

        //2.校验旧密码
        //调用userService根据用户拿到的原密码，在和old_Pwd进行比较
        Map<String, Object> claims = ThreadLocalUtil.get();
        String username = (String) claims.get("username");
        User loginUser = userService.findByUserName(username);
        if(!loginUser.getPassword().equals(Md5Util.getMD5String(oldPwd))){
            return Result.error("原密码错误");
        }
        //newpwd == repwd
        if(!newPwd.equals(rePwd)){
            return Result.error("两次密码不一致");
        }

        //2.调用service修改密码
        userService.updatePwd(newPwd);
        //删除redis旧token
        stringRedisTemplate.opsForValue().getOperations().delete(token);

        return Result.success();
    }


}
