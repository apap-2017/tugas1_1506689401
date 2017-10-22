package com.example.service;

import java.util.List;

import com.example.model.KecamatanModel;

public interface KecamatanService {
	KecamatanModel selectKecamatan (String id);
	
	List<KecamatanModel> selectKecamatanByKota(String id_kota);
}
