package ar.com.latam.creditCore.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import ar.com.latam.creditCore.dao.interfaz.ITiposDocumentoDAO;
import ar.com.latam.creditCore.dto.TipoDocumentoDTO;
import ar.com.latam.util.Util;

/**
 * 
 * @author diana.karina.rojas
 *
 */
public class TiposDocumentoDAO extends JdbcDaoSupport implements ITiposDocumentoDAO {
	
	private static final Logger LOG = Logger.getLogger(TiposDocumentoDAO.class);

	public List<TipoDocumentoDTO> obtenerTiposDocumento() throws Exception{
		List<TipoDocumentoDTO> listaTiposDocumento= null;
		SimpleDateFormat sdfForDate = new SimpleDateFormat(Util.DATE_TIME_PATTERN);
		SimpleDateFormat sdfForDateDifference = new SimpleDateFormat(Util.TIME_PATETRN);	
		try {
			
			String sqlListaTiposDocumento = "SELECT PARTY_IDENTIFICATION_TYPE_DWCD, "// 1
				    + "PARTY_IDENTIFICATION_TYPE_CD, "// 2
				    + "OBLIG_PARTY_GENDER_TYPE_CD, "//3
				    + "LAST_UPDATED_DT "// 4 
					+ "FROM RC_LKP_TIPO_DOC";					

			//resultado: lista de Tipos Documento
			Date start = new Date();
			LOG.info("Inicio Consulta: " + sdfForDate.format(start));	
			LOG.info("Sentencia: " + sqlListaTiposDocumento);
			listaTiposDocumento = super.getJdbcTemplate().query(sqlListaTiposDocumento,
					new RowMapper<TipoDocumentoDTO>() {
						public TipoDocumentoDTO mapRow(ResultSet rs, int rowNum) throws SQLException {							
							TipoDocumentoDTO tipoDocumento = new TipoDocumentoDTO();
							try{
								tipoDocumento.setTipoDocDW(validarNulos(rs.getString(1)));
								tipoDocumento.setTipoDocDWBackup(validarNulos(rs.getString(1)));
								tipoDocumento.setTipoDocAmdoc(validarNulos(rs.getString(2)));
								tipoDocumento.setTipoDocAmdocBackup(validarNulos(rs.getString(2)));
								tipoDocumento.setCargaGenero(validarNulos(rs.getString(3)));
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
	
	public void modificacionTipoDoc(TipoDocumentoDTO tipoDocumento)throws Exception{
		SimpleDateFormat sdfForDate = new SimpleDateFormat(Util.DATE_TIME_PATTERN);
		SimpleDateFormat sdfForDateDifference = new SimpleDateFormat(Util.TIME_PATETRN);
		int registrosActualizados = 0;
		try {
			String sqlUpdateTipoDoc = "UPDATE RC_LKP_TIPO_DOC"
					+ " SET PARTY_IDENTIFICATION_TYPE_DWCD="
					+ "'" + tipoDocumento.getTipoDocDW().trim() + "',"
					+ " PARTY_IDENTIFICATION_TYPE_CD="
					+ "'" + tipoDocumento.getTipoDocAmdoc().trim() + "',"
					+ " OBLIG_PARTY_GENDER_TYPE_CD="
					+ "'" + tipoDocumento.getCargaGenero().trim() + "',"
					+ " USER_UPDATE_CD="
					+ "'" + tipoDocumento.getUserCarga().trim() + "',"
					+ " LAST_UPDATED_DT="
					+ tipoDocumento.getFechaCarga()
					+ " WHERE"
					+ " PARTY_IDENTIFICATION_TYPE_DWCD = "
				    + "'" + tipoDocumento.getTipoDocDWBackup() + "'"
				    + " AND PARTY_IDENTIFICATION_TYPE_CD = "
					+ "'" + tipoDocumento.getTipoDocAmdocBackup() + "'";
			/*Ejemplo:
			 * UPDATE RC_LKP_TIPO_DOC SET PARTY_IDENTIFICATION_TYPE_DWCD='PA', PARTY_IDENTIFICATION_TYPE_CD='Passport', OBLIG_PARTY_GENDER_TYPE_CD='S', USER_UPDATE_CD='prueba', LAST_UPDATED_DT=SYSDATE
			 * WHERE PARTY_IDENTIFICATION_TYPE_DWCD = 'PS' AND PARTY_IDENTIFICATION_TYPE_CD = 'Passport'
			 */
			Date start = new Date();
			LOG.info("Inicio Modificacion: " + sdfForDate.format(start));	
			LOG.info("Sentencia: " + sqlUpdateTipoDoc);
			registrosActualizados = super.getJdbcTemplate().update(sqlUpdateTipoDoc);
			Date end = new Date();
			LOG.info("Fin Modificacion: " + sdfForDate.format(end));		
			Date difference = Util.differenceBetweenDates(start, end);
			
			StringBuilder builder = new StringBuilder();
			builder.append("Tiempo de ejecucion de: ");
			builder.append(sqlUpdateTipoDoc);

			builder.append(" ha sido de: ");
			builder.append(sdfForDateDifference.format(difference));
			builder.append(". Se actualizo " + registrosActualizados + " registro/s");						

			LOG.info(builder);
		}catch(Exception e){
			throw e;
		}		
	}
	
	public void altaTipoDoc(TipoDocumentoDTO tipoDocumento)throws Exception{
		SimpleDateFormat sdfForDate = new SimpleDateFormat(Util.DATE_TIME_PATTERN);
		SimpleDateFormat sdfForDateDifference = new SimpleDateFormat(Util.TIME_PATETRN);	
		try {
			String sqlInsertTipoDoc = "INSERT INTO RC_LKP_TIPO_DOC "
				    + "(PARTY_IDENTIFICATION_TYPE_DWCD, PARTY_IDENTIFICATION_TYPE_CD, OBLIG_PARTY_GENDER_TYPE_CD, USER_UPDATE_CD, LAST_UPDATED_DT) "
				    + "VALUES ("
				    + "'" + tipoDocumento.getTipoDocDW().trim() + "', "
					+ "'" + tipoDocumento.getTipoDocAmdoc().trim() + "', "
					+ "'" + tipoDocumento.getCargaGenero().trim() + "', "
					+ "'" + tipoDocumento.getUserCarga() + "', "
					+ tipoDocumento.getFechaCarga() + ")";
			/*Ejemplo:
			 * INSERT INTO RC_LKP_TIPO_DOC (PARTY_IDENTIFICATION_TYPE_DWCD, PARTY_IDENTIFICATION_TYPE_CD, OBLIG_PARTY_GENDER_TYPE_CD, USER_UPDATE_CD, LAST_UPDATED_DT)
			 *  VALUES ('NV', 'NUEVO', 'N', 'defeudise', SYSDATE)
			 */
			Date start = new Date();
			LOG.info("Inicio Insercion: " + sdfForDate.format(start));	
			LOG.info("Sentencia: " + sqlInsertTipoDoc);
			super.getJdbcTemplate().execute(sqlInsertTipoDoc);
			Date end = new Date();
			LOG.info("Fin Insercion: " + sdfForDate.format(end));		
			Date difference = Util.differenceBetweenDates(start, end);
			
			StringBuilder builder = new StringBuilder();
			builder.append("Tiempo de ejecucion de: ");
			builder.append(sqlInsertTipoDoc);

			builder.append(" ha sido de: ");
			builder.append(sdfForDateDifference.format(difference));

			LOG.info(builder);
		}catch(Exception e){
			throw e;
		}		
	}
	
	public void eliminarTipoDoc(TipoDocumentoDTO tipoDocumento)throws Exception{
		SimpleDateFormat sdfForDate = new SimpleDateFormat(Util.DATE_TIME_PATTERN);
		SimpleDateFormat sdfForDateDifference = new SimpleDateFormat(Util.TIME_PATETRN);
		int registrosEliminados = 0;
		try {
			String sqlDeletetTipoDoc = "DELETE RC_LKP_TIPO_DOC"
				+ " WHERE"
				+ " PARTY_IDENTIFICATION_TYPE_DWCD = "
			    + "'" + tipoDocumento.getTipoDocDW() + "'"
			    + " AND PARTY_IDENTIFICATION_TYPE_CD = "
				+ "'" + tipoDocumento.getTipoDocAmdoc() + "'";
			/*Ejemplo:
			 * DELETE RC_LKP_TIPO_DOC WHERE PARTY_IDENTIFICATION_TYPE_DWCD = 'NV' AND PARTY_IDENTIFICATION_TYPE_CD = 'NUEVO'
			 */
			Date start = new Date();
			LOG.info("Inicio Eliminacion: " + sdfForDate.format(start));	
			LOG.info("Sentencia: " + sqlDeletetTipoDoc);
			registrosEliminados = super.getJdbcTemplate().update(sqlDeletetTipoDoc);
			Date end = new Date();
			LOG.info("Fin Eliminacion: " + sdfForDate.format(end));		
			Date difference = Util.differenceBetweenDates(start, end);
			
			StringBuilder builder = new StringBuilder();
			builder.append("Tiempo de ejecucion de: ");
			builder.append(sqlDeletetTipoDoc);

			builder.append(" ha sido de: ");
			builder.append(sdfForDateDifference.format(difference));
			builder.append(". Se elimino " + registrosEliminados + " registro/s");	

			LOG.info(builder);
		}catch(Exception e){
			throw e;
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
