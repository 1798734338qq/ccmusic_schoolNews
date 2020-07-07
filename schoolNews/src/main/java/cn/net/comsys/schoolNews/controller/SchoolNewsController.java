package cn.net.comsys.schoolNews.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.net.comsys.schoolNews.service.SchoolNewsService;

@RestController
public class SchoolNewsController {

	@Autowired
	private SchoolNewsService service;
	/**
	 * saveNews 定时抓取新闻保存
	 * @return
	 */
	@RequestMapping(path="/saveNews", method= {RequestMethod.GET, RequestMethod.POST})
	public boolean saveNews() {
		boolean state = false;
		try {
			service.saveNews();
			state = true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			state = false;
		}
		return state;
	}
}
