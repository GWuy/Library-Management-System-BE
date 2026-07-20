package com.lms.swd392.lmsbe.scheduler;

import com.lms.swd392.lmsbe.service.BorrowRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class BorrowRecordScheduler {

    private final BorrowRecordService borrowRecordService;

    /**
     * Runs every day at 11:00 PM (23:00).
     */
    @Scheduled(cron = "0 0 23 * * *")
    public void processOverdueAndReminders() {
        log.info("Starting scheduled task: processOverdueAndReminders");
        try {
            borrowRecordService.processOverdueRecords();
            log.info("Finished scheduled task: processOverdueAndReminders");
        } catch (Exception e) {
            log.error("Error during scheduled task: processOverdueAndReminders", e);
        }
    }
}
