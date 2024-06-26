package idusw.springboot.hshblog.controller;

import idusw.springboot.hshblog.model.MemberDto;
import idusw.springboot.hshblog.serivce.MemberService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("members/")
public class MemberController {

    final MemberService memberService;
    public MemberController(MemberService memberService) { // 생성자 주입
        this.memberService = memberService;
    }

    @GetMapping("logout")
    public String getLogout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
    @GetMapping("{idx}")
    public String getMemberById(@PathVariable("idx") Long idx, Model model) {
        MemberDto dto = memberService.readByIdx(idx);
        model.addAttribute("dto", dto);
        return "./members/profile";
    }
    @GetMapping("")
    public String getMembers(Model model) {
        List<MemberDto> dtoList = memberService.readAll();
        model.addAttribute("dtoList", dtoList);
        return "./members/list";
    }
    // get방식으로 members/login을 요청하면 main/login.html로 이동
    @GetMapping("login")
    public String getLogin(Model model) {
        model.addAttribute("memberDto", MemberDto.builder().build());
        return "./main/login";
    }
    // main/login.html 에서 폼을 통해 post 요청, 처리후 main/index.html로 이동
    @PostMapping("login")
    public String postLogin(@ModelAttribute("memberDto") MemberDto memberDto, Model model, HttpSession session) {
        String id = memberDto.getId();
        String pw = memberDto.getPw();

        MemberDto m = MemberDto.builder()
                .id(id)
                .pw(pw)
                .build();
        String msg = "";

        // Database로 부터 정보를 가져올 예정임
        // 사용자가 제공한 정보와 DB로 부터 가져온 정보를 처리
        // 동작전 로그
        MemberDto ret = memberService.loginById(m);
        // 동작 후 로그
        if(ret != null) {
            session.setAttribute("id", id);
            session.setAttribute("idx", ret.getIdx());
            msg = "로그인 성공";
        } else {
            msg = "로그인 실패";
        }
        model.addAttribute("message", msg );
        return "./errors/error-message";
    }

    @GetMapping("register")
    public String getRegister(Model model) {
        model.addAttribute("memberDto", MemberDto.builder().build());
        return "./main/register";
    }

    @PostMapping("register")
    public String postRegister(@ModelAttribute("memberDto") MemberDto memberDto, Model model) {
        System.out.println(memberDto);
        // 등록 처리 -> service -> repository -> service -> controller
        if(memberService.create(memberDto) > 0 ) // 정상적으로 레코드의 변화가 발생하는 경우 영향받는 레코드 수를 반환
            return "redirect:/";
        else
            return "./errors/error-message";
    }

    @PostMapping("{idx}/update")
    public String updateMember(@PathVariable("idx") Long idx, @ModelAttribute("memberDto") MemberDto memberDto, Model model) {
        // 기존 회원 정보 가져오기
        MemberDto existingMember = memberService.readByIdx(idx);

        if (existingMember != null) {
            // 새로운 정보로 업데이트
            existingMember.setName(memberDto.getName());
            // existingMember.setPw(memberDto.getPw());
            existingMember.setPhone(memberDto.getPhone());
            existingMember.setAddress(memberDto.getAddress());
            existingMember.setEmail(memberDto.getEmail());

            // 업데이트 수행
            if (memberService.update(existingMember) > 0) {
                return "redirect:/"; // 업데이트 후 메인 페이지로 리다이렉트
            } else {
                model.addAttribute("message", "회원 정보 업데이트에 실패하였습니다.");
                return "./errors/error-message"; // 실패 시 보여줄 페이지
            }
        } else {
            model.addAttribute("message", "해당 회원을 찾을 수 없습니다.");
            return "./errors/error-message"; // 회원이 존재하지 않을 경우 보여줄 페이지
        }
    }
    @PostMapping("{idx}/delete")
    public String deleteMember(@PathVariable("idx") Long idx, HttpSession session, Model model) {
        MemberDto dto = memberService.readByIdx(idx);
        if (dto != null) {
            if (memberService.delete(dto) > 0) {
                session.invalidate(); // 회원 탈퇴 후 세션 무효화
                return "redirect:/"; // 메인 페이지로 리다이렉트
            } else {
                model.addAttribute("message", "회원 탈퇴에 실패하였습니다.");
                return "./errors/error-message"; // 실패 시 보여줄 페이지
            }
        } else {
            model.addAttribute("message", "해당 회원을 찾을 수 없습니다.");
            return "./errors/error-message"; // 회원이 존재하지 않을 경우 보여줄 페이지
        }
    }
}
