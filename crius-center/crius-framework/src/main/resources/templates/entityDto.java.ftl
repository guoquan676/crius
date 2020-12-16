package ${package.Parent};

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import ${package.Entity}.${entity};

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 	${table.comment} DTO
 * @author ${author}
 * @since ${date}
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ${entity}DTO extends ${entity} {

	private static final long serialVersionUID = 1L;
	// 多个主键id(用英文逗号分隔",")
	@JSONField(serialize = false)
	private String ids;
	// 查询开始时间
	@JSONField(serialize = false)
	private Date queryStartTime;
	// 查询结束时间
	@JSONField(serialize = false)
	private Date queryEndTime;
	// 查询开始时间戳
	@JSONField(serialize = false)
	private long longQueryStartTime;
	// 查询结束时间戳
	@JSONField(serialize = false)
	private long longQueryEndTime;
	// 分页查询-每页大小
	@JSONField(serialize = false)
	private long size;
	// 分页查询-当前查询页
	@JSONField(serialize = false)
	private long current;
}
