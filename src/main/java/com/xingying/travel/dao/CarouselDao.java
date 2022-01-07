package com.xingying.travel.dao;

import com.xingying.travel.pojo.Carousel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 数据访问接口
 *
 * @author Administrator
 */
public interface CarouselDao extends JpaRepository<Carousel, String>, JpaSpecificationExecutor<Carousel> {

}
