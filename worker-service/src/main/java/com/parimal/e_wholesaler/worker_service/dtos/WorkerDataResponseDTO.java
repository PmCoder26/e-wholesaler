package com.parimal.e_wholesaler.worker_service.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkerDataResponseDTO {

    private Long workerId;

    private Long shopId;

}
