package cats.twitter.webapp.controller.mvc;

import cats.twitter.model.Module;
import cats.twitter.model.Request;
import cats.twitter.repository.ModuleRepository;
import cats.twitter.repository.RequestRepository;
import cats.twitter.webapp.controller.mvc.dto.ModuleDTO;
import cats.twitter.webapp.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import cats.twitter.model.User;
import cats.twitter.repository.UserRepository;

import java.util.List;

/**
 *
 */
@Controller
@RequestMapping("/admin")
@SessionAttributes({"user"})
public class AdminController
{
	@Autowired
	UserRepository userRepository;

	@Autowired
	RequestRepository requestRepository;

	@Autowired
	ModuleRepository moduleRepository;
	private final int PAGE_SIZE = 10;

	/**
	 * Returns a list (page) of accounts.
	 * 
	 * @param page
	 *            the page number to return
	 * @return a list (page) of accounts
	 */
	@RequestMapping(value = "/accounts/{page}", method = RequestMethod.GET)
	public ModelAndView getUsers(@PathVariable(value = "page") int page)
	{
		ModelAndView model = new ModelAndView("account");

		Pageable result = new PageRequest(page,PAGE_SIZE);
		Page<User> users = userRepository.findAll(result);

		model.addObject("users",users);

		return model;
	}

	/**
	 * Returns the first page of accounts.
	 * 
	 * @return the first page of accounts
	 */
	@RequestMapping(value = "/accounts", method = RequestMethod.GET)
	public ModelAndView getUsersEmptyPage()
	{
		return getUsers(0);
	}

	/**
	 * Deletes a user by id
	 * 
	 * @param id
	 *            the id of the user to delete
	 * @return the first page of accounts
	 */
	@RequestMapping(value = "/deleteUser", method = RequestMethod.POST)
	public ModelAndView deleteUser(@RequestParam(value = "idu") Long id)
	{
		userRepository.delete(id);

		return getUsersEmptyPage();
	}

	/**
	 * Activate a user by id.
	 * 
	 * @param id
	 *            the id of the user to activate
	 * @return
	 */
	@RequestMapping(value = "/activateUser", method = RequestMethod.GET)
	public ModelAndView activateUser(@RequestParam(value = "idu") Long id)
	{
		User user = userRepository.findOne(id);
		if (user != null)
		{
			user.setActivated(true);
			MailService mailService = new MailService();
			mailService.SendMailActivated(user.getEmail(), user.getLogin());
			userRepository.save(user);
		}

		return getUsersEmptyPage();
	}

	/**
	 * Deactivate a user by id.
	 * 
	 * @param id
	 *            the id of the user to deactivate
	 * @return
	 */
	@RequestMapping(value = "/desactivateUser", method = RequestMethod.GET)
	public ModelAndView desactivateUser(@RequestParam(value = "idu") Long id)
	{
		User user = userRepository.findOne(id);
		if (user != null)
		{
			user.setActivated(false);
			userRepository.save(user);
		}

		return getUsersEmptyPage();
	}

	/**
	 * Draw the module manager
	 * @return
     */
	@RequestMapping(value = "/modules", method = RequestMethod.GET)
	@Transactional
	public ModelAndView manageModule(){
		List<Module> modules = moduleRepository.findAll();
		modules.forEach(module -> module.lazyLoad());
		ModelAndView modelAndView = new ModelAndView("modules");
		modelAndView.addObject("modules",modules);
		return modelAndView;
	}

	/**
	 * Add a module to the DB and redirect to the module manager
	 * @param dto Spring automatically map the requests param from modules.jsp into ModuleDTO
	 * @return the redirection
     */
	@RequestMapping(value = "/modules", method = RequestMethod.POST)
	public String addModule(ModuleDTO dto){
		Module module = dto.toModule();
		moduleRepository.save(module);
		return "redirect:/admin/modules";
	}

	/**
	 * Delete the module, if the module have any treatements associed, the references are set to null
	 * @param id id of the module
     */
	@RequestMapping(value = "/modules/{idModule}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	@Transactional
	public void deleteModule(@PathVariable("idModule") int id){
		Module module =moduleRepository.findOne(id);
		List<Request> requests = requestRepository.findByModule(module);
		for (Request request : requests) {
			request.setModule(null);
		}
		requestRepository.save(requests);
		moduleRepository.delete(module);
	}

}
