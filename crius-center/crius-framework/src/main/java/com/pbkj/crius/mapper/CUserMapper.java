package com.pbkj.crius.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pbkj.crius.entity.param.UserCursorParam;
import com.pbkj.crius.entity.po.CUser;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author GZQ
 * @since 2020-07-23
 */
public interface CUserMapper extends BaseMapper<CUser> {

    List<CUser> selectCursorList(UserCursorParam userCursorParam);
}
