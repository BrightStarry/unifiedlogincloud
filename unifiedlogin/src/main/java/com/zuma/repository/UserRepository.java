package com.zuma.repository;

import com.zuma.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * author:ZhengXing
 * datetime:2017/10/16 0016 17:30
 * 用户数据操作
 */
public interface UserRepository extends JpaRepository<User,Long>{

    /**
     * 根据用户名密码查询用户
     */
    User findTopByUsernameAndPassword(String username, String password);

    /**
     * 根据用户名查询用户
     */
    User findTopByUsername(String username);




}
