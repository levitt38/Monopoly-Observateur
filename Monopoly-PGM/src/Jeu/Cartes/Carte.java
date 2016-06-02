/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Jeu.Cartes;

import Data.TypeCarte;

/**
 *
 * @author Louis
 */
public abstract class Carte {
    private String text;
    private TypeCarte type;
    public TypeCarte getType(){
        return this.type;
    }

    public Carte(String text, TypeCarte type) {
        this.text = text;
        this.type = type;
    }
}
