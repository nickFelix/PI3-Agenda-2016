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
import javax.swing.JOptionPane;

/**
 *
 * @author Giovane Pecora e Nicolas
 */
public class Agenda {

    private static Connection obterConexao() throws SQLException, ClassNotFoundException {
        //variavel 'conn' ta local agr, e nao mais global.
        Connection conn = null;
        // Passo 1: Registrar driver JDBC.
        Class.forName("org.apache.derby.jdbc.ClientDriver");

        //slá que isso ai \/'-'
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

        String sql = "INSERT INTO TB_CONTATO (NM_CONTATO, DT_NASCIMENTO, VL_TELEFONE, VL_EMAIL, DT_CADASTRO)VALUES (?,?,?,?,?)";
        stmt = conn.prepareStatement(sql);

        stmt.setString(1, nome);
        stmt.setDate(2, data);
        stmt.setString(3, telefone);
        stmt.setString(4, email);
        stmt.setDate(5, new java.sql.Date(System.currentTimeMillis()));
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

    public static void alterarNome(int id, String nome) throws SQLException, ClassNotFoundException {
        //antes tava ID_PESSOA, agr mudei pra certo. mudei nos outros alterar tbm.
        
        try {
            PreparedStatement stmt;
            Connection conn = obterConexao();
            String sql = "UPDATE TB_CONTATO SET NM_CONTATO = ? WHERE ID_CONTATO = ?";

            stmt = conn.prepareStatement(sql);
            stmt.setString(1, nome);
            stmt.setInt(2, id);
            stmt.execute();
            stmt.close();
        } catch (SQLException sqlE) {
            JOptionPane.showMessageDialog(null, " [Erro ao processar alterar " + sqlE.getMessage() + "]");
        }
    }

    public static void alterarData(int id, String data) throws ClassNotFoundException, ParseException, SQLException {
        Connection conn = obterConexao();
        Date date = formataData(data);
        try {
            String sql = "UPDATE TB_CONTATO "
                    + "SET DT_NASCIMENTO = ?"
                    + "WHERE ID_CONTATO= ?";
            PreparedStatement stmt;

            stmt = conn.prepareStatement(sql);
            stmt.setDate(1, date);
            stmt.setInt(2, id);
            stmt.execute();
            stmt.close();
        } catch (SQLException sqlE) {
            JOptionPane.showMessageDialog(null, " [Erro ao processar alterar " + sqlE.getMessage() + "]");
        }
    }

    public static void alterarTelefone(int id, String tel) throws ClassNotFoundException {
        try {
            Connection conn = obterConexao();
            String sql = "UPDATE TB_CONTATO "
                    + "SET VL_TELEFONE = ?"
                    + "WHERE ID_CONTATO = ?";
            PreparedStatement stmt;

            stmt = conn.prepareStatement(sql);
            stmt.setString(1, tel);
            stmt.setInt(2, id);
            stmt.execute();
            stmt.close();
            System.out.println("DEU CERTO!");
        } catch (SQLException sqlE) {
            JOptionPane.showMessageDialog(null, " [Erro ao processar alterar " + sqlE.getMessage() + "]");
        }
    }

    public static void alterarEmail(int id, String email) throws ClassNotFoundException {
        try {
            Connection conn = Agenda.obterConexao();
            String sql = "UPDATE TB_CONTATO "
                    + "SET VL_EMAIL = ?"
                    + "WHERE ID_CONTATO= ?";
            PreparedStatement stmt;

            stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            stmt.setInt(2, id);
            stmt.execute();
            stmt.close();
        } catch (SQLException sqlE) {
            JOptionPane.showMessageDialog(null, " [Erro ao processar alterar " + sqlE.getMessage() + "]");
        }
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

                    Date date = formataData(data);
                    cadastrarPessoa(nome, date, telefone, email);

                    break;
                case 2:
                    listarPessoas();
                    break;
                case 3:
                    int alterar,
                     id;
                    System.out.println("Digite o ID de quem será alterado: ");
                    id = input.nextInt();
                    System.out.println("O que deseja alterar?");
                    System.out.println("1-) Nome");
                    System.out.println("2-) Data Nascimento");
                    System.out.println("3-) Telefone");
                    System.out.println("4-) Email");
                    alterar = input.nextInt();
                    switch (alterar) {
                        case 1:
                            System.out.println("Digite o novo nome: ");
                            String novoNome = input.nextLine();
                            novoNome = input.nextLine();
                             {
                                try {
                                    alterarNome(id, novoNome);
                                } catch (SQLException ex) {
                                    Logger.getLogger(Agenda.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                            break;
                        case 2:
                            System.out.println("Digite a nova data: ");
                            String novaData = input.nextLine();
                            novaData = input.nextLine();
                             {
                                try {
                                    alterarData(id, novaData);
                                } catch (ClassNotFoundException ex) {
                                    Logger.getLogger(Agenda.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                            break;
                        case 3:
                            System.out.println("Digite o novo telefone: ");
                            String novoTel = input.nextLine();
                            novoTel = input.nextLine();
                             {
                                try {
                                    alterarTelefone(id, novoTel);
                                } catch (ClassNotFoundException ex) {
                                    Logger.getLogger(Agenda.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                            break;
                        case 4:
                            System.out.println("Digite o novo Email: ");
                            String novoEmail = input.nextLine();
                            novoEmail = input.nextLine();
                             {
                                try {
                                    alterarEmail(id, novoEmail);
                                } catch (ClassNotFoundException ex) {
                                    Logger.getLogger(Agenda.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                            break;
                    }
                    break;
                case 4:
                    System.out.println("Informe o ID cadastro deseja excluir");
                    listarPessoas();
                    int id2 = input.nextInt();
                    removerPessoa(id2);

                    break;
            }

        } while (entrada != 0);

    }

    //Função que transforma String (dd/mm/aaaa) em Date SQL
    public static Date formataData(String data) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date(format.parse(data).getTime());

        return date;
    }
}
