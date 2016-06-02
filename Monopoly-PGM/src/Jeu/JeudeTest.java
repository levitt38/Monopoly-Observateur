/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Jeu;

import Data.Evenement;
import Data.TypeCarreau;
import IHM.Questions;
import IHM.TextColors;
import java.util.Scanner;

/**
 *
 * @author nourik
 */
public class JeudeTest extends Controleur{
    private  int i =0;
    private int[] liste = {1,1,2,3,4,1,2,5,4,3,2,4,1,2,1,1,2,1,2,3,4,5};
    
    
    public JeudeTest(){
        super();
    }
    
    public Carreau fakeDouble(Joueur j){
        //Lancer1
        int lancer = liste[i]; int position = 0;
        i++;
        //Lancer2
        int lancer2 = liste[i];
        i++;
        //Est-ce un double ?
        if(lancer==lancer2){
            lancerDouble = true;
            Questions.affiche(TextColors.BLUE+"Vous avez fait un double !"+TextColors.RESET);
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
    
    public void fakeJouerUnCoup(Joueur j){
        Carreau c = j.getPositionCourante(); CarreauAchetable cAchetable = null;
            if(c.getType()!=TypeCarreau.AutreCarreau) {
                cAchetable = (CarreauAchetable)j.getPositionCourante();
            }
            Evenement res = c.action(j);
            switch(res){
                case PayerLoyer : Questions.affiche(j.getNomJoueur()+" paye un loyer de "+cAchetable.calculLoyer()+"€ a"+cAchetable.getProprietaire()); 
                                 j.payerLoyer(cAchetable);
                                 cAchetable.getProprietaire().payerLoyer(cAchetable);
                                 break;
                case SurSaCase : Questions.affiche("Vous êtes sur une de vos propriété, détendez vous"); break;
                case AchatPossible : String choix = "non";                                    
                              if(Questions.askYN("Voulez-vous acheter "+c.getNomCarreau()+" pour "+cAchetable.getPrixAchat()+"€ ?")){
                                  j.payerLoyer(cAchetable);
                                  cAchetable.acheter(j);
                              } break;
                case AchatImpossible : Questions.affiche("Vous n'avez pas le budget pour acheter ce bien"); break;
                default : Questions.affiche("Vous êtes tranquille. Pour le moment..."); ;
            }
    }
    
    public static void main(String[] args){
        Scanner scan = new Scanner(System.in);
        JeudeTest jeu = new JeudeTest();
        //1) Affichage de chaque carreau
        for(Carreau c : jeu.getMonopoly().getCarreaux().values()){
            //on affichera chaque carreau
        }
        //2) Initialiser partie
        jeu.initPartie();
        /* Création des joueurs test */
        jeu.getMonopoly().getJoueurs().clear();
        Joueur joueur_test = new Joueur("joueur_test",jeu.getMonopoly().getCarreau(0));
        Joueur joueur_test2 = new Joueur("joueur_test2",jeu.getMonopoly().getCarreau(0));
        Joueur joueur_test3 = new Joueur("joueur_test3",jeu.getMonopoly().getCarreau(0));
        //3) Lancer dès avancer
        Questions.affiche(TextColors.CYAN+"Test lancer dès et avancer"+TextColors.RESET);
        String s = scan.nextLine();
        //
        System.out.println("position joueur avant: "+joueur_test.getPositionCourante().getNumero()+" "+joueur_test.getPositionCourante().getNomCarreau());
        joueur_test.setPositionCourante(jeu.lancerDesAvancer(joueur_test));
        System.out.println("position joueur après: "+joueur_test.getPositionCourante().getNumero()+" "+joueur_test.getPositionCourante().getNomCarreau());
        //4) Joueur fait un double
        Questions.affiche(TextColors.CYAN+"Test lancer double"+TextColors.RESET);
        s = scan.nextLine();
        //
        System.out.println("position joueur avant: "+joueur_test.getPositionCourante().getNumero()+" "+joueur_test.getPositionCourante().getNomCarreau());
        joueur_test.setPositionCourante(jeu.fakeDouble(joueur_test));
        System.out.println("position joueur après: "+joueur_test.getPositionCourante().getNumero()+" "+joueur_test.getPositionCourante().getNomCarreau());
        //5) Joueur touche sa paie
        Questions.affiche(TextColors.CYAN+"Test recevoir paie"+TextColors.RESET);
        s = scan.nextLine();
        joueur_test.setPositionCourante(jeu.getMonopoly().getCarreau(39));
        jeu.lancerDesAvancer(joueur_test);
        //6) Joueur achete une propriete
        Questions.affiche(TextColors.CYAN+"Test achat proprieté par joueur_test"+TextColors.RESET);
        s = scan.nextLine();
        joueur_test.setPositionCourante(jeu.getMonopoly().getCarreau(1));
        jeu.fakeJouerUnCoup(joueur_test);
        
        //7) Joueur n'as pas le budget pour acheter une proprieté
        Questions.affiche(TextColors.CYAN+"Test achat impossible par joueur_test"+TextColors.RESET);
        s = scan.nextLine();
        joueur_test.setCash(10);
        joueur_test.setPositionCourante(jeu.getMonopoly().getCarreau(6));
        jeu.fakeJouerUnCoup(joueur_test);
        //8) joueur-test2 paie loyer a joueur-test
        Questions.affiche(TextColors.CYAN+"joueur_test paie loyer a joueur_test2 sur Bd de Belleville"+TextColors.RESET);
        s = scan.nextLine();
        joueur_test2.setPositionCourante(jeu.getMonopoly().getCarreau(1));
        jeu.fakeJouerUnCoup(joueur_test2);
        //9) joueur-test visite sa proprieté
        Questions.affiche(TextColors.CYAN+"joueur_test visite sa proprieté du Bd de Belleville"+TextColors.RESET);
        s = scan.nextLine();
        joueur_test.setPositionCourante(jeu.getMonopoly().getCarreau(1));
        jeu.fakeJouerUnCoup(joueur_test);
        //10) calcul loyer gare
        Questions.affiche(TextColors.CYAN+"loyer gare pour 1 gare"+TextColors.RESET);
        s = scan.nextLine();
        CarreauAchetable gare1 = (CarreauAchetable)jeu.getMonopoly().getCarreau(15);
        gare1.acheter(joueur_test);
        joueur_test2.setPositionCourante(jeu.getMonopoly().getCarreau(15));
        jeu.fakeJouerUnCoup(joueur_test2);
        ////////////////////////
        Questions.affiche(TextColors.CYAN+"loyer gare pour 2 gare"+TextColors.RESET);
        s = scan.nextLine();
        CarreauAchetable gare2 = (CarreauAchetable)jeu.getMonopoly().getCarreau(5);
        gare2.acheter(joueur_test);
        joueur_test2.setPositionCourante(jeu.getMonopoly().getCarreau(5));
        jeu.fakeJouerUnCoup(joueur_test2);
        //11) calcul loyer compagnie
        Questions.affiche(TextColors.CYAN+"loyer gare pour 1 compagnie"+TextColors.RESET);
        s = scan.nextLine();
        CarreauAchetable compagnie1 = (CarreauAchetable)jeu.getMonopoly().getCarreau(12);
        compagnie1.acheter(joueur_test);
        joueur_test2.setPositionCourante(jeu.getMonopoly().getCarreau(12));
        jeu.fakeJouerUnCoup(joueur_test2);
        ////////////////
        Questions.affiche(TextColors.CYAN+"loyer gare pour 2 compagnie"+TextColors.RESET);
        s = scan.nextLine();
        CarreauAchetable compagnie2 = (CarreauAchetable)jeu.getMonopoly().getCarreau(28);
        compagnie2.acheter(joueur_test);
        joueur_test2.setPositionCourante(jeu.getMonopoly().getCarreau(28));
        jeu.fakeJouerUnCoup(joueur_test2);
        //12) joueur-test2 est bankrupt
        /*Questions.affiche(TextColors.CYAN+"joueur-test est bankrupt"+TextColors.RESET);
        s = scan.nextLine();*/
        //13) fin de la partie => joueur-test3 est aussi bankrupt
    }   
}









































