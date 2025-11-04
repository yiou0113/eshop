package com.example.demo.aspect;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.example.demo.model.User;
import com.example.demo.security.CustomUserDetails;

@Component
@Aspect
public class OrderLoggingAspect {
	private static final Logger logger = LoggerFactory.getLogger(OrderLoggingAspect.class);

	@Pointcut("execution(* com.example.demo.controller.OrderController.*(..))")
	public void orderControllerMethods() {}

	// 前置通知：方法執行前
	@Before("orderControllerMethods()")
	public void logBefore(JoinPoint joinPoint) {
		String methodName = joinPoint.getSignature().getName();
		Object[] args = joinPoint.getArgs();

		String userName = extractUserName(args);

		logger.info("[Order Log] 方法 {} 被呼叫, 使用者: {}, 參數: {}", methodName, userName, Arrays.toString(args));
	}

	// 後置通知：方法成功執行後
	@AfterReturning(pointcut = "orderControllerMethods()", returning = "result")
	public void logAfter(JoinPoint joinPoint, Object result) {
		String methodName = joinPoint.getSignature().getName();
		logger.info("[Order Log] 方法 {} 執行完成, 返回值: {}", methodName, result);
	}

	// 後置異常通知
	@AfterThrowing(pointcut = "orderControllerMethods()", throwing = "ex")
	public void logException(JoinPoint joinPoint, Throwable ex) {
		String methodName = joinPoint.getSignature().getName();
		logger.error("[Order Log] 方法 {} 發生異常: {}", methodName, ex.getMessage(), ex);
	}

	// 嘗試從方法參數提取使用者名稱
	private String extractUserName(Object[] args) {
		for (Object arg : args) {
			if (arg instanceof CustomUserDetails) {
				User user = ((CustomUserDetails) arg).getUser();
				return user.getEmail(); // 或 getEmail() / getId()
			}
		}
		return "匿名";
	}
}
