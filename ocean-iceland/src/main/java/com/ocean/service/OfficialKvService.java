package com.ocean.service;

import com.ocean.model.pojo.OfficialKv;

import java.util.List;
import java.util.Map;

/**
 * Created by niu on 18/1/22.
 */
public interface OfficialKvService {

    List<OfficialKv> getAllOfficialKvs();

    Map searchOfficialKvs(OfficialKv officialKv, Integer pageIndex, Integer pageLimit);

    int insertOfficialKv(OfficialKv officialKv) throws Exception;

    int deleteOfficialKv(String id);

    int updateOfficialKv(OfficialKv officialKv);

    OfficialKv getOfficialKvById(String id);
}
