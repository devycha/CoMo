package com.dongjji.como.meet.controller;

import com.dongjji.como.meet.dto.CreateMeetDto;
import com.dongjji.como.meet.dto.ExtendMeetPeriodDto;
import com.dongjji.como.meet.dto.InviteMembersDto;
import com.dongjji.como.meet.entity.Meet;
import com.dongjji.como.meet.entity.MeetApply;
import com.dongjji.como.meet.entity.MeetInvitation;
import com.dongjji.como.meet.service.MeetApplyService;
import com.dongjji.como.meet.service.MeetInvitationService;
import com.dongjji.como.meet.service.MeetService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@Controller
@RequestMapping("/meet")
public class MeetController {
    private final MeetService meetService;
    private final MeetInvitationService meetInvitationService;
    private final MeetApplyService meetApplyService;

    @GetMapping
    public String getAllMeets(Pageable pageable, Model model) {
        Page<Meet> meets = meetService.getAllMeets(pageable);
        model.addAttribute("meets", meets);

        return "/meet/all";
    }

    @PostMapping
    public String createMeet(CreateMeetDto.Request createMeetDto, Model model,
                              Authentication authentication) {
        CreateMeetDto.Response meet = meetService.createMeet(createMeetDto);
        meetInvitationService.inviteMembers(InviteMembersDto.Request
                                    .builder()
                                    .meetId(meet.getId())
                                    .emailList(createMeetDto.getEmails())
                                    .build());

        model.addAttribute("meet", meet);

        return "meet/meet-detail";
    }

    @PostMapping("/invite")
    public String inviteMember(InviteMembersDto.Request inviteMembersDto) {
        meetInvitationService.inviteMembers(inviteMembersDto);

        return "meet/meet-detail";
    }

    //    @PreAuthorize()
    @DeleteMapping("/{meetId}")
    public String deActivateMeet(@PathVariable Long meetId) {
        // TODO: 그룹장만 비활성화가 가능하게 PreAuthorize 적용 해야함.
        meetService.deActivateMeet(meetId);

        return "meet/meet-detail";
    }

    @GetMapping("/apply/{meetId}")
    public String getAllApplies(@PathVariable Long meetId, String email, Model model) {
        // TODO: 로그인한 계정의 이메일을 email
        List<MeetApply> applies = meetApplyService.getAllApplies(meetId, email);
        model.addAttribute("applies", applies);

        return "meet/applies";
    }

    @PostMapping("/apply/{meetId}")
    public String applyMeet(@PathVariable Long meetId, String email) {
        // TODO: 인증 정보 가져와서 email 넣어주기
        meetApplyService.applyMeetByMeetId(meetId, email);

        return "meet/meet-detail";
    }

    // 오너가 사용자의 신청을 수락
    @PostMapping("/apply/approve/{applyId}")
    public String approveApply(@PathVariable Long applyId) {
        Long meetId = meetApplyService.approveApply(applyId);

        return "redirect:/meet/apply/" + meetId;
    }

    // 오너가 사용자의 신청을 거절
    @PostMapping("/apply/refuse/{applyId}")
    public String refuseApply(@PathVariable Long applyId) {
        Long meetId = meetApplyService.refuseApply(applyId);

        return "redirect:/meet/apply/" + meetId;
    }

    // 사용자가 오너의 초대를 수락
    @PostMapping("/invite/approve/{invitationId}")
    public String approveInvitation(@PathVariable Long invitationId) {
        Long meetId = meetInvitationService.approveInvitation(invitationId);

        return "redirect:/meet/apply/" + meetId;
    }

    // 사용자가 오너의 초대를 거절
    @PostMapping("/invite/refuse/{invitationId}")
    public String refuseInvitation(@PathVariable Long invitationId) {
        Long meetId = meetInvitationService.refuseInvitation(invitationId);

        return "redirect:/meet/apply/" + meetId;
    }

    @GetMapping("/invites")
    public String getAllInvitations(String email, Model model) {
        // TODO: 인증 정보 가져와서 email 넣어주기
        List<MeetInvitation> invitations = meetInvitationService.getAllInvitations(email);
        model.addAttribute("invitations", invitations);

        return "meet/invites";
    }

    @PutMapping("/period/extend")
    public String extendPeriod(ExtendMeetPeriodDto.Request extendMeetPeriodDto) {
        // TODO: 그룹장만 기간 연장이 가능하게 PreAuthorize 적용 해야함.
        meetService.extendPeriod(extendMeetPeriodDto);
        return "meet/meet-detail";
    }

    @Scheduled(cron = "${scheduler.cron.period}")
    public void finishMeets() {
        meetService.finishMeets();
    }
}
