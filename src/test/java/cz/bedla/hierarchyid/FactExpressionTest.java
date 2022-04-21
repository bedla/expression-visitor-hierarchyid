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

class FactExpressionTest extends BaseTestcontainersTest {
    @Test
    void createExpressionFact() {
        int id = createFactExpression(List.of(
                Map.of(
                        // "leftOperator", null,
                        "columnName", "COL_AAA",
                        "operator", "EQ",
                        "value", "foo",
                        "rightOperator", "AND"
                ),
                Map.of(
                        "leftOperator", "NOT",
                        "columnName", "COL_BBB",
                        "operator", "LT",
                        "value", 123,
                        "rightOperator", "OR"
                ),
                Map.of(
                        // "leftOperator", null,
                        "columnName", "COL_CCC",
                        "operator", "GTE",
                        "value", 1000
                        // "rightOperator", "AND"
                )
        ));

        get("/fact-expression/{id}", id)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("$", hasSize(3))
                .body("[0].leftOperator", nullValue())
                .body("[0].columnName", equalTo("COL_AAA"))
                .body("[0].operator", equalTo("EQ"))
                .body("[0].value", equalTo("foo"))
                .body("[0].rightOperator", equalTo("AND"))
                .body("[1].leftOperator", equalTo("NOT"))
                .body("[1].columnName", equalTo("COL_BBB"))
                .body("[1].operator", equalTo("LT"))
                .body("[1].value", equalTo(123))
                .body("[1].rightOperator", equalTo("OR"))
                .body("[2].leftOperator", nullValue())
                .body("[2].columnName", equalTo("COL_CCC"))
                .body("[2].operator", equalTo("GTE"))
                .body("[2].value", equalTo(1000))
                .body("[2].rightOperator", nullValue());
    }

    @Test
    void updateExpressionFact() {
        int id = createFactExpression(List.of(
                Map.of(
                        // "leftOperator", null,
                        "columnName", "COL_AAA",
                        "operator", "EQ",
                        "value", "foo",
                        "rightOperator", "OR"
                ),
                Map.of(
                        "leftOperator", "NOT",
                        "columnName", "COL_BBB",
                        "operator", "LT",
                        "value", 123,
                        "rightOperator", "AND"
                ),
                Map.of(
                        "leftOperator", "NOT",
                        "columnName", "COL_CCC",
                        "operator", "GTE",
                        "value", 1000
                        // "rightOperator", "AND"
                )
        ));

        given()
                .contentType(ContentType.JSON)
                .body(List.of(
                        Map.of(
                                "leftOperator", "NOT",
                                "columnName", "IVOS",
                                "operator", "EQ",
                                "value", "hello"
                                // "rightOperator", null
                        )
                ))
                .put("/fact-expression/{id}", id)
                .then()
                .statusCode(HttpStatus.ACCEPTED.value());

        get("/fact-expression/{id}", id)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("$", hasSize(1))
                .body("[0].leftOperator", equalTo("NOT"))
                .body("[0].columnName", equalTo("IVOS"))
                .body("[0].operator", equalTo("EQ"))
                .body("[0].value", equalTo("hello"))
                .body("[0].rightOperator", nullValue());
    }

    @Test
    void updateInvalidExpressionFact() {
        given()
                .contentType(ContentType.JSON)
                .body(List.of(
                        Map.of(
                                "leftOperator", "NOT",
                                "columnName", "IVOS",
                                "operator", "EQ",
                                "value", "hello"
                                // "rightOperator", null
                        )
                ))
                .put("/fact-expression/{id}", 999999)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void invalidExpressionFact() {
        get("/fact-expression/{id}", 999999)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());

    }

    private int createFactExpression(List<Map<String, Object>> body) {
        var location = given()
                .contentType(ContentType.JSON)
                .body(body)
                .post("/fact-expression")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .header(HttpHeaders.LOCATION, startsWith("/fact-expression/"))
                .extract()
                .header(HttpHeaders.LOCATION);
        return toInt(substringAfterLast(location, "/"));
    }
}
