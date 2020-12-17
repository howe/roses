package cn.stylefeng.roses.kernel.log.modular.requestapi.aop;

import cn.stylefeng.roses.kernel.log.api.LogRecordApi;
import cn.stylefeng.roses.kernel.log.api.constants.LogConstants;
import cn.stylefeng.roses.kernel.log.api.expander.LogConfigExpander;
import cn.stylefeng.roses.kernel.log.api.factory.LogRecordFactory;
import cn.stylefeng.roses.kernel.log.api.factory.appender.AuthedLogAppender;
import cn.stylefeng.roses.kernel.log.api.factory.appender.HttpLogAppender;
import cn.stylefeng.roses.kernel.log.api.factory.appender.ParamsLogAppender;
import cn.stylefeng.roses.kernel.log.api.pojo.record.LogRecordDTO;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.Ordered;
import org.springframework.core.ParameterNameDiscoverer;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 每个请求接口记录日志的AOP
 * <p>
 * 将控制器controller包下的所有控制器类，执行的时候对url，参数，结果等进行记录
 *
 * @author fengshuonan
 * @date 2020/10/28 17:06
 */
@Aspect
@Slf4j
public class RequestApiLogRecordAop implements Ordered {

    /**
     * 日志记录的api
     */
    private final LogRecordApi logRecordApi;

    public RequestApiLogRecordAop(LogRecordApi logRecordApi) {
        this.logRecordApi = logRecordApi;
    }

    /**
     * 切所有controller包
     */
    @Pointcut("execution(* *..controller.*.*(..))")
    public void cutService() {

    }

    @Around("cutService()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Object result = point.proceed();

        try {
            Map<String, Object> args = getFieldsName(point);
            recordLog(args, result);
        } catch (Exception e) {
            log.error("日志记录没有记录成功！", e);
        }

        return result;
    }

    /**
     * 将请求方法记录日志的过程
     *
     * @param params AOP拦截方法的参数封装，key是参数名称，v是参数值
     * @param result AOP拦截方法的返回值
     * @author fengshuonan
     * @date 2020/10/28 17:38
     */
    private void recordLog(Map<String, Object> params, Object result) {

        // 创建日志对象
        LogRecordDTO logRecordDTO = LogRecordFactory.createLogRecord(LogConstants.LOG_DEFAULT_NAME, null);

        // 填充用户登录信息
        AuthedLogAppender.appendAuthedHttpLog(logRecordDTO);

        // 填充http接口请求信息
        HttpLogAppender.appendHttpLog(logRecordDTO);

        // 追加参数信息
        ParamsLogAppender.appendAuthedHttpLog(logRecordDTO, params, result);

        // 异步记录日志
        logRecordApi.recordLogAsync(logRecordDTO);
    }

    @Override
    public int getOrder() {
        return LogConfigExpander.getRequestApiLogAopSort();
    }

    /**
     * AOP获取参数名称和参数值
     *
     * @param joinPoint joinPoint对象
     * @return 返回K, V格式的参数，key是参数名称，v是参数值
     * @author majianguo
     * @date 2020/11/2 10:40
     */
    private Map<String, Object> getFieldsName(ProceedingJoinPoint joinPoint) {

        // 获取被拦截方法的所有参数
        Object[] args = joinPoint.getArgs();

        // 通过map封装参数和参数值，key参数名，value是参数值
        Map<String, Object> paramMap = new HashMap<>(args.length);
        try {
            String classType = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();

            // 处理基本类型
            Class<?>[] classes = new Class[args.length];
            for (int k = 0; k < args.length; k++) {
                classes[k] = args[k].getClass();
            }

            ParameterNameDiscoverer defaultParameterNameDiscoverer = new DefaultParameterNameDiscoverer();

            // 获取指定的方法，第二个参数可以不传，但是为了防止有重载的现象，还是需要传入参数的类型
            Method method = Class.forName(classType).getMethod(methodName, classes);

            // 参数名
            String[] parameterNames = defaultParameterNameDiscoverer.getParameterNames(method);

            // 为空直接返回
            if (null == parameterNames) {
                return new HashMap<>(1);
            }

            // 装载参数名称和参数值
            for (int i = 0; i < parameterNames.length; i++) {
                paramMap.put(parameterNames[i], args[i]);
            }

        } catch (Exception e) {

            // 打印日志
            if (log.isDebugEnabled()) {
                e.printStackTrace();
            }
            log.error(e.getMessage());

            // 有异常则不显示参数名称直接返回参数
            for (int i = 0; i < args.length; i++) {
                paramMap.put("args" + i, args[i]);
            }

        }

        return paramMap;
    }

}