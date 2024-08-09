package ar.com.latam.creditCore.controller;

import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.jmesa.limit.Limit;
import org.jmesa.model.PageItems;
import org.jmesa.model.TableModelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import ar.com.latam.creditCore.dto.TipoDocumentoDTO;
import ar.com.latam.creditCore.services.interfaz.ITiposDocumentoService;
import ar.com.latam.util.Util;

/**
 * 
 * @author diana.karina.rojas
 *
 */
@Controller
public class TiposDocumentoController {

	private static final Logger LOG = Logger.getLogger(TiposDocumentoController.class);
	
	private static final String VISTA_TIPOS_DOCUMENTOS = "tiposDocumento";
	private static final String VISTA_EDITAR_REGISTRO_TIPO_DOC = "editarRegistroTipoDoc";
	private static final String VISTA_ALTA_REGISTRO_TIPO_DOC = "altaRegistroTipoDoc";
	private static final String VISTA_ERROR ="errorPage";
	
	private static final String BEAN_REGISTRO = "registroTipoDocBean";
	private static final String EDICION_REGISTRO = "editarTipoDocBean";
	private static final String ALTA_REGISTRO = "altaTipoDocBean";
	
	private static final String LISTA_TIPOS_DOCUMENTO = "listaTiposDoc";
	
	private static final String TABLE_MODEL = "tablaTiposDoc";//Es el mismo id que tiene la tabla en riesgoCrediticio.jsp
	private static final String TIPOS_DOC_TOKEN = "TiposDocToken";
	
	private static final String CAMPO_TIPO_DOC_DW = "tipoDocDW";
	private static final String ERROR_TIPO_DOC_ALTA_VIOLACION_PRIMARY_KEY = "altaRegistroTipoDoc.error.violacionPrimaryKey";	
	private static final String ERROR_TIPO_DOC_MOD_VIOLACION_PRIMARY_KEY = "altaRegistroTipoDoc.error.violacionPrimaryKey";
	private static final String ERROR_TIPO_DOC_DW = "editarRegistroTipoDoc.error.tipoDocDW";	
	private static final String CAMPO_TIPO_DOC_AM_DOC = "tipoDocAmdoc";
	private static final String ERROR_TIPO_DOC_AM_DOC = "editarRegistroTipoDoc.error.tipoDocAmdoc";
	private static final String CAMPO_CARGA_GENERO = "cargaGenero";
	private static final String ERROR_CARGA_GENERO = "editarRegistroTipoDoc.error.cargaGenero";
	private static final String ERROR_DUPLICATE_KEY = "DuplicateKeyException";
	
	@Autowired
    ITiposDocumentoService tiposDocumentoService;	
    /*@Autowired
    private Validator altaTiposDocValidador; 
    @Autowired
    private Validator modificacionTiposDocValidador;*/
	  
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
   
