package com.xingying.travel.dao;

import com.xingying.travel.pojo.Scenic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * 数据访问接口
 *
 * @author Administrator
 */
public interface ScenicDao extends JpaRepository<Scenic, String>, JpaSpecificationExecutor<Scenic> {
    List<Scenic> findByContryLike(String name);
}
