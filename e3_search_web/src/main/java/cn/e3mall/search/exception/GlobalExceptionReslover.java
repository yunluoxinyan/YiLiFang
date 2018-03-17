package cn.e3mall.search.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

public class GlobalExceptionReslover implements HandlerExceptionResolver {
	
	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionReslover.class);

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object arg2,
			Exception e) {
		logger.error("系统发生异常", e);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("message", "系统发生异常，请稍后重试");
		modelAndView.setViewName("/error/exception");
		return modelAndView;
	}
	
}
