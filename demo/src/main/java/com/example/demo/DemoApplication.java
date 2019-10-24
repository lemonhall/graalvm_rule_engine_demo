package com.example.demo;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.web.bind.annotation.*;
import org.graalvm.polyglot.*;
import java.io.*;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ClassPathResource;

@SpringBootApplication
@RestController
@EnableAutoConfiguration
public class DemoApplication {


	public class Services {
	    @HostAccess.Export
	    public void createEmployee(String name) {
	        System.out.println(name);
	    }
	}

	//读取某个文件的帮助函
	public static String LoadContentByPath(String path) throws IOException {

		Resource resource = new ClassPathResource(path);
        InputStream is = resource.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        StringBuffer buffer = new StringBuffer();
        String line = "";
        while((line = br.readLine()) != null) {
            buffer.append(line);
        }
        
        br.close();
        isr.close();
        is.close();
        return buffer.toString();
  
  }

	Context context = Context.newBuilder().allowAllAccess(true).build();

    Services services = new Services();
        
	@RequestMapping("/")
    String home() {

    	String jsFile = "";

    	try{
    		jsFile = this.LoadContentByPath("t.js");
    	} catch (IOException e) {
			e.printStackTrace();
    	}

        context.getBindings("js").putMember("services", services);

        String result = context.eval("js",jsFile).asString();
        return result;
    }

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}
