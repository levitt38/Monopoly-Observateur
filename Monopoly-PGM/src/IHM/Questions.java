/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IHM;
import java.util.Scanner;
/**
 *
 * @author mouhatcl
 */
public class Questions {
    private static Scanner scan = new Scanner(System.in);
    private static int tab = 0;
    
    public static void increment(){
        tab++;
    }
    
    public static void decrement(){
        tab--;
    }
    
    public static void affiche(String s){
        for(int i=0;i<tab;i++){
            System.out.print("  ");
        }
        System.out.println(s);
    }
    
    public static int askNb(String s){
        int choix = 0;
        boolean valide = false;
        do{
            affiche(s);
            try{
                choix = scan.nextInt();
                valide = true;
            } catch(Exception e){ affiche(TextColors.RED+"Merci de rentrer un chiffre svp !"+TextColors.RESET); }
        }while(! valide);
        return choix;
    }
    
    public static String askStr(String s){
        affiche(s); String str;
        do{
            str = scan.nextLine();
        }while(str.equals(""));
        return str;
    }
    
    public static boolean askYN(String s){
        String choix = "";
        boolean valide = true;
        do{
            affiche(s);
            choix = scan.nextLine();
            if(! choix.equalsIgnoreCase("oui") && ! choix.equalsIgnoreCase("non")){
                valide = false;
                affiche(TextColors.RED+"Merci de rentrer soit \"oui\" ou \"non\" !"+TextColors.RESET);
            }
        }while(! valide);
        
        return choix.equalsIgnoreCase("oui");
    }
}
