package adamatti

import adamatti.eventHandlers.EventHandler
import groovy.util.logging.Slf4j
import org.springframework.stereotype.Component
import spark.Request
import spark.Response
import spark.Spark

import javax.annotation.PostConstruct

@Slf4j
@Component
class Web implements EventHandler {
	@PostConstruct
	public void registerRoutes(){
		Spark.get("/"){ Request req, Response res ->
			try {
				emit("ping.groovy", [:])

				return "working"
			} catch (Exception e){
				return "Error: ${e.message}"
			}
		}
	}
}
