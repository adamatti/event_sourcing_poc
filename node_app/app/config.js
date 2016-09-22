"use strict";

var config = {}
config.amqp = {
    url : process.env.CLOUDAMQP_URL || "amqp://admin:admin@localhost:5672",
    eventsExchangeName : 'events_exchange'
}
config.port = process.env.PORT || '3000';
config.useMQ = true;
config.sleep = parseInt(process.env.SLEEP || '1');

module.exports = config;