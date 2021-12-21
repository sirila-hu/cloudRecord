package com.example.top_sirilahu.repository;

import com.example.top_sirilahu.entity.pageEntity;
import com.example.top_sirilahu.entity.sectionEntity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface pageRepository extends CrudRepository<pageEntity, String>, JpaSpecificationExecutor<pageEntity>
{
    //获取分区关联的页数量
    @Query(value = "select count(*) from tb_page  where p_section = ?1", nativeQuery = true)
    int countByP_section(String p_secton);

    //获取最后一次添加的页
    @Query(value = "select * from tb_page  where p_section = ?1 order by p_date desc limit 0, 1", nativeQuery = true)
    pageEntity getLastpage(String p_section);

    //以分区编号为关键字查询页
    @Query(value = "select * from tb_page where p_section = ?1", nativeQuery = true)
    List<pageEntity> getPages(String s_id);

    //保存页实体
    @Transactional
    @Modifying
    @Query(value = "insert into tb_page(p_id, p_title, p_section) values(:#{#page.p_id}, :#{#page.p_title}, :#{#page.p_section})", nativeQuery = true)
    void savePage(pageEntity page);

    //修改页
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update tb_page set p_title=?1 where p_id=?2", nativeQuery = true)
    int editPage(String p_title, String p_id);
}
