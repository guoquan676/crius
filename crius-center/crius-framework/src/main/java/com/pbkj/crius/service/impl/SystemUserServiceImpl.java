package com.pbkj.crius.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pbkj.crius.common.utils.CursorModel;
import com.pbkj.crius.entity.param.UserCursorParam;
import com.pbkj.crius.entity.param.UserQueryParam;
import com.pbkj.crius.entity.po.SystemUser;
import com.pbkj.crius.entity.vo.UserVo;
import com.pbkj.crius.mapper.SystemUserMapper;
import com.pbkj.crius.service.ISystemUserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author GZQ
 * @since 2020-07-23
 */
@Service
public class SystemUserServiceImpl extends ServiceImpl<SystemUserMapper, SystemUser> implements ISystemUserService {

    @Resource
    private SystemUserMapper userMapper;

    @Override
    public IPage<SystemUser> pageByParam(UserQueryParam userQueryParam) {
        Page<SystemUser> page = userQueryParam.getPage();
        QueryWrapper<SystemUser> cUserQueryWrapper = new QueryWrapper<>();
        String username = userQueryParam.getUsername();
        cUserQueryWrapper.eq(StringUtils.isNotBlank(username), "username", username);
        return this.page(page, cUserQueryWrapper);
    }

    @Override
    public SystemUser getByUniqueId(Long userId) {
        return this.getById(userId);
    }

    @Override
    public CursorModel selectCursorList(UserCursorParam userCursorParam) {
        List<SystemUser> list = userMapper.selectCursorList(userCursorParam);
        return CursorModel.getCursorModel(list, (SystemUser systemUser) -> {
            UserVo userVo = new UserVo();
            BeanUtils.copyProperties(systemUser, userVo);
            return userVo;
        });
    }

    @Override
    public String getA() {
        String a = userMapper.getA();
        return a;
    }

    @Override
    public void insertText(String str) {
        userMapper.insertText( str);
    }
}
