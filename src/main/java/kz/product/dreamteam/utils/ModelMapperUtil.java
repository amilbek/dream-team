package kz.product.dreamteam.utils;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

public class ModelMapperUtil {

    private static final ModelMapper modelMapper;

    static {
        modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    private ModelMapperUtil() {
    }

    public static <D, T> D map(final T entity, Class<D> outClass) {
        if (entity == null) {
            return null;
        }
        return modelMapper.map(entity, outClass);
    }
}
