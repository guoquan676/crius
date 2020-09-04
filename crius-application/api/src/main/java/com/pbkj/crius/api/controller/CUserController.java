package com.pbkj.crius.api.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pbkj.crius.common.utils.CursorModel;
import com.pbkj.crius.entity.param.UserCursorParam;
import com.pbkj.crius.entity.param.UserQueryParam;
import com.pbkj.crius.entity.po.CUser;
import com.pbkj.crius.entity.vo.UserVo;
import com.pbkj.crius.service.ICUserService;
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
public class CUserController {

    @Resource
    private ICUserService userService;

    @GetMapping
    public UserVo selectById(Long userId) {
        CUser byUniqueId = userService.getByUniqueId(userId);
        return new UserVo(byUniqueId);
    }

    @PostMapping
    public List<UserVo> conditionSearch(@RequestBody UserQueryParam userQueryParam) {
        IPage<CUser> age = userService.pageByParam(userQueryParam);
        return age.convert(UserVo::new).getRecords();
    }

    @PostMapping("/selectCursorList")
    public CursorModel selectCursorList(@RequestBody UserCursorParam userCursorParam) {
        return userService.selectCursorList(userCursorParam);
    }
}

