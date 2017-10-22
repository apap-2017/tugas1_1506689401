package com.example.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.example.model.KeluargaModel;
import com.example.model.PendudukModel;

@Mapper
public interface KeluargaMapper {
	@Select("SELECT * FROM keluarga WHERE id=#{id}")
	KeluargaModel selectKeluarga(@Param("id") String id);
	
	@Select("SELECT * FROM keluarga WHERE nomor_kk=#{nomor_kk}")
	KeluargaModel selectNKKKeluarga(@Param("nomor_kk") String nomor_kk);
	
	@Select("SELECT * FROM penduduk WHERE id_keluarga=#{id}")
	List<PendudukModel> selectAnggotaKeluarga(@Param("id") String id);
	
	@Select("INSERT INTO keluarga(id,nomor_kk,alamat,rt,rw,id_kelurahan,is_tidak_berlaku) "
			+ "VALUES(#{id}, #{nomor_kk}, #{alamat}, #{rt}, #{rw}, #{id_kelurahan}, #{is_tidak_berlaku})")
	void tambahKeluarga(KeluargaModel keluarga);
	
	//Query utk ngecek seberapa banyak nkk yang sama
	@Select("SELECT count(*) FROM keluarga WHERE nomor_kk like #{nomor_kk}")
	int cekNKKYangSama(String nomor_kk);
	
	@Select("SELECT max(id) FROM keluarga")
	int hitungId();
	
	@Update("UPDATE keluarga SET nomor_kk=#{nomor_kk}, alamat=#{alamat}, rt=#{rt}, rw=#{rw}, id_kelurahan=#{id_kelurahan} WHERE id=#{id}")
	void ubahKeluarga (KeluargaModel keluarga);
	
	@Update("UPDATE keluarga SET is_tidak_berlaku = 1 WHERE id=#{id}")
	void keluargaTidakAktif(String id);
}