package Jeu;

import Data.CouleurPropriete;
import Exceptions.pasAssezDeMaisonsException;
import IHM.Questions;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Monopoly{
        private HashMap<String,Carreau> carreaux = new HashMap<>();
        private HashSet<Compagnie> compagnies = new HashSet<>();
        private ArrayList<Joueur> joueurs = new ArrayList<>();
        private HashMap<CouleurPropriete,Groupe> groupes;
        private int nbMaisons;
        private int nbHotels;
        private Prison prison;
        
	public void CreerPlateau(String dataFilename){
		buildGamePlateau(dataFilename);
	}
	
	private void buildGamePlateau(String dataFilename)
	{
		try{
			ArrayList<String[]> data = readDataFile(dataFilename, ",");
			
			
			for(int i=0; i<data.size(); ++i){
				String caseType = data.get(i)[0];
				if(caseType.compareTo("P") == 0){
                                        if(groupes.get(CouleurPropriete.valueOf(data.get(i)[3]))==null){
                                            groupes.put(CouleurPropriete.valueOf(data.get(i)[3]), new Groupe(CouleurPropriete.valueOf(data.get(i)[3])));
                                        }
                                        Groupe g = groupes.get(CouleurPropriete.valueOf(data.get(i)[3]));
					//System.out.println("Propriété :\t" + data.get(i)[2] + "\t@ case " + data.get(i)[1]);
                                        int[] prix = new int[6];
                                        for (int j=5;j<11;j++){
                                            prix[j-5]=Integer.valueOf(data.get(i)[j]);
                                        }
                                       Propriete p = new Propriete(Integer.valueOf(data.get(i)[1])-1,data.get(i)[2],g,
                                                Integer.valueOf(data.get(i)[4]),prix,Integer.valueOf(data.get(i)[11]));
                                        
                                        g.addPropriete(p);
                                        getCarreaux().put(Integer.toString(i),p);
                                        
				}
				else if(caseType.compareTo("G") == 0){
					//System.out.println("Gare :\t" + data.get(i)[2] + "\t@ case " + data.get(i)[1]);
                                        getCarreaux().put(Integer.toString(i),new Gare(Integer.valueOf(data.get(i)[1])-1,data.get(i)[2],Integer.valueOf(data.get(i)[3])));
				}
				else if(caseType.compareTo("C") == 0){
					//System.out.println("Compagnie :\t" + data.get(i)[2] + "\t@ case " + data.get(i)[1]);
                                        Compagnie c = new Compagnie(Integer.valueOf(data.get(i)[1])-1,data.get(i)[2],Integer.valueOf(data.get(i)[3]));
                                        getCarreaux().put(Integer.toString(i),c);
                                        getCompagnies().add(c);
				}
				else if(caseType.compareTo("AU") == 0){
					//System.out.println("Case Autre :\t" + data.get(i)[2] + "\t@ case " + data.get(i)[1]);
                                        getCarreaux().put(Integer.toString(i),new AutreCarreau(Integer.valueOf(data.get(i)[1])-1,data.get(i)[2]));
                                        //création case prison
                                        if(i==10){this.prison = new Prison(10);}
				}
				else
                                    if (i!=40){
					System.err.println("[buildGamePleateau()] : Invalid Data type");
                                    }
			}
			
		} 
		catch(FileNotFoundException e){
			System.err.println("[buildGamePlateau()] : File is not found!");
		}
		catch(IOException e){
			System.err.println("[buildGamePlateau()] : Error while reading file!");
		}
	}
	
	private ArrayList<String[]> readDataFile(String filename, String token) throws FileNotFoundException, IOException
	{
		ArrayList<String[]> data = new ArrayList<String[]>();
		
		BufferedReader reader  = new BufferedReader(new FileReader(filename));
		String line = null;
		while((line = reader.readLine()) != null){
			data.add(line.split(token));
		}
		reader.close();
		
		return data;
	}

        public void addJoueur(Joueur j){
            
            this.joueurs.add(j);
            
        }
        public ArrayList<Joueur> getJoueurs(){
            
            return this.joueurs;
            
        }
    /**
     * @return the carreaux
     */
        
    public HashMap<String,Carreau> getCarreaux() {
        return carreaux;
    }
    
    public void construire(Propriete p) throws pasAssezDeMaisonsException{
        if(p.getNbMaisons()<5){
            if (this.nbMaisons==0){
                throw new pasAssezDeMaisonsException();
            }else{
                this.nbMaisons--;
            }
        }else{
            this.nbHotels--;
            this.nbMaisons+=4;
        }
        p.construireMaison();
    }
    
    
    public Carreau getCarreau(int i){
        return this.getCarreaux().get(Integer.toString(i));
    }

    public HashSet<Compagnie> getCompagnies() {
        return compagnies;
    }
    
    public Monopoly() {
        this.groupes = new HashMap<>();
        this.CreerPlateau("data.txt");
        this.nbHotels = 12;
        this.nbMaisons = 32;
    }

    public Prison getPrison() {
        return prison;
    }
    
    
}

