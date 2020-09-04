package com.pbkj.crius.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pbkj.crius.common.utils.CursorModel;
import com.pbkj.crius.entity.param.UserCursorParam;
import com.pbkj.crius.entity.param.UserQueryParam;
import com.pbkj.crius.entity.po.CUser;
import com.pbkj.crius.entity.vo.UserVo;
import com.pbkj.crius.mapper.CUserMapper;
import com.pbkj.crius.service.ICUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
public class CUserServiceImpl extends ServiceImpl<CUserMapper, CUser> implements ICUserService {

    @Resource
    private CUserMapper userMapper;

    @Override
    public IPage<CUser> pageByParam(UserQueryParam userQueryParam) {
        Page<CUser> page = userQueryParam.getPage();
        QueryWrapper<CUser> cUserQueryWrapper = new QueryWrapper<>();
        String username = userQueryParam.getUsername();
        cUserQueryWrapper.eq(StringUtils.isNotBlank(username), "username", username);
        return this.page(page, cUserQueryWrapper);
    }

    @Override
    public CUser getByUniqueId(Long userId) {
        return this.getById(userId);
    }

    @Override
    public CursorModel selectCursorList(UserCursorParam userCursorParam) {
        List<CUser> list = userMapper.selectCursorList(userCursorParam);
        return CursorModel.getCursorModel(list, (CUser cUser) -> {
            UserVo userVo = new UserVo();
            BeanUtils.copyProperties(cUser, userVo);
            return userVo;
        });
    }
}
