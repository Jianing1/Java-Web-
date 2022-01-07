package com.xingying.travel.service;

import com.xingying.travel.dao.ScenicDao;
import com.xingying.travel.pojo.Scenic;
import com.xingying.travel.util.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
@Transactional
public class ScenicService {

    @Autowired
    private ScenicDao scenicDao;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private RedisTemplate redisTemplate;


    public List<Scenic> findAll() {
        return scenicDao.findAll();
    }


    public Page<Scenic> findSearch(Map whereMap, int page, int size) {
        Specification<Scenic> specification = createSpecification(whereMap);
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        return scenicDao.findAll(specification, pageRequest);
    }

    public List<Scenic> findSearch(Map whereMap) {
        Specification<Scenic> specification = createSpecification(whereMap);
        return scenicDao.findAll(specification);
    }

    public Optional<Scenic> findById(String id) {
        return scenicDao.findById(id);
    }

    public void add(Scenic scenic) {
        scenic.setId(idWorker.nextId() + "");
        //取到缓存中的文件url
        String fileurl = (String) redisTemplate.opsForValue().get("fileurl");

        scenic.setImg("https://travel-class2.oss-cn-hangzhou.aliyuncs.com" + fileurl);
        scenicDao.save(scenic);
    }

    public void update(Scenic scenic) {
        scenicDao.save(scenic);
    }


    public void deleteById(String id) {
        scenicDao.deleteById(id);
    }

    private Specification<Scenic> createSpecification(Map searchMap) {

        return new Specification<Scenic>() {

            @Override
            public Predicate toPredicate(Root<Scenic> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicateList = new ArrayList<Predicate>();
                // id
                if (searchMap.get("id") != null && !"".equals(searchMap.get("id"))) {
                    predicateList.add(cb.like(root.get("id").as(String.class), "%" + searchMap.get("id") + "%"));
                }
                // 景点图片
                if (searchMap.get("img") != null && !"".equals(searchMap.get("img"))) {
                    predicateList.add(cb.like(root.get("img").as(String.class), "%" + searchMap.get("img") + "%"));
                }
                // 景点名称
                if (searchMap.get("name") != null && !"".equals(searchMap.get("name"))) {
                    predicateList.add(cb.like(root.get("name").as(String.class), "%" + searchMap.get("name") + "%"));
                }

                return cb.and(predicateList.toArray(new Predicate[predicateList.size()]));

            }
        };

    }

    public List<Scenic> findByCountryLike(String contry) {
        return scenicDao.findByContryLike(contry);
    }

}
