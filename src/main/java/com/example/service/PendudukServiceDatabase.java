package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dao.PendudukMapper;
import com.example.model.PendudukModel;

import groovy.util.logging.Slf4j;

@Slf4j
@Service
public class PendudukServiceDatabase implements PendudukService {

	@Autowired
    private PendudukMapper pendudukMapper;
	
	@Override
	public PendudukModel selectPenduduk(String nik) {
		//log.info("select student with npm {}", nik);
		return pendudukMapper.selectPenduduk(nik);
	}
	
	
	//Ini buat apa ya kemarin lupa kenapa implementasi ini-_-
	@Override
	public List<PendudukModel> pendudukByKeluarga(int id_keluarga) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void tambahPenduduk(PendudukModel penduduk) {
		pendudukMapper.tambahPenduduk(penduduk);
	}

	@Override
	public int cekNIKYangSama(String nik) {
		return pendudukMapper.cekNIKYangSama(nik);
	}

	@Override
	public int hitungId() {
		return pendudukMapper.hitungId();
	}


	@Override
	public void ubahPenduduk(PendudukModel penduduk) {
		pendudukMapper.ubahPenduduk(penduduk);
	}

	@Override
	public void matikanPenduduk(String nik) {
		pendudukMapper.matikanPenduduk(nik);
	}


	@Override
	public int hitungNotWafat(String id_keluarga) {
		return pendudukMapper.hitungNotWafat(id_keluarga);
	}
}
