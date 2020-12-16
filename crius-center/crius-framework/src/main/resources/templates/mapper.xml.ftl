<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${package.Mapper}.${table.mapperName}">

    <resultMap id="BaseResultMap" type="${package.Entity}.${entity}">
<#list table.fields as field>
<#if field.keyFlag>
        <id column="${field.name}" property="${field.propertyName}" />
</#if>
</#list>
<#list table.commonFields as field>
        <result column="${field.name}" property="${field.propertyName}" />
</#list>
<#list table.fields as field>
<#if !field.keyFlag>
        <result column="${field.name}" property="${field.propertyName}" />
</#if>
</#list>
    </resultMap>
    
    <resultMap id="BaseResultMap_DTO" type="${package.Parent}.${dtoName}">
<#list table.fields as field>
<#if field.keyFlag>
        <id column="${field.name}" property="${field.propertyName}" />
</#if>
</#list>
<#list table.commonFields as field>
        <result column="${field.name}" property="${field.propertyName}" />
</#list>
<#list table.fields as field>
<#if !field.keyFlag>
        <result column="${field.name}" property="${field.propertyName}" />
</#if>
</#list>
	</resultMap>
	
<#if baseColumnList>
    <!-- 通用查询结果列 -->
    <sql id="Base_Column_List">
<#list table.commonFields as field>
        ${field.columnName},
</#list>
        ${table.fieldNames}
    </sql>

</#if>
	<select id="queryDataById" parameterType="long" resultMap="BaseResultMap_DTO">
		SELECT *
		FROM 
			${table.name}
		WHERE 1=1
		AND id = ${mapperLabel}id}
	</select>
	
	<select id="queryListDto" parameterType="map" resultMap="BaseResultMap_DTO">
		SELECT * 
		FROM
			${table.name}
		WHERE 1=1 
<#list table.fields as field>
<#if field.keyFlag>
	<if test="${entity?uncap_first}Dto.${field.propertyName} != null and ${entity?uncap_first}Dto.${field.propertyName} != ''">
			AND ${field.name} = ${mapperLabel}${entity?uncap_first}Dto.${field.propertyName}}
	</if>
</#if>
</#list>
<#list table.commonFields as field>
	<if test="${entity?uncap_first}Dto.${field.propertyName} != null and ${entity?uncap_first}Dto.${field.propertyName} != ''">
			AND ${field.name} = ${mapperLabel}${entity?uncap_first}Dto.${field.propertyName}}
	</if>
</#list>
<#list table.fields as field>
<#if !field.keyFlag>
	<if test="${entity?uncap_first}Dto.${field.propertyName} != null and ${entity?uncap_first}Dto.${field.propertyName} != ''">
			AND ${field.name} = ${mapperLabel}${entity?uncap_first}Dto.${field.propertyName}}
	</if>
</#if>
</#list>
	</select>
</mapper>
