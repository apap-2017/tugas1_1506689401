package com.example.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.model.KecamatanModel;
import com.example.model.KeluargaModel;
import com.example.model.KelurahanModel;
import com.example.model.KotaModel;
import com.example.model.PendudukModel;
import com.example.service.KecamatanService;
import com.example.service.KeluargaService;
import com.example.service.KelurahanService;
import com.example.service.KotaService;
import com.example.service.PendudukService;

@Controller
public class PendudukController {
	
	@Autowired
	PendudukService pendudukDAO;
	
	@Autowired
	KeluargaService keluargaDAO;
	
	@Autowired
	KelurahanService kelurahanDAO;
	
	@Autowired
	KecamatanService kecamatanDAO;
	
	@Autowired
	KotaService kotaDAO;
	
    @RequestMapping("/")
    public String homepage()
    {
        return "homepage";
    }
	
	@RequestMapping("/penduduk")
    public String tampilNIKPenduduk (Model model,
            @RequestParam(value = "nik", required=false) String nik)
    {
        PendudukModel penduduk = pendudukDAO.selectPenduduk(nik);
		
        if (penduduk != null) {
            model.addAttribute ("penduduk", penduduk);
            
            //mencoba merubah output dari tanggal lahir
            String tanggalLahir = penduduk.getTanggal_lahir().toString();	
            model.addAttribute("tanggal_lahir", tanggalLahir);
            
            //Buat object dari KeluargaModel agar bisa ditarik di view
            	//Karena mau ambil satu 'keluarga', dicek apakah sesuai dengan id_keluarga yang ada di penduduk
            		//Begitu juga dengan method2 lainnya dibawah ini
            KeluargaModel keluargaMdl = keluargaDAO.selectKeluarga(penduduk.getId_keluarga());
            model.addAttribute("keluarga", keluargaMdl);
            
            //Buat object dari KelurahanModel agar bisa ditarik di view
            KelurahanModel kelurahanMdl = kelurahanDAO.selectKelurahan(keluargaMdl.getId_kelurahan());
            model.addAttribute("kelurahan", kelurahanMdl);
            
            //Buat object dari KelurahanModel agar bisa ditarik di view
            KecamatanModel kecamatanMdl = kecamatanDAO.selectKecamatan(kelurahanMdl.getId_kecamatan());
            model.addAttribute("kecamatan", kecamatanMdl);
            
            //Buat object dari KelurahanModel agar bisa ditarik di view
            KotaModel kotaMdl = kotaDAO.selectKota(kecamatanMdl.getId_kota());
            model.addAttribute("kota", kotaMdl);
            
            return "viewPenduduk";
        }
        else {
            model.addAttribute("notifikasi", "Penduduk not found");
        	model.addAttribute ("detail", "NIK = " + nik + " tidak ditemukan");
            return "not-found";
        }
    }
	
	@RequestMapping(value="/penduduk/tambah", method=RequestMethod.GET)
	public String formTambahPenduduk() {
		return "form-tambahPenduduk";
	}
	
