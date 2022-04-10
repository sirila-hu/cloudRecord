package com.example.top_sirilahu.service;

import com.example.top_sirilahu.VO.pageVO;
import com.example.top_sirilahu.VO.searchVO;
import com.example.top_sirilahu.VO.sectionVO;
import com.example.top_sirilahu.entity.pageEntity;
import com.example.top_sirilahu.entity.recordEntity;
import com.example.top_sirilahu.entity.sectionEntity;
import com.example.top_sirilahu.entity.unitEntity;
import com.example.top_sirilahu.repository.pageRepository;
import com.example.top_sirilahu.repository.recordRepository;
import com.example.top_sirilahu.repository.sectionRepository;
import com.example.top_sirilahu.util.fileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class searchService {
    private static String rootPath;

    recordRepository recordRepo;
    sectionRepository sectionRepo;
    pageRepository pageRepo;

    @Value("${file.upload.rootPath}")
    public void setRootPath(String rootPath) {
        searchService.rootPath = rootPath;
    }

    public searchService() {
    }

    @Autowired
    public searchService(recordRepository recordRepo, sectionRepository sectionRepo, pageRepository pageRepo) {
        this.recordRepo = recordRepo;
        this.sectionRepo = sectionRepo;
        this.pageRepo = pageRepo;
    }

    /**
     * - 分区搜索 -
     *去数据空中获取含有关键字的section信息
     * @Param UID 用户ID，用于限定查询本用户的记录内容
     * @Param field 关键字用户搜索用的关键字
     */
    public List sectionSearch(long UID, String field) {
        //准备容器
        List searchVOS = new ArrayList<>();
        List<sectionVO> sectionVOS = null;

        //定义sql生成器
        Specification<sectionEntity> specification = new Specification<sectionEntity>() {
            @Override
            public Predicate toPredicate(Root<sectionEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();

                //联接操作
                Join<sectionEntity, recordEntity> join = root.join("record");
                predicates.add(criteriaBuilder.equal(join.get("r_creator"), UID));

                predicates.add(criteriaBuilder.like(root.get("s_title").as(String.class), "%" + field + "%"));

                predicates.add(criteriaBuilder.like(join.get("r_name").as(String.class), "%" + field + "%"));

                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };

        //获取含有关键字的section信息并转化为VO
        sectionVOS = sectionVO.convertToVO(sectionRepo.findAll(specification));

        //对信息进行包装
        for (sectionVO section: sectionVOS){
            String to = String.format("/%s/%s", section.getS_record(), section.getS_record());
            String titlePath = String.format("%s", section.getS_title());
            searchVOS.add(new searchVO<>(section, to, titlePath));
        }
        return searchVOS;
    }

    /**
     * - 页搜索 -
     * 包含两个步骤，页信息搜索和页问价搜索
     *
     * @Param UID 用户ID，用于限定查询本用户的记录内容
     * @Param field 关键字用户搜索用的关键字
     */
    public List pageSearch(long UID, String field) throws Exception {
        List pages = new ArrayList();

        //执行页信息搜索
        pages.addAll(pageInfoSearch(UID, field));

        //执行页文件搜索
        pages.addAll(pageFileSearch(UID, field));

        return pages;
    }

    /**
     * - 页信息搜索 -
     * 去数据空中查询含有关键字的页
     *
     * @param UID   用户ID
     * @param field 查询关键字
     * @return
     */
    private List pageInfoSearch(long UID, String field) {
        //准备容器
        List searchVOS = new ArrayList();
        List<pageEntity> pageEntities = null;

        //定义sql生成对象
        Specification<pageEntity> specification = new Specification<pageEntity>() {
            @Override
            public Predicate toPredicate(Root<pageEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

                //向上联接操作
                Join<pageEntity, recordEntity> join_record = root.join("section");
                Predicate predicate_equal = criteriaBuilder.equal(join_record.get("record").get("r_creator"), UID);

                Predicate predicate_title = criteriaBuilder.like(root.get("p_title").as(String.class), "%" + field + "%");
                Predicate predicate_field = criteriaBuilder.like(root.get("p_pre").as(String.class), "%" + field + "%");

                Predicate predicate_or = criteriaBuilder.or(predicate_title, predicate_field);

                return criteriaBuilder.and(predicate_equal, predicate_or);
            }
        };

        //获得含有关键字的page信息
        pageEntities = pageRepo.findAll(specification);

        //包装实体
        for (pageEntity page : pageEntities) {
            //创建业务对象
            pageVO pageVO = new pageVO(page);

            //拼接前端跳转路径
            sectionEntity section = page.getSection();
            String to = String.format("/%s/%s/%s", section.getRecord().getR_id(), page.getSection().getS_id(), page.getP_id());
            String titlePath = String.format("%s/%s", section.getRecord().getR_name(), page.getSection().getS_title());

            searchVOS.add(new searchVO<>(pageVO, to, titlePath));
        }

        return searchVOS;
    }

    /**
     * - 页文件搜索 -
     * 查询数据库中不含关键字且有页问价的页条目，搜索页文件内容
     *
     * @param UID   用户ID
     * @param field 关键字
     * @return
     * @throws IOException
     */
    private List pageFileSearch(long UID, String field) throws IOException {
        //准备容器
        List searchVOS = new ArrayList();
        List<pageEntity> pageEntities = null;

        //定义sql生成对象
        Specification<pageEntity> specification = new Specification<pageEntity>() {
            @Override
            public Predicate toPredicate(Root<pageEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                //向上联接操作
                Join<pageEntity, recordEntity> join_record = root.join("section");
                predicates.add(criteriaBuilder.equal(join_record.get("record").get("r_creator"), UID));
                //p_path不为空则表示该页含有页文件
                predicates.add(criteriaBuilder.isNotNull(root.get("p_path")));
                predicates.add(criteriaBuilder.notLike(root.get("p_title").as(String.class), "%" + field + "%"));
                predicates.add(criteriaBuilder.notLike(root.get("p_pre").as(String.class), "%" + field + "%"));

                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };

        //获得不含关键字且含有页文件的信息
        pageEntities = pageRepo.findAll(specification);

        //搜寻页文件
        String fullPath = "";
        for (pageEntity page : pageEntities) {
            fullPath = rootPath + page.getP_path();
            if (fileUtil.isPageHaveField(fullPath, field)) {
                //创建业务对象
                pageVO pageVO = new pageVO(page);

                //拼接前端跳转路径
                sectionEntity section = page.getSection();
                String to = String.format("/%s/%s/%s", section.getRecord().getR_id(), page.getSection().getS_id(), page.getP_id());
                String titlePath = String.format("%s/%s", section.getRecord().getR_name(), page.getSection().getS_title());

                searchVOS.add(new searchVO<>(pageVO, to, titlePath));
            }
        }

        return searchVOS;
    }

}
