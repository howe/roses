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
package cn.stylefeng.roses.kernel.system.modular.user.controller;

import cn.stylefeng.roses.kernel.rule.enums.ResBizTypeEnum;
import cn.stylefeng.roses.kernel.rule.pojo.response.ResponseData;
import cn.stylefeng.roses.kernel.rule.pojo.response.SuccessResponseData;
import cn.stylefeng.roses.kernel.scanner.api.annotation.ApiResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.GetResource;
import cn.stylefeng.roses.kernel.security.api.DragCaptchaApi;
import cn.stylefeng.roses.kernel.security.api.ImageCaptchaApi;
import cn.stylefeng.roses.kernel.security.api.pojo.DragCaptchaImageDTO;
import cn.stylefeng.roses.kernel.security.api.pojo.ImageCaptcha;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static cn.stylefeng.roses.kernel.rule.constants.RuleConstants.BASE64_IMG_PREFIX;

/**
 * 图形验证码
 *
 * @author chenjinlong
 * @date 2021/1/15 15:11
 */
@RestController
@ApiResource(name = "用户登录图形验证码", resBizType = ResBizTypeEnum.SYSTEM)
public class KaptchaController {

    @Resource
    private ImageCaptchaApi captchaApi;

    @Resource
    private DragCaptchaApi dragCaptchaApi;

    /**
     * 获取图形验证码
     *
     * @author fengshuonan
     * @date 2021/7/5 12:00
     */
    @GetResource(name = "获取图形验证码", path = "/captcha", requiredPermission = false, requiredLogin = false)
    public ResponseData<ImageCaptcha> captcha() {
        return new SuccessResponseData<>(captchaApi.captcha());
    }

    /**
     * 获取拖拽验证码
     *
     * @author fengshuonan
     * @date 2021/7/5 12:00
     */
    @GetResource(name = "获取图形验证码", path = "/dragCaptcha", requiredPermission = false, requiredLogin = false)
    public ResponseData<DragCaptchaImageDTO> dragCaptcha() {
        DragCaptchaImageDTO captcha = dragCaptchaApi.createCaptcha();
        captcha.setSrcImage(BASE64_IMG_PREFIX + captcha.getSrcImage());
        captcha.setCutImage(BASE64_IMG_PREFIX + captcha.getCutImage());
        return new SuccessResponseData<>(captcha);
    }

}
