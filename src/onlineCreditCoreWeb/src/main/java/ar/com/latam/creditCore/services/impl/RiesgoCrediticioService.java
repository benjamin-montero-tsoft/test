package ar.com.latam.creditCore.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import ar.com.latam.creditCore.dao.interfaz.IRiesgoCrediticioDAO;
import ar.com.latam.creditCore.dto.AltaManualClienteRiesgoCrediticioDTO;
import ar.com.latam.creditCore.dto.ClienteRiesgoCrediticioDTO;
import ar.com.latam.creditCore.dto.ExportCSVDTO;
import ar.com.latam.creditCore.dto.RiesgoCrediticioManualDTO;
import ar.com.latam.creditCore.dto.TipoDocumentoDTO;
import ar.com.latam.creditCore.services.interfaz.IRiesgoCrediticioService;
import ar.com.latam.creditCore.utils.SFTPCliente;
import ar.com.latam.util.Util;

/**
 * 
 * @author diana.karina.rojas
 *
 */
public class RiesgoCrediticioService implements IRiesgoCrediticioService {
	
	private static final Logger LOG = Logger.getLogger(RiesgoCrediticioService.class);
	
    /** Titulo Archivo CSV de Modificacion.**/
    private static final String PREFIJO_TITULO_CSV_MODIFICACION = "BatchUpdateCustomerSegmentScore_";
    
    private static final String PATH_ARCHIVO_CVS_PARAMETER = "pathArchivoCSVModif";
    private static final String HOST_AMDOCS = "hostAmdocs";
    private static final String PORT_AMDOCS = "portAmdocs";
    private static final String USER_SFTP = "userAmdocs";
    private static final String PASS_SFTP = "pwdAmdocs";
    private static final String REMOTE_DIR="remoteDirectory";

    
	IRiesgoCrediticioDAO riesgoCrediticioDAO;


	public IRiesgoCrediticioDAO getRiesgoCrediticioDAO() {
		return riesgoCrediticioDAO;
	}

	public void setRiesgoCrediticioDAO(IRiesgoCrediticioDAO riesgoCrediticioDAO) {
		this.riesgoCrediticioDAO = riesgoCrediticioDAO;
	}
	
	public void cargarConversionTipoDocumento(Map<String,String> conversionTipoDocumento) throws Exception{
		try{
			riesgoCrediticioDAO.cargarConversionTipoDocumento(conversionTipoDocumento);
		}catch (Exception e){
			LOG.info("Error en cargarConversionTipoDocumento de riesgoCrediticioDAO");
			throw e;
		}		
	}
	
	public List<TipoDocumentoDTO> obtenerTiposDocumento() throws Exception{
		try{
			return riesgoCrediticioDAO.obtenerTiposDocumento();
		}catch (Exception e){
			LOG.info("Error en obtenerTiposDocumento de riesgoCrediticioDAO");
			throw e;
		}			
	}
	
	public boolean existeNroDocumento(String tipoDoc, String nroDoc) throws Exception{
		try{
			return riesgoCrediticioDAO.existeNroDocumento(tipoDoc, nroDoc);
		}catch (Exception e){
			LOG.info("Error en existeNroDocumento de riesgoCrediticioDAO");
			throw e;
		}
	}
	
