package ar.com.latam.creditCore.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import ar.com.latam.creditCore.dao.interfaz.IRiesgoCrediticioHistDAO;
import ar.com.latam.creditCore.dto.ClienteRiesgoCrediticioDTO;
import ar.com.latam.creditCore.dto.RiesgoCrediticioHistoricoDTO;
import ar.com.latam.creditCore.dto.TipoDocumentoDTO;
import ar.com.latam.util.Util;

/**
 * 
 * @author diana.karina.rojas
 *
 */
public class RiesgoCrediticioHistDAO extends JdbcDaoSupport implements IRiesgoCrediticioHistDAO {
	
	private static final Logger LOG = Logger.getLogger(RiesgoCrediticioHistDAO.class);

	public void cargarConversionTipoDocumento(Map<String,String> conversionTipoDocumento) throws Exception{
		List<TipoDocumentoDTO> listaTiposDocumento= null;
		try{		
			listaTiposDocumento = obtenerTiposDocumento();
			if (listaTiposDocumento != null){
				for(int i=0;i<listaTiposDocumento.size();i++){
					conversionTipoDocumento.put(listaTiposDocumento.get(i).getTipoDocAmdoc(), listaTiposDocumento.get(i).getTipoDocDW());
				}
			}			
		}catch(Exception e){
			throw e;
		}
	} 
	
	public List<TipoDocumentoDTO> obtenerTiposDocumento() throws Exception{
		List<TipoDocumentoDTO> listaTiposDocumento= null;
		SimpleDateFormat sdfForDate = new SimpleDateFormat(Util.DATE_TIME_PATTERN);
		SimpleDateFormat sdfForDateDifference = new SimpleDateFormat(Util.TIME_PATETRN);	
		try {
			
			String sqlListaTiposDocumento = "SELECT PARTY_IDENTIFICATION_TYPE_DWCD, "// 1
				    + "PARTY_IDENTIFICATION_TYPE_CD, "// 2
				    + "OBLIG_PARTY_GENDER_TYPE_CD, "//3
				    + "LAST_UPDATED_DT "// 4 
					+ "FROM RC_LKP_TIPO_DOC "
					+ "order by PARTY_IDENTIFICATION_TYPE_CD asc";

			/*
			 * Ejemplo: SELECT PARTY_IDENTIFICATION_TYPE_DWCD, PARTY_IDENTIFICATION_TYPE_CD, OBLIG_PARTY_GENDER_TYPE_CD, LAST_UPDATED_DT FROM RC_LKP_TIPO_DOC order by PARTY_IDENTIFICATION_TYPE_CD asc
			 */
			//resultado: lista de Tipos Documento
			Date start = new Date();
			LOG.info("Inicio Consulta: " + sdfForDate.format(start));	
			LOG.info("Sentencia: " + sqlListaTiposDocumento);
			listaTiposDocumento = super.getJdbcTemplate().query(sqlListaTiposDocumento,
					new RowMapper<TipoDocumentoDTO>() {
						public TipoDocumentoDTO mapRow(ResultSet rs, int rowNum) throws SQLException {							
							TipoDocumentoDTO tipoDocumento = new TipoDocumentoDTO();
							try{
								String tipoDocAmdoc = validarNulos(rs.getString(2));
								//Lo cargo en Mayuscula
								String cargaGenero = validarNulos(rs.getString(3)).trim().toUpperCase();
								tipoDocumento.setTipoDocDW(validarNulos(rs.getString(1)));
								tipoDocumento.setTipoDocAmdoc(tipoDocAmdoc);
								tipoDocumento.setCargaGenero(cargaGenero);
								tipoDocumento.setTipoDocAmdocPipeCargaGenero(tipoDocAmdoc + Util.PIPE_CHARACTER + cargaGenero);
								String fechaCarga = new String();
								//Pasar a un string con formato dd/MM/yyyy	
								if (rs.getDate(4) == null){
									fechaCarga = "";
								}else{
									fechaCarga = Util.getFormatoFecha(rs.getDate(4), Util.DATE_TIME_SCREEN);
								}
								tipoDocumento.setFechaCarga(fechaCarga);
							
							}catch(NullPointerException e2){
								throw new NullPointerException(e2 + "");
							}					
							
							return tipoDocumento;
						}

					});
			Date end = new Date();
			LOG.info("Fin Consulta: " + sdfForDate.format(end));		
			Date difference = Util.differenceBetweenDates(start, end);
			
			StringBuilder builder = new StringBuilder();
			builder.append("Tiempo de ejecucion de la consulta: ");
			builder.append(sqlListaTiposDocumento);
			builder.append(" , que ha devuelto ");
			builder.append(listaTiposDocumento.size());

			builder.append(" registro/s, ha sido de: ");
			builder.append(sdfForDateDifference.format(difference));

			LOG.info(builder);
			if (listaTiposDocumento.isEmpty()) {
				return null;
			}
		}catch(Exception e){
			throw e;
		}				

		return listaTiposDocumento;
	}