   @RequestMapping("/tiposDocumento")	
   public ModelAndView tiposDocumento(HttpServletRequest request) throws Exception {
   	ModelAndView mv = null;
   	LOG.info("tiposDocumento");
   	Collection<TipoDocumentoDTO> gridItems; 
   	try{	
			request.getSession().removeAttribute(LISTA_TIPOS_DOCUMENTO);
			request.getSession().removeAttribute(TIPOS_DOC_TOKEN);
	    	List<TipoDocumentoDTO> listaTiposDocumentos = tiposDocumentoService.obtenerTiposDocumento();  		   			
	    	TipoDocumentoDTO tipoDocumento = new TipoDocumentoDTO();
	    	if (listaTiposDocumentos == null){
	    		gridItems = null;
	    	}
	    	else{
		        request.getSession().setAttribute(TIPOS_DOC_TOKEN, listaTiposDocumentos);	       	        
		        gridItems = getGridItems(request);	    			    		
	    	}
	    	request.getSession().setAttribute(LISTA_TIPOS_DOCUMENTO, gridItems);
	    	mv = new ModelAndView(VISTA_TIPOS_DOCUMENTOS);
	    	mv.addObject(BEAN_REGISTRO, tipoDocumento);
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
  private Collection<TipoDocumentoDTO> getGridItems(
                  final HttpServletRequest request) {

      return (Collection<TipoDocumentoDTO>) TableModelUtils.getItems(TABLE_MODEL,
          request, new PageItems() {

              public int getTotalRows(Limit limit) {

                  Collection<TipoDocumentoDTO> tiposDocTotal =
                      (Collection<TipoDocumentoDTO>) request.getSession()
                          .getAttribute(TIPOS_DOC_TOKEN);
                  return tiposDocTotal.size();
              }

              public Collection<TipoDocumentoDTO> getItems(Limit limit) {

                  int rowStart = limit.getRowSelect().getRowStart();
                  int rowEnd = limit.getRowSelect().getRowEnd();
                  List<TipoDocumentoDTO> tiposDocItems =
                      (List<TipoDocumentoDTO>) request.getSession().getAttribute(
                    		  TIPOS_DOC_TOKEN);
                  return tiposDocItems.subList(rowStart, rowEnd);
              }
          });
  }
  
  @RequestMapping("/paginarTiposDoc")	
  public ModelAndView paginarTiposDoc(HttpServletRequest request) throws Exception {	
  	LOG.info("paginarTiposDoc");
  	ModelAndView mv = null;
  	TipoDocumentoDTO tipoDocumento = new TipoDocumentoDTO();
  	Collection<TipoDocumentoDTO> gridItems; 
  	try{
  		request.getSession().removeAttribute(LISTA_TIPOS_DOCUMENTO);
  		//TIPOS_DOC_TOKEN se encuentra en sesion    		
  		gridItems = getGridItems(request);
    	request.getSession().setAttribute(LISTA_TIPOS_DOCUMENTO, gridItems);
    	mv = new ModelAndView(VISTA_TIPOS_DOCUMENTOS);	
    	mv.addObject(BEAN_REGISTRO, tipoDocumento);
  	}catch(Exception e){
  		LOG.error(e.getMessage(), e);
          request.setAttribute("errorSpecified", e.getMessage());
          request.setAttribute("error", stackTraceToString(e));
  		return new ModelAndView(VISTA_ERROR);
  	}
  	return mv;
  }  
  
  @RequestMapping("/editarRegistroTipoDoc")	
  public ModelAndView editarRegistroTipoDoc(HttpServletRequest request, @ModelAttribute("registroTipoDocBean") TipoDocumentoDTO tipoDocumento) throws Exception {
  	ModelAndView mv = null;
  	LOG.info("editarRegistroTipoDoc");
  	try{	
	    mv = new ModelAndView(VISTA_EDITAR_REGISTRO_TIPO_DOC);
	    mv.addObject(EDICION_REGISTRO, tipoDocumento);
  	}catch(Exception e){
  		LOG.error(e.getMessage(), e);
        request.setAttribute("errorSpecified", e.getMessage());
        request.setAttribute("error", stackTraceToString(e));
  		return new ModelAndView(VISTA_ERROR);
  	}
	    	return mv; 
  }
  
  @RequestMapping("/actualizarRegistroTipoDoc")	
  public ModelAndView actualizarRegistroTipoDoc(HttpServletRequest request, @ModelAttribute("editarTipoDocBean") TipoDocumentoDTO tipoDocumento, BindingResult result) throws Exception {
  	ModelAndView mv = null;   	
  	LOG.info("actualizarRegistroTipoDoc");
  	try{
  		String tipoDocDW = validarNulos(tipoDocumento.getTipoDocDW()).trim();
  		String tipoDocAmdoc = validarNulos(tipoDocumento.getTipoDocAmdoc()).trim();
  		String cargaGenero = validarNulos(tipoDocumento.getCargaGenero()).trim();
  		
  		if(Util.EMPTY_CHARACTER.equals(tipoDocDW) || Util.EMPTY_CHARACTER.equals(tipoDocAmdoc) || Util.EMPTY_CHARACTER.equals(cargaGenero)){
  			
			if(Util.EMPTY_CHARACTER.equals(tipoDocDW)){
				result.rejectValue(CAMPO_TIPO_DOC_DW,ERROR_TIPO_DOC_DW);
			}
			if(Util.EMPTY_CHARACTER.equals(tipoDocAmdoc)){
				result.rejectValue(CAMPO_TIPO_DOC_AM_DOC,ERROR_TIPO_DOC_AM_DOC);
			}
			if(Util.EMPTY_CHARACTER.equals(cargaGenero)){
				result.rejectValue(CAMPO_CARGA_GENERO,ERROR_CARGA_GENERO);
			}			
			mv=new ModelAndView(VISTA_EDITAR_REGISTRO_TIPO_DOC);
			mv.addObject(EDICION_REGISTRO, tipoDocumento);
			return mv;
  		}
  		
		String userId = Util.modifUser(request.getHeader(Util.UCID));
		LOG.info("USUARIO: " + userId);
		tipoDocumento.setUserCarga(userId);
		try{
			tiposDocumentoService.modificacionTipoDoc(tipoDocumento);
		}catch(Exception e){
			String error = e.getCause().toString();
			LOG.error(e.getMessage(), e);
			if (error.contains(ERROR_DUPLICATE_KEY)){
				result.rejectValue(CAMPO_TIPO_DOC_DW,ERROR_TIPO_DOC_MOD_VIOLACION_PRIMARY_KEY);
				mv=new ModelAndView(VISTA_EDITAR_REGISTRO_TIPO_DOC);
				mv.addObject(EDICION_REGISTRO, tipoDocumento);
				return mv;
			}else{				
		        request.setAttribute("errorSpecified", e.getMessage());
		        request.setAttribute("error", stackTraceToString(e));
		  		return new ModelAndView(VISTA_ERROR);
			}
		
	  	}
		return tiposDocumento(request);
  	}catch(Exception e){
  		LOG.error(e.getMessage(), e);
        request.setAttribute("errorSpecified", e.getMessage());
        request.setAttribute("error", stackTraceToString(e));
  		return new ModelAndView(VISTA_ERROR);
  	}
  } 
  
  @RequestMapping("/formAltaTipoDoc")	
  public ModelAndView formAltaTipoDoc(HttpServletRequest request) throws Exception {
  	ModelAndView mv = null;
  	LOG.info("formAltaTipoDoc");
  	try{	
  		TipoDocumentoDTO tipoDocumento = new TipoDocumentoDTO();
    	mv = new ModelAndView(VISTA_ALTA_REGISTRO_TIPO_DOC);
    	mv.addObject(ALTA_REGISTRO, tipoDocumento);
	}catch(Exception e){
		LOG.error(e.getMessage(), e);
		request.setAttribute("errorSpecified", e.getMessage());
		request.setAttribute("error", stackTraceToString(e));
		return new ModelAndView(VISTA_ERROR);
	}
    return mv; 
  }   
  
  @RequestMapping("/darAltaRegistroTipoDoc")	
  public ModelAndView darAltaRegistroTipoDoc(HttpServletRequest request, @ModelAttribute("altaTipoDocBean") TipoDocumentoDTO tipoDocumento, BindingResult result) throws Exception {
  	ModelAndView mv = null;   	
  	LOG.info("darAltaRegistroTipoDoc");
  	try{
  		String tipoDocDW = validarNulos(tipoDocumento.getTipoDocDW()).trim();
  		String tipoDocAmdoc = validarNulos(tipoDocumento.getTipoDocAmdoc()).trim();
  		String cargaGenero = validarNulos(tipoDocumento.getCargaGenero()).trim();
  		
  		if(Util.EMPTY_CHARACTER.equals(tipoDocDW) || Util.EMPTY_CHARACTER.equals(tipoDocAmdoc) || Util.EMPTY_CHARACTER.equals(cargaGenero)){
  			
			if(Util.EMPTY_CHARACTER.equals(tipoDocDW)){
				result.rejectValue(CAMPO_TIPO_DOC_DW,ERROR_TIPO_DOC_DW);
			}
			if(Util.EMPTY_CHARACTER.equals(tipoDocAmdoc)){
				result.rejectValue(CAMPO_TIPO_DOC_AM_DOC,ERROR_TIPO_DOC_AM_DOC);
			}
			if(Util.EMPTY_CHARACTER.equals(cargaGenero)){
				result.rejectValue(CAMPO_CARGA_GENERO,ERROR_CARGA_GENERO);
			}
			
			mv=new ModelAndView(VISTA_ALTA_REGISTRO_TIPO_DOC);
			mv.addObject(ALTA_REGISTRO, tipoDocumento);
			return mv;
  		}
  		
		String userId = Util.modifUser(request.getHeader(Util.UCID));
		LOG.info("USUARIO: " + userId);
		tipoDocumento.setUserCarga(userId);
		try{
		tiposDocumentoService.altaTipoDoc(tipoDocumento);
		}catch(Exception e){
			String error = e.getCause().toString();
			LOG.error(e.getMessage(), e);
			if (error.contains(ERROR_DUPLICATE_KEY)){
				result.rejectValue(CAMPO_TIPO_DOC_DW,ERROR_TIPO_DOC_ALTA_VIOLACION_PRIMARY_KEY);
				mv=new ModelAndView(VISTA_ALTA_REGISTRO_TIPO_DOC);
				mv.addObject(ALTA_REGISTRO, tipoDocumento);
				return mv;
			}else{				
		        request.setAttribute("errorSpecified", e.getMessage());
		        request.setAttribute("error", stackTraceToString(e));
		  		return new ModelAndView(VISTA_ERROR);
			}
		
	  	}
		return tiposDocumento(request);
  	}catch(Exception e){
  		LOG.error(e.getMessage(), e);
        request.setAttribute("errorSpecified", e.getMessage());
        request.setAttribute("error", stackTraceToString(e));
  		return new ModelAndView(VISTA_ERROR);
  	}
  }   
  
  @RequestMapping("/eliminarRegistroTipoDoc")	
  public ModelAndView eliminarRegistroTipoDoc(HttpServletRequest request, @ModelAttribute("registroTipoDocBean") TipoDocumentoDTO tipoDocumento) throws Exception {
  	LOG.info("eliminarRegistroTipoDoc");
  	try{
		tiposDocumentoService.eliminarTipoDoc(tipoDocumento);
		return tiposDocumento(request);
  	}catch(Exception e){
  		LOG.error(e.getMessage(), e);
        request.setAttribute("errorSpecified", e.getMessage());
        request.setAttribute("error", stackTraceToString(e));
  		return new ModelAndView(VISTA_ERROR);
  	}
  }  
	/**
	 *
	 * @param dato
	 * @return
	 */
	public String validarNulos(String dato){


		if (dato == null)
			return "";
		else
			return dato;
	}  
  
}
