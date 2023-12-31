package com.twodollar.tdboard.modules.common.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class ApiCmnResponse<T> {

    private static final String SUCCESS_STATUS = "200";
    private static final String ERROR_STATUS = "500";

    private String status;
    private T data;
    private String message;

    public static <T> ApiCmnResponse<T> success(T data) {
        return new ApiCmnResponse<>(SUCCESS_STATUS, data, "Success");
    }

    public static ApiCmnResponse<?> successWithNoContent() {
        return new ApiCmnResponse<>(SUCCESS_STATUS, null, "Success");
    }

    // Hibernate Validator에 의해 유효하지 않은 데이터로 인해 API 호출이 거부될때 반환
    public static ApiCmnResponse<?> fail(BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();

        List<ObjectError> allErrors = bindingResult.getAllErrors();
        for (ObjectError error : allErrors) {
            if (error instanceof FieldError) {
                errors.put(((FieldError) error).getField(), error.getDefaultMessage());
            } else {
                errors.put( error.getObjectName(), error.getDefaultMessage());
            }
        }
        return new ApiCmnResponse<>(ERROR_STATUS, errors, "errors");
    }

    // 예외 발생으로 API 호출 실패시 반환
    public static ApiCmnResponse<?> error(String message) {
        return new ApiCmnResponse<>(ERROR_STATUS, null, message);
    }
    public static ApiCmnResponse<?> error(String status, String message) {
        return new ApiCmnResponse<>(status, null, message);
    }

    private ApiCmnResponse(String status, T data, String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

}
