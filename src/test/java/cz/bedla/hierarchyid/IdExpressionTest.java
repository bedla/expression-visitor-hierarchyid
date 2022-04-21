package cz.bedla.hierarchyid;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.apache.commons.lang3.StringUtils.substringAfterLast;
import static org.apache.commons.lang3.math.NumberUtils.toInt;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.startsWith;

class IdExpressionTest extends BaseTestcontainersTest {
    @Test
    void createExpressionId() {
        int id = createIdExpression(List.of(
                Map.of(
                        // "leftOperator", null,
                        "id", 123,
                        "rightOperator", "AND"
                ),
                Map.of(
                        "leftOperator", "NOT",
                        "id", 456,
                        "rightOperator", "OR"
                ),
                Map.of(
                        // "leftOperator", null,
                        "id", 789
                        // "rightOperator", "AND"
                )
        ));

        get("/id-expression/{id}", id)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("$", hasSize(3))
                .body("[0].leftOperator", nullValue())
                .body("[0].id", equalTo(123))
                .body("[0].rightOperator", equalTo("AND"))
                .body("[1].leftOperator", equalTo("NOT"))
                .body("[1].id", equalTo(456))
                .body("[1].rightOperator", equalTo("OR"))
                .body("[2].leftOperator", nullValue())
                .body("[2].id", equalTo(789))
                .body("[2].rightOperator", nullValue());
    }

    @Test
    void updateExpressionId() {
        int id = createIdExpression(List.of(
                Map.of(
                        // "leftOperator", null,
                        "id", 111,
                        "rightOperator", "OR"
                ),
                Map.of(
                        "leftOperator", "NOT",
                        "id", 222,
                        "rightOperator", "AND"
                ),
                Map.of(
                        "leftOperator", "NOT",
                        "id", 333
                        // "rightOperator", "AND"
                )
        ));

        given()
                .contentType(ContentType.JSON)
                .body(List.of(
                        Map.of(
                                "leftOperator", "NOT",
                                "id", 999
                                // "rightOperator", null
                        )
                ))
                .put("/id-expression/{id}", id)
                .then()
                .statusCode(HttpStatus.ACCEPTED.value());

        get("/id-expression/{id}", id)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("$", hasSize(1))
                .body("[0].leftOperator", equalTo("NOT"))
                .body("[0].id", equalTo(999))
                .body("[0].rightOperator", nullValue());
    }

    @Test
    void updateInvalidExpressionId() {
        given()
                .contentType(ContentType.JSON)
                .body(List.of(
                        Map.of(
                                "leftOperator", "NOT",
                                "id", 999
                                // "rightOperator", null
                        )
                ))
                .put("/id-expression/{id}", 999999)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void invalidExpressionId() {
        get("/id-expression/{id}", 999999)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());

    }

    private int createIdExpression(List<Map<String, Object>> body) {
        var location = given()
                .contentType(ContentType.JSON)
                .body(body)
                .post("/id-expression")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .header(HttpHeaders.LOCATION, startsWith("/id-expression/"))
                .extract()
                .header(HttpHeaders.LOCATION);
        return toInt(substringAfterLast(location, "/"));
    }
}
