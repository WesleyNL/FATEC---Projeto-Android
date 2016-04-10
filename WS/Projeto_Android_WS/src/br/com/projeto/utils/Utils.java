package br.com.projeto.utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import br.com.projeto.conexao.Conexao;

public class Utils {

	/** @return Em Km*/
    public static double calcularDistanciaGeodesica(Double latitudeP1, Double longitudeP1, Double latitudeP2, Double longitudeP2){
        return (( Math.acos(Math.cos((90 - latitudeP1) * Math.PI / 180) * Math.cos((90 - latitudeP2) * Math.PI / 180) +
                            Math.sin((90 - latitudeP1) * Math.PI / 180) * Math.sin((90 - latitudeP2) * Math.PI / 180) *
                            Math.cos(Math.abs(((360 + longitudeP1) * Math.PI / 180) - ((360 + longitudeP2) * Math.PI / 180)))) ) * 6371.004);
    }
    
    public LinkedList<String> pesquisarIdiomas(Object obj) throws SQLException{
		
		LinkedList<String> listaIdiomas = new LinkedList<String>();
		ResultSet rst = null;
		
		try {
			
			String sql = "SELECT * FROM IDIOMA";
			
			PreparedStatement objPS = Conexao.getConexao().prepareStatement(sql);
			rst = objPS.executeQuery();
				
			while(rst.next()){
				listaIdiomas.add(rst.getInt("CODIGO") + " - " + rst.getString("DESCRICAO"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			if(rst != null){
				rst.close();
			}
		}
		
		return listaIdiomas;
	}
    
    public LinkedList<String> pesquisarFluencias(Object obj) throws SQLException{
		
		LinkedList<String> listaFluencias = new LinkedList<String>();
		ResultSet rst = null;
		
		try {
			
			String sql = "SELECT * FROM FLUENCIA";
			
			PreparedStatement objPS = Conexao.getConexao().prepareStatement(sql);
			rst = objPS.executeQuery();
				
			while(rst.next()){
				listaFluencias.add(rst.getInt("CODIGO") + " - " + rst.getString("DESCRICAO"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			if(rst != null){
				rst.close();
			}
		}
		
		return listaFluencias;
	}
}
