package adamatti

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer
import org.springframework.stereotype.Component

@Component
class Resources extends PropertyPlaceholderConfigurer {
	private static ConfigObject cfg

	static {
		def config = this.class.getResource("/config.groovy")
		if (!config) {
			//TODO change
			config = new File("src/main/resources/config.groovy")
		}
		cfg = new ConfigSlurper().parse(config.text)
	}

	@Override
	protected void loadProperties(Properties props) throws IOException {
		props.putAll(cfg.toProperties())
	}
}