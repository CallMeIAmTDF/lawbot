package com.example.template.config;

import com.example.template.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("auditAwareImpl")
@RequiredArgsConstructor
public class AuditAwareImpl implements AuditorAware<String> {
    private final SecurityUtil securityUtil;

    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of(securityUtil.getCurrentUserLogin().get());
    }
}