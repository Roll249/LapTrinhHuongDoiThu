import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LibraryManagementApp extends JFrame {
    private List<Sach> danhSachSach = new ArrayList<>();
    private List<DocGia> danhSachDocGia = new ArrayList<>();
    private List<MuonTra> danhSachMuonTra = new ArrayList<>();

    private JPanel mainPanel;
    private JTable sachTable;
    private JScrollPane scrollPane;

    public LibraryManagementApp() {
        setTitle("Quản lý Thư viện");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        mainPanel = new JPanel(new CardLayout());
        add(mainPanel, BorderLayout.CENTER);

        // Tải dữ liệu từ file CSV
        danhSachSach = DataLoader.loadSachFromCSV("sach.csv");
        danhSachDocGia = DataLoader.loadDocGiaFromCSV("docgia.csv");
        danhSachMuonTra = DataLoader.loadMuonTraFromCSV("muontra.csv", danhSachSach, danhSachDocGia);
        hienThiDanhSachSach();
        hienThiDanhSachDocGia();
        hienThiDanhSachMuonTra();
        muonTraPanel();

        createMenu();
    }
    
   
    private void muonTraPanel() {
        JPanel muonTraPanel = new JPanel(new GridLayout(2, 1));

        // Panel for borrowing books
        JPanel muonPanel = new JPanel(new GridLayout(5, 2));
        JTextField maDocGiaMuonField = new JTextField();
        JTextField maSachMuonField = new JTextField();
        JTextField soLuongMuonField = new JTextField();
        JTextField soNgayMuonField = new JTextField();

        muonPanel.add(new JLabel("      Mã độc giả:"));
        muonPanel.add(maDocGiaMuonField);
        muonPanel.add(new JLabel("      Mã sách:"));
        muonPanel.add(maSachMuonField);
        muonPanel.add(new JLabel("      Số lượng mượn:"));
        muonPanel.add(soLuongMuonField);
        muonPanel.add(new JLabel("      Số ngày mượn:"));
        muonPanel.add(soNgayMuonField);

        JButton muonButton = new JButton("Mượn");
        muonButton.addActionListener(e -> {
            String maDocGia = maDocGiaMuonField.getText();
            String maSach = maSachMuonField.getText();
            int soLuong = Integer.parseInt(soLuongMuonField.getText());
            int soNgay = Integer.parseInt(soNgayMuonField.getText());

            Sach sach = danhSachSach.stream().filter(s -> s.getMaSach().equals(maSach)).findFirst().orElse(null);
            DocGia docGia = danhSachDocGia.stream().filter(dg -> dg.getMaDocGia().equals(maDocGia)).findFirst().orElse(null);

            if (sach != null && docGia != null && sach.getSoLuong() >= soLuong) {
                double phiPhat =0;
                MuonTra muonTra = new MuonTra(sach, docGia, LocalDate.now(), LocalDate.now().plusDays(soNgay),soLuong,phiPhat);
                muonTra.setSoLuong(soLuong);
                danhSachMuonTra.add(muonTra);
                luuMuontraCSV();
                hienThiDanhSachMuonTra();
                sach.setSoLuong(sach.getSoLuong() - soLuong);
                luuSachCSV();
                hienThiDanhSachMuonTra();

            } else {
                JOptionPane.showMessageDialog(this, "Thông tin không hợp lệ hoặc sách không đủ số lượng!");
            }
        });

        muonPanel.add(new JLabel());
        muonPanel.add(muonButton);

        // Panel for returning books
        JPanel traPanel = new JPanel(new GridLayout(3, 2));
        JTextField maDocGiaTraField = new JTextField();
        JTextField maSachTraField = new JTextField();

        traPanel.add(new JLabel("      Mã độc giả:"));
        traPanel.add(maDocGiaTraField);
        traPanel.add(new JLabel("      Mã sách:"));
        traPanel.add(maSachTraField);

        JButton traButton = new JButton("Trả");
        traButton.addActionListener(e -> {
            String maDocGia = maDocGiaTraField.getText();
            String maSach = maSachTraField.getText();

            MuonTra muonTra = danhSachMuonTra.stream()
                    .filter(mt -> mt.getDocGia().getMaDocGia().equals(maDocGia) && mt.getSach().getMaSach().equals(maSach) && !mt.isDaTra())
                    .findFirst().orElse(null);

            if (muonTra != null) {
                muonTra.setNgayTra(LocalDate.now());
                muonTra.getSach().setSoLuong(muonTra.getSach().getSoLuong() + muonTra.getSoLuong());
                luuMuontraCSV();
                hienThiDanhSachMuonTra(); 
                luuSachCSV();
                hienThiDanhSachMuonTra();
            } else {
                JOptionPane.showMessageDialog(this, "Thông tin không hợp lệ hoặc sách chưa được mượn!");
            }
        });

        traPanel.add(new JLabel());
        traPanel.add(traButton);

        muonTraPanel.add(muonPanel);
        muonTraPanel.add(traPanel);

        mainPanel.add(muonTraPanel, "MuonVaTraSach");
    }

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
        mainPanel.add(scrollPane, "DanhSachSach");
    }

    private void hienThiDanhSachDocGia() {
        String[] columnNames = { "Mã độc giả", "Tên độc giả", "Số điện thoại", "Địa chỉ" };
        Object[][] data = new Object[danhSachDocGia.size()][5];

        for (int i = 0; i < danhSachDocGia.size(); i++) {
            DocGia docGia = danhSachDocGia.get(i);
            data[i][0] = docGia.getMaDocGia();
            data[i][1] = docGia.getTenDocGia();

            data[i][2] = docGia.getSoDienThoai();
            data[i][3] = docGia.getDiaChi();
        }

        JTable docGiaTable = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(docGiaTable);
        mainPanel.add(scrollPane, "DanhSachDocGia");
    }

    private void luuSachCSV() {
        try (FileWriter writer = new FileWriter("sach.csv")) {
            for (Sach sach : danhSachSach) {
                writer.write(sach.toCSV() + "\n");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi lưu dữ liệu sách!");
        }
    }

    private void luuDocGiaCSV() {
        try (FileWriter writer = new FileWriter("docgia.csv")) {
            for (DocGia docGia : danhSachDocGia) {
                writer.write(docGia.toCSV() + "\n");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi lưu dữ liệu độc giả!");
        }
    }
   
    private void luuMuontraCSV() {
        try (FileWriter writer = new FileWriter("muontra.csv")) {
            for (MuonTra muonTra : danhSachMuonTra) {
                writer.write(muonTra.toCSV() + "\n");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi lưu dữ liệu mượn trả!");
        }
    }
    private void createMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Tùy chọn");

        JMenuItem themSachItem = new JMenuItem("Thêm sách");
        themSachItem.addActionListener(e -> openAddBookPanel());

        JMenuItem themDocGiaItem = new JMenuItem("Thêm độc giả");
        themDocGiaItem.addActionListener(e -> openAddReaderPanel());

        JMenuItem hienThiSachItem = new JMenuItem("Hiển thị danh sách sách");
        hienThiSachItem.addActionListener(e -> switchToPanel("DanhSachSach"));

        JMenuItem hienThiDocGiaItem = new JMenuItem("Hiển thị danh sách độc giả");
        hienThiDocGiaItem.addActionListener(e -> switchToPanel("DanhSachDocGia"));

        JMenuItem muonVaTraSachItem = new JMenuItem("Mượn và Trả");
        muonVaTraSachItem.addActionListener(e -> switchToPanel("MuonVaTraSach"));

        JMenuItem hienThiMuonTraItem = new JMenuItem("Hiển thị danh sách mượn trả");
        hienThiMuonTraItem.addActionListener(e -> hienThiDanhSachMuonTra());


        JMenuItem baoCaoItem = new JMenuItem("Hiển thị báo cáo");
        baoCaoItem.addActionListener(e -> hienThiBaoCao());

        JMenuItem luuBaoCaoItem = new JMenuItem("Lưu báo cáo");
        luuBaoCaoItem.addActionListener(e -> luuBaoCaoRaFile());

        menu.add(themSachItem);
        menu.add(themDocGiaItem);
        menu.add(hienThiSachItem);
        menu.add(hienThiDocGiaItem);
        menu.add(baoCaoItem);
        menu.add(luuBaoCaoItem);
        menu.add(hienThiMuonTraItem);
        menu.add(muonVaTraSachItem);

        menuBar.add(menu);
        setJMenuBar(menuBar);
    }

    private void switchToPanel(String panelName) {
        CardLayout cl = (CardLayout) (mainPanel.getLayout());
        cl.show(mainPanel, panelName);
    }

    private void openAddReaderPanel() {
        JPanel addReaderPanel = new JPanel(new GridLayout(5, 2));
        JTextField maDocGiaField = new JTextField();
        JTextField tenDocGiaField = new JTextField();

        JTextField soDienThoaiField = new JTextField();
        JTextField diaChiField = new JTextField();

        addReaderPanel.add(new JLabel("Mã độc giả:"));
        addReaderPanel.add(maDocGiaField);
        addReaderPanel.add(new JLabel("Tên độc giả:"));
        addReaderPanel.add(tenDocGiaField);
        addReaderPanel.add(new JLabel("Số điện thoại:"));
        addReaderPanel.add(soDienThoaiField);
        addReaderPanel.add(new JLabel("Địa chỉ:"));
        addReaderPanel.add(diaChiField);

        int result = JOptionPane.showConfirmDialog(this, addReaderPanel, "Thêm độc giả mới", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            DocGia docGia = new DocGia(
                    maDocGiaField.getText(),
                    tenDocGiaField.getText(),
                    soDienThoaiField.getText(),
                    diaChiField.getText()
            );
            danhSachDocGia.add(docGia);
            luuDocGiaCSV();
            hienThiDanhSachDocGia();
        }
    }

    private void openAddBookPanel() {
        JPanel addBookPanel = new JPanel(new GridLayout(5, 2));
        JTextField maSachField = new JTextField();
        JTextField tenSachField = new JTextField();
        JTextField tacGiaField = new JTextField();
        JTextField theLoaiField = new JTextField();
        JTextField soLuongField = new JTextField();

        addBookPanel.add(new JLabel("Mã sách:"));
        addBookPanel.add(maSachField);
        addBookPanel.add(new JLabel("Tên sách:"));
        addBookPanel.add(tenSachField);
        addBookPanel.add(new JLabel("Tác giả:"));
        addBookPanel.add(tacGiaField);
        addBookPanel.add(new JLabel("Thể loại:"));
        addBookPanel.add(theLoaiField);
        addBookPanel.add(new JLabel("Số lượng:"));
        addBookPanel.add(soLuongField);

        int result = JOptionPane.showConfirmDialog(this, addBookPanel, "Thêm sách mới", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            Sach sach = new Sach(
                    maSachField.getText(),
                    tenSachField.getText(),
                    tacGiaField.getText(),
                    theLoaiField.getText(),
                    Integer.parseInt(soLuongField.getText())
            );
            danhSachSach.add(sach);
            luuSachCSV();  // Lưu vào CSV ngay lập tức
            hienThiDanhSachSach();
        }
    }
    //Hiển thị danh sách mượn trả
    private void hienThiDanhSachMuonTra() {
        String[] columnNames = { "Mã sách", "Mã độc giả", "Ngày mượn", "Ngày dự kiến trả", "Ngày trả", "Phí phạt", "Trạng thái" };
        Object[][] data = new Object[danhSachMuonTra.size()][7];

        for (int i = 0; i < danhSachMuonTra.size(); i++) {
            MuonTra muonTra = danhSachMuonTra.get(i);
            data[i][0] = muonTra.getSach().getMaSach();
            data[i][1] = muonTra.getDocGia().getMaDocGia();
            data[i][2] = muonTra.getNgayMuon();
            data[i][3] = muonTra.getNgayDuKienTra();
            data[i][4] = muonTra.getNgayTra() != null ? muonTra.getNgayTra().toString() : "Chưa trả";
            data[i][5] = muonTra.getPhiPhat();
            data[i][6] = muonTra.isDaTra() ? "Đã trả" : "Chưa trả";
        }

        JTable muonTraTable = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(muonTraTable);
        mainPanel.add(scrollPane, "DanhSachMuonTra");
        switchToPanel("DanhSachMuonTra");
    }


    private void hienThiBaoCao() {
        int tongSoSach = danhSachSach.size();
        long soSachDangMuon = danhSachMuonTra.stream().filter(muonTra -> muonTra.getNgayTra() == null).count();
        double tongPhiPhat = danhSachMuonTra.stream().mapToDouble(MuonTra::getPhiPhat).sum();

        JOptionPane.showMessageDialog(this,
                "Tổng số sách: " + tongSoSach +
                        "\nSố sách đang mượn: " + soSachDangMuon +
                        "\nTổng phí phạt: " + tongPhiPhat + " VND");
    }

    private void luuBaoCaoRaFile() {
        try (FileWriter writer = new FileWriter("bao_cao_thu_vien.txt")) {
            int tongSoSach = danhSachSach.size();
            long soSachDangMuon = danhSachMuonTra.stream().filter(muonTra -> muonTra.getNgayTra() == null).count();
            double tongPhiPhat = danhSachMuonTra.stream().mapToDouble(MuonTra::getPhiPhat).sum();

            writer.write("Tổng số sách: " + tongSoSach + "\n");
            writer.write("Số sách đang mượn: " + soSachDangMuon + "\n");
            writer.write("Tổng phí phạt: " + tongPhiPhat + " VND\n");
            JOptionPane.showMessageDialog(this, "Lưu báo cáo thành công!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi lưu báo cáo!");
        }
    }

    public static void main(String[] args) throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        SwingUtilities.invokeLater(() -> new LibraryManagementApp().setVisible(true));

    }
} 