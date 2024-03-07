package com.example.demo.web.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.common.FlashData;
import com.example.demo.entity.OrderDetail;
import com.example.demo.service.BaseService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/orderdetails")
public class OrderDetailsController {
	@Autowired
	BaseService<OrderDetail> orderdetailService;


	 /* 新規作成画面表示
	 */
	@GetMapping(value = "/create/{id}")
	public String form(@PathVariable Integer id, OrderDetail orderdetail, Model model) {
		model.addAttribute("OrderDetail", orderdetail);
		model.addAttribute("id", id);
		return "admin/orderdetails/create";
	}
	
	/*
	 * 新規登録
	 */
	@PostMapping(value = "/create/{order_id}")
	public String register(@PathVariable Integer order_id, @Valid OrderDetail orderDetail, BindingResult result, Model model, RedirectAttributes ra) {
		model.addAttribute("id", order_id);
		FlashData flash;
		try {
			if (result.hasErrors()) {
				return "admin/orderdetails/create";
			}
			// 新規登録
			orderdetailService.save(orderDetail);
			flash = new FlashData().success("新規作成しました");
		} catch (Exception e) {
			flash = new FlashData().danger("処理中にエラーが発生しました");
		}
		ra.addFlashAttribute("flash", flash);
		return "redirect:/admin/orders";
	}
	
	/*
	 * 編集
	 */
	/*
	 * 編集画面表示
	 */
	@GetMapping(value = "/edit/{id}")
	public String edit(@PathVariable Integer id, Model model, RedirectAttributes ra) {
		model.addAttribute("id", id);
		try {
			// 存在確認
			OrderDetail orderdetail = orderdetailService.findById(id);
			model.addAttribute("orderDetail", orderdetail);
		} catch (Exception e) {
			FlashData flash = new FlashData().danger("該当データがありません");
			ra.addFlashAttribute("flash", flash);
			return "redirect:/admin/orders";
		}
		return "admin/orderdetails/edit";
	}
	
	/*
	 * 更新
	 */
	@PostMapping(value = "/edit/{order_id}")
	public String update(@PathVariable Integer order_id, @Valid OrderDetail orderDetail, BindingResult result, Model model, RedirectAttributes ra) {
		model.addAttribute("id", order_id);
		FlashData flash;
		try {
			if (result.hasErrors()) {
				return "admin/orderdetails/edit";
			}
			
			OrderDetail OrderDetails = orderdetailService.findById(order_id);
			// 更新
			OrderDetails.setProduct(orderDetail.getProduct());
			OrderDetails.setUnit(orderDetail.getUnit());
			orderdetailService.save(OrderDetails);
			flash = new FlashData().success("更新しました");
		} catch (Exception e) {
			flash = new FlashData().danger("該当データがありません");
		}
		ra.addFlashAttribute("flash", flash);
		return "redirect:/admin/orders";
	}
}
