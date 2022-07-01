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
package cn.stylefeng.roses.kernel.system.api.pojo.resource;

import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import cn.stylefeng.roses.kernel.rule.tree.factory.base.AbstractTreeNode;
import lombok.Data;

import java.util.List;

/**
 * 用于渲染api资源树（layui插件）
 *
 * @author fengshuonan
 * @date 2021/1/14 21:51
 */
@Data
public class LayuiApiResourceTreeNode implements AbstractTreeNode<LayuiApiResourceTreeNode> {

    /**
     * 资源的上级编码
     */
    @ChineseDescription("资源的上级编码")
    private String parentId;

    /**
     * 节点名称
     */
    @ChineseDescription("节点名称")
    private String title;

    /**
     * 资源的编码
     */
    @ChineseDescription("资源的编码")
    private String id;

    /**
     * 是否展开状态 不展开-false 展开-true
     */
    @ChineseDescription("是否展开状态 不展开-false 展开-true")
    private Boolean spread = false;

    /**
     * 是否是资源标识
     * <p>
     * true-是资源标识
     * false-虚拟节点，不是一个具体资源
     */
    @ChineseDescription("是否是资源标识")
    private Boolean resourceFlag;

    /**
     * 节点URL
     */
    @ChineseDescription("节点URL")
    private String url;

    /**
     * 子节点的集合
     */
    @ChineseDescription("子节点的集合")
    private List<LayuiApiResourceTreeNode> children;

    @Override
    public String getNodeId() {
        return this.id;
    }

    @Override
    public String getNodeParentId() {
        return this.parentId;
    }

    @Override
    public void setChildrenNodes(List<LayuiApiResourceTreeNode> childrenNodes) {
        this.children = childrenNodes;
    }

}
