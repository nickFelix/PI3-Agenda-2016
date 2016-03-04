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
import java.text.ParseException;
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

    //Recebe nome, data, telefone e email como parametros
    public static void cadastrarPessoa(String nome, Date data, String telefone, String email
    ) throws SQLException, ClassNotFoundException {
        DateFormat formatadorData = new SimpleDateFormat("yyyy/MM/dd");
        formatadorData.format(data);
        System.out.println(data);

        PreparedStatement stmt = null;
        Connection conn = obterConexao();

        String sql = "INSERT INTO TB_CONTATO (NM_CONTATO, DT_NASCIMENTO, VL_TELEFONE, VL_EMAIL, DT_CADASTRO)VALUES (?,?,?,?,CURRENT_TIMESTAMP)";
        stmt = conn.prepareStatement(sql);

        stmt.setString(1, nome);
        stmt.setDate(2, data);
        stmt.setString(3, telefone);
        stmt.setString(4, email);
        stmt.execute();
        stmt.close();
        System.out.println("Sucesso!");
    }

    public static void removerPessoa(int id) throws SQLException, ClassNotFoundException {
        Connection conn = obterConexao();
        String sql = "DELETE FROM TB_CONTATO WHERE ID_CONTATO = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.execute();
        }
            System.out.println("sucesso!");
        
    }

    public static void alterarPessoa() {

    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException, ParseException {

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
                    System.out.println("Informe o nome do contato");
                    String nome = input.nextLine();
                    nome = input.nextLine();

                    System.out.println("Informe a data de nascimento no formato (dd/mm/AAAA)");
                    String data = input.nextLine();

                    System.out.println("Informe o telefone");
                    String telefone = input.nextLine();

                    System.out.println("Informe o email do contato");
                    String email = input.nextLine();
                    
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                    Date date = new Date(format.parse(data).getTime());
                    cadastrarPessoa(nome, date, telefone, email);
                    
                    break;
                case 2:
                    listarPessoas();
                    break;

                case 4:
                    System.out.println("Informe o ID cadastro deseja excluir");
                    listarPessoas();
                    int id = input.nextInt();
                    removerPessoa(id);

                    break;
            }

        } while (entrada != 0);

    }

}
