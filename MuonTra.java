import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class MuonTra {
    private Sach sach;
    private DocGia docGia;
    private LocalDate ngayMuon;
    private LocalDate ngayDuKienTra;
    private LocalDate ngayTra;
    private int soLuong;

    private double phiPhat;

    public void setSach(Sach sach) {
        this.sach = sach;
    }

    public void setDocGia(DocGia docGia) {
        this.docGia = docGia;
    }

    public void setNgayMuon(LocalDate ngayMuon) {
        this.ngayMuon = ngayMuon;
    }

    public void setNgayDuKienTra(LocalDate ngayDuKienTra) {
        this.ngayDuKienTra = ngayDuKienTra;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public MuonTra(Sach sach, DocGia docGia, LocalDate ngayMuon, LocalDate ngayDuKienTra, int soLuong, LocalDate ngayTra, double phiPhat) {
        this.sach = sach;
        this.docGia = docGia;
        this.ngayMuon = ngayMuon;
        this.ngayDuKienTra = ngayDuKienTra;
        this.soLuong = soLuong;
        this.ngayTra = ngayTra;
        this.phiPhat = phiPhat;
    }

    // Constructor
    public MuonTra(Sach sach, DocGia docGia, LocalDate ngayMuon, LocalDate ngayDuKienTra) {
        this.sach = sach;
        this.docGia = docGia;
        this.ngayMuon = ngayMuon;
        this.ngayDuKienTra = ngayDuKienTra;
        this.ngayTra = null;
        this.phiPhat = 0.0;
    }

    // Getters and Setters
    public Sach getSach() {
        return sach;
    }

    public DocGia getDocGia() {
        return docGia;
    }

    public LocalDate getNgayMuon() {
        return ngayMuon;
    }

    public LocalDate getNgayDuKienTra() {
        return ngayDuKienTra;
    }

    public LocalDate getNgayTra() {
        return ngayTra;
    }

    public void setNgayTra(LocalDate ngayTra) {
        this.ngayTra = ngayTra;
        tinhPhiPhat();  // Cập nhật phí phạt khi sách được trả
    }

    public double getPhiPhat() {
        return phiPhat;
    }

    public void setPhiPhat(double phiPhat) {
        this.phiPhat = phiPhat;
    }

    public boolean isDaTra() {
        return ngayTra != null;
    }

    // Tính phí phạt khi quá hạn
    private void tinhPhiPhat() {
        if (ngayTra != null && ngayTra.isAfter(ngayDuKienTra)) {
            long daysLate = ChronoUnit.DAYS.between(ngayDuKienTra, ngayTra);
            phiPhat = daysLate * 5000;
        } else {
            phiPhat = 0.0;
        }
    }
    public void muonSach(DocGia docGia, LocalDate ngayMuon, LocalDate ngayDuKienTra) {
        this.docGia = docGia;
        this.ngayMuon = ngayMuon;
        this.ngayDuKienTra = ngayDuKienTra;
        this.ngayTra = null;  // Cập nhật trạng thái chưa trả
        tinhPhiPhat();
    }


    // Phương thức chuyển đổi sang CSV
    public String toCSV() {
        return sach.getMaSach() + "," +
                docGia.getMaDocGia() + "," +
                ngayMuon + "," +
                ngayDuKienTra + "," +
                (ngayTra != null ? ngayTra : "") + "," +
                phiPhat + "," +
                (isDaTra() ? "Đã trả" : "Chưa trả");
    }
}
