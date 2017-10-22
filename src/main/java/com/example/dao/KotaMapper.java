package com.example.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.example.model.KotaModel;

@Mapper
public interface KotaMapper {
	@Select("SELECT * FROM kota WHERE id=#{id}")
	KotaModel selectKota(@Param("id") String id);
	
	@Select("SELECT * FROM kota")
	List<KotaModel> selectSemuaKota();
}
