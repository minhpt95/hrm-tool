package com.vatek.hrmtool.mapping.impl;

import com.vatek.hrmtool.dto.request.RequestDto;
import com.vatek.hrmtool.entity.RequestEntity;
import com.vatek.hrmtool.mapping.RequestMapping;
import com.vatek.hrmtool.mapping.UserMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RequestMappingImpl implements RequestMapping {
    @Autowired
    private UserMapping userMapping;

    @Override
    public RequestDto toDto(RequestEntity entity) {
        if ( entity == null ) {
            return null;
        }

        RequestDto requestDto = new RequestDto();

        requestDto.setId( entity.getId() );
        requestDto.setRequestTitle( entity.getRequestTitle() );
        requestDto.setRequestReason( entity.getRequestReason() );
        requestDto.setStatus( entity.getStatus() );
        requestDto.setTypeRequest( entity.getTypeRequest() );
        requestDto.setRequester( userMapping.toDto( entity.getRequester() ) );

        return requestDto;
    }
}
