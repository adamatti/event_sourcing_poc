package adamatti

import org.apache.camel.spring.SpringRouteBuilder
import org.springframework.stereotype.Component

@Component
class RouteBuilder extends SpringRouteBuilder {
	@Override
	void configure() throws Exception {
		//Global routes here
	}
}
