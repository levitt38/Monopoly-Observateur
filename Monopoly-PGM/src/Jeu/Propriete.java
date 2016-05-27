
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
      
        
        public Propriete(int numero, String nomCarreau, int prixAchat, int loyerNu) {
            super(numero, nomCarreau, prixAchat);
            this.loyerTerrainNu = loyerNu;
        }
        
        @Override
        public TypeCarreau getType(){
            return TypeCarreau.PropriteteAConstruire;
        }
        
        @Override
	public int calculLoyer() {
            return loyerTerrainNu;
	}
}