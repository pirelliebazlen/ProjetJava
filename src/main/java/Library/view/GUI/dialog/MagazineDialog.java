package Library.view.GUI.dialog;


import Library.model.entity.Magazine;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class MagazineDialog extends JDialog {

    private Magazine resultat = null;

    private final JTextField fTitre  = new JTextField(20);
    private final JTextField fAnnee  = new JTextField(20);
    private final JTextField fNumero = new JTextField(20);
    private final JTextField fImage  = new JTextField(20);

    public MagazineDialog(Frame parent, Magazine existant) {
        super(parent, existant == null ? "Nouveau magazine" : "Modifier magazine", true);

        if (existant != null) {
            fTitre.setText(existant.getTitre());
            fAnnee.setText(String.valueOf(existant.getAnnee()));
            fNumero.setText(String.valueOf(existant.getNumero()));
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

        JPanel form = new JPanel(new GridLayout(4, 2, 8, 8));
        form.setBorder(BorderFactory.createEmptyBorder(15, 15, 5, 15));
        form.add(new JLabel("Titre :"));   form.add(fTitre);
        form.add(new JLabel("Année :"));   form.add(fAnnee);
        form.add(new JLabel("Numéro :")); form.add(fNumero);
        form.add(new JLabel("Image :"));   form.add(imgPanel);

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

    private void valider(Magazine existant) {
        try {
            Magazine m = new Magazine(
                    fTitre.getText(),
                    Integer.parseInt(fAnnee.getText()),
                    LocalDate.now(),
                    Integer.parseInt(fNumero.getText())
            );
            m.setImagePath(fImage.getText());
            if (existant != null) m.setId(existant.getId());
            resultat = m;
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Année et numéro doivent être des nombres.",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** @return le Magazine encodé, ou null si annulé */
    public Magazine getResultat() { return resultat; }


}