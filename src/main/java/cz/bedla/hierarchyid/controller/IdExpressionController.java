package cz.bedla.hierarchyid.controller;

import cz.bedla.hierarchyid.rest.id.IdExpressionDto;
import cz.bedla.hierarchyid.service.IdExpressionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
public class IdExpressionController {
    private final IdExpressionService service;

    public IdExpressionController(IdExpressionService service) {
        this.service = service;
    }

    @GetMapping("/id-expression/{parentId}")
    public ResponseEntity<List<IdExpressionDto>> findIds(@PathVariable("parentId") int parentId) {
        return service.getExpression(parentId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/id-expression")
    public ResponseEntity<Void> createIds(@RequestBody List<IdExpressionDto> requestList) {
        var parentId = service.insertExpression(requestList);
        return ResponseEntity.created(URI.create("/id-expression/" + parentId)).build();
    }

    @PutMapping("/id-expression/{parentId}")
    public ResponseEntity<Void> editIds(
            @PathVariable("parentId") int parentId,
            @RequestBody List<IdExpressionDto> requestList
    ) {
        var updated = service.updateExpression(requestList, parentId);
        return updated
                ? ResponseEntity.accepted().build()
                : ResponseEntity.notFound().build();
    }
}
