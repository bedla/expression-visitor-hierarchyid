package cz.bedla.hierarchyid.config;

import cz.bedla.hierarchyid.db.fact.FactDefinitionToExpressionConvertor;
import cz.bedla.hierarchyid.db.fact.FactExpressionToDefinitionConverter;
import cz.bedla.hierarchyid.db.id.IdDefinitionToExpressionConvertor;
import cz.bedla.hierarchyid.db.id.IdExpressionToDefinitionConverter;
import cz.bedla.hierarchyid.rest.fact.FactExpressionToRestDtoConverter;
import cz.bedla.hierarchyid.rest.fact.FactRestDtoToExpressionConverter;
import cz.bedla.hierarchyid.rest.id.IdExpressionToRestDtoConverter;
import cz.bedla.hierarchyid.rest.id.IdRestDtoToExpressionConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {
    @Bean
    FactDefinitionToExpressionConvertor factDefinitionToExpressionConvertor() {
        return new FactDefinitionToExpressionConvertor();
    }

    @Bean
    FactExpressionToDefinitionConverter factExpressionToDefinitionConverter() {
        return new FactExpressionToDefinitionConverter();
    }

    @Bean
    FactRestDtoToExpressionConverter factRestDtoToExpressionConverter() {
        return new FactRestDtoToExpressionConverter();
    }

    @Bean
    FactExpressionToRestDtoConverter factExpressionToRestDtoConverter() {
        return new FactExpressionToRestDtoConverter();
    }

    @Bean
    IdDefinitionToExpressionConvertor idDefinitionToExpressionConvertor() {
        return new IdDefinitionToExpressionConvertor();
    }

    @Bean
    IdExpressionToDefinitionConverter idExpressionToDefinitionConverter() {
        return new IdExpressionToDefinitionConverter();
    }

    @Bean
    IdRestDtoToExpressionConverter idRestDtoToExpressionConverter() {
        return new IdRestDtoToExpressionConverter();
    }

    @Bean
    IdExpressionToRestDtoConverter idExpressionToRestDtoConverter() {
        return new IdExpressionToRestDtoConverter();
    }
}
