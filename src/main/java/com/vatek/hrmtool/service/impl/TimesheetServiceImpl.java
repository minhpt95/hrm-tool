package com.vatek.hrmtool.service.impl;

import com.vatek.hrmtool.constant.ErrorConstant;
import com.vatek.hrmtool.dto.timesheet.TimesheetDto;
import com.vatek.hrmtool.entity.DayOffEntity;
import com.vatek.hrmtool.entity.ProjectEntity;
import com.vatek.hrmtool.entity.TimesheetEntity;
import com.vatek.hrmtool.entity.UserEntity;
import com.vatek.hrmtool.entity.common.CommonEntity;
import com.vatek.hrmtool.enumeration.ApprovalStatus;
import com.vatek.hrmtool.enumeration.TimesheetType;
import com.vatek.hrmtool.exception.ErrorResponse;
import com.vatek.hrmtool.exception.ProductException;
import com.vatek.hrmtool.mapping.TimesheetMapping;
import com.vatek.hrmtool.projection.TimesheetWorkingHourProjection;
import com.vatek.hrmtool.readable.form.create.CreateTimesheetForm;
import com.vatek.hrmtool.respository.*;
import com.vatek.hrmtool.security.service.UserPrinciple;
import com.vatek.hrmtool.service.TimesheetService;
import com.vatek.hrmtool.util.DateUtil;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;

@Service
@AllArgsConstructor
@Log4j2
public class TimesheetServiceImpl implements TimesheetService {
    private final RequestRepository requestRepository;
    private TimesheetRepository timesheetRepository;

    private UserRepository userRepository;

    private ProjectRepository projectRepository;

    private DayOffEntityRepository dayOffEntityRepository;

    private TimesheetMapping timesheetMapping;

    @Override
    @Transactional
    public TimesheetDto createTimesheet(CreateTimesheetForm form){
        var currentUser = (UserPrinciple) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        UserEntity userEntity = userRepository.findUserEntityById(currentUser.getId());

        ProjectEntity projectEntity = projectRepository.findById(form.getProjectId()).orElse(null);

        if(projectEntity == null){
            throw new ProductException(
                    ErrorResponse
                            .builder()
                            .type(ErrorConstant.Type.NOT_FOUND)
                            .code(ErrorConstant.Code.NOT_FOUND)
                            .message(String.format(ErrorConstant.Message.NOT_FOUND,"Project id : " + form.getProjectId()))
                            .build()
            );
        }

        Collection<Long> projectIds = userEntity.getWorkingProject().stream().map(CommonEntity::getId).toList();

        if(!projectIds.contains(form.getProjectId())){
            throw new ProductException(
                    ErrorResponse
                            .builder()
                            .message("You not working in this project")
                            .build()
            );
        }

        Instant workingDayInstant = DateUtil.convertStringDateToInstant(form.getWorkingDay());

        var workingDayInstantDayOfWeek = workingDayInstant.atZone(ZoneId.systemDefault()).getDayOfWeek();

        if(
                form.getTimesheetType() == TimesheetType.NORMAL_WORKING && (workingDayInstantDayOfWeek == DayOfWeek.SATURDAY || workingDayInstantDayOfWeek == DayOfWeek.SUNDAY)
        ){
            throw new ProductException(
                    ErrorResponse
                            .builder()
                            .type(ErrorConstant.Type.CANNOT_LOG_ON_WEEKEND)
                            .code(ErrorConstant.Code.CANNOT_LOG_ON_WEEKEND)
                            .message(ErrorConstant.Message.CANNOT_LOG_ON_WEEKEND)
                            .build()
            );
        }



        Specification<DayOffEntity> dayOffEntitySpecification = (root, query, criteriaBuilder) -> {
            var predicates = new ArrayList<Predicate>();
            predicates.add(criteriaBuilder.equal(root.get("dayoffEntityId").get("dateOff"),workingDayInstant));
            predicates.add(criteriaBuilder.equal(root.get("dayoffEntityId").get("userId"),currentUser));
            Predicate[] p = new Predicate[predicates.size()];
            return criteriaBuilder.and(predicates.toArray(p));
        };



        var getTimesheetProjection = timesheetRepository
                .findByUserEntityIdAndWorkingDayAndStatusNot(
                        currentUser.getId(),
                        workingDayInstant,
                        ApprovalStatus.REJECTED
                );

        var totalWorkingHours = getTimesheetProjection
                .stream()
                .map(TimesheetWorkingHourProjection::getWorkingHours)
                .reduce(0,Integer::sum);

        var getRequestDayOff = dayOffEntityRepository.findAll(dayOffEntitySpecification);

        switch (getRequestDayOff.size()){
            case 2 -> {
                throw new ProductException(
                        ErrorResponse
                                .builder()
                                .type(ErrorConstant.Type.CANNOT_LOG_TIMESHEET)
                                .code(ErrorConstant.Code.CANNOT_LOG_TIMESHEET)
                                .message(String.format(ErrorConstant.Message.CANNOT_LOG_TIMESHEET,8))
                                .build()
                );
            }
            case 1 -> {
                switch (getRequestDayOff.get(0).getDayoffEntityId().getTypeDayOff()){
                    case FULL -> {

                    }
                    case MORNING -> {

                    }
                    case AFTERNOON -> {

                    }
                }
            }
            case 0 -> {
            }
        }



        TimesheetEntity timesheetEntity = new TimesheetEntity();
        timesheetEntity.setTitle(form.getTitle());
        timesheetEntity.setDescription(form.getDescription());
        timesheetEntity.setWorkingHours(form.getWorkingHours());
        timesheetEntity.setWorkingDay(workingDayInstant);
        timesheetEntity.setProjectEntity(projectEntity);
        timesheetEntity.setUserEntity(userEntity);
        timesheetEntity.setTimesheetType(form.getTimesheetType());
        timesheetEntity.setCreatedBy(currentUser.getId());
        timesheetEntity.setCreatedTime(DateUtil.getInstantNow());
        timesheetEntity = timesheetRepository.save(timesheetEntity);

        return timesheetMapping.toDto(timesheetEntity);
    }

    public TimesheetDto approvalTimesheet(){
        var currentUser = (UserPrinciple) SecurityContextHolder.getContext().getAuthentication().getPrincipal();



        return null;
    }
}
