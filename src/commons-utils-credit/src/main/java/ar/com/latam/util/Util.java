package ar.com.latam.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * @author diana.karina.rojas
 *
 */
public class Util {
	
	/** The Constant ZERO_CHARACTER. */
	private static final String ZERO_CHARACTER="0";
	/** The Constant SPACE_CHARACTER. */
	private static final String SPACE_CHARACTER=" ";
	/** The Constant EMPTY_CHARACTER. */
	public static final String EMPTY_CHARACTER="";
	/** The Constant NULL_CHARACTER. */
	public static final String NULL_CHARACTER="NULL";
	/** The Constant PIPE_CHARACTER. */
	public static final String PIPE_CHARACTER="|";	
	/** The Constant DECIMAL_CHARACTER. */
	private static final String DECIMAL_CHARACTER=",";		
	/** The Constant MILESIMAS_CHARACTER. */
	private static final String MILESIMAS_CHARACTER=".";		
	
	public static final String DATE_TIME_SCREEN = "dd/MM/yyyy";
	public static final String DATE_TIME_SCREEN_2 = "dd/MM/yyyy HH:mm";
	public static final String DATE_TIME_PATTERN = "dd/MM/yyyy HH:mm:ss.SSSS";
	public static final String TIME_PATETRN = "HH:mm:ss.SSS";
	
	public static final String FORMAT_DATE_RIESGO = "MM/dd/yyyy HH:mm:ss";
	public static final String FORMAT_DATE_BD = "MM/DD/YYYY HH24:MI:SS";
	
	public static final String UCID = "UCID";
	
	public static final String SYSDATE = "SYSDATE";
	
	public static final String TODOS = "TODOS";
	
	public static final String DU = "DU";
	public static final String DNI = "DNI";
	public static final String LC = "LC";
	public static final String LE = "LE";
	public static final String CL = "CL";	
	public static final String CUIL = "CUIL";
	public static final String CU = "CU";
	public static final String CUIT = "CUIT";
	public static final String GUION = "-";
	
	public static final String SI = "S";
	public static final String NO = "N";
	
	public static final String SEXO_FEMENINO = "F";
	public static final String SEXO_MASCULINO = "M";
	
	public static final String PLEASE_SPECIFY= "Please Specify";
	/*
	 * Pasar de dd-mm-yyyy a dd/mm/yyyy
	 */
	public static String getFormatoFecha(String fecha)throws Exception{
		String fechaFormato = null;
		try{		
			String dia = fecha.substring(0,2);
			String mes = fecha.substring(3,5);
			String anio = fecha.substring(6,10);						
			fechaFormato = dia + "/" + mes + "/"+ anio;
		}finally{
			
		}
		return fechaFormato;
	}
		
	/*
	 * Pasar una fecha a un string con un formato dado
	 */
	public static String getFormatoFecha(Date fecha, String formato){
		SimpleDateFormat format = new SimpleDateFormat(formato);
		String fechaFormato = format.format(fecha);
		return fechaFormato;
	}
	
