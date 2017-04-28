package rest_test;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @RequestMapping("/test")
    public Test test() {
    	Test t = new Test();
    	t.setNum(7);
    	t.setStr("Green");
    	return t;
    }
}