package com.ezen.samsamoo.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

// 유용하게 쓸 수 있는 함수들의 모음임돠
public class Util {
	
	// 현재 날짜와 시간
    public static String getNowDateStr() {
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date time = new Date();
        return format1.format(time);
    }

    
    // 맵으로 한꺼번에 많은 정보 다 때려담을 때 쓰는 함수.
    public static Map<String, Object> mapOf(Object... args) {
        if (args.length % 2 != 0) {
            throw new IllegalArgumentException("인자를 짝수개 입력해주세요.");
        }

        int size = args.length / 2;

        Map<String, Object> map = new LinkedHashMap<>();

        for (int i = 0; i < size; i++) {
            int keyIndex = i * 2;
            int valueIndex = keyIndex + 1;

            String key;
            Object value;

            try {
                key = (String) args[keyIndex];
            } catch (ClassCastException e) {
                throw new IllegalArgumentException("키는 String으로 입력해야 합니다. " + e.getMessage());
            }

            value = args[valueIndex];

            map.put(key, value);
        }

        return map;
    }

    
    // 정수값으로 변환해주는 함수
    public static int getAsInt(Object object, int defaultValue) {
        if (object instanceof BigInteger) {
            return ((BigInteger) object).intValue();
        } else if (object instanceof Double) {
            return (int) Math.floor((double) object);
        } else if (object instanceof Float) {
            return (int) Math.floor((float) object);
        } else if (object instanceof Long) {
            return (int) object;
        } else if (object instanceof Integer) {
            return (int) object;
        } else if (object instanceof String) {
            return Integer.parseInt((String) object);
        }

        return defaultValue;
    }

    
    // 경고창으로 메시지를 보냄과 동시에 바로 뒤 페이지로 이동하도록 해주는 함수1
    public static String msgAndBack(String msg) {
        StringBuilder sb = new StringBuilder();
        sb.append("<script>");
        sb.append("alert('" + msg + "');");
        sb.append("history.back();");
        sb.append("</script>");

        return sb.toString();
    }
    
