package com.twodollar.tdboard.modules.fileInfo.controller;

import com.twodollar.tdboard.modules.common.response.ApiCmnResponse;
import com.twodollar.tdboard.modules.fileInfo.controller.request.FileInfoRequest;
import com.twodollar.tdboard.modules.fileInfo.controller.response.FileInfoResponse;
import com.twodollar.tdboard.modules.fileInfo.entity.BoardAttach;
import com.twodollar.tdboard.modules.fileInfo.entity.FileInfo;
import com.twodollar.tdboard.modules.fileInfo.service.AttachService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AttachController {

    private final AttachService attachService;

    @Operation(summary = "파일첨부(테스트)", description = "파일첨부 테스트")
    @ApiResponses(value = {
            //@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class))),
            //@ApiResponse(responseCode = "400", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class)))
    })
    @PostMapping("/attaches")
    public ResponseEntity<ApiCmnResponse<?>> attachInsert(
            Authentication authentication,
            @RequestParam("boardId")Long boardId,
            @RequestParam("files")List<Long> files
    ){
        try {
            String userId = authentication.getName();

            List<BoardAttach> saves = attachService.createAttach(boardId, files);
            return ResponseEntity.status(HttpStatus.OK).body(ApiCmnResponse.success("success"));
        }catch(ResponseStatusException e){
            return ResponseEntity.status(e.getStatus()).body(ApiCmnResponse.error(String.valueOf(e.getStatus()), e.getReason()));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiCmnResponse.error("500", e.getMessage()));
        }
    }

    @Operation(summary = "파일첨부 수정(삭제포함)(테스트)", description = "파일첨부 수정(삭제포함)(테스트)")
    @ApiResponses(value = {
            //@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class))),
            //@ApiResponse(responseCode = "400", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class)))
    })
    @PutMapping("/attaches")
    public ResponseEntity<ApiCmnResponse<?>> attachUpdate(
            Authentication authentication,
            @RequestParam("boardId")Long boardId,
            @RequestParam("files")List<Long> files
    ){
        try {
            String userId = authentication.getName();
            List<BoardAttach> saves = attachService.createUpdate(boardId, files);
            return ResponseEntity.status(HttpStatus.OK).body(ApiCmnResponse.success("success"));
        }catch(ResponseStatusException e){
            return ResponseEntity.status(e.getStatus()).body(ApiCmnResponse.error(String.valueOf(e.getStatus()), e.getReason()));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiCmnResponse.error("500", e.getMessage()));
        }
    }

    @Operation(summary = "파일첨부 목록 조회(테스트)", description = "파일첨부 목록 조회")
    @ApiResponses(value = {
            //@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class))),
            //@ApiResponse(responseCode = "400", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class)))
    })
    @GetMapping("/attaches")
    public ResponseEntity<ApiCmnResponse<?>> attachList(
            Authentication authentication,
            @RequestParam("boardId")Long boardId
    ){
        try {
            String userId = authentication.getName();

            List<FileInfo> files = attachService.getAttaches(boardId);
            List<FileInfoResponse> fileResponses = files.stream().map(fileInfo -> fileInfo.toResponse()).collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.OK).body(ApiCmnResponse.success(fileResponses));
        }catch(ResponseStatusException e){
            return ResponseEntity.status(e.getStatus()).body(ApiCmnResponse.error(String.valueOf(e.getStatus()), e.getReason()));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiCmnResponse.error("500", e.getMessage()));
        }
    }
}
