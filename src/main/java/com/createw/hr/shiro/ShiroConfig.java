package com.createw.hr.shiro;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShiroConfig {
	@Bean
	public ShiroFilterFactoryBean shirFilter(org.apache.shiro.mgt.SecurityManager securityManager) {
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		// 必须设置 SecurityManager
		shiroFilterFactoryBean.setSecurityManager(securityManager);
		// setLoginUrl 如果不设置值，默认会自动寻找Web工程根目录下的"/login.jsp"页面 或 "/login" 映射
		shiroFilterFactoryBean.setLoginUrl("/web/index");
		shiroFilterFactoryBean.setSuccessUrl("/web/home");
		// 设置无权限时跳转的 url;
		shiroFilterFactoryBean.setUnauthorizedUrl("/notRole");
		// 设置拦截器
		Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
		// 游客，开发权限
		//filterChainDefinitionMap.put("/guest/**", "anon");
		// 用户，需要角色权限 “user”
		//filterChainDefinitionMap.put("/tbH5Activity/**", "roles[admin]");
		// 管理员，需要角色权限 “admin”
		//filterChainDefinitionMap.put("/admin/**", "roles[admin]");
		// 开放登陆接口
		filterChainDefinitionMap.put("/login", "anon");
		// 其余接口一律拦截
		// 主要这行代码必须放在所有权限设置的最后，不然会导致所有 url 都被拦截
		//
		//filterChainDefinitionMap.put("/**", "authc");
		filterChainDefinitionMap.put("/api/**", "anon");
		// //匿名访问静态资源
		filterChainDefinitionMap.put("/*.js", "anon");
		filterChainDefinitionMap.put("/encript/**", "anon");
		filterChainDefinitionMap.put("/images/**", "anon");
		filterChainDefinitionMap.put("/layui/**", "anon");
		filterChainDefinitionMap.put("/login/**", "anon");
		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
		filterChainDefinitionMap.put("/**", "user");
		System.out.println("Shiro拦截器工厂类注入成功");
		return shiroFilterFactoryBean;
	}

	/**
	 * 注入 securityManager
	 */
	@Bean
	public org.apache.shiro.mgt.SecurityManager securityManager() {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		// 设置realm.
		securityManager.setRealm(customRealm());
		return securityManager;
	}

	/**
	 * 自定义身份认证 realm;
	 * <p>
	 * 必须写这个类，并加上 @Bean 注解，目的是注入 CustomRealm， 否则会影响 CustomRealm类 中其他类的依赖注入
	 */
	@Bean
	public CustomRealm customRealm() {
		return new CustomRealm();
	}
}
