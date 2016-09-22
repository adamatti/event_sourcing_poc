package adamatti

import com.rabbitmq.client.Channel
import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory
import groovy.util.logging.Slf4j
import org.springframework.stereotype.Component

import javax.annotation.PostConstruct
import javax.annotation.PreDestroy

@Slf4j
@Component
class ConfigMq {
	private ConfigObject cfg = Resources.cfg
	private Channel channel
	private Connection connection

	@PostConstruct
	public void config(){
		ConnectionFactory factory = new ConnectionFactory()
		factory.setUri(cfg.amqp.url as String)
		connection = factory.newConnection()
		channel = connection.createChannel()

		this.configureExchange(channel)
		//this.configureQueue(channel)
	}

	private void configureQueue(Channel channel){
		String queueName = "QueueName"
		String bindingKey = "app.started"
		boolean durable = true
		boolean exclusive = false
		boolean autoDelete = false

		channel.queueDeclare(queueName,durable,exclusive,autoDelete,null)
		channel.queueBind(queueName,cfg.amqp.eventsExchangeName as String,bindingKey)
		log.info "Queue configured"
	}

	private void configureExchange(Channel channel){
		channel.exchangeDeclare(cfg.amqp.eventsExchangeName as String,"topic", true)
	}

	@PreDestroy
	public void preDestroy(){
		log.info "preDestroy"
		channel.close()
		connection.close()
	}
}
