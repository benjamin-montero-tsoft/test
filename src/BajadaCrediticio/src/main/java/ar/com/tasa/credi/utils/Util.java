package ar.com.tasa.credi.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ar.com.tasa.credi.exception.ReporteCrediticioOnlineException;

public class Util
{
  public static String generarNombreArchivo(Date fecha, String nombreArchivo, String pathArchivo, String extensionArchivo)
  {
    SimpleDateFormat sd = new SimpleDateFormat("yyyyMMdd_HHmmss");
    String fechaFormat = sd.format(fecha);
    
    String archivoFinal = pathArchivo + nombreArchivo + "_" + fechaFormat + extensionArchivo;
    
    return archivoFinal;
  }
  
  public static Date differenceBetweenDates(Date start, Date end)
    throws ParseException
  {
    String formato_fecha = "dd/MM/yyyy";
    SimpleDateFormat sdfOnlyDate = new SimpleDateFormat(formato_fecha);
    Date JAVA_START_DATE = new Date(0L);
    Long correctionFactor = Long.valueOf(sdfOnlyDate.parse(sdfOnlyDate.format(JAVA_START_DATE)).getTime());
    Long diff = Long.valueOf(end.getTime() - start.getTime() + correctionFactor.longValue());
    Date difference = new Date(diff.longValue());
    return difference;
  }
  
  public static String getFormatoFecha(Date fecha, String formato)
  {
    SimpleDateFormat format = new SimpleDateFormat(formato);
    String fechaFormato = format.format(fecha);
    return fechaFormato;
  }
  
  
  
  public static boolean validarFormatoOdate(String inDate) {
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
	    dateFormat.setLenient(false);
	    try {
	      dateFormat.parse(inDate.trim());
	    } catch (ParseException pe) {
	      return false;
	    }
	    return true;
	  }



}