	public void altaClienteRiesgoCrediticio(AltaManualClienteRiesgoCrediticioDTO altaManualClienteRiesgoCrediticio)throws Exception{
		try{
			RiesgoCrediticioManualDTO riesgoCrediticioManual = new RiesgoCrediticioManualDTO();
			riesgoCrediticioManual.setTipoDoc(altaManualClienteRiesgoCrediticio.getTipoDoc());
			riesgoCrediticioManual.setNroDoc(altaManualClienteRiesgoCrediticio.getNroDoc());
			riesgoCrediticioManual.setGenero(altaManualClienteRiesgoCrediticio.getGenero());
			riesgoCrediticioManual.setScoreManual(altaManualClienteRiesgoCrediticio.getScoreManual());
			riesgoCrediticioManual.setFechaCargaScoreManual(Util.SYSDATE);
			riesgoCrediticioManual.setUsuarioCargaCambio(altaManualClienteRiesgoCrediticio.getUsuarioAplicacion());
			
			riesgoCrediticioDAO.altaClienteRiesgoCrediticioManual(riesgoCrediticioManual);
		}catch(Exception e){
			LOG.info("Error en altaClienteRiesgoCrediticioManual de riesgoCrediticioDAO");
			LOG.info(e);
			throw e;
		}
		try{
			ClienteRiesgoCrediticioDTO clienteRiesgoCrediticio = new ClienteRiesgoCrediticioDTO();
			clienteRiesgoCrediticio.setTipoDoc(altaManualClienteRiesgoCrediticio.getTipoDoc());
			clienteRiesgoCrediticio.setNroDoc(altaManualClienteRiesgoCrediticio.getNroDoc());
			clienteRiesgoCrediticio.setGenero(altaManualClienteRiesgoCrediticio.getGenero());
			clienteRiesgoCrediticio.setScoreManual(altaManualClienteRiesgoCrediticio.getScoreManual());
			clienteRiesgoCrediticio.setFechaCargaScoreManual(Util.SYSDATE);
			clienteRiesgoCrediticio.setUsuarioCargaCambio(altaManualClienteRiesgoCrediticio.getUsuarioAplicacion());
			clienteRiesgoCrediticio.setFechaUltimoUpdate(Util.SYSDATE);			
			
			riesgoCrediticioDAO.altaClienteRiesgoCrediticioFinal(clienteRiesgoCrediticio);
		}catch(Exception e3){
			LOG.info("Error en altaClienteRiesgoCrediticioFinal de riesgoCrediticioDAO");
			LOG.info(e3);
			throw e3;
		}
	}
	
	public List<ClienteRiesgoCrediticioDTO> obtenerClientesRiesgoCrediticio(String tipoDoc, String nroDoc, String genero, Map<String,String> conversionTipoDocumento) throws Exception{
		try{
			return riesgoCrediticioDAO.obtenerClientesRiesgoCrediticio(tipoDoc, nroDoc, genero, conversionTipoDocumento);
		}catch (Exception e){
			LOG.info("Error en obtenerClientesRiesgoCrediticio de riesgoCrediticioDAO");
			LOG.info(e);
			throw e;
		}
	}
	
