package com.eunyoung.myweb.product.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.eunyoung.myweb.command.CategoryVO;
import com.eunyoung.myweb.command.ProductUploadVO;
import com.eunyoung.myweb.command.ProductVO;
import com.eunyoung.myweb.util.Criteria;

public interface ProductService {

	//글등록(파일업로드)
	public int regist(ProductVO vo, List<MultipartFile> list); 
	
	public ArrayList<ProductVO> getList(String user_id, Criteria cri); //조회: 특정회원정보만 조회
	public int getTotal(String user_id, Criteria cri);
	
	//카테고리 대분류
	public List<CategoryVO> getCategory();
	//카테고리 중분류, 소분류
	public List<CategoryVO> getCategoryChild(CategoryVO vo);
	
	//이미지데이터 조회
	public List<ProductUploadVO> getProductImg(ProductVO vo);
	
}
