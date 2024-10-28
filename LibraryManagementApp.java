import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.List;
import java.io.FileWriter;


public class LibraryManagementApp extends JFrame {
    private List<Sach> danhSachSach = new ArrayList<>();
    private List<DocGia> danhSachDocGia = new ArrayList<>();
    private JTextField maSachMuonField, maDocGiaMuonField;
    private JButton muonSachButton, traSachButton;
    private JTable sachTable;
    private JScrollPane scrollPane;
    private List<MuonTra> danhSachMuonTra = new ArrayList<>();

    public LibraryManagementApp() {
        setTitle("Quản lý Thư viện");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Tải dữ liệu sách và độc giả từ file CSV
        danhSachSach = DataLoader.loadSachFromCSV("sach.csv");
        danhSachDocGia = DataLoader.loadDocGiaFromCSV("docgia.csv");

        // Gọi phương thức hiển thị dữ liệu sách
        hienThiDanhSachSach();

        // Tạo giao diện nhập liệu
        createBorrowReturnInterface();

        // Thêm các thành phần vào JFrame
        add(scrollPane, BorderLayout.CENTER);
    }

    // Phương thức để tạo giao diện mượn/trả sách
    private void createBorrowReturnInterface() {
        JPanel muonTraPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        muonTraPanel.add(new JLabel("Mã Sách:"));
        maSachMuonField = new JTextField(15);
        muonTraPanel.add(maSachMuonField);

        muonTraPanel.add(new JLabel("Mã Độc Giả:"));
        maDocGiaMuonField = new JTextField(15);
        muonTraPanel.add(maDocGiaMuonField);

        muonSachButton = new JButton("Mượn sách");
        traSachButton = new JButton("Trả sách");

        muonSachButton.addActionListener(e -> muonSach());
        traSachButton.addActionListener(e -> traSach());

        muonTraPanel.add(muonSachButton);
        muonTraPanel.add(traSachButton);

        add(muonTraPanel, BorderLayout.SOUTH);
    }
    
    //Hien thi bao cao
    private void hienThiBaoCao() {
        int tongSoSach = danhSachSach.size();
        long soSachDangMuon = danhSachMuonTra.stream().filter(muonTra -> muonTra.getPhiPhat() > 0).count();
        double tongPhiPhat = danhSachMuonTra.stream().mapToDouble(MuonTra::getPhiPhat).sum();

        JOptionPane.showMessageDialog(this,
                "Tổng số sách: " + tongSoSach +
                "\nSố sách đang mượn: " + soSachDangMuon +
                "\nTổng phí phạt: " + tongPhiPhat + " VND");
    }

    // Phương thức hiển thị danh sách sách bằng JTable
    private void hienThiDanhSachSach() {
        String[] columnNames = { "Mã sách", "Tên sách", "Tác giả", "Thể loại", "Số lượng" };
        Object[][] data = new Object[danhSachSach.size()][5];

        for (int i = 0; i < danhSachSach.size(); i++) {
            Sach sach = danhSachSach.get(i);
            data[i][0] = sach.getMaSach();
            data[i][1] = sach.getTenSach();
            data[i][2] = sach.getTacGia();
            data[i][3] = sach.getTheLoai();
            data[i][4] = sach.getSoLuong();
        }

        sachTable = new JTable(data, columnNames);
        scrollPane = new JScrollPane(sachTable);
    }

    // Phương thức xử lý mượn sách
    private void muonSach() {
        String maSach = maSachMuonField.getText();
        String maDocGia = maDocGiaMuonField.getText();
        Sach sach = timSachTheoMa(maSach);
        DocGia docGia = timDocGiaTheoMa(maDocGia);

        if (sach != null && docGia != null && sach.getSoLuong() > 0) {
            sach.setSoLuong(sach.getSoLuong() - 1);
            JOptionPane.showMessageDialog(this, "Mượn sách thành công!");
            hienThiDanhSachSach();  // Cập nhật lại bảng để hiển thị số lượng
        } else {
            JOptionPane.showMessageDialog(this, "Không thể mượn sách!");
        }
    }

    private void traSach() {
        String maSach = maSachMuonField.getText();
        Sach sach = timSachTheoMa(maSach);

        if (sach != null) {
            sach.setSoLuong(sach.getSoLuong() + 1);
            JOptionPane.showMessageDialog(this, "Trả sách thành công!");
            hienThiDanhSachSach();  // Cập nhật lại bảng để hiển thị số lượng
        } else {
            JOptionPane.showMessageDialog(this, "Không tìm thấy thông tin mượn sách!");
        }
    }

    // Các phương thức phụ trợ tìm kiếm sách và độc giả theo mã
    private Sach timSachTheoMa(String maSach) {
        for (Sach sach : danhSachSach) {
            if (sach.getMaSach().equals(maSach)) {
                return sach;
            }
        }
        return null;
    }

    private DocGia timDocGiaTheoMa(String maDocGia) {
        for (DocGia docGia : danhSachDocGia) {
            if (docGia.getMaDocGia().equals(maDocGia)) {
                return docGia;
            }
        }
        return null;
    }
    private void themSach() {
        String maSach = JOptionPane.showInputDialog("Nhập mã sách:");
        String tenSach = JOptionPane.showInputDialog("Nhập tên sách:");
        String tacGia = JOptionPane.showInputDialog("Nhập tác giả:");
        String theLoai = JOptionPane.showInputDialog("Nhập thể loại:");
        int soLuong = Integer.parseInt(JOptionPane.showInputDialog("Nhập số lượng:"));
        danhSachSach.add(new Sach(maSach, tenSach, tacGia, theLoai, soLuong));
    }

    private void themDocGia() {
        String maDocGia = JOptionPane.showInputDialog("Nhập mã độc giả:");
        String tenDocGia = JOptionPane.showInputDialog("Nhập tên độc giả:");
        String diaChi = JOptionPane.showInputDialog("Nhập địa chỉ:");
        String soDienThoai = JOptionPane.showInputDialog("Nhập số điện thoại:");
        danhSachDocGia.add(new DocGia(maDocGia, tenDocGia, diaChi, soDienThoai));
    }
 

    private void luuBaoCaoRaFile() {
        try (FileWriter writer = new FileWriter("bao_cao_thu_vien.txt")) {
            int tongSoSach = danhSachSach.size();
            long soSachDangMuon = danhSachMuonTra.stream().filter(muonTra -> muonTra.getPhiPhat() > 0).count();
            double tongPhiPhat = danhSachMuonTra.stream().mapToDouble(MuonTra::getPhiPhat).sum();

            writer.write("Tổng số sách: " + tongSoSach + "\n");
            writer.write("Số sách đang mượn: " + soSachDangMuon + "\n");
            writer.write("Tổng phí phạt: " + tongPhiPhat + " VND\n");
            JOptionPane.showMessageDialog(this, "Lưu báo cáo thành công!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi lưu báo cáo!");
        }
    }


    public static void main(String[] args) {
        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }catch(Exception e){
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            LibraryManagementApp app = new LibraryManagementApp();
            app.setVisible(true);
        });

    }
}
