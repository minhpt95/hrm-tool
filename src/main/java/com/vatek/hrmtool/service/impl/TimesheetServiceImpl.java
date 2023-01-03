package com.vatek.hrmtool.service.impl;

import com.vatek.hrmtool.constant.ErrorConstant;
import com.vatek.hrmtool.dto.timesheet.TimesheetDto;
import com.vatek.hrmtool.entity.ProjectEntity;
import com.vatek.hrmtool.entity.RequestEntity;
import com.vatek.hrmtool.entity.TimesheetEntity;
import com.vatek.hrmtool.entity.UserEntity;
import com.vatek.hrmtool.entity.common.CommonEntity;
import com.vatek.hrmtool.enumeration.ApprovalStatus;
import com.vatek.hrmtool.enumeration.TimesheetType;
import com.vatek.hrmtool.enumeration.TypeDayOff;
import com.vatek.hrmtool.enumeration.TypeRequest;
import com.vatek.hrmtool.exception.ErrorResponse;
import com.vatek.hrmtool.exception.ProductException;
import com.vatek.hrmtool.mapping.TimesheetMapping;
import com.vatek.hrmtool.readable.form.create.CreateTimesheetForm;
import com.vatek.hrmtool.respository.ProjectRepository;
import com.vatek.hrmtool.respository.RequestRepository;
import com.vatek.hrmtool.respository.TimesheetRepository;
import com.vatek.hrmtool.respository.UserRepository;
import com.vatek.hrmtool.security.service.UserPrinciple;
import com.vatek.hrmtool.service.TimesheetService;
import com.vatek.hrmtool.util.DateUtil;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@AllArgsConstructor
@Log4j2
public class TimesheetServiceImpl implements TimesheetService {
    private TimesheetRepository timesheetRepository;

    private UserRepository userRepository;

    private ProjectRepository projectRepository;

    private RequestRepository requestRepository;

    private TimesheetMapping timesheetMapping;

    @Override
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
                            .type("")
                            .code("")
                            .message("You not working in this project")
                            .build()
            );
        }

        Instant workingDayInstant = DateUtil.convertStringDateToInstant(form.getWorkingDay());

        var workingDayInstantDayOfWeek = workingDayInstant.atZone(ZoneId.systemDefault()).getDayOfWeek();

        if(
                form.getTimesheetType() == TimesheetType.NORMAL_WORKING &&
                        (workingDayInstantDayOfWeek == DayOfWeek.SATURDAY || workingDayInstantDayOfWeek == DayOfWeek.SUNDAY)
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

        Specification<TimesheetEntity> timesheetEntitySpecification = (root, query, criteriaBuilder) -> {
            var predicates = new ArrayList<Predicate>();

            predicates.add(criteriaBuilder.notEqual(root.get("requestStatus"), ApprovalStatus.REJECTED));
            predicates.add(criteriaBuilder.equal(root.get("timesheetType"), form.getTimesheetType()));
            predicates.add(criteriaBuilder.equal(root.get("userEntity").get("id"),currentUser.getId()));
            predicates.add(criteriaBuilder.equal(root.get("workingDay"),workingDayInstant));

            Predicate[] p = new Predicate[predicates.size()];

            return criteriaBuilder.and(p);
        };

        List<TimesheetEntity> timesheetEntities = timesheetRepository.findAll(timesheetEntitySpecification);

        Integer totalWorkingHoursInDay = timesheetEntities.stream().map(TimesheetEntity::getWorkingHours).reduce(0,Integer::sum);

        Specification<RequestEntity> specification = (root, query, criteriaBuilder) -> {
            var predicates = new ArrayList<Predicate>();

            predicates.add(criteriaBuilder.equal(root.get("typeRequest"), TypeRequest.DAY_OFF));
            predicates.add(criteriaBuilder.greaterThan(root.get("toDate"),workingDayInstant));
            predicates.add(criteriaBuilder.lessThan(root.get("fromDate"),workingDayInstant));
            predicates.add(criteriaBuilder.notEqual(root.get("status"), ApprovalStatus.REJECTED));
            predicates.add(criteriaBuilder.equal(root.get("requester").get("id"),currentUser.getId()));

            criteriaBuilder.or(
                    criteriaBuilder.equal(root.get("typeDayOff"), TypeDayOff.MORNING),
                    criteriaBuilder.equal(root.get("typeDayOff"), TypeDayOff.AFTERNOON),
                    criteriaBuilder.equal(root.get("typeDayOff"), TypeDayOff.FULL)
            );

            Predicate[] p = new Predicate[predicates.size()];

            return criteriaBuilder.and(predicates.toArray(p));
        };

        var conditionRequest = requestRepository.findAll(specification);

        switch (form.getTimesheetType()){
            case NORMAL_WORKING -> {
                if(conditionRequest.size() > 2) {
                    throw new ProductException(
                            ErrorResponse
                                    .builder()
                                    .type(ErrorConstant.Type.CANNOT_LOG_ON_FULL_DAY_OFF)
                                    .code(ErrorConstant.Code.CANNOT_LOG_ON_FULL_DAY_OFF)
                                    .message(ErrorConstant.Message.CANNOT_LOG_ON_FULL_DAY_OFF)
                                    .build()
                    );
                }
                if(conditionRequest.size() == 1){
                    RequestEntity entity = conditionRequest.get(0);

                    switch (entity.getTypeDayOff()){
                        case FULL -> throw new ProductException(
                                ErrorResponse
                                .builder()
                                .type(ErrorConstant.Type.CANNOT_LOG_ON_FULL_DAY_OFF)
                                .code(ErrorConstant.Code.CANNOT_LOG_ON_FULL_DAY_OFF)
                                .message(ErrorConstant.Message.CANNOT_LOG_ON_FULL_DAY_OFF)
                                .build()
                        );
                        case MORNING -> {
                            if(totalWorkingHoursInDay + form.getWorkingHours() > 5){
                                throw new ProductException(
                                        ErrorResponse
                                                .builder()
                                                .type(ErrorConstant.Type.CANNOT_LOG_TIMESHEET)
                                                .code(ErrorConstant.Code.CANNOT_LOG_TIMESHEET)
                                                .message(String.format(ErrorConstant.Message.CANNOT_LOG_TIMESHEET,5))
                                                .build()
                                );
                            }
                        }
                        case AFTERNOON -> {
                            if(totalWorkingHoursInDay + form.getWorkingHours() > 3){
                                throw new ProductException(
                                        ErrorResponse
                                                .builder()
                                                .type(ErrorConstant.Type.CANNOT_LOG_TIMESHEET)
                                                .code(ErrorConstant.Code.CANNOT_LOG_TIMESHEET)
                                                .message(String.format(ErrorConstant.Message.CANNOT_LOG_TIMESHEET,3))
                                                .build()
                                );
                            }
                        }
                    }
                }

                if(totalWorkingHoursInDay + form.getWorkingHours() > 8){
                    throw new ProductException(
                            ErrorResponse
                                    .builder()
                                    .type(ErrorConstant.Type.CANNOT_LOG_TIMESHEET)
                                    .code(ErrorConstant.Code.CANNOT_LOG_TIMESHEET)
                                    .message(String.format(ErrorConstant.Message.CANNOT_LOG_TIMESHEET,8))
                                    .build()
                    );
                }

            }
            case OVERTIME , PROJECT_BONUS -> {

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

    public TimesheetDto updateInformationTimesheet(){
        return null;
    }

    public TimesheetDto approvalTimesheet(){
        var currentUser = (UserPrinciple) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return null;
    }
}
