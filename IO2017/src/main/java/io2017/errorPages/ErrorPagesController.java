package io2017.errorPages;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.boot.context.config.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class ErrorPagesController implements ErrorController{
	
	@RequestMapping("/403")
	public String accessDenied(Model model) {
		model.addAttribute("errorName", "Nie jesteś upoważniony aby zobaczyć tę stronę");
		model.addAttribute("errorInfo", "Aby ją zobaczyć zaloguj się jako administrator.");
		
	    return "errorPage";
	}
	
	@RequestMapping("/error")
	public String pageNotFound(Model model) {
		model.addAttribute("errorName", "Nie ma strony o takim adresie");
		model.addAttribute("errorInfo", "Upewnij się, że dobrze wprowadziłeś ściężkę strony.");
		
		
	    return "errorPage";
	}

	@Override
	public String getErrorPath() {
		// TODO Auto-generated method stub
		return null;
	}
}
