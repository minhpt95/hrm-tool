package com.vatek.hrmtool.service.impl;

import com.vatek.hrmtool.dto.user.UserDto;
import lombok.extern.log4j.Log4j2;
import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Map;

@Service
@Log4j2
public class ExcelTemplateServiceImpl {
    public void createDocument(OutputStream outputStream,String templateName, Map<String, Object> data) {
        log.debug("Start creation of document");

        String pathTemplateName = ("templates/excels/").concat(templateName).concat(".xlsx");
        Resource resource = new ClassPathResource(pathTemplateName);

        try(InputStream input = new FileInputStream(resource.getFile())) {//1
            Context context = new Context();
            for (Map.Entry<String, Object> element : data.entrySet()) { // 2
                context.putVar(element.getKey(), element.getValue());
            }
            JxlsHelper.getInstance().processTemplate(input, outputStream, context); // 3
        } catch (Exception exception) {
            log.error("Fail to generate the document", exception);
        }
    }
}
