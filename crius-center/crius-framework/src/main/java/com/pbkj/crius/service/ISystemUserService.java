package com.pbkj.crius.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pbkj.crius.common.utils.CursorModel;
import com.pbkj.crius.entity.param.UserCursorParam;
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
public interface ISystemUserService extends IService<SystemUser> {

    IPage<SystemUser> pageByParam(UserQueryParam userQueryParam);

    SystemUser getByUniqueId(Long userId);

    CursorModel selectCursorList(UserCursorParam userCursorParam);

    String getA();

    void insertText(String str);
}
