package top.ysqorz.forum;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ForumApplicationTest {
	@Autowired
	private RestTemplate restTemplate;

	@Test
    public void contextLoads() {
	}

	@Test
	public void testSSE() {

	}

}
