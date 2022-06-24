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
package cn.stylefeng.roses.kernel.system.modular.menu.factory;


import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.system.api.pojo.menu.MenuAndButtonTreeResponse;

import java.util.List;

/**
 * 填充是否必选状态
 *
 * @author fengshuonan
 * @date 2022/6/24 18:35
 */
public class IndeterminateFactory {

    /**
     * 填充菜单的半开状态
     *
     * @author fengshuonan
     * @date 2022/6/24 18:36
     */
    public static void fillIndeterminate(List<MenuAndButtonTreeResponse> menuAndButtonTreeResponses) {
        for (MenuAndButtonTreeResponse menuAndButtonTreeRespons : menuAndButtonTreeResponses) {
            List<MenuAndButtonTreeResponse> children = menuAndButtonTreeRespons.getChildren();
            if (ObjectUtil.isEmpty(children)) {
                continue;
            }

            // 获取选中状态子节点数量
            long selectedCount = children.stream().filter(MenuAndButtonTreeResponse::getChecked).count();

            // 获取子节点数量
            int childrenSize = children.size();

            // 统计选中的数量
            if (selectedCount == childrenSize) {
                menuAndButtonTreeRespons.setChecked(true);
                menuAndButtonTreeRespons.setIndeterminate(false);
            } else if (selectedCount == 0) {
                menuAndButtonTreeRespons.setChecked(false);
                menuAndButtonTreeRespons.setIndeterminate(false);
            } else {
                menuAndButtonTreeRespons.setChecked(false);
                menuAndButtonTreeRespons.setIndeterminate(true);
            }

            // 继续遍历子节点并填充
            IndeterminateFactory.fillIndeterminate(children);
        }
    }

}
