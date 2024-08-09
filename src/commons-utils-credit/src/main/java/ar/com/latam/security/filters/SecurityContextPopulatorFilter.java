package ar.com.latam.security.filters;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import ar.com.latam.security.services.interfaz.IAuthenticationService;

import com.ibm.websphere.servlet.session.IBMApplicationSession;
import com.ibm.websphere.servlet.session.IBMSession;

/**
 * 
 * @author diana.karina.rojas
 *
 */
public class SecurityContextPopulatorFilter extends AbstractSecurityContextPopulatorFilter {

	/** The Constant log. */
	private static final Logger log = Logger.getLogger(SecurityContextPopulatorFilter.class);
	
	private static final String UCID="UCID";
	private static final String COOKIE="Cookie";
	private static final String PRIVILEGES_TOKEN ="privilegesToken";
	
	private static final String NO_SECURITY_URL = "http://no.security";
	private static final String URL_PARAMETER = "url.sua";
	
	private IAuthenticationService authenticationService;
	
	@SuppressWarnings("unchecked")
	@Override
	protected List<String> getPrivileges(ServletRequest request) {
		HttpServletRequest req=(HttpServletRequest) request;
		
		String url = System.getProperty(URL_PARAMETER);
		
		List<String> privileges=null;
		
//		If the Environment is "No Security", The privileges are specified without user
		if(NO_SECURITY_URL.equals(url)){
			return getAllPrivileges();
		}
		
		String userId=req.getHeader(UCID);
		String cookie=req.getHeader(COOKIE);

		
		
		if(userId!=null){
			//entra por sua (primer acceso)
			log.debug("authenticationService no esta vacio");
			//se debe consultar solo desde consumos
			privileges=authenticationService.getWebService1Response(userId, cookie,url);
			if(log.isInfoEnabled()){
				log.info("Usuario ["+userId+"] logueado con los privilegios"+privileges);
			}
			if(privileges!=null){
				log.debug("privilegios:" + privileges);
				IBMApplicationSession ibmAppSession=((IBMSession)req.getSession()).getIBMApplicationSession();
				ibmAppSession.setAttribute(PRIVILEGES_TOKEN, privileges);
			} else {
				log.debug("sin privilegios");
			}
		}else{
			log.debug("user is null");
			IBMApplicationSession ibmAppSession=((IBMSession)req.getSession()).getIBMApplicationSession();
			privileges=(List<String>) ibmAppSession.getAttribute(PRIVILEGES_TOKEN);
		}
		return privileges;
	}

	public IAuthenticationService getAuthenticationService() {
		return authenticationService;
	}

	public void setAuthenticationService(
			IAuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}

	
	private List<String> getAllPrivileges() {
		
		log.debug("obtengo los privilegios");
		List<String> privileges=new ArrayList<String>();
		
			privileges.add("ROLE_CRED_CONS");
			
			privileges.add("ROLE_NOVEDAD");
			
			privileges.add("ROLE_ADMIN_TDOC");
			
			privileges.add("ROLE_CRED_MODIF");
			
			privileges.add("ROLE_CRED_ALTA");
			
			
		
		return privileges;
	}
}
