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
        this.setPositionCourante(pos);
        this._gares = new HashSet<>(); 
        this._proprietes = new HashSet<>();
        this._compagnies = new HashSet<>();
        this._carreaux = new HashSet<>();
    }
    
    public boolean estBankrupt(){
        return this.getCash()<0;
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
        if(this._positionCourante != null){
            this._positionCourante.removeJoueur(this);
        }
        _positionCourante.addJoueur(this);
        this._positionCourante = _positionCourante;
    }
    /////////////////////////////////////////////
    public HashSet<Gare> getGares() {
        return _gares;
    }
    public int getNbGares(){
        return getGares().size();
    }
    public HashSet<Compagnie> getCompagnies(){
        return _compagnies;
    }
    
    public int getNbCompagnies(){ 
        return getCompagnies().size();
    }
    /////////////////////////////////////////////
    public void addCarreauAchetable(CarreauAchetable c){
        this._carreaux.add(c);
        TypeCarreau type = c.getType();
        switch(type){
            case ProprieteAConstruire : this._proprietes.add((Propriete)c);break;
            case Gare : this._gares.add((Gare)c);break;
            case Compagnie : this._compagnies.add((Compagnie)c);break;
            default : return;
        }
    }
    /////////////////////////////////////////////
    public String getNomJoueur() {
        return _nomJoueur;
    }

    void payerPropriete(int prixAchat) {
        this.setCash(this.getCash()-prixAchat);
    }

    public void setCash(int _cash) {
        this._cash = _cash;
    }

    public HashSet<Propriete> getProprietes() {
        return _proprietes;
    }
   
}