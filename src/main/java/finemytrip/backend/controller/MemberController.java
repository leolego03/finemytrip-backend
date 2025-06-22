package finemytrip.backend.controller;

import finemytrip.backend.dto.LoginResponseDto;
import finemytrip.backend.dto.MemberLoginRequestDto;
import finemytrip.backend.dto.MemberRegisterRequestDto;
import finemytrip.backend.dto.MemberResponseDto;
import finemytrip.backend.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class MemberController {
    private final MemberService memberService;

    @GetMapping
    public ResponseEntity<List<MemberResponseDto>> getAllMembers() {
        List<MemberResponseDto> members = memberService.getAllMembers();
        return ResponseEntity.ok(members);
    }

    @PostMapping("/register")
    public ResponseEntity<MemberResponseDto> register(@RequestBody MemberRegisterRequestDto requestDto) {
        MemberResponseDto responseDto = memberService.register(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody MemberLoginRequestDto requestDto) {
        LoginResponseDto responseDto = memberService.login(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberResponseDto> getMemberById(@PathVariable Long id) {
        MemberResponseDto responseDto = memberService.getMemberById(id);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<?> getMemberByEmail(@PathVariable String email) {
        try {
            MemberResponseDto member = memberService.getMemberByEmail(email);
            return ResponseEntity.ok(member);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error occurred: " + e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {
        try {
            memberService.logout(token);
            return ResponseEntity.ok("Logged out successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error occurred: " + e.getMessage());
        }
    }
} 