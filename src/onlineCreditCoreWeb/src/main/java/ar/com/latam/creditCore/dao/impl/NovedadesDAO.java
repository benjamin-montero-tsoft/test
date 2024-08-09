package ar.com.latam.creditCore.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import ar.com.latam.creditCore.dao.interfaz.INovedadesDAO;
import ar.com.latam.creditCore.dto.NovedadDTO;
import ar.com.latam.util.Util;

/**
 * 
 * @author diana.karina.rojas
 *
 */
public class NovedadesDAO extends JdbcDaoSupport implements INovedadesDAO {

	private static final Logger LOG = Logger.getLogger(NovedadesDAO.class);
	
	public List<NovedadDTO> consultaTodasNovedades() throws Exception{
		List<NovedadDTO> listaNovedades= null;
		SimpleDateFormat sdfForDate = new SimpleDateFormat(Util.DATE_TIME_PATTERN);
		SimpleDateFormat sdfForDateDifference = new SimpleDateFormat(Util.TIME_PATETRN);	
		try {
			
			String sqlListaNovedades = "SELECT PARTY_IDENTIFICATION_TYPE_CD, "// 1
				    + "PARTY_IDENTIFICATION_NUM, "// 2
				    + "PARTY_GENDER_TYPE_CD, "// 3
				    + "PARTY_CREDIT_SCORE_IND, "// 4
				    + "ERROR_DESC, "// 5
				    + "LAST_UPDATED_IN_DT "// 6	    
					+ "FROM RC_FEEDBACK_AM"/*  WHERE PARTY_IDENTIFICATION_TYPE_CD = ?"
					+ " and PARTY_IDENTIFICATION_NUM = ?"*/
					+ " order by LAST_UPDATED_IN_DT desc";

			//resultado: lista de Novedades
			/*LOG.info("PARAMETROS:");
			LOG.info("   - Tipo Documento: " + tipoDocumento.trim());
			LOG.info("   - Nro Documento: " + nroDocumento.trim());*/
			Date start = new Date();
			LOG.info("Inicio Consulta: " + sdfForDate.format(start));	
			LOG.info("Sentencia: " + sqlListaNovedades);
			listaNovedades = super.getJdbcTemplate().query(sqlListaNovedades,/*new String[] {tipoDocumento.trim(), nroDocumento.trim()},*/
					new RowMapper<NovedadDTO>() {
						public NovedadDTO mapRow(ResultSet rs, int rowNum) throws SQLException {							
							NovedadDTO novedad = new NovedadDTO();
							try{
								novedad.setTipoDoc(validarNulos(rs.getString(1)));
								novedad.setNroDoc(validarNulos(rs.getString(2)));
								novedad.setGenero(validarNulos(rs.getString(3)).trim());
								novedad.setScoreEnv(validarNulos(rs.getString(4)));
								novedad.setDescripcionError(validarNulos(rs.getString(5)));
								String fechaCarga = new String();
								//Pasar a un string con formato dd/MM/yyyy	
								if (rs.getDate(6) == null){
									fechaCarga = "";
								}else{
									fechaCarga = Util.getFormatoFecha(rs.getDate(6), Util.DATE_TIME_SCREEN);
								}
								novedad.setFechaCarga(fechaCarga);
							
							}catch(NullPointerException e2){
								throw new NullPointerException(e2 + "");
							}					
							
							return novedad;
						}

					});
			Date end = new Date();
			LOG.info("Fin Consulta: " + sdfForDate.format(end));		
			Date difference = Util.differenceBetweenDates(start, end);
			
			StringBuilder builder = new StringBuilder();
			builder.append("Tiempo de ejecucion de la consulta: ");
			builder.append(sqlListaNovedades);
			builder.append(" , que ha devuelto ");
			builder.append(listaNovedades.size());

			builder.append(" registro/s, ha sido de: ");
			builder.append(sdfForDateDifference.format(difference));

			LOG.info(builder);
			if (listaNovedades.isEmpty()) {
				return null;
			}
		}catch(Exception e){
			throw e;
		}				

		return listaNovedades;
	}	
	
	public String validarNulos(String dato){


		if (dato == null)
			return "";
		else
			return dato;
	}	
}
