package com.example.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.example.model.KecamatanModel;

@Mapper
public interface KecamatanMapper {
	@Select("SELECT * FROM kecamatan WHERE id=#{id}")
	KecamatanModel selectKecamatan(@Param("id") String id);
	
	@Select("SELECT * FROM kecamatan WHERE id_kota=#{id_kota}")
	List<KecamatanModel> selectKecamatanByIdKota(@Param("id_kota") String id_kota);
}
