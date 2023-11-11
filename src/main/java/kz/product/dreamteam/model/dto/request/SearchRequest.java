package kz.product.dreamteam.model.dto.request;

import lombok.Data;

@Data
public class SearchRequest<T, F> {

    T filter;
    F sorting;
    PageRequest pageRequest;
}
