package com.vatek.hrmtool.service.impl;

import com.vatek.hrmtool.constant.ErrorConstant;
import com.vatek.hrmtool.dto.timesheet.TimesheetDto;
import com.vatek.hrmtool.entity.ProjectEntity;
import com.vatek.hrmtool.entity.RequestEntity;
import com.vatek.hrmtool.entity.TimesheetEntity;
import com.vatek.hrmtool.entity.UserEntity;
import com.vatek.hrmtool.entity.common.CommonEntity;
import com.vatek.hrmtool.enumeration.RequestStatus;
import com.vatek.hrmtool.enumeration.TypeDayOff;
import com.vatek.hrmtool.enumeration.TypeRequest;
import com.vatek.hrmtool.exception.ErrorResponse;
import com.vatek.hrmtool.exception.ProductException;
import com.vatek.hrmtool.readable.form.createForm.CreateTimesheetForm;
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
    private TimesheetRepository timesheetEntityRepository;

    private UserRepository userRepository;

    private ProjectRepository projectRepository;

    private RequestRepository requestRepository;

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
                workingDayInstantDayOfWeek == DayOfWeek.SATURDAY || workingDayInstantDayOfWeek == DayOfWeek.SUNDAY
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

            predicates.add(criteriaBuilder.notEqual(root.get("requestStatus"), RequestStatus.REJECTED));
            predicates.add(criteriaBuilder.equal(root.get("timesheetType"), form.getTimesheetType()));
            predicates.add(criteriaBuilder.equal(root.get("userEntity").get("id"),currentUser.getId()));
            predicates.add(criteriaBuilder.equal(root.get("workingDay"),workingDayInstant));

            Predicate[] p = new Predicate[predicates.size()];

            return criteriaBuilder.and(p);
        };

        List<TimesheetEntity> timesheetEntities = timesheetEntityRepository.findAll(timesheetEntitySpecification);

        Integer totalWorkingHoursInDay = timesheetEntities.stream().map(TimesheetEntity::getWorkingHours).reduce(0,Integer::sum);

        Specification<RequestEntity> specification = (root, query, criteriaBuilder) -> {
            var predicates = new ArrayList<Predicate>();

            predicates.add(criteriaBuilder.equal(root.get("typeRequest"), TypeRequest.DAY_OFF));
            predicates.add(criteriaBuilder.greaterThan(root.get("toDate"),workingDayInstant));
            predicates.add(criteriaBuilder.lessThan(root.get("fromDate"),workingDayInstant));
            predicates.add(criteriaBuilder.notEqual(root.get("status"),RequestStatus.REJECTED));
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
                if(totalWorkingHoursInDay == 8){
                    ErrorResponse
                            .builder()
                            .message(ErrorConstant.Message.CANNOT_LOG_MORE_TIMESHEET)
                            .code(ErrorConstant.Code.CANNOT_LOG_MORE_TIMESHEET)
                            .type(ErrorConstant.Type.CANNOT_LOG_MORE_TIMESHEET)
                            .build();
                }

                if(totalWorkingHoursInDay + form.getWorkingHours() > 8){
                    ErrorResponse
                            .builder()
                            .message(ErrorConstant.Message.CANNOT_LOG_THIS_TIMESHEET)
                            .code(ErrorConstant.Code.CANNOT_LOG_THIS_TIMESHEET)
                            .type(ErrorConstant.Type.CANNOT_LOG_THIS_TIMESHEET)
                            .build();
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
        timesheetEntity.setCreatedBy(currentUser.getId());
        timesheetEntity.setCreatedTime(DateUtil.getInstantNow());


        return null;
    }
}
