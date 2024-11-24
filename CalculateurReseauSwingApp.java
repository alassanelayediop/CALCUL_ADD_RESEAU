// CalculateurReseauSwingApp
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CalculateurReseauSwingApp extends UIComposant {

    private JButton boutonSauvegarder;  // Bouton Sauvegarder
    private JButton boutonExit;         // Bouton Exit
    private Reseau reseauCalcul;        // Stocker l'objet Reseau pour la sauvegarde

    public CalculateurReseauSwingApp() {
        super();
        initialiserUI();
    }

    @Override
    public void initialiserUI() {
        // Configuration de la fenêtre principale
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10)); // Espacement global de 10 px
        frame.setSize(600, 400); // Dimensions de la fenêtre (largeur x hauteur)
        frame.setResizable(false);

        JPanel panneauPrincipal = new JPanel(new GridBagLayout());
        GridBagConstraints contraintes = new GridBagConstraints();
        contraintes.insets = new Insets(10, 10, 10, 10); // Marges uniformes

        // Bloc pour les champs Adresse IP et Masque
        JPanel panneauChamps = new JPanel(new GridLayout(2, 2, 5, 5));
        panneauChamps.add(new JLabel("Adresse IP :"));
        panneauChamps.add(champAdresseIP);
        panneauChamps.add(new JLabel("Masque :"));
        panneauChamps.add(champMasque);

        contraintes.gridx = 0;
        contraintes.gridy = 0;
        panneauPrincipal.add(panneauChamps, contraintes);

        // Panneau pour le bouton Calculer (en haut)
        JPanel panneauBoutonCalculer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panneauBoutonCalculer.add(boutonCalculer);

        contraintes.gridx = 0;
        contraintes.gridy = 1;
        panneauPrincipal.add(panneauBoutonCalculer, contraintes);

        // Zone de résultats
        JPanel panneauResultats = new JPanel(new BorderLayout());
        panneauResultats.add(new JLabel("Résultats :"), BorderLayout.NORTH);
        panneauResultats.add(new JScrollPane(zoneResultats), BorderLayout.CENTER);

        contraintes.gridx = 0;
        contraintes.gridy = 2;
        panneauPrincipal.add(panneauResultats, contraintes);

        // Ajouter le panneau principal au centre
        frame.add(panneauPrincipal, BorderLayout.CENTER);

        // Panneau pour les boutons Sauvegarder et Exit (en bas)
        JPanel panneauBoutonsBas = new JPanel(new GridBagLayout());
        GridBagConstraints contraintesBas = new GridBagConstraints();
        contraintesBas.insets = new Insets(10, 10, 10, 10); // Marges uniformes
        contraintesBas.fill = GridBagConstraints.HORIZONTAL; // Alignement horizontal

        // Bouton Sauvegarder à gauche
        contraintesBas.gridx = 0;
        contraintesBas.gridy = 0;
        boutonSauvegarder = new JButton("Sauvegarder");
        panneauBoutonsBas.add(boutonSauvegarder, contraintesBas);

        // Bouton Exit à droite
        contraintesBas.gridx = 1;
        contraintesBas.gridy = 0;
        contraintesBas.anchor = GridBagConstraints.EAST; // Aligné à droite
        boutonExit = new JButton("Exit");
        panneauBoutonsBas.add(boutonExit, contraintesBas);

        // Ajouter les boutons en bas
        frame.add(panneauBoutonsBas, BorderLayout.SOUTH);

        // Gestionnaire d'événements pour Calculer
        boutonCalculer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String adresseIP = champAdresseIP.getText().trim();
                    String masque = champMasque.getText().trim();

                    reseauCalcul = new Reseau(adresseIP, masque);
                    reseauCalcul.calculerPlageAdresse();

                    String resultats = String.format("Adresse IP : %s\nClasse : %s\nAdresse réseau : %s\nPremière adresse : %s\nDernière adresse : %s\nMasque (bits) : /%d\n",
                        adresseIP,
                        reseauCalcul.calculerClasse(),
                        reseauCalcul.getAdresseReseau(),
                        reseauCalcul.getAdresseDebut(),
                        reseauCalcul.getAdresseFin(),
                        reseauCalcul.getMasqueEnBits()
                    );
                    mettreAJourResultats(resultats);

                    champAdresseIP.setText("");
                    champMasque.setText("");
                } catch (InvalidIPException ex) {
                    JOptionPane.showMessageDialog(frame, ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Une erreur est survenue.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }                
            }
        });

        // Gestionnaire d'événements pour Sauvegarder
        boutonSauvegarder.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (reseauCalcul != null) {
                    int confirmation = JOptionPane.showConfirmDialog(frame, "Voulez-vous vraiment sauvegarder ces résultats ?",
                            "Confirmation de sauvegarde", JOptionPane.YES_NO_OPTION);

                    if (confirmation == JOptionPane.YES_OPTION) {
                        try {
                            HistoriqueCalculs hc = new HistoriqueCalculs();
                            hc.ajouterCalcul(reseauCalcul);
                            hc.sauvegarderHistorique("fichier.txt");
                            JOptionPane.showMessageDialog(frame, "Les résultats ont été sauvegardés avec succès.",
                                    "Sauvegarde réussie", JOptionPane.INFORMATION_MESSAGE);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(frame, "Erreur lors de la sauvegarde.", "Erreur", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Veuillez d'abord effectuer un calcul avant de sauvegarder.",
                            "Aucun résultat", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        // Gestionnaire d'événements pour Exit
        boutonExit.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                int confirmationExit = JOptionPane.showConfirmDialog(frame, "Voulez-vous vraiment quitter l'application ?", "Confirmation de sortie", JOptionPane.YES_NO_OPTION);
                if (confirmationExit == JOptionPane.YES_OPTION)
                {
                    System.exit(0);
                }
            }
        });

        // Configurer la taille et rendre visible
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void mettreAJourResultats(String resultats) 
    {
        zoneResultats.setText(resultats);
    }
}