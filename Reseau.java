// Reseau
public class Reseau extends AdresseIP
{
    private String masque;
    private String adresseDebut;
    private String adresseFin;
    private String adresseReseau;

    // Constructeur
    public Reseau(String adresseIP, String masque) throws InvalidIPException 
    {
        super(adresseIP);
        validerMasque(masque);
        this.masque = masque;
        calculerPlageAdresse();
    }

    // Convertir un masque en nombre de bits
    public int getMasqueEnBits() 
    {
        String[] parts = masque.split("\\.");
        int bits = 0;
        for (String part : parts) {
            bits += Integer.bitCount(Integer.parseInt(part));
        }
        return bits;
    }

    @Override
    public void calculerPlageAdresse() 
    {
        // Convertir les octets en un entier 32 bits
        int ip = 0, mask = 0;
        for (int i = 0; i < 4; i++) 
        {
            ip = (ip << 8) | octets[i];
            mask = (mask << 8) | Integer.parseInt(masque.split("\\.")[i]);
        }

        // Calculer l'adresse réseau et l'adresse de diffusion
        int networkAddress = ip & mask;
        int broadcastAddress = networkAddress | ~mask;

        // Calculer les adresses début et fin
        adresseReseau = convertirEnIP(networkAddress); // Adresse réseau
        adresseDebut = convertirEnIP(networkAddress + 1); // Première adresse utilisable
        adresseFin = convertirEnIP(broadcastAddress - 1); // Dernière adresse utilisable
    }


    private String convertirEnIP(int adresse) 
    {
        return String.format("%d.%d.%d.%d", (adresse >> 24) & 0xFF, (adresse >> 16) & 0xFF, (adresse >> 8) & 0xFF, adresse & 0xFF);
    }

    public String getAdresseDebut()
    {
        return adresseDebut;
    }

    public String getAdresseFin()
    {
        return adresseFin;
    }

    public String getAdresseReseau()
    {
        return adresseReseau;
    }

    @Override
    public String toString()
    {
        return super.toString() + " | adresse Resau : " + adresseReseau + " | Masque : " + masque + " | Début : " + adresseDebut + " | Fin : " + adresseFin;
    }

    public void validerMasque(String masque) throws InvalidIPException 
    {
        String[] octets = masque.split("\\.");
        if (octets.length != 4) 
        {
            throw new InvalidIPException("Masque invalide : " + masque);
        }

        int masqueBits = 0;
        for (String octet : octets) 
        {
            int valeur;
            try {
                valeur = Integer.parseInt(octet);
            } catch (NumberFormatException e) {
                throw new InvalidIPException("Masque invalide : " + masque);
            }
            if (valeur < 0 || valeur > 255) {
                throw new InvalidIPException("Masque invalide : " + masque);
            }
            masqueBits = (masqueBits << 8) | valeur;
        }

        // Vérifie que les bits sont contigus (exemple : 11111111 11111111 00000000 00000000)
        boolean bitsContigus = ((~masqueBits + 1) & ~masqueBits) == 0;
        if (!bitsContigus) {
            throw new InvalidIPException("Masque invalide : " + masque);
        }
    }

}