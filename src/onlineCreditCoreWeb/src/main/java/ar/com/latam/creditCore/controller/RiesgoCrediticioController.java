package ar.com.latam.creditCore.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.jmesa.limit.Limit;
import org.jmesa.model.PageItems;
import org.jmesa.model.TableModelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import ar.com.latam.creditCore.dto.AltaManualClienteRiesgoCrediticioDTO;
import ar.com.latam.creditCore.dto.BusquedaRiesgoCrediticioDTO;
import ar.com.latam.creditCore.dto.ClienteRiesgoCrediticioDTO;
import ar.com.latam.creditCore.dto.ExportCSVDTO;
import ar.com.latam.creditCore.dto.TipoDocumentoDTO;
import ar.com.latam.creditCore.exceptions.CustomSftpException;
import ar.com.latam.creditCore.services.interfaz.IRiesgoCrediticioService;
import ar.com.latam.creditCore.utils.ExportCSV;
import ar.com.latam.util.Util;

/**
 * 
 * @author diana.karina.rojas
 *
 */
@Controller
public class RiesgoCrediticioController {
	
	private static final Logger LOG = Logger.getLogger(RiesgoCrediticioController.class);

	private static final String VISTA_ALTA_DNI_PROSPECTOS = "altaDNIProspectos";	
	private static final String VISTA_CLIENTES_RIESGO_CREDITICIO = "riesgoCrediticio";
	private static final String VISTA_OK_OPERACION_ALTA = "operacionAltaRealizada";
	private static final String VISTA_EDITAR_REGISTRO = "editarRegistro";
	private static final String VISTA_ERROR ="errorPage";
	
	private static final String BEAN_ALTA_DNI_PROSPECTOS = "formAltaDNIProspectosBean";	
	private static final String BEAN_CLIENTES_RIESGO_CREDITICIO = "busquedaRiesgoCrediticioBean";
	private static final String BEAN_REGISTRO = "registroClienteBean";
	private static final String EDICION_REGISTRO = "editarClienteBean";
	private static final String LISTA_TIPOS_DOCUMENTO = "listaTiposDocumento";
	private static final String LISTA_CLIENTES_RIESGO_CREDITICIO = "listaClientesRiesgoCrediticio";
	
	private static final String CAMPO_MENSAJE_ERROR = "mensajeError";
	private static final String CAMPO_MENSAJE_ERROR_VALUE = "error.registroNoEncontrado";
	private static final String TABLE_MODEL = "tablaClientesRiesgo";//Es el mismo id que tiene en riesgoCrediticio.jsp
	private static final String CLIENTES_TOKEN = "clientesToken";
	
	/** Titulo Reporte Manual.**/
    private static final String PREFIJO_TITULO_REPORTE_MANUAL = "AM_RIESGO_CREDITICIO_MANUAL_";
    /** Titulo Archivo CSV de Modificacion.**/
    private static final String PREFIJO_TITULO_CSV_MODIFICACION = "BatchUpdateCustomerSegmentScore_";
    
    private static final String PATH_ARCHIVO_CVS_PARAMETER = "pathArchivoCSVModif";
    
    private Map<String, String> conversionTipoDocumento = new HashMap<String, String>();
	
    @Autowired
    IRiesgoCrediticioService riesgoCrediticioService;	
    @Autowired
    private Validator altaManualRiesgoCrediticioValidador; 
    @Autowired
    private Validator busquedaRiesgoCrediticioValidador; 
    @Autowired
    private Validator modificacionRiesgoCrediticioValidador; 
    @Autowired
    private ExportCSV exportCSV;    
    
    /**
     * Inits the binder.
     *
     * @param binder the binder
     */
    @InitBinder
    protected void initBinder(WebDataBinder binder) throws Exception{
		DateFormat dateFormat = new SimpleDateFormat(Util.DATE_TIME_SCREEN);
		CustomDateEditor editor = new CustomDateEditor(dateFormat, true);
		binder.registerCustomEditor(Date.class, editor);
		if(binder.getTarget() instanceof AltaManualClienteRiesgoCrediticioDTO){
			binder.setValidator(altaManualRiesgoCrediticioValidador);
		}
		if(binder.getTarget() instanceof BusquedaRiesgoCrediticioDTO){
			binder.setValidator(busquedaRiesgoCrediticioValidador);
		}
		if(binder.getTarget() instanceof ClienteRiesgoCrediticioDTO){
			binder.setValidator(modificacionRiesgoCrediticioValidador);
		}
    }
	/**
	 * Handle exception.
	 *
	 * @param e the e
	 * @param request the request
	 * @return the model and view
	 */
	@ExceptionHandler(Exception.class)
	public ModelAndView handleException(Exception e, HttpServletRequest request) {
		LOG.error(e.getMessage(), e);
        request.setAttribute("errorSpecified", e.getMessage());
        request.setAttribute("error", stackTraceToString(e));
		return new ModelAndView(VISTA_ERROR);
	}  
	
