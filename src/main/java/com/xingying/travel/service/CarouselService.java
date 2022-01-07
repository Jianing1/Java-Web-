package com.xingying.travel.service;

import com.xingying.travel.dao.CarouselDao;
import com.xingying.travel.pojo.Carousel;
import com.xingying.travel.util.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
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
public class CarouselService {

    @Autowired
    private CarouselDao carouselDao;

    @Autowired
    private IdWorker idWorker;


    public List<Carousel> findAll() {
        return carouselDao.findAll();
    }

    public Page<Carousel> findSearch(Map whereMap, int page, int size) {
        Specification<Carousel> specification = createSpecification(whereMap);
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        return carouselDao.findAll(specification, pageRequest);
    }


    public List<Carousel> findSearch(Map whereMap) {
        Specification<Carousel> specification = createSpecification(whereMap);
        return carouselDao.findAll(specification);
    }

    public Carousel findById(String id) {
        return carouselDao.findById(id).get();
    }


    public void add(Carousel carousel) {
        carousel.setId(idWorker.nextId() + "");
        carouselDao.save(carousel);
    }

    public void update(Carousel carousel) {
        carouselDao.save(carousel);
    }

    public void deleteById(String id) {
        carouselDao.deleteById(id);
    }


    private Specification<Carousel> createSpecification(Map searchMap) {

        return new Specification<Carousel>() {

            @Override
            public Predicate toPredicate(Root<Carousel> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicateList = new ArrayList<Predicate>();
                // 
                if (searchMap.get("id") != null && !"".equals(searchMap.get("id"))) {
                    predicateList.add(cb.like(root.get("id").as(String.class), "%" + searchMap.get("id") + "%"));
                }
                // 标题
                if (searchMap.get("title") != null && !"".equals(searchMap.get("title"))) {
                    predicateList.add(cb.like(root.get("title").as(String.class), "%" + searchMap.get("title") + "%"));
                }
                // 内容
                if (searchMap.get("comment") != null && !"".equals(searchMap.get("comment"))) {
                    predicateList.add(cb.like(root.get("comment").as(String.class), "%" + searchMap.get("comment") + "%"));
                }
                // 时间
                if (searchMap.get("scenictime") != null && !"".equals(searchMap.get("scenictime"))) {
                    predicateList.add(cb.like(root.get("scenictime").as(String.class), "%" + searchMap.get("scenictime") + "%"));
                }

                return cb.and(predicateList.toArray(new Predicate[predicateList.size()]));

            }
        };

    }

}
