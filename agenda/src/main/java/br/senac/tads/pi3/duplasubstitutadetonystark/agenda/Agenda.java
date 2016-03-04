/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.senac.tads.pi3.duplasubstitutadetonystark.agenda;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Giovane Pecora e Nicolas
 */
public class Agenda {

    private static Connection obterConexao() throws SQLException, ClassNotFoundException {
        Connection conn = null;
        // Passo 1: Registrar driver JDBC.
        Class.forName("org.apache.derby.jdbc.ClientDriver");

        // Passo 2: Abrir a conexÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â£o
        conn = DriverManager.getConnection(
                "jdbc:derby://localhost:1527/sample",
                "app",// usuario
                "app"); // senha
        return conn;
    }

    public static void listarPessoas() {
        Statement stmt = null;
        Connection conn = null;

        String sql = "SELECT ID_CONTATO, NM_CONTATO, DT_NASCIMENTO, VL_TELEFONE, VL_EMAIL FROM TB_CONTATO";
        try {
            conn = obterConexao();
            stmt = conn.createStatement();
            ResultSet resultados = stmt.executeQuery(sql);

            DateFormat formatadorData = new SimpleDateFormat("dd/MM/yyyy");

            while (resultados.next()) {
                Long id = resultados.getLong("ID_CONTATO");
                String nome = resultados.getString("NM_CONTATO");
                Date dataNasc = resultados.getDate("DT_NASCIMENTO");
                String email = resultados.getString("VL_EMAIL");
                String telefone = resultados.getString("VL_TELEFONE");
                System.out.println(String.valueOf(id) + ", " + nome + ", " + formatadorData.format(dataNasc) + ", " + email + ", " + telefone);
            }

        } catch (SQLException ex) {
            Logger.getLogger(Agenda.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Agenda.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Agenda.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Agenda.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public static void cadastrarPessoa() throws SQLException, ClassNotFoundException {
        PreparedStatement stmt = null;
        Connection conn = null;

        String sql = "";
    }

    public static void removerPessoa() {

    }

    public static void alterarPessoa() {

    }

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);
        int entrada;
        do {
            System.out.println("O que deseja fazer?");
            System.out.println("1-) Cadastrar");
            System.out.println("2-) Consultar");
            System.out.println("3-) Alterar");
            System.out.println("4-) Excluir");
            System.out.println("0-) Sair");
            entrada = input.nextInt();

            switch (entrada) {
                case 1:

                case 2:
                    listarPessoas();
            }

        } while (entrada != 0);

    }

}
