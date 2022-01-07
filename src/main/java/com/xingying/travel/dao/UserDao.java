package com.xingying.travel.dao;

import com.xingying.travel.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 数据访问接口
 *
 * @author Administrator
 */
public interface UserDao extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {

    /**
     * 根据手机号查询用户
     *
     * @param mobile
     * @return
     */
    User findByMobile(String mobile);

    /**
     * 根据姓名查询用户
     *
     * @param name
     * @return
     */
    User findByName(String name);

    /**
     * @param email
     * @return
     */
    User findByEmail(String email);
}
