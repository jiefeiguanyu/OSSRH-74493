package cn.jcj.pojo;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class ParkingRecord implements Serializable {

  private static final long serialVersionUID = 6960588180024655619L;
  private Integer id;
@Pattern(regexp = "^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}[A-Z0-9]{4}[A-Z0-9挂学警港澳]{1}$"
        ,message = "请填入正确的车牌号！")
  private String car_num;
  private String brand;
  private Integer region_id;
  private Integer status;
  private String add_time;

}
