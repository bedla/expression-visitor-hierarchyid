package cz.bedla.hierarchyid.service;

import cz.bedla.hierarchyid.repository.IdExpressionRepository;
import cz.bedla.hierarchyid.rest.id.IdExpressionDto;
import cz.bedla.hierarchyid.rest.id.IdExpressionToRestDtoConverter;
import cz.bedla.hierarchyid.rest.id.IdRestDtoToExpressionConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class IdExpressionService {
    private final IdExpressionRepository repository;
    private final IdRestDtoToExpressionConverter restDtoToExpressionConverter;
    private final IdExpressionToRestDtoConverter idExpressionToRestDtoConverter;

    public IdExpressionService(
            IdExpressionRepository repository,
            IdRestDtoToExpressionConverter restDtoToExpressionConverter,
            IdExpressionToRestDtoConverter idExpressionToRestDtoConverter
    ) {
        this.repository = repository;
        this.restDtoToExpressionConverter = restDtoToExpressionConverter;
        this.idExpressionToRestDtoConverter = idExpressionToRestDtoConverter;
    }

    public Optional<List<IdExpressionDto>> getExpression(int parentId) {
        return repository.getExpression(parentId)
                .map(idExpressionToRestDtoConverter::convert);
    }

    public int insertExpression(List<IdExpressionDto> list) {
        var expression = restDtoToExpressionConverter.convert(list);
        return repository.insertExpression(expression);
    }

    public boolean updateExpression(List<IdExpressionDto> list, int parentId) {
        var expression = restDtoToExpressionConverter.convert(list);
        return repository.updateExpression(expression, parentId);
    }
}
