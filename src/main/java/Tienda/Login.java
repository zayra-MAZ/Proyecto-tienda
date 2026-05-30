package Tienda;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login extends JFrame {

    private JTextField txtUsuario;
    private JTextField txtEmail;
    private JPasswordField txtPassword;

    public Login() {

        setTitle("Iniciar sesión");
        setSize(350, 580);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(null);

        getContentPane().setBackground(new Color(245, 247, 250));

        JLabel titulo = new JLabel("Iniciar sesión");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        titulo.setForeground(new Color(45, 45, 45));
        titulo.setBounds(90, 20, 200, 30);
        add(titulo);

        try {
            ImageIcon icono = new ImageIcon(getClass().getResource("/Tienda/imagenes/osito.png"));
            Image imagenEscalada = icono.getImage().getScaledInstance(130, 130, Image.SCALE_SMOOTH);
            JLabel lblImagen = new JLabel(new ImageIcon(imagenEscalada));
            lblImagen.setBounds(105, 60, 130, 130);
            add(lblImagen);
        } catch (Exception ignored) { }

        JLabel lblUsuario = new JLabel("Usuario");
        lblUsuario.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblUsuario.setForeground(new Color(70, 70, 70));
        lblUsuario.setBounds(40, 210, 260, 20);
        add(lblUsuario);

        txtUsuario = new JTextField();
        txtUsuario.setBounds(40, 235, 260, 35);
        txtUsuario.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtUsuario.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2));
        add(txtUsuario);

        JLabel lblEmail = new JLabel("Correo electrónico");
        lblEmail.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblEmail.setForeground(new Color(70, 70, 70));
        lblEmail.setBounds(40, 285, 260, 20);
        add(lblEmail);

        txtEmail = new JTextField();
        txtEmail.setBounds(40, 310, 260, 35);
        txtEmail.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtEmail.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2));
        add(txtEmail);

        JLabel lblPassword = new JLabel("Contraseña");
        lblPassword.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblPassword.setForeground(new Color(70, 70, 70));
        lblPassword.setBounds(40, 360, 260, 20);
        add(lblPassword);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(40, 385, 260, 35);
        txtPassword.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtPassword.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2));
        add(txtPassword);

        JButton btnEntrar = new JButton("Entrar");
        btnEntrar.setBounds(40, 440, 260, 38);
        btnEntrar.setBackground(new Color(52, 152, 219));
        btnEntrar.setForeground(Color.WHITE);
        btnEntrar.setFocusPainted(false);
        btnEntrar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnEntrar.setBorder(BorderFactory.createEmptyBorder());
        add(btnEntrar);

        JButton btnRegistrar = new JButton("Crear cuenta");
        btnRegistrar.setBounds(40, 485, 260, 38);
        btnRegistrar.setBackground(new Color(46, 204, 113));
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnRegistrar.setBorder(BorderFactory.createEmptyBorder());
        add(btnRegistrar);

        btnEntrar.addActionListener(e -> {
            String user = txtUsuario.getText().trim();
            String email = txtEmail.getText().trim();
            String pass  = new String(txtPassword.getPassword());

            if (user.isEmpty() || email.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Complete todos los campos.");
            } else {
                validarAcceso(user, email, pass);
            }
        });

        btnRegistrar.addActionListener(e -> {
            new RegistroUsuario().setVisible(true);
        });
    }

    private void validarAcceso(String user, String email, String password) {

        String sql = "SELECT rol FROM usuarios " +
                     "WHERE usuario = ? AND email = ? AND contrasena = ?";

        try (
            Connection con = Conexion.conectar();
            PreparedStatement pst = con.prepareStatement(sql)
        ) {
            pst.setString(1, user);
            pst.setString(2, email);
            pst.setString(3, password);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                String rol = rs.getString("rol"); 

                JOptionPane.showMessageDialog(this, "Bienvenido, " + user + "!");
                this.setVisible(false);
                this.dispose();

                if ("gerente".equalsIgnoreCase(rol)) {
                    javax.swing.SwingUtilities.invokeLater(() -> {
                        VistaGerente vg = new VistaGerente(user);
                        vg.setVisible(true);
                    });
                } else {
                    javax.swing.SwingUtilities.invokeLater(() -> {
                        modelo m = new modelo();
                        vista v  = new vista(m.obtenerCategorias(), user);
                        new controlador(m, v);
                        v.setVisible(true);
                    });
                }

            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "Usuario o contraseña incorrectos.",
                    "Error de acceso",
                    JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error de conexión: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> new Login().setVisible(true));
    }
}
