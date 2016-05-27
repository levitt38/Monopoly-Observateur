
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
        private int loyerTerrainNu; //prix incrémenté par construction supp, dans version PT
        private String groupe;
      
        
        public Propriete(int numero, String nomCarreau, String groupe,int prixAchat, int loyerNu) {
            super(numero, nomCarreau, prixAchat);
            this.loyerTerrainNu = loyerNu;
            this.groupe = groupe;
        }
        
        @Override
        public TypeCarreau getType(){
            return TypeCarreau.PropriteteAConstruire;
        }
        
        public String getNomCarreau(){
            return super.getNomCarreau()+"/"+this.groupe;
        }
        
        @Override
	public int calculLoyer() {
            return loyerTerrainNu;
	}
}