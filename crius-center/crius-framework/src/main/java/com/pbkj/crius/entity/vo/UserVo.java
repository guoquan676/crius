package com.pbkj.crius.entity.vo;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.pbkj.crius.entity.po.SystemUser;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class UserVo extends BaseVo {

    public UserVo(SystemUser user) {
        BeanUtils.copyProperties(user, this);
        Long createdTime = user.getCreatedTime();
        this.createdTime = DateUtil.format(DateUtil.date(createdTime), DatePattern.NORM_DATETIME_PATTERN);
    }

    private String username;
    private Integer age;
    private String createdTime;
}
