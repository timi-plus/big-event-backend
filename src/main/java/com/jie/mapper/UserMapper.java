package com.jie.mapper;

import com.jie.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper {
    //根据用户名查询用户
    @Select("select * from user where username=#{username}")
    User findByUserName(String username);

    //添加
    @Insert("insert into user(username,password,create_time,update_time)"+
            " values(#{username},#{md5password},now(),now())")
    void add(String username, String md5password);

    //修改
    @Update("update user set nickname=#{nickname},email=#{email},update_time=#{updateTime} where id = #{id}")
    void update(User user);

    //修改用户头像
    @Update("update user set user_pic=#{avatar},update_time=now() where id = #{id}")
    void updateAvatar(String avatar,Integer id);

    //修改密码
    @Update("update user set password=#{newPwd},update_time=now() where id = #{id}")
    void updatePwd(String newPwd, Integer id);
}
