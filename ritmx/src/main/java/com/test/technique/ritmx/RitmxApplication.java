package com.test.technique.ritmx;

import com.test.technique.ritmx.utils.ILogService;
import com.test.technique.ritmx.utils.LogLine;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.List;

@SpringBootApplication
public class RitmxApplication {

	public static void main(String[] args) throws Exception {
		ApplicationContext applicationContext = SpringApplication.run(RitmxApplication.class, args);

		ILogService service = applicationContext.getBean(ILogService.class);
		List<LogLine> lines = service.readLog("src/main/resources/logs/log2.txt");
		service.listServersForEachClient(lines);
		service.listClientsForEachServer(lines);
		service.getServerWithMostConnections(lines);
	}

}
