package securecoding.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import securecoding.model.Notice;
import securecoding.repository.NoticeRepository;

@Controller
public class DashboardController {

	@Autowired
	private NoticeRepository noticeRepository;
	
	@RequestMapping(path = {"/", "/dashboard"}, method = RequestMethod.GET)
	public String getIndex() {
		return "pages/dashboard";
	}

	@RequestMapping(path = "/notices", method = RequestMethod.POST)
	public @ResponseBody Notice post(HttpServletRequest request) {
		Notice notice = new Notice(request.getParameter("title"), request.getParameter("content"));
		return noticeRepository.saveAndFlush(notice);
	}

}
