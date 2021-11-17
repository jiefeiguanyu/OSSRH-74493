package cn.jcj.service;

import cn.jcj.pojo.ParkingRecord;
import cn.jcj.pojo.Region;
import cn.jcj.vo.ParkingRecordVo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @since 2021/7/2 16:13   @author 纪村杰  @version 1.00
 * Description
 */

public interface ParkingRecordService {


    List<ParkingRecordVo> sel(Integer id);

    List<Region> selType();

    void upd(Integer id);

    void add(ParkingRecord parkingRecord);
}