	/**
    *
    * @param stackTrace
    * @return String
    */
   private static StringBuilder stackTraceToString( Exception e) {

       StringBuilder s = new StringBuilder();
       s.append(e+"<br/>");
       for (StackTraceElement stackTraceEl : e.getStackTrace()) {
           s.append("      at: "+stackTraceEl.toString()+"<br/>");
       }
       return s;
   }    

   @RequestMapping("/formAltaRiesgoCrediticio")	
   public ModelAndView formAltaRiesgoCrediticio(HttpServletRequest request) throws Exception {
   	ModelAndView mv = null;
   	LOG.info("formAltaRiesgoCrediticio");
   	try{	
   			request.getSession().removeAttribute(LISTA_TIPOS_DOCUMENTO);
   			AltaManualClienteRiesgoCrediticioDTO altaManualClienteRiesgoCrediticio = new AltaManualClienteRiesgoCrediticioDTO();
	    	List<TipoDocumentoDTO> listaTiposDocumentos = riesgoCrediticioService.obtenerTiposDocumento();
	    	request.getSession().setAttribute(LISTA_TIPOS_DOCUMENTO, listaTiposDocumentos);    			
	    	mv = new ModelAndView(VISTA_ALTA_DNI_PROSPECTOS);
	    	mv.addObject(BEAN_ALTA_DNI_PROSPECTOS, altaManualClienteRiesgoCrediticio);
   	}catch(Exception e){
   		LOG.error(e.getMessage(), e);
        request.setAttribute("errorSpecified", e.getMessage());
        request.setAttribute("error", stackTraceToString(e));
   		return new ModelAndView(VISTA_ERROR);
   	}
	    	return mv; 
   } 
   
   @RequestMapping("/darAltaRegistro")	
   public ModelAndView darAltaRegistro(HttpServletRequest request, @ModelAttribute("formAltaDNIProspectosBean") @Valid AltaManualClienteRiesgoCrediticioDTO altaManualClienteRiesgoCrediticio, BindingResult result) throws Exception {
   	ModelAndView mv = null;   	
   	LOG.info("darAltaRegistro");
   	try{	
		if(result.hasErrors()){
			mv=new ModelAndView(VISTA_ALTA_DNI_PROSPECTOS);
			mv.addObject(BEAN_ALTA_DNI_PROSPECTOS, altaManualClienteRiesgoCrediticio);
			return mv;
		}    		
		String userId = Util.modifUser(request.getHeader(Util.UCID));
		LOG.info("USUARIO: " + userId);
		altaManualClienteRiesgoCrediticio.setUsuarioAplicacion(userId);
		conversionTipoDocumento.clear();
		riesgoCrediticioService.cargarConversionTipoDocumento(conversionTipoDocumento); 
		altaManualClienteRiesgoCrediticio.setTipoDoc(conversionTipoDocumento.get(altaManualClienteRiesgoCrediticio.getTipoDoc()));
    	riesgoCrediticioService.altaClienteRiesgoCrediticio(altaManualClienteRiesgoCrediticio); 			
    	mv = new ModelAndView(VISTA_OK_OPERACION_ALTA);	
   	}catch(Exception e){
   		LOG.error(e.getMessage(), e);
        request.setAttribute("errorSpecified", e.getMessage());
        request.setAttribute("error", stackTraceToString(e));
   		return new ModelAndView(VISTA_ERROR);
   	}
   	return mv;
   }
   
