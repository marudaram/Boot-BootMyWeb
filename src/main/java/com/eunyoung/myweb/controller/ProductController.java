package com.eunyoung.myweb.controller;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.eunyoung.myweb.command.ProductVO;
import com.eunyoung.myweb.product.service.ProductService;
import com.eunyoung.myweb.util.Criteria;
import com.eunyoung.myweb.util.PageVO;

@Controller
@RequestMapping("/product")
public class ProductController {
	
	@Autowired
	@Qualifier("productService")
	public ProductService productService;
	

	@GetMapping("/productReg")
	public String reg() {
		return "product/productReg";
	}
	
	
	@GetMapping("/productList")
	public String list(HttpSession session, /* , HttpServletRequest request */
					   Model model,
					   Criteria cri) {
		
		
		//프로세스
		//사용할 데이터가 없어서 admin이라고 가정
		session.setAttribute("user_id", "admin"); //user_id라는 이름으로 admin을 넣음
		
		//로그인한 회원만 조회
		
		System.out.println(cri.toString());
		
		String user_id = (String)session.getAttribute("user_id");
		
		ArrayList<ProductVO> list = productService.getList(user_id, cri);
		model.addAttribute("list", list);
		
		//페이지네이션 처리
		int total = productService.getTotal(user_id, cri);
		PageVO pageVO = new PageVO(cri, total);
		model.addAttribute("pageVO", pageVO);
		
//		System.out.println(pageVO.toString());
		
		return "product/productList";
	}
	
	
	@GetMapping("/productDetail")
	public String detail() {
		return "product/productDetail";
	}
	
	
	
	
	
	//등록요청
	@PostMapping("/registForm")
	public String registForm(ProductVO vo, //@Valid -> 유효성 검사 여기선 생략
							 RedirectAttributes ra,
							 @RequestParam("file") List<MultipartFile> list) { 
		
		
		//리스트에서 빈값 제거하기
		list = list.stream().filter( (x) -> x.isEmpty() == false ).collect(Collectors.toList());
		
		//확장자가 image라면 경고문 
		for(MultipartFile file : list) {
			
			if(file.getContentType().contains("image") == false) {
				ra.addFlashAttribute("msg", "png, jpg, jpeg형식만 등록 가능합니다");
				return "redirect:/product/productReg";
			}
		}
		
		//파일 업로드 작업 -> service 영역으로 위임
		//글 등록
		int result = productService.regist(vo, list);
		
		String msg = result == 1 ? "정상 입력되었습니다" : "등록에 실패했습니다";
		ra.addFlashAttribute("msg", msg);
		
		return "redirect:/product/productList"; //목록으로
	}
	
	@ResponseBody
	@GetMapping("/xxx")
	public String xxx() {
		return "경로";
	}
	
	
	
	
	
}
