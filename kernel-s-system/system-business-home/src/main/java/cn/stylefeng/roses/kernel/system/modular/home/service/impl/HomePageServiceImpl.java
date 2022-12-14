package cn.stylefeng.roses.kernel.system.modular.home.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.stylefeng.roses.kernel.auth.api.context.LoginContext;
import cn.stylefeng.roses.kernel.auth.api.pojo.login.LoginUser;
import cn.stylefeng.roses.kernel.cache.api.CacheOperatorApi;
import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.log.api.LogManagerApi;
import cn.stylefeng.roses.kernel.log.api.pojo.manage.LogManagerRequest;
import cn.stylefeng.roses.kernel.log.api.pojo.record.LogRecordDTO;
import cn.stylefeng.roses.kernel.rule.enums.YesOrNotEnum;
import cn.stylefeng.roses.kernel.system.api.HomePageServiceApi;
import cn.stylefeng.roses.kernel.system.api.PositionServiceApi;
import cn.stylefeng.roses.kernel.system.api.UserServiceApi;
import cn.stylefeng.roses.kernel.system.api.pojo.home.HomeCompanyInfo;
import cn.stylefeng.roses.kernel.system.api.pojo.user.OnlineUserDTO;
import cn.stylefeng.roses.kernel.system.api.pojo.user.request.OnlineUserRequest;
import cn.stylefeng.roses.kernel.system.api.pojo.user.request.SysUserRequest;
import cn.stylefeng.roses.kernel.system.modular.home.entity.SysStatisticsCount;
import cn.stylefeng.roses.kernel.system.modular.home.entity.SysStatisticsUrl;
import cn.stylefeng.roses.kernel.system.modular.home.mapper.SysStatisticsUrlMapper;
import cn.stylefeng.roses.kernel.system.modular.home.pojo.OnlineUserStat;
import cn.stylefeng.roses.kernel.system.modular.home.service.HomePageService;
import cn.stylefeng.roses.kernel.system.modular.home.service.SysStatisticsCountService;
import cn.stylefeng.roses.kernel.system.modular.home.service.SysStatisticsUrlService;
import cn.stylefeng.roses.kernel.system.modular.menu.entity.SysMenu;
import cn.stylefeng.roses.kernel.system.modular.menu.mapper.SysMenuMapper;
import cn.stylefeng.roses.kernel.system.modular.organization.entity.HrOrganization;
import cn.stylefeng.roses.kernel.system.modular.organization.service.HrOrganizationService;
import cn.stylefeng.roses.kernel.system.modular.user.entity.SysUserOrg;
import cn.stylefeng.roses.kernel.system.modular.user.service.SysUserOrgService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static cn.stylefeng.roses.kernel.rule.constants.SymbolConstant.LEFT_SQUARE_BRACKETS;
import static cn.stylefeng.roses.kernel.rule.constants.SymbolConstant.RIGHT_SQUARE_BRACKETS;

/**
 * ?????????????????????
 *
 * @author fengshuonan
 * @date 2022/2/11 20:41
 */
@Service
public class HomePageServiceImpl implements HomePageService, HomePageServiceApi {

    @Resource
    private LogManagerApi logManagerApi;

    @Resource
    private UserServiceApi userServiceApi;

    @Resource
    private HrOrganizationService hrOrganizationService;

    @Resource
    private PositionServiceApi positionServiceApi;

    @Resource
    private SysUserOrgService sysUserOrgService;

    @Resource(name = "requestCountCacheApi")
    private CacheOperatorApi<Map<Long, Integer>> requestCountCacheApi;

    @Resource
    private SysStatisticsCountService sysStatisticsCountService;

    @Resource
    private SysStatisticsUrlService sysStatisticsUrlService;

    @Resource
    private SysStatisticsUrlMapper sysStatisticsUrlMapper;

    @Resource
    private SysMenuMapper sysMenuMapper;

