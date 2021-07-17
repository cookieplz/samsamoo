package com.ezen.samsamoo.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ezen.samsamoo.dto.SamFile;

import java.util.List;
import java.util.Map;

@Mapper
public interface SamFileDao {
	
	// 업로드된 파일의 메타정보를 DB에 저장
    void saveMeta(Map<String, Object> param);

    // 파일 데이터 가지고 오기
    SamFile getSamFile(@Param("relTypeCode") String relTypeCode, @Param("relId") int relId,
                       @Param("typeCode") String typeCode, @Param("type2Code") String type2Code, @Param("fileNo") int fileNo);

    // 파일 번호를 이용해 파일 데이터 가지고 오기
    SamFile getSamFileById(@Param("id") int id);

    // 파일들 여러 개 가지고 오기
    List<SamFile> getSamFiles(@Param("relTypeCode") String relTypeCode, @Param("relId") int relId,
                              @Param("typeCode") String typeCode, @Param("type2Code") String type2Code);
    
    // 관련타입코드와 관련 데이터 번호를 이용해 파일들 여러 개 가지고 오기
    List<SamFile> getSamFilesByRelTypeCodeAndRelId(@Param("relTypeCode") String relTypeCode, @Param("relId") int relId);
    
    // 관련 타입 코드, 관련 데이터 번호, 타입코드, 타입2코드를 이용해서 파일들 가지고 오기( dao.xml에서 쿼리 보면 쉽게 이해할 수 있음)
    List<SamFile> getSamFilesRelTypeCodeAndRelIdsAndTypeCodeAndType2Code(@Param("relTypeCode") String relTypeCode,
                                                                         @Param("relIds") List<Integer> relIds, @Param("typeCode") String typeCode,
                                                                         @Param("type2Code") String type2Code);
    
    // 관련 데이터번호 바꾸기
    void changeRelId(@Param("id") int id, @Param("relId") int relId);

    // 파일 삭제하기
    void deleteFile(@Param("id") int id);
    
    // 파일들 삭제하기
    void deleteFiles(@Param("relTypeCode") String relTypeCode, @Param("relId") int relId);




   
}
