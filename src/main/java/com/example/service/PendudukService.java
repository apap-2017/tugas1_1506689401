package com.example.service;

import java.util.List;

import com.example.model.PendudukModel;

public interface PendudukService {
	 PendudukModel selectPenduduk (String nik);
	 
	 List<PendudukModel> pendudukByKeluarga(int id_keluarga);

	 void tambahPenduduk(PendudukModel penduduk);
	 
	 int cekNIKYangSama(String nik);
	 
	 int hitungId();
	 
	 void ubahPenduduk(PendudukModel penduduk);
	 
	 void matikanPenduduk(String nik);
	 
	 int hitungNotWafat(String id_keluarga);
}