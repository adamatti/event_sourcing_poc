package spring

beans {
	customConnectionFactory(com.rabbitmq.client.ConnectionFactory.class){
		uri  = '${amqp.url}'
		automaticRecoveryEnabled = true
	}
}