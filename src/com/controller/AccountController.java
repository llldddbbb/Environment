package com.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.entity.Account;
import com.entity.City;
import com.service.AccountService;
import com.service.CityService;
import com.service.DeviceInfoService;
import com.util.MD5Util;
import com.util.ResponseUtil;
import com.util.StringUtil;

@Controller
@RequestMapping("/account")
public class AccountController {
	
	@Resource
	private AccountService accountService;
	
	@Resource
	private CityService cityService;
	
	@Resource
	private DeviceInfoService deviceInfoService;
	
	@RequestMapping("/login")
	public String login(Account account,@RequestParam(value="language",required=false)String language,HttpServletRequest request,HttpServletResponse response)throws Exception{
		HttpSession session=request.getSession();
		account.setPassword(MD5Util.getMD5(account.getPassword()));
		Account resultAccount=accountService.login(account);
		JSONObject result=new JSONObject();
		if(resultAccount==null){
			result.put("success", false);
			result.put("error", "用户名或密码错误");
		}else{
			session.setAttribute("currentAccount", resultAccount);
			
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("cityID", resultAccount.getPlace());
			List<City> cityList=cityService.findCityList(map);
			boolean isExistTable=checkTable(cityList);
			if(!isExistTable){
				result.put("success", false);
				result.put("error", "数据库表不存在");
			}else{
				Cookie cookie = new Cookie("account", resultAccount.getUsername()+"-"+resultAccount.getPassword());
				cookie.setMaxAge(resultAccount.getValidTime());
				cookie.setPath("/");
				response.addCookie(cookie);
				result.put("cityID", resultAccount.getPlace());
				result.put("success", true);
			}
		}
		if(StringUtil.isNotEmpty(language)){
			session.setAttribute("language", "English");
		}
		ResponseUtil.write(result, response);
		return null;
	}
	
	
	@RequestMapping("/logout")
	public String logout(HttpServletRequest request,HttpServletResponse response){
		HttpSession session=request.getSession();
		String language=(String)session.getAttribute("language");
		String goLogin=((Account)session.getAttribute("currentAccount")).getUsername();
		if(StringUtil.isEmpty(goLogin)){
			goLogin="a.jsp";
		}else{
			goLogin=goLogin+".jsp";
		}
		session.invalidate();
		HttpSession session2=request.getSession();
		session2.setAttribute("goLogin", goLogin);
		Cookie cookie=new Cookie("account",null);
		cookie.setPath("/");
		cookie.setMaxAge(0);
		response.addCookie(cookie);
		if(language==null||language.equals("Chinese")){
			return "redirect:/login/"+goLogin;
		}else{
			return "redirect:/en/login/"+goLogin;
		}
	}
	
	
	@RequestMapping("/login_vistors")
	public String login_vistors(HttpServletRequest request){
		Account vistor=new Account();
		vistor.setPlace(1+"");
		HttpSession session=request.getSession();
		session.setAttribute("currentAccount", vistor);
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("cityID", vistor.getPlace());
		List<City> cityList=cityService.findCityList(map);
		boolean isExistTable=checkTable(cityList);
		if(!isExistTable){
			session.setAttribute("tableError", "数据库表不存在");
			return "redirect:/login/a.jsp";
		}else{
			return "city/list.do?cityID=1";
		}
	}
	
	@RequestMapping("/checkLogin")
	public String checkLogin(HttpServletRequest request){
		HttpSession session=request.getSession();
		Cookie[] cookies=request.getCookies();
		String goLogin=(String)session.getAttribute("goLogin");
		if(StringUtil.isEmpty(goLogin)){
			goLogin="a.jsp";
		}
		if(cookies==null){
			return "redirect:/login/"+goLogin;
		}
		String userName = null;
		String password = null;
		for(Cookie c:cookies){
			if(c.getName().equals("account")){
				String userNameAndPassword=c.getValue();
				userName=userNameAndPassword.split("-")[0];
				password=userNameAndPassword.split("-")[1];
				break;
			}
		}
		if(userName==null||password==null){
			return "redirect:/login/"+goLogin;
		}
		Account account=new Account(userName,password);
		Account resultAccount=accountService.login(account);
		if(resultAccount==null){
			return "redirect:/login/"+goLogin;
		}
		session.setAttribute("currentAccount", resultAccount);
		return "redirect:/city/list.do?view=list&cityID="+resultAccount.getPlace();
	}
	
	private boolean checkTable(List<City> cityList){
		boolean isExistTable=true;
		for(City c:cityList){
			Map<String,Object> m=new HashMap<String,Object>();
			//特殊处理
			if("dev9002a170100001".equals(c.getDeviceInfo().getDeviceName())||"dev9002a170100002".equals(c.getDeviceInfo().getDeviceName())||"dev9002a170100003".equals(c.getDeviceInfo().getDeviceName())||"dev9002a170100003".equals(c.getDeviceInfo().getDeviceName())||"dev9002a170100004".equals(c.getDeviceInfo().getDeviceName())||"dev9002a170100005".equals(c.getDeviceInfo().getDeviceName())||"dev9002a170100006".equals(c.getDeviceInfo().getDeviceName())){
				m.put("table_name", c.getDeviceInfo().getDeviceName()+"_original");
			}else{
				m.put("table_name", c.getDeviceInfo().getDeviceName()+"_1h");
			}
			List<String> table_name=deviceInfoService.checkTableExist(m);
			if(table_name.size()==0){
				isExistTable=false;
				break;
			}
		}
		return isExistTable;
	}
	
	@RequestMapping("/changeLanguage")
	public String changeLanguage(String fromPageName,String language,HttpServletRequest request){
		HttpSession session=request.getSession();
		if(language.equals("English")){
			session.setAttribute("language", "English");
			return "redirect:/en/"+fromPageName+".jsp";
		}else{
			session.setAttribute("language", "Chinese");
			return "redirect:/"+fromPageName+".jsp";
		}
	}
	
	
}
