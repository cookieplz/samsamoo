package com.ezen.samsamoo.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartRequest;

import com.ezen.samsamoo.dto.SamFile;
import com.ezen.samsamoo.dto.ResultData;
import com.ezen.samsamoo.exception.SamFileNotFoundException;
import com.ezen.samsamoo.service.SamFileService;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

// 파일 업로드, 다운로드를 위한 컨트롤러
@Controller
public class SamFileController {
   
	// application.yml(프로퍼티스) 설정파일 내에 텍스트 값을 변수로 받고싶을 때 사용하는 어노테이션
	@Value("${custom.samFileDirPath}")
    private String samFileDirPath;

    @Autowired
    private SamFileService samFileService;

    
    // 파일 업로드를 위한 메서드-------------------------------------------------------------------------------------
    @RequestMapping("/common/samFile/doUpload")
    @ResponseBody
    public ResultData doUpload(@RequestParam Map<String, Object> param, MultipartRequest multipartRequest) {
        return samFileService.saveFiles(param, multipartRequest);
    }

 
    // 파일 다운로드를 위한 메서드------------------------------------------------------------------------------------
    @GetMapping("/common/samFile/doDownload")
    public ResponseEntity<Resource> downloadFile(int id, HttpServletRequest request) throws IOException {
      
    	SamFile samFile = samFileService.getSamFile(id);
        if (samFile == null) {
            throw new SamFileNotFoundException();
        }

        //파일 경로
        String filePath = samFile.getFilePath(samFileDirPath);
        // 바디에 담길 리소스
        Resource resource = new InputStreamResource(new FileInputStream(filePath));
        // 파일의 content type 결정
        String contentType = request.getServletContext().getMimeType(new File(filePath).getAbsolutePath());

        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()	
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + samFile.getOriginFileName() + "\"")
                .contentType(MediaType.parseMediaType(contentType)).body(resource);
    }

    
    // 파일을 불러오기를 위한 메서드-----------------------------------------------------------------------------------
    @GetMapping("/common/samFile/file/{relTypeCode}/{relId}/{typeCode}/{type2Code}/{fileNo}")
    public ResponseEntity<Resource> showFile(HttpServletRequest request, @PathVariable String relTypeCode,
                                             @PathVariable int relId, @PathVariable String typeCode, @PathVariable String type2Code,
                                             @PathVariable int fileNo) throws FileNotFoundException {
        SamFile samFile = samFileService.getSamFile(relTypeCode, relId, typeCode, type2Code, fileNo);

        if (samFile == null) {
            throw new SamFileNotFoundException();
        }
        
        //파일 경로
        String filePath = samFile.getFilePath(samFileDirPath);
        // 바디에 담길 리소스
        Resource resource = new InputStreamResource(new FileInputStream(filePath));
        // 파일의 content type 결정
        String contentType = request.getServletContext().getMimeType(new File(filePath).getAbsolutePath());

        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).body(resource);
    }
    
    
}
