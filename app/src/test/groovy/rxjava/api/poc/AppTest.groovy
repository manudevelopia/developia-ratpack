package rxjava.api.poc

import spock.lang.Specification

class AppTest extends Specification {
    def "application has a greeting"() {
        when:
        Launcher.main()
        then:
        noExceptionThrown()
    }
}
