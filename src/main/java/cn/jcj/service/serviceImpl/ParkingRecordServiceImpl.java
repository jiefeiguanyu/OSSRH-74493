package cn.jcj.service.serviceImpl;

import cn.jcj.dao.ParkingRecordDao;
import cn.jcj.pojo.ParkingRecord;
import cn.jcj.pojo.Region;
import cn.jcj.service.ParkingRecordService;
import cn.jcj.utils.RedisDataTool;
import cn.jcj.vo.ParkingRecordVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @since 2021/7/2 16:14   @author 纪村杰  @version 1.00
 * Description
 */
@Service
public class ParkingRecordServiceImpl implements ParkingRecordService {
    @Autowired
    ParkingRecordDao parkingRecordDao;

    @Override
    public List<ParkingRecordVo> sel(Integer id) {
        id=id==null?0:id;
        return RedisDataTool.queryRedisAutoKey(parkingRecordDao,"sel",ParkingRecordVo.class,id);
    }

    @Override
    public List<Region> selType() {
        return  RedisDataTool.queryRedisAutoKey(parkingRecordDao,"selType",Region.class);
    }

    @Override
    public void upd(Integer id) {
        RedisDataTool.deleteRedisAutoKey();
        parkingRecordDao.upd(id);
    }
    @Override
    public void add(ParkingRecord parkingRecord) {
        RedisDataTool.deleteRedisAutoKey();
        parkingRecordDao.add(parkingRecord);
    }
}
