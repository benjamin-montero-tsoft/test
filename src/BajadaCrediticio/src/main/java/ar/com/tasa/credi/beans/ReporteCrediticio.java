package ar.com.tasa.credi.beans;

public class ReporteCrediticio
{
  private String pathArchivo;
  private String nombreArchivo;
  private String odate;
  
  public String getNombreArchivo()
  {
    return this.nombreArchivo;
  }
  
  public void setNombreArchivo(String nombreArchivo)
  {
    this.nombreArchivo = nombreArchivo;
  }
  
  public String getPathArchivo()
  {
    return this.pathArchivo;
  }
  
  public String getOdate() {
	return odate;
}

public void setOdate(String odate) {
	this.odate = odate;
}

public void setPathArchivo(String pathArchivo)
  {
    this.pathArchivo = pathArchivo;
  }
}
