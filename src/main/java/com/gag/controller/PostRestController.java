package com.gag.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.gag.model.Post;
import com.gag.model.User;
import com.gag.model.dao.PostDAO;
import com.gag.model.dao.SectionDAO;

@Controller
public class PostRestController {

	@RequestMapping(value = "/post/{postId}", method = RequestMethod.GET)
	@ResponseBody
	public Post getPosts(@PathVariable int postId) {
		Post p;
		try {
			p = PostDAO.POST_DAO.getPostsById(postId);
		} catch (Exception e) {
			return null;
		}
		return p;
	}

	@RequestMapping(value = "/{sectionId}", method = RequestMethod.GET)
	public String getPostsBySection(@PathVariable int sectionId, Model m) {
		try {
			m.addAttribute("posts", PostDAO.POST_DAO.getPostsBySection(sectionId));
			m.addAttribute("sections", SectionDAO.SECTION_DAO.getAll());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "index";
	}
	
	@RequestMapping(value="/upvote",method = RequestMethod.POST)
	public void upvote(@RequestParam int postId, HttpSession session) throws Exception {
        User user = (User) session.getAttribute("user");
        PostDAO.POST_DAO.votePost(user.getId(), postId, 1);      
    }
	
	@RequestMapping(value="/downvote",method = RequestMethod.POST)
	public void downvote(@RequestParam int postId, HttpSession session) throws Exception {
        User user = (User) session.getAttribute("user");
        PostDAO.POST_DAO.votePost(user.getId(), postId, -1);      
    }
	
	@RequestMapping(value = "/votes/{postId}", method = RequestMethod.GET)
	@ResponseBody
	public int getAllVotes(@PathVariable int postId) throws SQLException {
		System.out.println(postId);
		System.out.println(PostDAO.POST_DAO.getAllVotes(postId));
		return PostDAO.POST_DAO.getAllVotes(postId);
	}
}
