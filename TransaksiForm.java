import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

class TransaksiForm extends JFrame {
    private JTextField kodeBarangField;
    private JTextField jumlahField;
    private JButton masukButton;
    private JButton keluarButton;
    private JButton tambahStokButton;
    private JTextArea logArea;

    public TransaksiForm() {
        setTitle("Form Transaksi Barang");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2));

        JLabel kodeBarangLabel = new JLabel("Kode Barang:");
        kodeBarangField = new JTextField();
        JLabel jumlahLabel = new JLabel("Jumlah:");
        jumlahField = new JTextField();

        masukButton = new JButton("Lihat Transaksi");
        keluarButton = new JButton("Barang Keluar");
        tambahStokButton = new JButton("Tambah Stok");

        logArea = new JTextArea();
        logArea.setEditable(true);

        panel.add(kodeBarangLabel);
        panel.add(kodeBarangField);
        panel.add(jumlahLabel);
        panel.add(jumlahField);
        panel.add(new JLabel());
        panel.add(masukButton);
        panel.add(new JLabel());
        panel.add(keluarButton);
        panel.add(new JLabel());
        panel.add(tambahStokButton);

        add(panel, BorderLayout.NORTH);
        add(new JScrollPane(logArea), BorderLayout.CENTER);

        masukButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String kodeBarang = kodeBarangField.getText();
                try {
                    Connection conn = Conn.getConnection(); // Ganti Conn dengan nama class koneksi Anda
                    Statement stmt = conn.createStatement();

                    // Membuat query SQL untuk mendapatkan semua transaksi berdasarkan kodeBarang
                    String sql = "SELECT CONCAT('Nama Barang: ', b.namaBarang, ', Jumlah: ', CAST(t.jumlah AS CHAR), ', Jenis Transaksi: ', t.jenis_transaksi) as log FROM transaksi t JOIN barang b ON t.kodeBarang = b.KodeBarang WHERE t.KodeBarang = '" + kodeBarang + "'";

                    // Menjalankan query SQL dan mendapatkan hasilnya
                    ResultSet rs = stmt.executeQuery(sql);

                    // Menampilkan hasil query di logArea
                    while (rs.next()) {
                        String logAtas = "Transaksi Dilakukan \n";
                        String log = rs.getString("log");
                        logArea.append(logAtas + log + "\n");
                    }

                    // Membuat query SQL untuk mendapatkan jumlah barang saat ini berdasarkan kodeBarang
                    sql = "SELECT CONCAT('Nama Barang: ', b.namaBarang, ', Jumlah: ', CAST(b.JumlahStok AS CHAR), ', Harga Satuan: ', b.HargaSatuan) as log FROM barang b WHERE b.KodeBarang = '" + kodeBarang + "'";

                    // Menjalankan query SQL dan mendapatkan hasilnya
                    rs = stmt.executeQuery(sql);

                    // Menampilkan hasil query di logArea
                    if (rs.next()) {
                        String logAtas = "\nSetelah Diupdate \n";
                        String log = rs.getString("log");
                        logArea.append(logAtas + log + "\n");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Terjadi kesalahan saat mendapatkan data transaksi", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        keluarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String kodeBarang = kodeBarangField.getText();
                int jumlah = Integer.parseInt(jumlahField.getText());

                // Menggunakan class conn untuk membuat koneksi ke database
                try {
                    Connection conn = Conn.getConnection(); // Ganti Conn dengan nama class koneksi Anda
                    Statement stmt = conn.createStatement();

                    // Mengecek apakah barang dengan kode tersebut sudah ada di database
                    String checkSql = "SELECT * FROM barang WHERE KodeBarang = '" + kodeBarang + "'";
                    ResultSet resultSet = stmt.executeQuery(checkSql);

                    if (resultSet.next()) {
                        // Membuat query SQL untuk update stok
                        String updateSql = "UPDATE barang SET JumlahStok = JumlahStok - " + jumlah + " WHERE kodeBarang = '" + kodeBarang + "'";
                        stmt.executeUpdate(updateSql);

                        // Catat transaksi ke logArea
                        String log = "Barang Keluar: Kode Barang: " + kodeBarang + ", Jumlah: " + jumlah;
                        logArea.append(log + "\n");
                        String insertSql = "INSERT INTO transaksi (kodeBarang, jumlah, jenis_transaksi) VALUES ('" + kodeBarang + "', " + jumlah + ", 'Barang Keluar')";
                        stmt.executeUpdate(insertSql);
                    } else {
                        JOptionPane.showMessageDialog(null, "Kode Barang Tidak Tersedia");
                    }

                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Terjadi kesalahan saat melakukan penambahan stok barang", "Error", JOptionPane.ERROR_MESSAGE);
                }

                // Kosongkan bidang input setelah submit
                kodeBarangField.setText("");
                jumlahField.setText("");
            }
        });

        tambahStokButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String kodeBarang = kodeBarangField.getText();
                int jumlah = Integer.parseInt(jumlahField.getText());

                // Menggunakan class conn untuk membuat koneksi ke database
                try {
                    Connection conn = Conn.getConnection(); // Ganti Conn dengan nama class koneksi Anda
                    Statement stmt = conn.createStatement();

                    // Mengecek apakah barang dengan kode tersebut sudah ada di database
                    String checkSql = "SELECT * FROM barang WHERE KodeBarang = '" + kodeBarang + "'";
                    ResultSet resultSet = stmt.executeQuery(checkSql);

                    if (resultSet.next()) {
                        // Membuat query SQL untuk update stok
                        String updateSql = "UPDATE barang SET JumlahStok = JumlahStok + " + jumlah + " WHERE kodeBarang = '" + kodeBarang + "'";
                        stmt.executeUpdate(updateSql);

                        // Catat transaksi ke logArea
                        String log = "Tambah Stok: Kode Barang: " + kodeBarang + ", Jumlah: " + jumlah;
                        logArea.append(log + "\n");
                        String insertSql = "INSERT INTO transaksi (kodeBarang, jumlah, jenis_transaksi) VALUES ('" + kodeBarang + "', " + jumlah + ", 'Barang Masuk')";
                        stmt.executeUpdate(insertSql);
                    } else {
                        JOptionPane.showMessageDialog(null, "Kode Barang Tidak Tersedia");
                    }

                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Terjadi kesalahan saat melakukan penambahan stok barang", "Error", JOptionPane.ERROR_MESSAGE);
                }

                // Kosongkan bidang input setelah submit
                kodeBarangField.setText("");
                jumlahField.setText("");
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TransaksiForm form = new TransaksiForm();
            form.setVisible(true);
        });
    }
}