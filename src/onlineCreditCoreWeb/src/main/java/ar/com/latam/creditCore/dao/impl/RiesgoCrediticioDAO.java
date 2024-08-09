package ar.com.latam.creditCore.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import ar.com.latam.creditCore.dao.interfaz.IRiesgoCrediticioDAO;
import ar.com.latam.creditCore.dto.ClienteRiesgoCrediticioDTO;
import ar.com.latam.creditCore.dto.ExportCSVDTO;
import ar.com.latam.creditCore.dto.RiesgoCrediticioManualDTO;
import ar.com.latam.creditCore.dto.TipoDocumentoDTO;
import ar.com.latam.creditCore.mapper.BajadaResulSetMapper;
import ar.com.latam.util.Util;

/**
 * 
 * @author diana.karina.rojas
 *
 */
public class RiesgoCrediticioDAO extends JdbcDaoSupport implements IRiesgoCrediticioDAO {
	
	private static final Logger LOG = Logger.getLogger(RiesgoCrediticioDAO.class);    
    
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
	
	/**
	 * Carga de tipos de doc para la bajada manual
	 * @return Map<String,String>
	 * @throws Exception
	 */
	public Map<String,String> cargarConversionTipoDocumentoBajadaManual() throws Exception{
		List<TipoDocumentoDTO> listaTiposDocumento= null;
		Map<String,String> conversionTipoDocumento= new HashMap();
		try{	
			
			listaTiposDocumento = obtenerTiposDocumento();
			if (listaTiposDocumento != null){
				for(int i=0;i<listaTiposDocumento.size();i++){
					conversionTipoDocumento.put(listaTiposDocumento.get(i).getTipoDocDW(), listaTiposDocumento.get(i).getTipoDocAmdoc());
				}
			}			
		}catch(Exception e){
			throw e;
		}
		return conversionTipoDocumento;
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
	
	public boolean existeNroDocumento(String tipoDoc, String nroDoc) throws Exception{
		List<ClienteRiesgoCrediticioDTO> listaClientesRiesgoCrediticio= null;
		SimpleDateFormat sdfForDate = new SimpleDateFormat(Util.DATE_TIME_PATTERN);
		SimpleDateFormat sdfForDateDifference = new SimpleDateFormat(Util.TIME_PATETRN);	
		try {
			
			String sqlListaClientesRiesgoCrediticio = "SELECT PARTY_IDENTIFICATION_TYPE_CD, PARTY_IDENTIFICATION_NUM "
					+ "FROM RC_RIESGO_CREDITICIO_FINAL "
					+ "WHERE PARTY_IDENTIFICATION_TYPE_CD = ";
					sqlListaClientesRiesgoCrediticio += "'" + tipoDoc.trim() + "' "
					+ "AND PARTY_IDENTIFICATION_NUM = "
					+ "'" + nroDoc.trim() + "' ";			    				
			/*Ejemplo:
			 * SELECT PARTY_IDENTIFICATION_TYPE_CD, PARTY_IDENTIFICATION_NUM FROM RC_RIESGO_CREDITICIO_FINAL 
			 * WHERE PARTY_IDENTIFICATION_TYPE_CD = 'CL' AND PARTY_IDENTIFICATION_NUM = '20213456327'			 
			 */
			//resultado: lista de Clientes de Riesgo Crediticio con ese tipo y nro de documento
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
				return false;
			}else if (listaClientesRiesgoCrediticio.size()>= 1) {
				return true;
			}
		}catch(Exception e){
			throw e;
		}
		return false;
	}
	
