package com.example.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.example.model.PendudukModel;

@Mapper
public interface PendudukMapper {
	@Select("SELECT * FROM penduduk WHERE nik=#{nik}")
	PendudukModel selectPenduduk(@Param("nik") String nik);
	
	@Insert("INSERT INTO penduduk(id, nik, nama, tempat_lahir, tanggal_lahir, jenis_kelamin, is_wni, id_keluarga, agama, pekerjaan, status_perkawinan, status_dalam_keluarga, golongan_darah, is_wafat)"
			+ "VALUES(#{id}, #{nik}, #{nama}, #{tempat_lahir}, #{tanggal_lahir}, #{jenis_kelamin}, #{is_wni}, #{id_keluarga}, #{agama}, #{pekerjaan}, #{status_perkawinan}, #{status_dalam_keluarga}, #{golongan_darah}, #{is_wafat})")
	void tambahPenduduk(PendudukModel penduduk);

	//Query utk ngecek seberapa banyak nik yang sama
	@Select("SELECT count(*) FROM penduduk WHERE nik like #{nik}")
	int cekNIKYangSama(String nik);
	
	@Select("SELECT max(id) FROM penduduk")
	int hitungId();
	
	@Update("UPDATE penduduk SET nik=#{nik}, nama=#{nama}, tempat_lahir=#{tempat_lahir}, tanggal_lahir=#{tanggal_lahir}, "
			+ "jenis_kelamin=#{jenis_kelamin}, is_wni=#{is_wni}, id_keluarga=#{id_keluarga}, agama=#{agama}, pekerjaan=#{pekerjaan}, "
			+ "status_perkawinan=#{status_perkawinan}, status_dalam_keluarga=#{status_dalam_keluarga}, golongan_darah=#{golongan_darah} "
			+ "WHERE id=#{id}")
	void ubahPenduduk(PendudukModel penduduk);
	
	@Update("UPDATE penduduk SET is_wafat = 1 WHERE nik=#{nik}")
	void matikanPenduduk(String nik);

	@Select("SELECT count(*) FROM penduduk WHERE id_keluarga=#{id_keluarga} AND is_wafat=0")
	int hitungNotWafat(@Param("id_keluarga")String id_keluarga);
	
}