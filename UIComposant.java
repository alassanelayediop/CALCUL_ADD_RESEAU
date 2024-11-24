// UIComposant
import javax.swing.*;

public abstract class UIComposant 
{
    protected JFrame frame;
    protected JTextField champAdresseIP;
    protected JTextField champMasque;
    protected JTextArea zoneResultats;
    protected JButton boutonCalculer;

    public UIComposant() 
    {
        // Initialisation des composants de base
        frame = new JFrame("Calculateur d'Adresse Réseau");
        champAdresseIP = new JTextField(15);
        champMasque = new JTextField(15);
        zoneResultats = new JTextArea(10, 30);
        zoneResultats.setEditable(false);
        boutonCalculer = new JButton("Calculer");
    }

    // Méthodes abstraites pour l'initialisation et la mise à jour
    public abstract void initialiserUI();
    public abstract void mettreAJourResultats(String resultats);
}