package com.example.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
public class KeluargaController {
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
	
	@RequestMapping("/keluarga")
    public String tampilNIKPenduduk (Model model,
            @RequestParam(value = "nomor_kk", required = false) String nomor_kk)
    {
        KeluargaModel keluarga = keluargaDAO.selectNKKKeluarga(nomor_kk);
        
        if (keluarga != null) {
            model.addAttribute ("keluarga", keluarga);
            
            //Buat object dari KelurahanModel agar bisa ditarik di view
            KelurahanModel kelurahanMdl = kelurahanDAO.selectKelurahan(keluarga.getId_kelurahan());
            model.addAttribute("kelurahan", kelurahanMdl);
            
            //Buat object dari KelurahanModel agar bisa ditarik di view
            KecamatanModel kecamatanMdl = kecamatanDAO.selectKecamatan(kelurahanMdl.getId_kecamatan());
            model.addAttribute("kecamatan", kecamatanMdl);
            
            //Buat object dari KelurahanModel agar bisa ditarik di view
            KotaModel kotaMdl = kotaDAO.selectKota(kecamatanMdl.getId_kota());
            model.addAttribute("kota", kotaMdl);
            
            //List anggotaKeluarga yang isinya anggota keluarga dari sebuah keluarga, di ambil dari penduduk model            
            List<PendudukModel> anggotaKeluarga = keluargaDAO.selectAnggotaKeluarga(keluarga.getId());
            model.addAttribute("anggota", anggotaKeluarga);
            
            return "viewKeluarga";
        } 
        else {
        	//Simpan JUDUL di notifikasi dan DETAIL di detail        	
        	model.addAttribute("notifikasi", "NKK not found");
            model.addAttribute ("detail", "NKK = " + nomor_kk + " tidak ditemukan");
            return "not-found";   
        }
    }
	
	@RequestMapping(value="/keluarga/tambah", method=RequestMethod.GET)
	public String formTambahKeluarga() {
		return "form-tambahKeluarga";
	}
	
	@RequestMapping(value="/keluarga/tambah", method=RequestMethod.POST)
	public String tambahKeluarga(Model model,
			@RequestParam(value = "alamat", required=false) String alamat,
			@RequestParam(value = "rt", required=false) String rt,
			@RequestParam(value = "rw", required=false) String rw,
			@RequestParam(value = "kelurahan", required=false) String kelurahan,
			@RequestParam(value = "kecamatan", required=false) String kecamatan,
			@RequestParam(value = "kota", required=false) String kota
			)
	{
		KelurahanModel kelurahanMdl = kelurahanDAO.selectKelurahan(kelurahan);
		//model.addAttribute("kelurahan", kelurahanMdl);
		KecamatanModel kecamatanMdl = kecamatanDAO.selectKecamatan(kelurahanMdl.getId_kecamatan());
        //model.addAttribute("kecamatan", kecamatanMdl);
		
		//masukkan kode kecamatan ke string agar bisa di substring
		String kodeKecamatan = kecamatanMdl.getKode_kecamatan();
		
		//ambil 6 kode awal dari kode kecamatan, yg 0 terakhir gausah karena pasti akhirnya 0
		kodeKecamatan = kodeKecamatan.substring(0, 6);
		
		//ambil tanggal yang bersesuaian dengan hari ini, gunakan format yang sesuai, lalu simpan ke tanggal hari ini
		Date today = new Date();
		SimpleDateFormat tanggalFormatBenar = new SimpleDateFormat("ddMMyy");
		String tanggalHariIni = tanggalFormatBenar.format(today);
				
		//Buat nkk yang sesuai dengan ketentuan di wikipedia (6 digit kode kecamatan + 6 digit hari ini + 4 digit kesesuaian dengan NKK lain)
		String nkk = kodeKecamatan;
		nkk = nkk + tanggalHariIni;
		
		//panggil method yang sudah dibuat untuk mengecek kesamaan NIK
		int cekKesamaanNKK = keluargaDAO.cekNKKYangSama(nkk+"%");
		
		//By Default, NKK akan diseet akhirnya 0001, jika ada yg sama baru ditambahin 1
		cekKesamaanNKK = cekKesamaanNKK + 1;
		String kesamaanNKK = cekKesamaanNKK + "";
		
		//Lakukan looping untuk mengecek seberapa banyak nkk yang sama
		int i=4;
		while(i > kesamaanNKK.length()) {
			kesamaanNKK = "0" + kesamaanNKK;
		}
		nkk = nkk + kesamaanNKK;
		
		int noFinalId = keluargaDAO.hitungId() + 1;
		String finalId = noFinalId + "";
		
		KeluargaModel keluarga = new KeluargaModel(finalId,nkk,alamat,rt,rw,kelurahan,0,null);
		keluargaDAO.tambahKeluarga(keluarga);
		model.addAttribute("keluarga", keluarga);
		
		return "suksesTambahKeluarga";
	}
	
	@RequestMapping(value="/keluarga/ubah/{nomor_kk}", method=RequestMethod.GET)
	public String formUbahKeluarga(Model model, @PathVariable(value="nomor_kk") String nomor_kk) {
		KeluargaModel keluarga = keluargaDAO.selectNKKKeluarga(nomor_kk);
		
		if(keluarga != null) {
			model.addAttribute("keluarga", keluarga);
			return "form-ubahKeluarga";
		}
		else {
			model.addAttribute("nomor_kk", nomor_kk);
			model.addAttribute("notifikasiKeluarga", "Keluarga not found");
			model.addAttribute("detailKeluarga", "Keluarga dengan NKK " + nomor_kk + " tidak ditemukan");
			return "not-found";
		}
	}
	
