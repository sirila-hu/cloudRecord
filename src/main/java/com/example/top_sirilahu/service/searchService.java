package com.example.top_sirilahu.service;

import com.example.top_sirilahu.entity.pageEntity;
import com.example.top_sirilahu.entity.recordEntity;
import com.example.top_sirilahu.entity.sectionEntity;
import com.example.top_sirilahu.entity.unitEntity;
import com.example.top_sirilahu.repository.pageRepository;
import com.example.top_sirilahu.repository.recordRepository;
import com.example.top_sirilahu.repository.sectionRepository;
import com.example.top_sirilahu.repository.unitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

@Service
public class searchService
{
    recordRepository recordRepo;
    sectionRepository  sectionRepo;
    pageRepository pageRepo;
    unitRepository unitRepo;

    public searchService() {
    }

    @Autowired
    public searchService(recordRepository recordRepo, sectionRepository sectionRepo, pageRepository pageRepo, unitRepository unitRepo) {
        this.recordRepo = recordRepo;
        this.sectionRepo = sectionRepo;
        this.pageRepo = pageRepo;
        this.unitRepo = unitRepo;
    }

    /*
    *- 记录搜索 -
    * @Param UID 用户ID，用于限定查询本用户的记录内容
    * @Param filed 关键字用户搜索用的关键字
    * */
    public List<recordEntity> recordSearch(long UID, String filed)
    {
        //创建Specification对象用于动态查询
        Specification<recordEntity> specification = new Specification<recordEntity>() {
            @Override
            public Predicate toPredicate(Root<recordEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Path r_creator = root.get("r_creator");
                Predicate pre_r_creator = criteriaBuilder.equal(r_creator, UID);
                Path r_name = root.get("r_name");
                Predicate pre_r_name = criteriaBuilder.like(r_name.as(String.class), "%" + filed + "%");
                return criteriaBuilder.and(pre_r_name, pre_r_creator);
            }
        };
        return recordRepo.findAll(specification);
    }

    /*
    * - 分区搜索 -
    *@Param UID 用户ID，用于限定查询本用户的记录内容
    *@Param filed 关键字用户搜索用的关键字
    * */
    public List<sectionEntity> sectionSearch(long UID, String filed)
    {
        Specification<sectionEntity> specification = new Specification<sectionEntity>() {
            @Override
            public Predicate toPredicate(Root<sectionEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();

                //联接操作
                Join<sectionEntity, recordEntity> join = root.join("record", JoinType.LEFT);
                predicates.add(criteriaBuilder.equal(join.get("r_creator"), UID));

                predicates.add(criteriaBuilder.like(root.get("s_title").as(String.class), "%" + filed + "%"));

                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        return sectionRepo.findAll(specification);
    }

    /*
    * - 页搜索 -
    * 联接分区表、记录表以及单元表，查找出本用户记录范围内，页标题以及页内容(页包含的单元)包含搜索关键字的页
    *@Param UID 用户ID，用于限定查询本用户的记录内容
    *@Param filed 关键字用户搜索用的关键字
    * */
    public List<pageEntity> pageSearch(long UID, String filed)
    {
        Specification<pageEntity> specification = new Specification<pageEntity>() {
            @Override
            public Predicate toPredicate(Root<pageEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                //向上联接操作
                Join<pageEntity, recordEntity> join_record = root.join("section", JoinType.LEFT);
                predicates.add(criteriaBuilder.equal(join_record.get("record").get("r_creator"), UID));
                predicates.add(criteriaBuilder.like(root.get("p_title").as(String.class), filed));
                //向下联接操作
                Join<pageEntity, unitEntity> join_unit = root.join("units", JoinType.INNER);
                Predicate predicate_title = criteriaBuilder.like(join_unit.get("u_title").as(String.class), filed);
                Predicate predicate_content = criteriaBuilder.like(join_unit.get("u_content").as(String.class), filed);
                join_unit.on(criteriaBuilder.or(predicate_content, predicate_title));

                Predicate predicate_like = criteriaBuilder.or(predicates.toArray(new Predicate[predicates.size()]));
                return criteriaBuilder.and(predicate_like);
            }
        };

        return pageRepo.findAll(specification);
    }

    /*
     * - 单元搜索 -
     * 想传入的页对象填充其下满足搜索关键词的单元
     *@Param UID 用户ID，用于限定查询本用户的记录内容
     *@Param filed 关键字用户搜索用的关键字
     * */
    public void unitSearch(List<pageEntity> pages, String filed)
    {
        for(pageEntity page:pages)
        {
            Specification<unitEntity> specification = new Specification<unitEntity>() {
                @Override
                public Predicate toPredicate(Root<unitEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder)
                {
                    Predicate predicate_u_page = criteriaBuilder.equal(root.get("u_page"), page.getP_id());
                    Predicate predicate_u_title = criteriaBuilder.like(root.get("u_title").as(String.class), filed);
                    Predicate predicate_u_content = criteriaBuilder.like(root.get("u_content").as(String.class), filed);

                    Predicate predicate_like = criteriaBuilder.or(predicate_u_content, predicate_u_title);
                    return criteriaBuilder.and(predicate_u_page, predicate_like);
                }
            };
            List<unitEntity> units = unitRepo.findAll(specification);

            if (!units.isEmpty())
            {
                for (unitEntity unit:units)
                {
                    String u_content = unit.getU_content();
                    if (u_content.indexOf(filed) != -1)
                    {
                        int start = 0;
                        int end = 30;
                        if (u_content.indexOf(filed) != 0)
                        {
                            start = u_content.indexOf(filed);
                        }

                        if (u_content.length() < 30)
                        {
                            end = u_content.length();
                        }
                        unit.setU_content(u_content.substring(start, end));
                    }
                }
                page.setUnits(units);
            }
        }
    }
}
