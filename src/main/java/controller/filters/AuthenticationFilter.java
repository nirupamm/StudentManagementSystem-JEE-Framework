package controller.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import utils.StringUtils;

public class AuthenticationFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;

		String uri = req.getRequestURI();

		if (uri.endsWith(".css") || uri.endsWith(StringUtils.URL_INDEX)) {
			chain.doFilter(request, response);
			return;
		}

		boolean isLogin = uri.endsWith(StringUtils.URL_LOGIN);
		boolean isLoginServlet = uri.endsWith(StringUtils.SERVLET_URL_LOGIN);
		boolean isLogoutServlet = uri.endsWith(StringUtils.SERVLET_URL_LOGOUT);

		HttpSession session = req.getSession(false);
		boolean isLoggedIn = session != null && session.getAttribute(StringUtils.USERNAME) != null;

		if (!isLoggedIn && !(isLogin || isLoginServlet)) {
			res.sendRedirect(req.getContextPath() + StringUtils.PAGE_URL_LOGIN);
		} else if (isLoggedIn && !(!isLogin || isLogoutServlet)) {
			res.sendRedirect(req.getContextPath() + StringUtils.URL_INDEX);
		} else {
			chain.doFilter(request, response);
		}
	}

	@Override
	public void destroy() {
	}
}
