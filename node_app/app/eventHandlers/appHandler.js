"use strict";
const eventEmitter = require("../eventEmitter"),
      logger = require("log4js").getLogger("appHandler")
;

eventEmitter.on("app.started",event =>{
    logger.info("Event 'app.started' received");
})