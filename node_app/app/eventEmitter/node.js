'use strict';

const events = require("events"),
      emitter = new events.EventEmitter(),
      uuid = require('uuid-v4')
;

module.exports = {
    emit : function(eventType, body) {
        var event = {
            id: uuid(),
            eventType: eventType,
            eventDate : new Date,
            body : body
        };
        return emitter.emit(eventType, event);
    },
    on : function(eventType, cb) {
        return emitter.on(eventType, cb);
    }
};