	/*
	 * Pasar una String a formato indicado
	 */
	public static Date getFormatoDate(String fechaIn , String formato){
		Date fecha = null;
		SimpleDateFormat format = new SimpleDateFormat(formato);
		
		try {
			fecha = format.parse(fechaIn);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return fecha;
	}
	
	public static Date getFormatoDate(String fechaIn){
	
		 Date fecha = null;
         SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
         try {

              fecha  = formato.parse(fechaIn);

         } catch (ParseException e1) {

                 // TODO Auto-generated catch block

                 e1.printStackTrace();

         }

         return fecha;
	}
	
	/*
	 * Pasar de dd/mm/yyyy a MM/DD/YYYY HH24:MI:SS
	 */	
	public static String getFormatoFechaFacturaData(String fecha) throws Exception{
		String fechaConFormatoFacturaData = null;
		try{		
			String dia = fecha.substring(0,2);
			String mes = fecha.substring(3,5);
			String anio = fecha.substring(6,10);						
			fechaConFormatoFacturaData = mes + "/" + dia + "/"+ anio + " 00:00:00";
		}finally{
		}
		return fechaConFormatoFacturaData;
	}
	/*
	 * Pasar de dd/mm/yyyy a yyyymmdd
	 */
	public static String getSinFormatoFecha(String fecha)throws Exception{
		String fechaSinFormato = null;
		try{		
			String dia = fecha.substring(0,2);
			String mes = fecha.substring(3,5);
			String anio = fecha.substring(6,10);						
			fechaSinFormato = anio + mes + dia;
		}finally{
		}
		return fechaSinFormato;
	}
	
    /**
     * Al "String original" se le argrega caracter  según "pos" hasta
     * el que el nuevo String  tenga la "cantDigitos" especificado
     *
     * @param aValor String original
     * @param aCar Caracter a completar
     * @param cantDigitos Cantidad de caracteres
     * @param pos "I"=izquierda, "D"=derecha
     *
     * @return String original al que se le agrego  el/los caracter/es
     */
    
    public static String fillString(String aValor, String aCar,
        int cantDigitos, String pos) throws Exception {
    	try{
	        if(pos.equals("D")) { //agrega a derecha
	
	            while(aValor.length() < cantDigitos) {
	                aValor = aValor + aCar;
	            }
	
	            ;
	        } else { //agrega a izquierda
	
	            while(aValor.length() < cantDigitos) {
	                aValor = aCar + aValor;
	            }
	
	            ;
	        }
    	}finally{
    		
    	}
        return aValor;
    }
    
    /**
     * Calcula la diferencia
     * @param start
     * @param end
     * @return
     * @throws ParseException
     */
    public static Date differenceBetweenDates(Date start, Date end)
        throws ParseException {
    	String formato_fecha = "dd/MM/yyyy";
    	SimpleDateFormat sdfOnlyDate = new SimpleDateFormat(formato_fecha);
    	Date JAVA_START_DATE = new Date(0);
        Long correctionFactor = sdfOnlyDate.parse(sdfOnlyDate.format(JAVA_START_DATE)).getTime();
        Long diff = end.getTime() - start.getTime() + correctionFactor;
        Date difference = new Date(diff);
        return difference;
    }    

	/**
	 * Removes the starting zeros.
	 *
	 * @param s the string
	 * @param cant Cantidad de caracteres minima
	 * @return the string
	 */
	public static String removeStartingZeros(String s, int cant){
		try{
		
			if (s != null){	
				if (cant >= 0){
			        while(((s.startsWith(ZERO_CHARACTER)||(s.startsWith(SPACE_CHARACTER))))&&(s.length()>cant)){
			            s=s.substring(1, s.length());
			        }
				}
		        if (cant == 0 && s.length() == 1){
		        	if((s.startsWith(ZERO_CHARACTER)||(s.startsWith(SPACE_CHARACTER)))){
		        		s = EMPTY_CHARACTER;
		        	}        	        	
		        }else if(s.length()==0){
		            s = EMPTY_CHARACTER;
		        }
			}
		}catch(Exception e){
			System.out.println(e);
		}
        return s;
    }
	/**
	 * Formato importe: xx.xxx.xxx,xx (preparado para un numero con hasta 8 numeros en la parte entera)
	 * Se ingresa xxxxxxxx,xx y se obtiene el numero con formato xx.xxx.xxx,xx
	 * @param importe (Si tiene decimales se ingresa una ",")
	 * @return
	 */
	public static String formatoImporte(String importe){
		String importeFormato = null;
		String parteEntera = "";
		String parteDecimal = "";
		String formatoParteEntera = "";
		int posParteDecimal = importe.indexOf(DECIMAL_CHARACTER);
		if (posParteDecimal != -1){
			parteEntera = importe.substring(0, posParteDecimal);			
			parteDecimal = importe.substring(posParteDecimal + 1, importe.length());
		}else{//en caso de que no tenga decimales
			return importe;
		}
		//Formato de milesimas de la parte entera
		if (parteEntera.length() <= 3){
			return importe;
		}else if (parteEntera.length() <= 6){
			formatoParteEntera = parteEntera.substring(0, parteEntera.length()-3) + MILESIMAS_CHARACTER + parteEntera.substring(parteEntera.length()-3, parteEntera.length()); 
		}else if (parteEntera.length() == 7 || parteEntera.length() == 8){
			formatoParteEntera = parteEntera.substring(0, parteEntera.length()-6) + MILESIMAS_CHARACTER + parteEntera.substring(parteEntera.length()-6, parteEntera.length()-3) + MILESIMAS_CHARACTER + parteEntera.substring(parteEntera.length()-3, parteEntera.length()); 
		}
		importeFormato = formatoParteEntera + DECIMAL_CHARACTER + parteDecimal;
		return importeFormato;
	}
	
	public static String modifUser(String userId) {
		String value = "";
		
		if(userId == null) {
			return value;
		}
		String[] b = userId.split(",");

		for(int x = 0 ; x < b.length; x++) {
			if(b[x].contains("uid=")) {
				value = b[x].replaceAll("uid=", "");
				//ademas si el usuario tiene mas de 20 caracteres lo corto
				if(value.length() >20) {
					value = value.substring(0, 20);
				}
			}
		}
		
		return value;
	}
	
	public static String generarCabecera(){
		
		StringBuilder sb= new StringBuilder();
		sb.append("CUSTOMER_ID_TYPE")
		  .append(",")
		  .append("CUSTOMER_ID")
		  .append(",")
		  .append("GENDER")
		  .append(",")
		  .append("SEGMENT")
		  .append(",")
		  .append("SCORE")
		  .append(",") 
		  .append("INCOME")
		  .append(",")
		  .append("COST")
		  .append(",")
		  .append("SATISFACTION_LEVEL")
		  .append('\n');
		
		
		
		return sb.toString();
	}


	public static String validarGenero(String dato){

		if (dato == null || dato.equals(""))
	
			return PLEASE_SPECIFY;
		else
			System.out.println("DATO |"+dato+"|");
			return dato;
		
	}

}
