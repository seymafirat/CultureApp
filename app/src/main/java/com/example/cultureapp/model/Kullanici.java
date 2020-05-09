package com.example.cultureapp.model;

public class Kullanici {
    private String id;
    private String kullaniciAdi;
    private String AdSoyad;
    private String resimurl;
    private String Bio;

    public Kullanici() {
    }

    public Kullanici(String id, String kullaniciAdi, String AdSoyad, String resimurl, String Bio) {
        this.id = id;
        this.kullaniciAdi = kullaniciAdi;
        this.AdSoyad = AdSoyad;
        this.resimurl = resimurl;
        this.Bio = Bio;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKullaniciAdi() {
        return kullaniciAdi;
    }

    public void setKullaniciAdi(String kullaniciAdi) {
        this.kullaniciAdi = kullaniciAdi;
    }

    public String getAdSoyad() {
        return AdSoyad;
    }

    public void setAdSoyad(String adSoyad) {
        AdSoyad = adSoyad;
    }

    public String getResimurl() {
        return resimurl;
    }

    public void setResimurl(String resimurl) {
        this.resimurl = resimurl;
    }

    public String getBio() {
        return Bio;
    }

    public void setBio(String bio) {
        Bio = bio;
    }

    @Override
    public String toString() {
        return "Kullanici{" +
                "id='" + id + '\'' +
                ", kullaniciadi='" + kullaniciAdi + '\'' +
                ", ad='" + AdSoyad + '\'' +
                ", resimurl='" + resimurl + '\'' +
                ", bio='" + Bio + '\'' +
                '}';
    }
}
