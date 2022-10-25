package cn.stylefeng.roses.kernel.system.api.pojo.role.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Set;

/**
 * 角色拥有的菜单、按钮、资源信息的集合
 *
 * @author fengshuonan
 * @date 2022/10/25 15:59
 */
@Data
@AllArgsConstructor
public class RoleAuthorizeInfo {

    /**
     * 角色拥有的菜单集合
     */
    private List<Long> menuIdList;

    /**
     * 角色拥有的按钮id集合
     */
    private List<Long> buttonIdList;

    /**
     * 角色拥有的资源编码集合
     */
    private Set<String> resourceCodeList;

}