	@RequestMapping(value="/penduduk/tambah", method=RequestMethod.POST)
	public String tambahPenduduk (Model model,
            @RequestParam(value = "nama", required=false) String nama,
            @RequestParam(value = "tempat_lahir", required=false) String tempat_lahir,
            @RequestParam(value = "tanggal_lahir", required=false) String tanggal_lahir,
            @RequestParam(value = "golongan_darah", required=false) String golongan_darah,
            @RequestParam(value = "agama", required=false) String agama,
            @RequestParam(value = "status_perkawinan", required=false) String status_perkawinan,
            @RequestParam(value = "pekerjaan", required=false) String pekerjaan,
            @RequestParam(value = "is_wni", required=false) Integer is_wni,
            @RequestParam(value = "is_wafat", required=false) Integer is_wafat,
            @RequestParam(value = "id_keluarga", required=false) String id_keluarga,
            @RequestParam(value = "jenis_kelamin", required=false) String jenis_kelamin,
            @RequestParam(value = "status_dalam_keluarga", required=false) String status_dalam_keluarga
			) 
	{
		KeluargaModel keluargaMdl = keluargaDAO.selectKeluarga(id_keluarga);
		//model.addAttribute("keluarga", keluargaMdl);
		KelurahanModel kelurahanMdl = kelurahanDAO.selectKelurahan(keluargaMdl.getId_kelurahan());
		//model.addAttribute("kelurahan", kelurahanMdl);
		KecamatanModel kecamatanMdl = kecamatanDAO.selectKecamatan(kelurahanMdl.getId_kecamatan());
        //model.addAttribute("kecamatan", kecamatanMdl);
		
		//masukkan kode kecamatan ke string agar bisa di substring
		String kodeKecamatan = kecamatanMdl.getKode_kecamatan();
		
		//ambil 6 kode awal dari kode kecamatan, yg 0 terakhir gausah, karena akhirnya pasti 0
		kodeKecamatan = kodeKecamatan.substring(0, 6);	
		
		//ambil tanggal lahir
		String tglLahir = tanggal_lahir;
		//System.out.println(tglLahir);	//cek di console keluarannya gimana : tahun-bulan-tanggal
		
		//pisah tanggalnya biar bisa dipisahin
		String[] pisahTanggal = tglLahir.split("-");
		
		//cek jenis kelamin, jika jenis kelaminnya wanita maka tanggal lahirnya ditambah 40, sedangkan pria tidak diubah
			//lakukan parsing karena tipe data awalnya string
				//parse dari string ke integer lalu parse lagi ke string
		if(jenis_kelamin.equals("1")) {
			pisahTanggal[2] = (Integer.parseInt(pisahTanggal[2] ) + 40 + ""); 		
		}
		
		//Buat nik yang sesuai dengan ketentuan di wikipedia (6 digit kode kecamatan + 2 digit tgl lahir + 2 digit bulan lahir + 2 digit tahun lahir + 4 digit kesesuaian dengan NIK lain)
		String nik = kodeKecamatan;
		nik = nik + pisahTanggal[2] + pisahTanggal[1] + pisahTanggal[0].substring(2);
		
		//panggil method yang sudah dibuat untuk mengecek kesamaan NIK
		int cekKesamaanNIK = pendudukDAO.cekNIKYangSama(nik+"%");
		
		//By Default, NIK akan diseet akhirnya 0001, jika ada yg sama baru ditambahin 1
		cekKesamaanNIK = cekKesamaanNIK + 1;
		String kesamaanNIK = cekKesamaanNIK + "";
		
		//Lakukan looping untuk mengecek seberapa banyak nik yang sama
		for(int i=4; i>kesamaanNIK.length(); i+=0) {
			kesamaanNIK = "0" + kesamaanNIK;
		}
		nik = nik + kesamaanNIK;
		
		//Versi lain dari looping diatas
		/* int i=4;
		while(i>kesamaanNIK.length()) {
			kesamaanNIK="0" + kesamaanNIK;
		}*/
		
		int finalId = pendudukDAO.hitungId() + 1;
		
		PendudukModel penduduk = new PendudukModel(finalId,nik,nama,tempat_lahir,tanggal_lahir,jenis_kelamin,is_wni,id_keluarga,agama, pekerjaan, status_perkawinan, status_dalam_keluarga, golongan_darah, is_wafat);
		pendudukDAO.tambahPenduduk(penduduk);
		model.addAttribute("penduduk", penduduk);
		
		return "suksesTambahPenduduk";
	}
	
	@RequestMapping(value = "/penduduk/ubah/{nik}", method=RequestMethod.GET)
    public String formUbahPenduduk(Model model, @PathVariable(value = "nik") String nik) {
        PendudukModel penduduk = pendudukDAO.selectPenduduk(nik);
        
        if (penduduk != null) {
        	model.addAttribute("penduduk", penduduk);
            return "form-ubahPenduduk";
        } 
        else {
            model.addAttribute ("nik", nik);
            model.addAttribute("notifikasiPenduduk", "Penduduk not found");
        	model.addAttribute ("detailPenduduk", "Penduduk dengan NIK " + nik + " tidak ditemukan");
            
            return "not-found";
        }
    }
	
