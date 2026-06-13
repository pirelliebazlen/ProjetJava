package Library.view.GUI;

import Library.controller.ControllerAdmin;
import Library.model.dao.DAODocument;
import Library.model.entity.Document;
import Library.model.entity.DVD;
import Library.model.entity.Livre;
import Library.model.entity.Magazine;
import Library.view.GUI.dialog.DVDDialog;
import Library.view.GUI.dialog.LivreDialog;
import Library.view.GUI.dialog.MagazineDialog;
import Library.view.ViewAdmin;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

/**
 * Fenêtre principale de l'administrateur.
 * Représente la <b>Vue</b> dans l'architecture MVC.
 * N'implémente aucune logique métier : tout passe par {@link ControllerAdmin}.
 */
public class AdminFrame extends JFrame implements ViewAdmin {


    private final DefaultTableModel modelLivres    = creerModel("ID", "Titre", "Auteur", "Pages");
    private final DefaultTableModel modelDVDs      = creerModel("ID", "Titre", "Durée");
    private final DefaultTableModel modelMagazines = creerModel("ID", "Titre", "Numéro");

    private final JTable tableLivres    = new JTable(modelLivres);
    private final JTable tableDVDs      = new JTable(modelDVDs);
    private final JTable tableMagazines = new JTable(modelMagazines);


    private final JTextField txtTitre  = champLecture();
    private final JTextField txtAuteur = champLecture();
    private final JTextField txtAnnee  = champLecture();
    private final JTextField txtType   = champLecture();
    private final JLabel     imageLabel = creerImageLabel();


    private final JButton btnAjouter   = creerBouton("Ajouter",   new Color(52, 152, 219), "AJOUTER");
    private final JButton btnModifier  = creerBouton("Modifier",  new Color(46, 204, 113), "MODIFIER");
    private final JButton btnSupprimer = creerBouton("Supprimer", new Color(231, 76, 60),  "SUPPRIMER");

    private DAODocument daoDocument;

