package ar.com.tasa.credi.dao;

import ar.com.tasa.credi.beans.CrediLineRecord;
import ar.com.tasa.credi.exception.ReporteCrediticioOnlineException;
import java.text.ParseException;
import java.util.Map;

public  interface ReporteCrediticioDao
{
  public  void consultarDatosCrediticios(Map<String, String> paramMap)
    throws ParseException, ReporteCrediticioOnlineException, Exception;
  
  public  void altaClienteRiesgoCrediticioHistorico(CrediLineRecord paramCrediLineRecord)
    throws Exception;
  
  public  void borrarScoreManualEnClienteRiesgoCrediticioFinal(CrediLineRecord paramCrediLineRecord)
    throws Exception;
  
  public  void borrarScoreAMDOCsEnClienteRiesgoCrediticioFinal(CrediLineRecord paramCrediLineRecord)
    throws Exception;
  
  public  void borrarScoreManualEnClienteRiesgoCrediticioManual(CrediLineRecord paramCrediLineRecord)
    throws Exception;
}
