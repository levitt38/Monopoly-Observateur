/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Jeu;

import Data.Evenement;
import Data.TypeCarreau;
import Exceptions.joueurDeadException;
import Exceptions.joueurTripleDouble;
import Exceptions.partieFinieException;
import Exceptions.pasAssezDeMaisonsException;
import IHM.Affichage;
import IHM.Questions;
import IHM.TextColors;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mouhatcl
 */
public class Controleur {
    protected Monopoly monopoly;
    protected boolean partieContinue = true;
    protected boolean lancerDouble = false;
    
    public Controleur() {
        this.monopoly = new Monopoly();
    }

    public void payerJoueur(Joueur j){
        j.recevoirPaie();
        Questions.affiche(TextColors.GREEN+"Joueur "+j.getNomJoueur()+" recoit sa paie : +200€"+TextColors.RESET);
    }
    
    private int lancerD6(){
        return (int)(Math.random()*100%6)+1;
    }
    
    public Carreau lancerDesAvancer(Joueur j) throws joueurTripleDouble{
        //Lancer1
        int lancer = lancerD6(), position = 0;
        //Lancer2
        int lancer2 = lancerD6();
        //Est-ce un double ?
        if(lancer==lancer2){
            this.lancerDouble = true;
            // le joueur avait il fait un double au tour precedant ?
            if(j.isDernierDouble()){
            j.setDoublesALaSuite(j.getDoublesALaSuite()+1);
            } else {j.setDoublesALaSuite(0);}
            // le joueur en est il a son troisième double ?
            if(j.getDoublesALaSuite()==3){
                j.setDoublesALaSuite(0);
                Questions.affiche(TextColors.RED+"C'est votre 3ème double => direction prison !"+TextColors.RESET);
                throw new joueurTripleDouble();
            } else { Questions.affiche(TextColors.BLUE+"Vous avez fait un double !"+TextColors.RESET); }
        } else { this.lancerDouble=false; }
        lancer += lancer2;
        //Cette ligne sert a récupérer le montant des dès du lancer pour réaliser le loyer d'une compagnie
        for (Compagnie c : this.getMonopoly().getCompagnies()){
            c.setDernierLancer(lancer);
        }
        //Recup position du joueur
        position = j.getPositionCourante().getNumero()+lancer;
        //Est-ce un jour de paye ?
        if(position>40){
            payerJoueur(j);
        }
        position = position%40;
        //Affichage IHM des dès
        Questions.affiche(TextColors.BLUE+"Résultat lancer : "+j.getNomJoueur()+" a fait un "+lancer+TextColors.RESET);
        //Return carreau correspondant
        return monopoly.getCarreau(position);
    }
    
    public void joueurDead(Joueur j){
        
        for (Propriete p:j.getProprietes()){
            this.monopoly.setNbMaisons(this.monopoly.getNbMaisons()+p.resetMaisons());
            this.monopoly.setNbHotels(this.monopoly.getNbHotels()+(p.resetHotel() ? 1 : 0));
            p.resetProprietaire();
            j.removeCarreauAchetable(p);
        }
        for (Gare g:j.getGares()){
            g.resetProprietaire();
            j.removeCarreauAchetable(g);
        }
        for (Compagnie c:j.getCompagnies()){
            c.resetProprietaire();
            j.removeCarreauAchetable(c);
        }
        Questions.affiche(TextColors.RED+"Le joueur "+j.getNomJoueur()+" vient d'être éliminé"+TextColors.RESET);
    }
    
    public boolean partieEstFinie(){
        int nbAlive = 0;
        for(Joueur j:this.monopoly.getJoueurs()){
            if (!j.estBankrupt()){
                nbAlive+=1;
            }
        }
        return nbAlive<=1;/*
        if(this.monopoly.getJoueurs().size()<2){
            this.partieContinue = false;
            
            throw new partieFinieException();
        }*/
    }
    
    public void gestionPrisonnier(Joueur j){
        //Il faudra gérer si le joueur possède la carte SortirdePrison
        int lancer1, lancer2;
        lancer1 = lancerD6();
        lancer2 = lancerD6();
        if(lancer1==lancer2){
            Questions.affiche(TextColors.GREEN+"Vous avez fait un double, fin de votre séjour en prison ! Vous rejouer"+TextColors.RESET);
            this.monopoly.getPrison().libérerDétenu(j);
            j.setPositionCourante(this.monopoly.getCarreau(lancer1+lancer2));
            // IL FAUDRA GERER LE FAIT QUE LE JOUEUR REJOUE DIRECT DANS LA MAINLOOP
            return; // sort de la méthode
        } else {Questions.affiche(TextColors.RED+"Vous n'avez pas fait de double, vous restez en prison !"+TextColors.RESET); }
        
        j.setNb_toursEnPrison(j.getNb_toursEnPrison()+1);
        
        if(j.getNb_toursEnPrison()==3){
            Questions.affiche(TextColors.RED+"fin de vos 3 tours en prison ! vous payer 50€"+TextColors.RESET);
            j.setCash(j.getCash()-50);
            this.monopoly.getPrison().libérerDétenu(j);
        }
    }
    
