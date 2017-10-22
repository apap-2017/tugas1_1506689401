package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dao.KecamatanMapper;
import com.example.model.KecamatanModel;

import groovy.util.logging.Slf4j;

@Slf4j
@Service
public class KecamatanServiceDatabase implements KecamatanService {
	@Autowired
    private KecamatanMapper kecamatanMapper;
		
	@Override
	public KecamatanModel selectKecamatan(String id) {
		return kecamatanMapper.selectKecamatan(id);
	}

	@Override
	public List<KecamatanModel> selectKecamatanByKota(String id_kota) {
		return kecamatanMapper.selectKecamatanByIdKota(id_kota);
	}
}
