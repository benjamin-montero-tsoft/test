package ar.com.latam.creditCore.utils;

public class CUIT {
	
	
	/**
	 * Algoritmo que realiza el calculo del nro. de CUIT/CUIL a partir de un documento unico.
	 * @param doc
	 * @return
	 */
	public static String generar(int dniInt, char xyChar) { 
		Documento doc = new Documento();
		doc.setDniStc(dniInt);
		int xyStc;
		if (xyChar == 'F' || xyChar == 'f') 
			xyStc = 27; 
			else if (xyChar == 'M' || xyChar == 'm') 
				xyStc = 20; 
			else 
				xyStc = 30; 
			
			doc.setXyStc(xyStc);
			calcular(doc); 
			return formatear(doc); 
		} 
	
	
	/** * Metodo privado q calcula el CUIT * */ 
	private static void calcular(Documento doc) { 
	long tmp1, tmp2; 
	long acum = 0; 
	int n = 2; 
	tmp1 = doc.getXyStc() * 100000000L + doc.getDniStc(); 
	for (int i = 0; i < 10; i++) { 
		tmp2 = tmp1 / 10; 
		acum += (tmp1 - tmp2 * 10L) * n; 
		tmp1 = tmp2; 
		if (n < 7) 
		n++; 
		else 
		n = 2; 
	} 
	n = (int) (11 - acum % 11); 
	if (n == 10) { 
		if (doc.getXyStc() == 20 || doc.getXyStc() == 27 || doc.getXyStc() == 24) 
		doc.setXyStc(23); 
		else 
			doc.setXyStc(33); /* 
		* No es necesario hacer la llamada recursiva a 
		* calcular(), *se puede poner el digito en 9 si el 
		* prefijo original era *23 o 33 o poner el dijito 
		* en 4 si el prefijo era 27 
		*/ 
		calcular(doc); 
	} else { 
		if (n == 11) 
		doc.setDigitoStc(0); 
		else 
		doc.setDigitoStc(n); 
		} 
	}
	
	
	/** * Metodo privado q da formato al CUIT como String * */ 
	private static String formatear(Documento doc) { 
		return String.valueOf(doc.getXyStc())  + completar(String.valueOf(doc.getDniStc()) +  String.valueOf(doc.getDigitoStc())); 
	} 

	/** * Metodo privado q completa con ceros el DNI para q quede con 8 digitos * */ 
	private static String completar(String dniStr) { 
	int n = dniStr.length(); 
	while (n < 8) { 
	dniStr = "0" + dniStr; 
	n = dniStr.length(); 
	} 
	return dniStr; 
	} 
	/**
	 * Valida que el nro ingresado es un cuit/cuil valido.
	 * @param cuit
	 * @return
	 */
	public static boolean validar(String cuit) { 
		
		int dniStc; 

		int xyStc; 
		
		int digitoStc=0;
		
		String xyStr, dniStr, digitoStr; 
		int digitoTmp; 
		int n = cuit.lastIndexOf("-"); 
		xyStr = cuit.substring(0, 2); 
		dniStr = cuit.substring(cuit.indexOf("-") + 1, n); 
		digitoStr = cuit.substring(n + 1, n + 2); 
		if (xyStr.length() != 2 || dniStr.length() > 8 || digitoStr.length() != 1) 
		return false; 
		try { 
		xyStc = Integer.parseInt(xyStr); 
		dniStc = Integer.parseInt(dniStr); 
		digitoTmp = Integer.parseInt(digitoStr); 
		} catch (NumberFormatException e) { 
		return false; 
		} 
		if (xyStc != 20 && xyStc != 23 && xyStc != 24 && xyStc != 27 && xyStc != 30 && xyStc != 33 && xyStc != 34) 
			return false; 
		digitoStc= calcular(xyStc, dniStc, digitoStc); 
		if (digitoStc == digitoTmp && xyStc == Integer.parseInt(xyStr)) 
		return true; 
		return false; 
		} 


	/** * Metodo privado q calcula el CUIT * */ 
	private static int calcular(int xyStc, int dniStc, int digitoStc) { 
		
	long tmp1, tmp2; 
	long acum = 0; 
	int n = 2; 
	tmp1 = xyStc * 100000000L + dniStc; 
	for (int i = 0; i < 10; i++) { 
	tmp2 = tmp1 / 10; 
	acum += (tmp1 - tmp2 * 10L) * n; 
	tmp1 = tmp2; 
	if (n < 7) 
	n++; 
	else 
	n = 2; 
	} 
	n = (int) (11 - acum % 11); 
	if (n == 10) { 
	if (xyStc == 20 || xyStc == 27 || xyStc == 24) 
	xyStc = 23; 
	else 
	xyStc = 33; /* 
	* No es necesario hacer la llamada recursiva a 
	* calcular(), *se puede poner el digito en 9 si el 
	* prefijo original era *23 o 33 o poner el dijito 
	* en 4 si el prefijo era 27 
	*/ 
	calcular(xyStc, dniStc, digitoStc); 
	} else { 
	if (n == 11) 
	digitoStc = 0; 
	else 
	digitoStc = n; 
	} 
	
	return digitoStc;
	} 
	



}
