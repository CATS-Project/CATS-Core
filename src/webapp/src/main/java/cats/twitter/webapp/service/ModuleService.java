package cats.twitter.webapp.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import cats.twitter.model.*;
import org.springframework.web.multipart.MultipartFile;

import cats.twitter.webapp.dto.Query;

import cats.twitter.webapp.dto.Result;
public interface ModuleService
{
	Module register(Module module);
	Request postResult(Result result);
    Query send(Module module, Map<String, String> flatten, Corpus corpus);
	Query send(Module module, Map<String, String> flatten, SubCorpus subCorpus);

	void sendChain(String[] modules, Map<String, String[]> map, Corpus corpus);

	void initNextModule(Result result);

	void postResultCorpus(String token, List<Tweet> tweets);

	Request postResultAsFile(Result result);
}
