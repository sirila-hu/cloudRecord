package com.example.top_sirilahu.repository;

import com.example.top_sirilahu.entity.unitEntity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface unitRepository extends CrudRepository<unitEntity, String>, JpaSpecificationExecutor<unitEntity>
{
    //获取页关联的单元数量
    @Query(value = "select count(*) from tb_unit where u_page = ?1", nativeQuery = true)
    int countByU_page(String u_page);

    //获取最后一次添加的单元
    @Query(value = "select * from tb_unit  where u_page = ?1 order by u_date desc limit 0, 1", nativeQuery = true)
    unitEntity getLastUnit(String u_page);

    //以页编号为关键字查询单元
    @Query(value = "select * from tb_unit where u_page = ?1", nativeQuery = true)
    List<unitEntity> getUnits(String p_id);

    //保存页实体
    @Transactional
    @Modifying
    @Query(value = "insert into tb_unit(u_id, u_title, u_content, u_img, u_page) values(:#{#unit.u_id}, :#{#unit.u_title}, :#{#unit.u_content}, :#{#unit.u_img}, :#{#unit.u_page})", nativeQuery = true)
    void saveUnit(unitEntity unit);

    //修改页
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update tb_unit set u_content=:#{#unit.u_content}, u_title=:#{#unit.u_title} where u_id=:#{#unit.u_id}", nativeQuery = true)
    int editUnit(unitEntity unit);

    //图片上传修改
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update tb_unit set u_img=?1 where u_id=?2", nativeQuery = true)
    int editUnit(String u_img, String u_id);
}
