package com.parimal.e_wholesaler.sales_service.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PairDTO<A, B> {
    
    private A data1;
    
    private B data2;
    
}
