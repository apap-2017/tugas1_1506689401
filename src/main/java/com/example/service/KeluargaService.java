package com.example.service;

import java.util.List;

import com.example.model.KeluargaModel;
import com.example.model.PendudukModel;

public interface KeluargaService {
	KeluargaModel selectKeluarga (String id);
	
	
	KeluargaModel selectNKKKeluarga(String nomor_kk);
	
	List<PendudukModel> selectAnggotaKeluarga(String id);
	
	void tambahKeluarga(KeluargaModel keluarga);
	
	int cekNKKYangSama(String nomor_kk);
	
	int hitungId();

	void ubahKeluarga(KeluargaModel keluarga);
	
	void keluargaTidakAktif(String id);
	
}
