package com.msc.ms.processor.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class VersionConfiguration {

    public VersionConfiguration(@Value("${msc.app.version}")String pVersion){
        log.info("Running Manage System College Processor Micro Service MSC-MS-PROCESSOR version: {}",pVersion);
    }
}
