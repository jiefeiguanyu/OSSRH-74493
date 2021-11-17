package cn.jcj.vo;

import cn.jcj.pojo.Region;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @since 2021/7/2 16:07   @author 纪村杰  @version 1.00
 * Description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParkingRecordVo implements Serializable {

    private static final long serialVersionUID = -4797412204651980219L;
    private Integer id;
    private String car_num;
    private String brand;
    private Region region;
    private Integer status;
    private String add_time;

}
