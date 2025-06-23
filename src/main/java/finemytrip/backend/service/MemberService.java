package finemytrip.backend.service;

import finemytrip.backend.dto.LoginResponseDto;
import finemytrip.backend.dto.MemberLoginRequestDto;
import finemytrip.backend.dto.MemberRegisterRequestDto;
import finemytrip.backend.dto.MemberResponseDto;
import finemytrip.backend.entity.Member;
import finemytrip.backend.repository.MemberRepository;
import finemytrip.backend.util.JwtUtil;
import finemytrip.backend.util.TokenBlacklist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;
    private final TokenBlacklist tokenBlacklist;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public MemberService(MemberRepository memberRepository, JwtUtil jwtUtil, TokenBlacklist tokenBlacklist) {
        this.memberRepository = memberRepository;
        this.jwtUtil = jwtUtil;
        this.tokenBlacklist = tokenBlacklist;
    }

    @Transactional
    public MemberResponseDto register(MemberRegisterRequestDto requestDto) {
        if (memberRepository.existsByEmail(requestDto.getEmail())) {
            throw new RuntimeException("Email already exists: " + requestDto.getEmail());
        }

        Member member = Member.builder()
                .email(requestDto.getEmail())
                .password(encodePassword(requestDto.getPassword()))
                .marketingAgreed(requestDto.getMarketingAgreed())
                .build();

        Member savedMember = memberRepository.save(member);
        return convertToResponseDto(savedMember);
    }

    public LoginResponseDto login(MemberLoginRequestDto requestDto) {
        Member member = memberRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found."));
        
        if (!passwordEncoder.matches(requestDto.getPassword(), member.getPassword())) {
            throw new RuntimeException("Bad credentials");
        }

        String token = jwtUtil.generateToken(member.getEmail(), member.getId());

        return LoginResponseDto.builder()
                .token(token)
                .id(member.getId())
                .email(member.getEmail())
                .marketingAgreed(member.getMarketingAgreed())
                .createdAt(member.getCreatedAt())
                .updatedAt(member.getUpdatedAt())
                .build();
    }

    public MemberResponseDto getMemberById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found. ID: " + id));
        return convertToResponseDto(member);
    }

    public MemberResponseDto getMemberByEmail(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Member not found. Email: " + email));
        return convertToResponseDto(member);
    }

    public void logout(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            String actualToken = token.substring(7);
            
            if (jwtUtil.validateToken(actualToken) && !jwtUtil.isTokenExpired(actualToken)) {
                tokenBlacklist.addToBlacklist(actualToken);
            } else {
                throw new RuntimeException("Invalid token.");
            }
        } else {
            throw new RuntimeException("Valid token format required.");
        }
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    private MemberResponseDto convertToResponseDto(Member member) {
        return MemberResponseDto.builder()
                .id(member.getId())
                .email(member.getEmail())
                .marketingAgreed(member.getMarketingAgreed())
                .createdAt(member.getCreatedAt())
                .updatedAt(member.getUpdatedAt())
                .build();
    }

    public List<MemberResponseDto> getAllMembers() {
        return memberRepository.findAll().stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }
}
