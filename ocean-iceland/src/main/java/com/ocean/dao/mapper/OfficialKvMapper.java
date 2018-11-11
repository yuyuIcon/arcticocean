package com.ocean.dao.mapper;

import com.ocean.model.pojo.OfficialKv;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by niu on 18/1/22.
 */
@Repository
public interface OfficialKvMapper {

    List<OfficialKv> getAllOfficialKvs();

    List<OfficialKv> searchOfficialKvs(
            @Param("id") String id, @Param("title") String title, @Param("subtitle") String subtitle,
            @Param("status") String status, @Param("openType") String openType, @Param("menuId") Long menuId,
            @Param("creator") String creator, @Param("lastModifier") String lastModifier, @Param("beginDate") String beginDate,
            @Param("endDate") String endDate, @Param("pageIndex") Integer pageIndex, @Param("pageLimit") Integer pageLimit
    );

    int insertOfficialKv(OfficialKv officialKv);

    int deleteByPrimaryKey(String id);

    int updateByPrimaryKey(OfficialKv officialKv);

    OfficialKv getOfficialKvById(String id);
}
