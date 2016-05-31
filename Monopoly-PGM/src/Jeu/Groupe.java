package Jeu;
import Data.CouleurPropriete;
import java.util.ArrayList;


public class Groupe {
	private CouleurPropriete couleur;
        private ArrayList<Propriete> proprietes;            //Chaque groupe contient de 2 a 3 priopriete
        
        
        
        public Groupe(CouleurPropriete C){
            this.couleur = C;
            
        }
    /**
     * @return the couleur
     */
    public CouleurPropriete getCouleur() {
        return couleur;
    }

    /**
     * @return the proprietes
     */
    public ArrayList<Propriete> getProprietes() {
        return proprietes;
    }

    /**
     * @param proprietes the proprietes to set
     */
    public void setProprietes(ArrayList<Propriete> proprietes) {
        this.proprietes = proprietes;
    }
        
    public void addPropriete(Propriete p){
        this.getProprietes().add(p);
    }
        
}