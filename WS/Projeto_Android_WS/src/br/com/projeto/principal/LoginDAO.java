package br.com.projeto.principal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import br.com.projeto.conexao.Conexao;
import br.com.projeto.utils.Constantes;

public class LoginDAO {
	
	public boolean ativar(String userId) throws SQLException{
		return executarOpcao(userId, Constantes.CODIGO_ATIVO);
	}
	
	public boolean desativar(String userId) throws SQLException{
		return executarOpcao(userId, Constantes.CODIGO_INATIVO);
	}

	private boolean executarOpcao(String userId, byte opcao) throws SQLException{

		boolean retorno = false;
		ResultSet rst = null;
		
		try {
			
			String sql = "UPDATE USER_LOGIN" +
						 " SET SITUACAO = ?" +
						 " WHERE USER_ID = ?";
		
			PreparedStatement objPS = Conexao.getInstance().getConexao().prepareStatement(sql);
			objPS.setByte(1, opcao);
			objPS.setString(2, userId);

			retorno = objPS.executeUpdate() == 1;
						
		} catch (SQLException e) {
			e.printStackTrace();
			retorno = false;
		} finally{
			if(rst != null){
				rst.close();
			}
		}
		
		return retorno;
	}
	
	public boolean salvar(Login login) throws SQLException{
		
		boolean retorno = false;
		ResultSet rst = null;
		
		try {
			
			if(existe(login.getUserId())){
				return atualizar(login.getUserId(), login.getCoordUltimoAcesso());
			}
			
			String sql = "INSERT INTO USER_LOGIN" +
						 " (USER_ID, SITUACAO, DATA_ULTIMO_ACESSO, COORDENADA_ULTIMO_ACESSO)" +
						 " VALUES(?,?,?,?)";
		
			PreparedStatement objPS = Conexao.getInstance().getConexao().prepareStatement(sql);
			objPS.setString(1, login.getUserId());
			objPS.setByte(2, Constantes.CODIGO_ATIVO);
			objPS.setDate(3, new java.sql.Date(new Date().getTime()));
			objPS.setString(4, login.getCoordUltimoAcesso());

//			ConfiguracaoDAO objConfiguracao = new ConfiguracaoDAO();
//			objConfiguracao.setUserId(login.getUserId());
//			objConfiguracao.consultar(objConfiguracao);
			
			retorno = objPS.execute();
						
		} catch (SQLException e) {
			e.printStackTrace();
			retorno = false;
		} finally{
			if(rst != null){
				rst.close();
			}
		}
		
		return retorno;
	}
	
	private boolean existe(String userId) throws SQLException{
		
		boolean retorno = false;
		ResultSet rst = null;
		
		try {
			
			String sql = "SELECT * FROM USER_LOGIN" +
						 " WHERE USER_ID = ?";
			
			PreparedStatement objPS = Conexao.getInstance().getConexao().prepareStatement(sql);
			objPS.setString(1, userId);
			rst = objPS.executeQuery();
			
			if(rst.next()){
				retorno = true;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			retorno = false;
		} finally{
			if(rst != null){
				rst.close();
			}
		}
		
		return retorno;
	}
	
	public boolean atualizar(String userId, String coordUltimoAcesso) throws SQLException{

		boolean retorno = false;
		ResultSet rst = null;
		
		try {
			
			String sql = "UPDATE USER_LOGIN" +
						 " SET SITUACAO = ?, " +
						 " DATA_ULTIMO_ACESSO = ?," +
						 " COORDENADA_ULTIMO_ACESSO = ?" +
						 " WHERE USER_ID = ?";
		
			PreparedStatement objPS = Conexao.getInstance().getConexao().prepareStatement(sql);
			objPS.setByte(1, Constantes.CODIGO_ATIVO);
			objPS.setDate(2, new java.sql.Date(new Date().getTime()));
			objPS.setString(3, coordUltimoAcesso);
			objPS.setString(4, userId);

			retorno = objPS.executeUpdate() == 1;
						
		} catch (SQLException e) {
			e.printStackTrace();
			retorno = false;
		} finally{
			if(rst != null){
				rst.close();
			}
		}
		
		return retorno;
	}
}