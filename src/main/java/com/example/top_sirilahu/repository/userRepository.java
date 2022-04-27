package com.example.top_sirilahu.repository;

import com.example.top_sirilahu.entity.userEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

public interface userRepository extends CrudRepository<userEntity, Long>
{
    @Query(value = "select * from tb_user where username = ?1", nativeQuery = true)
    userEntity findByUsername(String username);

    @Query(value = "select password from tb_user where UID = ?1", nativeQuery = true)
    String findPasswordByUID(long UID);
    //修改页
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update tb_user set password=?1 where UID=?2", nativeQuery = true)
    int changePassword(String password, Long UID);
}
