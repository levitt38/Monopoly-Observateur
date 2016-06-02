
package Jeu;

import Data.TypeCarreau;
import IHM.Affichage;
import IHM.Questions;
import IHM.TextColors;
import java.util.Scanner;

/**
 *
 * @author nourik
 */
public class Propriete extends CarreauAchetable {  
        private int[] loyers; //prix incrémenté par construction supp, dans version PT
        private Groupe groupe;
        private int nbMaisons;//de 0 à 5
        private int prixMaison;
      
        
        public Propriete(int numero, String nomCarreau, Groupe groupe,int prixAchat, int[] loyers, int prixMaison) {
            super(numero, nomCarreau, prixAchat);
            this.loyers = loyers;
            this.groupe = groupe;
            this.nbMaisons=0;
            this.prixMaison=prixMaison;
        }
        
        @Override
        public TypeCarreau getType(){
            return TypeCarreau.ProprieteAConstruire;
        }
        
        public String getNomCarreauColored(){
            return TextColors.colorToCode(this.groupe.getCouleur().toString())+super.getNomCarreau()+TextColors.RESET;
        }
        
        @Override
	public int calculLoyer() {
            return this.getLoyers()[this.getNbMaisons()];
	}

    public int[] getLoyers() {
        return loyers;
    }

    public int getNbMaisons() {
        return nbMaisons;
    }

    public Groupe getGroupe() {
        return groupe;
    }

    public int getPrixMaison() {
        return prixMaison;
    }

    public void setNbMaisons(int nbMaisons) {
        this.nbMaisons = nbMaisons;
    }
}