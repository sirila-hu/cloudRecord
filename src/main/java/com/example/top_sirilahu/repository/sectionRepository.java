package com.example.top_sirilahu.repository;

import com.example.top_sirilahu.entity.recordEntity;
import com.example.top_sirilahu.entity.sectionEntity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface sectionRepository extends CrudRepository<sectionEntity, String>, JpaSpecificationExecutor<sectionEntity>
{
    //获取笔记本关联的分区数量
    @Query(value = "select count(*) from tb_section s where s.s_record = ?1", nativeQuery = true)
    int countByS_record(String s_record);

    //获取最后一次添加分区
    @Query(value = "select * from tb_section s where s.s_record = ?1 order by s_date desc limit 0, 1", nativeQuery = true)
    public sectionEntity getLastSection(String s_record);

    //以笔记本为关键字查询分区
    @Query(value = "select * from tb_section where s_record = ?1", nativeQuery = true)
    List<sectionEntity> getSections(String r_id);

    //保存分区实体
    @Transactional
    @Modifying
    @Query(value = "insert into tb_section(s_id, s_title, s_record) values(:#{#section.s_id}, :#{#section.s_title}, :#{#section.s_record})", nativeQuery = true)
    void saveSection(sectionEntity section);

    //修改页
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update tb_section set s_title=?1 where s_id=?2", nativeQuery = true)
    int editSection(String s_title, String s_id);
}
