package Jeu;

import Data.TypeCarreau;
import Exceptions.joueurDeadException;
import java.util.HashSet;

public class Joueur {
	private String _nomJoueur;
	private int _cash = 1500;
	private HashSet<Gare> _gares;
        private HashSet<Propriete> _proprietes;
        private HashSet<Compagnie> _compagnies;
        private HashSet<CarreauAchetable> _carreaux;
	private Carreau _positionCourante;

    public Joueur(String _nomJoueur, Carreau pos) {
        this._nomJoueur = _nomJoueur;
        this._positionCourante=pos;
        this._gares = new HashSet<>(); 
        this._proprietes = new HashSet<>();
        this._compagnies = new HashSet<>();
        this._carreaux = new HashSet<>();
    }
    
    public void estBankrupt() throws joueurDeadException{
        if(getCash()<0){
            throw new joueurDeadException();
        }
    }
    
    /////////////////////////////////////////////
    public void recevoirPaie(){
        this._cash += 200;
    }
    
    public int getCash() {
        return _cash;
    }
    
    public void recevoirLoyer(int loyer){
        this._cash = getCash()+loyer;
    }
    
    public void payerLoyer(int loyer){
        this._cash = getCash()-loyer;
    }
    /////////////////////////////////////////////
    public Carreau getPositionCourante(){
        return this._positionCourante;
    }
    
    public void setPositionCourante(Carreau _positionCourante) {
        this._positionCourante = _positionCourante;
    }
    /////////////////////////////////////////////
    public HashSet<Gare> getGares() {
        return _gares;
    }
    
    public HashSet<Compagnie> getCompagnies(){
        return _compagnies;
    }
    /////////////////////////////////////////////
    public void addCarreauAchetable(CarreauAchetable c){
        this._carreaux.add(c);
        TypeCarreau type = c.getType();
        switch(type){
            case PropriteteAConstruire : this._proprietes.add((Propriete)c);
            case Gare : this._gares.add((Gare)c);
            case Compagnie : this._compagnies.add((Compagnie)c);
            default : return;
        }
    }
    /////////////////////////////////////////////
    public String getNomJoueur() {
        return _nomJoueur;
    }
   
}