 <%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="pageTitle"
		value="<span><i class='far fa-clipboard'></i></span>
					<span>${board.name} ARTICLE DETAIL </span>"/>
						
<%@ include file="../common/head.jspf"%>
<script src="https://rawgit.com/jackmoore/autosize/master/dist/autosize.min.js"></script>

<c:set var="fileInputMaxCount" value="3" />

<!-- 댓글 수정 모달 -->
<script>
function ReplyModify__showModal(el) {
    const $div = $(el).closest('[data-id]');
    const replyId = $div.attr('data-id');
    const replyBody = $div.find('.reply-body').html();
    $('.section-reply-modify [name="id"]').val(replyId);
    $('.section-reply-modify [name="body"]').val(replyBody);
    $('.section-reply-modify').css('display', 'flex');
}
function ReplyModify__hideModal() {
    $('.section-reply-modify').hide();
}
let ReplyModify__submitFormDone = false;
function ReplyModify__submitForm(form) {
    if ( ReplyModify__submitFormDone ) {
        return;
    }
    form.body.value = form.body.value.trim();
    if ( form.body.value.length == 0 ) {
        alert('내용을 입력해주세요.');
        form.body.focus();
        return;
    }
    form.submit();
    ReplyModify__submitFormDone = true;
}
</script>
<!-- 댓글 입력 시작 -->
<script>
	let ReplyWrite__submitFormDone = false;
	function ReplyWrite__submitForm(form) {
		if (ReplyWrite__submitFormDone) {
			return;
		}
		form.body.value = form.body.value.trim();
		if (form.body.value.length == 0) {
			alert('내용을 입력해주세요.');
			form.body.focus();
			return;
		}
		form.submit();
		ReplyWrite__submitFormDone = true;
	}
</script>
<script>
	function ReplyList__goToReply(id) {
		setTimeout(function() {
			const $target = $('.reply-list [data-id="' + id + '"]');
			const targetOffset = $target.offset();
			$(window).scrollTop(targetOffset.top - 50);
			$target.addClass('focus');
			setTimeout(function() {
				$target.removeClass('focus');
			}, 1000);
		}, 1000);
	}
	function ReplyList__deleteReply(btn) {
		const $clicked = $(btn);
		const $target = $clicked.closest('[data-id]');
		const id = $target.attr('data-id');
		$clicked.text('삭제중...');
		$.post('../reply/doDeleteAjax', {
			id : id
		}, function(data) {
			if (data.success) {
				$target.remove();
			} else {
				if (data.msg) {
					alert(data.msg);
				}
				$clicked.text('삭제실패!!');
			}
		}, 'json');
	}
	if (param.focusReplyId) {
		ReplyList__goToReply(param.focusReplyId);
	}
</script>



