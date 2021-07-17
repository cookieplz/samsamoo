package com.ezen.samsamoo.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ezen.samsamoo.dto.Member;

import java.util.List;

@Mapper
public interface MemberDao {
	
	// 로그인 아이디를 통해 회원 한 명의 데이터 얻기
    public Member getMemberByLoginId(@Param("loginId") String loginId);

    // member 번호를 통해 회원 한 명의 데이터 얻기
    public Member getMemberById(@Param("id") int id);

    // 회원 이름과 이메일을 통해 회원 한명의 데이터 얻기
    public Member getMemberByNameAndEmail(@Param("name") String name, @Param("email") String email);

    // 회원들의 자세한 정보 리스트를 얻기
    public List<Member> getForPrintMembers();
    
    // 회원가입하기
    public void join(@Param("loginId") String loginId, @Param("loginPw") String loginPw, @Param("name") String name, @Param("nickname") String nickname, @Param("cellphoneNo") String cellphoneNo, @Param("email") String email);

    // 회원 수정하기
    void modify(@Param("id") int id, @Param("loginPw") String loginPw, @Param("name") String name, @Param("nickname") String nickname, @Param("cellphoneNo") String cellphoneNo, @Param("email") String email);

    // insert 이후 데이터가 들어가 auto_increment로 되어있는 PK값을 가져오기  
    public int getLastInsertId();


}
