package cz.bedla.hierarchyid.service;

import cz.bedla.hierarchyid.repository.FactExpressionRepository;
import cz.bedla.hierarchyid.rest.fact.FactExpressionDto;
import cz.bedla.hierarchyid.rest.fact.FactExpressionToRestDtoConverter;
import cz.bedla.hierarchyid.rest.fact.FactRestDtoToExpressionConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FactExpressionService {
    private final FactExpressionRepository repository;
    private final FactRestDtoToExpressionConverter restDtoToExpressionConverter;
    private final FactExpressionToRestDtoConverter expressionToRestDtoConverter;

    public FactExpressionService(
            FactExpressionRepository repository,
            FactRestDtoToExpressionConverter restDtoToExpressionConverter,
            FactExpressionToRestDtoConverter expressionToRestDtoConverter
    ) {
        this.repository = repository;
        this.restDtoToExpressionConverter = restDtoToExpressionConverter;
        this.expressionToRestDtoConverter = expressionToRestDtoConverter;
    }

    public Optional<List<FactExpressionDto>> getExpression(int parentId) {
        return repository.getExpression(parentId)
                .map(expressionToRestDtoConverter::convert);
    }

    public int insertExpression(List<FactExpressionDto> list) {
        var expression = restDtoToExpressionConverter.convert(list);
        return repository.insertExpression(expression);
    }

    public boolean updateExpression(List<FactExpressionDto> list, int parentId) {
        var expression = restDtoToExpressionConverter.convert(list);
        return repository.updateExpression(expression, parentId);
    }
}