	public List<ClienteRiesgoCrediticioDTO> existeNroDocumentoResultado(String tipoDoc, String nroDoc) throws Exception{
		List<ClienteRiesgoCrediticioDTO> listaClientesRiesgoCrediticio= null;
		SimpleDateFormat sdfForDate = new SimpleDateFormat(Util.DATE_TIME_PATTERN);
		SimpleDateFormat sdfForDateDifference = new SimpleDateFormat(Util.TIME_PATETRN);	
		try {
			
			String sqlListaClientesRiesgoCrediticio = "SELECT PARTY_IDENTIFICATION_TYPE_CD, PARTY_IDENTIFICATION_NUM, PARTY_GENDER_TYPE_CD "
					+ "FROM RC_RIESGO_CREDITICIO_FINAL "
					+ "WHERE PARTY_IDENTIFICATION_TYPE_CD = ";
					sqlListaClientesRiesgoCrediticio += "'" + tipoDoc.trim() + "' "
					+ "AND PARTY_IDENTIFICATION_NUM = "
					+ "'" + nroDoc.trim() + "' ";				    				
			/*Ejemplo:
			 * SELECT PARTY_IDENTIFICATION_TYPE_CD, PARTY_IDENTIFICATION_NUM, PARTY_GENDER_TYPE_CD FROM RC_RIESGO_CREDITICIO_FINAL 
			 * WHERE PARTY_IDENTIFICATION_TYPE_CD = 'DU'
			 * AND PARTY_IDENTIFICATION_NUM = '21345632'
			 * 
			 */
			//resultado: lista de Clientes de Riesgo Crediticio con ese tipo, nro de documento
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
			}
		}catch(Exception e){
			throw e;
		}
		return listaClientesRiesgoCrediticio;
	}	

	public void altaClienteRiesgoCrediticioManual(RiesgoCrediticioManualDTO riesgoCrediticioManual) throws Exception{
		SimpleDateFormat sdfForDate = new SimpleDateFormat(Util.DATE_TIME_PATTERN);
		SimpleDateFormat sdfForDateDifference = new SimpleDateFormat(Util.TIME_PATETRN);
		String score = validarNulos(riesgoCrediticioManual.getScoreManual()).trim();
		String genero = validarNulos(riesgoCrediticioManual.getGenero()).trim();
		try {
			String sqlInsertClienteRiesgoCrediticioManual = "INSERT INTO RC_RIESGO_CREDITICIO_MANUAL "
				    + "(PARTY_IDENTIFICATION_TYPE_CD, PARTY_IDENTIFICATION_NUM, PARTY_GENDER_TYPE_CD, PARTY_CREDIT_SCORE_M_IND, UPDATE_MANUAL_M_DT, USER_UPDATE_M_CD) "
				    + "VALUES ("
				    + "'" + riesgoCrediticioManual.getTipoDoc().trim() + "', "
					+ "'" + riesgoCrediticioManual.getNroDoc().trim() + "', ";
					if (Util.EMPTY_CHARACTER.equals(genero)){
						sqlInsertClienteRiesgoCrediticioManual += Util.NULL_CHARACTER + ", ";
					}else{
						sqlInsertClienteRiesgoCrediticioManual += "'" + genero + "', ";				
					}			
					if (Util.EMPTY_CHARACTER.equals(score)){
						sqlInsertClienteRiesgoCrediticioManual += Util.NULL_CHARACTER + ", ";
					}else{
						sqlInsertClienteRiesgoCrediticioManual += score + ", ";				
					}					
					sqlInsertClienteRiesgoCrediticioManual += riesgoCrediticioManual.getFechaCargaScoreManual() + ", "
					+ "'" + riesgoCrediticioManual.getUsuarioCargaCambio()+"')";
			/*Ejemplo sin Genero:
			 * INSERT INTO RC_RIESGO_CREDITICIO_MANUAL 
			 * (PARTY_IDENTIFICATION_TYPE_CD, PARTY_IDENTIFICATION_NUM, PARTY_GENDER_TYPE_CD,PARTY_CREDIT_SCORE_M_IND, UPDATE_MANUAL_M_DT, USER_UPDATE_M_CD)
			 *  VALUES ('DNI', '28245611', NULL, 999, SYSDATE, 'defeudise') 
			 *  
			 * Ejemplo con Genero:
			 * INSERT INTO RC_RIESGO_CREDITICIO_MANUAL 
			 * (PARTY_IDENTIFICATION_TYPE_CD, PARTY_IDENTIFICATION_NUM, PARTY_GENDER_TYPE_CD, PARTY_CREDIT_SCORE_M_IND, UPDATE_MANUAL_M_DT, USER_UPDATE_M_CD)
			 *  VALUES ('DNI', '28245611', 'F', 999, SYSDATE, 'defeudise') 
			 */
			Date start = new Date();
			LOG.info("Inicio Insercion: " + sdfForDate.format(start));	
			LOG.info("Sentencia: " + sqlInsertClienteRiesgoCrediticioManual);
			super.getJdbcTemplate().execute(sqlInsertClienteRiesgoCrediticioManual);
			Date end = new Date();
			LOG.info("Fin Insercion: " + sdfForDate.format(end));		
			Date difference = Util.differenceBetweenDates(start, end);
			
			StringBuilder builder = new StringBuilder();
			builder.append("Tiempo de ejecucion de: ");
			builder.append(sqlInsertClienteRiesgoCrediticioManual);

			builder.append(" ha sido de: ");
			builder.append(sdfForDateDifference.format(difference));

			LOG.info(builder);
		}catch(Exception e){
			throw e;
		}
	}

	public void altaClienteRiesgoCrediticioFinal(ClienteRiesgoCrediticioDTO clienteRiesgoCrediticio)throws Exception {
		SimpleDateFormat sdfForDate = new SimpleDateFormat(Util.DATE_TIME_PATTERN);
		SimpleDateFormat sdfForDateDifference = new SimpleDateFormat(Util.TIME_PATETRN);
		String genero = validarNulos(clienteRiesgoCrediticio.getGenero()).trim();
		try {
			String sqlInsertClienteRiesgoCrediticioFinal = "INSERT INTO RC_RIESGO_CREDITICIO_FINAL "
				    + "(PARTY_IDENTIFICATION_TYPE_CD, PARTY_IDENTIFICATION_NUM, PARTY_GENDER_TYPE_CD, PARTY_CREDIT_SCORE_M_IND, UPDATE_MANUAL_M_DT, USER_UPDATE_M_CD, LAST_UPDATED_F_DT) "
				    + "VALUES ("
				    + "'" + clienteRiesgoCrediticio.getTipoDoc().trim() + "', "
					+ "'" + clienteRiesgoCrediticio.getNroDoc().trim() + "', ";
					if (Util.EMPTY_CHARACTER.equals(genero)){
						sqlInsertClienteRiesgoCrediticioFinal += Util.NULL_CHARACTER + ", ";
					}else{
						sqlInsertClienteRiesgoCrediticioFinal += "'" + genero + "', ";				
					}					
					sqlInsertClienteRiesgoCrediticioFinal +=  clienteRiesgoCrediticio.getScoreManual().trim() + ", "
					+ clienteRiesgoCrediticio.getFechaCargaScoreManual() + ", "
					+ "'" + clienteRiesgoCrediticio.getUsuarioCargaCambio() + "', "
					+ clienteRiesgoCrediticio.getFechaUltimoUpdate() + ")";
			/*Ejemplo sin Genero:
			 * INSERT INTO RC_RIESGO_CREDITICIO_FINAL 
			 * (PARTY_IDENTIFICATION_TYPE_CD, PARTY_IDENTIFICATION_NUM, PARTY_GENDER_TYPE_CD, PARTY_CREDIT_SCORE_M_IND, UPDATE_MANUAL_M_DT, USER_UPDATE_M_CD, LAST_UPDATED_F_DT)
			 *  VALUES ('CU', '28245611', NULL, 999, SYSDATE, 'defeudise', SYSDATE)
			 *  
			 *  Ejemplo con Genero:
			 * INSERT INTO RC_RIESGO_CREDITICIO_FINAL 
			 * (PARTY_IDENTIFICATION_TYPE_CD, PARTY_IDENTIFICATION_NUM, PARTY_GENDER_TYPE_CD, PARTY_CREDIT_SCORE_M_IND, UPDATE_MANUAL_M_DT, USER_UPDATE_M_CD, LAST_UPDATED_F_DT)
			 *  VALUES ('DNI', '28245611', 'F', 999, SYSDATE, 'defeudise', SYSDATE) 
			 */
			Date start = new Date();
			LOG.info("Inicio Insercion: " + sdfForDate.format(start));	
			LOG.info("Sentencia: " + sqlInsertClienteRiesgoCrediticioFinal);
			super.getJdbcTemplate().execute(sqlInsertClienteRiesgoCrediticioFinal);
			Date end = new Date();
			LOG.info("Fin Insercion: " + sdfForDate.format(end));		
			Date difference = Util.differenceBetweenDates(start, end);
			
			StringBuilder builder = new StringBuilder();
			builder.append("Tiempo de ejecucion de: ");
			builder.append(sqlInsertClienteRiesgoCrediticioFinal);

			builder.append(" ha sido de: ");
			builder.append(sdfForDateDifference.format(difference));

			LOG.info(builder);
		}catch(Exception e){
			throw e;
		}	
	}
	
	public List<ClienteRiesgoCrediticioDTO> obtenerClientesRiesgoCrediticio(String tipoDoc, String nroDoc, String genero, Map<String,String> conversionTipoDocumento_AMDOCS_DW) throws Exception{
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
			
			if(Util.TODOS.equals(validarNulos(tipoDoc)) && Util.TODOS.equals(validarNulos(nroDoc))){
				sqlListaClientesRiesgoCrediticio += " UPDATE_MANUAL_M_DT IS NOT NULL AND ";
			}else if(!(Util.TODOS.equals(validarNulos(tipoDoc)) && Util.TODOS.equals(validarNulos(nroDoc)))){
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

	public int modificacionClienteRiesgoCrediticioManual(RiesgoCrediticioManualDTO riesgoCrediticioManual)throws Exception{
		SimpleDateFormat sdfForDate = new SimpleDateFormat(Util.DATE_TIME_PATTERN);
		SimpleDateFormat sdfForDateDifference = new SimpleDateFormat(Util.TIME_PATETRN);
		int registrosActualizados = 0;
		String score = validarNulos(riesgoCrediticioManual.getScoreManual()).trim();
		
		try {
			
			String sqlUpdateClienteRiesgoCrediticioManual = "UPDATE RC_RIESGO_CREDITICIO_MANUAL"
					+ " SET PARTY_CREDIT_SCORE_M_IND=";
			if (score.equals(Util.EMPTY_CHARACTER)){
				sqlUpdateClienteRiesgoCrediticioManual += Util.NULL_CHARACTER + ",";
			}else{
				sqlUpdateClienteRiesgoCrediticioManual += score + ",";				
			}
			sqlUpdateClienteRiesgoCrediticioManual += " UPDATE_MANUAL_M_DT="
					+ riesgoCrediticioManual.getFechaCargaScoreManual() + ","
					+ " USER_UPDATE_M_CD="
					+ "'" + riesgoCrediticioManual.getUsuarioCargaCambio() + "'"
					+ " WHERE"
					+ " PARTY_IDENTIFICATION_TYPE_CD = "
				    + "'" + riesgoCrediticioManual.getTipoDoc() + "'"
				    + " AND PARTY_IDENTIFICATION_NUM = "
					+ "'" + riesgoCrediticioManual.getNroDoc() + "'";		
			/*Ejemplo:
			 * UPDATE RC_RIESGO_CREDITICIO_MANUAL SET PARTY_CREDIT_SCORE_M_IND=102, UPDATE_MANUAL_M_DT=SYSDATE, USER_UPDATE_M_CD='prueba'
			 * WHERE PARTY_IDENTIFICATION_TYPE_CD = 'DNI' AND PARTY_IDENTIFICATION_NUM = '23458091'
			 */
			Date start = new Date();
			LOG.info("Inicio Modificacion: " + sdfForDate.format(start));	
			LOG.info("Sentencia: " + sqlUpdateClienteRiesgoCrediticioManual);
			registrosActualizados = super.getJdbcTemplate().update(sqlUpdateClienteRiesgoCrediticioManual);
			Date end = new Date();
			LOG.info("Fin Modificacion: " + sdfForDate.format(end));		
			Date difference = Util.differenceBetweenDates(start, end);
			
			StringBuilder builder = new StringBuilder();
			builder.append("Tiempo de ejecucion de: ");
			builder.append(sqlUpdateClienteRiesgoCrediticioManual);
			builder.append(" ha sido de: ");
			builder.append(sdfForDateDifference.format(difference));
			builder.append(". Se actualizo " + registrosActualizados + " registro/s");		
			
			LOG.info(builder);
			return registrosActualizados;
		}catch(Exception e){
			throw e;
		}		
	}

	public void altaClienteRiesgoCrediticioHistorico(ClienteRiesgoCrediticioDTO clienteRiesgoCrediticio, Map<String,String> conversionTipoDocumento) throws Exception {
		SimpleDateFormat sdfForDate = new SimpleDateFormat(Util.DATE_TIME_PATTERN);
		SimpleDateFormat sdfForDateDifference = new SimpleDateFormat(Util.TIME_PATETRN);	
		List<ClienteRiesgoCrediticioDTO> listaClientesRiesgoCrediticio= null;
		int registrosInsertados = 0;
		
		try{
			listaClientesRiesgoCrediticio = obtenerClientesRiesgoCrediticio(clienteRiesgoCrediticio.getTipoDoc().trim(), clienteRiesgoCrediticio.getNroDoc().trim(), null, conversionTipoDocumento);
		}catch(Exception e){
			throw e;
		}	
		
		try {
	
			String sqlInsertClienteRiesgoCrediticioHistorico = "INSERT INTO RC_RIESGO_CREDITICIO_HIST "
				    + "(PARTY_IDENTIFICATION_TYPE_CD, "
				    + "PARTY_IDENTIFICATION_NUM, ";
			
			if (listaClientesRiesgoCrediticio.get(0) != null && !Util.EMPTY_CHARACTER.equals(listaClientesRiesgoCrediticio.get(0).getGenero())){
				sqlInsertClienteRiesgoCrediticioHistorico += "PARTY_GENDER_TYPE_CD, ";
			}
			if (!Util.EMPTY_CHARACTER.equals(listaClientesRiesgoCrediticio.get(0).getScoreCalculadoIN())){
				sqlInsertClienteRiesgoCrediticioHistorico += "PARTY_CREDIT_SCORE_IN_IND, ";
			}
			
			if (!Util.EMPTY_CHARACTER.equals(listaClientesRiesgoCrediticio.get(0).getFechaCargaScoreINBase())){
				sqlInsertClienteRiesgoCrediticioHistorico +=  "LAST_UPDATED_IN_DT, "; 
			}
			
			if (!Util.EMPTY_CHARACTER.equals(listaClientesRiesgoCrediticio.get(0).getScoreLegadoAM())){
				sqlInsertClienteRiesgoCrediticioHistorico += "PARTY_CREDIT_SCORE_AM_IND, ";
			}
			
			if (!Util.EMPTY_CHARACTER.equals(listaClientesRiesgoCrediticio.get(0).getFechaUpdateAMBase())){
				sqlInsertClienteRiesgoCrediticioHistorico += "LAST_UPDATED_AM_DT, "; 
			}

			if (!Util.EMPTY_CHARACTER.equals(listaClientesRiesgoCrediticio.get(0).getScoreManual())){
				sqlInsertClienteRiesgoCrediticioHistorico += "PARTY_CREDIT_SCORE_M_IND, ";
			}			
			
			if (!Util.EMPTY_CHARACTER.equals(listaClientesRiesgoCrediticio.get(0).getFechaCargaScoreManualBase())){
				sqlInsertClienteRiesgoCrediticioHistorico += "UPDATE_MANUAL_M_DT, ";
			}
			
			sqlInsertClienteRiesgoCrediticioHistorico += "USER_UPDATE_M_CD, ";
			
			if (!Util.EMPTY_CHARACTER.equals(listaClientesRiesgoCrediticio.get(0).getFechaUltimoUpdateBase())){
				sqlInsertClienteRiesgoCrediticioHistorico += "LAST_UPDATED_F_DT, ";
			}
			
			sqlInsertClienteRiesgoCrediticioHistorico += "LAST_UPDATED_DT) "
				    + "VALUES ("
				    + "'" + listaClientesRiesgoCrediticio.get(0).getTipoDoc() + "', "
					+ "'" + listaClientesRiesgoCrediticio.get(0).getNroDoc()+ "', ";
			
			if (!Util.EMPTY_CHARACTER.equals(listaClientesRiesgoCrediticio.get(0).getGenero())){
				sqlInsertClienteRiesgoCrediticioHistorico += "'" + listaClientesRiesgoCrediticio.get(0).getGenero() + "', ";
			}
			
			if (!Util.EMPTY_CHARACTER.equals(listaClientesRiesgoCrediticio.get(0).getScoreCalculadoIN())){
				sqlInsertClienteRiesgoCrediticioHistorico += listaClientesRiesgoCrediticio.get(0).getScoreCalculadoIN() + ", ";
			}
			
			if (!Util.EMPTY_CHARACTER.equals(listaClientesRiesgoCrediticio.get(0).getFechaCargaScoreINBase())){
				sqlInsertClienteRiesgoCrediticioHistorico +=  "TO_DATE('" + listaClientesRiesgoCrediticio.get(0).getFechaCargaScoreINBase() + "', '" + Util.FORMAT_DATE_BD + "'), ";
			}
			
			if (!Util.EMPTY_CHARACTER.equals(listaClientesRiesgoCrediticio.get(0).getScoreLegadoAM())){
				sqlInsertClienteRiesgoCrediticioHistorico += listaClientesRiesgoCrediticio.get(0).getScoreLegadoAM() + ", ";
			}
			
			if (!Util.EMPTY_CHARACTER.equals(listaClientesRiesgoCrediticio.get(0).getFechaUpdateAMBase())){
				sqlInsertClienteRiesgoCrediticioHistorico +=  "TO_DATE('" + listaClientesRiesgoCrediticio.get(0).getFechaUpdateAMBase() + "', '" + Util.FORMAT_DATE_BD + "'), ";
			}
			
			if (!Util.EMPTY_CHARACTER.equals(listaClientesRiesgoCrediticio.get(0).getScoreManual())){
				sqlInsertClienteRiesgoCrediticioHistorico += listaClientesRiesgoCrediticio.get(0).getScoreManual() + ", ";
			}
			
			if (!Util.EMPTY_CHARACTER.equals(listaClientesRiesgoCrediticio.get(0).getFechaCargaScoreManualBase())){
				sqlInsertClienteRiesgoCrediticioHistorico +=  "TO_DATE('" + listaClientesRiesgoCrediticio.get(0).getFechaCargaScoreManualBase() + "', '" + Util.FORMAT_DATE_BD + "'), ";
			}
			
			sqlInsertClienteRiesgoCrediticioHistorico +=  "'" + listaClientesRiesgoCrediticio.get(0).getUsuarioCargaCambio() + "', ";
			
			if (!Util.EMPTY_CHARACTER.equals(listaClientesRiesgoCrediticio.get(0).getFechaUltimoUpdateBase())){
				sqlInsertClienteRiesgoCrediticioHistorico +=  "TO_DATE('" + listaClientesRiesgoCrediticio.get(0).getFechaUltimoUpdateBase() + "', '" + Util.FORMAT_DATE_BD + "'), ";
			}
			
			sqlInsertClienteRiesgoCrediticioHistorico +=  "SYSDATE)";
				
			/*Ejemplo:
				INSERT INTO RC_RIESGO_CREDITICIO_HIST
						PARTY_IDENTIFICATION_TYPE_CD, 
						PARTY_IDENTIFICATION_NUM,
						PARTY_GENDER_TYPE_CD, 
						PARTY_CREDIT_SCORE_IN_IND, 
						LAST_UPDATED_IN_DT, 
						PARTY_CREDIT_SCORE_AM_IND, 
						LAST_UPDATED_AM_DT, 
						PARTY_CREDIT_SCORE_M_IND, 
						UPDATE_MANUAL_M_DT, 
						USER_UPDATE_M_CD, 
						LAST_UPDATED_F_DT, 
						LAST_UPDATED_DT) 
					VALUES 
						('DNI', 
						'26123456', 
						'F',
						100, 
						TO_DATE('04/07/2014 18:03:50', 'MM/DD/YYYY HH24:MI:SS'), 
						102, 
						TO_DATE('04/07/2014 18:03:50', 'MM/DD/YYYY HH24:MI:SS'), 
						103, 
						TO_DATE('04/07/2014 18:03:50', 'MM/DD/YYYY HH24:MI:SS'), 
						'defeudise', 
						TO_DATE('04/07/2014 18:24:53', 'MM/DD/YYYY HH24:MI:SS'), 
						SYSDATE)
			 */
			Date start = new Date();
			LOG.info("Inicio Insercion: " + sdfForDate.format(start));	
			LOG.info("Sentencia: " + sqlInsertClienteRiesgoCrediticioHistorico);
			registrosInsertados = super.getJdbcTemplate().update(sqlInsertClienteRiesgoCrediticioHistorico);
			Date end = new Date();
			LOG.info("Fin Insercion: " + sdfForDate.format(end));		
			Date difference = Util.differenceBetweenDates(start, end);
			
			StringBuilder builder = new StringBuilder();
			builder.append("Tiempo de ejecucion de: ");
			builder.append(sqlInsertClienteRiesgoCrediticioHistorico);

			builder.append(" ha sido de: ");
			builder.append(sdfForDateDifference.format(difference));
			builder.append(". Se inserto " + registrosInsertados + " registro/s");	

			LOG.info(builder);
		}catch(Exception e){
			throw e;
		}	
	}
	
	public void modificacionClienteRiesgoCrediticioFinal(ClienteRiesgoCrediticioDTO clienteRiesgoCrediticio)throws Exception{
		SimpleDateFormat sdfForDate = new SimpleDateFormat(Util.DATE_TIME_PATTERN);
		SimpleDateFormat sdfForDateDifference = new SimpleDateFormat(Util.TIME_PATETRN);
		int registrosActualizados = 0;
		String score = validarNulos(clienteRiesgoCrediticio.getScoreManual()).trim();
		try {
			String sqlUpdateClienteRiesgoCrediticioFinal = "UPDATE RC_RIESGO_CREDITICIO_FINAL"
					+ " SET PARTY_CREDIT_SCORE_M_IND=";
					if (score.equals(Util.EMPTY_CHARACTER)){
						sqlUpdateClienteRiesgoCrediticioFinal += Util.NULL_CHARACTER + ",";
					}else{
						sqlUpdateClienteRiesgoCrediticioFinal += score + ",";				
					}						
					sqlUpdateClienteRiesgoCrediticioFinal += " UPDATE_MANUAL_M_DT="		
					+ clienteRiesgoCrediticio.getFechaCargaScoreManual() + ","
					+ " USER_UPDATE_M_CD="
					+ "'" + clienteRiesgoCrediticio.getUsuarioCargaCambio() + "', "
					+ " LAST_UPDATED_F_DT="
					+ clienteRiesgoCrediticio.getFechaUltimoUpdate()
					+ " WHERE"
					+ " PARTY_IDENTIFICATION_TYPE_CD = "
				    + "'" + clienteRiesgoCrediticio.getTipoDoc() + "'"
				    + " AND PARTY_IDENTIFICATION_NUM = "
					+ "'" + clienteRiesgoCrediticio.getNroDoc() + "'";		
			/*Ejemplo:
			 * UPDATE RC_RIESGO_CREDITICIO_FINAL SET PARTY_CREDIT_SCORE_M_IND=102, UPDATE_MANUAL_M_DT=SYSDATE, USER_UPDATE_M_CD='prueba', LAST_UPDATED_F_DT=SYSDATE
			 * WHERE PARTY_IDENTIFICATION_TYPE_CD = 'DNI' AND PARTY_IDENTIFICATION_NUM = '23458091'
			 */
			Date start = new Date();
			LOG.info("Inicio Modificacion: " + sdfForDate.format(start));	
			LOG.info("Sentencia: " + sqlUpdateClienteRiesgoCrediticioFinal);
			registrosActualizados = super.getJdbcTemplate().update(sqlUpdateClienteRiesgoCrediticioFinal);
			Date end = new Date();
			LOG.info("Fin Modificacion: " + sdfForDate.format(end));		
			Date difference = Util.differenceBetweenDates(start, end);
			
			StringBuilder builder = new StringBuilder();
			builder.append("Tiempo de ejecucion de: ");
			builder.append(sqlUpdateClienteRiesgoCrediticioFinal);
			builder.append(" ha sido de: ");
			builder.append(sdfForDateDifference.format(difference));
			builder.append(". Se actualizo " + registrosActualizados + " registro/s");

			LOG.info(builder);
		}catch(Exception e){
			throw e;
		}		
	}

	public List<ClienteRiesgoCrediticioDTO> obtenerReporteRiesgosCrediticiosManuales(ExportCSVDTO dto) throws Exception{
		
		
		LOG.info("Bajada reporte");
		
		String sqlFinal = "";
		
		String fechaDesde= dto.getFechaDesde()+" 12:00:00 AM";
		String fechaHasta= dto.getFechaHasta()+" 11:59:59 PM";
		
		LOG.info("Inicia bajada reporte  fecha desde: " +fechaDesde +" fechaHasta: " + fechaHasta);
		
		sqlFinal = "SELECT PARTY_IDENTIFICATION_TYPE_CD, PARTY_IDENTIFICATION_NUM," 
			   + "PARTY_GENDER_TYPE_CD, PARTY_CREDIT_SCORE_IN_IND, LAST_UPDATED_IN_DT," 
			   + "PARTY_CREDIT_SCORE_AM_IND, LAST_UPDATED_AM_DT, PARTY_CREDIT_SCORE_M_IND,"
			   + "UPDATE_MANUAL_M_DT, USER_UPDATE_M_CD, LAST_UPDATED_F_DT FROM RC_RIESGO_CREDITICIO_FINAL" 
			   + " WHERE UPDATE_MANUAL_M_DT IS NOT NULL AND UPDATE_MANUAL_M_DT BETWEEN to_date( ? ,'dd/mm/yyyy hh12:MI:ss AM')"
			   + " AND  to_date( ? ,'dd/mm/yyyy hh12:MI:ss PM')";

		

		Object[] args = new Object[] {fechaDesde, fechaHasta }; 
		int[] argTypes = new int[] { Types.VARCHAR, Types.VARCHAR };
		
		RowCallbackHandler rowMapper= new BajadaResulSetMapper(dto, super.getJdbcTemplate());
		super.getJdbcTemplate().query(sqlFinal, args, rowMapper);
	
		LOG.info("Finaliza bajada reporte  fecha desde: " +fechaDesde +" fechaHasta: " + fechaHasta);
		
		return null;		
	}
	
	public ClienteRiesgoCrediticioDTO conversorTipoDoc(ClienteRiesgoCrediticioDTO clienteRiesgoCrediticio){
		
		List<TipoDocumentoDTO> listaTiposDocumento= null;
		Map<String,String> conversionTipoDocumento_DW_AMDOCS = new HashMap<String,String> ();
			
			try{
				listaTiposDocumento = obtenerTiposDocumento();
			}catch (Exception e){
				e.printStackTrace();
			}
			
			if (listaTiposDocumento != null){
				for(int i=0;i<listaTiposDocumento.size();i++){
					conversionTipoDocumento_DW_AMDOCS.put(listaTiposDocumento.get(i).getTipoDocDW(), listaTiposDocumento.get(i).getTipoDocAmdoc());
				}
			}
			
			if(conversionTipoDocumento_DW_AMDOCS.containsKey(clienteRiesgoCrediticio.getTipoDoc()) == true){
				//Si el tipo de Doc esta en el key del hashMap conversionTipoDocumento_DW_AMDOCS, entonces, es de tipo DW
				clienteRiesgoCrediticio.setTipoDocAMDOCS(conversionTipoDocumento_DW_AMDOCS.get(clienteRiesgoCrediticio.getTipoDoc()));
			}else{
				clienteRiesgoCrediticio.setTipoDocAMDOCS(clienteRiesgoCrediticio.getTipoDoc());
			}
		
		return clienteRiesgoCrediticio;
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

	public List<ClienteRiesgoCrediticioDTO> bajadaTablaRCrediticioFinal (ExportCSVDTO dto){
		
		List<ClienteRiesgoCrediticioDTO> listBajadaTablaRCrediticioFinal = null;
	
		String sqlFinal = "";
		
		String fechaDesde= dto.getFechaDesde()+" 12:00:00 AM";
		String fechaHasta= dto.getFechaHasta()+" 11:59:59 PM";
		
		sqlFinal = "SELECT PARTY_IDENTIFICATION_TYPE_CD, PARTY_IDENTIFICATION_NUM," 
			   + "PARTY_GENDER_TYPE_CD, PARTY_CREDIT_SCORE_IN_IND, LAST_UPDATED_IN_DT," 
			   + "PARTY_CREDIT_SCORE_AM_IND, LAST_UPDATED_AM_DT, PARTY_CREDIT_SCORE_M_IND,"
			   + "UPDATE_MANUAL_M_DT, USER_UPDATE_M_CD, LAST_UPDATED_F_DT FROM RC_RIESGO_CREDITICIO_FINAL" 
			   + " WHERE UPDATE_MANUAL_M_DT IS NOT NULL AND UPDATE_MANUAL_M_DT BETWEEN to_date( ? ,'dd/mm/yyyy hh12:MI:ss AM')"
			   + " AND  to_date( ? ,'dd/mm/yyyy hh12:MI:ss PM')";

		

		Object[] args = new Object[] {fechaDesde, fechaHasta }; 
		int[] argTypes = new int[] { Types.VARCHAR, Types.VARCHAR };
		
		RowCallbackHandler rowMapper= new BajadaResulSetMapper(dto, super.getJdbcTemplate());
		super.getJdbcTemplate().query(sqlFinal, args, rowMapper);
	
		
	return listBajadaTablaRCrediticioFinal;
	}
	
/*public List<ClienteRiesgoCrediticioDTO> bajadaTablaRCrediticioFinal (String fechaDesde, String fechaHasta){
		
		List<ClienteRiesgoCrediticioDTO> listBajadaTablaRCrediticioFinal = null;
	
		String sqlFinal = "";
		
		sqlFinal = "SELECT PARTY_IDENTIFICATION_TYPE_CD, PARTY_IDENTIFICATION_NUM," 
			   + "PARTY_GENDER_TYPE_CD, PARTY_CREDIT_SCORE_IN_IND, LAST_UPDATED_IN_DT," 
			   + "PARTY_CREDIT_SCORE_AM_IND, LAST_UPDATED_AM_DT, PARTY_CREDIT_SCORE_M_IND,"
			   + "UPDATE_MANUAL_M_DT, USER_UPDATE_M_CD, LAST_UPDATED_F_DT FROM RC_RIESGO_CREDITICIO_FINAL" 
			   + " WHERE UPDATE_MANUAL_M_DT IS NOT NULL AND UPDATE_MANUAL_M_DT >= to_date('" + fechaDesde + " 12:00:00 AM','dd/mm/yyyy hh12:MI:ss AM')"
			   + " AND  UPDATE_MANUAL_M_DT <= to_date('" + fechaHasta + " 11:59:59 PM','dd/mm/yyyy hh12:MI:ss PM')";

		
	
	
		listBajadaTablaRCrediticioFinal = super.getJdbcTemplate().query(sqlFinal, 
			new RowMapper<ClienteRiesgoCrediticioDTO>() {
				public ClienteRiesgoCrediticioDTO mapRow(ResultSet rs, int rowNum) throws SQLException {							
					ClienteRiesgoCrediticioDTO clienteRiesgoCrediticio = new ClienteRiesgoCrediticioDTO();
					try{
						clienteRiesgoCrediticio.setTipoDoc(validarNulos(rs.getString(1)));
						clienteRiesgoCrediticio.setNroDoc(validarNulos(rs.getString(2)));
						clienteRiesgoCrediticio.setGenero(validarNulos(rs.getString(3)));
						clienteRiesgoCrediticio.setScoreCalculadoIN(validarNulos(rs.getString(4)));
						clienteRiesgoCrediticio.setFechaCargaScoreIN(rs.getString(5));
						clienteRiesgoCrediticio.setScoreLegadoAM(validarNulos(rs.getString(6)));
						clienteRiesgoCrediticio.setFechaUpdateAM(rs.getString(7));
						clienteRiesgoCrediticio.setScoreManual(validarNulos(rs.getString(8)));
						clienteRiesgoCrediticio.setFechaCargaScoreManual(rs.getString(9));
						clienteRiesgoCrediticio.setUsuarioCargaCambio(validarNulos(rs.getString(10)));
						clienteRiesgoCrediticio.setFechaUltimoUpdate(validarNulos(rs.getString(11)));
					
					}catch(NullPointerException e2){
						throw new NullPointerException(e2 + "");
					}					
					
					return clienteRiesgoCrediticio;
				}

			});
	
	return listBajadaTablaRCrediticioFinal;
	}*/
	
	public ClienteRiesgoCrediticioDTO bajadaUltimoScoredPorMaxFecha (String tipoDoc, String numDoc, String genero){
		
		String tipoDocNull;
		String numDocNull;
		String campoGenero;
		
		ClienteRiesgoCrediticioDTO clienteRiesgoCrediticioFinal = null;
		
		if(genero.equals("")){
			campoGenero = " is null";
		}else{
			campoGenero = "='" + genero + "'";
		}
		
		String sqlConsultaRCrediticioFinal = "select PARTY_IDENTIFICATION_TYPE_CD, PARTY_IDENTIFICATION_NUM, PARTY_GENDER_TYPE_CD," 
										   + "PARTY_CREDIT_SCORE_IN_IND, LAST_UPDATED_IN_DT, PARTY_CREDIT_SCORE_AM_IND, LAST_UPDATED_AM_DT," 
										   + "PARTY_CREDIT_SCORE_M_IND, UPDATE_MANUAL_M_DT, USER_UPDATE_M_CD, LAST_UPDATED_F_DT"
										   + " from RC_RIESGO_CREDITICIO_HIST where last_updated_dt =" 
										   + "(select max(last_updated_dt) from RC_RIESGO_CREDITICIO_HIST"
										   + " where party_identification_type_cd ='" + tipoDoc + "' "
										   + "and party_identification_num ='" + numDoc + "' "
										   + "and party_gender_type_cd" + campoGenero + ") "
										   + "and party_identification_type_cd ='" + tipoDoc + "' "
										   + "and party_identification_num ='" + numDoc + "' "
										   + "and party_gender_type_cd" + campoGenero;
		

		
		tipoDocNull = validarNulos(tipoDoc);
		numDocNull = validarNulos(numDoc);
		
		if(!tipoDocNull.equals("") && !numDocNull.equals("")){
			
			clienteRiesgoCrediticioFinal = new ClienteRiesgoCrediticioDTO();

			try{
				clienteRiesgoCrediticioFinal = (ClienteRiesgoCrediticioDTO) super.getJdbcTemplate().queryForObject(sqlConsultaRCrediticioFinal,
						new RowMapper<ClienteRiesgoCrediticioDTO>() {
							public ClienteRiesgoCrediticioDTO mapRow(ResultSet rs, int rowNum) throws SQLException {							
								ClienteRiesgoCrediticioDTO clienteRiesgoCrediticio = new ClienteRiesgoCrediticioDTO();
								try{
									clienteRiesgoCrediticio.setTipoDoc(validarNulos(rs.getString(1)));
									clienteRiesgoCrediticio.setNroDoc(validarNulos(rs.getString(2)));
									clienteRiesgoCrediticio.setGenero(validarNulos(rs.getString(3)));
									clienteRiesgoCrediticio.setScoreCalculadoIN(validarNulos(rs.getString(4)));
									clienteRiesgoCrediticio.setFechaCargaScoreIN(rs.getString(5));
									clienteRiesgoCrediticio.setScoreLegadoAM(validarNulos(rs.getString(6)));
									clienteRiesgoCrediticio.setFechaUpdateAM(rs.getString(7));
									clienteRiesgoCrediticio.setScoreManual(validarNulos(rs.getString(8)));
									clienteRiesgoCrediticio.setFechaCargaScoreManual(rs.getString(9));
									clienteRiesgoCrediticio.setUsuarioCargaCambio(validarNulos(rs.getString(10)));
									clienteRiesgoCrediticio.setFechaUltimoUpdate(validarNulos(rs.getString(11)));
								
								}catch(NullPointerException e){
									throw new NullPointerException(e + "");
								}					
								
								return clienteRiesgoCrediticio;
							}
	
						});
			}catch(EmptyResultDataAccessException e){
				return null;
			}
		}
		
		return clienteRiesgoCrediticioFinal;
	}
	
}
