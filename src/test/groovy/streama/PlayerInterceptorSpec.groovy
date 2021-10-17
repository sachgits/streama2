package streama


import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(PlayerInterceptor)
class PlayerInterceptorSpec extends Specification {

    def setup() {
    }

    def cleanup() {

    }

    void "Test player interceptor matching"() {
        when:"A request matches the interceptor"
            withRequest(controller:"player")

        then:"The interceptor does match"
            interceptor.doesMatch()
    }
}
