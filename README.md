# Boolean expression evaluated and converted to hiearchyId definitions

Note: Before starting Spring Boot's application, start MSSQL DB by running `docker compose up` command.

REST API Features:

- REST Endpoint for create/update/get `Fact` entity
- REST Endpoint for create/update/get `Id` entity
- Convert REST DTO flat structure to `Expression` AST
    - see `RestDtoToExpressionConverter` class
    - see `FlattenExpressionVisitor` class
- Convert `Expression` AST to REST DTO flat structure
    - see `ExpressionToRestDtoConverter` class
    - see `RestDtoReplaceVariablesVisitor` class

DB features:

- DB Repository for working with
  MSSQL [hierarchyid](https://docs.microsoft.com/en-us/sql/t-sql/data-types/hierarchyid-data-type-method-reference?view=sql-server-ver15)
  data type
    - see `FactExpressionRepository` class
    - see `IdExpressionRepository` class
- Convert DB entity to `Expression` AST
    - see `DefinitionToExpressionConvertor` base class
- Convert `Expression` AST to DB entity
    - see `ExpressionToDefinitionConverter` base class
- Convert `Fact` expression AST to SQL WHERE statement using [JSqlParser](https://github.com/JSQLParser/JSqlParser)
    - see `SqlGeneratorVisitor` class

DB Structure `Fact` rich data:

```sql
create table foo.EXPRESSION_FACT_ITEM
(
    ID               int identity constraint PK_EXPRESSION_FACT_ITEM primary key,
    PARENT_ID        int         not null,
    HIERARCHY        hierarchyid not null,
    TYPE             varchar(14) not null,
    LOGICAL_OPERATOR varchar(8),
    VAL_COLUMN_NAME  varchar(64),
    VAL_OPERATOR     varchar(64),
    VAL_VALUE        clob
)
```

DB Structure `Id` simple `int` artificial relation:

```sql
create table foo.EXPRESSION_ID_ITEM
(
    ID               int identity constraint PK_EXPRESSION_ID_ITEM primary key,
    PARENT_ID        int         not null,
    HIERARCHY        hierarchyid not null,
    TYPE             varchar(14) not null,
    LOGICAL_OPERATOR varchar(8),
    VAL_ID           int
)
```

Other features:

- Parse logical expression string without parentheses to detect implicit operator precedence
    - see `StringToExpressionParser` class
    - [ANTLR](https://www.antlr.org/) grammar `SimpleBoolean.g4`

---

TBD

- [ ] better description
- [ ] unit-tests

In MSSQL we
have [hierarchyid](https://docs.microsoft.com/en-us/sql/t-sql/data-types/hierarchyid-data-type-method-reference?view=sql-server-ver15)
datatype (we simulate DB entity by `ExpressionDefinition` class).

We have boolean expressions (AND, OR, NOT) and want to evaluate them.

Also, we want to convert between `ExpressionDefinition`s and `Expression`s.

We use [Visitor design pattern](https://en.wikipedia.org/wiki/Visitor_pattern) to traverse tree structures.

## EvalMainApplication sample

We define various Boolean expressions, we print them with `PrintVisitor` and we evaluate them with `EvalVisitor`.

Output looks like

```shell
true	 = true
false	 = false
false	 = !true
true	 = !false
false	 = (false)
true	 = (false OR true)
false	 = (false)
false	 = (false AND true)
false	 = ((false AND true) OR !true)
true	 = ((true AND true) OR !false)
```

## DefinitionToExpressionMain sample

We have list of `ExpressionDefinition` classes:

```java
new ExpressionDefinition("/",ExpressionDefinition.Type.OPERATOR,ExpressionDefinition.Operator.AND,null),
        new ExpressionDefinition("/1/",ExpressionDefinition.Type.VALUE,null,true),
        new ExpressionDefinition("/2/",ExpressionDefinition.Type.OPERATOR,ExpressionDefinition.Operator.OR,null),
        new ExpressionDefinition("/2/1/",ExpressionDefinition.Type.VALUE,null,true),
        new ExpressionDefinition("/2/2/",ExpressionDefinition.Type.VALUE,null,false),
        new ExpressionDefinition("/2/3/",ExpressionDefinition.Type.VALUE,null,true)
```

Then we convert them to `Expression` root node class:

```shell
(AND true (OR true false true))
```

And then we convert them back to `ExpressionDefinition` classes:

```shell
ExpressionDefinition{hierarchyId='/', type=OPERATOR, operator=AND, operand=null}
ExpressionDefinition{hierarchyId='/1/', type=VALUE, operator=null, operand=true}
ExpressionDefinition{hierarchyId='/2/', type=OPERATOR, operator=OR, operand=null}
ExpressionDefinition{hierarchyId='/2/1/', type=VALUE, operator=null, operand=true}
ExpressionDefinition{hierarchyId='/2/2/', type=VALUE, operator=null, operand=false}
ExpressionDefinition{hierarchyId='/2/3/', type=VALUE, operator=null, operand=true}
```

## Convert logical expression without parentheses to Expression

See `LogicalStringToExpressionMain` for conversion.

See `Antlr4Main` for `EvalVisitor` and `AddParenthesesVisitor`.

## Convert expression to logical string without parentheses

See `FlattenMainApplication` for conversion. 