   /**
   *
   * @param request HttpServletRequest
   * @return Collection
   */
  @SuppressWarnings("unchecked")
  private Collection<ClienteRiesgoCrediticioDTO> getGridItems(
                  final HttpServletRequest request) {

      return (Collection<ClienteRiesgoCrediticioDTO>) TableModelUtils.getItems(TABLE_MODEL,
          request, new PageItems() {

              public int getTotalRows(Limit limit) {

                  Collection<ClienteRiesgoCrediticioDTO> clientesTotal =
                      (Collection<ClienteRiesgoCrediticioDTO>) request.getSession()
                          .getAttribute(CLIENTES_TOKEN);
                  return clientesTotal.size();
              }

              public Collection<ClienteRiesgoCrediticioDTO> getItems(Limit limit) {

                  int rowStart = limit.getRowSelect().getRowStart();
                  int rowEnd = limit.getRowSelect().getRowEnd();
                  List<ClienteRiesgoCrediticioDTO> clientesItems =
                      (List<ClienteRiesgoCrediticioDTO>) request.getSession().getAttribute(
                    		  CLIENTES_TOKEN);
                  return clientesItems.subList(rowStart, rowEnd);
              }
          });
  }
  
   @RequestMapping("/formConsultaModifRiesgoCrediticio")	
   public ModelAndView formConsultaModifRiesgoCrediticio(HttpServletRequest request) throws Exception {
   	ModelAndView mv = null;
   	LOG.info("formConsultaModifRiesgoCrediticio");
   	Collection<ClienteRiesgoCrediticioDTO> gridItems; 
   	try{	
			request.getSession().removeAttribute(LISTA_TIPOS_DOCUMENTO);
			request.getSession().removeAttribute(CLIENTES_TOKEN);
			request.getSession().removeAttribute(LISTA_CLIENTES_RIESGO_CREDITICIO);
	    	List<TipoDocumentoDTO> listaTiposDocumentos = riesgoCrediticioService.obtenerTiposDocumento();
	    	request.getSession().setAttribute(LISTA_TIPOS_DOCUMENTO, listaTiposDocumentos);    		   			
   			BusquedaRiesgoCrediticioDTO busquedaClientesRiesgoCrediticio = new BusquedaRiesgoCrediticioDTO();
   			ClienteRiesgoCrediticioDTO clienteRiesgoCrediticio = new ClienteRiesgoCrediticioDTO();
   			conversionTipoDocumento.clear();
   			riesgoCrediticioService.cargarConversionTipoDocumento(conversionTipoDocumento);   			
	    	List<ClienteRiesgoCrediticioDTO> listaClientesRiesgoCrediticio = riesgoCrediticioService.obtenerClientesRiesgoCrediticio(Util.TODOS, Util.TODOS, Util.EMPTY_CHARACTER, conversionTipoDocumento);
	    	if(listaClientesRiesgoCrediticio == null){
	    		gridItems = null;
	    	}
	    	else{
		        request.getSession().setAttribute(CLIENTES_TOKEN, listaClientesRiesgoCrediticio);	       	        
		        gridItems = getGridItems(request);	    			    		
	    	}
	    	request.getSession().setAttribute(LISTA_CLIENTES_RIESGO_CREDITICIO, gridItems);
	    	mv = new ModelAndView(VISTA_CLIENTES_RIESGO_CREDITICIO);
	    	mv.addObject(BEAN_CLIENTES_RIESGO_CREDITICIO, busquedaClientesRiesgoCrediticio);
	    	mv.addObject(BEAN_REGISTRO, clienteRiesgoCrediticio);
   	}catch(Exception e){
   		LOG.error(e.getMessage(), e);
        request.setAttribute("errorSpecified", e.getMessage());
        request.setAttribute("error", stackTraceToString(e));
   		return new ModelAndView(VISTA_ERROR);
   	}
	    	return mv; 
   }
   @RequestMapping("/consultarRiesgoCrediticio")	
   public ModelAndView consultarRiesgoCrediticio(HttpServletRequest request, @ModelAttribute("busquedaRiesgoCrediticioBean") @Valid BusquedaRiesgoCrediticioDTO busquedaRiesgoCrediticio, BindingResult result) throws Exception {
   	ModelAndView mv = null;
   	LOG.info("consultarRiesgoCrediticio");
   	Collection<ClienteRiesgoCrediticioDTO> gridItems; 
   	try{	
   			List<ClienteRiesgoCrediticioDTO> listaClientesRiesgoCrediticio;
   			ClienteRiesgoCrediticioDTO clienteRiesgoCrediticio = new ClienteRiesgoCrediticioDTO();   			
   			if(result.hasErrors()){
   				mv=new ModelAndView(VISTA_CLIENTES_RIESGO_CREDITICIO);
   				mv.addObject(BEAN_CLIENTES_RIESGO_CREDITICIO, busquedaRiesgoCrediticio);
   				mv.addObject(BEAN_REGISTRO, clienteRiesgoCrediticio);
   				return mv;
   			}     	
   			request.getSession().removeAttribute(CLIENTES_TOKEN);
   			request.getSession().removeAttribute(LISTA_CLIENTES_RIESGO_CREDITICIO);    			
   			conversionTipoDocumento.clear();
   			riesgoCrediticioService.cargarConversionTipoDocumento(conversionTipoDocumento);
	    	listaClientesRiesgoCrediticio = riesgoCrediticioService.obtenerClientesRiesgoCrediticio(busquedaRiesgoCrediticio.getTipoDoc(), busquedaRiesgoCrediticio.getNroDoc(), busquedaRiesgoCrediticio.getGenero(), conversionTipoDocumento);
	    	if (listaClientesRiesgoCrediticio == null){
	    		//Muestro el mensaje de error y a continuacion obtengo el listado de toda la tabla
	    		result.rejectValue(CAMPO_MENSAJE_ERROR, CAMPO_MENSAJE_ERROR_VALUE);
	    		listaClientesRiesgoCrediticio = riesgoCrediticioService.obtenerClientesRiesgoCrediticio(Util.TODOS, Util.TODOS, Util.EMPTY_CHARACTER, conversionTipoDocumento);
	    	}
	    	if (listaClientesRiesgoCrediticio == null){
	    		gridItems = null;
	    	}else{
		        request.getSession().setAttribute(CLIENTES_TOKEN, listaClientesRiesgoCrediticio);	       	        
		        gridItems = getGridItems(request);	    			    		
	    	}  		    	
	    	request.getSession().setAttribute(LISTA_CLIENTES_RIESGO_CREDITICIO, gridItems);
	    	mv = new ModelAndView(VISTA_CLIENTES_RIESGO_CREDITICIO);
	    	mv.addObject(BEAN_CLIENTES_RIESGO_CREDITICIO, busquedaRiesgoCrediticio);
	    	mv.addObject(BEAN_REGISTRO, clienteRiesgoCrediticio);
   	}catch(Exception e){
   		LOG.error(e.getMessage(), e);
        request.setAttribute("errorSpecified", e.getMessage());
        request.setAttribute("error", stackTraceToString(e));
   		return new ModelAndView(VISTA_ERROR);
   	}
	    	return mv; 
   }
   
