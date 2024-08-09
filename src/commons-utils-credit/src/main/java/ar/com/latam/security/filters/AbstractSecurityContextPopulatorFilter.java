package ar.com.latam.security.filters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

/**
 * 
 * @author diana.karina.rojas
 *
 */
public abstract class AbstractSecurityContextPopulatorFilter implements Filter {

    private static final Logger log = Logger.getLogger(SecurityContextPopulatorFilter.class);
	private static String WITHOUT_ROL = "WITHOUT_ROL";
	
	public void init(FilterConfig filterConfig) throws ServletException {
		//do nothing
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req=(HttpServletRequest) request;
		
		
		SecurityContext securityContext=SecurityContextHolder.getContext();
		HttpSession session = ((HttpServletRequest) request).getSession(true);
		if(securityContext.getAuthentication()==null){
			List<String> privileges =null; 
			if(req.isRequestedSessionIdValid() && req.getRequestedSessionId() != null){
				if(null != (List<String>) session.getAttribute("privileges"))
					privileges = (List<String>) session.getAttribute("privileges");
			}	
			if(privileges==null){
				privileges=getPrivileges(request);
				session.setAttribute("privileges", privileges);
			}	
			List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
			if (privileges!=null) {
				if(privileges.size() == 0) {
				    log.info("Ingreso en No hay privilegios");
					GrantedAuthority grantedAuthority = new GrantedAuthorityImpl(WITHOUT_ROL);
					log.info("Se agrega el rol WITHOUT_ROL");
					grantedAuthorities.add(grantedAuthority);
					log.info("Fin en No hay privilegios");
				}

				for(String privilege : privileges){
					GrantedAuthority grantedAuthority = new GrantedAuthorityImpl(privilege);
					grantedAuthorities.add(grantedAuthority);
				}
			}
			Authentication authentication=new PreAuthenticatedAuthenticationToken(null, null, grantedAuthorities);
			securityContext.setAuthentication(authentication);
			req.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
		}
		else{
			
			session.invalidate();

		}
		chain.doFilter(request, response);		
	}

	public void destroy() {
		//do nothing
	}
	
	protected abstract List<String> getPrivileges(ServletRequest request);
	

}
