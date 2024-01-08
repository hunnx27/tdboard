package com.twodollar.tdboard.modules.board.controller;

import com.twodollar.tdboard.modules.board.controller.response.BoardResponse;
import com.twodollar.tdboard.modules.board.entity.Board;
import com.twodollar.tdboard.modules.board.entity.enums.BoardTypeEnum;
import com.twodollar.tdboard.modules.board.service.BoardService;
import com.twodollar.tdboard.modules.common.dto.CustomPageImpl;
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
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    public ResponseEntity<ApiCmnResponse<CustomPageImpl<BoardResponse>>> noticeAll(
            Pageable pageable
    ){
        int totalSize = boardService.getTotalBoardSize(BoardTypeEnum.NOTICE);
        List<Board> boardList = boardService.getNoticeBoards(pageable);
        List<BoardResponse> boardResponseList = boardList.stream().map(board -> board.toResponse()).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(ApiCmnResponse.success(new CustomPageImpl<>(boardResponseList, pageable, totalSize)));
    }

    @Operation(summary = "공지사항 상세 조회", description = "공지사항 상세 조회")
    @ApiResponses(value = {
            //@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class))),
            //@ApiResponse(responseCode = "400", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class)))
    })
    @GetMapping("/boards/type/notices/{id}")
    public ResponseEntity<ApiCmnResponse<BoardResponse>> noticeDetail(
            @PathVariable("id") Long id
    ){
        Board board = boardService.getBoardById(id, BoardTypeEnum.NOTICE);
        return ResponseEntity.status(HttpStatus.OK).body(ApiCmnResponse.success(board.toResponse()));
    }

    @Operation(summary = "공지사항 검색", description = "공지사항 검색")
    @ApiResponses(value = {
            //@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class))),
            //@ApiResponse(responseCode = "400", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class)))
    })
    @GetMapping("/boards/type/notices/search")
    public ResponseEntity<ApiCmnResponse<Page<BoardResponse>>> noticeSearch(
            @RequestParam(value = "searchCode",defaultValue = "title")String searchCode,
            @RequestParam(value = "keyword", defaultValue = "") String keyword,
            Pageable pageable

    ){
        int totalSize = boardService.getTotalBoardSize(BoardTypeEnum.NOTICE);
        List<Board> boardList = null;
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
        List<BoardResponse> boardResponseList = boardList.stream().map(board -> board.toResponse()).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(ApiCmnResponse.success(new CustomPageImpl<>(boardResponseList, pageable, totalSize)));
    }

    @Operation(summary = "자료실 전체 조회", description = "자료실 전체 조회")
    @ApiResponses(value = {
            //@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class))),
            //@ApiResponse(responseCode = "400", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class)))
    })
    @GetMapping("/boards/type/datas")
    public ResponseEntity<ApiCmnResponse<CustomPageImpl<BoardResponse>>> dataAll(
            Pageable pageable
    ){
        int totalSize = boardService.getTotalBoardSize(BoardTypeEnum.DATA);
        List<Board> boardList = boardService.getDataBoards(pageable);
        List<BoardResponse> boardResponseList = boardList.stream().map(board -> board.toResponse()).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(ApiCmnResponse.success(new CustomPageImpl<>(boardResponseList, pageable, totalSize)));
    }
    @Operation(summary = "자료실 상세 조회", description = "자료실 상세 조회")
    @ApiResponses(value = {
            //@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class))),
            //@ApiResponse(responseCode = "400", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class)))
    })
    @GetMapping("/boards/type/datas/{id}")
    public ResponseEntity<ApiCmnResponse<BoardResponse>> dataDetail(
            @PathVariable("id") Long id
    ){
        Board board = boardService.getBoardById(id, BoardTypeEnum.DATA);
        return ResponseEntity.status(HttpStatus.OK).body(ApiCmnResponse.success(board.toResponse()));
    }

    @Operation(summary = "FAQ 전체 조회", description = "FAQ 전체 조회")
    @ApiResponses(value = {
            //@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class))),
            //@ApiResponse(responseCode = "400", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class)))
    })
    @GetMapping("/boards/type/faqs")
    public ResponseEntity<ApiCmnResponse<CustomPageImpl<BoardResponse>>> faqAll(
            Pageable pageable
    ){
        int totalSize = boardService.getTotalBoardSize(BoardTypeEnum.FAQ);
        List<Board> boardList = boardService.getFAQBoards(pageable);
        List<BoardResponse> boardResponseList = boardList.stream().map(board -> board.toResponse()).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(ApiCmnResponse.success(new CustomPageImpl<>(boardResponseList, pageable, totalSize)));
    }
    @Operation(summary = "FAQ 상세 조회", description = "FAQ 상세 조회")
    @ApiResponses(value = {
            //@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class))),
            //@ApiResponse(responseCode = "400", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class)))
    })
    @GetMapping("/boards/type/faqs/{id}")
    public ResponseEntity<ApiCmnResponse<BoardResponse>> faqDetail(
            @PathVariable("id") Long id
    ){
        Board board = boardService.getBoardById(id, BoardTypeEnum.FAQ);
        return ResponseEntity.status(HttpStatus.OK).body(ApiCmnResponse.success(board.toResponse()));
    }

    @Operation(summary = "QNA 전체 조회", description = "QNA 전체 조회")
    @ApiResponses(value = {
            //@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class))),
            //@ApiResponse(responseCode = "400", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class)))
    })
    @GetMapping("/boards/type/qnas")
    public ResponseEntity<ApiCmnResponse<CustomPageImpl<BoardResponse>>> qnaAll(
            Pageable pageable
    ){
        int totalSize = boardService.getTotalBoardSize(BoardTypeEnum.QNA);
        List<Board> boardList = boardService.getQNABoards(pageable);
        List<BoardResponse> boardResponseList = boardList.stream().map(board -> board.toResponse()).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(ApiCmnResponse.success(new CustomPageImpl<>(boardResponseList, pageable, totalSize)));
    }
    @Operation(summary = "QNA 상세 조회", description = "QNA 상세 조회")
    @ApiResponses(value = {
            //@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class))),
            //@ApiResponse(responseCode = "400", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class)))
    })
    @GetMapping("/boards/type/qnas/{id}")
    public ResponseEntity<ApiCmnResponse<BoardResponse>> qnaDetail(
            @PathVariable("id") Long id
    ){
        Board board = boardService.getBoardById(id, BoardTypeEnum.QNA);
        return ResponseEntity.status(HttpStatus.OK).body(ApiCmnResponse.success(board.toResponse()));
    }



}
