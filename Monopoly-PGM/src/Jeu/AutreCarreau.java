
package Jeu;

import Data.Evenement;
import Data.TypeCarreau;

/**
 *
 * @author nourik
 */
public class AutreCarreau extends Carreau {
	private Joueur proprietaire;

        public AutreCarreau(int num, String chaine){
            super(num,chaine);
        }
           
    @Override
    public TypeCarreau getType(){
        return TypeCarreau.AutreCarreau;
    }

    @Override
    public Evenement action(Joueur j) {
        return Evenement.Rien;
    }
    
    
}