package com.bjtu.testmanageplatform.controller;

import com.bjtu.testmanageplatform.InitConfig;
import com.bjtu.testmanageplatform.beans.*;
import com.bjtu.testmanageplatform.beans.base.JResponse;
import com.bjtu.testmanageplatform.beans.base.UserProfile;
import com.bjtu.testmanageplatform.model.User;
import com.bjtu.testmanageplatform.service.UserService;
import com.bjtu.testmanageplatform.util.Encryption;
import com.bjtu.testmanageplatform.util.Generator;
import com.bjtu.testmanageplatform.util.service.JRedisPoolService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * @Author: gaofeng
 * @Date: 2019-07-04
 * @Description: 用户中心
 */
@Slf4j
@RestController
@RequestMapping(value = "/v1/user")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 登录
     *
     * @param request
     * @param loginReq
     *
     * @return
     */
    @RequestMapping(value = "/login")
    public LoginResponse doLogin(HttpServletRequest request,
                                 @RequestBody @Valid LoginReq loginReq) {
        LoginReq.LoginData loginData = loginReq.getData();
        log.info("login username={}", loginData.getUsername());
        LoginResponse loginResponse = new LoginResponse();

        // 判断是否存在该用户
        User user = userService.getUserByUsername(loginData.getUsername());
        log.info(user.toString());
        if (user == null) {
            loginResponse.setErr_no(101131841);
            loginResponse.setErr_msg("no this username in db");
            return loginResponse;
        }
        // 判断密码是否正确
        if (!userService.checkPassword(loginData.getPassword(), user)) {
            loginResponse.setErr_no(101131849);
            loginResponse.setErr_msg("wrong password");
            return loginResponse;
        }

        // 密码正确生成token，将生成的token存入redis key值为user_id，value则为生成的token
        String token = Generator.generateToken(user.getUsername(), user.getUserId(),
                user.getPhone());
        try {
            Jedis jedis = JRedisPoolService.getInstance(InitConfig.REDIS_POOL);
            log.info("key={} token={}", InitConfig.LOGIN_TOKEN_PRE + user.getUserId(), token);
            jedis.setex(InitConfig.LOGIN_TOKEN_PRE + user.getUserId(), InitConfig.ONE_DAY_EXPIRE,
                    token);

        } catch (Exception e) {
            log.error(e.getMessage());
            loginResponse.setErr_no(101241317);
            loginResponse.setErr_msg("redis error");
            return loginResponse;
        }


        LoginResponse.LoginResData loginResData = new LoginResponse.LoginResData();
        loginResData.setToken(token);
        loginResponse.setData(loginResData);

        return loginResponse;
    }

    /**
     * 内置系统管理员分配用户账号
     *
     * @param request
     * @param assignUserReq
     *
     * @return
     */
    @RequestMapping(value = "/assignUser")
    public JResponse assignUser(HttpServletRequest request,
                                @RequestBody @Valid AssignUserReq assignUserReq) {
        AssignUserReq.AssignUserData assignUserData = assignUserReq.getData();
        log.info("assignUser username={} name={} phone={}", assignUserData.getUsername(),
                assignUserData.getName(), assignUserData.getPhone());
        JResponse jResponse = new JResponse();
        // 用户身份鉴别
        UserProfile userProfile = assignUserReq.getUser_profile();
        Integer role = userService.getRoleByUserId(userProfile.getUser_id());
        if (!role.equals(User.Role.SYSTEM_ADMINISTRATOR)) {
            // 非系统管理员返回错误
            jResponse.setErr_no(101090910);
            jResponse.setErr_msg("Wrong user identity, can't assign user");
            return jResponse;
        }
        // 该用户名是否已经被注册过
        if (userService.getUserByUsername(assignUserData.getUsername()) != null) {
            jResponse.setErr_no(101090918);
            jResponse.setErr_msg("the username has been registered");
            return jResponse;
        }
        // 该手机号是否被注册过
        if (userService.getUserByPhone(assignUserData.getPhone()) != null) {
            jResponse.setErr_no(101090920);
            jResponse.setErr_msg("the phone has been registered");
            return jResponse;
        }
        // 构建实体对象，进入业务逻辑
        User user = new User();
        user.setUsername(assignUserData.getUsername());
        user.setPassword(assignUserData.getPassword());
        user.setName(assignUserData.getName());
        user.setPhone(assignUserData.getPhone());
        user.setDepartment(assignUserData.getDepartment());
        user.setRole(assignUserData.getRole());
        boolean result = userService.create(user);

        if (!result) {
            jResponse.setErr_no(101110127);
            jResponse.setErr_msg("insert into db error");
        }
        return jResponse;
    }

    /**
     * 初始时，创建系统管理员
     *
     * @param request
     *
     * @return
     */
    @RequestMapping(value = "/createAdministrator")
    public JResponse createAdministrator(HttpServletRequest request) {
        log.info("createAdministrator");
        JResponse jResponse = new JResponse();

        // 构建实体对象，进入业务逻辑
        User user = new User();
        user.setUsername("administrator");
        user.setPassword(Encryption.encryptedData("123456", InitConfig.Const.publicKey));
        user.setName("冯凤娟");
        user.setPhone("17801020888");
        user.setDepartment("铁路总局");
        user.setRole(User.Role.SYSTEM_ADMINISTRATOR);
        userService.create(user);

        return jResponse;
    }

    /**
     * 根据用户角色拉取用户列表
     *
     * @param request
     * @param retriveUserReq
     *
     * @return
     */
    @RequestMapping(value = "/getUsersByRole")
    public RetriveUserResponse getUserByRole(HttpServletRequest request,
                                             @RequestBody @Valid RetriveUserReq retriveUserReq) {
        RetriveUserReq.RetriveUserData retriveUserData = retriveUserReq.getData();
        log.info("getUserByRole role={}", retriveUserData.getRole());
        RetriveUserResponse retriveUserResponse = new RetriveUserResponse();

        // 查找数据库，根据role查找，使user方法直接返回封装好的RetriveUserResData List
        List<RetriveUserResponse.RetriveUserResData> retriveUserResDataList =
                userService.getUsersByRole(retriveUserData.getRole());
        retriveUserResponse.setData(retriveUserResDataList);
        return retriveUserResponse;
    }
}
