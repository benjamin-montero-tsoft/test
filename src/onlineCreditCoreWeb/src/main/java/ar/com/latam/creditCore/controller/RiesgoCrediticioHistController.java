package ar.com.latam.creditCore.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.jmesa.limit.Limit;
import org.jmesa.model.PageItems;
import org.jmesa.model.TableModelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import ar.com.latam.creditCore.dto.RiesgoCrediticioHistoricoDTO;
import ar.com.latam.creditCore.dto.TipoDocumentoDTO;
import ar.com.latam.creditCore.services.interfaz.IRiesgoCrediticioHistService;
import ar.com.latam.util.Util;

/**
 * 
 * @author diana.karina.rojas
 *
 */
@Controller
public class RiesgoCrediticioHistController {
	
	private static final Logger LOG = Logger.getLogger(RiesgoCrediticioHistController.class);

	private static final String VISTA_CLIENTES_RIESGO_CREDITICIO_HIST = "riesgoCrediticioHist";
	private static final String VISTA_ERROR ="errorPage";
	
	private static final String BEAN_CLIENTES_RIESGO_CREDITICIO_HIST = "busquedaRiesgoCrediticioHistBean";
	private static final String LISTA_TIPOS_DOCUMENTO_HIST = "listaTiposDocumentoHist";
	private static final String MOSTRAR_LISTA_HISTORICA = "mostrarListaHistorica";
	private static final String LISTA_CLIENTES_RIESGO_CREDITICIO_HIST = "listaClientesRiesgoCrediticioHist";
	
	private static final String TABLE_MODEL = "tablaClientesRiesgoHist";//Es el mismo id que tiene en riesgoCrediticioHist.jsp
	private static final String CLIENTES_HIST_TOKEN = "clientesHistToken";	
	private Map<String, String> conversionTipoDocumento = new HashMap<String, String>();
	
