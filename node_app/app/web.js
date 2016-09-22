"use strict";
const logger = require("log4js").getLogger("index"),
      express = require("express"),
      app = express(),
      eventEmitter = require("./eventEmitter"),
      config = require("./config")
;


app.get('/', (req,res) => {
    eventEmitter.emit("ping.node",{});

    res.set('Content-Type', 'application/json');
    res.send('{"status":"ok"}');
});

app.listen(config.port, function () {
    logger.info('Example app listening on port ' + config.port);
    eventEmitter.emit("app.started",{});
});
