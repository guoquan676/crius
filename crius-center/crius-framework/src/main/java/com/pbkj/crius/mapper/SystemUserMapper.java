package com.pbkj.crius.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pbkj.crius.entity.param.UserCursorParam;
import com.pbkj.crius.entity.po.SystemUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author GZQ
 * @since 2020-07-23
 */
public interface SystemUserMapper extends BaseMapper<SystemUser> {

    List<SystemUser> selectCursorList(UserCursorParam userCursorParam);

    String getA();

    void insertText(@Param("str")String str);
}