    /**
     * Construit la fenêtre d'administration avec toutes ses tables et son panneau de détails.
     */
    public AdminFrame() {
        super("Library Management");
        setSize(1400, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) { System.exit(0); }
        });

        setJMenuBar(construireMenuBar());

        JPanel main = new JPanel(new BorderLayout(15, 15));
        main.setBorder(new EmptyBorder(15, 15, 15, 15));
        setContentPane(main);

        main.add(construireTitre(),   BorderLayout.NORTH);
        main.add(construireTables(),  BorderLayout.CENTER);
        main.add(construireDetails(), BorderLayout.SOUTH);

        configurerTables();
        ajouterListenersSelection();
    }


    private JMenuBar construireMenuBar() {
        JMenuBar bar  = new JMenuBar();
        JMenu menu    = new JMenu("Gestion");
        JMenuItem item = new JMenuItem("Se déconnecter");
        item.setActionCommand("DECONNEXION");
        menu.add(item);
        bar.add(menu);
        return bar;
    }

    private JLabel construireTitre() {
        JLabel lbl = new JLabel("LIBRARY MANAGEMENT SYSTEM", SwingConstants.CENTER);
        lbl.setFont(new Font("Arial", Font.BOLD, 28));
        return lbl;
    }

    private JPanel construireTables() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 10, 10));
        panel.add(creerPanelTable("LIVRES",    tableLivres));
        panel.add(creerPanelTable("DVD",       tableDVDs));
        panel.add(creerPanelTable("MAGAZINES", tableMagazines));
        return panel;
    }

    private JPanel construireDetails() {
        JPanel form = new JPanel(new GridLayout(4, 2, 4, 8));
        form.setBorder(new EmptyBorder(10, 10, 10, 10));
        form.add(new JLabel("Titre :"));  form.add(txtTitre);
        form.add(new JLabel("Auteur :")); form.add(txtAuteur);
        form.add(new JLabel("Année :"));  form.add(txtAnnee);
        form.add(new JLabel("Type :"));   form.add(txtType);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btns.add(btnAjouter);
        btns.add(btnModifier);
        btns.add(btnSupprimer);

        JPanel center = new JPanel(new BorderLayout());
        center.add(form, BorderLayout.CENTER);
        center.add(btns, BorderLayout.SOUTH);

        JPanel details = new JPanel(new BorderLayout(10, 10));
        details.setBorder(BorderFactory.createTitledBorder("Détails du document"));
        details.add(center,     BorderLayout.CENTER);
        details.add(imageLabel, BorderLayout.EAST);
        return details;
    }

    private void configurerTables() {
        for (JTable t : new JTable[]{tableLivres, tableDVDs, tableMagazines}) {
            t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            styliserTable(t);
        }
    }


    private void ajouterListenersSelection() {
        tableLivres.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;
            tableDVDs.clearSelection(); tableMagazines.clearSelection();
            afficherDetails(getSelectedDocument());
        });
        tableDVDs.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;
            tableLivres.clearSelection(); tableMagazines.clearSelection();
            afficherDetails(getSelectedDocument());
        });
        tableMagazines.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;
            tableLivres.clearSelection(); tableDVDs.clearSelection();
            afficherDetails(getSelectedDocument());
        });
    }

    /**
     * Affiche les détails d'un document dans le panneau inférieur.
     * Événement secondaire : ne modifie pas le modèle.
     *
     * @param d le document à afficher
     */
    private void afficherDetails(Document d) {
        if (d == null) return;
        txtTitre.setText(d.getTitre());
        txtAnnee.setText(String.valueOf(d.getAnnee()));
        txtType.setText(d.getType());
        txtAuteur.setText(d instanceof Livre l ? l.getAuteur() : "—");

        if (d.getImagePath() != null && !d.getImagePath().isBlank()) {
            Image img = new ImageIcon(d.getImagePath())
                    .getImage().getScaledInstance(200, 220, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(img));
            imageLabel.setText("");
        } else {
            imageLabel.setIcon(null);
            imageLabel.setText("Pas d'image");
        }
    }


    @Override
    public void setDAODocument(DAODocument dao) { this.daoDocument = dao; }


    @Override
    public Document getSelectedDocument() {
        if (daoDocument == null) return null;
        int row;
        if ((row = tableLivres.getSelectedRow())    != -1)
            return daoDocument.getById((int) modelLivres.getValueAt(row, 0));
        if ((row = tableDVDs.getSelectedRow())      != -1)
            return daoDocument.getById((int) modelDVDs.getValueAt(row, 0));
        if ((row = tableMagazines.getSelectedRow()) != -1)
            return daoDocument.getById((int) modelMagazines.getValueAt(row, 0));
        return null;
    }


    @Override
    public void afficherLivres(ArrayList<Document> livres) {
        modelLivres.setRowCount(0);
        livres.forEach(d -> { Livre l = (Livre) d;
            modelLivres.addRow(new Object[]{l.getId(), l.getTitre(), l.getAuteur(), l.getNbPages()}); });
    }


    @Override
    public void afficherDVDs(ArrayList<Document> dvds) {
        modelDVDs.setRowCount(0);
        dvds.forEach(d -> { DVD dvd = (DVD) d;
            modelDVDs.addRow(new Object[]{dvd.getId(), dvd.getTitre(), dvd.getDureeMinutes()}); });
    }

    @Override
    public void afficherMagazines(ArrayList<Document> magazines) {
        modelMagazines.setRowCount(0);
        magazines.forEach(d -> { Magazine m = (Magazine) d;
            modelMagazines.addRow(new Object[]{m.getId(), m.getTitre(), m.getNumero()}); });
    }


    @Override
    public Livre promptNouveauLivre() {
        LivreDialog dlg = new LivreDialog(this, null);
        dlg.setVisible(true);
        return dlg.getResultat();
    }


    @Override
    public Livre promptModifierLivre(Livre l) {
        LivreDialog dlg = new LivreDialog(this, l);
        dlg.setVisible(true);
        return dlg.getResultat();
    }


    @Override
    public DVD promptNouveauDVD() {
        DVDDialog dlg = new DVDDialog(this, null);
        dlg.setVisible(true);
        return dlg.getResultat();
    }


    @Override
    public DVD promptModifierDVD(DVD d) {
        DVDDialog dlg = new DVDDialog(this, d);
        dlg.setVisible(true);
        return dlg.getResultat();
    }


    @Override
    public Magazine promptNouveauMagazine() {
        MagazineDialog dlg = new MagazineDialog(this, null);
        dlg.setVisible(true);
        return dlg.getResultat();
    }


    @Override
    public Magazine promptModifierMagazine(Magazine m) {
        MagazineDialog dlg = new MagazineDialog(this, m);
        dlg.setVisible(true);
        return dlg.getResultat();
    }


    @Override
    public void showMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Information", JOptionPane.INFORMATION_MESSAGE);
    }


    @Override
    public void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Erreur", JOptionPane.ERROR_MESSAGE);
    }


    @Override
    public void fermer() { dispose(); }


    @Override
    public void setController(ControllerAdmin c) {
        btnAjouter.addActionListener(c);
        btnModifier.addActionListener(c);
        btnSupprimer.addActionListener(c);
        getJMenuBar().getMenu(0).getItem(0).addActionListener(c);
    }

    private static DefaultTableModel creerModel(String... cols) {
        return new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
    }

    private static JTextField champLecture() {
        JTextField f = new JTextField();
        f.setEditable(false);
        return f;
    }

    private static JLabel creerImageLabel() {
        JLabel lbl = new JLabel("Pas d'image", SwingConstants.CENTER);
        lbl.setPreferredSize(new Dimension(200, 220));
        lbl.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        return lbl;
    }

    private static JButton creerBouton(String texte, Color bg, String cmd) {
        JButton b = new JButton(texte);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Arial", Font.BOLD, 14));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setPreferredSize(new Dimension(140, 45));
        b.setActionCommand(cmd);
        return b;
    }

    private static JPanel creerPanelTable(String titre, JTable table) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(titre));
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private static void styliserTable(JTable table) {
        table.setRowHeight(28);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        table.setGridColor(new Color(220, 220, 220));
        table.setShowGrid(true);
    }
}