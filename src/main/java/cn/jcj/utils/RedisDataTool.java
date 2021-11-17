package cn.jcj.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

/**
 * @since 2021/7/30  @author  关于皆非  @version 4.10
 * Description 用来实现redis与数据库同步数据的功能。将其定义为一个配置类，从根源解除键保存时的乱码
 */
@Configuration
public class RedisDataTool {
    //项目名称
    static String projectKey;
    //前缀
    final static String PREFIXKEY = "JFRDT";

    //内置静态RedisTemplate
    static RedisTemplate redisTemplate;

    //设置redis的序列化方式，避免出现乱码
    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<String, String>();
        redisTemplate.setConnectionFactory(factory);
        //key序列化方式;（不然会出现乱码;）,但是如果方法上有boolean等非String类型的话，会出现问题，所以一定要toSting啊
        RedisSerializer<String> redisSerializer = new StringRedisSerializer();//Long类型不可以会出现异常信息;
        redisTemplate.setKeySerializer(redisSerializer);
        redisTemplate.setHashKeySerializer(redisSerializer);
        return redisTemplate;
    }

    /**
     * <br>使用正则表达式来删除对应key的redis数据，返回响应情况
     *
     * @param pattern 正则表达式，用来查询你需要删除的key
     * @return String 返回一条处理结果信息
     * @author 关于皆非
     */
    public static String deleteRedisData(String pattern) {
        Set<String> keys = null;
        try {
            //注意，这里的正则表达式的*通配符似乎不能匹配null这个字母，因此在selall方法中将null替换为0
            keys = redisTemplate.keys(pattern);
            //给它一个集合，让它自己删去
            redisTemplate.delete(keys);
        } catch (Exception exception) {
            return exception.fillInStackTrace().toString();
        }
        return "OK Succeed DeleteKeys:" + keys;
    }

    /**
     * <br>调用这个方法来实现将自动生成的key删除
     *
     * @return String 返回一条处理结果信息
     * @author 关于皆非
     */
    public static String deleteRedisAutoKey() {
        return deleteRedisData(PREFIXKEY + projectKey + "*");
    }

    /**
     * @param key           没错！这只是一个key,用来教redis怎么储存到redis中ヾ(•ω•`)o
     * @param daoImpl       daoImpl？啊，我没有实现类啊？怎么办？不用担心！我们只需要dao的接口就可以了！
     * @param daoMethodName 有了dao接口，但你得想办法让我们知道您想要执行的是什么方法，然后剩下的就交给我们了
     * @param parameters    嘿。别忘了有时候你的方法需要传参，所以这个参数就出现了！因此它可要也可不要！
     * @param objectClass   一个类的字节码，用来表示返回的list集合是什么类型的集合
     * @param <D>           dao层的接口或实现类的class
     * @param <T>           要输出的List类型
     * @param <C>           为dao层传值的类型
     * @return List
     * @author : 关于皆非
     * @since: Description: 判断并查询然后将数据存入redis的方法
     */
    public static <D, T, C> List<T> queryRedis(String key, D daoImpl, String daoMethodName, C parameters, Class<T> objectClass) {
        return queryOverloadAutoKey(daoImpl, daoMethodName, objectClass, key, parameters,true);
    }

    public static <D, T> List<T> queryRedis(String key, D daoImpl, String daoMethodName, Class<T> objectClass) {
        return queryOverloadAutoKey(daoImpl, daoMethodName, objectClass, key, null,false);
    }

    /**
     * @param daoImpl       在这里，您只需要传入一个dao接口
     * @param daoMethodName 有了dao接口，但您得想办法让我的工具知道您想要执行的是什么方法，然后剩下的就交给我的工具了！
     * @param objectClass   指定返回一个什么类型的List集合，就比如：Object.class
     * @param parameters    为dao层传入的值类
     * @param <D>           dao层的接口或实现类的class
     * @param <T>           要输出的List类型
     * @param <C>           为dao层传值的类型
     * @return List
     * @author : 关于皆非
     * @since: Description: queryRedisAutoKey系列无需自己手动设置key了，当然它一样可以传一个参
     */

    public static <D, T, C> List<T> queryRedisAutoKey(D daoImpl, String daoMethodName, Class<T> objectClass, C parameters) {
        return queryOverloadAutoKey(daoImpl, daoMethodName, objectClass, null, parameters,true);
    }

    public static <D, T> List<T> queryRedisAutoKey(D daoImpl, String daoMethodName, Class<T> objectClass) {
        return queryOverloadAutoKey(daoImpl, daoMethodName, objectClass, null, null,false);
    }

    private static <D, T, C> List<T> queryOverloadAutoKey(D daoImpl, String daoMethodName, Class<T> objectClass, String key, C parameters,Boolean tag) {
        //设置更复杂的key，更好的避免数据重复
        if (key == null || key.equals("") || key.isEmpty()) {
            key = PREFIXKEY + projectKey + daoImpl.getClass().getName() + AutoKey(parameters);
        }
        ValueOperations valueOperations = redisTemplate.opsForValue();
        List<T> redisList = (List<T>) valueOperations.get(key);
        try {
            if (redisList == null || redisList.isEmpty()) {
                Method declaredMethod = null;
                if (parameters == null&&!tag) {
                    declaredMethod = daoImpl.getClass().getDeclaredMethod(daoMethodName);
                    declaredMethod.setAccessible(true);
                    redisList = (List<T>) declaredMethod.invoke(daoImpl);
                } else {
                    try {
                        declaredMethod = daoImpl.getClass().getDeclaredMethod(daoMethodName,parameters.getClass());
                    }catch (NullPointerException e) {
                        System.out.println("警告：传入的值不允许为null，请传入一个带有准确类型的任意值！"+e);
                        return null;
                    }
                    declaredMethod.setAccessible(true);
                    redisList = (List<T>) declaredMethod.invoke(daoImpl, parameters);
                }
                valueOperations.set(key, redisList);
                return redisList;
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return redisList;
    }

    //生成key
    public static <C> String AutoKey(C parameters) {
        if (parameters == null) {
            return "parameters";
        }
        StringBuilder key = new StringBuilder(RedisDataTool.class.getName() + parameters.getClass().getName());
        Class<?> pClass = parameters.getClass();
        Field[] df = pClass.getDeclaredFields();
        for (Field d : df) {
            d.setAccessible(true);
            try {
                Object o = d.get(parameters);
                key.append(d.getName()).append("Is").append(o);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return key.toString();
    }

    //获取并设置静态属性的项目KEY(由项目名与项目端口号组成)
    @Value("${spring.application.name}${server.port}")
    public void setprojectKey(String projectKey) {
        RedisDataTool.projectKey = projectKey;
    }

    //注入静态RedisTemplate
    @Resource(name = "redisTemplate")
    public void setRestTemplate(RedisTemplate redisTemplate) {
        RedisDataTool.redisTemplate = redisTemplate;
    }

}

/*
 *              更新日志 有BUG加Q2476535821
 * 4.10 2021/11/4
 * 内置redisTemplate
 *
 * 4.01 2021/11/2
 * 修复两个潜在bug
 *
 *
 * 4.00 2021/10/12
 * 自动化！您不需要额外的设置key了！
 *
 * 加入queryRedisAutoKey系列的方法来实现自动化
 * 加入deleteRedisAutoKey用来删除自动化redis储存的值
 *
 *
 * 3.00
 * 一个划时代式的更新，解决了许多头疼的问题，它能够在更加复杂的环境下正确运行。如mybatis的pagehelp分页、模糊查询等
 *
 * 我将这个工具类定义为一个配置类，并设置序列化方式，从根源解除redis储存东西时KEY出现乱码导致的一连串问题
 *
 * 提升了与数据库一致性
 *
 * 2.10
 * 对工具类的单参和无参方法进行优化，直接使用list接受
 *
 *
 * 2.00
 * 增加了对redis进行查询的单参和无参方法
 *
 *
 *1.00
 * 增加了一个对redis数据删除的操作
 *
 *
 * */
