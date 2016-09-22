"use strict";
const logger = require("log4js").getLogger("index"),
      fs = require("fs"),
      sleep = require("sleep"),
      config = require("./config")
;

function requireFolder(folderName) {
    fs.readdirSync('./app/'+folderName).map(fileName =>{
        logger.trace('Loading ('+folderName+') ' + fileName);
        require('./'+folderName+'/' + fileName);
    });
}

logger.info ("Starting app");

logger.info("Wait %s seconds for rabbitmq",config.sleep);
sleep.sleep(config.sleep); //wait X seconds for rabbitmq

requireFolder("eventHandlers");
require("./web");