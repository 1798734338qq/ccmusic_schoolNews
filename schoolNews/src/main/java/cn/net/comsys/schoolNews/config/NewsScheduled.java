package cn.net.comsys.schoolNews.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import cn.net.comsys.schoolNews.service.SchoolNewsService;

@Configuration
@EnableScheduling
public class NewsScheduled {

	@Autowired
	private SchoolNewsService service;
	
	@Scheduled(fixedRate=60000)
	public void task() {
		service.saveNews();
	}
}
