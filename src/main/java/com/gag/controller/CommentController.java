package com.gag.controller;

import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.gag.controller.manager.CommentManager;
import com.gag.model.Comment;
import com.gag.model.Post;
import com.gag.model.User;
import com.gag.model.dao.CommentDAO;
import com.gag.model.dao.UserDAO;

@Controller
public class CommentController {

	@RequestMapping(value = "/addComment", method = RequestMethod.POST)
	public String comment(Model model, HttpServletRequest req, HttpSession session) {
		try {
			if (session.getAttribute("user") == null) {
				model.addAttribute("error", "We don't know who you are. Please, login first.");
				return "login";
			}
			Post post = (Post) req.getAttribute("post");
			User user = UserDAO.USER_DAO.getUserByUsername((String)req.getSession().getAttribute("username"));
			String content = (String)req.getAttribute("content");
			Comment com = new Comment(post, user, content);
			CommentManager.COMMENT_MANAGER.writeComment(post, com);
			return "index";
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
			return "index";
		}
	}
	
	@RequestMapping(value = "/deleteComment", method = RequestMethod.DELETE)
	public String deleteComment(Model model, HttpServletRequest req, HttpSession session) {
		try {
			if (session.getAttribute("user") == null) {
				model.addAttribute("error", "We don't know who you are. Please, login first.");
				return "login";
			}
			Comment comment = (Comment) req.getAttribute("comment");
			CommentManager.COMMENT_MANAGER.removeComment(comment);
			return "index";
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
			return "index";
		}
	}
	
	@RequestMapping(value = "/voteComment", method = RequestMethod.POST)
	public String voteComment(Model model, HttpServletRequest request, HttpSession session) {
		try {
			if (session.getAttribute("user") == null) {
				model.addAttribute("error", "We don't know who you are. Please, login first.");
				return "login";
			}
			User user = UserDAO.USER_DAO.getUserByUsername((String) session.getAttribute("username"));
			Comment comment = (Comment) request.getAttribute("comment");
			CommentDAO.COMMENT_DAO.voteComment(user, comment, (int) request.getAttribute("vote"));
			return "index";
		} catch (SQLException e) {
			model.addAttribute("error", e.getMessage());
			return "index";
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
			return "index";
		}
	}
	
	@RequestMapping(value = "/changeComment", method = RequestMethod.POST)
	public String changeComment(Model model, HttpServletRequest request, HttpSession session) {
		try {
			if (session.getAttribute("user") == null) {
				model.addAttribute("error", "We don't know who you are. Please, login first.");
				return "login";
			}
			Comment com = (Comment) request.getAttribute("comment");
			String content = request.getParameter("content");
			com.setContent(content);
			CommentManager.COMMENT_MANAGER.changeComment(com);
			return "index";
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
			return "index";
		}
	}
}
