<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<script>
<!-- 경고 메시지 보내는 함수-->
const alertMsg = '${msg}'.trim();

if(alertMsg){
	alert(alertMsg);
}


<!-- 세션 기록의 바로 뒤 페이지로 이동하도록 지시하는 함수 -->
const historyBack = '${historyBack}' == 'true';

if(historyBack){
	history.back();
}


<!-- 기존의 페이지를 새로운 페이지로 변경시키는 함수 -->
const replaceUri = '${replaceUri}'.trim();

if(replaceUri){
	location.replace(replaceUri);
}
</script>