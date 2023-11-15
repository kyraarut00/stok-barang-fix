import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

class AdminForm extends JFrame {
    private JTextField emailField;  // Updated field name for email
    private JPasswordField passwordField;
    private JButton loginButton;

    public AdminForm() {
        setTitle("Form Admin");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 150);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));

        JLabel emailLabel = new JLabel("Email:");  // Updated label for email
        emailField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();

        loginButton = new JButton("Login");

        panel.add(emailLabel);
        panel.add(emailField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(new JLabel());  // Placeholder for spacing
        panel.add(loginButton);

        add(panel);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();  // Updated variable for email
                char[] password = passwordField.getPassword();

                // Menggunakan class conn untuk membuat koneksi ke database
                try {
                    Connection conn = Conn.getConnection(); // Ganti Conn dengan nama class koneksi Anda
                    Statement stmt = conn.createStatement();

                    // Membuat query SQL
                    String sql = "SELECT * FROM admin WHERE email = '" + email + "' AND password = '" + String.valueOf(password) + "'";

                    // Menjalankan query SQL
                    ResultSet rs = stmt.executeQuery(sql);

                    // Jika ResultSet tidak kosong, berarti login berhasil
                    if (rs.next()) {
                        JOptionPane.showMessageDialog(null, "Login berhasil!", "Admin", JOptionPane.INFORMATION_MESSAGE);
                        // Di sini Anda dapat membuka halaman admin atau melakukan tindakan lainnya setelah login berhasil
                    } else {
                        JOptionPane.showMessageDialog(null, "Login gagal. Email atau password salah.", "Admin", JOptionPane.ERROR_MESSAGE);
                        // Di sini Anda dapat menangani tindakan jika login gagal
                    }

                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Terjadi kesalahan saat melakukan otentikasi", "Error", JOptionPane.ERROR_MESSAGE);
                }

                // Kosongkan bidang input setelah submit
                emailField.setText("");
                passwordField.setText("");
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AdminForm form = new AdminForm();
            form.setVisible(true);
        });
    }
}
