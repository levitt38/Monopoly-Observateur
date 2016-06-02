/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Jeu;

import java.util.ArrayList;

/**
 *
 * @author nourik
 */
public class Prison extends AutreCarreau{
    private ArrayList<Joueur> listeDétenus;
    
    public Prison(int num){
        super(num,"Prison");
        this.listeDétenus = new ArrayList<>();
    }
    
    public void ajouterDétenu(Joueur j){
        this.listeDétenus.add(j);
    }
    
    public void libérerDétenu(Joueur j){
        this.listeDétenus.remove(j);
    }
    
}
