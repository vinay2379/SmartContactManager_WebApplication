package com.SmartContactManager.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.SmartContactManager.Helper.Message;
import com.SmartContactManager.Model.Contect;
import com.SmartContactManager.Model.User;
import com.SmartContactManager.Repository.ContactRepository;
import com.SmartContactManager.Repository.UserRepository;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class UserController {
	@Autowired
	private BCryptPasswordEncoder  pse;
	
	
	@Autowired
	private UserRepository repo;
	
	@Autowired
	private ContactRepository crepo;
	
	

//_______________________________________________________________________________________________________________	
	@GetMapping("/")
	public String home(Model model) {
		model.addAttribute("title", "Smart Contact manager");
		return "home";
	}
	
//____________________________________________________________________________________________________________	
	@GetMapping("/about")
	public String about(Model model) {
		model.addAttribute("title", " about_Smart Contact manager");
		return "about";
	}
//____________________________________________________________________________________________________
	
	
	@GetMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("title", " signup_Smart Contact manager");
		model.addAttribute("user", new User());
		return "signup";
	}
	@GetMapping("/register")
	public String regisertedUser( @Valid @ModelAttribute("user") User user,
			@RequestParam(value="check",defaultValue="false") Boolean check, 
			Model model,HttpSession session,BindingResult result) {
		try {
			
			
			
			
			if(!check) {
				System.out.println("please check the condition");
				throw new Exception("please check the condition");
			
			}
			if (result.hasErrors()) {
				System.out.println("ERROR " + result.toString());
				model.addAttribute("user", user);
				return "signup";
			}
			
			user.setRole("Role_User");
			user.setEnabled("true");
			user.setImageUrl("default.png");
			user.setPassword(pse.encode(user.getPassword()));
			
			User	res=this.repo.save(user);
			model.addAttribute("user", new User());
			session.setAttribute("message", new Message("Succesfully Registered","alert-succes"));
			
			
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("user", user);
			
			session.setAttribute("message", new Message("something went worng"+e.getMessage(),"alert-danger"));
			
		}
		return "signup";
		
	}
	
//_____________________________________________________________________________________________________S	
	
	@GetMapping("/welcome")
	public String login(Model model, Principal principal) {
		
	String	username=principal.getName();
	User	user=repo.getUserByName(username);
	model.addAttribute("user", user);
		model.addAttribute("title", "login_Smart Contact manager ");
		
		return "login";
	}
	

//________________________________________________________________________________________________________		
	@GetMapping("/contact")	
	public String AddContact(Model model, Principal principal) {
		
		String	username=principal.getName();
		User	user=repo.getUserByName(username);
		model.addAttribute("user", user);
		model.addAttribute("title", "addContact_Smart Contact manager ");
		model.addAttribute("contact", new Contect());
		
		return "contact";
	}
	
	@GetMapping("/register2")
	public String ProcessContact(@ModelAttribute("contact") Contect contact,Model model, Principal principal,HttpSession session) {
		try {
			
		
		System.out.println(contact);
		String	username=principal.getName();
		User	user=repo.getUserByName(username);
		contact.setUser(user);
		user.getContact().add(contact);
		this.repo.save(user);
		model.addAttribute("user", user);
		model.addAttribute("title", "addContact_Smart Contact manager ");
		model.addAttribute("contact", new Contect());
		session.setAttribute("message", new Message("succesfully register","alert-succes"));
		} catch (Exception e) {
			System.out.println("error"+e.getMessage());
			e.printStackTrace();
			session.setAttribute("message", new Message("something went worng"+e.getMessage(),"alert-danger"));
		}
		return "contact";
	}
		
//___________________________________________________________________________________________________________		
		@GetMapping("/show")
		public String ContactShow(Model model, Principal principal) {
			
			String	username=principal.getName();
			User	user=repo.getUserByName(username);
			model.addAttribute("user", user);
				model.addAttribute("title", "show_contact Contact manager ");
	
		List<Contect>	contact	=this.crepo.findByUser(user.getId());
		model.addAttribute("contact", contact);
				return "viewShow";
			
		
		
		
	}

//________________________________________________________________________________________________________		
    @GetMapping("/delete/{cid}")
	public String DeleteContact(@PathVariable("cid") Integer cid,Model model, Principal principal) {
    	String	username=principal.getName();
		User	user=repo.getUserByName(username);
		model.addAttribute("user", user);
			model.addAttribute("title", "show_contact Contact manager ");

    Optional<Contect>	co=this.crepo.findById(cid);
 Contect  contact= co.get();
    this.crepo.delete(contact);
		return "redirect:/show";
	}
		
//_____________________________________________________________________________________________________		
	@PostMapping("/update/{cid}")	
	public String UpdateContact(@PathVariable("cid") Integer cid,Model model, Principal principal) {
		
		Contect    contact =this.crepo.findById(cid).get();
		model.addAttribute("contact", contact);
			model.addAttribute("title", "show_contact Contact manager ");
		return "updateForm";
	}
		
	@PostMapping("/register3")
	public String Update(@ModelAttribute("contact")  Contect contact,Model model, Principal principal, HttpSession session) {
		try {
		String	username=principal.getName();
		User	user=repo.getUserByName(username);
		contact.setUser(user);   
		model.addAttribute("user", user);
		
			model.addAttribute("title", "show_contact Contact manager ");
			System.out.println(contact.getName());
			this.crepo.save(contact);
			return "redirect:/show";
			}
			catch (Exception e) {
				// TODO: handle exception
			}
		return "redirect:/show";
	}
//________________________________________________________________________________________________________			

	@GetMapping("/profile")
	public String profile(Model model, Principal principal) {
		String	username=principal.getName();
		User	user=repo.getUserByName(username);
		model.addAttribute("user", user);
			model.addAttribute("title", "show_contact Contact manager ");

		return "profile";
	}
	
	
//_____________________________________________________________________________________________________	
	
	
	
	
	
}