	@Transactional (rollbackFor = Exception.class) 
	public List<ClienteRiesgoCrediticioDTO> modificacionScoreClienteRiesgoCrediticio(ClienteRiesgoCrediticioDTO clienteRiesgoCrediticio, Map<String,String> conversionTipoDocumento)throws Exception{
		
		List<ClienteRiesgoCrediticioDTO> listaClientesRiesgoCrediticioCSV = new ArrayList<ClienteRiesgoCrediticioDTO>();
		
		try{
			//Modifico el registro Seleccionado
			modificacionClienteRiesgoCrediticio(clienteRiesgoCrediticio, conversionTipoDocumento);
			//Agrego el registro Seleccionado a la lista del archivo CSV
			listaClientesRiesgoCrediticioCSV.add(clienteRiesgoCrediticio);
			
			/**
			 * Una Persona Fisica tiene DNI, CUIL y CUIT (el cuil y cuit son el mismo número)
			 * 
			 * Una empresa Juridica (empresa) no tiene DNI ni CUIL, solo tiene CUIT
			 */
			
		
			
			generarArchivosCSVModificacion(listaClientesRiesgoCrediticioCSV);
			
		}catch (Exception e){
			LOG.info("Error en modificacionScoreClienteRiesgoCrediticio de RiesgoCrediticioService");
			LOG.info(e);
			throw e;
		}
		return listaClientesRiesgoCrediticioCSV;
	}

	
	public void modificacionClienteRiesgoCrediticio(ClienteRiesgoCrediticioDTO clienteRiesgoCrediticio, Map<String,String> conversionTipoDocumento)throws Exception{		
		
		RiesgoCrediticioManualDTO riesgoCrediticioManual = new RiesgoCrediticioManualDTO();
		try{
		riesgoCrediticioManual.setTipoDoc(clienteRiesgoCrediticio.getTipoDoc());
		riesgoCrediticioManual.setNroDoc(clienteRiesgoCrediticio.getNroDoc());
		riesgoCrediticioManual.setGenero(clienteRiesgoCrediticio.getGenero());
		riesgoCrediticioManual.setScoreManual(clienteRiesgoCrediticio.getScoreManual());
		riesgoCrediticioManual.setFechaCargaScoreManual(Util.SYSDATE);
		riesgoCrediticioManual.setUsuarioCargaCambio(clienteRiesgoCrediticio.getUsuarioCargaCambio());
		}catch(Exception e1){
			LOG.info("Error en modificacionClienteRiesgoCrediticio de RiesgoCrediticioService");
			LOG.info(e1);
			throw e1;
		}
		
		int registrosActualizados = 0;
		
		try{						
			registrosActualizados = riesgoCrediticioDAO.modificacionClienteRiesgoCrediticioManual(riesgoCrediticioManual);
		}catch(Exception e2){
			LOG.info("Error en modificacionClienteRiesgoCrediticioManual de riesgoCrediticioDAO");
			LOG.info(e2);
			throw e2;
		}
		
		try{
 			/*Si el registro de la tabla RC_RIESGO_CREDITICIO_MANUAL se actualizo es debido a que el alta del score se realizo por Web 
			 * (que crea el registro en la tabla RC_RIESGO_CREDITICIO_MANUAL). 
			 * Si el alta se hizo por Batch, el registro en la tabla RC_RIESGO_CREDITICIO_MANUAL no existe, entonces se debe hacer un insert
			 */	
			if(registrosActualizados == 0){//no se actualizo, entonces inserto el registro
				LOG.info("El registro no existe, por lo tanto no se actualizo. Entonces inserto el registro");
				riesgoCrediticioDAO.altaClienteRiesgoCrediticioManual(riesgoCrediticioManual);
			}			
		}catch(Exception e3){
			LOG.info("Error en altaClienteRiesgoCrediticioManual de riesgoCrediticioDAO");
			LOG.info(e3);
			throw e3;
		}		

		try{
			//Se copia los valores del registro de la tabla RC_RIESGO_CREDITICIO_FINAL en el historico RC_RIESGO_CREDITICIO_HIST:
			riesgoCrediticioDAO.altaClienteRiesgoCrediticioHistorico(clienteRiesgoCrediticio, conversionTipoDocumento);
		}catch(Exception e4){
			LOG.info("Error en altaClienteRiesgoCrediticioHistorico de riesgoCrediticioDAO");
			LOG.info(e4);
			throw e4;
		}		
		try{
			//Se actualiza la tabla RC_RIESGO_CREDITICIO_FINAL:
			clienteRiesgoCrediticio.setFechaCargaScoreManual(Util.SYSDATE);
			clienteRiesgoCrediticio.setFechaUltimoUpdate(Util.SYSDATE);
			
			riesgoCrediticioDAO.modificacionClienteRiesgoCrediticioFinal(clienteRiesgoCrediticio);
		}catch(Exception e5){
			LOG.info("Error en modificacionClienteRiesgoCrediticioFinal de riesgoCrediticioDAO");
			LOG.info(e5);
			throw e5;
		}
	}
	
