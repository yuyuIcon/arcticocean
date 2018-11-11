package com.ocean.dao.mapper;

import com.ocean.model.pojo.AiHuiShouLog;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Author: Qigaowei
 * Date: 2018/7/11 10:00
 * Description: 爱回收传递数据日志接口类
 */
@Repository
public interface AiHuiShouLogMapper {

    public List<AiHuiShouLog> getLogsByOrderNo(String orderNo);

    public int insertAHSLog(AiHuiShouLog aiHuiShouLog);
}