    @Override
    public List<LogRecordDTO> getRecentLogs() {

        // ????????????????????????
        LogManagerRequest logManagerRequest = new LogManagerRequest();
        logManagerRequest.setUserId(LoginContext.me().getLoginUser().getUserId());

        PageResult<LogRecordDTO> page = logManagerApi.findPage(logManagerRequest);
        return page.getRows();
    }

    @Override
    public OnlineUserStat getOnlineUserList(OnlineUserRequest onlineUserRequest) {

        OnlineUserStat onlineUserStat = new OnlineUserStat();

        // ?????????????????????
        List<OnlineUserDTO> onlineUserDTOS = userServiceApi.onlineUserList(onlineUserRequest);

        // ??????????????????????????????
        HashSet<String> onlineUserList = new HashSet<>();
        for (OnlineUserDTO onlineUserDTO : onlineUserDTOS) {
            if (ObjectUtil.isNotEmpty(onlineUserDTO.getRealName())) {
                onlineUserList.add(onlineUserDTO.getRealName());
            }
        }
        onlineUserStat.setTotalNum(onlineUserList.size());

        // ?????????20??????
        Set<String> newSet = onlineUserList.stream().limit(20).collect(Collectors.toSet());
        onlineUserStat.setTotalUserNames(newSet);

        return onlineUserStat;
    }

    @Override
    public HomeCompanyInfo getHomeCompanyInfo() {
        HomeCompanyInfo homeCompanyInfo = new HomeCompanyInfo();

        // ???????????????????????????
        long count = hrOrganizationService.count();
        homeCompanyInfo.setOrganizationNum(Convert.toInt(count));

        // ???????????????????????????
        SysUserRequest sysUserRequest = new SysUserRequest();
        List<Long> allUserIdList = userServiceApi.queryAllUserIdList(sysUserRequest);
        homeCompanyInfo.setEnterprisePersonNum(allUserIdList.size());

        // ????????????????????????
        int positionNum = positionServiceApi.positionNum();
        homeCompanyInfo.setPositionNum(positionNum);

        // ????????????????????????????????????id
        LoginUser loginUser = LoginContext.me().getLoginUser();
        Long organizationId = loginUser.getOrganizationId();

        // ??????????????????????????????????????????(???????????????)
        LambdaQueryWrapper<HrOrganization> wrapper = Wrappers.lambdaQuery(HrOrganization.class)
                .like(HrOrganization::getOrgPids, LEFT_SQUARE_BRACKETS + organizationId + RIGHT_SQUARE_BRACKETS)
                .or()
                .eq(HrOrganization::getOrgId, organizationId)
                .select(HrOrganization::getOrgId);
        List<HrOrganization> organizations = hrOrganizationService.list(wrapper);
        homeCompanyInfo.setCurrentDeptNum(organizations.size());

        // ???????????????????????????????????????????????????
        List<Long> orgIds = organizations.stream().map(HrOrganization::getOrgId).collect(Collectors.toList());
        Long currentOrgPersonNum = sysUserOrgService.count(Wrappers.lambdaQuery(SysUserOrg.class).in(SysUserOrg::getOrgId, orgIds));
        homeCompanyInfo.setCurrentCompanyPersonNum(Convert.toInt(currentOrgPersonNum));

        return homeCompanyInfo;
    }

