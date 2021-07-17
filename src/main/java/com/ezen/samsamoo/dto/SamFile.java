package com.ezen.samsamoo.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
// 테이블명을 File명으로 하면 자바 기본패키지에 File이라는 게 들어있기 때문에 객체생성할 때 곤란해지기 때문에 SamFile로
public class SamFile {
    private int id;	// 번호
    private String regDate;	// 작성 날짜
    private String updateDate;	// 수정 날짜
    private String delDate;	// 삭제 날짜
    private boolean delStatus;	// 삭제 상태 (0: 미삭제, 1: 삭제)
    private String relTypeCode;	// 관련 데이터 타입(ex: article, member)
    private int relId;	// 관련 데이터 번호(ex: 게시물 번호)
    private String originFileName;		// 업로드 당시의 파일 이름
    private String fileExt;	// 파일 확장자 
    private String typeCode;	// 카테고리(종류) 코드	(ex: common, article, member)
    private String type2Code;	//카테고리(종류) 코드2 (ex: 첨부파일)
    private int fileSize;	// 파일 사이즈
    private String fileExtTypeCode;	// 파일규격코드 (ex: img, video)
    private String fileExtType2Code;		// 파일규격코드2	(jpg, jpeg, png, bmp)  
    private int fileNo;	// 파일번호. 예컨대 typeCode(게시물)의 typeCode(첨부파일)의 1번 첨부파일을 가져올 때 '1번'을 담당
    private String fileDir;	// 파일이 저장되는 폴더 이름

    
    // 사용 예시 쿼리
    // SELECT * FROM samFile 
    //	WHERE relTypeCode = 'article'  AND relId = 1 AND typeCode = 'common' AND type2Code = 'attachment' AND fileNo = 1
    //	참고로 DB 인덱스를 아래와 같이 설계했음
    // KEY relId (relId, relTypeCode, typeCode, type2Code, fileNo)
    								

    // 파일 저장경로를 가져오기 위한 메서드(전체경로)
    @JsonIgnore
    public String getFilePath(String samFileDirPath) {
        return samFileDirPath + getBaseFileUri();
    }

    
    // 파일 저장경로를 가져오기 위한 메서드
    @JsonIgnore
    private String getBaseFileUri() {
        return "/" + relTypeCode + "/" + fileDir + "/" + getFileName();
    }
    
    
    // 파일 이름 가져오기 -> id.확장자
    public String getFileName() {
        return id + "." + fileExt;
    }

    
    // 파일의 url 가져오기 예컨대 ${thumbFile.getForPrintUrl()} 와 같이 첨부파일의 섬네일 img의 url을 가져올 때 쓸 수 있음
    public String getForPrintUrl() {
        return "/sam" + getBaseFileUri() + "?updateDate=" + updateDate;
    }

    
    // 파일 다운로드 url가져오기
    public String getDownloadUrl() {
        return "/common/samFile/doDownload?id=" + id;
    }

    
    // 이미지 파일을 html에 삽입하기 위한 메서드 
    public String getMediaHtml() {
        return "<img src=\"" + getForPrintUrl() + "\">";
    }
    
}
