package ar.com.tasa.credi.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultSetProcessor {

	public void procesarResultSet(ResultSet rs) throws SQLException;

}