    @Override
    public List<SysMenu> getCommonFunctions() {

        // ??????????????????????????????????????????????????????
        LoginUser loginUser = LoginContext.me().getLoginUser();
        List<SysStatisticsCount> statList = sysStatisticsCountService.list(
                Wrappers.lambdaQuery(SysStatisticsCount.class).eq(SysStatisticsCount::getUserId, loginUser.getUserId()).orderByDesc(SysStatisticsCount::getStatCount));
        List<Long> statUrlIdList = statList.stream().map(SysStatisticsCount::getStatUrlId).collect(Collectors.toList());

        // ??????????????????????????????
        LambdaQueryWrapper<SysStatisticsUrl> wrapper = Wrappers.lambdaQuery(SysStatisticsUrl.class)
                .eq(SysStatisticsUrl::getAlwaysShow, YesOrNotEnum.Y)
                .select(SysStatisticsUrl::getStatUrlId);
        List<SysStatisticsUrl> alwaysShowList = sysStatisticsUrlService.list(wrapper);

        // ???????????????????????????????????????????????????
        if (ObjectUtil.isNotEmpty(alwaysShowList)) {
            statUrlIdList.addAll(0, alwaysShowList.stream().map(SysStatisticsUrl::getStatUrlId).collect(Collectors.toList()));
        }

        // ??????statUrlId??????8???????????????8???
        if (statUrlIdList.size() > 8) {
            statUrlIdList = statUrlIdList.subList(0, 8);
        }

        // ????????????id??????
        List<Long> usualMenuIds = sysStatisticsUrlMapper.getMenuIdsByStatUrlIdList(statUrlIdList);

        // ??????????????????????????????????????????
        List<SysMenu> list = sysMenuMapper.getMenuStatInfoByMenuIds(usualMenuIds);

        // ?????????icon????????????????????????
        for (SysMenu sysMenu : list) {
            if (sysMenu.getAntdvIcon() != null) {
                String replace = sysMenu.getAntdvIcon().replace("-", "_");
                sysMenu.setAntdvIcon(StrUtil.upperFirst(StrUtil.toCamelCase(replace)));
            }
        }

        return list;
    }

    @Override
    public void saveStatisticsCacheToDb() {
        // key?????????id???value???key???statUrlId????????????value?????????
        Map<String, Map<Long, Integer>> userRequestStats = requestCountCacheApi.getAllKeyValues();
        Set<String> userIds = userRequestStats.keySet();

        // ???????????????????????????????????????
        if (ObjectUtil.isEmpty(userIds)) {
            return;
        }

        // ?????????????????????????????????
        List<SysStatisticsCount> cacheCountList = new ArrayList<>();
        for (String userId : userIds) {
            // ????????????????????????????????????key???statUrlId???value?????????
            Map<Long, Integer> userCounts = userRequestStats.get(userId);
            for (Map.Entry<Long, Integer> userCountItem : userCounts.entrySet()) {
                Long statUrlId = userCountItem.getKey();
                Integer count = userCountItem.getValue();
                SysStatisticsCount sysStatisticsCount = new SysStatisticsCount();
                sysStatisticsCount.setUserId(Long.valueOf(userId));
                sysStatisticsCount.setStatUrlId(statUrlId);
                sysStatisticsCount.setStatCount(count);
                cacheCountList.add(sysStatisticsCount);
            }
        }

        // ??????????????????????????????????????????
        List<Long> userIdsLong = userIds.stream().map(Long::valueOf).collect(Collectors.toList());
        List<SysStatisticsCount> sysStatisticsCounts = sysStatisticsCountService.list(
                Wrappers.lambdaQuery(SysStatisticsCount.class).in(SysStatisticsCount::getUserId, userIdsLong));

        for (SysStatisticsCount cacheItem : cacheCountList) {
            boolean haveRecord = false;
            for (SysStatisticsCount dbItem : sysStatisticsCounts) {
                // ?????????????????????????????????????????????????????????
                if (dbItem.getStatUrlId().equals(cacheItem.getStatUrlId()) && dbItem.getUserId().equals(cacheItem.getUserId())) {
                    haveRecord = true;
                    cacheItem.setStatCountId(dbItem.getStatCountId());
                }
            }
            // ????????????????????????????????????????????????????????????id
            if (!haveRecord) {
                cacheItem.setStatCountId(IdWorker.getId());
            }
        }

        // ????????????????????????????????????????????????
        sysStatisticsCountService.saveOrUpdateBatch(cacheCountList);
    }
}
