package cn.jcj.dao;

import cn.jcj.pojo.ParkingRecord;
import cn.jcj.pojo.Region;
import cn.jcj.vo.ParkingRecordVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @since 2021/7/2 16:10   @author 纪村杰  @version 1.00
 * Description
 */
@Mapper
public interface ParkingRecordDao {

   List<ParkingRecordVo> sel(Integer region);

   List<Region> selType();

   void upd(Integer id);

   void add(ParkingRecord parkingRecord);

}
