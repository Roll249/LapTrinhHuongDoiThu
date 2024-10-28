import java.io.*;
import java.util.*;
import java.time.LocalDate;

import static java.time.LocalDate.parse;

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

    public static List<MuonTra> loadMuonTraFromCSV(String filePath) {
        List<MuonTra> muonTraList = new ArrayList<>();
        List<Sach> sachList = loadSachFromCSV("sach.csv");
        List<DocGia> docGiaList = loadDocGiaFromCSV("docgia.csv");
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length == 7) {
                    String maSach = fields[0];
                    String maDocGia = fields[1];
                    LocalDate ngayMuon = parse(fields[2]);
                    LocalDate ngayDuKienTra = parse(fields[3]);
                    int soLuong = Integer.parseInt(fields[4]);
                    LocalDate ngayTra = parse(fields[5]);
                    double phiPhat = Double.parseDouble(fields[6]);

                    Sach sach = findSachByMa(maSach, sachList);
                    DocGia docGia = findDocGiaByMa(maDocGia, docGiaList);
                    if (sach != null && docGia != null) {
                        MuonTra muonTra = new MuonTra(sach, docGia, ngayMuon, ngayDuKienTra, soLuong, ngayTra, phiPhat);
                        muonTraList.add(muonTra);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();

        }
        return muonTraList;
    }

    public static void saveMuonTraToCSV(String fileName, List<MuonTra> muonTraList) {
        try (FileWriter writer = new FileWriter(fileName)) {
            for (MuonTra muonTra : muonTraList) {
                writer.write(muonTra.toCSV() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static DocGia timDocGiaTheoMa(String maDocGia, List<DocGia> danhSachDocGia) {
        for (DocGia docGia : danhSachDocGia) {
            if (docGia.getMaDocGia().equals(maDocGia)) {
                return docGia;
            }
        }
        return null;  // Trả về null nếu không tìm thấy
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
