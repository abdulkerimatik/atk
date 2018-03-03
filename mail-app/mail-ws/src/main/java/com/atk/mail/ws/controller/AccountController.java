package com.atk.mail.ws.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.atk.mail.domain.repo.AccountRepo;
import com.atk.mail.domain.repo.UserRepo;
import com.atk.mail.domain.validator.UserValidator;
import com.atk.mail.dto.AccountDTO;
import com.atk.mail.entity.Account;
import com.atk.mail.entity.User;
import com.atk.mail.ws.controller.base.ControllerBase;

@Controller
public class AccountController extends ControllerBase<Account>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4380320264698547300L;
	
	@Autowired
    private UserRepo userRepo;
	
	@Autowired
	private AccountRepo accountRepo;
	
	@Autowired
	private UserValidator validator;
	
	 @RequestMapping(value = "/new-account", method = RequestMethod.GET)
	    public ModelAndView registration(Model model) {
	    		ModelAndView modelAndView = new ModelAndView();
			AccountDTO account = new AccountDTO();
			modelAndView.addObject("account", account);
			modelAndView.setViewName("authentication/new-account");
			return modelAndView;
	    }

	   @RequestMapping(value = "/save-new-account", method = RequestMethod.POST)
	    public String registration(@ModelAttribute("userForm") AccountDTO accountDTO, BindingResult bindingResult, Model model) {
		   validator.validate(accountDTO, bindingResult);
	        
		   if (bindingResult.hasErrors()) {
	            return "redirect:/authentication/new-account";
	        }
		   
	        accountRepo.saveAccount(accountDTO);
	        return "redirect:/";
	    }
	   
	   @RequestMapping(value = "/forgot-password", method = RequestMethod.GET)
	   public ModelAndView recoverPassword(Model model) {
	   		ModelAndView modelAndView = new ModelAndView();
			User user = new User();
			modelAndView.addObject("user", user);
			modelAndView.setViewName("authentication/forgot-password");
			return modelAndView;
	   }

	  @RequestMapping(value = "/recover-forgot-password", method = RequestMethod.POST)
	   public String recoverPassword(@ModelAttribute("userForm") User userForm, BindingResult bindingResult, Model model) {

	       if (bindingResult.hasErrors()) {
	           return "authentication/forgot-password";
	       }
	       userRepo.save(userForm);
	       return "authentication/login";
	   }

}
