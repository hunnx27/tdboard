package com.twodollar.tdboard.modules.admin.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Slf4j
@Controller
public class AdminWebController {

    /**
     * 인덱스
     *
     * @return
     */
    @GetMapping("admin")
    public String index() {
        return "redirect:/admin/member-list";
    }

    // 회원
    @GetMapping("admin/member-list")
    public String memberList() {
        return "pages/admin/member/list";
    }
    @GetMapping("admin/member-detail/{id}")
    public String memberDetail(@PathVariable("id") Long id, Model model) {
        model.addAttribute("id", id);
        return "pages/admin/member/detail";
    }

    // 교육
    @GetMapping("admin/edu-list")
    public String eduList() {
        return "pages/admin/edu/list";
    }
    @GetMapping("admin/edu-create")
    public String eduCreate() {
        return "pages/admin/edu/create";
    }
    @GetMapping("admin/edu-detail/{id}")
    public String eduDetail(@PathVariable("id") Long id, Model model) {
        model.addAttribute("id", id);
        return "pages/admin/edu/detail";
    }
    @GetMapping("admin/edu-modify/{id}")
    public String eduModify(@PathVariable("id") Long id, Model model) {
        model.addAttribute("id", id);
        return "pages/admin/edu/modify";
    }

    // 장비
    @GetMapping("admin/equipment-list")
    public String equipmentList() {
        return "pages/admin/equipment/list";
    }
    @GetMapping("admin/equipment-detail/{id}")
    public String equipmentDetail(@PathVariable("id") Long id, Model model) {
        model.addAttribute("id", id);
        return "pages/admin/equipment/detail";
    }
    @GetMapping("admin/equipment-modify/{id}")
    public String equipmentModify(@PathVariable("id") Long id, Model model) {
        model.addAttribute("id", id);
        return "pages/admin/equipment/modify";
    }

    // 시설
    @GetMapping("admin/facility-list")
    public String facilityList() {
        return "pages/admin/facility/list";
    }
    @GetMapping("admin/facility-detail/{id}")
    public String facilityDetail(@PathVariable("id") Long id, Model model) {
        model.addAttribute("id", id);
        return "pages/admin/facility/detail";
    }
    @GetMapping("admin/facility-modify/{id}")
    public String facilityModify(@PathVariable("id") Long id, Model model) {
        model.addAttribute("id", id);
        return "pages/admin/facility/modify";
    }

    // 설문지
    @GetMapping("admin/survey-list")
    public String surveyList() {
        return "pages/admin/survey/list";
    }

    // 현황
    @GetMapping("admin/status/edu-list")
    public String statusEduList() {
        return "pages/admin/status/edu";
    }
    @GetMapping("admin/status/equipment-list")
    public String statusEquipmentList() {
        return "pages/admin/status/equipment";
    }
    @GetMapping("admin/status/facility-list")
    public String statusFacilityList() {
        return "pages/admin/status/facility";
    }


}
