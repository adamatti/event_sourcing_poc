package spring

beans {
	xmlns([context:"http://www.springframework.org/schema/context"])

	context.'component-scan'('base-package':'adamatti')
	context.'annotation-config'()

	importBeans('classpath:spring/camel.groovy')
	importBeans('classpath:spring/rabbitmq.groovy')

	commonAnnotationBeanPostProcessor(org.springframework.context.annotation.CommonAnnotationBeanPostProcessor)
}