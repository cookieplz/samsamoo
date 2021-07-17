package com.ezen.samsamoo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezen.samsamoo.dao.ArticleDao;
import com.ezen.samsamoo.dto.Article;
import com.ezen.samsamoo.dto.Board;
import com.ezen.samsamoo.dto.ResultData;

import java.util.List;

@Service
public class ArticleService {
	
	@Autowired
	private ArticleDao articleDao;

	
//----------------------------------------------------------------------------------------------------  	
	// 게시글이 존재하지 않는지 확인하는 함수
	private boolean isEmpty(Article article) {
		if (article == null) {
			return true;
		} else if (article.isDelStatus()) {	// 게시글이 삭제된 상태도 게시글이 존재하지 않는 것으로 취급
			return true;
		}

		return false;
	}

	
//----------------------------------------------------------------------------------------------------  
	// 게시판 번호(id)를 통해 게시판 한 개의 정보를 가져오기
	public Board getBoardById(int id) {
		return articleDao.getBoardById(id);
	}
	
	
	// 게시글 번호(id)를 통해 게시글 한 개를 가져오기
	public Article getArticleById(int id) {
		return articleDao.getArticleById(id);
	}
	
	
	// 게시글 번호(id)를 통해 게시글 한 개의 자세한 정보를 가져오기
	public Article getForPrintArticleById(int id) {
		return articleDao.getForPrintArticleById(id);
	}
		
	
//----------------------------------------------------------------------------------------------------  	
	// 게시판 내의 전체 게시글의 개수를 가져오기
	public int getArticlesTotalCount(int boardId, String searchKeywordTypeCode, String searchKeyword) {
		if (searchKeyword != null && searchKeyword.length() == 0) {
			searchKeyword = null;
		}

		return articleDao.getArticlesTotalCount(boardId, searchKeywordTypeCode, searchKeyword);
	}

	
	// 게시글들의 자세한 정보를 가져오기(ForPrint가 붙으면 자세한 정보를 가져오는 함수로 만들어놓음)
	public List<Article> getForPrintArticles(int boardId, String searchKeywordTypeCode, String searchKeyword,
			int itemsCountInAPage, int page) {
		if (searchKeyword != null && searchKeyword.length() == 0) {
			searchKeyword = null;
		}

		int limitFrom = (page - 1) * itemsCountInAPage;
		int limitTake = itemsCountInAPage;

		return articleDao.getForPrintArticles(boardId, searchKeywordTypeCode, searchKeyword, limitFrom, limitTake);
	}
	
	
//----------------------------------------------------------------------------------------------------  	
	// 게시글 쓰기
	public ResultData writeArticle(int boardId, int memberId, String title, String body) {
		articleDao.writeArticle(boardId, memberId, title, body);
		int id = articleDao.getLastInsertId();

		return new ResultData("S-1", "게시물이 작성되었습니다.", "id", id);
	}
	
	
	// 게시글 수정하기
	public ResultData modifyArticle(int id, String title, String body) {
		Article article = getArticleById(id);

		if (isEmpty(article)) {
			return new ResultData("F-1", "존재하지 않는 게시물 번호입니다.", "id", id);
		}

		articleDao.modifyArticle(id, title, body);

		return new ResultData("S-1", "게시물이 수정되었습니다.", "id", id);
	}

	
	// 게시글 삭제하기
	public ResultData deleteArticleById(int id) {
		Article article = getArticleById(id);

		if (isEmpty(article)) {
			return new ResultData("F-1", "게시물이 존재하지 않습니다.", "id", id);
		}

		articleDao.deleteArticleById(id);

		return new ResultData("S-1", id + "번 게시물이 삭제되었습니다.", "id", id, "boardId", article.getBoardId());
	}

	
//----------------------------------------------------------------------------------------------------  	
	

}
