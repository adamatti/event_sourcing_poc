package spring

beans {
	xmlns([camel: "http://camel.apache.org/schema/spring"])

	camel.camelContext(id: "camelContext", autoStartup: "true") {
		//Search beans with SpringRouteBuilder
		camel.contextScan()

		camel.route(id: "testRoute"){
			camel.from(uri: "file:/tmp/in")
			camel.to(uri:"file:/tmp/out")
		}
	}
}