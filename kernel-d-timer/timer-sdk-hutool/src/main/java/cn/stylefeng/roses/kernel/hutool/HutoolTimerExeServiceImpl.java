package cn.stylefeng.roses.kernel.hutool;

import cn.hutool.core.util.StrUtil;
import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import cn.hutool.extra.spring.SpringUtil;
import cn.stylefeng.roses.kernel.timer.api.TimerAction;
import cn.stylefeng.roses.kernel.timer.api.TimerExeService;
import cn.stylefeng.roses.kernel.timer.api.exception.TimerException;
import cn.stylefeng.roses.kernel.timer.api.exception.enums.TimerExceptionEnum;
import lombok.extern.slf4j.Slf4j;

/**
 * hutool方式的定时任务执行
 *
 * @author fengshuonan
 * @date 2020/10/27 14:05
 */
@Slf4j
public class HutoolTimerExeServiceImpl implements TimerExeService {

    @Override
    public void startTimer(String taskId, String cron, String className) {

        // 判断任务id是否为空
        if (StrUtil.isBlank(taskId)) {
            String userTip = StrUtil.format(TimerExceptionEnum.PARAM_HAS_NULL.getUserTip(), "taskId");
            throw new TimerException(TimerExceptionEnum.PARAM_HAS_NULL, userTip);
        }

        // 判断任务cron表达式是否为空
        if (StrUtil.isBlank(cron)) {
            String userTip = StrUtil.format(TimerExceptionEnum.PARAM_HAS_NULL.getUserTip(), "cron");
            throw new TimerException(TimerExceptionEnum.PARAM_HAS_NULL, userTip);
        }

        // 判断类名称是否为空
        if (StrUtil.isBlank(className)) {
            String userTip = StrUtil.format(TimerExceptionEnum.PARAM_HAS_NULL.getUserTip(), "className");
            throw new TimerException(TimerExceptionEnum.PARAM_HAS_NULL, userTip);
        }

        // 预加载类看是否存在此定时任务类
        try {
            Class.forName(className);
        } catch (ClassNotFoundException e) {
            String userTip = StrUtil.format(TimerExceptionEnum.CLASS_NOT_FOUND.getUserTip(), className);
            throw new TimerException(TimerExceptionEnum.CLASS_NOT_FOUND, userTip);
        }

        // 定义hutool的任务
        Task task = () -> {
            try {
                TimerAction timerAction = (TimerAction) SpringUtil.getBean(Class.forName(className));
                timerAction.action();
            } catch (ClassNotFoundException e) {
                log.error(">>> 任务执行异常：{}", e.getMessage());
            }
        };

        // 开始执行任务
        CronUtil.schedule(taskId, cron, task);
    }

    @Override
    public void stopTimer(String taskId) {
        CronUtil.remove(taskId);
    }

}