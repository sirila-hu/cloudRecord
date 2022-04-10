package com.example.top_sirilahu.repository;

import com.example.top_sirilahu.entity.imgEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface imgRepository extends CrudRepository<imgEntity, Long>
{
    //保存图片信息
    @Transactional
    @Modifying
    @Query(value = "insert into tb_image(imgName, path, uploader) values(:#{#img.imgName}, :#{#img.path}, :#{#img.uploader})", nativeQuery = true)
    void saveImg(imgEntity img);

    //分页获取图片列表
    @Query(value = "select * from tb_image i where i.uploader = ?1 order by uploadDate desc limit ?2, ?3", nativeQuery = true)
    List<imgEntity> getImagesPagination(long uploader, int start, int offset);

    //获取用户的图片上传总数
    @Query(value = "select count(*) from tb_image i where i.uploader = ?1", nativeQuery = true)
    int countByUploader(long uploader);
}
