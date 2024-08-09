package ar.com.tasa.credi.beans;

import java.io.Serializable;

public class TipoDocumentoDTO
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private String tipoDocDW;
  private String tipoDocAmdoc;
  private String userCarga;
  private String fechaCarga;
  
  public String getTipoDocDW()
  {
    return this.tipoDocDW;
  }
  
  public void setTipoDocDW(String tipoDocDW)
  {
    this.tipoDocDW = tipoDocDW;
  }
  
  public String getTipoDocAmdoc()
  {
    return this.tipoDocAmdoc;
  }
  
  public void setTipoDocAmdoc(String tipoDocAmdoc)
  {
    this.tipoDocAmdoc = tipoDocAmdoc;
  }
  
  public String getUserCarga()
  {
    return this.userCarga;
  }
  
  public void setUserCarga(String userCarga)
  {
    this.userCarga = userCarga;
  }
  
  public String getFechaCarga()
  {
    return this.fechaCarga;
  }
  
  public void setFechaCarga(String fechaCarga)
  {
    this.fechaCarga = fechaCarga;
  }
}
