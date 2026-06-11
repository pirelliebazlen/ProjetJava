package Library.view.GUI;

import Library.controller.ControllerAdmin;
import Library.model.dao.DAODocument;
import Library.model.entity.Document;
import Library.model.entity.DVD;
import Library.model.entity.Livre;
import Library.model.entity.Magazine;
import Library.view.ViewAdmin;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import java.util.ArrayList;

public class AdminFrame extends JFrame implements ViewAdmin {

    private final DefaultTableModel modelLivres;
    private final DefaultTableModel modelDVDs;
    private final DefaultTableModel modelMagazines;

    private final JTable tableLivres;
    private final JTable tableDVDs;
    private final JTable tableMagazines;

    private final JTextField txtTitre;
    private final JTextField txtAuteur;
    private final JTextField txtAnnee;
    private final JTextField txtType;
    private final JLabel imageLabel;

    private final JButton btnAjouter;
    private final JButton btnModifier;
    private final JButton btnSupprimer;

    private DAODocument daoDocument;

    public AdminFrame() {
        super("Library Management");
        setSize(1400, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) { System.exit(0); }
        });

        JMenuBar menuBar = new JMenuBar();
        JMenu menuSession = new JMenu("Gestion");
        JMenuItem itemDeconnexion = new JMenuItem("Se déconnecter");
        itemDeconnexion.setActionCommand("DECONNEXION");
        menuSession.add(itemDeconnexion);
        menuBar.add(menuSession);
        setJMenuBar(menuBar);

        JPanel main = new JPanel(new BorderLayout(15, 15));
        main.setBorder(new EmptyBorder(15, 15, 15, 15));
        setContentPane(main);

        JLabel titre = new JLabel("LIBRARY MANAGEMENT SYSTEM", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 28));
        main.add(titre, BorderLayout.NORTH);

        JPanel tablesPanel = new JPanel(new GridLayout(1, 3, 10, 10));

        modelLivres = creerModel("ID", "Titre", "Auteur", "Pages");
        tableLivres = new JTable(modelLivres);
        tableLivres.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        styliserTable(tableLivres);
        tablesPanel.add(creerPanelTable("LIVRES", tableLivres));

        modelDVDs = creerModel("ID", "Titre", "Durée");
        tableDVDs = new JTable(modelDVDs);
        tableDVDs.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        styliserTable(tableDVDs);
        tablesPanel.add(creerPanelTable("DVD", tableDVDs));

        modelMagazines = creerModel("ID", "Titre", "Numéro");
        tableMagazines = new JTable(modelMagazines);
        tableMagazines.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        styliserTable(tableMagazines);
        tablesPanel.add(creerPanelTable("MAGAZINES", tableMagazines));

        main.add(tablesPanel, BorderLayout.CENTER);

        JPanel detailsPanel = new JPanel(new BorderLayout(10, 10));
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Détails du document"));

        JPanel form = new JPanel(new GridLayout(4, 2, 4, 8));
        form.setBorder(new EmptyBorder(10, 10, 10, 10));

        form.add(new JLabel("Titre :"));
        txtTitre = new JTextField(); txtTitre.setEditable(false); form.add(txtTitre);

        form.add(new JLabel("Auteur :"));
        txtAuteur = new JTextField(); txtAuteur.setEditable(false); form.add(txtAuteur);

        form.add(new JLabel("Année :"));
        txtAnnee = new JTextField(); txtAnnee.setEditable(false); form.add(txtAnnee);

        form.add(new JLabel("Type :"));
        txtType = new JTextField(); txtType.setEditable(false); form.add(txtType);

        imageLabel = new JLabel("Pas d'image", SwingConstants.CENTER);
        imageLabel.setPreferredSize(new Dimension(200, 220));
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        JPanel panelBtns = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        btnAjouter   = new JButton("Ajouter");
        btnModifier  = new JButton("Modifier");
        btnSupprimer = new JButton("Supprimer");

        styliserBouton(btnAjouter,   new Color(52, 152, 219));
        styliserBouton(btnModifier,  new Color(46, 204, 113));
        styliserBouton(btnSupprimer, new Color(231, 76, 60));

        btnAjouter.setActionCommand("AJOUTER");
        btnModifier.setActionCommand("MODIFIER");
        btnSupprimer.setActionCommand("SUPPRIMER");

        panelBtns.add(btnAjouter);
        panelBtns.add(btnModifier);
        panelBtns.add(btnSupprimer);

        JPanel centerDetails = new JPanel(new BorderLayout());
        centerDetails.add(form, BorderLayout.CENTER);
        centerDetails.add(panelBtns, BorderLayout.SOUTH);

        detailsPanel.add(centerDetails, BorderLayout.CENTER);
        detailsPanel.add(imageLabel, BorderLayout.EAST);

        main.add(detailsPanel, BorderLayout.SOUTH);

        ajouterListenerSelection();
    }

    public void setDAODocument(DAODocument dao) {
        this.daoDocument = dao;
    }

    private void ajouterListenerSelection() {
        tableLivres.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;
            if (tableLivres.getSelectedRow() != -1) {
                tableDVDs.clearSelection();
                tableMagazines.clearSelection();
                afficherDetails(getSelectedDocument());
            }
        });
        tableDVDs.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;
            if (tableDVDs.getSelectedRow() != -1) {
                tableLivres.clearSelection();
                tableMagazines.clearSelection();
                afficherDetails(getSelectedDocument());
            }
        });
        tableMagazines.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;
            if (tableMagazines.getSelectedRow() != -1) {
                tableLivres.clearSelection();
                tableDVDs.clearSelection();
                afficherDetails(getSelectedDocument());
            }
        });
    }

    private void afficherDetails(Document d) {
        if (d == null) return;

        txtTitre.setText(d.getTitre());
        txtAnnee.setText(String.valueOf(d.getAnnee()));
        txtType.setText(d.getType());
        txtAuteur.setText(d instanceof Livre l ? l.getAuteur() : "—");

        if (d.getImagePath() != null && !d.getImagePath().isBlank()) {
            ImageIcon icon = new ImageIcon(d.getImagePath());
            Image img = icon.getImage().getScaledInstance(200, 220, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(img));
            imageLabel.setText("");
        } else {
            imageLabel.setIcon(null);
            imageLabel.setText("Pas d'image");
        }
    }

    @Override
    public Document getSelectedDocument() {
        if (daoDocument == null) return null;

        int row;

        row = tableLivres.getSelectedRow();
        if (row != -1) {
            int id = (int) modelLivres.getValueAt(row, 0);
            return daoDocument.getById(id);
        }

        row = tableDVDs.getSelectedRow();
        if (row != -1) {
            int id = (int) modelDVDs.getValueAt(row, 0);
            return daoDocument.getById(id);
        }

        row = tableMagazines.getSelectedRow();
        if (row != -1) {
            int id = (int) modelMagazines.getValueAt(row, 0);
            return daoDocument.getById(id);
        }

        return null;
    }

    @Override
    public void afficherLivres(ArrayList<Document> livres) {
        modelLivres.setRowCount(0);
        for (Document d : livres) {
            Livre l = (Livre) d;
            modelLivres.addRow(new Object[]{l.getId(), l.getTitre(), l.getAuteur(), l.getNbPages()});
        }
    }

    @Override
    public void afficherDVDs(ArrayList<Document> dvds) {
        modelDVDs.setRowCount(0);
        for (Document d : dvds) {
            DVD dvd = (DVD) d;
            modelDVDs.addRow(new Object[]{dvd.getId(), dvd.getTitre(), dvd.getDureeMinutes()});
        }
    }

    @Override
    public void afficherMagazines(ArrayList<Document> magazines) {
        modelMagazines.setRowCount(0);
        for (Document d : magazines) {
            Magazine m = (Magazine) d;
            modelMagazines.addRow(new Object[]{m.getId(), m.getTitre(), m.getNumero()});
        }
    }

    @Override public Livre promptNouveauLivre()              { return promptLivre(null); }
    @Override public Livre promptModifierLivre(Livre l)      { return promptLivre(l); }
    @Override public DVD promptNouveauDVD()                  { return promptDVD(null); }
    @Override public DVD promptModifierDVD(DVD d)            { return promptDVD(d); }
    @Override public Magazine promptNouveauMagazine()        { return promptMagazine(null); }
    @Override public Magazine promptModifierMagazine(Magazine m) { return promptMagazine(m); }

    private Livre promptLivre(Livre existant) {
        JTextField fTitre  = new JTextField(existant != null ? existant.getTitre() : "");
        JTextField fAuteur = new JTextField(existant != null ? existant.getAuteur() : "");
        JTextField fAnnee  = new JTextField(existant != null ? String.valueOf(existant.getAnnee()) : "");
        JTextField fPages  = new JTextField(existant != null ? String.valueOf(existant.getNbPages()) : "");
        JTextField fImage  = new JTextField(existant != null && existant.getImagePath() != null ? existant.getImagePath() : "");
        JButton btnParcourir = new JButton("Parcourir");
        btnParcourir.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
                fImage.setText(fc.getSelectedFile().getAbsolutePath());
        });
        JPanel imgPanel = new JPanel(new BorderLayout(4, 0));
        imgPanel.add(fImage, BorderLayout.CENTER);
        imgPanel.add(btnParcourir, BorderLayout.EAST);

        JPanel p = new JPanel(new GridLayout(5, 2, 8, 8));
        p.add(new JLabel("Titre :"));  p.add(fTitre);
        p.add(new JLabel("Auteur :")); p.add(fAuteur);
        p.add(new JLabel("Année :"));  p.add(fAnnee);
        p.add(new JLabel("Pages :"));  p.add(fPages);
        p.add(new JLabel("Image :"));  p.add(imgPanel);

        int res = JOptionPane.showConfirmDialog(this, p,
                existant == null ? "Nouveau livre" : "Modifier le livre",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (res != JOptionPane.OK_OPTION) return null;

        try {
            Livre l = new Livre(fTitre.getText(), Integer.parseInt(fAnnee.getText()),
                    LocalDate.now(), fAuteur.getText(), Integer.parseInt(fPages.getText()));
            l.setImagePath(fImage.getText());
            if (existant != null) l.setId(existant.getId());
            return l;
        } catch (NumberFormatException ex) {
            showError("Valeur numérique invalide.");
            return null;
        }
    }

    private DVD promptDVD(DVD existant) {
        JTextField fTitre = new JTextField(existant != null ? existant.getTitre() : "");
        JTextField fAnnee = new JTextField(existant != null ? String.valueOf(existant.getAnnee()) : "");
        JTextField fDuree = new JTextField(existant != null ? String.valueOf(existant.getDureeMinutes()) : "");
        JTextField fImage = new JTextField(existant != null && existant.getImagePath() != null ? existant.getImagePath() : "");
        JButton btnParcourir = new JButton("Parcourir");
        btnParcourir.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
                fImage.setText(fc.getSelectedFile().getAbsolutePath());
        });
        JPanel imgPanel = new JPanel(new BorderLayout(4, 0));
        imgPanel.add(fImage, BorderLayout.CENTER);
        imgPanel.add(btnParcourir, BorderLayout.EAST);

        JPanel p = new JPanel(new GridLayout(4, 2, 8, 8));
        p.add(new JLabel("Titre :"));       p.add(fTitre);
        p.add(new JLabel("Année :"));       p.add(fAnnee);
        p.add(new JLabel("Durée (min) :")); p.add(fDuree);
        p.add(new JLabel("Image :"));       p.add(imgPanel);

        int res = JOptionPane.showConfirmDialog(this, p,
                existant == null ? "Nouveau DVD" : "Modifier le DVD",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (res != JOptionPane.OK_OPTION) return null;

        try {
            DVD d = new DVD(fTitre.getText(), Integer.parseInt(fAnnee.getText()),
                    LocalDate.now(), Integer.parseInt(fDuree.getText()));
            d.setImagePath(fImage.getText());
            if (existant != null) d.setId(existant.getId());
            return d;
        } catch (NumberFormatException ex) {
            showError("Valeur numérique invalide.");
            return null;
        }
    }

    private Magazine promptMagazine(Magazine existant) {
        JTextField fTitre  = new JTextField(existant != null ? existant.getTitre() : "");
        JTextField fAnnee  = new JTextField(existant != null ? String.valueOf(existant.getAnnee()) : "");
        JTextField fNumero = new JTextField(existant != null ? String.valueOf(existant.getNumero()) : "");
        JTextField fImage  = new JTextField(existant != null && existant.getImagePath() != null ? existant.getImagePath() : "");
        JButton btnParcourir = new JButton("Parcourir");
        btnParcourir.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
                fImage.setText(fc.getSelectedFile().getAbsolutePath());
        });
        JPanel imgPanel = new JPanel(new BorderLayout(4, 0));
        imgPanel.add(fImage, BorderLayout.CENTER);
        imgPanel.add(btnParcourir, BorderLayout.EAST);

        JPanel p = new JPanel(new GridLayout(4, 2, 8, 8));
        p.add(new JLabel("Titre :"));  p.add(fTitre);
        p.add(new JLabel("Année :"));  p.add(fAnnee);
        p.add(new JLabel("Numéro :")); p.add(fNumero);
        p.add(new JLabel("Image :"));  p.add(imgPanel);

        int res = JOptionPane.showConfirmDialog(this, p,
                existant == null ? "Nouveau magazine" : "Modifier le magazine",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (res != JOptionPane.OK_OPTION) return null;

        try {
            Magazine m = new Magazine(fTitre.getText(), Integer.parseInt(fAnnee.getText()),
                    LocalDate.now(), Integer.parseInt(fNumero.getText()));
            m.setImagePath(fImage.getText());
            if (existant != null) m.setId(existant.getId());
            return m;
        } catch (NumberFormatException ex) {
            showError("Valeur numérique invalide.");
            return null;
        }
    }

    @Override
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Erreur", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void setController(ControllerAdmin c) {
        btnAjouter.addActionListener(c);
        btnModifier.addActionListener(c);
        btnSupprimer.addActionListener(c);
        getJMenuBar().getMenu(0).getItem(0).addActionListener(c);
    }

    @Override public void fermer() { dispose(); }

    private DefaultTableModel creerModel(String... colonnes) {
        return new DefaultTableModel(colonnes, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
    }

    private JPanel creerPanelTable(String titre, JTable table) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(titre));
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private void styliserTable(JTable table) {
        table.setRowHeight(28);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        table.setGridColor(new Color(220, 220, 220));
        table.setShowGrid(true);
    }

    private void styliserBouton(JButton b, Color bg) {
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Arial", Font.BOLD, 14));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setPreferredSize(new Dimension(140, 45));
    }
}