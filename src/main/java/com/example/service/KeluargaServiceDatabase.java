package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dao.KeluargaMapper;
import com.example.model.KeluargaModel;
import com.example.model.PendudukModel;

import groovy.util.logging.Slf4j;

@Slf4j
@Service
public class KeluargaServiceDatabase implements KeluargaService{

	@Autowired
    private KeluargaMapper keluargaMapper;
		
	@Override
	public KeluargaModel selectKeluarga(String id) {
		return keluargaMapper.selectKeluarga(id);
	}

	@Override
	public KeluargaModel selectNKKKeluarga(String nomor_kk) {
		return keluargaMapper.selectNKKKeluarga(nomor_kk);
	}

	@Override
	public List<PendudukModel> selectAnggotaKeluarga(String id) {
		return keluargaMapper.selectAnggotaKeluarga(id);
	}

	@Override
	public void tambahKeluarga(KeluargaModel keluarga) {
		keluargaMapper.tambahKeluarga(keluarga);
	}

	@Override
	public int cekNKKYangSama(String nomor_kk) {
		return keluargaMapper.cekNKKYangSama(nomor_kk);
	}

	@Override
	public int hitungId() {
		return keluargaMapper.hitungId();
	}

	@Override
	public void ubahKeluarga(KeluargaModel keluarga) {
		keluargaMapper.ubahKeluarga(keluarga);
		
	}

	@Override
	public void keluargaTidakAktif(String id) {
		keluargaMapper.keluargaTidakAktif(id);		
	}
}