package adamatti

import groovy.util.logging.Slf4j
import org.springframework.context.ApplicationContext
import org.springframework.context.support.GenericGroovyApplicationContext
import spark.Spark

@Slf4j
class Main {
	private static ConfigObject cfg = Resources.cfg

	public static void main(String [] args){
		log.info "Starting app"
		startSpark()
		startSpring()
	}

	private static void startSpark(){
		Spark.port(cfg.port as int)
	}

	private static void startSpring(){
		ApplicationContext applicationContext = new GenericGroovyApplicationContext("classpath:spring/root.groovy")
		applicationContext.registerShutdownHook()
		log.info "Spring started"
	}
}