	public List<ClienteRiesgoCrediticioDTO> obtenerReporteRiesgosCrediticiosManuales(ExportCSVDTO dto) throws Exception{
		
		try{
			dto.setConversionTipoDocumento_DW_AMDOCS(riesgoCrediticioDAO.cargarConversionTipoDocumentoBajadaManual());
			return riesgoCrediticioDAO.obtenerReporteRiesgosCrediticiosManuales(dto);
		}catch (Exception e){
			LOG.info("Error en obtenerReporteRiesgosCrediticiosManuales de riesgoCrediticioDAO");
			LOG.info(e);
			throw e;
		}		
	}

	public void generarArchivosCSVModificacion(List<ClienteRiesgoCrediticioDTO> listaClienteRiesgoCrediticio) throws Exception{
		try{
			
		String nombreArchivocsv =PREFIJO_TITULO_CSV_MODIFICACION+ SFTPCliente.getDateReports() + ".CSV";
		String nombreArchivotrig =PREFIJO_TITULO_CSV_MODIFICACION+ SFTPCliente.getDateReports() + ".TRIG";

		LOG.info("ARCHIVO CSV: "+ nombreArchivocsv);
		LOG.info("ARCHIVO TRIG: "+ nombreArchivotrig);
		StringBuilder stringBuilder = new StringBuilder();
		/*
		 * Ejemplo 1 Persona Fisica:
		 * DNI,11111111,F,,100,,,
		 * CUIL,20111111120,M,,100,,,
		 * CUIT,20111111120,M,,100,,,
		 * 
		 * Ejemplo 2 Persona Fisica:
		 * CUIL,20111111120,M,,100,,,
		 * DNI,11111111,F,,100,,,
		 * CUIT,20111111120,M,,100,,,
		 * 
		 * Ejemplo 3 Persona Fisica:
		 * CUIT,20111111120,M,,100,,,
		 * DNI,11111111,F,,100,,,
		 * CUIL,20111111120,M,,100,,,
		 * 
		 * Una empresa Juridica (empresa) no tiene DNI ni CUIL, solo tiene CUIT
		 * */
		if(listaClienteRiesgoCrediticio != null){
			if(listaClienteRiesgoCrediticio.size() > 0){
				
				stringBuilder.append(Util.generarCabecera());
				
				for(int i=0; i<listaClienteRiesgoCrediticio.size(); i++){
					stringBuilder.append(listaClienteRiesgoCrediticio.get(i).getTipoDocAMDOCS() + ",");
					stringBuilder.append(listaClienteRiesgoCrediticio.get(i).getNroDoc() + ",");
					stringBuilder.append(Util.validarGenero(listaClienteRiesgoCrediticio.get(i).getGenero()) + ",NULL,");
					stringBuilder.append(listaClienteRiesgoCrediticio.get(i).getScoreManual() + ",NULL,NULL,NULL");
					//Se agrega un salto de linea una vez finalizada la escritura de los datos obtenidos de la base de datos.
					stringBuilder.append("\n");
					//Se agregan NULLs en los campos vacios a pedido del ap
				}				
			}
		}


		
		String port=System.getProperty(PORT_AMDOCS);
		String host=System.getProperty(HOST_AMDOCS);
		String user=System.getProperty(USER_SFTP);
		String pwd=System.getProperty(PASS_SFTP);
		String dir=System.getProperty(REMOTE_DIR);
		
		
		SFTPCliente sftpCliente = new SFTPCliente(host, Integer.parseInt(port));
		sftpCliente.conectar(user, pwd);
		sftpCliente.transferir(stringBuilder.toString(), nombreArchivocsv,nombreArchivotrig , dir);
		sftpCliente.desconectar();	
		
		
		}catch(Exception e){
			LOG.info("Error en generarArchivosCSVModificacion de riesgoCrediticioDAO");
			LOG.info(e);
			throw e;
		}
	}

	public void generarArchivosCSVModificacion(String rutaCompletaArchivo,
			List<ClienteRiesgoCrediticioDTO> listaClienteRiesgoCrediticio)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

}
