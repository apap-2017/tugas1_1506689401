package com.example.dao;

import java.util.List;

import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.example.model.KeluargaModel;
import com.example.model.KelurahanModel;
import com.example.model.PendudukModel;

@Mapper
public interface KelurahanMapper {
	
	//Manggil anak2nya, butuh di implementasikan di service
	@Select("SELECT * FROM kelurahan WHERE id=#{id}")
	@Results(value = {
    		@Result(property="id", column="id"),
    		@Result(property="kode_kelurahan", column="kode_kelurahan"),
    		@Result(property="id_kecamatan", column="id_kecamatan"),
    		@Result(property="nama_kelurahan", column="nama_kelurahan"),
    		@Result(property="kode_pos", column="kode_pos"),
    		@Result(property="keluargas", column="id",
    				javaType = List.class,
    				many=@Many(select="selectKeluargaKelurahan"))
    })
	KelurahanModel selectKelurahan(@Param("id") String id);
	
	@Select("SELECT * FROM kelurahan WHERE id_kecamatan=#{id_kecamatan}")
	List<KelurahanModel> selectKelurahanByIdKecamatan(@Param("id_kecamatan") String id_kecamatan);
	
	//ini dipanggil sama method selectKelurahanByIdKecamatan
	@Select("SELECT * FROM keluarga WHERE id_kelurahan=#{id_kelurahan}")
	@Results(value = {
    		@Result(property="id", column="id"),
    		@Result(property="penduduks", column="id",
    				javaType = List.class,
    				many=@Many(select="selectAnggotaKeluarga"))
    })
	List<KeluargaModel> selectKeluargaKelurahan(@Param("id_kelurahan") String id_kelurahan);
	
	//Dipanggil sama method selectKeluargaKelurahan
	@Select("SELECT * FROM penduduk WHERE id_keluarga=#{id}")
	List<PendudukModel> selectAnggotaKeluarga(@Param("id") String id);
	
	
}
