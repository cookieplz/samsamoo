package com.ezen.samsamoo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public
class Board {
	private int id;	//  게시판 번호
	private String regDate;	// 작성날짜
	private String updateDate;	// 수정날짜
	private String code;	//코드	
	private String name;		//이름	
    private boolean delStatus;	// 블라인드 여부
    private String delDate;		// 블라이드 날짜
    private int hitCount;	//	조회수
    private int repliesCount;	//	댓글수
    private int likeCount;	// 좋아요수
    private int dislikeCount;	// 싫어요수
}