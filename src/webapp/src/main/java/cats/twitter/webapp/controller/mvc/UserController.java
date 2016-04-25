package cats.twitter.webapp.controller.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import cats.twitter.model.Authority;
import cats.twitter.model.User;
import cats.twitter.repository.UserRepository;
import cats.twitter.security.AuthoritiesConstants;

@Controller
@SessionAttributes({ "twitter" })
public class UserController
{
	@Autowired
	private UserRepository userRepository;

	/**
	 * @param error
	 *            if the user fail to login
	 * @param logout
	 *            if the user decided to logout
	 * @return The view to draw with some string in case of error or logout
	 */
	@RequestMapping(value = "/connect", method = RequestMethod.GET)
	public ModelAndView connect(
		@RequestParam(value = "error", required = false) String error,
		@RequestParam(value = "logout", required = false) String logout,
		@RequestParam(value = "success", required = false) String success)
	{

		ModelAndView model = new ModelAndView();
		if (error != null)
		{
			model.addObject("error","Invalid username and password!");
		}

		if (logout != null)
		{
			model.addObject("msg","You've been logged out successfully.");
		}

		if (success != null)
		{
			model.addObject("msg","User added successfully, please wait for admin activation.");
		}
		model.setViewName("login");

		return model;
	}

	/**
	 * Returns register new user page
	 * 
	 * @param error
	 *            if the user already exists
	 * @param logout
	 *            if the user decided to logout
	 * @return The view to draw with some string in case of error or logout
	 */
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public ModelAndView addUserPage(
		@RequestParam(value = "error", required = false) String error,
		@RequestParam(value = "params", required = false) String params)
	{

		ModelAndView model = new ModelAndView();
		if (error != null)
		{
			model.addObject("error","User exists!");
		}
		if (params != null)
		{
			model.addObject("error","Please verify your informations");
		}

		model.setViewName("register");

		return model;
	}

	/**
	 * Controller to add a new user
	 * 
	 * @param userToAdd
	 *            the new user
	 * @return the ModelAndView after adding the user
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@RequestMapping(value = "/newUser", method = RequestMethod.POST)
	public String addUser(
		@RequestParam("login") String login,
		@RequestParam("email") String email,
		@RequestParam("password") String password,
		@RequestParam("firstName") String firstName,
		@RequestParam("lastName") String lastName)
	{
		User userToAdd = new User();

		if (login == null)
		{
			return "redirect:/register?error";
		}

		PasswordEncoder encoder = new BCryptPasswordEncoder();

		userToAdd.setLogin(login);
		userToAdd.setActivated(false);
		userToAdd.setEmail(email);
		userToAdd.setPassword(encoder.encode(password));
		userToAdd.setFirstName(firstName);
		userToAdd.setLastName(lastName);
		Authority userAuth = new Authority();
		userAuth.setName(AuthoritiesConstants.USER);
		userToAdd.getAuthorities().add(userAuth);

		if (!userRepository.findOneByLogin(userToAdd.getLogin()).isPresent())
		{
			try
			{
				userRepository.save(userToAdd);
				return "redirect:/connect?success";
			}
			catch (UnexpectedRollbackException ex)
			{
				return "redirect:/register?params";
			}
			catch (Exception ex)
			{
				return "redirect:/register?params";
			}
		}

		return "redirect:/register?error";
	}

	/**
	 * @return The index page to draw
	 */
	@RequestMapping("/")
	public String index(@ModelAttribute User user)
	{
		return "redirect:/corpus";
	}

}
