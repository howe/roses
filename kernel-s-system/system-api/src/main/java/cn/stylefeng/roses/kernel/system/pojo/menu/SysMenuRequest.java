package cn.stylefeng.roses.kernel.system.pojo.menu;

import cn.stylefeng.roses.kernel.rule.pojo.request.BaseRequest;
import cn.stylefeng.roses.kernel.validator.validators.flag.FlagValue;
import cn.stylefeng.roses.kernel.validator.validators.unique.TableUniqueValue;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 系统菜单参数
 *
 * @author fengshuonan
 * @date 2020/3/26 20:42
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysMenuRequest extends BaseRequest {

    /**
     * 主键
     */
    @NotNull(message = "id不能为空，请检查id参数", groups = {edit.class, delete.class, detail.class})
    private Long id;

    /**
     * 父id
     */
    @NotNull(message = "pid不能为空，请检查pid参数", groups = {add.class, edit.class})
    private Long pid;

    /**
     * 名称
     */
    @NotBlank(message = "名称不能为空，请检查name参数", groups = {add.class, edit.class})
    @TableUniqueValue(
            message = "名称存在重复，请检查name参数",
            groups = {add.class, edit.class},
            tableName = "sys_menu",
            columnName = "name")
    private String name;

    /**
     * 编码
     */
    @NotBlank(message = "编码不能为空，请检查code参数", groups = {add.class, edit.class})
    @TableUniqueValue(
            message = "编码存在重复，请检查code参数",
            groups = {add.class, edit.class},
            tableName = "sys_menu",
            columnName = "code")
    private String code;

    /**
     * 菜单类型（字典 0目录 1菜单 2按钮）
     */
    @NotNull(message = "菜单类型不能为空，请检查type参数", groups = {add.class, edit.class})
    @Min(value = 0, message = "菜单类型格式错误，请检查type参数", groups = {add.class, edit.class})
    @Max(value = 2, message = "菜单类型格式错误，请检查type参数", groups = {add.class, edit.class})
    private Integer type;

    /**
     * 图标
     */
    private String icon;

    /**
     * 路由地址
     */
    private String router;

    /**
     * 组件地址
     */
    private String component;

    /**
     * 权限标识
     */
    private String permission;

    /**
     * 应用分类（应用编码）
     */
    @NotBlank(message = "应用分类不能为空，请检查appCode参数", groups = {add.class, edit.class, getAppMenus.class})
    private String appCode;

    /**
     * 打开方式（字典 0无 1组件 2内链 3外链）
     */
    @NotNull(message = "打开方式不能为空，请检查openType参数", groups = {add.class, edit.class})
    @Min(value = 0, message = "打开方式格式错误，请检查openType参数", groups = {add.class, edit.class})
    @Max(value = 3, message = "打开方式格式错误，请检查openType参数", groups = {add.class, edit.class})
    private Integer openType;

    /**
     * 是否可见（Y-是，N-否）
     */
    @NotBlank(message = "是否可见不能为空，请检查visible参数", groups = {add.class, edit.class})
    @FlagValue(message = "是否可见格式错误，正确格式应该Y或者N，请检查visible参数", groups = {add.class, edit.class})
    private String visible;

    /**
     * 内链地址
     */
    private String link;

    /**
     * 重定向地址
     */
    private String redirect;

    /**
     * 权重（字典 1系统权重 2业务权重）
     */
    @NotNull(message = "权重不能为空，请检查weight参数", groups = {add.class, edit.class})
    @Min(value = 0, message = "权重格式错误，请检查weight参数", groups = {add.class, edit.class})
    @Max(value = 2, message = "权重格式错误，请检查weight参数", groups = {add.class, edit.class})
    private Integer weight;

    /**
     * 排序
     */
    @NotNull(message = "排序不能为空，请检查sort参数", groups = {add.class, edit.class})
    private Integer sort;

    /**
     * 备注
     */
    private String remark;

    /**
     * 获取某个应用的左侧菜单树
     */
    public @interface getAppMenus {
    }

}