   @RequestMapping("/paginarRiesgoCrediticio")	
   public ModelAndView paginarRiesgoCrediticio(HttpServletRequest request, @ModelAttribute("busquedaRiesgoCrediticioBean") BusquedaRiesgoCrediticioDTO busquedaRiesgoCrediticio) throws Exception {	
   	LOG.info("paginarRiesgoCrediticio");
   	ModelAndView mv = null;
   	ClienteRiesgoCrediticioDTO clienteRiesgoCrediticio = new ClienteRiesgoCrediticioDTO();
   	Collection<ClienteRiesgoCrediticioDTO> gridItems; 
   	try{
   		request.getSession().removeAttribute(LISTA_CLIENTES_RIESGO_CREDITICIO);
   		//CLIENTES_TOKEN se encuentra en sesion    		
   		gridItems = getGridItems(request);
    	request.getSession().setAttribute(LISTA_CLIENTES_RIESGO_CREDITICIO, gridItems);
    	mv = new ModelAndView(VISTA_CLIENTES_RIESGO_CREDITICIO);	
    	mv.addObject(BEAN_CLIENTES_RIESGO_CREDITICIO, busquedaRiesgoCrediticio);
    	mv.addObject(BEAN_REGISTRO, clienteRiesgoCrediticio);
   	}catch(Exception e){
   		LOG.error(e.getMessage(), e);
        request.setAttribute("errorSpecified", e.getMessage());
        request.setAttribute("error", stackTraceToString(e));
   		return new ModelAndView(VISTA_ERROR);
   	}
   	return mv;
   }    
   
