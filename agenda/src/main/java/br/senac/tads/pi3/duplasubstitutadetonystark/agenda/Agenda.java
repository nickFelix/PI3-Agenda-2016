/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.senac.tads.pi3.duplasubstitutadetonystark.agenda;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author giovane.gpecora1
 */
public class Agenda {
    private Connection obterConexao() throws SQLException, ClassNotFoundException{
        Connection conn= null;
        //Passo 1: Registrar driver JDBC.
        Class.forName("org.apache.derby.jdbc.ClientDataSource");
        
        //Passo 2: Abrir a conexão
        conn = DriverManager.getConnection("jdbc:derby://localhost:1527//agendabd;SecurityMechanism=3",
        "app",//usuario
        "app");//senha
        return conn;
    }
    
}
