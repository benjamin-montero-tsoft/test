package ar.com.tasa.credi.service;

import ar.com.tasa.credi.beans.ReporteCrediticio;
import ar.com.tasa.credi.beans.TipoDocumentoDTO;
import ar.com.tasa.credi.dao.imp.ReporteCrediticioDAOImpl;
import ar.com.tasa.credi.exception.ReporteCrediticioOnlineException;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

public class FileProcessorService
{
  private static ReporteCrediticioDAOImpl reporteCrediticioDAO;
  static Logger log;
  
  public FileProcessorService()
  {
    reporteCrediticioDAO = new ReporteCrediticioDAOImpl();
    log = Logger.getLogger(FileProcessorService.class);
  }
  
  public void generarArchivo(ReporteCrediticio reporte)
    throws Exception
  {
    boolean generarCSVTRIG = true;
    try
    {
      List<TipoDocumentoDTO> listaTiposDocumento = null;
      Map<String, String> conversionTipoDocumento_DW_AMDOCS = new HashMap();
      try
      {
        listaTiposDocumento = 
          reporteCrediticioDAO.obtenerTiposDocumento();
        if (listaTiposDocumento != null) {
          for (int i = 0; i < listaTiposDocumento.size(); i++) {
            conversionTipoDocumento_DW_AMDOCS.put(
              ((TipoDocumentoDTO)listaTiposDocumento.get(i)).getTipoDocDW(), 
              ((TipoDocumentoDTO)listaTiposDocumento.get(i)).getTipoDocAmdoc());
          }
        }
      }
      catch (ReporteCrediticioOnlineException e)
      {
        throw e;
      }
      try
      {
        reporteCrediticioDAO.consultarDatosCrediticios(reporte, conversionTipoDocumento_DW_AMDOCS);
      }
      catch (ReporteCrediticioOnlineException e)
      {
        log.info("Error al realizar la consulta a la base de datos " + 
          e);
      }
      if (!generarCSVTRIG) {
        return;
      }
    }
    catch (Exception e)
    {
      log.info("Error en generarArchivo de FileProcessorService");
      log.info(e);
      generarCSVTRIG = false;
    }
    String rutaCompletaArchivoCSVTRIG = reporte.getNombreArchivo() + 
      ".trig";
    File archivoCSVTRIG = new File(rutaCompletaArchivoCSVTRIG);
    log.info("ARCHIVO CSV.TRIG: " + rutaCompletaArchivoCSVTRIG);
    FileWriter fileWriterCSVTRIG = new FileWriter(archivoCSVTRIG);
    PrintWriter printWriterCSVTRIG = new PrintWriter(fileWriterCSVTRIG);
    fileWriterCSVTRIG.close();
  }
}