    // 경고창으로 메시지를 보냄과 동시에 바로 뒤 페이지로 이동하도록 해주는 함수2
    public static String msgAndBack(HttpServletRequest req, String msg) {
        req.setAttribute("msg", msg);
        req.setAttribute("historyBack", true);
        return "common/redirect";
    }

    
    // 경고창으로 메시지를 보냄과 동시에 새로운 주소로 이동하도록 해주는 함수1
    public static String msgAndReplace(String msg, String uri) {
        StringBuilder sb = new StringBuilder();
        sb.append("<script>");
        sb.append("alert('" + msg + "');");
        sb.append("location.replace('" + uri + "');");
        sb.append("</script>");

        return sb.toString();
    }

    
    // 경고창으로 메시지를 보냄과 동시에 새로운 주소로 이동하도록 해주는 함수2
    public static String msgAndReplace(HttpServletRequest req, String msg, String replaceUri) {
        req.setAttribute("msg", msg);
        req.setAttribute("replaceUri", replaceUri);
        return "common/redirect";
    }

 
    // 자바 오브젝트를 제이슨 문자열로 바꿔주는 감사한 함수... 고오맙습니다..
    public static String toJsonStr(Object obj) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return "";
    }

    
    // 맵에 담긴 이것저것 여러 잡것들을 전부 제이슨 문자열로 바꿔주는 함수.. 거지같은 설명 ㅈㅅ
    public static String toJsonStr(Map<String, Object> param) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(param);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return "";
    }

    
    // 클라이언트로부터 요청받은 값들을 키 밸류 나눠서 맵 형태로 받아줌
    public static Map<String, String> getParamMap(HttpServletRequest request) {
        Map<String, String> param = new HashMap<>();

        Enumeration<String> parameterNames = request.getParameterNames();

        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            String paramValue = request.getParameter(paramName);

            param.put(paramName, paramValue);
        }

        return param;
    }

    
    // uri 주소를 UTF-8로 인코딩해주는 함수
    public static String getUriEncoded(String str) {
        try {
            return URLEncoder.encode(str, "UTF-8");
        } catch (Exception e) {
            return str;
        }
    }

    
    // Null인지 확인해서 널이 아니면 그 데이터 그대로, 널이면 defaultValue를 반환
    public static <T> T ifNull(T data, T defaultValue) {
        return data != null ? data : defaultValue;
    }

    
    // 만약 Null일 경우 defaultValue를, 그렇지 않으면 요청받은 값을 가진다.
    public static <T> T reqAttr(HttpServletRequest req, String attrName, T defaultValue) {
        return (T) ifNull(req.getAttribute(attrName), defaultValue);
    }

    
    // 공백인지 확인
    public static boolean isEmpty(Object data) {
        if (data == null) {
            return true;
        }

        if (data instanceof String) {
            String strData = (String) data;

            return strData.trim().length() == 0;
        } else if (data instanceof List) {
            List listData = (List) data;

            return listData.isEmpty();
        } else if (data instanceof Map) {
            Map mapData = (Map) data;

            return mapData.isEmpty();
        }

        return false;
    }

    
    // 공백인지 확인하여 공백일 경우 defaultValue를, 그렇지 않으면 본연의 data를 반환
    public static <T> T ifEmpty(T data, T defaultValue) {
        if (isEmpty(data)) {
            return defaultValue;
        }

        return data;
    }

    
    // 파일 네임으로 부터 확장자를 추출해서 어떤 파일타입인지 알려주는 함수(ex: img, video, audio)
    public static String getFileExtTypeCodeFromFileName(String fileName) {
        String ext = getFileExtFromFileName(fileName).toLowerCase();

        switch (ext) {
            case "jpeg":
            case "jpg":
            case "gif":
            case "png":
                return "img";
            case "mp4":
            case "avi":
            case "mov":
                return "video";
            case "mp3":
                return "audio";
        }

        return "etc";
    }

    
    // 파일 네임으로부터 확장자를 추출해서 어떤 파일타입2인지 알려주는 함수(이미지 파일에서 jpg말고는 다 ext로 분류 ㅎ. 그냥그런줄아셈,,,)
    public static String getFileExtType2CodeFromFileName(String fileName) {
        String ext = getFileExtFromFileName(fileName).toLowerCase();

        switch (ext) {
            case "jpeg":
            case "jpg":
                return "jpg";
            case "gif":
                return ext;
            case "png":
                return ext;
            case "mp4":
                return ext;
            case "mov":
                return ext;
            case "avi":
                return ext;
            case "mp3":
                return ext;
        }

        return "etc";
    }
    
    
    // 파일 네임으로 부터 확장자를 추출해주는 함수
    public static String getFileExtFromFileName(String fileName) {
        int pos = fileName.lastIndexOf(".");
        String ext = fileName.substring(pos + 1);

        return ext;
    }

    
    // 현재 년도와 월을 리턴해주는 함수
    public static String getNowYearMonthDateStr() {
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy_MM");

        String dateStr = format1.format(System.currentTimeMillis());

        return dateStr;
    }

    
    // 문자열을 divideBy 기준으로 쪼개주고 List를 생성해주는 함수
    public static List<Integer> getListDividedBy(String str, String divideBy) {
        return Arrays.asList(str.split(divideBy))
        			.stream().map(s -> Integer.parseInt(s.trim()))
        			.collect(Collectors.toList());
    }

    
    // 파일위치를 넘겨받으면 그 파일을 삭제해주는 함수
    public static boolean deleteFile(String filePath) {
        java.io.File ioFile = new java.io.File(filePath);
        if (ioFile.exists()) {
            return ioFile.delete();
        }

        return true;
    }

    
    // 파일 용량 표시할 때 천의자리수 단위로 쉼표 삽입하기 위해 쓰는 함수
    public static String numberFormat(int num) {
        DecimalFormat df = new DecimalFormat("###,###,###");

        return df.format(num);
    }

    
    // 문자열을 Integer 형식으로 바꿔주는 함수
    public static String numberFormat(String numStr) {
        return numberFormat(Integer.parseInt(numStr));
    }

    
    // 숫자로만 구성된 문자열인지 여부 판별
    public static boolean allNumberString(String str) {
        if (str == null) {
            return false;
        }

        if (str.length() == 0) {
            return true;
        }

        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i)) == false) {
                return false;
            }
        }

        return true;
    }

    
    // 숫자로 시작하는 문자열인지 여부를 판별해주는 함수
    public static boolean startsWithNumberString(String str) {
        if (str == null) {
            return false;
        }

        if (str.length() == 0) {
            return false;
        }

        return Character.isDigit(str.charAt(0));
    }

    
    // 로그인 아이디 유효성 검사.
    // 5자 이상, 20자 이하로 구성
    // 숫자로 시작 금지
    // _, 알파벳, 숫자로만 구성
    public static boolean isStandardLoginIdString(String str) {
        if (str == null) {
            return false;
        }

        if (str.length() == 0) {
            return false;
        }

        return Pattern.matches("^[a-zA-Z]{1}[a-zA-Z0-9_]{4,19}$", str);
    }

    
    // 개중요한 함수!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    // Util.getNewUriRemoved("http://www.naver.com/?search=123", "search")
    // 이렇게 된다. http://www.naver.com/?
    //Util.getNewUriRemoved("http://www.naver.com/?search=123&type=abc", "search")
    // 이렇게 된다.  http://www.naver.com/?type=abc
    public static String getNewUriRemoved(String uri, String paramName) {
        String deleteStrStarts = paramName + "=";
        int delStartPos = uri.indexOf(deleteStrStarts);

        if (delStartPos != -1) {
            int delEndPos = uri.indexOf("&", delStartPos);

            if (delEndPos != -1) {
                delEndPos++;
                uri = uri.substring(0, delStartPos) + uri.substring(delEndPos, uri.length());
            } else {
                uri = uri.substring(0, delStartPos);
            }
        }

        if (uri.charAt(uri.length() - 1) == '?') {
            uri = uri.substring(0, uri.length() - 1);
        }

        if (uri.charAt(uri.length() - 1) == '&') {
            uri = uri.substring(0, uri.length() - 1);
        }

        return uri;
    }

    
    // paramName과 paramValue를 뽑아와서 정리해 새로운 uri 만들어주기
    // getNewUri("http://www.naver.com/?search=123&type=abc&page=4", "page", 5);
    // 이렇게 된다. http://www.naver.com/?search=123&type=abc&page=5
    public static String getNewUri(String uri, String paramName, String paramValue) {
        uri = getNewUriRemoved(uri, paramName);

        if (uri.contains("?")) {
            uri += "&" + paramName + "=" + paramValue;
        } else {
            uri += "?" + paramName + "=" + paramValue;
        }

        uri = uri.replace("?&", "?");

        return uri;
    }

    
    // 위의 함수들을 통해 새롭게 만든 uri를 utf-8로 인코딩해주는 함수
    public static String getNewUriAndEncoded(String uri, String paramName, String pramValue) {
        return getUriEncoded(getNewUri(uri, paramName, pramValue));
    }



    // 임시 패스워드 만드는 함수
    public static String getTempPassword(int length) {
        int index = 0;
        char[] charArr = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < length; i++) {
            index = (int) (charArr.length * Math.random());
            sb.append(charArr[index]);
        }

        return sb.toString();
    }

    
    // 보안 해시 알고리즘
    // 해시는 문자가 아닌 바이트로 동작한다.
    // digest() -> 지금까지 전달된 데이터의 요약을 반환한다. 이것은 length 크기의 바이트열 객체이며 0에서 255까지의 전체 범위에 있는 바이트를 포함할 수 있다.
    // SHA-256은 SHA(Secure Hash Algorithm) 알고리즘의 한 종류로서 256비트로 구성되며 64자리 문자열을 반환합니다.
    // 해시 알고리즘 SHA-2 계열 중 하나이며, 2^256만큼 경우의 수를 만들 수 있다.
    // SHA-256은 단방향 암호화 방식이기 때문에 복호화가 불가능하다.
    public static String sha256(String base) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();

        } catch (Exception ex) {
            return "";
        }
    }

    
    // seconds만큼 지난 날짜와 시간을 보여주는 함수
    public static String getDateStrLater(long seconds) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String dateStr = format.format(System.currentTimeMillis() + seconds * 1000);

        return dateStr;
    }

    
    // 제이슨 문자열을 자바 오브젝트로
    public static <T> T fromJsonStr(String jsonStr, Class<T> cls) {
        ObjectMapper om = new ObjectMapper();
        try {
            return (T) om.readValue(jsonStr, cls);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    
}
