package com.pbkj.crius.common.base;

/**
 * 常用返回码
 * 
 * @author mengpp
 * @date 2019年9月16日21:06:59
 */
public class ResultCode {

	/**
	 * 成功，在请求不是被成功执行的情况下，严禁返回此状态
	 */
	public static final int SUCCESS = 200;

	/**
	 * 创建成功，代表立即完成该资源可以访问
	 */
//	public static final int SUCCESS_CREATE = 201;

	/**
	 * 错误的请求，服务器不理解的请求，如参数错误
	 */
	public static final int ERROR_REQUEST = 400;

	/**
	 * 需要验证身份（登录）
	 */
	public static final int ERROR_IDENTITY = 401;

	/**
	 * 身份有效，但权限不足
	 */
	public static final int ERROR_PERMISSIONS = 403;

	/**
	 * 请求的资源不存在
	 */
	public static final int ERROR_RESOURCES = 404;

	/**
	 * 针对这个资源所请求的方法被禁止
	 */
	public static final int ERROR_METHODS = 405;
	/**
	 * 针对这个方法传递的参数已存在于DB,请刷新页面后尝试
	 */
	public static final int ERROR_Data_Already = 408;
	/**
	 * 服务端错误，错误原因未知（可在返回此状态同时附加message说明具体错误）
	 */
	public static final int ERROR_SYSTEM = 500;
	public static final int SERVICE_UNAVAILABLE = 503;

}
