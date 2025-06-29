package finemytrip.backend.config;

import finemytrip.backend.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {
    private final MemberRepository memberRepository;
    private final Environment environment;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public DataLoader(MemberRepository memberRepository, Environment environment) {
        this.memberRepository = memberRepository;
        this.environment = environment;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Data loading completed.");
    }
}
