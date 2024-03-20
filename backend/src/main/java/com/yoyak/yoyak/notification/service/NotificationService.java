package com.yoyak.yoyak.notification.service;

import com.yoyak.yoyak.account.domain.Account;
import com.yoyak.yoyak.account.domain.AccountRepository;
import com.yoyak.yoyak.notification.domain.Notification;
import com.yoyak.yoyak.notification.domain.NotificationRepository;
import com.yoyak.yoyak.notification.dto.NotificationFindDto;
import com.yoyak.yoyak.notification.dto.NotificationModifyDto;
import com.yoyak.yoyak.notification.dto.NotificationRegistDto;
import com.yoyak.yoyak.util.exception.CustomException;
import com.yoyak.yoyak.util.exception.CustomExceptionStatus;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class NotificationService {

    private final AccountRepository accountRepository;
    private final NotificationRepository notificationRepository;

    // 알람 등록
    public Notification addNotification(NotificationRegistDto notificationRegistDto) {
        Account account = accountRepository.findById(notificationRegistDto.getAccountSeq())
            .orElseThrow(() -> new CustomException(CustomExceptionStatus.ACCOUNT_INVALID));

        Notification notification = Notification.builder()
            .name(notificationRegistDto.getName())
            .startDate(notificationRegistDto.getStartDate())
            .endDate(notificationRegistDto.getEndDate())
            .period(notificationRegistDto.getPeriod())
            .time(notificationRegistDto.getTime())
            .createDate(LocalDateTime.now())
            .account(account)
            .build();

        Notification notificationSaved = notificationRepository.save(notification);

        return notificationSaved;
    }

    // 알림 상세 보기
    public NotificationFindDto findNotification(Long notiSeq) {
        Notification notification = findById(notiSeq);

        NotificationFindDto notificationFindDto = NotificationFindDto.builder()
            .notiSeq(notification.getSeq())
            .name(notification.getName())
            .startDate(notification.getStartDate())
            .endDate(notification.getEndDate())
            .period(notification.getPeriod())
            .time(notification.getTime())
            .build();

        return notificationFindDto;
    }

    // 알람 수정
    public Notification modifyNotification(NotificationModifyDto notificationModifyDto) {
        Notification notification = findById(notificationModifyDto.getNotiSeq());
        notification.modifyNotification(notificationModifyDto);

        return notification;
    }

    // 알람 삭제
    public void removeNotification(Long notiSeq) {
        Notification notification = findById(notiSeq);
        notificationRepository.delete(notification);
    }

    // 알람 조회
    public Notification findById(Long seq) {
        Notification notification = notificationRepository.findById(seq)
            .orElseThrow(() -> new CustomException(CustomExceptionStatus.NOTI_INVALID));

        return notification;
    }
}
