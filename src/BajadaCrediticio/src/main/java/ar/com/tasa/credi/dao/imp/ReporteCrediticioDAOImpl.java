package ar.com.tasa.credi.dao.imp;

import ar.com.tasa.credi.beans.CrediLineRecord;
import ar.com.tasa.credi.beans.ReporteCrediticio;
import ar.com.tasa.credi.beans.TipoDocumentoDTO;
import ar.com.tasa.credi.dao.ReporteCrediticioDao;
import ar.com.tasa.credi.exception.ReporteCrediticioOnlineException;
import ar.com.tasa.credi.utils.Util;
import ar.com.telefonica.utils.Configuracion;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class ReporteCrediticioDAOImpl
  implements ReporteCrediticioDao
{
  static Logger log = Logger.getLogger(ReporteCrediticioDAOImpl.class);
  static Connection conn = null;
  
  public Connection getConnection()
    throws Exception
  {
    Configuracion configLoader = Configuracion.instance();
    configLoader.load("OnlineRCredi");
    log.info("ruta properties:" + configLoader.getPath());
    PropertyConfigurator.configure(configLoader.getPath());
    
    String dbDriver = configLoader.get("dbDriver");
    String dbConnString = configLoader.get("dbConnString");
    String server = configLoader.get("server");
    String puerto = configLoader.get("puerto");
    String base = configLoader.get("base");
    String dbUser = configLoader.get("dbUser");
    String dbPassword = configLoader.get("dbPassword");
    
    dbConnString = dbConnString + server + ":" + puerto + ":" + base;
    

    Class.forName(dbDriver).newInstance();
    

    Connection conn = DriverManager.getConnection(dbConnString, dbUser, dbPassword);
    conn.setAutoCommit(true);
    
    return conn;
  }
  
  public void consultarDatosCrediticios(ReporteCrediticio reporte, Map<String, String> conversionDW_AMDOCs)
    throws ParseException, ReporteCrediticioOnlineException, Exception
  {
    Configuracion configLoader = Configuracion.instance();
    configLoader.load("OnlineRCredi");
    PropertyConfigurator.configure(configLoader.getPath());
    String nombreArchivo = configLoader.get("DatafileName");
    String pathArchivo = configLoader.get("LocationDataFile");
    String extensionArchivo = configLoader.get("extensionFile");
    
    StringBuilder query = new StringBuilder();
    Statement stmt = null;
    ResultSet rs = null;
    int i = 1;
    try
    {
      String odate = reporte.getOdate();
      System.out.println("odte=  " + odate );
      conn = getConnection();
      stmt = conn.createStatement();
      query.append("select ");
      query.append("Party_Identification_Type_Cd, ");
      query.append("Party_Identification_Num, ");
      query.append("Party_Gender_Type_Cd, ");
      query.append("Party_Credit_Score_IN_Ind, ");
      query.append("Last_Updated_IN_Dt, ");
      query.append("Party_Credit_Score_AM_Ind, ");
      query.append("Last_Updated_AM_Dt, ");
      query.append("Party_Credit_Score_M_Ind, ");
      query.append("Update_Manual_M_Dt, ");
      query.append("User_Update_M_Cd, ");
      query.append("Last_Updated_F_Dt ");
      query.append("from RC_RIESGO_CREDITICIO_FINAL ");
      query.append("where LAST_UPDATED_IN_DT >= to_date('" + odate +
				" 00:00:00', 'YYYYMMDD HH24:MI:SS')" +
				" and LAST_UPDATED_IN_DT <= to_date('" +	odate +
				" 23:59:59', 'YYYYMMDD HH24:MI:SS') ");
      
      log.info("Query a ejecutar = " + query.toString());
      stmt.setFetchSize(10000);
      rs = stmt.executeQuery(query.toString());
      

      File fichero = new File(reporte.getNombreArchivo());
      log.info("ARCHIVO CSV: " + reporte.getNombreArchivo());
      
      FileWriter fileWriter = new FileWriter(fichero);
      BufferedWriter bufferWr = new BufferedWriter(fileWriter);
      while (rs.next())
      {
        if (i > 50000)
        {
          try
          {
            Thread.sleep(1000L);
          }
          catch (InterruptedException ex)
          {
            Thread.currentThread().interrupt();
          }
          bufferWr.flush();
          bufferWr.close();
          fileWriter.close();
          

          Date fecha = new Date();
          


          fichero = new File(Util.generarNombreArchivo(fecha, nombreArchivo, pathArchivo, extensionArchivo));
          log.info("ARCHIVO CSV: " + fichero);
          String rutaCompletaArchivoCSVTRIG = fichero + 
            ".trig";
          
          File fichero1 = new File(rutaCompletaArchivoCSVTRIG);
          log.info("ARCHIVO CSV: " + reporte.getNombreArchivo());
          
          FileWriter fw = new FileWriter(fichero1);
          
          fileWriter = new FileWriter(fichero);
          bufferWr = new BufferedWriter(fileWriter);
          
          i = 0;
        }
        CrediLineRecord registro = new CrediLineRecord();
        
        String last_Updated_IN_Dt_BASE = new String();
        if (rs.getDate("Last_Updated_IN_Dt") == null) {
          last_Updated_IN_Dt_BASE = "";
        } else {
          last_Updated_IN_Dt_BASE = Util.getFormatoFecha(rs.getTimestamp("Last_Updated_IN_Dt"), "MM/dd/yyyy HH:mm:ss");
        }
        String last_Updated_AM_Dt_BASE = new String();
        if (rs.getDate("Last_Updated_AM_Dt") == null) {
          last_Updated_AM_Dt_BASE = "";
        } else {
          last_Updated_AM_Dt_BASE = Util.getFormatoFecha(rs.getTimestamp("Last_Updated_AM_Dt"), "MM/dd/yyyy HH:mm:ss");
        }
        String update_Manual_M_Dt_BASE = new String();
        if (rs.getDate("Update_Manual_M_Dt") == null) {
          update_Manual_M_Dt_BASE = "";
        } else {
          update_Manual_M_Dt_BASE = Util.getFormatoFecha(rs.getTimestamp("Update_Manual_M_Dt"), "MM/dd/yyyy HH:mm:ss");
        }
        String last_Updated_F_Dt_BASE = new String();
        if (rs.getDate("Last_Updated_F_Dt") == null) {
          last_Updated_F_Dt_BASE = "";
        } else {
          last_Updated_F_Dt_BASE = Util.getFormatoFecha(rs.getTimestamp("Last_Updated_F_Dt"), "MM/dd/yyyy HH:mm:ss");
        }
        registro.setParty_Identification_Type_Cd(validarNulos(rs.getString("Party_Identification_Type_Cd")));
        registro.setParty_Identification_Num(validarNulos(rs.getString("Party_Identification_Num")));
        registro.setParty_Gender_Type_Cd(validarNulos(rs.getString("Party_Gender_Type_Cd")));
        registro.setParty_Credit_Score_IN_Ind(validarNulos(rs.getString("Party_Credit_Score_IN_Ind")));
        registro.setLast_Updated_IN_Dt(last_Updated_IN_Dt_BASE);
        registro.setParty_Credit_Score_AM_Ind(validarNulos(rs.getString("Party_Credit_Score_AM_Ind")));
        registro.setLast_Updated_AM_Dt(last_Updated_AM_Dt_BASE);
        registro.setParty_Credit_Score_M_Ind(validarNulos(rs.getString("Party_Credit_Score_M_Ind")));
        registro.setUpdate_Manual_M_Dt(update_Manual_M_Dt_BASE);
        registro.setUser_Update_M_Cd(validarNulos(rs.getString("User_Update_M_Cd")));
        registro.setLast_Updated_F_Dt(last_Updated_F_Dt_BASE);
        

        CrediLineRecord crediLineRecord = procesarRegistroFinal(registro, conversionDW_AMDOCs, i);
        
        bufferWr.append(crediLineRecord.toString() + "\r\n");
        
        i++;
      }
      bufferWr.flush();
      bufferWr.close();
      fileWriter.close();
    }
    catch (Exception e)
    {
      log.error("Error: Sale por excepción en el metodo consultarDatosCrediticios " + e.getMessage());
      e.printStackTrace();
      throw e;
    }
    finally
    {
      try
      {
        if (rs != null) {
          rs.close();
        }
      }
      catch (SQLException e)
      {
        e.printStackTrace();
      }
      try
      {
        if (stmt != null) {
          stmt.close();
        }
      }
      catch (SQLException e)
      {
        e.printStackTrace();
      }
      try
      {
        if (conn != null) {
          conn.close();
        }
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
    StringBuilder builder = new StringBuilder();
    builder.append("Ha devuelto ");
    builder.append(i - 1);
    builder.append(" registro/s");
    
    log.info(builder);
  }
  
  public CrediLineRecord procesarRegistroFinal(CrediLineRecord crediLineRecord, Map<String, String> conversionTipoDocumento_DW_AMDOCS, int contador)
    throws Exception
  {
    boolean borrarScoreManual = false;
    boolean borrarScoreAMDOCs = false;
    if (("".equals(crediLineRecord.getParty_Credit_Score_IN_Ind())) && ("".equals(crediLineRecord.getParty_Credit_Score_M_Ind())))
    {
      borrarScoreAMDOCs = true;
      crediLineRecord.setScoreEnviadoAMDOCs("NULL");
      //log.info("Registro: " + contador + ". Se borrara el score de AMDOCs y se enviara un score NULL a AMDOCs");
    }
    else if ("".equals(crediLineRecord.getParty_Credit_Score_IN_Ind()))
    {
      borrarScoreManual = true;
      crediLineRecord.setScoreEnviadoAMDOCs("NULL");
      //log.info("Registro: " + contador + ". Se borrara el score Manual y se enviara un score NULL a AMDOCs");
    }
    else if ("".equals(crediLineRecord.getParty_Credit_Score_M_Ind()))
    {
      crediLineRecord.setScoreEnviadoAMDOCs(validarNulos(crediLineRecord.getParty_Credit_Score_IN_Ind()));
      //log.info("Registro: " + contador + ". Se enviara el score de DW a AMDOCs");
    }
    else
    {
      int scoreManual = Integer.parseInt(crediLineRecord.getParty_Credit_Score_M_Ind());
      if (((scoreManual >= 0) && (scoreManual <= 99)) || ((scoreManual >= 951) && (scoreManual <= 999)))
      {
        crediLineRecord.setScoreEnviadoAMDOCs(crediLineRecord.getParty_Credit_Score_M_Ind());
        //log.info("Registro: " + contador + ". Se enviara el score Manual a AMDOCs");
      }
      else if ((scoreManual >= 100) && (scoreManual <= 950))
      {
        borrarScoreManual = true;
        crediLineRecord.setScoreEnviadoAMDOCs(crediLineRecord.getParty_Credit_Score_IN_Ind());
        //log.info("Registro: " + contador + ". Se borrara el score Manual y se enviara el score de DW a AMDOCs");
      }
    }
    if (borrarScoreManual) {
      borrarScoreManualEnTablas(crediLineRecord);
    }
    if (borrarScoreAMDOCs) {
      borrarScoreAMDOCsEnTablas(crediLineRecord);
    }
    if (!conversionTipoDocumento_DW_AMDOCS.isEmpty()) {
      if (conversionTipoDocumento_DW_AMDOCS.containsKey(crediLineRecord.getParty_Identification_Type_Cd())) {
        crediLineRecord.setTipoDocFormatoAMDOCs((String)conversionTipoDocumento_DW_AMDOCS.get(crediLineRecord.getParty_Identification_Type_Cd()));
      } else {
        crediLineRecord.setTipoDocFormatoAMDOCs(crediLineRecord.getParty_Identification_Type_Cd());
      }
    }
    if ((crediLineRecord.getParty_Gender_Type_Cd() == null) || (crediLineRecord.getParty_Gender_Type_Cd().equals("")) || 
      (crediLineRecord.getParty_Gender_Type_Cd().equals(" "))) {
      crediLineRecord.setParty_Gender_Type_Cd("Please Specify");
    }
    return crediLineRecord;
  }
  
  public void borrarScoreAMDOCsEnTablas(CrediLineRecord clienteRiesgoCrediticio)
    throws Exception
  {
    try
    {
      altaClienteRiesgoCrediticioHistorico(clienteRiesgoCrediticio);
    }
    catch (Exception e1)
    {
      log.info("Error en altaClienteRiesgoCrediticioHistorico de riesgoCrediticioDAO");
      log.info(e1);
      throw e1;
    }
    try
    {
      borrarScoreAMDOCsEnClienteRiesgoCrediticioFinal(clienteRiesgoCrediticio);
    }
    catch (Exception e2)
    {
      log.info("Error en borrarScoreAMDOCsEnClienteRiesgoCrediticioFinal de riesgoCrediticioDAO");
      log.info(e2);
      throw e2;
    }
  }
  
  public void borrarScoreManualEnTablas(CrediLineRecord clienteRiesgoCrediticio)
    throws Exception
  {
    try
    {
      altaClienteRiesgoCrediticioHistorico(clienteRiesgoCrediticio);
    }
    catch (Exception e1)
    {
      log.info("Error en altaClienteRiesgoCrediticioHistorico de riesgoCrediticioDAO");
      log.info(e1);
      throw e1;
    }
    try
    {
      borrarScoreManualEnClienteRiesgoCrediticioFinal(clienteRiesgoCrediticio);
    }
    catch (Exception e2)
    {
      log.info("Error en borrarScoreManualEnClienteRiesgoCrediticioFinal de riesgoCrediticioDAO");
      log.info(e2);
      throw e2;
    }
    try
    {
      borrarScoreManualEnClienteRiesgoCrediticioManual(clienteRiesgoCrediticio);
    }
    catch (Exception e3)
    {
      log.info("Error en borrarScoreManualEnClienteRiesgoCrediticioManual de riesgoCrediticioDAO");
      log.info(e3);
      throw e3;
    }
  }
  
  public List<TipoDocumentoDTO> obtenerTiposDocumento()
    throws Exception
  {
    List<TipoDocumentoDTO> listaTiposDocumento = new ArrayList();
    
    String sqlListaTiposDocumento = null;
    ResultSet rs = null;
    Statement stmt = null;
    try
    {
      conn = getConnection();
      stmt = conn.createStatement();
      sqlListaTiposDocumento = "SELECT PARTY_IDENTIFICATION_TYPE_DWCD, PARTY_IDENTIFICATION_TYPE_CD FROM RC_LKP_TIPO_DOC order by PARTY_IDENTIFICATION_TYPE_CD asc";
      





      log.info("Query a ejecutar = " + sqlListaTiposDocumento);
      
      rs = stmt.executeQuery(sqlListaTiposDocumento);
      while (rs.next())
      {
        TipoDocumentoDTO tipoDocumento = new TipoDocumentoDTO();
        
        tipoDocumento.setTipoDocDW(validarNulos(rs.getString(1)));
        tipoDocumento.setTipoDocAmdoc(validarNulos(rs.getString(2)));
        listaTiposDocumento.add(tipoDocumento);
      }
    }
    catch (Exception e)
    {
      log.error("Error: Sale por excepción en el metodo obtenerTiposDocumento " + e.getMessage());
      e.printStackTrace();
      throw e;
    }
    finally
    {
      try
      {
        if (rs != null) {
          rs.close();
        }
      }
      catch (SQLException e)
      {
        e.printStackTrace();
      }
      try
      {
        if (stmt != null) {
          stmt.close();
        }
      }
      catch (SQLException e)
      {
        e.printStackTrace();
      }
      try
      {
        if (conn != null) {
          conn.close();
        }
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
    StringBuilder builder = new StringBuilder();
    builder.append("Ha devuelto ");
    builder.append(listaTiposDocumento.size());
    builder.append(" registro/s");
    
    log.info(builder);
    
    return listaTiposDocumento;
  }
  
  public void altaClienteRiesgoCrediticioHistorico(CrediLineRecord clienteRiesgoCrediticio)
    throws Exception
  {
    SimpleDateFormat sdfForDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSSS");
    SimpleDateFormat sdfForDateDifference = new SimpleDateFormat("HH:mm:ss.SSS");
    int registrosInsertados = 0;
    Statement stmt = null;
    try
    {
      conn = getConnection();
      stmt = conn.createStatement();
      
      String sqlInsertClienteRiesgoCrediticioHistorico = "INSERT INTO RC_RIESGO_CREDITICIO_HIST (PARTY_IDENTIFICATION_TYPE_CD, PARTY_IDENTIFICATION_NUM, ";
      if (!"".equals(clienteRiesgoCrediticio.getParty_Gender_Type_Cd())) {
        sqlInsertClienteRiesgoCrediticioHistorico = sqlInsertClienteRiesgoCrediticioHistorico + "PARTY_GENDER_TYPE_CD, ";
      }
      if (!"".equals(clienteRiesgoCrediticio.getParty_Credit_Score_IN_Ind())) {
        sqlInsertClienteRiesgoCrediticioHistorico = sqlInsertClienteRiesgoCrediticioHistorico + "PARTY_CREDIT_SCORE_IN_IND, ";
      }
      if (!"".equals(clienteRiesgoCrediticio.getLast_Updated_IN_Dt())) {
        sqlInsertClienteRiesgoCrediticioHistorico = sqlInsertClienteRiesgoCrediticioHistorico + "LAST_UPDATED_IN_DT, ";
      }
      if (!"".equals(clienteRiesgoCrediticio.getParty_Credit_Score_AM_Ind())) {
        sqlInsertClienteRiesgoCrediticioHistorico = sqlInsertClienteRiesgoCrediticioHistorico + "PARTY_CREDIT_SCORE_AM_IND, ";
      }
      if (!"".equals(clienteRiesgoCrediticio.getLast_Updated_AM_Dt())) {
        sqlInsertClienteRiesgoCrediticioHistorico = sqlInsertClienteRiesgoCrediticioHistorico + "LAST_UPDATED_AM_DT, ";
      }
      if (!"".equals(clienteRiesgoCrediticio.getParty_Credit_Score_M_Ind())) {
        sqlInsertClienteRiesgoCrediticioHistorico = sqlInsertClienteRiesgoCrediticioHistorico + "PARTY_CREDIT_SCORE_M_IND, ";
      }
      if (!"".equals(clienteRiesgoCrediticio.getUpdate_Manual_M_Dt())) {
        sqlInsertClienteRiesgoCrediticioHistorico = sqlInsertClienteRiesgoCrediticioHistorico + "UPDATE_MANUAL_M_DT, ";
      }
      sqlInsertClienteRiesgoCrediticioHistorico = sqlInsertClienteRiesgoCrediticioHistorico + "USER_UPDATE_M_CD, ";
      if (!"".equals(clienteRiesgoCrediticio.getLast_Updated_F_Dt())) {
        sqlInsertClienteRiesgoCrediticioHistorico = sqlInsertClienteRiesgoCrediticioHistorico + "LAST_UPDATED_F_DT, ";
      }
      sqlInsertClienteRiesgoCrediticioHistorico = 
      

        sqlInsertClienteRiesgoCrediticioHistorico + "LAST_UPDATED_DT) VALUES ('" + clienteRiesgoCrediticio.getParty_Identification_Type_Cd() + "', " + "'" + clienteRiesgoCrediticio.getParty_Identification_Num() + "', ";
      if (!"".equals(clienteRiesgoCrediticio.getParty_Gender_Type_Cd())) {
        sqlInsertClienteRiesgoCrediticioHistorico = sqlInsertClienteRiesgoCrediticioHistorico + "'" + clienteRiesgoCrediticio.getParty_Gender_Type_Cd() + "', ";
      }
      if (!"".equals(clienteRiesgoCrediticio.getParty_Credit_Score_IN_Ind())) {
        sqlInsertClienteRiesgoCrediticioHistorico = sqlInsertClienteRiesgoCrediticioHistorico + clienteRiesgoCrediticio.getParty_Credit_Score_IN_Ind() + ", ";
      }
      if (!"".equals(clienteRiesgoCrediticio.getLast_Updated_IN_Dt())) {
        sqlInsertClienteRiesgoCrediticioHistorico = sqlInsertClienteRiesgoCrediticioHistorico + "TO_DATE('" + clienteRiesgoCrediticio.getLast_Updated_IN_Dt() + "', '" + "MM/DD/YYYY HH24:MI:SS" + "'), ";
      }
      if (!"".equals(clienteRiesgoCrediticio.getParty_Credit_Score_AM_Ind())) {
        sqlInsertClienteRiesgoCrediticioHistorico = sqlInsertClienteRiesgoCrediticioHistorico + clienteRiesgoCrediticio.getParty_Credit_Score_AM_Ind() + ", ";
      }
      if (!"".equals(clienteRiesgoCrediticio.getLast_Updated_AM_Dt())) {
        sqlInsertClienteRiesgoCrediticioHistorico = sqlInsertClienteRiesgoCrediticioHistorico + "TO_DATE('" + clienteRiesgoCrediticio.getLast_Updated_AM_Dt() + "', '" + "MM/DD/YYYY HH24:MI:SS" + "'), ";
      }
      if (!"".equals(clienteRiesgoCrediticio.getParty_Credit_Score_M_Ind())) {
        sqlInsertClienteRiesgoCrediticioHistorico = sqlInsertClienteRiesgoCrediticioHistorico + clienteRiesgoCrediticio.getParty_Credit_Score_M_Ind() + ", ";
      }
      if (!"".equals(clienteRiesgoCrediticio.getUpdate_Manual_M_Dt())) {
        sqlInsertClienteRiesgoCrediticioHistorico = sqlInsertClienteRiesgoCrediticioHistorico + "TO_DATE('" + clienteRiesgoCrediticio.getUpdate_Manual_M_Dt() + "', '" + "MM/DD/YYYY HH24:MI:SS" + "'), ";
      }
      sqlInsertClienteRiesgoCrediticioHistorico = sqlInsertClienteRiesgoCrediticioHistorico + "'" + clienteRiesgoCrediticio.getUser_Update_M_Cd() + "', ";
      if (!"".equals(clienteRiesgoCrediticio.getLast_Updated_F_Dt())) {
        sqlInsertClienteRiesgoCrediticioHistorico = sqlInsertClienteRiesgoCrediticioHistorico + "TO_DATE('" + clienteRiesgoCrediticio.getLast_Updated_F_Dt() + "', '" + "MM/DD/YYYY HH24:MI:SS" + "'), ";
      }
      sqlInsertClienteRiesgoCrediticioHistorico = sqlInsertClienteRiesgoCrediticioHistorico + "SYSDATE)";
      





























      Date start = new Date();
      log.info("Inicio Insercion: " + sdfForDate.format(start));
      log.info("Query a ejecutar = " + sqlInsertClienteRiesgoCrediticioHistorico);
      
      registrosInsertados = stmt.executeUpdate(sqlInsertClienteRiesgoCrediticioHistorico);
      Date end = new Date();
      log.info("Fin Insercion: " + sdfForDate.format(end));
      Date difference = Util.differenceBetweenDates(start, end);
      
      StringBuilder builder = new StringBuilder();
      builder.append("El Tiempo de ejecucion ha sido de: ");
      builder.append(sdfForDateDifference.format(difference));
      builder.append(". Se inserto " + registrosInsertados + " registro/s");
      
      log.info(builder);
    }
    catch (Exception e)
    {
      log.error("Error: Sale por excepción en el metodo altaClienteRiesgoCrediticioHistorico " + e.getMessage());
      e.printStackTrace();
      throw e;
    }
    finally
    {
      try
      {
        if (stmt != null) {
          stmt.close();
        }
      }
      catch (SQLException e)
      {
        e.printStackTrace();
      }
      try
      {
        if (conn != null) {
          conn.close();
        }
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
  }
  
  public void borrarScoreManualEnClienteRiesgoCrediticioFinal(CrediLineRecord clienteRiesgoCrediticio)
    throws Exception
  {
    SimpleDateFormat sdfForDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSSS");
    SimpleDateFormat sdfForDateDifference = new SimpleDateFormat("HH:mm:ss.SSS");
    int registrosActualizados = 0;
    Statement stmt = null;
    String genero = validarNulos(clienteRiesgoCrediticio.getParty_Gender_Type_Cd());
    try
    {
      conn = getConnection();
      stmt = conn.createStatement();
      String sqlUpdateClienteRiesgoCrediticioFinal = "UPDATE RC_RIESGO_CREDITICIO_FINAL SET PARTY_CREDIT_SCORE_M_IND= NULL, UPDATE_MANUAL_M_DT=SYSDATE, USER_UPDATE_M_CD='BATCH', LAST_UPDATED_F_DT= SYSDATE WHERE PARTY_IDENTIFICATION_TYPE_CD = '" + 
      






        clienteRiesgoCrediticio.getParty_Identification_Type_Cd() + "'" + 
        " AND PARTY_IDENTIFICATION_NUM = " + 
        "'" + clienteRiesgoCrediticio.getParty_Identification_Num() + "'";
      if (genero.equals("")) {
        sqlUpdateClienteRiesgoCrediticioFinal = sqlUpdateClienteRiesgoCrediticioFinal + " AND PARTY_GENDER_TYPE_CD IS NULL ";
      } else {
        sqlUpdateClienteRiesgoCrediticioFinal = 
          sqlUpdateClienteRiesgoCrediticioFinal + " AND PARTY_GENDER_TYPE_CD = '" + genero + "'";
      }
      Date start = new Date();
      log.info("Inicio Modificacion: " + sdfForDate.format(start));
      log.info("Query a ejecutar = " + sqlUpdateClienteRiesgoCrediticioFinal);
      registrosActualizados = stmt.executeUpdate(sqlUpdateClienteRiesgoCrediticioFinal);
      Date end = new Date();
      log.info("Fin Modificacion: " + sdfForDate.format(end));
      Date difference = Util.differenceBetweenDates(start, end);
      
      StringBuilder builder = new StringBuilder();
      builder.append("El Tiempo de ejecucion ha sido de: ");
      builder.append(sdfForDateDifference.format(difference));
      builder.append(". Se actualizo " + registrosActualizados + " registro/s");
      
      log.info(builder);
    }
    catch (Exception e)
    {
      log.error("Error: Sale por excepción en el metodo borrarScoreManualEnClienteRiesgoCrediticioFinal " + e.getMessage());
      e.printStackTrace();
      throw e;
    }
    finally
    {
      try
      {
        if (stmt != null) {
          stmt.close();
        }
      }
      catch (SQLException e)
      {
        e.printStackTrace();
      }
      try
      {
        if (conn != null) {
          conn.close();
        }
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
  }
  
  public void borrarScoreManualEnClienteRiesgoCrediticioManual(CrediLineRecord clienteRiesgoCrediticio)
    throws Exception
  {
    SimpleDateFormat sdfForDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSSS");
    SimpleDateFormat sdfForDateDifference = new SimpleDateFormat("HH:mm:ss.SSS");
    int registrosActualizados = 0;
    Statement stmt = null;
    String genero = validarNulos(clienteRiesgoCrediticio.getParty_Gender_Type_Cd());
    try
    {
      conn = getConnection();
      stmt = conn.createStatement();
      
      String sqlUpdateClienteRiesgoCrediticioManual = "UPDATE RC_RIESGO_CREDITICIO_MANUAL SET PARTY_CREDIT_SCORE_M_IND= NULL, UPDATE_MANUAL_M_DT=SYSDATE, USER_UPDATE_M_CD='BATCH' WHERE PARTY_IDENTIFICATION_TYPE_CD = '" + 
      





        clienteRiesgoCrediticio.getParty_Identification_Type_Cd() + "'" + 
        " AND PARTY_IDENTIFICATION_NUM = " + 
        "'" + clienteRiesgoCrediticio.getParty_Identification_Num() + "'";
      if (genero.equals("")) {
        sqlUpdateClienteRiesgoCrediticioManual = sqlUpdateClienteRiesgoCrediticioManual + " AND PARTY_GENDER_TYPE_CD IS NULL";
      } else {
        sqlUpdateClienteRiesgoCrediticioManual = 
          sqlUpdateClienteRiesgoCrediticioManual + " AND PARTY_GENDER_TYPE_CD= '" + genero + "'";
      }
      Date start = new Date();
      log.info("Inicio Modificacion: " + sdfForDate.format(start));
      log.info("Query a ejecutar = " + sqlUpdateClienteRiesgoCrediticioManual);
      registrosActualizados = stmt.executeUpdate(sqlUpdateClienteRiesgoCrediticioManual);
      Date end = new Date();
      log.info("Fin Modificacion: " + sdfForDate.format(end));
      Date difference = Util.differenceBetweenDates(start, end);
      
      StringBuilder builder = new StringBuilder();
      builder.append("El Tiempo de ejecucion ha sido de: ");
      builder.append(sdfForDateDifference.format(difference));
      builder.append(". Se actualizo " + registrosActualizados + " registro/s");
      
      log.info(builder);
    }
    catch (Exception e)
    {
      log.error("Error: Sale por excepción en el metodo borrarScoreManualEnClienteRiesgoCrediticioManual " + e.getMessage());
      e.printStackTrace();
      throw e;
    }
    finally
    {
      try
      {
        if (stmt != null) {
          stmt.close();
        }
      }
      catch (SQLException e)
      {
        e.printStackTrace();
      }
      try
      {
        if (conn != null) {
          conn.close();
        }
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
  }
  
  public void borrarScoreAMDOCsEnClienteRiesgoCrediticioFinal(CrediLineRecord clienteRiesgoCrediticio)
    throws Exception
  {
    SimpleDateFormat sdfForDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSSS");
    SimpleDateFormat sdfForDateDifference = new SimpleDateFormat("HH:mm:ss.SSS");
    int registrosActualizados = 0;
    Statement stmt = null;
    String genero = validarNulos(clienteRiesgoCrediticio.getParty_Gender_Type_Cd());
    try
    {
      conn = getConnection();
      stmt = conn.createStatement();
      String sqlUpdateClienteRiesgoCrediticioFinal = "UPDATE RC_RIESGO_CREDITICIO_FINAL SET PARTY_CREDIT_SCORE_AM_IND= NULL, LAST_UPDATED_AM_DT=SYSDATE, USER_UPDATE_M_CD='BATCH', LAST_UPDATED_F_DT= SYSDATE WHERE PARTY_IDENTIFICATION_TYPE_CD = '" + 
      






        clienteRiesgoCrediticio.getParty_Identification_Type_Cd() + "'" + 
        " AND PARTY_IDENTIFICATION_NUM = " + 
        "'" + clienteRiesgoCrediticio.getParty_Identification_Num() + "'";
      if (genero.equals("")) {
        sqlUpdateClienteRiesgoCrediticioFinal = sqlUpdateClienteRiesgoCrediticioFinal + " AND PARTY_GENDER_TYPE_CD IS NULL ";
      } else {
        sqlUpdateClienteRiesgoCrediticioFinal = 
          sqlUpdateClienteRiesgoCrediticioFinal + " AND PARTY_GENDER_TYPE_CD = '" + genero + "'";
      }
      Date start = new Date();
      log.info("Inicio Modificacion: " + sdfForDate.format(start));
      log.info("Query a ejecutar = " + sqlUpdateClienteRiesgoCrediticioFinal);
      registrosActualizados = stmt.executeUpdate(sqlUpdateClienteRiesgoCrediticioFinal);
      Date end = new Date();
      log.info("Fin Modificacion: " + sdfForDate.format(end));
      Date difference = Util.differenceBetweenDates(start, end);
      
      StringBuilder builder = new StringBuilder();
      builder.append("El Tiempo de ejecucion ha sido de: ");
      builder.append(sdfForDateDifference.format(difference));
      builder.append(". Se actualizo " + registrosActualizados + " registro/s");
      
      log.info(builder);
    }
    catch (Exception e)
    {
      log.error("Error: Sale por excepción en el metodo borrarScoreAMDOCsEnClienteRiesgoCrediticioFinal " + e.getMessage());
      e.printStackTrace();
      throw e;
    }
    finally
    {
      try
      {
        if (stmt != null) {
          stmt.close();
        }
      }
      catch (SQLException e)
      {
        e.printStackTrace();
      }
      try
      {
        if (conn != null) {
          conn.close();
        }
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
  }
  
  public String validarNulos(String dato)
  {
    if (dato == null) {
      return "";
    }
    return dato;
  }
  
  public String validarNulos(int dato)
  {
    if (dato == 0) {
      return "";
    }
    return String.valueOf(dato);
  }
  
  public void consultarDatosCrediticios(Map<String, String> conversionDW_AMDOCs)
    throws ParseException, ReporteCrediticioOnlineException, Exception
  {}
}
