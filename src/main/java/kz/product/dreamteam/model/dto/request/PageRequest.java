package kz.product.dreamteam.model.dto.request;

import lombok.Data;

@Data
public class PageRequest {

    private Integer page;
    private Integer limit;
}
