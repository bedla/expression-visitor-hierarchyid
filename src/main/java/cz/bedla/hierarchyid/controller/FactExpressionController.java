package cz.bedla.hierarchyid.controller;

import cz.bedla.hierarchyid.rest.fact.FactExpressionDto;
import cz.bedla.hierarchyid.rest.fact.FactSqlDto;
import cz.bedla.hierarchyid.service.FactExpressionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
public class FactExpressionController {
    private final FactExpressionService service;

    public FactExpressionController(FactExpressionService service) {
        this.service = service;
    }

    @GetMapping("/fact-expression/{parentId}")
    public ResponseEntity<List<FactExpressionDto>> findFacts(@PathVariable("parentId") int parentId) {
        return service.getExpression(parentId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/fact-expression")
    public ResponseEntity<Void> createFacts(@RequestBody List<FactExpressionDto> requestList) {
        var parentId = service.insertExpression(requestList);
        return ResponseEntity.created(URI.create("/fact-expression/" + parentId)).build();
    }

    @GetMapping("/fact-expression/{parentId}/sql")
    public ResponseEntity<FactSqlDto> generateSql(@PathVariable("parentId") int parentId, @RequestParam("tableName") String tableName) {
        return service.generateSql(parentId, tableName)
                .map(FactSqlDto::new)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/fact-expression/{parentId}")
    public ResponseEntity<Void> editFacts(
            @PathVariable("parentId") int parentId,
            @RequestBody List<FactExpressionDto> requestList
    ) {
        var updated = service.updateExpression(requestList, parentId);
        return updated
                ? ResponseEntity.accepted().build()
                : ResponseEntity.notFound().build();
    }
}
