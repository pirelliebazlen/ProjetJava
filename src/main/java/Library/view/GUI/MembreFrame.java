package Library.view.GUI;

import Library.controller.ControllerMembre;
import Library.model.entity.Document;
import Library.view.ViewMembre;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MembreFrame extends JFrame implements ViewMembre {


    private final DefaultTableModel modelDispo;
    private final DefaultTableModel modelEmpruntes;
    private final JTable tableDispo;
    private final JTable tableEmpruntes;

    private final JButton btnEmprunter;
    private final JButton btnRetourner;

    private final JLabel lblNomMembre;
    private final JLabel lblCompteur;

    public MembreFrame() {
        super("Bibliothèque — Espace Membre");
        setSize(900, 620);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        JMenuBar menuBar = new JMenuBar();
        JMenu menuSession = new JMenu("Session");
        JMenuItem itemDeconnexion = new JMenuItem("Se déconnecter");
        itemDeconnexion.setActionCommand("DECONNEXION");
        menuSession.add(itemDeconnexion);
        menuBar.add(menuSession);
        setJMenuBar(menuBar);

        JPanel main = new JPanel(new BorderLayout(12, 12));
        main.setBackground(new Color(245, 245, 250));
        main.setBorder(new EmptyBorder(16, 16, 16, 16));
        setContentPane(main);

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        lblNomMembre = new JLabel("", SwingConstants.LEFT);
        lblNomMembre.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblNomMembre.setForeground(new Color(44, 62, 80));

        lblCompteur = new JLabel("", SwingConstants.RIGHT);
        lblCompteur.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblCompteur.setForeground(new Color(100, 100, 100));

        header.add(lblNomMembre, BorderLayout.WEST);
        header.add(lblCompteur, BorderLayout.EAST);
        main.add(header, BorderLayout.NORTH);

        JPanel tablesPanel = new JPanel(new GridLayout(1, 2, 16, 0));
        tablesPanel.setOpaque(false);

        modelDispo = creerModel("N°", "Titre", "Type", "Année");
        tableDispo = new JTable(modelDispo);
        tableDispo.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        styliserTable(tableDispo);
        tablesPanel.add(creerPanelTable("Documents disponibles",  tableDispo, new Color(39, 174, 96)));

        modelEmpruntes = creerModel("N°", "Titre", "Type", "Emprunté le");
        tableEmpruntes = new JTable(modelEmpruntes);
        tableEmpruntes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        styliserTable(tableEmpruntes);
        tablesPanel.add(creerPanelTable("Mes emprunts [max 5]",
                tableEmpruntes, new Color(41, 128, 185)));

        main.add(tablesPanel, BorderLayout.CENTER);

        JPanel panelBtns = new JPanel(new FlowLayout(FlowLayout.CENTER, 24, 8));
        panelBtns.setOpaque(false);

        btnEmprunter = new JButton("Emprunter");
        styliserBouton(btnEmprunter, new Color(39, 174, 96));
        btnEmprunter.setActionCommand("EMPRUNTER");

        btnRetourner = new JButton("Retourner");
        styliserBouton(btnRetourner, new Color(41, 128, 185));
        btnRetourner.setActionCommand("RETOURNER");

        panelBtns.add(btnEmprunter);
        panelBtns.add(btnRetourner);
        main.add(panelBtns, BorderLayout.SOUTH);
    }

    @Override
    public void afficherDocumentsDisponibles(Document[] documents) {
        modelDispo.setRowCount(0);
        if (documents == null) return;
        for (Document d : documents) {
            if (d != null) {
                modelDispo.addRow(new Object[]{
                        d.getId(), d.getTitre(), d.getType(), d.getAnnee()
                });
            }
        }
    }

    @Override
    public void afficherDocumentsEmpruntes(Document[] documents) {
        modelEmpruntes.setRowCount(0);
        if (documents == null) return;
        for (Document d : documents) {
            if (d != null) {
                modelEmpruntes.addRow(new Object[]{
                        d.getId(), d.getTitre(), d.getType(), "—"
                });
            }
        }
    }

    @Override
    public void afficherNomMembre(String nom) {
        lblNomMembre.setText("Membre " + nom);
    }

    @Override
    public void afficherNbEmprunts(int nb, int max) {
        lblCompteur.setText("Emprunts : " + nb + " / " + max);
        lblCompteur.setForeground(nb >= max
                ? new Color(192, 57, 43)
                : new Color(100, 100, 100));
    }

    @Override
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message,
                "Information", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message,
                "Erreur", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void setController(ControllerMembre c) {
        btnEmprunter.addActionListener(c);
        btnRetourner.addActionListener(c);
        JMenu session = getJMenuBar().getMenu(0);
        session.getItem(0).addActionListener(c);
    }

    @Override
    public void fermer() {
        dispose();
    }

    public int getSelectedDocumentDisponibleId() {
        int row = tableDispo.getSelectedRow();
        if (row == -1) return -1;
        return (int) modelDispo.getValueAt(row, 0);
    }

    public int getSelectedDocumentEmprunteId() {
        int row = tableEmpruntes.getSelectedRow();

        if (row == -1) return -1;
        return (int) modelEmpruntes.getValueAt(row, 0);
    }

    private DefaultTableModel creerModel(String... colonnes) {
        return new DefaultTableModel(colonnes, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
    }

    private JPanel creerPanelTable(String titre, JTable table, Color couleur) {
        JPanel panel = new JPanel(new BorderLayout(0, 6));
        panel.setOpaque(false);
        JLabel label = new JLabel(titre);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(couleur);
        label.setBorder(new EmptyBorder(0, 0, 4, 0));
        panel.add(label, BorderLayout.NORTH);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(couleur.darker(), 1));
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private void styliserTable(JTable table) {
        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(20, 200, 241));
        table.setGridColor(new Color(220, 220, 220));
        table.setShowGrid(true);
    }

    private void styliserBouton(JButton b, Color bg) {
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setPreferredSize(new Dimension(180, 42));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }
}