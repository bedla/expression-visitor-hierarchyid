package cz.bedla.hierarchyid;

import io.restassured.RestAssured;
import io.restassured.filter.log.LogDetail;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MSSQLServerContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.wait.strategy.LogMessageWaitStrategy;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;
import java.time.Duration;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class BaseTestcontainersTest {
    private static final String mssqlAlias = "mssql-db";

    private static final Network network = Network.newNetwork();
    private static final MSSQLServerContainer<?> mssqlServerContainer = new MSSQLServerContainer<>(DockerImageName.parse("mcr.microsoft.com/mssql/server:2019-latest"))
            .acceptLicense()
            .withNetwork(network)
            .withNetworkAliases(mssqlAlias)
            .withFileSystemBind("./sql", "/db-migration");

    private static final GenericContainer<?> liquibaseContainer = new GenericContainer<>(DockerImageName.parse("liquibase/liquibase"))
            .withCommand(
                    "--url=jdbc:sqlserver://" + mssqlAlias + ":1433;database=ExprDB",
                    "--changeLogFile=./changelog/changelog.xml",
                    "--username=" + mssqlServerContainer.getUsername(),
                    "--password=" + mssqlServerContainer.getPassword(),
                    "update")
            .withFileSystemBind("./sql", "/liquibase/changelog")
            .waitingFor(new LogMessageWaitStrategy()
                    .withRegEx("Liquibase command '.+' was executed successfully\\.\\n")
                    .withStartupTimeout(Duration.ofSeconds(30)))
            .withNetwork(network)
            .dependsOn(mssqlServerContainer);

    @DynamicPropertySource
    static void setupDynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> mssqlServerContainer.getJdbcUrl() + ";database=ExprDB");
        registry.add("spring.datasource.username", mssqlServerContainer::getUsername);
        registry.add("spring.datasource.password", mssqlServerContainer::getPassword);
    }

    @LocalServerPort
    protected int localServerPort;

    @BeforeAll
    static void beforeAll() throws IOException, InterruptedException {
        mssqlServerContainer.start();
        runInContainer(mssqlServerContainer,
                "/opt/mssql-tools/bin/sqlcmd",
                "-S", "mssql-db,1433",
                "-U", mssqlServerContainer.getUsername(),
                "-P", mssqlServerContainer.getPassword(),
                "-i", "/db-migration/create-db.sql");
        liquibaseContainer.start();
    }

    @BeforeEach
    void setUp() {
        RestAssured.port = localServerPort;
        RestAssured.basePath = "/";
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails(LogDetail.ALL);

        runInContainer(mssqlServerContainer,
                "/opt/mssql-tools/bin/sqlcmd",
                "-S", "mssql-db,1433",
                "-U", mssqlServerContainer.getUsername(),
                "-P", mssqlServerContainer.getPassword(),
                "-d", "ExprDB",
                "-Q", "\"TRUNCATE TABLE foo.EXPRESSION_FACT_ITEM, foo.EXPRESSION_ID_ITEM;\"");
    }

    private static void runInContainer(GenericContainer<?> container, String... command) {
        try {
            var execResult = container.execInContainer(command);
            if (execResult.getExitCode() != 0) {
                throw new IllegalStateException("Unable to run " + command[0] + "\n\n" + execResult);
            }
        } catch (IOException | InterruptedException e) {
            ExceptionUtils.rethrow(e);
        }
    }
}
