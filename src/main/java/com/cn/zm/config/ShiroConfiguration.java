package com.cn.zm.config;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.io.ResourceUtils;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import com.cn.zm.shiro.RetryLimitHashedCredentialsMatcher;
import com.cn.zm.shiro.ShiroRealm;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import lombok.extern.slf4j.Slf4j;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;

@Slf4j
@Configuration
public class ShiroConfiguration {
	
    /**
     * LifecycleBeanPostProcessor，这是个DestructionAwareBeanPostProcessor的子类，
     * 负责org.apache.shiro.util.Initializable类型bean的生命周期的，初始化和销毁。
     * 主要是AuthorizingRealm类的子类，以及EhCacheManager类。
     */
    @Bean(name = "lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /**ShiroRealm，这是个自定义的认证类，继承自AuthorizingRealm，
     * 负责用户的认证和权限的处理，可以参考JdbcRealm的实现。
     */
    @Bean(name = "shiroRealm")
    @DependsOn("lifecycleBeanPostProcessor")
    public ShiroRealm shiroRealm() {
        ShiroRealm realm = new ShiroRealm();
        realm.setCredentialsMatcher(hashedCredentialsMatcher());
        return realm;
    }

	/**
	 * ehcache缓存管理器；shiro整合ehcache：
	 * 通过安全管理器：securityManager
	 * 单例的cache防止热部署重启失败
	 * @return EhCacheManager
	 */
    @Bean(name = "ehCacheManager")
    @DependsOn("lifecycleBeanPostProcessor")
        public EhCacheManager ehCacheManager(){
        EhCacheManager ehCacheManager = new EhCacheManager();
        CacheManager cacheManager = CacheManager.getCacheManager("es");
        if (cacheManager == null) {
			try {

				cacheManager = CacheManager.create(ResourceUtils
						.getInputStreamForPath("classpath:config/ehcache.xml"));

			} catch (CacheException | IOException e) {
				e.printStackTrace();
			}
		}
        ehCacheManager.setCacheManager(cacheManager);
        return ehCacheManager;
    }
    
    /**
     *  SecurityManager，权限管理，这个类组合了登陆，登出，权限，session的处理，是个比较重要的类。
     * @return
     */
    @Bean(name = "securityManager")
    public DefaultWebSecurityManager securityManager(){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(shiroRealm());
        securityManager.setCacheManager(ehCacheManager());//用户授权/认证信息Cache, 采用EhCache 缓存
		// 注入Cookie记住我管理器
		securityManager.setRememberMeManager(rememberMeManager());
        return securityManager;
    }

    /**
     * ShiroFilterFactoryBean，是个factorybean，为了生成ShiroFilter。
     * 它主要保持了三项数据，securityManager，filters，filterChainDefinitionManager。
     */
    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager securityManager){
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		log.debug("-----------------Shiro拦截器工厂类注入开始");
		// 配置shiro安全管理器 SecurityManager
		shiroFilterFactoryBean.setSecurityManager(securityManager);

		// 指定要求登录时的链接
		shiroFilterFactoryBean.setLoginUrl("/login");
		// 登录成功后要跳转的链接
		shiroFilterFactoryBean.setSuccessUrl("/home");
		// 未授权时跳转的界面;
		shiroFilterFactoryBean.setUnauthorizedUrl("/error");

		// filterChainDefinitions拦截器=map必须用：LinkedHashMap，因为它必须保证有序
		Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
		// 配置退出过滤器,具体的退出代码Shiro已经实现
		filterChainDefinitionMap.put("/logout", "logout");
		// 配置记住我或认证通过可以访问的地址
		filterChainDefinitionMap.put("/user/userList", "user");
		filterChainDefinitionMap.put("/", "user");
		//
		// // 配置不会被拦截的链接 从上向下顺序判断
		filterChainDefinitionMap.put("/login", "anon");
		filterChainDefinitionMap.put("/css/*", "anon");
		filterChainDefinitionMap.put("/js/*", "anon");
		filterChainDefinitionMap.put("/js/*/*", "anon");
		filterChainDefinitionMap.put("/js/*/*/*", "anon");
		filterChainDefinitionMap.put("/images/*/**", "anon");
		filterChainDefinitionMap.put("/layui/*", "anon");
		filterChainDefinitionMap.put("/layui/*/**", "anon");
		filterChainDefinitionMap.put("/treegrid/*", "anon");
		filterChainDefinitionMap.put("/treegrid/*/*", "anon");
		filterChainDefinitionMap.put("/fragments/*", "anon");
		filterChainDefinitionMap.put("/layout", "anon");

		filterChainDefinitionMap.put("/user/sendMsg", "anon");
		filterChainDefinitionMap.put("/user/login", "anon");
		filterChainDefinitionMap.put("/home", "anon");
		// //add操作，该用户必须有【addOperation】权限
		// filterChainDefinitionMap.put("/user/setUser", "roles[superman]");
		filterChainDefinitionMap
				.put("/user/delUser", "authc,perms[usermanage]");
		//
		// // <!-- authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问【放行】-->
		filterChainDefinitionMap.put("/*", "authc");
		filterChainDefinitionMap.put("/*/*", "authc");
		filterChainDefinitionMap.put("/*/*/*", "authc");
		filterChainDefinitionMap.put("/*/*/*/**", "authc");

		shiroFilterFactoryBean
				.setFilterChainDefinitionMap(filterChainDefinitionMap);
		log.debug("-----------------Shiro拦截器工厂类注入成功");
		return shiroFilterFactoryBean;
    }
	
