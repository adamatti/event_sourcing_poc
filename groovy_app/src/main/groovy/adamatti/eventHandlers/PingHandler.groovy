package adamatti.eventHandlers

import groovy.util.logging.Slf4j
import org.apache.camel.Exchange
import org.springframework.stereotype.Component

import javax.annotation.PostConstruct

@Slf4j
@Component
class PingHandler implements EventHandler {
	@PostConstruct
	public void registerListeners(){
		from("ping.groovy"){ Map event ->
			log.info "Ping groovy received"
		}

		from("ping.node") {Exchange e ->
			log.info "Ping node received"
		}
	}
}
