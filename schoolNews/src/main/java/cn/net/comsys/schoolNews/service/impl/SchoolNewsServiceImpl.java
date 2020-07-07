package cn.net.comsys.schoolNews.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.validator.internal.util.privilegedactions.NewInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.hutool.http.HttpUtil;
import cn.net.comsys.schoolNews.service.SchoolNewsService;


@Service
public class SchoolNewsServiceImpl implements SchoolNewsService{

	@Value("${news.content.uri}")
	private String contentUri;
	
	@Value("${news.http.uri}")
	private String newsHttpUri;
	
	@Value("${news.school.id}")
	private String schoolNewsId;
	
	@Value("${news.performance.id}")
	private String performanceId;
	
	private static SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
	
	@Autowired
	private JdbcTemplate jdbc;
	public void saveNews() {
		// TODO Auto-generated method stub
		saveDatas(this.schoolNewsId, 3);
		saveDatas(this.performanceId, 2);
	}

	/**
	 * saveDatas 保存
	 * @param id 栏目id
	 * @param state  2.学术演出抓取,3.校园新闻抓取
	 */
	public void saveDatas(String id, int state) {
		// TODO Auto-generated method stub
		String urlString  = this.newsHttpUri + "/cms/addons/pullContent";
		String dateString = sf.format(new Date());
		//获取校园新闻
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("classify_id", id);
		map.put("cmd", "channel");
		map.put("date", dateString);
		//map.put("date", "2020-01-06");
		map.put("page", "1");
		map.put("pageSize", "100");
		//System.out.println("urlString is 【"+urlString+"】, param is 【"+map.toString()+"】.");
		String newsDatas = HttpUtil.get(urlString, map);
		//System.out.println("newsDatas is 【"+newsDatas+"】");
		JSONObject object = JSONObject.parseObject(newsDatas);
		boolean op = object.getBoolean("success");
		if (op) {
			JSONArray ary = JSONArray.parseArray(object.getString("data"));
			
			for (Object object2 : ary) {
				JSONObject obj = JSONObject.parseObject(object2+"");
				String url = null;
				if ("".indexOf("http")==-1) {
					url = contentUri + obj.getString("href");
				}else {
					url = obj.getString("href");
				}
				List<Map<String, Object>> reMaps = jdbc.queryForList("SELECT ID FROM  T_GRAPE_PERFORMANCERELEASE WHERE URL = ? AND  PERFORMANCETIME = ? AND PUBULISHSTATE = '1' "
						+ "AND REFERSTATE = ?", new Object[] {url, obj.getString("publishDate"), state+""});
				if (reMaps.size()==0) {
					
					jdbc.update("INSERT INTO T_GRAPE_PERFORMANCERELEASE (ID, PERFORMANCETITLE,"
							+ "PERFORMANCETIME, REFERSTATE, URL, IMG,PUBULISHSTATE) VALUES ( PERFORMANCERELEASE_SEQUENCE.NEXTVAL, ?, ?, ?, ?, ?,'1')", new Object[] { 
									obj.getString("title"), obj.getString("publishDate"), state+"", url,
									obj.getString("picurl")});
				}else {
					jdbc.update("UPDATE T_GRAPE_PERFORMANCERELEASE SET PERFORMANCETITLE = ?, PERFORMANCETIME=? WHERE ID = ?", 
							new Object[] {obj.getString("title"), obj.getString("publishDate"), reMaps.get(0).get("ID")});
				}
				
			}
			
		}
	}
	
	
}
