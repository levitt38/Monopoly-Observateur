/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Jeu;

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
    
    public static void main(String[] args){
        Scanner scan = new Scanner(System.in);
        JeudeTest jeu = new JeudeTest();
        //1) Affichage de chaque carreau
        for(Carreau c : jeu.getMonopoly().getCarreaux().values()){
            //on affichera chaque carreau
        }
        //2) Initialiser partie
        jeu.initPartie();
        /* Création du joueur test */
        Joueur joueur_test = jeu.getMonopoly().getJoueurs().get(0);
        Joueur joueur_test2 = jeu.getMonopoly().getJoueurs().get(1);
        //3) Lancer dès avancer
        Questions.affiche(TextColors.CYAN+"Test lancer dès et avancer");
        String s = scan.nextLine();
        //
        System.out.println("position joueur avant: "+joueur_test.getPositionCourante().getNumero()+" "+joueur_test.getPositionCourante().getNomCarreau());
        joueur_test.setPositionCourante(jeu.lancerDesAvancer(joueur_test));
        System.out.println("position joueur après: "+joueur_test.getPositionCourante().getNumero()+" "+joueur_test.getPositionCourante().getNomCarreau());
        //4) Joueur fait un double
        Questions.affiche(TextColors.CYAN+"Test lancer double");
        s = scan.nextLine();
        //
        joueur_test.setPositionCourante(jeu.fakeDouble(joueur_test));
        //5)                                                                                                                                                                                                                                                                                                                                                                                                                                                                
    }
}









































