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
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import ar.com.latam.creditCore.dto.NovedadDTO;
import ar.com.latam.creditCore.services.interfaz.INovedadesService;

/**
 * 
 * @author diana.karina.rojas
 *
 */
@Controller
public class NovedadesController {
	
	private static final Logger LOG = Logger.getLogger(NovedadesController.class);	

	private static final String VISTA_NOVEDADES = "novedades";	
	private static final String VISTA_ERROR ="errorPage";
	
	/*private static final String BEAN_BUSQUEDA_NOVEDADES = "busquedaNovedadesBean";*/
	
	private static final String LISTA_NOVEDADES = "listaNovedades";
	
	private static final String TABLE_MODEL = "tablaNovedades";//Es el mismo id que tiene en novedades.jsp
	private static final String NOVEDADES_TOKEN = "novedadesToken";
	
    @Autowired
    INovedadesService novedadesService;	
    
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
  private Collection<NovedadDTO> getGridItems(
                  final HttpServletRequest request) {

      return (Collection<NovedadDTO>) TableModelUtils.getItems(TABLE_MODEL,
          request, new PageItems() {

              public int getTotalRows(Limit limit) {

                  Collection<NovedadDTO> novedadesTotal =
                      (Collection<NovedadDTO>) request.getSession()
                          .getAttribute(NOVEDADES_TOKEN);
                  return novedadesTotal.size();
              }

              public Collection<NovedadDTO> getItems(Limit limit) {

                  int rowStart = limit.getRowSelect().getRowStart();
                  int rowEnd = limit.getRowSelect().getRowEnd();
                  List<NovedadDTO> novedadesItems =
                      (List<NovedadDTO>) request.getSession().getAttribute(
                    		  NOVEDADES_TOKEN);
                  return novedadesItems.subList(rowStart, rowEnd);
              }
          });
  }
   
   @RequestMapping("/novedades")	
   public ModelAndView novedades(HttpServletRequest request) throws Exception {
   	ModelAndView mv = null;
   	LOG.info("novedades");
   	try{	
   		List<NovedadDTO> listaNovedades;
   		Collection<NovedadDTO> gridItems;
		request.getSession().removeAttribute(LISTA_NOVEDADES);
		request.getSession().removeAttribute(NOVEDADES_TOKEN);
		/*NovedadDTO busquedaNovedad = new NovedadDTO();*/    	
    	listaNovedades = novedadesService.consultaTodasNovedades();
    	if (listaNovedades == null){
    		gridItems = null;
    	}else{
	        request.getSession().setAttribute(NOVEDADES_TOKEN, listaNovedades);	       	        
	        gridItems = getGridItems(request);	    			    		
    	}    	
    	request.getSession().setAttribute(LISTA_NOVEDADES, gridItems);
    	mv = new ModelAndView(VISTA_NOVEDADES);
    	//mv.addObject(BEAN_BUSQUEDA_NOVEDADES, busquedaNovedad);
   	}catch(Exception e){
   		LOG.error(e.getMessage(), e);
        request.setAttribute("errorSpecified", e.getMessage());
        request.setAttribute("error", stackTraceToString(e));
   		return new ModelAndView(VISTA_ERROR);
   	}
	return mv; 
   }
   
   @RequestMapping("/paginarNovedades")	
   public ModelAndView paginarNovedades(HttpServletRequest request) throws Exception {	
   	LOG.info("paginarNovedades");
   	ModelAndView mv = null;
   	Collection<NovedadDTO> gridItems; 
   	try{
   		request.getSession().removeAttribute(LISTA_NOVEDADES);
   		//CLIENTES_TOKEN se encuentra en sesion    		
   		gridItems = getGridItems(request);
    	request.getSession().setAttribute(LISTA_NOVEDADES, gridItems);
    	mv = new ModelAndView(VISTA_NOVEDADES);	
    	//mv.addObject(BEAN_BUSQUEDA_NOVEDADES, busquedaNovedad);
   	}catch(Exception e){
   		LOG.error(e.getMessage(), e);
        request.setAttribute("errorSpecified", e.getMessage());
        request.setAttribute("error", stackTraceToString(e));
   		return new ModelAndView(VISTA_ERROR);
   	}
   	return mv;
   }    
   
}
