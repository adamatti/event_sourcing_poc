package adamatti.eventHandlers

import adamatti.RouteBuilder
import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import groovy.util.logging.Slf4j
import org.apache.camel.Exchange
import org.apache.camel.model.RouteDefinition

import static java.util.UUID.randomUUID
import adamatti.Resources
import org.apache.camel.ProducerTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Slf4j
@Component
trait EventHandler {
	private static ConfigObject cfg = Resources.cfg
	private Map<String,Closure> handlers = [:]

	@Autowired
	private ProducerTemplate producerTemplate

	@Autowired
	private RouteBuilder routeBuilder


	public RouteDefinition from(String eventType, Closure cb){
		handlers[eventType] = cb

		String queueName = "groovy.${this.class.simpleName}"
		String endpoint = this.buildEndpoint(eventType) + "&queue=${queueName}"

		routeBuilder
			.from(endpoint)
			//.removeHeaders("rabbitmq.*")
			.process { Exchange e ->
				String json = new String(e.in.body as byte[])
				Map event = new JsonSlurper().parseText(json)

				Closure handler = handlers[event.eventType as String]
				if (handler) {
					if (handler.parameterTypes[0] == Exchange) {
						handler.call(e)
					}
					if (handler.parameterTypes[0] == Map){
						handler.call(event)
					}
				} else {
					log.error "No handler for event ${event.eventType}"
				}
			}
	}

	public void emit(String eventType,Object payload){
		String event = this.createEvent(eventType, payload)

		String endpoint = this.buildEndpoint(eventType)
		producerTemplate.sendBody(endpoint,event)

		log.info "Event emitted [type: ${eventType}]"
	}

	private String buildEndpoint(String eventType){
		"rabbitmq://localhost:5672/${cfg.amqp.eventsExchangeName}?" +
		"exchangeType=topic&"+
		"durable=true&" +
		"routingKey=${eventType}&" +
		"autoDelete=false&"+
		"connectionFactory=#customConnectionFactory"
	}

	private String createEvent(String eventType, Object payload){
		Map event = [
			id : randomUUID().toString(),
			eventType: eventType,
			eventDate: new Date(),
			body : payload
		]

		new JsonBuilder(event).toPrettyString()
	}
}
