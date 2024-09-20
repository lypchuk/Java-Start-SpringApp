package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaginationResponse<T>  {
    private Iterable<T> itemsList;
    private long totalElements ;
}