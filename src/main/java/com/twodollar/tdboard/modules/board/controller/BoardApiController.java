package com.twodollar.tdboard.modules.board.controller;

import com.twodollar.tdboard.modules.board.entity.Board;
import com.twodollar.tdboard.modules.board.service.BoardService;
import com.twodollar.tdboard.modules.common.response.ApiCmnResponse;
import io.swagger.annotations.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class BoardApiController {

    private final BoardService boardService;

    @Operation(summary = "공지사항 전체 조회", description = "공지사항 전체 조회")
    @ApiResponses(value = {
            //@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class))),
            //@ApiResponse(responseCode = "400", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class)))
    })
    @GetMapping("/boards/type/notices")
    public ResponseEntity<ApiCmnResponse<Page<Board>>> noticeAll(
            @RequestParam(value="page", defaultValue="0") int page,
            @RequestParam(value="size", defaultValue="10", required = false) int size
    ){
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(ApiCmnResponse.success(boardService.getNoticeBoards(pageable)));
    }

    @Operation(summary = "공지사항 검색", description = "공지사항 검색")
    @ApiResponses(value = {
            //@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class))),
            //@ApiResponse(responseCode = "400", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class)))
    })
    @GetMapping("/boards/type/notices/search")
    public ResponseEntity<ApiCmnResponse<Page<Board>>> noticeSearch(
            @RequestParam("searchCode")String searchCode,
            @RequestParam("keyword") String keyword,
            @RequestParam(value="page", defaultValue="0") int page,
            @RequestParam(value="size", defaultValue="10", required = false) int size

    ){
        Pageable pageable = PageRequest.of(page, size);
        Page<Board> boardList = Page.empty();
            switch (searchCode){
                case "title":
                    boardList = boardService.getNoticeBoardsWithTitle(keyword, pageable);
                    break;
                case "context":
                    boardList = boardService.getNoticeBoardsWithContext(keyword, pageable);
                    break;
                default:
                    log.error("Keyword not matching");
                    throw new IllegalArgumentException ("Codes only 'title' and 'context' are avaiable.");
            }
        return ResponseEntity.status(HttpStatus.OK).body(ApiCmnResponse.success(boardList));
    }



}
