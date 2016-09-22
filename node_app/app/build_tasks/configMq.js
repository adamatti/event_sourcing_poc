'use strict';
const logger = require('log4js').getLogger("configMq"),
      config = require("../config"),
      events = require("events"),
      eventEmitter = new events.EventEmitter(),
      _ = require("lodash"),
      Promise = require("bluebird"),
      amqp = require('amqplib/callback_api')
;

function stopIfError(err, result){
    if (!err){
        result();
    } else {
        logger.error("error: ", err);
        eventEmitter.emit("shutdown",{shutdownStatus:1});
    }
}

eventEmitter.on("start_config", ctx => {
    amqp.connect(config.amqp.url, function(err, conn) {
        return stopIfError(err, () => {
            logger.trace("connected");
            eventEmitter.emit("mq_connected",{conn: conn});
        });
    })
});

eventEmitter.on("mq_connected", ctx => {
    ctx.conn.createChannel(function(err, ch) {
        return stopIfError(err, () =>{            
            ctx["ch"] = ch;
            eventEmitter.emit("mq_channel_created",ctx);
        });
    });
});

eventEmitter.on("mq_channel_created", ctx =>{
    ctx.ch.assertExchange(config.amqp.eventsExchangeName, 'topic', {durable: true});
    logger.info("Exchange created: ", config.amqp.eventsExchangeName);
    eventEmitter.emit("exchange_created",ctx);
});

eventEmitter.on("exchange_created", ctx =>{ 
    //FIXME it seems we need at least one queue to persist the exchange
    const queueName = "dummy_queue";
    ctx.ch.assertQueue(queueName, {durable: true}, function(err, q){
        return stopIfError(err, () => {
            const consumerKey = 'invalidKey'; 
            ctx.ch.bindQueue(queueName, config.amqp.eventsExchangeName, consumerKey, {}, function(err){
                return stopIfError(err, () => {
                    eventEmitter.emit("shutdown",ctx);
                });
            });                 
        });
    });
});

eventEmitter.on("shutdown", ctx => {
    const shutdownStatus = ctx.shutdownStatus || 0;
    logger.trace("shutting down, status:",shutdownStatus);
    if (ctx.conn){
        ctx.conn.close();
    }
    process.exit(shutdownStatus);
})

eventEmitter.emit("start_config");