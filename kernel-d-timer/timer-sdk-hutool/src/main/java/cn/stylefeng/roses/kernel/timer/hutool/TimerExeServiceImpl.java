package cn.stylefeng.roses.kernel.timer.hutool;

import cn.hutool.core.util.StrUtil;
import cn.hutool.cron.CronUtil;
import cn.stylefeng.roses.kernel.timer.api.exception.TimerException;
import cn.stylefeng.roses.kernel.timer.api.exception.enums.TimerExceptionEnum;
import lombok.extern.slf4j.Slf4j;

/**
 * HuToolTimerExeServiceImpl的改造
 * 当引入spring cloud后，需要对原有事件进行改造，使其只执行一次，防止出现错误。
 *
 * @author xielin
 * @date 2022/11/11 16:04
 */
@Slf4j
public class TimerExeServiceImpl extends HutoolTimerExeServiceImpl {

    @Override
    public void start() {

        // 设置秒级别的启用
        CronUtil.setMatchSecond(true);

        // 启动定时器执行器
        if (!CronUtil.getScheduler().isStarted()) {
            CronUtil.start();
            log.info("scheduler{} is not started", CronUtil.getScheduler().toString());
        } else {
            log.info("scheduler{} has already started", CronUtil.getScheduler().toString());
        }
    }

    @Override
    public void startTimer(String taskId, String cron, String className, String params) {

        // 判断任务id是否为空
        if (StrUtil.isBlank(taskId)) {
            throw new TimerException(TimerExceptionEnum.PARAM_HAS_NULL, "taskId");
        }

        // 如果之前有已经在执行的任务，则停止
        if (CronUtil.getScheduler().getTask(taskId) != null) {
            log.info("task ID:{} is existed，then will replace before one", taskId);
            CronUtil.getScheduler().deschedule(taskId);
        }

        // 执行以前的启动定时任务的逻辑
        super.startTimer(taskId, cron, className, params);
    }

}
