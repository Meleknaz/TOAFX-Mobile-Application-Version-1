# TOAF Nedir?

[TOAF](https://www.toafx.org/) ekibi farklı üniversitelerden ve farklı disiplinlerden takım arkadaşlarının, ülkedeki tarım verimliliği ve otomasyon ihracatını artırma hedefiyle kurulmuş bir arge takımıdır. TEKNOFEST’te 1468 başvuru arasında finale kalarak geliştirilmesini yaptığımız araçla ilk 10 takım arasına girdik. Uzun süren çalışmalarımız sonucunda elde ettiğimiz takım çalışması deneyimiyle çalışmalarımıza devam ediyoruz.

## TOAFX Mobil Uygulaması


![](Images/1.jpeg)
Aracımız üzerinde bulunan sensörler (pH, ec vs.) ve açık kaynak kodlu uydulardan (NDVI, EVI) aldığımız verileri, kendi geliştirdiğimiz mobil uygulamaya kullanıcı dostu ara yüzümüzle çiftçiye aktarıp, yeri geldiğinde çiftçiyi uyarıp en verimli tarımı yapmasını hedefledik. Olağanüstü bir hava durumuyla (don, fırtına vs.) karşılaşılması durumunda çiftçiye alınması gereken önlemlerle ilgili geri bildirimde bulunmayı hedefliyoruz. Aynı zamanda aracımızı manuel olarak kontrol etmemizin gerektiği durumlarda mobil uygulamamız aracılığıyla aracımıza bağlanarak aracımızı kontrol edebiliyoruz. Tarım için önemli olan NDVI, EVI ve toprak sıcaklığı gibi parametreleri; uydu API’ları aracılığıyla mobil uygulamamıza entegre edip çiftçinin arazisi hakkındaki bu değerlere kolayca ulaşmasını sağladık. Ayrıca bu verilerin geçmiş verilerle de karşılaştırılması için belirli tarihler arasında grafikleştirmeler sunabiliyoruz. Sunduğumuz bu verilerin ışığında ileriye dönük yapılacak tarımsal faaliyetlerin daha verimli yapılmasına katkıda bulunmayı amaçlıyoruz.

**Uygulamamızdaki mevcut arayüzler**:

* **Ana Sayfa**: Ana sayfa kullanıcımızın konum bilgisine göre hava durumu bilgilerini içeriyor.(nem, hissedilen sıcaklık, basınç, rüzgar değerleri)
* **Hava Durumu Uyarısı Sayfası**: Olağanüstü bir hava durumuyla (don, fırtına vs.) uyarı verilmesi için geliştirilmek üzere tasarlandı
* **Uydu Görüntüsü Sayfası**: Point sınıfı için CPP uygulama dosyası
* **Arazinin Durumu Sayfası**: Square sınıfı için başlık dosyası
* **Cihaz Bağlantısı Sayfası**: Square sınıfı için CPP uygulama dosyası



![](Images/Ekran görüntüsü 2020-12-11 205112.png)

```java

```



**java**