	@RequestMapping(value="/penduduk/ubah", method = RequestMethod.POST)
    public String ubahPenduduk (Model model,
    		@RequestParam ( value = "nik" , required = false ) String nik, @Valid @ModelAttribute PendudukModel updatedPenduduk) 
    {		
		//Buat dua buah penduduk, satu utk ambil yang lama, satu yg baru untuk diupdate
		PendudukModel oldPenduduk = pendudukDAO.selectPenduduk(nik);
		
		//Cek jika atribut yang dapat membuat nik diganti (id keluarga, tgl lahir, jenis kelamin) ada yang berbeda, maka buat nik dari baru lagi seperti fungsi tambah penduduk diatas
		if(oldPenduduk.getId_keluarga() != updatedPenduduk.getId_keluarga() || oldPenduduk.getTanggal_lahir() != updatedPenduduk.getTanggal_lahir() || 
				oldPenduduk.getJenis_kelamin() != updatedPenduduk.getJenis_kelamin()) {
			
			/* 
			 * Karena penduduk ingin diupdate datanya, lakukan proses pembuatan nik penduduk dari awal lagi		 
			 */
				KeluargaModel keluargaMdl = keluargaDAO.selectKeluarga(updatedPenduduk.getId_keluarga());
				KelurahanModel kelurahanMdl = kelurahanDAO.selectKelurahan(keluargaMdl.getId_kelurahan());
				KecamatanModel kecamatanMdl = kecamatanDAO.selectKecamatan(kelurahanMdl.getId_kecamatan());
				
				//masukkan kode kecamatan ke string agar bisa di substring
				String kodeKecamatan = kecamatanMdl.getKode_kecamatan();
				
				//ambil 6 kode awal dari kode kecamatan, yg 0 terakhir gausah, karena akhirnya pasti 0
				kodeKecamatan = kodeKecamatan.substring(0, 6);
				System.out.println(kodeKecamatan);
				
				//ambil tanggal lahir
				String tglLahir = updatedPenduduk.getTanggal_lahir();
				
				//pisah tanggalnya biar bisa dipisahin
				String pisahTanggal[] = tglLahir.split("-");
				
				//cek jenis kelamin, jika jenis kelaminnya wanita maka tanggal lahirnya ditambah 40, sedangkan pria tidak diubah
					//lakukan parsing karena tipe data awalnya string
						//parse dari string ke integer lalu parse lagi ke string
				if(updatedPenduduk.getJenis_kelamin().equals("1")) {
					pisahTanggal[2] = (Integer.parseInt(pisahTanggal[2]) + 40 + "");
				}
				
				//Buat nik yang sesuai dengan ketentuan di wikipedia (6 digit kode kecamatan + 2 digit tgl lahir + 2 digit bulan lahir + 2 digit tahun lahir + 4 digit kesesuaian dengan NIK lain)
				String updatedNik = kodeKecamatan;
				updatedNik = updatedNik + pisahTanggal[2] + pisahTanggal[1] + pisahTanggal[0].substring(2);
				
				//panggil method yang sudah dibuat untuk mengecek kesamaan NIK
				int cekKesamaanNIK = pendudukDAO.cekNIKYangSama(updatedNik+"%");
		
				//By Default, NIK akan diset akhirnya 0001, jika ada yg sama baru ditambahin 1
				cekKesamaanNIK = cekKesamaanNIK + 1;
				String kesamaanNIK = cekKesamaanNIK + "";
				
				//Lakukan looping untuk mengecek seberapa banyak nik yang sama
				for(int i=4; i>kesamaanNIK.length(); i+=0) {
					kesamaanNIK = "0" + kesamaanNIK;
				}
				updatedNik = updatedNik + kesamaanNIK;
				/* * Proses pembuatan NIK seperti menambahkan NIK baru berhenti sampai sini */
			
			//Masukkan nik yang sudah dibuat ulang lalu set menjadi nik yang aktif
			updatedPenduduk.setNik(updatedNik);
			
			pendudukDAO.ubahPenduduk(updatedPenduduk);
			model.addAttribute("nik", nik);
			model.addAttribute("oldPenduduk", oldPenduduk);
			return "suksesUbahPenduduk";
		}
		else {
			pendudukDAO.ubahPenduduk(updatedPenduduk);
			model.addAttribute("nik", nik);
			model.addAttribute("updatedPenduduk", updatedPenduduk);
			return "suksesUbahPenduduk";
		}
    }
	
