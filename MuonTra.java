import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class MuonTra {
    private Sach sach;
    private DocGia docGia;
    private LocalDate ngayMuon;
    private LocalDate ngayDuKienTra;
    private LocalDate ngayTra;
    private double phiPhat;
    private boolean daTra;
    int soLuong;

    public MuonTra(Sach sach, DocGia docGia, LocalDate ngayMuon, LocalDate ngayDuKienTra,int soLuong,double phiPhat) {
        this.sach = sach;
        this.docGia = docGia;
        this.ngayMuon = ngayMuon;
        this.ngayDuKienTra = ngayDuKienTra;
        this.daTra = false; // mặc định chưa trả
        this.soLuong = soLuong;
        this.phiPhat = phiPhat;
    }

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

    public double getPhiPhat() {
        return phiPhat;
    }

    public boolean isDaTra() {
        return daTra;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public void setNgayTra(LocalDate ngayTra) {
        this.ngayTra = ngayTra;
        this.daTra = true;
        if (ngayTra.isAfter(ngayDuKienTra)) {
            long daysLate = ChronoUnit.DAYS.between(ngayDuKienTra, ngayTra);
            this.phiPhat = daysLate * 5000; // tính phí phạt
        } else {
            this.phiPhat = 0;
        }
    }

    public void setPhiPhat(double phiPhat) {
        this.phiPhat = phiPhat;
    }
}
