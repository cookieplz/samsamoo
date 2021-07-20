package com.ezen.samsamoo.service;


import com.ezen.samsamoo.dao.SamFileDao;
import com.ezen.samsamoo.dto.SamFile;
import com.ezen.samsamoo.dto.ResultData;
import com.ezen.samsamoo.util.Util;
import com.google.common.base.Joiner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class SamFileService {
	
    @Value("${custom.samFileDirPath}")
    private String samFileDirPath;

    @Autowired
    private SamFileDao samFileDao;

    
//----------------------------------------------------------------------------------------------------       
   // 업로드된 파일의 메타정보를 DB에 저장하기
    public ResultData saveMeta(String relTypeCode, int relId, String typeCode, String type2Code, int fileNo,
                               String originFileName, String fileExtTypeCode, String fileExtType2Code, String fileExt, int fileSize,
                               String fileDir) {

    	// Util.mapOf 함수를 통해 파일의 정보를 하나의 객체에 다 때려넣을 수 있다. 짱!ㄴ
        Map<String, Object> param = Util.mapOf("relTypeCode", relTypeCode, "relId", relId, "typeCode", typeCode,
                "type2Code", type2Code, "fileNo", fileNo, "originFileName", originFileName, "fileExtTypeCode",
                fileExtTypeCode, "fileExtType2Code", fileExtType2Code, "fileExt", fileExt, "fileSize", fileSize,
                "fileDir", fileDir);
        samFileDao.saveMeta(param);

        int id = Util.getAsInt(param.get("id"), 0);
        return new ResultData("S-1", "성공하였습니다.", "id", id);
    }

    
//----------------------------------------------------------------------------------------------------     
    // 업로드된 파일을 서버의 특정 위치에 올바른 파일명으로 저장하기
    public ResultData save(MultipartFile multipartFile, String relTypeCode, int relId, String typeCode, String type2Code, int fileNo) {
        String fileInputName = multipartFile.getName();
        String[] fileInputNameBits = fileInputName.split("__");

        if (fileInputNameBits[0].equals("file") == false) {
            return new ResultData("F-1", "파라미터 명이 올바르지 않습니다.");
        }

        int fileSize = (int) multipartFile.getSize();
        if (fileSize <= 0) {
            return new ResultData("F-2", "파일이 업로드 되지 않았습니다.");
        }

        String originFileName = multipartFile.getOriginalFilename();
        String fileExtTypeCode = Util.getFileExtTypeCodeFromFileName(multipartFile.getOriginalFilename());
        String fileExtType2Code = Util.getFileExtType2CodeFromFileName(multipartFile.getOriginalFilename());
        String fileExt = Util.getFileExtFromFileName(multipartFile.getOriginalFilename()).toLowerCase();

        if (fileExt.equals("jpeg")) {
            fileExt = "jpg";
        } else if (fileExt.equals("htm")) {
            fileExt = "html";
        }

        String fileDir = Util.getNowYearMonthDateStr();
        
        if (relId > 0) {
            SamFile oldSamFile = getSamFile(relTypeCode, relId, typeCode, type2Code, fileNo);
            if (oldSamFile != null) {
                deleteSamFile(oldSamFile);
            }
        }

        ResultData saveMetaRd = saveMeta(relTypeCode, relId, typeCode, type2Code, fileNo, originFileName,
                fileExtTypeCode, fileExtType2Code, fileExt, fileSize, fileDir);
        
        int newSamFileId = (int) saveMetaRd.getBody().get("id");

        // 새 파일이 저장될 폴더(io파일) 객체 생성
        String targetDirPath = samFileDirPath + "/" + relTypeCode + "/" + fileDir;
        java.io.File targetDir = new java.io.File(targetDirPath);

        // 새 파일이 저장될 폴더가 존재하지 않는다면 생성
        if (targetDir.exists() == false) {
            targetDir.mkdirs();
        }

        String targetFileName = newSamFileId + "." + fileExt;
        String targetFilePath = targetDirPath + "/" + targetFileName;

        // 파일 생성(업로드된 파일을 지정된 경로롤 옮김)
        try {
            multipartFile.transferTo(new File(targetFilePath));
        } catch (IllegalStateException | IOException e) {
            return new ResultData("F-3", "파일저장에 실패하였습니다.");
        }

        return new ResultData("S-1", "파일이 생성되었습니다.", "id", newSamFileId, "fileRealPath", targetFilePath, "fileName",
                targetFileName, "fileInputName", fileInputName);
    }

    
//----------------------------------------------------------------------------------------------------  
    // 파일 저장
    public ResultData save(MultipartFile multipartFile) {
    	String fileInputName = multipartFile.getName();
        String[] fileInputNameBits = fileInputName.split("__");

        String relTypeCode = fileInputNameBits[1];
        int relId = Integer.parseInt(fileInputNameBits[2]);
        String typeCode = fileInputNameBits[3];
        String type2Code = fileInputNameBits[4];
        int fileNo = Integer.parseInt(fileInputNameBits[5]);

        return save(multipartFile, relTypeCode, relId, typeCode, type2Code, fileNo);
    }

    // 파일 저장
    public ResultData save(MultipartFile multipartFile, int relId) {
        String fileInputName = multipartFile.getName();
        String[] fileInputNameBits = fileInputName.split("__");

        String relTypeCode = fileInputNameBits[1];
        String typeCode = fileInputNameBits[3];
        String type2Code = fileInputNameBits[4];
        int fileNo = Integer.parseInt(fileInputNameBits[5]);

        return save(multipartFile, relTypeCode, relId, typeCode, type2Code, fileNo);
    }
    
    
//----------------------------------------------------------------------------------------------------         
    //	파일 여러개 한 번에 저장
     public ResultData saveFiles(Map<String, Object> param, MultipartRequest multipartRequest) {
         // 업로드 시작
         Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();

         Map<String, ResultData> filesResultData = new HashMap<>();
         List<Integer> samFileIds = new ArrayList<>();

         for (String fileInputName : fileMap.keySet()) {
             MultipartFile multipartFile = fileMap.get(fileInputName);

             if (multipartFile.isEmpty() == false) {
                 ResultData fileResultData = save(multipartFile);
                 int samFileId = (int) fileResultData.getBody().get("id");
                 samFileIds.add(samFileId);

                 filesResultData.put(fileInputName, fileResultData);
             }
         }

         String samFileIdsStr = Joiner.on(",").join(samFileIds);

         // 삭제 시작
         int deleteCount = 0;

         for (String inputName : param.keySet()) {
             String[] inputNameBits = inputName.split("__");

             if (inputNameBits[0].equals("deleteFile")) {
                 String relTypeCode = inputNameBits[1];
                 int relId = Integer.parseInt(inputNameBits[2]);
                 String typeCode = inputNameBits[3];
                 String type2Code = inputNameBits[4];
                 int fileNo = Integer.parseInt(inputNameBits[5]);

                 SamFile oldSamFile = getSamFile(relTypeCode, relId, typeCode, type2Code, fileNo);

                 if (oldSamFile != null) {
                     deleteSamFile(oldSamFile);
                     deleteCount++;
                 }
             }
         }

         return new ResultData("S-1", "파일을 업로드하였습니다.", "filesResultData", filesResultData, "samFileIdsStr",
                 samFileIdsStr, "deleteCount", deleteCount);
     }
    
        
//----------------------------------------------------------------------------------------------------     
    // 파일 한 개 가져오기
    public SamFile getSamFile(String relTypeCode, int relId, String typeCode, String type2Code, int fileNo) {
        return samFileDao.getSamFile(relTypeCode, relId, typeCode, type2Code, fileNo);
    }

    
 // 파일 리스트 가져오기
    public List<SamFile> getSamFiles(String relTypeCode, int relId, String typeCode, String type2Code) {
        return samFileDao.getSamFiles(relTypeCode, relId, typeCode, type2Code);
    }    
    

//----------------------------------------------------------------------------------------------------
    // 관련 데이터 번호 바꾸기
    public void changeRelId(int id, int relId) {
        samFileDao.changeRelId(id, relId);
    }

    
    // 관련 데이터 번호 바꾸기
    // 파일이 먼저 생성된 후에, 관련 데이터가 생성되는 경우에는, file의 관련 데이터 번호(relId)가 일단 0으로 저장된다.
    // 그것을 뒤늦게라도 이렇게 고쳐야 한다.
    public void changeInputFileRelIds(Map<String, Object> param, int id) {
        String samFileIdsStr = Util.ifEmpty((String) param.get("samFileIdsStr"), null);

        if (samFileIdsStr != null) {
            List<Integer> samFileIds = Util.getListDividedBy(samFileIdsStr, ",");

            for (int samFileId : samFileIds) {
                changeRelId(samFileId, id);
            }
        }
    }
    
    
//----------------------------------------------------------------------------------------------------
    // 파일을 삭제하는 메서드
    private void deleteSamFile(SamFile samFile) {
        String filePath = samFile.getFilePath(samFileDirPath);
        Util.deleteFile(filePath);

        samFileDao.deleteFile(samFile.getId());
    }
    
    
    // 파일 삭제하기(관련 타입코드, 관련 데이터 번호, 타입코드, 타입2코드, 파일번호를 이용해)
    public void deleteSamFile(String relTypeCode, int relId, String typeCode, String type2Code, int fileNo) {
        SamFile samFile = samFileDao.getSamFile(relTypeCode, relId, typeCode, type2Code, fileNo);

        deleteSamFile(samFile);
    }
    
    
    // 파일 여러개 삭제하기
    public void deleteSamFiles(String relTypeCode, int relId) {
        List<SamFile> samFiles = samFileDao.getSamFilesByRelTypeCodeAndRelId(relTypeCode, relId);

        for (SamFile samFile : samFiles) {
            deleteSamFile(samFile);
        }
    }
    
    
//----------------------------------------------------------------------------------------------------
    // 파일 번호를 이용하여 파일 데이터 가지고 오기
    public SamFile getSamFile(int id) {
        return samFileDao.getSamFileById(id);
    }

    
    /* 일단 주석. 오류발생시 주석 해제 바람
    public Map<Integer, Map<String, SamFile>> getFilesMapKeyRelIdAndFileNo(String relTypeCode, List<Integer> relIds,  String typeCode, String type2Code) {
        List<SamFile> samFiles = samFileDao.getSamFilesRelTypeCodeAndRelIdsAndTypeCodeAndType2Code(relTypeCode, relIds, typeCode, type2Code);
        
        Map<Integer, Map<String, SamFile>> rs = new LinkedHashMap<>();
        for (SamFile samFile : samFiles) {
            if (rs.containsKey(samFile.getRelId()) == false) {
                rs.put(samFile.getRelId(), new LinkedHashMap<>());
            }
            rs.get(samFile.getRelId()).put(samFile.getFileNo() + "", samFile);
        }
        return rs;
    }
    */
 

}