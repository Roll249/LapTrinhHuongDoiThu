import java.io.*;
import java.util.*;

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
}
