package ${package.ServiceImpl};

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ${package.Mapper}.${table.mapperName};
import ${package.Entity}.${entity};
import ${package.Parent}.${dtoName};
import ${package.Service}.${table.serviceName};
import com.pbkj.cloud.framework.base.ResultCode;
import com.pbkj.cloud.framework.vo.ResultPageModel;

/**
 * 	${table.comment}
 * @author ${author}
 * @since ${date}
 */
@Service
public class ${table.serviceImplName} extends ${superServiceImplClass}<${table.mapperName}, ${entity}> implements ${table.serviceName} {

	@Autowired
	private ${table.mapperName} ${table.mapperName?uncap_first};
	
	// 新增
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int saveData(${entity} ${entity?uncap_first}) {
		super.save(${entity?uncap_first});
		return ResultCode.SUCCESS;
	}
	
	// 删除-支持批量方式
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int removeByIds(String ids) {
		List<Integer> list = new ArrayList<Integer>();
		for (String id : ids.split(",")) {
			list.add(Integer.parseInt(id));
		}
		this.baseMapper.deleteBatchIds(list);
		return ResultCode.SUCCESS;
	}
	
	// 修改-支持批量方式
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int updateByIds(String ids) {
		List<${entity}> list = new ArrayList<${entity}>();
		for (String id : ids.split(",")) {
			${entity} ${entity?uncap_first} = new ${entity}();
			${entity?uncap_first}.setId(Long.parseLong(id));
			list.add(${entity?uncap_first});
		}
		super.updateBatchById(list);
		return ResultCode.SUCCESS;
	}
	
	// 查询-根据id获取
	@Override
	public ${dtoName} getDataById(Long id) {
		return ${table.mapperName?uncap_first}.queryDataById(id);
	}
	
	// 分页查询-返回dto
	@Override
	public ResultPageModel<${dtoName}> queryDtoListByPage(${dtoName} ${dtoName?uncap_first}) {
		Page<Object> page = new Page<Object>(${dtoName?uncap_first}.getCurrent(), ${dtoName?uncap_first}.getSize());
		List<${dtoName}> list = queryDtoList(${dtoName?uncap_first}, page);
		return new ResultPageModel<${dtoName}>(list, (int)page.getCurrent(), (int)page.getSize(), (int)page.getTotal());
	}

	// 获取所有dto
	@Override
	public List<${dtoName}> queryDtoList(${dtoName} ${dtoName?uncap_first}) {
		return queryDtoList(${dtoName?uncap_first}, null);
	}
	
	// 分页查询-返回entity
	@Override
	public ResultPageModel<${entity}> queryEntityListByPage(${dtoName} ${dtoName?uncap_first}) {
		Page<${entity}> page = new Page<${entity}>(${dtoName?uncap_first}.getCurrent(), ${dtoName?uncap_first}.getSize());
        page = this.page(page, setQueryWrapper(${dtoName?uncap_first}));
        return new ResultPageModel<${entity}>(page.getRecords(), (int)page.getCurrent(), (int)page.getSize(), (int)page.getTotal());
	}
	
	// 获取所有entity
	@Override
	public List<${entity}> queryEntityList(${dtoName} ${dtoName?uncap_first}) {
        return super.list(setQueryWrapper(${dtoName?uncap_first}));
	}
	
	// 设置QueryWrapper查询条件
	private QueryWrapper<${entity}> setQueryWrapper(${dtoName} ${dtoName?uncap_first}) {
		QueryWrapper<${entity}> queryWrapper = new QueryWrapper<${entity}>();
		return queryWrapper;
	}
	
	// 根据mapper.xml的方式获取所有dto
	private List<${dtoName}> queryDtoList(${dtoName} ${dtoName?uncap_first}, Page<Object> page) {
		HashMap<String, Object> param = new HashMap<String, Object>();
		// 添加分页参数
		if(page != null) {
			param.put("page", page);
		}
		param.put("${dtoName?uncap_first}", ${dtoName?uncap_first});
		if(${dtoName?uncap_first}.getQueryStartTime() != null) {
			${dtoName?uncap_first}.setLongQueryStartTime(${dtoName?uncap_first}.getQueryStartTime().getTime());
		}
		if(${dtoName?uncap_first}.getQueryEndTime() != null) {
			${dtoName?uncap_first}.setLongQueryEndTime(${dtoName?uncap_first}.getQueryEndTime().getTime());
		}
		List<${dtoName}> list = ${table.mapperName?uncap_first}.queryListDto(param);
		dataConvert(list);
		return list;
	}
	
	// 枚举数据转换
	private void dataConvert(List<${dtoName}> list) {
		list.forEach(data ->{
			
		});
	}
}