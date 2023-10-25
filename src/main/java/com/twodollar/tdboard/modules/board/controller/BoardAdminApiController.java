package com.twodollar.tdboard.modules.board.controller;

import com.twodollar.tdboard.modules.board.entity.Board;
import com.twodollar.tdboard.modules.board.service.BoardService;
import com.twodollar.tdboard.modules.common.response.ApiCmnResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class BoardAdminApiController {

    private final BoardService boardService;

    @Operation(summary = "공지사항 전체 조회", description = "공지사항 전체 조회")
    @ApiResponses(value = {
            //@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class))),
            //@ApiResponse(responseCode = "400", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class)))
    })
    @GetMapping("/boards/type/notices")
    public ResponseEntity<ApiCmnResponse<List<Board>>> noticeAll(){
        return ResponseEntity.status(HttpStatus.OK).body(ApiCmnResponse.success(boardService.getNoticeBoards()));
    }

    @Operation(summary = "공지사항 검색", description = "공지사항 검색")
    @ApiResponses(value = {
            //@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class))),
            //@ApiResponse(responseCode = "400", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class)))
    })
    @GetMapping("/boards/type/notices/search")
    public ResponseEntity<ApiCmnResponse<List<Board>>> noticeSearch(
            @RequestParam("searchCode")String searchCode,
            @RequestParam("keyword") String keyword){
        List<Board> boardList = new ArrayList<>();
            switch (searchCode){
                case "title":
                    boardList = boardService.getNoticeBoardsWithTitle(keyword);
                    break;
                case "context":
                    boardList = boardService.getNoticeBoardsWithContext(keyword);
                    break;
                default:
                    log.error("Keyword not matching");
                    throw new IllegalArgumentException ("Codes only 'title' and 'context' are avaiable.");
            }
        return ResponseEntity.status(HttpStatus.OK).body(ApiCmnResponse.success(boardList));
    }




}