<div class="section section-article-detail">
	<div class="detail_container mx-auto px-2">
		<div class="card bordered item-bt-1-not-last-child">

			<div class="card-title">
				<a href="javascript:history.back();" class="cursor-pointer"> <i class="fas fa-arrow-left mr-4"></i>
				</a>
				<span>게시물 상세페이지</span>
			</div>

			<!-- 게시글 부분 ----------------------------------------------------------------->
			<div>
				<h1 class="title-bar-type-2 px-4">상세내용</h1>
				<div class="px-4 py-8">
					<div class="flex">
						<span>
							<span>Comments: </span>
							<span class="text-gray-400 text-light">댓글 개수 자리</span>
						</span>
						<span class="ml-3">
							<span>Views: </span>
							<span class="text-gray-400 text-light">${article.hitCount}</span>
						</span>
						<span class="ml-3">
							<span>Likes: </span>
							<span class="text-gray-400 text-light">${article.likeCount }</span>
						</span>
					</div>

					<!-- 프사 -->
					<div class="mt-4">
						<div class="mt-2">
							<img class="w-20 h-20 object-cover rounded" onerror="${article.writerProfileFallbackImgOnErrorHtmlAttr}" src="${article.writerProfileImgUri}" alt="">
						</div>
					</div>
					<!-- 게시글 정보 박스 -->
					<div class="mt-3 grid sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-3">
						<div>
							<span class="badge inline-flex items-center justify-center px-2 py-1  leading-none rounded border-0 bg-blue-600 w-20 mr-3"> 번호 </span>
							<span> ${article.id} </span>
						</div>
						<div>
							<span class="badge inline-flex items-center justify-center px-2 py-1  leading-none rounded border-0 bg-blue-600 w-20 mr-3"> 작성자 </span>
							<span> ${article.extra__writerName } </span>
						</div>
						<div>
							<span class="badge inline-flex items-center justify-center px-2 py-1  leading-none rounded border-0 bg-blue-600 w-20 mr-3"> 등록날짜 </span>
							<span> ${article.regDate} </span>
						</div>
						<div>
							<span class="badge inline-flex items-center justify-center px-2 py-1  leading-none rounded border-0 bg-blue-600 w-20 mr-3 mb-6"> 수정날짜 </span>
							<span> ${article.updateDate} </span>
						</div>
					</div>
					<hr>

					<!-- 게시글 내용 -->

					<div class="text-gray-400 w-96 mx-auto font-medium text-sm mb-7 mt-6" >
						<c:forEach begin="1" end ="${fileInputMaxCount}" var="inputNo">
							<c:set var="fileNo" value="${String.valueOf(inputNo)}" />
							<c:set var="file" value="${article.extra.file__common__attachment[fileNo]}"/>
								${file.mediaHtml }
						</c:forEach>
					</div>
					<div class="text-gray-600 font-semibold text-lg mb-2">${article.title }</div>
					<div class="gext-gray-500 font-thin text-sm mb-6">${article.body }</div>	
				</div>
			</div>

			<div class="plain-link-wrap gap-3 mt-4">
				<a href="modify?id=${article.id}" class="ml-2 text-blue-500 hover:underline">수정</a>
				 <a onclick="if ( !confirm('삭제하시겠습니까?') ) return false;" href="doDelete?id=${article.id}" class="ml-2 text-blue-500 hover:underline"> 
				 	<span>
						<i class="fas fa-trash"></i>
						 <span>삭제</span>
					</span>
				</a>
			</div>

			<!-- 댓글 수정 모달~(히든 속성)-->
			<div class="section section-reply-modify hidden">
				<div>
					<div class="container mx-auto">
						<form method="POST" enctype="multipart/form-data" action="../reply/doModify" onsubmit="ReplyModify__submitForm(this); return false;">
							<input type="hidden" name="id" value="" />
							<input type="hidden" name="redirectUri" value="${rq.currentUri}" />
							<div class="form-control">
								<label class="label"> 내용 </label>
								<textarea class="textarea textarea-bordered w-full h-24" placeholder="내용을 입력해주세요." name="body" maxlength="2000"></textarea>
							</div>
							<div class="mt-4 btn-wrap gap-1">
								<button type="submit" href="#" class="btn btn-primary btn-sm mb-1">
									<span>
										<i class="far fa-edit"></i>
									</span>
									&nbsp;
									<span>수정</span>
								</button>
								<button type="button" onclick="history.back();" class="btn btn-sm mb-1" title="닫기">
									<span>
										<i class="fas fa-list"></i>
									</span>
									&nbsp;
									<span>닫기</span>
								</button>
							</div>
						</form>
					</div>
				</div>
			</div>
			<hr>
			<div>
				<h1 class="title-bar-type-2 px-4 mt-6">댓글</h1>
				<c:if test="${rq.notLogined}">
					<div class="text-center py-4">
						글 작성은 <a class="plain-link" href="${rq.loginPageUri }">로그인</a>후 이용할 수 있습니다.
					</div>
				</c:if>
				<c:if test="${rq.logined}">
					<div class="flex justify-left w-full mx-auto px-4">
						<form enctype="multipart/form-data" method="POST" action="../reply/doWrite" class="relative flex py-4 text-gray-600 focus-within:text-gray-400"
							onsubmit="ReplyWrite__submitForm(this); return false;">
							<input type="hidden" name="relTypeCode" value="article" />
							<input type="hidden" name="relId" value="${article.id}" />
							<input type="hidden" name="redirectUri" value="${rq.currentUri}" />
							<img class="w-10 h-10 object-cover rounded-full shadow mr-2 cursor-pointer" onerror="${article.writerProfileFallbackImgOnErrorHtmlAttr}" src="${article.writerProfileImgUri}" alt="User avatar"">
							<span class="absolute inset-y-0 right-0 flex items-center pr-6">
								<button type="submit" class="p-1 focus:outline-none focus:shadow-none hover:text-blue-500">
									<i class="fas fa-pen"></i>
								</button>
							</span>
							<textarea name="body"
								class="flex h-48 py-2 pl-4 pr-10 text-sm bg-gray-100 border border-transparent appearance-none rounded-tg placeholder-gray-400 focus:bg-white focus:outline-none focus:border-blue-500 focus:text-gray-900 focus:shadow-outline-blue"
								style="width: 400px; border-radius: 25px; resize: none" placeholder="댓글을 입력해주세요."></textarea>
						</form>
					</div>
				</c:if>


				<div class="reply-list ">
					<c:forEach items="${replies}" var="reply">
						<div data-id="${reply.id}" class="reply_list_one py-5 px-4 w-96">
							<script type="text/x-template" class="reply-body hidden">${reply.body}</script>
							<div class="flex">
								<!-- 아바타 이미지 -->
								<div class="flex-shrink-0">
									<img class="w-10 h-10 object-cover rounded-full shadow mr-2 cursor-pointer" onerror="${article.writerProfileFallbackImgOnErrorHtmlAttr}" src="${article.writerProfileImgUri}" alt="User avatar"">
								</div>
								<div class="flex-grow px-1">
									<div class="flex text-gray-400 text-light text-sm">
										<span>${reply.extra__writerName}</span>
										<span class="mx-1">·</span>
										<span>${reply.updateDate}</span>
									</div>
									<div class="break-all">${reply.bodyForPrint}</div>
								</div>
							</div>
							<div class="plain-link-wrap gap-3 mt-3 pl-14">
								<c:if test="${reply.memberId == rq.loginedMemberId}">
									<button onclick="if ( confirm('정말 삭제하시겠습니까?') ) { ReplyList__deleteReply(this); } return false;" class="plain-link"> 
										<span>
											<i class="fas fa-trash-alt"></i>
										</span> <span>글 삭제</span>
									</button>
								</c:if>
								<c:if test="${reply.memberId == rq.loginedMemberId}">
									<button onclick="ReplyModify__showModal(this);" class="plain-link ml-3">
										<span>
											<i class="far fa-edit"></i>
										</span>
										<span>글 수정</span>
									</button>
								</c:if>
							</div>
						</div>
					</c:forEach>
				</div>
			</div>
		</div>
	</div>
</div>


<script>
	autosize($("textArea"));
</script>
<%@ include file="../common/foot.jspf"%>