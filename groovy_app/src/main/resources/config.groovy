port = (System.env.PORT ?: '8080').toInteger()

amqp = [
	url : System.env.CLOUDAMQP_URL ?: System.env.MQ_URL ?: "amqp://admin:admin@localhost:5672",
	eventsExchangeName : "events_exchange"
]