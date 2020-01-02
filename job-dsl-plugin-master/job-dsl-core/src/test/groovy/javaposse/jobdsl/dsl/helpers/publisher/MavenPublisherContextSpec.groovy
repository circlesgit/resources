package javaposse.jobdsl.dsl.helpers.publisher

import javaposse.jobdsl.dsl.Item
import javaposse.jobdsl.dsl.JobManagement
import spock.lang.Specification

class MavenPublisherContextSpec extends Specification {
    private final JobManagement jobManagement = Mock(JobManagement)
    private final Item item = Mock(Item)
    private final MavenPublisherContext context = new MavenPublisherContext(jobManagement, item)

    def 'call deployArtifacts with no options'() {
        when:
        context.deployArtifacts()

        then:
        with(context.publisherNodes[0]) {
            name() == 'hudson.maven.RedeployPublisher'
            children().size() == 3
            id[0].value().empty
            uniqueVersion[0].value() == true
            evenIfUnstable[0].value() == false
        }
    }

    def 'call deployArtifacts with all options'() {
        when:
        context.deployArtifacts {
            repositoryUrl('foo')
            repositoryId('bar')
            releaseEnvVar('var')
            uniqueVersion(false)
            evenIfUnstable()
        }

        then:
        with(context.publisherNodes[0]) {
            name() == 'hudson.maven.RedeployPublisher'
            children().size() == 5
            url[0].value() == 'foo'
            id[0].value() == 'bar'
            releaseEnvVar[0].value() == 'var'
            uniqueVersion[0].value() == false
            evenIfUnstable[0].value() == true
        }
    }

    def 'call publishers which use addStaticAnalysis*'() {
        when:
        context.tasks('**/*.java', '', 'FIXME, XXX', 'TODO', '', true)
        context.warnings(['Maven', 'Java Compiler (javac)'])

        then:
        noExceptionThrown()
    }
}
