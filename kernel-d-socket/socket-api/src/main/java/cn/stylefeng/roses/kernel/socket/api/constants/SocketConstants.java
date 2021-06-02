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
package cn.stylefeng.roses.kernel.socket.api.constants;

/**
 * socket 模块常量
 *
 * @author majianguo
 * @date 2021/6/1 上午11:21
 */
public interface SocketConstants {

    /**
     * socket模块的名称
     */
    String SOCKET_MODULE_NAME = "kernel-d-socket";

    /**
     * 异常枚举的步进值
     */
    String SOCKET_EXCEPTION_STEP_CODE = "30";

    /**
     * Socket监听地址
     */
    String SOCKET_HOST = "socket_host";

    /**
     * Socket监听端口
     */
    String SOCKET_PORT = "socket_port";

    /**
     * Socket服务器内存池最大可分配空间大小
     */
    String SOCKET_SERVER_CHUNK_SIZE = "socket_server_chunk_size";

}
