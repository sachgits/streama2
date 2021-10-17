package streama


import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(VideoInterceptor)
class VideoInterceptorSpec extends Specification {

    def setup() {
    }

    def cleanup() {

    }

    void "Test video interceptor matching"() {
        when:"A request matches the interceptor"
            withRequest(controller:"video")

        then:"The interceptor does match"
            interceptor.doesMatch()
    }
}
