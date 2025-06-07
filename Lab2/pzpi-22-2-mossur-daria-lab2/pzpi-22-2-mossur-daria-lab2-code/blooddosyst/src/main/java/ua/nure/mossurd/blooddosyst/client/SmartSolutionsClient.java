package ua.nure.mossurd.blooddosyst.client;

import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ua.nure.mossurd.blooddosyst.dto.SmartSolutionsRequestDto;
import ua.nure.mossurd.blooddosyst.dto.SmartSolutionsResponseDto;

@FeignClient(value = "smartsolutions", url = "${blooddo.smart-solutions.url}")
@Headers({
        "Authentication: Bearer ${blooddo.smart-solutions.secret}",
        "Accept: application/json",
        "Content-Type: application/json"
})
public interface SmartSolutionsClient {

    @PostMapping("/query")
    SmartSolutionsResponseDto queryCall(@RequestBody SmartSolutionsRequestDto requestDto);
}
