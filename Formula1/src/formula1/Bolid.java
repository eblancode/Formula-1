/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package formula1;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author ebladieg
 */
public class Bolid {
    private int num_bolid;
    private String nom_dissenyador;
    private ArrayList<Part> parts;
    //private String escuderia_nom;

    public Bolid() {
    }
    
    public Bolid(int num_bolid, String nom_dissenyador) {
        this.num_bolid = num_bolid;
        this.nom_dissenyador = nom_dissenyador;
    }

    public Bolid(int num_bolid, String nom_dissenyador, ArrayList<Part> parts) {
        this.num_bolid = num_bolid;
        this.nom_dissenyador = nom_dissenyador;
        this.parts = parts;
    }

    public int getNum_bolid() {
        return num_bolid;
    }

    public String getNom_dissenyador() {
        return nom_dissenyador;
    }
    
    public ArrayList<Part> getParts() {
        return parts;
    }
    
    public void setNum_bolid(int num_bolid) {
        this.num_bolid = num_bolid;
    }

    public void setNom_dissenyador(String nom_dissenyador) {
        this.nom_dissenyador = nom_dissenyador;
    }

    public void setParts(ArrayList<Part> parts) {
        this.parts = parts;
    }
    
    public void addParts(Part part) {
        if (this.parts == null) {
            this.parts = new ArrayList<Part>();
        }
        this.parts.add(part);
    }

    @Override
    public String toString() {
        String parts = "";
        if (this.parts!=null) {
            Iterator<Part> itP = this.parts.iterator();
            while(itP.hasNext()) {
                parts += itP.next().getCodi() + ", ";
            }
            parts = parts.substring(0,parts.length()-2);
        }

        return "Bolid " + num_bolid + ": Dissenyat per " + nom_dissenyador + " i consta de les parts " + parts + '.';
    }

    
}
