package cats.twitter.webapp.controller.module;

import cats.twitter.model.Tweet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import cats.twitter.model.Module;
import cats.twitter.model.Tweet;
import cats.twitter.webapp.dto.Result;
import cats.twitter.webapp.service.ModuleService;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

/**
 * Controller of the request mechanical module side.
 */
@RestController
@RequestMapping("/module")
public class ModuleControllerRest
{

	private final Logger log = LoggerFactory.getLogger(ModuleControllerRest.class);

	@Autowired
	private ModuleService modService;

	/**
	 * First a module have to register himself among CATS. He has to send a JSON object:
	 * {
			 "endpoint":"http://localhost:3000",
			 "name":"That's my module3",
			 "description":"A very complicated module", //Explanation of what the module does
			 "returns":["html","data","corpus"], // The returns type of the module
			 "params":[ // If needed, CATS can send to the module some parameters, you have to specify them
			 {
			 "type":"text", // type of the argument : number, bool, or text
			 "name":"keyword", // Name , it's like an id
			 "displayName":"Keyword" // What's it's gonna be displayed to user in the form
			 },
			 {
			 "type":"bool",
			 "name":"check",
			 "displayName":"Keep only most present"
			 }
			 ]
			 }
	 * @param json The Spring serialized json into our object.
	 * @return Return the same object with an ID filled.
     */
	/*@RequestMapping(value = "/register", method = RequestMethod.POST)
	@ResponseStatus(code = HttpStatus.CREATED)
	public Module register(@RequestBody Module json) {
		return modService.register(json);
	}*/

	/**
	 * If the module have specified a return type as 'html', he has to do a request on /result with a json in parameter:
	 * {
	 *     "token":"98R73987938YY788A7628GIGEUYZG3", // The token sent in the request
	 *     "result":"<html>blablabla</html>" //HTML code interlocked in String type
	 * }
	 * @param result The Spring serialized json into our object.
     */
	@RequestMapping(value = "/result", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE})
	@ResponseStatus(code = HttpStatus.CREATED)
	public void postResult(@RequestBody Result result) {
		modService.postResult(result);
	}

	/**
	 * If the module have specified a return type as 'data', he has to do a request on /result with a json in parameter:
	 * {
	 *     "token":"98R73987938YY788A7628GIGEUYZG3", // The token sent in the request
	 *     "result":"data" // data in whichever type of data, interlocked in String type
	 * }
	 * @param result The Spring serialized json into our object.
	 */
	@RequestMapping(value = "/resultFile", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE})
	@ResponseStatus(code = HttpStatus.CREATED)
	public @ResponseBody
	void handleFileUpload(@RequestBody Result result){
		modService.postResultAsFile(result);
		modService.initNextModule(result);
	}

	/**
	 * If the module have specified a return type as 'corpus', he has to do a request on /result/{token}
	 * with a json in parameter: the list of tweets. We will create a new corpus with that data.
	 * @param token the token passed in path variable
	 * @param tweets The spring serialized list of tweet to insert in our DB
     */
	@RequestMapping(value = "/result/{token}", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE})
	@ResponseStatus(code = HttpStatus.CREATED)
	public void handleResultNone(@PathVariable("token") String token,
						  @RequestBody List<Tweet> tweets){
		modService.postResultCorpus(token,tweets);
	}
}
