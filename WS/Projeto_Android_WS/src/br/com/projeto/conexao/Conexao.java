package br.com.projeto.conexao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import br.com.projeto.Sistema;

public class Conexao {

	private static String URL = null;
	private static final String USUARIO = Sistema.getParametro("CONEXAO.USUARIO");
	private static final String SENHA = Sistema.getParametro("CONEXAO.SENHA");
	private static Connection con = null;
	private static boolean conectado = false;
	
	public static Connection getConexao() throws SQLException{
		
		if(con != null){
			return con;
		}

		try{
			switch(Integer.parseInt(Sistema.getParametro("CONEXAO.TIPO"))){
			case 1:
				URL = "jdbc:mysql://" + Sistema.getParametro("CONEXAO.SERVIDOR") + "/" + Sistema.getParametro("CONEXAO.BANCO");
				Class.forName("com.mysql.jdbc.Driver");
				break;
			case 2: 
				URL = "jdbc:sqlserver://" + Sistema.getParametro("CONEXAO.SERVIDOR") + ";databaseName=" + Sistema.getParametro("CONEXAO.BANCO");
				Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
				break;
			default:
				System.out.println("Banco ainda n�o configurado.");
				return con;
			}
		} catch(ClassNotFoundException e){
			e.printStackTrace();
		}
		
		con = DriverManager.getConnection(URL, USUARIO, SENHA);
		
		if(con != null){
			conectado = true;
		}
		
		return con;
	}

	public static boolean isConectado() {
		return conectado;
	}
}