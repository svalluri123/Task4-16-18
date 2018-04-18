package proxyServerImplementation;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/*
 * @Sirisha
 * Rest Controller class - with two resources, displays entered data with incremented counter number
 * on the REST client.
 */


@RestController
public class GreetingController {

    private static final String template = "Hello, %s! lives in %s.";
    private final AtomicLong counter = new AtomicLong();
    
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
