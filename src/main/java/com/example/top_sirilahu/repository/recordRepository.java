package com.example.top_sirilahu.repository;

import com.example.top_sirilahu.entity.recordEntity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface recordRepository extends CrudRepository<recordEntity, String>, JpaSpecificationExecutor<recordEntity>
{
    //保存记录本
    @Transactional
    @Modifying
    @Query(value = "insert into tb_record(r_id, r_name, r_creator) values(:#{#record.r_id}, :#{#record.r_name}, :#{#record.r_creator})", nativeQuery = true)
    void saveRecord(recordEntity record);

    //获取用户的记录本数量
    @Query(value = "select count(*) from tb_record r where r.r_creator = ?1", nativeQuery = true)
    int countByRCreator(long rCreator);

    //获取用户的记录本
    @Query(value = "select * from tb_record r where r.r_creator = ?1 order by r_date desc limit ?2, ?3", nativeQuery = true)
    List<recordEntity> getRecordsPagination(long rCreator, int start, int offset);

    //修改记录本
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update tb_record set r_name=?1 where r_id=?2", nativeQuery = true)
    public void update(String rName, String rId);
}
