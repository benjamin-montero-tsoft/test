package ar.com.latam.credit.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 
 * @author diana.karina.rojas
 *
 */
@Controller
public class CreditController {
	/** The Constant log. */
    private static final Logger LOG = Logger.getLogger(CreditController.class);
	private static final String ERROR_VIEW = "errorPage";	
	private static final String LOG_OUT = "logOut";
	private static final String BLANK = "tiles/blank";
	
    /**
     * Inits the binder.
     *
     * @param binder the binder
     */
    @InitBinder
    protected void initBinder(WebDataBinder binder) {
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
        return new ModelAndView(ERROR_VIEW);
    }
	
	@RequestMapping("/jsp/logOut")	
	public ModelAndView logOut(HttpServletRequest request, HttpServletResponse response) {
		request.getSession().invalidate();
		Cookie[] cookies = request.getCookies();
		LOG.info("cookies: " + cookies.toString() );
        for (Cookie cookie : cookies) {
            cookie.setMaxAge(0);
            cookie.setValue(null);
            cookie.setPath("/");
            response.addCookie(cookie);
            LOG.info("Borrando cookie: " + cookie.getName() + "-"+cookie.getMaxAge());
			}
        
		return new ModelAndView(LOG_OUT);
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
}
