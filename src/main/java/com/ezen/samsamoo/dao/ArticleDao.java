package com.ezen.samsamoo.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ezen.samsamoo.dto.Article;
import com.ezen.samsamoo.dto.Board;

import java.util.List;
import java.util.Map;

@Mapper
public interface ArticleDao {
	
    // 게시판 번호(id)를 통해 게시판 한 개의 정보를 가져오기
    public Board getBoardById(@Param("id") int id);
	
	// 게시글 번호(id)를 통해 게시글 한 개를 가져오기
    public Article getArticleById(@Param("id") int id);
    
    // 게시글 번호(id)를 통해 게시글 한 개의 자세한 정보를 가져오기
    public Article getForPrintArticleById(@Param("id") int id);
       
    // 게시판 내의 전체 게시글의 개수를 가져오기
    public int getArticlesTotalCount(@Param("boardId") int boardId,
                              @Param("searchKeywordTypeCode") String searchKeywordTypeCode, @Param("searchKeyword") String searchKeyword);
    
    // 게시글들의 자세한 정보를 가져오기
    public List<Article> getForPrintArticles(@Param("boardId") int boardId,
            @Param("searchKeywordTypeCode") String searchKeywordTypeCode, @Param("searchKeyword") String searchKeyword,
            @Param("limitFrom") int limitFrom, @Param("limitTake") int limitTake);
    
    // 게시글 쓰기
    public void writeArticle(Map<String, Object> param);
    
    // 게시글 수정하기
    public void modifyArticle(Map<String, Object> param);

    // 게시글 삭제하기 
    public void deleteArticleById(@Param("id") int id);
    
    // insert 이후 데이터가 들어가 auto_increment로 되어있는 PK값을 가져오기 
    public int getLastInsertId();
    
    // 게시글 조회수 1씩 증가
    public int updateHitCount(@Param("id") int id);
    
    // 게시글 좋아요 1씩 증가
    public int updateLikeCount(@Param("id") int id);
    
    // 게시글 싫어요 1씩 증가
    public int updateDislikeCount(@Param("id") int id);

}
