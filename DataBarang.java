import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

class DataBarang extends JFrame {
    private JTextField kodeBarangField;
    private JTextField namaBarangField;
    private JTextField jumlahStokField;
    private JTextField hargaSatuanField;
    private JButton submitButton;

    public DataBarang() {
        setTitle("Form Data Barang");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2));

        JLabel kodeBarangLabel = new JLabel("Kode Barang:");
        kodeBarangField = new JTextField();
        JLabel namaBarangLabel = new JLabel("Nama Barang:");
        namaBarangField = new JTextField();
        JLabel jumlahStokLabel = new JLabel("Jumlah Stok:");
        jumlahStokField = new JTextField();
        JLabel hargaSatuanLabel = new JLabel("Harga Satuan:");
        hargaSatuanField = new JTextField();

        submitButton = new JButton("Submit");

        panel.add(kodeBarangLabel);
        panel.add(kodeBarangField);
        panel.add(namaBarangLabel);
        panel.add(namaBarangField);
        panel.add(jumlahStokLabel);
        panel.add(jumlahStokField);
        panel.add(hargaSatuanLabel);
        panel.add(hargaSatuanField);
        panel.add(new JLabel());  // Placeholder for spacing
        panel.add(submitButton);

        add(panel);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String kodeBarang = kodeBarangField.getText();
                String namaBarang = namaBarangField.getText();
                int jumlahStok = Integer.parseInt(jumlahStokField.getText());
                double hargaSatuan = Double.parseDouble(hargaSatuanField.getText());

                // Menggunakan class conn untuk membuat koneksi ke database
                try {
                    Connection conn = Conn.getConnection(); // Ganti Conn dengan nama class koneksi Anda
                    Statement stmt = conn.createStatement();

                    // Membuat query SQL
                    String sql = "INSERT INTO barang (kodeBarang, namaBarang, jumlahStok, hargaSatuan) VALUES ('" + kodeBarang + "', '" + namaBarang + "', " + jumlahStok + ", " + hargaSatuan + ")";

                    // Menjalankan query SQL
                    stmt.executeUpdate(sql);

                    // Menampilkan pesan bahwa data telah berhasil dimasukkan
                    JOptionPane.showMessageDialog(null, "Data berhasil dimasukkan ke database", "Sukses", JOptionPane.INFORMATION_MESSAGE);

                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Terjadi kesalahan saat memasukkan data ke database", "Error", JOptionPane.ERROR_MESSAGE);
                }

                // Setel semua bidang input ke kosong setelah submit
                kodeBarangField.setText("");
                namaBarangField.setText("");
                jumlahStokField.setText("");
                hargaSatuanField.setText("");
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DataBarang form = new DataBarang ();
            form.setVisible(true);
        });
    }
}