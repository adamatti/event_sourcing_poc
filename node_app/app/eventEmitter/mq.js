const logger = require('log4js').getLogger("eventEmitter"),
      uuid = require('uuid-v4'),
      amqp = require('amqplib')
      config = require("../config"),
      mqConn = amqp.connect(config.amqp.url),
      mqChannel = mqConn.then(conn => {return conn.createChannel()}),
      callerId = require('caller-id'),
      S = require("string")
;

var queues = {};

function recordCallback(queueName,eventType, cb){
    if (!queues[queueName]){
        queues[queueName] = {};
    }
    queues[queueName][eventType] = cb;
}

module.exports = {
    emit : function(eventType, body) {
        var event = {
            id: uuid(),
            eventType: eventType,
            eventDate : new Date,
            body : body
        };
        const json = JSON.stringify(event);
        
        return mqChannel.then(channel => {
            return channel.publish(config.amqp.eventsExchangeName, eventType, new Buffer(json));
        }).then ( () => {
            logger.trace("Event emitted (mq), queue: %s, content: %s ",eventType, json);
        }).catch(error => {
            logger.error("Error on emitToRabbitExchange: ", error);
        });
    },
    
    on : function(eventType, cb) {
        const queueName = "node." + 
            S(
                S(
                    callerId.getData().filePath
                ).splitRight('/', 1)[1]
            ).strip(".js")
        ;

        var channel;
        return mqChannel.then(_channel => {
            channel = _channel;
            return channel.assertQueue(queueName, {
                durable: true,
                exclusive: false
            });
        }).then( () => {
            logger.trace("assertQueue[q: %s]", queueName);
            return channel.bindQueue(queueName,config.amqp.eventsExchangeName,eventType);
        }).then ( () => {
            return recordCallback(queueName,eventType, cb);
        }).then( () => {
            logger.trace("bindQueue[q: %s, eventType: %s]", queueName, eventType);
            channel.consume(queueName, msg => {
                try {
                    const obj = JSON.parse(msg.content);                    
                    logger.trace("dequeue[queue: %s, content: %s]",queueName, JSON.stringify(obj));                
                    const functionToCall = queues[queueName][obj.eventType];
                    if (functionToCall){
                        functionToCall(obj);
                    } else {
                        logger.warn("No listeners [event %s, queue: %s]",obj.eventType,queueName);
                    }                    
                    channel.ack(msg);
                } catch (error){
                    logger.error("Error: ", error);
                }
            });
        }).catch(error => {
            logger.error("Error: ", error);
        });
    }
}