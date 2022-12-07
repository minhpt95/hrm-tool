package com.vatek.hrmtool.service.impl.schedule;



import com.vatek.hrmtool.respository.CronRepository;
import com.vatek.hrmtool.service.schedule.ScheduleService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@AllArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {
    private CronRepository cronRepository;

}
