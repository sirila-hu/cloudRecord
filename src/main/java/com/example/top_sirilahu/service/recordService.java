package com.example.top_sirilahu.service;

import com.alibaba.fastjson.JSON;
import com.example.top_sirilahu.Exception.RunException;
import com.example.top_sirilahu.VO.pageSortVO;
import com.example.top_sirilahu.VO.recordVO;
import com.example.top_sirilahu.entity.pageEntity;
import com.example.top_sirilahu.entity.recordEntity;
import com.example.top_sirilahu.entity.userEntity;
import com.example.top_sirilahu.jsonBody.statusJSON;
import com.example.top_sirilahu.jsonBody.paginationJSON;
import com.example.top_sirilahu.repository.pageRepository;
import com.example.top_sirilahu.repository.recordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import javax.persistence.NoResultException;
import javax.persistence.criteria.*;
import java.util.List;
import java.util.Optional;

@Service
@ConfigurationProperties(prefix = "record")
public class recordService {
    private static int pageSize = 8;

    private recordRepository recordRepo;
    private pageRepository pageRepo;


    //页文件管理逻辑
    private pageFileService pageFileSe;

    public recordService() {
    }

    @Autowired
    public recordService(recordRepository recordRepo, pageRepository pageRepo, pageFileService pageFileSe) {
        this.recordRepo = recordRepo;
        this.pageRepo = pageRepo;
        this.pageFileSe = pageFileSe;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    //获取用户的记录本数
    public int getCount(long UID) {
        return recordRepo.countByRCreator(UID);
    }

    /**
     * 以id为关键子来获取单个record
     *
     * @param r_id record ID
     * @return record转换后的业务对象
     */
    public recordVO getRecord(String r_id) {
        Optional<recordEntity> op = recordRepo.findById(r_id);
        if (!op.isPresent()) {
            throw new NoResultException("此笔记本不存在");
        }
        recordEntity record = recordRepo.findById(r_id).get();

        return new recordVO(record);
    }

    //获取用户的记录本(分页显示)
    public String getRecords(userEntity user, pageSortVO pageSort) throws RunException {
        Page<recordEntity> records = null;
        List recordVOS = null;
        try {
            //获取用户的分页记录本
            records = customSortQuery(user.getUID(), pageSort, pageSize);

            //业务对象转换
            recordVOS = recordVO.excludeSections(records.getContent());
        } catch (NoResultException ne) {
            //无影响异常
            return JSON.toJSONString(new statusJSON(0, ne.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            String msg = "查询发生错误";
            if (!StringUtils.isEmptyOrWhitespace(e.getMessage())) {
                msg = e.getMessage();
            }
            throw new RunException(msg, JSON.toJSONString(new statusJSON(1, msg)));
        }

        //返回json
        return JSON.toJSONString(new paginationJSON(0, records.getTotalPages(), pageSort.getPage(), recordVOS));
    }

    /**
     * - 用户自定义分页查询 -
     * @param UID
     * @param pageSort
     * @param pageSize
     * @return
     */
    private Page<recordEntity> customSortQuery(long UID, pageSortVO pageSort, int pageSize) {
        //生成分页对象
        PageRequest pageRequest = PageRequest.of(pageSort.getPage() - 1, pageSize);
        //定义sql生成器
        Specification specification = new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Path r_creator = root.get("r_creator");
                Path orderField = root.get(pageSort.getOrderField());
                //排序对象
                Order order = null;

                //筛选本用户的笔记本
                Predicate predicate = criteriaBuilder.equal(r_creator.as(long.class), UID);

                //判断选择何种排序方式
                if (pageSort.isASC()) {
                    order = criteriaBuilder.asc(orderField);
                } else {
                    order = criteriaBuilder.desc(orderField);
                }
                criteriaQuery.orderBy(order);

                return criteriaQuery.where(predicate).getRestriction();
            }
        };

        return recordRepo.findAll(specification, pageRequest);
    }

    //添加记录本
    @Transactional
    public void addRecord(recordEntity record, userEntity user) {
        //生成记录本编号
        int count = getCount(user.getUID());
        String r_id = String.format("%d-1", user.getUID());

        //当该用户已有笔记本时的r_id生成
        if (count > 0) {
            recordEntity lastRecord = recordRepo.getRecordsPagination(user.getUID(), 0, 1).get(0);
            String[] code = lastRecord.getR_id().split("-");
            r_id = String.format("%s-%d", code[0], Integer.parseInt(code[1]) + 1);
        }

        record.setR_id(r_id);

        record.setR_creator(user.getUID());

        //保存修改
        recordRepo.saveRecord(record);
    }

    //修改记录本
    public void editRecord(String r_name, String r_id) {
        int updateRows = recordRepo.update(r_name, r_id);
        if (updateRows <= 0) {
            throw new EmptyResultDataAccessException(updateRows);
        }
    }

    //删除记录本
    @Transactional(rollbackFor = Exception.class)
    public void delRecord(String r_id) throws Exception {
        List<pageEntity> pages = pageRepo.getPagesbyR_ID(r_id);
        //细粒度删除关联的页
        for (pageEntity page : pages) {
            pageFileSe.delPage(page);
        }
        //级联删除rocord
        recordRepo.deleteById(r_id);
    }
}
