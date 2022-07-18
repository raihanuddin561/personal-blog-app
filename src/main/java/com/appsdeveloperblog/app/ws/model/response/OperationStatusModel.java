package com.appsdeveloperblog.app.ws.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OperationStatusModel {
    private String operationResult;
    private String operationName;
}