   @RequestMapping("/editarRegistro")	
   public ModelAndView editarRegistro(HttpServletRequest request, @ModelAttribute("registroClienteBean") ClienteRiesgoCrediticioDTO clienteRiesgoCrediticio) throws Exception {
   	ModelAndView mv = null;
   	LOG.info("editarRegistro");
   	try{	
	    mv = new ModelAndView(VISTA_EDITAR_REGISTRO);
	    mv.addObject(EDICION_REGISTRO, clienteRiesgoCrediticio);
   	}catch(Exception e){
   		LOG.error(e.getMessage(), e);
        request.setAttribute("errorSpecified", e.getMessage());
        request.setAttribute("error", stackTraceToString(e));
   		return new ModelAndView(VISTA_ERROR);
   	}
	return mv; 
   }
   @RequestMapping("/actualizarRegistro")	
   public ModelAndView actualizarRegistro(HttpServletRequest request, @ModelAttribute("editarClienteBean") @Valid ClienteRiesgoCrediticioDTO clienteRiesgoCrediticio, BindingResult result) throws Exception {
   	ModelAndView mv = null;   	
   	LOG.info("actualizarRegistro");
   	List<ClienteRiesgoCrediticioDTO> listaClientesRiesgoCrediticioCSV;
   	try{	
		if(result.hasErrors()){
			mv=new ModelAndView(VISTA_EDITAR_REGISTRO);
			mv.addObject(EDICION_REGISTRO, clienteRiesgoCrediticio);
			return mv;
		}    		
		String userId = Util.modifUser(request.getHeader(Util.UCID));
		String rutaArchivoCSV = System.getProperty(PATH_ARCHIVO_CVS_PARAMETER);
		LOG.info("USUARIO: " + userId);
		clienteRiesgoCrediticio.setUsuarioCargaCambio(userId);
		conversionTipoDocumento.clear();
		riesgoCrediticioService.cargarConversionTipoDocumento(conversionTipoDocumento);
		listaClientesRiesgoCrediticioCSV = riesgoCrediticioService.modificacionScoreClienteRiesgoCrediticio(clienteRiesgoCrediticio, conversionTipoDocumento);
		return formConsultaModifRiesgoCrediticio(request);

   	}catch (CustomSftpException e) {
   		LOG.error(stackTraceToString(e), e);
   		mv= new ModelAndView(VISTA_EDITAR_REGISTRO);
   		mv.addObject(clienteRiesgoCrediticio);
		request.setAttribute("errorSFTP", "errorSFTP");
		return mv;
		
    }catch(Exception e){   		
   		LOG.error(e.getMessage(), e);
        request.setAttribute("errorSpecified", e.getMessage());
        request.setAttribute("error", stackTraceToString(e));
       	return new ModelAndView(VISTA_ERROR);   		
   	}
   } 
   
   @RequestMapping("/bajadaReporteManualCSV")	
   public ModelAndView bajadaReporteManualCSV(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("busquedaRiesgoCrediticioBean") @Valid BusquedaRiesgoCrediticioDTO busquedaRiesgoCrediticio, BindingResult result) throws Exception {	
   	
	LOG.info("bajadaReporteManualCSV");

	try{ 			
		ExportCSVDTO dto = new ExportCSVDTO();
		dto.setResponse(response);
		dto.setFechaDesde(busquedaRiesgoCrediticio.getFechaDesde());
		dto.setFechaHasta(busquedaRiesgoCrediticio.getFechaHasta());
   		List<ClienteRiesgoCrediticioDTO> listaReporteManual = riesgoCrediticioService.obtenerReporteRiesgosCrediticiosManuales(dto);
   		//exportCSV.createCSVReporteManual(response, listaReporteManual, getTitleReports(request) + ".CSV");
   		dto.getResponse().getOutputStream().close();
       	return null; 
   	}catch(Exception e){
   		LOG.error(e.getMessage(), e);
        request.setAttribute("errorSpecified", e.getMessage());
        request.setAttribute("error", stackTraceToString(e));
        return new ModelAndView(VISTA_ERROR);
   	}       	
   } 
   
   private String getTitleReports(HttpServletRequest request){
	  return PREFIJO_TITULO_REPORTE_MANUAL + getDateReports();
   }
	  
  public String getDateReports(){
	  /*AAAAMMDD_HHMMSS*/
      SimpleDateFormat formato = new SimpleDateFormat("yyyyMMdd_HHmmss");
      Date fechaActual = new Date();
      return formato.format(fechaActual);
  }
}
