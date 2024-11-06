import java.io.*;
import java.util.*;
import java.time.LocalDate;

public class DataLoader {
    // Đọc danh sách sách từ file CSV
    public static List<Sach> loadSachFromCSV(String fileName) {
        List<Sach> sachList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length == 5) {
                    String maSach = fields[0];
                    String tenSach = fields[1];
                    String tacGia = fields[2];
                    String theLoai = fields[3];
                    int soLuong = Integer.parseInt(fields[4]);
                    sachList.add(new Sach(maSach, tenSach, tacGia, theLoai, soLuong));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sachList;
    }

    // Đọc danh sách độc giả từ file CSV
    public static List<DocGia> loadDocGiaFromCSV(String fileName) {
        List<DocGia> docGiaList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length == 4) {
                    String maDocGia = fields[0];
                    String tenDocGia = fields[1];
                    String diaChi = fields[2];
                    String soDienThoai = fields[3];
                    docGiaList.add(new DocGia(maDocGia, tenDocGia, diaChi, soDienThoai));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return docGiaList;
    }

    public static List<MuonTra> loadMuonTraFromCSV(String filePath, List<Sach> danhSachSach, List<DocGia> danhSachDocGia) {
        List<MuonTra> danhSachMuonTra = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");

                // Đọc các thuộc tính từ CSV
                String maSach = values[0];
                String maDocGia = values[1];
                LocalDate ngayMuon = LocalDate.parse(values[2]);
                LocalDate ngayDuKienTra = LocalDate.parse(values[3]);
                LocalDate ngayTra = (values[4].equals("null")) ? null : LocalDate.parse(values[4]);
                int soLuong = Integer.parseInt(values[5]);
                double phiPhat = Double.parseDouble(values[6]);


                // Lấy đối tượng Sach và DocGia từ danh sách đã tải
                Sach sach = findSachByMa(maSach, danhSachSach);
                DocGia docGia = findDocGiaByMa(maDocGia, danhSachDocGia);

                // Tạo đối tượng MuonTra và cập nhật thông tin trả
                MuonTra muonTra = new MuonTra(sach, docGia, ngayMuon, ngayDuKienTra,soLuong,phiPhat);
                muonTra.setNgayTra(ngayTra);
                danhSachMuonTra.add(muonTra);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return danhSachMuonTra;
    }

    // Tìm kiếm sách theo mã
    private static Sach findSachByMa(String maSach, List<Sach> danhSachSach) {
        return danhSachSach.stream().filter(s -> s.getMaSach().equals(maSach)).findFirst().orElse(null);
    }

    // Tìm kiếm độc giả theo mã
    private static DocGia findDocGiaByMa(String maDocGia, List<DocGia> danhSachDocGia) {
        return danhSachDocGia.stream().filter(d -> d.getMaDocGia().equals(maDocGia)).findFirst().orElse(null);
    }

}
