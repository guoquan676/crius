package com.pbkj.crius.api.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pbkj.crius.common.base.BaseResponse;
import com.pbkj.crius.common.utils.CursorModel;
import com.pbkj.crius.enhance.redis.enums.BannerEnumCacheKey;
import com.pbkj.crius.entity.param.UserCursorParam;
import com.pbkj.crius.entity.param.UserQueryParam;
import com.pbkj.crius.entity.po.SystemUser;
import com.pbkj.crius.entity.vo.UserVo;
import com.pbkj.crius.service.ISystemUserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author GZQ
 * @since 2020-07-23
 */
@RestController
@RequestMapping("/user")
public class SystemUserController {

    @Resource
    private ISystemUserService userService;

    private String password;

    @GetMapping
    public UserVo selectById(Long userId) {
        SystemUser byUniqueId = userService.getByUniqueId(userId);
//        String s = BannerEnumCacheKey.BANNER_MODULE_TYPE.getWarpper().get("123");
        System.out.println(password);
        return new UserVo(byUniqueId);
    }

    @GetMapping("/selectById")
    public ResponseEntity<Object> getChannelDetail(Long userId) {
        SystemUser byUniqueId = userService.getByUniqueId(userId);
        return BaseResponse.sendMessageSuccess(new UserVo(byUniqueId));
    }

    @PostMapping
    public List<UserVo> conditionSearch(@RequestBody UserQueryParam userQueryParam) {
        IPage<SystemUser> age = userService.pageByParam(userQueryParam);
        return age.convert(UserVo::new).getRecords();
    }

    @PostMapping("/selectCursorList")
    public CursorModel selectCursorList(@RequestBody UserCursorParam userCursorParam) {
        return userService.selectCursorList(userCursorParam);
    }

    @GetMapping("/text")
    public JSONObject text() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("str", userService.getA());
        return jsonObject;
    }

    @PostMapping("/insertText")
    public void insertText(@RequestBody JSONObject str) {
        String str1 = str.getString("str");
        userService.insertText(str1);
    }
}