package com.bjtu.testmanageplatform.service;

import com.bjtu.testmanageplatform.InitConfig;
import com.bjtu.testmanageplatform.beans.RetriveUserResponse;
import com.bjtu.testmanageplatform.mapper.UserMapper;
import com.bjtu.testmanageplatform.model.User;
import com.bjtu.testmanageplatform.util.Conversion;
import com.bjtu.testmanageplatform.util.Encryption;
import com.bjtu.testmanageplatform.util.Generator;
import com.bjtu.testmanageplatform.util.JLog;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: gaofeng
 * @Date: 2019-07-09
 * @Description:
 */
@Service
public class UserService {

    private UserMapper userMapper;

    @Autowired
    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    /**
     * 根据用户id获取用户角色，用户角色定义见用户实体类
     *
     * @param userId
     *
     * @return
     */
    public Integer getRoleByUserId(Long userId) {
        return userMapper.selectRoleByUserId(userId);
    }

    /**
     * 根据用户名查找用户
     *
     * @param username
     *
     * @return
     */
    public User getUserByUsername(String username) {
        return userMapper.selectByUsername(username);
    }

    /**
     * 根据用户手机号查找用户
     *
     * @param phone
     *
     * @return
     */
    public User getUserByPhone(String phone) {
        return userMapper.selectByPhone(phone);
    }

    /**
     * 创建用户
     *
     * @param user
     *
     * @return
     */
    public Boolean create(User user) {
        JLog.info(String.format("enter create user phone=%s", user.getPhone()));
        // 按照公私钥解析出密码，对其进行加盐，然后进行md5
        // String decryptedPassword = Encryption.decryptData(user.getPassword(),
        //         InitConfig.Const.privateKey);
        String salt = Generator.createSalt();
        String md5Password = Conversion.getMD5(user.getPassword() + salt);

        // 补充完善user实体对象属性
        user.setUserId(Generator.generateLongId());
        user.setSalt(salt);
        user.setPassword(md5Password);
        user.setPortraitUrl("");

        JLog.info(user.toString());

        // 插入数据库
        Integer affect = userMapper.save(user);
        if (affect.equals(1)) {
            JLog.info(String.format("user save success userId=%s phone=%s", user.getUserId(),
                    user.getPhone()));
            return true;
        } else {
            JLog.error(String.format("user save failed userId=%s phone=%s errNo=101110127",
                    user.getUserId(), user.getPhone()), 101110127);
            return false;
        }
    }

    /**
     * 校验传来的密码与数据库中用户的密码是否相等
     *
     * @param resourcePassword
     * @param user
     *
     * @return
     */
    public Boolean checkPassword(String resourcePassword, User user) {
        // 传来的密码是被公钥加密过的，所以需要先用私钥解密
        // String decryptedPassword = Encryption.decryptData(resourcePassword,
        //         InitConfig.Const.privateKey);
        // 将解密出来的密码加盐之后md5
        String md5Password = Conversion.getMD5(resourcePassword + user.getSalt());
        // 判断两个密码是否相等返回结果
        if (md5Password.equals(user.getPassword())) {
            return true;
        }
        return false;
    }

    /**
     * 根据用户角色拉取用户列表
     *
     * @param role
     *
     * @return
     */
    public List<RetriveUserResponse.RetriveUserResData> getUsersByRole(Integer role) {
        JLog.info(String.format("enter getUsersByRole role=%s", role));
        List<User> users = userMapper.selectByRole(role);

        List<RetriveUserResponse.RetriveUserResData> retriveUserResDataList = new ArrayList<>();

        for (User user : users) {
            RetriveUserResponse.RetriveUserResData retriveUserResData =
                    new RetriveUserResponse.RetriveUserResData();
            BeanUtils.copyProperties(user, retriveUserResData);
            JLog.info(user.getUserId().toString());
            retriveUserResData.setUser_id(user.getUserId());
            retriveUserResData.setPortrait_url(user.getPortraitUrl());
            retriveUserResDataList.add(retriveUserResData);
        }

        return retriveUserResDataList;
    }


    /**
     * 判断传来的用户是否都为某种角色
     *
     * @param userIds
     * @param role
     *
     * @return
     */
    public Boolean checkUserRole(List<Long> userIds, Integer role) {
        JLog.info(String.format("enter checkUserRole role=%s", role));

        for (Long userId : userIds) {
            Integer roleInDb = userMapper.selectRoleByUserId(userId);
            if (!roleInDb.equals(role)) {
                return false;
            }
        }
        return true;
    }


    /**
     * 通过用户id查找用户姓名
     *
     * @param userId
     *
     * @return
     */
    public String getNameByUserId(Long userId) {
        JLog.info(String.format("enter getNameByUserId userId=%s", userId));
        return userMapper.selectNameByUserId(userId);
    }
}
