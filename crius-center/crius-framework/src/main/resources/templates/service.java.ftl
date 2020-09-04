package ${package.Service};

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import ${package.Entity}.${entity};
import ${package.Entity}.dto.${dtoName};
import com.pbkj.cloud.framework.vo.ResultPageModel;

/**
 * 	${table.comment}
 * @author ${author}
 * @since ${date}
 */
public interface ${table.serviceName} extends ${superServiceClass}<${entity}> {
	
	// 新增
	int saveData(${entity} ${entity?uncap_first});
	
	// 删除-支持批量方式
	int removeByIds(String ids);
	
	// 修改-支持批量方式
	int updateByIds(String ids);

	// 查询-根据id获取
	${dtoName} getDataById(Long id);
	
	// 分页查询-返回dto
	ResultPageModel<${dtoName}> queryDtoListByPage(${dtoName} ${dtoName?uncap_first});
	
	// 分页查询-返回entity
	ResultPageModel<${entity}> queryEntityListByPage(${dtoName} ${dtoName?uncap_first});
	
	// 获取所有dto
	List<${dtoName}> queryDtoList(${dtoName} ${dtoName?uncap_first});
	
	// 获取所有entity
	List<${entity}> queryEntityList(${dtoName} ${dtoName?uncap_first});
	
}
