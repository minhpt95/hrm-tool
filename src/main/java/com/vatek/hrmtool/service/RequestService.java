package com.vatek.hrmtool.service;

import com.vatek.hrmtool.dto.request.RequestDto;
import com.vatek.hrmtool.readable.form.createForm.CreateRequestForm;

public interface RequestService {
    RequestDto createRequest(CreateRequestForm createRequestForm);
}
