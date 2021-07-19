<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" 
			value="<span><i class='far fa-clipboard'></i></span>
						 <span>${board.name} ARTICLE WRITE</span>" />
<%@ include file="../common/head.jspf"%>



<c:set var="fileInputMaxCount" value="3" />
<script>
ArticleWrite__fileInputMaxCount = parseInt("${fileInputMaxCount}");
</script>

<script>
ArticleWrite__submited = false;
function ArticleWrite__checkAndSubmit(form) {
	if ( ArticleWrite__submited ) {
		alert('처리중입니다.');
		return;
	}
	
	form.title.value = form.title.value.trim();
	if ( form.title.value.length == 0 ) {
		alert('제목을 입력해주세요.');
		form.title.focus();
		return false;
	}
	form.body.value = form.body.value.trim();
	if ( form.body.value.length == 0 ) {
		alert('내용을 입력해주세요.');
		form.body.focus();
		return false;
	}
	var maxSizeMb = 50;
	var maxSize = maxSizeMb * 1024 * 1024;
	for ( let inputNo = 1; inputNo <= ArticleWrite__fileInputMaxCount; inputNo++ ) {
		const input = form["file__article__0__common__attachment__" + inputNo];
		
		if (input.value) {
			if (input.files[0].size > maxSize) {
				alert(maxSizeMb + "MB 이하의 파일을 업로드 해주세요.");
				input.focus();
				
				return;
			}
		}
	}
	const startSubmitForm = function(data) {
		if (data && data.body && data.body.samFileIdsStr) {
			form.samFileIdsStr.value = data.body.samFileIdsStr;
		}
		
		for ( let inputNo = 1; inputNo <= ArticleWrite__fileInputMaxCount; inputNo++ ) {
			const input = form["file__article__0__common__attachment__" + inputNo];
			input.value = '';
		}
		
		form.submit();
	};
	const startUploadFiles = function(onSuccess) {
		var needToUpload = false;
		for ( let inputNo = 1; inputNo <= ArticleWrite__fileInputMaxCount; inputNo++ ) {
			const input = form["file__article__0__common__attachment__" + inputNo];
			if ( input.value.length > 0 ) {
				needToUpload = true;
				break;
			}
		}
		
		if (needToUpload == false) {
			onSuccess();
			return;
		}
		
		var fileUploadFormData = new FormData(form);
		
		$.ajax({
			url : '/common/samFile/doUpload',
			data : fileUploadFormData,
			processData : false,
			contentType : false,
			dataType : "json",
			type : 'POST',
			success : onSuccess
		});
	}
	ArticleWrite__submited = true;
	startUploadFiles(startSubmitForm);
}
</script>


<section class="flex justify-center section-1 mx-auto" >
	<div class="bg-white shadow-md rounded container p-8 mt-8 mb-8" >
		<form onsubmit="ArticleWrite__checkAndSubmit(this); return false;" action="doWrite" method="POST" enctype="multipart/form-data">
			<input type="hidden" name="samFileIdsStr" value="" />
			<input type="hidden" name="boardId" value="${param.boardId}" />


			<div class="m-6">
				<div class="lg:flex lg:items-center lg:w-28">
					<span class="text-blue-700 mb-4">제목</span>
				</div>
				<div class="lg:flex-grow">
					<input type="text" name="title" autofocus="autofocus" class="form-row-input w-full rounded-sm mb-6" placeholder="제목을 입력해주세요." />
				</div>
				<textarea class="form-control summernote " rows="5" id="content" name="body"></textarea>
			</div>



			<c:forEach begin="1" end="${fileInputMaxCount}" var="inputNo">
				<div class="form-row flex flex-col lg:flex-row mb-4">
					<div class="lg:flex lg:items-center lg:w-28">
						<span>첨부파일 ${inputNo}</span>
					</div>
					<div class="lg:flex-grow">
						<input type="file" name="file__article__0__common__attachment__${inputNo}"
							class="form-row-input w-full rounded-sm" />
					</div>
				</div>
			</c:forEach>
			<div class="form-row flex flex-col lg:flex-row">
				<div class="lg:flex lg:items-center lg:w-28">
					<span>작성</span>
				</div>
				<div class="lg:flex-grow">
					<div class="btns">
						<input type="submit" class="btn-primary bg-blue-500 hover:bg-blue-dark text-white font-bold py-2 px-4 rounded" value="작성">
						<input onclick="history.back();" type="button" class="btn-info bg-red-500 hover:bg-red-dark text-white font-bold py-2 px-4 rounded" value="취소">
					</div>
				</div>
			</div>
		</form>
	</div>
</section>

<hr>

 <script>
      $('.summernote').summernote({
        placeholder: '내용을 입력해주세요.',
        tabsize: 2,
        height: 300,
        toolbar: [
          ['style', ['style']],
          ['font', ['bold', 'underline', 'clear']],
          ['color', ['color']],
          ['para', ['ul', 'ol', 'paragraph']],
          ['table', ['table']],
          ['view', ['fullscreen', 'codeview', 'help']]
        ]
      });
    </script>



<%@ include file="../common/foot.jspf"%>