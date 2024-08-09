package ar.com.latam.creditCore.validador;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import ar.com.latam.creditCore.dto.AltaManualClienteRiesgoCrediticioDTO;
import ar.com.latam.creditCore.services.interfaz.IRiesgoCrediticioService;
import ar.com.latam.creditCore.utils.CUIT;
import ar.com.latam.util.Util;

/**
 * 
 * @author diana.karina.rojas
 *
 */
public class AltaManualRiesgoCrediticioValidador implements Validator{
	
	private static final Logger log = Logger.getLogger(AltaManualRiesgoCrediticioValidador.class);
	
	private static final String CAMPO_TIPO_DOC = "tipoDoc";
	private static final String CAMPO_NRO_DOC = "nroDoc";
	private static final String CAMPO_GENERO = "genero";
	private static final String CAMPO_SCORE_MANUAL = "scoreManual";
	private static final String ERROR_TIPO_DOC = "error.campoTipoDoc.vacio";	
	private static final String ERROR_NRO_DOC_VACIO = "error.campoNroDoc.vacio";
	private static final String ERROR_CUIT_CUIL_INVALIDO= "error.campoNroDoc.cuit.cuil.invalido";
	private static final String ERROR_GENERO_VACIO = "error.genero.vacio";
	private static final String ERROR_NRO_DOC_NO_VALIDO = "error.campoNroDoc.noValido";
	private static final String ERROR_NRO_DOC_EXISTENTE = "error.campoNroDoc.existe";
	private static final String ERROR_QUERY_NRO_DOC_EXISTENTE = "error.campoNroDoc.query.existe";	
	private static final String ERROR_SCORE_MANUAL_VACIO = "error.campoScoreManual.vacio";
	private static final String ERROR_SCORE_MANUAL_NO_VALIDO = "error.campoScoreManual.noValido";
    
    IRiesgoCrediticioService riesgoCrediticioService;
    private Map<String, String> conversionTipoDocumento = new HashMap<String, String>();
	
	public boolean supports(Class<?> clase) {
		return AltaManualClienteRiesgoCrediticioDTO.class.isAssignableFrom(clase);
	}

	public void validate(Object target, Errors errors){
		AltaManualClienteRiesgoCrediticioDTO beanCliente=(AltaManualClienteRiesgoCrediticioDTO) target;
		log.info("Se ingreso al metodo validate de AltaManualRiesgoCrediticioValidador");
		boolean validarDocumento = true;
		String tipoDoc = validarNulos(beanCliente.getTipoDoc()).trim();
		String nroDoc = validarNulos(beanCliente.getNroDoc()).trim();
		String nroDocFormato = validarNulos(beanCliente.getNroDocFormato()).trim();
		String cargaGenero = validarNulos(beanCliente.getCargaGenero()).trim();
		String genero = validarNulos(beanCliente.getGenero()).trim();
		String scoreManualString = validarNulos(beanCliente.getScoreManual()).trim();
		
		try{
			if (Util.EMPTY_CHARACTER.equals(tipoDoc)){
				errors.rejectValue(CAMPO_TIPO_DOC, ERROR_TIPO_DOC);
				validarDocumento = false;
			}
			
			if (Util.EMPTY_CHARACTER.equals(nroDoc)){
				errors.rejectValue(CAMPO_NRO_DOC, ERROR_NRO_DOC_VACIO);
				validarDocumento = false;
			}
			
		
			
			if(validarDocumento == true){
				if(Util.CU.equals(tipoDoc) || Util.CUIT.equals(tipoDoc) || Util.CL.equals(tipoDoc) || Util.CUIL.equals(tipoDoc)){
					if (nroDocFormato.length()!= 13 || CUIT.validar(nroDocFormato) == false){
						errors.rejectValue(CAMPO_NRO_DOC, ERROR_CUIT_CUIL_INVALIDO);
						validarDocumento = false;
					}
				}
			}
			
			if(validarDocumento == true){
				try{
					if(Util.DU.equals(tipoDoc) || Util.DNI.equals(tipoDoc) || Util.LC.equals(tipoDoc) || Util.LE.equals(tipoDoc)){
						Long.parseLong(nroDoc);
					}					
					try{
						conversionTipoDocumento.clear();
			   			riesgoCrediticioService.cargarConversionTipoDocumento(conversionTipoDocumento);
						if (riesgoCrediticioService.existeNroDocumento(conversionTipoDocumento.get(beanCliente.getTipoDoc()), nroDoc)){
							errors.rejectValue(CAMPO_NRO_DOC, ERROR_NRO_DOC_EXISTENTE);
						}
					}catch(Exception e){
						errors.rejectValue(CAMPO_NRO_DOC, ERROR_QUERY_NRO_DOC_EXISTENTE);
						log.info(e);
					}
				}catch(NumberFormatException e){
					errors.rejectValue(CAMPO_NRO_DOC, ERROR_NRO_DOC_NO_VALIDO);
				}
			}
			
			if (Util.EMPTY_CHARACTER.equals(scoreManualString)){
				errors.rejectValue(CAMPO_SCORE_MANUAL, ERROR_SCORE_MANUAL_VACIO);			
			}else{
				try{
					int scoreManual = Integer.parseInt(scoreManualString);
					if (scoreManual < 0 || scoreManual > 999){
						errors.rejectValue(CAMPO_SCORE_MANUAL, ERROR_SCORE_MANUAL_NO_VALIDO);
					}
				}catch(NumberFormatException e){
					errors.rejectValue(CAMPO_SCORE_MANUAL, ERROR_SCORE_MANUAL_NO_VALIDO);
				}
			}
		}catch(Exception e){
			log.info(e);
		}
	}

	public IRiesgoCrediticioService getRiesgoCrediticioService() {
		return riesgoCrediticioService;
	}

	public void setRiesgoCrediticioService(
			IRiesgoCrediticioService riesgoCrediticioService) {
		this.riesgoCrediticioService = riesgoCrediticioService;
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
