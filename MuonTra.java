import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class MuonTra {
    private Sach sach;
    private DocGia docGia;
    private LocalDate ngayMuon;
    private LocalDate ngayTra;
    private double phiPhatNgay = 10000; // phí phạt 10,000 VND/ngày
    private double phiPhat;

    public MuonTra(Sach sach, DocGia docGia, LocalDate ngayMuon, LocalDate ngayTra) {
        this.sach = sach;
        this.docGia = docGia;
        this.ngayMuon = ngayMuon;
        this.ngayTra = ngayTra;
        this.phiPhat = tinhPhiPhat();
    }

    private double tinhPhiPhat() {
        if (LocalDate.now().isAfter(ngayTra)) {
            long soNgayQuaHan = ChronoUnit.DAYS.between(ngayTra, LocalDate.now());
            return soNgayQuaHan * phiPhatNgay;
        }
        return 0;
    }

    // Getters và Setters cho các thuộc tính

    public Sach getSach() {
        return sach;
    }

    public void setSach(Sach sach) {
        this.sach = sach;
    }

    public DocGia getDocGia() {
        return docGia;
    }

    public void setDocGia(DocGia docGia) {
        this.docGia = docGia;
    }

    public LocalDate getNgayMuon() {
        return ngayMuon;
    }

    public void setNgayMuon(LocalDate ngayMuon) {
        this.ngayMuon = ngayMuon;
    }

    public LocalDate getNgayTra() {
        return ngayTra;
    }

    public void setNgayTra(LocalDate ngayTra) {
        this.ngayTra = ngayTra;
    }

    public double getPhiPhatNgay() {
        return phiPhatNgay;
    }

    public void setPhiPhatNgay(double phiPhatNgay) {
        this.phiPhatNgay = phiPhatNgay;
    }

    public double getPhiPhat() {
        return phiPhat;
    }

    public void setPhiPhat(double phiPhat) {
        this.phiPhat = phiPhat;
    }
}
