package tg.DocVers.Controller.Advice;

import jakarta.annotation.Nullable;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import tg.DocVers.DTO.External.ErrorResponse;
import tg.DocVers.DTO.External.ReturnModel;
import tools.jackson.databind.ObjectMapper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ModelResponseAdivice implements ResponseBodyAdvice<Object> {
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public @Nullable Object beforeBodyWrite(@Nullable Object body,
                                            MethodParameter returnType,
                                            MediaType selectedContentType,
                                            Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                            ServerHttpRequest request, ServerHttpResponse response) {

        if (body == null) return null;
        if (body instanceof ReturnModel) return body;

        String limitHeader = request.getHeaders().getFirst("limit");
        String batchHeader = request.getHeaders().getFirst("batch");

        int limit = (limitHeader != null && !limitHeader.isEmpty()) ? Integer.parseInt(limitHeader) : 10;
        int batch = (batchHeader != null && !batchHeader.isEmpty()) ? Integer.parseInt(batchHeader) : 1;

        ReturnModel returnModel;

        if (body instanceof ErrorResponse errorResponse) {
            returnModel = new ReturnModel(
                    errorResponse.status(),
                    request.getURI().getPath(),
                    false,
                    null,
                    null,
                    errorResponse
            );
        } else {
            if (body instanceof List<?> lista) {
                returnModel = new ReturnModel(
                        200,
                        request.getURI().getPath(),
                        true,
                        lista.size(),
                        lista,
                        null
                );
            } else {
                returnModel = new ReturnModel(
                        200,
                        request.getURI().getPath(),
                        true,
                        1,
                        Collections.singletonList(body),
                        null
                );
            }
        }

        if (StringHttpMessageConverter.class.isAssignableFrom(selectedConverterType)) {
            try {
                response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
                return mapper.writeValueAsString(returnModel);
            } catch (Exception e) {
                throw new RuntimeException("Erro ao serializar ReturnModel para JSON", e);
            }
        }

        return returnModel;
    }
}