	/**
	 * 设置记住我cookie过期时间
	 * @return
	 */
	@Bean
	public SimpleCookie remeberMeCookie() {
		log.debug("记住我，设置cookie过期时间！");
		SimpleCookie scookie = new SimpleCookie("rememberMe");
		// 记住我cookie生效时间30天 ,单位秒 [1小时]
		scookie.setMaxAge(3600);
		return scookie;
	}
	
    /**
	 * 配置cookie记住我管理器
	 * @return
	 */
	@Bean
	public CookieRememberMeManager rememberMeManager() {
		log.debug("配置cookie记住我管理器！");
		CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
		cookieRememberMeManager.setCookie(remeberMeCookie());
		return cookieRememberMeManager;
	}
	

	/**
	 * 凭证匹配器 （由于我们的密码校验交给Shiro的SimpleAuthenticationInfo进行处理了
	 * 所以我们需要修改下doGetAuthenticationInfo中的代码,更改密码生成规则和校验的逻辑一致即可; ）
	 *
	 * @return
	 */
	@Bean
	public HashedCredentialsMatcher hashedCredentialsMatcher() {
		HashedCredentialsMatcher hashedCredentialsMatcher = new RetryLimitHashedCredentialsMatcher(
				ehCacheManager());
		// new HashedCredentialsMatcher();
		hashedCredentialsMatcher.setHashAlgorithmName("md5");// 散列算法:这里使用MD5算法;
		hashedCredentialsMatcher.setHashIterations(1);// 散列的次数，比如散列两次，相当于 //
														// md5(md5(""));
		return hashedCredentialsMatcher;
	}
	
	/**
	 * @描述：ShiroDialect，为了在thymeleaf里使用shiro的标签的bean 
	 * @创建人：wyait
	 * @创建时间：2017年12月21日 下午1:52:59
	 * @return
	 */
	@Bean
	public ShiroDialect shiroDialect() {
		return new ShiroDialect();
	}
	
    /**
     *  DefaultAdvisorAutoProxyCreator，Spring的一个bean，由Advisor决定对哪些类的方法进行AOP代理。
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator daap = new DefaultAdvisorAutoProxyCreator();
        daap.setProxyTargetClass(true);
        return daap;
    }

    /**
     * AuthorizationAttributeSourceAdvisor，shiro里实现的Advisor类，
     * 内部使用AopAllianceAnnotationsAuthorizingMethodInterceptor来拦截用以下注解的方法。
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor aasa = new AuthorizationAttributeSourceAdvisor();
        aasa.setSecurityManager(securityManager);
        return aasa;
    }
}
