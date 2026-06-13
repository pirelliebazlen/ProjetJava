package Library.view.GUI.dialog;

import Library.model.entity.Livre;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class LivreDialog extends JDialog {

    private Livre resultat = null;

    private final JTextField fTitre  = new JTextField(20);
    private final JTextField fAuteur = new JTextField(20);
    private final JTextField fAnnee  = new JTextField(20);
    private final JTextField fPages  = new JTextField(20);
    private final JTextField fImage  = new JTextField(20);

    public LivreDialog(Frame parent, Livre existant) {
        super(parent, existant == null ? "Nouveau livre" : "Modifier livre", true);

        if (existant != null) {
            fTitre.setText(existant.getTitre());
            fAuteur.setText(existant.getAuteur());
            fAnnee.setText(String.valueOf(existant.getAnnee()));
            fPages.setText(String.valueOf(existant.getNbPages()));
            if (existant.getImagePath() != null)
                fImage.setText(existant.getImagePath());
        }

        JButton btnParcourir = new JButton("Parcourir");
        btnParcourir.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
                fImage.setText(fc.getSelectedFile().getAbsolutePath());
        });

        JPanel imgPanel = new JPanel(new BorderLayout(4, 0));
        imgPanel.add(fImage, BorderLayout.CENTER);
        imgPanel.add(btnParcourir, BorderLayout.EAST);

        JPanel form = new JPanel(new GridLayout(5, 2, 8, 8));
        form.setBorder(BorderFactory.createEmptyBorder(15, 15, 5, 15));
        form.add(new JLabel("Titre :"));  form.add(fTitre);
        form.add(new JLabel("Auteur :")); form.add(fAuteur);
        form.add(new JLabel("Année :"));  form.add(fAnnee);
        form.add(new JLabel("Pages :"));  form.add(fPages);
        form.add(new JLabel("Image :"));  form.add(imgPanel);

        JButton btnOk     = new JButton("OK");
        JButton btnCancel = new JButton("Annuler");

        btnOk.addActionListener(e -> valider(existant));
        btnCancel.addActionListener(e -> dispose());

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.add(btnOk);
        btnPanel.add(btnCancel);

        setLayout(new BorderLayout());
        add(form, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(parent);
    }

    private void valider(Livre existant) {
        try {
            Livre l = new Livre(
                    fTitre.getText(),
                    Integer.parseInt(fAnnee.getText()),
                    LocalDate.now(),
                    fAuteur.getText(),
                    Integer.parseInt(fPages.getText())
            );
            l.setImagePath(fImage.getText());
            if (existant != null) l.setId(existant.getId());
            resultat = l;
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Année et pages doivent être des nombres.",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** @return le Livre encodé, ou null si annulé */
    public Livre getResultat() { return resultat; }


}