    public void evenementCaseAchetable(Evenement res, CarreauAchetable cAchetable, Joueur j){
        switch(res){
            case PayerLoyer : Questions.affiche(j.getNomJoueur()+"paye un loyer de "+cAchetable.getPrixAchat()+"€ a"+cAchetable.getProprietaire().getNomJoueur()); 
                             j.payerLoyer(cAchetable);
                             break;
            case SurSaCase : Questions.affiche("Vous êtes sur une de vos propriété, détendez vous"); break;
            case AchatPossible : String choix = "non";                                    
                          if(Questions.askYN("Voulez-vous acheter "+cAchetable.getNomCarreau()+" pour "+cAchetable.getPrixAchat()+"€ ?")){
                              cAchetable.acheter(j);
                          } break;
            case AchatImpossible : Questions.affiche("Vous n'avez pas le budget pour acheter ce bien"); break;
            default : Questions.affiche("Vous êtes tranquille. Pour le moment..."); ;
        }
    }
    
    public void evenementCaseAutre(Evenement res, AutreCarreau cAutre, Joueur j){
        // A implémenter avec les différents évenements
        switch(res){
            case EstEnPrison : gestionPrisonnier(j); break;
            case AllerEnPrison : this.monopoly.getPrison().emprisonnerDétenu(j); 
                                 Questions.affiche(TextColors.RED+"joueur "+j.getNomJoueur()+" envoyé en prison!"+TextColors.RESET);
                                 break;
            // futures autres évenements : carte chance
        }
    }
    
    public void jouerUnCoup(Joueur j){
        try{
            if(! j.estPrisonnier()){
                j.setPositionCourante(lancerDesAvancer(j));
            }
            Carreau c = j.getPositionCourante(); CarreauAchetable cAchetable = null; AutreCarreau cAutre = null; 
            // Récupère l'évenement en cours indépendament d'une case achetable ou autre
            Evenement res = c.action(j);
            // Est on sur une case achetable ou un autre carreau ?
            if(c.getType()==TypeCarreau.Compagnie || c.getType()==TypeCarreau.Gare || c.getType()==TypeCarreau.ProprieteAConstruire) {
                cAchetable = (CarreauAchetable)j.getPositionCourante();
                evenementCaseAchetable(res,cAchetable,j); // voir deux méthodes d'au dessus ^^^
            } else {
                cAutre = (AutreCarreau)j.getPositionCourante();
                evenementCaseAutre(res,cAutre,j);
            }
            boolean construire = false;
            if(j.getProprietesConstructibles().size()>0){
                construire = Questions.askYN(TextColors.BLUE+"Voulez-vous construire ?"+TextColors.RESET);
            }
            // On gère les constructions éventuelles si le joueur possède tous les carreaux d'un groupe
            while(construire&&j.getProprietesConstructibles().size()>0){
                String s;
                do{
                    s=Questions.askStr("Entrez le nom d'une rue");
                }while(!this.monopoly.getCarreaux().containsKey(s) || !(this.monopoly.getCarreaux().get(s).getType()==TypeCarreau.ProprieteAConstruire));
                Propriete p = (Propriete) this.monopoly.getCarreaux().get(s);
                if(p.getNbMaisons()<5){
                    if(p.getPrixMaison()<=j.getCash()){
                        if(p.getNbMaisons()<=p.getGroupe().getMinMaisons()){
                            try {
                                this.monopoly.construire(p);
                            } catch (pasAssezDeMaisonsException ex) {
                                Questions.affiche(TextColors.RED+"Il n'y a plus de maisons disponibles."+TextColors.RESET);
                            }
                        }else{
                            Questions.affiche(TextColors.RED+"Vous devez d'abord construire sur les autres terrains de ce groupe."+TextColors.RESET);
                        }
                    }else{
                        Questions.affiche(TextColors.RED+"Vous n'avez pas assez d'argent pour construire sur ce terrain."+TextColors.RESET);
                    }
                }else{
                    Questions.affiche(TextColors.RED+"Il y a déjà le nombre maximal de maisons sur ce terrain."+TextColors.RESET);
                }
            }
          // si le joueur en est a son 3eme double => go to prison
        } catch(joueurTripleDouble e){this.monopoly.getPrison().emprisonnerDétenu(j);};
    }
    
       
    public void initPartie(){
            int nb;
            do{
                nb=Questions.askNb("Entrez le nombre de joueurs");
            }while (nb<=1||nb>6);            
            for(int i=0;i<nb;i++){
                this.monopoly.addJoueur(new Joueur(Questions.askStr("Entrez le nom du joueur "+Integer.toString(i+1)),this.monopoly.getCarreau(0)));
                Affichage.AfficherJoueur(this.monopoly.getJoueurs().get(this.monopoly.getJoueurs().size()-1));
            }
    }
    
    public void mainLoop(){
        initPartie();
        int tour = 0;
        while(!this.partieEstFinie()){
            Joueur j=this.getMonopoly().getJoueurs().get(tour);
            if(!j.estBankrupt()){
                Affichage.afficherPlateau(monopoly);
                Affichage.AfficherJoueur(j);
                this.jouerUnCoup(j);
                if(j.estBankrupt()){
                    joueurDead(j); 
                }
                Affichage.afficherFinTour();
            }
            if((!this.lancerDouble)&&(!j.estBankrupt())){
                tour=(tour+1)%this.monopoly.getJoueurs().size();
            }
        }
        for (Joueur j:this.getMonopoly().getJoueurs()){
            if(!j.estBankrupt()){
                Questions.affiche(TextColors.BLUE+"Partie Terminée !! "+TextColors.GREEN+"Le joueur "+j.getNomJoueur()+" l'emporte"+TextColors.RESET);
            }
        }
    }

    public Monopoly getMonopoly() {
        return monopoly;
    }
    
    
   
}
