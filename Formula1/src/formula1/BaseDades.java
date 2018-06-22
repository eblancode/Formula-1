/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package formula1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JOptionPane;

/**
 *
 * @author ebladieg
 */
public class BaseDades {
    private static Connection conn;
    
    private ArrayList<Escuderia> escuderies;
    private ArrayList<Circuit> circuits;
    
    public BaseDades(ArrayList<Escuderia> escuderies, ArrayList<Circuit> circuits) {
        conn = getConnection();
        
        this.escuderies = escuderies;
        this.circuits = circuits;
    }
    
    public static Connection getConnection() {
        Connection conexion=null;
        
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            String servidor = "jdbc:mysql://localhost/f1";
            String usuarioDB="root";
            String passwordDB="";
            conexion= DriverManager.getConnection(servidor,usuarioDB,passwordDB);
        }
        catch(ClassNotFoundException ex)
        {
            JOptionPane.showMessageDialog(null, ex, "Error1 en la Conexión con la BD "+ex.getMessage(), JOptionPane.ERROR_MESSAGE);
            conexion=null;
        }
        catch(SQLException ex)
        {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error en la Conexión con la BD", JOptionPane.ERROR_MESSAGE);
            conexion=null;
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null, ex, "Error3 en la Conexión con la BD "+ex.getMessage(), JOptionPane.ERROR_MESSAGE);
            conexion=null;
        }
        finally
        {
            return conexion;
        }
    }
    
    public static Connection getConn() {
        return conn;
    }

    public ArrayList<Escuderia> getEscuderies() {
        return escuderies;
    }

    public ArrayList<Circuit> getCircuits() {
        return circuits;
    }
    
    public static void saveData(ArrayList<Escuderia> escuderies, ArrayList<Circuit> circuits) {
        try {
            Statement statementCir = conn.createStatement();
            Statement statementEsc = conn.createStatement();
            
            String consultaEscuderia = "select * from escuderies;";
            String consultaCircuits = "select * from circuits;";
            
            ResultSet resultSetEscuderia = statementEsc.executeQuery(consultaEscuderia);
            ResultSet resultSetCircuits = statementCir.executeQuery(consultaCircuits);
            
            if(!resultSetCircuits.next()||!resultSetEscuderia.next()) {
                if(!resultSetCircuits.next()) {
                    saveCircuits(statementCir,circuits);
                } else {
                    System.err.println("Circuits ja afegits a la BD.");
                }

                if(!resultSetEscuderia.next()) {
                    saveEscuderies(statementEsc,escuderies);
                } else {
                    System.err.println("Escuderies ja afegides a la BD.");
                }
                JOptionPane.showMessageDialog(null, "Dades afegides correctament a la BD.");
            }
            else {
                JOptionPane.showMessageDialog(null, "Dades ja afegides a la BD.", null, JOptionPane.INFORMATION_MESSAGE);
            }
        }
        catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex, "Error al treballar amb la base de dades: " + ex.getMessage(), JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void saveEscuderies(Statement statement, ArrayList<Escuderia> escuderies) {
        Iterator<Escuderia> itEscuderia = escuderies.iterator();
        
        while(itEscuderia.hasNext()) {
            Escuderia e = itEscuderia.next();
            String insertEscuderia = "insert into escuderies "
                                   + "(nom, pais) "
                                   + "values ('" + e.getNom() + "', '" + e.getPais() + "')";
            
            try {
                statement.executeUpdate(insertEscuderia);
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
            }
            
            saveBolids(e); 
            saveTreballadors(e);
        }
    }
    
    private static void saveTreballadors(Escuderia e) {
        Iterator<Pilot> itPilots = e.getPilots().iterator();
        Iterator<Provador> itProvadors = e.getProvadors().iterator();
        Iterator<Mecanic> itMecanics = e.getMecanics().iterator();
        Statement stTreballadors = null;
        
        try {
            stTreballadors = conn.createStatement();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        //Pilots
        while(itPilots.hasNext()) {
            Pilot p = itPilots.next();
            
            String insertTreballador = "insert into treballadors "
                                   + "(nom, escuderia_nom) "
                                   + "values ('" + p.getNom() + "', '" + e.getNom() + "')";
            
            String insertPilot = "insert into pilots "
                                   + "(nom, sou) "
                                   + "values ('" + p.getNom() + "', '" + p.getSou() + "')";
            
            try {
                stTreballadors.executeUpdate(insertTreballador);
                stTreballadors.executeUpdate(insertPilot);
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
            }
        }
        
        //Provadors
        while(itProvadors.hasNext()) {
            Provador p = itProvadors.next();
            
            /*String insertTreballador = "insert into treballadors "
                                   + "(nom, escuderia_nom) "
                                   + "values ('" + p.getNom() + "', '" + e.getNom() + "')";*/
            
            String insertProvador = "insert into provadors "
                                   + "(nom) "
                                   + "values ('" + p.getNom() + "')";
            
            try {
                //stTreballadors.executeUpdate(insertTreballador);
                stTreballadors.executeUpdate(insertProvador);
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
            }
        }
        
        //Mecanics
        while(itMecanics.hasNext()) {
            Mecanic m = itMecanics.next();
            Iterator<Part> itParts = m.getParts().iterator();
            
            String insertTreballador = "insert into treballadors "
                                   + "(nom, escuderia_nom) "
                                   + "values ('" + m.getNom() + "', '" + e.getNom() + "')";
            
            String insertMecanic = "insert into mecanics "
                                   + "(nom) "
                                   + "values ('" + m.getNom() + "')";

            try {
                stTreballadors.executeUpdate(insertTreballador);
                stTreballadors.executeUpdate(insertMecanic);
                
                while(itParts.hasNext()) {
                    int part_codi = itParts.next().getCodi();
                    String insertPart = "insert into responsables "
                                   + "(mecanic_nom, part_codi) "
                                   + "values ('" + m.getNom() + "', " + part_codi + ")";
                    stTreballadors.executeUpdate(insertPart);
                }
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
            }
        }
    }
    
    private static void saveBolids(Escuderia e) {
        Iterator<Bolid> itBolids = e.getBolids().iterator();
        Statement stBolids = null;
        
        try {
            stBolids = conn.createStatement();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        while(itBolids.hasNext()) {
            Bolid b = itBolids.next();

            String insertBolid = "insert into bolids "
                               + "(num_bolid, nom_dissenyador, escuderia_nom) "
                               + "values ('" + b.getNum_bolid() + "', '" + b.getNom_dissenyador() + "', '" + e.getNom() + "')";
            
            try {
                stBolids.executeUpdate(insertBolid);
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
            }
            
            savePartsBolid(b);
        }
    }
    
    private static void savePartsBolid(Bolid b) {
        Iterator<Part> itParts = b.getParts().iterator();
        Statement stParts = null;
        
        try {
            stParts = conn.createStatement();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        while(itParts.hasNext()) {
            Part p = itParts.next();
            
            String insertPart = "insert into parts_bolid "
                               + "(codi, descripcio, bolid_num) "
                               + "values ('" + p.getCodi()+ "', '" + p.getDescripcio()+ "', '" + b.getNum_bolid() + "')";
            
            try {
                stParts.executeUpdate(insertPart);
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
            }
        }
    }
    
    private static void saveCircuits(Statement stC, ArrayList<Circuit> circuits) {
        Iterator<Circuit> itCircuits = circuits.iterator();
        
        while(itCircuits.hasNext()) {
            Circuit c = itCircuits.next();
            String insertCircuit = "insert into circuits "
                                   + "(nom) "
                                   + "values ('" + c.getNom() + "')";
            
            try {
                stC.executeUpdate(insertCircuit);
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
            }
        }
    }
    
}

