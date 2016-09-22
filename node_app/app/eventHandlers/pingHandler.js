"use strict";
const eventEmitter = require("../eventEmitter"),
      logger = require("log4js").getLogger("pingHandler")
;

eventEmitter.on("ping.node", event => {
    logger.info("Event 'ping.node' received ")
})

eventEmitter.on("ping.groovy", event => {
    logger.info("Event 'ping.groovy' received ")
})