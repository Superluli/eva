package com.eva.appservice.logging;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ExecutionAOPMonitor {

	@AfterReturning(pointcut = "execution(* com.eva.appservice..*.*(..)) "
			+ "&& @annotation(com.eva.appservice.logging.LogMe)", returning = "retVal")
	public void logReturning(JoinPoint jp, Object retVal) {

		System.err.println(jp.getSignature());
		System.err.println(Arrays.toString(jp.getArgs()));
		System.err.println(retVal);
	}

	@AfterThrowing(pointcut = "execution(* com.eva.appservice..*.*(..)) "
			+ "&& @annotation(com.eva.appservice.logging.LogMe)", throwing = "ex")
	public void logThrowable(JoinPoint jp, Throwable ex) {

		System.err.println(jp.getSignature());
		System.err.println(Arrays.toString(jp.getArgs()));
		System.err.println(ex);
	}

}