    @Autowired
    IRiesgoCrediticioHistService riesgoCrediticioHistService;	
    @Autowired
    private Validator busquedaRiesgoCrediticioHistValidador; 
    
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
		if(binder.getTarget() instanceof RiesgoCrediticioHistoricoDTO){
			binder.setValidator(busquedaRiesgoCrediticioHistValidador);
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
 
   /**
   *
   * @param request HttpServletRequest
   * @return Collection
   */
  @SuppressWarnings("unchecked")
  private Collection<RiesgoCrediticioHistoricoDTO> getGridItems(
                  final HttpServletRequest request) {

      return (Collection<RiesgoCrediticioHistoricoDTO>) TableModelUtils.getItems(TABLE_MODEL,
          request, new PageItems() {

              public int getTotalRows(Limit limit) {

                  Collection<RiesgoCrediticioHistoricoDTO> clientesHistTotal =
                      (Collection<RiesgoCrediticioHistoricoDTO>) request.getSession()
                          .getAttribute(CLIENTES_HIST_TOKEN);
                  return clientesHistTotal.size();
              }

              public Collection<RiesgoCrediticioHistoricoDTO> getItems(Limit limit) {

                  int rowStart = limit.getRowSelect().getRowStart();
                  int rowEnd = limit.getRowSelect().getRowEnd();
                  List<RiesgoCrediticioHistoricoDTO> clientesHistItems =
                      (List<RiesgoCrediticioHistoricoDTO>) request.getSession().getAttribute(
                    		  CLIENTES_HIST_TOKEN);
                  return clientesHistItems.subList(rowStart, rowEnd);
              }
          });
  }
  
   @RequestMapping("/formConsultaRiesgoCrediticioHist")	
   public ModelAndView formConsultaRiesgoCrediticioHist(HttpServletRequest request) throws Exception {
   	ModelAndView mv = null;
   	LOG.info("formConsultaRiesgoCrediticioHist");
   	boolean mostrar_lista_historica = false;
   	try{	
			request.getSession().removeAttribute(LISTA_TIPOS_DOCUMENTO_HIST);
			request.getSession().removeAttribute(CLIENTES_HIST_TOKEN);
			request.getSession().removeAttribute(LISTA_CLIENTES_RIESGO_CREDITICIO_HIST);
			request.getSession().removeAttribute(MOSTRAR_LISTA_HISTORICA);
	    	List<TipoDocumentoDTO> listaTiposDocumentos = riesgoCrediticioHistService.obtenerTiposDocumento();
	    	request.getSession().setAttribute(LISTA_TIPOS_DOCUMENTO_HIST, listaTiposDocumentos);
	    	request.getSession().setAttribute(MOSTRAR_LISTA_HISTORICA, mostrar_lista_historica);    
	    	RiesgoCrediticioHistoricoDTO busquedaClientesRiesgoCrediticioHist = new RiesgoCrediticioHistoricoDTO();
	    	mv = new ModelAndView(VISTA_CLIENTES_RIESGO_CREDITICIO_HIST);
	    	mv.addObject(BEAN_CLIENTES_RIESGO_CREDITICIO_HIST, busquedaClientesRiesgoCrediticioHist);
   	}catch(Exception e){
   		LOG.error(e.getMessage(), e);
        request.setAttribute("errorSpecified", e.getMessage());
        request.setAttribute("error", stackTraceToString(e));
   		return new ModelAndView(VISTA_ERROR);
   	}
	    	return mv; 
   }
   @RequestMapping("/consultarRiesgoCrediticioHist")	
   public ModelAndView consultarRiesgoCrediticioHist(HttpServletRequest request, @ModelAttribute("busquedaRiesgoCrediticioHistBean") @Valid RiesgoCrediticioHistoricoDTO busquedaRiesgoCrediticioHist, BindingResult result) throws Exception {
   	ModelAndView mv = null;
   	LOG.info("consultarRiesgoCrediticioHist");
   	Collection<RiesgoCrediticioHistoricoDTO> gridItems; 
   	try{	
   			List<RiesgoCrediticioHistoricoDTO> listaClientesRiesgoCrediticioHist;
   			boolean mostrar_lista_historica = false;
   			if(result.hasErrors()){
   				mv=new ModelAndView(VISTA_CLIENTES_RIESGO_CREDITICIO_HIST);
   				request.getSession().setAttribute(MOSTRAR_LISTA_HISTORICA, mostrar_lista_historica);
   				mv.addObject(BEAN_CLIENTES_RIESGO_CREDITICIO_HIST, busquedaRiesgoCrediticioHist);
   				return mv;
   			}
   			request.getSession().removeAttribute(CLIENTES_HIST_TOKEN);
   			request.getSession().removeAttribute(LISTA_CLIENTES_RIESGO_CREDITICIO_HIST);
   			conversionTipoDocumento.clear();
   			riesgoCrediticioHistService.cargarConversionTipoDocumento(conversionTipoDocumento); 
	    	listaClientesRiesgoCrediticioHist = riesgoCrediticioHistService.obtenerClientesRiesgoCrediticioActualMasHist(busquedaRiesgoCrediticioHist.getTipoDoc(), busquedaRiesgoCrediticioHist.getNroDoc(), busquedaRiesgoCrediticioHist.getGenero(), conversionTipoDocumento);
	    	if (listaClientesRiesgoCrediticioHist == null){
	    		gridItems = null;
	    	}else{
		        request.getSession().setAttribute(CLIENTES_HIST_TOKEN, listaClientesRiesgoCrediticioHist);	       	        
		        gridItems = getGridItems(request);	    			    		
	    	}
	    	mostrar_lista_historica = true;
   			request.getSession().setAttribute(MOSTRAR_LISTA_HISTORICA, mostrar_lista_historica);
	    	request.getSession().setAttribute(LISTA_CLIENTES_RIESGO_CREDITICIO_HIST, gridItems);
	    	mv = new ModelAndView(VISTA_CLIENTES_RIESGO_CREDITICIO_HIST);
	    	mv.addObject(BEAN_CLIENTES_RIESGO_CREDITICIO_HIST, busquedaRiesgoCrediticioHist);
   	}catch(Exception e){
   		LOG.error(e.getMessage(), e);
        request.setAttribute("errorSpecified", e.getMessage());
        request.setAttribute("error", stackTraceToString(e));
   		return new ModelAndView(VISTA_ERROR);
   	}
	    	return mv; 
   }
   
   @RequestMapping("/paginarRiesgoCrediticioHist")	
   public ModelAndView paginarRiesgoCrediticioHist(HttpServletRequest request, @ModelAttribute("busquedaRiesgoCrediticioBean") RiesgoCrediticioHistoricoDTO busquedaRiesgoCrediticioHist) throws Exception {	
   	LOG.info("paginarRiesgoCrediticioHist");
   	ModelAndView mv = null;
   	Collection<RiesgoCrediticioHistoricoDTO> gridItems; 
   	try{
   		request.getSession().removeAttribute(LISTA_CLIENTES_RIESGO_CREDITICIO_HIST);
   		//CLIENTES_HIST_TOKEN se encuentra en sesion
   		//MOSTRAR_LISTA_HISTORICA se encuentra en sesion
   		gridItems = getGridItems(request);
    	request.getSession().setAttribute(LISTA_CLIENTES_RIESGO_CREDITICIO_HIST, gridItems);
    	mv = new ModelAndView(VISTA_CLIENTES_RIESGO_CREDITICIO_HIST);	
    	mv.addObject(BEAN_CLIENTES_RIESGO_CREDITICIO_HIST, busquedaRiesgoCrediticioHist);
   	}catch(Exception e){
   		LOG.error(e.getMessage(), e);
        request.setAttribute("errorSpecified", e.getMessage());
        request.setAttribute("error", stackTraceToString(e));
   		return new ModelAndView(VISTA_ERROR);
   	}
   	return mv;
   }       
}