	@RequestMapping(value="/penduduk/mati", method=RequestMethod.POST)
	public String suksesMatiPenduduk(Model model, @RequestParam(value="nik") String nik) {
		PendudukModel penduduk = pendudukDAO.selectPenduduk(nik);
		model.addAttribute("penduduk", penduduk);
		return "form-matiPenduduk";
	}
	
	@RequestMapping(value="/penduduk", method=RequestMethod.POST)
	public String formMatiPenduduk(Model model, @RequestParam(value="nik") String nik) {
		PendudukModel penduduk = pendudukDAO.selectPenduduk(nik);
		//Matikan penduduk
		pendudukDAO.matikanPenduduk(nik);
		
		int hitungAnggota = pendudukDAO.hitungNotWafat(penduduk.getId_keluarga());
		
		KeluargaModel keluarga = keluargaDAO.selectKeluarga(penduduk.getId_keluarga());
				
		if(hitungAnggota == 0) {
			keluarga.setIs_tidak_berlaku(1);
		}
		keluargaDAO.keluargaTidakAktif(keluarga.getId());
		model.addAttribute("nik", nik);
		return "suksesMatiPenduduk";
	}		
	
	@RequestMapping(value="/penduduk/cari", method=RequestMethod.GET)
	 public String cariPenduduk(Model model, 
			 @RequestParam Optional<String> kt,
			 @RequestParam Optional<String> kc,
			 @RequestParam Optional<String> kl) 
	{
		List<KotaModel> allKota = kotaDAO.selectSemuaKota();
		model.addAttribute("allKota", allKota);
		KecamatanModel kecamatan = null;
		KelurahanModel kelurahan = null;
		
		if(kt.isPresent()) {
			model.addAttribute("idKotaNow", kt.get());
			List<KecamatanModel> kecamatanByKota = kecamatanDAO.selectKecamatanByKota(kt.get());		
			model.addAttribute("kecamatanByKota", kecamatanByKota);
			
			//Untuk mengambil nama Kota
			String judulKota = kotaDAO.selectKota(kt.get()).getNama_kota();
			model.addAttribute("judulKota", judulKota);
		}
		
		if(kc.isPresent()) {
			model.addAttribute("idKecamatanNow", kc.get());
			List<KelurahanModel> kelurahanByKecamatan = kelurahanDAO.selectKelurahanByIdKecamatan(kc.get());
			model.addAttribute("kelurahanByKecamatan", kelurahanByKecamatan);
			
			//Untuk mengambil nama Kecamatan
			kecamatan = kecamatanDAO.selectKecamatan(kc.get());
			String judulKecamatan = kecamatan.getNama_kecamatan();
			model.addAttribute("judulKecamatan", judulKecamatan);
		}
		
		if(kl.isPresent()) {
			model.addAttribute("idKelurahanNow", kl.get());
			
			//Untuk mengambil nama Kelurahan
			//ini Bapaknya yang punya List of keluarga di sebuah kelurahan
				//anaknya punya List of penduduk di sebuah keluarga
			kelurahan = kelurahanDAO.selectKelurahan(kl.get());	
			String judulKelurahan = kelurahan.getNama_kelurahan();
			model.addAttribute("kelurahan", kelurahan);
			
			//Inisiasi penduduk (tanpa isi)
			List<PendudukModel> penduduks = new ArrayList<PendudukModel>();
			/*
			 * Contoh Object Oriented 
			 */
			//Buat sebuah ArrayList kosong yang nanti menampung penduduk-penduduk dari keluarga yang ada 
			for (KeluargaModel keluarga : kelurahan.getKeluargas()) {
		    	for (PendudukModel penduduk : keluarga.getPenduduks()) {
					penduduks.add(penduduk);
				}
			}
			model.addAttribute("penduduks", penduduks);
		}
		return "tampilkanPenduduk";
	}
}