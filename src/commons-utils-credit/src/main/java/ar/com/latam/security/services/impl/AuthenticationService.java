package ar.com.latam.security.services.impl;

/**
 * Implement access to a web service throw an RPC Call.
 * <P>
 * @author HJSolá - Oracle IDevelopment
 */

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.apache.soap.Constants;
import org.apache.soap.SOAPException;
import org.apache.soap.encoding.SOAPMappingRegistry;
import org.apache.soap.rpc.Call;
import org.apache.soap.rpc.Parameter;
import org.apache.soap.rpc.Response;

import ar.com.latam.security.services.interfaz.IAuthenticationService;

/**
 * 
 * @author diana.karina.rojas
 *
 */
public class AuthenticationService implements IAuthenticationService {

	private static Logger logger = Logger
			.getLogger(AuthenticationService.class);

	private static final String PARAMETER_NAME = "parametro";
	
	private String url;
	private String applicationName;
	private String targetObjectURI;
	private String method;
	// Boolean que sirve para saber si en el List de retorno hay otro valor que no sea WITHOUT_ROL. Si hay otro, entonces nunca grabamos en la lista WITHOUT_ROL. Si solo hay WITHOUT_ROL o no hay nada, va este valor. 
	private boolean isNotWithRol=false;
	
	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getTargetObjectURI() {
		return targetObjectURI;
	}

	public void setTargetObjectURI(String targetObjectURI) {
		this.targetObjectURI = targetObjectURI;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
	
	public List<String> getWebService1Response(String userId, String cookie,String url){
		List<String> value = new ArrayList<String>();
		List<String> appName = new ArrayList<String>();
		appName = parseApplicationName(applicationName);
		for(int i=0; i< appName.size();i++){
			List<String> getWeb = new ArrayList<String>();
			getWeb = getWebService1Response( userId, cookie, url, appName.get(i));
			if(null !=getWeb && getWeb.size()>0){
				value.addAll(getWeb);
			}
		}
		if(isNotWithRol){
			value = checkWithoutRole(value);
		}	
		return value;
	}
	public List<String> getWebService1Response(String userId, String cookie,String url,String applicationN){
		Call call = new Call();
		
		logger.debug("url:" + url);
		logger.debug("targetObjectURI:" + targetObjectURI);
		logger.debug("method:" + method);
		logger.debug("userId:" + userId);
		logger.debug("applicationName:" + applicationN);
		logger.debug("cookie:" + cookie);
		
		call.setSOAPMappingRegistry(new SOAPMappingRegistry());
		call.setTargetObjectURI(this.targetObjectURI);
		call.setMethodName(method);
		call.setEncodingStyleURI(Constants.NS_URI_SOAP_ENC);
		Vector<Parameter> params = new Vector<Parameter>();
		params.add(new Parameter(PARAMETER_NAME, String.class, userId, null));
		params.add(new Parameter(PARAMETER_NAME, String.class, applicationN,	null));
		params.add(new Parameter(PARAMETER_NAME, String.class, cookie, null));
		call.setParams(params);

		Response resp=null;
		try {
			URL urlReal = new URL(url);
			resp = call.invoke(urlReal, "");
		} catch (SOAPException e) {
			logger.error(e.getMessage(), e.getCause());
		} catch (MalformedURLException e) {
			logger.error(e.getMessage(), e.getCause());
		}

		if ((resp==null)||(resp.generatedFault())){
			logger.info("resp es null AuthenticationService");
			
			List<String> values=new ArrayList<String>();
			values.add("WITHOUT_ROL");
			
			return values;

		}

		return parseResponse((String) resp.getReturnValue().getValue());

	}
	
	private List<String> parseResponse(String response){
		isNotWithRol=true;
		List<String> values=new ArrayList<String>();
		 StringTokenizer st = new StringTokenizer(response,";");
		 while(st.hasMoreTokens()){
			 values.add(st.nextToken());
		 }
		return values;
	}
	
	//lo usamos para separar las diferentes "nombre de aplicaciones" que estan definidas en ApplicationContext
	private List<String> parseApplicationName(String applicationName){
		List<String> value = new ArrayList<String>();
		// la coma es el valor que separa las palabras
		value = Arrays.asList(applicationName.split(","));
		return value;
	}
	
	private List<String> checkWithoutRole (List<String> value){
		
		while(value.contains("WITHOUT_ROL")){
			value.remove(value.indexOf("WITHOUT_ROL"));
		}
		return value;
		
	}
}
