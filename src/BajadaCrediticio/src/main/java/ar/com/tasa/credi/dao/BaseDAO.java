package ar.com.tasa.credi.dao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import ar.com.telefonica.utils.Configuracion;

public abstract class BaseDAO {

	static Logger log ;

	public BaseDAO(){
		log = Logger.getLogger(BaseDAO.class);
	}

	/**
	 *
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static Connection getConnection()throws Exception{

		Configuracion configLoader = Configuracion.instance();
    	configLoader.load("OnlineRCredi");
    	log.info("ruta properties:" + configLoader.getPath());
    	PropertyConfigurator.configure(configLoader.getPath());
    	
		String dbDriver = configLoader.get("dbDriver");
		String dbConnString = configLoader.get("dbConnString");;
		String server = configLoader.get("server");;
	    String puerto = configLoader.get("puerto");;
		String base = configLoader.get("base");;
		String dbUser = configLoader.get("dbUser");;
		String dbPassword = configLoader.get("dbPassword");;        	

		dbConnString = dbConnString + server + ":" + puerto + ":" + base;

		// Establece el driver de conexion
        Class.forName(dbDriver).newInstance();

        //creo el objeto connection que sera devuelto
        Connection con = (Connection) DriverManager.getConnection(dbConnString , dbUser , dbPassword);

        return con;	
	}


	/**
	 *
	 * @param query
	 * @param resultSetProcessor
	 */
	public void ejecutarConsulta (String query, ResultSetProcessor resultSetProcessor){

		Connection con = null;
		Statement stm = null;
		ResultSet rs = null;

		try{

			//obtengo la conexion
			con = getConnection();

			stm = con.createStatement();

			//se ejecuta la consulta
			rs = stm.executeQuery(query);

			//se procesan los valores obtenidos de la consulta
			resultSetProcessor.procesarResultSet(rs);

			con.commit();

		}catch (Exception ex){
			log.error("Error al realizar la consulta" + ex );

			try{
					if(con != null)
					{
						con.rollback();
					}

			   }catch(SQLException e){
				   	log.error("Se realiza un rollback");
			   }
		}

		 finally {
	    		try {
	    			if (rs != null)
	    				rs.close();
	    		} catch (SQLException e) {
	    			e.printStackTrace();
	    		}
	    		try {
	    			if (stm != null)
	    				stm.close();
	    		} catch (SQLException e) {
	    			e.printStackTrace();
	    		}
	    		try {
	    			if (con != null)
	    				con.close();
	    		} catch (Exception e) {
	    			e.printStackTrace();
				}
	    	}
	}
	
	/**
     * Ejecuta la query recibida como parámetro. La query debe ser un INSERT, UPDATE o DELETE
     * 
     * @param query
     */
    public int ejecutarUpdateQuery(String query) {
    	
    	Connection conn = null;
    	Statement stmt = null;
    	int registros = 0;
		
    	try {
    		conn = getConnection();
    		stmt = conn.createStatement();
    		registros = stmt.executeUpdate(query);
			
    		conn.commit();
    		
    	} catch (Exception e) {
    		e.printStackTrace();
    		log.error("Error interactuando con la BBDD");
    		
			try {
				if (conn != null)
					conn.rollback();
			} catch (SQLException se) {
				se.printStackTrace();
			}
			
    	} finally {
    		try {
    			if (stmt != null)
    				stmt.close();
    		} catch (SQLException e) {
    			e.printStackTrace();
    		}
    		try {
    			if (conn != null)
    				conn.close();
    		} catch (Exception e) {
    			e.printStackTrace();
			}

    	}
    	return registros;
    }	

}
