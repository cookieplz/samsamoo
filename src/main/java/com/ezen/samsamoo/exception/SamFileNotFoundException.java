package com.ezen.samsamoo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// ResponseStatus 어노테이션 -> 사용자에게 원하는 response code와 원인을 리턴해줄 수 있다.
// 상태코드 NOT_FOUND
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "파일을 찾을 수 없습니당..")
public class SamFileNotFoundException  extends RuntimeException {
}