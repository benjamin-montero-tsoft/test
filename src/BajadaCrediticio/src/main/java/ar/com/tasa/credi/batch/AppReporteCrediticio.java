package ar.com.tasa.credi.batch;

import ar.com.tasa.credi.beans.ReporteCrediticio;
import ar.com.tasa.credi.exception.ReporteCrediticioOnlineException;
import ar.com.tasa.credi.service.FileProcessorService;
import ar.com.tasa.credi.utils.Util;
import ar.com.telefonica.utils.Configuracion;
import java.util.Date;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class AppReporteCrediticio
{
  private static ResourceBundle fileData;
  private static ReporteCrediticio reporteCrediticio;
  private static FileProcessorService fileProcessorService;
  static Logger log;
  
  public static void main(String[] args)
    throws ReporteCrediticioOnlineException, Exception
  {
    Boolean cargaPropertiesOk = Boolean.valueOf(true);
    try
    {
      log = Logger.getLogger("AppReporteCrediticio.class");
      log.info("Ejecutando el metodo main de la clase AppReporteCrediticio.java");
      log.info("**************************************************************************************************************");
      log.info("Se inicia el proceso");
      
      if (args == null || args.length != 1){
    	  log.error("No se recibio el parametro de la odate");
    	  System.exit(1);
      }
  	
      String odate = args[0];
      
  
      
      reporteCrediticio = new ReporteCrediticio();
      fileProcessorService = new FileProcessorService();
      

      Configuracion configLoader = Configuracion.instance();
      configLoader.load("OnlineRCredi");
      log.info("ruta properties:" + configLoader.getPath());
      PropertyConfigurator.configure(configLoader.getPath());
      
      
      if (  odate.length() != 8 || !Util.validarFormatoOdate(odate)){
    	  
    	  log.error("El formato de la odate no es correcta. Utilizar: YYYYMMDD");
    	  System.exit(1);
      }
    
      log.info("Odate ejecuci�n: " + odate);
      
      
      try
      {
        Date fecha = new Date();
        log.error("Fecha: " + fecha);
        

        String nombreArchivo = configLoader.get("DatafileName");
        String pathArchivo = configLoader.get("LocationDataFile");
        String extensionArchivo = configLoader.get("extensionFile");
        reporteCrediticio.setNombreArchivo(Util.generarNombreArchivo(fecha, nombreArchivo, pathArchivo, extensionArchivo));
        reporteCrediticio.setPathArchivo(configLoader.get("LocationDataFile"));
		reporteCrediticio.setOdate(odate);

        
        log.info("Nombre del archivo a generar " + reporteCrediticio.getNombreArchivo());
        log.info("Locacion donde generar el archivo " + reporteCrediticio.getPathArchivo());
        
        fileProcessorService.generarArchivo(reporteCrediticio);
      }
      catch (Exception ex)
      {
        log.error("Error general de la aplicacion = " + ex);
      }
      log.info("Carga Properties: " + cargaPropertiesOk);
    }
    catch (Exception ex)
    {
      cargaPropertiesOk = Boolean.valueOf(false);
      throw new Exception("Error al tratar de cargar el archivo OnlineRCredi.properties en el metodo main de AppReporteCrediticio");
    }
    log.info("Finaliza el proceso ok ");
    log.info("**************************************************************************************************************");
  }
}
