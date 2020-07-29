package com.pbkj.crius.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pbkj.crius.entity.po.CUser;
import com.pbkj.crius.mapper.CUserMapper;
import com.pbkj.crius.entity.param.UserQueryParam;
import com.pbkj.crius.service.ICUserService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author GZQ
 * @since 2020-07-23
 */
@Service
public class CUserServiceImpl extends ServiceImpl<CUserMapper, CUser> implements ICUserService {

    @Override
    public IPage<CUser> pageByParam(UserQueryParam userQueryParam) {
        Page<CUser> page = userQueryParam.getPage();
        QueryWrapper<CUser> cUserQueryWrapper = userQueryParam.queryBuild();
        return this.page(page, cUserQueryWrapper);
    }

    @Override
    public CUser getByUniqueId(Long userId) {
        return this.getById(userId);
    }
}
