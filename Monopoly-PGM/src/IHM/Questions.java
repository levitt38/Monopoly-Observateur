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
        affiche(s);
        return scan.nextInt();
    }
    
    public static String askStr(String s){
        affiche(s);
        return scan.nextLine();
    }
    
    public static boolean askYN(String s){
        String choix = "";
        while(!choix.equals("oui") && !choix.equals("non")){
            choix = askStr(s+" (oui/non)").toLowerCase();
        }
        return choix.equals("oui");
    }
}
