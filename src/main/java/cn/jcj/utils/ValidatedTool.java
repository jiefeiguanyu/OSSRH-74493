package cn.jcj.utils;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @since 2021/7/1 19:44   @author 纪村杰  @version 1.00
 * Description 用来处理对象检查格式的工具类
 */
public class ValidatedTool {

    /**<br>
     * 检查传过来的BindingResult对象，如果判定为异常，那么将会返回错误信息(一个HashMap)
     * 否则返回一个null
     *
     * @param br 需要一个BindingResult对象
     *
     * @return HashMap
     *
     * */
    public static HashMap validatedPattern(BindingResult br) {
        HashMap<String, String> map = new HashMap<String, String>();
        if (br.hasErrors()) {

            List<FieldError> fieldErrors = br.getFieldErrors();

            for (FieldError f : fieldErrors) {

                String fgf = f.getField();

                String fdm = f.getDefaultMessage();

                map.put(fgf, fdm);
            }
            return map;
        }


        return null;
    }


}
