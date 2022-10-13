/*
 * Copyright [2020-2030] [https://www.stylefeng.cn]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Guns采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改Guns源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://gitee.com/stylefeng/guns
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/stylefeng/guns
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */
package cn.stylefeng.roses.kernel.system.modular.role.service.impl;

import cn.hutool.core.collection.ListUtil;
import cn.stylefeng.roses.kernel.db.api.context.DbOperatorContext;
import cn.stylefeng.roses.kernel.rule.constants.RuleConstants;
import cn.stylefeng.roses.kernel.rule.enums.DbTypeEnum;
import cn.stylefeng.roses.kernel.system.modular.role.entity.SysRoleMenuButton;
import cn.stylefeng.roses.kernel.system.modular.role.mapper.SysRoleMenuButtonMapper;
import cn.stylefeng.roses.kernel.system.modular.role.service.SysRoleMenuButtonService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 角色按钮关联 服务实现类
 *
 * @author fengshuonan
 * @date 2021/01/09 11:48
 */
@Service
public class SysRoleMenuButtonServiceImpl extends ServiceImpl<SysRoleMenuButtonMapper, SysRoleMenuButton> implements SysRoleMenuButtonService {

    @Override
    public void batchSaveRoleMenuButton(List<SysRoleMenuButton> roleMenuButtons) {
        DbTypeEnum currentDbType = DbOperatorContext.me().getCurrentDbType();
        if (DbTypeEnum.MYSQL.equals(currentDbType)) {
            List<List<SysRoleMenuButton>> split = ListUtil.split(roleMenuButtons, RuleConstants.DEFAULT_BATCH_INSERT_SIZE);
            for (List<SysRoleMenuButton> roleMenuButtonList : split) {
                this.getBaseMapper().insertBatchSomeColumn(roleMenuButtonList);
            }
        } else {
            this.saveBatch(roleMenuButtons);
        }
    }

}
