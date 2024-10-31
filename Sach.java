public class Sach {
    private String maSach;
    private String tenSach;
    private String tacGia;
    private String theLoai;
    private int soLuong;

    public Sach(String maSach, String tenSach, String tacGia, String theLoai, int soLuong) {
        this.maSach = maSach;
        this.tenSach = tenSach;
        this.tacGia = tacGia;
        this.theLoai = theLoai;
        this.soLuong = soLuong;
    }

    // Getters và Setters cho các thuộc tính

    public String getMaSach() {
        return maSach;
    }

    public void setMaSach(String maSach) {
        this.maSach = maSach;
    }

    public String getTenSach() {
        return tenSach;
    }


    public void setTenSach(String tenSach) {
        this.tenSach = tenSach;
    }

    public String getTacGia() {
        return tacGia;
    }

    public void setTacGia(String tacGia) {
        this.tacGia = tacGia;
    }

    public String getTheLoai() {
        return theLoai;
    }

    public void setTheLoai(String theLoai) {
        this.theLoai = theLoai;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }
    public String toCSV() {
        return maSach + "," + tenSach + "," + tacGia + "," + theLoai + "," + soLuong;
    }
}