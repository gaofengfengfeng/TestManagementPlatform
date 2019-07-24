package com.bjtu.testmanageplatform.mapper;

import com.bjtu.testmanageplatform.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: gaofeng
 * @Date: 2019-07-09
 * @Description:
 */
@Component
@Mapper
public interface UserMapper {

    @Insert("INSERT INTO user(user_id, username, password, salt, name, phone, department, " +
            "portrait_url, role, create_time) VALUES(#{userId}, #{username}, #{password}, " +
            "#{salt}, #{name}, #{phone}, #{department}, #{portraitUrl}, #{role}, #{createTime})")
    Integer save(User user);

    @Select("SELECT role from user WHERE user_id=#{userId}")
    Integer selectRoleByUserId(Long userId);

    @Select("SELECT name from user WHERE user_id=#{userId}")
    String selectNameByUserId(Long userId);

    @Select("SELECT * FROM user where username=#{username}")
    @Results({
            @Result(property = "userId", column = "user_id"),
            @Result(property = "portraitUrl", column = "portrait_url"),
            @Result(property = "createTime", column = "create_time")
    })
    User selectByUsername(String username);

    @Select("SELECT * FROM user where phone=#{phone}")
    @Results({
            @Result(property = "userId", column = "user_id"),
            @Result(property = "portraitUrl", column = "portrait_url"),
            @Result(property = "createTime", column = "create_time")
    })
    User selectByPhone(String phone);

    @Select("SELECT * FROM user WHERE role=#{role}")
    @Results({
            @Result(property = "userId", column = "user_id"),
            @Result(property = "portraitUrl", column = "portrait_url"),
            @Result(property = "createTime", column = "create_time")
    })
    List<User> selectByRole(Integer role);
}
