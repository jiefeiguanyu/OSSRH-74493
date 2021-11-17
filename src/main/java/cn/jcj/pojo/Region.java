package cn.jcj.pojo;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Region implements Serializable {

  private static final long serialVersionUID = -1523846783138875380L;
  private Integer id;
  private String name;

}