	public List<ClienteRiesgoCrediticioDTO> obtenerClientesRiesgoCrediticioActual(String tipoDoc, String nroDoc, String genero, Map<String,String> conversionTipoDocumento_AMDOCS_DW) throws Exception{
		List<ClienteRiesgoCrediticioDTO> listaClientesRiesgoCrediticio= null;
		SimpleDateFormat sdfForDate = new SimpleDateFormat(Util.DATE_TIME_PATTERN);
		SimpleDateFormat sdfForDateDifference = new SimpleDateFormat(Util.TIME_PATETRN);
		Map<String,String> conversionTipoDocumento_DW_AMDOCS = new HashMap<String,String> ();
		try {
			String sqlListaClientesRiesgoCrediticio;
			
			sqlListaClientesRiesgoCrediticio = "SELECT PARTY_IDENTIFICATION_TYPE_CD, "// 1
				    + "PARTY_IDENTIFICATION_NUM, "// 2
				    + "PARTY_GENDER_TYPE_CD, "//3
				    + "PARTY_CREDIT_SCORE_IN_IND, "// 4 
				    + "LAST_UPDATED_IN_DT, "// 5
				    + "PARTY_CREDIT_SCORE_AM_IND, "// 6 
				    + "LAST_UPDATED_AM_DT, "// 7
				    + "PARTY_CREDIT_SCORE_M_IND, "// 8 
				    + "UPDATE_MANUAL_M_DT, "// 9
				    + "USER_UPDATE_M_CD, "// 10
				    + "LAST_UPDATED_F_DT "// 11
			        + "FROM RC_RIESGO_CREDITICIO_FINAL" 
			        + " WHERE";
			
				LOG.info("PARAMETROS:");
				if (conversionTipoDocumento_AMDOCS_DW.get(validarNulos(tipoDoc).trim()) != null){//Entonces el tipoDoc es de formato AMDOCS
					sqlListaClientesRiesgoCrediticio += " (PARTY_IDENTIFICATION_TYPE_CD = "
						 + "'" + validarNulos(tipoDoc).trim() + "' OR"
						 + " PARTY_IDENTIFICATION_TYPE_CD = "
						 + "'" + conversionTipoDocumento_AMDOCS_DW.get(validarNulos(tipoDoc).trim()) + "')";	
					LOG.info("   - Tipo Doc. : " + validarNulos(tipoDoc).trim() + "/" + conversionTipoDocumento_AMDOCS_DW.get(validarNulos(tipoDoc).trim()));
				}else{//Entonces el tipoDoc es de formato DW
					sqlListaClientesRiesgoCrediticio += " PARTY_IDENTIFICATION_TYPE_CD = "
						 							 + "'" + validarNulos(tipoDoc).trim() + "'";
					LOG.info("   - Tipo Doc. : " + validarNulos(tipoDoc).trim());					
				}
				sqlListaClientesRiesgoCrediticio += " AND PARTY_IDENTIFICATION_NUM = " 
												 + "'" + validarNulos(nroDoc).trim() + "'"
												 +" AND";
				LOG.info("   - Nro Doc.: " + validarNulos(nroDoc).trim());				
				if(!Util.EMPTY_CHARACTER.equals(validarNulos(genero))){
					sqlListaClientesRiesgoCrediticio += " PARTY_GENDER_TYPE_CD = " 
						 + "'" + genero + "'"
						 +" AND";
					LOG.info("   - Sexo.: " + validarNulos(genero));	
				}										
			
			sqlListaClientesRiesgoCrediticio += " ROWNUM <= 20";
			
			/* Ejemplo 1: (sin parametros)
			 * SELECT PARTY_IDENTIFICATION_TYPE_CD, PARTY_IDENTIFICATION_NUM, PARTY_GENDER_TYPE_CD, PARTY_CREDIT_SCORE_IN_IND, LAST_UPDATED_IN_DT,
			 * PARTY_CREDIT_SCORE_AM_IND, LAST_UPDATED_AM_DT, PARTY_CREDIT_SCORE_M_IND, UPDATE_MANUAL_M_DT, USER_UPDATE_M_CD, LAST_UPDATED_F_DT FROM RC_RIESGO_CREDITICIO_FINAL WHERE UPDATE_MANUAL_M_DT IS NOT NULL AND ROWNUM <= 20
			 * 
			 * Ejemplo 2: (con parametros y con genero)
			 * Sentencia: SELECT PARTY_IDENTIFICATION_TYPE_CD, PARTY_IDENTIFICATION_NUM, PARTY_GENDER_TYPE_CD, PARTY_CREDIT_SCORE_IN_IND, LAST_UPDATED_IN_DT, PARTY_CREDIT_SCORE_AM_IND, LAST_UPDATED_AM_DT, PARTY_CREDIT_SCORE_M_IND, UPDATE_MANUAL_M_DT, USER_UPDATE_M_CD, LAST_UPDATED_F_DT FROM RC_RIESGO_CREDITICIO_FINAL WHERE PARTY_IDENTIFICATION_TYPE_CD = 'DU' AND PARTY_IDENTIFICATION_NUM = '28432562' AND PARTY_GENDER_TYPE_CD='F' AND ROWNUM <= 20
			 * 
			 * Ejemplo 3: (con parametros y sin genero)
			 * Sentencia: SELECT PARTY_IDENTIFICATION_TYPE_CD, PARTY_IDENTIFICATION_NUM, PARTY_GENDER_TYPE_CD, PARTY_CREDIT_SCORE_IN_IND, LAST_UPDATED_IN_DT, PARTY_CREDIT_SCORE_AM_IND, LAST_UPDATED_AM_DT, PARTY_CREDIT_SCORE_M_IND, UPDATE_MANUAL_M_DT, USER_UPDATE_M_CD, LAST_UPDATED_F_DT FROM RC_RIESGO_CREDITICIO_FINAL WHERE PARTY_IDENTIFICATION_TYPE_CD = 'CU' AND PARTY_IDENTIFICATION_NUM = '27289002317' AND ROWNUM <= 20
			 */

			//resultado: lista de Clientes de Riesgo Crediticio
			Date start = new Date();
			LOG.info("Inicio Consulta: " + sdfForDate.format(start));	
			LOG.info("Sentencia: " + sqlListaClientesRiesgoCrediticio);
			listaClientesRiesgoCrediticio = super.getJdbcTemplate().query(sqlListaClientesRiesgoCrediticio,
					new RowMapper<ClienteRiesgoCrediticioDTO>() {
						public ClienteRiesgoCrediticioDTO mapRow(ResultSet rs, int rowNum) throws SQLException {							
							ClienteRiesgoCrediticioDTO clienteRiesgoCrediticio = new ClienteRiesgoCrediticioDTO();
							try{
								clienteRiesgoCrediticio.setTipoDoc(validarNulos(rs.getString(1)));
								clienteRiesgoCrediticio.setNroDoc(validarNulos(rs.getString(2)));
								clienteRiesgoCrediticio.setGenero(validarNulos(rs.getString(3)));
								clienteRiesgoCrediticio.setScoreCalculadoIN(validarNulos(rs.getString(4)));
								String fechaCargaScoreIN = new String();
								String fechaCargaScoreINBase = new String();								
								if (rs.getDate(5) == null){
									fechaCargaScoreIN = "";
									fechaCargaScoreINBase = "";
								}else{
									fechaCargaScoreIN = Util.getFormatoFecha(rs.getDate(5), Util.DATE_TIME_SCREEN);
									fechaCargaScoreINBase = Util.getFormatoFecha(rs.getTimestamp(5), Util.FORMAT_DATE_RIESGO);
								}
								//Pasar a un string con formato dd/MM/yyyy
								clienteRiesgoCrediticio.setFechaCargaScoreIN(fechaCargaScoreIN);
								//Pasar a un string con formato MM/dd/yyyy HH:mm:ss
								clienteRiesgoCrediticio.setFechaCargaScoreINBase(fechaCargaScoreINBase);								
								clienteRiesgoCrediticio.setScoreLegadoAM(validarNulos(rs.getString(6)));
								String fechaUpdateAM = new String();
								String fechaUpdateAMBase = new String();								
								if (rs.getDate(7) == null){
									fechaUpdateAM = "";
									fechaUpdateAMBase = "";
								}else{
									fechaUpdateAM = Util.getFormatoFecha(rs.getDate(7), Util.DATE_TIME_SCREEN);		
									fechaUpdateAMBase = Util.getFormatoFecha(rs.getTimestamp(7), Util.FORMAT_DATE_RIESGO);	
								}
								//Pasar a un string con formato dd/MM/yyyy
								clienteRiesgoCrediticio.setFechaUpdateAM(fechaUpdateAM);
								//Pasar a un string con formato MM/dd/yyyy HH:mm:ss
								clienteRiesgoCrediticio.setFechaUpdateAMBase(fechaUpdateAMBase);								
								clienteRiesgoCrediticio.setScoreManual(validarNulos(rs.getString(8)));
								String fechaCargaScoreManual = new String();
								String fechaCargaScoreManualBase = null;							
								if (rs.getDate(9) == null){
									fechaCargaScoreManual = "";
									fechaCargaScoreManualBase = "";
								}else{
									fechaCargaScoreManual = Util.getFormatoFecha(rs.getTimestamp(9), Util.DATE_TIME_SCREEN_2);
									fechaCargaScoreManualBase = Util.getFormatoFecha(rs.getTimestamp(9), Util.FORMAT_DATE_RIESGO);
								}
								//Pasar a un string con formato dd/MM/yyyy	
								clienteRiesgoCrediticio.setFechaCargaScoreManual(fechaCargaScoreManual);
								//Pasar a un string con formato MM/dd/yyyy HH:mm:ss
								clienteRiesgoCrediticio.setFechaCargaScoreManualBase(fechaCargaScoreManualBase);								
								clienteRiesgoCrediticio.setUsuarioCargaCambio(validarNulos(rs.getString(10)));
								String fechaUltimoUpdate = new String();
								String fechaUltimoUpdateBase = new String();
								if (rs.getDate(11) == null){
									fechaUltimoUpdate = "";
									fechaUltimoUpdateBase = "";
								}else{
									fechaUltimoUpdate = Util.getFormatoFecha(rs.getDate(11), Util.DATE_TIME_SCREEN);
									fechaUltimoUpdateBase = Util.getFormatoFecha(rs.getTimestamp(11), Util.FORMAT_DATE_RIESGO);
								}	
								//Pasar a un string con formato dd/MM/yyyy	
								clienteRiesgoCrediticio.setFechaUltimoUpdate(fechaUltimoUpdate);
								//Pasar a un string con formato MM/dd/yyyy HH:mm:ss
								clienteRiesgoCrediticio.setFechaUltimoUpdateBase(fechaUltimoUpdateBase);								
							}catch(NullPointerException e2){
								throw new NullPointerException(e2 + "");
							}					
							
							return clienteRiesgoCrediticio;
						}

					});
			Date end = new Date();
			LOG.info("Fin Consulta: " + sdfForDate.format(end));		
			Date difference = Util.differenceBetweenDates(start, end);
			
			StringBuilder builder = new StringBuilder();
			builder.append("Tiempo de ejecucion de la consulta: ");
			builder.append(sqlListaClientesRiesgoCrediticio);
			builder.append(" , que ha devuelto ");
			builder.append(listaClientesRiesgoCrediticio.size());

			builder.append(" registro/s, ha sido de: ");
			builder.append(sdfForDateDifference.format(difference));

			LOG.info(builder);
			if (listaClientesRiesgoCrediticio.isEmpty()) {
				return null;
			}else{
				Iterator it = conversionTipoDocumento_AMDOCS_DW.entrySet().iterator();
				
				while (it.hasNext()) {
					Map.Entry<String, String> e = (Map.Entry)it.next();
					conversionTipoDocumento_DW_AMDOCS.put(e.getValue(), e.getKey() );
				}
				
				for(int i=0;i<listaClientesRiesgoCrediticio.size();i++){
					if(conversionTipoDocumento_DW_AMDOCS.containsKey(listaClientesRiesgoCrediticio.get(i).getTipoDoc()) == true){
						//Si el tipo de Doc esta en el key del hashMap conversionTipoDocumento_DW_AMDOCS, entonces, es de tipo DW
						listaClientesRiesgoCrediticio.get(i).setTipoDocAMDOCS(conversionTipoDocumento_DW_AMDOCS.get(listaClientesRiesgoCrediticio.get(i).getTipoDoc()));
					}else{
						listaClientesRiesgoCrediticio.get(i).setTipoDocAMDOCS(listaClientesRiesgoCrediticio.get(i).getTipoDoc());
					}
				}
			}
		}catch(Exception e){
			throw e;
		}				

		return listaClientesRiesgoCrediticio;		
	}	
	
