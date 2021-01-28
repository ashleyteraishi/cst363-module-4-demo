package edu.cst363;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@Controller
public class MyController {
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	/* 
	 * do a review search 
	 * enter the url   /search?name=NNN&item=TTT
	 * examples:  list all review by bob on korean food       /search?name=bob&item=korean food
	 *            list all reviews on korean food             /search?name=&item=korean food              
	 *            list all reviews by bob                     /search?name=bob&item=    
	 *            list all reviews                            /search?name=&item=    
	 */
	@GetMapping("/search")
	public String reviews( @RequestParam("name") String name, @RequestParam("item") String item, Model model) {
		ArrayList<Rating> ratings = new ArrayList<>();
		try {
			Connection conn = jdbcTemplate.getDataSource().getConnection();
			
			// prepare SQL statement with parameters
			PreparedStatement ps = null;
			ps = conn.prepareStatement("select name, item, rating from reviews where name like ? and item like ?");
				
			// set SQL parameters
			ps.setString(1, name.trim()+"%");
		    ps.setString(2, item.trim()+"%");

			// execute SQL.  Make arraylist of Ratings from the result set of rows
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				// get the name, item and rating values from the row
				Rating r = new Rating(rs.getString(1), rs.getString(2), rs.getInt(3));
				ratings.add(r);
			}
			conn.close();
			model.addAttribute("ratings", ratings);
			return "ratings";	
		} catch (SQLException se) {
			System.out.println("Error:  FirstApp#reviews SQLException " + se.getMessage() );
			model.addAttribute("msg",se.getMessage());
			return "error";
		}  
		
	}
	
	/*
	 * download the rating form page
	 */
	@GetMapping("/rateform")
	public String getRateForm(Model model) {
		model.addAttribute("rating", new Rating());
		return "rateform";
	}
	
	/*
	 * process the submitted form for a new rating
	 */
	@PostMapping("/rateform")
	public String processForm( Rating rating, BindingResult result, Model model) {
		try {
			Connection conn = jdbcTemplate.getDataSource().getConnection();
			PreparedStatement ps = conn.prepareStatement("insert into reviews (name, item, rating) values(?, ?, ?)");
			ps.setString(1, rating.name);
			ps.setString(2,  rating.item);
			ps.setInt(3,  rating.stars);
			int count = ps.executeUpdate();
			conn.close();
			model.addAttribute("count", count);
			return "updatesuccess";	
		} catch (SQLException se) {
			System.out.println("Error:  FirstApp#rateform SQLException " + se.getMessage() );
			model.addAttribute("msg",se.getMessage());
			return "error";
		}  
	}

}
