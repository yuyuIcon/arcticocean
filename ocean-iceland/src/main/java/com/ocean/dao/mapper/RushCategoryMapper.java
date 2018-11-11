package com.ocean.dao.mapper;

import com.ocean.model.pojo.RushCategory;
import org.springframework.stereotype.Repository;

/**
 * Author: Qigaowei
 * Date: 2018/10/16 11:42
 * Description:
 */
@Repository
public interface RushCategoryMapper {

    RushCategory getCategoryById(long id);

    void updateRushCategory(RushCategory rushCategory);

    void insertRushCategory(RushCategory rushCategory);

}
