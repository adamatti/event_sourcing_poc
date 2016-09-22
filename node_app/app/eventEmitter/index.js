const useMQ = require('../config').useMQ;
module.exports = useMQ ? require("./mq") : require("./node");