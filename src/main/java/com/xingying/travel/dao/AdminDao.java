package com.xingying.travel.dao;

import com.xingying.travel.pojo.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 数据访问接口
 *
 * @author Administrator
 */
public interface AdminDao extends JpaRepository<Admin, String>, JpaSpecificationExecutor<Admin> {

    /**
     * 查名字
     *
     * @param name
     * @return
     */
    Admin findByName(String name);
}
