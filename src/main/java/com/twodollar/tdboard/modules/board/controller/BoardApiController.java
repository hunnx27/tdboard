package com.twodollar.tdboard.modules.board.controller;

import com.twodollar.tdboard.modules.board.controller.request.BoardRequest;
import com.twodollar.tdboard.modules.board.controller.response.BoardResponse;
import com.twodollar.tdboard.modules.board.entity.Board;
import com.twodollar.tdboard.modules.board.entity.enums.BoardTypeEnum;
import com.twodollar.tdboard.modules.board.service.BoardService;
import com.twodollar.tdboard.modules.common.dto.CustomPageImpl;
import com.twodollar.tdboard.modules.common.response.ApiCmnResponse;
import com.twodollar.tdboard.modules.user.entity.User;
import com.twodollar.tdboard.modules.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
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
public class BoardApiController {

    private final BoardService boardService;
    private final UserService userService;

    /*
        공통
     */
    @Operation(summary = "게시글 등록", description = "게시글 등록")
    @ApiResponses(value = {
            //@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class))),
            //@ApiResponse(responseCode = "400", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class)))
    })
    @PostMapping("/boards")
    public ResponseEntity<ApiCmnResponse<?>> boardRegister(
            @RequestBody BoardRequest boardRequest,
            Authentication authentication
    ){
        try {
            // Validation
            // 1. 관리자는 모든 글을 등록 가능하다.
            // 2. QNA는 사용자가 글을 등록할 수 있다.
            User user = userService.getUserByUserId(authentication.getName());
            String boardType = boardRequest.getBoardType();
            switch (user.getRole().name()){
                case "ROLE_ADMIN":
                    break;
                default:
                    if(!"QNA".equals(boardType)){
                        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "게시글 등록에 대한 권한이 없습니다.");
                    }
                    break;
            }

            Board board = boardService.createBoard(boardRequest);
            return ResponseEntity.status(HttpStatus.OK).body(ApiCmnResponse.success(board.toResponse()));
        }catch(ResponseStatusException e){
            return ResponseEntity.status(e.getStatus()).body(ApiCmnResponse.error(String.valueOf(e.getStatus()), e.getReason()));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiCmnResponse.error("500", e.getMessage()));
        }
    }
    @Operation(summary = "게시글 수정", description = "게시글 수정")
    @ApiResponses(value = {
            //@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class))),
            //@ApiResponse(responseCode = "400", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class)))
    })
    @PutMapping("/boards/{id}")
    public ResponseEntity<ApiCmnResponse<?>> boardUpdate(
            @PathVariable("id") Long id,
            @RequestBody BoardRequest boardRequest,
            Authentication authentication
    ){
        try {
            // Validation
            // 1. 관리자는 모든 글을 수정 가능하다.
            // 2. QNA는 사용자가 글을 수정할 수 있지만 본인 글만 수정할 수 있다.
            User user = userService.getUserByUserId(authentication.getName());
            Board valboard = boardService.getBoardById(id, BoardTypeEnum.valueOf(boardRequest.getBoardType()));
            switch (user.getRole().name()){
                case "ROLE_ADMIN":
                    break;
                default:
                    if(!"QNA".equals(valboard.getBoardType().name())){
                        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "게시글 수정에 대한 권한이 없습니다.");
                    }
                    if(user.getId() != valboard.getUserId()){
                        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, " 게시글 수정은 본인만 수정할 수 있습니다.");
                    }
                    break;
            }

            Board board = boardService.updateBoard(id, boardRequest);
            return ResponseEntity.status(HttpStatus.OK).body(ApiCmnResponse.success(board.toResponse()));
        }catch(ResponseStatusException e){
            return ResponseEntity.status(e.getStatus()).body(ApiCmnResponse.error(String.valueOf(e.getStatus()), e.getReason()));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiCmnResponse.error("500", e.getMessage()));
        }
    }

    @Operation(summary = "게시글 삭제", description = "게시글 삭제")
    @ApiResponses(value = {
            //@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class))),
            //@ApiResponse(responseCode = "400", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class)))
    })
    @DeleteMapping("/boards/{id}")
    public ResponseEntity<ApiCmnResponse<?>> boardDelete(
            @PathVariable("id") Long id,
            @RequestBody BoardRequest boardRequest,
            Authentication authentication
    ){
        try {
            // Validation
            // 1. 관리자는 모든 글을 삭제 가능하다.
            // 2. QNA는 사용자가 글을 삭제할 수 있지만 본인 글만 삭제할 수 있다.
            User user = userService.getUserByUserId(authentication.getName());
            Board valboard = boardService.getBoardById(id, BoardTypeEnum.valueOf(boardRequest.getBoardType()));
            switch (user.getRole().name()){
                case "ROLE_ADMIN":
                    break;
                default:
                    if(!"QNA".equals(valboard.getBoardType().name())){
                        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "게시글 삭제에 대한 권한이 없습니다.");
                    }
                    if(user.getId() != valboard.getUserId()){
                        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, " 게시글 삭제는 본인만 수정할 수 있습니다.");
                    }
                    break;
            }

            Board board = boardService.deleteBoard(id);
            return ResponseEntity.status(HttpStatus.OK).body(ApiCmnResponse.success(board.toResponse()));
        }catch(ResponseStatusException e){
            return ResponseEntity.status(e.getStatus()).body(ApiCmnResponse.error(String.valueOf(e.getStatus()), e.getReason()));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiCmnResponse.error("500", e.getMessage()));
        }
    }

    @Operation(summary = "게시글 조회수 올리기", description = "게시글 조회수 올리기")
    @ApiResponses(value = {
            //@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class))),
            //@ApiResponse(responseCode = "400", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class)))
    })
    @PostMapping("/boards/{id}/up-hit")
    public ResponseEntity<ApiCmnResponse<?>> boardUpHit(
            @PathVariable("id") Long id
    ){
        try {
            Board board = boardService.upHitBoard(id);
            return ResponseEntity.status(HttpStatus.OK).body(ApiCmnResponse.success(board.toResponse()));
        }catch(ResponseStatusException e){
            return ResponseEntity.status(e.getStatus()).body(ApiCmnResponse.error(String.valueOf(e.getStatus()), e.getReason()));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiCmnResponse.error("500", e.getMessage()));
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN')") // 답글은 관리자만 등록 가능하다.
    @Operation(summary = "게시글 답글 달기", description = "게시글 답글 달기")
    @ApiResponses(value = {
            //@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class))),
            //@ApiResponse(responseCode = "400", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class)))
    })
    @PostMapping("/boards/{upId}/reply-board")
    public ResponseEntity<ApiCmnResponse<?>> replyRegister(
            @PathVariable("upId") Long upId,
            @RequestBody BoardRequest boardRequest
    ){
        try {
            boardRequest.setUpId(upId);
            Board board = boardService.createBoard(boardRequest);
            return ResponseEntity.status(HttpStatus.OK).body(ApiCmnResponse.success(board.toResponse()));
        }catch(ResponseStatusException e){
            return ResponseEntity.status(e.getStatus()).body(ApiCmnResponse.error(String.valueOf(e.getStatus()), e.getReason()));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiCmnResponse.error("500", e.getMessage()));
        }
    }

    @Operation(summary = "게시글 답글 조회", description = "게시글 답글 조회")
    @ApiResponses(value = {
            //@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class))),
            //@ApiResponse(responseCode = "400", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class)))
    })
    @GetMapping("/boards/{upId}/reply-board")
    public ResponseEntity<ApiCmnResponse<?>> replyAll(
            @PathVariable("upId") Long upId,Pageable pageable
    ){
        try {
            int totalSize = boardService.getTotalReplyBoardSize(upId);
            List<Board> boardList = boardService.getBoardsByUpId(upId, pageable);
            List<BoardResponse> boardResponseList = boardList.stream().map(board -> board.toResponse()).collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.OK).body(ApiCmnResponse.success(new CustomPageImpl<>(boardResponseList, pageable, totalSize)));
        }catch(ResponseStatusException e){
            return ResponseEntity.status(e.getStatus()).body(ApiCmnResponse.error(String.valueOf(e.getStatus()), e.getReason()));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiCmnResponse.error("500", e.getMessage()));
        }
    }

    @Operation(summary = "공지사항 전체 조회", description = "공지사항 전체 조회")
    @ApiResponses(value = {
            //@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class))),
            //@ApiResponse(responseCode = "400", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class)))
    })
    @GetMapping("/boards/type/notices")
    public ResponseEntity<ApiCmnResponse<?>> noticeAll(
            Pageable pageable
    ){
        try{
            int totalSize = boardService.getTotalBoardSize(BoardTypeEnum.NOTICE);
            List<Board> boardList = boardService.getNoticeBoards(pageable);
            List<BoardResponse> boardResponseList = boardList.stream().map(board -> board.toResponse()).collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.OK).body(ApiCmnResponse.success(new CustomPageImpl<>(boardResponseList, pageable, totalSize)));
        }catch(ResponseStatusException e){
            return ResponseEntity.status(e.getStatus()).body(ApiCmnResponse.error(String.valueOf(e.getStatus()), e.getReason()));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiCmnResponse.error("500", e.getMessage()));
        }
    }

    @Operation(summary = "공지사항 상세 조회", description = "공지사항 상세 조회")
    @ApiResponses(value = {
            //@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class))),
            //@ApiResponse(responseCode = "400", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class)))
    })
    @GetMapping("/boards/type/notices/{id}")
    public ResponseEntity<ApiCmnResponse<?>> noticeDetail(
            @PathVariable("id") Long id
    ){
        try {
            Board board = boardService.getBoardById(id, BoardTypeEnum.NOTICE);
            return ResponseEntity.status(HttpStatus.OK).body(ApiCmnResponse.success(board.toResponse()));
        }catch(ResponseStatusException e){
            return ResponseEntity.status(e.getStatus()).body(ApiCmnResponse.error(String.valueOf(e.getStatus()), e.getReason()));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiCmnResponse.error("500", e.getMessage()));
        }
    }

    @Operation(summary = "공지사항 검색", description = "공지사항 검색")
    @ApiResponses(value = {
            //@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class))),
            //@ApiResponse(responseCode = "400", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class)))
    })
    @GetMapping("/boards/type/notices/search")
    public ResponseEntity<ApiCmnResponse<?>> noticeSearch(
            @RequestParam(value = "searchCode",defaultValue = "title")String searchCode,
            @RequestParam(value = "keyword", defaultValue = "") String keyword,
            Pageable pageable

    ){
        try {
            int totalSize = boardService.getTotalBoardSize(BoardTypeEnum.NOTICE);
            List<Board> boardList = null;
            switch (searchCode) {
                case "title":
                    boardList = boardService.getNoticeBoardsWithTitle(keyword, pageable);
                    break;
                case "context":
                    boardList = boardService.getNoticeBoardsWithContext(keyword, pageable);
                    break;
                default:
                    log.error("Keyword not matching");
                    throw new IllegalArgumentException("Codes only 'title' and 'context' are avaiable.");
            }
            List<BoardResponse> boardResponseList = boardList.stream().map(board -> board.toResponse()).collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.OK).body(ApiCmnResponse.success(new CustomPageImpl<>(boardResponseList, pageable, totalSize)));
        }catch(ResponseStatusException e){
            return ResponseEntity.status(e.getStatus()).body(ApiCmnResponse.error(String.valueOf(e.getStatus()), e.getReason()));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiCmnResponse.error("500", e.getMessage()));
        }
    }

    @Operation(summary = "자료실 전체 조회", description = "자료실 전체 조회")
    @ApiResponses(value = {
            //@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class))),
            //@ApiResponse(responseCode = "400", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class)))
    })
    @GetMapping("/boards/type/datas")
    public ResponseEntity<ApiCmnResponse<?>> dataAll(
            Pageable pageable
    ){
        try {
            int totalSize = boardService.getTotalBoardSize(BoardTypeEnum.DATA);
            List<Board> boardList = boardService.getDataBoards(pageable);
            List<BoardResponse> boardResponseList = boardList.stream().map(board -> board.toResponse()).collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.OK).body(ApiCmnResponse.success(new CustomPageImpl<>(boardResponseList, pageable, totalSize)));
        }catch(ResponseStatusException e){
            return ResponseEntity.status(e.getStatus()).body(ApiCmnResponse.error(String.valueOf(e.getStatus()), e.getReason()));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiCmnResponse.error("500", e.getMessage()));
        }
    }
    @Operation(summary = "자료실 상세 조회", description = "자료실 상세 조회")
    @ApiResponses(value = {
            //@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class))),
            //@ApiResponse(responseCode = "400", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class)))
    })
    @GetMapping("/boards/type/datas/{id}")
    public ResponseEntity<ApiCmnResponse<?>> dataDetail(
            @PathVariable("id") Long id
    ){
        try {
            Board board = boardService.getBoardById(id, BoardTypeEnum.DATA);
            return ResponseEntity.status(HttpStatus.OK).body(ApiCmnResponse.success(board.toResponse()));
        }catch(ResponseStatusException e){
            return ResponseEntity.status(e.getStatus()).body(ApiCmnResponse.error(String.valueOf(e.getStatus()), e.getReason()));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiCmnResponse.error("500", e.getMessage()));
        }
    }

    @Operation(summary = "FAQ 전체 조회", description = "FAQ 전체 조회")
    @ApiResponses(value = {
            //@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class))),
            //@ApiResponse(responseCode = "400", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class)))
    })
    @GetMapping("/boards/type/faqs")
    public ResponseEntity<ApiCmnResponse<?>> faqAll(
            Pageable pageable
    ){
        try {
            int totalSize = boardService.getTotalBoardSize(BoardTypeEnum.FAQ);
            List<Board> boardList = boardService.getFAQBoards(pageable);
            List<BoardResponse> boardResponseList = boardList.stream().map(board -> board.toResponse()).collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.OK).body(ApiCmnResponse.success(new CustomPageImpl<>(boardResponseList, pageable, totalSize)));
        }catch(ResponseStatusException e){
            return ResponseEntity.status(e.getStatus()).body(ApiCmnResponse.error(String.valueOf(e.getStatus()), e.getReason()));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiCmnResponse.error("500", e.getMessage()));
        }
    }
    @Operation(summary = "FAQ 상세 조회", description = "FAQ 상세 조회")
    @ApiResponses(value = {
            //@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class))),
            //@ApiResponse(responseCode = "400", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class)))
    })
    @GetMapping("/boards/type/faqs/{id}")
    public ResponseEntity<ApiCmnResponse<?>> faqDetail(
            @PathVariable("id") Long id
    ){
        try {
            Board board = boardService.getBoardById(id, BoardTypeEnum.FAQ);
            return ResponseEntity.status(HttpStatus.OK).body(ApiCmnResponse.success(board.toResponse()));
        }catch(ResponseStatusException e){
            return ResponseEntity.status(e.getStatus()).body(ApiCmnResponse.error(String.valueOf(e.getStatus()), e.getReason()));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiCmnResponse.error("500", e.getMessage()));
        }
    }

    @Operation(summary = "QNA 전체 조회", description = "QNA 전체 조회")
    @ApiResponses(value = {
            //@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class))),
            //@ApiResponse(responseCode = "400", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class)))
    })
    @GetMapping("/boards/type/qnas")
    public ResponseEntity<ApiCmnResponse<?>> qnaAll(
            Pageable pageable
    ){
        try {
            int totalSize = boardService.getTotalBoardSize(BoardTypeEnum.QNA);
            List<Board> boardList = boardService.getQNABoards(pageable);
            List<BoardResponse> boardResponseList = boardList.stream().map(board -> board.toResponse()).collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.OK).body(ApiCmnResponse.success(new CustomPageImpl<>(boardResponseList, pageable, totalSize)));
        }catch(ResponseStatusException e){
            return ResponseEntity.status(e.getStatus()).body(ApiCmnResponse.error(String.valueOf(e.getStatus()), e.getReason()));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiCmnResponse.error("500", e.getMessage()));
        }
    }
    @Operation(summary = "QNA 상세 조회", description = "QNA 상세 조회")
    @ApiResponses(value = {
            //@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class))),
            //@ApiResponse(responseCode = "400", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = CompanySearchRequest.class)))
    })
    @GetMapping("/boards/type/qnas/{id}")
    public ResponseEntity<ApiCmnResponse<?>> qnaDetail(
            @PathVariable("id") Long id
    ){
        try {
            Board board = boardService.getBoardById(id, BoardTypeEnum.QNA);
            return ResponseEntity.status(HttpStatus.OK).body(ApiCmnResponse.success(board.toResponse()));
        }catch(ResponseStatusException e){
            return ResponseEntity.status(e.getStatus()).body(ApiCmnResponse.error(String.valueOf(e.getStatus()), e.getReason()));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiCmnResponse.error("500", e.getMessage()));
        }
    }





}
