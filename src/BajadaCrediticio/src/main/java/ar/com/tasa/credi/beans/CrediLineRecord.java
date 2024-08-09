package ar.com.tasa.credi.beans;

public class CrediLineRecord
{
  private String Party_Identification_Type_Cd;
  private String tipoDocFormatoAMDOCs;
  private String Party_Identification_Num;
  private String Party_Gender_Type_Cd;
  private String Party_Credit_Score_IN_Ind;
  private String Last_Updated_IN_Dt;
  private String Party_Credit_Score_AM_Ind;
  private String Last_Updated_AM_Dt;
  private String Party_Credit_Score_M_Ind;
  private String Update_Manual_M_Dt;
  private String User_Update_M_Cd;
  private String Last_Updated_F_Dt;
  private String scoreEnviadoAMDOCs;
  
  public String getParty_Identification_Type_Cd()
  {
    return this.Party_Identification_Type_Cd;
  }
  
  public void setParty_Identification_Type_Cd(String Party_Identification_Type_Cd)
  {
    this.Party_Identification_Type_Cd = Party_Identification_Type_Cd;
  }
  
  public String getTipoDocFormatoAMDOCs()
  {
    return this.tipoDocFormatoAMDOCs;
  }
  
  public void setTipoDocFormatoAMDOCs(String tipoDocFormatoAMDOCs)
  {
    this.tipoDocFormatoAMDOCs = tipoDocFormatoAMDOCs;
  }
  
  public String getParty_Identification_Num()
  {
    return this.Party_Identification_Num;
  }
  
  public void setParty_Identification_Num(String Party_Identification_Num)
  {
    this.Party_Identification_Num = Party_Identification_Num;
  }
  
  public String getParty_Gender_Type_Cd()
  {
    return this.Party_Gender_Type_Cd;
  }
  
  public void setParty_Gender_Type_Cd(String party_Gender_Type_Cd)
  {
    this.Party_Gender_Type_Cd = party_Gender_Type_Cd;
  }
  
  public String getParty_Credit_Score_IN_Ind()
  {
    return this.Party_Credit_Score_IN_Ind;
  }
  
  public void setParty_Credit_Score_IN_Ind(String Party_Credit_Score_IN_Ind)
  {
    this.Party_Credit_Score_IN_Ind = Party_Credit_Score_IN_Ind;
  }
  
  public String getLast_Updated_IN_Dt()
  {
    return this.Last_Updated_IN_Dt;
  }
  
  public void setLast_Updated_IN_Dt(String Last_Updated_IN_Dt)
  {
    this.Last_Updated_IN_Dt = Last_Updated_IN_Dt;
  }
  
  public String getParty_Credit_Score_AM_Ind()
  {
    return this.Party_Credit_Score_AM_Ind;
  }
  
  public void setParty_Credit_Score_AM_Ind(String Party_Credit_Score_AM_Ind)
  {
    this.Party_Credit_Score_AM_Ind = Party_Credit_Score_AM_Ind;
  }
  
  public String getParty_Credit_Score_M_Ind()
  {
    return this.Party_Credit_Score_M_Ind;
  }
  
  public void setParty_Credit_Score_M_Ind(String Party_Credit_Score_M_Ind)
  {
    this.Party_Credit_Score_M_Ind = Party_Credit_Score_M_Ind;
  }
  
  public String getLast_Updated_AM_Dt()
  {
    return this.Last_Updated_AM_Dt;
  }
  
  public void setLast_Updated_AM_Dt(String Last_Updated_AM_Dt)
  {
    this.Last_Updated_AM_Dt = Last_Updated_AM_Dt;
  }
  
  public String getUpdate_Manual_M_Dt()
  {
    return this.Update_Manual_M_Dt;
  }
  
  public void setUpdate_Manual_M_Dt(String Update_Manual_M_Dt)
  {
    this.Update_Manual_M_Dt = Update_Manual_M_Dt;
  }
  
  public String getUser_Update_M_Cd()
  {
    return this.User_Update_M_Cd;
  }
  
  public void setUser_Update_M_Cd(String User_Update_M_Cd)
  {
    this.User_Update_M_Cd = User_Update_M_Cd;
  }
  
  public String getLast_Updated_F_Dt()
  {
    return this.Last_Updated_F_Dt;
  }
  
  public void setLast_Updated_F_Dt(String Last_Updated_F_Dt)
  {
    this.Last_Updated_F_Dt = Last_Updated_F_Dt;
  }
  
  public String getScoreEnviadoAMDOCs()
  {
    return this.scoreEnviadoAMDOCs;
  }
  
  public void setScoreEnviadoAMDOCs(String scoreEnviadoAMDOCs)
  {
    this.scoreEnviadoAMDOCs = scoreEnviadoAMDOCs;
  }
  
  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    sb.append(this.tipoDocFormatoAMDOCs)
      .append(",")
      .append(this.Party_Identification_Num)
      .append(",")
      .append(this.Party_Gender_Type_Cd)
      .append(",")
      .append("NULL,")
      .append(this.scoreEnviadoAMDOCs)
      .append(",")
      .append("NULL,")
      .append("NULL,");
    
    return sb.toString();
  }
}