	@RequestMapping(value="/keluarga/ubah", method=RequestMethod.POST)
	public String ubahKeluarga(Model model, KeluargaModel keluarga,
			@RequestParam(value="old_id_kelurahan", required=false) String old_id_kelurahan,
			@RequestParam(value="id", required=false) String id)
	{		
		//Kalo id kelurahan yg lama gasama dgn id kelurahan yg baru
		if(!keluarga.getId_kelurahan().equalsIgnoreCase(old_id_kelurahan)) {
			List<PendudukModel> anggotaKeluarga = keluargaDAO.selectAnggotaKeluarga(id);		
			
			/*
			 * Lakukan updated NKK seperti menambah NKK baru yg ada di method tambahKeluarga
			 * 	==tambahKeluarga di KeluargaController
				*/
					KelurahanModel kelurahanMdl = kelurahanDAO.selectKelurahan(keluarga.getId_kelurahan());
					//model.addAttribute("kelurahan", kelurahanMdl);
					KecamatanModel kecamatanMdl = kecamatanDAO.selectKecamatan(kelurahanMdl.getId_kecamatan());
			        //model.addAttribute("kecamatan", kecamatanMdl);
					
					//masukkan kode kecamatan ke string agar bisa di substring
					String kodeKecamatan = kecamatanMdl.getKode_kecamatan();
					
					//ambil 6 kode awal dari kode kecamatan, yg 0 terakhir gausah karena pasti akhirnya 0
					kodeKecamatan = kodeKecamatan.substring(0, 6);
					
					//ambil tanggal yang bersesuaian dengan hari ini, gunakan format yang sesuai, lalu simpan ke tanggal hari ini
					Date today = new Date();
					SimpleDateFormat tanggalFormatBenar = new SimpleDateFormat("ddMMyy");
					String tanggalHariIni = tanggalFormatBenar.format(today);
							
					//Buat nkk yang sesuai dengan ketentuan di wikipedia (6 digit kode kecamatan + 6 digit hari ini + 4 digit kesesuaian dengan NKK lain)
					String updatedNkk = kodeKecamatan;
					updatedNkk = updatedNkk + tanggalHariIni;
					
					//panggil method yang sudah dibuat untuk mengecek kesamaan NIK
					int cekKesamaanNKK = keluargaDAO.cekNKKYangSama(updatedNkk+"%");
					
					//By Default, NKK akan diseet akhirnya 0001, jika ada yg sama baru ditambahin 1
					cekKesamaanNKK = cekKesamaanNKK + 1;
					String kesamaanNKK = cekKesamaanNKK + "";
					
					//Lakukan looping untuk mengecek seberapa banyak nkk yang sama
					int i=4;
					while(i > kesamaanNKK.length()) {
						kesamaanNKK = "0" + kesamaanNKK;
					}
					updatedNkk = updatedNkk + kesamaanNKK;
					
					//Untuk mengambil nomor KK yang lama
					model.addAttribute("oldies_nomor_kk", keluarga.getNomor_kk());
					
					keluarga.setNomor_kk(updatedNkk);
					
					keluargaDAO.ubahKeluarga(keluarga);
			/* Updated NKK sampai sini*/
					
					//Looping utk update NIK penduduk baru
					for(int x=0; x<anggotaKeluarga.size();x++) {
						/*
						 * Lakukan pembuatan NIK baru seperti yang ada di updated NIK Penduduk
						 * 	Jadi kita looping tiap anggota keluarganya, terus NIKnya diganti satu persatu, disesuain dengan NKK yg sudah diupdate
						 * 	== method ubahPenduduk di PendudukController
						 */
							
							//ambil tanggal lahir
							String tglLahir = anggotaKeluarga.get(x).getTanggal_lahir();
							
							//pisah tanggalnya biar bisa dipisahin
							String pisahTanggal[] = tglLahir.split("-");
							
							//cek jenis kelamin, jika jenis kelaminnya wanita maka tanggal lahirnya ditambah 40, sedangkan pria tidak diubah
								//lakukan parsing karena tipe data awalnya string
									//parse dari string ke integer lalu parse lagi ke string
							if(anggotaKeluarga.get(x).getJenis_kelamin().equals("1")) {
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
							for(int j=4; j>kesamaanNIK.length(); j+=0) {
								kesamaanNIK = "0" + kesamaanNIK;
							}
							updatedNik = updatedNik + kesamaanNIK;
							/* * Proses pembuatan NIK seperti menambahkan NIK baru berhenti sampai sini */
							
							//Masukkan nik yang sudah dibuat ulang lalu set menjadi nik yang aktif
							anggotaKeluarga.get(x).setNik(updatedNik);
							
							pendudukDAO.ubahPenduduk(anggotaKeluarga.get(x));
						/*Membuat NIK baru dengan NKK baru sampai sini*/
					}
					return "suksesUbahKeluarga";
		}
		else {
			keluargaDAO.ubahKeluarga(keluarga);
			model.addAttribute("oldies_nomor_kk", keluarga.getNomor_kk());
			return "suksesUbahKeluarga";
		}
	}
}