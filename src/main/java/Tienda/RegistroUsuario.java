package Tienda;
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegistroUsuario extends JFrame {

    private static final String CODIGO_GERENTE = "JEFE2025";

    private JTextField txtUsuario;
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JPasswordField txtConfirmar;
    private JComboBox<String> cmbRol;
    private JPasswordField txtCodigoGerente;
    private JLabel lblCodigo;

    public RegistroUsuario() {

        setTitle("Crear cuenta");
        setSize(380, 560);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(null);
        getContentPane().setBackground(new Color(245, 247, 250));

        JLabel titulo = new JLabel("Crear cuenta nueva");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        titulo.setForeground(new Color(45, 45, 45));
        titulo.setBounds(70, 18, 260, 28);
        add(titulo);

        int y = 60;
        int gap = 60;

        add(label("Usuario", y));
        txtUsuario = campo(y + 22);
        add(txtUsuario);
        y += gap;

        add(label("Correo electrónico", y));
        txtEmail = campo(y + 22);
        add(txtEmail);
        y += gap;

        add(label("Contraseña", y));
        txtPassword = passField(y + 22);
        add(txtPassword);
        y += gap;

        add(label("Confirmar contraseña", y));
        txtConfirmar = passField(y + 22);
        add(txtConfirmar);
        y += gap;

        add(label("Tipo de cuenta", y));
        cmbRol = new JComboBox<>(new String[]{"Cliente", "Gerente"});
        cmbRol.setBounds(40, y + 22, 290, 32);
        cmbRol.setFont(new Font("SansSerif", Font.PLAIN, 13));
        add(cmbRol);
        y += gap;

        lblCodigo = label("Código de autorización (Gerente)", y);
        lblCodigo.setVisible(false);
        add(lblCodigo);

        txtCodigoGerente = passField(y + 22);
        txtCodigoGerente.setVisible(false);
        add(txtCodigoGerente);

        cmbRol.addActionListener(e -> {
            boolean esGerente = "Gerente".equals(cmbRol.getSelectedItem());
            lblCodigo.setVisible(esGerente);
            txtCodigoGerente.setVisible(esGerente);
        });

        y += gap;

        JButton btnRegistrar = new JButton("Registrar");
        btnRegistrar.setBounds(40, y + 10, 290, 38);
        btnRegistrar.setBackground(new Color(46, 204, 113));
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnRegistrar.setBorder(BorderFactory.createEmptyBorder());
        add(btnRegistrar);

        btnRegistrar.addActionListener(e -> registrar());

        setSize(380, y + 110);
    }

    private void registrar() {
        String usuario  = txtUsuario.getText().trim();
        String email    = txtEmail.getText().trim();
        String pass     = new String(txtPassword.getPassword());
        String confirm  = new String(txtConfirmar.getPassword());
        String rolSel   = (String) cmbRol.getSelectedItem();
        String rol      = "Gerente".equals(rolSel) ? "gerente" : "cliente";
        
        if (usuario.isEmpty() || email.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete todos los campos.");
            return;
        }
        if (!pass.equals(confirm)) {
            JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden.");
            return;
        }
        if (!email.contains("@")) {
            JOptionPane.showMessageDialog(this, "Correo electrónico no válido.");
            return;
        }

        if ("gerente".equals(rol)) {
            String codigo = new String(txtCodigoGerente.getPassword());
            if (!CODIGO_GERENTE.equals(codigo)) {
                JOptionPane.showMessageDialog(this,
                    "Código de gerente incorrecto.",
                    "Acceso denegado",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        String sql = "INSERT INTO usuarios (usuario, email, contrasena, rol) VALUES (?, ?, ?, ?)";

        try (
            Connection con = Conexion.conectar();
            PreparedStatement pst = con.prepareStatement(sql)
        ) {
            pst.setString(1, usuario);
            pst.setString(2, email);
            pst.setString(3, pass);
            pst.setString(4, rol);
            pst.executeUpdate();

            JOptionPane.showMessageDialog(this,
                "Cuenta creada exitosamente.\nRol: " + rolSel);
            dispose();

        } catch (SQLException ex) {
            if (ex.getMessage().contains("Duplicate")) {
                JOptionPane.showMessageDialog(this,
                    "Ya existe un usuario con ese nombre o correo.");
            } else {
                JOptionPane.showMessageDialog(this,
                    "Error al registrar: " + ex.getMessage());
            }
        }
    }

    private JLabel label(String texto, int y) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 12));
        lbl.setForeground(new Color(70, 70, 70));
        lbl.setBounds(40, y, 290, 18);
        return lbl;
    }

    private JTextField campo(int y) {
        JTextField tf = new JTextField();
        tf.setBounds(40, y, 290, 32);
        tf.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tf.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2));
        return tf;
    }

    private JPasswordField passField(int y) {
        JPasswordField pf = new JPasswordField();
        pf.setBounds(40, y, 290, 32);
        pf.setFont(new Font("SansSerif", Font.PLAIN, 13));
        pf.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2));
        return pf;
    }
}
