package cn.jcj.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 杰哥  @version 1.00
 * Description 前后端交互统一消息返回类
 */
public class Msg {

    //自定义状态码
    private int code;

    //成功或失败 的提示信息
    private String msg;


    //用户要返回给浏览器的数据
    private Map<String, Object> content = new HashMap<String, Object>();

    //成功调用的方法---此处暂定 200为成功
    public static Msg success() {
        Msg msg = new Msg();
        msg.setCode(200);
        msg.setMsg("成功!");
        return msg;
    }

    //失败调用的方法---此处暂定 500为失败
    public static Msg fail() {
        Msg msg = new Msg();
        msg.setCode(500);
        msg.setMsg("失败!");
        return msg;
    }

    //通用方法
    public static Msg setMsgCodeAndPrint(Integer code, String stringMsg) {
        Msg msg = new Msg();
        msg.setCode(code);
        msg.setMsg(stringMsg);
        return msg;
    }


    //添加数据
    public Msg add(String key, Object value) {
        this.getContent().put(key, value);
        return this;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Map<String, Object> getContent() {
        return content;
    }

    public void setContent(Map<String, Object> content) {
        this.content = content;
    }


}
