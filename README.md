# Boolean expression evaluated and converted to hiearchyId definitions

TBD

- [ ] better description
- [ ] unit-tests
- [ ] conversion between Expression and Jackson API DTOs

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
