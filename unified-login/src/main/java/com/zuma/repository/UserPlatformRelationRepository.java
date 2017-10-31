package com.zuma.repository;

import com.zuma.domain.User;
import com.zuma.domain.UserPlatformRelation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * author:ZhengXing
 * datetime:2017/10/16 0016 17:32
 * 用户-平台-关系数据操作
 */
public interface UserPlatformRelationRepository extends JpaRepository<UserPlatformRelation,Long> {

    /**
     * 分页查询某用户授权的所有平台
     */
    Page<UserPlatformRelation> findAllByUserId(Long userId, Pageable pageable);

    /**
     * 查询某用户授权的所有平台
     */
    List<UserPlatformRelation> findAllByUserId(Long userId);

    /**
     * 根据用户id和平台id查询到对应的关系记录
     */
    UserPlatformRelation findTopByUserIdAndPlatformId(Long userId, Long platform);

    /**
     * 分页查询某平台的所有授权用户
     */
    Page<UserPlatformRelation> findAllByPlatformId(Long platformId, Pageable pageable);

    /**
     * 根据userId删除记录
     */
    void deleteAllByUserId(Long userId);

    /**
     * 根据平台id修改平台名
     */
    @Modifying
    @Query("update UserPlatformRelation set platformName = :updatedName where platformId = :platformId")
    void updatePlatformNameByPlatformId(@Param("platformId") Long platformId, @Param("updatedName") String updatedName);



}
