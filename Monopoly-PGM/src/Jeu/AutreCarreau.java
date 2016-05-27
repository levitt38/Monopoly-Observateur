
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
        
        
	public void action(Joueur aJ) {
		throw new UnsupportedOperationException();
	}
        
    @Override
    public TypeCarreau getType(){
        return TypeCarreau.AuteCarreau;
    }

    @Override
    public Evenement evenementEnCours(Joueur j) {
        return Evenement.Rien;
    }
    
    
}