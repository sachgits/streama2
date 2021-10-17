package streama


import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(HelpInterceptor)
class HelpInterceptorSpec extends Specification {

    def setup() {
    }

    def cleanup() {

    }

    void "Test help interceptor matching"() {
        when:"A request matches the interceptor"
            withRequest(controller:"help")

        then:"The interceptor does match"
            interceptor.doesMatch()
    }
}
