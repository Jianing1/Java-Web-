package com.xingying.travel.service;

import com.xingying.travel.dao.HotelDao;
import com.xingying.travel.pojo.Hotel;
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

@Service
@Transactional
public class HotelService {

    @Autowired
    private HotelDao hotelDao;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private HotelService hotelService;

    @Autowired
    private RedisTemplate redisTemplate;

    public List<Hotel> findAll() {
        return hotelDao.findAll();
    }


    public void add(Hotel hotel) {
        hotel.setId(idWorker.nextId() + "");
        //取到缓存中的文件url
        String fileurl = (String) redisTemplate.opsForValue().get("fileurl");

        hotel.setImg("https://travel-class2.oss-cn-hangzhou.aliyuncs.com/" + fileurl);
        hotelDao.save(hotel);
    }


    public Page<Hotel> findSearch(Map whereMap, int page, int size) {
        Specification<Hotel> specification = createSpecification(whereMap);
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        return hotelDao.findAll(specification, pageRequest);
    }

    public List<Hotel> findSearch(Map whereMap) {
        Specification<Hotel> specification = createSpecification(whereMap);
        return hotelDao.findAll(specification);
    }

    public Hotel findById(String id) {
        return hotelDao.findById(id).get();
    }


    public void update(Hotel hotel) {
        hotelDao.save(hotel);
    }


    public void deleteById(String id) {
        hotelDao.deleteById(id);
    }


    private Specification<Hotel> createSpecification(Map searchMap) {

        return new Specification<Hotel>() {

            @Override
            public Predicate toPredicate(Root<Hotel> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicateList = new ArrayList<Predicate>();
                // 
                if (searchMap.get("id") != null && !"".equals(searchMap.get("id"))) {
                    predicateList.add(cb.like(root.get("id").as(String.class), "%" + searchMap.get("id") + "%"));
                }
                // 图片
                if (searchMap.get("img") != null && !"".equals(searchMap.get("img"))) {
                    predicateList.add(cb.like(root.get("img").as(String.class), "%" + searchMap.get("img") + "%"));
                }
                // 酒店名称
                if (searchMap.get("name") != null && !"".equals(searchMap.get("name"))) {
                    predicateList.add(cb.like(root.get("name").as(String.class), "%" + searchMap.get("name") + "%"));
                }
                // 描述
                if (searchMap.get("miaoshu") != null && !"".equals(searchMap.get("miaoshu"))) {
                    predicateList.add(cb.like(root.get("miaoshu").as(String.class), "%" + searchMap.get("miaoshu") + "%"));
                }

                return cb.and(predicateList.toArray(new Predicate[predicateList.size()]));

            }
        };

    }

    public List<Hotel> findByCountryLike(String contry) {
        return hotelDao.findByAddrLike(contry);
    }

}
