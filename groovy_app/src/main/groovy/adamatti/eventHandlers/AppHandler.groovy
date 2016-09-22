package adamatti.eventHandlers

import groovy.util.logging.Slf4j
import org.apache.camel.Exchange
import org.springframework.stereotype.Component

import javax.annotation.PostConstruct

@Slf4j
@Component
class AppHandler implements EventHandler{
	@PostConstruct
	public void start(){
		log.info "AppHandler created"

		from("app.started"){ Exchange e ->
			log.info "Event 'app.started' received"
		}

		emit("app.started",[:])
	}
}
