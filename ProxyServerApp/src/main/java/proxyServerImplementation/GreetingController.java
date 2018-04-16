package proxyServerImplementation;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

    private static final String template = "Hello, %s! I live in %s.";
    private final AtomicLong counter = new AtomicLong();

//    @RequestMapping(path = "/greeting", method = RequestMethod.POST)
//    public String greeting(@RequestBody String request) throws IOException {
//        final byte[] requestContent;
//        requestContent = request.getBytes();
//        return new String(requestContent, StandardCharsets.UTF_8);
//    }
    
    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name,
    						@RequestParam(value="address", defaultValue="NJ") String address) {
        
    	System.out.println("==>>Greetings :)"+counter.incrementAndGet());
    	System.out.println("name: "+ name + " address:" + address);
    	return new Greeting(counter.incrementAndGet(),
                            String.format(template, name,address));
    }
    
    @RequestMapping( "/greeting1")
    public Greeting greeting1(@RequestParam(value="name", defaultValue="World") String name) {
        return new Greeting(counter.incrementAndGet(),
                            String.format(template, name));
    }
}
