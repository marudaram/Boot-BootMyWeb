package com.eunyoung.myweb.product.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.eunyoung.myweb.command.CategoryVO;
import com.eunyoung.myweb.command.ProductVO;
import com.eunyoung.myweb.util.Criteria;

@Mapper //반드시 선언
public interface ProductMapper {

	public int regist(ProductVO vo);
	//매개변수로 전달되는 데이터가 2개 이상이면 이름붙이기
	public ArrayList<ProductVO> getList(@Param("user_id") String user_id, 
										@Param ("cri") Criteria cri); //조회: 특정회원
	public int getTotal(@Param("user_id") String user_id, 
						@Param("cri") Criteria cri);
	
	//카테고리 대분류
	public List<CategoryVO> getCategory();
	//카테고리 중분류, 소분류
	public List<CategoryVO> getCategoryChild(CategoryVO vo);

}
