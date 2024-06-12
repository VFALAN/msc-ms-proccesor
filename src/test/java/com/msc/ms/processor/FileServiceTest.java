package com.msc.ms.processor;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.msc.ms.processor.file.IFileService;
import com.msc.ms.processor.users.UserJsonDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class FileServiceTest {
    @Autowired
    private IFileService iFileService;

    public FileServiceTest() {

    }


    @Test
    public void testFileProcessing() {
        final var bucket = "process-files";
        final var listOfFiles = iFileService.getFilesNamesFromBucket(bucket);
        log.info("status code of the response {}", listOfFiles.getStatusCode());
        Assert.assertEquals(HttpStatus.OK, listOfFiles.getStatusCode());
        final var list = listOfFiles.getBody();
        Assert.assertFalse(list.isEmpty());
        log.info("the list size is {}", list.size());
    }

    @Test
    public void testGetAndProcessFile() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        final var bucket = "process-files";
        final var responseListOfFiles = iFileService.getFilesNamesFromBucket(bucket);
        final var listOfFiles = responseListOfFiles.getBody();
        final var file = listOfFiles.getFirst();
        final var fileResponse = iFileService.getFile(bucket, file);
        final var fileResource = fileResponse.getBody();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileResource.getInputStream()));
        final var jsonString = bufferedReader.lines().collect(Collectors.joining(System.lineSeparator()));
        Assert.assertNotNull(jsonString);
        final var pasing = objectMapper.readValue(fileResource.getInputStream(), new TypeReference<List<UserJsonDTO>>() {
        });
        log.info("size of final objects {}", pasing.size());
        Assert.assertNotNull(pasing);
    }
}
