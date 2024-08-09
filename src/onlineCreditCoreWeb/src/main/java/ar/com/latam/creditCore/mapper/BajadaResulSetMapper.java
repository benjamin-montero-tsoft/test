package ar.com.latam.creditCore.mapper;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;

import org.apache.log4j.Logger;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;

import ar.com.latam.creditCore.dto.ClienteRiesgoCrediticioDTO;
import ar.com.latam.creditCore.dto.ExportCSVDTO;


public class BajadaResulSetMapper implements RowCallbackHandler {
	
	static Logger log = Logger.getLogger(BajadaResulSetMapper.class);
	
	ExportCSVDTO dto;
	/** Titulo Reporte Manual.**/
    private static final String PREFIJO_TITULO_REPORTE_MANUAL = "AM_RIESGO_CREDITICIO_MANUAL_";
    
    JdbcTemplate jdbcTemplate;
    
	Map<String,String> conversionTipoDocumento_DW_AMDOCS;
	
	public BajadaResulSetMapper(ExportCSVDTO exportCSVDTO, JdbcTemplate jdbcTemplate){
		dto=exportCSVDTO;
		dto.getResponse().setContentType("application/octet-stream");
		dto.getResponse().setHeader("Content-Disposition","attachment;filename="+ getTitleReports() + ".CSV");
		conversionTipoDocumento_DW_AMDOCS=dto.getConversionTipoDocumento_DW_AMDOCS();
		this.jdbcTemplate=jdbcTemplate;
	}
	

	public void processRow(ResultSet rs) throws SQLException {
	
	
		ClienteRiesgoCrediticioDTO clienteRiesgoCrediticio = new ClienteRiesgoCrediticioDTO();
		clienteRiesgoCrediticio.setTipoDoc(validarNulos(rs.getString(1)));
		clienteRiesgoCrediticio.setNroDoc(validarNulos(rs.getString(2)));
		clienteRiesgoCrediticio.setGenero(validarNulos(rs.getString(3)));
		clienteRiesgoCrediticio.setScoreCalculadoIN(validarNulos(rs.getString(4)));
		clienteRiesgoCrediticio.setFechaCargaScoreIN(rs.getString(5));
		clienteRiesgoCrediticio.setScoreLegadoAM(validarNulos(rs.getString(6)));
		clienteRiesgoCrediticio.setScoreManual(validarNulos(rs.getString(8)));
		clienteRiesgoCrediticio.setFechaCargaScoreManual(rs.getString(9));
		clienteRiesgoCrediticio.setUsuarioCargaCambio(validarNulos(rs.getString(10)));
		clienteRiesgoCrediticio.setFechaUltimoUpdate(validarNulos(rs.getString(11)));

	
		try {
			ServletOutputStream outputStream = dto.getResponse().getOutputStream();
			
				try{
					  		
				          	conversorTipoDoc(clienteRiesgoCrediticio);
							escribirEnCSV(clienteRiesgoCrediticio, outputStream);
				            ClienteRiesgoCrediticioDTO clienteRiesgoCrediticioTemp =
				            	bajadaUltimoScoredPorMaxFecha(clienteRiesgoCrediticio.getTipoDoc(), 
				            			clienteRiesgoCrediticio.getNroDoc(), clienteRiesgoCrediticio.getGenero());
				        	if(clienteRiesgoCrediticioTemp!=null){
				        		
				        		conversorTipoDoc(clienteRiesgoCrediticioTemp);
				        		escribirEnCSV(clienteRiesgoCrediticioTemp, outputStream);
				        	}
					    outputStream.flush();
					    
				    }catch(Exception e){
				    	log.info("Error en createCSVReporteManual de ExportCSV");
				    	log.info(e);
				    	
				        if(rs!=null){
				        	rs.getStatement().cancel();
				        	rs.close();
				        }
				    
						throw e;
				    }
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
			
		
	}


	private void escribirEnCSV(
			ClienteRiesgoCrediticioDTO clienteRiesgoCrediticio,
			ServletOutputStream outputStream) throws IOException {
		StringBuffer rowC = createBody(clienteRiesgoCrediticio);
		outputStream.write((rowC.toString() + "\n").getBytes());
	}

	private StringBuffer createBody(ClienteRiesgoCrediticioDTO clienteRiesgoCrediticio){
		StringBuffer sb = new StringBuffer("");
		sb.append(clienteRiesgoCrediticio.getTipoDocAMDOCS()+ ",");
        sb.append(clienteRiesgoCrediticio.getNroDoc()+ ",");
        sb.append(clienteRiesgoCrediticio.getGenero()+ ",");
        sb.append(clienteRiesgoCrediticio.getScoreCalculadoIN()+ ",");
        sb.append(validarNulos(clienteRiesgoCrediticio.getFechaCargaScoreIN())+ ",");
        sb.append(clienteRiesgoCrediticio.getScoreLegadoAM()+ ",");
        sb.append(validarNulos(clienteRiesgoCrediticio.getFechaUpdateAM())+ ",");
        sb.append(clienteRiesgoCrediticio.getScoreManual()+ ",");
        sb.append(validarNulos(clienteRiesgoCrediticio.getFechaCargaScoreManual())+ ",");
        sb.append(clienteRiesgoCrediticio.getUsuarioCargaCambio()+ ",");
        sb.append(clienteRiesgoCrediticio.getFechaUltimoUpdate());

        return sb;
	}
	 private String getTitleReports(){
		  return PREFIJO_TITULO_REPORTE_MANUAL + getDateReports();
	   }
		  
	  public String getDateReports(){
		  /*AAAAMMDD_HHMMSS*/
	      SimpleDateFormat formato = new SimpleDateFormat("yyyyMMdd_HHmmss");
	      Date fechaActual = new Date();
	      return formato.format(fechaActual);
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
				clienteRiesgoCrediticioFinal = (ClienteRiesgoCrediticioDTO) jdbcTemplate.queryForObject(sqlConsultaRCrediticioFinal,
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

public ClienteRiesgoCrediticioDTO conversorTipoDoc(ClienteRiesgoCrediticioDTO clienteRiesgoCrediticio){
	
	if(conversionTipoDocumento_DW_AMDOCS.containsKey(clienteRiesgoCrediticio.getTipoDoc())){
			//Si el tipo de Doc esta en el key del hashMap conversionTipoDocumento_DW_AMDOCS, entonces, es de tipo DW
			clienteRiesgoCrediticio.setTipoDocAMDOCS(conversionTipoDocumento_DW_AMDOCS.get(clienteRiesgoCrediticio.getTipoDoc()));
		}else{
			clienteRiesgoCrediticio.setTipoDocAMDOCS(clienteRiesgoCrediticio.getTipoDoc());
		}
	
	return clienteRiesgoCrediticio;
}
}
