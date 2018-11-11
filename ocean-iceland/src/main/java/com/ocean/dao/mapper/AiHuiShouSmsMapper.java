package com.ocean.dao.mapper;

import com.ocean.model.pojo.AiHuiShouSms;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Author: Qigaowei
 * Date: 2018/7/13 10:01
 * Description:
 */
@Repository
public interface AiHuiShouSmsMapper {

    public List<AiHuiShouSms> getSmsByOrderNoOrVoucher(String parameter);

    public int insertAHSSms(AiHuiShouSms aiHuiShouSms);
}
