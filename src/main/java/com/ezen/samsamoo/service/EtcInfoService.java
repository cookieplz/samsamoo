package com.ezen.samsamoo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezen.samsamoo.dao.EtcInfoDao;
import com.ezen.samsamoo.dto.EtcInfo;

@Service
public class EtcInfoService {
	
    @Autowired
    private EtcInfoDao etcInfoDao;

    
//----------------------------------------------------------------------------------------------------  
 // relId, relTypeCode, typeCode, type2Code, expireDate를 이용해 기타 정보 가져오기 
    public EtcInfo get(String relTypeCode, int relId, String typeCode, String type2Code) {
        return etcInfoDao.get(relTypeCode, relId, typeCode, type2Code);
    }

    
    // 파일 이름을 통해 파일에 대한 정보를 가져오기 위한 함수
    // 예컨대 "member__0__extra__profileImg__1"의 경우
    // split하면 relTypeCode = member, reId = 0, typeCode = extra, type2Code = profileImg가 되는 것이다. 
    // 요약하자면 __ 단위로 쪼개서 데이터를 저장하기 위한 함수이다.
    public EtcInfo get(String name) {
        String[] nameBits = name.split("__");
        String relTypeCode = nameBits[0];
        int relId = Integer.parseInt(nameBits[1]);
        String typeCode = nameBits[2];
        String type2Code = nameBits[3];

        return get(relTypeCode, relId, typeCode, type2Code);
    }
    
//----------------------------------------------------------------------------------------------------  
    // relId, relTypeCode, typeCode, type2Code, expireDate를 이용해 밸류값을 가져오기
    public String getValue(String relTypeCode, int relId, String typeCode, String type2Code) {
        String value = etcInfoDao.getValue(relTypeCode, relId, typeCode, type2Code);

        if ( value == null ) {
            return "";
        }

        return value;
    }


    public String getValue(String name) {
        String[] nameBits = name.split("__");
        String relTypeCode = nameBits[0];
        int relId = Integer.parseInt(nameBits[1]);
        String typeCode = nameBits[2];
        String type2Code = nameBits[3];

        return getValue(relTypeCode, relId, typeCode, type2Code);
    }
    
//----------------------------------------------------------------------------------------------------  
    // relId, relTypeCode, typeCode, type2Code, expireDate을 etcInfo 객체에 저장하기 
    public int setValue(String relTypeCode, int relId, String typeCode, String type2Code, String value, String expireDate) {
        etcInfoDao.setValue(relTypeCode, relId, typeCode, type2Code, value, expireDate);
        EtcInfo etcInfo = get(relTypeCode, relId, typeCode, type2Code);

        if (etcInfo != null) {
            return etcInfo.getId();
        }

        return -1;
    }
    
    
    public int setValue(String name, String value, String expireDate) {
        String[] nameBits = name.split("__");
        String relTypeCode = nameBits[0];
        int relId = Integer.parseInt(nameBits[1]);
        String typeCode = nameBits[2];
        String type2Code = nameBits[3];

        return setValue(relTypeCode, relId, typeCode, type2Code, value, expireDate);
    }
   
    
  //----------------------------------------------------------------------------------------------------  
 // relId, relTypeCode, typeCode, type2Code, expireDate을 이용해 데이터 삭제하기  
    public int remove(String name) {
        String[] nameBits = name.split("__");
        String relTypeCode = nameBits[0];
        int relId = Integer.parseInt(nameBits[1]);
        String typeCode = nameBits[2];
        String type2Code = nameBits[3];

        return remove(relTypeCode, relId, typeCode, type2Code);
    }

    
    public int remove(String relTypeCode, int relId, String typeCode, String type2Code) {
        return etcInfoDao.remove(relTypeCode, relId, typeCode, type2Code);
    }    
    
}
