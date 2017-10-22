package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dao.KelurahanMapper;
import com.example.dao.KotaMapper;
import com.example.model.KelurahanModel;
import com.example.model.KotaModel;

import groovy.util.logging.Slf4j;

@Slf4j
@Service
public class KotaServiceDatabase implements KotaService {
	@Autowired
    private KotaMapper kotaMapper;
		
	@Override
	public KotaModel selectKota(String id) {
		// TODO Auto-generated method stub
		return kotaMapper.selectKota(id);
	}

	@Override
	public List<KotaModel> selectSemuaKota() {
		return kotaMapper.selectSemuaKota();
	}
}
