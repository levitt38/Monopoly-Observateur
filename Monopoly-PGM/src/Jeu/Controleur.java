/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Jeu;

import Data.Evenement;
import Data.TypeCarreau;
import Exceptions.joueurDeadException;
import Exceptions.partieFinieException;
import IHM.Affichage;
import IHM.Questions;
import IHM.TextColors;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author mouhatcl
 */
public class Controleur {
    private Monopoly monopoly;
    private boolean partieContinue = true;
    private boolean lancerDouble = true;
    
    public Controleur() {
        this.monopoly = new Monopoly();
    }

    public void payerJoueur(Joueur j){
        j.recevoirPaie();
        Questions.affiche(TextColors.GREEN+"Joueur "+j.getNomJoueur()+" recoit sa paie : +200€"+TextColors.RESET);
    }
    
    private int lancerDes(){
        return (int)(Math.random()*100%6)+1;
    }
    
    public Carreau lancerDesAvancer(Joueur j){
        //Lancer1
        int lancer = lancerDes(), position = 0;
        //Lancer2
        int lancer2 = lancerDes();
        //Est-ce un double ?
        if(lancer==lancer2){
            this.lancerDouble = true;
        }
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
        this.monopoly.getJoueurs().remove(j);
        Questions.affiche(TextColors.RED+"Le joueur "+j.getNomJoueur()+" vient d'être éliminé"+TextColors.RESET);
    }
    
    public void partieEstFinie() throws partieFinieException{
        if(this.monopoly.getJoueurs().size()<2){
            this.partieContinue = false;
            Questions.affiche(TextColors.BLUE+"Partie Terminée !! "+TextColors.GREEN+"Le joueur "+monopoly.getJoueurs().get(0).getNomJoueur()+" l'emporte");
            throw new partieFinieException();
        }
    }
    
    public void jouerUnCoup(Joueur j) throws joueurDeadException{
        this.lancerDouble=true;
        while(lancerDouble){
            this.lancerDouble = false;
            j.setPositionCourante(lancerDesAvancer(j));
            Carreau c = j.getPositionCourante(); CarreauAchetable cAchetable = null;
            if(c.getType()!=TypeCarreau.AuteCarreau) {
                cAchetable = (CarreauAchetable)j.getPositionCourante();
            }
            Evenement res = c.evenementEnCours(j);
            switch(res){
                case PayéLoyer : Questions.affiche(j.getNomJoueur()+"paye un loyer de "+cAchetable.calculLoyer()+"€ a"+cAchetable.getProprietaire()); 
                                 j.payerLoyer(cAchetable.calculLoyer());
                                 cAchetable.getProprietaire().payerLoyer(cAchetable.calculLoyer());
                                 break;
                case SurSaCase : Questions.affiche("Vous êtes sur une de vos propriété, détendez vous"); break;
                case AchatPossible : String choix = "non";                                    
                              if(Questions.askYN("Voulez-vous acheter "+c.getNomCarreau()+" pour "+cAchetable.getPrixAchat()+"€ ?")){
                                  j.payerLoyer(cAchetable.getPrixAchat());
                                  j.addCarreauAchetable(cAchetable);
                              } break;
                case AchatImpossible : Questions.affiche("Vous n'avez pas le budget pour acheter ce bien"); break;
                default : Questions.affiche("tour"); ;
            }
            //Renvoie une arithmetic exception si le joueur fait faillite
            j.estBankrupt();
        }
    }
    
       
    public void initPartie(){
            int nb;
            nb=Questions.askNb("Entrez le nombre de joueurs");
            for(int i=0;i<nb;i++){
                this.monopoly.addJoueur(new Joueur(Questions.askStr("Entrez le nom du joueur "+Integer.toString(i+1)),this.monopoly.getCarreau(0)));
                Affichage.AfficherJoueur(this.monopoly.getJoueurs().get(this.monopoly.getJoueurs().size()-1));
            }
            Affichage.afficherPlateau(this.monopoly);
    }
    
    public void Play(){
            initPartie();
            while(partieContinue){
                try {
                    for (Joueur j:this.monopoly.getJoueurs()){
                        try{
                            Affichage.afficherPlateau(monopoly);
                            Affichage.AfficherJoueur(j);
                            this.jouerUnCoup(j);
                        }
                        //Une exception est levée si le joueur est insolvable, puis on vérifie si la partie continue
                        catch(joueurDeadException e) {
                            joueurDead(j); 
                            partieEstFinie();
                        } 
                        Affichage.afficherFinTour();
                    }
                } 
                //Si exception => le boolean=false et on arrête de boucler
                catch(partieFinieException e) {};
            }
    }

    public Monopoly getMonopoly() {
        return monopoly;
    }
    
    
   
}
