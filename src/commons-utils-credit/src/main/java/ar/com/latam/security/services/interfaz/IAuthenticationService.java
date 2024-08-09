package ar.com.latam.security.services.interfaz;

import java.util.List;

/**
 * 
 * @author diana.karina.rojas
 *
 */
public interface IAuthenticationService {

	public List<String> getWebService1Response(String userId, String cookie,String url);
	
	public String getMethod();

	public void setMethod(String method);
	
	public String getUrl();

	public void setUrl(String url);
	
	public String getTargetObjectURI();

	public void setTargetObjectURI(String targetObjectURI);

	public String getApplicationName();

	public void setApplicationName(String applicationName);

}
