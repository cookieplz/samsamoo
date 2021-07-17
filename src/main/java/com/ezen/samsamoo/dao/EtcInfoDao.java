package com.ezen.samsamoo.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ezen.samsamoo.dto.EtcInfo;

@Mapper
public interface EtcInfoDao {
		 
	// relId, relTypeCode, typeCode, type2Code, expireDate를 이용해 기타 정보 가져오기 
    public EtcInfo get(@Param("relTypeCode") String relTypeCode, @Param("relId") int relId, @Param("typeCode") String typeCode, @Param("type2Code") String type2Code);

    // relId, relTypeCode, typeCode, type2Code, expireDate를 이용해 밸류값을 가져오기
    public String getValue(@Param("relTypeCode") String relTypeCode, @Param("relId") int relId, @Param("typeCode") String typeCode, @Param("type2Code") String type2Code);
    
   // relId, relTypeCode, typeCode, type2Code, expireDate을 etcInfo 객체에 저장하기 
    public int setValue(@Param("relTypeCode") String relTypeCode, @Param("relId") int relId, @Param("typeCode") String typeCode, @Param("type2Code") String type2Code, @Param("value") String value, @Param("expireDate") String expireDate);
    
    // relId, relTypeCode, typeCode, type2Code, expireDate을 이용해 데이터 삭제하기
    public int remove(@Param("relTypeCode") String relTypeCode, @Param("relId") int relId, @Param("typeCode") String typeCode, @Param("type2Code") String type2Code);
}