	public List<RiesgoCrediticioHistoricoDTO> obtenerClientesRiesgoCrediticioHist(String tipoDoc, String nroDoc, String genero, Map<String,String> conversionTipoDocumento_AMDOCS_DW) throws Exception{
		List<RiesgoCrediticioHistoricoDTO> listaClientesRiesgoCrediticioHist= null;
		SimpleDateFormat sdfForDate = new SimpleDateFormat(Util.DATE_TIME_PATTERN);
		SimpleDateFormat sdfForDateDifference = new SimpleDateFormat(Util.TIME_PATETRN);
		Map<String,String> conversionTipoDocumento_DW_AMDOCS = new HashMap<String,String> ();
		try {
			String sqlListaClientesRiesgoCrediticioHist;
			
			sqlListaClientesRiesgoCrediticioHist = "SELECT PARTY_IDENTIFICATION_TYPE_CD, "// 1
				    + "PARTY_IDENTIFICATION_NUM, "// 2
				    + "PARTY_GENDER_TYPE_CD, "//3
				    + "PARTY_CREDIT_SCORE_IN_IND, "// 4 
				    + "LAST_UPDATED_IN_DT, "// 5 
				    + "PARTY_CREDIT_SCORE_AM_IND, "// 6 
				    + "LAST_UPDATED_AM_DT, "// 7
				    + "PARTY_CREDIT_SCORE_M_IND, "// 8
				    + "UPDATE_MANUAL_M_DT, "// 9 
				    + "USER_UPDATE_M_CD "// 10
			        + "FROM RC_RIESGO_CREDITICIO_HIST " 
			        + "WHERE ";
			LOG.info("PARAMETROS:");
			if (conversionTipoDocumento_AMDOCS_DW.get(validarNulos(tipoDoc).trim()) != null){//Entonces el tipoDoc es de formato AMDOCS
				sqlListaClientesRiesgoCrediticioHist += " (PARTY_IDENTIFICATION_TYPE_CD = "
					 + "'" + validarNulos(tipoDoc).trim() + "' OR"
					 + " PARTY_IDENTIFICATION_TYPE_CD = "
					 + "'" + conversionTipoDocumento_AMDOCS_DW.get(validarNulos(tipoDoc).trim()) + "')";	
				LOG.info("   - Tipo Doc. : " + validarNulos(tipoDoc).trim() + "/" + conversionTipoDocumento_AMDOCS_DW.get(validarNulos(tipoDoc).trim()));
			}else{//Entonces el tipoDoc es de formato DW
				sqlListaClientesRiesgoCrediticioHist += " PARTY_IDENTIFICATION_TYPE_CD = "
					 							 + "'" + validarNulos(tipoDoc).trim() + "'";
				LOG.info("   - Tipo Doc. : " + validarNulos(tipoDoc).trim());					
			}			        
			
			sqlListaClientesRiesgoCrediticioHist += "AND PARTY_IDENTIFICATION_NUM = " 
				    + "'" + validarNulos(nroDoc).trim() + "' "
					 +" AND";
			LOG.info("   - Nro Doc.: " + validarNulos(nroDoc).trim());
			if(!Util.EMPTY_CHARACTER.equals(validarNulos(genero))){
				sqlListaClientesRiesgoCrediticioHist += " PARTY_GENDER_TYPE_CD = " 
					 + "'" + genero + "'"
					 +" AND";
				LOG.info("   - Sexo.: " + validarNulos(genero));	
			}				    
			sqlListaClientesRiesgoCrediticioHist += " ROWNUM <= 1000 ORDER BY LAST_UPDATED_DT DESC";

			/* Ejemplo 1: (con parametros y sin genero)
			 * Sentencia: SELECT PARTY_IDENTIFICATION_TYPE_CD, PARTY_IDENTIFICATION_NUM, PARTY_GENDER_TYPE_CD,PARTY_CREDIT_SCORE_IN_IND, LAST_UPDATED_IN_DT, PARTY_CREDIT_SCORE_AM_IND, LAST_UPDATED_AM_DT, PARTY_CREDIT_SCORE_M_IND, UPDATE_MANUAL_M_DT, USER_UPDATE_M_CD FROM RC_RIESGO_CREDITICIO_HIST WHERE PARTY_IDENTIFICATION_TYPE_CD = 'CU' AND PARTY_IDENTIFICATION_NUM = '16765183' AND ROWNUM <= 1000 ORDER BY LAST_UPDATED_DT DESC
			 * 
			 * Ejemplo 2: (con parametros y con genero)
			 * Sentencia: SELECT PARTY_IDENTIFICATION_TYPE_CD, PARTY_IDENTIFICATION_NUM, PARTY_GENDER_TYPE_CD,PARTY_CREDIT_SCORE_IN_IND, LAST_UPDATED_IN_DT, PARTY_CREDIT_SCORE_AM_IND, LAST_UPDATED_AM_DT, PARTY_CREDIT_SCORE_M_IND, UPDATE_MANUAL_M_DT, USER_UPDATE_M_CD FROM RC_RIESGO_CREDITICIO_HIST WHERE PARTY_IDENTIFICATION_TYPE_CD = 'DU' AND PARTY_IDENTIFICATION_NUM = '20167651837' AND PARTY_GENDER_TYPE_CD='F' AND ROWNUM <= 1000 ORDER BY LAST_UPDATED_DT DESC
			 */

			//resultado: lista de Clientes de Riesgo Crediticio Historico
			Date start = new Date();
			LOG.info("Inicio Consulta: " + sdfForDate.format(start));	
			LOG.info("Sentencia: " + sqlListaClientesRiesgoCrediticioHist);
			listaClientesRiesgoCrediticioHist = super.getJdbcTemplate().query(sqlListaClientesRiesgoCrediticioHist,
					new RowMapper<RiesgoCrediticioHistoricoDTO>() {
						public RiesgoCrediticioHistoricoDTO mapRow(ResultSet rs, int rowNum) throws SQLException {							
							RiesgoCrediticioHistoricoDTO clienteRiesgoCrediticio = new RiesgoCrediticioHistoricoDTO();
							try{
								clienteRiesgoCrediticio.setTipoDoc(validarNulos(rs.getString(1)));
								clienteRiesgoCrediticio.setNroDoc(validarNulos(rs.getString(2)));
								clienteRiesgoCrediticio.setGenero(validarNulos(rs.getString(3)));
								clienteRiesgoCrediticio.setScoreCalculadoIN(validarNulos(rs.getString(4)));
								String fechaCargaScoreIN = new String();
								//Pasar a un string con formato dd/MM/yyyy
								if (rs.getDate(5) == null){
									fechaCargaScoreIN = "";
								}else{
									fechaCargaScoreIN = Util.getFormatoFecha(rs.getDate(5), Util.DATE_TIME_SCREEN);
								}
								clienteRiesgoCrediticio.setFechaCargaScoreIN(fechaCargaScoreIN);
								clienteRiesgoCrediticio.setScoreLegadoAM(validarNulos(rs.getString(6)));
								String fechaUpdateAM = new String();
								//Pasar a un string con formato dd/MM/yyyy
								if (rs.getDate(7) == null){
									fechaUpdateAM = "";
								}else{
									fechaUpdateAM = Util.getFormatoFecha(rs.getDate(7), Util.DATE_TIME_SCREEN);									
								}
								clienteRiesgoCrediticio.setFechaUpdateAM(fechaUpdateAM);
								clienteRiesgoCrediticio.setScoreManual(validarNulos(rs.getString(8)));
								String fechaCargaScoreManual = new String();
								//Pasar a un string con formato dd/MM/yyyy	
								if (rs.getDate(9) == null){
									fechaCargaScoreManual = "";
								}else{
									fechaCargaScoreManual = Util.getFormatoFecha(rs.getTimestamp(9), Util.DATE_TIME_SCREEN_2);
								}
								clienteRiesgoCrediticio.setFechaCargaScoreManual(fechaCargaScoreManual);
								clienteRiesgoCrediticio.setUsuarioCargaCambio(validarNulos(rs.getString(10)));
							
							}catch(NullPointerException e2){
								throw new NullPointerException(e2 + "");
							}					
							
							return clienteRiesgoCrediticio;
						}

					});
			Date end = new Date();
			LOG.info("Fin Consulta: " + sdfForDate.format(end));		
			Date difference = Util.differenceBetweenDates(start, end);
			
			StringBuilder builder = new StringBuilder();
			builder.append("Tiempo de ejecucion de la consulta: ");
			builder.append(sqlListaClientesRiesgoCrediticioHist);
			builder.append(" , que ha devuelto ");
			builder.append(listaClientesRiesgoCrediticioHist.size());

			builder.append(" registro/s, ha sido de: ");
			builder.append(sdfForDateDifference.format(difference));

			LOG.info(builder);
			if (listaClientesRiesgoCrediticioHist.isEmpty()) {
				return null;
			}else{
				Iterator it = conversionTipoDocumento_AMDOCS_DW.entrySet().iterator();
				
				while (it.hasNext()) {
					Map.Entry<String, String> e = (Map.Entry)it.next();
					conversionTipoDocumento_DW_AMDOCS.put(e.getValue(), e.getKey() );
				}
				
				for(int i=0;i<listaClientesRiesgoCrediticioHist.size();i++){
					if(conversionTipoDocumento_DW_AMDOCS.containsKey(listaClientesRiesgoCrediticioHist.get(i).getTipoDoc()) == true){
						//Si el tipo de Doc esta en el key del hashMap conversionTipoDocumento_DW_AMDOCS, entonces, es de tipo DW
						listaClientesRiesgoCrediticioHist.get(i).setTipoDocAMDOCS(conversionTipoDocumento_DW_AMDOCS.get(listaClientesRiesgoCrediticioHist.get(i).getTipoDoc()));
					}else{
						listaClientesRiesgoCrediticioHist.get(i).setTipoDocAMDOCS(listaClientesRiesgoCrediticioHist.get(i).getTipoDoc());
					}
				}
			}
		}catch(Exception e){
			throw e;
		}				

		return listaClientesRiesgoCrediticioHist;		
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
