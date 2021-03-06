package com.pbkj.crius.api.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pbkj.crius.entity.param.UserQueryParam;
import com.pbkj.crius.entity.po.SystemUser;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author GZQ
 * @since 2020-07-23
 */
public interface ICUserService extends IService<SystemUser> {

    IPage<SystemUser> pageByParam(UserQueryParam userQueryParam);

    SystemUser getByUniqueId(Long userId);
}
