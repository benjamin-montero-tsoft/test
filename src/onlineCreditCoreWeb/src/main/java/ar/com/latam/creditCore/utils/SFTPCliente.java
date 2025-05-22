package ar.com.latam.creditCore.utils;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.log4j.Logger;

import ar.com.latam.creditCore.exceptions.CustomSftpException;


import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;


public class SFTPCliente {

	public String host;
	public int port;
	public Channel channel;
	public Session session;
	Properties config;


	public SFTPCliente(String host, int port) {
		this.host = host;
		this.port = port;
		config = new java.util.Properties();         
		config.put("StrictHostKeyChecking", "no"); 
	}
	
	
	private static Logger log = Logger.getLogger(SFTPCliente.class);
	

	public void conectar(String user, String pwd) throws CustomSftpException {
		try {
			JSch ssh = new JSch();
			session = ssh.getSession(user, host, port);
			session.setConfig(config);
			session.setPassword(pwd);
			session.connect();
			channel = session.openChannel("sftp");
			channel.connect();

		}
		catch (JSchException e) {
			e.printStackTrace();
			log.error("Error en la conexi�n a destino: "+host);
			throw new CustomSftpException("Error en la conexi�n a destino: "+host);

		}

	}

	public void desconectar() throws CustomSftpException {
		try {
			channel.disconnect();
			session.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error en la conexi�n a destino: "+host);
			throw new CustomSftpException("Error en la desconexi�n de destino: "+host);

		}
	}

	public void transferir(String archivoCVS, String nombreArchivo, String nombreArchivoTrig, String directorioRemoto) throws CustomSftpException {
		ChannelSftp sftp;
		try {

			sftp = (ChannelSftp) channel;
			toDirectorioRemoto(sftp, directorioRemoto);
			ByteArrayInputStream archivoByte = new ByteArrayInputStream(
					archivoCVS.getBytes());

			sftp.put(archivoByte, nombreArchivo);
			sftp.put(nombreArchivoTrig);
			
		} catch (Exception e)  {
			e.printStackTrace();
			log.error("Error en la conexi�n a destino: "+host);
			throw new CustomSftpException("Error en la tranferencia de datos a destino: "+host);

		}

	}

	public void toDirectorioRemoto(ChannelSftp sftp, String directorioRemoto) throws SftpException {
		sftp.cd(directorioRemoto);
	}
	
	

	  public static String getDateReports(){
		  /*AAAAMMDD_HHMMSS*/
	      SimpleDateFormat formato = new SimpleDateFormat("yyyyMMdd_HHmmss");
	      Date fechaActual = new Date();
	      return formato.format(fechaActual);
	  }

}
