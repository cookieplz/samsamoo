<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="pageTitle"
	value="<span><i class='far fa-clipboard'></i></span> <span>${board.name} ARTICLE LIST</span>" />

<%@ include file="../common/head.jspf"%>

<!-- 자바스크립트 정리 -->
<script>
<!-- 검색어타입 옵션 선택창을 위한 함수-->
	const param__searchKeywordType = '${param.searchKeywordType}';
	if (param__searchKeywordType.length > 0) {
		$('.search-form-box form [name="searchKeywordType"]').val(
				'${param.searchKeywordType}');
	}
</script>


<!-- 게시물 검색 폼 -->
<div class="section section-article-search">
	<div class="container mx-auto">
		<div class="search-form-box">
			<div class="card boardered">
				<div class="card-title">
					<a href="javascript:history.back();" class="cursor-pointer">
						<i class="fas fa-arrow-left mr-4"></i>	
					</a>
					<span>게시물 리스트</span>
				</div>
				<form action="" class="grid justify-items-center gap-2 px-4 py-4 ">
					<input type="hidden" name="boardId" value="${board.id }" />
					
					<!-- 검색 옵션 선택창 -->
					<div class="form flex justify-center">
						<label class="label float-left" >
							<span class="label-text mr-3"><strong>옵션</strong></span>
						</label>
						<select class="select select-bordered w-96" name="searchKeywordType">
							<option value="titleAndBody">제목+내용</option>
							<option value="title">제목</option>
							<option value="body">내용</option>
						</select>
					</div>
					<!-- 검색어 입력 인풋 박스 -->
					<div class="form flex justify-center">
						<label class="label float-left">
							<span class="label-text mr-3"><strong>제목</strong></span>
						</label>
						<input type="text" class="input input-bordered w-96" value="${param.searchKeyword}"
						name="searchKeyword"  placeholder="검색어를 입력해주세요 (10자 이내)" maxlength="10" />
					</div>
					<!--폼 데이터 전송을 위한 검색버튼 -->
					<div class="form flex justify-center mt-4">
						<input type="submit" class="btn bg-blueGray-500 text-white active:bg-blueGray-600 ml-6 w-28" value="검색"/>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>


<!-- 게시물 리스트 -->
<div class="section section-article-list mt-8">
	<div class="container mx-auto">
		<div class="articles">
			<div class="card bordered">
				<div class="card-title">
					<a href="#" class="cursor-pointer"> <i class="fas fa-list"></i>
					</a>
					<span>게시물 리스트</span>
				</div>
			</div>
			<div class="grid gap-3 px-4 pt-4">
				<div class="total-items">
					<span class="badge inline-flex items-center justify-center px-2 py-1  leading-none rounded border-0 bg-green-600 w-28 mr-3">전체 게시글 수 </span>
					<span>${totalItemsCount }</span>
				</div>
				<div class="total-pages">
					<span class="badge inline-flex items-center justify-center px-2 py-1  leading-none rounded border-0 bg-green-600 w-28 mr-3">전체 페이지 수 </span>
					<span>${totalPage}</span>
				</div>
				<div class="page">
					<span class="badge inline-flex items-center justify-center px-2 py-1  leading-none rounded border-0 bg-green-600 w-28 mr-3">현재 페이지</span>
					<span>${page}</span>
				</div>
				<hr />
				<div class="plain-link-wrap gap-3 flex justify-center mt-3 mb-3">
					<a href="write?boardId=${board.id}" class="plain-link">
						<button class="btn bg-green-600 text-white active:bg-green-900 border-0  ml-6 w-28">
							<span>
								<i class="fas fa-edit mr-3"></i>
							</span>
							<strong>글쓰기</strong>
						</button>
					</a>
				</div>
				<hr />
			</div>


			<div class="item-bt-1-not-last-child">
				<c:forEach items="${articles}" var="article">
					<c:set var="detailUri" value="../article/detail?id=${article.id}" />
					<div class="px-4 py-8">

						<div class="grid gap-3" style="grid-template-columns: 100px 1fr;">
							<a href="${detail.Uri}"> <img class="w-full object-cover" onerror="${article.writerProfileFallbackImgOnErrorHtmlAttr}" src="${article.writerProfileImgUri}" alt="" />
							</a>
							<div class="m-2">
								<a href="${detailUri }" class="no-underline cursor-pointer"> 
									<span class="badge badge-outline">제목</span>
									<div class="line-clamp-3">${article.title}</div>
								</a> 
								<a href="${degailUri }" class="no-underline cursor-pointer"> 
									<span class="badge badge-outline">본문</span>
									<div class="line-clamp-3">${article.body }</div>
								</a>
							</div>
						</div>

						<div class="mt-3 grid sm:grid-cols-2 xl:grid-cols-4 gap-3">
							<a href="${detailUri}" class="no-underline cursor-pointer"> <span class="badge inline-flex items-center justify-center px-2 py-1 leading-none rounded border-0 bg-gray-400">번호</span> <span>${article.id }</span>
							</a> <a href="${detailUri }" class="no-underline cursor-pointer"> <span class="badge inline-flex items-center justify-center px-2 py-1 leading-none rounded border-0 bg-gray-400">작성자</span> <span>${article.extra__writerName }</span>
							</a> <a href="${detailUri}" class="no-underline cursor-pointer"> <span class="badge inline-flex items-center justify-center px-2 py-1 leading-none rounded border-0 bg-gray-400">등록날짜</span> <span
									class="text-gray-600 text-light">${article.regDate }</span>
							</a> <a href="${detailUri}" class="no-underline cursor-pointer"> <span class="badge inline-flex items-center justify-center px-2 py-1 leading-none rounded border-0 bg-gray-400">수정날짜</span> <span
									class="text-gray-600 text-light">${article.updateDate }</span>
							</a>
						</div>
					</div>
				</c:forEach>
			</div>


















			<%@ include file="../common/foot.jspf"%>