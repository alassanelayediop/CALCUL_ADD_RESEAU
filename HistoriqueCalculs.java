import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class HistoriqueCalculs
{
    private ArrayList<AdresseIP> historique; // Liste des calculs

    public HistoriqueCalculs()
    {
        historique = new ArrayList<>();
    }

    // Ajouter un calcul à l'historique
    public void ajouterCalcul(AdresseIP adresse)
    {
        historique.add(adresse);
    }

    // Sauvegarder l'historique dans un fichier
    public void sauvegarderHistorique(String nomFichier) throws InvalidIPException
    {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nomFichier, true))) 
        { // Mode append
            for (AdresseIP adresse : historique) 
            {
                writer.write(adresse.toString());
                writer.newLine(); // Ajoute un saut de ligne après chaque calcul
            }
            historique.clear(); // Vide la collection après sauvegarde pour éviter les doublons
        } catch (IOException e) {
            throw new InvalidIPException("Erreur lors de la sauvegarde de l'historique : " + e.getMessage());
        }
    }
}