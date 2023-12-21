package com.twodollar.tdboard.modules.contents.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class ContentsWebController {

    @GetMapping("contents/about")
    public String about() {
        return "pages/contents/about";
    }

    @GetMapping("contents/support")
    public String support() {
        return "pages/contents/support";
    }

    @GetMapping("contents/direction")
    public String direction() {
        return "pages/contents/direction";
    }

    // EDU
    @GetMapping("contents/edu-list")
    public String eduList() {
        return "pages/contents/edu/list";
    }

    @GetMapping("contents/edu-detail")
    public String eduDetail() {
        return "pages/contents/edu/detail";
    }

    @GetMapping("contents/edu-calendar")
    public String eduCalendar() {
        return "pages/contents/edu/calendar";
    }

    @GetMapping("contents/edu-confirm")
    public String eduConfirm() {
        return "pages/contents/edu/confirm";
    }

    // SHARE
    @GetMapping("contents/share-list")
    public String shareList() {
        return "pages/contents/share/list";
    }

    @GetMapping("contents/share-calendar")
    public String shareCalendar() {
        return "pages/contents/share/calendar";
    }

    @GetMapping("contents/share-confirm")
    public String shareConfirm() {
        return "pages/contents/share/confirm";
    }

    // KDP 세한 달력
    @GetMapping("contents/calendar")
    public String calendar() {
        return "pages/contents/sehankdp/calendar";
    }

    // 게시판
    @GetMapping("contents/notice")
    public String notice() {
        return "pages/contents/board/notice";
    }

    @GetMapping("contents/data")
    public String data() {
        return "pages/contents/board/data";
    }

    @GetMapping("contents/faq")
    public String faq() {
        return "pages/contents/board/faq";
    }

    @GetMapping("contents/qna")
    public String qna() {
        return "pages/contents/board/qna";
    }

    @GetMapping("contents/notice/0")
    public String noticeDetail() {
        return "pages/contents/board/noticeDetail";
    }

    @GetMapping("contents/data/0")
    public String dataDetail() {
        return "pages/contents/board/dataDetail";
    }
    @GetMapping("contents/qna/0")
    public String qnaDetail() {
        return "pages/contents/board/qnaDetail";
    }

    @GetMapping("contents/group-qna")
    public String groupQna() {
        return "pages/contents/groupQna";
    }


}
